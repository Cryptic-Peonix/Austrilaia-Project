package me.connor.aus.java.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Helper class used for party -> index and index -> party translation
 * @author cclark5
 *
 */
public class PartyHelper {

	private ArrayList<String> list;

	private Scanner pList;
	private int length = 0;
	private static final String[] ALIAS = { "LIBERAL/THE NATIONALS", "Liberal & Nationals",
			"Liberal National Party of Queensland", "The Nationals", "Country Liberals (NT)", "Labor/Country Labor",
			"Australian Labor Party (Northern Territory) Branch", "The Greens (VIC)", "The Greens (WA)" };

	public PartyHelper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Fetches a party named based on the index entered. Automatically builds a party list, so you don't have to create one beforehand.
	 * @param partyIndex party index entered
	 * @return the name of the party requested as a string
	 * @throws FileNotFoundException
	 */
	public String convertIndexToName(int partyIndex) throws FileNotFoundException {
		String name = "";
		createPartyList();
		removeAliases();
		name = getPartyName(partyIndex);
		return name;
	}

	/**
	 * Creates an array list of all 52 Political Parties from the 2016 election
	 * @throws FileNotFoundException
	 */
	public void createPartyList() throws FileNotFoundException {
		pList = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		list = new ArrayList<String>();
		while (pList.hasNextLine()) {
			String temp = pList.nextLine();
			list.add(temp);
			length++;
		}
		String temp = list.get(0).substring(3);
		list.set(0, temp);
	}
	
	/**
	 * Removes any aliases from the party list
	 */
	public void removeAliases() {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < ALIAS.length; j++) {
				if (list.get(i).contentEquals(ALIAS[j])) {
					list.remove(i);
					length--;
					i--;
				}
			}
		}
	}
	
	/**
	 * gets the list length
	 * @return The length of the party list
	 */
	public int getListLength() {
		return length;
	}

	/**
	 * Fetches the party name requested, used within this class
	 * @param index index requested
	 * @return
	 */
	public String getPartyName(int index) {
		return list.get(index);
	}

	/**
	 * Prints an unformatted version of the party list
	 */
	public void printList() {
		System.out.println(list);
	}

	/**
	 * Prints a formatted version of the party list line-by-line with indexes included
	 */
	public void printFormattedList() {
		System.out.println("Parties: ");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + ": " + list.get(i));
		}
	}
}
