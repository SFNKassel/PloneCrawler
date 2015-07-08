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
						if (page.equals(object.toString())) {
							exsist = true;
							break;
						}
					}
				}
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
				System.out.println("1");
				int before = futurePages.size() + 1;
				synchronized (donePages) {
					System.out.println("2");
					donePages.add(finished);
					synchronized (futurePages) {
						System.out.println("db");
						for (Page p : finished.getLinks()) {
							System.out.println("db2");
							if (URLChecker.check(p.url)) {
								System.out.println("deadlock");
								synchronized (Urls) {
									System.out.println("2.5");
									Urls.add(p.url.toString());
								}
								futurePages.add(p);
							}
							System.out.println("3");
						}
						System.out.println("jo");
					}
				}
				System.out.println(before + "," + (futurePages.size() - before) + "," + donePages.size());
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
							System.out.println(returnPage.getNameHash());
							return returnPage;
						}
					}
			}
		};

		for (int i = 0; i < 1; i++) {
			Crawler c = new Crawler(onTaskQueEmptyListener, onTaskFinished);
			c.setName("Crawler Thread " + i);
			c.start();
		}
	}
}
