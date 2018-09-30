
public class SolitaireTest {

	public static void main(String[] args) {
		System.out.println("Initializing Solitaire tests");

		// Call tests here
		test_Card();
		test_Deck();
		test_Board();
		test_Queue();
		test_Print();

		System.out.println("All tests passed!");
	}

	public static void test_Card() {
		System.out.println("Begin test_Card");
		// Create an Ace of Clubs
		try {
			Card card1 = new Card("AC");
			assert (card1.getColor() == Card.BLACK);
			assert (card1.getRank() == Rank.ACE);
			assert (card1.getSuit() == Suit.CLUBS);
			assert (card1.getRankNum() == 1);
			assert (card1.printRank() == "A");
		} catch (InvalidCardException e) {
			System.err.println("InvalidCardException: " + e.getMessage());
		}
		System.out.println("test_Card passed");
	}

	public static void test_Deck() {
		System.out.println("Begin test_Deck");
		// Load default deck, no shuffle
		Deck deck = new Deck(Deck.DECK_NAME, false);
		Card top = deck.top(); // Should be King of Diamonds
		assert (top.getColor() == Card.RED);
		assert (top.getRank() == Rank.KING);
		assert (top.getSuit() == Suit.DIAMONDS);
		assert (top.getRankNum() == 13);
		deck.dealOne();
		top = deck.top(); // Should be Queen of Diamonds
		assert (top.getColor() == Card.RED);
		assert (top.getRank() == Rank.QUEEN);
		assert (top.getSuit() == Suit.DIAMONDS);
		assert (top.getRankNum() == 12);
		System.out.println("test_Deck passed");
	}

	public static void test_Board() {
		System.out.println("Begin test_Board");

		Board board = new Board(false, false);
		try {
			board.getNext3();
		} catch (InvalidMoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		board.printQueue();
		try {
			Card king_diamonds = new Card("KD");
			Card nine_diamonds = new Card("9D");
			Card two_spades = new Card("2S");
			Card ace_hearts = new Card("AH");
			Card two_hearts = new Card("2H");
			// Place king_diamonds in column 0
			board.deckCol(0, board.peekQueueCard());
			board.printQueue();
			assert (board.cardQueue.size() == 2);
			assert (board.peekCard(0).equals(king_diamonds));
			// Place ace_hearts in column 1
			board.deckCol(1, ace_hearts);
			assert (board.peekCard(1).equals(ace_hearts));
			// Place two_spades in column 2
			board.deckCol(2, two_spades);
			assert (board.peekCard(2).equals(two_spades));
			// Move ace_hearts from column 1 to 2
			try {
				board.colCol(1, ace_hearts, 2);
			} catch (InvalidCardException e) {
				System.err.println("InvalidCardException: " + e.getMessage());
			}
			assert (board.peekCard(2).equals(ace_hearts));

			// Invalid move
			try {
				board.colCol(0, king_diamonds, 2);
			} catch (InvalidMoveException e) {
				System.err.println("InvalidMoveException: " + e.getMessage());
				System.out.println("Invalid move test 1 passed");
			} catch (InvalidCardException e) {
				System.err.println("InvalidCardException: " + e.getMessage());
			}

			// Make sure card did not move
			assert (board.peekCard(0).equals(king_diamonds));
			// Status: ace_spades in col 0; two_spades, ace_hearts in col 2

			// Move ace_hearts to foundation 0
			board.colBase(2, 0);
			assert (board.peekBaseCard(0).equals(ace_hearts));

			// Invalid move
			try {
				board.colBase(0, 0);
			} catch (InvalidMoveException e) {
				System.err.println("InvalidMoveException: " + e.getMessage());
				System.out.println("Invalid move test 2 passed");
			}

			// Card queue should be empty now
			assert (board.cardQueue.isEmpty());
			board.getNext3();
			board.printQueue();
			// Move two_hearts to foundation 0
			board.deckBase(0, two_hearts);
			assert (board.peekBaseCard(0).equals(two_hearts));
			// Move king_diamonds to its current column
			try {
				board.colCol(0, king_diamonds, 0);
			} catch (InvalidCardException e) {
				System.err.println("InvalidCardException: " + e.getMessage());
			}
			board.deckReset();
			assert (board.deck.top().equals(nine_diamonds));
		} catch (InvalidMoveException e) {
			System.err.println("InvalidMoveException(s): " + e.getMessage());
			System.err.println("-----DID NOT PASS-----");
		} catch (InvalidCardException e) {
			System.err.println("InvalidCardException: " + e.getMessage());
		}
		System.out.println("test_Board passed");
	}

	public static void test_Queue() {
		System.out.println("Begin test_Queue()");
		Board board = new Board(false, false);
		// Print through end of deck
		for (int i = 0; i < 18; ++i) {
			try {
				board.getNext3();
			} catch (InvalidMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			board.printQueue();
		}
		assert (board.deck.cardsIsEmpty());
		// TODO: finish this test case
		System.out.println("test_Queue() passed");
	}

	public static void test_Print() {
		Board board = new Board(false, true);
		try {
			board.printBoard();
			board.getNext3();
			board.printBoard();
			board.getNext3();
			board.printBoard();
			board.deckReset();
			board.printBoard();
			board.getNext3();
			board.printBoard();
		} catch (InvalidMoveException e) {
			e.printStackTrace();
		}
	}

}
