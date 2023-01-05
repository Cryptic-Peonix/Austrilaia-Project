package me.connor.aus.java.util;

import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.StringTokenizer;

import australianPoliticalAffinity.Ballot;
import australianPoliticalAffinity.Party;
import me.connor.aus.java.objects.ManualParty;

public class PartyArrayEdit {
	private Scanner scan;
	private ArrayList<ManualParty> partyArray;
	private int upperBoundIndex;
	private int lowerBoundIndex;
	private Hashtable<String, Integer> partyIndexies;
	private String[] fileList;
	private Scanner orderList;
	private Scanner in;
	private ArrayList<String> partyOrder;
	private ArrayList<String> firstLine;
	private String currentLine;
	private String partyName;
	private int index;
	private NewSearcher search;
	private boolean BTL;
	public static final String[] alias = { "LIBERAL/THE NATIONALS", "Liberal & Nationals",
			"Liberal National Party of Queensland", "The Nationals", "Country Liberals (NT)", "Labor/Country Labor",
			"Australian Labor Party (Northern Territory) Branch", "The Greens (VIC)", "The Greens (WA)" };
	private PartyHelper helper;

	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException This constructor assigns basic values to all
	 *                               the instance variables
	 */
	public PartyArrayEdit(int uBound, int lBound) throws FileNotFoundException {
		upperBoundIndex = uBound;
		lowerBoundIndex = lBound;
		scan = new Scanner(System.in);
		partyArray = new ArrayList<ManualParty>();
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
		search = new NewSearcher();
		BTL = false;
		partyIndexies = new Hashtable<String, Integer>();
		helper = new PartyHelper();
		while (orderList.hasNextLine()) {
			String temp = orderList.nextLine();
			if (temp.charAt(0) > 6000) {
				System.out.println("found a BOM in orderComplete");
				temp = temp.substring(1);
			}
			partyOrder.add(temp);
		}
		// creates a array of Party objects and assigns each one one of the parties
		// names
		Scanner partyList = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		boolean lineOneFix = false;
		while (partyList.hasNextLine()) {
			String temp = partyList.nextLine();
			if (temp.charAt(0) > 6000) {
				temp = temp.substring(1);
			}
			if (!lineOneFix) {
				temp = temp.substring(3);
				lineOneFix = true;
			}
			ManualParty party = new ManualParty(temp, lowerBoundIndex, upperBoundIndex);
			partyArray.add(party);
		}
	}

	@Deprecated
	public PartyArrayEdit() throws FileNotFoundException {
		scan = new Scanner(System.in);
		partyArray = new ArrayList<ManualParty>();
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
		search = new NewSearcher();
		BTL = false;
		helper = new PartyHelper();
		while (orderList.hasNextLine()) {
			String temp = orderList.nextLine();
			if (temp.charAt(0) > 6000) {
				System.out.println("found a BOM in orderComplete");
				temp = temp.substring(1);
			}
			partyOrder.add(temp);
		}
		// creates a array of Party objects and assigns each one one of the parties
		// names
		Scanner partyList = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		boolean lineOneFix = false;
		while (partyList.hasNextLine()) {
			String temp = partyList.nextLine();
			if (temp.charAt(0) > 6000) {
				temp = temp.substring(1);
			}
			if (!lineOneFix) {
				temp = temp.substring(3);
				lineOneFix = true;
			}
			ManualParty party = new ManualParty(temp);
			partyArray.add(party);
		}
	}

