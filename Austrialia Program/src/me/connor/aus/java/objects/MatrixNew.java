package me.connor.aus.java.objects;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

import me.connor.aus.java.util.PartyArrayEdit;

/**
 * Edited version of the Matrix class made by Marc
 * 
 * @author cclark5
 *
 */
public class MatrixNew {
	private ArrayList<String> currentLine;
	private ArrayList<String> firstRow;
	private ArrayList<String> firstLetters;
	private PartyArrayEdit parties;
	private int counter;
	private boolean sameOrder;
	private boolean ifUG;
	private String stateName;
	private static final String[] alias = { "LIBERAL/THE NATIONALS", "Liberal & Nationals",
			"Liberal National Party of Queensland", "The Nationals", "Country Liberals (NT)", "Labor/Country Labor",
			"Australian Labor Party (Northern Territory) Branch", "The Greens (VIC)", "The Greens (WA)" };

	/**
	 * @param none
	 * @return void
	 * @throws IOException 
	 */
	public MatrixNew() throws IOException {
		currentLine = new ArrayList<String>();
		firstRow = new ArrayList<String>();
		firstLetters = new ArrayList<String>();
		parties = new PartyArrayEdit();
		parties.createPartyArray();
		parties.fillRankTwoAlias();
		counter = 0;
		sameOrder = false;
		ifUG = false;
		stateName = "";

	}

	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException This method will go through every ATL vote and
	 *                               find its second rank vote
	 */

	public void makeMatrix() throws FileNotFoundException {
		// creates an arrayList with every party name in it
		ArrayList<String> partyOrderList = new ArrayList<String>();
		Scanner scan = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		while (scan.hasNextLine()) {
			String party = scan.nextLine();
			boolean add = true;
			for (String alai : alias) {
				if (party.contentEquals(alai)) {
					add = false;
				}
			}
			if (add) {
				partyOrderList.add(party);
			}
		}
		// creates a 2D array for the matrix that has 2 more rows and columns then the
		// newly created partyOrderList ArrayList
		String[][] rankTwoMatrix = new String[partyOrderList.size() + 2][partyOrderList.size() + 2];
		for (int x = 1; x < partyOrderList.size(); x++) {
			rankTwoMatrix[x][0] = partyOrderList.get(x);
			rankTwoMatrix[0][x] = partyOrderList.get(x);
		}
		// Append party array 
		for (int i = 0; i < parties.getPartyArray().size(); i++) {
			for (String alai : alias) {
				if (parties.getPartyArray().get(i).getPartyName().contentEquals(alai)) {
					parties.getPartyArray().remove(i);
					i--;
					break;
				}
			}
		}
		 //System.out.println(parties.getPartyArray().size());
		 //System.out.println(partyOrderList.size());
		// will go through every party in the PartyArray
		for (int partyIndex = 0; partyIndex < parties.getPartyArray().size(); partyIndex++) {
			// will loop through every ballot for a party in the PartyArray
			for (int ballotIndex = 0; ballotIndex < parties.getParty(partyIndex).getRankTwoLength(); ballotIndex++) {
				System.out.println(parties.getParty(partyIndex).getRankTwoLength());
				String temp = parties.getParty(partyIndex).getRankTwoIndex(ballotIndex) + "";
				rankTwoMatrix[partyIndex + 1][ballotIndex + 1] = temp;
			}
		}
		printMatix(rankTwoMatrix);

	}

	/**
	 * @param rankTwoMatrix
	 * @return void This method will print the matrixZ
	 */
	public void printMatix(String[][] rankTwoMatrix) {
		for (String[] temp : rankTwoMatrix) {
			for (String x : temp) {
				System.out.print(x + ",");
				System.out.print("\t");
			}
			System.out.println("");
		}
	}

}
