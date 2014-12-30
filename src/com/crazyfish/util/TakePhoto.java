package com.crazyfish.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.crazyfish.util.GlobalVariable;
import android.net.Uri;
import android.os.Environment;

public class TakePhoto {
	public Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}
	public File getOutputMediaFile(int type){
		File imageStoreDir = null;
		try{
			imageStoreDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"istorehere");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if( !imageStoreDir.exists()){
			if(!imageStoreDir.mkdirs()){
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File imageFile = null;
		if( type == GlobalVariable.MEDIA_TYPE_IMAGE){
			imageFile = new File(imageStoreDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");
		}else{
			return null;
		}
		return imageFile;
	}
}
