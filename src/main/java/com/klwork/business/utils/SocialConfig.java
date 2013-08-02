package com.klwork.business.utils;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class SocialConfig {
	private static Properties props = new Properties();
	static {
		try {
			loadProperties();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void loadProperties() throws IOException {
		props.load(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("social.properties"));
	}

	private SocialConfig() {
	}

	public static String getString(String key) {
		try {
			return props.getProperty(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
