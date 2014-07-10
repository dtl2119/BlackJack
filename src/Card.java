public class Card {

	int value;
	int suit;

	public Card(int v, int s) {
		value = v;
		suit = s;
	}

	public String toString() {
		String[] suits = { "Hearts", "Clubs", "Diamonds", "Spades" };
		String[] values = { "Ace", "Two", "Three", "Four", "Five", "Six",
				"Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King" };

		return (values[this.value] + " of " + suits[this.suit]);
	}

}