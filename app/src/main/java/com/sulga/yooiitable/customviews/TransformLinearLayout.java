package com.sulga.yooiitable.customviews;

import org.holoeverywhere.widget.LinearLayout;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.sulga.yooiitable.mylog.*;


public class TransformLinearLayout extends LinearLayout {
	private final String TAG = "TransformLinearLayout";
	private int screenWidth;
	//private Context mContext;
	private Camera mCamera;

	public TransformLinearLayout(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public TransformLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0); 
	}

	public TransformLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs);
//		if (android.os.Build.VERSION.SDK_INT < 
//				android.os.Build.VERSION_CODES.JELLY_BEAN){
//			super(context,attrs);
//			//layout.setBackgroundDrawable(TileMe); 
//		} else{ 
//			//layout.setBackground(TileMe); 
//			super(context, attrs, defStyle);
//		}
		
		init();
		//setSpacing(-30);  // child view 의 간격을 줄여 겹치는 듯한 효과를 준다
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		child.invalidate();
		if(child instanceof CheckBox){
			MyLog.d("getChildStaticTransformation", "child is checkbox");
		}
		HorizontalScrollView parent = (HorizontalScrollView) getParent();
		//*****not calling this, checkbox not invalidated properly.
		invalidate();
		
		int scrollPosition = parent.getScrollX();

//		final int mCenter =
//				(getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
		final int childCenter = child.getLeft() + child.getWidth() / 2;
		final int childWidth = child.getWidth();
		Rect r = new Rect();
		getGlobalVisibleRect(r);
		int viewInScreenWidth = r.width();

//		MyLog.d(TAG, "child : " + child + "\n childCenter : " + childCenter);
//		MyLog.d(TAG, "scrollPosition : " + scrollPosition);

		t.clear();
		t.setTransformationType(Transformation.TYPE_BOTH);
		float rate = Math.abs((float)
				(scrollPosition - childCenter + viewInScreenWidth / 2) 
				/ childWidth);

//		MyLog.d(TAG, "rate : " + rate);
		mCamera.save();
		final Matrix matrix = t.getMatrix();
		float zoomAmount = (float) (rate * 200.0);
		mCamera.translate(0.0f, 0.0f, zoomAmount);        
		mCamera.getMatrix(matrix);    
		matrix.preTranslate(-(childWidth/2), -(childWidth/2));   
		matrix.postTranslate((childWidth/2), (childWidth/2));
		mCamera.restore();

		return true;
	}

	//첫번째 child 뷰 왼쪽과와 마지막 child뷰의 오른쪽에 마진을 더해줌으로서 뷰의 중앙에 멈추도록 만듬.
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b){
		super.onLayout(changed, l, t, r, b);
		addPaddingLeftRight();
	}

	protected void addPaddingLeftRight(){
		Rect r = new Rect();
		getGlobalVisibleRect(r);
		int viewInScreenWidth = r.width();
		
		try{
			if(getChildCount() == 0){
				throw new Exception(
						"TransformLinearLayout must have one child");
			}
		}catch(Exception e){}
		
		if(getChildCount() == 1){
			//if layout only has one child...
			View firstChild = getChildAt(0);
			MyLog.d("TransformLinearLayout", "viewInScreenWidth : " + viewInScreenWidth);
			((LinearLayout.LayoutParams)firstChild.getLayoutParams()).leftMargin
				= ( screenWidth - firstChild.getWidth() ) / 2;
			return;
		}

		View firstChild = getChildAt(0);
		int firstChildCenter= firstChild.getLeft() + firstChild.getWidth() / 2 ;
		View lastChild = getChildAt(getChildCount() - 1);
		int currentPaddingLeft = firstChild.getPaddingLeft();
		int currentPaddingRight = lastChild.getPaddingRight();

		int lastChildCenter = lastChild.getLeft() + lastChild.getWidth() / 2;
		MyLog.d("TransformLinearLayout", 
				"firstChildCenter : " + firstChildCenter + 
				", lastChildCenter : " + lastChildCenter);
		((LinearLayout.LayoutParams)firstChild.getLayoutParams()).leftMargin
		= viewInScreenWidth / 2 - firstChild.getWidth() / 2;

		((LinearLayout.LayoutParams)lastChild.getLayoutParams()).rightMargin
		= viewInScreenWidth / 2 - lastChild.getWidth() / 2;
		//		this.setPadding(
		//				currentPaddingLeft + firstChildCenter, 
		//				this.getPaddingTop(), 
		//				currentPaddingRight + firstChildCenter, 
		//				this.getPaddingBottom()
		//				);
		//		FrameLayout.LayoutParams ilp = 
		//				((FrameLayout.LayoutParams)getLayoutParams());
		//		ilp.setMargins(
		//				ilp.leftMargin, 
		//				ilp.topMargin, 
		//				ilp.rightMargin + firstChildCenter, 
		//				ilp.bottomMargin);
		//internalWrapper.offsetLeftAndRight(viewInScreenWidth / 2);
		//		setLayoutParams(ilp);
		//this.offsetLeftAndRight(viewInScreenWidth / 2);
		MyLog.d("TransformLinearLayout",
				"padding left : " + 
						(currentPaddingLeft + firstChildCenter ) +
						"padding right : " + 
						(currentPaddingRight + lastChildCenter )
				);

		MyLog.d("TransformLinearLayout", 
				"View visible width : " + viewInScreenWidth + 
				", view Width : " + getWidth());
	}
	
	private void init(){
		//mContext = context;
		mCamera = new Camera();
		this.setStaticTransformationsEnabled(true);
		this.setChildrenDrawingOrderEnabled(true);
		screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		//addPaddingLeftRight();
	}

}
