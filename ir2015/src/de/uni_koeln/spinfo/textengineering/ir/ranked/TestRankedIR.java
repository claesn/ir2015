package de.uni_koeln.spinfo.textengineering.ir.ranked;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestRankedIR {

	private Corpus corpus;
	private String query;
	// NEU: Da wir in den Tests nur einen Indextyp verwenden, hier auch als Klassenvariable:
	private InvertedIndex index;
	// ... ebenso das Ergebnis, hier nun Document-Objekte statt nur docIds
	private Set<Document> result;
	// NEU: wir benutzen einen Ranker, um das Ergebnis zu bewerten:
	private Ranker ranker;

	@Before
	public void setUp() throws Exception {
		corpus = new Corpus("pg100.txt", "1[56][0-9]{2}\n", "\n");
		index = new InvertedIndex(corpus);
		query = "brutus caesar";
		/*
		 * Ranking erfolgt relativ zu einer Anfrage, deshalb initialisieren wir
		 * den Ranker mit der query:
		 */
		ranker = new Ranker(query, index);
	}

	@Test
	public void unrankedResults() {
		result = index.search(query);
		System.out.println(result.size() + " ungerankte Treffer für " + query);
		assertTrue("Ergebnis sollte nicht leer sein!", result.size() > 0);
		print(new ArrayList<Document>(result));
	}

	@Test
	public void resultRanked() {
		result = index.search(query);
		System.out.println(result.size() + " gerankte Treffer für " + query);
		assertTrue("Ergebnis sollte nicht leer sein!", result.size() > 0);
		// Ergebnis ranken:
		List<Document> rankedResult = ranker.rank(result);
		print(rankedResult);
	}

	/*
	 * Hilfsmethode, um Ergebnisse übersichtlicher darzustellen.
	 */
	public void print(List<Document> resultList) {
		System.out.println("-------------------------------");
		for (Document doc : resultList) {
			System.out.println(doc);
		}
		System.out.println("-------------------------------");
	}
}
