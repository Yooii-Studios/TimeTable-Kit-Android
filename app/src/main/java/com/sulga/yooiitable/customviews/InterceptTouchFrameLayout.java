package com.sulga.yooiitable.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;


public class InterceptTouchFrameLayout extends FrameLayout {

	TimetableFragment parent;
	public InterceptTouchFrameLayout(Context context) {
		super(context);
	}

	public InterceptTouchFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterceptTouchFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setParentFragment(TimetableFragment parent){
		this.parent = parent;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev){
		//아직 메인액티비티가 터치이벤트 처리를 다 못했으면
		MyLog.d("TouchSystemCheck", "touched : gridOverlapLayout/onInterceptTouchEvent");
		if(parent.isEditLessonLengthMode || parent.isDraggingLessonMode || parent.isSelectPeriodMode) {
			MyLog.d("TouchSystemCheck", "touched : gridOverlapLayout/onInterceptTouchEvent, onTouchEvent called, ev : " + ev.getAction());
			return onTouchEvent(ev);     //니가 다 처리해라고 하고 종료.
		}
		//메인액티비티가 터치미벤트 처리를 다 했거나 그냥 스크롤만 하고 싶다면
		return super.onInterceptTouchEvent(ev); 
	}

}
