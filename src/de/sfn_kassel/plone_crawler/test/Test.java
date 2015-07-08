package de.sfn_kassel.plone_crawler.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Test {

	static ArrayList<Page> futurePages = new ArrayList<>();
	static ArrayList<Page> donePages = new ArrayList<>();
	static ArrayList<String> Urls = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		String startpage = "http://physikclub.de";
		futurePages.add(new Page(new URL(startpage)));

		Checker<URL> URLChecker = new Checker<URL>() {
			@Override
			public boolean check(URL object) {
				boolean exsist = false;
				synchronized (Urls) {
					for (String page : Urls) {
						if (page.equals(new HashLink(object).getNameHash(startpage))) {
							exsist = true;
							break;
						}
					}
				}
				
//				System.err.println(exsist ? "true	" : "false	" + object.toString());
				return !object.toString().contains("#") && object.toString().contains(startpage)
						&& !object.toString().contains("@") && !exsist;
			}
		};

		OnTaskFinished onTaskFinished = new OnTaskFinished() {

			@Override
			public synchronized void taskQueEmpty(Page finished) {
				// System.out.println("done: " + donePages.size() + " | todo: "
				// + futurePages.size() + " "
				// + finished.url.toString());
				int before = futurePages.size() + 1;
				synchronized (donePages) {
					donePages.add(finished);
					System.out.println(finished.url.toString() + "	->	" + new HashLink(finished.url).getNameHash(startpage));
					synchronized (futurePages) {
						for (Page p : finished.getLinks()) {
							if (URLChecker.check(p.url)) {
								synchronized (Urls) {
									Urls.add(new HashLink(p.url).getNameHash(startpage));
								}
								futurePages.add(p);
							}
						}
					}
				}
//				System.out.println(before + "," + (fguturePages.size() - before) + "," + donePages.size());
			}
		};

		OnTaskQueEmptyListener onTaskQueEmptyListener = new OnTaskQueEmptyListener() {
			@Override
			public synchronized Page taskQueEmpty() {
				while (true)
					while (true) {
						synchronized (futurePages) {
							try {
								futurePages.get(0);
							} catch (IndexOutOfBoundsException e) {
								break;
							}
							Page returnPage = futurePages.get(0);
							futurePages.remove(0);
//							System.out.println(returnPage.url.toString());
							return returnPage;
						}
					}
			}
		};

		for (int i = 0; i < 10; i++) {
			Crawler c = new Crawler(onTaskQueEmptyListener, onTaskFinished);
			c.setName("Crawler Thread " + i);
			c.start();
		}
	}
}