	/**
	 * @param none
	 * @return void
	 * @throws IOException 
	 */
	public void createPartyArray() throws IOException {
		// goes through every line in the csv files
		int totalLines = 0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		for (int i = 0; i < fileList.length; i++) {
			int currentLines = 0;
			System.err.println("Now Reading: " + fileList[i]);
			try {
				in = new Scanner(new FileInputStream("data\\" + fileList[i]));
			} catch (FileNotFoundException e) {
				continue;
			}
			// takes the first line of a csv file that contians the party order and saves it
			// to the firstLine ArrayList
			currentLine = in.nextLine();
			firstLine.clear();
			StringTokenizer st = new StringTokenizer(currentLine, ",", false);
			// adds the first line of a csv file to a arrayList as to remember the party
			// order
			while (st.hasMoreTokens()) {
				firstLine.add(st.nextToken());
			}
			// creates an ArrayList of just the letter ID of the parties
			ArrayList<String> firstLetters = new ArrayList<String>();
			for (int x = 6; x < firstLine.size(); x++) {
				firstLetters.add(firstLine.get(x).substring(0, firstLine.get(x).indexOf(":") + 1));
			}

			// will check every line in the current csv file
			search.checkOrder(currentLine);
			totalLines++;
			// currentLines++;
			// System.out.println("This should send once/while start");
			while (in.hasNextLine()) {
				// replaces all the blank spaces on the current ballot with -1
				currentLine = in.nextLine();
				// System.out.println("pre: " + currentLine);
				currentLine = currentLine.replace(",,", ",-1,");
				// System.out.println("One time: " + currentLine);
				currentLine = currentLine.replace(",,", ",-1,");
				// System.out.println("Two times: " + currentLine);
				// System.out.println("first index call: " + index);
				if (search.searchBallot(currentLine)) {
					index = search.BTLIndex();
					// System.out.println("index after BTLIndex(): " + index);
					// System.out.println("Mew index is: " + index);
					BTL = true;
				} else {
					// System.out.println("index before getIndex(): " + index);
					index = search.getIndex();
					// System.out.println("index after getIndex(): " + index);
					BTL = false;
				}
				int rankTwoIndex = search.getRankTwoIndex();
				if (search.getChanged()) {
				}
				if (!BTL) {
					// System.out.println("index: " + index);
					partyName = firstLine.get(index).substring(firstLine.get(index).indexOf(":") + 1);

					// System.out.println("1" + partyName);
				} else {
					if (index != -500) {
						partyName = firstLine.get(index).substring(firstLine.get(index).indexOf(":") + 1);
					}

					// UG have no party thus are always below the line
					else {
						// System.out.println("Changed to UG");
						partyName = "UG";
						// BTL = true;
					}

				}
				String rankTwoPartyName = "";
				if (rankTwoIndex == -500) {
					rankTwoPartyName = "UG";
				} else if (rankTwoIndex != -1) {
					rankTwoPartyName = firstLine.get(rankTwoIndex).substring(0,
							firstLine.get(rankTwoIndex).indexOf(":") + 1);
					rankTwoPartyName = search.findRankTwoPartyName(rankTwoPartyName);
					// System.out.println("2" + rankTwoPartyName);
				} else {
					rankTwoPartyName = "NA";
				}

				// This shit works fine.
				for (int c = 0; c < alias.length; c++) {
					if (partyName.contentEquals(alias[c])) {
						if (c <= 4) {
							partyName = "Liberal";
						} else if (c > 4 && c <= 6) {
							partyName = "Australian Labor Party";
						} else {
							partyName = "The Greens";
						}
						break;
					}
				}

				if (rankTwoPartyName.contentEquals(partyName) && rankTwoIndex != -1) {
					if (rankTwoPartyName.contentEquals("Labor/Country Labor")) {
						// System.out.println(currentLine);
						// System.out.println(search.getLine());
						// System.out.println("BTL: " + BTL);
						// System.out.println("Line changed: " + lineChanged);
						// System.out.println("Index: " + index + " vs Rank Two Index: " +
						// rankTwoIndex);
					}
					rankTwoPartyName = "same";
					rankTwoIndex = -1;

					// System.out.println(partyName);
					// System.out.println(currentLine);
					// System.out.println("BTL: " + BTL);
					// System.out.println("Line changed: " + lineChanged);
					// System.out.println("Index: " + index + " vs Rank Two Index: " +
					// rankTwoIndex);
					// System.out.println("");
					// System.out.println("");
					// System.out.println("");
					// System.out.println("Blank party name: " +currentLine);
					// if(BTL)
					// {
					// System.out.println("current error line is BTL");
					// System.out.println(rankTwoIndex);
					// }
				}

				// System.out.println("Post: " + partyName + " : " + currentLine);
				// System.out.println(BTL);
				// finds the rank 1 party name and adds the ballot to the correct party object
				for (int x = 0; x < partyArray.size(); x++) {
					boolean foundAndAdded = false;
					if (partyArray.get(x).getPartyName().equals(partyName)) {
						partyArray.get(x).addBallots(index, rankTwoIndex, rankTwoPartyName);
						String answer = search.supportWho(upperBoundIndex, lowerBoundIndex);
						if (answer.contentEquals("upper")) {
							partyArray.get(x).addUpperVote();
						} else if (answer.contentEquals("lower")) {
							// if(partyName.equals("Liberal"))
							// {
							// System.out.println("The error ballot was: " + BTL);
							// System.out.println( currentLine);
							// }
							partyArray.get(x).addLowerVote();
						} else if (answer.contentEquals("equal")) {
							partyArray.get(x).addESupport();
						} else {
							// System.out.println("No key words found " + currentLine);

						}
						foundAndAdded = true;
					}
					if (foundAndAdded) {
						break;
					}
				}
				// System.out.println("I got counted");
				totalLines++;
				// currentLines++;
				if (totalLines % 5000 == 0) {
					System.err.println(totalLines + " lines have been read so far. Time: " + dtf.format(LocalDateTime.now()));
					//System.out.println(dtf.format(LocalDateTime.now()));
	
				}
				// System.out.println("end" + "\n");
			}
			// System.out.println("while end");
			in.close();
			// System.err.println(currentLines + " lines were read in the file  " + fileList[i]);
		}
	}

	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException This method will read the Party array and print
	 *                               the name of teh parties and their sizes
	 */
	public void printPartyArrayInfo() {
		for (Party temp : partyArray) {
			temp.printPartyName();
			temp.printBallotSize();
			// temp.printTotalBallots();
		}
	}

