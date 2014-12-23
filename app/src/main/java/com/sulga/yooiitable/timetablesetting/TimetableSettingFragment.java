package com.sulga.yooiitable.timetablesetting;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.YTAlarmManager;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.constants.RequestCodes;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.Timetable.ColumnTypes;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.YTTimetableTheme;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;
import com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs.SettingDayDialogCreator;
import com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs.SettingPeriodDialogCreator;
import com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs.SettingTimeDialogCreator;
import com.sulga.yooiitable.timetablesetting.utils.TimetableSettingStringManager;
import com.sulga.yooiitable.utils.MNDeviceSizeChecker;
import com.sulga.yooiitable.utils.ToastMaker;
import com.sulga.yooiitable.utils.YTBitmapLoader;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableSettingFragment extends Activity{
	public static String BUNDLE_PAGE_INDEX_KEY = "TimetablePageIndex";
	private static final String TAG = "TimetableSettingFragment";
	//Context context;
	public static final int COLUMNNUM_USER_CUSTOM = -1;

	private int timetablePageIndex;	//you MUST set this
	private Timetable timetable;

//	private View ;
	
	LinearLayout settingsDayWrapper;
	LinearLayout settingsPeriodWrapper;
	LinearLayout settingsTimeWrapper;
	LinearLayout settingsAlarmWrapper;
	TextView settingsDayText;
	TextView settingsPeriodText;
	TextView settingsTimeText;
	TextView settingsAlarmText;
	
	LinearLayout pickThemeWrapper;
	TextView pickThemeText;
	int pickThemeClickedItemPosition;
	
	ImageView alarmLockImage;

	private final static int[] startDays = new int[]{
		GregorianCalendar.SUNDAY,
		GregorianCalendar.MONDAY
	};
	private static String[] startDayStrings;

	private final static int[] endDays = new int[]{
		GregorianCalendar.FRIDAY,
		GregorianCalendar.SATURDAY,
		GregorianCalendar.SUNDAY
	};
	private static String[] endDayStrings;
	
	private final static ColumnTypes[] columnTypes = new ColumnTypes[]{
		ColumnTypes.BY_PERIOD,
		ColumnTypes.BY_ALPHABET,
		ColumnTypes.BY_TIME
	};
	private static String[] columnTypeStrings;
	
	private final static int[] columnNums = new int[]{
		6,
		8,
		10,
		12
	};
	private final static int[] columnNums_double = new int[]{
		12,
		16,
		20,
		24
	};
	private static String[] columnNumStrings;
	
	private final static int[] startTimes = new int[]{
		0,1,2,3,4,5,6,
		7, 8, 9,10,11,12,
		13,14,15,16,17,18,
		19,20,21,22,23
	};
	private static String[] startTimeStrings;
	
	private final static int[] timeOffsets = new int[]{
		30, 45, 60, 90
	};
	private static String[] timeOffsetStrings;
	
	private static String[] alarmTimeStrings;

	private static final int[] alarmTimes = {
		Timetable.LESSON_ALARM_NONE, 0, 1,
		5, 10, 20,
		30, 40, 50,
		60, 120, 180
	};
	
	private static String[] themeStrings;

	int tt_startDay;
	int tt_endDay;
	Timetable.ColumnTypes tt_columnType;
	int tt_columnNum;
	int tt_startTime;
	int tt_timeOffset;
	//boolean tt_isLessonAlarmExist;
	int tt_lessonAlarmTime = 0;

	private boolean showTimeSettingDialog = false;
	Resources res;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		LayoutInflater inflater = (LayoutInflater) 
//				TimetableSettingFragment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
		super.onCreate(savedInstanceState);
		timetablePageIndex = getIntent().getIntExtra(BUNDLE_PAGE_INDEX_KEY, -1);
		showTimeSettingDialog = getIntent().getBooleanExtra("ShowTimeSettingDialog", false);
		
		setContentView(R.layout.view_timetable_option_settings_iconstyle);
//				inflater.inflate(
//						R.layout.view_timetable_option_settings_iconstyle, 
//						container, 
//						false);		
		
		
		
		settingsDayWrapper = (LinearLayout) 
				findViewById(R.id.view_timetable_settings_day_wrapper);
		settingsPeriodWrapper = (LinearLayout) 
				findViewById(R.id.view_timetable_settings_period_wrapper);
		settingsTimeWrapper = (LinearLayout) 
				findViewById(R.id.view_timetable_settings_time_wrapper);
		settingsAlarmWrapper = (LinearLayout) 
				findViewById(R.id.view_timetable_settings_alarm_wrapper);
		settingsDayText = (TextView) 
				findViewById(R.id.view_timetable_settings_day_text);
		settingsPeriodText = (TextView) 
				findViewById(R.id.view_timetable_settings_period_text);
		settingsTimeText = (TextView) 
				findViewById(R.id.view_timetable_settings_time_text);
		settingsAlarmText = (TextView) 
				findViewById(R.id.view_timetable_settings_alarm_text);
		alarmLockImage = (ImageView) 
				findViewById(R.id.view_timetable_settings_alarm_lockimage);
		
		pickThemeWrapper = (LinearLayout) 
				findViewById(R.id.view_timetable_settings_theme_wrapper);
		pickThemeText = (TextView) 
				findViewById(R.id.view_timetable_settings_theme_text);
		
		if(TimetableDataManager.getCurrentFullVersionState(this) == false){
			//if not full version
			alarmLockImage.setVisibility(View.VISIBLE);
		}else{
			alarmLockImage.setVisibility(View.INVISIBLE);
		}

		timetable = TimetableDataManager
				.getInstance()
				.getTimetableAtPage(timetablePageIndex);

		res = getResources();
		themeStrings = res.getStringArray(R.array.timetable_setting_theme_themes);
		startDayStrings = res.getStringArray(R.array.timetable_setting_startdays);
		endDayStrings = res.getStringArray(R.array.timetable_setting_enddays);
		columnTypeStrings = res.getStringArray(R.array.timetable_setting_columnTypes);
		columnNumStrings = res.getStringArray(R.array.timetable_setting_columnNums);
		startTimeStrings = res.getStringArray(R.array.timetable_setting_startTimes);
		timeOffsetStrings = res.getStringArray(R.array.timetable_setting_timeOffsets);
		alarmTimeStrings = res.getStringArray(R.array.timetable_setting_lessonAlarms);
		
		tt_startDay = timetable.getStartDay();
		tt_endDay = timetable.getEndDay();
		tt_columnType = timetable.getColumnType();
		tt_columnNum = timetable.getPeriodNum();
		tt_startTime = timetable.getStartTime();
		tt_timeOffset = timetable.getPeriodUnit();
		//tt_isLessonAlarmExist = timetable.getIsLessonAlarmExists();
		tt_lessonAlarmTime = timetable.getLessonAlarmTime();

		MyLog.d("TimetableSettingFragment", timetable.getLessonList().toString());


		//		pickStartDayLayout = (RelativeLayout) 
		//				findViewById(R.id.fragment_timetable_option_select_startday);
		//		pickStartDayText = (TextView) 
		//				findViewById(R.id.fragment_timetable_option_startday_text);
		//		pickStartDayText.setText(
		//				Timetable.getDayStringFromGregorianCalendar(timetable.getStartDay())
		//				);

		initSettingsText();
		
		pickThemeWrapper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelectOptionDialogCreator.showThemeUnlockListDialog(
						TimetableSettingFragment.this,
						res.getString(R.string.timetable_setting_theme_select_theme_title), 
						themeStrings, 
						YTTimetableTheme.getThemeTypeValues(),
						new SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener() {

							@Override
							public void onClick(int clickedItemPosition) {
								// TODO Auto-generated method stub
								YTTimetableTheme.ThemeType[] themes =
										YTTimetableTheme.getThemeTypeValues();
								YTTimetableTheme.ThemeType clickedTheme = 
										themes[clickedItemPosition];
								pickThemeClickedItemPosition = clickedItemPosition;

								if(TimetableDataManager.getCurrentFullVersionState(TimetableSettingFragment.this) == false){
									for(int i = 0; i < YTTimetableTheme.lockedThemes.length ; i++){
										YTTimetableTheme.ThemeType locked = YTTimetableTheme.lockedThemes[i];
										if(clickedTheme == locked){
											String message = TimetableSettingFragment.this.getResources().getString(R.string.unlock_full_version);
											ToastMaker.popupToastAtCenter(TimetableSettingFragment.this, message);
											return;
										}
									}
								}

								if(clickedTheme != YTTimetableTheme.ThemeType.Photo){
									timetable.setThemeType(
											themes[clickedItemPosition]
											);
									(TimetableSettingFragment.this).onThemeSettled();
									//									pickThemeText.setText(
									//											themeStrings[clickedItemPosition]
									//											);
								}else{
									//									Intent intent = new Intent(Intent.ACTION_PICK);
									//									
									//									intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
									//									//								intent.putExtra("crop", "true");
									//									intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
									//									intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									//									Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, 
									//											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									(TimetableSettingFragment.this).showImagePicker();
									//									((Activity)m_Context).startActivityForResult(photoPickerIntent, 
									//											RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_PICK_IMAGE_PORTRAIT);
								}
								pickThemeText.setText(themeStrings[clickedItemPosition]);

								Map<String, String> info = new HashMap<String, String>();
								info.put(FlurryConstants.THEME_INFO_KEY, clickedTheme.toString());
								FlurryAgent.logEvent(FlurryConstants.THEME_SELECTED, info);
							}
						});

			}
		});

		
		settingsDayWrapper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int def_startDayIdx = TimetableSettingStringManager.
						getIntegerItemIndexOfArray(tt_startDay, startDays);
				final int def_endDayIdx = TimetableSettingStringManager.
						getIntegerItemIndexOfArray(tt_endDay, endDays);

				SettingDayDialogCreator.createDialog(TimetableSettingFragment.this, 
						startDayStrings, startDays, def_startDayIdx, 
						endDayStrings, endDays, def_endDayIdx,
						onSettingDayEndListener).show();
			}
		});

		settingsPeriodWrapper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int defColumnTypeIdx = TimetableSettingStringManager
						.getColumnTypeItemIndexOfArray(tt_columnType, columnTypes);
				int defColumnNumIdx = -1;
				final int maxPeriodNum = (60 * 24) / timetable.getPeriodUnit();
				defColumnNumIdx = TimetableSettingStringManager.
							getIntegerItemIndexOfArray(tt_columnNum, columnNums);
				if(defColumnTypeIdx == -1){
					defColumnTypeIdx = 0;
				}
				if(defColumnNumIdx == -1){
					defColumnNumIdx = 0;
				}
				SettingPeriodDialogCreator.createDialog(TimetableSettingFragment.this, 
						timetable, maxPeriodNum,
						columnTypeStrings, columnTypes, defColumnTypeIdx, 
						tt_columnNum, 
						onSettingsPeriodEndListener).show();

				
			}
		});

		settingsTimeWrapper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int defStartTimeIdx = TimetableSettingStringManager
						.getIntegerItemIndexOfArray(tt_startTime, startTimes);
				int defTimeOffsetIdx = TimetableSettingStringManager.
						getIntegerItemIndexOfArray(tt_timeOffset, timeOffsets);
				if(defStartTimeIdx == -1){
					defStartTimeIdx = 0;
				}
				if(defTimeOffsetIdx == -1){
					defTimeOffsetIdx = 0;
				}
				SettingTimeDialogCreator.createDialog(TimetableSettingFragment.this,
						startTimeStrings, startTimes, defStartTimeIdx, 
						timeOffsetStrings, timeOffsets, defTimeOffsetIdx,
						onSettingsTimeEndListener).show();
			}
		});

		settingsAlarmWrapper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TimetableDataManager.getCurrentFullVersionState(TimetableSettingFragment.this) == false){
					//if not full version
					String warning = TimetableSettingFragment.this.getResources().getString(R.string.unlock_full_version);
					ToastMaker.popupToastAtCenter(TimetableSettingFragment.this, warning);
					return;
				}
				SelectOptionDialogCreator.showListDialog(
						TimetableSettingFragment.this, 
						res.getString(R.string.timetable_setting_select_lessonAlarm_title),
						alarmTimeStrings, 
						new SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener() {
							@Override
							public void onClick(int clickedItemPosition) {
								// TODO Auto-generated method stub
								tt_lessonAlarmTime = alarmTimes[clickedItemPosition];
//								pickLessonAlarmText.setText(alarmTimeStrings[clickedItemPosition]);
								String str = "";
								String alarmCancelled = TimetableSettingFragment.this.getResources()
										.getString(R.string.timetable_setting_alarm_cancelled);
								String alarmSettledOnTime = TimetableSettingFragment.this.getResources()
										.getString(R.string.timetable_setting_alarm_on_time);
								String alarmBeforeA = TimetableSettingFragment.this.getResources()
										.getString(R.string.timetable_setting_alarm_before_A);
								String alarmBeforeB = TimetableSettingFragment.this.getResources()
										.getString(R.string.timetable_setting_alarm_before_B);
								if(tt_lessonAlarmTime == Timetable.LESSON_ALARM_NONE){
									//no alarm
//									str = "알람 설정이 해지되었습니다.";
									str = alarmCancelled;
								}else if(tt_lessonAlarmTime == 0){
									//just in time alarm
//									str = "수업 정각에 알람이 설정되었습니다.";
									str = alarmSettledOnTime;
								}else{
									str = alarmBeforeA + Integer.toString(tt_lessonAlarmTime)
											+ alarmBeforeB;
								}

								Map<String, String> alarmInfo = new HashMap<String, String>();
								String alarmTypeStr = tt_lessonAlarmTime == Timetable.LESSON_ALARM_NONE ? 
										"None" : Integer.toString(tt_lessonAlarmTime);
								alarmInfo.put(FlurryConstants.ALARM_INFO_TIME, alarmTypeStr);
								alarmInfo.put(FlurryConstants.ALARM_TYPE_KEY, FlurryConstants.ALARM_TYPE_TIMETABLE);
								FlurryAgent.logEvent(FlurryConstants.ALARM_USED, alarmInfo);
								
								saveOption();
								saveAlarmOption();
								
								settingsAlarmText.setText(alarmTimeStrings[clickedItemPosition]);
								Toast.makeText(
										TimetableSettingFragment.this, 
										str,
										Toast.LENGTH_LONG)
										.show();
								
							}
						});
			}
		});
		
		if(showTimeSettingDialog == true){
			showTimeSettingDialogOnStart();
		}
		return ;
	}
	private void showTimeSettingDialogOnStart(){
		int defStartTimeIdx = TimetableSettingStringManager
				.getIntegerItemIndexOfArray(tt_startTime, startTimes);
		int defTimeOffsetIdx = TimetableSettingStringManager.
				getIntegerItemIndexOfArray(tt_timeOffset, timeOffsets);
		if(defStartTimeIdx == -1){
			defStartTimeIdx = 0;
		}
		if(defTimeOffsetIdx == -1){
			defTimeOffsetIdx = 0;
		}
		SettingTimeDialogCreator.createDialog(TimetableSettingFragment.this,
				startTimeStrings, startTimes, defStartTimeIdx, 
				timeOffsetStrings, timeOffsets, defTimeOffsetIdx,
				onSettingsTimeEndListener).show();
	}
	
	private void initSettingsText(){
		int def_themeTypeIdx = 
				TimetableSettingStringManager.
				getThemeTypeItemIndexOfArray(timetable.getThemeType(), ThemeType.values());
		//		pickThemeText.setText(themeStrings[def_themeTypeIdx]);
		pickThemeText.setText(themeStrings[def_themeTypeIdx]);

		int def_startDayIdx = TimetableSettingStringManager.
				getIntegerItemIndexOfArray(tt_startDay, startDays);
		int def_endDayIdx = TimetableSettingStringManager.
				getIntegerItemIndexOfArray(tt_endDay, endDays);
		if(def_startDayIdx == -1){
			def_startDayIdx = 0;
		}
		if(def_endDayIdx == -1){
			def_endDayIdx = 0;
		}
		settingsDayText.setText(startDayStrings[def_startDayIdx] + " - " + endDayStrings[def_endDayIdx]);
		int def_columnTypeIdx = 
				TimetableSettingStringManager
				.getColumnTypeItemIndexOfArray(tt_columnType, columnTypes);
		if(def_columnTypeIdx == -1){
			def_columnTypeIdx = 0;
		}
		settingsPeriodText.setText(columnTypeStrings[def_columnTypeIdx]
				+ " / " 
				+ Integer.toString(tt_columnNum));
		String timeStr = 
				tt_startTime < 10 ? 
						"0" + Integer.toString(tt_startTime) + " : 00" 
						: Integer.toString(tt_startTime) + " : 00"; 
		settingsTimeText.setText(timeStr + " / " + Integer.toString(tt_timeOffset) + "Min");
		int def_alarmIdx = TimetableSettingStringManager.
								getIntegerItemIndexOfArray(tt_lessonAlarmTime, alarmTimes);
		settingsAlarmText.setText(alarmTimeStrings[def_alarmIdx]);
	}
	public boolean saveOption(){
		if(clearTimetable == true){
			YTAlarmManager.cancelTimetableAlarm(TimetableSettingFragment.this, timetable);
			for(Lesson l : timetable.getLessonList()){
				timetable.onRemoveLesson(l);
			}
			timetable.getLessonList().clear();
			clearTimetable = false;
			SettingTimeDialogCreator.willClearTimetable = false;
		}else if(tt_lessonAlarmTime == Timetable.LESSON_ALARM_NONE){
			YTAlarmManager.cancelTimetableAlarm(TimetableSettingFragment.this, timetable);
		}
		

		timetable.setStartDay(tt_startDay);
		timetable.setEndDay(tt_endDay);
		timetable.setColumnType(tt_columnType);
		timetable.setPeriodNum(tt_columnNum);
		timetable.setStartTime(tt_startTime);
		timetable.setPeriodUnit(tt_timeOffset);
		//timetable.setIsLessonAlarmExists(tt_isLessonAlarmExist);
		timetable.setLessonAlarmTime(tt_lessonAlarmTime);

		//		Intent data = new Intent();
		//		data.putExtra("timetablePageIndex", timetablePageIndex);
		//		TimetableDataManager.getInstance().writeDatasToExternalStorage();
		//((Activity) TimetableSettingFragment.this).setResult(Activity.RESULT_OK, data);
		//TimetableSettingFragment.this.TimetableSettingFragment.this.set
		//TimetableSettingFragment.this.finish();
		TimetableDataManager.writeDatasToExternalStorage();
		return true;
	}
	
	private void saveAlarmOption(){
//		int timetablePageIndex = data.getIntExtra("timetablePageIndex", -1);
		if(timetablePageIndex != -1){
			Timetable timetable = 
					TimetableDataManager
					.getInstance()
					.getTimetableAtPage(timetablePageIndex);
			if(timetable.getLessonAlarmTime() != Timetable.LESSON_ALARM_NONE){
				YTAlarmManager.cancelTimetableAlarm(TimetableSettingFragment.this, timetable);
				YTAlarmManager.startTimetableAlarm(TimetableSettingFragment.this, timetable);
			}
			else if(timetable.getLessonAlarmTime() == Timetable.LESSON_ALARM_NONE)
				YTAlarmManager.cancelTimetableAlarm(TimetableSettingFragment.this, timetable);
		}

	}
	SettingDayDialogCreator.OnSettingDayEndListener onSettingDayEndListener = new SettingDayDialogCreator.OnSettingDayEndListener() {

		@Override
		public void onSettingsEnd(int startDay, int endDay, String startString, String endString) {
			// TODO Auto-generated method stub
			tt_startDay = startDay;
			tt_endDay = endDay;
			
//			int startDayIdx = timetable.getDayIndexFromGregorianCalendarDay(tt_startDay);
//			int endDayIdx = timetable.getDayIndexFromGregorianCalendarDay(tt_endDay);
			
			saveOption();
			for(int i = timetable.getLessonList().size() - 1; i >= 0 ; i--){
				Lesson l = timetable.getLessonList().get(i);
				if(timetable.doesTimetableIncludesGregorianDay(l.getDay()) == false){
					//if lesson overflows timetable day size
					YTAlarmManager.cancelLessonAlarm(TimetableSettingFragment.this, l);
					timetable.onRemoveLesson(l);
					timetable.getLessonList().remove(l);
					MyLog.d("onSettingDayEndListener", "timetable lesson removed");
				}
			}
			settingsDayText.setText(startString + " - " + endString);
		}
	};
	SettingPeriodDialogCreator.OnSettingColumnEndListener onSettingsPeriodEndListener = new SettingPeriodDialogCreator.OnSettingColumnEndListener() {

		@Override
		public void onSettingsEnd(ColumnTypes columnType, int columnNum) {
			// TODO Auto-generated method stub
			tt_columnType = columnType;
			tt_columnNum = columnNum;
			
			saveOption();
			int def_columnTypeIdx = 
					TimetableSettingStringManager
					.getColumnTypeItemIndexOfArray(tt_columnType, columnTypes);
			settingsPeriodText.setText(columnTypeStrings[def_columnTypeIdx]
					+ " / " 
					+ Integer.toString(tt_columnNum));
		}
	};
	private boolean clearTimetable = false;
	SettingTimeDialogCreator.OnSettingTimeEndListener onSettingsTimeEndListener = new SettingTimeDialogCreator.OnSettingTimeEndListener() {

		@Override
		public void onSettingsEnd(boolean willClearTimetable, int startTime,
				int timeOffset) {
			// TODO Auto-generated method stub
			clearTimetable = willClearTimetable;
			tt_startTime = startTime;
			tt_timeOffset = timeOffset;
			saveOption();
			String timeStr = 
					startTime < 10 ? 
							"0" + Integer.toString(startTime) + " : 00" : Integer.toString(startTime) + " : 00"; 
			settingsTimeText.setText(timeStr + " / " + Integer.toString(tt_timeOffset) + "Min");
		}
	};
	
	public void onThemeSettingsChanged(){
		YTTimetableTheme.ThemeType[] themes =
				YTTimetableTheme.getThemeTypeValues();
		MyLog.d("TimetableThemeFragment", "pickTemeClickedItemPosition : " 
				+ pickThemeClickedItemPosition);
		timetable.setThemeType(
				themes[pickThemeClickedItemPosition]
				);
		//		pickThemeText.setText(
		//				themes[pickThemeClickedItemPosition].toString()
		//				);
	}
	
	/** 다시 액티비티로 복귀하였을때 이미지를 셋팅 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		Log.i(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		// 모토롤라 폰에서, 아트를 선택하면 배경이 변경되어 버리는 버그가 있다. 추후 모토롤라 폰에서 예외처리를 해 주어야 할듯
		case RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_PICK_IMAGE_PORTRAIT:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Log.i(TAG, "REQ_CODE_PICK_IMAGE_PORTRAIT && RESULT_OK");

					// 기기의 가로세로 비율을 찾아낸다.
					int width = MNDeviceSizeChecker.getDeviceWidth(this);
					int height = MNDeviceSizeChecker.getDeviceHeight(this);
					float ratio = (float)height / (float)width;

					//					Uri originalPortImageUri = data.getData();

					//					if (originalPortImageUri == null || originalPortImageUri.toString().length() == 0) {
					Uri originalPortImageUri = Uri.fromFile(mTempFile);
					//	                    file = mTempFile;
					//	                }
					startCropActivity(data, ratio,
							originalPortImageUri,
							RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_CROP_FROM_IMAGE_PORTRAIT);
				}
			}
			break;

		case RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_CROP_FROM_IMAGE_PORTRAIT:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Log.i(TAG, "REQ_CROP_FROM_CAMERA && RESULT_OK");
					try {
						setThemeAndRelaunchApp(
								YTBitmapLoader.getPortraitCroppedImageUri(
										TimetableSettingFragment.this, 
										timetable.getId()));
						onThemeSettled();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				finish();
			}
			break;
		}
	}
	
	private File mTempFile;
	public void showImagePicker() { 
		mTempFile = getFileStreamPath("tempCropFile");
		mTempFile.getParentFile().mkdirs();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setType("image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
		intent.putExtra("outputFormat",Bitmap.CompressFormat.PNG.name());       
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		startActivityForResult(intent, RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_PICK_IMAGE_PORTRAIT);
	}

	public void setThemeAndRelaunchApp(Uri imageUri) throws FileNotFoundException {
		Bitmap bitmap = YTBitmapLoader.loadAutoScaledBitmapFromUri(this, imageUri);
		// crop이 완료된 비트맵을 로컬에 저장한 후 메인에서 보여줄 수 있게 구현
		YTBitmapLoader.saveBitmapToUri(this, imageUri, bitmap);
		//GeneralSetting.setTheme(photoTheme);
		timetable.setThemeType(YTTimetableTheme.ThemeType.Photo);

		//settingsConfigureView.onGeneralSettingChanged();
		bitmap.recycle();
		//timetable.getTheme().setRootBackground(resId);
		onThemeSettingsChanged();
		//relaunchApplication();
	}


	public void startCropActivity(Intent data, float ratio, Uri originalPortImageUri, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");

		//Uri originalImageUri = data.getData();
		MyLog.d("CropActivity", "ImageToBeCropped : " + originalPortImageUri.getPath());
		/*mImageCaptureUri = data.getData();
        File original_file = getImageFile(mImageCaptureUri);

        mImageCaptureUri = createSaveCropFile();
        File cpoy_file = new File(mImageCaptureUri.getPath()); 

        // SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
        copyFile(original_file , cpoy_file);*/
		Uri croppedImageUri = YTBitmapLoader.createPortraitCroppedImage(
				this, originalPortImageUri, timetable.getId());

		intent.setDataAndType(croppedImageUri, "image/*");

		intent.putExtra("aspectX", 100);
		intent.putExtra("aspectY", (int)(100 * ratio));
		intent.putExtra("noFaceDetection", true);	// 얼굴부분 찾아내지 않기.
		intent.putExtra("output", croppedImageUri);

		List list = getPackageManager().queryIntentActivities(intent, 0);
		intent.setData(data.getData());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);	// 이 부분이 추가되면 모토롤라 폰은 배경이 바뀐다.

		Intent i = new Intent(intent);
		ResolveInfo res = (ResolveInfo) list.get(0);
		i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		startActivityForResult(intent, requestCode);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			setResultAndFinish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		onThemeSettingsChanged();
		setResultAndFinish();
	}
	public void onThemeSettled(){
//		setResultAndFinish();
		onThemeSettingsChanged();
		setResultAndFinish();
	}
	
	public void setResultAndFinish(){
		Intent data = new Intent();
		data.putExtra("TimetablePageIndex", timetablePageIndex);
		setResult(Activity.RESULT_OK, data);
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

}
