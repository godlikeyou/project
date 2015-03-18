package com.crazyfish.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class GETThread extends Thread {
	private String link;
	private String resultData;
	public void setResultData(String resultData) {
		this.resultData = resultData;
	}
	public GETThread(String link){
		this.link = link;
	}
	public String getResultData(){
		return resultData;
	}
	@Override
	public void run(){
		resultData = getDataFromService(link);
	}
	public void startServiceThread(){
		this.start();
		try {
			this.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getDataFromService(String link){
		 StringBuffer data = null;
		try {
			// ʵ��URL
			URL url = new URL(link);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setConnectTimeout(10000);//10s
			httpUrlConnection.setReadTimeout(10000);//10s
			httpUrlConnection.connect();
			Log.i("resp", httpUrlConnection.getResponseCode()+"");
			// ��URL��Ӧ��������
			InputStream is = httpUrlConnection.getInputStream();
			byte[] bbuf = new byte[1024];
			// ����ָʾ�Ƿ������
			int hasRead = 0;
			data = new StringBuffer("");
			// ѭ��������ݵ�data��
			while ((hasRead = is.read(bbuf)) > 0) {
				data.append(new String(bbuf, 0, hasRead));
			}
			return data.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch( SocketTimeoutException e){
			e.printStackTrace();
			return "timeout";
		} catch (IOException e) {
			e.printStackTrace();
		} 
		if( data == null)return null;
		return data.toString();
	} 
}
