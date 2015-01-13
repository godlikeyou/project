package com.crazyfish.activity.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.asynctask.HttpGetTask;
import com.crazyfish.demo.R;
import com.crazyfish.extension.EListView;
import com.crazyfish.extension.EListView.OnRefreshListener;
import com.crazyfish.useradapter.AllArticleAdapter;
import com.crazyfish.util.FileUtils;
import com.crazyfish.util.GETThread;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;
import com.crazyfish.util.NetUtil;

//import com.crazyfish.extension.HkDialogLoading;
public class HomeFragment extends Fragment implements OnScrollListener {
	private View view = null;
	private AllArticleAdapter allAdapter;
	// private SimpleAdapter mSimpleAdapter;
	private Button bt;
	private ProgressBar pg;
	private ProgressBar pbLoad;//载入中
    private EditText etInput;//user comment
	List<Map<String, Object>> lmap;
	private View moreView;
	private Handler handler;
	private int lastVisibleIndex;
	private int size;//
	private String result;
    private LinearLayout llUserPost;
    private LinearLayout bottomList;
    private Button btUserRec;
    private TextView tvAllGag;
    private LinearLayout llAllGag;
    private TextView tvPureText;
    private LinearLayout llPureText;
    private TextView tvHavePic;
    private LinearLayout llHavePic;
    private TextView tvSchool;
    private LinearLayout llSchool;
    private TextView tvSelected;
    private LinearLayout llSelected;
    private Resources resources;
	// private HkDialogLoading dialogLoading;
	private Button btRefresh;
    private int filterType = 1;//default all gag
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_fragment, container, false);
		return view;
	}

	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GlobalVariable.HANDLER_GET_CODE:
				Bundle bundle = msg.getData();
				result = bundle.getString("result");
				String rtype = bundle.getString("type");
				String url1 = GlobalVariable.URLHEAD + "/article/gagsize";
				String url = GlobalVariable.URLHEAD + "/article/allarticle/1";
                String url2 = GlobalVariable.URLHEAD + "/article/allarticle/havepic/1";
                String url3 = GlobalVariable.URLHEAD + "/article/gagsize/havepic";
                String url4 = GlobalVariable.URLHEAD + "/article/gagsize/puretext";
                String url5 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
				Log.i("handler", result + rtype);
				if (result.equals("timeout")) {
					Toast.makeText(getActivity(), "加载失败，为您加载了历史浏览信息，请检查网络",
							Toast.LENGTH_LONG).show();
					pbLoad.setVisibility(View.GONE);
					//btRefresh.setVisibility(View.VISIBLE);
                    String link = GlobalVariable.FILE_CACHE_LOCATION + File.separator
                            + "allgag";
                    String str = FileUtils.readCacheFile(link);
                    Log.i("ifdata", str);
                    String type = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
                    // json decode
                    lmap = JsonCodec.deJson(str, type);
                    final EListView lv = (EListView) view.findViewById(R.id.allArticle);
                    allAdapter = new AllArticleAdapter(getActivity(), lmap,etInput,llUserPost,bottomList,btUserRec,h);
                    lv.setAdapter(allAdapter);
					break;
				}
				if (rtype.equals(url1)||rtype.equals(url3)||rtype.equals(url4)) {
					try {
						size = Integer.parseInt(result);
						Log.i("hhwwyy", "" + size);
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} else if (rtype.equals(url)) {
					if (FileUtils.fileCache("allgag", result) == null) {
					}
					String type = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
					// json decode
					List<Map<String, Object>> tmp = JsonCodec.deJson(result,
							type);
					Log.i("size", "" + tmp.size());
					lmap.clear();
					lmap.addAll(tmp);
					allAdapter.notifyDataSetChanged();
					// dialogLoading.hide();
					pbLoad.setVisibility(View.GONE);
					bt.setVisibility(View.VISIBLE);
					btRefresh.setVisibility(View.GONE);
				}else if(rtype.equals(url2)){
                    if (FileUtils.fileCache("allgag", result) == null) {
                    }
                    String type = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
                    // json decode
                    List<Map<String, Object>> tmp = JsonCodec.deJson(result,
                            type);
                    lmap.clear();
                    lmap.addAll(tmp);
                    allAdapter.notifyDataSetChanged();
                    // dialogLoading.hide();
                    pbLoad.setVisibility(View.GONE);
                    bt.setVisibility(View.VISIBLE);
                    bt.setEnabled(true);
                    bt.setText("点击加载更多");
                    btRefresh.setVisibility(View.GONE);
                }else if(rtype.equals(url5)){
                    if (FileUtils.fileCache("allgag", result) == null) {
                    }
                    String type = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
                    // json decode
                    List<Map<String, Object>> tmp = JsonCodec.deJson(result,
                            type);
                    lmap.clear();
                    lmap.addAll(tmp);
                    allAdapter.notifyDataSetChanged();
                    // dialogLoading.hide();
                    pbLoad.setVisibility(View.GONE);
                    bt.setVisibility(View.VISIBLE);
                    bt.setEnabled(true);
                    bt.setText("点击加载更多");
                    btRefresh.setVisibility(View.GONE);
                }
				break;
                case GlobalVariable.HANDLER_GOOD_CODE:
                    Bundle b = msg.getData();
                    if( b.getInt("sf") == GlobalVariable.SUCCESS){
                        int goodc = b.getInt("goodc");
                        String gid = b.getString("gid");
                        lmap = JsonCodec.changeList(lmap,gid,goodc);
                        allAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"点赞成功,恭喜",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"点赞失败,悲剧，请检查网络",Toast.LENGTH_LONG).show();
                    }
                break;
                case GlobalVariable.HANDLER_COLLECTION_CODE:
                    Bundle bu = msg.getData();
                    if( bu.getInt("sf") == GlobalVariable.SUCCESS){
                        String gid = bu.getString("gid");
                        Toast.makeText(getActivity(),"收藏成功,恭喜",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"收藏失败,悲剧，请检查网络",Toast.LENGTH_LONG).show();
                    }
                break;
                case GlobalVariable.HANDLER_REC_CODE:
                    Bundle bun = msg.getData();
                    if( bun.getInt("sf") == GlobalVariable.SUCCESS){
                        String gid = bun.getString("gid");
                        int recc = bun.getInt("recc");
                        lmap = JsonCodec.changeListRecNumber(lmap,gid,recc);
                        allAdapter.notifyDataSetChanged();
                        LinearLayout bottomList = (LinearLayout)getActivity().findViewById(R.id.bottomList);
                        LinearLayout flPost = (LinearLayout)getActivity().findViewById(R.id.llUserPost);
                        bottomList.setVisibility(View.VISIBLE);
                        flPost.setVisibility(View.INVISIBLE);
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                        Toast.makeText(getActivity(),"评论成功,恭喜",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"评论失败,悲剧，请检查网络",Toast.LENGTH_LONG).show();
                    }
                break;
			}
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        etInput = (EditText)getActivity().findViewById(R.id.etInputA);
        llUserPost = (LinearLayout)getActivity().findViewById(R.id.llUserPost);
        bottomList = (LinearLayout)getActivity().findViewById(R.id.bottomList);
        btUserRec = (Button)getActivity().findViewById(R.id.btUserRec);
        tvAllGag = (TextView)getActivity().findViewById(R.id.tvAllGag);
        llAllGag = (LinearLayout)getActivity().findViewById(R.id.llAllGag);
        tvPureText = (TextView)getActivity().findViewById(R.id.tvPureText);
        llPureText = (LinearLayout)getActivity().findViewById(R.id.llPureText);
        tvHavePic = (TextView)getActivity().findViewById(R.id.tvHavePic);
        llHavePic = (LinearLayout)getActivity().findViewById(R.id.llHavePic);
        tvSchool = (TextView)getActivity().findViewById(R.id.tvSelSchool);
        llSchool = (LinearLayout)getActivity().findViewById(R.id.llSchool);
        tvSelected = (TextView)getActivity().findViewById(R.id.tvSelected);
        llSelected = (LinearLayout)getActivity().findViewById(R.id.llSelected);

        tvAllGag.setOnClickListener(showAllGag);
        tvPureText.setOnClickListener(showPureText);
        tvHavePic.setOnClickListener(showHavePic);
        tvSchool.setOnClickListener(showSchool);
        tvSelected.setOnClickListener(showSelected);

        resources = getResources();
        tvAllGag.setTextColor(resources.getColor(R.color.head));
        llAllGag.setBackgroundColor(resources.getColor(R.color.head));
		if (NetUtil.checkNet(getActivity())) {
			// dialogLoading = new HkDialogLoading(getActivity());
			// dialogLoading.show();
			lmap = new ArrayList<Map<String, Object>>();
			final EListView lv = (EListView) view.findViewById(R.id.allArticle);
			moreView = getLayoutInflater(savedInstanceState).inflate(
					R.layout.moredata, null);
			bt = (Button) moreView.findViewById(R.id.bt_load);
			btRefresh = (Button) view.findViewById(R.id.btRefresh);
			pg = (ProgressBar) moreView.findViewById(R.id.pg);
			pbLoad = (ProgressBar) view.findViewById(R.id.pbLoad);
			handler = new Handler();
			bt.setVisibility(View.GONE);
			btRefresh.setVisibility(View.GONE);
			String url1 = GlobalVariable.URLHEAD + "/article/gagsize";
			HttpGetTask task1 = new HttpGetTask(h);
			task1.execute(url1);
			String url = GlobalVariable.URLHEAD + "/article/allarticle/1";
			HttpGetTask task = new HttpGetTask(h);
			task.execute(url);
			allAdapter = new AllArticleAdapter(getActivity(), lmap,etInput,llUserPost,bottomList,btUserRec,h);
			lv.addFooterView(moreView);
			lv.setAdapter(allAdapter);
			lv.setonRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							String url3 = GlobalVariable.URLHEAD
									+ "/article/gagsize";
							GETThread th3 = new GETThread(url3);
							th3.startServiceThread();
							boolean flag = false;
							if (th3 != null
									&& !th3.getResultData().equals("timeout")) {
								size = Integer.valueOf(th3.getResultData());
								Log.i("size", String.valueOf(size));
								String url4 = GlobalVariable.URLHEAD
										+ "/article/allarticle/1";
								GETThread th4 = new GETThread(url4);
								th4.startServiceThread();
								String xx4 = th4.getResultData();
								if (xx4 != null && !xx4.equals("timeout")) {
									lmap.clear();
									String type4 = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
									// json decode
									List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
									lm = JsonCodec.deJson(xx4, type4);
									Log.i("return sizes", "" + lmap.size());
									lmap.addAll(lm);
									bt.setText("点击重新加载");
									bt.setEnabled(true);
									allAdapter.notifyDataSetChanged();// success
								} else {
									flag = true;// outline
								}
							} else {
								flag = true;// outline
							}
							lv.onRefreshComplete();
							if (flag) {// request failure
								Toast.makeText(getActivity(),
										"reload", Toast.LENGTH_LONG)
										.show();

							}
						}
					}.execute(null, null, null);
				}
			});
			lv.setItemsCanFocus(true);
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			lv.setOnScrollListener(this);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pg.setVisibility(View.VISIBLE);
					bt.setVisibility(View.GONE);
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							String rlt = loadMoreData();
							bt.setVisibility(View.VISIBLE);
							pg.setVisibility(View.GONE);
							if ("success".equals(rlt)) {
								allAdapter.notifyDataSetChanged();
							} else {
								bt.setText("加载失败");
							}
						}
					}, 2000);
				}
			});
		} else {
			final EListView lv = (EListView) view.findViewById(R.id.allArticle);
			moreView = getLayoutInflater(savedInstanceState).inflate(
					R.layout.moredata, null);
			bt = (Button) moreView.findViewById(R.id.bt_load);
			pg = (ProgressBar) moreView.findViewById(R.id.pg);
			handler = new Handler();
			String link = GlobalVariable.FILE_CACHE_LOCATION + File.separator
					+ "allgag";
			String str = FileUtils.readCacheFile(link);
			Log.i("ifdata", str);
			String type = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
			// json decode
			lmap = JsonCodec.deJson(str, type);
			allAdapter = new AllArticleAdapter(getActivity(), lmap,etInput,llUserPost,bottomList,btUserRec,h);
			lv.addFooterView(moreView);
			lv.setAdapter(allAdapter);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							bt.setVisibility(View.VISIBLE);
							bt.setText("点击加载");
							bt.setEnabled(false);
							Toast.makeText(getActivity(), "加载中",
									Toast.LENGTH_LONG).show();
						}
					}, 2000);
				}
			});
		}
	}

	private String loadMoreData() {
		int count = allAdapter.getCount();
		Log.i("currrentsize", "" + count);
		if (count + GlobalVariable.GAG_PAGE_SIZE < (size + 2)) {
			int currentPage = count / GlobalVariable.GAG_PAGE_SIZE + 1;
			Log.i("currentpage", "" + currentPage);
			String url = GlobalVariable.URLHEAD + "/article/allarticle/"
					+ currentPage;
			GETThread th = new GETThread(url);
			th.startServiceThread();
			String xx = th.getResultData();
			boolean s = false;
			if (xx != null && !xx.equals("timeout")) {
				String type = "gContent,gId,gtReccount,gtGoodcount,school,customer,gPic";
				// json decode
				List<Map<String, Object>> tmp = new ArrayList<Map<String, Object>>();
				tmp = JsonCodec.deJson(xx, type);
				lmap.addAll(tmp);
			} else {// request failure
				return "failure";
			}
		}
		return "success";
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (size == 0) {
			size = -1;
		}
		Log.i("ifsame", "" + totalItemCount + (size + 2));
		if (totalItemCount == size + 2) {
			// lv.removeFooterView(moreView);
			bt.setText("没有更多了");
			bt.setEnabled(false);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == allAdapter.getCount()) {
			// pg.setVisibility(View.VISIBLE);
			// bt.setVisibility(View.GONE);
			// handler.postDelayed(new Runnable() {
			//
			// @Override
			// public void run() {
			// loadMoreDate();
			// bt.setVisibility(View.VISIBLE);
			// pg.setVisibility(View.GONE);
			// mSimpleAdapter.notifyDataSetChanged();
			// }
			//
			// }, 2000);
		}
	}
    public View.OnClickListener showAllGag =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filterType = GlobalVariable.FILTER_ALL_GAG;

            pbLoad.setVisibility(View.VISIBLE);

            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize";
            HttpGetTask task1 = new HttpGetTask(h);
            task1.execute(url3);
            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/1";
            HttpGetTask task = new HttpGetTask(h);
            task.execute(url4);

            tvAllGag.setTextColor(resources.getColor(R.color.head));
            llAllGag.setBackgroundColor(resources.getColor(R.color.head));

            tvHavePic.setTextColor(resources.getColor(R.color.content));
            llHavePic.setBackgroundColor(resources.getColor(R.color.white));

            tvPureText.setTextColor(resources.getColor(R.color.content));
            llPureText.setBackgroundColor(resources.getColor(R.color.white));

            tvSchool.setTextColor(resources.getColor(R.color.content));
            llSchool.setBackgroundColor(resources.getColor(R.color.white));

            tvSelected.setTextColor(resources.getColor(R.color.content));
            llSelected.setBackgroundColor(resources.getColor(R.color.white));
        }
    };
    public View.OnClickListener showHavePic =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filterType = GlobalVariable.FILTER_HAVE_PIC;

            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/havepic";
            HttpGetTask task1 = new HttpGetTask(h);
            task1.execute(url3);
            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/havepic/1";
            HttpGetTask task = new HttpGetTask(h);
            task.execute(url4);

            tvAllGag.setTextColor(resources.getColor(R.color.content));
            llAllGag.setBackgroundColor(resources.getColor(R.color.white));

            tvHavePic.setTextColor(resources.getColor(R.color.head));
            llHavePic.setBackgroundColor(resources.getColor(R.color.head));

            tvPureText.setTextColor(resources.getColor(R.color.content));
            llPureText.setBackgroundColor(resources.getColor(R.color.white));

            tvSchool.setTextColor(resources.getColor(R.color.content));
            llSchool.setBackgroundColor(resources.getColor(R.color.white));

            tvSelected.setTextColor(resources.getColor(R.color.content));
            llSelected.setBackgroundColor(resources.getColor(R.color.white));
        }
    };
    public View.OnClickListener showPureText =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filterType = GlobalVariable.FILTER_PURE_TEXT;

            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/puretext";
            HttpGetTask task1 = new HttpGetTask(h);
            task1.execute(url3);
            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
            HttpGetTask task = new HttpGetTask(h);
            task.execute(url4);

            tvAllGag.setTextColor(resources.getColor(R.color.content));
            llAllGag.setBackgroundColor(resources.getColor(R.color.white));

            tvHavePic.setTextColor(resources.getColor(R.color.content));
            llHavePic.setBackgroundColor(resources.getColor(R.color.white));

            tvPureText.setTextColor(resources.getColor(R.color.head));
            llPureText.setBackgroundColor(resources.getColor(R.color.head));

            tvSchool.setTextColor(resources.getColor(R.color.content));
            llSchool.setBackgroundColor(resources.getColor(R.color.white));

            tvSelected.setTextColor(resources.getColor(R.color.content));
            llSelected.setBackgroundColor(resources.getColor(R.color.white));
        }
    };
    public View.OnClickListener showSchool =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filterType = GlobalVariable.FILTER_SCHOOL;

            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/puretext";
            HttpGetTask task1 = new HttpGetTask(h);
            task1.execute(url3);
            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
            HttpGetTask task = new HttpGetTask(h);
            task.execute(url4);

            tvAllGag.setTextColor(resources.getColor(R.color.content));
            llAllGag.setBackgroundColor(resources.getColor(R.color.white));

            tvHavePic.setTextColor(resources.getColor(R.color.content));
            llHavePic.setBackgroundColor(resources.getColor(R.color.white));

            tvPureText.setTextColor(resources.getColor(R.color.content));
            llPureText.setBackgroundColor(resources.getColor(R.color.white));

            tvSchool.setTextColor(resources.getColor(R.color.head));
            llSchool.setBackgroundColor(resources.getColor(R.color.head));

            tvSelected.setTextColor(resources.getColor(R.color.content));
            llSelected.setBackgroundColor(resources.getColor(R.color.white));
        }
    };
    public View.OnClickListener showSelected =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filterType = GlobalVariable.FILTER_SELECTED;

            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/puretext";
            HttpGetTask task1 = new HttpGetTask(h);
            task1.execute(url3);
            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
            HttpGetTask task = new HttpGetTask(h);
            task.execute(url4);

            tvAllGag.setTextColor(resources.getColor(R.color.content));
            llAllGag.setBackgroundColor(resources.getColor(R.color.white));

            tvHavePic.setTextColor(resources.getColor(R.color.content));
            llHavePic.setBackgroundColor(resources.getColor(R.color.white));

            tvPureText.setTextColor(resources.getColor(R.color.content));
            llPureText.setBackgroundColor(resources.getColor(R.color.white));

            tvSchool.setTextColor(resources.getColor(R.color.content));
            llSchool.setBackgroundColor(resources.getColor(R.color.white));

            tvSelected.setTextColor(resources.getColor(R.color.head));
            llSelected.setBackgroundColor(resources.getColor(R.color.head));
        }
    };
}
