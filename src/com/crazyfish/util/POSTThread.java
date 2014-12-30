package com.crazyfish.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class POSTThread extends Thread {
	private String link;
	private String resultData;
	private List<NameValuePair> params;
	public void setResultData(String resultData) {
		this.resultData = resultData;
	}
	public POSTThread(String link,List<NameValuePair> params){
		this.link = link;
		this.params = params;
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
		 String data = null;
		try {
			HttpPost request = new HttpPost(link);
			//List<NameValuePair> params = new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair("customer","kamal"));
			//params.add(new BasicNameValuePair("passwd","123456"));
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if( response.getStatusLine().getStatusCode() != 404){
				data = EntityUtils.toString(response.getEntity());	
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if( data == null)return null;
		return data.toString();
	} 
}
