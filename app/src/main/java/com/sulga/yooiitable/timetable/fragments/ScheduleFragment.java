package com.sulga.yooiitable.timetable.fragments;

import java.text.*;
import java.util.*;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.ViewPager;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.view.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.*;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.google.calendar.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.theme.parts.*;
import com.sulga.yooiitable.timetable.*;
import com.sulga.yooiitable.timetable.fragments.dialogbuilders.*;
import com.sulga.yooiitable.utils.*;

public class ScheduleFragment extends Fragment {
	public static final int MAX_SCHEDULE_COUNT_FREE_VERSION = 12;
	public static ScheduleFragment newInstance() {
		ScheduleFragment scheduleFragment = new ScheduleFragment();

		return scheduleFragment;
	}

	private View scheduleView;

	private LinearLayout scheduleListWrapper;
	private ScrollView scheduleListScroll;

	private Button addSchedule;
	private Button syncSchedule;

	private Timetable mainTimetable;

	//	private ActionBar actionBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		scheduleView = inflater.inflate(R.layout.fragment_schedule, container, false);
		FixTileModeBug.fixBackgroundRepeat(scheduleView);
		//		actionBar = this.getSupportActionBar();
		//		setupActionBar();

		//scheduleListView = (ListView) scheduleView.findViewById(R.id.fragment_schedule_listview);
		scheduleListWrapper = (LinearLayout)
				scheduleView.findViewById(R.id.fragment_schedule_listwrapper);
		scheduleListScroll = (ScrollView)
				scheduleView.findViewById(R.id.fragment_schedule_listscroll);

		/*if(getArguments() != null)
			schedules = getArguments().getParcelableArrayList("Schedules");
		if(schedules == null)
			schedules = new ArrayList<Schedule>();*/
		//		scheduleMap = TimetableDataManager.getInstance().getSchedules();
		mainTimetable = TimetableDataManager.getMainTimetable();
//				final int sc = scheduleCounts;
//		MyLog.d("ScheduleFragment", "Schedule count : " + sc);

		//scheduleAdapter = new ScheduleAdapter(getActivity(), R.layout.listitem_schedules_a_day, mainTimetable.getLessonList(), schedules);
		//scheduleListView.setAdapter(scheduleAdapter);

		//scheduleListScroll = (ScrollView) scheduleView.findViewById(R.id.fragment_schedule_listscroll);
		//scheduleListWrapper = (LinearLayout) scheduleView.findViewById(R.id.fragment_schedule_listwrapper);
		//noItemHere = (TextView) scheduleView.findViewById(R.id.fragment_schedule_text_list_no_item);
		addSchedule = (Button) scheduleView.findViewById(R.id.fragment_schedule_additem);
		addSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Collection<ArrayList<Schedule>> collection = 
						TimetableDataManager.getSchedules().values();
				int scheduleCounts = 0;
				for(ArrayList<Schedule> itemList : collection){
					for(int i = 0; i < itemList.size() ; i++){
						scheduleCounts++;
					}
				}
				if(TimetableDataManager.getCurrentFullVersionState(getSupportActivity()) == false 
						&& scheduleCounts >= MAX_SCHEDULE_COUNT_FREE_VERSION){
					String message = getSupportActivity().getResources().getString(R.string.unlock_full_version);
					ToastMaker.popupToastAtCenter(getSupportActivity(), message);
					return;
				}
				ScheduleEditDialogBuilder builder = new ScheduleEditDialogBuilder();
				builder.createDialog(getSupportActivity(), null, ScheduleFragment.this).show();

