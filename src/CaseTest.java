public interface CaseTest {

	void deckCol(final int col, final Card card) throws InvalidMoveException ;
	
	void deckBase(final int baseNum, final Card card) throws InvalidMoveException ;
	
	void colCol(final int old_col, final Card card, final int new_col)
			throws InvalidMoveException, InvalidCardException ;
	
	void colBase(final int col, final int baseNum) throws InvalidMoveException ;
	
	void getNext3() throws InvalidMoveException;
	
	void deckReset() throws InvalidMoveException;
}
