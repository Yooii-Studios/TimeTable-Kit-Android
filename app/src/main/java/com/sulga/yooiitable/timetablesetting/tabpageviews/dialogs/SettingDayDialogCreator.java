package com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetablesetting.utils.TimetableSettingStringManager;


public class SettingDayDialogCreator {

	private static int startDay = -1;
	private static int endDay = -1;
	private static String startString = "";
	private static String endString = "";

	class DayItem{
		String name;
		int item;
		public DayItem(String name, int item){
			this.name = name;
			this.item = item;
		}
	}

	public static Dialog createDialog(final Context context, 
			final String[] startDayNames,
			final int[] startDays,
			final int defStartDayIdx,
			final String[] endDayNames,
			final int[] endDays,
			final int defEndDayIdx,
			final OnSettingDayEndListener onSettingsEndListener){
		startDay = startDays[defStartDayIdx];
		endDay = endDays[defEndDayIdx];
		startString = startDayNames[defStartDayIdx];
		endString = endDayNames[defEndDayIdx];
		
		MyLog.d("OnItemSelected", "startDaySpinner, startDay : " + startDay + ", endDay : " + endDay);

		Resources res = context.getResources();
		String title = res.getString(R.string.dialog_settings_daypick_title);

		View dialogView = View.inflate(context, R.layout.dialog_settings_daypick, null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();		

		final Spinner startDaySpinner = (Spinner) dialogView.findViewById(R.id.dialog_settings_daypick_startday_spinner);
		ArrayAdapter startDayAdapter = new ArrayAdapter<String>(context,
				R.layout.simple_spinner_dropdown_item, 
				startDayNames);
		startDaySpinner.setAdapter(startDayAdapter);
		startDaySpinner.setSelection(defStartDayIdx, false);		//if not setting this, onItemSelected called on instantiate spinner
		
		final Spinner endDaySpinner = (Spinner) dialogView.findViewById(R.id.dialog_settings_daypick_endday_spinner);
		ArrayAdapter endDayAdapter = new ArrayAdapter<String>(context, 
				R.layout.simple_spinner_dropdown_item,
				endDayNames);
		endDaySpinner.setAdapter(endDayAdapter);
		endDaySpinner.setSelection(defEndDayIdx, false);		//if not setting this, onItemSelected called on instantiate spinner
		
		startDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final int tmp = startDay;
				final String tmpStr = startString;
				startDay = startDays[position];
				startString = startDayNames[position];
				MyLog.d("OnItemSelected", "startDaySpinner, startDay : " + startDay + ", endDay : " + endDay +", position : " + position);
				if(startDay == endDay){
					startDaySpinner.post(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int idxBefore = TimetableSettingStringManager.getIntegerItemIndexOfArray(tmp, startDays);
							startDaySpinner.setSelection(idxBefore);
						}
					});
					startDay = tmp;
					startString = tmpStr;
					String warn = context.getResources().
							getString(R.string.timetable_setting_select_startday_warning_overlapdays);
					Toast.makeText(
							context, 
							warn, Toast.LENGTH_LONG
							).show();
					return;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				MyLog.d("OnItemSelected", "nothing selected");
			}
		});
		endDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final int tmp = endDay;
				final String tmpStr = endString;
				endDay = endDays[position];
				endString = endDayNames[position];
				MyLog.d("OnItemSelected", "endDaySpinner, endDay : " + endDay + ", startDay : " + startDay);
				if(endDay == startDay){
					endDaySpinner.post(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int idxBefore = TimetableSettingStringManager
									.getIntegerItemIndexOfArray(tmp, endDays);
							endDaySpinner.setSelection(idxBefore);
						}
					});
					endDay = tmp;
					endString = tmpStr;
					String warn = context.getResources().
							getString(R.string.timetable_setting_select_endday_warning_overlapdays);
					Toast.makeText(
							context, 
							warn, Toast.LENGTH_LONG
							).show();
					return;
				}

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.dialog_settings_daypick_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSettingsEndListener.onSettingsEnd(startDay, endDay, startString, endString);
				dialog.dismiss();
			}
		});

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_settings_daypick_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startDay = -1;
				endDay = -1;
				startString = "";
				endString = "";
				dialog.dismiss();
			}
		});
		
		return dialog;
	}
	
	


	//	class DayAdapter extends ArrayAdapter<DayItem>
	//    {
	//        private Activity context;
	//        ArrayList<DayItem> data = null;
	//
	//        public DayAdapter(Activity context, int resource, ArrayList<DayItem> data)
	//        {
	//            super(context, resource, data);
	//            this.context = context;
	//            this.data = data;
	//        }
	//
	//        @Override
	//        public View getView(int position, View convertView, ViewGroup parent) 
	//        {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
	//            return super.getView(position, convertView, parent);   
	//        }
	//
	//        @Override
	//        public View getDropDownView(int position, View convertView, ViewGroup parent)
	//        {   // This view starts when we click the spinner.
	//            View row = convertView;
	//            if(row == null)
	//            {
	//                LayoutInflater inflater = context.getLayoutInflater();
	//                row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
	//            }
	//
	//            DayItem i = data.get(position);
	//
	//            if(i != null)
	//            {   // Parse the data from each object and set it.
	//            	TextView text = (TextView) row.findViewById(android.R.id.text1);
	//            	if(i.item == )
	//            }
	//
	//            return row;
	//        }
	//    }
	public interface OnSettingDayEndListener{
		public void onSettingsEnd(int startDay, int endDay, String startString, String endString);
	}
}