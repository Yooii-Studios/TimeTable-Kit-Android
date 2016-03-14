package com.sulga.yooiitable.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.mylog.MyLog;

import java.util.ArrayList;
import java.util.Calendar;


public class YTAlarmManager {
	public static final int YT_ALARM_NOT_REGISTERED = -1;
	//알람 타입 식별
	public static final int YT_ALARM_TYPE_LESSON = 0;
	public static final int YT_ALARM_TYPE_SCHEDULE = 1;

	public static void startTimetableAlarm(Context context, Timetable timetable){
		if(timetable.getLessonAlarmTime() == Timetable.LESSON_ALARM_NONE){
			return;
		}
		ArrayList<Lesson> lessonList = timetable.getLessonList();
		int lessonAlarmBeforeByMin = timetable.getLessonAlarmTime();
		for(int i = 0; i < lessonList.size() ; i++){
			Lesson l = lessonList.get(i);
			startLessonAlarm(context, l, lessonAlarmBeforeByMin);
		}
	}
	
	public static void cancelTimetableAlarm(Context context, Timetable timetable){
		ArrayList<Lesson> lessonList = timetable.getLessonList();
		for(int i = 0; i < lessonList.size() ; i++){
			Lesson l = lessonList.get(i);
			cancelLessonAlarm(context, l);
		}
	}

	public static void startLessonAlarm(Context context, 
			Lesson lesson, int lessonAlarmBeforeByMin){		
		int alarmId = lesson.getAlarmId();
		if(alarmId == YTAlarmManager.YT_ALARM_NOT_REGISTERED){
			lesson.setAlarmId(getNewAlarmId(context));
			alarmId = lesson.getAlarmId();
		}

		MyLog.d("YTAlarmManager", "Lesson Alarm Id : " + lesson.getAlarmId());
		MyLog.d("YTAlarmManager", "Lesson start Hour : " + lesson.getLessonInfo().getStartHour());

		Calendar calNow = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		//calendar.setTimeInMillis(System.currentTimeMillis());
		//calendar.set(GregorianCalendar.MONTH, lesson.get)
		calendar.set(Calendar.DAY_OF_WEEK, lesson.getLessonInfo().getDay());
		calendar.set(Calendar.HOUR_OF_DAY, lesson.getLessonInfo().getStartHour());
		calendar.set(Calendar.MINUTE, lesson.getLessonInfo().getStartMin());
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.MINUTE, lessonAlarmBeforeByMin * -1);
		if(lesson.shouldAlarmNextDay()){
			MyLog.d("YTAlarmManager", "Lesson Alarm Should work next day.");
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}

		MyLog.d("YTAlarmManager", "Now : " + calNow.getTime().toString() 
				+ "\nLesson At : " +calendar.getTime().toString());

		if(calendar.compareTo(calNow) <= 0){
			//Today Set time passed, count to next week
			MyLog.d("YTAlarmManager", "This Week Lesson already passed");
			calendar.add(Calendar.DAY_OF_MONTH, 7);
		}

		Intent intent = new Intent(context, YTAlarmNotificationReceiver.class);
		intent.putExtra("AlarmId", alarmId);
		intent.putExtra("AlarmType", YT_ALARM_TYPE_LESSON);
		intent.putExtra("LessonName", lesson.getLessonName());
		intent.putExtra("LessonWhere", lesson.getLessonWhere());
		intent.putExtra("Professor", lesson.getProfessor());

