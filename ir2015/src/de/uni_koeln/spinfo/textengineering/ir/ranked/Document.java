package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.List;

import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class Document {

	private String text;
	private String title;

	public Document(String text, String title) {
		this.text = text;
		this.title = title;
	}

	public List<String> getTerms() {
		return new Preprocessor().getTerms(text);
	}

	/*
	 * Mit dem Überschreiben der toString()-Methode sorgen wir dafür, dass bei Ausgabe des Document-Objekts mit sysout
	 * nur der Titel ausgegeben wird
	 */
	@Override
	public String toString() {
		return title;
	}

}
