package com.sulga.yooiitable.utils;

import android.content.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;

public class LazyViewAnimationManager {

	/**
	 * Registers view animation, method only used in lesson view currently so 
	 * no animation parameter exists currently. 
	 * *CAUTION : You MUST call this method after view is placed, no onCreate call allowed!
	 * @param context
	 * @param delayTime
	 * @param v : view to register animation
	 * @param viewToCenterInside : view's parent, so parameter v centers inside this parameter
	 */
	public static void registerLessonViewAnimation(final Context context,
			int delayTime, final View v, final Animation animation){
//		final TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f,
//				0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
//		animation.setDuration(400);  // animation duration 
		//animation.setFillAfter(true);      
		v.postDelayed(new Runnable(){
			@Override
			public void run() {
				v.setVisibility(View.VISIBLE);
				v.startAnimation(animation);
			}
		}, delayTime);
	}
}
