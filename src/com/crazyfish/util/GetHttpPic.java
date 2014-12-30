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
				// ��ʾ���������ص�ͼƬ
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
			// ����һ��url����
			URL url = new URL("http://www.baidu.com/img/bdlogo.png");
			// ��URL��Ӧ����Դ������
			InputStream is = url.openStream();
			// ��InputStream���н�����ͼƬ
			bitmap = BitmapFactory.decodeStream(is);
			// imageview.setImageBitmap(bitmap);
			// ������Ϣ��֪ͨUI�����ʾͼƬ
			handler.sendEmptyMessage(0x9527);
			// �ر�������
			is.close();
			is = url.openStream();
			//������ֻ��У�������Ϊbaidulogo.png
			FileOutputStream os = openFileOutput("baidulogo.png", Context.MODE_PRIVATE + Context.MODE_APPEND);
			byte[] buff = new byte[1024];
			int len = 0;
			//��Ϊ��������һ�㲻����һ��������ϣ����ǽ�ÿ�����غõ���Ч����д��
			while ((len = is.read(buff)) > 0)
			{
			os.write(buff,0,len);
			}
			//�ر���
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
