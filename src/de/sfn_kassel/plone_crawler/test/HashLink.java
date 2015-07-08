package de.sfn_kassel.plone_crawler.test;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashLink {
	URL url;
	
	public HashLink(URL url) {
		this.url = url;
	}
	
	public static String hash(String original) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
	
	public String getNameHash(String rootName) {
//		System.err.print(url.toString() + "	->	");
		if (!url.toString().equals(rootName)) {
			String end = url.toString().substring(url.toString().lastIndexOf('.'));
			if (end.length() <= 4 && !end.equals(".de")) {
				String beginning = url.toString().substring(0, url.toString().lastIndexOf('.') - 1);
				try {
//					System.out.println(HashLink.hash(beginning) + end);
					return HashLink.hash(beginning) + end;
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				try {
//					System.out.println(HashLink.hash(url.toString()) + ".html");
					return HashLink.hash(url.toString()) + ".html";
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					return null;
				}
			}
		} else {
			return "index.html";
		}
	}
}
