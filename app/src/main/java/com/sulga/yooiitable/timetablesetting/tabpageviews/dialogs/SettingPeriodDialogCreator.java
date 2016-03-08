package com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.Timetable.ColumnTypes;
import com.sulga.yooiitable.mylog.MyLog;

public class SettingPeriodDialogCreator {

	private static ColumnTypes columnType;
	private static int columnNum;
	
	public static Dialog createDialog(final Context context,
									  final Timetable timetable, final int maxPeriodNum,
									  final String[] columnTypeNames,
									  final ColumnTypes[] columnTypes,
									  final int defColumnTypeIdx,
//			final String[] columnNumNames,
//			final int[] columnNums,
//			final int defColumnNumIdx,
									  int defColumnNum,
									  final OnSettingColumnEndListener onSettingsEndListener){
//		MyLog.d("OnItemSelected", "startDaySpinner, startDay : " + startDay + ", endDay : " + endDay);
		columnType = columnTypes[defColumnTypeIdx];
		columnNum = defColumnNum;

		final Resources res = context.getResources();
		String title = res.getString(R.string.dialog_settings_periodpick_title);

		View dialogView = View.inflate(context, R.layout.dialog_settings_periodpick_edittext, null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();

		final Spinner columnTypeSpinner =
				(Spinner) dialogView.findViewById(R.id.dialog_settings_period_periodtype_spinner);
		ArrayAdapter columnTypeAdapter = new ArrayAdapter<String>(context,
				R.layout.simple_spinner_dropdown_item, 
				columnTypeNames);
		columnTypeSpinner.setAdapter(columnTypeAdapter);
		columnTypeSpinner.setSelection(defColumnTypeIdx, false);		//if not setting this, onItemSelected called on instantiate spinner
		
		//시간표에 들어있는 수업들 중 마지막에 끝나는 교시가 몇교시?
		Lesson latestLesson = null;
		int latestLessonEndPeriod = 0;
		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			Lesson l = timetable.getLessonList().get(i);
			int end = (int) l.getLessonEndPeriodByFloat();
			if(end > latestLessonEndPeriod){
				latestLessonEndPeriod = end;
				latestLesson = l;
			}
		}
		int _minPeriodNum = 1;
		if(latestLesson != null){
			_minPeriodNum = (int) Math.ceil(
					latestLesson.getLessonEndPeriodByFloat());
		}
		final int minPeriodNum = _minPeriodNum;


		if(latestLesson != null){
			MyLog.d("TimetableSettingView", "finalLessonEndPeriod : " + 
					latestLesson.getLessonEndPeriodByFloat()
					);
		}

//		String[] _columnNumNames = null;
//		int[] _columnNums = null;
//		if(defColumnNumIdx == -1){
//			_columnNumNames = new String[columnNumNames.length + 1];
//			_columnNumNames[0] = Integer.toString(columnNum);
//			for(int i = 0; i < columnNumNames.length ; i++){
//				_columnNumNames[i + 1] = columnNumNames[i];
//			}
//			_columnNums = new int[columnNums.length + 1];
//			_columnNums[0] = columnNum;
//			for(int i = 0; i < columnNums.length ; i++){
//				_columnNums[i + 1] = columnNums[i];
//			}
//		}else{
//			_columnNumNames = new String[columnNumNames.length];
//			_columnNumNames[0] = Integer.toString(columnNum);
//			for(int i = 0; i < columnNumNames.length ; i++){
//				_columnNumNames[i] = columnNumNames[i];
//			}
//			_columnNums = new int[columnNums.length];
//			_columnNums[0] = columnNum;
//			for(int i = 0; i < columnNums.length ; i++){
//				_columnNums[i] = columnNums[i];
//			}
//		}
//		final String[] __columnNumNames = _columnNumNames;
//		final int[] __columnNums = _columnNums;
		
//		final Spinner columnNumSpinner = 
//				(Spinner) dialogView.findViewById(R.id.dialog_settings_period_periodnum_spinner);
//		ArrayAdapter columnNumAapter = new ArrayAdapter<String>(context,
//				android.R.layout.simple_spinner_dropdown_item, __columnNumNames);
//		columnNumSpinner.setAdapter(columnNumAapter);
//		columnNumSpinner.setSelection(defColumnNumIdx, false);		//if not setting this, onItemSelected called on instantiate spinner
		
//		columnNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				final int tmp = columnNum;
//				if( ( latestLesson != null ) &&
//						( __columnNums[position] + 1 < 
//								latestLesson.getLessonEndPeriodByFloat() + 1 ) ){
//					String warnA = res.
//							getString(R.string.timetable_setting_warning_tableLengthLowerThanLesson_A);
//					String warnB = res.
//							getString(R.string.timetable_setting_warning_tableLengthLowerThanLesson_B);
//
//					Toast.makeText(
//							context, 
//							"\"" + latestLesson.getLessonName() + "\" " + 
//									warnA+ 
//									(int)latestLesson.getLessonEndPeriodByFloat() + 
//									warnB, 
//									Toast.LENGTH_LONG)
//									.show();
//					columnNumSpinner.post(new Runnable(){
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							int idxBefore = TimetableSettingStringManager
//									.getIntegerItemIndexOfArray(tmp, __columnNums);
//							columnNumSpinner.setSelection(idxBefore);
//						}
//					});
//					return;
//				}
//				columnNum = __columnNums[position];
////				pickColumnNumText.setText(columnNumStrings[clickedItemPosition]);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				MyLog.d("OnItemSelected", "nothing selected");
//			}
//		});
		
		
		final EditText columnNumEditText =
				(EditText) dialogView.findViewById(R.id.dialog_settings_period_periodnum_edittext);
		columnNumEditText.setText(Integer.toString(defColumnNum));
		columnNumEditText.setHint(
				minPeriodNum + " - " + maxPeriodNum);
		
		columnTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				columnType = columnTypes[position];
//				pickColumnTypeText.setText(columnTypeStrings[clickedItemPosition]);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.dialog_settings_period_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int inputPeriodNum = -1;
				if(columnNumEditText.getText().toString().equals("") == false){
					inputPeriodNum = Integer.parseInt(columnNumEditText.getText().toString());
				}
				if(inputPeriodNum < minPeriodNum ||
						inputPeriodNum > maxPeriodNum){
					Toast.makeText(context, "Input must be between "
						+ minPeriodNum
						+ " - " 
						+ maxPeriodNum,
						Toast.LENGTH_SHORT)
						.show();
					return;
				}else{
					columnNum = Integer.parseInt(
							columnNumEditText.getText().toString());
				}
				onSettingsEndListener.onSettingsEnd(columnType, columnNum);
				dialog.dismiss();
			}
		});

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_settings_period_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				columnType = null;
				columnNum = -1;
				
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
	public interface OnSettingColumnEndListener{
		public void onSettingsEnd(ColumnTypes columnType, int columnNum);
	}

}
