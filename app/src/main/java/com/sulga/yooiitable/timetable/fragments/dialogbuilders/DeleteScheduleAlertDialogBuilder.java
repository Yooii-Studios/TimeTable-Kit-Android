package com.sulga.yooiitable.timetable.fragments.dialogbuilders;

import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.*;

import android.content.*;
import android.content.res.*;
import android.view.*;

import com.sulga.yooiitable.R;

public class DeleteScheduleAlertDialogBuilder {

	public static Dialog createDialog(Context context, final View.OnClickListener onOKClickedListener){
		Resources res = context.getResources();
		String title = res.getString(R.string.dialog_deleteschedule_title);
		
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
				dialog.dismiss();
			}
		});

		final Button ok = (Button) dialogView.findViewById(R.id.dialog_alert_simple_button_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onOKClickedListener.onClick(ok);
				dialog.dismiss();
			}
		});

		return dialog;
	}

}
