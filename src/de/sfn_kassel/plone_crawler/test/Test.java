package de.sfn_kassel.plone_crawler.test;

import java.io.IOException;
import java.net.URL;

public class Test {

	public static void main(String[] args) throws IOException {
		new Page(new URL("http://www.physikclub.de")).loadPage();
	}
}
