import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {

	public static void main(String[] args) throws Exception {
		ArrayList<ArrayList<Integer>> bankAmountLists = new ArrayList<ArrayList<Integer>>();
		int gains = 0;
		int losses = 0;
		int caps = 0;
		int brokes = 0;
		int unevenWins = 0;
		int gameWins = 0;
		int gameDoubleWins = 0;
		int gameLosses = 0;
		int gameDoubleLosses = 0;
		int gameBusts = 0;
		int gameDoubleBusts = 0;
		int gameBlackjacks = 0;
		int gameDoubleBlackJacks = 0;
		int gamePushes = 0;
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		System.out.println("What would you like your starting balance to be?");
		int startBalance = myObj.nextInt();  // Read user input
		System.out.println("What would you like your bet unit to be?");
		int betUnit = myObj.nextInt();  // Read user input
		System.out.println("What would you like your max bet to be?");
		int maxBet = myObj.nextInt();  // Read user input
		System.out.println("What target value are you aiming for (1.5,2,3,etc....)?");
		double targetScalar = myObj.nextDouble();  // Read user input
		System.out.println("How many games should be played before a reset?");
		int gamesPerReset = myObj.nextInt();  // Read user input
		System.out.println("How many sims of that many games would you like to run?");
		int resets = myObj.nextInt();  // Read user input

		
		for(int k = 0; k<resets; k++){
				BlackjackGame mygame = new BlackjackGame();
				ArrayList<Integer> bankAmounts = new ArrayList<Integer>();
				int i = 0;
				mygame.initializeGame(false,startBalance,betUnit,maxBet);//pass true if you want to print each individual game
				System.out.print("\u001B[41m" + "---------------STARTING A FRESH TEST!!! (Test No." + (k+1) + ")--------------------" + "\u001B[0m"+((mygame.printEnabled)?"\n":"\r"));
				while(i<gamesPerReset){
					i++;
					if(mygame.printEnabled)
						System.out.println("\u001B[44m" + "---------------STARTING A FRESH GAME!!! (Test No." + (i) + ")--------------------" + "\u001B[0m");
					mygame.shuffle();
					mygame.getBets();
					if((mygame.getPlayer(0).getBet()<mygame.getPlayer(0).getBank()) && mygame.getPlayer(0).getBank()<(mygame.getPlayer(0).getStartingBalance()*targetScalar)){
						mygame.dealCards();
						mygame.printStatus();
						if(mygame.checkBlackjack() == false){
							mygame.hitOrStand();
							mygame.dealerPlays();
							mygame.settleBets();
						}
						mygame.printMoney();
						bankAmounts.add(mygame.getPlayer(0).getBank());
						mygame.clearHands();
					}else if(mygame.getPlayer(0).getBank()>=(mygame.getPlayer(0).getStartingBalance()*targetScalar)){
						if(mygame.printEnabled)
							System.out.println(mygame.getPlayer(0).getName() + " Hit their cutoff point!");
						caps++;
						break;
					}else{
						if(mygame.printEnabled)
							System.out.println(mygame.getPlayer(0).getName() + " went broke!");
						brokes++;
						break;
					}
					
				}
				if(mygame.getPlayer(0).getBank()>mygame.getPlayer(0).getStartingBalance()){
					gains++;
				}else{
					losses++;
				}
				bankAmountLists.add(bankAmounts);
				//if(((mygame.getPlayer(0).getLosses()*(-1000))+(mygame.getPlayer(0).getDoubleLosses()*(-2000))+(mygame.getPlayer(0).getBusts()*(-1000))+mygame.getPlayer(0).getWins()*1000+mygame.getPlayer(0).getDoubleWins()*2000+mygame.getPlayer(0).getBlackjacks()*1500)+mygame.getPlayer(0).getStartingBalance()!=mygame.getPlayer(0).getBank())
				//	unevenWins++;
				gameWins += mygame.getPlayer(0).getWins();
				gameDoubleWins += mygame.getPlayer(0).getDoubleWins();
				gameLosses += mygame.getPlayer(0).getLosses();
				gameDoubleLosses += mygame.getPlayer(0).getDoubleLosses();
				gameBusts += mygame.getPlayer(0).getBusts();
				gameDoubleBusts += mygame.getPlayer(0).getDoubleBusts();
				gameBlackjacks += mygame.getPlayer(0).getBlackjacks();
				gameDoubleBlackJacks += mygame.getPlayer(0).getDoubleBlackjacks();
				gamePushes += mygame.getPlayer(0).getPushes();
			}	
			
			
			try (PrintWriter writer = new PrintWriter(new File("output.csv"))) {

				StringBuilder sb = new StringBuilder();
				for(int i = 0; i<bankAmountLists.size(); i++){
					for(int j=0;j<bankAmountLists.get(i).size();j++){
						sb.append(bankAmountLists.get(i).get(bankAmountLists.get(i).size()-j-1));
						sb.append(',');
					}
					sb.append('\n');
				}
				sb.append('\n');
				writer.write(sb.toString());
		
				System.out.println();
				System.out.println("After running 10000 sims, " + caps + " met the cap");
				System.out.println("After running 10000 sims, " + brokes + " went broke");
				System.out.println();
				/*
				System.out.println("After running 10000 sims, " + gains + " ended higher than they started");
				System.out.println("After running 10000 sims, " + losses + " ended lower than they started");
				System.out.println();
				*/

				System.out.println("With a final tally of " + (gameWins+gameDoubleWins+gameLosses+gameDoubleLosses+gameBusts+gameDoubleBusts+gameBlackjacks+gameDoubleBlackJacks+gamePushes) + " games, heres the breakdown:");
				System.out.println(gameWins + " Wins, " + gameDoubleWins + " doubled down Wins");
				System.out.println(gameBlackjacks + " Blackjacks, " + gameDoubleBlackJacks + " doubled down Blackjacks");
				System.out.println(gameLosses + " Losses, " + gameDoubleLosses + " doubled down Losses");
				System.out.println(gameBusts + " Busts, " + gameDoubleBusts + " doubled down Busts");
				System.out.println(gamePushes + " Pushes");
				int resultingProfit = (gameWins*1000)+(gameLosses*(-1000))+(gameBusts*(-1000))+(gameBlackjacks*1500);
				System.out.println("Resulting in an overall profit margin of "+resultingProfit);
				
		
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
	}
		

} //End class