	/**
	 * @param none
	 * @return ArrayList<Party> this method returns partyArray
	 */
	public ArrayList<ManualParty> getPartyArray() {
		return partyArray;
	}

	/**
	 * 
	 * @param x
	 * @return Party This method returns the Party from the parameter index
	 */
	public ManualParty getParty(int x) {
		return partyArray.get(x);
	}

	/**
	 * @param partyLoc
	 * @param ballotLoc
	 * @return String This method returns the ballot from the specific party ballot
	 *         array location
	 */
	public Ballot getBallot(int partyLoc, int ballotLoc) {
		return partyArray.get(partyLoc).getBallotIndex(ballotLoc);
	}

	/**
	 * @param x
	 * @return int This method returns the number of ballots form the specific Party
	 *         object
	 */
	public int getBallotSize(int x) {
		return getParty(x).getBallotSize();
	}
	
	/**
	 * @param none
	 * @return void This method will print the spectrum vals of every Party object
	 *         in the PartyArray
	 * @throws IOException 
	 */
	public void printSpectrumVals(double dev, double altDev) throws IOException {
		String[] aliases = { "LIBERAL/THE NATIONALS", "Liberal & Nationals", "Liberal National Party of Queensland",
				"The Nationals", "Country Liberals (NT)", "Labor/Country Labor",
				"Australian Labor Party (Northern Territory) Branch", "The Greens (VIC)", "The Greens (WA)" };
		double specVal = 0;
		double bestSpecVal = -1;
		String bestSpecValParty = "";
		double lowestSpecVal = -1;
		String lowestSpecValParty = "";
		File file = new File("output\\" + String.format("%02d-%02d.txt", lowerBoundIndex, upperBoundIndex));
		PrintWriter out = new PrintWriter(new FileWriter(file));
		out.println("Lower bound: " + helper.convertIndexToName(lowerBoundIndex) + ", Upper Bound: " + helper.convertIndexToName(upperBoundIndex));
		try {
			if (file.createNewFile()) {
				// System.out.println("file made");
			} else {
				// System.err.println("file already exists");
			}
		} catch (IOException e) {
		}
		for (ManualParty temp : partyArray) {
			boolean isAlias = false;
			for (int i = 0; i < aliases.length; i++) {
				if (temp.getPartyName().contentEquals(aliases[i])) {
					isAlias = true;
					break;
				}
			}
			if (!isAlias) {
				temp.addSpectrumVal();
				// highest and lowest spectrum value calculations
				specVal = temp.getSpectrumVals();
				if (bestSpecVal == -1 && lowestSpecVal == -1) {
					if (!Double.isNaN(specVal) && specVal != 1 && specVal != 0) {
						bestSpecVal = specVal;
						bestSpecValParty = temp.getPartyName();
						lowestSpecVal = specVal;
						lowestSpecValParty = temp.getPartyName();
					}
				}
				if (specVal > bestSpecVal && specVal != 1 && specVal != 0) {
					bestSpecVal = specVal;
					bestSpecValParty = temp.getPartyName();
				}
				if (specVal < lowestSpecVal && specVal != 1 && specVal != 0) {
					lowestSpecVal = specVal;
					lowestSpecValParty = temp.getPartyName();
				}
				// temp.printSpectrumVals();
				temp.printSpectrumToFile(file, out);
			}
		}
		// Print all of the shit
		out.println("\n" + "Standard Deviation: ");
		out.printf("%.4f\n", dev);
		out.println("\n" + "Non-extreme Standard Deviation: ");
		out.printf("%.4f\n", altDev);
		out.println("\n" + "Largest Spectrum Value: " + bestSpecValParty);
		out.printf("%.4f\n", bestSpecVal);
		out.println("\n" + "Lowest Spectrum Value: " + lowestSpecValParty);
		out.printf("%.4f\n", lowestSpecVal);
		out.close();
	}

