package de.uni_koeln.spinfo.textengineering.ir.evaluation;

/**
 * Ergebnisdarstellung einer Evaluation. Die Klasse kapselt die Werte für Precision, Recall und F-Maß. Durch
 * Überschrieben der toString()-Methode erzeugen wir eine Ausgabe der Form:
 * 
 * "EvaluationResult mit p=0,31, r=0,92 and f=0,46".
 * 
 * @author spinfo
 */
public class EvaluationResult {

	private double p;
	private double r;
	public double f;

	public EvaluationResult(double p, double r, double f) {
		this.p = p;
		this.r = r;
		this.f = f;
	}

	@Override
	public String toString() {
		/*
		 * Mit String.format können wir die Ausgabe vorformatieren, z.B. die Zahlen auf zwei Nachkommastellen
		 * formatieren (%.2f):
		 */
		return String.format("Ergebnis mit p=%.2f ,  r=%.2f, f=%.2f", p, r, f);
	}
}
