package de.uni_koeln.spinfo.textengineering.ir.basic;

import java.util.Set;

public interface InformationRetrieval {

	/*
	 * Information-Retrieval in der einfachsten Form: unabhängig von der konkreten Implementation (z.B. LinearSearch,
	 * TermDocumentMatrix) gibt 'search' die Ids aller Werke zurück, in denen der String "query" vorkommt. Als Ids
	 * nehmen wir die Indexposition der Werke.
	 */
	public Set<Integer> search(String query);

}
