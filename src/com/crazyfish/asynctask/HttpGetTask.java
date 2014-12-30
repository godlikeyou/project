package com.crazyfish.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpGetTask extends AsyncTask<String,Integer,String>{
	private String url;
	private Handler handler;
	public HttpGetTask(Handler handler){
		this.handler = handler;
	}
	private String getDataFromService(String link){
		 StringBuffer data = null;
		try {
			// 实例化URL
			URL url = new URL(link);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setConnectTimeout(10000);//10s
			httpUrlConnection.setReadTimeout(10000);//10s
			httpUrlConnection.connect();
			Log.i("resp", httpUrlConnection.getResponseCode()+"");
			// 打开URL对应的输入流
			InputStream is = httpUrlConnection.getInputStream();
			byte[] bbuf = new byte[1024];
			// 用以指示是否还有数据
			int hasRead = 0;
			data = new StringBuffer("");
			// 循环输入数据到data中
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
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		url = params[0].toString();
		String resultData = getDataFromService(url);
		return resultData;
	}
	@Override
	protected void onPostExecute(String result){
		Log.i("x111x",""+(handler==null));
		Message ms = new Message();
		ms.what = 1;
		Bundle bundle = new Bundle();
		bundle.putString("type", url);
		bundle.putString("result", result);
		ms.setData(bundle);
		handler.sendMessage(ms);
	}
	

}
