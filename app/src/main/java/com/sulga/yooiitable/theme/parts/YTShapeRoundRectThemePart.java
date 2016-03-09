package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.graphics.drawable.shapes.*;
import android.view.*;

import com.sulga.yooiitable.utils.*;

public class YTShapeRoundRectThemePart extends YTRoundRectThemePart {

//	private int resId;
	private float radius;
	private int alpha;
	private int color;
	private int borderColor;
	private int borderWidth;
//	private ShapeDrawable sd;
	public YTShapeRoundRectThemePart(float radius, 
			int alpha, int color, 
			int borderColor, int borderWidth) {
		// TODO Auto-generated constructor stub
//		this.resId = resId;
		this.radius = radius;
		this.alpha = alpha;
		this.color = color;
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
	}

	/**
	 * sets round rect all corners
	 */
	@Override
	public View setViewTheme(Context context, View v) {
		// TODO Auto-generated method stub
//		Drawable d = context.getResources().getDrawable(resId);
		
//		sd.setAlpha(alpha);
//		v.setBackgroundDrawable(d);
//		GradientDrawable gd = (GradientDrawable)v.getBackground();
//		gd.setColor(color);
//		MyLog.d("YTShapeRoundRectThemePart", "setViewTheme called");
		Shape s = new RoundRectShape(
				getRoundRectCornersArray(radius, true, true, true, true), 
				null, 
				null);
		
		StrokeShapeDrawable sd = 
				new StrokeShapeDrawable(s, color, alpha, borderColor, borderWidth);
		
//				sd.setShape(s);
//		sd.setAlpha(alpha);
//		sd.getPaint().setColor(color);
		
		v.setBackgroundDrawable(sd);
		return v;
	}
	
	
	@Override
	public View setViewTheme(Context context, View v,
			float radius,
			boolean LT, boolean RT, boolean RB, boolean LB) {
		// TODO Auto-generated method stub
//		ShapeDrawable d = new ShapeDrawable();
//		MyLog.d("YTShapeRoundRectThemePart", "setViewTheme called");
		Shape s = new RoundRectShape(
				getRoundRectCornersArray(radius, LT, RT, RB, LB), 
				null, 
				null);
		StrokeShapeDrawable sd = 
				new StrokeShapeDrawable(s, color, alpha, borderColor, borderWidth);
//		sd.setShape(s);
//		sd.setAlpha(alpha);
//		sd.getPaint().setColor(color);
//		sd.setShape(s);
		v.setBackgroundDrawable(sd);
		return v;
	}
	
	@Override
	public View setViewTheme(Context context, View v,
			float radius,
			boolean LT, boolean RT, boolean RB, boolean LB,
			boolean strokeL, boolean strokeT, boolean strokeR, boolean strokeB) {
		// TODO Auto-generated method stub
//		ShapeDrawable d = new ShapeDrawable();
//		MyLog.d("YTShapeRoundRectThemePart", "setViewTheme called");
		Shape s = new RoundRectShape(
				getRoundRectCornersArray(radius, LT, RT, RB, LB), 
				null,
				null);
		StrokeShapeDrawable sd = 
				new StrokeShapeDrawable(s, color, alpha, borderColor, borderWidth,
						strokeL, strokeT, strokeR, strokeB);
//		sd.setShape(s);
//		sd.setAlpha(alpha);
//		sd.getPaint().setColor(color);
//		sd.setShape(s);
		v.setBackgroundDrawable(sd);
		return v;
	}

	public int getColor(){
		return color;
	}
	
	/**
	 * You should call setViewTheme after setColor!!
	 * @param _color
	 */
	public void setColor(int _color){
		this.color = _color;
	}
}