				MyLog.d("ScheduleFragment", mainTimetable.getLessonList().toString());
			}
		});

		syncSchedule = (Button) scheduleView.findViewById(R.id.fragment_schedule_sync);
		syncSchedule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GCAccountManager syncManager = GCAccountManager.getInstance();
				syncManager.showAccountSelectDialog(ScheduleFragment.this.getSupportActivity());
			}
		});
		scheduleListWrapper.removeAllViews();
		createScheduleListView(TimetableDataManager.getSchedules());

		return scheduleView;
	}

	@Override
	public void onPause(){
		super.onPause();
		MyLog.d("ScheduleFragment", "onPause");
	}

	@Override
	public void onStop(){
		super.onStop();
		MyLog.d("ScheduleFragment", "onStop");
	}


	/*public void addScheduleView(Schedule s){
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.YEAR, s.getScheduleYear());
		c.set(Calendar.MONTH, s.getScheduleMonth());
		c.set(Calendar.DAY_OF_MONTH, s.getScheduleDay());
		c.set(Calendar.HOUR_OF_DAY, s.getScheduleHour());
		c.set(Calendar.MINUTE, s.getScheduleMin());		

		Activity av = getActivity();
		RelativeLayout listItem = (RelativeLayout) View.inflate(av, R.layout.listitem_schedule, null);


		TextView attatchedLessonName = (TextView) listItem.findViewById(R.id.listitem_schedule_attatchedlesson);
		TextView scheduleName = (TextView)listItem.findViewById(R.id.listitem_schedule_schedulename);
		TextView scheduleDate = (TextView)listItem.findViewById(R.id.listitem_schedule_date);
		TextView scheduleTime = (TextView)listItem.findViewById(R.id.listitem_schedule_time);

		//1.부모 레슨 네임 설정
		if(s.getParentLessonIndex() != Schedule.PARENT_LESSON_NONE){
			ArrayList<Lesson> lessonList = mainTimetable.getLessonList();
			attatchedLessonName.setText(lessonList.get(s.getParentLessonIndex()).getLessonName());
		}else
			attatchedLessonName.setVisibility(View.INVISIBLE);
		if(s.getParentLesson() != null){
			attatchedLessonName.setText(s.getParentLesson().getLessonName());
		}else
			attatchedLessonName.setVisibility(View.INVISIBLE);
		//2.스케쥴이름 설정
		scheduleName.setText(s.getScheduleName());
		//3.스케쥴 날짜 설정
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMMMM dd(EEE), yyyy");		
		String date = sdfDate.format(c.getTime());
		//date = s.getScheduleMonth() + "월 " + s.getScheduleDay() + "일, " + s.getScheduleYear();
		scheduleDate.setText(date);
		//4.스케쥴 시간 설정
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH : mm");
		String time = sdfTime.format(c.getTime());
		//time = s.getScheduleHour() + " : " + s.getScheduleMin();
		scheduleTime.setText(time);

		scheduleListWrapper.addView(listItem);

		scheduleListScroll.post(new Runnable() {
		    @Override
		    public void run() {
		        scheduleListScroll.fullScroll(ScrollView.FOCUS_DOWN);
		    }
		});

		MyLog.d("AddSchedule", "item count : " + ( scheduleListWrapper.getChildCount() - 1 ) );
	}*/

	public void refresh(){
		/*scheduleListWrapper.removeViews(1, scheduleListWrapper.getChildCount() - 1);
		if(schedules.size() == 0)
			noItemHere.setVisibility(View.VISIBLE);
		else
			noItemHere.setVisibility(View.INVISIBLE);


		for(int i = 0; i < schedules.size() ; i++){
			addScheduleView(schedules.get(i));
		}*/
		//scheduleAdapter.notifyDataSetChanged();
		//scheduleListView.invalidate();

		scheduleListWrapper.removeAllViews();
		createScheduleListView(TimetableDataManager.getSchedules());
		scheduleListWrapper.invalidate();
		MyLog.d("ScheduleFragment", "Refreshed!");
	}

	//	public void refresh(boolean enableView){
	//		scheduleListWrapper.removeAllViews();
	//		createScheduleListView(TimetableDataManager.getSchedules());
	//
	//		enableDisableViewGroup(scheduleListScroll, true);
	//	}

	private void createScheduleListView(HashMap<String, ArrayList<Schedule>> scheduleMap){

		Calendar cal = Calendar.getInstance();	
		String todayKey = TimetableDataManager.makeKeyFromDate(
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH));
		//스케쥴맵 소팅 필요!!!!!
		//트리맵은 자동으로 스트링을 소팅해주는듯.
		//20130101 - 20130102...순으로 알아서 스트링 정렬 기본만으로도 충분할거같아 따로 소팅구현 필요없을듯
		Map<String, ArrayList<Schedule>> sortedScheduleMap = 
				new TreeMap<String, ArrayList<Schedule>>(scheduleMap);
		for(String s : sortedScheduleMap.keySet()){
			MyLog.d("ScheduleFragment_sort", s);
		}

		//1 - 해시맵에 오늘의 스케쥴이 존재하지 않으면 아래의 for문을 돌지 않아버린다. 
		//따라서 해시맵에 오늘의 스케쥴 존재 여부를 확인한 후에, 존재하지 않으면 수업만이라도 더해주도록 처리한다.
		//2013.12.08-deleted.
		//		if(sortedScheduleMap.containsKey(todayKey) == false){
		//			ArrayList<TimeInfo> tl = new ArrayList<TimeInfo>();
		//
		//			for(int i = 0 ; i < lessonList.size() ; i++){
		//				if(lessonList.get(i).getDay() == cal.get(GregorianCalendar.DAY_OF_WEEK)){
		//					tl.add(lessonList.get(i));
		//				}
		//			}
		//
		//			if(tl.size() != 0)
		//				scheduleListWrapper.addView(createTimeInfoView(tl, todayKey));
		//		}

		//2 - 그리고 스케쥴 관련하여 뷰를 더한다.
		for(Map.Entry<String, ArrayList<Schedule>> entry : sortedScheduleMap.entrySet()){
			String key = entry.getKey();
			ArrayList<Schedule> sl = entry.getValue();

			//			ArrayList<TimeInfo> tl = new ArrayList<TimeInfo>();
			//			tl.addAll(sl);
			//			if(key.equals(todayKey)){
			//				//'오늘'의 스케쥴 정보의 경우 수업 + 스케쥴 표시이므로...	
			//				//1.수업을 더한다.
			//				for(int i = 0 ; i < lessonList.size() ; i++){
			//					if(lessonList.get(i).getDay() == cal.get(GregorianCalendar.DAY_OF_WEEK)){
			//						tl.add(lessonList.get(i));
			//					}
			//				}
			//				//2.스케쥴을 더한다.
			//				tl.addAll(sl);
			//
			//			}else{
			//				tl.addAll(sl);
			//			}

			if(sl.size() == 0)
				continue;

			if(key.equals(todayKey)){
				//add todayline view
				addTodayLineView();
			}

			//3 - 스케쥴 소팅.
			//2013.12.08 - no lessons added now.
			Collections.sort(sl, new Comparator<TimeInfo>(){
				@Override
				public int compare(TimeInfo lhs, TimeInfo rhs) {
					// TODO Auto-generated method stub
					int a = lhs.getStartHour() * 60 + lhs.getStartMin();
					int b = rhs.getStartHour() * 60 + rhs.getStartMin();
					return a < b ? -1 : a > b ? 1 : 0; 
				}
			});

			for(int i = 0; i < sl.size() ; i++){
				addScheduleView(sl.get(i), key);
			}
			//			//4 - 이제 뷰를 만든다.
			//			scheduleListWrapper.addView(
			//					createTimeInfoView(sl, key)
			//					);
		}
	}

	private void addTodayLineView(){
		LayoutInflater inflater = getSupportActivity().getLayoutInflater();

		LinearLayout todayLine = (LinearLayout) inflater.inflate(
				R.layout.view_schedule_todayline,
				scheduleListWrapper,
				false
				);
		TextView todayLineText = 
				(TextView) todayLine.findViewById(R.id.view_schedule_todayline_todaytext);
		//		Resources res = getSupportActivity().getResources();
		//		float roundRectCorner = res.getDimension(R.dimen.view_schedule_corners);
		YTRoundRectThemePart todayThemePart = new YTShapeRoundRectThemePart(
				12.0f,
				255, Color.parseColor("#1f1f1f"),
				Color.parseColor("#d2d0c6"), 1 
				);
		todayThemePart.setViewTheme(getSupportActivity(), todayLineText);
		scheduleListWrapper.addView(todayLine);
	}


	//	private final static String SCHEDULE_ITEMS_WRAPPER = "SCHEDULE_ITEMS_WRAPPER";
	private void addScheduleView(Schedule s, String dateKey){
		LayoutInflater inflater = getSupportActivity().getLayoutInflater();
		//			if(t instanceof Lesson){
		//				//	android.support.v4.view.ViewPager lessonViewPager = 
		//				//		(android.support.v4.view.ViewPager) inflater.inflate(R.layout.item_lesson_viewpager, subViewWrapper, false);
		//				View lessonView = getLessonItemView(inflater, subViewWrapper, t);
		//				subViewWrapper.addView(lessonView);
		//
		//			}else 
		//			if(t instanceof Schedule){

		//LinearLayout wrapper = (LinearLayout) inflater.inflate(R.layout.item_schedule_viewpager, subViewWrapper, false);
		final ViewPager scheduleViewPager = 
				(ViewPager) inflater.inflate(
						R.layout.view_schedule_viewpager, 
						scheduleListWrapper, 
						false);

		//Schedule item inflate/add occurs line below
		scheduleViewPager.setAdapter(new ScheduleItemsPagerAdapter(this.getActivity(), s));
		scheduleViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING
						|| arg0 == android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING){
					scheduleListWrapper.requestDisallowInterceptTouchEvent(true);
				}else{
					scheduleListWrapper.requestDisallowInterceptTouchEvent(false);
				}
			}
		});
		scheduleViewPager.setCurrentItem(0);
		scheduleViewPager.setTag(s);
		//View scheduleView = getScheduleItemView(inflater, subViewWrapper, t);
		//r.getBackground().setColorFilter(Color.CYAN, Mode.SRC_ATOP);
		//		item.setTag(dateKey);
		scheduleListWrapper.addView(scheduleViewPager);
		return;
	}

	//	private class ScheduleTag{
	//		private Schedule s;
	//		private String key;
	//		public ScheduleTag(String key, Schedule s){
	//			this.s = s;
	//			this.key = key;
	//		}
	//
	//		public boolean equals(ScheduleTag st){
	//			if(key.equals(st.key) &&
	//					s.equals(st.s))
	//				return true;
	//
	//			return false;
	//		}
	//	}

	public void startAddAndEditScheduleAnimation(final Schedule s){
		scheduleListWrapper.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ViewPager v = (ViewPager) getScheduleView(s);		//got viewpager
				//		v.getv.getCurrentItem()
				View page = v.findViewWithTag(SCHEDULE_ITEM_TAG);
				scrollToEditedScheduleView(v);
				startScheduleBackgroundFadeAnimation(page);
			}
		});
	}

	private void scrollToEditedScheduleView(View v){
		int loc[] = new int[2];
		v.getLocationOnScreen(loc);

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int displayHeight = display.getHeight();

		int scrollTo = scheduleListScroll.getTop() + loc[1] + v.getHeight() /2 
				- displayHeight / 2;

		MyLog.d("scrollToEditedScheduleView", "Scrolled To : " + scrollTo);
		scheduleListScroll.smoothScrollTo(0, scrollTo);
	}

	private void startScheduleBackgroundFadeAnimation(View v){
		//		View tmp = v.getChildAt(0);
		final View backgroundForAnim = v.findViewById(R.id.view_schedule_fadeoutview);
		Animation fadeout = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.schedule_fadeout_background);
		backgroundForAnim.setVisibility(View.VISIBLE);
		fadeout.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				backgroundForAnim.setVisibility(View.INVISIBLE);
			}
		});

		backgroundForAnim.startAnimation(fadeout);
	}

	private View getScheduleView(Schedule s){
		//		String key = TimetableDataManager.makeKeyFromSchedule(s);
		//
		//		LinearLayout schedule_a_day = (LinearLayout) scheduleListWrapper.findViewWithTag(key);
		//		LinearLayout wrapper = (LinearLayout) schedule_a_day.findViewWithTag(SCHEDULE_ITEMS_WRAPPER);
		View v = scheduleListWrapper.findViewWithTag(s);
		return v;
	}

	//	private View getLessonItemView(LayoutInflater inflater, ViewGroup subViewWrapper, TimeInfo t){
	//		RelativeLayout r = (RelativeLayout) inflater.inflate(R.layout.item_lesson, subViewWrapper, false);
	//		TextView lessonNameText = (TextView) r.findViewById(R.id.item_lesson_lessonname);
	//		TextView lessonWhereText = (TextView) r.findViewById(R.id.item_lesson_lessonwhere);
	//		TextView lessonWhenText = (TextView) r.findViewById(R.id.item_lesson_time);
	//
	//		Lesson l = (Lesson)t;
	//		lessonNameText.setText(l.getLessonName());
	//		lessonWhereText.setText(l.getLessonWhere());
	//
	//		int startHour = l.getLessonInfo().getStartHour();
	//		int startMin = l.getLessonInfo().getStartMin();
	//		String sH = startHour < 10 ? "0"+startHour : Integer.toString(startHour);
	//		String sM = startMin < 10 ? "0"+startMin : Integer.toString(startMin);
	//
	//		lessonWhenText.setText(sH + " : " + sM);
	//
	//		return r;
	//	}


	private View getScheduleItemView(LayoutInflater inflater, 
			ViewGroup wrapper, 
			final Schedule s){
		View root = (FrameLayout) inflater.inflate(
				R.layout.view_schedule, wrapper, false);

		TextView monthText = (TextView) root.findViewById(R.id.view_schedule_dateview_monthtext);
		TextView dateText = (TextView) root.findViewById(R.id.view_schedule_dateview_datetext);
		//		Typeface robotoThin = Typefaces.get(getSupportActivity(), "roboto_thin.ttf");
		//		dateText.setTypeface(robotoThin);
		TextView dayText = (TextView) root.findViewById(R.id.view_schedule_dateview_daytext);
		TextView ddayText = (TextView) root.findViewById(R.id.view_schedule_contentsview_dday_text);

		//		Resources res = getSupportActivity().getResources();
		float roundRectCorner = 8.0f;
		YTRoundRectThemePart monthThemePart = new YTShapeRoundRectThemePart(
				roundRectCorner,
				255, Color.parseColor("#d2d0c6"),
				0, 0
				);
		monthThemePart.setViewTheme(getSupportActivity(), monthText,
				roundRectCorner,
				true, true, false, false);

		YTRoundRectThemePart dayThemePart = new YTShapeRoundRectThemePart(
				roundRectCorner,
				255, Color.parseColor("#bababa"),
				0, 0
				);
		dayThemePart.setViewTheme(getSupportActivity(), 
				dayText, 
				roundRectCorner, 
				false, false, true, true);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 
				s.getScheduleYear());
		c.set(Calendar.MONTH, 
				s.getScheduleMonth());
		c.set(Calendar.DAY_OF_MONTH, 
				s.getScheduleDay());

		SimpleDateFormat month_date = new SimpleDateFormat("MMM");
		String month_name = month_date.format(c.getTime());
		monthText.setText(month_name);
		dateText.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));

		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEEEEE");
		String day_name = dayFormat.format(c.getTime());
		dayText.setText(day_name);

		long Dday = ScheduleFragment.calculateDDay(
				c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH), 
				c.get(Calendar.DAY_OF_MONTH)
				);

		ddayText.setText("D - " + Dday);
		if(Dday < 0){
			//			ddayText.setPaintFlags(ddayText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			ddayText.setTextColor(Color.parseColor("#ffffff"));
			ddayText.setText("D + " + Dday * -1);
		}else if(Dday == 0){
			ddayText.setTextColor(Color.parseColor("#ffffff"));
		}


		TextView scheduleName = (TextView) 
				root.findViewById(R.id.view_schedule_contentsview_title_text);
		TextView parentLesson = (TextView) 
				root.findViewById(R.id.view_schedule_contentsview_class_text);
		parentLesson.setText("");
		//		TextView scheduleTime = (TextView) root.findViewById(R.id.item_schedule_time);

		scheduleName.setText(s.getScheduleName());
		if(s.getParentLesson() != null){
			parentLesson.setText(s.getParentLesson().getLessonName());
		}

		//		int startHour = s.getStartHour();
		//		int startMin =s.getStartMin();
		//		String sH = startHour < 10 ? "0"+startHour : Integer.toString(startHour);
		//		String sM = startMin < 10 ? "0"+startMin : Integer.toString(startMin);
		//
		//		scheduleTime.setText(sH + " : " + sM);

		root.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				Intent intent = new Intent(ScheduleFragment.this.getActivity(), EditScheduleActivity.class);
				ScheduleEditDialogBuilder builder = new ScheduleEditDialogBuilder();
				builder
				.createDialog(getSupportActivity(), s, ScheduleFragment.this)
				.show();

				//				String scheduleKey = TimetableDataManager.makeKeyFromSchedule(s);
				//				intent.putExtra("ScheduleKey", scheduleKey);
				//				intent.putExtra("ScheduleIndex", TimetableDataManager.
				//						getInstance().getIndexOfSchedule(s));
				//intent.putExtra("MainTimetableIndex", mainTimetableIndex);

				//				ScheduleFragment.this.getActivity().startActivityForResult(intent, RequestCodes.CALL_ACTIVITY_EDIT_SCHEDULE_ACTIVITY);
			}
		});

		return root;
	}


	private final static String SCHEDULE_ITEM_TAG = "YT_SCHEDULE_PAGER_ITEM";
	private class ScheduleItemsPagerAdapter extends PagerAdapter{

		private Context context;
		private Schedule s;
		private View mCurrentView;
		public ScheduleItemsPagerAdapter(Context context, Schedule s){
			super();
			this.context = context;
			this.s = s;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object instantiateItem(final ViewGroup collection, int position) {
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = null;
			if(position == 0){
				v = getScheduleItemView(inflater, collection, s);
				v.setTag(SCHEDULE_ITEM_TAG);
				v.setOnLongClickListener(new OnScheduleLongClickListener(collection));
				((ViewPager)collection).addView(v);
				//return r;
			}else{
				v = (Button) inflater.inflate(
						R.layout.view_schedule_deletebutton, 
						collection, 
						false
						);
				v.setOnClickListener(new OnDeleteScheduleClickListener(collection));

				((ViewPager)collection).addView(v);
				//return b;
			}
			return v;
		}

		@Override
		public float getPageWidth(int position) {
			if(position == 1){
				float nbPages = 4; // You could display partial pages using a float value
				return (1 / nbPages);
			}else{
				return super.getPageWidth(position);
			}		    
		}
		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			//super.destroyItem(collection, position, view);
			//			holder.remove(view);
			collection.removeView((View) view);
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			MyLog.d("ScheduleFragment", "isViewFromObject : " + (view == object));
			return (view==object);
		}
		@Override
		public void finishUpdate(ViewGroup arg0) {}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(ViewGroup arg0) {}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			mCurrentView = (View)object;
		}

		public View getCurrentItem(){
			return mCurrentView;
		}
	}





	public static long calculateDDay(int targetYear, int targetMonth, int targetDate){
		Calendar cal = Calendar.getInstance();

		long today = cal.getTimeInMillis(); //현재 시간

		cal.set(targetYear, targetMonth, targetDate); //목표일을 cal에 set

		long target_day = cal.getTimeInMillis(); //목표일에 대한 시간

		long d_day = 0;

		if(target_day >= today){
			d_day =  (target_day - today) / (60*60*24*1000);
		}else{
			long tmp = today;
			today = target_day;
			target_day = tmp;

			d_day = ( today - target_day ) / (60*60*24*1000);

		}

		MyLog.d("DDay", d_day+"");
		return d_day;
	}


	public ScrollView getScheduleScroll(){
		return scheduleListScroll;
	}
	//
	//	private void startScheduleListLayoutAnimation(
	//			final ViewGroup schedule_a_day,
	//			final ViewGroup scheduleWrapper, final View viewToRemove, 
	//			final int moveYOffset, final int scheduleViewHeight, final int timeOffset){
	//
	//		//scheduleListWrapper.requestDisallowInterceptTouchEvent(true);
	//
	//		final int removedScheduleViewPosition = scheduleWrapper.indexOfChild(viewToRemove);
	//		final int schedule_a_day_position = scheduleListWrapper.indexOfChild(schedule_a_day);
	//
	//		//현재 schedule_a_day 내부의 child들만 위로 올려주는중임.
	//		int animatedCount = 0;
	//		for(int i = removedScheduleViewPosition + 1; i < scheduleWrapper.getChildCount() ; i++){
	//
	//			ViewAnimationManager.moveViewVertical(
	//					scheduleViewHeight, 
	//					scheduleWrapper.getChildAt(i), 
	//					timeOffset * animatedCount, 
	//					300, true, 
	//					true, true, 
	//					null);
	//
	//			animatedCount++;
	//		}
	//		//float moveRate = (float)moveYOffset / (float)scheduleViewHeight * -1;
	//
	//		//삭제된 스케쥴 이후의 날짜(schedule_a_day) 뷰들을 위로 올려준다.
	//		scheduleListWrapper.setClipChildren(false);
	//		for(int i = schedule_a_day_position + 1 ; i < scheduleListWrapper.getChildCount() ; i++){
	//			ViewGroup tmpScheduleADay = (ViewGroup) scheduleListWrapper.getChildAt(i);
	//			tmpScheduleADay.setClipChildren(false);
	//
	//			View title = tmpScheduleADay.findViewById(R.id.item_timeinfo_title_background);
	//			ViewAnimationManager.moveViewVertical(
	//					moveYOffset, 
	//					title, 
	//					animatedCount * timeOffset, 
	//					300, true, true, true, 
	//					null);
	//			animatedCount++;
	//
	//			ViewGroup wrapper = (ViewGroup) 
	//					tmpScheduleADay.findViewWithTag(SCHEDULE_ITEMS_WRAPPER);
	//			for(int j = 0; j < wrapper.getChildCount() ; j++){
	//				ViewAnimationManager.moveViewVertical(
	//						moveYOffset, 
	//						wrapper.getChildAt(j), 
	//						animatedCount * timeOffset, 
	//						300, true, true, true, 
	//						null);
	//
	//
	//				//현재 각 스케쥴 뷰들이 위로 올라가면서 parent view바깥으로 벗어나 뷰가 사라져버린다.
	//
	//				animatedCount++;
	//			}
	//
	//			//animatedCount++;
	//		}	
	//
	//		new Handler().postDelayed(new Runnable(){
	//
	//			@Override
	//			public void run() {
	//				// TODO Auto-generated method stub
	//				MyLog.d("ListAnimation", "Schedule View Refreshed! : " + System.currentTimeMillis());
	//				//애니메이션이 끝났으므로 뷰들의 터치를 다시 활성화.
	//				ScheduleFragment.this.refresh(true);
	//				//enableDisableViewGroup(scheduleListScroll, true);
	//				//				for(int i = schedule_a_day_position + 1 ; i < scheduleListWrapper.getChildCount() ; i++){
	//				//					LinearLayout v = (LinearLayout) scheduleListWrapper.getChildAt(i);
	//				////
	//				////					MyLog.d("ListAnimation",
	//				////							"Move SCHEDULE_A_DAY top : " + v.getTop()
	//				////							+ ", moveYOffset : " + moveYOffset);
	//				//					v.layout(
	//				//							v.getLeft(), 
	//				//							v.getTop() - moveYOffset,
	//				//							v.getRight(),
	//				//							v.getBottom() - moveYOffset
	//				//							);
	//				//					//v.set
	//				//
	//				//					//moveScheduleADayViewAfterAnimEnd(v, moveYOffset);
	//				//
	//				//					//					LinearLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
	//				//					//										
	//				//					//					v.setLayoutParams(lp);
	//				//					//					v.requestLayout();
	//				//					//					MyLog.d("ListAnimation",
	//				//					//							"Move SCHEDULE_A_DAY top : " + lp.topMargin
	//				//					//							+ ", moveYOffset : " + moveYOffset);
	//				//
	//				//				}
	//				//
	//			}
	//
	//		}, (animatedCount) * timeOffset + 300);
	//		//
	//	}

	//	public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
	//		int childCount = viewGroup.getChildCount();
	//
	//		for (int i = 0; i < childCount; i++) {
	//			View view = viewGroup.getChildAt(i);
	//			if(enabled == false)
	//				view.setOnTouchListener(blockOnTouchListener);
	//			else
	//				view.setOnTouchListener(null);
	//
	//			if(view instanceof ScheduleViewPager){
	//				if(enabled == false){
	//					((ScheduleViewPager) view).setPagingEnabled(enabled);
	//				}else{
	//					((ScheduleViewPager) view).setPagingEnabled(enabled);
	//				}
	//			}
	//
	//			if (view instanceof ViewGroup) {
	//				enableDisableViewGroup((ViewGroup) view, enabled);
	//			}
	//		}
	//	}
	//
	//	private static View.OnTouchListener blockOnTouchListener = new View.OnTouchListener() {
	//
	//		@Override
	//		public boolean onTouch(View v, MotionEvent event) {
	//			// TODO Auto-generated method stub
	//			Log.e("ScheduleFragment", "touch blocked!!");
	//			return true;
	//		}
	//	};

	private class OnScheduleLongClickListener implements View.OnLongClickListener{
		private ViewGroup collection;
		public OnScheduleLongClickListener(ViewGroup collection){
			this.collection = collection;
		}
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			final Schedule s = (Schedule) collection.getTag();
			DeleteScheduleAlertDialogBuilder.createDialog(getSupportActivity(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onDeleteSchedule(s);
				}
			}).show();
