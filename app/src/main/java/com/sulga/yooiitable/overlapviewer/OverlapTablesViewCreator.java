package com.sulga.yooiitable.overlapviewer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.Timetable.ColumnTypes;
import com.sulga.yooiitable.mylog.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class OverlapTablesViewCreator {

	private static final float FIXED_PERIOD_UNIT = 60.0f;
	private static class CellTag{
		private int day;
		private int period;	//start period 기준.
		public CellTag(int day, int period){
			this.day = day;
			this.period = period;
		}
		@Override
		public boolean equals(Object tag){
			if(tag == null)
			{
				return false;
			}
			if(this == tag)
			{
				return true;
			}
			if(this.day == ((CellTag)tag).day &&
					this.period == ((CellTag)tag).period)
			{
				return true;
			}else
			{
				return false;
			}		
		}
	}


	//	private static View widgetView = null;
	private static Timetable tempOverlapTable;
	private static FrameLayout root;
	private static LinearLayout grid;
	public static FrameLayout createAppWidgetView(
			Context context, 
			ArrayList<Timetable> timetables
			){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout widgetTableView = 
				(FrameLayout) inflater.inflate(R.layout.view_overlap_container, null);
		root = widgetTableView;

		grid = (LinearLayout) widgetTableView.findViewById(R.id.view_overlap_container);

		int earlistStartTimeByMin = getEarliestStartTimeByMin(timetables);
		int latestEndTimeByMin = getLatestEndTimeByMin(timetables);
		int rowNum = (int) 
				Math.ceil((latestEndTimeByMin - earlistStartTimeByMin) 
						/ FIXED_PERIOD_UNIT);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		int columnNum = getMaxColumnNum(timetables);
		c.add(Calendar.DAY_OF_WEEK, columnNum);
		tempOverlapTable = new Timetable(Calendar.MONDAY, 
				c.get(Calendar.DAY_OF_WEEK), 
				ColumnTypes.BY_TIME, 
				earlistStartTimeByMin / 60, rowNum, 
				(int)FIXED_PERIOD_UNIT, 
				Timetable.LESSON_ALARM_NONE);

		MyLog.d("OverlapTableViewCreator", 
				"earlist start time : " + earlistStartTimeByMin +
				", latestEndTime : " + latestEndTimeByMin);

		createAppWidgetDayrowView(context, grid, columnNum);
		//		grid.addView(dayrow);

		for(int i = 0; i < rowNum ; i++){
			//			MyLog.d("YTAppWidgetProvider", "getTimetableStartPeriodByCurrentTime : " + getTimetableStartPeriodByCurrentTime(timetable, rowNum));
			LinearLayout bodyrow = createAppWidgetBodyrowView(
					context, grid, columnNum, 
					i);
			grid.addView(bodyrow);
		}

		//MyLog.d("createAppWidgetView", "dayrow Height : " + dayrow.getHeight());
		//		widgetView = widgetTableView;
		return widgetTableView;
	}

	private static int getEarliestStartTimeByMin(ArrayList<Timetable> timetables){
		int startHour = -1;
		int startMin = -1;
		for(int i = 0; i < timetables.size() ; i++){
			Timetable t = timetables.get(i);
			int _startHour = t.getStartHourOfPeriod(0);
			int _startMin = t.getStartMinOfPeriod(0);

			int _startTimeByMin = _startHour * 60 + _startMin;
			int startTimeByMin = startHour * 60 + startMin;
			if(startTimeByMin < 0){
				startHour = _startHour;
				startMin = _startMin;
				continue;
			}
			if(startTimeByMin > _startTimeByMin){
				startHour = _startHour;
				startMin = _startMin;
			}
		}
		return startHour * 60 + startMin;
	}

	private static int getLatestEndTimeByMin(ArrayList<Timetable> timetables){
		int endTimeByMin = 0;
		for(int i = 0; i < timetables.size() ; i++){
			Timetable t = timetables.get(i);
			int startTimeByMin = t.getStartHourOfPeriod(0) * 60 
					+ t.getStartMinOfPeriod(0);
			int _endTimeByMin = startTimeByMin + t.getPeriodUnit() * t.getPeriodNum();

			if(_endTimeByMin > endTimeByMin){
				endTimeByMin = _endTimeByMin;
			}
		}
		return endTimeByMin;
	}

	private static int getMaxColumnNum(ArrayList<Timetable> timetables){
		int dayNum = 0;
		for(int i = 0; i < timetables.size() ; i++){
			if(timetables.get(i).doesTimetableIncludesGregorianDay(GregorianCalendar.SUNDAY)){
				return 7;
			}
			Timetable t = timetables.get(i);
			int _dayNum = t.getDayNum();
			if(dayNum < _dayNum){
				dayNum = _dayNum;
			}
		}
		return dayNum;
	}

	private static LinearLayout createAppWidgetDayrowView(
			Context context, 
			LinearLayout rowParent, 
			int dayNum){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//		LinearLayout dayrow = 
		//				(LinearLayout) inflater.inflate(
		//						R.layout.view_overlap_dayrow, 
		//						rowParent, 
		//						false);
		LinearLayout dayrow = (LinearLayout) 
				root.findViewById(R.id.view_overlap_dayrow_wrapper);

		LinearLayout dayrowOffset = 
				(LinearLayout) inflater.inflate(
						R.layout.view_overlap_dayrow_offsetcell, 
						rowParent, 
						false);
		dayrow.addView(dayrowOffset);

		//calendar initialized to start day of timetable
		//		Calendar c = initializeCalendar(timetable);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		//		Calendar todayC = GregorianCalendar.getInstance();
		//		if(startFromFirstDayOfWeek == false){
		//			c.set(GregorianCalendar.DAY_OF_WEEK, todayC.get(GregorianCalendar.DAY_OF_WEEK));	
		//		}
		//		
		for(int i = 0; i < dayNum ; i++){
			TextView daycell = 
					(TextView)inflater.inflate(
							R.layout.view_overlap_dayrow_daycell, 
							rowParent, 
							false);
			daycell.setText(
					Timetable.getDayStringFromGregorianCalendar(
							context, c.get(Calendar.DAY_OF_WEEK)));
			dayrow.addView(daycell);			
			c.add(Calendar.DAY_OF_WEEK, 1);
		}
		return dayrow;

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
			Context context, LinearLayout rowParent, 
			int dayNum, int currentPeriod){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout bodyrow = 
				(LinearLayout) inflater.inflate(
						R.layout.view_overlap_bodyrow, 
						rowParent, 
						false);
		LinearLayout timelineCell = 
				(LinearLayout) inflater.inflate(
						R.layout.view_overlap_timelinecell, 
						rowParent, 
						false);
		TextView timelineText = 
				(TextView) timelineCell.findViewById(R.id.view_overlap_timelinecell_text);

		//		timelineText.setText(Integer.toString(currentPeriod + 1));


		int timeInMinute = tempOverlapTable.getStartTimeByMin()
				+ currentPeriod * tempOverlapTable.getPeriodUnit();
		int startHour = ( timeInMinute / 60 ) % 24 ;
		int startMinute = timeInMinute % 60;			

		String sH = startHour < 10 ? "0"+startHour : Integer.toString(startHour);
		String sM = startMinute < 10 ? "0" + startMinute : Integer.toString(startMinute);
		String s = sH + " : " + sM;
		timelineText.setText(s);

		bodyrow.addView(timelineCell);
		//		Calendar c = initializeCalendar(timetable);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		//		Calendar todayC = GregorianCalendar.getInstance();
		//		if(startFromFirstDayOfWeek == false){
		//			c.set(GregorianCalendar.DAY_OF_WEEK, todayC.get(GregorianCalendar.DAY_OF_WEEK));	
		//		}

		for(int i = 0; i < dayNum ; i++){
			LinearLayout timecell = 
					(LinearLayout) inflater.inflate(
							R.layout.view_overlap_timecell, 
							rowParent, 
							false);
			//			timecell.setTag(new CellTag(
			//					c.get(GregorianCalendar.DAY_OF_WEEK), currentPeriod));
			timecell.setTag(new CellTag(i, currentPeriod));
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
			Context context, int widgetRowNum, Lesson lesson, int viewAlpha){
		RelativeLayout lessonView= (RelativeLayout) 
				View.inflate(context, R.layout.view_overlap_lessonview, null);
		//grid.setclip
		TextView textSubject = (
				(TextView)lessonView.findViewById(R.id.view_overlap_lessoninfo_subject)
				);
		textSubject.setText(lesson.getLessonName());
		TextView textLocation = (
				(TextView)lessonView.findViewById(R.id.view_overlap_lessoninfo_location)
				);
		textLocation.setText(lesson.getLessonWhere());
		//자리가 없으니 교수님은 생략
		TextView textProfessor = (
				(TextView)lessonView.findViewById(R.id.view_overlap_lessoninfo_professor)
				);
		textProfessor.setVisibility(View.GONE);

		float lessonViewTextSize;
		switch(tempOverlapTable.getDayNum()){
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
		textSubject.setTextSize(TypedValue.COMPLEX_UNIT_SP, lessonViewTextSize);
		textLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, lessonViewTextSize);
		textProfessor.setTextSize(TypedValue.COMPLEX_UNIT_SP, lessonViewTextSize);

		//		textProfessor.setText(lesson.getProfessor());



		//		if(timetable == TimetableDataManager.getMainTimetable()){
		//			//			HashMap<String, ArrayList<Schedule>> scheduleMap = TimetableDataManager.getInstance().getSchedules();
		//
		//			Collection<ArrayList<Schedule>> scheduleCollection = TimetableDataManager.getSchedules().values();
		//
		//			int scheduleNum = 0;
		//			for(ArrayList<Schedule> scheduleList : scheduleCollection){
		//				for(int i = 0; i < scheduleList.size() ; i++){
		//					Schedule s = scheduleList.get(i);
		//					if(s.getParentLesson() == lesson){
		//						scheduleNum++;
		//					}
		//				}
		//			}
		//
		//			if(scheduleNum == 0){
		//				textScheduleNum.setVisibility(View.GONE);
		//			}else{
		//				textScheduleNum.setText(scheduleNum+"");
		//			}
		//		}else{
		//			textScheduleNum.setVisibility(View.GONE);
		//		}

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

		//		if(lesson.getProfessor().equals("")){
		//			textProfessor.setVisibility(View.GONE);
		//		}else{
		//			textProfessor.setVisibility(View.VISIBLE);
		//		}

		//Resources res = getResources();
		int dayIdx = tempOverlapTable.getDayIndexFromGregorianCalendarDay(lesson.getDay());
		int[] wh = getLessonViewWHFromLesson(
				context, dayIdx, dayIdx,
				lesson.getLessonStartPeriodByFloat(), 
				lesson.getLessonEndPeriodByFloat());

		//		float cellMargin = context.getResources().getDimension(R.dimen.appwidget_daycell_margin);
		//		int lessonLength = Math.round(lesson.getLessonEndPeriodByFloat() - lesson.getLessonStartPeriodByFloat());

		//		int[] wh = new int[2];
		//		//1. width
		//		wh[0] = timecellWidth;
		//		//2.height
		//		wh[1] = (int) ((lesson.getPeriodLengthByFloat() * timecellHeight) 
		//				+ (float)( lessonLength - 1 ) * 2f * cellMargin + cellMargin);

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

		/*FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.width = (int) cellWidth;
		params.height = (int) (cellHeight * (lesson.getPeriodLengthByFloat()));
		Log.e("params.height", params.height + 
				", end : " + lesson.getLessonEndPeriodByFloat() + 
				", start : " + lesson.getLessonStartPeriodByFloat()
				);*/


		/*//만약 타임라인부분이 터치됬으면 return -1
		float timelineOffset =  res.getDimension(R.dimen.fragment_timetable_timeline_width)
				+ res.getDimension(R.dimen.fragment_timetable_body_padding)
				+ res.getDimension(R.dimen.fragment_timetable_cell_margin);
		int left = (int) (timelineOffset + dayIdx * cellWidth);

		MyLog.d("LessonView Left", left+"");
		params.leftMargin = left;
		params.topMargin = (int) ( ( lesson.getLessonStartPeriodByFloat()) * cellHeight );*/

		int[] tl = getLessonViewTLMarginFromLesson(context, lesson);


		//		int[] tl = new int[2];
		//		tl[0] = row.getTop();
		//		tl[1] = cell.getLeft();

		params.topMargin = tl[0];
		params.leftMargin = tl[1];
		//params.topMargin = (int) ( (startP) * cellHeight );

		lessonView.setLayoutParams(params);

		Drawable lessonViewBackgroundDrawable = 
				context.getResources().getDrawable(R.drawable.yt_timetable_body_lessonview_background_shape);
		//		lessonViewBackgroundDrawable.setAlpha(timetable.getTheme().getLessonViewAlpha());
		lessonView.setBackgroundDrawable(lessonViewBackgroundDrawable);
		GradientDrawable gd = (GradientDrawable) lessonView.getBackground();
		gd.setColor(lesson.getColor());
		gd.setAlpha(viewAlpha);
		lessonView.setBackgroundDrawable(gd);

		//lessonView.setBackgroundColor(lesson.getColor());
		//		lessonView.setOnClickListener(lessonOnClick);
		//		lessonView.setOnLongClickListener(lessonViewOnLongClickListener);
		//lessonView.setOnTouchListener(lessonViewOnTouchListener);

		//		lessonView.setTag(lesson);
		return lessonView;
	}


	private static int[] getLessonViewWHFromLesson(
			Context context, int startDay, int endDay, float startPeriod, float endPeriod){

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

		View l_cell = grid.findViewWithTag(new CellTag(startDay, 0));
		View l_cell_parent = (View) l_cell.getParent();
		//		View rb_cell = grid.findViewWithTag(new CellTag(endDay, endPeriod - 1));
		//		if(lt_cell == null || rb_cell == null){
		//			return null;
		//		}
		//		View lt_cell_parent_row = (View) lt_cell.getParent();
		//		View rb_cell_parent_row = (View) rb_cell.getParent();

		float cellMargin = 
				context.getResources().getDimension(R.dimen.activity_overlap_cell_margin);
		float rowHeight = l_cell_parent.getHeight();

		//		int rb_cell_right = rb_cell.getRight();
		//		int lt_cell_left = lt_cell.getLeft();
		//		int rb_cell_parent_bottom = rb_cell_parent_row.getBottom();
		//		int lt_cell_parent_top = lt_cell_parent_row.getTop();

		wh[0] = l_cell.getWidth() * (endDay - startDay + 1);
		//		wh[1] = 
		//				(int) (rb_cell_parent_bottom -  lt_cell_parent_top  - cellMargin * 2); 
		wh[1] = (int) (rowHeight * (endPeriod - startPeriod) - cellMargin * 2);

		MyLog.d("getLessonViewWHFromLesson", "wh[0] : " + wh[0] + ", wh[1] : " + wh[1]);

		return wh;
	}

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
	//	private static Calendar initializeCalendar(Timetable timetable){
	//		Calendar cal = GregorianCalendar.getInstance();
	//		int today = cal.get(GregorianCalendar.DAY_OF_WEEK);
	//
	//		cal.set(GregorianCalendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
	//		cal.clear(GregorianCalendar.MINUTE);
	//		cal.clear(GregorianCalendar.SECOND);
	//		cal.clear(GregorianCalendar.MILLISECOND);
	//
	//		if(timetable.doesTimetableIncludesGregorianDay(today) == false){
	//			//타임테이블에 오늘의 요일이 없다-> 다음주 시간표를 보여주자.
	//			//cal.add(field, value)
	//			cal.add(GregorianCalendar.DAY_OF_WEEK, 1);
	//		}
	//
	//		// get start of this week in milliseconds
	//		//first day는 sunday로 치고 있는듯.
	//		cal.set(GregorianCalendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
	//		if(cal.getFirstDayOfWeek() == GregorianCalendar.SUNDAY){
	//			if(timetable.getStartDay() == GregorianCalendar.MONDAY){
	//				cal.add(GregorianCalendar.DAY_OF_WEEK, 1);
	//			}else if(timetable.getStartDay() == GregorianCalendar.SUNDAY){
	//
	//			}
	//		}else if(cal.getFirstDayOfWeek() == GregorianCalendar.MONDAY){
	//			if(timetable.getStartDay() == GregorianCalendar.SUNDAY){
	//				cal.add(GregorianCalendar.DAY_OF_WEEK, -1);
	//			}
	//		}
	//		return cal;
	//	}

	public static void addLessonViews(
			Context context, View widgetView, ArrayList<Timetable> timetables){

		ArrayList<Lesson> fetchAllLessons = new ArrayList<Lesson>();
		for(int i = 0; i < timetables.size() ; i++){
			ArrayList<Lesson> ll = timetables.get(i).getLessonList();
			ArrayList<Lesson> clonedList = new ArrayList<Lesson>();
			for(int j = 0; j < ll.size() ; j++){
				clonedList.add(ll.get(j).clone());
			}
			fetchAllLessons.addAll(clonedList);
		}
		for(int i = 0; i < fetchAllLessons.size() ; i++){
			fetchAllLessons.get(i).setParentTimetable(tempOverlapTable);
		}

		MyLog.d("OverlapTablesViewCreator", "all lesson size : " + fetchAllLessons.size());

		final FrameLayout lessonViewContainer = 
				(FrameLayout) 
				widgetView
				.findViewById(R.id.view_overlap_lessonview_container);
		lessonViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				MyLog.d("lessonViewContainer", "height : " + 
				lessonViewContainer.getHeight());
			}
		});
		int viewAlpha = 255 / timetables.size();
		for(int i = 0; i < fetchAllLessons.size() ; i++){
			Lesson l = fetchAllLessons.get(i);
			MyLog.d("OverlapTablesViewCreator", "lesson day : " + l.getDay());

			View lessonView = createAppWidgetLessonView(context,
					tempOverlapTable.getPeriodNum(), 
					l, (int) ((int)255 * 0.5f));
			
			lessonViewContainer.addView(lessonView);
//			MyLog.d("lessonViewContainer", "height : " + lessonViewContainer.getMeasuredHeight());

			lessonView.setVisibility(View.INVISIBLE);
			registerLessonViewAnimation(context, i * 100, lessonView);
		}
	}

	private static void registerLessonViewAnimation(final Context context,
			int delayTime, final View v){      
		v.postDelayed(new Runnable(){
			@Override
			public void run() {
				v.setVisibility(View.VISIBLE);
				v.bringToFront();
				v.startAnimation(moveViewToScreenCenter(context, v));
			}
		}, delayTime);
	}

	private static AnimationSet moveViewToScreenCenter(Context context, View view)
	{
		//	    RelativeLayout root = (RelativeLayout) findViewById( R.id.rootLayout );
		AnimationSet animSet = new AnimationSet(true);

		//1.translate animation
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics( dm );

		int statusBarOffset = dm.heightPixels - grid.getMeasuredHeight();

		int originalPos[] = new int[2];
		view.getLocationOnScreen( originalPos );

		int xDest = dm.widthPixels/2;
		xDest -= (view.getMeasuredWidth()/2);
		int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;

		TranslateAnimation transAnim = new TranslateAnimation( 
				xDest - originalPos[0], 0, yDest - originalPos[1], 0);
		transAnim.setDuration(600);
		transAnim.setFillAfter( true );

		AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
		alphaAnim.setDuration(300);
		alphaAnim.setFillAfter(true);

		animSet.addAnimation(transAnim);
		animSet.addAnimation(alphaAnim);

		return animSet;
	}

	/**
	 * lesson을 받아서 grid에 새롭게 생길 lesson view의 top margin과 left margin int배열 tl[2]에 넣어 반환.
	 * timeline offset등의 기타 마진값들도 미리 계산되어 넘겨준다.
	 * tl[0] : top margin, tl[1] : left margin
	 * @param lesson
	 * @return lt[2]
	 */
	private static int[] getLessonViewTLMarginFromLesson(Context context, Lesson lesson){
		//		Timetable timetable = getTimetableDataFromManager();
		//int[] tl = new int[2];
		if(tempOverlapTable == null){
			return null;
		}
		float startPeriod = lesson.getLessonStartPeriodByFloat();
		int startDayIdx = tempOverlapTable
				.getDayIndexFromGregorianCalendarDay(lesson.getLessonInfo().getDay());

		return getGridOverlapViewTLMargin(context, startDayIdx, startPeriod);
	}

	private static int[] getGridOverlapViewTLMargin(Context context, 
			int startDayIndex, 
			float startPeriod){
		int[] tl = new int[2];
		View l_cell = grid.findViewWithTag(new CellTag(startDayIndex, 0));
		View l_cell_parent = (View) l_cell.getParent();
		if(l_cell == null){
			return null;
		}

		//		View lt_cell_parent_row = (View) l_cell.getParent();
		float cellMargin = context.getResources()
				.getDimension(R.dimen.activity_overlap_cell_margin);
		float rowHeight = l_cell_parent.getHeight();
		//		float timelineOffset =  
		//				context
		//				.getResources()
		//				.getDimension(R.dimen.fragment_timetable_timeline_width);
		//				res.getDimension(R.dimen.fragment_timetable_body_padding);				

		int cellLeft = l_cell.getLeft();

		//		tl[0] = Math.round(lt_cell_parent_row.getTop());
		tl[0] = (int) (rowHeight * startPeriod + cellMargin);
		//		tl[0] = getTimetableBodyViewTopMargin(startPeriod);
		tl[1] = Math.round(cellLeft);
		return tl;
	}
	//
	//	private int getTimetableBodyViewTopMargin(float startPeriod){
	//		View cell = grid.findViewWithTag(new CellTag(0, startPeriod));
	//		int[] loc = new int[2];
	//		cell.getLocationOnScreen(loc);
	//		MyLog.d("getTimetableBodyViewTopMargin", loc[1]+"");
	//		return loc[1] - getGridViewTop();
	//	}


	private int getGridViewTop(){
		int loc[] = new int[2];
		grid.getLocationOnScreen(loc);  
		return loc[1];
	}

}
