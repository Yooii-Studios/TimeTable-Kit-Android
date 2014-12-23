package com.sulga.yooiitable.appwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.parts.YTShapeRoundRectThemePart;

import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

public class AppWidgetTableViewCreator {
	public static final float ROUNDRECT_RADIUS = 8.0f;

	private static final String TIMELINE_CELL_TAG = "TimeLineCell";
	private static final String DAYROW_TAG = "DayRow";
	private static final String TIMECELL = "TimeCell";
	private static final String BODYROW_TAG = "BodyRow";

	
	private static View widgetView = null;
	public static View createAppWidgetView(
			Context context, YTShapeRoundRectThemePart widgetThemePart, 
			Timetable timetable, int columnNum, int rowNum, boolean startFromFirstDayOfWeek){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		FrameLayout widgetTableView =
				(FrameLayout) inflater.inflate(R.layout.appwidget_layout, null);
		LinearLayout tableContainer = 
				(LinearLayout) widgetTableView.findViewById(R.id.appwidget_container);
		widgetThemePart.setViewTheme(context, widgetTableView);
		//widgetTableView.removeAllViews();
		//widgetTableView.setBackgroundResource(R.drawable.yt_appwidget_2x4_root_background_shape);

		LinearLayout dayrow = createAppWidgetDayrowView(context, 
				tableContainer,
				widgetThemePart, 
				timetable, 
				columnNum,
				startFromFirstDayOfWeek);
		tableContainer.addView(dayrow);

		for(int i = 0; i < rowNum ; i++){
			//			MyLog.d("YTAppWidgetProvider", "getTimetableStartPeriodByCurrentTime : " + getTimetableStartPeriodByCurrentTime(timetable, rowNum));
			LinearLayout bodyrow = createAppWidgetBodyrowView(
					context, widgetThemePart, 
					tableContainer, timetable, columnNum, 
					i + getTimetableStartPeriodByCurrentTime(timetable, rowNum),
					startFromFirstDayOfWeek);
			bodyrow.setTag(BODYROW_TAG + 
					Integer.toString(i + getTimetableStartPeriodByCurrentTime(timetable, rowNum)));
			tableContainer.addView(bodyrow);
		}
		//MyLog.d("createAppWidgetView", "dayrow Height : " + dayrow.getHeight());
		widgetView = widgetTableView;

		return widgetTableView;
	}

	private static LinearLayout createAppWidgetDayrowView(
			Context context, LinearLayout rowParent, YTShapeRoundRectThemePart widgetThemePart, 
			Timetable timetable, int dayNum, boolean startFromFirstDayOfWeek){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout dayrow = 
				(LinearLayout) inflater.inflate(
						R.layout.appwidget_view_dayrow, 
						rowParent, 
						false);

		LinearLayout dayrowOffset = 
				(LinearLayout) inflater.inflate(
						R.layout.appwidget_view_dayrow_offsetcell, 
						rowParent, 
						false);
		dayrow.addView(dayrowOffset);

		int textColor = Color.BLACK;
		if(widgetThemePart.getColor() == Color.BLACK){
			textColor = Color.WHITE;
		}
		//calendar initialized to start day of timetable
		Calendar c = initializeCalendar(timetable, startFromFirstDayOfWeek);

		for(int i = 0; i < dayNum ; i++){
			TextView daycell = 
					(TextView)inflater.inflate(
							R.layout.appwidget_view_dayrow_daycell, 
							rowParent, 
							false);
			daycell.setTextColor(textColor);
			daycell.setText(
					Timetable.getDayStringFromGregorianCalendar(
							context, c.get(Calendar.DAY_OF_WEEK)));
			dayrow.addView(daycell);			
			c.add(Calendar.DAY_OF_WEEK, 1);
		}
		dayrow.setTag(DAYROW_TAG);
		return dayrow;
		/*RemoteViews dayrow = 
				new RemoteViews(context.getPackageName(), R.layout.appwidget_view_dayrow);
		RemoteViews dayrowOffset = 
				new RemoteViews(context.getPackageName(), R.layout.appwidget_view_dayrow_offsetcell);
		dayrow.addView(R.id.appwidget_2x4_dayrow_wrapper, dayrowOffset);

		for(int i = 0; i < dayNum ; i++){
			RemoteViews daycell = 
					new RemoteViews(context.getPackageName(), R.layout.appwidget_view_dayrow_daycell);
			dayrow.addView(R.id.appwidget_2x4_dayrow_wrapper, daycell);
		}

		return dayrow;*/
	}

