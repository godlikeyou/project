package com.crazyfish.useradapter;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyfish.asynctask.BitmapWorkerTask;
import com.crazyfish.demo.R;
import com.crazyfish.util.JsonCodec;

public class AllArticleAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> listItems;
	private LayoutInflater listContainer;
	private Handler mHandler;

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public final class AllArticleView {
		public TextView title;
		public TextView content;
		public Button btnCollect;
		public Button btnGood;
		public Button btnRec;
		public ImageView ivGap;
		public TextView userName;
		public TextView signature;
		public ImageView userUpload;
		// public ProgressBar picLoad;
	}

	public AllArticleAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if( listItems == null)return 0;
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "123.JPG");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

	private void collectGag(int selectID) {
		new AlertDialog.Builder(context).setTitle("收藏说说").setMessage("收藏本说说?")
				.setPositiveButton("确定", null).setNegativeButton("取消", null)
				.show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int selectID = position;
		AllArticleView view = null;

		if (convertView == null) {
			view = new AllArticleView();
			convertView = listContainer.inflate(R.layout.all_article, null);
			view.title = (TextView) convertView.findViewById(R.id.title);
			view.content = (TextView) convertView.findViewById(R.id.content);
			view.btnCollect = (Button) convertView
					.findViewById(R.id.btnCollect);
			view.btnGood = (Button) convertView.findViewById(R.id.btnGood);
			view.btnRec = (Button) convertView.findViewById(R.id.btnRec);
			view.ivGap = (ImageView) convertView.findViewById(R.id.ivGap);
			view.userName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			view.signature = (TextView) convertView
					.findViewById(R.id.tvSignature);
			view.userUpload = (ImageView) convertView
					.findViewById(R.id.ivUserUpload);
			// view.picLoad = (ProgressBar) convertView
			// .findViewById(R.id.pbPicLoad);
			convertView.setTag(view);
		} else {
			view = (AllArticleView) convertView.getTag();
		}
		if (listItems != null) {
			// set user name
			String user = "["
					+ String.valueOf(listItems.get(position).get("customer"))
					+ "]";
			String types = "cId,cName,cNickname,cPurl,cSignature";
			List<Map<String, Object>> ulist = JsonCodec.deJson(user, types);
			view.userName.setText(ulist.get(0).get("cName").toString());
			view.signature.setText(ulist.get(0).get("cSignature").toString());
			// test
			view.userUpload.setTag(listItems.get(position).get("gPic")
					.toString());
			view.ivGap.setTag(ulist.get(0).get("cPurl").toString());

			new Thread() {
				@Override
				public void run() {
					super.run();

				}
			}.start();

			BitmapWorkerTask asyncTask = new BitmapWorkerTask(view.userUpload,
					view.ivGap, context);
			asyncTask.execute(listItems.get(position).get("gPic").toString(),
					ulist.get(0).get("cPurl").toString());

			// show user upload pic
			// String purl1 = listItems.get(position).get("gPic").toString();
			// if(purl1 != null && !"".equals(purl1)){
			// Bitmap bitmap = null;
			// Log.i("pic", purl1);
			// DownloadPicThread dpic1 = new DownloadPicThread(purl1);
			// dpic1.startServiceThread();
			// bitmap = dpic1.getResultData();
			// bitmap = PicHandler.toRoundCorner(bitmap, 10);
			// view.userUpload.setImageBitmap(bitmap);
			// bitmap = null;
			// }
			// BitmapWorkerTask task = new BitmapWorkerTask(view.ivGap,
			// view.picLoad);
			// task.execute(ulist.get(0).get("cPurl").toString());
			// show user pic
			// String purl = ulist.get(0).get("cPurl").toString();
			// if(purl != null && !"".equals(purl)){
			// Bitmap bitmap = null;
			// DownloadPicThread dpic = new DownloadPicThread(purl);
			// dpic.startServiceThread();
			// bitmap = dpic.getResultData();
			// bitmap = PicHandler.toRoundCorner(bitmap, 100);
			// view.ivGap.setImageBitmap(bitmap);
			// bitmap = null;
			// }
			// set school name
			String data = "["
					+ String.valueOf(listItems.get(position).get("school"))
					+ "]";
			String paraType = "sId,sName,sCount";
			List<Map<String, Object>> list = JsonCodec.deJson(data, paraType);
			view.title.setText(list.get(0).get("sName").toString());
			view.content.setText((String) listItems.get(position).get(
					"gContent"));
			view.btnGood.setText(String.valueOf(listItems.get(position).get(
					"gtGoodcount"))
					+ "赞");
			view.btnRec.setText(String.valueOf(listItems.get(position).get(
					"gtReccount"))
					+ "评");
			view.btnCollect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					collectGag(selectID);
				}
			});
		}
		return convertView;
	}

	class imgThread extends Thread {
		String imgUrl;

		@Override
		public void run() {

		}
	}
}
