package com.sulga.yooiitable.alarm;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class YTAlarmDialogPopUpActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    private int m_alarmId;
    private int m_alarmType;
    private String m_alarmTitle;
    private String m_alarmMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the alarm ID from the intent extra data
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.empty);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        getWindow().getAttributes().width = 1;
        getWindow().getAttributes().height = 1;
        getWindow().getAttributes().gravity = Gravity.CENTER;

//
        if (extras != null) {
            m_alarmId = extras.getInt("AlarmId", -1);
            m_alarmType = extras.getInt("AlarmType", -1);
            m_alarmTitle = extras.getString("Title");
            m_alarmMessage = extras.getString("Message");

            if(m_alarmType == YTAlarmManager.YT_ALARM_TYPE_SCHEDULE) {
                Schedule s = getScheduleFromAlarmId(m_alarmId);
                MyLog.d("Schedule popup", "Schedule name : " + s.getAlarmId());
                try {
                    SKAlarmSoundPlayer.playAlarmSound(s.getAlarmSound(), 80, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dialog = createAlarmDialog();
            dialog.show();
        } else {
            m_alarmId = -1;
        }
    }

    AlertDialog createAlarmDialog() {
        MyLog.d("YTAlarmDialogPopupActivity", "showAlramDialog called");
        return new AlertDialog.Builder(this)
                .setTitle(m_alarmTitle)
                .setMessage(m_alarmMessage)
                .setCancelable(false)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SKAlarmSoundPlayer.stop(YTAlarmDialogPopUpActivity.this);
                        YTAlarmDialogPopUpActivity.this.finish();
                    }
                })
                .create();
    }

    private AlertDialog dialog;
    @Override
    public void onPause()
    {
        super.onPause();

//        SKAlarmSoundPlayer.stop(YTAlarmDialogPopUpActivity.this);
//        if(dialog != null && dialog.isShowing())
//        {
//            dialog.dismiss();
//        }
//        finish();
    }

    public void onDestroy()
    {
        super.onDestroy();
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
            SKAlarmSoundPlayer.stop(YTAlarmDialogPopUpActivity.this);
        }
    }

    private Schedule getScheduleFromAlarmId(int alarmId) {
        Calendar cal = Calendar.getInstance();
        String todayKey = TimetableDataManager.makeKeyFromDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        HashMap<String, ArrayList<Schedule>> scheduleMap = TimetableDataManager.getSchedules();
        ArrayList<Schedule> schedules = scheduleMap.get(todayKey);
        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = schedules.get(i);
            if (s.getAlarmId() == alarmId)
                return s;
        }
        return null;
    }
}