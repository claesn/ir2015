/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.basic;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author spinfo
 *
 */
public class TestBasicIR {

	private static Corpus corpus;

	@BeforeClass
	public static void setUp() throws Exception {
		// Korpus einlesen und in Werke unterteilen:
		String filename = "pg100.txt";
		corpus = new Corpus(filename);
	}

	@Test
	public void testCorpus() throws Exception {
		// Testen, ob Korpus korrekt angelegt wurde:
		String text = corpus.getText();
		assertTrue("Korpus sollte nicht leer sein!", text.length() > 0);
		System.out.println("Größe des Korpus: " + text.length() + " Zeichen.");
		System.out.println("Korpus: " + text);
	}

}
