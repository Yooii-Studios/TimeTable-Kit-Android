package com.sulga.yooiitable.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HalfSquareLinearLayout extends LinearLayout {

	public HalfSquareLinearLayout(Context context) {
		super(context);
	}

	public HalfSquareLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HalfSquareLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//noinspection SuspiciousNameCombination
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		int width = getMeasuredWidth();
	    int height = getMeasuredHeight();
	    setMeasuredDimension(width, height / 4 * 3);		
//	    super.onMeasure(widthMeasureSpec, widthMeasureSpec / 2);
	}

}
