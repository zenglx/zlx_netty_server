package com.zlx.domain;

import java.nio.charset.Charset;

public class Constants {
	public static final String HOST = System.getProperty("host", "192.168.1.136");
	public static final int PORT = Integer.parseInt(System.getProperty("port", "9999"));

	public static final String DELIMITER = "$#";

	public final static String CHARSET_NAME = "UTF-8";

	public final static Charset UTF_8 = Charset.forName(CHARSET_NAME);
}
