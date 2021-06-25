

public class BlackjackGame {
	
	private int users; 
	private Player[] players;
	private Deck deck;
	private Dealer dealer = new Dealer();
	public boolean printEnabled = false;

	// Starts game and displays the rules
	public void initializeGame(boolean print,int startB, int betU, int maxB){
		String names;		
		printEnabled = print;
		// Gets the amount of players and creates them
		users = 1;

		players = new Player[users];
		deck = new Deck();

		// Asks for player names and assigns them
		for (int i = 0; i < users; i++) {
			names = "P2L";
			players[i] = new Player(startB,betU,maxB);
			players[i].setName(names);
		}
	}
	
	// Shuffles the deck
	public void shuffle() throws InvalidDeckPositionException, InvalidCardSuitException, InvalidCardValueException {
		deck.shuffle();
	}

	// Gets the bets from the players
	public void getBets(){
		for (int i =0; i < users; i++) {  	
			if (players[i].getBank() > 0 && printEnabled) {
			System.out.println(players[i].getName() + " is now betting " + players[i].getBet());
			System.out.println("");
			}
		}

	}
	
	// Deals the cards to the players and dealer
	public void dealCards(){
		for(int j = 0; j < 2; j++){
			for (int i = 0; i < users; i++) {
				if(players[i].getBank() > 0)
				{
				players[i].addCard(deck.nextCard(),0);
				}
			}
	
			dealer.addCard(deck.nextCard());
		}
	}
	
	// Initial check for dealer or player Blackjack
	public boolean checkBlackjack(){

		if (dealer.isBlackjack() ) {
			if(printEnabled)
				System.out.println("Dealer has BlackJack!");
			for (int i =0; i < users; i++) {
				if (players[i].getTotal(0) == 21 ) {
					if(printEnabled)
						System.out.println(players[i].getName() + " pushes");
					players[i].push();
				} else {
					if(printEnabled)
						System.out.println(players[i].getName() + " loses");
					players[i].loss(players[i].getHand(0).getMultiplier());
				}	
			}
			return true;
		} else {
			if (dealer.peek()) {
				if(printEnabled)
					System.out.println("Dealer peeks and does not have a BlackJack");
			}

			for (int i =0; i < users; i++) {
				if (players[i].getTotal(0) == 21 ) {
					if(printEnabled)
						System.out.println(players[i].getName() + " has blackjack!");
					players[i].blackjack(players[i].getHand(0).getMultiplier());
					return true;
				}
			}
		}
		return false;	
	}
	
	// This code takes the user commands to hit or stand
	public void hitOrStand() {
		//String command;
		char c;
		for (int i = 0; i < users; i++) {
			if ( players[i].getBet() > 0 ) {
				if(printEnabled)
					System.out.println();
				for (int j=0; j<players[i].getNoOfHands(); j++){
					do {
						do {
							c = players[i].getMove(dealer.getHand().getFirstCard().getValue(),j);
						} while ( ! ( c == 'H' || c == 'S' || c == 'D' || c == 'P') );
						if ( c == 'H' ) {
							if(printEnabled)
								System.out.println(players[i].getName() + " is hitting on hand " + j);
							players[i].addCard(deck.nextCard(),j);
							if(printEnabled)
								System.out.println(players[i].getName() + " has " + players[i].getHandString(j));
						}else if ( c == 'D'){
							if(printEnabled)
								System.out.println(players[i].getName() + " is doubling down on hand " + j);
							players[i].getHand(j).setDoubleDown(true);
							players[i].addCard(deck.nextCard(),j);
							if(printEnabled)
								System.out.println(players[i].getName() + " has " + players[i].getHandString(j));
							c = 'S';
						}else if ( c == 'P'){
							if(printEnabled)
								System.out.println(players[i].getName() + " is splitting");
							players[i].splitHand();
						}else if ( c == 'S'){
							if(printEnabled)
								System.out.println(players[i].getName() + " is now standing on hand " + j);
						}else{
							if(printEnabled)
								System.out.println("Im doing something else");
							c = 'S';
						}
					} while (c != 'S' && players[i].getTotal(j) <= 21 );
				}
			}
		}
	}
	
	// Code for the dealer to play
	public void dealerPlays() {
		boolean isSomePlayerStillInTheGame = false;
		for (int i =0; i < users && isSomePlayerStillInTheGame == false; i++){
			if (players[i].getBet() > 0 && players[i].getTotal(0) <= 21 ) {
				isSomePlayerStillInTheGame = true;
			}
		}
		if (isSomePlayerStillInTheGame) {
			dealer.dealerPlay(deck, printEnabled);
		}
	}
	
