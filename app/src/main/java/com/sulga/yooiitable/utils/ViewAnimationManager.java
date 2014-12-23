package com.sulga.yooiitable.utils;

import android.view.*;
import android.view.animation.*;



public class ViewAnimationManager {
	public static void moveViewVertical(int moveDistance, 
			View viewToMove, int startTimeOffset, int duration,
			boolean moveViewUpper, boolean fillEnabled, boolean fillAfter,
			Animation.AnimationListener animListener){
		int moveDirection = moveViewUpper ? -1 : 1;
		
		float moveRate = (float)moveDistance 
				/ (float)viewToMove.getHeight() * moveDirection;
		
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation transAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 
				moveRate
				);
		//transAnimation.
		transAnimation.setDuration(duration);
		transAnimation.setStartOffset(startTimeOffset);
		transAnimation.setFillEnabled(fillEnabled);
		transAnimation.setFillAfter(fillAfter);
		
		transAnimation.setAnimationListener(animListener);
		set.addAnimation(transAnimation);
		
		
		viewToMove.startAnimation(transAnimation);
		//animatedCount++;
	}
}
