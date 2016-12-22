package com.jiaogui.androidexam.tools;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/** 
 * @Title: JacksonUtil.java 
 * @author fusz
 * @date   2012-3-22
 * @version
 */
public class JacksonUtil {
	@SuppressWarnings("unchecked")
	public static String getUrlString(String url){
		try{
			HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(50000);
			conn.setDoOutput(true);
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			StringBuffer sb = new StringBuffer();
			String temp = rd.readLine();
			while(temp!=null){
				sb.append(temp);
				temp = rd.readLine();
			}
			rd.close();
			conn.disconnect();
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public static List<Map<String, Object>> readJson2List(String url) throws Exception {
	  	   ObjectMapper mapper = new ObjectMapper();
	  	   return mapper.readValue(getUrlString(url),List.class);
	}
	
	 
}
