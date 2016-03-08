package com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs;

import android.annotation.SuppressLint;
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
	
	@SuppressLint("SetTextI18n")
	public static Dialog createDialog(final Context context,
									  final Timetable timetable, final int maxPeriodNum,
									  final String[] columnTypeNames,
									  final ColumnTypes[] columnTypes,
									  final int defColumnTypeIdx,
									  int defColumnNum,
									  final OnSettingColumnEndListener onSettingsEndListener){
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
		ArrayAdapter columnTypeAdapter = new ArrayAdapter<>(context,
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

		final EditText columnNumEditText =
				(EditText) dialogView.findViewById(R.id.dialog_settings_period_periodnum_edittext);
		columnNumEditText.setText(Integer.toString(defColumnNum));
		columnNumEditText.setHint(
				minPeriodNum + " - " + maxPeriodNum);
		
		columnTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				columnType = columnTypes[position];
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.dialog_settings_period_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int inputPeriodNum = -1;
				if(!columnNumEditText.getText().toString().equals("")){
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
				columnType = null;
				columnNum = -1;
				
				dialog.dismiss();
			}
		});
		
		return dialog;
	}

	public interface OnSettingColumnEndListener{
		void onSettingsEnd(ColumnTypes columnType, int columnNum);
	}
}
