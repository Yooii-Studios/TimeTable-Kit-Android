package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;

import com.sulga.yooiitable.utils.*;

public class YTTiledRoundedRectBitmap extends YTRoundRectThemePart {

	private int resId;
	private int alpha;
	private float radius;
	private float[] corners;
	public YTTiledRoundedRectBitmap(int resId, int alpha, 
			float radius) {
		// TODO Auto-generated constructor stub
		this.resId = resId;
		this.alpha = alpha;
		this.radius = radius;
//		this.corners = getRoundRectCornersArray(radius, roundLT, roundRT, roundRB, roundLB);
	}

	@Override
	public View setViewTheme(Context context, View v) {
		// TODO Auto-generated method stub
		Drawable d = 
				BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
						context, 
						Shader.TileMode.REPEAT,
						Shader.TileMode.REPEAT,
						getRoundRectCornersArray(radius, true, true, true, true),
						resId
						);
		d.setAlpha(alpha);
		v.setBackgroundDrawable(d);
		return v;
	}

	@Override
	public View setViewTheme(Context context, View v, 
			float radius,
			boolean LT, boolean RT, boolean RB, boolean LB){
		Drawable d = 
				BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
						context, 
						Shader.TileMode.REPEAT,
						Shader.TileMode.REPEAT,
						getRoundRectCornersArray(radius, LT, RT, RB, LB),
						resId
						);
		d.setAlpha(alpha);
		v.setBackgroundDrawable(d);
		return v;
	}

	@Override
	public View setViewTheme(Context context, View v, float radius, boolean LT,
			boolean RT, boolean RB, boolean LB, boolean strokeL,
			boolean strokeT, boolean strokeR, boolean strokeB) {
		// TODO Auto-generated method stub
		return setViewTheme(context, v, radius, 
				LT, RT, RB, LB);

	}
}
