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
 * 图片异步显示
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
//		Log.i("缓存都没有", "缓存都没有只能从网络下载");
//		try {
//			URL url = new URL(link);
//			// 打开URL对应的资源输入流
//			InputStream is = url.openStream();
//			// 从InputStream流中解析出图片
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
	 * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
	 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
	 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
	 */
	@Override
	protected List<Bitmap> doInBackground(String... params) {
		List<Bitmap> list = new ArrayList<Bitmap>();
		try {
			url1 = params[0].toString();
			url2 = params[1].toString();
			Bitmap userupload = PicHandler.getBitmap(url1,context);// 放入到result中,对应第三个参数
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
	 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
	 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
	 */
	@Override
	protected void onPostExecute(List<Bitmap> result) {
		// progressBar.setVisibility(View.GONE);
		// imageView.setVisibility(View.VISIBLE);
		try {
			if (imageView.getTag().equals(url1))
				imageView.setImageBitmap(result.get(0));
			if (userHead.getTag().equals(url2))
				userHead.setImageBitmap(result.get(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
	@Override
	protected void onPreExecute() {
		// imageView.setVisibility(View.GONE);
		imageView.setImageBitmap(null);
		userHead.setImageBitmap(null);
	}

	/**
	 * 这里的Intege参数对应AsyncTask中的第二个参数
	 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
	 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		progressBar.setProgress(values[0]);
	}
}
