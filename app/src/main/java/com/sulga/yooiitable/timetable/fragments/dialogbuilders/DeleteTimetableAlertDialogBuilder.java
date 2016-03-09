package com.sulga.yooiitable.timetable.fragments.dialogbuilders;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;


public class DeleteTimetableAlertDialogBuilder{
	public static Dialog createDialog(Context context,
									  final TimetableFragment parent){
		Resources res = context.getResources();
		String title = res.getString(R.string.dialog_deletetimetable_title);
		
		View dialogView = View.inflate(context, R.layout.dialog_alert_simple, null);
	
		final Dialog dialog =  new AlertDialog.Builder(context)
		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();		

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_alert_simple_button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.onActivityResult(
						TimetableFragment.FRAG_TIMETABLE_DIALOG_DELETE_TIMETABLE, 
						android.app.Activity.RESULT_CANCELED, 
						null
						);
				dialog.dismiss();

			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.dialog_alert_simple_button_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				parent.onActivityResult(
						TimetableFragment.FRAG_TIMETABLE_DIALOG_DELETE_TIMETABLE, 
						android.app.Activity.RESULT_OK

						);
				dialog.dismiss();
				FlurryAgent.logEvent(FlurryConstants.TIMETABLE_DELETED);
			}
		});

		return dialog;
		//		final Dialog dialog = super.onCreateDialog(savedInstanceState);

		//		
		//		
		//		dialog.setTitle(title);
		//		dialog.setContentView(R.layout.dialog_delete_timetable);
		//		
		//		dialog.setCancelable(false);
		//		
		//		return dialog;
	}
}
