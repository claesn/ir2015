package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class EditDistance implements StringSimilarity {

	/**
	 * Stringvergleich mittels Levenshtein-Distanz, hier unter Verwendung der StringUtils (Apache Commons).
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.ir.tolerant.StringSimilarity#getVariants(java.lang.String,
	 *      java.util.Set)
	 */
	public List<String> getVariants(String q, Set<String> terms) {

		List<String> variants = new ArrayList<String>();
		for (String t : terms) {
			/*
			 * Die Levenshtein-Distanz entspricht der Anzahl an Operationen (insert, delete, replace), die nötig sind
			 * um einen String in einen anderen String zu überführen (hier: jeweils q und t).
			 */
			double levenshteinDistance = StringUtils.getLevenshteinDistance(q, t);
			if (levenshteinDistance <= 2) {
				// if(levenshteinDistance / t.length() < 0.5){// Alternativ unter Berücksichtigung der Länge
				variants.add(t);
			}
		}
		return variants;
	}

}
