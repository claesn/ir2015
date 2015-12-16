package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Ranker {

	/**
	 * Ranking des Ergebnis-Sets, implementiert als einfache Sortierung nach Ähnlichkeit zur Query.
	 * 
	 * @param result
	 *            - Das unsortierte (boolesche) Ergebnis.
	 * @param query
	 *            - Die query als Document-Objekt.
	 * @return Sortierte Liste.
	 */
	public List<Document> rank(Set<Document> result, Document query, InformationRetrieval index) {

		// result wird zunächst in eine Liste umgewandelt:
		ArrayList<Document> toSort = new ArrayList<Document>(result);
		/*
		 * Java stellt für Collections (Listen, Maps, etc) die Methode sort() bereit, der man einen Sortierschlüssel
		 * (einen Comparator) übergeben kann. Wir wollen Dokumente anhand ihrer Ähnlichkeit zur query sortieren, deshalb
		 * müssen wir uns zunächst einen geeigneten Comparator schreiben:
		 */
		Collections.sort(toSort, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				/*
				 * Wir sortieren alle Vektoren nach ihrer (Cosinus-) Ähnlichkeit zur Anfrage (query), dazu benötigen wir
				 * zunächst die Ähnlichkeiten von d1 zum Query und d2 zum Query:
				 */
				Double s1 = d1.similarity(query, index);
				Double s2 = d2.similarity(query, index);
				/*
				 * Anschließend sortieren wir nach diesen beiden Ähnlichkeiten. Wir wollen absteigende Ähnlichkeit, d.h.
				 * s2.compareTo(s1) statt s1.compareTo(s2) d.h. die höchsten Werte und damit besten Treffer zuerst:
				 */
				return s2.compareTo(s1);
			}

		});
		return toSort;
	}

}
