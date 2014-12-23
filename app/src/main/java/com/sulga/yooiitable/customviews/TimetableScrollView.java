package com.sulga.yooiitable.customviews;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.timetable.fragments.*;

public class TimetableScrollView extends ScrollView {
	private TimetableFragment parent;

	public TimetableScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TimetableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TimetableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setParentFragment(TimetableFragment parent){
		this.parent = parent;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev){
		//아직 메인액티비티가 터치이벤트 처리를 다 못했으면
		/*MyLog.d("TouchSystemCheck", "touched : timetableScrollView/onInterceptTouchEvent");
		if(parent.isDraggingLessonMode == true){
			return onTouchEvent(ev);     //니가 다 처리해라고 하고 종료.
		}
*/		//메인액티비티가 터치미벤트 처리를 다 했거나 그냥 스크롤만 하고 싶다면
		return super.onInterceptTouchEvent(ev);
		
	}

}
