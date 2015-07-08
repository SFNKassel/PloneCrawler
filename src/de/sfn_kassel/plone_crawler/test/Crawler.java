package de.sfn_kassel.plone_crawler.test;

import java.io.File;

public class Crawler extends Thread{
	TaskQueEmptyListener onTaskQueEmptyListener;
	TaskFinishedListener onTaskFinished;
	String startname;
	String startpage;
	boolean waiting = true;
	
	public Crawler(TaskQueEmptyListener onTaskQueEmptyListener, TaskFinishedListener onTaskFinished, String startpage) {
		this.onTaskFinished = onTaskFinished;
		this.onTaskQueEmptyListener = onTaskQueEmptyListener;
		this.startpage = startpage;
	}
	
	@Override
	public void run() {
		startname = this.getName();
		setNamePostfix("initializing");
		int i = 0;
		while (true) {
			try {
			waiting = true;
			Page currentPage = onTaskQueEmptyListener.onTaskQueEmpty();
			waiting = false;
			setNamePostfix("downloading" + "	(" + i + ")");
			currentPage.loadPage();
			i++;
			setNamePostfix("waiting" + "	(" + i + ")");
			onTaskFinished.onTaskFinished(currentPage);
			currentPage.replaceLinks(startpage);
			currentPage.writePage(new File("page/"), startpage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setNamePostfix(String s) {
		this.setName(startname + " [" + s + "]");
	}
}
