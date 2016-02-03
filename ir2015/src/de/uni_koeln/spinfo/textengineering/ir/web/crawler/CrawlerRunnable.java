/**
 * Material for the course 'Information-Retrieval', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-informationretrieval.html)
 * <p/>
 * Copyright (C) 2008-2009 Fabian Steeg
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_koeln.spinfo.textengineering.ir.web.crawler;

import java.util.List;
import java.util.Set;

import de.uni_koeln.spinfo.textengineering.ir.web.WebDocument;

/*
 * Ein Runnable, die Einheit dessen, was parallel zu tun ist. In unserem Fall
 * heisst das: Von einer Ausgangs-URL bis zur gewünschten Tiefe crawlen. Wir
 * wollen nicht jeden Link parallel verfolgen, da die meisten Links auf dem
 * gleiche Host liegen und dies zur Folge hätte, dass aus vielen (bei spiegel.de
 * z.B. mehrere hundert) Threads gleichzeit Anfragen an einen host gingen, was
 * nicht höflich wäre und auch nicht funktionieren würde.
 */
/**
 * A crawler runnbale for concurrent crawling.
 * 
 * @author Fabian Steeg
 */
class CrawlerRunnable implements Runnable {

	private static final int CRAWL_DELAY_MS = 100;
	private List<WebDocument> docs;
	private int depth;
	private String url;
	private int max;

	/**
	 * @param url
	 *            The URL of the page to crawl
	 * @param depth
	 *            The depth (0 means only the given page, 1 all links on the page, etc.)
	 * @param docs
	 *            The list to add the crawled document to
	 * @param max
	 */
	public CrawlerRunnable(final String url, final int depth, final List<WebDocument> docs, final int max) {
		this.url = url;
		this.depth = depth;
		this.docs = docs;
		this.max = max;
	}

	/*
	 * Die Einstiegsmethode: wird vom ExecutorService aufgerufen
	 */
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		crawl(url, 0);
	}

	/*
	 * Die rekursive crawling-Methode: parst das Dokument an der angegebenen URL, fügt das Ergebnis der Collection hinzu
	 * und wenn i kleiner als das Level-Limit ist, ruft sie sich selbst mit allen ausgehenden Links des Dokuments an der
	 * angegebenen URL auf.
	 */
	private void crawl(final String urlToCrawl, final int i) {
		if (docs.size() >= max) {
			return;
		}
		WebDocument document = Parser.parse(urlToCrawl);
		if (document != null) {
			docs.add(document);
			/* Ein Mindestmass an Verzögerung, dann die Rekursion: */
			delay();
			if (i < depth) {
				int j = i + 1;
				System.out.println("Crawling " + document.getLinks().size() + " URLs on level " + j);
				Set<String> links = document.getLinks();
				for (String out : links) {
					crawl(out, j);
				}
			}
		}
	}

	private void delay() {
		try {
			Thread.sleep(CRAWL_DELAY_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
