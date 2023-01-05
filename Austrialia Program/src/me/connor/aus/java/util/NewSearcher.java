package me.connor.aus.java.util;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.StringTokenizer;

import australianPoliticalAffinity.Searcher;

/**
 * Am edit of the searcher class developed by Marc, better optimized for use
 * with any political party
 * 
 * @author cclark5
 *
 */
public class NewSearcher extends Searcher {

	// Create party helper
	PartyHelper helper = new PartyHelper();
	String s = Arrays.toString(line);
	Hashtable<String, String> partyCodes;
	public static final String[] alias = { "LIBERAL/THE NATIONALS", "Liberal & Nationals",
			"Liberal National Party of Queensland", "The Nationals", "Country Liberals (NT)", "Liberal",
			"Labor/Country Labor", "Australian Labor Party", "Australian Labor Party (Northern Territory) Branch",
			"The Greens (VIC)", "The Greens", "The Greens (WA)" };

	public NewSearcher() throws FileNotFoundException {
		super();
		partyCodes = new Hashtable<String, String>();
	}

	/**
	 * Checks the current ballot and determines which party it supports. Uses
	 * different methods for ATL/BTL
	 * 
	 * @param uBound The upper bound entered by the user
	 * @param lBound the lower party bound entered by the user
	 * @return A string that says which party, if any, is supported
	 * @throws FileNotFoundException
	 */
	public String supportWho(int uBound, int lBound) throws FileNotFoundException {
		String answer = "";
		helper.createPartyList();
		helper.removeAliases();
		String upperParty = helper.getPartyName(uBound);
		String lowerParty = helper.getPartyName(lBound);
		int upperPartyRank = -1;
		int lowerPartyRank = -1;
		int BTLtempUpper = -1;
		int BTLtempLower = -1;
		boolean upperFound = false;
		boolean lowerFound = false;
		String lineOneParty = "";
		String BTLcannidateLetter = "";
		int partysEndPoint = checkSecondA();

		// Alias check setup
		boolean upperAlias = false;
		boolean lowerAlias = false;
		String upperAliasName = alaisChecker(upperParty);
		String lowerAliasName = alaisChecker(lowerParty);

		if (!(upperAliasName.contentEquals("none"))) {
			upperAlias = true;
		}
		if (!(lowerAliasName.contentEquals("none"))) {
			lowerAlias = true;
		}

		// check BTL
		if (!BTL) {
			if (!upperAlias && !lowerAlias) {
				// Checking the ATL ballot
				for (int u = 6; (u < partysEndPoint); u++) {
					// get party name
					lineOneParty = firstLine.get(u).substring(firstLine.get(u).indexOf(":") + 1);
					// System.out.println(lineOneParty);
					if (lineOneParty.equals(lowerParty) && line[u] != null) {
						lowerPartyRank = Integer.parseInt(line[u]);
						// System.out.println("low rank: " + lowerPartyRank);
						lowerFound = true;
					}
					if (lineOneParty.equals(upperParty) && line[u] != null) {
						upperPartyRank = Integer.parseInt(line[u]);
						// System.out.println("high rank: " + upperPartyRank);
						upperFound = true;
					}
					if ((upperFound == true) && (lowerFound == true)) {
						break;
					}
				}
			} else {
				if (upperAlias && !lowerAlias) {
					int[] upperArray = ATLarrayCheckOne(upperParty, upperAliasName, lowerParty);
					upperPartyRank = upperArray[0];
					lowerPartyRank = upperArray[1];
				}
				if (lowerAlias && !upperAlias) {
					int[] lowerArray = ATLarrayCheckOne(lowerParty, lowerAliasName, upperParty);
					lowerPartyRank = lowerArray[0];
					upperPartyRank = lowerArray[1];
				} else {
					int[] bothArray = ATLarrayCheckBoth(upperParty, upperAliasName, lowerParty, lowerAliasName);
					upperPartyRank = bothArray[0];
					lowerPartyRank = bothArray[1];
				}

			}
		} else {
			// System.out.println("Its BTL");
			if (!upperAlias && !lowerAlias) {
				// check the BTL portion of the ballot
				for (int u = partysEndPoint; (u < line.length); u++) {
					if (line[u] == null) {
						continue;
					}
					// Get the cannnidate's letter code
					BTLcannidateLetter = firstLine.get(u).substring(0, firstLine.get(u).indexOf(":") + 1);
					// check for no UG
					if (!BTLcannidateLetter.contentEquals("UG:")) {
						// get party name
						lineOneParty = partyCodes.get(BTLcannidateLetter);
						// Simpifly coalition parties
						// check lower
						if (lineOneParty.equals(lowerParty) && line[u] != null) {
							lowerPartyRank = Integer.parseInt(line[u]);
							// used to ensure a higher position (lesser weight) is not accidently selected
							// after a pos has already been set
							if (lowerPartyRank > BTLtempLower && BTLtempLower != -1) {
								lowerPartyRank = BTLtempLower;
							}
							BTLtempLower = lowerPartyRank;
							// System.out.println("btl low rank: " + lowerPartyRank);
							// lowerFound = true;
						}
						// check upper
						if (lineOneParty.equals(upperParty) && line[u] != null) {
							upperPartyRank = Integer.parseInt(line[u]);
							// used to ensure a higher position (lesser weight) is not accidently selected
							// after a pos has already been set
							if (upperPartyRank > BTLtempUpper && BTLtempUpper != -1) {
								upperPartyRank = BTLtempUpper;
							}
							BTLtempUpper = upperPartyRank;
							// System.out.println("btl high rank: " + upperPartyRank);
							// upperFound = true;
						}
					}
				}
			} else {
				if (upperAlias && !lowerAlias) {
					int[] upperResults = BTLarrayCheckOne(upperParty, upperAliasName, lowerParty);
					upperPartyRank = upperResults[0];
					lowerPartyRank = upperResults[1];
				}
				if (lowerAlias && !upperAlias) {
					int[] lowerResults = BTLarrayCheckOne(lowerParty, lowerAliasName, upperParty);
					upperPartyRank = lowerResults[1];
					lowerPartyRank = lowerResults[0];
				} else {
					int[] bothArray = BTLarrayCheckBoth(upperParty, upperAliasName, lowerParty, lowerAliasName);
					upperPartyRank = bothArray[0];
					lowerPartyRank = bothArray[1];
				}
			}
		}
		if (!(upperPartyRank == -1 || lowerPartyRank == -1)) {
			if (upperPartyRank < lowerPartyRank) {
				answer = "upper";
				// System.out.println(answer);
			} else if (upperPartyRank > lowerPartyRank) {
				answer = "lower";
				// System.out.println(answer);
			} else {
				// System.out.println("equal");
			}
		} else {
			if (upperPartyRank == -1 && lowerPartyRank != -1) {
				answer = "lower";
			} else if (lowerPartyRank == -1 && upperPartyRank != -1) {
				answer = "upper";
			} else {
				answer = "equal";
			}
		}
		// System.out.println("returned");
		// System.out.println("Person: " + line[2]);
		// System.out.println("Ballot Set: " + line[4]);
		// System.out.println("Ballot No. " + line[5]);
		return answer;
	}

