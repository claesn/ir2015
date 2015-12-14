/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.boole;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

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
		// Testen, ob Suche in invertiertem Index ein Ergebnis liefert:

		System.out.println();
		System.out.println("Inverted Index:");
		System.out.println("-------------------");
		ir = new InvertedIndex(corpus);

		query = "Brutus Caesar";
		Set<Integer> result = ir.search(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("Ergebnis für " + query + ": " + result);
	}
	
	@Test
	public void testPositionalIndex() {

		System.out.println();
		System.out.println("Positional Index:");
		System.out.println("-------------------");
		PositionalIndex posIndex = new PositionalIndex(corpus);

		/*
		 * Standard-Suche (wie bisher):
		 */
		query = "Brutus Caesar";
		Set<Integer> result = posIndex.search(query);
		assertTrue("ergebnis sollte nicht leer sein!", result.size() > 0);
		System.out.println("Ergebnis für " + query + ": " + result);
		
		System.out.println("-------------------");
		query = "to be or not to be";
		result = posIndex.search(query);
		assertTrue("ergebnis sollte nicht leer sein!", result.size() > 0);
		System.out.println("Ergebnis für " + query + ": " + result);
		
		/*
		 * Proximity-Suche:
		 */
		System.out.println("-------------------");
		SortedMap<Integer, List<Integer>> posResult;
		posResult = posIndex.proximitySearch(query, 1);// nur konsekutive Terme
		assertTrue("ergebnis sollte nicht leer sein!", posResult.size() > 0);
		System.out.println("Ergebnis für '" + query + "': " + posResult);
		posIndex.printSnippets(query, posResult, 1);

		System.out.println("-------------------");
		query = "Brutus Caesar";
		posResult = posIndex.proximitySearch(query, 3);
		assertTrue("ergebnis sollte nicht leer sein!", posResult.size() > 0);
		System.out.println("Ergebnis für '" + query + "': " + posResult);
		posIndex.printSnippets(query, posResult, 1);
	}
	
}
