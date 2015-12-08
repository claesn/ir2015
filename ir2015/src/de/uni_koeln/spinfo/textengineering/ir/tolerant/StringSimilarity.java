package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.List;
import java.util.Set;

public interface StringSimilarity {

	/**
	 * Findet in einer Liste von Termen die 'nächsten' Elemente zu q, je nach Implementation auf Grundlage der
	 * Levenshtein-Distanz, des Soundex-Algorithmus, oder anderen Methoden des Stringvergleichs.
	 * 
	 * @param q
	 * @param terms
	 * @return eine Liste von Varianten zu String q.
	 */
	public List<String> getVariants(String q, Set<String> terms);

}
