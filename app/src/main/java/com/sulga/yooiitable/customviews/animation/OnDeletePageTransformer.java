package com.sulga.yooiitable.customviews.animation;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.*;

import com.sulga.yooiitable.mylog.*;

public class OnDeletePageTransformer implements PageTransformer {
	private static float MIN_SCALE = 0f;
	private static float MIN_ALPHA = 0.5f;

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();
		MyLog.d("AddPage", "position : " + position);

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		} else if (position <= 0) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			//float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			//float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			float moveVert = pageHeight * (1 - scaleFactor);
			if (position < 0) {
				//view.setTranslationX(horzMargin - vertMargin / 2);
				//view.setTranslationY(vertMargin - horzMargin / 2); 
				view.setTranslationY(moveVert);
			} else {
				//view.setTranslationX(-horzMargin + vertMargin / 2);
				view.setTranslationY(moveVert);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA +
					(scaleFactor - MIN_SCALE) /
					(1 - MIN_SCALE) * (1 - MIN_ALPHA));

			//view.setTranslationY(translationY)
		}else if(position <= 1){
			view.setAlpha(1);
			view.setTranslationX(0);
			view.setScaleX(1);
			view.setScaleY(1);
		}else { // (1,+Infinity]
				// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}