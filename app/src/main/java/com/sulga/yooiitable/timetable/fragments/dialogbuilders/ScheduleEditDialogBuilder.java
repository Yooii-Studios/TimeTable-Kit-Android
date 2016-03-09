package com.sulga.yooiitable.timetable.fragments.dialogbuilders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.YTAlarmManager;
import com.sulga.yooiitable.constants.FixedSizes;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.language.YTLanguage;
import com.sulga.yooiitable.language.YTLanguageType;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.parts.YTShapeRoundRectThemePart;
import com.sulga.yooiitable.timetable.TimetableActivity;
import com.sulga.yooiitable.timetable.fragments.ScheduleFragment;
import com.yooiistudios.common.ad.AdUtils;
import com.yooiistudios.stevenkim.alarmsound.OnAlarmSoundClickListener;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSound;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundDialog;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleEditDialogBuilder {

	private RelativeLayout pickDate;
	private TextView pickDateText;
	private RelativeLayout pickTime;
	private TextView pickTimeText;
	private Spinner pickParentLesson;
	private EditText editScheduleName;
	private EditText editScheduleMemo;
	private Spinner alarmScheduleSpinner;
    //Alarm Sound Pick
    private LinearLayout alarmSoundPickWrapper;
    private TextView alarmSoundPickText;

	private ImageButton ok;
	private ImageButton cancel;
	private ImageButton delete;

	private Timetable mainTimetable;

	private DatePickerDialog dateDialog = null;
	private int scheduleYear = -1;
	private int scheduleMonth = -1;
	private int scheduleDay = -1;

	//private TimePickerDialogFragment timeDialog;
	private TimePickerDialog timeDialog = null;

	private int scheduleHour = -1;
	private int scheduleMin = -1;

	private int scheduleAlarm = Schedule.SCHEDULE_ALARM_NONE;
    private SKAlarmSound alarmSound;

	private Schedule schedule;
	String passedScheduleKey = null;
	int passedScheduleIndex = -1;

	private class SpinnerItem{
		private String itemName;
		private Object item;
		public SpinnerItem(String itemName, Object item){
			this.itemName = itemName;
			this.item = item;
		}		
	}

	public Dialog createDialog(final Context context,
			final Schedule schedule,
			final ScheduleFragment parent){
		Resources res = context.getResources();

		final LinearLayout dialogView = (LinearLayout) 
				View.inflate(context, R.layout.dialog_editschedule, null);
		final Dialog dialog =  new Dialog(context, R.style.TransparentDialog);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		dialog.getWindow().setLayout(
				(int)res.getDimension(R.dimen.dialog_editelsson_width), 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		float topOffset = res.getDimension(R.dimen.dialog_top_offset);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.y += FixedSizes.ACTIONBAR_HEIGHT + topOffset;
		wlp.gravity = Gravity.TOP;
		window.setAttributes(wlp);
		
		View rootView = dialogView.findViewById(R.id.dialog_editschedule_root);
		rootView.requestFocus();
		new YTShapeRoundRectThemePart(12.0f, 
				(int)(255f * 0.85f),
				Color.parseColor("#404040"), 
				Color.parseColor("#d2d0c6"), 3)
		.setViewTheme(context, rootView);

		Calendar c = Calendar.getInstance();
		//c.set(Calendar.MINUTE, CustomTimePickerDialog.getRoundedMinute(c.get(Calendar.MINUTE)));

		//mainTimetable = getIntent().getParcelableExtra("MainTimetable");
		//int mainTimetableIndex = getIntent().getIntExtra("MainTimetableIndex", -1);
		mainTimetable = TimetableDataManager.getMainTimetable();

		MyLog.d("EditScheduleActivity", mainTimetable.getLessonList().toString());
		final ArrayList<Lesson> lessonList = mainTimetable.getLessonList();

		//		passedScheduleKey = getIntent().getStringExtra("ScheduleKey");
		//		passedScheduleIndex = getIntent().getIntExtra("ScheduleIndex", -1);
		//		if(passedScheduleKey == null || passedScheduleIndex == -1){
		//			schedule = null;
		//		}else{
		//			schedule = TimetableDataManager.getInstance().getScheduleListFromKey(passedScheduleKey).get(passedScheduleIndex);
		//		}

		if(schedule == null){
			scheduleYear = c.get(Calendar.YEAR);
			scheduleMonth = c.get(Calendar.MONTH);
			scheduleDay = c.get(Calendar.DAY_OF_MONTH);
			scheduleHour = c.get(Calendar.HOUR_OF_DAY);
			scheduleMin = c.get(Calendar.MINUTE);
		}else{
			scheduleYear = schedule.getScheduleYear();
			scheduleMonth = schedule.getScheduleMonth();
			scheduleDay = schedule.getScheduleDay();
			scheduleHour = schedule.getScheduleHour();
			scheduleMin = schedule.getScheduleMin();
		}

		pickDate = (RelativeLayout) 
				dialogView.findViewById(R.id.dialog_editschedule_button_editdate);
		pickDateText = (TextView)
				dialogView.findViewById(R.id.dialog_editschedule_editdate_text);
		//SimpleDateFormat sdfDate = new SimpleDateFormat("MMMMM dd(EEE)");
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMMMM dd(EEE)"); 
		if(YTLanguage.getCurrentLanguageType(context) == YTLanguageType.ENGLISH){
			sdfDate = new SimpleDateFormat("EE, dd MMMM");
		}
		c.set(scheduleYear, scheduleMonth, scheduleDay, scheduleHour, scheduleMin, 0);
		Date d = c.getTime();

		String date = sdfDate.format(d);

		//schedule.setScheduleYear(c.get(GregorianCalendar.YEAR));
		//schedule.setScheduleMonth(c.get(GregorianCalendar.MONTH));
		//schedule.setScheduleDay(c.get(GregorianCalendar.DAY_OF_MONTH));	

		pickDateText.setText(date);

		pickDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dateDialog = new DatePickerDialog(
						context,
						new ScheduleOnDateSetListener(context),
						scheduleYear,
						scheduleMonth, 
						scheduleDay);
				dateDialog.show();
				//				if(dateDialog == null)
				//					dateDialog = DatePickerDialogFragment.newInstance();
				//
				//				Bundle b = new Bundle();
				//				b.putInt("Year", scheduleYear);
				//				b.putInt("Month", scheduleMonth);
				//				b.putInt("Day", scheduleDay);
				//				dateDialog.setArguments(b);
				//
				//				dateDialog.setOnDateSetListener(EditScheduleActivity.this);
				//				dateDialog.show(getSupportFragmentManager(), "DatePick");				
			}
		});

		pickTime = (RelativeLayout) dialogView.findViewById(R.id.dialog_editschedule_button_edittime);
		pickTimeText = (TextView) dialogView.findViewById(R.id.dialog_editschedule_edittime_text);
		//Calendar.getInstance().getTime()
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH : mm");
		String time = sdfTime.format(d);

		//schedule.setScheduleHour(c.get(GregorianCalendar.HOUR_OF_DAY));
		//schedule.setScheduleMin(c.get(GregorianCalendar.MINUTE));
		pickTimeText.setText(time);

		pickTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(timeDialog == null)
					timeDialog = TimePickerDialogFragment.newInstance();

				Bundle b = new Bundle();
				b.putInt("Hour", scheduleHour);
				b.putInt("Min", scheduleMin);
				timeDialog.setArguments(b);

				timeDialog.setOnTimeSetListener(EditScheduleActivity.this);
				timeDialog.show(getSupportFragmentManager(), "TimePick");	
				 */

				final Calendar c = Calendar.getInstance();
				if(scheduleHour == -1 || scheduleMin == -1){
					scheduleHour = c.get(Calendar.HOUR_OF_DAY);
					scheduleMin = c.get(Calendar.MINUTE);
				}else{
					c.set(Calendar.HOUR_OF_DAY, scheduleHour);
					c.set(Calendar.MINUTE, scheduleMin);
				}
				timeDialog = new TimePickerDialog(context,
						onTimeSetListener, 
						c.get(Calendar.HOUR_OF_DAY),
						c.get(Calendar.MINUTE), 
						true);