	/**
	 * Finds the best ranks of one alias and one non-Alias party for ATL only
	 * 
	 * @param party         the alias party
	 * @param aliasName     the alias
	 * @param nonAliasParty the non alias-party
	 * @return an int[] with results. int[0] contains the best rank of the alias
	 *         party and int[1] contains the best rank of the non alias party.
	 */
	private int[] ATLarrayCheckOne(String party, String aliasName, String nonAliasParty) {
		int rank = -1;
		int nonArank = -1;
		int bestRank = -1;
		int[] results = new int[2];
		int partysEndPoint = checkSecondA();
		String lineOneParty = "";
		String currentAliasName = "";
		int checkEnd = -1;
		int checkStart = -1;

		if (aliasName.contentEquals("liberal")) {
			checkEnd = 5;
			checkStart = 0;
		} else if (aliasName.contentEquals("labor")) {
			checkStart = 6;
			checkEnd = 8;
		} else {
			checkStart = 9;
			checkEnd = 11;
		}

		// Checking the ATL ballot
		// loop through the ATL parties
		for (int u = 6; u < partysEndPoint; u++) {
			lineOneParty = firstLine.get(u).substring(firstLine.get(u).indexOf(":") + 1);
			if (lineOneParty.equals(nonAliasParty) && line[u] != null) {
				nonArank = Integer.parseInt(line[u]);
				// System.out.println("low rank: " + lowerPartyRank);
			}
			// loop through Alias'
			for (int i = checkStart; i <= checkEnd; i++) {
				currentAliasName = alias[i];
				if (lineOneParty.equals(currentAliasName) && line[u] != null) {
					rank = Integer.parseInt(line[u]);
					if (bestRank < rank && bestRank != -1) {
						rank = bestRank;
					}
					bestRank = rank;
				}
			}

		}
		results[0] = rank;
		results[1] = nonArank;
		return results;
	}

