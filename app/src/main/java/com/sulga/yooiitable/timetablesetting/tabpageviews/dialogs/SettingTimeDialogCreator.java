package com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs;


@Deprecated
public class SettingTimeDialogCreator {
/*
	private static int startTime = -1;
	private static int timeOffset = -1;
	private static StartTimeSpinnerOnItemSelected startOnSelected;
	private static TimeOffsetSpinnerOnItemSelected timeOffsetOnSelected;

	public static Dialog createDialog(final Context context, 
			final String[] startTimeNames,
			final int[] startTimes,
			final int defStartTimeIdx,
			final String[] timeOffsetNames,
			final int[] timeOffsets,
			final int defTimeOffsetIdx,
			final OnSettingTimeEndListener onSettingsEndListener){

		startTime = startTimes[defStartTimeIdx];
		timeOffset = timeOffsets[defTimeOffsetIdx];
		
//		MyLog.d("OnItemSelected", "startDaySpinner, startDay : " + startDay + ", endDay : " + endDay);

		final Resources res = context.getResources();
		String title = res.getString(R.string.dialog_settings_timepick_title);

		View dialogView = View.inflate(context, R.layout.dialog_settings_timepick, null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();

		final Spinner startTimeSpinner = 
				(Spinner) dialogView.findViewById(
						R.id.dialog_settings_timepick_starttime_spinner);
		ArrayAdapter startTimeAdapter = 
				new ArrayAdapter<String>(context, 
						R.layout.simple_spinner_dropdown_item, 
						startTimeNames);
		startTimeSpinner.setAdapter(startTimeAdapter);
		startTimeSpinner.setSelection(defStartTimeIdx, false);		//if not setting this, onItemSelected called on instantiate spinner
		
		final Spinner timeOffsetSpinner = (Spinner) 
				dialogView.findViewById(R.id.dialog_settings_timepick_interval_spinner);
		ArrayAdapter timeOffsetAdapter =
				new ArrayAdapter<String>(context, 
						R.layout.simple_spinner_dropdown_item, 
						timeOffsetNames);
		timeOffsetSpinner.setAdapter(timeOffsetAdapter);
		timeOffsetSpinner.setSelection(defTimeOffsetIdx, false);		//if not setting this, onItemSelected called on instantiate spinner
		
		startOnSelected = 
				new StartTimeSpinnerOnItemSelected(context, startTimes, startTimeSpinner);
		timeOffsetOnSelected = 
				new TimeOffsetSpinnerOnItemSelected(context, timeOffsets, timeOffsetSpinner);
		
		startTimeSpinner.setOnItemSelectedListener(startOnSelected);
		timeOffsetSpinner.setOnItemSelectedListener(timeOffsetOnSelected);

		Button ok = (Button) dialogView.findViewById(R.id.dialog_settings_timepick_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSettingsEndListener.onSettingsEnd(willClearTimetable, startTime, timeOffset);
				dialog.dismiss();
			}
		});

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_settings_timepick_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		return dialog;
	}

	
	private static class OnStartTimeChangedAlertDialogOKOnClick implements DialogInterface.OnClickListener{
		private int newStartTime;
		public OnStartTimeChangedAlertDialogOKOnClick(int newStartTime){
			this.newStartTime = newStartTime;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			willClearTimetable = true;
			startTime = newStartTime;
		}
	};

	private static class OnStartTimeChangedAlertDialogCancelOnClick implements DialogInterface.OnClickListener{ 
		private int itemBeforeIdx;
		private Spinner spinner;
		public OnStartTimeChangedAlertDialogCancelOnClick(int itemBeforeIdx, Spinner spinner){
			this.itemBeforeIdx = itemBeforeIdx;
			this.spinner = spinner;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			spinner.setOnItemSelectedListener(null);
			spinner.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					spinner.setSelection(itemBeforeIdx, false);
					spinner.setOnItemSelectedListener(startOnSelected);
				}
				
			});
			dialog.dismiss();
		}
	};

	public static boolean willClearTimetable = false;
	public interface OnSettingTimeEndListener{
		public void onSettingsEnd(boolean willClearTimetable, int startTime, int timeOffset);
	}

	private static class OnTimeOffsetChangedAlertDialogOKOnClick implements DialogInterface.OnClickListener{
		private int newTimeOffset;
		public OnTimeOffsetChangedAlertDialogOKOnClick(int newTimeOffset){
			this.newTimeOffset = newTimeOffset;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			willClearTimetable = true;
			timeOffset = newTimeOffset;
		}
	};
	
	private static class OnTimeOffsetChangedAlertDialogCancelOnClick 
	implements DialogInterface.OnClickListener{ 
		private int itemBeforeIdx;
		private Spinner spinner;
		public OnTimeOffsetChangedAlertDialogCancelOnClick(int itemBeforeIdx, Spinner spinner){
			this.itemBeforeIdx = itemBeforeIdx;
			this.spinner = spinner;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			spinner.setOnItemSelectedListener(null);
			spinner.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					spinner.setSelection(itemBeforeIdx, false);
					spinner.setOnItemSelectedListener(timeOffsetOnSelected);
				}
				
			});
			dialog.dismiss();
		}
	};
 

//	private static DialogInterface.OnClickListener onTimeOffsetChangedAlertDialogCancelOnClick = 
//			new DialogInterface.OnClickListener() {
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			dialog.dismiss();
//		}
//	};

	private static class StartTimeSpinnerOnItemSelected implements AdapterView.OnItemSelectedListener{
		Context context;
		int[] startTimes;
		Spinner startTimeSpinner;
		public StartTimeSpinnerOnItemSelected(Context context, 
				int[] startTimes, Spinner startTimeSpinner){
			this.context = context;
			this.startTimes = startTimes;
			this.startTimeSpinner = startTimeSpinner;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			MyLog.d("onItemSelected", "Called!!!!!!!");
			int beforeIndex = TimetableSettingStringManager
					.getIntegerItemIndexOfArray(startTime, startTimes);
			if(beforeIndex == -1){
				beforeIndex = 0;
			}
			String warnTitle = 
					context.getResources().getString(R.string.notice);
			String warnMessage = 
					context.getResources().getString(R.string.timetable_setting_warning_tableinit_on_starttimechange);
			AlertDialogCreator.getClearTimetableAlertDialog(
					warnTitle,
					warnMessage,
					context, 
					new OnStartTimeChangedAlertDialogOKOnClick(startTimes[position]),
					new OnStartTimeChangedAlertDialogCancelOnClick(beforeIndex, startTimeSpinner)
					).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	}
	
	private static class TimeOffsetSpinnerOnItemSelected implements AdapterView.OnItemSelectedListener{
		Context context;
		int[] timeOffsets;
		Spinner timeOffsetSpinner;
		public TimeOffsetSpinnerOnItemSelected(Context context, 
				int[] timeOffsets, Spinner timeOffsetSpinner){
			this.context = context;
			this.timeOffsets = timeOffsets;
			this.timeOffsetSpinner = timeOffsetSpinner;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
			MyLog.d("onItemSelected", "Called!!!!!!!");
			int beforeIndex = TimetableSettingStringManager
					.getIntegerItemIndexOfArray(timeOffset, timeOffsets);
			if(beforeIndex == -1){
				beforeIndex = 0;
			}
			String warnTitle = 
					context.getResources().getString(R.string.notice);
			String warnMessage = 
					context.getResources().getString(
							R.string.timetable_setting_warning_tableinit_on_timeoffsetchange);
			AlertDialogCreator.getClearTimetableAlertDialog(
					warnTitle,
					warnMessage,
					context, 
					new OnTimeOffsetChangedAlertDialogOKOnClick(timeOffsets[position]),
					new OnTimeOffsetChangedAlertDialogCancelOnClick(beforeIndex, timeOffsetSpinner)
					).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	}
*/
	
}

