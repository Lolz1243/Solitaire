import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Board board = new Board(false, true); // No shuffle for debugging
		board.printBoard();
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Choose an action:");
			System.out.println("[1 - Next 3 cards]\n[2 - Deck to Col]");
			System.out.println("[3 - Deck to Foundation]\n[4 - Col to Col]");
			System.out.println("[5 - Col to Foundation]\n[6 - Reset Deck]");
			System.out.println("[7 - Quit Game]");
			boolean valid = false;
			while (!valid) {
				int choice = in.nextInt();
				switch (choice) {
				// Next 3 cards
				case 1: {
					try {
						board.getNext3();
						valid = true;
					} catch (InvalidMoveException e) {
						System.err.println("Error: " + e.getMessage());
					}
					break;
				}
				// Deck to Col
				case 2: {
					if (board.cardQueue.isEmpty()) {
						System.err.println("Error: No cards in queue");
						break;
					}
					Card topCard = board.peekQueueCard();
					boolean validMove = false;
					while (!validMove) {
						try {
							System.out.print("Move to which column (-1 to go back)? ");
							int col = in.nextInt();
							if (col == -1) {
								valid = true;
								validMove = true;
								break;
							}
							board.deckCol(col, topCard);
							validMove = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid = true;
					break;
				}
				// Deck to Foundation
				case 3: {
					if (board.cardQueue.isEmpty()) {
						System.err.println("Error: No cards in queue");
						break;
					}
					Card topCard = board.peekQueueCard();
					boolean validMove = false;
					while (!validMove) {
						try {
							System.out.print("Move to which foundation (-1 to go back)? ");
							int foundation = in.nextInt();
							if (foundation == -1) {
								valid = true;
								validMove = true;
								break;
							}
							board.deckBase(foundation, topCard);
							validMove = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid = true;
					break;
				}
				// Col to Col
				case 4: {
					boolean validMove = false;
					while (!validMove) {
						try {
							System.out.print("From which column (-1 to go back)? ");
							int oldCol = in.nextInt();
							System.out.print("To which column? ");
							int newCol = in.nextInt();
							System.out.print("Move which card? ");
							String card_code = in.next();
							if (oldCol == -1 || newCol == -1 || card_code == "-1") {
								valid = true;
								break;
							}
							board.colCol(oldCol, new Card(card_code), newCol);
							validMove = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						} catch (InvalidCardException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid = true;
					break;
				}
				// Col to Foundation
				case 5: {
					boolean validMove = false;
					while (!validMove) {
						try {
							System.out.print("From which column (-1 to go back) ");
							int oldCol = in.nextInt();
							System.out.print("To which foundation? ");
							int foundation_num = in.nextInt();
							if (oldCol == -1 || foundation_num == -1) {
								valid = true;
								validMove = true;
								break;
							}
							board.colBase(oldCol, foundation_num);
							validMove = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid = true;
					break;
				}
				// Reset Deck
				case 6: {
					try {
						board.deckReset();
						valid = true;
					} catch (InvalidMoveException e) {
						System.err.println("Error: " + e.getMessage());
					}
					break;
				}
				case 7: {
					System.out.print("Are you sure you want to quit (y/n)? ");
					String quit = in.next();
					if (quit.equals("n")) {
						valid = true;
						break;
					} else {
						System.out.println("Thanks for playing!");
						in.close();
						System.exit(0);
					}
				}
				default: {
					System.out.println("Error: invalid choice. Select a different choice.");
				}
				} // switch (choice)
				if (valid)
					board.printBoard();
			} // while (!valid)
		} // while (true)
	} // main
}
