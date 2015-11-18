/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.boole;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.InformationRetrieval;

/**
 * @author spinfo
 *
 */
public class TestBooleanIR {

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
	public void testMatrixSearch() {
		// Testen, ob Suche in Term-Dokument-Matrix ein Ergebnis liefert:

		System.out.println();
		System.out.println("Term-Dokument-Matrix:");
		System.out.println("-------------------");
		ir = new TermDokumentMatrix(corpus);

		query = "Brutus Caesar";
		Set<Integer> result = ir.search(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("OR-Ergebnis für " + query + ": " + result);

		result = ((TermDokumentMatrix) ir).booleanSearch(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("AND-Ergebnis für " + query + ": " + result);
	}

	@Test
	public void testInvertedIndex() {
		// Testen, ob lineare Suche ein Ergebnis liefert:
		System.out.println();
		System.out.println("Inverted Index:");
		System.out.println("-------------------");
		ir = new InvertedIndex(corpus);

		query = "Brutus Caesar";
		Set<Integer> result = ir.search(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("Ergebnis für " + query + ": " + result);
	}
}
