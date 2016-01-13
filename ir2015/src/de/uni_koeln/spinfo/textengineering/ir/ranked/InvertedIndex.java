package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uni_koeln.spinfo.textengineering.ir.boole.Intersection;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class InvertedIndex implements InformationRetrieval {

	private Map<String, SortedSet<Integer>> index;
	// NEU: Korpus für Zugriff auf Werke
	private Corpus corpus;

	public InvertedIndex(Corpus corpus) {
		long start = System.currentTimeMillis();
		this.corpus = corpus;// NEU: Korpus mit ablegen
		index = index(corpus);
		System.out.println("Index erstellt, Dauer: "
				+ (System.currentTimeMillis() - start) + " ms.");
	}

	private Map<String, SortedSet<Integer>> index(Corpus corpus) {
		HashMap<String, SortedSet<Integer>> index = new HashMap<String, SortedSet<Integer>>();
		// NEU: 'Documents' statt Strings
		List<Document> works = corpus.getWorks();
		for (int i = 0; i < works.size(); i++) {
			// NEU: Preprocessor muss schon im Document eingesetzt werden
			List<String> terms = works.get(i).getTerms();
			// der Rest bleibt wie bisher ...
			for (String t : terms) {
				SortedSet<Integer> postings = index.get(t);
				if (postings == null) {
					postings = new TreeSet<Integer>();
					index.put(t, postings);
				}
				postings.add(i);
			}
			// printSortedIndexTerms(index);//optionale Ausgabe der Indexterme
		}
		return index;
	}

	/*
	 *  NEU: Rückgabe von Documents anstelle von docIds.
	 */
	@Override
	public Set<Document> search(String query) {
		List<String> queries = new Preprocessor().tokenize(query);
		List<SortedSet<Integer>> allPostings = new ArrayList<SortedSet<Integer>>();
		for (String q : queries) {
			SortedSet<Integer> postings = index.get(q);
			allPostings.add(postings);
		}
		Collections.sort(allPostings, new Comparator<SortedSet<Integer>>() {
			public int compare(SortedSet<Integer> o1, SortedSet<Integer> o2) {
				return Integer.valueOf(o1.size()).compareTo(o2.size());
			}
		});
		SortedSet<Integer> result = allPostings.get(0);
		for (SortedSet<Integer> set : allPostings) {
			result = Intersection.of(result, set);
		}
		/*
		 * NEU: Abschließend holen wir zu jeder docId das passende Document,
		 * indem wir sie uns direkt vom Korpus geben lassen ...
		 */
		Set<Document> resultAsDocSet = new HashSet<Document>();
		for (Integer docId : result) {
			Document doc = getWorks().get(docId);
			resultAsDocSet.add(doc);
		}
		return resultAsDocSet;
	}

	/*
	 *  Alle Dokumente.
	 */
	public List<Document> getWorks() {
		return corpus.getWorks();
	}

	@Override
	public List<String> getTerms() {
		return new ArrayList<String>(index.keySet());
	}

	/*
	 *  Die Dokumentenfrequenz zu einem Term:
	 */
	public Integer getDocFreq(String term) {
		return index.get(term).size();
	}

}
