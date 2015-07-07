package de.sfn_kassel.plone_crawler.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Test {
	
	static ArrayList<Page> futurePages = new ArrayList<>();
	static ArrayList<Page> donePages = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		futurePages.add(new Page(new URL("http://www.phytikclub.de")));
		
		OnTaskFinished onTaskFinished = new OnTaskFinished() {
			
			@Override
			public void taskQueEmpty(Page finished) {
				synchronized (donePages) {
					donePages.add(finished);
				}
			}
		};
		
		OnTaskQueEmptyListener onTaskQueEmptyListener = new OnTaskQueEmptyListener() {
			
			@Override
			public Page taskQueEmpty() {
				synchronized (futurePages) {
					boolean exsist = false;
					while (!exsist){
						try {
							futurePages.get(0);
							exsist = true;
						} catch (ArrayIndexOutOfBoundsException e) {
							
						}
					}
					Page returnPage = futurePages.get(0);
					futurePages.remove(0);
					return returnPage;
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
