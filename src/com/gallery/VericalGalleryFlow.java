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
import android.view.ViewConfiguration;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class VericalGalleryFlow extends Gallery {

    private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 60;
    private int mMaxZoom = -120;
    private int mCoveflowCenter;

    private boolean mAlphaMode = true;
 	private boolean mCircleMode = false;
 	
    public VericalGalleryFlow(Context context) {
            super(context);
            this.setStaticTransformationsEnabled(true);
    }

    public VericalGalleryFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
    }

    public VericalGalleryFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
    }
    
    /**
     * 垂直gallery
     */
    @Override
    protected void onDraw(Canvas canvas) {
	    // TODO Auto-generated method stub
	    canvas.translate(0, 600f);
	    canvas.rotate(-90f);
	    super.onDraw(canvas);
    }
    /***
     * 垂直控制
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
	    switch (keyCode) {
		    case KeyEvent.KEYCODE_DPAD_UP:
		    	Log.i("KeyEvent.KEYCODE_DPAD_UP","KeyEvent.KEYCODE_DPAD_UP");
		    	return super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, event);
		    case KeyEvent.KEYCODE_DPAD_DOWN:
		    	Log.i("KeyEvent.KEYCODE_DPAD_UP","KeyEvent.KEYCODE_DPAD_UP");
		    	return super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, event);
		    case KeyEvent.KEYCODE_DPAD_LEFT:
		    case KeyEvent.KEYCODE_DPAD_RIGHT:
		    case KeyEvent.KEYCODE_DPAD_CENTER:
		    case KeyEvent.KEYCODE_ENTER:
		    	break;
		    default:
		    		break;
	    }
	    Log.i("KeyEvent.keyCode",keyCode+"");
	    return super.onKeyDown(keyCode, event);
    }
   
    private int verticalMinDistance = 30;  
    private int minVelocity         = 0;  
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//    		float velocityY) {
//    	Log.i("onFling", "onFling");
//    	// TODO Auto-generated method stub
//    	if(e1.getX()- e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
//    		//向左
//    		Log.i("onFling", "left");
//    		//return true;
//        }else if(e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) { 
//        	//向右
//        	Log.i("onFling", "right");
//        	//return true;
//    	}else if(e1.getY() - e2.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity) { 
//        	//向上，变向右
//    		Log.i("onFling", "up,"+e1.getY()+","+e2.getY());
//    		//float x=e1.getX()+e2.getY() - e1.getY();
//    		e2.setLocation(e1.getX()+e1.getY() - e2.getY(), e1.getY());
//    		//e1.setLocation(x, 0);
//    		return super.onFling(null, null,e1.getY() - e2.getY(), 1);
//    	}else if(e2.getY() - e1.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity) { 
//        	//向下，变向左
//    		Log.i("onFling", "down,"+e1.getY()+","+e2.getY());
//    		//float y=e2.getY();
//    		//e2.setLocation(e1.getX()-(e2.getY() - e1.getY()), e1.getY());
//    		//e1.setLocation(y, 0);
//    		return super.onFling(null, null, e1.getY()- e2.getY(), 1);
//    	}
//    	return true;
//    }
    
    private int mTouchStartX;
    private int mTouchStartY;
	private static final int TOUCH_STATE_RESTING = 0;
	private static final int TOUCH_STATE_CLICK = 1;
	private static final int TOUCH_STATE_SCROLL = 2;
	private int mCurrentTouchState = TOUCH_STATE_RESTING;
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getChildCount() == 0) {
			return false;
		}
		int cur_position=-1;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				cur_position=pointToPosition((int) event.getX(), (int) event.getY());
				Log.i("touch down position", cur_position+"");
				if(cur_position>0)
				{
					setSelection(cur_position);
				}
				startTouch(event);
				break;
			case MotionEvent.ACTION_MOVE:
				if (mCurrentTouchState == TOUCH_STATE_CLICK) {
					startScrollIfNeeded(event);
				}
				if (mCurrentTouchState == TOUCH_STATE_SCROLL) {
					int scrolledXdistance=(int) event.getX() - mTouchStartX;
					int scrolledYDistance = (int) event.getY() - mTouchStartY;
//					Log.i("move x", event.getX()+"");
//					Log.i("move y",  event.getY() +"");
//					Log.i("x distance", scrolledXdistance+"");
//					Log.i("y distance", scrolledYDistance+"");
					event.setLocation(mTouchStartX-scrolledYDistance, mTouchStartY+scrolledXdistance);
					return super.onTouchEvent(event);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mCurrentTouchState == TOUCH_STATE_CLICK) {
					
				}
				endTouch();
				int scrolledXdistance=(int) event.getX() - mTouchStartX;
				int scrolledYDistance = (int) event.getY() - mTouchStartY;
//				Log.i("up x", event.getX()+"");
//				Log.i("up y",  event.getY() +"");
				event.setLocation(mTouchStartX-scrolledYDistance, mTouchStartY+scrolledXdistance);
				return super.onTouchEvent(event);
			default:
				endTouch();
				break;
		}
		return true;
	}
    
//	 @Override
//	public boolean onTouchEvent(MotionEvent event) {
//		 Log.i("onTouch", "onTouch");
//		return false;
//	}
	
    private void startTouch(MotionEvent event) {
		mTouchStartX = (int) event.getX();
		mTouchStartY = (int) event.getY();
		Log.i("down x", mTouchStartX+"");
		Log.i("down y", mTouchStartY+"");
		mCurrentTouchState = TOUCH_STATE_CLICK;
	}
    
    private boolean startScrollIfNeeded(MotionEvent event) {
    	mCurrentTouchState = TOUCH_STATE_SCROLL;
    	return true;
//		int yDistance = (int) event.getY() - mTouchStartY;
//		int threshold = ViewConfiguration.get(getContext())
//				.getScaledTouchSlop();
//		if (Math.abs(yDistance) > threshold) {
//			mCurrentTouchState = TOUCH_STATE_SCROLL;
//			//removeCallbacks(mLongClickRunnable);
//			return true;
//		} else {
//			return false;
//		}
	}
    private void endTouch() {
		mCurrentTouchState = TOUCH_STATE_RESTING;
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
        t.setTransformationType(Transformation.TYPE_BOTH);        // alpha 和 matrix 都变换 
 
        if (childCenter == mCoveflowCenter)  
        {   // 正中间的childView 
            transformImageBitmap((ImageView) child, t, 0);     
        }  
        else  
        {   // 两侧的childView 
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
