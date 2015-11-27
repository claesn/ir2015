package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.InformationRetrieval;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

/*
 * Erweiterung des invertierten Index: Zusätzlich zu den Werken werden auch
 * jeweils die Positionen des Terms im Werk gespeichert. Der Ursprüngliche
 * Index ist nach wie vor enthalten und kann auf die gewohnte Weise abgefragt werden.
 */
public class PositionalIndex implements InformationRetrieval {

	private Map<String, SortedMap<Integer, List<Integer>>> posIndex;
	private Preprocessor p = new Preprocessor();

	public PositionalIndex(Corpus corpus) {
		long start = System.currentTimeMillis();
		System.out.println("Erstelle Index ...");
		posIndex = index(corpus);
		System.out.println("Index erstellt, Dauer: " + (System.currentTimeMillis() - start) + " ms.");
	}

	/*
	 * Statt Postings-Listen jetzt Postings-Maps mit Pos-Listen für jedes Werk.
	 */
	private Map<String, SortedMap<Integer, List<Integer>>> index(Corpus corpus) {

		Map<String, SortedMap<Integer, List<Integer>>> posIndex = new HashMap<String, SortedMap<Integer, List<Integer>>>();
		// wir indexieren wieder Werk für Werk:
		List<String> works = corpus.getWorks();
		for (int i = 0; i < works.size(); i++) {
			String work = works.get(i);
			// für die Positionen brauchen wir hier die Tokens (anstelle der Terme)
			List<String> tokens = p.tokenize(work);
			int c = 0;
			for (String term : tokens) {
				// wir holen uns die postings-Map des terms aus dem Index:
				SortedMap<Integer, List<Integer>> postings = posIndex.get(term);
				// beim ersten Vorkommen des Terms ist diese noch leer (null), also legen wir uns einfach eine neue an:
				if (postings == null) {
					postings = new TreeMap<Integer, List<Integer>>();
					posIndex.put(term, postings);
				}
				// ebenso bei den Positionslisten:
				List<Integer> posList = postings.get(i);
				if (posList == null) {
					posList = new ArrayList<Integer>();
				}
				posList.add(c);
				c++;
				/*
				 * Der Term wird indexiert, indem die Id des aktuellen Werks (= der aktuelle Zählerwert) zusammen mit
				 * der aktualisierten Positions-Liste der postings-Map hinzugefügt wird:
				 */
				postings.put(i, posList);
			}
		}
		return posIndex;
	}

	/*
	 * Die 'einfache' Index-Suche: Gibt Werke zurück, die (Teil-)queries
	 * enthalten. Einziger Unterschied: Zugriff auf Postings über keySet().
	 */
	public Set<Integer> search(String query) {
		List<String> queries = p.tokenize(query);
		List<Set<Integer>> allPostings = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			// Einzige Veränderung ggü der Suche im invertierten Index: Wir nehmen das keySet der Postings-Maps
			SortedSet<Integer> postings = (SortedSet<Integer>) posIndex.get(q).keySet();
			allPostings.add(postings);
		}
		Set<Integer> result = allPostings.get(0);
		for (Set<Integer> postings : allPostings) {
			// result.addAll(postings);// OR-Verknüpfung
			// result.retainAll(postings);// UND-Verknüpfung
			Intersection.of(result, postings);// AND-Verknüpfung
		}
		return result;
	}

	/*
	 * Suche mit Beschränkung durch 'Nähe'. Grundidee: Positional Index als
	 * erweiterte Indexstruktur - zuerst wie bisher die Werke ermitteln, in
	 * denen beide Terme vorkommen, dann die PositionalIntersection
	 * "zuschalten". Vorteil: einfach "einklinken", ohne den Rest zu verändern.
	 */
	public SortedMap<Integer, List<Integer>> proximitySearch(String query, int k) {
		long start = System.currentTimeMillis();
	List<String> queries = p.getTerms(query);
		List<SortedMap<Integer, List<Integer>>> allPostingsMaps = new ArrayList<>();
		for (String q : queries) {
			SortedMap<Integer, List<Integer>> postingsMap = posIndex.get(q);
			allPostingsMaps.add(postingsMap);
		}
		// Ergebnis ist die Schnittmenge (Intersection) der ersten Map...
		SortedMap<Integer, List<Integer>> result = allPostingsMaps.get(0);
		// ... mit allen weiteren:
		for (SortedMap<Integer, List<Integer>> postingsMap : allPostingsMaps) {

			result = Intersection.of(result, postingsMap, k);

		}
		System.out.println("Proximity-Suche (range " + k + "): "
				+ (System.currentTimeMillis() - start) + " ms.");
		return (SortedMap<Integer, List<Integer>>) result;
	}

}