	// This code calculates all possible outcomes and adds or removes the player bets
	public void settleBets() {
		if(printEnabled)
			System.out.println();

		for (int i = 0; i < users; i++) {
			if (players[i].getBet() > 0 ) {
				for (int j = 0; j < players[i].getNoOfHands(); j++){
					if( players[i].getTotal(j) > 21 ) {
						if(printEnabled)
							System.out.println(players[i].getName() + " has busted on hand " + j);
						players[i].bust(players[i].getHand(j).getMultiplier());
					} else if ( players[i].getTotal(j) == dealer.calculateTotal() ) {
						if(printEnabled)
							System.out.println(players[i].getName() + " has pushed on hand " + j);
						players[i].push();
					} else if ( players[i].getTotal(j) < dealer.calculateTotal() && dealer.calculateTotal() <= 21 ) {
						if(printEnabled)
							System.out.println(players[i].getName() + " has lost on hand " + j);
						players[i].loss(players[i].getHand(j).getMultiplier());
					} else if (players[i].getTotal(j) == 21) {
						if(printEnabled)
							System.out.println(players[i].getName() + " has won with blackjack! on hand " + j);
						players[i].win(players[i].getHand(j).getMultiplier());
					} else {
						if(printEnabled)
							System.out.println(players[i].getName() + " has won on hand " + j);
						players[i].win(players[i].getHand(j).getMultiplier());
						
					}
				}
			}
		}

	}

	// This prints the players hands
	public void printStatus() {
		for (int i = 0; i < users; i++) {
			if(players[i].getBank() > 0 && printEnabled)
			{
			System.out.println(players[i].getName() + " has " + players[i].getHandString(0));
			}
		}
		if(printEnabled)
			System.out.println("Dealer has " + dealer.getHandString(true, true));
	}
	
	// This prints the players banks and tells the player if s/he is out of the game
	public void printMoney() {
		for (int i = 0; i < users; i++) {
			if(players[i].getBank() > 0)
			{
				if(printEnabled){
					System.out.println(players[i].getName() + " has " + players[i].getBank());
					System.out.println("Wins: " + players[i].getWins() + " Double wins: " + players[i].getDoubleWins());
					System.out.println("Losses: " + players[i].getLosses() + " Double Losses: " + players[i].getDoubleLosses());
					System.out.println("Blackjacks: " + players[i].getBlackjacks() + " Double Blackjacks: " + players[i].getDoubleBlackjacks());
					System.out.println("Busts: " + players[i].getBusts() + " Double Busts: " + players[i].getDoubleBusts());
					System.out.println("Pushes: " + players[i].getPushes());
				}
					
			}
			if(players[i].getBank() == 0)
			{
			if(printEnabled)
				System.out.println(players[i].getName() + " has " + players[i].getBank() + " and is out of the game.");
			players[i].removeFromGame();
			}
		}
	}

	// This code resets all hands
	public void clearHands() {
		for (int i = 0; i < users; i++) {
			if(players[i].getHands().size()>1)
				players[i].getHands().remove(1);
			players[i].clearHand(0);
			players[i].getHand(0).setDoubleDown(false);
		}
		dealer.clearHand();
	}
	
	/*
	// This decides to force the game to end when all players lose or lets players choose to keep playing or not
	public boolean playAgain() {
		String command;
		char c;
		Boolean playState = true;
		if(forceEnd()) {
			playState = false;	
		}
		else {
			do {
				if(printEnabled)
				System.out.println("");
				System.out.print("Do you want to play again (Y)es or (N)o? ");
				command = ki.next();
				c = command.toUpperCase().charAt(0);
			} while ( ! ( c == 'Y' || c == 'N' ) );
			if(c == 'N')
			{
				playState = false;
			}
		}
		return playState;
	}
	*/
	
	// This says true or false to forcing the game to end
	public boolean forceEnd() {
		boolean end = false;
		int endCount = 0;
		
		for (int i = 0; i < users; i++) {
			if(players[i].getBank() == -1)
			{
				endCount++;
			}
		}
		if(endCount == users)
		{
			end = true;
		}
		if(end)
		{
			System.out.println("");
			System.out.println("All players have lost and the game ends.");
		}
		
		return end;
	}
	
	// This is the endgame code for when all players are out of the game or players decide to stop playing
		public void endGame() {
			int endAmount;
			String endState = " no change.";
			System.out.println("");
			for (int i = 0; i < users; i++) {
				if(players[i].getBank() == -1)
				{
					players[i].resetBank();
				}
				endAmount = players[i].getBank() - 100;
				if(endAmount > 0)
				{
					endState = " gain of ";
				}
				else if(endAmount < 0)
				{
					endState = " loss of ";
				}
				System.out.println(players[i].getName() + " has ended the game with " + players[i].getBank() + ".");
				if(endState != " no change.")
				{
				System.out.println("A" + endState + Math.abs(endAmount) + ".");
				}
				else
				{
				System.out.println("No change from their starting value.");	
				}
				System.out.println("");
			}
			System.out.println("");
			System.out.println("");
			System.out.println("Thank you for playing!");
		}

		public Player getPlayer(int index){
			return players[index];
		}


} //End class