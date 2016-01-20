package de.uni_koeln.spinfo.textengineering.ir.ranked;

public class TermWeighting {

	/**
	 * Umsetzung der tfIdf-Formel aus dem Seminar (siehe Folien).
	 * 
	 * @param t
	 * @param document
	 * @param index
	 * 
	 * @return Der tf-idf-Wert für t in document.
	 */
	public static double tfIdf(String t, Document document, InformationRetrieval index) {
		/*
		 * Abweichend zu den Folien im Seminar verzichten wir hier auf das sog. 'Add-one-smoothing' und auf den
		 * Logarithmus, beides greift erst bei größeren Sammlungen:
		 */
		double tf = document.getTf(t);
		double n = index.getWorks().size();
		double df = index.getDocFreq(t);
		double idf = Math.log(n / df);
		return tf * idf;
	}

}
