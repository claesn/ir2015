package de.uni_koeln.spinfo.textengineering.ir.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Corpus {

	private String text;

	public Corpus(String filename) {

		StringBuilder sb = new StringBuilder();

		try {
			Scanner scanner = new Scanner(new File(filename));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				sb.append(line);
				sb.append("\n");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		text = sb.toString();
	}

	public String getText() {
		return text;
	}

}
