package com.crazyfish.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.crazyfish.cache.ImageFileCache;
import com.crazyfish.cache.ImageGetFromHttp;
import com.crazyfish.cache.ImageMemoryCache;

public class PicHandler {
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) { 
        
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
        Canvas canvas = new Canvas(output); 
 
        final int color = 0xff424242; 
        final Paint paint = new Paint(); 
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
        final RectF rectF = new RectF(rect); 
        final float roundPx = pixels; 
 
        paint.setAntiAlias(true); 
        canvas.drawARGB(0, 0, 0, 0); 
        paint.setColor(color); 
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
 
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
        canvas.drawBitmap(bitmap, rect, rect, paint); 
 
        return output; 
    }
	public static Bitmap getBitmap(String url,Context context) {
		ImageFileCache fileCache = new ImageFileCache();
		ImageMemoryCache memoryCache = new ImageMemoryCache(context);
		// ���ڴ滺���л�ȡͼƬ
		Bitmap result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			// �ļ������л�ȡ
			result = fileCache.getImage(url);
			if (result == null) {
				// �������ȡ
				result = ImageGetFromHttp.downloadBitmap(url);
				if (result != null) {
					fileCache.saveBitmap(result, url);
					memoryCache.addBitmapToCache(url, result);
				}
			} else {
				//Log.i("�ڴ���û��", "�ڴ���û�У�ֻ�ܴ��ļ������л�ȡ");
				// ��ӵ��ڴ滺��
				memoryCache.addBitmapToCache(url, result);
			}
		}
		//Log.i("xx","�ڴ����У����ڴ���ֱ�Ӽ���");
		return result;
	}
	/*�ӱ����ļ���ȡͼƬ*/
	public static Bitmap getBitmapFromFile(String uri){
		int width = 300;
        int height = 300;
        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, factoryOptions);
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
       Bitmap thumbnail = BitmapFactory.decodeFile(uri,
                factoryOptions);
       return thumbnail;
	}
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){

        // 获得图片的宽高

        int width = bm.getWidth();

        int height = bm.getHeight();

        // 计算缩放比例

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // 取得想要缩放的matrix参数

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片

        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        return newbm;

    }
}
