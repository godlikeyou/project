package com.crazyfish.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadPicThread extends Thread {
	private String link;
	private Bitmap resultData;
	public void setResultData(Bitmap resultData) {
		this.resultData = resultData;
	}
	public DownloadPicThread(String link){
		this.link = link;
	}
	public Bitmap getResultData(){
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
	private Bitmap getDataFromService(String link){
		 Bitmap bitmap = null;
		try {
			URL url = new URL(link);
			// 打开URL对应的资源输入流
			InputStream is = url.openStream();
			// 从InputStream流中解析出图片
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if( bitmap == null)return null;
		return bitmap;
	} 
}
