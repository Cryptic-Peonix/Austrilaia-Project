package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Program to read output files from my Austrialia Spectrum program
 * @author cclark5
 *
 */
public class Main {

	public static void main(String[] args) {

		Scanner current;
		PrintWriter outputWriter = null;
		PrintWriter outputWrite2 = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		double bestStandDiv = -1;
		String bestStandCase = "";
		double bestNEStandDiv = -1;
		String NEname = "";

		// make files
		String fileList[];
		File file = new File("input");
		fileList = file.list();
		Arrays.parallelSort(fileList);
		file = new File("input");
		fileList = file.list();
		Arrays.sort(fileList);
		File output = new File("output//output-" + dtf.format(LocalDateTime.now()) + ".csv");
		File output2 = new File("output//output-" + dtf.format(LocalDateTime.now()) + ".txt");
		try {
			// create file
			output.createNewFile();
			output2.createNewFile();
			
			// Make header
			outputWriter = new PrintWriter(new FileWriter(output));
			outputWrite2 = new PrintWriter(new FileWriter(output2));
			outputWriter.println("Case,First Party,Second Party,Standard Deviation, Non-Extreme Standard Deviation");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Sorting and shit
		for (int i = 0; i < fileList.length; i++) {
			StringBuilder sb = new StringBuilder();
			String caseName = fileList[i].substring(0, 5);
			caseName = caseName.substring(0, 2) + "-" + caseName.substring(2);
			sb.append(caseName + ",");
			int currentLine = 1;
			try {
				current = new Scanner(new FileInputStream("input//" + fileList[i]));
			} catch (IOException e) {
				continue;
			}
			while (current.hasNextLine()) {
				String test = current.nextLine();
				if (currentLine == 1) {
					String partyOne = test.substring(test.indexOf(":") + 2, test.indexOf(","));
					sb.append(partyOne + ",");
					String partyTwo = test.substring(test.indexOf(",") + 2);
					partyTwo = partyTwo.substring(partyTwo.indexOf(":") + 2);
					sb.append(partyTwo + ",");
				}
				if (currentLine == 340) {
					double temp = Double.parseDouble(test);
					if (temp > bestStandDiv) {
						bestStandDiv = temp;
						bestStandCase = fileList[i];
					}
					sb.append(temp + ",");
				}
				if (currentLine == 343) {
					double temp = Double.parseDouble(test);
					if (temp > bestNEStandDiv) {
						bestNEStandDiv = temp;
						NEname = fileList[i];
					}
					sb.append(temp + ",");
				}
				currentLine++;
			}
			sb.append("," + "\n");
			outputWriter.write(sb.toString());
		}
		outputWriter.close();
		outputWrite2.println("Best Standard Deviation: " + bestStandDiv + " (" + bestStandCase + "), Best Non-Extreme Standard Deviation: " + bestNEStandDiv + " (" + NEname + ").");
		outputWrite2.close();
	}
}
