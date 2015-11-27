package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Intersection {

	/*
	 * Implementierung der Listen-Intersection, die die Sortierung der Listen ausnutzt, fast Zeile-für-Zeile umgesetzt
	 * wie in Manning et al. 2008, S. 11, beschrieben.
	 */
	public static SortedSet<Integer> of(Set<Integer> result, Set<Integer> postings) {

		SortedSet<Integer> answer = new TreeSet<Integer>();
		Iterator<Integer> it1 = result.iterator();
		Iterator<Integer> it2 = postings.iterator();
		// ein einfaches 'next()' reicht leider nicht - s. Kommentar unten
		Integer p1 = nextOrNull(it1);
		Integer p2 = nextOrNull(it2);
		while (p1 != null && p2 != null) {
			if (p1 == p2) {
				answer.add(p1);
				p1 = nextOrNull(it1);
				p2 = nextOrNull(it2);
			} else if (p1 < p2) {
				p1 = nextOrNull(it1);
			} else {
				p2 = nextOrNull(it2);
			}
		}
		return answer;
	}

	/*
	 * Um nah am Pseudocode zu bleiben, müssen wir u.a. NoSuchElementExceptions vermeiden (wenn der Iterator keine
	 * Elemente mehr hat).
	 */
	protected static Integer nextOrNull(Iterator<Integer> i1) {
		return i1.hasNext() ? i1.next() : null;
	}

	// Unsere bisherige Umsetzung mithilfe der Java-API:
	public static SortedSet<Integer> ofApi(SortedSet<Integer> pl1, SortedSet<Integer> pl2) {
		SortedSet<Integer> answer = new TreeSet<Integer>(pl1);
		answer.retainAll(pl2);
		return answer;
	}

	public static SortedMap<Integer, List<Integer>> of(SortedMap<Integer, List<Integer>> result,
			SortedMap<Integer, List<Integer>> postings, int k) {

		// geänderter Rückgabetyp:
		SortedMap<Integer, List<Integer>> answer = new TreeMap<Integer, List<Integer>>();

		Iterator<Integer> it1 = result.keySet().iterator();
		Iterator<Integer> it2 = postings.keySet().iterator();
		// ein einfaches 'next()' reicht leider nicht - s. Kommentar unten
		Integer p1 = nextOrNull(it1);
		Integer p2 = nextOrNull(it2);

		while (p1 != null && p2 != null) {
			if (p1 == p2) {
				// bis hier bleibt alles gleich (bis auf den Rückgabetyp)
				// answer.add(p1); (siehe oben) muss ersetzt werden durch:

				List<Integer> l = new ArrayList<Integer>();
				Iterator<Integer> posIt1 = result.get(p1).iterator();
				Iterator<Integer> posIt2 = postings.get(p2).iterator();
				Integer pp1 = nextOrNull(posIt1);
				Integer pp2 = nextOrNull(posIt2);
				while (pp1 != null) {
					while (pp2 != null) {

						// ... hier k prüfen etc.
						if (Math.abs(pp1 - pp2) <= k) {
							l.add(pp2);
						}

						/*
						 * TODO Umsetzung der positionalIntersect gemäß Folien ...
						 */

					}
				}
				// ab hier bleibt der Rest des Algorithmus wie bisher:
				p1 = nextOrNull(it1);
				p2 = nextOrNull(it2);
			} else if (p1 < p2) {
				p1 = nextOrNull(it1);
			} else {
				p2 = nextOrNull(it2);
			}
		}

		return answer;

	}

}
