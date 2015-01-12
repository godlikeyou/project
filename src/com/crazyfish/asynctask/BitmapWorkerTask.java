package com.crazyfish.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crazyfish.cache.ImageFileCache;
import com.crazyfish.cache.ImageGetFromHttp;
import com.crazyfish.cache.ImageMemoryCache;
import com.crazyfish.util.PicHandler;

/**
 * ͼƬ�첽��ʾ
 * 
 */
public class BitmapWorkerTask extends AsyncTask<String, Integer, List<Bitmap>> {
	private ImageView imageView;
	private ImageView userHead;
	private ProgressBar progressBar;
	private Context context;
	private String url1;
	private String url2;

	public BitmapWorkerTask(ImageView imageView, ImageView userHead,Context context) {
		super();
		this.userHead = userHead;
		this.imageView = imageView;
		this.context = context;
		// this.progressBar = progressBar;
	}

//	private Bitmap getDataFromService(String link) {
//		Bitmap bitmap = null;
//		Log.i("���涼û��", "���涼û��ֻ�ܴ���������");
//		try {
//			URL url = new URL(link);
//			// ��URL��Ӧ����Դ������
//			InputStream is = url.openStream();
//			// ��InputStream���н�����ͼƬ
//			bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//			return bitmap;
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if (bitmap == null)
//			return null;
//		return bitmap;
//	}

	

	/**
	 * �����Integer�����ӦAsyncTask�еĵ�һ������ �����String����ֵ��ӦAsyncTask�ĵ��������
	 * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
	 * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���
	 */
	@Override
	protected List<Bitmap> doInBackground(String... params) {
		List<Bitmap> list = new ArrayList<Bitmap>();
		try {
			url1 = params[0].toString();
			url2 = params[1].toString();
			Bitmap userupload = PicHandler.getBitmap(url1,context);// ���뵽result��,��Ӧ���������
			Bitmap userhead = PicHandler.getBitmap(url2,context);
			list = new ArrayList<Bitmap>();
			list.add(userupload);
			list.add(PicHandler.toRoundCorner(userhead, 100));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * �����String�����ӦAsyncTask�еĵ��������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
	 */
	@Override
	protected void onPostExecute(List<Bitmap> result) {
		// progressBar.setVisibility(View.GONE);
		// imageView.setVisibility(View.VISIBLE);
		try {
			if (imageView.getTag().equals(url1)) {
                imageView.setImageBitmap(result.get(0));
            }
			if (userHead.getTag().equals(url2)) {
                userHead.setImageBitmap(result.get(1));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �÷���������UI�̵߳���,����������UI�̵߳��� ���Զ�UI�ռ��������
	@Override
	protected void onPreExecute() {
		// imageView.setVisibility(View.GONE);
		imageView.setImageBitmap(null);
		userHead.setImageBitmap(null);
	}

	/**
	 * �����Intege�����ӦAsyncTask�еĵڶ�������
	 * ��doInBackground�������У���ÿ�ε���publishProgress�������ᴥ��onProgressUpdateִ��
	 * onProgressUpdate����UI�߳���ִ�У����п��Զ�UI�ռ���в���
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		progressBar.setProgress(values[0]);
	}
}
