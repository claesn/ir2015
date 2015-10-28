package de.uni_koeln.spinfo.textengineering.ir.basic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinearSearch {

	private List<String> works;

	public LinearSearch(Corpus corpus) {
		// Corpus wird übergeben, wir benötigen für die Suche die Werke
		this.works = corpus.getWorks();
	}

	public Set<Integer> search(String query) {
		Set<Integer> result = new HashSet<>();

		/*
		 * Lineare Suche: laufe über alle Werke, wenn String gefunden, Indexposition zu result hinzufügen, fertig.
		 * 
		 */

		return result;
	}

}