//			Toast.makeText(getSupportActivity(), s.toString(), Toast.LENGTH_SHORT).show();
			return true;
		}
	}
	
	private class OnDeleteScheduleClickListener implements View.OnClickListener{
		private ViewGroup collection;

		public OnDeleteScheduleClickListener(ViewGroup collection){
			this.collection = collection;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			//final ScheduleTag st = (ScheduleTag) collection.getTag();

			Schedule s = (Schedule) collection.getTag();
			onDeleteSchedule(s);

			//날짜 기준으로 분류한 스케쥴들을 감싼다. 제목+스케쥴뷰 래퍼
			//			final LinearLayout schedule_a_day = (LinearLayout) 
			//					scheduleListWrapper.findViewWithTag(TimetableDataManager.makeKeyFromSchedule(s));
			//			//제목 아래의 스케쥴뷰 래퍼
			//			final LinearLayout wrapper = (LinearLayout) 
			//					schedule_a_day.findViewWithTag(SCHEDULE_ITEMS_WRAPPER);
			//
			//			//뷰들의 터치를 막아버린다.
			//			enableDisableViewGroup(scheduleListScroll, false);
			//			scheduleListScroll.requestDisallowInterceptTouchEvent(true);

			//			processRemoveScheduleView(collection, s, schedule_a_day, wrapper);

			//아직 wrapper.removeview가 호출되지 않았으므로 래퍼의 차일드 카운트는 1일 것이다.
			//300ms로 약간 늦게 종료시킨다.
			//			if(wrapper.getChildCount() == 1){
			//				//scheduleListWrapper.removeView(schedule_a_day);
			//				processRemoveScheduleTitleView(collection, s, schedule_a_day, wrapper);
			//			}

			//startScheduleListLayoutAnimation(scheduleListWrapper);
		}

	}

	public boolean onDeleteSchedule(Schedule s){
		if(s == null){
			return false;
		}
		if(s.getGoogleCalendarEventKey().equals(Schedule.EVENT_KEY_NONE) == false)
			TimetableDataManager.getInstance().putEventIDToDeleteOnGoogleCalendar(s.getGoogleCalendarEventKey());
		String key = TimetableDataManager.makeKeyFromSchedule(s);

		TimetableDataManager.getSchedules().get(key).remove(s);
//		TimetableDataManager.writeDatasToExternalStorage();
		YTAlarmManager.cancelScheduleAlarm(getActivity(), s);

		((TimetableActivity)ScheduleFragment.this.getActivity())
		.refreshTimetablePage(TimetableDataManager.getTimetables().size());

		refresh();
		return true;
	}

	//	private void processRemoveScheduleView(
	//			final ViewGroup collection, final Schedule s,
	//			final ViewGroup schedule_a_day, final ViewGroup wrapper){
	//
	//		final boolean deleteTitle = wrapper.getChildCount() == 1 ? true : false;
	//		final String key = TimetableDataManager.makeKeyFromSchedule(s);
	//
	//		//Animation vanish =AnimationUtils.loadAnimation(getActivity(),R.anim.vanish);
	//		Animation go_left = AnimationUtils.loadAnimation(getActivity(), R.anim.schedule_go_left);
	//		collection.startAnimation(go_left);
	//		go_left.setFillEnabled(true);
	//		go_left.setFillAfter(true);
	//		go_left.setAnimationListener(new Animation.AnimationListener() {
	//			public void onAnimationStart(Animation animation) {}
	//			public void onAnimationRepeat(Animation animation) {}
	//			@Override
	//			public void onAnimationEnd(Animation animation) {
	//				// TODO Auto-generated method stub
	//				if(s.getGoogleCalendarEventKey().equals(Schedule.EVENT_KEY_NONE) == false)
	//					TimetableDataManager.getInstance().putEventIDToDeleteOnGoogleCalendar(s.getGoogleCalendarEventKey());
	//
	//				TimetableDataManager.getInstance().getSchedules().get(key).remove(s);
	//				TimetableDataManager.writeDatasToExternalStorage();
	//				YTAlarmManager.cancelScheduleAlarm(getActivity(), s);
	//				//collection.setVisibility(View.INVISIBLE);
	//				if(deleteTitle == false){
	//					//타이틀 삭제할 필요가 없을 경우 스케쥴 리스트 레이아웃의 전체 애니메이션을 실행한다.
	//					startScheduleListLayoutAnimation(
	//							schedule_a_day,
	//							wrapper, collection,
	//							collection.getHeight(), collection.getHeight(), 100);
	//				}
	//				//타이틀을 삭제해야 될 경우에는 스케쥴 리스트 레이아웃 처리를 타이틀 삭제 이후로 넘긴다.
	//			}
	//		});		
	//	}
	//
	//	private void processRemoveScheduleTitleView(
	//			final ViewGroup collection, final Schedule s,
	//			final ViewGroup schedule_a_day, final ViewGroup wrapper){
	//
	//		Animation title_go_left = 
	//				AnimationUtils.loadAnimation(getActivity(), R.anim.schedule_title_go_left);
	//		final View schedule_title = 
	//				schedule_a_day.findViewById(R.id.item_timeinfo_title);
	//		title_go_left.setFillEnabled(true);
	//		title_go_left.setFillAfter(true);
	//		schedule_title.startAnimation(title_go_left);
	//		//title_go_left.get
	//		//타이틀의 날짜표시/디데이 텍스트 두개가 왼쪽으로 사라진다.
	//		title_go_left.setAnimationListener(new Animation.AnimationListener() {
	//			public void onAnimationStart(Animation animation) {}
	//			public void onAnimationRepeat(Animation animation) {}
	//			@Override
	//			public void onAnimationEnd(Animation animation) {
	//				//텍스트가 사라지면 non visible
	//				//schedule_title.setVisibility(View.INVISIBLE);
	//				//그리고 남아있는 검은 줄 백그라운드가 사라진다.
	//				final View schedule_title_background = 
	//						schedule_a_day.findViewById(R.id.item_timeinfo_title_background);
	//				Animation title_disappear = 
	//						AnimationUtils.loadAnimation(getActivity(), R.anim.schedule_title_disappear);
	//				title_disappear.setFillEnabled(true);
	//				title_disappear.setFillAfter(true);
	//				schedule_title_background.startAnimation(title_disappear);
	//
	//
	//				title_disappear.setAnimationListener(new Animation.AnimationListener() {
	//					public void onAnimationStart(Animation animation) {}
	//					public void onAnimationRepeat(Animation animation) {}
	//					@Override
	//					public void onAnimationEnd(Animation animation) {
	//						//schedule_title_background.startAnimation(title_disappear);
	//						//schedule_title_background.setVisibility(View.INVISIBLE);
	//						startScheduleListLayoutAnimation(
	//								schedule_a_day,
	//								wrapper, collection,
	//								collection.getHeight() + schedule_title_background.getHeight(),
	//								collection.getHeight(), 100
	//								);
	//					}
	//				});
	//
	//			}
	//		});
	//
	//	}
}
