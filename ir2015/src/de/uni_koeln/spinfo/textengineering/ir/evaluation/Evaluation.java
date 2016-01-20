package de.uni_koeln.spinfo.textengineering.ir.evaluation;

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

	public EvaluationResult evaluate(List<Document> unranked) {
		/*
		 * TODO Für das Suchergebnis müssen Precision, Recall und F-Maß errechnet werden. Grundlage sind die
		 * "true positives", die anhand des Goldstandards ermittelt werden.
		 * 
		 * Rückgabe ist ein Objekt vom Typ EvaluationResult, das die Werte kapselt.
		 */
		return null;
	}

}
