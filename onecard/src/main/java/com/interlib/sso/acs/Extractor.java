/*
 * Created on 2005-9-3
 *
 */
package com.interlib.sso.acs;

import java.util.HashMap;

/**
 * @author Zhou.Q.
 * 
 */
public class Extractor {
	private String src;
	private HashMap map;

	public Extractor(String in) {
		this.src = in;
		this.map = new HashMap();
		fillMap(src);
	}

	public String extract(String fieldid) {

		Object o = map.get(fieldid);
		if (o == null)
			return "";

		String returnString = (String) o;
		return returnString;
	}

	public String extractOrg(String fieldid) {

		Object o = map.get(fieldid);
		if (o == null)
			return "";

		String returnString = (String) o;
		return returnString;
	}

	protected void fillMap(String src) {
		String ar[] = src.split("\\|");

		for (int i = 0; i < ar.length; i++) {
			String s = ar[i];
			if (s.length() < 2)
				continue;

			char a = s.charAt(0);
			if (a >= 'A' && a <= 'Z') {
				String key = s.substring(0, 2);
				String value = s.substring(2);
				if (key.equals("AY")) { // sequence + checksum
					int n = value.indexOf("AZ"); // checksum
					if (n < 0) //
					{
						map.put(key, value);
					} else {
						map.put("AY", value.substring(0, n));
						map.put("AZ", value.substring(n + 2));
					}
				} else {
					map.put(key, value);
				}
			} else {
				map.put("AA", s);
			}
		}

	}

}
