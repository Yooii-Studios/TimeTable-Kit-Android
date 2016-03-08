package com.sulga.yooiitable.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SoftKeyboardDetectLinearLayout extends LinearLayout {
	OnSoftKeyboardStateChangedListener onSoftKeyboardStateChangedListener;

	public SoftKeyboardDetectLinearLayout(Context context) {
		super(context);
	}

	public SoftKeyboardDetectLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SoftKeyboardDetectLinearLayout(Context context, AttributeSet attrs, int defStyle ) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
		final int actualHeight = getHeight();

	
		if(onSoftKeyboardStateChangedListener != null){
			if (actualHeight > proposedHeight){
				onSoftKeyboardStateChangedListener.onKeyboardAppear();
				//MyLog.d("onMeasure", MeasureSpec.toString(heightMeasureSpec));
			
			} else {
				onSoftKeyboardStateChangedListener.onKeyboardHidden();
				//MyLog.d("onMeasure", MeasureSpec.toString(heightMeasureSpec));
			}
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

//	public void setOnSoftKeyboardStateChangedListener(OnSoftKeyboardStateChangedListener listener){
//		this.onSoftKeyboardStateChangedListener = listener;
//	}

	public interface OnSoftKeyboardStateChangedListener{
		void onKeyboardAppear();
		void onKeyboardHidden();
	}
}
