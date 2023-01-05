package australianPoliticalAffinity;
/**
 * This class will construct an adjacency matrix.
 * the first row and column have every party listed. the parties on teh row represent parties that were ranked frist 
 * the columns represent parties that were ranked 2 
 * 
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Matrix {
	private ArrayList<String> currentLine;
	private ArrayList<String> firstRow;
	private ArrayList<String> firstLetters;
	private PartyArray parties;
	private int counter;
	private boolean sameOrder;
	private boolean ifUG;
	private String stateName;
	
	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException
	 * This constructor sets all instance variables to have default values. 
	 */
	public Matrix() throws FileNotFoundException
	{
		currentLine = new ArrayList<String>();
		firstRow = new ArrayList<String>();
		firstLetters = new ArrayList<String>();
		parties = new PartyArray();
		parties.createPartyArray();
		parties.fillRankTwo();
		counter =0;
		sameOrder = false;
		ifUG = false;
		stateName = "";

	}
	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException
	 * This method will go through every ATL vote and find its second rank vote
	 */
	
	public void makeMatrix() throws FileNotFoundException
	{
		// creates an arrayList with every party name in it
		ArrayList<String> partyOrderList = new ArrayList<String>();
		Scanner scan = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		while(scan.hasNextLine())
		{
			partyOrderList.add(scan.nextLine());
		}
		//creates a 2D array for the matrix that has 2 more rows and columns then the newly created partyOrderList ArrayList
		String[][] rankTwoMatrix = new String[partyOrderList.size()+2][partyOrderList.size()+2];
		for(int x = 1; x < partyOrderList.size();x++)
		{
			rankTwoMatrix[x][0] = partyOrderList.get(x);
			rankTwoMatrix[0][x] = partyOrderList.get(x);
		}
		// will go through every party in the PartyArray
				for(int partyIndex = 0; partyIndex < parties.getPartyArray().size(); partyIndex++)
				{
					// will loop through every ballot for a party in the PartyArray
					for(int ballotIndex = 0; ballotIndex < parties.getParty(partyIndex).getRankTwoLength(); ballotIndex++)
					{
						String temp = parties.getParty(partyIndex).getRankTwoIndex(ballotIndex) +"";
						rankTwoMatrix[partyIndex+1][ballotIndex+1] = temp;
					}
				}
		printMatix(rankTwoMatrix);
		
	}
	/**
	 * @param rankTwoMatrix
	 * @return void
	 * This method will print the matrix
	 */
	public void printMatix(String[][] rankTwoMatrix)
	{
		for(String[] temp: rankTwoMatrix)
		{
			for(String x: temp)
			{
				System.out.print(x + ",");
				System.out.print("\t");
			}
			System.out.println("");
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Matrix test = new Matrix();
		test.makeMatrix();
				
	}
	
	
	

}
