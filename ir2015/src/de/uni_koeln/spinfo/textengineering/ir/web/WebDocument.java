package de.uni_koeln.spinfo.textengineering.ir.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import de.uni_koeln.spinfo.textengineering.ir.ranked.Document;
import de.uni_koeln.spinfo.textengineering.ir.web.crawler.LinkHelper;

/*
 * Das Ergebnis der Verarbeitung einer URL: Ein Web-Dokument mit der Herkunfts-URL (z.B. für
 * Relevanz-Gewichtungen), dem eigentlichen Inhalt als String und den ausgehenden Links (für
 * weitergehendes Crawling etc.). Hier haben wir die Links einfach im Dokument anstelle einer
 * eigenen Frontier mit front queues und back queues (siehe IR-Buch, Kap. 20)
 */
/**
 * Document representation of a website, consisting of the text, the URL and the outgoing links.
 * 
 * @author Fabian Steeg, Claes Neuefeind
 */
public final class WebDocument extends Document{

	private String url;
	private Set<String> links;
	private long checksum;

	/**
	 * @param url
	 *            The URL this document represents
	 * @param text
	 *            The plain text of this document
	 * @param links
	 *            The outgoing links
	 */
	public WebDocument(final String url, final String content, final Set<String> links) {
		// Wir initialisieren zunächst das Document, als Titel nehmen wir hier die URL 
		super(content, url);
		if (url == null || content == null || links == null) {
			throw new IllegalArgumentException("Document parameters must not be null");
		}
		this.url = url;
		/* Die Links müssen normalisiert werden: */
		Set<String> cleanLinks = LinkHelper.normalize(links, url);
		/* Und sollten keine verbotenen Ziele haben: */
		cleanLinks = LinkHelper.allowed(cleanLinks, url);
		this.links = cleanLinks;

		/*
		 * Für den effizienten Vergleich bilden wir eine Checksum für den Inhalt des Dokument, der zum Vergleich benutzt
		 * wird (siehe equals() weiter unten). Jave bietet verschiedene Implementierung (Strg-T auf Checksum zeigt die,
		 * wie bei allen Interfaces)
		 */
		Checksum cs = new CRC32();
		cs.update(content.getBytes(), 0, content.length());
		this.checksum = cs.getValue();
	}

	/*
	 * Wir überschreiben equals und hashCode damit wir doppelte Dokumente vermeiden können, indem wir einfach eine
	 * entsprechende Datenstruktur verwenden (hier ein Set). Wenn man equals ueberschreibt, muss man auch hashCode
	 * ueberschreiben, da die Collection-Klassen beides pruefen.
	 */
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof WebDocument)) {
			return false;
		}
		/* Vergleich der Checksums statt der Inhalte selbst (effizienter): */
		 return checksum == ((WebDocument) obj).checksum;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (checksum + "").hashCode();
	}

	/* Und wir überschreiben toString um hilfreiche Ausgaben zu bekommen: */
	@Override
	public String toString() {
		return String.format("WebDocument at %s with %s outgoing links and text size %s", url, links.size(),
				getText().length());
	}

	/**
	 * @return The outgoing links
	 */
	public Set<String> getLinks() {
		return links;
	}

	/**
	 * @return The URL
	 */
	public URL getUrl() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
