package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.view.*;

public abstract class YTRoundRectThemePart implements YTThemePart {

	
	@Override
	public abstract View setViewTheme(Context context, View v);
	
	public abstract View setViewTheme(Context context, View v, 
			float radius,
			boolean LT, boolean RT, boolean RB, boolean LB);
	public abstract View setViewTheme(Context context, View v,
			float radius,
			boolean LT, boolean RT, boolean RB, boolean LB,
			boolean strokeL, boolean strokeT, boolean strokeR, boolean strokeB);
	
	protected static float[] getRoundRectCornersArray(
			float radius,
			boolean LT, boolean RT,
			boolean RB, boolean LB){
		float[] corners = new float[]{
			0,0,0,0,
			0,0,0,0
		};
		
		if(LT){
			corners[0] = radius;
			corners[1] = radius;
		}
		if(RT){
			corners[2] = radius;
			corners[3] = radius;
		}
		if(RB){
			corners[4] = radius;
			corners[5] = radius;
		}
		if(LB){
			corners[6] = radius;
			corners[7] = radius;
		}
		
		return corners;
	}

	
}
