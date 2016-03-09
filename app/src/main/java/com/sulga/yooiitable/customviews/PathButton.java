/**
 * KTH Developed by Java <br>
 *
 * @Copyright 2011 by Service Platform Development Team, KTH, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of KTH, Inc. <br>
 * You shall not disclose such Confidential Information and shall use it only <br>
 * in accordance with the terms of the license agreement you entered into with KTH.
 */
package com.sulga.yooiitable.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


/**
 * com.paran.animation.demo.app.animation.PathButton.java - Creation date: 2011. 12. 22. <br>
 * 하위 메뉴를 위한 Button 클래스
 * 
 * @author KTH 단말어플리케이션개발팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 */
public class PathButton extends Button {
//	private float x_offset = 0;
//	private float y_offset = 0;
	private int x_offset = 0;
	private int y_offset = 0;
	private ModeBtnClickedListener mOnModeBtnClickedListener;
	
	public ModeBtnClickedListener getOnModeBtnClickedListener() {
		return mOnModeBtnClickedListener;
	}

	public void setOnModeBtnClickedListener(
			ModeBtnClickedListener mOnModeBtnClickedListener) {
		this.mOnModeBtnClickedListener = mOnModeBtnClickedListener;
	}

	public PathButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PathButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PathButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public void getHitRect(Rect outRect) {
//		Rect curr = new Rect();
//	    super.getHitRect(curr);
//	    
//	    outRect.bottom = (int) (curr.bottom + y_offset);
//	    outRect.top = (int) (curr.top + y_offset);
//	    outRect.left = (int) (curr.left + x_offset);
//	    outRect.right = (int) (curr.right + x_offset);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
////		Rect curr = new Rect();
////		if (event.getAction() == MotionEvent.ACTION_UP){
////			super.getHitRect(curr);
////		}
//		
//		return true;
//	}
	
	public void setOffset(int endX, int endY) {
		x_offset = endX;
		y_offset = endY;
	}
	
	public int getXOffset() {
		return x_offset;
	}
	
	public int getYOffset() {
		return y_offset;
	}
	
	public interface ModeBtnClickedListener{
		void onClick(View v);
	}
}
