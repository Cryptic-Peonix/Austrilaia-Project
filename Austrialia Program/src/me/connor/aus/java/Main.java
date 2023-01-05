package me.connor.aus.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import me.connor.aus.java.objects.MatrixNew;
import me.connor.aus.java.util.Log;
import me.connor.aus.java.util.PartyHelper;
import me.connor.aus.java.util.SpectrumSortEdit;

/**
 * Main class for Australia program
 * @author cclark5
 *
 */
public class Main {

	PartyHelper helper = new PartyHelper();
	private double bestStandDev = -1;
	private double standDev;
	private String standDevParties;
	private double altBestStandardDev = -1;
	private double altStandDev;
	private String altStandDevParties;
	static Log log = new Log();

	// Have user select the party bounds
	public void RunManualSelection() throws IOException {

		// create variables
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		int lowBound;
		int highBound;
		String[] coalitionParties = { "LIBERAL/THE NATIONALS", "Liberal & Nationals",
				"Liberal National Party of Queensland", "Liberal", "The Nationals", "Country Liberals (NT)" };
		String[] laborParties = { "Australian Labor Party", "Labor/Country Labor",
				"Australian Labor Party (Northern Territory) Branch" };

		// Create a list of the parties, format it, and print it
		PartyHelper list = new PartyHelper();
		list.createPartyList();
		list.removeAliases();
		list.printFormattedList();
		// helper.createPartyList();
		// helper.printFormattedList();
		// PartyList list = new PartyList();
		// list.createPartyList();
		// list.printFormattedList();

		try {
			// get the lower bound
			System.out.println("\n" + "Using the indexies provided, please select the lower party bound: ");
			lowBound = scan.nextInt();
			/*
			 * // Convert any alias to liberal/coalition to simplify things for (int i = 0;
			 * i < laborParties.length; i++) { if (lowBound != 6 &&
			 * list.getPartyName(lowBound).equals(laborParties[i])) { lowBound = 6; } } for
			 * (int i = 0; i < coalitionParties.length; i++) { if (lowBound != 29 &&
			 * list.getPartyName(lowBound).equals(coalitionParties[i])) { lowBound = 29; } }
			 * for (int i = 0; i < greenParties.length; i++) { if (lowBound != 43 &&
			 * list.getPartyName(lowBound).contentEquals(greenParties[i])) { lowBound = 43;
			 * } }
			 */
			System.out.println(list.getPartyName(lowBound) + " selected.");
		} catch (InputMismatchException e) {
			System.out.println("\n" + "Please enter a number!");
			throw e;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\n" + "Please enter a valid number!");
			throw e;
		}

		try {
			// get the upper bound
			System.out.println("\n" + "Please select the upper party bound: ");
			highBound = scan.nextInt();
			if (highBound == lowBound) {
				throw new IllegalArgumentException("Please select two different parties!");
			}
			// check that second party is not part of a coalition
			for (int c = 0; c < coalitionParties.length; c++) {
				if (helper.convertIndexToName(highBound).equals(coalitionParties[c])
						&& helper.convertIndexToName(lowBound).equals(coalitionParties[c])) {
					throw new IllegalArgumentException("Please select two non-coalition parties!");
				}
			}
			// check that the second party is not part of a labor
			for (int l = 0; l < laborParties.length; l++) {
				if (helper.convertIndexToName(highBound).equals(laborParties[l])
						&& helper.convertIndexToName(lowBound).equals(laborParties[l])) {
					throw new IllegalArgumentException("Please select two non-labor parties!");
				}
			}
			// print user selection
			System.out.println(list.getPartyName(highBound) + " selected.");
			// try to catch any other possible errors
		} catch (InputMismatchException e) {
			System.out.println("\n" + "Please enter a number!");
			throw e;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\n" + "Please enter a valid number!");
			throw e;
		}

		// Confirmation
		System.out.println("\n" + "Now calculating for " + list.getPartyName(lowBound) + " as the lower bound and "
				+ list.getPartyName(highBound) + " as the upper bound.");

		SpectrumSortEdit test = new SpectrumSortEdit(highBound, lowBound);
		test.runPartyArray();
		// test.sortThrough();
		test.printSpectrum();
		// test.calculateStandardDeviation();
		System.out.println("Finished!");

	}
 
