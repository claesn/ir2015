package de.uni_koeln.spinfo.textengineering.ir.evaluation;

import java.util.Arrays;
import java.util.List;

public class SampleGoldStandard {

	/* Konzeptuell besteht ein Goldstandard aus so etwas wie folgt: */
	SampleGoldStandard(String description, List<String> keywords,
			List<String> documents) {
	}

	/*
	 * ... den man dann (an anderer Stelle) z.B. so benutzen könnte:
	 */
	SampleGoldStandard royal = new SampleGoldStandard(
			"Werke von Shakespeare über Königsfamilien", // Beschreibung
			Arrays.asList("King", "Prince", "Queen"), // Suchbegriffe
			Arrays.asList("Hamlet", "Othello", "King Henry")); // Werke

	SampleGoldStandard love = new SampleGoldStandard(
			"Romantische Werke von Shakespeare", // Beschreibung
			Arrays.asList("Love", "Man", "Woman"), // Suchbegriffe
			Arrays.asList("Romeo and Juliet", "A midsummer night's dream"));// Werke
}
