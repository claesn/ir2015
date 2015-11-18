package de.uni_koeln.spinfo.textengineering.ir.preprocess;

/*
 * Einfacher Preprocessor: splittet und gibt Tokens oder sortierte Types/Stems zurück.
 */

public final class Preprocessor {

	/*
	 * Ein Unicode-wirksamer Ausdruck für "Nicht-Buchstabe", der auch Umlaute berücksichtigt; die einfache (ASCII)
	 * Version ist: "\\W"
	 */
	private static final String UNICODE_AWARE_DELIMITER = "[^\\p{L}]";
	private static final String ASCII_DELIMITER = "\\W";

	/*
	 * TODO Methoden?
	 */

}
