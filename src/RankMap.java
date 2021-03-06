
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class RankMap {
	private BidiMap<Rank, Integer> ranks;

	public RankMap() {
		ranks = new TreeBidiMap<Rank, Integer>();
		ranks.put(Rank.ACE, 1);
		ranks.put(Rank.TWO, 2);
		ranks.put(Rank.THREE, 3);
		ranks.put(Rank.FOUR, 4);
		ranks.put(Rank.FIVE, 5);
		ranks.put(Rank.SIX, 6);
		ranks.put(Rank.SEVEN, 7);
		ranks.put(Rank.EIGHT, 8);
		ranks.put(Rank.NINE, 9);
		ranks.put(Rank.TEN, 10);
		ranks.put(Rank.JACK, 11);
		ranks.put(Rank.QUEEN, 12);
		ranks.put(Rank.KING, 13);
	}

	public int getRankNum(Rank key) {
		return ranks.get(key);
	}

	private int parseValue(String value) {
		switch (value) {
		case "A":
			return 1;
		case "10":
			return 10;
		case "J":
			return 11;
		case "Q":
			return 12;
		case "K":
			return 13;
		default:
			return Integer.parseInt(value);
		}
	}

	public Rank getRank(String value) {
		return ranks.getKey(parseValue(value));
	}
}