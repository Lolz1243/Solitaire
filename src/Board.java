import java.util.*;

public class Board implements CaseTest {
	private final int TOTAL_COLUMNS = 7;
	private final int TOTAL_BASES = 4;
	private final int TOTAL_QUEUE = 3;

	// A cardDeck in the game board
	private class Column {

		public Stack<Card> cardStack; // Face down
		public ArrayDeque<Card> cardDeck; // Face up
		public String topColor = null;

		public Column() {
			cardDeck = new ArrayDeque<Card>();
			cardStack = new Stack<Card>();
		}

	}

	private ArrayList<Column> gameBoard;
	private ArrayList<Stack<Card>> bases;
	protected Deck deck; // Protected for debug purposes.
	protected ArrayDeque<Card> cardQueue; // addLast and removeFirst.

	// REQUIRES: col is [0, TOTAL_COLUMNS - 1]
	// EFFECTS: Checks whether can place card in col
	private boolean testColMove(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col >= 0 && col < TOTAL_COLUMNS);

		Column cardDeck = gameBoard.get(col);
		// Column if empty and card is King
		if (cardDeck.cardDeck.isEmpty() && card.getRank() == Rank.KING) {
			return (card.getRank() == Rank.KING);
		}
		// If topColor is "Red"
		boolean correctColor = card.getColor() == Card.RED;
		if (cardDeck.topColor == Card.RED) {
			correctColor = !correctColor;
		}
		// True if card is correct color and rank
		boolean correctRank = card.getRankNum() == peekCard(col).getRankNum() - 1;
		return correctColor && correctRank;
	}

	// REQUIRES: numOfCardStack is [0, TOTAL_BASES - 1]
	// EFFECTS: Checks whether can place card in bases
	private boolean testBaseMove(final int numOfCardStack, final Card card) {
		Stack<Card> cardStack1 = bases.get(numOfCardStack);
		if (cardStack1.isEmpty()) {
			// True if card is an Ace
			return card.getRank() == Rank.ACE;
		} else {
			// True if card is same suit and one rank higher
			boolean correct_suit = card.getSuit() == peekBaseCard(numOfCardStack).getSuit();
			boolean correctRank = card.getRankNum() == cardStack1.peek().getRankNum() + 1;
			return correct_suit && correctRank;
		}
	}

	// REQUIRES col is [0, TOTAL_COLUMNS - 1]
	// MODIFIES gameBoard
	// EFFECTS pushes card to end of cardDeck deque. Updates color.
	private void pushCard(final int col, final Card card) {
		Column col1 = gameBoard.get(col);
		col1.cardDeck.addLast(card);
		col1.topColor = card.getColor();
	}

	// REQUIRES col is [0, TOTAL_COLUMNS - 1]
	// col is not empty
	// MODIFIES gameBoard
	// EFFECTS pushes sequence of cards to end of cardDeck deque. Updates color.
	private void pushCards(final int col, final ArrayList<Card> sequence) {
		Column col1 = gameBoard.get(col);
		col1.cardDeck.addAll(sequence);
		col1.topColor = col1.cardDeck.getLast().getColor();
	}

	// REQUIRES col is [0, TOTAL_COLUMNS - 1]
	// col is not empty
	// MODIFIES gameBoard
	// EFFECTS pops card from end of cardDeck deque. Updates color.
	private void popColCard(final int col) {
		Column col1 = gameBoard.get(col);
		col1.cardDeck.removeLast();
		if (!col1.cardDeck.isEmpty())
			col1.topColor = col1.cardDeck.getLast().getColor();
		else
			col1.topColor = null;
	}

	// REQUIRES col is [0, TOTAL_COLUMNS - 1]
	// col has at least pop_num elements
	// MODIFIES gameBoard
	// EFFECTS pops pop_num cards from end of cardDeck deque. Updates color.
	private void popColCards(final int col, final int pop_num) {
		assert (gameBoard.get(col).cardDeck.size() >= pop_num);
		for (int i = 0; i < pop_num; i++) {
			popColCard(col);
		}
	}

	// REQUIRES baseNum is [0, TOTAL_BASES - 1]
	// MODIFIES bases
	// EFFECTS Adds card to selected foundation
	private void pushBaseCard(final int baseNum, final Card card) {
		Stack<Card> base1 = bases.get(baseNum);
		base1.push(card);
	}

	// REQUIRES numOfCardStack is [0, TOTAL_BASES - 1]
	// MODIFIES gameBoard
	// EFFECTS Adds card to selected cardStack. Use for dealing.
	private void pushCardToCardStack(final int numOfCardStack, final Card card) {
		Stack<Card> cardStack1 = gameBoard.get(numOfCardStack).cardStack;
		cardStack1.push(card);
	}

	// MODIFIES gameBoard, deck
	// EFFECTS Deals cards from deck to gameBoard
	private void dealNewGame() {
		// Face up for current cardDeck, then face down for rest of cardDecks
		// Column 0 should have 1 card, cardDeck 6 should have 7 cards
		for (int i = 0; i < TOTAL_COLUMNS; ++i) {
			pushCard(i, deck.dealOne());
			for (int j = i + 1; j < TOTAL_COLUMNS; ++j) {
				pushCardToCardStack(j, deck.dealOne());
			}
		}
	}

	// MODIFIES: this
	// EFFECTS: Initializes board, shuffle=empty=false for debug
	public Board(final boolean shuffle, final boolean deal) {
		gameBoard = new ArrayList<Column>();
		bases = new ArrayList<Stack<Card>>();
		deck = new Deck(Deck.DECK_NAME, shuffle);
		cardQueue = new ArrayDeque<Card>();

		// Add empty Columns
		for (int i = 0; i < TOTAL_COLUMNS; ++i) {
			gameBoard.add(new Column());
		}
		// Add empty cardStacks
		for (int j = 0; j < TOTAL_BASES; ++j) {
			bases.add(new Stack<Card>());
		}
		if (deal)
			dealNewGame();
	}

	// MODIFIES: this
	// EFFECTS: Initialize generic board
	public Board() {
		this(true, true);
	}

	// REQUIRES: col is [0, TOTAL_COLUMNS - 1].
	// EFFECTS: Returns bottom card in selected cardDeck
	// Returns null if empty
	public Card peekCard(final int col) {
		if (gameBoard.get(col).cardDeck.isEmpty())
			return null;
		return gameBoard.get(col).cardDeck.getLast();
	}

	// REQUIRES: foundation is [0, TOTAL_BASES - 1].
	// EFFECTS: Returns card on top of selected foundation
	// Returns null if empty
	public Card peekBaseCard(final int baseNum) {
		if (bases.get(baseNum).isEmpty())
			return null;
		return bases.get(baseNum).peek();
	}

	// EFFECTS: Returns card at front of cardQueue
	// Returns null if empty
	public Card peekQueueCard() {
		if (cardQueue.isEmpty())
			return null;
		return cardQueue.getLast();
	}

	// REQUIRES: col is [0, TOTAL_COLUMNS - 1], queue is not empty
	// MODIFIES: cardDecks, cardQueue
	// EFFECTS: Places card in cardDeck if valid
	// Throws exception if invalid
	public void deckCol(final int col, final Card card) throws InvalidMoveException {
		// Assert REQUIRES statement
		if (col < 0 || col >= TOTAL_COLUMNS) {
			throw new InvalidMoveException(String.format("Invalid cardDeck index %d", col));
		} else if (cardQueue.isEmpty()) {
			throw new InvalidMoveException("No cards in queue");
		}
		if (!testColMove(col, card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		pushCard(col, card); // Add card to cardDeck
		cardQueue.removeLast(); // Remove card from queue

		if (cardQueue.isEmpty()) {
			try {
				getLast3();
			} catch (InvalidMoveException e) {
				System.err.println("InvalidMoveException: " + e.getMessage());
			}
		}
	}

	// REQUIRES: baseNum is [0, TOTAL_BASES - 1], queue is not empty
	// MODIFIES: bases, cardQueue
	// EFFECTS: Places card on bases if valid
	// Throws exception if invalid
	public void deckBase(final int baseNum, final Card card) throws InvalidMoveException {
		// Assert REQUIRES statement
		if (baseNum < 0 || baseNum >= TOTAL_BASES) {
			throw new InvalidMoveException(String.format("Invalid foundation index %d", baseNum));
		} else if (cardQueue.isEmpty()) {
			throw new InvalidMoveException("No cards in queue");
		}
		if (!testBaseMove(baseNum, card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		pushBaseCard(baseNum, card); // Add card to bases
		cardQueue.removeLast(); // Remove card from queue

		if (cardQueue.isEmpty()) {
			try {
				getLast3();
			} catch (InvalidMoveException e) {
				System.err.println("InvalidMoveException: " + e.getMessage());
			}
		}
	}

	// REQUIRES: col is [0, TOTAL_COLUMNS - 1]
	// flip_cardStack[col] is not empty
	// col is empty
	// MODIFIES: gameBoard
	// EFFECTS: flips card from top of cardStack when col is empty
	private void flipCol(final int col) throws InvalidMoveException {
		Column col1 = gameBoard.get(col);
		assert (col1.cardDeck.isEmpty());
		assert (col >= 0 && col < TOTAL_COLUMNS);

		if (col1.cardStack.isEmpty()) {
			throw new InvalidMoveException("No cards to flip");
		}

		Card topCard = col1.cardStack.peek();
		col1.cardStack.pop();
		pushCard(col, topCard);
	}

	// REQUIRES: col is [0, TOTAL_COLUMNS - 1]
	// EFFECTS: returns all cards in col starting from specified card
	private ArrayList<Card> getCardSequence(final int col, final Card card) throws InvalidCardException {
		// No such card in cardDeck
		if (!gameBoard.get(col).cardDeck.contains(card)) {
			throw new InvalidCardException(String.format("Column %d does not contain Card %s", col, card));
		}

		ArrayList<Card> sequence = new ArrayList<Card>();
		for (Iterator<Card> itr = gameBoard.get(col).cardDeck.iterator(); itr.hasNext();) {
			if (itr.next().equals(card)) {
				sequence.add(card);
				while (itr.hasNext()) {
					sequence.add(itr.next());
				}
				return sequence;
			}
		}
		assert (false);
		return null;
	}

	// REQUIRES: oldCol and newCol are [0, TOTAL_COLUMNS - 1].
	// oldCol is not empty
	// MODIFIES: cardDecks
	// EFFECTS: Moves cards from one cardDeck to the other if valid
	// Throws exception if invalid move
	public void colCol(final int oldCol, final Card card, final int newCol)
			throws InvalidMoveException, InvalidCardException {
		// Assert REQUIRES statement
		if (oldCol < 0 || oldCol >= TOTAL_COLUMNS) {
			throw new InvalidMoveException(String.format("Invalid first cardDeck index %d", oldCol));
		} else if (newCol < 0 || newCol >= TOTAL_COLUMNS) {
			throw new InvalidMoveException(String.format("Invalid second cardDeck index %d", newCol));
		} else if (gameBoard.get(oldCol).cardDeck.isEmpty()) {
			throw new InvalidMoveException("No cards to move");
		} else if (oldCol == newCol) {
			return; // Move to its own cardDeck
		}

		// Get sequence of cards from oldCol
		ArrayList<Card> sequence = getCardSequence(oldCol, card);

		// Make sure last card in sequence is the specified card
		assert (sequence.get(0).equals(card));

		// Attempt to add to newCol
		if (!testColMove(newCol, card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		popColCards(oldCol, sequence.size());
		pushCards(newCol, sequence);

		// If cardDeck is now empty, flip over next card
		if (gameBoard.get(oldCol).cardDeck.isEmpty()) {
			try {
				flipCol(oldCol);
			} catch (InvalidMoveException e) {
			}
		}
	}

	// REQUIRES: col is [0, TOTAL_COLUMNS - 1]
	// baseNum is [0, TOTAL_BASES - 1]
	// col is not empty
	// MODIFIES: cardDecks, bases
	// EFFECTS: Moves card from cardDeck to bases if valid
	// Throws exception if invalid move
	public void colBase(final int col, final int baseNum) throws InvalidMoveException {
		if (col < 0 || col >= TOTAL_COLUMNS) {
			throw new InvalidMoveException("Invalid cardDeck index");
		} else if (baseNum < 0 || baseNum >= TOTAL_BASES) {
			throw new InvalidMoveException("Invalid bases index");
		} else if (gameBoard.get(col).cardDeck.isEmpty()) {
			throw new InvalidMoveException("No cards to move");
		}

		Card topCard = peekCard(col);
		if (!testBaseMove(baseNum, topCard)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		popColCard(col);
		pushBaseCard(baseNum, topCard);

		// If cardDeck is now empty, flip over next card
		if (gameBoard.get(col).cardDeck.isEmpty()) {
			try {
				flipCol(col);
			} catch (InvalidMoveException e) {
			}
		}
	}

	// MODIFIES: deck, cardQueue
	// EFFECTS: Adds next three cards from deck to cardQueue
	// Throws exception if deck is empty
	public void getNext3() throws InvalidMoveException {
		if (deck.cardsIsEmpty()) {
			throw new InvalidMoveException("Deck is empty");
		}
		deck.addToDealt(cardQueue);
		cardQueue.clear();
		while (!deck.cardsIsEmpty() && cardQueue.size() != TOTAL_QUEUE) {
			cardQueue.addFirst(deck.dealOne());
		}
	}

	// MODIFIES: cardQueue, dealt
	// EFFECTS: Adds previous 3 queue cards back into cardQueue
	// Throws exception if dealt is empty or queue is not empty
	public void getLast3() throws InvalidMoveException {
		if (deck.dealtIsEmpty()) {
			throw new InvalidMoveException("Dealt cardStack is empty");
		} else if (!cardQueue.isEmpty()) {
			throw new InvalidMoveException("Queue is not empty");
		}
		cardQueue.addFirst(deck.retrieveOne());
	}

	// MODIFIES: deck, cardQueue
	// EFFECTS: Resets deck when player reaches bottom of deck
	// Throws exception if invalid move
	public void deckReset() throws InvalidMoveException {
		if (!deck.cardsIsEmpty()) {
			throw new InvalidMoveException("Deck is not empty");
		}
		deck.addToDealt(cardQueue);
		cardQueue.clear();
		deck.reset();
	}

	// EFFECTS: Prints out contents of cardQueue
	public void printQueue() {
		for (Iterator<Card> itr = cardQueue.iterator(); itr.hasNext();) {
			System.out.print(itr.next() + " ");
		}
		System.out.println("-->"); // Indicate queue direction
	}

	// EFFECTS: Prints out cards in cardStacks
	public void printCardStack() {
		for (int i = 0; i < TOTAL_COLUMNS; ++i) {
			System.out.print(" " + gameBoard.get(i).cardStack.size() + "| ");
		}
		System.out.println();
	}

	// EFFECTS: Prints out contents of cardDecks
	public void printCardDeck() {
		// Find len of longest cardDeck
		int max = 0;
		for (int i = 0; i < TOTAL_COLUMNS; ++i) {
			int len = gameBoard.get(i).cardDeck.size();
			if (len > max) {
				max = len;
			}
		}

		// create ArrayList of Iterators to reference in double for loop
		ArrayList<Iterator<Card>> itr = new ArrayList<Iterator<Card>>();
		for (int j = 0; j < TOTAL_COLUMNS; ++j) {
			itr.add(gameBoard.get(j).cardDeck.iterator());
		}

		// Print cards in cardDecks, skipping if null
		for (int k = 0; k < max; ++k) { // Iterate vertically
			for (int l = 0; l < TOTAL_COLUMNS; ++l) { // Iterate horizontally
				if (itr.get(l).hasNext()) {
					Card current = itr.get(l).next();
					System.out.print(current);
				} else {
					// Empty spot
					System.out.print("    ");
					continue;
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	// EFFECTS: Prints out top of each foundation
	public void printBases() {
		for (int i = 0; i < TOTAL_BASES; ++i) {
			Card current = peekBaseCard(i);
			if (current == null)
				// Empty cardStack
				System.out.print(" -- ");
			else
				System.out.print(current);
		}
		System.out.println();
	}

	// EFFECTS: Prints out number of cards left in deck
	public void printDeckSize() {
		int size = deck.remaining();
		System.out.println(size);
	}

	// EFFECTS: Prints board layout
	public void printBoard() {
		System.out.println("~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-");
		System.out.print("Deck Size: ");
		printDeckSize();
		System.out.print("Card Queue: ");
		printQueue();
		System.out.print("Bases: ");
		printBases();
		System.out.println("Game Board: ");
		System.out.println(" #0  #1  #2  #3  #4  #5  #6 ");
		printCardStack();
		printCardDeck();
	}

	public boolean emptycardStack(int numOfCardStack) {
		assert (numOfCardStack >= 0 && numOfCardStack < TOTAL_COLUMNS);
		return gameBoard.get(numOfCardStack).cardStack.empty();
	}

}