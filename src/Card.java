public class Card {
	public static final String RED = "Red";
	public static final String BLACK = "Black";

	public static final SuitMap suits = new SuitMap();
	public static final RankMap ranks = new RankMap();

	private Rank rank;
	private Suit suit;
	private String color; // Consider removing this

	// EFFECTS: Returns string of card color
	private String suit_color(Suit suit) {
		if (suit == Suit.SPADES || suit == Suit.CLUBS) {
			return "Black";
		} else {
			return "Red";
		}
	}

	public Card(String cardString) throws InvalidCardException {
		String rank = null;
		String suit = null;
		// Use first letters of cardString
		rank = cardString.substring(0, cardString.length() - 1);
		// Use last letter in cardString
		suit = String.valueOf(cardString.charAt(cardString.length() - 1));

		// Set card values
		this.rank = ranks.getRank(rank);
		this.suit = suits.getSuit(suit);
		this.color = suit_color(this.suit);

		// Invalid rank or suit
		if (this.rank == null || this.suit == null) {
			throw new InvalidCardException(String.format("Invalid card %s", cardString));
		}
	}

	public Rank getRank() {
		return this.rank;
	}

	public int getRankNum() {
		return ranks.getRankNum(this.rank);
	}

	// Converts rank to print format
	public String printRank() {
		int symbol = this.getRankNum();
		if (symbol == 1) {
			return "A";
		} else if (symbol == 10) {
			return "10";
		} else if (symbol == 11) {
			return "J";
		} else if (symbol == 12) {
			return "Q";
		} else if (symbol == 13) {
			return "K";
		}
		return Integer.toString(symbol);
	}

	public Suit getSuit() {
		return this.suit;
	}

	public String getColor() {
		return this.color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

	// For printing purposes
	public String toString() {
		if (this.printRank() == "10") {
			return this.printRank() + suits.getSuitChar(this.suit) + " ";
		}
		return " " + this.printRank() + suits.getSuitChar(this.suit) + " ";
	}

}
