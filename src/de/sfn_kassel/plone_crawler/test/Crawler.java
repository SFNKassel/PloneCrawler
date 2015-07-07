package de.sfn_kassel.plone_crawler.test;

public class Crawler extends Thread{
	OnTaskQueEmptyListener onTaskQueEmptyListener;
	OnTaskFinished onTaskFinished;
	
	public Crawler(OnTaskQueEmptyListener onTaskQueEmptyListener, OnTaskFinished onTaskFinished) {
		this.onTaskFinished = onTaskFinished;
		this.onTaskQueEmptyListener = onTaskQueEmptyListener;
	}
	
	@Override
	public void run() {
		while (true) {
			Page currentPage = onTaskQueEmptyListener.taskQueEmpty();
			currentPage.loadPage();
			onTaskFinished.taskQueEmpty(currentPage);
		}
	}
}
