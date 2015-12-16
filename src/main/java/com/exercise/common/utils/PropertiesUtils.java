package com.exercise.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	
	private static PropertiesUtils propUtils = null;
	private static Properties prop = new Properties();
	private InputStream inputStream = null;
	
	private PropertiesUtils() throws IOException {
		init();
	}
	
	private void init() throws IOException {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			inputStream = classLoader.getResourceAsStream("../ratelimit.properties");
			prop.load(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	public static PropertiesUtils getInstance() throws IOException {
		if (propUtils == null) {
			propUtils = new PropertiesUtils();
		}
		return propUtils;
	}
	
	public int getNumberOfCall(String clientId) {
		String s = prop.getProperty(clientId+"_num_calls");
		if (s == null || s.trim().isEmpty()) {
			s = prop.getProperty("global_num_calls");
		}
		return Integer.parseInt(s);
	}
	
	public long getTimeWindow(String clientId) {
		String s = prop.getProperty(clientId+"_time_window");
		if (s == null || s.trim().isEmpty()) {
			s = prop.getProperty("global_time_window");
		}
		return Long.parseLong(s);
	}
	
	public long getSuspenedTime(String clientId) {
		String s = prop.getProperty(clientId+"_suspended");
		if (s == null || s.trim().isEmpty()) {
			s = prop.getProperty("global_suspended");
		}
		return Long.parseLong(s);
	}
	
	public String getServiceURL() {
		return prop.getProperty("service_url");
	}
}
