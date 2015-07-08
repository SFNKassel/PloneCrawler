package de.sfn_kassel.plone_crawler.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class Test {

	static ArrayList<Page> futurePages = new ArrayList<>();
	static ArrayList<Page> donePages = new ArrayList<>();
	static HashSet<String> urls = new HashSet<>();

	public static void main(String[] args) throws IOException {
//		String startpage = "http://physikclub.de";
		String startpage = "http://blog.aschnabel.bplaced.net";
		futurePages.add(new Page(new URL(startpage)));

		Checker<URL> urlChecker = new Checker<URL>() {
			@Override
			public boolean check(URL object) {
				boolean exists = false;
				synchronized (urls) {
					exists = urls.contains(object.toString());
				}
				
//				System.err.println(exsist ? "true	" : "false	" + object.toString());
				return !object.toString().contains("#") && object.toString().contains(startpage)
						&& !object.toString().contains("@") && !exists;
			}
		};

		TaskFinishedListener taskFinishedListener = new TaskFinishedListener() {

			@Override
			public synchronized void onTaskFinished(Page finished) {
				// System.out.println("done: " + donePages.size() + " | todo: "
				// + futurePages.size() + " "
				// + finished.url.toString());
				int before = futurePages.size() + 1;
				synchronized (donePages) {
					donePages.add(finished);
					System.out.println(finished.url.toString() + "	->	" /*+ new HashLink(finished.url).getNameHash(startpage)*/);
					synchronized (futurePages) {
						for (Page p : finished.getLinks()) {
							if (urlChecker.check(p.url)) {
								synchronized (urls) {
									urls.add(p.url.toString());
								}
								futurePages.add(p);
							}
						}
					}
				}
				System.out.println(before + "," + (futurePages.size() - before) + "," + donePages.size());
			}
		};

		TaskQueEmptyListener taskQueEmptyListener = new TaskQueEmptyListener() {
			@Override
			public synchronized Page onTaskQueEmpty() {
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
			Crawler c = new Crawler(taskQueEmptyListener, taskFinishedListener);
			c.setName("Crawler Thread " + i);
			c.start();
		}
		
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (urls) {
				System.out.print("saving... ");
				File out = new File("out.jobj");
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(out));
				oos.writeObject(urls);
				oos.close();
				File backup = new File("backup.jobj");
				backup.delete();
				out.renameTo(backup);
				System.out.println("finished");
			}
			synchronized (futurePages) {
				if (futurePages.isEmpty())
					break;
			}
		}
	}
}
