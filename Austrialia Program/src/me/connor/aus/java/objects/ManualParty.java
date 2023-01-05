package me.connor.aus.java.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import australianPoliticalAffinity.Party;
import me.connor.aus.java.util.PartyHelper;

public class ManualParty extends Party {

	private String upperBound;
	private String lowerBound;
	private int upperBoundSuppourt;
	private int lowerBoundSuppourt;
	private PartyHelper list = new PartyHelper();

	/**
	 * 
	 * @param partyName
	 * @param lBound
	 * @param uBound
	 * @throws FileNotFoundException
	 */
	public ManualParty(String partyName, int lBound, int uBound) throws FileNotFoundException {
		super(partyName);
		upperBoundSuppourt = 0;
		lowerBoundSuppourt = 0;
		list.createPartyList();
		list.removeAliases();
		upperBound = list.getPartyName(uBound);
		lowerBound = list.getPartyName(lBound);
	}

	@Deprecated
	public ManualParty(String partyName) throws FileNotFoundException {
		super(partyName);
		upperBoundSuppourt = 0;
		lowerBoundSuppourt = 0;
	}

	@Override
	public void printSpectrumVals() {
		System.out.println("\n" + partyName + " SpectrumVals:");
		System.out.println("Ballot Size: " + ballotArray.size());
		System.out.printf("U/(U+L) =  %.4f\n", spectrumVal);
		// System.out.printf("U/Ballot.size = %.4f\n", ((double)upperBoundSuppourt /
		// ((double)ballotArray.size())));
		System.out.println("U - L: " + (upperBoundSuppourt - lowerBoundSuppourt));
		System.out.println("Total votes: " + " vs " + lowerBound + ": " + lowerBoundSuppourt + " " + upperBound + ": "
				+ upperBoundSuppourt);
		System.out.println("No " + upperBound + " or " + lowerBound + " ranked: " + equalSupport);
	}

	public void printSpectrumToFile(File file, PrintWriter out) throws IOException {
		out.println("\n" + partyName + " SpectrumVals:");
		out.println("Ballot Size: " + ballotArray.size());
		out.printf("U/(U+L) =  %.4f\n", spectrumVal);
		out.println("U - L: " + (upperBoundSuppourt - lowerBoundSuppourt));
		out.println("Total votes:" + " vs " + lowerBound + ": " + lowerBoundSuppourt + " " + upperBound + ": "
				+ upperBoundSuppourt);
		out.println("No " + upperBound + " or " + lowerBound + " ranked: " + equalSupport);
	}

	@Override
	public double calculateSpectrumVal() {
		double oTemp = upperBoundSuppourt;
		double tTemp = lowerBoundSuppourt;
		double suppourtVal = oTemp + tTemp;
		double temp = oTemp / suppourtVal;
		return temp;
	}

	@Override
	public void addSpectrumVal() {
		spectrumVal = calculateSpectrumVal();
	}

	/**
	 * Add a vote for the upper bound
	 */
	public void addUpperVote() {
		upperBoundSuppourt++;
	}

	/**
	 * Add a vote for the lower bound
	 */
	public void addLowerVote() {
		lowerBoundSuppourt++;
	}

	@Override
	public void addESupport() {
		super.addESupport();
	}

	public double getUpperVote() {
		return upperBoundSuppourt;
	}

	public double getLowerVote() {
		return lowerBoundSuppourt;
	}

}
