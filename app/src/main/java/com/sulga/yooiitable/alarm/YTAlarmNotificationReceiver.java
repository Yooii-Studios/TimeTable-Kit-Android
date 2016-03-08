package com.sulga.yooiitable.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.Devs;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.TimetableActivity;

public class YTAlarmNotificationReceiver extends BroadcastReceiver {
	private int alarmId = -1;
	private int alarmType = -1;

	@Override
	public void onReceive(Context context, Intent intent) {
		MyLog.d("YTAlarmNotificationReceiver", "Received!!");
		
		alarmId = intent.getIntExtra("AlarmId", -100);
		alarmType = intent.getIntExtra("AlarmType", -1);
		MyLog.d("YTAlarmNotificationReceiver", "Alarm ID : " + alarmId + ", alarmType : " + alarmType);

		if(alarmType == YTAlarmManager.YT_ALARM_TYPE_LESSON){
			//메인 액티비티를 실행
			Intent mainActivityIntent = new Intent(context, TimetableActivity.class);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mainActivityIntent.putExtra("WakeLock", true);

			String lessonName = intent.getStringExtra("LessonName");
			String lessonWhere = intent.getStringExtra("LessonWhere");

			String notification_stateBar = lessonName + "!";
			String notification_alarmTitle = context.getString(R.string.app_name);
			String notification_alarmSummary = lessonName + " at " + lessonWhere;

			// 알람 노티파이를 띄움
			startNotification(context, mainActivityIntent,
					notification_stateBar,
					notification_alarmTitle,
					notification_alarmSummary);

			// 알람 다이얼로그를 띄움
//			displayAlarmDialog(context, alarmDialogTitle, alarmDialogMessage);
		} else if(alarmType == YTAlarmManager.YT_ALARM_TYPE_SCHEDULE) {
			Intent mainActivityIntent = new Intent(context, TimetableActivity.class);

			//메인 액티비티를 실행
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mainActivityIntent.putExtra("WakeLock", true);
			context.startActivity(mainActivityIntent);	

			String scheduleName = intent.getStringExtra("ScheduleName");
			String scheduleMemo = intent.getStringExtra("ScheduleMemo");

			String app_name = context.getResources().getString(R.string.app_name);
			
			String notification_stateBar = scheduleName + "!";

			// 알람 노티파이를 띄움
			startNotification(context, mainActivityIntent, notification_stateBar, app_name,
					scheduleName);

			// 알람 다이얼로그를 띄움
			String alarmDialogMessage = scheduleName + "\n" + scheduleMemo;
			displayAlarmDialog(context, app_name, alarmDialogMessage);
		}
	}

	private void startNotification(Context context, 
			Intent mainActivityIntent, 
			String notification_stateBar,
			String notification_alarmTitle,
			String notification_alarmSummary){
		NotificationManager notifier = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

        String notifyStr = notification_stateBar;

		//noinspection ConstantConditions
		notifyStr = Devs.isDevMode ? notifyStr += ", AlarmId : " + alarmId : notifyStr;

		// 수정: 새 API 를 사용해 노티피케이션 로직 변경
		Notification notify = new Notification(R.drawable.ic_launcher_f3, 
				notifyStr,
				System.currentTimeMillis()
				);

            //이 인텐트는 노티피케이션을 클릭하면 실행되는 액티비티의 인텐트.
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);

		notify.setLatestEventInfo(context, 
				notification_alarmTitle, 
				notification_alarmSummary,
				pendingIntent);

		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notify.vibrate = new long[] { 200, 200, 500, 300 };
		// notify.sound=Uri.parse("file:/");
		notify.number++;

		notifier.notify(1, notify);
	}

	private void displayAlarmDialog(Context context, String dialogTitle, String dialogMessage) {
		Intent alarmDialogIntent = new Intent("android.intent.action.MAIN");

		alarmDialogIntent.setClass(context, YTAlarmDialogPopUpActivity.class);
		alarmDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        alarmDialogIntent.putExtra("AlarmType", alarmType);
		alarmDialogIntent.putExtra("AlarmId", alarmId);
		alarmDialogIntent.putExtra("Title", dialogTitle);
		alarmDialogIntent.putExtra("Message", dialogMessage);

		// Pass on the alarm ID as extra data
		// alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));

		// Start the popup activity
		context.startActivity(alarmDialogIntent);        
	}
}