	/**
	 * Finds the best ranks for two alias parties
	 * 
	 * @param party          first alias party
	 * @param aliasName      first alias
	 * @param lowerParty     second alias party
	 * @param lowerAliasName second alias
	 * @return an int[] with results. int[0] contains the best rank of party 1 and
	 *         int[1] contains the best rank of party 2.
	 */
	private int[] ATLarrayCheckBoth(String party, String aliasName, String lowerParty, String lowerAliasName) {
		int rank = -1;
		int bestRank = -1;
		int pTwoRank = -1;
		int pTwoBestRank = -1;
		int[] results = new int[2];
		int partysEndPoint = checkSecondA();
		String lineOneParty = "";
		String currentAliasName = "";
		int checkEnd = -1;
		int checkStart = -1;
		int checkEndTwo = -1;
		int checkStartTwo = -1;

		if (aliasName.contentEquals("liberal")) {
			checkEnd = 5;
			checkStart = 0;
		} else if (aliasName.contentEquals("labor")) {
			checkStart = 6;
			checkEnd = 8;
		} else {
			checkStart = 9;
			checkEnd = 11;
		}

		if (lowerAliasName.contentEquals("liberal")) {
			checkEndTwo = 5;
			checkStartTwo = 0;
		} else if (lowerAliasName.contentEquals("labor")) {
			checkStartTwo = 6;
			checkEndTwo = 8;
		} else {
			checkStartTwo = 9;
			checkEndTwo = 11;
		}

		// Checking the ATL ballot
		// loop through the ATL parties
		for (int u = 6; u < partysEndPoint; u++) {
			lineOneParty = firstLine.get(u).substring(firstLine.get(u).indexOf(":") + 1);
			// loop through Alias'
			for (int i = checkStart; i <= checkEnd; i++) {
				currentAliasName = alias[i];
				if (lineOneParty.equals(currentAliasName) && line[u] != null) {
					rank = Integer.parseInt(line[u]);
					if (bestRank < rank && bestRank != -1) {
						rank = bestRank;
					}
					bestRank = rank;
				}
			}
			for (int i = checkStartTwo; i <= checkEndTwo; i++) {
				currentAliasName = alias[i];
				if (lineOneParty.equals(currentAliasName) && line[u] != null) {
					pTwoRank = Integer.parseInt(line[u]);
					if (pTwoBestRank < pTwoRank && pTwoBestRank != -1) {
						pTwoRank = pTwoBestRank;
					}
					pTwoBestRank = pTwoRank;
				}
			}

		}
		results[0] = rank;
		results[1] = pTwoRank;
		return results;
	}

