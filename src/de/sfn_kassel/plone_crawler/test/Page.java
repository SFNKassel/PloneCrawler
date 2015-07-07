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
		System.out.println(url);
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
		String[] raw = content.toString().split("href=\"");
		for (int i = 0; i < raw.length; i++) {
			raw[i] = raw[i].split("\"")[0];
		}
		ArrayList<Page> links = new ArrayList<Page>();
		for (int i = 0; i < raw.length; i++) {
			try {
				URL newUrl = new URL(raw[i]);
				if (checkURL(url))
					links.add(new Page(newUrl));
			} catch (MalformedURLException e) {
//				e.printStackTrace();
			}
		}
		return links.toArray(new Page[1]);
	}
	
	public Integer getContentHash() {
		return content == null ? null : content.hashCode();
	}
	
	private boolean checkURL(URL url) {
		return url.toString().contains("https://docs.oracle.com/javase/8/javafx/api");
	}
	
	@Override
	public String toString() {
		return "[Page: " + this.url.toString() + " loaded: " + (content != null) + "]";
	}
}
