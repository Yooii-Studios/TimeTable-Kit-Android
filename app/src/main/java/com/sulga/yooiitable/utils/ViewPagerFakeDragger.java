package com.sulga.yooiitable.utils;

import org.holoeverywhere.widget.*;

import android.content.*;
import android.os.*;

import com.sulga.yooiitable.customviews.*;
import com.sulga.yooiitable.mylog.*;


public class ViewPagerFakeDragger {
	//public static final int MIN_DISTANCE_FOR_FLING = ;		//dip
	public static final int DEFAULT_DELAY_MILLIS = 15;
	public static final int SLOW_DELAY_MILLIS = 20;
	public static final int FAST_DELAY_MILLIS = 5;
	public static void goToRightPage(
			Context context, 
			boolean reverseDrawingOrder,
			ViewPager.PageTransformer pageTransformer,
			final ViewPager pager,
			float f,
			int delayMillis){
//		float density = context.getResources().getDisplayMetrics().density;
//		final int mFlingDistance = (int) (f * density);
        final int mFlingDistance = (int)f;
		final int loopCount = 30;	//10번 루프돌며 스와이프.

		pager.setPageTransformer(reverseDrawingOrder, pageTransformer);
		//pager.setOnPageChangeListener(onPageChangeListener);
		//		pager.requestDisallowInterceptTouchEvent(true);
		if(pager instanceof ParentViewPager){
			((ParentViewPager) pager).disablePaging(true);
		}

		pager.beginFakeDrag();
		for(int i = 1; i <= loopCount ; i++){
			final int idx = i;
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					int flingedDistance = (mFlingDistance / loopCount) * (idx + 1);
					MyLog.d("RemovePage", "Page Dragging : " + flingedDistance);
					// TODO Auto-generated method stub
					try{
						pager.fakeDragBy(-1 * (mFlingDistance / loopCount));
					}catch(Exception e){
						e.printStackTrace();
					}
				}},	delayMillis * i);
		}
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				MyLog.d("RemovePage", "end dragging...");
				try{
					pager.endFakeDrag();
				}catch(Exception e){
					e.printStackTrace();
				}
				//                    pager.requestDisallowInterceptTouchEvent(false);

			}}, (loopCount+1) * delayMillis
				);
	}

	public interface OnFakeDragEndListener{
		public void onFakeEnded(ViewPager mPager);
	}
}
