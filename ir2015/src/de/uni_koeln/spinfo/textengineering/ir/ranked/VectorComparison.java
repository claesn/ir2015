package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.List;

public class VectorComparison {

	/**
	 * Berechnung der Cosinus-Ähnlichkeit auf Grundlage von 'dot product'
	 * und euklidischer Länge (siehe Folien)
	 * 
	 * @param v1
	 * @param v2
	 * @return Die cosinus-Ähnlichkeit zweier Vektoren.
	 */
	public static double compare(List<Double> v1, List<Double> v2) {

		double result = dotProduct(v1, v2)
				/ (euclidicLength(v1) * euclidicLength(v2));

		return result;
	}

	private static double dotProduct(List<Double> v1, List<Double> v2) {
		/*
		 * Dot product: Summe der Produkte der korrespondierenden Vektor-Werte
		 */
		double sum = 0;
		for (int i = 0; i < v1.size(); i++) {
			sum += v1.get(i) * v2.get(i);
		}
		return sum;
	}

	private static double euclidicLength(List<Double> v) {
		/*
		 * Euklidische Länge: Wurzel aus der Summe der quadrierten Elemente
		 */
		double sum = 0;
		for (int i = 0; i < v.size(); i++) {
			sum += Math.pow(v.get(i), 2);
		}
		return Math.sqrt(sum);
	}

}
