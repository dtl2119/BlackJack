import java.util.ArrayList;
import java.util.Scanner;

public class Game {
	PlayerHand person;
	PlayerHand dealer;
	Deck gameDeck;
	Scanner input;
	int bet;
	int round;
	String reply;

	public Game() {

		// Create two players for the game
		person = new PlayerHand(100); // Give player 100 chips
		dealer = new PlayerHand();
		gameDeck = new Deck(5); // Game uses 5 decks
		input = new Scanner(System.in);
		bet = 1;
		round = 0;

	}

	public void play() {

		// Shuffle 5-Deck every 10 rounds
		if (round % 10 == 0) {
			// Reset deck and hands
			reset();
			round = 0;
		}

		round++;

		person.hand.clear();
		dealer.hand.clear();
		ArrayList<Integer> results = new ArrayList<Integer>();
		int dealerResult;

		person.dealHand(gameDeck);
		dealer.dealHand(gameDeck);

		if (person.chips <= 0) {
			System.out.println("Sorry, you don't have chips.  You must leave");
			System.exit(-1);
		}

		// Ask to bet, while loop to ensure bet is valid
		System.out.println("Your chip count is " + person.chips);
		while (true) {
			System.out.println("How many chips would you like to bet?");
			if (input.hasNextInt()) {
				bet = input.nextInt();

				if (bet < 1) {
					System.out.println("Please bet more than 1 chip");
				} else if (bet <= person.chips) {
					break;
				} else {
					System.out.println("You do not have enough chips");
				}

			} else {
				System.out.println("Please enter a valid numeric response");
				input.next(); // To clear the invalid input
			}

		}// end while

		System.out.println("Your hand:");
		person.sayHand();

		// Push if both BlackJack
		if (dealer.check() == 21 && person.check() == 21) {
			System.out.println();
			dealer.sayHand();
			System.out.println("Push:  Both got BlackJack!");
			return;
		} else if (dealer.check() == 21) {
			System.out.println();
			dealer.sayHand();
			System.out.println("BlackJack!");
			System.out.println("Dealer Wins! (Lost " + bet + " credit)");
			person.changeChips(-1 * bet);
			return;
		}

		System.out.println();
		dealer.dealerSayHand();

		if (person.check() == 21) {
			System.out.println("BlackJack!");
			System.out.println("Player Wins! (Won " + 3 * bet / 2 + " credit)");
			person.changeChips(3 * bet / 2);
			return;
		}

		System.out.println("Player's turn ");

		if (canSplit(person.hand)) {
			while (true) {
				System.out
						.println("Would you like to (1) hit, (2) stand, or (3) split?");
				reply = input.next();
				if (reply.equals("1") || reply.equals("2") || reply.equals("3"))
					break;

				System.out
						.print("Invalid Response.  Please enter 1, 2, or 3\n");
			}
		} else {

			while (true) {
				System.out.println("Would you like to (1) hit or (2) stand?");
				reply = input.next();
				if (reply.equals("1") || reply.equals("2"))
					break;

				System.out
						.print("Invalid Response.  Please enter either 1 or 2.\n");
			}
		}

		if (reply.equals("1") || reply.equals("2")) {
			// Player decides to hit
			if (reply.equals("1")) {
				// Hit until stand, and then add total into results

				results.add(playerHits(person));
			} else {
				results.add(person.check());
			}

			// Only simulate dealer if player didn't bust
			if (results.get(0) <= 21) {
				dealerResult = simulateDealer();
			} else {
				dealerResult = dealer.check();
			}

			// Compare Results and deal with bet
			System.out.println("\nResults:");
			System.out.println("Dealer (" + dealerResult + ")\nPlayer ("
					+ results.get(0) + ")");
			giveResults(results.get(0), dealerResult);

		}// end 1 or 2

		if (reply.equals("3")) {

			// Divide into two seperate hands
			PlayerHand person1 = new PlayerHand();
			person1.hand.add(person.hand.get(1));
			person.hand.remove(1);

			System.out.println("Here's your first hand:");
			person.sayHand();
			results.add(playerHits(person));

			System.out.println("\nHere's your second hand:");
			person1.sayHand();
			results.add(playerHits(person1));

			// Only simulate dealer if at least one hand didn't bust
			if (results.get(0) <= 21 || results.get(1) <= 21) {
				dealerResult = simulateDealer();
			} else {
				dealerResult = dealer.check();
			}

			// First hand results
			System.out.println("\nResults for first hand:");
			System.out.println("Dealer (" + dealerResult + ")\nPlayer ("
					+ results.get(0) + ")");
			giveResults(results.get(0), dealerResult);
			System.out.println();

			// Second hand results
			System.out.println("Results for second hand:");
			System.out.println("Dealer (" + dealerResult + ")\nPlayer ("
					+ results.get(1) + ")");
			giveResults(results.get(1), dealerResult);
		}

	}// End play()

	// Shuffle 5-Deck
	private void reset() {
		// Create and setup deck
		gameDeck.createDeck();
		gameDeck.shuffle();

	}

	// Method to print results and reward/collect bets
	private void giveResults(int pScore, int dScore) {

		if (pScore > 21) {
			System.out.println("Player Bust! (Lost " + bet + " credit)");
			person.changeChips(-1 * bet);
		} else if (dScore > 21 && pScore <= 21) {
			System.out.println("Dealer Bust! (Won " + bet + " credit)");
			person.changeChips(bet);
		} else if (pScore == dScore) {
			System.out.println("Push!");
		} else if (pScore > dScore) {
			System.out.println("Player Wins! (Won " + bet + " credit)");
			person.changeChips(bet);
		} else {
			System.out.println("Dealer Wins! (Lost " + bet + " credit)");
			person.changeChips(-1 * bet);
		}

	}// End giveResults

	// Returns the total sum of cards
	private int playerHits(PlayerHand p) {
		int total;
		while (true) {
			p.hit(gameDeck);
			total = p.check();
			if (total > 21) {
				return total;
			} else if (total == 21) {
				System.out.println("You got 21!");
				return total;
			} else {

				// Keep prompting until player gives valid response
				while (true) {
					System.out
							.println("\nWould you like to (1) hit or (2) stand?");
					reply = input.next();
					if (reply.equals("1"))
						break;
					if (reply.equals("2"))
						return total;

					System.out.print("Invalid Response.  Please enter 1 or 2");
				}

			}
		}

	}

	// Returns true if player is allowed to split (same card)
	private boolean canSplit(ArrayList<Card> h) {
		if (h.get(0).value == h.get(1).value)
			return true;
		else
			return false;
	}

	// Returns dealer's score after simulation
	private int simulateDealer() {

		System.out.println("Dealer's turn.\n");
		System.out.println("Dealer reveals hand");
		dealer.sayHand();

		// Simulate Dealer: must hit < 17
		while (dealer.check() < 17) {

			dealer.hit(gameDeck);
			if (dealer.check() > 21) {
				// System.out.println("Dealer busts!");
				return dealer.check();
			} else if (dealer.check() == 21) {
				// System.out.println("Dealer got 21!");
				return dealer.check();
			}
		}

		return dealer.check();

	}

}// End Game Class
