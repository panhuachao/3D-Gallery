package com.gallery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private Integer[] mImageIds;
	private ImageView[] mImages;

	public ImageAdapter(Context c, Integer[] ImageIds) {
		mContext = c;
		mImageIds = ImageIds;
		mImages = new ImageView[mImageIds.length];
	}

	public boolean createReflectedImages() 
	{ 
        final int reflectionGap = 4; 
        final int Height = 200; 
        int index = 0; 
        for (int imageId : mImageIds)
        { 
        	Bitmap originalImage = BitmapFactory.decodeResource(mContext
					.getResources(), imageId);   
            int width = originalImage.getWidth(); 
            int height = originalImage.getHeight(); 
            float scale = Height / (float)height; 
             
            Matrix sMatrix = new Matrix(); 
            sMatrix.postScale(scale, scale); 
            Bitmap miniBitmap = Bitmap.createBitmap(originalImage, 0, 0, 
                    originalImage.getWidth(), originalImage.getHeight(), sMatrix, true); 
             
            //是否原图片数据，节省内存 
            originalImage.recycle(); 
 
            int mwidth = miniBitmap.getWidth(); 
            int mheight = miniBitmap.getHeight(); 
            Matrix matrix = new Matrix(); 
            // 图片矩阵变换（从低部向顶部的倒影） 
            matrix.preScale(1, -1);             
            // 截取原图下半部分 
            Bitmap reflectionImage = Bitmap.createBitmap(miniBitmap, 0, mheight/2, mwidth, mheight/2, matrix, false); 
            // 创建倒影图片（高度为原图3/2） 
            Bitmap bitmapWithReflection = Bitmap.createBitmap(mwidth, (mheight + mheight / 2), Config.ARGB_8888);     
            // 绘制倒影图（原图 + 间距 + 倒影） 
            Canvas canvas = new Canvas(bitmapWithReflection);     
            // 绘制原图 
            canvas.drawBitmap(miniBitmap, 0, 0, null);         
            Paint paint = new Paint(); 
            // 绘制原图与倒影的间距 
            canvas.drawRect(0, mheight, mwidth, mheight + reflectionGap, paint); 
            // 绘制倒影图 
            canvas.drawBitmap(reflectionImage, 0, mheight + reflectionGap, null);     
 
            paint = new Paint(); 
            // 线性渐变效果 
            LinearGradient shader = new LinearGradient(0, miniBitmap.getHeight(), 0, bitmapWithReflection.getHeight() 
                    + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP); 
            paint.setShader(shader);     
            // 倒影遮罩效果 
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));         
            // 绘制倒影的阴影效果 
            canvas.drawRect(0, mheight, mwidth, bitmapWithReflection.getHeight() + reflectionGap, paint);         
            ImageView imageView = new ImageView(mContext);  // 设置倒影图片 
            imageView.setImageBitmap(bitmapWithReflection);         
            imageView.setLayoutParams(new GalleryFlow.LayoutParams((int)(width * scale), 
                    (int)(mheight * 3 / 2.0 + reflectionGap))); 
            imageView.setScaleType(ScaleType.MATRIX); 
            mImages[index++] = imageView; 
        }
        return true; 
    }
	
	public boolean createImages() 
	{
        final int reflectionGap = 4; 
        final int Height = 200; 
        int index = 0; 
        for (int imageId : mImageIds)
        { 
        	Bitmap originalImage = BitmapFactory.decodeResource(mContext
					.getResources(), imageId);   
            int width = originalImage.getWidth(); 
            int height = originalImage.getHeight(); 
            float scale = Height / (float)height; 
             
            Matrix sMatrix = new Matrix(); 
            sMatrix.postScale(scale, scale); 
            Bitmap miniBitmap = Bitmap.createBitmap(originalImage, 0, 0, 
                    originalImage.getWidth(), originalImage.getHeight(), sMatrix, true); 
             
            //是否原图片数据，节省内存 
            originalImage.recycle(); 
 
            int mwidth = miniBitmap.getWidth(); 
            int mheight = miniBitmap.getHeight();
            
            ImageView imageView = new ImageView(mContext); 
            imageView.setImageBitmap(miniBitmap);         
            imageView.setLayoutParams(new VericalGalleryFlow.LayoutParams((int)(width * scale), 
                    (int)(mheight * 3 / 2.0 + reflectionGap))); 
            imageView.setScaleType(ScaleType.MATRIX); 
            mImages[index++] = imageView; 
        }
        return true; 
    }
	
	public boolean createNormalImages()
	{
		int index = 0; 
		for (int imageId : mImageIds)
	    { 
		  Bitmap originalImage = BitmapFactory.decodeResource(mContext
				.getResources(), imageId);   
		  ImageView imageView = new ImageView(mContext);
          imageView.setImageBitmap(originalImage);     
          mImages[index++] = imageView;
	    }
		return true;
	}
	
	
	private Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return mImages[position];
	}

//	public float getScale(boolean focused, int offset) {
//		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
//	}

}
