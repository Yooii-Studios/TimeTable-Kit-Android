package com.sulga.yooiitable.timetablesetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.YTAlarmManager;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.language.YTLanguage;
import com.sulga.yooiitable.language.YTLanguageType;
import com.sulga.yooiitable.theme.YTTimetableTheme;
import com.sulga.yooiitable.timetable.SettingLanguageDialogCreator;
import com.sulga.yooiitable.timetableinfo.TimetableSettingInfoActivity;
import com.sulga.yooiitable.timetablesetting.utils.TimetableSettingStringManager;
import com.yooiistudios.common.ad.AdUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fldldi0212 on 15. 1. 20..
 *
 * TimetableSettingsAlarmFragment
 */
public class TimetableSettingsAlarmFragment extends Fragment {
    public static String BUNDLE_PAGE_INDEX_KEY = "TimetablePageIndex";

    private int timetablePageIndex;	//you MUST set getActivity()
    private Timetable timetable;

    LinearLayout settingsAlarmWrapper;
    TextView settingsAlarmPromptText;
    TextView settingsAlarmText;

    LinearLayout pickThemeWrapper;
    TextView pickThemePromptText;
    TextView pickThemeText;
    int pickThemeClickedItemPosition;

    LinearLayout pickLanguageWrapper;
    TextView pickLanguagePromptText;
    TextView pickLanguageText;

    private static String[] alarmTimeStrings;
    private static final int[] alarmTimes = {
            Timetable.LESSON_ALARM_NONE, 0, 1,
            5, 10, 20,
            30, 40, 50,
            60, 120, 180
    };
    private static String[] themeStrings;