	/**
	 * timetable body의 한 행(bodyrow)를 만을어 timeline에는 currentPeriod(starts from 0)를 세팅하고, 
	 * dayNum만큼의 timecell을 추가하여 RemoteView인 bodyrow를 반환.
	 * @param context
	 * @param dayNum
	 * @param currentPeriod
	 * @return
	 */
	public static LinearLayout createAppWidgetBodyrowView(
			Context context, YTShapeRoundRectThemePart widgetThemePart, 
			LinearLayout rowParent, 
			Timetable timetable, int dayNum, int currentPeriod, boolean startFromFirstDayOfWeek){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout bodyrow = 
				(LinearLayout) inflater.inflate(
						R.layout.appwidget_view_bodyrow, 
						rowParent, 
						false);
		LinearLayout timelineCell = 
				(LinearLayout) inflater.inflate(
						R.layout.appwidget_view_timelinecell, 
						rowParent, 
						false);
		timelineCell.setTag(TIMELINE_CELL_TAG);
		TextView timelineText = 
				(TextView) timelineCell.findViewById(R.id.appwidget_view_timelinecell_text);
		switch(timetable.getColumnType()){
		case BY_ALPHABET:
			char ch = (char) (65 + ( currentPeriod) );
			timelineText.setText(ch+"");
			break;
		case BY_PERIOD:
			timelineText.setText(Integer.toString(currentPeriod + 1));
			break;
		case BY_TIME:
			int startHour = timetable.getStartHourOfPeriod(currentPeriod);
			int startMin = timetable.getStartMinOfPeriod(currentPeriod);
			String hs = startHour < 10 ? "0" + Integer.toString(startHour) : 
				Integer.toString(startHour);
			String ms = startMin < 10 ? "0" + Integer.toString(startMin) : 
				Integer.toString(startMin);
			timelineText.setText(hs + " : " + ms);
			timelineText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
			break;
		default:
			timelineText.setText(Integer.toString(currentPeriod + 1));
			break;		
		}
		int textColor = Color.BLACK;
		if(widgetThemePart.getColor() == Color.BLACK){
			textColor = Color.WHITE;
		}
		timelineText.setTextColor(textColor);

		widgetThemePart.setViewTheme(context, timelineCell);
		bodyrow.addView(timelineCell);
		Calendar c = initializeCalendar(timetable, startFromFirstDayOfWeek);

		for(int i = 0; i < dayNum ; i++){
			LinearLayout timecell = 
					(LinearLayout) inflater.inflate(
							R.layout.appwidget_view_timecell, 
							rowParent, 
							false);

			//			timecell.setTag(new CellTag(
			//					c.get(GregorianCalendar.DAY_OF_WEEK), currentPeriod));
			timecell.setTag(TIMECELL + c.get(Calendar.DAY_OF_WEEK));
			widgetThemePart.setViewTheme(context, timecell);
			bodyrow.addView(timecell);
			c.add(Calendar.DAY_OF_WEEK, 1);
		}

		/*RemoteViews bodyrow = 
				new RemoteViews(context.getPackageName(), R.layout.appwidget_view_bodyrow);
		//타임라인 최 좌측 추가
		RemoteViews timelineCell = 
				new RemoteViews(context.getPackageName(), R.layout.appwidget_view_timelinecell);
		bodyrow.addView(R.id.appwidget_bodyrow_wrapper, timelineCell);
		//이틀간의 시간표 시간을 집어넣는다.
		for(int j = 0; j < dayNum ; j++){
			RemoteViews timecell = 
					new RemoteViews(context.getPackageName(), R.layout.appwidget_view_timecell);
			//				timecell.setlayou
			bodyrow.addView(R.id.appwidget_bodyrow_wrapper, timecell);
		}*/
		return bodyrow;
	}

