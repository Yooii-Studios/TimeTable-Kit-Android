package com.sulga.yooiitable.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YTAlarmNotificationOnReboot extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			ArrayList<Timetable> timetables = TimetableDataManager.getTimetables();
			for(int i = 0; i < timetables.size() ; i++){
				YTAlarmManager.startTimetableAlarm(context, timetables.get(i));
				MyLog.d("YTAlarmNotificationOnReboot", "Timetable Alarm assigned after reboot");
			}
			
			HashMap<String, ArrayList<Schedule>> schedules = TimetableDataManager.getSchedules();
			for(Map.Entry<String, ArrayList<Schedule>> entry : schedules.entrySet()){
				ArrayList<Schedule> sl = entry.getValue();
				for(int i = 0; i < sl.size() ; i++){
					YTAlarmManager.startScheduleAlarm(context, sl.get(i));
					MyLog.d("YTAlarmNotificationOnReboot", "Schedule alarm assigned after reboot");
				}				
			}
		}
	}

}
