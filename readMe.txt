*********************************************************************************
* BlackJack readMe
* Written by: Drew Limm
* Insight Data Engineering
*
*********************************************************************************
Description
-----------
I created a Java BlackJack program with one dealer (computer), and one player
controlled by user input.  Players are given the option to hit, stand, surrender,
double down, or split (when allowed).  The source code includes five classes:

Card - An object with a value and suit (13 different values, 4 different suits)
Deck - Variable sized array of Card Objects, capable of being dealt and shuffled
PlayerHand - Holds ArrayList of Cards and methods for the player's hand
Game - Core of program.  Creates deck, deals hands, and plays the game
Main - Main method.  Creates a Game, and keeps playing it until player quits

Specific Rules
--------------
- Player must bet in integers greater than 1.
- (3/2) payout for Blackjack.  Rounds down if bet is odd.  
- Player breaks even on a push.
- Player can split if initial hand is a pair (10 and 10 works, but not J and K).
- Player can surrender, losing half their bet.  Rounds down if bet is odd.
- Player can double down if they have enough chips.
- Dealer hits if hand is less than 17.
- Game is played with 5 Decks shuffled every 10 rounds.

Execution
---------
To run my program, first download and unzip the BlackJack.zip file and cd into 
BlackJack/src/.  Once in the correct directory, compile and run the main method:
>javac *.java
>java Main

Additional Notes:
-----------------
I created a PlayerHand class so that it would be easier in the future if we
want to add more players.  Also, it makes it easier when a player decides
to split since we can just create a new PlayerHand object, and treat the
two hands separately.  

I like to keep my main method clean with minimal code in it.  All it does 
is create a Game object, and continuously play() it until the player decides
to quit or he runs out of money.  The main simulation and algorithms of this
BlackJack game is coded in the play() method of the Game class, thus minimizing
the clutter in the main method.  Also, all input errors are handled.  The
program makes sure all bets and other user inputs are valid.  