    int tt_lessonAlarmTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.view_timetable_option_settings_alarm_iconstyle,
                container, false);
        timetablePageIndex = getArguments().getInt(BUNDLE_PAGE_INDEX_KEY, -1);
        timetable = TimetableDataManager.getInstance().getTimetableAtPage(timetablePageIndex);

        settingsAlarmWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_alarm_wrapper);
        settingsAlarmPromptText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_alarm_prompt);
        settingsAlarmText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_alarm_text);

        pickThemeWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_theme_wrapper);
        pickThemePromptText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_theme_prompt);
        pickThemeText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_theme_text);

        pickLanguageWrapper = (LinearLayout)
                contentView.findViewById(R.id.view_timetable_settings_language_wrapper);
        pickLanguagePromptText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_language_prompt);
        pickLanguageText = (TextView)
                contentView.findViewById(R.id.view_timetable_settings_language_text);

        themeStrings = getResources().getStringArray(R.array.timetable_setting_theme_themes);
        alarmTimeStrings = getResources().getStringArray(R.array.timetable_setting_lessonAlarms);
        tt_lessonAlarmTime = timetable.getLessonAlarmTime();

        initSettingsText();

        setupPickThemeWrapper();
        setupSettingsAlarmWrapper();
        setupLanguageWrapper();

        return contentView;
    }

    void initSettingsText() {
        //Theme
        int def_themeTypeIdx =
                TimetableSettingStringManager.
                        getThemeTypeItemIndexOfArray(timetable.getThemeType(), YTTimetableTheme.ThemeType.values());
        pickThemeText.setText(themeStrings[def_themeTypeIdx]);
        pickThemePromptText.setText(R.string.activity_setting_viewpager_themeprompt);
        //Alarm
        int def_alarmIdx = TimetableSettingStringManager.
                getIntegerItemIndexOfArray(tt_lessonAlarmTime, alarmTimes);
        settingsAlarmText.setText(alarmTimeStrings[def_alarmIdx]);
        settingsAlarmPromptText.setText(R.string.activity_setting_viewpager_alarmprompt);

        YTLanguageType currentLanguageType = YTLanguage.getCurrentLanguageType(getActivity());
        String languageStr = YTLanguageType.toTranselatedString(currentLanguageType.getIndex(), getActivity());
        pickLanguageText.setText(languageStr);
        pickLanguagePromptText.setText(R.string.activity_setting_viewpager_languageprompt);
    }

    void setupPickThemeWrapper(){
        pickThemeWrapper.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SelectOptionDialogCreator.showThemeUnlockListDialog(
                        getActivity(),
                        getResources().getString(R.string.timetable_setting_theme_select_theme_title),
                        themeStrings,
                        YTTimetableTheme.getThemeTypeValues(),
                        new SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener() {

                            @Override
                            public void onClick(int clickedItemPosition) {
                                YTTimetableTheme.ThemeType[] themes =
                                        YTTimetableTheme.getThemeTypeValues();
                                YTTimetableTheme.ThemeType clickedTheme =
                                        themes[clickedItemPosition];
                                pickThemeClickedItemPosition = clickedItemPosition;

                                if(!TimetableDataManager.getCurrentFullVersionState(getActivity())){
                                    for(int i = 0; i < YTTimetableTheme.lockedThemes.length ; i++){
                                        YTTimetableTheme.ThemeType locked = YTTimetableTheme.lockedThemes[i];
                                        if(clickedTheme == locked){
                                            AdUtils.showInHouseStoreAd(getActivity());
                                            return;
                                        }
                                    }
                                }

                                if (clickedTheme != YTTimetableTheme.ThemeType.Photo) {
                                    timetable.setThemeType(themes[clickedItemPosition]);
                                    ((TimetableSettingInfoActivity) getActivity()).onThemeSettled();
                                } else {
                                    ((TimetableSettingInfoActivity) getActivity()).showImagePicker();
                                }
                                pickThemeText.setText(themeStrings[clickedItemPosition]);

                                Map<String, String> info = new HashMap<>();
                                info.put(FlurryConstants.THEME_INFO_KEY, clickedTheme.toString());
                                FlurryAgent.logEvent(FlurryConstants.THEME_SELECTED, info);
                            }
                        });

            }
        });
    }

    void setupSettingsAlarmWrapper()
    {
        settingsAlarmWrapper.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SelectOptionDialogCreator.showListDialog(
                        getActivity(),
                        getResources().getString(R.string.timetable_setting_select_lessonAlarm_title),
                        alarmTimeStrings,
                        new SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener() {
                            @Override
                            public void onClick(int clickedItemPosition) {
                                tt_lessonAlarmTime = alarmTimes[clickedItemPosition];
                                String str;
                                String alarmCancelled = getActivity().getResources()
                                        .getString(R.string.timetable_setting_alarm_cancelled);
                                String alarmSettledOnTime = getActivity().getResources()
                                        .getString(R.string.timetable_setting_alarm_on_time);
                                String alarmBeforeA = getActivity().getResources()
                                        .getString(R.string.timetable_setting_alarm_before_A);
                                String alarmBeforeB = getActivity().getResources()
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

                                Map<String, String> alarmInfo = new HashMap<>();
                                String alarmTypeStr = tt_lessonAlarmTime == Timetable.LESSON_ALARM_NONE ?
                                        "None" : Integer.toString(tt_lessonAlarmTime);
                                alarmInfo.put(FlurryConstants.ALARM_INFO_TIME, alarmTypeStr);
                                alarmInfo.put(FlurryConstants.ALARM_TYPE_KEY, FlurryConstants.ALARM_TYPE_TIMETABLE);
                                FlurryAgent.logEvent(FlurryConstants.ALARM_USED, alarmInfo);

                                saveOption();
                                saveAlarmOption();

                                settingsAlarmText.setText(alarmTimeStrings[clickedItemPosition]);
                                Toast.makeText(
                                        getActivity(),
                                        str,
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                        });
            }
        });
    }

    void setupLanguageWrapper(){
        final String langTitle = getString(R.string.activity_setting_viewpager_languageprompt);
        pickLanguageWrapper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SettingLanguageDialogCreator.showSettingsLanguageListDialog(
                        getActivity(),
                        langTitle,
                        new SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener() {

                            @Override
                            public void onClick(int clickedItemPosition) {
                                onLanguageChanged(clickedItemPosition);
                                ((TimetableSettingInfoActivity)getActivity()).onLanguageChanged();
                            }
                        });
            }
        });

    }

    public void onLanguageChanged(int position) {
        YTLanguage.setLanguageType(YTLanguageType.valueOf(position), getActivity());
    }

    public boolean saveOption() {
        if (tt_lessonAlarmTime == Timetable.LESSON_ALARM_NONE) {
            YTAlarmManager.cancelTimetableAlarm(getActivity(), timetable);
        }
        timetable.setLessonAlarmTime(tt_lessonAlarmTime);
        return true;
    }

    private void saveAlarmOption(){
        if (timetablePageIndex != -1) {
            Timetable timetable = TimetableDataManager.getInstance().getTimetableAtPage(timetablePageIndex);
            if (timetable.getLessonAlarmTime() != Timetable.LESSON_ALARM_NONE) {
                YTAlarmManager.cancelTimetableAlarm(getActivity(), timetable);
                YTAlarmManager.startTimetableAlarm(getActivity(), timetable);
            } else if (timetable.getLessonAlarmTime() == Timetable.LESSON_ALARM_NONE) {
                YTAlarmManager.cancelTimetableAlarm(getActivity(), timetable);
            }
        }

    }
}
