package de.sfn_kassel.plone_crawler.test;

public class PageContent {

	String content;
	Type contentType;
	
	public PageContent(String content, Type type) {
		this.content = content;
		this.contentType = type;
	}
	
	public enum Type {
		Success, Error404("404"), Error;
		
		String content = null;
		private Type(String content) {
			this.content = content;
		}
		private Type() {}
	}
}