//						TimePickerDialog.newInstance(
//								onTimeSetListener, 
//								c.get(Calendar.HOUR_OF_DAY),
//								c.get(Calendar.MINUTE), 
//								true);

				timeDialog.show();
				//				CustomTimePickerDialog timePickerDialog = 
				//						new CustomTimePickerDialog(
				//								EditScheduleActivity.this, EditScheduleActivity.this,
				//								c.get(Calendar.HOUR_OF_DAY),
				//								c.get(Calendar.MINUTE), 
				//								true
				//								);
				//				//timePickerDialog.setTitle("2. Select Time");
				//				timePickerDialog.show();
			}
		});

		pickParentLesson = (Spinner) dialogView.findViewById(R.id.dialog_editschedule_lessonpick_spinner);
		setLessonSpinner(parent.getActivity(), lessonList);
		if(schedule != null){
			pickParentLesson.setSelection(
					getParentLessonPosition(
							mainTimetable.getLessonList(), 
							schedule.getParentLesson())
					);
		}
		editScheduleName = (EditText) dialogView.findViewById(R.id.dialog_editschedule_edit_title);
		if(schedule != null)
			editScheduleName.setText(schedule.getScheduleName());
		editScheduleMemo = (EditText) dialogView.findViewById(R.id.dialog_editschedule_edit_memo);
		if(schedule != null)
			editScheduleMemo.setText(schedule.getScheduleMemo());


		alarmScheduleSpinner = (Spinner) dialogView.findViewById(R.id.dialog_editschedule_alarmpick_spinner);
		if(schedule != null) {
            scheduleAlarm = schedule.getScheduleAlarm();
            alarmSound = schedule.getAlarmSound();
        }
		String[] alarmTimeNames = 
				res.getStringArray(R.array.timetable_setting_lessonAlarms);

        //2015.01.13 Added Alarm Sound Pick
        alarmSoundPickWrapper = (LinearLayout) dialogView.findViewById(R.id.dialog_editschedule_alarmsoundpick_wrapper);
        alarmSoundPickText = (TextView) dialogView.findViewById(R.id.dialog_editschedule_alarmsoundpick_text);
        alarmSoundPickText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SKAlarmSoundDialog.makeSoundDialog(context, alarmSound,
                        new OnAlarmSoundClickListener() {
                            @Override
                            public void onAlarmSoundSelected(SKAlarmSound alarmSound) {
                                ScheduleEditDialogBuilder.this.alarmSound = alarmSound;
                                alarmSoundPickText.setText(alarmSound.getSoundTitle());
                            }
                            @Override
                            public void onAlarmSoundSelectCanceled() {

                            }
                            @Override
                            public void onAlarmSoundSelectFailedDueToUsbConnection() {

                            }
                        }).show();
            }
        });
        if(scheduleAlarm == Schedule.SCHEDULE_ALARM_NONE){
            alarmSoundPickWrapper.setVisibility(View.INVISIBLE);
        }else{
            alarmSoundPickWrapper.setVisibility(View.VISIBLE);
        }
        //


        if(TimetableDataManager.getCurrentFullVersionState(context) == false) {
            //if not full version
            alarmTimeNames[0] = res.getString(R.string.unlock_full_version);
            alarmScheduleSpinner.setEnabled(false);
            alarmScheduleSpinner.setClickable(false);
            alarmScheduleSpinner.setFocusable(false);
            alarmScheduleSpinner.setFocusableInTouchMode(false);
//            alarmScheduleSpinner.requestDisallowInterceptTouchEvent(true);
            View alarmScheduleWrapper = dialogView.findViewById(R.id.dialog_editschedule_alarmpick_wrapper);
            alarmScheduleWrapper.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AdUtils.showInHouseStoreAd(context);
                    dialog.dismiss();
                }
            });
        }
		final Integer[] alarmTimes = {
				-1, 0, 1,
				5, 10, 20,
				30, 40, 50,
				60, 120, 180
		};

		ArrayList<SpinnerItem> alarmTimeList = 
				createSpinnerItemList(alarmTimeNames, alarmTimes);

		createAlarmSpinner(context, alarmTimeList, alarmScheduleSpinner);
		alarmScheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, 
					View view, int position, long id) {
				// TODO Auto-generated method stub
				scheduleAlarm = alarmTimes[position];

                if(scheduleAlarm == -1){
                    alarmSoundPickWrapper.setVisibility(View.INVISIBLE);
                }else{
                    alarmSoundPickWrapper.setVisibility(View.VISIBLE);
                    if(alarmSound == null)
                       alarmSound = SKAlarmSoundManager.loadLatestAlarmSound(context);
                    alarmSoundPickText.setText(alarmSound.getSoundTitle());
                }

				Map<String, String> alarmInfo = new HashMap<String, String>();
				String alarmTypeStr = scheduleAlarm == -1 ? 
						"None" : Integer.toString(scheduleAlarm);
				alarmInfo.put(FlurryConstants.ALARM_INFO_TIME, alarmTypeStr);
				alarmInfo.put(FlurryConstants.ALARM_TYPE_KEY, 
						FlurryConstants.ALARM_TYPE_SCHEDULE);
				FlurryAgent.logEvent(FlurryConstants.ALARM_USED, alarmInfo);
				MyLog.d("TimetableSetting", "Alarm Before : " + scheduleAlarm);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		alarmScheduleSpinner.setSelection(getAlarmTimePosition(alarmTimes, scheduleAlarm));

		ok = (ImageButton) dialogView.findViewById(R.id.dialog_editschedule_button_check);
		cancel = (ImageButton) dialogView.findViewById(R.id.dialog_editschedule_button_cancel);
		delete = (ImageButton) dialogView.findViewById(R.id.dialog_editschedule_button_delete);

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				Schedule newSchedule = null;
				if(schedule == null){
					newSchedule = new Schedule();
				}else{
					TimetableDataManager.getInstance().removeScheduleInMap(schedule);
					newSchedule = schedule;
				}
				//만약 스케쥴의 날짜가 바뀌면 해시테이블의 키가 바뀌는 경우와 같으니 remove후 다시 put해줘야한다.
				//				if(passedScheduleKey != null && passedScheduleIndex != -1){
				//					TimetableDataManager.getInstance().removeScheduleInMap(schedule);					
				//				}

				//String scheduleName = editScheduleName.			
				//1.스케쥴 이름 저장
				newSchedule.setScheduleName(editScheduleName.getText().toString());
				//2.스케쥴 메모 저장
				newSchedule.setScheduleMemo(editScheduleMemo.getText().toString());
				//3.스케쥴 날짜 저장
				newSchedule.setScheduleYear(scheduleYear);
				newSchedule.setScheduleMonth(scheduleMonth);
				newSchedule.setScheduleDay(scheduleDay);
				//4.스케쥴 시간 저장
				newSchedule.setScheduleHour(scheduleHour);
				newSchedule.setScheduleMin(scheduleMin);
				newSchedule.setScheduleAlarm(scheduleAlarm);
                newSchedule.setAlarmSound(alarmSound);

				int pickParentLessonSpinnerPosition = pickParentLesson.getSelectedItemPosition();
				if(pickParentLessonSpinnerPosition == 0){
					newSchedule.setParentLesson(null);
				}else{
					newSchedule.setParentLesson(lessonList.get(pickParentLessonSpinnerPosition - 1));
				}

				newSchedule.setLastUpdated(Calendar.getInstance().getTimeInMillis());

				//데이터 세팅 완료 후 TimetableActivity로 전송
				//				Intent intent = new Intent();
				//intent.putExtra("Schedule", schedule);
				//TimetableDataManager.getInstance().getSchedules().add(schedule);

				TimetableDataManager.getInstance().putSchedule(newSchedule);
//				TimetableDataManager.writeDatasToExternalStorage();

				if(newSchedule.getScheduleAlarm() != Schedule.SCHEDULE_ALARM_NONE){
					YTAlarmManager.cancelScheduleAlarm(context, newSchedule);
					YTAlarmManager.startScheduleAlarm(context, newSchedule);
				}else{
					YTAlarmManager.cancelScheduleAlarm(context, newSchedule);
				}
				parent.refresh();
				parent.startAddAndEditScheduleAnimation(newSchedule);
				
				((TimetableActivity)parent.getActivity()).refreshTimetablePage(
						TimetableDataManager.getTimetables().size() - 1);
				//				String key = TimetableDataManager.makeKeyFromSchedule(newSchedule);
				//				int index = TimetableDataManager.getInstance().getIndexOfSchedule(newSchedule);
				//				intent.putExtra("ScheduleKey", key);
				//				intent.putExtra("ScheduleIndex", index);
				//				parent.setResult(Activity.RESULT_OK, intent);


				dialog.dismiss();
				FlurryAgent.logEvent(FlurryConstants.SCHEDULE_ADDED);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				EditScheduleActivity.this.setResult(Activity.RESULT_CANCELED);

				//				InputMethodManager inputManager = (InputMethodManager)            
				//						EditScheduleActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE); 
				//				inputManager.hideSoftInputFromWindow(
				//						EditScheduleActivity.this.getCurrentFocus().getApplicationWindowToken(),      
				//						InputMethodManager.HIDE_NOT_ALWAYS
				//						);
				//
				//				finish();
				dialog.dismiss();
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.onDeleteSchedule(schedule);
				dialog.dismiss();
			}
		});



		return dialog;

	}

	private Spinner createAlarmSpinner(Context context, 
			ArrayList<SpinnerItem> list, Spinner spinner){
		String[] items = new String[list.size()];
		for(int i = 0; i < list.size() ; i++){
			items[i] = list.get(i).itemName;
		}
		ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(
				context,       // 액티비티 클래스 내에 어댑터를 정의할 경우 this는 액티비티 자신을 의미합니다.
				android.R.layout.simple_spinner_item,    // 현재 선택된 항목을 보여주는 레이아웃의 ID
				items                            // 위에 정의한 문자열의 배열 객체를 대입합니다.
				);

		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);

		return spinner;
	}


	public void setLessonSpinner(Activity activity, final ArrayList<Lesson> lessonList){
		//		ArrayList<String> spinnerItems = new ArrayList<String>();
		//		spinnerItems.add("None");
		//		for(int i = 0; i < lessonList.size() ; i++){
		//			Lesson l = lessonList.get(i);
		//			String s = l.getLessonName() + 
		//					" (" + Timetable.getDayStringFromGregorianCalendar(
		//							context, l.getLessonInfo().getDay())+")";
		//			spinnerItems.add(s);
		//		}
		ArrayList<LessonAdapterItem> lessonAdapterItems = 
				new ArrayList<LessonAdapterItem>();
		lessonAdapterItems.add(new LessonAdapterItem("None", -1, Color.TRANSPARENT));
		for(int i = 0; i < lessonList.size() ; i++){
			Lesson l = lessonList.get(i);
			LessonAdapterItem item = new LessonAdapterItem(
					l.getLessonName(), l.getDay(), l.getColor());
			lessonAdapterItems.add(item);
		}
		ArrayAdapter<LessonAdapterItem> adapter = new LessonAdapter(
				activity, 
				R.layout.item_lesson_spinner,
				//				android.R.layout.simple_spinner_item, 
				lessonAdapterItems);
		adapter.setDropDownViewResource(R.layout.item_lesson_spinner_dropdown);
		pickParentLesson.setAdapter(adapter);
		if(schedule != null && schedule.getParentLesson() != null){
			//만약 스케쥴 에딧할게 존재한다면 parentlesson존재여부를 따진 후에 디폴트를 설정한다.
			int position = -1;
			for(int i = 0; i < lessonList.size() ; i++){
				if(schedule.getParentLesson() == lessonList.get(i)){
					position = i;
					break;
				}
			}
			if(position != -1){
				pickParentLesson.setSelection(position + 1);
				//				pickParentLesson.setBackgroundColor(schedule.getParentLesson().getColor());
			}
		}

		pickParentLesson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, 
					View view, int position, long id) {
				// TODO Auto-generated method stub
				pickParentLesson.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private int getAlarmTimePosition(Integer[] alarmTimes, int alarmTime){
		//int alarmTimePosition = -1;
		for(int i = 0; i < alarmTimes.length ; i++){
			if(scheduleAlarm == alarmTimes[i]){
				return i;
			}
		}
		return -1;
	}

	private int getParentLessonPosition(ArrayList<Lesson> lessons, Lesson lesson){
		for(int i = 0; i < lessons.size() ; i++){
			if(lessons.get(i) == lesson){
				return i + 1;
			}
		}
		return -1;
	}

	private ArrayList<SpinnerItem> createSpinnerItemList(String[] itemNames, Object[] items){
		if(itemNames.length == items.length 
				&& itemNames.length != 0){
			ArrayList<SpinnerItem> itemList = new ArrayList<SpinnerItem>();
			for(int i = 0 ; i < itemNames.length ; i++){
				SpinnerItem item = new SpinnerItem(itemNames[i], items[i]);
				itemList.add(item);
			}
			return itemList;
		}else{
			return null;
		}
	}


	private class ScheduleOnDateSetListener implements DatePickerDialog.OnDateSetListener{
		private Context context;
		public ScheduleOnDateSetListener(Context context){
			this.context = context;
		}
//		@Override
//		public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear,
//				int dayOfMonth) {
//			// TODO Auto-generated method stub
//			MyLog.d("EditScheduleActivity", "DataSet called");				
//			//schedule.setScheduleYear(year);
//			//schedule.setScheduleMonth(monthOfYear);
//			//schedule.setScheduleDay(dayOfMonth);
//			scheduleYear = year;
//			scheduleMonth = monthOfYear;
//			scheduleDay = dayOfMonth;
//
//			SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd(EEE), yyyy");
//			GregorianCalendar c = new GregorianCalendar(year,monthOfYear, dayOfMonth);
//			String date = sdf.format(c.getTime());
//			Log.e("Calendar", date);
//
//			pickDateText.setText(date);
//		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			MyLog.d("EditScheduleActivity", "DataSet called");				
			//schedule.setScheduleYear(year);
			//schedule.setScheduleMonth(monthOfYear);
			//schedule.setScheduleDay(dayOfMonth);
			scheduleYear = year;
			scheduleMonth = monthOfYear;
			scheduleDay = dayOfMonth;

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMMM");
			if(YTLanguage.getCurrentLanguageType(context) == YTLanguageType.KOREAN){
				sdfDate = new SimpleDateFormat("MMMMM dd(EEE)"); 
			}
			c.set(scheduleYear, scheduleMonth, scheduleDay, scheduleHour, scheduleMin, 0);
			Date d = c.getTime();
			String date = sdfDate.format(d);

//			SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd(EEE), yyyy");
//			GregorianCalendar c = new GregorianCalendar(year,monthOfYear, dayOfMonth);
//			String date = sdf.format(c.getTime());
//			Log.e("Calendar", date);

			pickDateText.setText(date);

		}
	}

	TimePickerDialog.OnTimeSetListener onTimeSetListener = 
			new TimePickerDialog.OnTimeSetListener() {

//		@Override
//		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
//			// TODO Auto-generated method stub
//			MyLog.d("EditScheduleActivity", "TimeSet called");	
//			//schedule.setScheduleHour(hourOfDay);
//			//schedule.setScheduleMin(minute);
//
//			scheduleHour = hourOfDay;
//			scheduleMin = minute;
//
//			SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
//			GregorianCalendar c = new GregorianCalendar();
//			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//			c.set(Calendar.MINUTE, minute);
//
//			String time = sdf.format(c.getTime());
//			Log.e("Calendar", time);
//
//			pickTimeText.setText(time);
//
//		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			MyLog.d("EditScheduleActivity", "TimeSet called");	
			//schedule.setScheduleHour(hourOfDay);
			//schedule.setScheduleMin(minute);

			scheduleHour = hourOfDay;
			scheduleMin = minute;

			SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
			GregorianCalendar c = new GregorianCalendar();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);

			String time = sdf.format(c.getTime());
			Log.e("Calendar", time);

			pickTimeText.setText(time);

		}
	};

	//	class City {
	//        public City(String city, int d) {
	//            this.city = city;
	//            this.distance = String.valueOf(d);
	//        }
	//
	//        String city;
	//        String distance;
	//    }

	private class LessonAdapterItem{
		String name;
		int day;
		int color;
		public LessonAdapterItem(String name, int day, int color){
			this.name = name;
			this.color = color;
			this.day = day;
		}
	}
	class LessonAdapter extends ArrayAdapter<LessonAdapterItem> {

		int promptLayout;
		Activity activity;
		public LessonAdapter(Activity activity, 
				int promptLayout, 
				List<LessonAdapterItem> objects) {
			super(activity, promptLayout, objects);
			this.activity = activity;
			this.promptLayout = promptLayout;
		}

		@Override //don't override if you don't want the default spinner to be a two line view
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = activity.getLayoutInflater();
			View row = inflater.inflate(promptLayout, parent, false);
			TextView label = (TextView) row.findViewById(R.id.item_lesson_spinner_lessonName);
			ImageView color = (ImageView) row.findViewById(R.id.item_lesson_spinner_lessoncolor);
			LessonAdapterItem item = getItem(position);
			String lessonNameString = null;
			if(position != 0){
				lessonNameString = item.name + 
						" (" + Timetable.getDayStringFromGregorianCalendar(
								getContext(), item.day) 
								+ ")";
			}else{
				lessonNameString = item.name;
			}

			label.setText(lessonNameString);
			GradientDrawable gd = (GradientDrawable)color.getBackground();
			gd.setColor(item.color);
			color.setBackgroundDrawable(gd);


			return row;

		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return initView(position, convertView);
		}

		private View initView(int position, View convertView) {
			if(convertView == null)
				convertView = View.inflate(getContext(),
						R.layout.item_lesson_spinner_dropdown,
						null);
			LessonAdapterItem l = getItem(position);
			TextView lessonName = (TextView)convertView
					.findViewById(R.id.item_lesson_spinner_lessonName);
			ImageView lessonColor = (ImageView)convertView
					.findViewById(R.id.item_lesson_spinner_lessoncolor); 

			String lessonNameString = null;
			if(position != 0){
				lessonNameString = l.name + 
						" (" + Timetable.getDayStringFromGregorianCalendar(
								getContext(), l.day) 
								+ ")";
			}else{
				lessonNameString = l.name;
			}

			lessonName.setText(lessonNameString);
			GradientDrawable gd = (GradientDrawable)lessonColor.getBackground();
			gd.setColor(l.color);
			lessonColor.setBackgroundDrawable(gd);

			//            TextView tvText1 = (TextView)convertView.findViewById(android.R.id.text1);
			//            TextView tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
			//            tvText1.setText(getItem(position).city);
			//            tvText2.setText(getItem(position).distance);
			return convertView;
		}
	}

}
