package com.sulga.yooiitable.customviews;

import org.holoeverywhere.widget.*;

import android.content.*;
import android.util.*;
import android.view.*;



public class ScheduleViewPager extends ViewPager {

	private boolean enabled;

	public ScheduleViewPager(Context context){
		super(context);
	}
	public ScheduleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	} }