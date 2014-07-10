public class Deck {
	int numOfDecks;
	Card[] d;
	int top;

	public Deck(int n) {
		numOfDecks = n;
		d = new Card[52 * numOfDecks];
		top = 0;// top of deck to deal
	}

	// Generate the Deck: 52 Cards
	public void createDeck() {

		int numCard = 0; // numCard for current card index
		
		// Outer = number of Decks, Middle = suit, Inner = value
		for (int i = 0; i < numOfDecks; i++) {
			for (int s = 0; s < 4; s++) {
				for (int v = 0; v < 13; v++) {
					Card add = new Card(v, s);
					d[numCard] = add;
					numCard++;
				}
			}
		}
	}

	// Shuffle Deck
	public void shuffle() {
		// reset top of deck to index 0
		top = 0;

		// random swapping n times
		for (int i = 0; i < 1000000; i++) {

			// Get two random indices (0 to 51)
			int first = (int) (Math.random() * (52*numOfDecks));
			int second = (int) (Math.random() * (52*numOfDecks));
			Card temp = d[first];
			d[first] = d[second];
			d[second] = temp;
		}// end shuffle for loop

	}// End shuffle method

	// deal top card of deck;
	public Card deal() {

		// Have to increment before returning
		// Therefore, increment and then choose
		// 1 less than top;
		top++;
		return d[top - 1];

	}

	public void cheat() {
		Card temp = d[1];
		d[1] = d[13];
		d[13] = temp;
	}

}