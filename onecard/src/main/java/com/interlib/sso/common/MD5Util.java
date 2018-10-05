package com.interlib.sso.common;
import java.security.MessageDigest;

public class MD5Util {

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}
	
	public static String MD5Encode(String origin) {
		String resultString = null;
		String charsetname = "UTF-8";
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static void main(String[] args) {
		//7e7daaf208f113bd51b01e31d4dc27ee
		//http://192.168.0.62:7788/opac/webservice/financeWebservice/getFinanceByPaysign?rdid=D27827&password=cb298444ad97e91dea7be1b7b5ad648a&paysign=0&doPage=false&toPage=1&pageSize=12
		//192.168.0.89:8089/sso/api/finance/query?rdid=2015003&appcode=selfprint&userid=admin&libcode=999&feetype=206&enc=95dcf989e7b42fa76345a6e5447871b8
		System.out.println(MD5Util.MD5Encode("zzwy20170425zzwy", "UTF-8"));
		System.out.println(MD5Util.MD5Encode("123456", "UTF-8"));
	}
}
