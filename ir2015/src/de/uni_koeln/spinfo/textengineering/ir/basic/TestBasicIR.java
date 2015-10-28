/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.basic;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author spinfo
 *
 */
public class TestBasicIR {

	private static Corpus corpus;
	private String query;

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
	public void testSearch() {
		// Testen, ob lineare Suche ein Ergebnis liefert:

		System.out.println();
		System.out.println("Lineare Suche:");
		System.out.println("-------------------");
		LinearSearch linear = new LinearSearch(corpus);

		query = "Brutus";
		Set<Integer> result = linear.search(query);
	}

}
