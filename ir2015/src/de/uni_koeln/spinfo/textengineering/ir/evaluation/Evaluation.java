package de.uni_koeln.spinfo.textengineering.ir.evaluation;

import java.util.ArrayList;
import java.util.List;

import de.uni_koeln.spinfo.textengineering.ir.ranked.Document;

/*
 * Evaluation (Precision, Recall, F-Maß) einer Query und einer Dokumentenmenge gegen einen Goldstandard.
 */
public class Evaluation {

	private List<Document> relevant;

	public Evaluation(List<Document> goldstandard) {
		this.relevant = goldstandard;
	}

	public EvaluationResult evaluate(List<Document> retrieved) {
		/*
		 * Für das Suchergebnis müssen Precision, Recall und F-Maß errechnet werden. Grundlage sind die "true positives"
		 * , die anhand des Goldstandards ermittelt werden.
		 */

		int tp = truePositives(retrieved);
		int fp = retrieved.size() - tp;
		int fn = relevant.size() - tp;

		double p = (double) tp / (tp + fp);
		double r = (double) tp / (tp + fn);
		double f = 2 * p * r / (p + r);

		return new EvaluationResult(p, r, f);
	}

	private int truePositives(List<Document> retrieved) {
		/*
		 * Zur Ermittlung der true positives bilden wir die Schnittmenge der beiden Listen und erhalten dadurch die
		 * Anzahl der gefundenen Elemente, die auch relevant sind:
		 */
		List<Document> tp = new ArrayList<Document>(retrieved);
		tp.retainAll(relevant);
		return tp.size();
	}
}
