package de.uni_koeln.spinfo.textengineering.ir.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/*
 * Einfacher Preprocessor: splittet und gibt Tokens oder sortierte Types/Stems zurück.
 */

public final class Preprocessor {

	/*
	 * Ein Unicode-wirksamer Ausdruck für "Nicht-Buchstabe", der auch Umlaute berücksichtigt; die einfache (ASCII)
	 * Version ist: "\\W"
	 */
	private static final String UNICODE_AWARE_DELIMITER = "[^\\p{L}]";

	public List<String> tokenize(String text) {
		List<String> result = new ArrayList<String>();
		List<String> tokens = Arrays.asList(text.toLowerCase().split(UNICODE_AWARE_DELIMITER));
		for (String s : tokens) {
			if (s.trim().length() > 0) {
				result.add(s.trim());
			}
		}
		return result;
	}

	public List<String> getTerms(String text) {
		SortedSet<String> terms = new TreeSet<String>(tokenize(text));
		return new ArrayList<String>(terms);
	}

	/*
	 * Für das Stemming können wir den Snowball-Stemmer nutzen (siehe http://snowball.tartarus.org/download.php), der
	 * einfach mit ins Projektverzeichnis gelegt werden kann. Ein Beispiel für die Nutzung findet sich in der Klasse
	 * org.tartarus.snowball.TestApp.
	 */
	public List<String> getStems(String text) {
		SnowballStemmer stemmer = new englishStemmer();
		List<String> result = new ArrayList<String>();
		List<String> terms = getTerms(text);
		for (String t : terms) {

			// TODO: Stemmer nach stem zu term fragen und stem zu result hinzufügen...

		}
		return result;
	}

	/*
	 * Beispiel für Tokenisierungsproblematik:
	 * 
	 * ttt_Pink_Kreuzfahrtkapitän_O'neill@uni-koeln.ch
	 */

}
