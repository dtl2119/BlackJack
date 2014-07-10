import java.util.Scanner;

// main method
public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Game g = new Game();

		do {
			g.play();
			System.out
					.println("Type 'y' to play again (or any other key to exit)");

		} while (in.next().equals("y"));

		in.close();

	}

}
