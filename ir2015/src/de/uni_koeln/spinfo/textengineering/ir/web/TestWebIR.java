package de.uni_koeln.spinfo.textengineering.ir.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.ir.web.crawler.Crawler;
import de.uni_koeln.spinfo.textengineering.ir.web.crawler.Parser;

/**
 * Tests for crawling and parsing.
 * 
 * @author Fabian Steeg, Claes Neuefeind
 */
public final class TestWebIR {

	/**
	 * Test simple connection via URL objects.
	 * 
	 * @throws IOException
	 *             If the used URL is invalid
	 */
	@Test
	public void connect() throws IOException {
		/* Grundsätzlich ist das Verbinden mit einer Website trivial: */
		URL url = new URL("http://www.spinfo.uni-koeln.de/");
		/*
		 * Wir bekommen für die URL einen stream, den wir z.B. in einer Schleife lesen könnten:
		 */
		Scanner scanner = new Scanner(url.openStream());
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNextLine())
			sb.append(scanner.nextLine() + "\n");
		scanner.close();
		/*
		 * Wenn wir das tun, haben wir den rohen und zudem oft invaliden HTML-Inhalt, was uns so nicht viel bringt:
		 */
		System.out.println(sb.toString());
	}

	/* Wir wollen ein Dokument an einer URL parsen: */
	private static final String LOC = "http://nlp.stanford.edu/IR-book/html/htmledition/link-analysis-1.html";
	private static final WebDocument DOC = Parser.parse(LOC);

	/**
	 * Test website parsing.
	 * 
	 * @throws MalformedURLException
	 *             If a link from a parsed site is invalid.
	 */
	/*
	 * Grundlage für das Crawling mehrer Dokumente ist zunächst die Verarbeitung eines einzelnen Dokuments:
	 */
	@Test
	public void parse() throws MalformedURLException {
		WebDocument doc = DOC;
		String text = doc.getText();
		Set<String> links = doc.getLinks();
		/* Wir brauchen sowohl einen Inhalt: */
		Assert.assertTrue("Document content should exist", text.length() > 0);
		/* Als auch die ausgehenden Links: */
		Assert.assertTrue("Outgoing links should exist", links.size() > 0);
		System.out.println("Text: " + text);
		System.out.println("Links: " + links);
	}

	/**
	 * Test link normalization (making relative links absolute).
	 * 
	 * @throws MalformedURLException
	 *             If a contained link is invalid
	 */
	@Test
	public void normalize() throws MalformedURLException {
		Set<String> links = DOC.getLinks();
		for (String link : links) {
			/* Die links sollten normalisiert sein: */
			Assert.assertTrue("Links should be absolute", link.startsWith("http://"));
			/* Und nicht zweimal eine Datei enhalten: */
			URL url = new URL(link);
			String file = url.getFile();
			Assert.assertTrue("Links should contain none or one file extension",
					!file.contains(".") || file.split("\\.").length == 2);
		}
	}

	/**
	 * Test document hashing for avoiding duplicates when storing results in a set.
	 */
	@Test
	public void hashing() {
		/*
		 * Wie wollen keine doppelten Dokumente behalten, der Vergleich erfolgt in der WebDocument-Klasse ueber die
		 * Prüfsummen
		 */
		Set<WebDocument> docs = new HashSet<WebDocument>();
		docs.add(Parser.parse(LOC));
		docs.add(Parser.parse(LOC));
		Assert.assertEquals("A set of documents should not contain duplicates", 1, docs.size());
	}

	/**
	 * Test exclusion of forbidden links when parsing sites.
	 */
	@Test
	public void forbidden() {
		/* Und unser Parser, und damit unser Crawler, soll keinen verbotenen Links folgen: */
		WebDocument doc = Parser.parse("http://www.ub.uni-koeln.de/");
		Assert.assertTrue("Forbidden links should not be part of the parsed document",
				/* Wie z.B. dem 'Anmelden'-Link auf der UB-Startseite: */
				!doc.getLinks()
						.contains("http://www.ub.uni-koeln.de/IPS?SERVICE=TEMPLATE&SUBSERVICE=GOTO&LOCATION=USB&LA"
								+ "NGUAGE=de&DEST_SERVICE=SESSION&DEST_SUBSERVICE=GENLOGIN"));
	}

	/**
	 * Test crawling.
	 */
	@Test
	public void crawl() {
		/* Wir testen das Crawling mit einem Seed aus drei URLs auf verschiedenen Servern: */
		List<String> seed = Arrays.asList("http://www.ub.uni-koeln.de/", "http://www.spinfo.uni-koeln.de/",
				"http://nlp.stanford.edu/IR-book/html/htmledition/link-analysis-1.html");
		Assert.assertTrue(Crawler.crawl(0, seed).size() == seed.size());
	}

	/**
	 * Test crawling.
	 */
	@Test
	public void crawlLevel1() {
		List<String> seed = Arrays.asList("http://www.ub.uni-koeln.de/", "http://www.spinfo.uni-koeln.de/",
				"http://nlp.stanford.edu/IR-book/html/htmledition/link-analysis-1.html");
		/* Ein konkreter, längerer Crawl-Vorgang (dauert) */
		final int linksPerSite = 5; // grobe Annahme: > 5 Links / Seite
		Assert.assertTrue(Crawler.crawl(1, seed).size() > seed.size() * linksPerSite);
	}

}
