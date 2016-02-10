package de.uni_koeln.spinfo.textengineering.ir.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import de.uni_koeln.spinfo.textengineering.ir.ranked.Corpus;

public class Indexer {

	// das Herzstück der Lucene-Indexierung ist der sog. IndexWriter:
	private IndexWriter writer;

	public Indexer(String indexDir) throws IOException {
		/* Das Verzeichnis, in dem der Index gespeichert wird: */
		Directory luceneDir = new SimpleFSDirectory(new File(indexDir).toPath());
		/* Der Analyzer ist für das Preprocessing zuständig (Tokenizing etc) */
		Analyzer analyzer = new StandardAnalyzer();
		/* Der IndexWriter wird mit dem Analyzer konfiguriert: */
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(luceneDir, conf);
	}

	/*
	 * Kapselt die Schritte A.1 bis A.4
	 */
	public void index(Corpus corpus) throws IOException {

		System.out.print("Indexing corpus ");
		writer.deleteAll();
		/*
		 * Schritt A.1 (acquire content) ist hier im Grunde bereits abgehakt. Dass wir hier einfach unser Corpus
		 * übergeben ist dabei ein Spezialfall. Der allgemeinere Fall: Lesen von Textdateien aus einem Verzeichnis.
		 */
		List<de.uni_koeln.spinfo.textengineering.ir.ranked.Document> works = corpus.getWorks();
		for (de.uni_koeln.spinfo.textengineering.ir.ranked.Document work : works) {
			/*
			 * Schritt A.2: build document (alternativ könnten wir die Lucene-Documents auch schon direkt in der
			 * Corpus-Klasse erstellen, dann wären die Schritte A.1 und A.2 hier bereits abgehakt).
			 */
			Document doc = buildLuceneDocument(work.getText(), work.getTitle(), corpus.getDocId(work));
			/*
			 * Schritt A.3 + A.4: analyze + index document
			 */
			writer.addDocument(doc);
			System.out.print(".");
		}
		System.out.println(" " + writer.numDocs() + " Dokumente indexiert.");
	}

	/*
	 * Schritt A.2: 'build document' - Die Klasse Document ist ein Container für sog. 'Fields', welche die eigentlichen
	 * Daten kapseln. Strukturell ähnelt ein Field einer Map<Key, Value>, d.h. auf einen Key (ID) wird ein Value
	 * (textuelle Daten) abgebildet.
	 */
	private Document buildLuceneDocument(String text, String title, int docId) {
		Document doc = new Document();
		doc.add(new TextField("contents", text, Store.NO));
		doc.add(new TextField("title", title, Store.YES));
		doc.add(new TextField("text", text, Store.YES));
		// StringField wird nicht tokenisiert, gut z.B. für Sortierung
		doc.add(new StringField("title", title, Store.YES));
		doc.add(new IntField("docId", docId, Store.YES));
		/* Noch ein Beispiel: Zeitpunkt der Indexierung: */
		doc.add(new StringField("indexDate", DateTools.dateToString(new Date(), DateTools.Resolution.MINUTE),
				Field.Store.YES));
		return doc;
	}

	/*
	 * Beim Umgang mit Ressourcen ist es immer gut, diese explizit freizugeben.
	 */
	public void close() throws IOException {
		writer.close();
	}

	/*
	 * Hilfsmethode für unsere Tests.
	 */
	public int getNumDocs() {
		return writer.numDocs();
	}
}