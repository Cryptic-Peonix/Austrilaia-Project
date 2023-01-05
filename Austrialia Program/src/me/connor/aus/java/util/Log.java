package me.connor.aus.java.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Tried making a log, failed, not gonna use it for now :/
 * @author cclark5
 *
 */
public class Log {

	private File logFile;
	private PrintWriter writer;

	public Log() {

	}
	
	public void setup() throws IOException {
		logFile = new File("log.txt");
		logFile.createNewFile();
		writer = new PrintWriter(new FileWriter(logFile, true));
	}

	public void print(String stuff) {
		writer.print(stuff);
	}

	public void println(String x) {
		writer.println(x);
	}
	
	public void close() {
		writer.close();
	}
}
