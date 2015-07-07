package de.sfn_kassel.plone_crawler.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Test {

	static ArrayList<Page> futurePages = new ArrayList<>();
	static ArrayList<Page> donePages = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		futurePages.add(new Page(new URL("http://www.physikclub.de")));

		OnTaskFinished onTaskFinished = new OnTaskFinished() {

			@Override
			public synchronized void taskQueEmpty(Page finished) {
				System.out.println("done: " + donePages.size()  + " | todo: " + futurePages.size());
				synchronized (donePages) {
					donePages.add(finished);
					synchronized (futurePages) {
						for (Page p : finished.getLinks()) {
							futurePages.add(p);
						}
					}
				}
			}
		};

		OnTaskQueEmptyListener onTaskQueEmptyListener = new OnTaskQueEmptyListener() {
			@Override
			public synchronized Page taskQueEmpty() {
				while(true)
				while (true) {
					synchronized (futurePages) {
						try {
							futurePages.get(0);
						} catch (IndexOutOfBoundsException e) {
							break;
						}
						Page returnPage = futurePages.get(0);
						futurePages.remove(0);
						return returnPage;
					}
				}
			}
		};

		for (int i = 0; i < Integer.parseInt(args[0]); i++) {
			Crawler c = new Crawler(onTaskQueEmptyListener, onTaskFinished);
			c.setName("Crawler Thread " + i);
			c.start();
		}
	}
}
