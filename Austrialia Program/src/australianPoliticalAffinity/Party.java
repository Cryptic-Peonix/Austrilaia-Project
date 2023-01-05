package australianPoliticalAffinity;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
/**
 * 
 * This class will be used as the object type for a future hash table or ArrayList
 * @author Marc D'Avanzo, edited by Connor Clark
 */
public class Party {
	protected ArrayList<Ballot> ballotArray;
	private ArrayList<String> rankTwoNames;
	private int[] rankTwo;
	//Will hold all the ballots that ranked the specified party first
	//the scanner used for user inputs
	//will be used to hold the name of the selected party
	protected String partyName; 
	private Searcher search;
	protected double spectrumVal;
	private int coalitionSupport;
	private int laborSupport;
	protected int equalSupport;
	
	/**
	 * @param none
	 * @throws FileNotFoundException 
	 * @return void
	 * This constructor will instantiate all instance variables
	 */
	public Party(String partyName) throws FileNotFoundException
	{
		ballotArray = new ArrayList<Ballot>();
		this.partyName = partyName;
		search = new Searcher();
		spectrumVal = 0;
		laborSupport =0;
		coalitionSupport = 0;
		equalSupport = 0;
		rankTwoNames  =new ArrayList<String>();
		Scanner in = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		while(in.hasNextLine())
		{
			String temp = in.nextLine();
			if(temp.charAt(0) > 6000)
			{
				temp = temp.substring(1);
			}
			rankTwoNames.add(temp);
		}
		rankTwo = new int[rankTwoNames.size()];

	}
	
	/**
	 * 
	 * @param partyName
	 * @return void
	 * @throws FileNotFoundException
	 * This method class the Formater class and will add to the ballots ArrayList
	 */
	public void addBallots(int one, int two, String name) throws FileNotFoundException
	{
		Ballot temp = new Ballot(one,two,name);
		ballotArray.add(temp);
		
		
	}
	
	/**
	 * @param none
	 * @return void
	 * This class will print the size of ballot
	 */
	public void printBallotSize()
	{
		System.out.println("There are " + ballotArray.size() + "  ballots in this array");

	}
	
	/**
	 * @param none
	 * @return void
	 * This method will fill the rankTwo array
	 */
	public void fillRankTwo()
	{
		int invalid =0;
		//if the current ballot does not have a rank 2 then it ill not be added to the array
		for(int x = 0; x < ballotArray.size();x++)
		{
			if(ballotArray.get(x).getRankTwo() !=-1)
			{
			 int temp = rankTwoNames.indexOf(ballotArray.get(x).getRankTwoPartyName());
			 if(temp == -1)
			 { 
				 System.out.println("error: " + ballotArray.get(x).getRankTwoPartyName() );
				 continue;
			 }
			 int tempVal = rankTwo[temp] + 1;
			 rankTwo[temp] = tempVal; 
			}
			else
			{
				invalid++;
			}
		}
		System.out.println("There were this many invalid ballots for " + partyName + " " + invalid);
	}
	/**
	 * @param none
	 * @return ballot
	 * This method will return the ArrayList ballot
	 */
	public ArrayList<Ballot> getBallots()
	{
		return ballotArray;
	}
	/**
	 * @param x
	 * @return Ballot
	 * This method will return the ballot at the selected index
	 */
	public Ballot getBallotIndex(int x )
	{
		return ballotArray.get(x);
	}
	/**
	 * @param x
	 * @return int
	 * This method will return the rankOneIndex value from the selected ballot
	 */
	public int getRankOne(int x)
	{
		return ballotArray.get(x).getRankOne();
	}
	/**
	 * @param x
	 * @return int
	 * This method will return the rankTwoIndex value from the selected ballot
	 */
	public int getRankTwo(int x)
	{
		return ballotArray.get(x).getRankTwo();
	}
	/**
	 * @param x
	 * @return int
	 * This method will return the rankTwoPartyName value from the selected ballot
	 */
	public String getRankTwoPartyName(int x)
	{
		return ballotArray.get(x).getRankTwoPartyName();
	}
	/**
	 * @param none
	 * @return int
	 * This method will return the ballotArray Size
	 */
	public int getBallotSize()
	{
		return ballotArray.size();
	}
	/**
	 * @param none
	 * @return void
	 * This method will clear ballotArray
	 */
	public void clearBallots()
	{
		ballotArray.clear();
	}
	
	
	
	/**
	 * 
	 * @param none
	 * @return partyName
	 * returns the String partyName
	 */
	public String getPartyName()
	{
		return partyName;
	}
	
	/**
	 * @param none
	 * @return void
	 * prints out the partyName variable
	 */
	public void printPartyName()
	{
		System.out.println(partyName);
	}
	
	/**
	 * @param none
	 * @returns void
	 * prints the number of elements in the array. 
	 * 
	 */
	public void printSpectrumVals()
	{
		System.out.println("");
		System.out.println(partyName + " SpectrumVals:");
		System.out.println("Ballot Size: " + ballotArray.size());
		System.out.println("C/(C+L) =  " + spectrumVal);
		System.out.println("C/Ballot.size = " + ((double)coalitionSupport / ((double)ballotArray.size())));
		System.out.println("C- L: " + (coalitionSupport - laborSupport) );
		System.out.println("Total votes: Coalition Votes " + coalitionSupport + " vs " + "Labor votes " + laborSupport );
		System.out.println("No Coalition or Labor party ranked: " + equalSupport);
	}
	/**
	 * @param none
	 * @return double
	 * This method will return spectrumVal
	 */
	public double getSpectrumVals()
	{
		return spectrumVal;
	}
	/**
	 * @param none
	 * @return void
	 * This method will call the calculateSpectrumVal method will what is returned becomes spectrumVal
	 */
	public void addSpectrumVal()
	{
	   spectrumVal = calculateSpectrumVal();
	}
	/**
	 * @param none
	 * @return double
	 * This method will calculate the spectrumVal
	 */
	public double calculateSpectrumVal()
	{
		double cTemp = coalitionSupport;
		double lTemp = laborSupport;
		double supportVal = cTemp + lTemp;
		double temp = cTemp / supportVal;
		return temp;
	}

	/**
	 * @param none
	 * @return void
	 * This method will add one to coalitionSupport
	 */
	public void addCVote()
	{
		coalitionSupport++;
	}
	/**
	 * @param none
	 * @return void
	 * This method will add one to laborSupport
	 */
	public void addLVote() {
		laborSupport++;
	}
	/**
	 * @param none
	 * @return void
	 * This method will add one to equalSupport
	 */
	public void addESupport()
	{
		equalSupport++;
	}
	/**
	 * @param none
	 * @return double
	 * This method will return the coalitionSupport value
	 */
	public double getCVote()
	{
		return coalitionSupport;
		
	}
	/**
	 * @param none
	 * @return double
	 * This method will return the laborSupport value
	 */
	public double getLVote()
	{
		return laborSupport;
	}
	/**
	 * @param none
	 * @return int
	 * This method will return the length of rankTwo[]
	 */
	public int getRankTwoLength() {
	return rankTwo.length;
	}
	/**
	 * @param rankTwoIndex
	 * @return int
	 * this method will return the rankTwo value from the rankTwo[] of the selected index pos
	 */
	public int getRankTwoIndex(int rankTwoIndex)
	{
		return rankTwo[rankTwoIndex];
	}
	public static void main(String[] args) throws FileNotFoundException
	{
		Party test = new Party("Liberal");
		test.addBallots(1,1,"test");
		test.printBallotSize();
		
	}
	

}
