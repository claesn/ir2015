package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.InformationRetrieval;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class InvertedIndex implements InformationRetrieval {

	// der invertierte Index für die spätere Suche
	private Map<String, SortedSet<Integer>> index;
	private Preprocessor p = new Preprocessor();

	public InvertedIndex(Corpus corpus) {
		long start = System.currentTimeMillis();
		System.out.println("Erstelle Index ...");
		index = index(corpus);
		System.out.println("Index erstellt, Dauer: " + (System.currentTimeMillis() - start) + " ms.");
	}

	private Map<String, SortedSet<Integer>> index(Corpus corpus) {

		Map<String, SortedSet<Integer>> invIndex = new HashMap<String, SortedSet<Integer>>();
		List<String> works = corpus.getWorks();
		for (int i = 0; i < works.size(); i++) {
			String work = works.get(i);
			// an dieser Stelle können wir einen 'richtigen' Tokenizer einsetzen:
			List<String> terms = p.getTerms(work);
			for (String term : terms) {
				// wir holen uns die postings-Liste des terms aus dem Index:
				SortedSet<Integer> postings = invIndex.get(term);
				/*
				 * beim ersten Vorkommen des Terms ist diese noch leer (null), also legen wir uns einfach eine neue an:
				 */
				if (postings == null) {
					postings = new TreeSet<Integer>();
					invIndex.put(term, postings);
				}
				/*
				 * Der Term wird indexiert, indem die Id des aktuellen Werks (= der aktuelle Zählerwert) der
				 * postings-list hinzugefügt wird:
				 */
				postings.add(i);
			}
		}
		return invIndex;
	}

	@Override
	public Set<Integer> search(String query) {
		long start = System.currentTimeMillis();
		// Wir müssen den gleichen Preprocessor benutzen wie oben!
		List<String> queries = p.getTerms(query);
		/*
		 * Wir holen uns zunächst die Postings-Listen der Teilqueries:
		 */
		List<Set<Integer>> allPostings = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			SortedSet<Integer> postings = index.get(q);
			allPostings.add(postings);
		}
		// Ergebnis ist die Schnittmenge (Intersection) der ersten Liste...
		Set<Integer> result = allPostings.get(0);
		// ... mit allen weiteren:
		for (Set<Integer> postings : allPostings) {
			// result.addAll(postings);// OR-Verknüpfung
			result.retainAll(postings);// UND-Verknüpfung
		}
		System.out.println("Suchdauer: " + (System.currentTimeMillis() - start) + " ms.");
		return result;
	}

}
