import java.util.Random;

public class Deck extends Exception
{

	private int nextCardIndex;
	

	Card[] deck = new Card[52];

	public Deck(){

		int count = 0;
		try{
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card('H', i);
		}
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card('S', i);
		}
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card('C', i);
		}
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card('D', i);
		}	
		}
		
		catch(InvalidCardValueException | InvalidCardSuitException exp1) {
			
		}
		nextCardIndex = 0;
	}
	private void isIndexGood(int index) throws InvalidDeckPositionException {
		if (index < 0 || index > 51) {
			throw new InvalidDeckPositionException(index);
		}
	}
	
	public String toString(){

		String str = "";

		for (int i = 0; i < deck.length; i++) {
			str +=	deck[i].toString() + " ";
		}
		return str;
	}


	private void swapCards(int index1, int index2) throws InvalidDeckPositionException {	
		Card hold;

		isIndexGood(index1);
		isIndexGood(index2);
		hold = deck[index1];
		deck[index1] = deck[index2];
		deck[index2] = hold;
	}

	public void shuffle() throws InvalidDeckPositionException {
		Random rn = new Random();
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < deck.length; j++) {
				swapCards(j, rn.nextInt(52));
			}
		}
		nextCardIndex = 0;
	}
	
	public Card getCard(int index) throws InvalidDeckPositionException{
		isIndexGood(index);
		return deck[index];
	}

		

	public boolean compareTo(Deck otherDeck) throws InvalidDeckPositionException {
		for (int i=0; i < deck.length; i++){
			if (! deck[i].compareTo(otherDeck.getCard(i)) ) {
				return false;
			}
		}
		return true;
	}

	public Card nextCard() {
		if (nextCardIndex < 0 || nextCardIndex > 51) {
			System.out.println("Future exception goes here");
		}
		//System.out.println("Adding card to deck: " + deck[nextCardIndex+1].toString());
		return deck[nextCardIndex++];
	}

} //End class