package de.sfn_kassel.plone_crawler.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
		} catch (IOException e) {
			this.content = new PageContent("error", Type.Error);
		}
	}

	public void writePage() {
		
	}
	
	public Page[] getLinks() {
		String[] raw = content.toString().split("href=\"");
		for (int i = 0; i < raw.length; i++) {
			raw[i] = raw[i].split("\"")[0];
		}
		Page[] links = new Page[raw.length - 1];
		for (int i = 0; i < links.length; i++) {
			try {
				links[i] = new Page(new URL(raw[i + 1]));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return links;
	}
	
	public Integer getContentHash() {
		return content == null ? null : content.hashCode();
	}
	
	@Override
	public String toString() {
		return "[Page: " + this.url.toString() + " loaded: " + (content != null) + "]";
	}
}
