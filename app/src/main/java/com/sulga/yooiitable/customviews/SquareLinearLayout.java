package com.sulga.yooiitable.customviews;

import org.holoeverywhere.widget.*;

import android.content.*;
import android.util.*;

public class SquareLinearLayout extends LinearLayout {

	public SquareLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public SquareLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SquareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
