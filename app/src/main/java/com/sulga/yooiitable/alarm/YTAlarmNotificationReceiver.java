package com.sulga.yooiitable.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
            mainActivityIntent.putExtra("WakeLock", true);

			String lessonName = intent.getStringExtra("LessonName");
			String lessonWhere = intent.getStringExtra("LessonWhere");

			String stateBar = lessonName + "!";
			String alarmTitle = context.getString(R.string.app_name);
			String alarmSummary = lessonName + " at " + lessonWhere;

			// 알람 노티파이를 띄움
			startNotification(context, mainActivityIntent, stateBar, alarmTitle, alarmSummary);
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

	private void startNotification(Context context, Intent intent, String stateBar,
								   String alarmTitle, String alarmSummary) {
		//noinspection ConstantConditions
		String tickerString = Devs.isDevMode ? stateBar += ", AlarmId : " + alarmId : stateBar;

		// 이 인텐트는 노티피케이션을 클릭하면 실행되는 액티비티의 인텐트
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

		// 수정: 새 API 를 사용해 노티피케이션 로직 변경
		/*
		Notification notify = new Notification(R.drawable.ic_launcher_f3,
				tickerString,
				System.currentTimeMillis()
				);
		*/

		/*
		notify.setLatestEventInfo(context, 
				alarmTitle,
				alarmSummary,
				contentIntent);

		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notify.vibrate = new long[] { 200, 200, 500, 300 };
		// notify.sound=Uri.parse("file:/");
		notify.number++;
		*/

		NotificationCompat.Builder builder = createNotificationBuilder(context, contentIntent,
				alarmTitle, alarmSummary, tickerString);
		builder.setNumber(builder.mNumber++);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, builder.build());
	}

	private NotificationCompat.Builder createNotificationBuilder(Context context,
																 PendingIntent contentIntent,
																 String title, String message,
																 String tickerText) {
		return new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentTitle(title)
				.setContentText(message)
				.setContentIntent(contentIntent)
				.setVibrate(new long[] {200, 200, 500, 300})
				.setAutoCancel(true)
				.setTicker(tickerText);
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
