package de.sfn_kassel.plone_crawler.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import de.sfn_kassel.plone_crawler.test.PageContent.Type;

public class Page {

	URL url;
	PageContent content;

	public Page(URL url) {
		this.url = url;
	}

	public void loadPage() {
		URLConnection connection = null;
		try {
			connection = url.openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			String content = scanner.next();
			scanner.close();
			this.content = new PageContent(content, Type.Success);
		} catch (Exception e) {
			this.content = new PageContent("error", Type.Error);
		}
	}

	public void writePage() {

	}

	public Page[] getLinks() {
		String[] href = content.toString().split("href=\"");
		for (int i = 0; i < href.length; i++) {
			href[i] = href[i].split("\"")[0];
		}

		String[] src = content.toString().split("src=\"");
		for (int i = 0; i < src.length; i++) {
			src[i] = src[i].split("\"")[0];
		}
		// System.out.println("ping");
		// String[] raw = (String[]) Stream.concat(Arrays.stream(href),
		// Arrays.stream(href)).toArray();
		String[] raw = Arrays.copyOf(href, href.length+src.length);
		System.arraycopy(src, 0, raw, href.length, src.length);
		// System.out.println("pong");
		ArrayList<Page> links = new ArrayList<Page>();
		for (int i = 0; i < raw.length; i++) {
			try {
				if (raw[i].startsWith("../")) {
					URL superURL = url;
					do {
						String s = superURL.toString();
						superURL = new URL(s.substring(0, s.lastIndexOf('/')));
						raw[i] = raw[i].substring(3);
					} while (raw[i].startsWith("../"));
					raw[i] = superURL.toString() + "/" + raw[i];
				} else if (raw[i].startsWith("/")) {
					String host = url.getHost();
					raw[i] = url.toString().substring(0, url.toString().indexOf(host)+host.length()) + raw[i];
				}
				URL newUrl = new URL(raw[i]);
				links.add(new Page(newUrl));
			} catch (MalformedURLException e) {
				// e.printStackTrace();
			}
		}
		return links.toArray(new Page[1]);
	}

	@Override
	public String toString() {
		return "[Page: " + this.url.toString() + " loaded: " + (content != null) + "]";
	}
	
}