	public void totalSize() {
		int size = 0;
		for (Party temp : partyArray) {
			if (temp.getBallotSize() > 0) {
				System.out.println(temp.getPartyName());
				System.out.println(temp.getBallotSize());
			}
			size = temp.getBallotSize() + size;
		}
		System.out.println("Total ballots counted is " + size);
	}

	public void fillRankTwo() {
		for (int x = 0; x < partyArray.size(); x++) {
			partyArray.get(x).fillRankTwo();
			// System.out.println("Finished sorting: " + partyArray.get(x).getPartyName());
		}
	}

	/**
	 * Prints a formatted version of the entire party array
	 */
	public void printFormattedList() {
		System.out.println("Parties: ");
		for (int i = 0; i < partyArray.size(); i++) {
			System.out.println(i + ": " + partyArray.get(i).getPartyName());
		}
	}

	public static void main(String[] args) throws IOException {
		PartyArrayEdit test = new PartyArrayEdit();
		test.createPartyArray();
		// test.readPartyList();
		test.printPartyArrayInfo();
		test.printSpectrumVals(0, 0);
		// test.totalSize();
		// test.printTotalBallots();
		// test.printLiberal();

	}

	public void fillRankTwoAlias() {
		for (int x = 0; x < partyArray.size(); x++) {
			boolean fill = true;
			for (String alai : alias) {
				if (partyArray.get(x).getPartyName().contentEquals(alai)) {
					fill = false;
					break;
				}
			}
			if (fill) {
				partyArray.get(x).fillRankTwo();
				System.out.println("Finished sorting: " + partyArray.get(x).getPartyName());
			}
		}
	}

}
