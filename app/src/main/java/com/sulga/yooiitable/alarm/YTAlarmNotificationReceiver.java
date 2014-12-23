package com.sulga.yooiitable.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.TimetableActivity;

public class YTAlarmNotificationReceiver extends BroadcastReceiver {

	//private String notification_stateBar = "Statebar Text";
	//private String notification_alarmTitle = "Alarm Title";
	//private String notification_alarmSummary = "Alram Summary";

	private int alarmId = -1;
	private int alarmType = -1;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();
		MyLog.d("YTAlarmNotificationReceiver", "Received!!");
		
		//alarmId = intent.getIntExtra("AlarmId", -1);
		alarmId = intent.getIntExtra("AlarmId", -100);
		alarmType = intent.getIntExtra("AlarmType", -1);
		MyLog.d("YTAlarmNotificationReceiver", "Alarm ID : " + alarmId + ", alarmType : " + alarmType);

		if(alarmType == YTAlarmManager.YT_ALARM_TYPE_LESSON){
			Intent mainActivityIntent = new Intent(context, TimetableActivity.class);
			//메인 액티비티를 실행한다.
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.startActivity(mainActivityIntent);	

			String lessonName = intent.getStringExtra("LessonName");
			String lessonWhere = intent.getStringExtra("LessonWhere");
			String professor = intent.getStringExtra("Professor");

			String notification_stateBar = lessonName + "!";
			String title = context.getString(R.string.app_name);
			String notification_alarmTitle = title;
			String notification_alarmSummary = lessonName 
					+ " at " 
					+ lessonWhere;
			//알람 노티파이를 띄운다.
			startNotification(context, mainActivityIntent,
					notification_stateBar, 
					notification_alarmTitle, 
					notification_alarmSummary);
			String app_name = context.getResources().getString(R.string.app_name);
			String alarmDialogTitle = app_name;
			String alarmDialogMessage = lessonName + "\n" + lessonWhere + "\n" + professor;
			//알람 다이얼로그를 띄운다.
			displayAlarmDialog(context, alarmDialogTitle, alarmDialogMessage);
		}else if(alarmType == YTAlarmManager.YT_ALARM_TYPE_SCHEDULE){
			Intent mainActivityIntent = new Intent(context, TimetableActivity.class);
			//메인 액티비티를 실행한다.
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.startActivity(mainActivityIntent);	

			String scheduleName = intent.getStringExtra("ScheduleName");
			String scheduleMemo = intent.getStringExtra("ScheduleMemo");
			//String professor = intent.getStringExtra("Professor");
			
			String app_name = context.getResources().getString(R.string.app_name);
			
			String notification_stateBar = scheduleName + "!";
			String notification_alarmTitle = app_name;
			String notification_alarmSummary = scheduleName; 
					
			//알람 노티파이를 띄운다.
			startNotification(context, mainActivityIntent,
					notification_stateBar, 
					notification_alarmTitle, 
					notification_alarmSummary);

			String alarmDialogTitle = app_name;
			String alarmDialogMessage = scheduleName + "\n" + scheduleMemo;
			//알람 다이얼로그를 띄운다.
			displayAlarmDialog(context, alarmDialogTitle, alarmDialogMessage);
		}


		//displayAlert(context);
	}

	private void startNotification(Context context, 
			Intent mainActivityIntent, 
			String notification_stateBar,
			String notification_alarmTitle,
			String notification_alarmSummary){
		NotificationManager notifier = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notify = new Notification(R.drawable.ic_launcher_f3, 
				notification_stateBar + ", AlarmId : " + alarmId,
				System.currentTimeMillis()
				);

		//이 인텐트는 노티피케이션을 클릭하면 실행되는 액티비티의 인텐트.
		mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pender = PendingIntent
				.getActivity(context, 0, mainActivityIntent, 0);

		notify.setLatestEventInfo(context, 
				notification_alarmTitle, 
				notification_alarmSummary,
				pender);

		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notify.vibrate = new long[] { 200, 200, 500, 300 };
		// notify.sound=Uri.parse("file:/");
		notify.number++;

		notifier.notify(1, notify);
	}

	private void displayAlarmDialog(Context context, 
			String dialogTitle,
			String dialogMessage)
	{
		Intent alarmDialogIntent = new Intent("android.intent.action.MAIN");

		alarmDialogIntent.setClass(context, YTAlarmDialogPopUpActivity.class);
		alarmDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		alarmDialogIntent.putExtra("AlarmId", alarmId);
		alarmDialogIntent.putExtra("Title", dialogTitle);
		alarmDialogIntent.putExtra("Message", dialogMessage);
		// Pass on the alarm ID as extra data
		//alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));

		// Start the popup activity
		context.startActivity(alarmDialogIntent);        
	}
}