		//intent.setAction(NotificationReceiver.);
		PendingIntent sender 
		= PendingIntent.getBroadcast(
				context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager manager 
		= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		//long triggerTime = SystemClock.elapsedRealtime() + 1000*60;
		//MyLog.e("registerAlarm", calendar.getTimeInMillis() + " : timeInMillis");

		//manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		manager.setRepeating(
				AlarmManager.RTC_WAKEUP, 
				calendar.getTimeInMillis(), 
				1000 * 60 * 60 * 24 * 7, 
				sender);
		MyLog.d("YTAlarmManager", calendar.getTimeInMillis() + ", " + calendar.getTime().toString());

	}

	public static void cancelLessonAlarm(Context context, Lesson lesson){
		int alarmId = lesson.getAlarmId();
		if(alarmId == YTAlarmManager.YT_ALARM_NOT_REGISTERED){
			//lesson.setAlarmId(getNewAlarmId(context));
			MyLog.d("YTAlarmManager", "Lesson Alarm cancel failed, id not registered!");
			return;
		}
		MyLog.d("YTAlarmManager", "Lesson Alarm Id : " + lesson.getAlarmId() + " Canceled!");


		Intent intent = new Intent(context, YTAlarmNotificationReceiver.class);
		intent.putExtra("AlarmId", alarmId);
		intent.putExtra("AlarmType", YT_ALARM_TYPE_LESSON);
		intent.putExtra("LessonName", lesson.getLessonName());
		intent.putExtra("LessonWhere", lesson.getLessonWhere());
		intent.putExtra("Professor", lesson.getProfessor());

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);
	}

	public static void startScheduleAlarm(Context context, Schedule schedule){
		if(schedule.getScheduleAlarm() == Schedule.SCHEDULE_ALARM_NONE){
			cancelScheduleAlarm(context, schedule);
			return;
		}
		//스케쥴에서 YTAlarm 객체를 받아와 id를 가져오고, 스케쥴에서 정보를 읽어와 캘린더를 설정
		Calendar calNow = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		//calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.set(
				schedule.getScheduleYear(), 
				schedule.getScheduleMonth(), 
				schedule.getScheduleDay(), 
				schedule.getScheduleHour(), 
				schedule.getScheduleMin(), 
				0);
		calendar.add(Calendar.MINUTE, schedule.getScheduleAlarm() * -1);

		if(calendar.compareTo(calNow) <= 0){
			//Today Set time passed, count to next week
			MyLog.d("YTAlarmManager", "Schedule time already passed! Alarm not registering!");
			return;
		}

		int alarmId = schedule.getAlarmId();
		if(alarmId == YTAlarmManager.YT_ALARM_NOT_REGISTERED){
			schedule.setAlarmId(getNewAlarmId(context));
			alarmId = schedule.getAlarmId();
			//TimetableDataManager.getInstance().writeDatasToExternalStorage();
		}
		MyLog.d("YTAlarmManager", "Schedule Alarm Id : " + schedule.getAlarmId());
		MyLog.d("YTAlarmManager", "Schedule Alarm At : " + calendar.getTime().toString());

		Intent intent = new Intent(context, YTAlarmNotificationReceiver.class);
		intent.putExtra("AlarmId", alarmId);
		intent.putExtra("AlarmType", YT_ALARM_TYPE_SCHEDULE);
		intent.putExtra("ScheduleName", schedule.getScheduleName());
		intent.putExtra("ScheduleMemo", schedule.getScheduleMemo());

		//intent.setAction(NotificationReceiver.);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	public static void cancelScheduleAlarm(Context context, Schedule schedule){
		int alarmId = schedule.getAlarmId();
		if(alarmId == YTAlarmManager.YT_ALARM_NOT_REGISTERED){
			//schedule.setAlarmId(getNewAlarmId(context));
			MyLog.d("YTAlarmManager", "Schedule Alarm cancel failed, id not registered!");
			return;
			//TimetableDataManager.getInstance().writeDatasToExternalStorage();
		}
		MyLog.d("YTAlarmManager", "Schedule Alarm Id : " + schedule.getAlarmId() + " Canceled");

		Intent intent = new Intent(context, YTAlarmNotificationReceiver.class);
		intent.putExtra("AlarmId", alarmId);
		intent.putExtra("AlarmType", YT_ALARM_TYPE_SCHEDULE);
		intent.putExtra("ScheduleName", schedule.getScheduleName());
		intent.putExtra("ScheduleMemo", schedule.getScheduleMemo());

		PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(sender);
	}

	//private static int alarmIdInHistory = 0;
	public static int getNewAlarmId(Context context){
		int latestAlarmId = readAlarmIdInHistoryFromPref(context);
		//int latestAlarmIdToIncrement = latestAlarmId + 1;
		//latestAlarmId++;
		writeAlarmIdInHistoryToPref(latestAlarmId + 1, context);
		return latestAlarmId;
	}

	private static final String YT_ALARM_HISTORY_FILE_NAME = "YTAlarmIdHistory";
	// 값 저장하기
	private static void writeAlarmIdInHistoryToPref(int latestAlarmId, Context context){
		SharedPreferences pref = context.getSharedPreferences(
				YT_ALARM_HISTORY_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("LatestAlarmId", latestAlarmId);
		editor.apply();
	}

	private static int readAlarmIdInHistoryFromPref(Context context){
		SharedPreferences pref = context.getSharedPreferences(
				YT_ALARM_HISTORY_FILE_NAME, Context.MODE_PRIVATE);
		int result = pref.getInt("LatestAlarmId", 0);
		MyLog.d("YTAlarmManager", "readAlarmId : " + result);
		return result;
	}
}
