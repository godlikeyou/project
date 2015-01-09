package com.crazyfish.activity.fragment;

import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.activity.PostActivity;
import com.crazyfish.activity.TextPostActivity;
import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.TakePhoto;
public class TakePhotoFragment extends Fragment {
	
	private View view = null;
	private Button btnTakePhoto = null;
	private Uri fileUri = null;
	private Button btnFromFile = null;
	private ImageView imageView = null;
    private Button btnTextPost = null;
	@Override 
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		view = inflater.inflate(R.layout.takephoto_fragment,container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		btnTakePhoto = (Button)view.findViewById(R.id.takePhoto);
		btnTakePhoto.setOnClickListener(listener);
		btnFromFile = (Button)view.findViewById(R.id.btnFromFile);
		btnFromFile.setOnClickListener(fromfile);
        btnTextPost = (Button)view.findViewById(R.id.btnTextPost);
        btnTextPost.setOnClickListener(textPost);
	}
	public OnClickListener listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			TakePhoto tp = new TakePhoto();
            fileUri = tp.getOutputMediaFileUri(GlobalVariable.MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            Log.i("uri", fileUri.toString());
            startActivityForResult(intent, GlobalVariable.CAPTURE_IMAGE_REQUEST_CODE);
		}
	};
    public OnClickListener textPost = new OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(getActivity(), TextPostActivity.class);
            startActivity(intent);
        }
    };
	public OnClickListener fromfile = new OnClickListener(){
		@Override
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent,GlobalVariable.SELECT_PIC);
		}
	};
	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

		 int h= (int) (newHeight*densityMultiplier);
		 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

		 photo=Bitmap.createScaledBitmap(photo, w, h, true);

		 return photo;
		 }
	@Override
	public void onActivityResult(int requestCode, int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("bug",String.valueOf(requestCode)+":"+ String.valueOf(resultCode)+":"+String.valueOf(FragmentActivity.RESULT_CANCELED));
		if(GlobalVariable.CAPTURE_IMAGE_REQUEST_CODE == requestCode){
			Bitmap thumbnail = null;
			if(FragmentActivity.RESULT_OK == resultCode){
				Log.i("yyy",String.valueOf(data));
				if ( data != null){
					if( data.hasExtra("data")){
						Log.i("hasExtra",data.getDataString());
						thumbnail = data.getParcelableExtra("data");
						//imageView.setImageBitmap(thumbnail);
					}
				}else{
					Log.i("hea","you");
					int width = 300;
	                int height = 300;
	                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
	                factoryOptions.inJustDecodeBounds = true;
	                BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
	                int imageWidth = factoryOptions.outWidth;
	                int imageHeight = factoryOptions.outHeight;
	                // Determine how much to scale down the image
	                int scaleFactor = Math.min(imageWidth / width, imageHeight
	                        / height);
	                // Decode the image file into a Bitmap sized to fill the
	                // View
	                factoryOptions.inJustDecodeBounds = false;
	                factoryOptions.inSampleSize = scaleFactor;
	               // factoryOptions.inBitmap = true;
	                Log.i("thumb",fileUri.getPath());
	                thumbnail = BitmapFactory.decodeFile(fileUri.getPath(),
	                        factoryOptions);
				}
				Intent intentx = new Intent();
				intentx.setClass(getActivity(), PostActivity.class);
				Bundle bun = new Bundle();
				bun.putParcelable("image",thumbnail);
				bun.putString("uri", fileUri.getPath());
				intentx.putExtras(bun);
				//intentx.putExtra("uri", fileUri.toString());
				startActivityForResult(intentx,GlobalVariable.REQUEST_CODE);
			}else if (resultCode == FragmentActivity.RESULT_CANCELED){
	            Toast.makeText(getActivity(), "取消拍照片", Toast.LENGTH_LONG).show();
	        }else{
	        	Toast.makeText(getActivity(), "拍照失败", Toast.LENGTH_LONG).show();
	        }
		}else if( requestCode == GlobalVariable.SELECT_PIC){
			if( resultCode == FragmentActivity.RESULT_OK){
				Toast.makeText(getActivity(), "选择照片", Toast.LENGTH_LONG).show();
				Uri uri = data.getData();  
	            Log.e("uri", uri.toString());  
	            ContentResolver cr = getActivity().getContentResolver();  
	            try {  
	                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
	                bitmap = TakePhotoFragment.scaleDownBitmap(bitmap, 200, getActivity().getApplicationContext());
	                Intent intentx = new Intent();
					intentx.setClass(getActivity(), PostActivity.class);
					Bundle bun = new Bundle();
					bun.putParcelable("image",bitmap);
					bun.putString("uri", uri.getPath());
					Log.i("uuu", uri.getPath());
					intentx.putExtras(bun);
					startActivityForResult(intentx,GlobalVariable.REQUEST_CODE);
	            } catch (FileNotFoundException e) {  
	                Log.e("Exception", e.getMessage(),e);  
	            }  
			}else if(resultCode == FragmentActivity.RESULT_CANCELED){
				Toast.makeText(getActivity(), "取消选择照片", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getActivity(), "选择图片失败", Toast.LENGTH_LONG).show();
			}
		}
	}
}
