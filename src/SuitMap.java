
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class SuitMap {

	private BidiMap<Suit, String> suits;

	public SuitMap() {
		suits = new TreeBidiMap<Suit, String>();
		suits.put(Suit.SPADES, "S");
		suits.put(Suit.HEARTS, "H");
		suits.put(Suit.CLUBS, "C");
		suits.put(Suit.DIAMONDS, "D");
	}

	public String getSuitChar(Suit key) {
		return suits.get(key);
	}

	public Suit getSuit(String value) {
		return suits.getKey(value);
	}
}