package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Raport {
	private static PrintWriter writer;

	static {
		try {
			writer = new PrintWriter(new File("raport.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void makeRaport(String data) {
		writer.println(data);
		writer.println("");
		writer.flush();
	}

	public static void close() {
		writer.close();
	}
}