	public static RelativeLayout createAppWidgetLessonView(
			Context context, int widgetRowNum, Timetable timetable, Lesson lesson){
		RelativeLayout lessonView= (RelativeLayout) 
				View.inflate(context, R.layout.view_timetable_lessonview, null);
		//grid.setclip
		TextView textSubject = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_subject)
				);
		textSubject.setText(lesson.getLessonName());
		TextView textLocation = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_location)
				);
		textLocation.setText(lesson.getLessonWhere());
		//자리가 없으니 교수님은 생략
		TextView textProfessor = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_professor)
				);
		textProfessor.setVisibility(View.GONE);
		float lessonViewTextSize;
		switch(timetable.getDayNum()){
		case 5 :
			lessonViewTextSize = 10;
			break;
		case 6 : 
			lessonViewTextSize = 9;
			break;
		case 7 :
			lessonViewTextSize = 8;
			break;
		default :
			lessonViewTextSize = 8;
			break;
		}
		MyLog.d("AppWidgetTableViewCreator", "lessonViewTextSize : " + lessonViewTextSize);
		textSubject.setTextSize(TypedValue.COMPLEX_UNIT_SP, lessonViewTextSize);
		textLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, lessonViewTextSize);

		TextView textScheduleNum = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_schedulenum)
				);

		if(timetable == TimetableDataManager.getMainTimetable()){
			//			HashMap<String, ArrayList<Schedule>> scheduleMap = TimetableDataManager.getInstance().getSchedules();

			Collection<ArrayList<Schedule>> scheduleCollection = TimetableDataManager.getSchedules().values();

			int scheduleNum = 0;
			for(ArrayList<Schedule> scheduleList : scheduleCollection){
				for(int i = 0; i < scheduleList.size() ; i++){
					Schedule s = scheduleList.get(i);
					if(s.getParentLesson() == lesson){
						scheduleNum++;
					}
				}
			}
			if(scheduleNum == 0){
				textScheduleNum.setVisibility(View.GONE);
			}else{
				textScheduleNum.setText(scheduleNum+"");
			}
		}else{
			textScheduleNum.setVisibility(View.GONE);
		}

		//Log.e("dialog", "editSubject : " + editSubject.getText());
		if(lesson.getLessonName().equals("")){						
			textSubject.setVisibility(View.GONE);
		}else {
			textSubject.setVisibility(View.VISIBLE);
		}

		if(lesson.getLessonWhere().equals("")){
			textLocation.setVisibility(View.GONE);
		}else{
			textLocation.setVisibility(View.VISIBLE);
		}

		int timetableStartPeriod = getTimetableStartPeriodByCurrentTime(timetable, widgetRowNum);
		float cellMargin = context.getResources().getDimension(R.dimen.appwidget_daycell_margin);
		float lessonLength = lesson.getLessonEndPeriodByFloat() - lesson.getLessonStartPeriodByFloat();
		if(lesson.getLessonStartPeriodByFloat() < timetableStartPeriod){
			lessonLength -= (timetableStartPeriod - lesson.getLessonStartPeriodByFloat());
		}
		if(lesson.getLessonEndPeriodByFloat() > timetableStartPeriod + widgetRowNum){
			lessonLength -= (
					lesson.getLessonEndPeriodByFloat() 
					- (timetableStartPeriod + widgetRowNum)
					);
		}
		MyLog.d("createAppWidgetLessonView", "lesson start : " + lesson.getLessonStartPeriodByFloat()
				+ "lesson end : " + lesson.getLessonEndPeriodByFloat()
				+ "lessonLength : " + lessonLength);
		int[] wh = new int[2];
		//1. width
		wh[0] = timecellWidth;
		//2.height
		wh[1] = Math.round(
				(lessonLength * (float)bodyrowHeight) 
				- cellMargin);
		//				+ ( lessonLength - 1 ) * 2f * cellMargin + cellMargin);

		//markSelectedRangeLayout.setBackgroundColor(Color.CYAN);
		FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT, 
						android.view.ViewGroup.LayoutParams.FILL_PARENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		//params.width = (int) cellWidth * (endD - startD + 1);
		//params.height = (int) (cellHeight * (endP - startP));
		params.width = wh[0];
		params.height = wh[1];


		String bodyrowTag = lesson.getLessonStartPeriodByFloat() < timetableStartPeriod ?
				BODYROW_TAG + Integer.toString(timetableStartPeriod) :
					BODYROW_TAG + Integer.toString((int)lesson.getLessonStartPeriodByFloat());
				String cellTag = TIMECELL+Integer.toString(lesson.getDay());
				Log.d("AddLessonViews", 
						bodyrowTag + ", " + cellTag);
				ViewGroup row = (ViewGroup) widgetView.findViewWithTag(bodyrowTag);
				View cell = row.findViewWithTag(cellTag);
				Log.d("AddLessonViews", 
						"row : " + row + ", cell : " + cell);

				int intStartPeriod = (int) lesson.getLessonStartPeriodByFloat();
				float topOffset = (lesson.getLessonStartPeriodByFloat() - (float)intStartPeriod)
						* (float)bodyrowHeight;
				int[] tl = new int[2];
				tl[0] = Math.round((float)row.getTop() + cellMargin + topOffset);
				tl[1] = cell.getLeft();

				params.topMargin = tl[0];
				params.leftMargin = tl[1];
				//params.topMargin = (int) ( (startP) * cellHeight );

				lessonView.setLayoutParams(params);

				//		float rrradius = context.getResources().getDimension(R.dimen.view_lessonview_roundrect_radius);
				float rrradius = ROUNDRECT_RADIUS;
				//		int timetableStartPeriod = 
				//				getTimetableStartPeriodByCurrentTime(timetable, widgetRowNum);
				boolean LT = true, RT = true, RB = true, LB = true;
				if(lesson.getLessonStartPeriodByFloat() < timetableStartPeriod){
					LT = false;
					RT = false;
				}
				if(lesson.getLessonEndPeriodByFloat() > timetableStartPeriod + widgetRowNum){
					RB = false;
					LB = false;
				}
				new YTShapeRoundRectThemePart(rrradius, 255, lesson.getColor(), 0, 0)
				.setViewTheme(context, lessonView, rrradius, LT, RT, RB, LB);

				//		Drawable lessonViewBackgroundDrawable = 
				//				context.getResources().getDrawable(R.drawable.yt_timetable_body_lessonview_background_shape);
				//		//		lessonViewBackgroundDrawable.setAlpha(timetable.getTheme().getLessonViewAlpha());
				//		lessonView.setBackgroundDrawable(lessonViewBackgroundDrawable);
				//		GradientDrawable gd = (GradientDrawable) lessonView.getBackground();
				//		gd.setColor(lesson.getColor());
				//		lessonView.setBackgroundDrawable(gd);

				//lessonView.setBackgroundColor(lesson.getColor());
				//		lessonView.setOnClickListener(lessonOnClick);
				//		lessonView.setOnLongClickListener(lessonViewOnLongClickListener);
				//lessonView.setOnTouchListener(lessonViewOnTouchListener);

				//		lessonView.setTag(lesson);
				return lessonView;
	}

	public static Bitmap getBitmapOfView(int w, int h, View v){
		// Create a new bitmap and a new canvas using that bitmap
		final Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);

		v.setDrawingCacheEnabled(true);

		// Supply measurements
		v.measure(MeasureSpec.makeMeasureSpec(canvas.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(canvas.getHeight(), MeasureSpec.EXACTLY));

		// Apply the measures so the layout would resize before drawing.
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

		// and now the bmp object will actually contain the requested layout
		canvas.drawBitmap(v.getDrawingCache(), 0, 0, new Paint());
		v.setDrawingCacheEnabled(false);
		return bmp;
	}
	private static int timelineWidth;
	private static int dayrowHeight;
	private static int timecellHeight;
	private static int timecellWidth;
	private static int bodyrowHeight;
	public static void prepareToAddLessonViews(
			int widgetWidth, int widgetHeight,
			Timetable timetable, int widgetRowNum, View v, boolean startFromFirstDayOfWeek){
		View widgetView = layoutView(widgetWidth, widgetHeight, v);

		Calendar c = initializeCalendar(timetable, startFromFirstDayOfWeek);	

		View timelineCell = widgetView.findViewWithTag(TIMELINE_CELL_TAG);
		View dayrow = widgetView.findViewWithTag(DAYROW_TAG);
		ViewGroup bodyrow = (ViewGroup) widgetView.findViewWithTag(
				BODYROW_TAG 
				+ Integer.toString(
						getTimetableStartPeriodByCurrentTime(timetable, widgetRowNum)));
		View timeCell = bodyrow.findViewWithTag(
				TIMECELL 
				+ Integer.toString(
						c.get(Calendar.DAY_OF_WEEK)));

		timelineWidth = timelineCell.getWidth();
		dayrowHeight = dayrow.getHeight();
		timecellHeight = timeCell.getHeight();
		timecellWidth = timeCell.getWidth();
		bodyrowHeight = bodyrow.getHeight();

		Log.d("prepareToAddLessonViews", 
				"timelineWidth : " + timelineWidth + 
				", dayrowHeight : " + dayrowHeight + 
				", timecellWidth : " + timecellWidth + 
				", timecellHeight: " + timecellHeight +
				", bodyrowHeight : " + bodyrowHeight);
	}

	/*private static int[] getLessonViewWHFromLesson(
			Context context, int startDay, int endDay, int startPeriod, int endPeriod){
		int[] wh = new int[2];
		//
		//		View lt_cell = grid.findViewWithTag(new CellTag(startDay, startPeriod));
		//		View rb_cell = grid.findViewWithTag(new CellTag(endDay, endPeriod - 1));		//1을 빼주는 이유는 celltag가 start period를 기준으로 설정되어서.
		//		View lt_cell_parent_row = (View) lt_cell.getParent();
		//		View rb_cell_parent_row = (View) rb_cell.getParent();
		//		float cellMargin = res.getDimension(R.dimen.fragment_timetable_cell_margin);
		//
		//		int markWidth = rb_cell.getRight()- lt_cell.getLeft();
		//		int markHeight = (int) (( rb_cell_parent_row.getBottom() - cellMargin ) 
		//		- ( lt_cell_parent_row.getTop() + cellMargin ));  




		View lt_cell = widgetView.findViewWithTag(new CellTag(startDay, startPeriod));
		View rb_cell = widgetView.findViewWithTag(new CellTag(endDay, endPeriod - 1));
		if(lt_cell == null || rb_cell == null){
			return null;
		}
		View lt_cell_parent_row = (View) lt_cell.getParent();
		View rb_cell_parent_row = (View) rb_cell.getParent();
		float cellMargin = 
				context.getResources().getDimension(R.dimen.fragment_timetable_cell_margin);

		int rb_cell_right = rb_cell.getRight();
		int lt_cell_left = lt_cell.getLeft();
		int rb_cell_parent_bottom = rb_cell_parent_row.getBottom();
		int lt_cell_parent_top = lt_cell_parent_row.getTop();

		wh[0] = rb_cell_right- lt_cell_left;
		wh[1] = 
				(int) (( rb_cell_parent_bottom - cellMargin ) 
						- ( lt_cell_parent_top + cellMargin ));  
		MyLog.d("getLessonViewWHFromLesson", "rb_cell_right : " + rb_cell_right 
				+ "lt_cell_left : " + lt_cell_left
				+ "rb_cell_parent_bottom : " + rb_cell_parent_bottom 
				+ "lt_cell_parent_top : " + lt_cell_parent_top
				+ "wh[0] : " + wh[0]
						+ "wh[1] : " + wh[1]);
		return wh;
	}
	 */	
	/*private static int[] getLessonViewTLMarginFromLesson(Context context, int startDayIndex, int startPeriod){
		int[] tl = new int[2];
		View lt_cell = widgetView.findViewWithTag(new CellTag(startDayIndex, startPeriod));
		if(lt_cell == null){
			return null;
		}
		View lt_cell_parent_row = (View) lt_cell.getParent();
		float cellMargin = context.getResources().getDimension(R.dimen.fragment_timetable_cell_margin);		
		float timelineOffset =  context.getResources().getDimension(R.dimen.fragment_timetable_timeline_width) + 
				context.getResources().getDimension(R.dimen.fragment_timetable_body_padding);				

		int cellLeft = lt_cell.getLeft();

		tl[0] = Math.round(lt_cell_parent_row.getTop());
		tl[1] = Math.round(timelineOffset + cellLeft);
		return tl;
	}
	 */
	private static Calendar initializeCalendar(Timetable timetable, boolean startFromFirstDayOfWeek){
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK);

		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		//first day는 sunday로 치고 있는듯.
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		if(cal.getFirstDayOfWeek() == Calendar.SUNDAY){
			if(timetable.getStartDay() == Calendar.MONDAY){
				if(today == Calendar.SUNDAY){
					cal.add(Calendar.WEEK_OF_YEAR, -1);
				}
				cal.add(Calendar.DAY_OF_WEEK, 1);
			}else if(timetable.getStartDay() == Calendar.SUNDAY){

			}
		}else if(cal.getFirstDayOfWeek() == Calendar.MONDAY){
			if(timetable.getStartDay() == Calendar.SUNDAY){
				cal.add(Calendar.DAY_OF_WEEK, -1);
			}
		}
		if(timetable.doesTimetableIncludesGregorianDay(today) == false){
			//타임테이블에 오늘의 요일이 없다-> 다음주 시간표를 보여주자.
			//cal.add(field, value)
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}

		Calendar todayC = Calendar.getInstance();
		if(startFromFirstDayOfWeek == false){
			if(timetable.doesTimetableIncludesGregorianDay(todayC.get(GregorianCalendar.DAY_OF_WEEK))
					== true)
				cal.set(Calendar.DAY_OF_WEEK, todayC.get(Calendar.DAY_OF_WEEK));		
		}

		return cal;
	}

	/**
	 * get timetable parameter's start period to show on Android AppWidget.
	 * Counts from 0.
	 * @param timetable
	 * @param periodNum
	 * @return
	 */
	private static int getTimetableStartPeriodByCurrentTime(Timetable timetable, int periodNum){
		//1.시간표 교시 갯수가 periodNum보다 적으면 start는 무조건 0
		if(timetable.getPeriodNum() < periodNum){
			return 0;
		}

		//위젯은 상하 10교시만 보여주도록 되어있으므로, 
		//		Calendar c = initializeCalendar(timetable);

		Calendar c = Calendar.getInstance();
		if(timetable.doesTimetableIncludesGregorianDay(c.get(Calendar.DAY_OF_WEEK))
				== false){
			//만약 오늘이 일요일이고 시간표는 월-금이라면, 월요일 0교시부터 보여준다.
			return 0;
		}

		float currentPeriod = timetable.getPeriodByFloatFromTime(
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE));

		int timetableStartPeriod = 
				(int) (currentPeriod - (periodNum + 1) / 2 + 1);
		int timetableEndPeriod = timetableStartPeriod + periodNum;

		MyLog.d("prepareToAddLessonViews", "Before adjust : " + 
				"currentPeriod : " + currentPeriod
				+ ", timetableStartPeriod : " + timetableStartPeriod
				+ ", timetableEndPeriod : " + timetableEndPeriod);

		if(timetableStartPeriod <= 0){
			MyLog.d("prepareToAddLessonViews", "After adjust, return 0");
			return 0;
		}

		if(currentPeriod >= timetable.getPeriodNum()){
			//현재 교시가 시간표의 period보다 더 크다면 수업이 모두 끝났다는 뜻.
			MyLog.d("prepareToAddLessonViews", 
					"Today Lesson Ended!! After adjust, return 0");
			return 0;
		}

		if(timetableEndPeriod > timetable.getPeriodNum()){
			MyLog.d("prepareToAddLessonViews", "After adjust, return " + (timetableEndPeriod - periodNum));
			return timetable.getPeriodNum() - periodNum;
		}

		MyLog.d("prepareToAddLessonViews", "After adjust(no adjust needed), return " + timetableStartPeriod);
		return timetableStartPeriod;
	}

	public static View layoutView(int w, int h, View v){
		//		v.setDrawingCacheEnabled(true);

		// Supply measurements
		v.measure(
				MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
		// Apply the measures so the layout would resize before drawing.
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		return v;
	}

	public static void addLessonViews(
			Context context, View widgetView, int widgetRowNum, int widgetColumnNum, Timetable timetable, boolean startFromFirstDayOfWeek){
		ArrayList<Lesson> lessonList = timetable.getLessonList();

		//오늘~표시될 day num까지 돌며 날짜가 같은 수업을 fetch
		ArrayList<Lesson> fetchedLessonList = fetchIncludedLessons(
				widgetColumnNum, widgetRowNum, 
				timetable, lessonList, 
				startFromFirstDayOfWeek);
		if(fetchedLessonList.size() == 0){
			return;
		}
		FrameLayout lessonViewContainer = 
				(FrameLayout) 
				widgetView
				.findViewById(R.id.appwidget_lessonview_container);
		for(Lesson l : fetchedLessonList){
			View lessonView = createAppWidgetLessonView(context, widgetRowNum, timetable, l);
			lessonViewContainer.addView(lessonView);
		}
	}

	private static ArrayList<Lesson> fetchIncludedLessons(
			int widgetColumnNum, int widgetRowNum, 
			Timetable timetable, ArrayList<Lesson> lessonList, 
			boolean startFromFirstDayOfWeek){
		Calendar c = initializeCalendar(timetable, startFromFirstDayOfWeek);	

		ArrayList<Lesson> ll = new ArrayList<Lesson>();

		int timetableStartPeriod = 
				getTimetableStartPeriodByCurrentTime(timetable, widgetRowNum);

		for(int i = 0; i < widgetColumnNum ; i++){
			for(Lesson l : lessonList){
				if(l.getDay() == c.get(Calendar.DAY_OF_WEEK)){
					//만약 현재 교시 범위(currentStartPeriod~endperiod)
					if(l.getLessonName().equals("회로이론2")){
						MyLog.d("fetchIncludedLessons", "l.getLessonStart : " + l.getLessonStartPeriodByFloat() 
								+ ", l.getLessonEnd : " + l.getLessonEndPeriodByFloat()
								+ ", timetableStartPeriod : " + timetableStartPeriod);
					}
					if(l.getLessonStartPeriodByFloat() >= timetableStartPeriod + widgetRowNum){
						//lesson starts after current appwidget timetable's avaliable period size.
						continue;
					}
					if(l.getLessonEndPeriodByFloat() <= timetableStartPeriod){
						//this lesson already passed, and finished.
						continue;
					}

					//날짜가 겹치면 fetch
					Lesson cloned = l.clone();
					cloned.setPeriodInfo(l.getLessonInfo().clone());
					if(l.getLessonStartPeriodByFloat() < timetableStartPeriod){
						//						cloned.setStartHour(
						//								timetable.getStartHourOfPeriod(timetableStartPeriod));
						//						cloned.setStartMin(
						//								timetable.getStartMinOfPeriod(timetableStartPeriod));
					}
					ll.add(cloned);
				}
			}
			c.add(Calendar.DAY_OF_WEEK, 1);
		}		
		return ll;

	}


}
