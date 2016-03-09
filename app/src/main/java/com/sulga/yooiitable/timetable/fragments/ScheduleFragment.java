package com.sulga.yooiitable.timetable.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.YTAlarmManager;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.TimeInfo;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.parts.YTRoundRectThemePart;
import com.sulga.yooiitable.theme.parts.YTShapeRoundRectThemePart;
import com.sulga.yooiitable.timetable.TimetableActivity;
import com.sulga.yooiitable.timetable.fragments.dialogbuilders.DeleteScheduleAlertDialogBuilder;
import com.sulga.yooiitable.timetable.fragments.dialogbuilders.ScheduleEditDialogBuilder;
import com.sulga.yooiitable.utils.FixTileModeBug;
import com.yooiistudios.common.ad.AdUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ScheduleFragment extends Fragment {
	public static final int MAX_SCHEDULE_COUNT_FREE_VERSION = 5;
	public static ScheduleFragment newInstance() {
		ScheduleFragment scheduleFragment = new ScheduleFragment();

		return scheduleFragment;
	}

	private View scheduleView;

	private LinearLayout scheduleListWrapper;
	private ScrollView scheduleListScroll;

	private Button addSchedule;
//	private Button syncSchedule; // 민수에게 문의 결과 구현중 막혀서 더이상 쓰지 않음

	private Timetable mainTimetable;

	//	private ActionBar actionBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		scheduleView = inflater.inflate(R.layout.fragment_schedule, container, false);
		FixTileModeBug.fixBackgroundRepeat(scheduleView);

		scheduleListWrapper = (LinearLayout)
				scheduleView.findViewById(R.id.fragment_schedule_listwrapper);
		scheduleListScroll = (ScrollView)
				scheduleView.findViewById(R.id.fragment_schedule_listscroll);

		mainTimetable = TimetableDataManager.getMainTimetable();

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
				if(!TimetableDataManager.getCurrentFullVersionState(getActivity())
						&& scheduleCounts >= MAX_SCHEDULE_COUNT_FREE_VERSION){
					String message = getActivity().getResources().getString(R.string.unlock_full_version);
//					ToastMaker.popupUnlockFullVersionToast(getActivity(),
//                            ToastMaker.UNLOCK_FULL_VERSION_TOAST_OVERFLOW_SCHEDULENUM,
//                            false);
                    AdUtils.showInHouseStoreAd(getActivity(),
                            getString(R.string.unlock_full_version_schedule_overflow));
					return;
				}
				ScheduleEditDialogBuilder builder = new ScheduleEditDialogBuilder();
				builder.createDialog(getActivity(), null, ScheduleFragment.this).show();

				MyLog.d("ScheduleFragment", mainTimetable.getLessonList().toString());
			}
		});

		/*
		syncSchedule = (Button) scheduleView.findViewById(R.id.fragment_schedule_sync);
		syncSchedule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GCAccountManager syncManager = GCAccountManager.getInstance();
				syncManager.showAccountSelectDialog(ScheduleFragment.this.getActivity());
			}
		});
		*/
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

	public void refresh(){
        if(scheduleListWrapper == null)
            return;
		scheduleListWrapper.removeAllViews();
		createScheduleListView(TimetableDataManager.getSchedules());
		scheduleListWrapper.invalidate();
		MyLog.d("ScheduleFragment", "Refreshed!");
	}

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
		}
	}

	private void addTodayLineView(){
		LayoutInflater inflater = getActivity().getLayoutInflater();

		LinearLayout todayLine = (LinearLayout) inflater.inflate(
				R.layout.view_schedule_todayline,
				scheduleListWrapper,
				false
				);
		TextView todayLineText = 
				(TextView) todayLine.findViewById(R.id.view_schedule_todayline_todaytext);
		YTRoundRectThemePart todayThemePart = new YTShapeRoundRectThemePart(
				12.0f,
				255, Color.parseColor("#1f1f1f"),
				Color.parseColor("#d2d0c6"), 1 
				);
		todayThemePart.setViewTheme(getActivity(), todayLineText);
		scheduleListWrapper.addView(todayLine);
	}


	//	private final static String SCHEDULE_ITEMS_WRAPPER = "SCHEDULE_ITEMS_WRAPPER";
	private void addScheduleView(Schedule s, String dateKey){
		LayoutInflater inflater = getActivity().getLayoutInflater();

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
		scheduleListWrapper.addView(scheduleViewPager);
		return;
	}

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
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				backgroundForAnim.setVisibility(View.INVISIBLE);
			}
		});

		backgroundForAnim.startAnimation(fadeout);
	}

	private View getScheduleView(Schedule s){
		View v = scheduleListWrapper.findViewWithTag(s);
		return v;
	}

	private View getScheduleItemView(LayoutInflater inflater,
			ViewGroup wrapper, 
			final Schedule s){
		View root = inflater.inflate(R.layout.view_schedule, wrapper, false);

		TextView monthText = (TextView) root.findViewById(R.id.view_schedule_dateview_monthtext);
		TextView dateText = (TextView) root.findViewById(R.id.view_schedule_dateview_datetext);
		TextView dayText = (TextView) root.findViewById(R.id.view_schedule_dateview_daytext);
		TextView ddayText = (TextView) root.findViewById(R.id.view_schedule_contentsview_dday_text);

		//		Resources res = getActivity().getResources();
		float roundRectCorner = 8.0f;
		YTRoundRectThemePart monthThemePart = new YTShapeRoundRectThemePart(
				roundRectCorner,
				255, Color.parseColor("#d2d0c6"),
				0, 0
				);
		monthThemePart.setViewTheme(getActivity(), monthText,
				roundRectCorner,
				true, true, false, false);

		YTRoundRectThemePart dayThemePart = new YTShapeRoundRectThemePart(
				roundRectCorner,
				255, Color.parseColor("#bababa"),
				0, 0
				);
		dayThemePart.setViewTheme(getActivity(), 
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

		scheduleName.setText(s.getScheduleName());
		if(s.getParentLesson() != null){
			parentLesson.setText(s.getParentLesson().getLessonName());
		}

		root.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//				Intent intent = new Intent(ScheduleFragment.this.getActivity(), EditScheduleActivity.class);
				ScheduleEditDialogBuilder builder = new ScheduleEditDialogBuilder();
				builder
				.createDialog(getActivity(), s, ScheduleFragment.this)
				.show();
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
				collection.addView(v);
				//return r;
			}else{
				v = (Button) inflater.inflate(
						R.layout.view_schedule_deletebutton, 
						collection, 
						false
						);
				v.setOnClickListener(new OnDeleteScheduleClickListener(collection));

				collection.addView(v);
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

	private class OnScheduleLongClickListener implements View.OnLongClickListener{
		private ViewGroup collection;
		public OnScheduleLongClickListener(ViewGroup collection){
			this.collection = collection;
		}
		public boolean onLongClick(View v) {
			final Schedule s = (Schedule) collection.getTag();
			DeleteScheduleAlertDialogBuilder.createDialog(getActivity(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onDeleteSchedule(s);
				}
			}).show();
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
			Schedule s = (Schedule) collection.getTag();
			onDeleteSchedule(s);
		}
	}

	public boolean onDeleteSchedule(Schedule s){
		if(s == null){
			return false;
		}
		if(!s.getGoogleCalendarEventKey().equals(Schedule.EVENT_KEY_NONE))
			TimetableDataManager.getInstance().putEventIDToDeleteOnGoogleCalendar(s.getGoogleCalendarEventKey());
		String key = TimetableDataManager.makeKeyFromSchedule(s);

		TimetableDataManager.getSchedules().get(key).remove(s);
		YTAlarmManager.cancelScheduleAlarm(getActivity(), s);

		((TimetableActivity)ScheduleFragment.this.getActivity())
		.refreshTimetablePage(TimetableDataManager.getTimetables().size());

		refresh();
		return true;
	}
}