	/**
	 * This method will determine the ranks of one alias party and one non-alias
	 * party for BTL only
	 * 
	 * @param party         the Alias party name
	 * @param aliasName     The partys's Alias
	 * @param nonAliasParty The non-alias party
	 * @return an int[] with results. int[0] contains the best rank of the alias
	 *         party and int[1] contains the best rank of the non alias party.
	 */
	private int[] BTLarrayCheckOne(String party, String aliasName, String nonAliasParty) {
		int bestRank = -1;
		int tempBest = -1;
		int nonArank = -1;
		int nonAtemp = -1;
		int[] results = new int[2];
		int partysEndPoint = checkSecondA();
		String lineOneParty = "";
		String BTLcannidateLetter = "";
		int checkEnd = -1;
		int checkStart = -1;
		String currentAliasName = "";

		if (aliasName.contentEquals("liberal")) {
			checkEnd = 5;
			checkStart = 0;
		} else if (aliasName.contentEquals("labor")) {
			checkStart = 6;
			checkEnd = 8;
		} else {
			checkStart = 9;
			checkEnd = 11;
		}

		for (int u = partysEndPoint; (u < line.length); u++) {
			if (line[u] == null) {
				continue;
			}
			// Get the cannnidate's letter code
			BTLcannidateLetter = firstLine.get(u).substring(0, firstLine.get(u).indexOf(":") + 1);
			// check for no UG
			if (!BTLcannidateLetter.contentEquals("UG:")) {
				// get party name
				lineOneParty = partyCodes.get(BTLcannidateLetter);
				if (lineOneParty.equals(nonAliasParty) && line[u] != null) {
					nonArank = Integer.parseInt(line[u]);
					// used to ensure a higher position (lesser weight) is not accidently selected
					// after a pos has already been set
					if (nonArank > nonAtemp && nonAtemp != -1) {
						nonArank = nonAtemp;
					}
					nonAtemp = nonArank;
					// System.out.println("btl low rank: " + lowerPartyRank);
					// lowerFound = true;
				}
				for (int i = checkStart; i <= checkEnd; i++) {
					// get current alias
					currentAliasName = alias[i];
					// check party
					if (lineOneParty.equals(currentAliasName) && line[u] != null) {
						bestRank = Integer.parseInt(line[u]);
						// used to ensure a higher position (lesser weight) is not accidently selected
						// after a pos has already been set
						if (bestRank > tempBest && tempBest != -1) {
							bestRank = tempBest;
						}
						tempBest = bestRank;
					}
				}
			}
		}
		results[0] = bestRank;
		results[1] = nonArank;
		return results;
	}

