package com.sulga.yooiitable.customviews;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.mylog.*;

public class SnappingHorizontalScrollView extends HorizontalScrollView {
	private static final int STOPSCROLL_MIN = 15;
	private Runnable scrollerTask;
	private int initialPosition;

	private int newCheck = 50;
	private static final String TAG = "SnappingHorizontalScrollView";
//	private boolean isScrollStopped = true;

	public interface OnScrollStoppedListener{
		void onScrollStopped();
	}
	private OnScrollStoppedListener onScrollStoppedListener;
	private OnScrollStoppedListener onSnappingFinishedListener;

	public SnappingHorizontalScrollView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public SnappingHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public SnappingHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
		// TODO Auto-generated constructor stub
	}
	private void init(){
		this.setSmoothScrollingEnabled(true);
		scrollerTask = new Runnable() {

			@Override
			public void run() {

				int newPosition = getScrollX();
				int posOffset = initialPosition - newPosition;
				MyLog.d(TAG, "posOffset : " + posOffset);
				
				if(Math.abs(initialPosition - newPosition) < STOPSCROLL_MIN){//has stopped

					if(onScrollStoppedListener!=null){

						onScrollStoppedListener.onScrollStopped();
					}
				}else{
					initialPosition = getScrollX();
					SnappingHorizontalScrollView.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};

	}

	private TransformLinearLayout tl;
	private int leftMargin = -1;
	public void setSnappingFeatures(final TransformLinearLayout tl){
		this.tl = tl;
		//		if(tl.getChildCount() == 0){
		//			return;
		//		}

		setOnScrollStoppedListener(new OnScrollStoppedListener() {

			@Override
			public void onScrollStopped() {

				int currentChildIdx = getCurrentFeaturedItem();
				View currentChild = tl.getChildAt(currentChildIdx);
				int scrollTo = 
						currentChild.getLeft() 
						- leftMargin;
				smoothScrollTo(scrollTo, 0);

				if(onSnappingFinishedListener != null){
					onSnappingFinishedListener.onScrollStopped();
				}
				
				MyLog.d(TAG, "stopped");
			}
		});
		
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(leftMargin == -1){
					//init left margin value
					LinearLayout.LayoutParams lp =
							(LinearLayout.LayoutParams)tl.getChildAt(0).getLayoutParams();
					leftMargin = lp.leftMargin;
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					MyLog.d(TAG, "ScrollerTask start!");
					SnappingHorizontalScrollView.this.startScrollerTask();
				}

				return false;

				//            	 MyLog.d("SnappingHorizontalScrollView",
				//                 		"scrollX : " + getScrollX() + 
				//                 		", mActiveFeature : " + mActiveFeature);
				//If the user swipes

				//				if(leftMargin == -1){
				//					//init left margin value
				//					LinearLayout.LayoutParams lp =
				//							(LinearLayout.LayoutParams)tl.getChildAt(0).getLayoutParams();
				//					leftMargin = lp.leftMargin;
				//				}
				//				
				//				if (mGestureDetector.onTouchEvent(event)) {
				//					return true;
				//				}
				//				else if(event.getAction() == MotionEvent.ACTION_UP 
				//						|| event.getAction() == MotionEvent.ACTION_CANCEL ){
				//					
				//					int currentChildIdx = getCurrentFeaturedItem();
				//					View currentChild = tl.getChildAt(currentChildIdx);
				//					int scrollTo = 
				//							currentChild.getLeft() 
				//							
				//							- leftMargin;
				//					smoothScrollTo(scrollTo, 0);
				//					
				//					return true;
				//				}
				//				else{
				//					return false;
				//				}
			}
		});
		//		mGestureDetector = new GestureDetector(getContext(), new MyGestureDetector());
	}
	public void setOnScrollStoppedListener(SnappingHorizontalScrollView
			.OnScrollStoppedListener listener){
		onScrollStoppedListener = listener;
	}

	public void startScrollerTask(){

		initialPosition = getScrollY();
		SnappingHorizontalScrollView.this.postDelayed(scrollerTask, newCheck);
	}

	//	@Override
	//	protected void onDraw(Canvas canvas){
	//		super.onDraw(canvas);
	//		//		VelocityTracker vt = VelocityTracker.obtain();
	//		//		Log.d("SnappingHorizontalScrollView", "x speed : " + vt.getXVelocity());
	//		//		vt.recycle();
	//	}
	//
	//	class MyGestureDetector extends SimpleOnGestureListener {
	//		@Override
	//		public boolean onFling(MotionEvent e1, MotionEvent e2,
	//				float velocityX, float velocityY) {
	//			try {
	//				//right to left
	//				MyLog.d("SnappingHorizontalScrollView", "onFling Called, velocityX : " + velocityX);
	//				if(Math.abs(velocityX) < 5){
	//
	//					int currentChildIdx = getCurrentFeaturedItem();
	//					View currentChild = tl.getChildAt(currentChildIdx);
	//
	//					int scrollTo = 
	//							currentChild.getLeft() 
	//
	//							- leftMargin;
	//
	//					MyLog.d("SnappingHorizontalScrollView", "scroll to : " + scrollTo);
	//					smoothScrollTo(scrollTo, 0);
	//				}
	//				//                if(e2.getX() - e1.getX() < SWIPE_MIN_DISTANCE 
	//				//                		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	//				////                    int featureWidth = 
	//				////                    		SnappingHorizontalScrollView.this
	//				////                    		.getFocusedChild().getMeasuredWidth();
	//				////                    MyLog.d("SnappingHorizontalScrollView", "featureWidth : " + featureWidth
	//				////                    		+ ", mActiveFeature : " + mActiveFeature);
	//				////                    mActiveFeature = (mActiveFeature < (tl.getChildCount() - 1)) ? 
	//				////                    				mActiveFeature + 1 : tl.getChildCount() -1;
	//				////                    smoothScrollTo(mActiveFeature * featureWidth, 0);
	//				//                    return true;
	//				//                }
	//				//                //left to right
	//				//                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE 
	//				//                		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	//				////                    int featureWidth = getMeasuredWidth();
	//				////                    mActiveFeature = (mActiveFeature > 0) ? 
	//				////                    		mActiveFeature - 1 : 0;
	//				////                    smoothScrollTo(mActiveFeature*featureWidth, 0);
	//				//                    return true;
	//				//                }
	//			} catch (Exception e) {
	//				Log.e("Fling", 
	//						"There was an error processing the Fling event : " + e.getMessage());
	//			}
	//			return false;
	//		}
	//	}


	int getCurrentFeaturedItem(){

		int activeFeature = -1;
		int scrollX = getScrollX();
		if(scrollX < 0){
			scrollX = 0;
		}

		for(int i = 0; i < tl.getChildCount() ; i++){
			View v = tl.getChildAt(i);

			int left = v.getLeft() - leftMargin;
			int right = v.getRight() - leftMargin;
			int center = ( left + right ) / 2;
			int centerBefore = center - v.getWidth();

			MyLog.d("SnappingHorizontalScrollView", 
					"scrollX : " + scrollX + ", left : " + left + ", right : " + right
					+ ", center : " + center + ", centerBefore : " + centerBefore);
			if(scrollX > centerBefore &&
					scrollX <= center){
				activeFeature = i;
				break;
			}
		}
		MyLog.d("SnappingHorizontalScrollView", "current featured item : " + activeFeature);
		return activeFeature;
	}
	
	public int getCurrentSnappedItem(){
		return getCurrentFeaturedItem();
	}
	
	public void setOnSnappingFinishedListener(OnScrollStoppedListener onSnappingFinishedListener){
		this.onSnappingFinishedListener = onSnappingFinishedListener;
	}
	
}
