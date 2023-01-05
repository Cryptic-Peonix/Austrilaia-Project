/**
 * This class is used to read the first line of every CSV file and find all of the parties on that csv file and write them to a text file. 
 * @author Marc D'Avanzo
 */
package australianPoliticalAffinity;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;


public class AlphabeticalOrder {
	private Scanner scan;
	private ArrayList<String> list;
	private WritePartyList docWriter;
	private Scanner csvScanner;
	private Scanner orderScanner;
	private ArrayList<String> orderList;
	//private ArrayList<String> ballots;
	private File f;
	private String[] fileList;
	private String currentLine;
	private ArrayList<String> itemizedBallot;
	private ArrayList<String> alphabeticalPartyOrder;
	private Scanner scanAlphabetical;
	private int linesRead;
	private Scanner test;
//	private int linesAdded;
//	private WritePartyList testTemp;
	
	
	
	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public AlphabeticalOrder() throws FileNotFoundException
	{
		list = new ArrayList<String>();
		scan = new Scanner(new FileInputStream("PartyListOrganized.txt"));
		docWriter = new WritePartyList("AlphabeticalPartyOrder.txt", true);
		f = new File("data");
		fileList = f.list();
		Arrays.parallelSort(fileList);
		csvScanner = null;
		orderList = new ArrayList<String>();
		//ballots = new ArrayList<String>();
		currentLine ="";
		itemizedBallot = new ArrayList<String>();
		scanAlphabetical = new Scanner(new FileInputStream("AlphabeticalPartyOrder.txt"));
		alphabeticalPartyOrder = new ArrayList<String>();
		linesRead = 1;
	//	linesAdded = 0;
	//	testTemp = new WritePartyList("testData.csv", true);
		
		test = new Scanner(new FileInputStream("ACTdata.csv"));
		
		
	}
	/**
	 * 
	 * @param none
	 * @return void
	 * will sort PartyListOrganized.txt into alphabetical order
	 */
	public void sort()
	{
		while(scan.hasNextLine())
		{
			list.add(scan.nextLine());
		}
		// uses the insertion sort to sort parties into alphabetical order
		for( int x = 1; x < list.size(); x++)
		{
			String key = list.get(x);
			int oneLess = x -1;
			while(oneLess >= 0 && list.get(oneLess).compareTo(key) > 0)
			{
				String temp = list.get(oneLess);
				list.set(oneLess+1, temp);
				oneLess--;
				
			}
			list.set(oneLess +1, key);
		}
		
		
	}
	
	/**
	 * @param noen
	 * @return void
	 * This method will read the first line of each CSV file to get the order the parties are in. 
	 * 
	 */
	public void readFirstLine()
	{
		orderList.clear();
		String firstLine = csvScanner.nextLine();
		StringTokenizer st = new StringTokenizer(firstLine, "," , false);
		//System.out.println("Has this many tokens: " + st.countTokens());
		while(st.hasMoreTokens())
		{
			orderList.add(st.nextToken());
		}
	//	System.out.println(orderList.get(orderList.size() -1) + " Last index of orderList");
		System.out.println(orderList.size() + " orderList size"); 
		//for(String temp: orderList)
		//{
			//System.out.println(temp);
		//}
	}
	
	@SuppressWarnings("unchecked")
	public void CheckOrder() throws IOException
	{
		Scanner check = new Scanner(new FileInputStream("AlphabeticalOrder.csv"));
		ArrayList<String> checkOrder = new ArrayList<String>();
		while(check.hasNextLine())
		{
			checkOrder.add(check.nextLine());
		}
	//	System.out.println(checkOrder.size());
		
		Collections.sort(checkOrder, new AlphabeticalOrderComparator());
//		System.out.println(checkOrder.size());

		WritePartyList orderChecked = new WritePartyList("CheckedAlphabeticalOrder.csv", true);
		for(String temp: checkOrder)
		{
	//		System.out.println(temp);

			orderChecked.writeToFile(temp);
		}
		
	}
	
	
	/*
	 * @param none
	 * @return void
	 * will return a string with every party in alphabetical order and the ballot id that the start of the ballot. 
	 */
	@SuppressWarnings("unchecked")
	public String addOrder() throws FileNotFoundException
	{
		Scanner temp = new Scanner(new FileInputStream("AlphabeticalOrder.csv"));
		ArrayList<String> tempArray = new ArrayList<String>();
		String alpha ="";
		while(temp.hasNextLine())
		{
			String tempLine = temp.nextLine();
			tempArray.add(tempLine);
			
		}
		//Collections.sort(tempArray, new AlphabeticalOrderComparator());
		for(String elem: tempArray)
		{
			alpha = alpha + elem + ",";
		}
		alpha = "State,Division,Vote Collection Point Name,Vote Collection Point ID,Batch No,Paper No," + alpha;
		alpha = alpha.substring(0, alpha.lastIndexOf(","));
		
		
		return alpha;
	}
	void writeList() throws IOException
	{
		for(String temp: list)
		{
			docWriter.writeToFile(temp);
		}
	}
	public void printSort()
	{
		for(String temp: list)
		{
			System.out.println(temp);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		AlphabeticalOrder test = new AlphabeticalOrder();
		//test.sort();
	//	test.printSort();
		//test.writeList();
		//test.CheckOrder();
	}

}
