package com.jiaogui.androidexam.serivce;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageService {

	public static byte[] getImage(String image) throws Exception {

		URL url = new URL(image);
		byte[] b = null;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream input = conn.getInputStream();
			b = StreamTool.readInputStream(input);
			input.close();
			return b;

		}

		return null;

	}
	
	
	private static class StreamTool {

		public static byte[] readInputStream(InputStream in) throws Exception {

			int len = 0;

			byte buf[] = new byte[1024];

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len); // 把数据写入内存
			}
			out.close(); // 关闭内存输出流
			return out.toByteArray(); // 把内存输出流转换成byte数组
		}

	}
}
