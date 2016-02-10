package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Corpus {

	private String text;
	// NEU: Korpus als Sammlung von Document-Objekten
	private List<Document> works;

	// NEU: delimiter für Trennung von Titel und Text
	public Corpus(String location, String worksDelimiter, String titleDelimiter) {
		StringBuilder sb = new StringBuilder();
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(new File(location));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				sb.append(line);
				sb.append("\n"); // Newline für Werkeinteilung (s. delimiter)
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		text = sb.toString();
		/*
		 * NEU: Anstatt wie bisher den gesamten Text in Teilstrings zu splitten,
		 * werden diese jetzt selbst nochmals mithilfe des titleDelimiter in
		 * Titel und Text gesplittet und in einem Document-Objekt gekapselt.
		 * Dabei lassen wir das erste "Werk" weg (Lizenzvereinbarung etc.):
		 */
		works = new ArrayList<Document>();
		List<String> worksAsList = Arrays.asList(text.split(worksDelimiter));
		for (String work : worksAsList.subList(1, worksAsList.size())) {
			/*
			 * trim() schneidet überschüssige Leerzeichen ab, indexOf() gibt die
			 * erste Position des delimiters im Text zurück - damit erhalten wir
			 * einen bereinigten Teilstring, der vom Beginn des Dokuments bis
			 * zum ersten Vorkommen des delimiters reicht (= der Titel)
			 */
			String title = (work.trim().substring(0,
					work.trim().indexOf(titleDelimiter))).trim();
			works.add(new Document(work, title));
		}
	}

	// Korpus als Sammlung von Document-Objekten
	public List<Document> getWorks() {
		return works;
	}

	public String getText() {
		return text;
	}

	// NEU: Zugriff auf die docIds (für die Erstellung von Lucene-Docs)
	public int getDocId(Document work) {
		return this.works.indexOf(work);
	}

}
