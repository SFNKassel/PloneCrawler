package de.sfn_kassel.plone_crawler.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashLink {
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
}
