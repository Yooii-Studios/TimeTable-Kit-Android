package com.sulga.yooiitable.timetablesetting;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.YTAlarmManager;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.Timetable.ColumnTypes;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs.SettingDayDialogCreator;
import com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs.SettingPeriodDialogCreator;
import com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs.TimeIntervalPickerDialogBuilder;
import com.sulga.yooiitable.timetablesetting.utils.TimetableSettingStringManager;
import com.sulga.yooiitable.utils.AlertDialogCreator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimetableSettingFragment extends Fragment {
	public static String BUNDLE_PAGE_INDEX_KEY = "TimetablePageIndex";
	private static final String TAG = "TimetableSettingFragment";
	//Context context;
	public static final int COLUMNNUM_USER_CUSTOM = -1;

	private int timetablePageIndex;	//you MUST set getActivity()
	private Timetable timetable;

	LinearLayout settingsDayWrapper;
	LinearLayout settingsPeriodWrapper;
	LinearLayout settingsStartTimeWrapper;
    LinearLayout settingsTimeIntervalWrapper;
	TextView settingsDayText;
	TextView settingsPeriodText;
	TextView settingsStartTimeText;
    TextView settingsTimeIntervalText;

	private final static int[] startDays = new int[]{
		GregorianCalendar.SUNDAY,
		GregorianCalendar.MONDAY,
        GregorianCalendar.TUESDAY
	};
	private static String[] startDayStrings;

	private final static int[] endDays = new int[]{
        GregorianCalendar.THURSDAY,
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

	int tt_startDay;
	int tt_endDay;
	Timetable.ColumnTypes tt_columnType;
	int tt_columnNum;
	int tt_startHour;
    int tt_startMin;
	int tt_timeOffset;
	//boolean tt_isLessonAlarmExist;

	private boolean showTimeSettingDialog = false;
    private boolean showDaySettingDialog = false;
	Resources res;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //setContentView(R.layout.view_timetable_option_settings_iconstyle);
		View contentView = inflater.inflate(
						R.layout.view_timetable_option_settings_iconstyle,
						container,
						false);
        timetablePageIndex = getArguments().getInt(BUNDLE_PAGE_INDEX_KEY, -1);
        showTimeSettingDialog = getArguments().getBoolean("ShowTimeSettingDialog", false);
        showDaySettingDialog = getArguments().getBoolean("ShowDaySettingDialog", false);
        timetable = TimetableDataManager
                .getInstance()
                .getTimetableAtPage(timetablePageIndex);

        settingsDayWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_day_wrapper);
        settingsPeriodWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_period_wrapper);
        settingsStartTimeWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_starttime_wrapper);
        settingsTimeIntervalWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_timeinterval_wrapper);
        settingsDayText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_day_text);
        settingsPeriodText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_period_text);
        settingsStartTimeText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_starttime_text);
        settingsTimeIntervalText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_timeinterval_text);

        res = getResources();

        startDayStrings = res.getStringArray(R.array.timetable_setting_startdays);
        endDayStrings = res.getStringArray(R.array.timetable_setting_enddays);
        columnTypeStrings = res.getStringArray(R.array.timetable_setting_columnTypes);
        columnNumStrings = res.getStringArray(R.array.timetable_setting_columnNums);
        startTimeStrings = res.getStringArray(R.array.timetable_setting_startTimes);
        timeOffsetStrings = res.getStringArray(R.array.timetable_setting_timeOffsets);

        tt_startDay = timetable.getStartDay();
        tt_endDay = timetable.getEndDay();
        tt_columnType = timetable.getColumnType();
        tt_columnNum = timetable.getPeriodNum();
        tt_startHour = timetable.getStartTime();
        tt_startMin = timetable.getStartMin();
        tt_timeOffset = timetable.getPeriodUnit();

        MyLog.d("TimetableSettingFragment", timetable.getLessonList().toString());

        initSettingsText();

        settingsDayWrapper.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int def_startDayIdx = TimetableSettingStringManager.
                        getIntegerItemIndexOfArray(tt_startDay, startDays);
                final int def_endDayIdx = TimetableSettingStringManager.
                        getIntegerItemIndexOfArray(tt_endDay, endDays);

                SettingDayDialogCreator.createDialog(getActivity(),
                        startDayStrings, startDays, def_startDayIdx,
                        endDayStrings, endDays, def_endDayIdx,
                        onSettingDayEndListener).show();
            }
        });

        settingsPeriodWrapper.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                SettingPeriodDialogCreator.createDialog(getActivity(),
                        timetable, maxPeriodNum,
                        columnTypeStrings, columnTypes, defColumnTypeIdx,
                        tt_columnNum,
                        onSettingsPeriodEndListener).show();


            }
        });

        settingsStartTimeWrapper.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, tt_startHour);
                c.set(Calendar.MINUTE, tt_startMin);

                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(),
                        new OnStartTimeSetListener(tt_startHour, tt_startMin),
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                timeDialog.show();
            }
        });

        settingsTimeIntervalWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getResources().getString(R.string.timetable_setting_select_timeoffset);
                Dialog dialog = TimeIntervalPickerDialogBuilder.build(getActivity(),
                        title,
                        tt_timeOffset,
                        tt_columnNum,
                        new TimeIntervalPickerDialogBuilder.OnTimeIntervalPickedListener() {
                            @Override
                            public void onTimeIntervalPicked(int pickedNumber) {
                                String warnTitle =
                                        TimetableSettingFragment.this.getActivity()
                                                .getResources().getString(R.string.notice);
                                String warnMessage =
                                        TimetableSettingFragment.this.getActivity()
                                                .getResources().getString(R.string.timetable_setting_warning_tableinit_on_starttimechange);
                                AlertDialogCreator.getClearTimetableAlertDialog(
                                        warnTitle,
                                        warnMessage,
                                        TimetableSettingFragment.this.getActivity(),
                                        new OnTimeIntervalChangedAlertDialogOKOnClick(pickedNumber),
                                        new OnStartTimeChangedAlertDialogCancelOnClick()
                                ).show();
                            }
                        }
                );
                dialog.show();
            }
        });

        if(showDaySettingDialog) {
            showDaySettingDialogOnStart();
        }
        return contentView;
    }

    private class OnStartTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private int currEndHour, currEndMin = 0;
        public OnStartTimeSetListener(int currEndHour, int currEndMin){
            this.currEndHour = currEndHour;
            this.currEndMin = currEndMin;
        }
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String warnTitle =
                    TimetableSettingFragment.this.getActivity()
                            .getResources().getString(R.string.notice);
            String warnMessage =
                    TimetableSettingFragment.this.getActivity()
                            .getResources().getString(R.string.timetable_setting_warning_tableinit_on_starttimechange);
            AlertDialogCreator.getClearTimetableAlertDialog(
                    warnTitle,
                    warnMessage,
                    TimetableSettingFragment.this.getActivity(),
                    new OnStartTimeChangedAlertDialogOKOnClick(hourOfDay, minute),
                    new OnStartTimeChangedAlertDialogCancelOnClick()
            ).show();
        }
    }

    private class OnStartTimeChangedAlertDialogOKOnClick implements DialogInterface.OnClickListener{
        int hourOfDay, minute;
        public OnStartTimeChangedAlertDialogOKOnClick(int hourOfDay, int minute){
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            tt_startHour = hourOfDay;
            tt_startMin = minute;

            SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
            GregorianCalendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            String time = sdf.format(c.getTime());
            clearTimetable = true;
            saveOption();
            settingsStartTimeText.setText(time);
            dialog.dismiss();
        }
    }
    private class OnStartTimeChangedAlertDialogCancelOnClick implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private class OnTimeIntervalChangedAlertDialogOKOnClick implements  DialogInterface.OnClickListener{
        int intervalMin;
        public OnTimeIntervalChangedAlertDialogOKOnClick(int intervalMin){
            this.intervalMin = intervalMin;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String min = getResources().getString(R.string.minute_short);
            tt_timeOffset = intervalMin;
            clearTimetable = true;
            saveOption();
            String resultText = tt_timeOffset < 10 ?
                    "0" + Integer.toString(tt_timeOffset) + " " + min :
                    Integer.toString(tt_timeOffset) + " " + min;
            settingsTimeIntervalText.setText(resultText);
            dialog.dismiss();
        }
    }

    private void showDaySettingDialogOnStart(){
        int def_startDayIdx = TimetableSettingStringManager.
                getIntegerItemIndexOfArray(tt_startDay, startDays);
        int def_endDayIdx = TimetableSettingStringManager.
                getIntegerItemIndexOfArray(tt_endDay, endDays);

        SettingDayDialogCreator.createDialog(getActivity(),
                startDayStrings, startDays, def_startDayIdx,
                endDayStrings, endDays, def_endDayIdx,
                onSettingDayEndListener).show();
    }
	
	private void initSettingsText(){

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
        //Start Time Text
        SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
        GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, tt_startHour);
        c.set(Calendar.MINUTE, tt_startMin);
        String timeStr = sdf.format(c.getTime());

		settingsStartTimeText.setText(timeStr);

        //Time Interval Text
        String min = getResources().getString(R.string.minute_short);
        saveOption();
        String resultText = tt_timeOffset < 10 ?
                "0" + Integer.toString(tt_timeOffset) + " " + min :
                Integer.toString(tt_timeOffset) + " " + min;
        settingsTimeIntervalText.setText(resultText);
	}
	public boolean saveOption(){
        if(clearTimetable == true){
            YTAlarmManager.cancelTimetableAlarm(getActivity(), timetable);
            for(Lesson l : timetable.getLessonList()){
                timetable.onRemoveLesson(l);
            }
            timetable.getLessonList().clear();
            clearTimetable = false;
//			SettingTimeDialogCreator.willClearTimetable = false;
        }
		timetable.setStartDay(tt_startDay);
		timetable.setEndDay(tt_endDay);
		timetable.setColumnType(tt_columnType);
		timetable.setPeriodNum(tt_columnNum);
		timetable.setStartHour(tt_startHour);
        timetable.setStartMin(tt_startMin);
		timetable.setPeriodUnit(tt_timeOffset);

		TimetableDataManager.writeDatasToExternalStorage();
		return true;
	}

	SettingDayDialogCreator.OnSettingDayEndListener onSettingDayEndListener = new SettingDayDialogCreator.OnSettingDayEndListener() {

		@Override
		public void onSettingsEnd(int startDay, int endDay, String startString, String endString) {
			tt_startDay = startDay;
			tt_endDay = endDay;
			
			saveOption();
			for(int i = timetable.getLessonList().size() - 1; i >= 0 ; i--){
				Lesson l = timetable.getLessonList().get(i);
				if(!timetable.doesTimetableIncludesGregorianDay(l.getDay())){
					//if lesson overflows timetable day size
					YTAlarmManager.cancelLessonAlarm(getActivity(), l);
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
}
