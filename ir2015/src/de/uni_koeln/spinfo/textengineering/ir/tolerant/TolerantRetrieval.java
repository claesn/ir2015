package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.boole.InvertedIndex;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class TolerantRetrieval extends InvertedIndex {

	public TolerantRetrieval(Corpus corpus) {
		super(corpus);
	}

	/*
	 * Tolerante Suche: wird für eine (Teil-)query kein Treffer gefunden, werden Alternativen ermittelt.
	 */
	public Set<Integer> searchTolerant(String query, StringSimilarity tr) {
		long start = System.currentTimeMillis();
		List<String> queries = new Preprocessor().getTerms(query);
		List<Set<Integer>> allPostings = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			Set<Integer> postings = super.index.get(q);
			/*
			 * Im Unterschied zu oben machen wir hier einen null-Check und ermitteln ggf. Alternativen.
			 */
			if (postings == null) {
				System.out.println("Keine Treffer für Suchwort '" + q + "', suche Varianten ...");
				String best = getBestVariant(q, tr);// this, index.keySet());
				System.out.println("Zeige Ergebnisse für: " + best);
				postings = index.get(best);
			}
			allPostings.add(postings);
		}
		Set<Integer> result = allPostings.get(0);
		for (Set<Integer> postings : allPostings) {
			result.retainAll(postings);
		}
		System.out.println("Suchdauer: " + (System.currentTimeMillis() - start) + " ms.");
		return result;
	}

	/**
	 * Gibt diejenige Alternative zu einem Query-Term zurück, die die meisten Suchtreffer verspricht.
	 * 
	 * @param q
	 * @param tr
	 * @return die 'beste' Alternative
	 */
	public String getBestVariant(String q, StringSimilarity tr) {

		// Wir holen uns zunächst Varianten zur query (abweichend je nach konkreter Implementierung):
		List<String> alternatives = tr.getVariants(q, index.keySet());
		/*
		 * Und ermitteln dann die 'beste' Variante, indem wir zu jedem Element der Liste eine eigene Suche starten und
		 * das Element mit der längsten Trefferliste behalten. Hierfür müssen wir die Ergebnisse absteigend sortieren:
		 */
		Map<Integer, String> postingsForQuery = new TreeMap<Integer, String>(Collections.reverseOrder());
		for (String a : alternatives) {
			// Wir können hier die 'normale' Suche einsetzen, da wir sicher wissen, dass die Varianten im Index sind:
			Set<Integer> result = search(a);
			postingsForQuery.put(result.size(), a);
		}
		String bestResult = postingsForQuery.values().iterator().next();// das erste Element der sortierten Map
		System.out.println("Beste Alternativen: " + postingsForQuery);
		return bestResult;
	}

}