	/**
	 * Automatically run through every bound possible
	 * @throws IOException
	 */
	public void RunAutoSelection() throws IOException {
		log.setup();
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		helper.createPartyList();
		helper.removeAliases();
		helper.printFormattedList();
		// use helper.getListLength to get the length of the party list :)
		System.out.println("\n" + "Starting pos:");
		int ans1 = scan.nextInt();
		System.out.println("How far to go?");
		int ans = scan.nextInt();
		int maxCounts = 0;
		int counter = 0;
		int maxLength = helper.getListLength();
		for (int low = ans1; low <= ans; low++) {
			for (int u = low + 1; u <= maxLength - 1; u++) {
				maxCounts++;
			}
		}
		// NEW LOOP
		for (int low = ans1; low <= ans; low++) {
			for (int u = low + 1; u <= maxLength - 1; u++) {
				counter++;
				System.out.println(
						"\n" + "Test " + counter + "/" + maxCounts + ": Calculating " + helper.getPartyName(low)
								+ " as the lower bound and " + helper.getPartyName(u) + " as the upper bound.");
				log.println("\n" + "Test " + counter + "/" + maxCounts + ": Calculating " + helper.getPartyName(low)
						+ " as the lower bound and " + helper.getPartyName(u) + " as the upper bound.");
				SpectrumSortEdit sort = new SpectrumSortEdit(u, low);
				sort.runPartyArray();
				sort.printSpectrum();
				standDev = sort.getStandardDeviation();
				if (bestStandDev == -1) {
					bestStandDev = standDev;
					standDevParties = String.format("%02d-%02d", low, u);
				}
				if (standDev > bestStandDev) {
					bestStandDev = standDev;
					standDevParties = String.format("%02d-%02d", low, u);
				}
				altStandDev = sort.getAltStandardDeviation();
				if (altBestStandardDev == -1) {
					altBestStandardDev = altStandDev;
					altStandDevParties = String.format("%02d-%02d", low, u);
				}
				if (altStandDev > altBestStandardDev) {
					altBestStandardDev = altStandDev;
					altStandDevParties = String.format("%02d-%02d", low, u);
				}
				printBestStandDev();
				System.out.println("Test complete!");
				log.println("Test Complete!");
			}
		}
	}

	/**
	 * Prints the best standard deviation from a test to it's output file
	 * @throws IOException If the file is missing for some reason
	 */
	public void printBestStandDev() throws IOException {
		File file = new File("output\\BestStandDev.txt");
		PrintWriter writer = new PrintWriter(new FileWriter(file, false));
		try {
			if (file.createNewFile()) {
				// System.err.println("stand dev file made");
			} else {
				// System.err.println("stand dev file exists");
			}
		} catch (IOException e) {

		}
		writer.println("Best Standard Deviation: ");
		writer.printf("%.4f\n", bestStandDev);
		writer.println("Case: " + standDevParties);
		writer.println("\n" + "Best Non-Extreme Standard Deviation: ");
		writer.printf("%.4f\n", altBestStandardDev);
		writer.println("Case: " + altStandDevParties);
		writer.close();
	}

	/**
	 * Main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Main main = new Main();
		Scanner scan = new Scanner(System.in);
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		System.out.println("Would you like to run spectrum (0) or matrix (1)");
		String ans = scan.next();
		if (ans.contentEquals("0")) {

			System.out.println("Would you like to run manual (0) or automatic (1) selection");
			String answer = scan.next();
			if (answer.equalsIgnoreCase("manual") || answer.equalsIgnoreCase("0")) {
				main.RunManualSelection();
			} else if (answer.equalsIgnoreCase("automatic") || answer.equalsIgnoreCase("1")) {
				main.RunAutoSelection();
			} else {
				log.println("ERROR: Please input a valid choice!");
				scan.close();
				throw new IllegalArgumentException("Please input a valid choice!");
			}
		} else {
			System.out.println("WORK IN PROGRESS...");
			// MatrixNew matrix = new MatrixNew();
			// matrix.makeMatrix();
		}
		scan.close();
	}

}
