/**
 * This class will create an array of the party class.
 * It will then search through every ballot and place every rank one ballot with its corresponding party
 * @author Marc D;Avanzo
 */

package australianPoliticalAffinity;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class PartyArray {
	private Scanner scan;
	private Scanner reader;
	private ArrayList<Party> partyArray;
	private int totalBallots;
	
	private File file;
	private String[] fileList;
	private Scanner orderList;
	private Scanner in;
	private ArrayList<String> partyOrder;
	private ArrayList<String> firstLine;
	private String currentLine;
	private String partyName;
	private int index;
	private Searcher search;	
	private String lineOne;
	private boolean BTL;
	
	/** 
	 * @param none
	 * @return void
	 * @throws FileNotFoundException
	 * This constructor assigns basic values to all the instance variables
	 */
	public PartyArray() throws FileNotFoundException
	{
		reader = null;
		scan = new Scanner(System.in);
		partyArray = new ArrayList<Party>();
		File file = new File("data");
		fileList = file.list();
		Arrays.parallelSort(fileList);
		file = new File("data");
		fileList = file.list();
		Arrays.sort(fileList);
		orderList = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		in = null;
		partyOrder = new ArrayList<String>();
		firstLine = new ArrayList<String>();
		index = -1;
		currentLine = "";
		partyName = "";
		search = new Searcher();
		lineOne ="";
		BTL = false;
		while(orderList.hasNextLine())
		{
			String temp = orderList.nextLine();
			if(temp.charAt(0) > 6000)
			{
				System.out.println("found a BOM in orderComplete");
				temp = temp.substring(1);
			}
			partyOrder.add(temp);
		}
		//creates a array of Party objects and assigns each one one of the parties names
		Scanner partyList = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		while(partyList.hasNextLine())
		{
			String temp = partyList.nextLine();
			if(temp.charAt(0) > 6000)
			{
				temp = temp.substring(1);
			}
			Party party = new Party(temp);
			partyArray.add(party);	
		}
	}
	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException
	 * This method will create the party array by searching the csv files and sorting all the rank 1 ballots by party
	 */
	public void createPartyArray() throws FileNotFoundException
	{
		
		// goes through every line in the csv files
		int totalLines = 0;
		for(int i = 0; i < fileList.length; i++)
		{
			int currentLines = 0;
			System.out.println("Now Reading: " + fileList[i]);
			try
			{
			in = new Scanner(new FileInputStream("data\\" + fileList[i]));
			}
			catch(FileNotFoundException e)
			{
				continue;
			}
			//takes the first line of a csv file that contians the party order and saves it to the firstLine ArrayList
			currentLine = in.nextLine();
			firstLine.clear();
			StringTokenizer st = new StringTokenizer(currentLine, ",", false);
		    // adds the first line of a csv file to a arrayList as to remember the party order
			while(st.hasMoreTokens())
			{
				firstLine.add(st.nextToken());
			}
			// creates an ArrayList of just the letter ID of the parties
			ArrayList<String> firstLetters = new ArrayList<String>();
			for(int x =6; x < firstLine.size(); x++ )
			{
				firstLetters.add(firstLine.get(x).substring(0,firstLine.get(x).indexOf(":")+1));
			}
			
			// will check every line in the current csv file
			search.checkOrder(currentLine);
			totalLines++;
			currentLines++;
			while(in.hasNextLine())
			{
				//replaces all the blank spaces on the current ballot with -1
				currentLine = in.nextLine();
				//System.out.println("pre: " + currentLine);
				currentLine = currentLine.replace(",,", ",-1,");
				//System.out.println("One time: "  + currentLine);
				currentLine = currentLine.replace(",,", ",-1,");
			    //System.out.println("Two times: " + currentLine);
				 if(search.searchBallot(currentLine))
				 {
				    index = search.BTLIndex();
				 //   System.out.println("Mew index is: " + index);
				    BTL = true;
				 }
				 else
				 {
					 index = search.getIndex();
					 BTL = false;
				 }
	     		 int rankTwoIndex = search.getRankTwoIndex();
	     		 boolean lineChanged = false; 
				 if(search.getChanged())
				 {
				 //currentLine = search.getLine();
					 lineChanged = true;
				 }
				if(!BTL)
				{
					System.out.println(index);
				 partyName = firstLine.get(index).substring(firstLine.get(index).indexOf(":")+1);
				}
				else {
					if(index != -500)
					{
					partyName = firstLine.get(index).substring(firstLine.get(index).indexOf(":")+1);
					}
					
				
					//UG have no party thus are always below the line
					else
					{
					//	System.out.println("Changed to UG");
						partyName = "UG";
					//	BTL = true;
					}



				}
				String rankTwoPartyName;
				if(rankTwoIndex !=-1)
				{
	     		 rankTwoPartyName = firstLine.get(rankTwoIndex).substring(0,firstLine.get(rankTwoIndex).indexOf(":")+1);
	     		 rankTwoPartyName = search.findRankTwoPartyName(rankTwoPartyName);
				}
				else 
				{
					rankTwoPartyName = "NA";
				}
				
	     		

	     	    	
	     		if(rankTwoPartyName.contentEquals(partyName) && rankTwoIndex != -1)
	     		{
	     			if(rankTwoPartyName.contentEquals("Labor/Country Labor"))
		     		{
		     		//	System.out.println(currentLine);
		     		//	System.out.println(search.getLine());
		     		//	System.out.println("BTL: " + BTL);
		     		//	System.out.println("Line changed: "  + lineChanged);
		     		//	System.out.println("Index: " + index + " vs Rank Two Index: " + rankTwoIndex);
		     		}
	     			rankTwoPartyName = "same";
	     			rankTwoIndex = -1;
	     			
	     			//System.out.println(partyName);
	     			//System.out.println(currentLine);
	     			//System.out.println("BTL: " + BTL);
	     			//System.out.println("Line changed: "  + lineChanged);
	     			//System.out.println("Index: " + index + " vs Rank Two Index: " + rankTwoIndex);
	     			//System.out.println("");
	     			//System.out.println("");
	     			//System.out.println("");
	     		//	System.out.println("Blank party name: " +currentLine);
	     	//		if(BTL)
	    // 			{
	     		//	System.out.println("current error line is BTL");
		     	//	 System.out.println(rankTwoIndex);
	   //  			}
	    		}
	    		
	     		
	     		
	     		
				
					
				//System.out.println("Post: " + partyName + " : " + currentLine);
				//System.out.println(BTL);
				//finds the rank 1 party name and adds the ballot to the correct party object
				for(int x = 0; x < partyArray.size(); x++)
				{
					if(partyArray.get(x).getPartyName().equals(partyName))
					{
							
							partyArray.get(x).addBallots(index, rankTwoIndex, rankTwoPartyName );
							String answer =search.supportWho();
							if(answer.contentEquals("coalition"))
							{
								partyArray.get(x).addCVote();
							}
							else if(answer.contentEquals("labor"))
							{
								//if(partyName.equals("Liberal"))
							//		{
						//			System.out.println("The error ballot was: " + BTL);
						//			System.out.println( currentLine);
					//				}
								partyArray.get(x).addLVote();
							}
							else if(answer.contentEquals("equal"))
							{
								partyArray.get(x).addESupport();
							}
							else
							{
							//	System.out.println("No key words found " + currentLine);
												
							}

					}
				}
			//	System.out.println("I got counted");
				totalLines++;
				currentLines++;
				if(totalLines % 250000 ==0)
				{
					System.out.println(totalLines + " lines have been read so far");
				}
			}
			in.close();
			System.out.println(currentLines + " lines were read in the file  " + fileList[i] );
			}
		}
		

	  /**
	   * @param none
	   * @return void
	   * @throws FileNotFoundException
	   * This method will read the Party array and print the name of teh parties and their sizes
	   */
	  public void printPartyArrayInfo()
	  {
		  for(Party temp: partyArray)
		  {
			  temp.printPartyName();
			  temp.printBallotSize();
			 // temp.printTotalBallots();
		  }
	  }
	  /**
	   * @param none
	   * @return ArrayList<Party>
	   * this method returns partyArray
	   */
	  public ArrayList<Party> getPartyArray()
	  {
		  return partyArray; 
	  }
	  
	  /**
	   * 
	   * @param x
	   * @return Party
	   * This method returns the Party from the parameter index
	   */
	  public Party getParty(int x)
	  {
		  return partyArray.get(x);
	  }
	  /**
	   * @param partyLoc
	   * @param ballotLoc
	   * @return String 
	   * This method returns the ballot from the specific party ballot array location
	   */
	  public Ballot getBallot(int partyLoc, int ballotLoc)
	  {
		  return partyArray.get(partyLoc).getBallotIndex(ballotLoc);
	  }
	  /**
	   * @param x
	   * @return int
	   * This method returns the number of ballots form the specific Party object
	   */
	  public int getBallotSize(int x)
	  {
		  return getParty(x).getBallotSize();
	  }



	  
	  /**
	   * @param none
	   * @return void
	   * This method will print the spectrum vals of every Party object in the PartyArray
	   */
	  public void printSpectrumVals()
	  {
		  for(Party temp : partyArray)
		  {
			  temp.addSpectrumVal();
			  temp.printSpectrumVals();
		  }
			  
	  }
	  
	  public void totalSize()
	  {
		  int size = 0;
		  for(Party temp: partyArray)
		  {
			  if(temp.getBallotSize() > 0)
			  {
				  System.out.println(temp.getPartyName());
				  System.out.println(temp.getBallotSize());
			  }
			  size = temp.getBallotSize() + size;
		  }
		  System.out.println("Total ballots counted is " + size);
	  }
	  
	  public void fillRankTwo()
	  {
		  for(int x  = 0; x < partyArray.size();x++)
		  {
			  partyArray.get(x).fillRankTwo();
			  System.out.println("Finished sorting: " + partyArray.get(x).getPartyName());
		  }
	  }
	  



	  
	   

	  
	  
	  
	  public static void main(String[] args) throws FileNotFoundException
	  {
		  PartyArray test = new PartyArray();
		  test.createPartyArray();
		//  test.readPartyList();
		  test.printPartyArrayInfo();
		  test.printSpectrumVals();
		//  test.totalSize();
		//  test.printTotalBallots();
		 // test.printLiberal();
		  
	  }
	
	
	
	

}
