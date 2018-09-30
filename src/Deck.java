import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Deck {

	public static final String DECK_NAME = "deck";
	private final int DECK_SIZE = 52;
	// Top of deck is cards.size() - 1
	private List<Card> cards = new ArrayList<Card>(DECK_SIZE);
	// Holds cards that have been dealt but not used
	protected List<Card> dealt = new ArrayList<Card>(DECK_SIZE);

	// MODIFIES this
	// EFFECTS populates cards and order
	private void initializeDeck(final String fileName) {
		try {
			Scanner in = new Scanner(new File(fileName));
			// Read in cards from deck file
			for (int i = 0; i < DECK_SIZE; i++) {
				try {
					cards.add(new Card(in.next()));
				} catch (InvalidCardException e) {
					System.err.println("Error: " + e.getMessage());
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.printf("Unable to open file %s.txt\n", fileName);
			System.exit(1);
		} catch (NoSuchElementException ex) {
			System.out.printf("Not enough cards in %s.txt\n", fileName);
			System.exit(1);
		}
	}

	// MODIFIES this
	// EFFECTS Load specific deck and shuffle
	public Deck(final String fileName) {
		this(fileName, true);
	}

	// MODIFIES this
	// EFFECTS Load specific deck, shuffle=false for debugging
	public Deck(final String fileName, boolean shuffle) {
		initializeDeck(fileName);
		// No shuffle option for debugging purposes
		if (shuffle) {
			shuffle();
		}
	}

	// MODIFIES order
	// EFFECTS shuffles deck by shuffling order
	public void shuffle() {
		Collections.shuffle(cards);
		// : Shuffle with random seed each time, then we can save the seed
		// Collections.shuffle(cards, new Random(10));
	}

	// REQUIRES deck is not empty
	// EFFECTS Returns top card
	public Card top() {
		return cards.get(cards.size() - 1);
	}

	public void addToDealt(ArrayDeque<Card> cardQueue) {
		cardQueue.addAll(dealt);
		dealt.clear();
		dealt.addAll(cardQueue);
	}

	// REQUIRES deck is not empty
	// EFFECTS Deals top card
	public Card dealOne() {
		if (cards.isEmpty()) {
			// Make this and retrieve_one an exception instead?
			System.out.println("dealOne(): Deck is empty");
		}
		Card topCard = top();
		cards.remove(cards.size() - 1);
		return topCard;
	}

	public Card retrieveOne() {
		if (dealt.isEmpty()) {
			System.out.println("retrieveOne(); Dealt is empty");
		}
		Card last = dealt.remove(0);
		return last;
	}

	// EFFECTS Returns True if deck is empty
	public boolean cardsIsEmpty() {
		return cards.isEmpty();
	}

	// EFFECTS Returns True if dealt is empty
	public boolean dealtIsEmpty() {
		return dealt.isEmpty();
	}

	// MODIFIES this
	// EFFECTS Resets deck. Maintains order and used
	public void reset() {
		cards.addAll(dealt);
		dealt.clear();
	}

	// EFFECTS Returns number of cards remaining in deck
	public int remaining() {
		return cards.size();
	}

}