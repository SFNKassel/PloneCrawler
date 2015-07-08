package de.sfn_kassel.plone_crawler.test;

public interface Checker<T> {
	public boolean check(T object);
}
