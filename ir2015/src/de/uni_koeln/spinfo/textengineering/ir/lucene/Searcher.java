package de.uni_koeln.spinfo.textengineering.ir.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

public class Searcher {

	private IndexSearcher searcher;
	private DirectoryReader reader;
	private int totalHits;

	public Searcher(String luceneDir) throws IOException {
		/* Das Lucene-Verzeichnis: */
		Directory directory = new SimpleFSDirectory(new File(luceneDir).toPath());
		/* Der Reader ist für den Lese-Zugriff auf das Index-Verzeichnis zuständig: */
		reader = DirectoryReader.open(directory);
		/* Der IndexSearcher ist im Wesentlichen ein Wrapper um den Reader: */
		searcher = new IndexSearcher(reader);
	}

	/*
	 * Kapselt die Schritte B.2 bis B.4 (B.1 ist in unserer Applikation nicht vorgesehen).
	 */
	public void search(String searchPhrase) throws ParseException, IOException {

		System.out.println("Search for " + searchPhrase);
		/*
		 * B.2: build query - Analog zur Indexierung können wir hier einen Preprocessor nutzen (QueryParser):
		 */
		Query query;
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser("contents", analyzer);
		query = parser.parse(searchPhrase);
		/*
		 * ... oder uns eine Query selbst bauen (Lucene stellt eine Reihe von versch. Query-Typen bereit):
		 */
		// query = new TermQuery(new Term("contents",query));
		// query = new PrefixQuery(new Term("contents",query));
		// query = new FuzzyQuery(new Term("contents",query));
		// ...
		System.out.println("Lucene-Query: " + query);
		/*
		 * B.3: Search query - Lucene stellt verschiedene search-Methoden bereit, die in der Regel ein gewichtetes
		 * Ergebnis zurückgeben.
		 */
		TopDocs topDocs = searcher.search(query, 20);
		totalHits = topDocs.totalHits;// für Tests
		System.out.println(totalHits + " Treffer für " + searchPhrase);
		/*
		 * B.4: Render results - Das Gegenstück zur buildDocument()-Methode beim Indexieren: Je nachdem, was dort
		 * definiert wurde, können hier die Felder einzeln angesprochen und ausgelesen werden.
		 */
		renderResults(topDocs);
	}

	private void renderResults(TopDocs topDocs) throws IOException {
		/*
		 * Analog zum Erstellen von Dokumenten im Indexer können wir hier die enthaltenen Felder ausgeben:
		 */
		for (int i = 0; i < topDocs.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			Document doc = searcher.doc(scoreDoc.doc);
			System.out.print("docId: " + doc.get("docId") + " - ");
			System.out.println(doc.get("title"));
			// System.out.println("Datum: " + doc.get("indexDate"));
			// System.out.println("text"+doc.get("text"));//der gesamte text
		}
	}

	/*
	 * Beim Umgang mit Ressourcen ist es immer gut, diese explizit freizugeben.
	 */
	public void close() throws IOException {
		reader.close();
	}

	/*
	 * Hilfsmethode für Assertions in unseren Tests
	 */
	public int totalHits() {
		return totalHits;
	}

	/*
	 * Hilfsmethode für Assertions in unseren Tests
	 */
	public int indexSize() {
		return reader.maxDoc();
	}

}
