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
	ArrayList<Integer> results;
	int dealerResult;

	public Game() {
		// Create two players for the game
		person = new PlayerHand(100); // Give player 100 chips
		dealer = new PlayerHand();
		gameDeck = new Deck(5); // Game uses 5 decks
		input = new Scanner(System.in);
		bet = 1;
		round = 0;
		results = new ArrayList<Integer>();
		dealerResult = 0;
	}

	public void play() {

		// Shuffle 5-Deck every 10 rounds
		if (round % 10 == 0) {
			// Create and setup deck
			gameDeck.createDeck();
			gameDeck.shuffle();
			round = 0;
		}
		round++;

		// Clear hands and totals
		reset();

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

		dealer.dealerSayHand();

		if (person.check() == 21) {
			System.out.println("BlackJack!");
			System.out.println("Player Wins! (Won " + 3 * bet / 2 + " credit)");
			person.changeChips(3 * bet / 2);
			return;
		}

		System.out.println("Player's turn ");

		if (canSplit(person)) {
			while (true) {
				System.out
						.println("Would you like to (1) hit, (2) stand, (3) surrender, (4) double down, or (5) split?");
				reply = input.next();
				if (reply.equals("1") || reply.equals("2") || reply.equals("3")
						|| reply.equals("4") || reply.equals("5"))
					break;

				System.out
						.print("Invalid Response.  Please enter 1, 2, 3, or 4\n");
			}
		} else if (canDouble(person)) {
			while (true) {
				System.out
						.println("Would you like to (1) hit, (2) stand, (3) surrender, or (4) double down");
				reply = input.next();
				if (reply.equals("1") || reply.equals("2") || reply.equals("3")
						|| reply.equals("4"))
					break;

				System.out
						.print("Invalid Response.  Please enter 1, 2, 3, or 4\n");
			}
		} else {
			while (true) {
				System.out
						.println("Would you like to (1) hit, (2) stand, or (3) surrender?");
				reply = input.next();
				if (reply.equals("1") || reply.equals("2") || reply.equals("3"))
					break;

				System.out
						.print("Invalid Response.  Please enter either 1, 2, or 3\n");
			}
		}

		switch (reply) {
		case "1":
			// When done hitting, add total to results ArrayList
			results.add(playerHits(person));
			dealerResult = simulateDealer();
			giveResults(results.get(0), dealerResult);
			break;
		case "2":
			// stand --> add total to results ArrayList
			results.add(person.check());
			dealerResult = simulateDealer();
			giveResults(results.get(0), dealerResult);
			break;
		case "3":
			System.out.println("Surrendered! ");
			System.out.println("Dealer Wins! (Lost " + bet / 2 + " credit)");
			person.changeChips(-1 * bet / 2);
			return;
		case "4":
			System.out.println("Doubled Down");
			doubleDown(person);
			results.add(person.check());
			dealerResult = simulateDealer();
			giveResults(results.get(0), dealerResult);
			break;
		case "5":
			// Divide into two separate hands
			PlayerHand person1 = new PlayerHand();
			person1.hand.add(person.hand.get(1));
			person.hand.remove(1);

			System.out.println("Here's your first hand:");
			person.sayHand();
			results.add(playerHits(person));

			System.out.println("\nHere's your second hand:");
			person1.sayHand();
			results.add(playerHits(person1));

			dealerResult = simulateDealer();

			// First hand results
			System.out.print("\n(First Hand)");
			giveResults(results.get(0), dealerResult);

			// Second hand results
			System.out.print("\n(Second Hand)");
			giveResults(results.get(1), dealerResult);
			break;
		}// End switch

	}// End play()

	// Reset for new game
	private void reset() {
		person.hand.clear();
		dealer.hand.clear();
		results.clear();
		dealerResult = 0;
	}

	// Method to print results and reward/collect bets
	private void giveResults(int pScore, int dScore) {

		System.out.println("\nResults:");
		System.out.println("Dealer (" + dScore + ")\nPlayer (" + pScore + ")");
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

	}

	private int doubleDown(PlayerHand p) {
		bet *= 2;
		int total = 0;
		p.hit(gameDeck);
		total = p.check();
		return total;
	}

	// Keep asking to hit, and returns hand total once player stands
	private int playerHits(PlayerHand p) {
		int total;
		while (true) {
			p.hit(gameDeck);
			total = p.check();
			if (total > 21) {
				System.out.println("BUST\n");
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

	}// End

	// Make sure player has enough chips to double their bet
	private boolean canDouble(PlayerHand p) {
		if (p.chips >= 2 * bet)
			return true;

		return false;
	}

	// Returns true if player's hand is a pair
	private boolean canSplit(PlayerHand p) {
		if (p.hand.get(0).value == p.hand.get(1).value && canDouble(p))
			return true;

		return false;
	}

	// Simulate dealer and return their hand total
	private int simulateDealer() {

		System.out.println("Dealer's turn.\n");
		System.out.println("Dealer reveals hand");
		dealer.sayHand();

		// Don't simulate dealer if player bust
		if (results.size() == 1 && results.get(0) > 21) {
			return dealer.check();
		}

		// Don't simulate dealer when player splits and bust both hands
		if (results.size() == 2 && results.get(0) > 21 && results.get(1) > 21) {
			return dealer.check();
		}

		// Must hit < 17
		while (dealer.check() < 17)
			dealer.hit(gameDeck);

		return dealer.check();

	}

}// End Game Class
