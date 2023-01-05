package australianPoliticalAffinity;
/**
 * This class will calculate and print the spectrum values of all parties. 
 * @author Marc D'Avanzo
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SpectrumSort {
	private int coalitionTotal;
	private int laborTotal;
	private PartyArray parties;
	private String partyName;
	private int partyIndex; 
	private String[] coalitionParties = {"LIBERAL/THE NATIONALS", "Liberal & Nationals" , "Liberal Democrats",
				"Liberal National Party of Queensland", "Liberal", "The Nationals", "Country Liberals (NT)"};
	private String[] laborParties = {"Australian Labor Party", "Labor/Country Labor", "Australian Labor Party (Northern Territory) Branch"};
	private int[] coalitionIndex;
	private int[] laborIndex;
	private Scanner scanFirstRow;
	private ArrayList<String> firstRow;
	private ArrayList<String> order;
	private ArrayList<String> currentLine;
	private ArrayList<String> firstLetters;
	private ArrayList<String> list;
	private Boolean freshOrder;
	private int iterations; 

	
	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException
	 * this constructor will set all the instance variables to their default values
	 */
	public SpectrumSort() throws FileNotFoundException
	{
		coalitionTotal = 0;
		laborTotal = 0; 
		parties = new PartyArray();
		partyName = "";
		partyIndex = -1;	
		coalitionIndex = new int[coalitionParties.length];
		laborIndex = new int[laborParties.length];
		scanFirstRow = new Scanner(new FileInputStream("FirstRow.csv"));
		firstRow = new ArrayList<String>();
		 order = new ArrayList<String>();
		 currentLine = new ArrayList<String>();
		 firstLetters = new ArrayList<String>();
		 list = new ArrayList<String>();
		 freshOrder = true;
		 iterations = 0;
		 //checks for any BOMs (invisible characters)
		while(scanFirstRow.hasNextLine())
		{
			String removeBom = scanFirstRow.nextLine();
			if(removeBom.charAt(0) == 65279)
			{
				//System.out.println("BOM");
				removeBom = removeBom.substring(1);
			}
			firstRow.add(removeBom);
		}
		
		}
     /**
      * @param none
      * @return void
      * @throws FileNotFoundException
      * This method creates a partyArray
      */
     public void runPartyArray() throws FileNotFoundException
     {
    	 parties.createPartyArray();
    	// parties.readPartyList();
    	 parties.printPartyArrayInfo();
     }
     /**
      * @param stateID
      * @return ArrayList<String>
      * This method will match the correct ballot order the correct csv file
      */
     public void rowOne(String stateID)
     {
  
    	// if this is not the first run through
    	if(order.size()>0)
    	{
    		//if the order is still valid do not change order
    		if(!stateID.equals(order.get(0)))
    		{
    			freshOrder = false;
    		}
    		// if order is no longer valid find the correct order
    		else 
    		{
    			order.clear();
    			list.clear();
    			freshOrder = true;
    			for(int x = 0; x < firstRow.size(); x = x+ 2)
    			{
    				if(firstRow.get(x).charAt(0) < 6000 && stateID.charAt(0) < 6000)
    				{
    					if(firstRow.get(x).equals(stateID))
    					{
    						StringTokenizer temp = new StringTokenizer(firstRow.get(x+1),",",false);
    						while(temp.hasMoreTokens())
    						{
    							order.add(temp.nextToken());
    							list.add(order.get(order.size()-1));
    						}	 	 
    					}
    				}
    				else
    				{	
    					System.out.println("BOM was found");
    				}
    	 
    			}
    		}
    	 
    	
    	}
    	//finds the correct order for the current ballot
    	else
    	{
    		freshOrder =true;
    		for(int x = 0; x < firstRow.size(); x = x+ 2)
    		{
    			if(firstRow.get(x).charAt(0) < 6000 && stateID.charAt(0) < 6000)
    			{
    				if(firstRow.get(x).equals(stateID))
    				{
    					StringTokenizer temp = new StringTokenizer(firstRow.get(x+1),",",false);
    					while(temp.hasMoreTokens())
    					{
    						order.add(temp.nextToken());
							list.add(order.get(order.size()-1));
    					}	 	 
    				}
    			}
    			else
    			{	
    				System.out.println("BOM was found");
    			}
    	 }
    		
    	}
     }
     /**
      * @param none
      * @return void
      * This method will find the indexes of the coalition and labor parties for the current order
      */
     public void getMajorPartyIndex()
     {
    	 //if order has beem changed tehn find new index values
    	 if(freshOrder)
    	 {
    //		 System.out.println("FreshOrder");
    		 firstLetters.clear();
    		 //with a new order, change the firstLetters arrayList to match new order
    		 for(int x = 6; x < list.size(); x++)
    		 {
    			 // adds the first letters to a new Array 
    			 String firstLetter = list.get(x).substring(0,list.get(x).indexOf(":")+1);
    			 firstLetters.add(firstLetter);
    			 //removes the first letters from the firstRow array
    			 list.set(x, list.get(x).substring(list.get(x).indexOf(":")+1));
    		 }
    	 //resets the coaltionIndex[]
    	 for(int x = 0; x < coalitionIndex.length; x++)
    	 {
    		 coalitionIndex[x] = 0;
    	 }
    	 //resets the laborIndex[]
    	 for(int x = 0; x < laborIndex.length; x++)
    	 {
    		 laborIndex[x] = 0;
    	 }
    	 for(int x = 0; x < list.size(); x++)
    	 {
    		 //checks for the invisible character
    		 if(list.get(x).charAt(0) > 6000)
    		 {
    			 list.set(x, list.get(x).substring(1));
    			 System.out.println("Found a BOM");
    		 }
    		 //checks if the current party is a coalition party
    		 for(int cIndex = 0; cIndex < coalitionParties.length; cIndex++)
    		 {
 			//	System.out.println( order.get(x) + " vs " + coalitionParties[cIndex]);

    			 if(list.get(x).equals(coalitionParties[cIndex]))
    			 {
    			//	System.out.println("Match: " + order.get(x) + " equals " + coalitionParties[cIndex]);
    				coalitionIndex[cIndex] = x; 
    			 }
    		 }
    		 //checks that the current party is a labor party
    		 for(int lIndex = 0; lIndex < laborIndex.length; lIndex++)
    		 {
  			//	System.out.println(order.get(x) + " vs " + laborParties[lIndex]);

    			 if(list.get(x).equals(laborParties[lIndex]))
    			 {
     			//	System.out.println("Match: " + order.get(x) + " equals " + laborParties[lIndex]);
    				laborIndex[lIndex] = x; 
    			 }
    		 }
    	 } 
    	 for(int x = 0; x < coalitionParties.length;x++)
    	 {
    		 if(coalitionIndex[x] ==0)
    		 {
    			 coalitionIndex[x] = -1;
    		 }
    	 }
    	 for(int x = 0; x < laborIndex.length;x++)
    	 {
    		 if(laborIndex[x] ==0)
    		 {
    			 laborIndex[x] = -1;
    		 }
    	 }
    	 
     /** 	
    	 for(int temp: coalitionIndex)
    	 {
    		 System.out.println("CoalitionIndex: " + temp);
    		 if(temp != -1) {
    		 System.out.println(order.get(temp));
    		 }
    	 }
    	 System.out.println("");
    	 System.out.println("");
    	 for(int temp: laborIndex)
    	 {
    		 System.out.println("laborIndex: " + temp);
    		 if(temp != -1) {
    		 System.out.println(order.get(temp));
    		 }
    	 }
             **/    	 
    	 }
    	 
    	 
     }
     /**
      * @param partyName
      * @return void
      * This method will find the party's index. 
      */
     public void findPartyIndex(String partyName)
     {

    		this.partyName = partyName;
    			for(int x = 6; x < list.size(); x++)
       	 		{
    				if(list.get(x).contentEquals(partyName))
    				{
    				
    					partyIndex = x;
    				    break;
    				}
       	 	}
    		
       	 	
    		
    	 
    	}
    	 
     
     /**
      * @param none
      * @return void
      * This method will go go through every ballot in the party array to calculate its spectrum value
      */
     public void sortThrough()
     {
    	 //will go through every party in the party array
    	 for(int partiesIndex = 0; partiesIndex < parties.getPartyArray().size(); partiesIndex++)
    	 {
    		 parties.getParty(partiesIndex).printSpectrumVals();
    		 //will go through every rank one ballot for a party
    		 //for(int ballotIndex  = 0; ballotIndex < parties.getBallotSize(partiesIndex); ballotIndex++ )
    		// {
    	 }
     }
    /**
     * @param none
     * @return void
     * This method will print all the spectrumVals of every party	
     */
     public void printSpectrum()
     {
    	 
    	 for(Party party: parties.getPartyArray())
    	 {
    		 party.addSpectrumVal();
    	 }
    	 parties.printSpectrumVals();
     }
    
 
     
     public static void main(String[] args) throws FileNotFoundException
     {
    	 SpectrumSort test = new SpectrumSort();
    	 test.runPartyArray();
    	 test.sortThrough();
    	 test.printSpectrum();
    	 //test.printLaborAndLiberal();
     }
     
	
	
		
		
	
	
	
}


