package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class Document {

	private String text;
	private String title;
	private Map<String, Integer> tf;

	public Document(String text, String title) {
		this.text = text;
		this.title = title;
		this.tf = computeTf();
	}

	/*
	 * In der IR-Logik können Termfrequenzen (tf) direkt beim Indexieren gesammelt werden. Ein Beispiel ist der
	 * PositionalIndex, tf entspricht dort einfach der Länge der Positionslisten. In unserem Beispiel hier gehen wir
	 * davon aus, dass diese Positionslisten nicht zur Verfügung stehen, d.h. wir müssen uns die tf-Werte vorberechnen
	 * und für bequemeren Zugriff in einer Map ablegen.
	 */
	private Map<String, Integer> computeTf() {
		Map<String, Integer> termMap = new HashMap<String, Integer>();
		/* Wir zählen die Häufigkeiten der Tokens: */
		List<String> tokens = new Preprocessor().tokenize(text);
		for (String token : tokens) {
			Integer tf = termMap.get(token);
			/*
			 * Wenn der Term noch nicht vorkam, beginnen wir zu zählen (d.h. wir setzen 1)
			 */
			if (tf == null) {
				tf = 1;
			} else {// sonst zählen wir einfach bei jedem Vorkommen hoch
				tf = tf + 1;
			}
			termMap.put(token, tf);
		}
		return termMap;
	}

	public List<String> getTerms() {
		return new ArrayList<String>(tf.keySet());
	}

	/*
	 * Zugriff auf Tf-Werte (für Termgewichtung)
	 */
	public double getTf(String t) {
		Integer integer = tf.get(t);
		return integer == null ? 0 : integer;
	}

	/*
	 * Mit dem Überschreiben der toString()-Methode sorgen wir dafür, dass bei Ausgabe des Document-Objekts mit sysout
	 * nur der Titel ausgegeben wird
	 */
	@Override
	public String toString() {
		return title;
	}

	public Double similarity(Document query, InformationRetrieval index) {

		List<Double> queryVec = query.computeVector(index);
		List<Double> docVec = this.computeVector(index);

		return cosinus(queryVec, docVec); // TODO Berechnung?
	}

	private List<Double> computeVector(InformationRetrieval index) {

		List<String> terms = index.getTerms();
		/*
		 * Ein Vektor für dieses Dokument ist eine Liste (Länge = Anzahl Terme insgesamt)
		 */
		List<Double> vector = new ArrayList<Double>();
		Double tfIdf;
		/* ...und dieser Vektor enthält für jeden Term im Vokabular... */
		for (String term : terms) {
			/*
			 * ...den tfIdf-Wert des Terms (Berechnung in einer eigenen Klasse):
			 */
			tfIdf = getTfIdf(term);// TODO Berechnung?
			vector.add(tfIdf);
		}

		return vector;
	}

}
