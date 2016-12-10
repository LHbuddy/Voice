package com.voice.test;

import java.util.HashMap;

public class ResultTranslateUtil {
	private static HashMap<Integer, String> dp_message_map = new HashMap<Integer, String>();
	private static HashMap<String, String> special_content_map = new HashMap<String, String>();
	
	static {
		dp_message_map.put(0, "Õı³£");
		dp_message_map.put(16, "Â©¶Á");
		dp_message_map.put(32, "Ôö¶Á");
		dp_message_map.put(64, "»Ø¶Á");
		dp_message_map.put(128, "Ìæ»»");
		
		special_content_map.put("sil", "¾²Òô");
		special_content_map.put("silv", "¾²Òô");
		special_content_map.put("fil", "ÔëÒô");
	}
	
	public static String getDpMessageInfo(int dp_message) {
		return dp_message_map.get(dp_message);
	}
	
	public static String getContent(String content) {
		String val = special_content_map.get(content);
		return (null == val)? content: val;
	}
}
