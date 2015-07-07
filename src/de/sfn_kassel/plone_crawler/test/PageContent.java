package de.sfn_kassel.plone_crawler.test;

public class PageContent {

	String content;
	
	public enum Type {
		Success, Error404("404"), Error;
		
		String content = null;
		private Type(String content) {
			this.content = content;
		}
		private Type() {}
	}
}
