/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.basic;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author spinfo
 *
 */
public class TestBasicIR {

	private static Corpus corpus;
	private String query;
	private InformationRetrieval ir;

	@BeforeClass
	public static void setUp() throws Exception {
		// Korpus einlesen und in Werke unterteilen:
		String filename = "pg100.txt";
		String delimiter = "1[56][0-9]{2}\n";
		corpus = new Corpus(filename, delimiter);
	}

	@Test
	public void testCorpus() throws Exception {
		// Testen, ob Korpus korrekt angelegt wurde:
		List<String> works = corpus.getWorks();
		assertTrue("Korpus sollte mehr als 1 Werk enthalten!", works.size() > 1);
		System.out.println("Größe des Korpus: " + works.size() + " Werke.");
	}

	@Test
	public void testLinearSearch() {
		// Testen, ob lineare Suche ein Ergebnis liefert:

		System.out.println();
		System.out.println("Lineare Suche:");
		System.out.println("-------------------");
		ir = new LinearSearch(corpus);

		query = "Brutus Caesar";
		Set<Integer> result = ir.search(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("Ergebnis für " + query + ": " + result);
	}
	
	@Test
	public void testMatrixSearch() {
		// Testen, ob Suche in Term-Dokument-Matrix ein Ergebnis liefert:

		System.out.println();
		System.out.println("Term-Dokument-Matrix:");
		System.out.println("-------------------");
		ir = new TermDokumentMatrix(corpus);

		query = "Brutus Caesar";
		Set<Integer> result = ir.search(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("Ergebnis für " + query + ": " + result);
	}


}
