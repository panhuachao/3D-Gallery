package com.gallery;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {

    private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 50;
    private int mMaxZoom = -120;
    private int mCoveflowCenter;
    
    private boolean mAlphaMode = true;
	private boolean mCircleMode = false;
	
    public GalleryFlow(Context context) {
            super(context);
            this.setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
    }
    
    
    public int getMaxRotationAngle() {
            return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
            mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
            return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
            mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }

    
    protected boolean getChildStaticTransformation(View child, Transformation t)  { 
    	//图像的中心点和宽度 
        final int childCenter = getCenterOfView(child); 
        final int childWidth = child.getWidth(); 
        int rotationAngle = 0; 
 
        t.clear(); 
        //t.setTransformationType(Transformation.TYPE_BOTH);        // alpha 和 matrix 都变换 
        t.setTransformationType(Transformation.TYPE_MATRIX);
 
        if (childCenter == mCoveflowCenter)  
        {      // 正中间的childView 
            transformImageBitmap((ImageView) child, t, 0);     
        }  
        else  
        {          // 两侧的childView 
            rotationAngle = (int) ( ( (float) (mCoveflowCenter - childCenter) / childWidth ) * mMaxRotationAngle ); 
            if (Math.abs(rotationAngle) > mMaxRotationAngle)  
            { 
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle; 
            }
            //根据偏移角度对图片进行处理，看上去有3D的效果
            //transformImageBitmap((ImageView) child, t, rotationAngle); 
            //加透明
            transformAlphaImageBitmap((ImageView) child, t, rotationAngle); 
        }
 
        return true; 
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         //重写计算旋转的中心 
         mCoveflowCenter = getCenterOfCoverflow();
         super.onSizeChanged(w, h, oldw, oldh);
    }

    private void transformImageBitmap(ImageView child, Transformation t,
                    int rotationAngle) {
            mCamera.save();
            final Matrix imageMatrix = t.getMatrix();
            final int imageHeight = child.getLayoutParams().height;
            final int imageWidth = child.getLayoutParams().width;
            final int rotation = Math.abs(rotationAngle);

            // 在Z轴上正向移动camera的视角，实际效果为放大图片。  
            // 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。  
            mCamera.translate(0.0f, 0.0f, 100.0f);
            //mCamera.translate(100.0f, 0.0f, 0.0f);

            // As the angle of the view gets less, zoom in
            if (rotation < mMaxRotationAngle) {
                    float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
                    mCamera.translate(0.0f, 0.0f, zoomAmount);
            }

            // 在Y轴上旋转，对应图片竖向向里翻转。  
            // 如果在X轴上旋转，则对应图片横向向里翻转。
            mCamera.rotateY(rotationAngle);
            //mCamera.rotateX(rotationAngle);
            mCamera.getMatrix(imageMatrix);
            imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
            imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
            mCamera.restore();
    }
    
    
    private void transformAlphaImageBitmap(ImageView child, Transformation t,
            int rotationAngle) {
    	mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);
		mCamera.translate(0.0f, 0.0f, 100.0f);
		// As the angle of the view gets less, zoom in
		if (rotation <= mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
			if (mCircleMode) {
				if (rotation < 40)
					mCamera.translate(0.0f, 155, 0.0f);
				else
					mCamera.translate(0.0f, (255 - rotation * 2.5f), 0.0f);
			}
			if (mAlphaMode) {
				((ImageView) (child)).setAlpha((int) (255 - rotation * 2.5));
			}
		}
		mCamera.rotateY(rotationAngle);
		mCamera.getMatrix(imageMatrix);

		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();
    }
}
