package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					Log.i("HttpUtil","URL="+address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(30*1000);
					connection.setReadTimeout(30*1000);
					
					InputStream in = connection.getInputStream();
					InputStreamReader reader = new InputStreamReader(in);
					BufferedReader br = new BufferedReader(reader);
					int len = connection.getContentLength();
					Log.i("HttpUtil","服务器返回信息长度为:"+len);
					StringBuilder response = new StringBuilder();
					String line;
					while((line=br.readLine())!=null){
						response.append(line);
					}
					
					Log.i("HttpUtil",response.toString());
					if(in!=null){
						in.close();
					}
					if(connection!=null){
						connection.disconnect();
					}
					if(listener != null){
						listener.onFinish(response.toString());
					}
				} catch (IOException e) {
					Log.i("HttpUtil","Exception:"+e.toString());
					if(listener!=null){
						listener.onError(e);
					}
					// TODO: handle exception
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
