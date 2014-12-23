package com.sulga.yooiitable.customviews.animation;


import android.support.v4.view.ViewPager.PageTransformer;
import android.view.*;

import com.sulga.yooiitable.mylog.*;

public class OnAddPageTransformer implements PageTransformer {
	private static float MIN_SCALE = 0f;
	private static float MIN_ALPHA = 0.5f;

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();
		MyLog.d("AddPage", "position : " + position + ", PageWidth : " + pageWidth);

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		} else if (position <= 0) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			//float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			//float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			float moveLeft = pageWidth * (1 - scaleFactor);

			MyLog.d("AddPage", "move Left : " + moveLeft);

			if(position < 0){
				view.setTranslationX(moveLeft * -1);
			}else if(position == 0){
				view.setTranslationX(0);
			}

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA +
					(scaleFactor - MIN_SCALE) /
					(1 - MIN_SCALE) * (1 - MIN_ALPHA));

			//view.setTranslationY(translationY)
		}else if(position <= 1){
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position * 4));
			//float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			//float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			float moveRight = pageWidth * (1 - scaleFactor);

			MyLog.d("AddPage", "move Right : " + moveRight);

			if(position < 1){
				view.setTranslationX(moveRight);
			}else if(position == 1){
				//view.setTranslationX(0);
				
			}

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA +
					(scaleFactor - MIN_SCALE) /
					(1 - MIN_SCALE) * (1 - MIN_ALPHA));		
		}else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(1);
		}
	}
}