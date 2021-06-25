import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable
{

	private ArrayList<Card> theHand = new ArrayList<Card>();
	private boolean doubleDown = false;

	// Calculates the total of a hand and also decides whether ace is 1 or 11
	public int calculateTotal() {
		int total =0;
		boolean aceFlag = false;
		for (int i = 0; i < theHand.size(); i++) {
			int value = theHand.get(i).getValue();
			if (value >= 10) {
				value = 10;
			} else if ( value == 1) {
				aceFlag = true;
			}
			total += value;
		}
		if (aceFlag && total + 10 <= 21) {
			total += 10;
		}
		return total;
	}
	
	public String toString(){
		return this.toString(false, false);
	}
	
	public String toString(boolean isDealer, boolean hideHoleCard){
		String str = "";
		int total =0;
		boolean aceFlag = false;
		String aceString = "";
		for (int i = 0; i < theHand.size(); i++) {
			if ( isDealer && hideHoleCard && i == 0) {
				str = " Showing";
			} else {
				int value = theHand.get(i).getValue();
				String valueName;
				if (value > 10) {
					valueName = theHand.get(i).getValueName().substring(0, 1);
				} else if ( value == 1 ){
					valueName = "A";
				} else {
					valueName = Integer.toString(value);
				}
						str += " " +valueName + theHand.get(i).getSuitDesignator();
				if (value > 10) {
					value = 10;
				} else if ( value == 1) {
					aceFlag = true;
				}
				total += value;
			}
		}
		if (aceFlag && total + 10 <= 21) {
			aceString = " or "+ (total + 10);
		}
		if ( hideHoleCard) {
			return str;
		} else {
			return str+ " totals "+ total + aceString;
		}
		
	}
	
	public void addCard(Card card) {
		theHand.add(card);
	}
	
	public void clearHand() {
		theHand.clear();
	}
	
	public boolean dealerPeek() {
		int value = theHand.get(1).getValue();
		return value == 1 || value >= 10;
	}

	public boolean isSoft(){
		int total =0;
		boolean aceFlag = false;
		for (int i = 0; i < theHand.size(); i++) {
			int value = theHand.get(i).getValue();
			if (value > 10) {
				value = 10;
			} else if ( value == 1) {
				aceFlag = true;
			}
			total += value;
		}
		if (aceFlag && total + 10 <= 21) {
			return true;
		}
		return false;
	}

	public Card getFirstCard() {
		return theHand.get(1);
	}

	public int getNoOfCards(){
		return theHand.size();
	}

	public Card removeCard(int index){
		return theHand.remove(index);
	}

	public boolean canSplit() {
		if(theHand.size() == 2){
			if(theHand.get(0).getValue() == theHand.get(1).getValue())
				return true;
		}
		return false;
	}

	public void setDoubleDown(boolean b){
		doubleDown = b;
	}

	public int getMultiplier(){
		return (doubleDown)?2:1;
	}


} //End class