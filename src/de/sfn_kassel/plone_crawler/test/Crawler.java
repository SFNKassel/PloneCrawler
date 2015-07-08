package de.sfn_kassel.plone_crawler.test;

import java.io.File;

public class Crawler extends Thread{
	TaskQueEmptyListener onTaskQueEmptyListener;
	TaskFinishedListener onTaskFinished;
	String startname;
	
	public Crawler(TaskQueEmptyListener onTaskQueEmptyListener, TaskFinishedListener onTaskFinished) {
		this.onTaskFinished = onTaskFinished;
		this.onTaskQueEmptyListener = onTaskQueEmptyListener;
	}
	
	@Override
	public void run() {
		startname = this.getName();
		setNamePostfix("initializing");
		int i = 0;
		while (true) {
			try {
			Page currentPage = onTaskQueEmptyListener.onTaskQueEmpty();
			setNamePostfix("downloading" + "	(" + i + ")");
			currentPage.loadPage();
			i++;
			setNamePostfix("waiting" + "	(" + i + ")");
			currentPage.replaceLinks(startname);
			currentPage.writePage(new File("page/"), startname);
			onTaskFinished.onTaskFinished(currentPage);
			} catch(Exception e) {
//				e.printStackTrace();
			}
		}
	}
	
	private void setNamePostfix(String s) {
		this.setName(startname + " [" + s + "]");
	}
}
