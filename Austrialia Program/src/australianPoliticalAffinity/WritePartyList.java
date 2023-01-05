package australianPoliticalAffinity;
/**
 * This class will write a text file
 * @author Marc D'Avanzo
 * 
 */
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException; 

public class WritePartyList {
	private String path;
	private boolean appendToFile = false;
	
	public WritePartyList(String filePath)
	{
		path = filePath;
		
	}
	
	public WritePartyList(String filePath, boolean appendValue)
	{
		path = filePath;
		appendToFile = appendValue;
	}
	
	public void writeToFile(String textLine) throws IOException
	{
		FileWriter write = new FileWriter(path, appendToFile);
		PrintWriter printLine = new PrintWriter(write);
		printLine.printf("%s" + "%n",textLine);
		printLine.close();
		
		
	}

}
