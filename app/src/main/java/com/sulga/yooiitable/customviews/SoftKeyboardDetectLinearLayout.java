package com.sulga.yooiitable.customviews;

import org.holoeverywhere.widget.*;

import android.content.*;
import android.util.*;

public class SoftKeyboardDetectLinearLayout extends LinearLayout {
	OnSoftKeyboardStateChangedListener onSoftKeyboardStateChangedListener;
	public SoftKeyboardDetectLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SoftKeyboardDetectLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SoftKeyboardDetectLinearLayout(Context context, AttributeSet attrs, int defStyle ) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
		final int actualHeight = getHeight();

	
		if(onSoftKeyboardStateChangedListener != null){
			if (actualHeight > proposedheight){
				onSoftKeyboardStateChangedListener.onKeyboardAppear();
				//MyLog.d("onMeasure", MeasureSpec.toString(heightMeasureSpec));
			
			} else {
				onSoftKeyboardStateChangedListener.onKeyboardHidden();
				//MyLog.d("onMeasure", MeasureSpec.toString(heightMeasureSpec));
			}
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setOnSoftKeyboardStateChangedListener(OnSoftKeyboardStateChangedListener listener){
		this.onSoftKeyboardStateChangedListener = listener;
	}
	public interface OnSoftKeyboardStateChangedListener{
		public void onKeyboardAppear();
		public void onKeyboardHidden();
	}
}
