package com.sulga.yooiitable.customviews;

import org.holoeverywhere.widget.*;

import android.content.*;
import android.util.*;

public class HalfSquareLinearLayout extends LinearLayout {

	public HalfSquareLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HalfSquareLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HalfSquareLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	    int size = 0;
	    int width = getMeasuredWidth();
	    int height = getMeasuredHeight();
	    setMeasuredDimension(width, height / 4 * 3);		
//	    super.onMeasure(widthMeasureSpec, widthMeasureSpec / 2);
	}

}
