import java.util.ArrayList;

public class PlayerHand {
	ArrayList<Card> hand;
	int chips;

	public PlayerHand(int c) {
		chips = 100;
		hand = new ArrayList<Card>();
	}

	// Overload Constructor
	public PlayerHand() {
		chips = 0;
		hand = new ArrayList<Card>();
	}

	public void dealHand(Deck deck) {

		// Deal two Cards
		this.hand.add(deck.deal());
		this.hand.add(deck.deal());

	}

	public void hit(Deck deck) {

		// Add card to hand and print it
		this.hand.add(deck.deal());
		System.out.println("Card dealt: " + hand.get(hand.size() - 1));

	}

	// Increase or decrease player's chips from betting
	public void changeChips(int c) {
		chips += c;
	}

	// Dealer hides one card
	public void dealerSayHand() {
		System.out.println("Dealer's hand:");
		System.out.println("?"); // hide first card
		System.out.println(hand.get(1).toString() + '\n'); // show second card
	}

	// Method to print cards in hand
	public void sayHand() {
		for (Card c : this.hand) {
			System.out.println(c.toString());
		}

	}

	// Method to return how many aces a hand has
	private int howManyAces() {
		int count = 0;

		for (Card c : this.hand) {
			if (c.value == 0)
				count++;
		}

		return count;
	}

	// check status of hand by returning appropriate sum of cards
	public int check() {

		// You can have AT MOST one Ace high
		int sumAhigh = 0; // One of Aces high
		int sumAlow = 0; // All aces low
		int sum = 0;
		int add; // what to add to sum

		for (Card c : this.hand) {
			add = c.value + 1; // Add 1 because index is 1 less

			// J, Q, K = 10
			if (add == 11 || add == 12 || add == 13) {
				add = 10;
			}

			// Disregard aces now, we will deal with later
			if (add == 1) {
				add = 0;
			}
			sum += add;
		}

		int aces = howManyAces();

		// If no aces, can return sum already
		if (aces == 0)
			return sum;

		sumAhigh = sum + aces + 10; // one of the aces is 11
		sumAlow = sum + aces; // all aces are 1

		if (sumAhigh == 21 || sumAlow == 21)
			return 21;
		else if (sumAhigh < 21 && sumAlow < 21)
			return sumAhigh; // return highest if both below 21
		else
			return sumAlow;

	}// End check()

} // End Player class
