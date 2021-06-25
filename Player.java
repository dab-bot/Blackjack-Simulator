import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable
{
	private int bank;
	private int bet;
	private int profit;
	private int winCounter;
	private int loseCounter;
	private int bjCounter;
	private int bustCounter;
	private int pushCounter;
	private int doubleWinCounter;
	private int doubleLoseCounter;
	private int doubleBjCounter;
	private int doubleBustCounter;
	private int doublePushCounter;
	private String name;
	private ArrayList<Hand> hands = new ArrayList<Hand>();
	private int startBalance;
	private int betUnit;
	private int maxBet;
	
	// Creates a player object
	public Player(int startB, int betU, int maxB) {
		startBalance = startB;
		betUnit = betU;
		maxBet = maxB;
		hands.add(new Hand());
		bank = startBalance;
		bet = betUnit*1;
		winCounter = 0;
		loseCounter = 0;
		bjCounter = 0;
		bustCounter = 0;
		pushCounter = 0;
	}
	
	// Gets a player's bank amount
	public int getBank() {
		return bank;
	}
	
	
	// Removes a player's bet from their bank if they bust. Sets bet to zero afterwards.
	public void bust(int betMultiplier) {
		bank -= bet*betMultiplier;
		profit -= bet*betMultiplier;
		if(betMultiplier>1){
			doubleBustCounter++;
		}else{
			bustCounter++;
		}
	}
	
	// Adds a player's bet from their bank if they win. Sets bet to zero afterwards.
	public void win(int betMultiplier) {
		bank += (bet*betMultiplier);
		profit += (bet*betMultiplier);
		if(profit < betUnit){
			if(profit + bet + betUnit > betUnit){
				bet = Math.min(betUnit-profit,maxBet);
			}else{
				bet = Math.min(betUnit+bet,maxBet);
			}
		}else if(profit>= betUnit){
			bet = betUnit;
			profit = 0;
		}
		if(betMultiplier>1){
			doubleWinCounter++;
		}else{
			winCounter++;
		}
	}

	// Removes a player's bet from their bank if they lose. Sets bet to zero afterwards.
	public void loss(int betMultiplier) {
		bank -= bet*betMultiplier;
		profit -= bet*betMultiplier;
		if(betMultiplier>1){
			doubleLoseCounter++;
		}else{
			loseCounter++;
		}
	}

	// This calculate the bet for a player who has a Blackjack
	public void blackjack(int betMultiplier) {
		bank += (bet*betMultiplier*1.5);
		profit += (bet*betMultiplier*1.5);
		if(profit < betUnit){
			if(profit + bet + betUnit > betUnit){
				bet = Math.min(betUnit-profit,maxBet);
			}else{
				bet = Math.min(betUnit+bet,maxBet);
			}
		}else if(profit>= betUnit){
			bet = betUnit;
			profit = 0;
		}
		if(betMultiplier>1){
			doubleBjCounter++;
		}else{
			bjCounter++;
		}
	}
	
	// Sets a player's bet to 0 if the "push". Notice, no bet is added or removed.
	public void push() {
		if(profit < betUnit){
			if(profit + bet + betUnit > betUnit){
				bet = Math.min(betUnit-profit,maxBet);
			}else{
				bet = Math.min(betUnit+bet,maxBet);
			}
		}
		pushCounter++;
	}
	

	/*
	//Flat bet
	// Removes a player's bet from their bank if they bust. Sets bet to zero afterwards.
	public void bust(int betMultiplier) {
		bank -= (bet*betMultiplier);
		if(betMultiplier>1){
			doubleBustCounter++;
		}else{
			bustCounter++;
		}

	}
	
	// Adds a player's bet from their bank if they win. Sets bet to zero afterwards.
	public void win(int betMultiplier) {
		bank += (bet*betMultiplier);
		if(betMultiplier>1){
			doubleWinCounter++;
		}else{
			winCounter++;
		}
	}

	// Removes a player's bet from their bank if they lose. Sets bet to zero afterwards.
	public void loss(int betMultiplier) {
		bank -= (bet*betMultiplier);
		if(betMultiplier>1){
			doubleLoseCounter++;
		}else{
			loseCounter++;
		}
	}

	// This calculate the bet for a player who has a Blackjack
	public void blackjack(int betMultiplier) {
		bank += (bet*betMultiplier*1.5);
		if(betMultiplier>1){
			doubleBjCounter++;
		}else{
			bjCounter++;
		}
	}
	
	// Sets a player's bet to 0 if the "push". Notice, no bet is added or removed.
	public void push() {
		pushCounter++;
	}
	*/
	
	// This sets the player bank to -1. -1 is unreachable and they are removed from the game
	public void removeFromGame() {
		bank = -1;
	}
	
	// This resets the bank to 0. Currently used to reset a removed player's bank from -1 to 0.
	public void resetBank() {
		bank = 0;
	}
	
	// Sets a player's bet
	public void setBet(int newBet) {
		bet = newBet;
	}
	
	// Sets a player's name
	public void setName(String name1){
		name = name1;
	}
	
	// Gets a player's name
	public String getName() {
		return name;
	}
	
	// Gets a player's hand total
	public int getTotal(int index) {
		return hands.get(index).calculateTotal();
	}
	
	// Gets a player's bet
	public int getBet(){
		return this.bet;
	}
		
	// Adds a card to a player's hand
	public void addCard(Card card, int index) {
		hands.get(index).addCard(card);
	}
	
	// Gets the player's cards to print as a string
	public String getHandString(int index) {
		String str = "Cards:" + hands.get(index).toString();
		return str;
	}

	// Gets the player's cards to print as a string
	public Hand getHand(int index) {
		return hands.get(index);
	}

	// Gets the player's cards to print as a string
	public ArrayList<Hand> getHands() {
		return hands;
	}
		
	// Clears a player's hand
	public void clearHand(int index) {
		hands.get(index).clearHand();
	}

	// Return number of hands
	public int getNoOfHands() {
		return hands.size();
	}

	public int getStartingBalance(){
		return startBalance;
	}

	/*
	Character->move Legend:
	Hit = H
	Stand = S
	Double otherwise Hit = E
	Double otherwise Stand = F
	Split = P
	Split if double after is allowed, otherwise hit = Q
	Hit if 1 away from charlie,otherwise stand = O
	Hit if 1 or 2 away from charlie, otherwise stand = T */
	public char getMove(int dealerCard, int index) {
		String options = "SSSSSSSSSS";
		char move = 'S';
		//Hard
		if(!getHand(index).canSplit() && !getHand(index).isSoft()){
			//System.out.println("This hand is a hard " + getTotal());
			if(getTotal(index) <= 8)		{options = "HHHHHHHHHH";}
			else if(getTotal(index)==9)		{options = "HEEEEHHHHH";}
			else if(getTotal(index)==10)	{options = "EEEEEEEEHH";}
			else if(getTotal(index)==11)	{options = "EEEEEEEEEH";}
			else if(getTotal(index)==12)	{options = "HHTTTHHHHH";}
			else if(getTotal(index)==13)	{options = "TTOOOHHHHH";}
			else if(getTotal(index)==14)	{options = "OOOOOHHHHH";}
			else if(getTotal(index)==15)	{options = "OOOOOHHHHH";}
			else if(getTotal(index)==16)	{options = "OOSSSHHHHH";}
			else if(getTotal(index)==17)	{options = "SSSSSSSOOO";}
			else							{options = "SSSSSSSSSS";}
		}
		//Soft
		else if(!getHand(index).canSplit() && getHand(index).isSoft()){
			//System.out.println("This hand is a soft " + getTotal());
			if(getTotal(index) == 13)		{options = "HHHEEHHHHH";}
			else if(getTotal(index)==14)	{options = "HHHEEHHHHH";}
			else if(getTotal(index)==15)	{options = "HHEEEHHHHH";}
			else if(getTotal(index)==16)	{options = "HHEEEHHHHH";}
			else if(getTotal(index)==17)	{options = "HEEEEHHHHH";}
			else if(getTotal(index)==18)	{options = "TFFFFTTTTT";}
			else if(getTotal(index)==19)	{options = "OOOOOOOOTO";}
			else if(getTotal(index)==20)	{options = "OOOOOOOOOO";}
			else if(getTotal(index)==21)	{options = "OOOOOOOOOO";}
		}
		//Splits
		else if(getHand(index).canSplit() &&  hands.size()<2){
			if(getTotal(index) == 4)		{options = "QQPPPPHHHH";}
			else if(getTotal(index)==6)		{options = "QQPPPPHHHH";}
			else if(getTotal(index)==8)		{options = "HHHQQHHHHH";}
			else if(getTotal(index)==12)	{options = "QPPPPHHHHH";}
			else if(getTotal(index)==14)	{options = "PPPPPPHHHH";}
			else if(getTotal(index)==16)	{options = "PPPPPPPPPP";}
			else if(getTotal(index)==18)	{options = "PPPPPSPPSS";}
			else if(getTotal(index)==12)	{options = "PPPPPPPPPP";}
		}
		//System.out.println("The available options are: " + options);
		//System.out.println("Dealers card is: " + dealerCard);
		//System.out.println("Which is at index: " + Math.floorMod(dealerCard-2,10));
		//System.out.println("Move chosen for P2L: " + options.charAt(Math.floorMod(dealerCard-2,10)));

		move = options.charAt(Math.floorMod(dealerCard-2,10));
		if(move == 'E' || move == 'F'){
			if (getHand(index).getNoOfCards()<=2){
				move = 'D';
			}else{move = (move == 'E')?'H':'S';}
		}
		if(move == 'O'){move = (getHand(index).getNoOfCards()==6)?'H':'S';}
		if(move == 'T'){move = (getHand(index).getNoOfCards()>=5)?'H':'S';}
		if(move == 'Q'){move = 'P';}

		return move;
	}

	public void splitHand(){
		hands.add(new Hand());
		hands.get(1).addCard(hands.get(0).removeCard(1));
	}

	public int getWins() {
		return winCounter;
	}

    public int getLosses() {
        return loseCounter;
    }

	public int getBlackjacks(){
		return bjCounter;
	}

	public int getPushes(){
		return pushCounter;
	}

	public int getBusts(){
		return bustCounter;
	}

	public int getDoubleWins() {
		return doubleWinCounter;
	}

    public int getDoubleLosses() {
        return doubleLoseCounter;
    }

	public int getDoubleBlackjacks(){
		return doubleBjCounter;
	}

	public int getDoublePushes(){
		return doublePushCounter;
	}

	public int getDoubleBusts(){
		return doubleBustCounter;
	}
		
} //End class