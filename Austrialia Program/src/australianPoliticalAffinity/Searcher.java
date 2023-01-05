/**
 * 
 * This method will search every csv file line by line and determine which party was ranked 1, rank 2 
 * @author Marc D'Avanzo
 */
package australianPoliticalAffinity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Searcher {
	private File f;
	private String[] fileList;
	private Scanner orderList;
	private Scanner in;
	private ArrayList<String> partyOrder;
	protected ArrayList<String> firstLine;
	protected String[] line;
	// private String currentLine;
	private String partyName;
	protected int index;
	protected int rankTwoIndex;
	protected boolean BTL;
	protected boolean changed;
	protected int lastIndex;
	protected ArrayList<String> firstLetters;
	private String[] coalitionParties = { "LIBERAL/THE NATIONALS", "Liberal & Nationals", "Liberal Democrats",
			"Liberal National Party of Queensland", "Liberal", "The Nationals", "Country Liberals (NT)" };
	private String[] laborParties = { "Australian Labor Party", "Labor/Country Labor",
			"Australian Labor Party (Northern Territory) Branch" };
	protected int[] coalitionIndex;
	protected int[] laborIndex;

	/**
	 * @param none
	 * @return void
	 * @throws FileNotFoundException basic constructor that assigns basic values to
	 *                               all instance variables.
	 * 
	 */
	public Searcher() throws FileNotFoundException {
		// f = new File("AlphabeticalData");
		// fileList = f.list();
		// Arrays.sort(fileList);
		orderList = new Scanner(new FileInputStream("AlphabeticalOrderComplete.csv"));
		in = null;
		partyOrder = new ArrayList<String>();
		firstLine = new ArrayList<String>();
		index = -1;
		rankTwoIndex = -1;
		lastIndex = -1;
//		currentLine = "";
		partyName = "";
		BTL = true;
		changed = false;
		firstLetters = new ArrayList<String>();
		while (orderList.hasNextLine()) {
			partyOrder.add(orderList.nextLine());
		}

	}

	/**
	 * @param currentLine
	 * @return void This method will check the first line of a csv file and find the
	 *         order of parties and remove any BOMs
	 */
	public void checkOrder(String currentLine) {
		coalitionIndex = new int[coalitionParties.length];
		laborIndex = new int[laborParties.length];
		firstLine.clear();
		firstLetters.clear();
		StringTokenizer st = new StringTokenizer(currentLine, ",", false);
		// takes the first line and puts it into an ArrayList
		while (st.hasMoreTokens()) {
			firstLine.add(st.nextToken());
		}
		// will check the firstLine ArrayList for any BOMs
		for (int x = 0; x < firstLine.size(); x++) {
			if (firstLine.get(x).charAt(0) > 6000) {
				System.out.println("Found a BOM in first Line");
				firstLine.set(x, firstLine.get(x).substring(1));
			}
		}
		for (int x = 7; x < firstLine.size(); x++) {
			// System.out.println(firstLine.get(x).substring(0,firstLine.get(x).indexOf(":")));
			firstLetters.add(firstLine.get(x).substring(0, firstLine.get(x).indexOf(":") + 1));
		}

		for (int x = 6; x < firstLine.size(); x++) {
			for (int c = 0; c < coalitionIndex.length; c++) {
				String temp = firstLine.get(x).substring(firstLine.get(x).indexOf(":") + 1);
				if (coalitionParties[c].contentEquals(temp)) {
					// System.out.println("Coalition index: " + x);
					// System.out.println(temp);
					coalitionIndex[c] = x;
				}
			}
			for (int l = 0; l < laborIndex.length; l++) {
				String temp = firstLine.get(x).substring(firstLine.get(x).indexOf(":") + 1);
				if (laborParties[l].contentEquals(temp)) {
					// System.out.println("labor index: " + x);
					laborIndex[l] = x;
				}

			}
		}

	}

	/**
	 * @param currentLine
	 * @return boolean This method will search the ballot to see if it is above or
	 *         BTL and check for exceptions It will check for incorrect ranking, if
	 *         the ballot is valid above or BTL, and if both ranks are present if it
	 *         is above or BTL
	 */
	public boolean searchBallot(String currentLine) {
		System.out.println("Btl: " + BTL);
		rankTwoIndex = -1;
		index = -1;
		changed = false;
		BTL = true;
		System.out.println("Btl after seacrh set: " + BTL);
		line = new String[firstLine.size()];
		// takes the current line and places it into an ArrayList
		int lineIndex = 0;
		StringTokenizer st = new StringTokenizer(currentLine, ",", false);
		while (st.hasMoreTokens()) {
			String tempST = st.nextToken();
			if (tempST.equals("-1")) {
				line[lineIndex] = null;
			} else {
				line[lineIndex] = tempST;
			}
			lineIndex++;
		}

		// will check the BTL part of the ballot for rank 1 and if the ballot is valid.
		String s = Arrays.toString(line);
		System.out.println(s);
		int highestNum = -1;
		lastIndex = firstLetters.indexOf("A:") + 6;
		// System.out.println("");
		// will find the highest rank for BLT
		for (int x = lastIndex + 1; x < line.length; x++) {
			if (line[x] != null) {
				if (Integer.parseInt(line[x]) > highestNum) {
					highestNum = Integer.parseInt(line[x]);
				}
			}
		}
		// creates an arrayList to check the BTL part of the ballot for excpetions and
		// to see if the ballot is BTL
		System.out.println("highest num: " + highestNum);
		if (highestNum > 5) {
			// System.out.println(highestNum + " is the hight rank");
			// System.out.println("Last index is: " + lastIndex);
			// System.out.println("IS BTL");
			// System.out.println(currentLine);
			int indexTracker = 0;
			int[] checkBTL = new int[line.length - lastIndex];
			for (int x = lastIndex + 1; x < line.length; x++) {
				// System.out.println("line[" + x+"]: " + line[x]);
				if (line[x] != null) {
					checkBTL[indexTracker] = Integer.parseInt(line[x]);
				}
				indexTracker++;
			}
			// checks that there are at lest 6 ranks for BTL and no double rank 6s
			boolean BTLformal = true;
			int BTLcounter = 1;
			while (BTLcounter <= highestNum && BTLformal) {
				boolean numFound = false;
				// System.out.println("BTLcounter is: " + BTLcounter);
				for (int x = 0; x < checkBTL.length; x++) {
					if (checkBTL[x] == BTLcounter && !numFound) {
						if (BTLcounter == 1) {
							// System.out.println("x: " + x + " and lastIndex: " + lastIndex);
							index = x + lastIndex + 1;
						}
						if (BTLcounter == 2) {
							// System.out.println("found rank two");
							rankTwoIndex = x + lastIndex + 1;
						}
						numFound = true;
					} else if (checkBTL[x] == BTLcounter && numFound) {
						if (BTLcounter < 6) {
							BTL = false;
							index = -1;
							rankTwoIndex = -1;
						}
						BTLformal = false;
						for (int pos = lastIndex + 1; pos < line.length; pos++) {
							if (line[pos] != null) {
								if (Integer.parseInt(line[pos]) >= BTLcounter) {
									line[pos] = null;
								}
							}
						}
						changed = true;

					}

				}
				if (!numFound && BTLcounter > 6) {
					BTLformal = false;
					for (int pos = lastIndex + 1; pos < line.length; pos++) {
						if (line[pos] != null) {
							if (Integer.parseInt(line[pos]) >= BTLcounter) {
								line[pos] = null;
							}
						}
					}
					changed = true;
				} else if (!numFound && BTLcounter <= 6) {
					BTLformal = false;
					for (int pos = lastIndex + 1; pos < line.length; pos++) {
						line[pos] = null;
					}
					changed = true;
					BTL = false;
					index = -1;
					rankTwoIndex = -1;
				}
				if (numFound) {
					BTLcounter++;
				}

			}
			// System.out.println("BTL index is: " + index);
		} else {
			BTL = false;
			for (int pos = lastIndex + 1; pos < line.length; pos++) {
				line[pos] = null;
			}
			changed = true;

		}
		if (!BTL) {
			System.out.println("Ballot not BTL");
			// System.out.println("Should not have changed: " +lastIndex);
			int[] ATLchecker = new int[lastIndex - 5];
			int ATLcheckerIndex = 0;
			for (int x = 6; x < lastIndex + 1; x++) {
				// System.out.println("line[x] " +line[x]);
				if (line[x] != null) {
					ATLchecker[ATLcheckerIndex] = Integer.parseInt(line[x]);
				}
				ATLcheckerIndex++;
			}
			int ATLcounter = 1;
			boolean ATLformal = true;
			while (ATLformal) {
				boolean numFound = false;

				for (int x = 0; x < ATLchecker.length; x++) {
					// System.out.println(ATLcounter + " vs " + ATLchecker[x]);
					if (ATLcounter == ATLchecker[x] && !numFound) {
						// System.out.println("got here");
						if (ATLcounter == 1) {
							// System.out.println("Found rank 1");
							index = x + 6;
						}
						if (ATLcounter == 2) {
							// System.out.println("found rank 2");
							rankTwoIndex = x + 6;
						}
						numFound = true;
					} else if (ATLcounter == ATLchecker[x] && numFound) {
						ATLformal = false;
						for (int pos = 6; pos < lastIndex + 1; pos++) {
							if (line[pos] != null) {
								if (Integer.parseInt(line[pos]) >= ATLcounter) {
									line[pos] = null;
								}
							}
						}
						changed = true;
					}
				}
				if (!numFound) {

					ATLformal = false;
					for (int pos = 6; pos < lastIndex + 1; pos++) {
						if (line[pos] != null) {
							if (Integer.parseInt(line[pos]) >= ATLcounter) {
								line[pos] = null;
							}
						}
					}
					changed = true;
				} else {
					// System.out.println("ATL counter went up");
					ATLcounter++;
				}

			}

		} else {
			for (int x = 6; x < lastIndex + 1; x++) {
				line[x] = null;
			}
			changed = true;
		}

		// System.out.println(currentLine);
		// System.out.println("index is: " +index);
		System.out.println("Btl end: " + BTL);
		return BTL;

	}

	/**
	 * @param none
	 * @return int This method will return the party index from the rank 1 candidate
	 *         only applies for BTL ballots
	 */
	public int BTLIndex() {
		int BTLindex = -1;
		firstLetters.add(0, "A:");
		// System.out.println("got to BTL index");
		// System.out.println("First row: " + firstLine.get(index));
		String letterID = firstLetters.get(index - 6);
		// System.out.println("First Letters: " + letterID );
		if (!letterID.contentEquals("UG:")) {
			for (int x = 0; x < firstLetters.size(); x++) {
				// System.out.println("test: " + firstLine.get(x).substring(0,
				// firstLine.get(x).indexOf(":")+1));
				// System.out.println("temp is: "+ temp);
				// System.out.println(firstLetters.get(x) + " vs " + temp);
				if (firstLetters.get(x).equals(letterID)) {
					// System.out.println("Found the match");
					BTLindex = x + 6;
					// System.out.println("BTL equiv: " + firstLine.get(BTLindex));
					break;
				}
			}
		} else {
			BTLindex = -500;
		}
		// if(BTLindex == -1)
		// {
		// System.out.println("index val is: " + index);
		// for(String tempString: line)
		// {

		// System.out.print(tempString+",");
		// }
		// System.out.println("");
		// }
		firstLetters.remove(0);
		// System.out.println("BTL index is pre change to ATL: " + index);
		return BTLindex;
	}

	/**
	 * @param none
	 * @return int this method will return the rank one party index only for ATL
	 *         ballots
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param none
	 * @return int will return the index position of the rank 2 party
	 */
	public int getRankTwoIndex() {
		if (rankTwoIndex < line.length) {
			if (!BTL) {
				return rankTwoIndex;
			} else {
				//System.out.println("pre rank two index change:" + rankTwoIndex);
				BTLRankTwoIndex();
				return rankTwoIndex;
			}
		} else
			return -1;

	}

	/**
	 * @param none
	 * @return String This method will determine which major party was ranked the
	 *         highest
	 */
	public String supportWho() {
		firstLetters.add(0, "A:");
		String answer = "";
		int lowestCVal = 500;
		int lowestLVal = 500;
		if (!BTL) {
			for (int c = 0; c < coalitionIndex.length; c++) {
				// System.out.println(coalitionIndex[c]);
				// System.out.println(line.length + " length");
				if (coalitionIndex[c] < line.length && coalitionIndex[c] != 0) {
					if (line[coalitionIndex[c]] != null && Integer.parseInt(line[coalitionIndex[c]]) < lowestCVal) {
						lowestCVal = Integer.parseInt(line[coalitionIndex[c]]);
					}
				}
			}
			for (int l = 0; l < laborIndex.length; l++) {
				if (laborIndex[l] < line.length && laborIndex[l] != 0) {
					if (line[laborIndex[l]] != null && Integer.parseInt(line[laborIndex[l]]) < lowestLVal) {
						lowestLVal = Integer.parseInt(line[laborIndex[l]]);
					}
				}
			}
		} else {
			for (int c = 0; c < coalitionIndex.length; c++) {
				if (coalitionIndex[c] < line.length && coalitionIndex[c] != 0) {
					for (int x = lastIndex + 1; x < line.length; x++) {
						// System.out.println("test: " + firstLetters.get(x) + " vs " +
						// firstLetters.get(coalitionIndex[c]-6));
						if (firstLine.get(x).contains(firstLetters.get(coalitionIndex[c] - 6))) {
							if (line[x] != null) {
								// System.out.println("CVals: " + line[x]);
								int parsedInt = Integer.parseInt(line[x]);
								if (parsedInt != -1 && parsedInt < lowestCVal) {
									lowestCVal = parsedInt;
								}
							}
						}
					}
				}
			}
			// System.out.println("Rank one index is: " + index);
			for (int l = 0; l < laborIndex.length; l++) {
				if (laborIndex[l] < line.length && laborIndex[l] != 0) {
					for (int x = lastIndex + 1; x < line.length; x++) {
						// System.out.println("test: " + firstLetters.get(x) + " vs " +
						// firstLetters.get(laborIndex[l]-6));
						if (firstLine.get(x).contains(firstLetters.get(laborIndex[l] - 6))) {
							if (line[x] != null) {
								// System.out.println("LVals: " + line[x]);
								int parsedInt = Integer.parseInt(line[x]);
								if (parsedInt != -1 && parsedInt < lowestCVal) {
									lowestLVal = parsedInt;
								}
							}
						}
					}
				}
			}

		}
		if (line[6] != null && line[6].contentEquals("1") && lowestCVal > lowestLVal) {
			// System.out.println("You fucked up: c vs l:" + lowestCVal + " vs " +
			// lowestLVal);
			// for(String temp: line)
			// {
			// System.out.print(temp+",");

			// }
			// System.out.println("");
		}
		if (lowestCVal != 500 && lowestCVal != 0 && lowestCVal < lowestLVal) {
			answer = "coalition";
		} else if (lowestLVal != 500 && lowestLVal != 0 && lowestCVal > lowestLVal) {
			answer = "labor";
			if (line[6] != null && line[6].contentEquals("1")) {
				// System.out.println("You fucked up: c vs l:" + lowestCVal + " vs " +
				// lowestLVal);
				// for(String temp: line)
				// {
				// System.out.print(temp+",");

				// }
				// System.out.println("");
			}
		} else if (lowestCVal == lowestLVal) {
			answer = "equal";
		}
		firstLetters.remove(0);
		return answer;

	}

	/**
	 * @param none
	 * @return void This method will convert a rank two BTL index into a rank tow
	 *         ATL index
	 */
	public void BTLRankTwoIndex() {
		firstLetters.add(0, "A:");
		// System.out.println("BTLRank Two index: " + index + " vs " + rankTwoIndex);
		// System.out.println("index -6: " + firstLetters.get(index-6) + " vs " +
		// "rankTWo: " + firstLetters.get(rankTwoIndex-6));
		if (firstLetters.get(index - 6).equals(firstLetters.get(rankTwoIndex - 6))) {
			String letterID = firstLetters.get(index - 6);
			boolean left = true;
			boolean right = true;
			boolean foundNum = false;
			int nextTo = 1;
			int nextNum = 2;
			int savedL = -1;
			int savedR = -1;
			while (left || right) {
				foundNum = false;
				// if the index ID to the left matches the original ID then check to see if the
				// rank is next numerically
				// System.out.println("To the left");
				// System.out.println(letterID + " vs " +
				// firstLine.get(index-nextTo).substring(0,firstLine.get(index-nextTo).indexOf(":")+1));
				if (index - nextTo - 6 > -1) {
					if (letterID.contentEquals(firstLetters.get(index - nextTo - 6)) && left) {
						if (line[index - nextTo] != null && Integer.parseInt(line[index - nextTo]) == nextNum) {
							nextNum++;
							foundNum = true;
						}

					}

					else {
						left = false;
					}
				} else {
					left = false;
				}
				// if the index ID to the right matches the original ID then check to see if the
				// rank is next numerically
				// System.out.println("To the right");
				// System.out.println(letterID + " vs " +
				// firstLine.get(index+nextTo).substring(0,firstLine.get(index+nextTo).indexOf(":")+1));

				if (index + nextTo - 6 < firstLetters.size()) {
					if (letterID.contentEquals(firstLetters.get(index + nextTo - 6)) && right) {

						if (line[index + nextTo] != null && Integer.parseInt(line[index + nextTo]) == nextNum) {
							nextNum++;
							foundNum = true;
						}
					} else {
						right = false;

					}
				} else {
					right = false;

				}

				if (foundNum) {
					nextTo = 1;
					left = true;
					right = true;
				} else {
					nextTo++;
				}

			}
			for (int x = lastIndex + 1; x < line.length; x++) {
				if (line[x] != null && Integer.parseInt(line[x]) == nextNum) {
					rankTwoIndex = x;
				}
			}

		}
		firstLetters.remove(0);

	}

	/**
	 * @param none
	 * @return boolean will return the boolean value of changed
	 */
	public boolean getChanged() {
		return changed;
	}

	/**
	 * @param none
	 * @return String This method will return the current line being worked one
	 */
	public String getLine() {
		String temp = "";
		for (String x : line) {
			temp = temp + x + ",";
		}
		temp = temp.substring(0, temp.lastIndexOf(","));
		// System.out.println("This is temp: " + temp);
		return temp;
	}

	/**
	 * 
	 * @param name
	 * @return String this method will find the party name of the rank two
	 *         party/candidate
	 */
	public String findRankTwoPartyName(String name) {
		firstLetters.add(0, "A:");
		String rankTwoPartyName = name;
		if (!rankTwoPartyName.contentEquals("UG:")) {
			int tempIndexPos = firstLetters.indexOf(rankTwoPartyName);
			String tempName = firstLine.get(tempIndexPos + 6);
			System.out.println("tempName: " + tempName);
			rankTwoPartyName = tempName = tempName.substring(tempName.indexOf(":") + 1);
		} else {
			rankTwoPartyName = "UG";
		}
		firstLetters.remove(0);
		return rankTwoPartyName;
	}

	/**
	 * @param none
	 * @return String This method will return the party at the selected index
	 */
	public String getPartyAtIndex() {
		return partyOrder.get(index);
	}

}
