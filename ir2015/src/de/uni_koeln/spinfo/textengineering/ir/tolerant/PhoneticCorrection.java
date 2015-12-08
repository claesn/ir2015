package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

public class PhoneticCorrection implements StringSimilarity {

	/**
	 * Phonetischer Stringvergleich mit dem Soundex-Algorithmus, hier unter Nutzung der entsprechenden
	 * Codec-Implementation von Apache Commons.
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.ir.tolerant.StringSimilarity#getVariants(java.lang.String,
	 *      java.util.Set)
	 */
	public List<String> getVariants(String q, Set<String> terms) {

		List<String> variants = new ArrayList<String>();
		Soundex soundex = new Soundex();
		try {
			for (String t : terms) {
				if (soundex.difference(q, t) == 4) {
					variants.add(t);
					// Soundex.difference() ergibt Werte von 0-4; hier bedeutet 4 höchste Ähnlichkeit
				}
			}
		} catch (EncoderException e) {
			e.printStackTrace();
		}
		return variants;
	}

}