	/**
	 * This method will determine the best ranks of two alias parties for BTL only
	 * 
	 * @param party          the first alias party
	 * @param aliasName      the first alias
	 * @param lowerParty     the second alias party
	 * @param lowerAliasName the second alias
	 * @return an int[], int[0] will contain the first party and int[1] will contain
	 *         the second party.
	 */
	private int[] BTLarrayCheckBoth(String party, String aliasName, String lowerParty, String lowerAliasName) {
		int bestRank = -1;
		int tempBest = -1;
		int bestRankTwo = -1;
		int tempBestTwo = -1;
		int[] results = new int[2];
		int partysEndPoint = checkSecondA();
		String lineOneParty = "";
		String BTLcannidateLetter = "";
		int checkEnd = -1;
		int checkStart = -1;
		int checkEndTwo = -1;
		int checkStartTwo = -1;
		String currentAliasName = "";

		if (aliasName.contentEquals("liberal")) {
			checkEnd = 5;
			checkStart = 0;
		} else if (aliasName.contentEquals("labor")) {
			checkStart = 6;
			checkEnd = 8;
		} else {
			checkStart = 9;
			checkEnd = 11;
		}
		
		if (lowerAliasName.contentEquals("liberal")) {
			checkEndTwo = 5;
			checkStartTwo = 0;
		} else if (lowerAliasName.contentEquals("labor")) {
			checkStartTwo = 6;
			checkEndTwo = 8;
		} else {
			checkStartTwo = 9;
			checkEndTwo = 11;
		}

		for (int u = partysEndPoint; (u < line.length); u++) {
			if (line[u] == null) {
				continue;
			}
			// Get the cannnidate's letter code
			BTLcannidateLetter = firstLine.get(u).substring(0, firstLine.get(u).indexOf(":") + 1);
			// check for no UG
			if (!BTLcannidateLetter.contentEquals("UG:")) {
				// get party name
				lineOneParty = partyCodes.get(BTLcannidateLetter);
				for (int i = checkStart; i <= checkEnd; i++) {
					// get current alias
					currentAliasName = alias[i];
					// check party
					if (lineOneParty.equals(currentAliasName) && line[u] != null) {
						bestRank = Integer.parseInt(line[u]);
						// used to ensure a higher position (lesser weight) is not accidently selected
						// after a pos has already been set
						if (bestRank > tempBest && tempBest != -1) {
							bestRank = tempBest;
						}
						tempBest = bestRank;
					}
				}
				for (int i = checkStartTwo; i <= checkEndTwo; i++) {
					// get current alias
					currentAliasName = alias[i];
					// check party
					if (lineOneParty.equals(currentAliasName) && line[u] != null) {
						bestRankTwo = Integer.parseInt(line[u]);
						// used to ensure a higher position (lesser weight) is not accidently selected
						// after a pos has already been set
						if (bestRankTwo > tempBestTwo && tempBestTwo != -1) {
							bestRankTwo = tempBestTwo;
						}
						tempBestTwo = bestRankTwo;
					}
				}
			}
		}
		results[0] = bestRank;
		results[1] = bestRankTwo;
		return results;
	}

