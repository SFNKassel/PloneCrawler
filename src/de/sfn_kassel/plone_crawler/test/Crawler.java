package de.sfn_kassel.plone_crawler.test;

public class Crawler extends Thread{
	OnTaskQueEmptyListener onTaskQueEmptyListener;
	OnTaskFinished onTaskFinished;
	String startname;
	
	public Crawler(OnTaskQueEmptyListener onTaskQueEmptyListener, OnTaskFinished onTaskFinished) {
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
			Page currentPage = onTaskQueEmptyListener.taskQueEmpty();
			setNamePostfix("downloading" + "	(" + i + ")");
			currentPage.loadPage();
			i++;
			setNamePostfix("waiting" + "	(" + i + ")");
			onTaskFinished.taskQueEmpty(currentPage);
			} catch(Exception e) {
//				e.printStackTrace();
			}
		}
	}
	
	private void setNamePostfix(String s) {
		this.setName(startname + " [" + s + "]");
	}
}
