package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.List;
import java.util.Set;

/*
 * Information-Retrieval in der einfachsten Form: unabhängig von der
 * konkreten Implementation (z.B. LinearSearch, TermDocumentMatrix) gibt
 * 'search' die Ids aller Werke zurück, in denen der String "query"
 * vorkommt. Als Ids nehmen wir die Indexposition der Werke.
 */

public interface InformationRetrieval {

	// NEU: Rückgabe von Documents (statt docIds)
	Set<Document> search(String query);

	/*
	 * Neu: Ergänzungen, damit weiterhin 'generisch' mit dem Interface gearbeitet werden kann:
	 */
	List<String> getTerms();

	List<Document> getWorks();

	Integer getDocFreq(String term);

}
