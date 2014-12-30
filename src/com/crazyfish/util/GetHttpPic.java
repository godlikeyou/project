package com.crazyfish.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class GetHttpPic extends Activity{
	private String link;
	private Bitmap bitmap;
	private ImageView imageview;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x9527) {
				// 显示从网上下载的图片
				imageview.setImageBitmap(bitmap);
			}
		}
	};
	public void startServiceThread(){
		new Thread(){
			public void run(){
				 getPicFromService(link,imageview);
			}
		}.start();
	}
	
	private void getPicFromService(String link,ImageView imageview){
		try {
			// 创建一个url对象
			URL url = new URL("http://www.baidu.com/img/bdlogo.png");
			// 打开URL对应的资源输入流
			InputStream is = url.openStream();
			// 从InputStream流中解析出图片
			bitmap = BitmapFactory.decodeStream(is);
			// imageview.setImageBitmap(bitmap);
			// 发送消息，通知UI组件显示图片
			handler.sendEmptyMessage(0x9527);
			// 关闭输入流
			is.close();
			is = url.openStream();
			//存放在手机中，并命名为baidulogo.png
			FileOutputStream os = openFileOutput("baidulogo.png", Context.MODE_PRIVATE + Context.MODE_APPEND);
			byte[] buff = new byte[1024];
			int len = 0;
			//因为网络下载一般不可能一次下载完毕，我们将每次下载好的有效数据写入
			while ((len = is.read(buff)) > 0)
			{
			os.write(buff,0,len);
			}
			//关闭流
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