	@Override
	public boolean searchBallot(String currentLine) {
		// System.out.println("Btl: " + BTL);
		rankTwoIndex = -1;
		index = -1;
		int secondAColon = -1;
		changed = false;
		BTL = false;
		// System.out.println("Btl after seacrh set: " + BTL);
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

		secondAColon = checkSecondA();

		for (int i = secondAColon; i < line.length; i++) {
			if (line[i] != null) {
				BTL = true;
				break;
			}
		}

		// Create Hashtable of party Codes
		for (int t = 6; t < secondAColon; t++) {
			String tempKey = firstLine.get(t).substring(0, firstLine.get(t).indexOf(":") + 1);
			String tempVal = firstLine.get(t).substring(firstLine.get(t).indexOf(":") + 1);
			partyCodes.put(tempKey, tempVal);
		}

		// will check the BTL part of the ballot for rank 1 and if the ballot is valid.
		// String s = Arrays.toString(line);
		// System.out.println(s);
		int highestNum = -1;
		// lastIndex = firstLetters.indexOf("A:") + 6;
		lastIndex = secondAColon; // fixed line
		// System.out.println(lastIndex);
		// System.out.println("");
		// will find the highest rank for BLT
		for (int x = lastIndex; x < line.length; x++) {
			if (line[x] != null) {
				if (Integer.parseInt(line[x]) > highestNum) {
					highestNum = Integer.parseInt(line[x]);
				}
			}
		}
		// creates an arrayList to check the BTL part of the ballot for excpetions and
		// to see if the ballot is BTL
		// System.out.println("highest num: " + highestNum);
		if (highestNum > 5) {
			// System.out.println(highestNum + " is the hight rank");
			// System.out.println("Last index is: " + lastIndex);
			// System.out.println("IS BTL");
			// System.out.println(currentLine);
			int indexTracker = 0;
			int[] checkBTL = new int[line.length - lastIndex];
			for (int x = lastIndex; x < line.length; x++) {
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
							index = x + lastIndex;
							// System.out.println("1: " + index);
						}
						if (BTLcounter == 2) {
							// System.out.println("found rank two");
							rankTwoIndex = x + lastIndex;
						}
						numFound = true;
					} else if (checkBTL[x] == BTLcounter && numFound) {
						if (BTLcounter < 6) {
							BTL = false;
							index = -1;
							// System.out.println("2: " + index);
							rankTwoIndex = -1;
						}
						BTLformal = false;
						for (int pos = lastIndex; pos < line.length; pos++) {
							if (line[pos] != null) {
								if (Integer.parseInt(line[pos]) >= BTLcounter) {
									line[pos] = null;
								}
							}
						}
						changed = true;
						// s = Arrays.toString(line);
						// System.out.println("1: " + s);

					}

				}
				if (!numFound && BTLcounter > 6) {
					BTLformal = false;
					for (int pos = lastIndex; pos < line.length; pos++) {
						if (line[pos] != null) {
							if (Integer.parseInt(line[pos]) >= BTLcounter) {
								line[pos] = null;
							}
						}
					}
					changed = true;
					// s = Arrays.toString(line);
					// System.out.println("2: " + s);
				} else if (!numFound && BTLcounter <= 6) {
					BTLformal = false;
					for (int pos = lastIndex; pos < line.length; pos++) {
						line[pos] = null;
					}
					changed = true;
					// s = Arrays.toString(line);
					// System.out.println("3: " + s);
					BTL = false;
					index = -1;
					// System.out.println("3: " + index);
					rankTwoIndex = -1;
				}
				if (numFound) {
					BTLcounter++;
				}

			}
			// System.out.println("BTL index is: " + index);
		} else {
			BTL = false;
			for (int pos = lastIndex; pos < line.length; pos++) {
				line[pos] = null;
			}
			changed = true;
			// s = Arrays.toString(line);
			// System.out.println("4: " + s);
		}
		if (!BTL) {
			// System.out.println("Ballot not BTL");
			// System.out.println("Should not have changed: " +lastIndex);
			int[] ATLchecker = new int[lastIndex - 6];
			int ATLcheckerIndex = 0;
			for (int x = 6; x < lastIndex; x++) {
				// System.out.println("line[x] " + line[x]);
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
							// System.err.println("4: " + index);
						}
						if (ATLcounter == 2) {
							// System.out.println("found rank 2");
							rankTwoIndex = x + 6; // FIX LATER
							// rankTwoIndex = findRankTwoIndex();
						}
						numFound = true;
					} else if (ATLcounter == ATLchecker[x] && numFound) {
						ATLformal = false;
						for (int pos = 6; pos < lastIndex; pos++) {
							if (line[pos] != null) {
								if (Integer.parseInt(line[pos]) >= ATLcounter) {
									line[pos] = null;
								}
							}
						}
						if (ATLcounter == 2) {
							rankTwoIndex = -1;
						}
						changed = true;
						// s = Arrays.toString(line);
						// System.out.println("5: " + s);
					}
				}

				if (!numFound) {

					ATLformal = false;
					for (int pos = 6; pos < lastIndex; pos++) {
						if (line[pos] != null) {
							if (Integer.parseInt(line[pos]) >= ATLcounter) {
								// Fix later
								line[pos] = null;
							}
						}
					}
					changed = true;
					// s = Arrays.toString(line);
					// System.out.println("6: " + s);
				} else {
					// System.out.println("ATL counter went up");
					ATLcounter++;
				}

			}

		} else {
			for (int x = 6; x < lastIndex; x++) {
				line[x] = null;
			}
			changed = true;
		}

		// System.out.println(currentLine);
		// System.out.println("index is: " +index);
		// System.out.println("Btl end: " + BTL);
		return BTL;
	}

	/**
	 * Searches the first line to determine where the ATL portion ends and the BTL
	 * portion begins
	 * 
	 * @return an int of the index where the first BTL vote should be
	 */
	public int checkSecondA() {
		int secondAColon = -1;
		// Get Second A colon location
		for (int l = 7; l < firstLine.size(); l++) {
			if (firstLine.get(l).substring(0, 2).equals("A:")) {
				secondAColon = l;
				break;
			}
		}
		return secondAColon;
	}

	/**
	 * Finds the index of the rank two choice in a ballot
	 * 
	 * @param name The Letter ID of the party
	 * @return the index of rank 2
	 */
	public int findRankTwoIndex(String name) {
		int result = -1;
		int secondAColon = checkSecondA();
		for (int i = 6; i < secondAColon; i++) {
			if (firstLine.get(i).substring(0, firstLine.get(i).indexOf(":") + 1).contentEquals(name)) {
				result = i;
				break;
			}
		}
		/*
		 * // System.out.println(Arrays.toString(line)); for (int i = 6; i <
		 * secondAColon; i++) { // System.out.println(line[i]); if (line[i] != null) {
		 * if (line[i].equals("2")) { result = i; break; } } }
		 */
		return result;
	}

	@Override
	public int BTLIndex() {
		int BTLindex = -1;
		int secondAColon = checkSecondA();
		String letterID = firstLine.get(index).substring(0, firstLine.get(index).indexOf(":") + 1);
		if (!letterID.contentEquals("UG:")) {
			for (int x = 6; x < secondAColon; x++) {
				if (firstLine.get(x).substring(0, firstLine.get(x).indexOf(":") + 1).contentEquals(letterID)) {
					BTLindex = x;
					break;
				}
			}
		} else {
			BTLindex = -500;
		}
		return BTLindex;
	}

	@Override
	public void BTLRankTwoIndex() {
		int BTLindexTwo = -1;
		int secondAColon = checkSecondA();
		String letterID = firstLine.get(rankTwoIndex).substring(0, firstLine.get(index).indexOf(":") + 1);
		if (!letterID.contentEquals("UG:")) {
			for (int i = 6; i < secondAColon; i++) {
				if (firstLine.get(i).substring(0, firstLine.get(i).indexOf(":") + 1).contentEquals(letterID)) {
					BTLindexTwo = i;
					break;
				}
			}
		} else {
			BTLindexTwo = -500;
		}
		rankTwoIndex = BTLindexTwo;
	}

	@Override
	public String findRankTwoPartyName(String name) {
		String rankTwoPartyName = name;
		if (!rankTwoPartyName.contentEquals("UG:")) {
			int tempIndexPos;
			if (!BTL) {
				// tempIndexPos = firstLine.indexOf(rankTwoPartyName);
				tempIndexPos = findRankTwoIndex(rankTwoPartyName);
				// tempIndexPos = rankTwoIndex;
			} else {
				tempIndexPos = rankTwoIndex;
			}
			String tempName = firstLine.get(tempIndexPos);
			// System.out.println("tempname: " + tempName);
			rankTwoPartyName = tempName = tempName.substring(tempName.indexOf(":") + 1);
		} else {
			rankTwoPartyName = "UG";
		}
		return rankTwoPartyName;
	}

	/**
	 * Will check the party to see if it is part of a coalition
	 * 
	 * @param partyName the party to check
	 * @return which coalition (if any) the party is a part of
	 */
	public String alaisChecker(String partyName) {
		String answer = "none";
		String[] alias = { "LIBERAL/THE NATIONALS", "Liberal & Nationals", "Liberal National Party of Queensland",
				"The Nationals", "Country Liberals (NT)", "Liberal", "Labor/Country Labor", "Australian Labor Party",
				"Australian Labor Party (Northern Territory) Branch", "The Greens (VIC)", "The Greens" };
		for (int i = 0; i < alias.length; i++) {
			if (partyName.contentEquals(alias[i])) {
				// liberal
				if (i <= 5) {
					answer = "liberal";
					break;
				} else
				// labor
				if ((i > 5 && i <= 8)) {
					answer = "labor";
					break;
				} else {
					// green
					answer = "green";
					break;
				}

			}
		}
		return answer;
	}

}
