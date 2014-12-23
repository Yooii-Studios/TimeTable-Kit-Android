package com.sulga.yooiitable.showalltables;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import android.graphics.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.theme.parts.*;

public class TimetablePreviewViewCreator {
	private Activity activity;
	public TimetablePreviewViewCreator(Activity activity){
		this.activity = activity;
	}
	public RelativeLayout getTTPreView(Timetable timetable){
		RelativeLayout item = getItem();
		//wrapper = addRowsToWrapper(wrapper, timetable);
		addLessonViews(item, timetable);
		return item;
	}

	private RelativeLayout getItem(){
		LayoutInflater inflater = activity.getLayoutInflater();
		RelativeLayout root = (RelativeLayout)
				inflater.inflate(R.layout.view_showalltables_item, null, false);
		
//		FrameLayout preView = 
//				(FrameLayout) root.findViewById(R.id.view_showalltables_item_content);
		
//		Display mDisplay = 
//				((WindowManager)context
//						.getSystemService(Context.WINDOW_SERVICE))
//						.getDefaultDisplay();
//		int displayWidth= mDisplay.getWidth();
//		int displayHeight= mDisplay.getHeight();

		//화면 전체 크기의 1/4을 차지하는 프리뷰.
//		LinearLayout.LayoutParams preViewParams = 
//				new LinearLayout.LayoutParams(
//						(int)(displayWidth * 0.5f), 
//						(int)(displayHeight * 0.5f)
//						);
		//preViewParams.setMargins(1, 1, 1, 1);

//		preView.setLayoutParams(preViewParams);

//		preView.setBackgroundResource(R.drawable.yt_timetable_body_cell_background_shape);

		return root;
	}

	private void addLessonViews(
			final RelativeLayout item, final Timetable timetable){
		final FrameLayout preView = 
				(FrameLayout) item.findViewById(R.id.view_showalltables_item_content);
		TextView pageText = 
				(TextView) item.findViewById(R.id.view_showalltables_item_pagetext);
		
		int reverseStd = TimetableDataManager.getTimetables().size() + 1;
		pageText.setText( 
				(reverseStd - ( TimetableDataManager.getInstance().getTimetableIndex(timetable) + 1 ) )
				+ "/" + TimetableDataManager.getTimetables().size()
				);
		new YTShapeRoundRectThemePart(
				10.0f, 
				255, Color.TRANSPARENT,
				Color.parseColor("#219aca"), 1).setViewTheme(activity, pageText);

		preView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// Ensure you call it only once :
				preView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

				// Here you can get the size :)
				int periodNum = timetable.getPeriodNum();
				int dayNum = timetable.getDayNum();				

				int lessonViewWidthUnit = preView.getWidth() / dayNum;
				int lessonViewHeightUnit = preView.getHeight() / periodNum;

				MyLog.d("PreView", "preView Width : " + preView.getWidth() + 
						", preView Height : " + preView.getHeight() + "\n" +
						"periodNum : " + periodNum + ", dayNum : " + dayNum +
						", wUnit : " + lessonViewWidthUnit + 
						", hUnit : " + lessonViewHeightUnit);

				for(int i = 0; i < timetable.getLessonList().size() ; i++){
					Lesson lesson = timetable.getLessonList().get(i);

					float lessonStartPeriod = lesson.getLessonStartPeriodByFloat();
					float lessonEndPediod = lesson.getLessonEndPeriodByFloat();
					int lessonDay = timetable.getDayIndexFromGregorianCalendarDay(lesson.getDay());

					int leftMargin = lessonViewWidthUnit * lessonDay;
					int topMargin = (int) 
							(lessonViewHeightUnit * lessonStartPeriod);
					int lessonViewHeight = (int) 
							(lessonViewHeightUnit * lesson.getPeriodLengthByFloat());

					MyLog.d("PreView", 
							"leftMargin : " + leftMargin + ", topMargin : " + topMargin);
					LinearLayout lessonView = new LinearLayout(activity);
					FrameLayout.LayoutParams lessonViewParams = 
							new FrameLayout.LayoutParams(lessonViewWidthUnit, lessonViewHeight);
					lessonViewParams.leftMargin = leftMargin;
					lessonViewParams.topMargin = topMargin;
					lessonViewParams.gravity = Gravity.LEFT | Gravity.TOP;

					lessonView.setLayoutParams(lessonViewParams);
					lessonView.setBackgroundColor(lesson.getColor());

					preView.addView(lessonView);
				}
			}
		});	
	}

}
