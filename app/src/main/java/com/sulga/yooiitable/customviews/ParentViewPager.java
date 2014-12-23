package com.sulga.yooiitable.customviews;

import org.holoeverywhere.widget.*;

import android.content.*;
import android.util.*;
import android.view.*;

import com.sulga.yooiitable.mylog.*;

public class ParentViewPager extends ViewPager {

	public ParentViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ParentViewPager(Context context, AttributeSet attrs){
		super(context, attrs);
		//init(context);
	}


	private boolean mDisablePaging = false;
	public void disablePaging(boolean isDisabled){
		MyLog.d("ParentViewPager", "disable Paging : " + isDisabled);
		mDisablePaging = isDisabled;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// Don't allow swiping to switch between pages if we disabled it
		if (mDisablePaging) {
			return false;
		}

		MyLog.d("ParentViewPager", "onInterceptTouchEvent");

		// Otherwise, do the normal behavior 
		try{
			return super.onInterceptTouchEvent(arg0);
		}catch(IllegalArgumentException ex){
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0){
		if(mDisablePaging){
			return false;
		}

		try{
			return super.onTouchEvent(arg0);
		}catch(IllegalArgumentException ex){
			//disablePaging이 false가 되고나서 계속 터치이벤트를 막 줘버리면 
			//pointerIndex out of range버그.
			ex.printStackTrace();
		}
		return false;		
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		//MyLog.d("ParentViewPager", "canScroll? dx : " + dx + ", x : " + x + ", y : " + y);

		if(v != this && v instanceof ViewPager) {
			if(dx < 0){
				//오른쪽으로 스크롤
				return true;
			}else{
				if( ((ViewPager)v).getCurrentItem() != 0){
					return true;
				}
				return false;
			}
			//return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}
}
