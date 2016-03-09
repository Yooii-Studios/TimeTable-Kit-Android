package com.sulga.yooiitable.timetable;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;

import com.sulga.yooiitable.R;

public class StartTutorialDialogBuilder {

	public static Dialog createDialog(Context context){
		View dialogView = View.inflate(context, R.layout.dialog_tutorial_start, null);
		
		final Dialog dialog =  new AlertDialog.Builder(context)
		.setCancelable(true)
		.setView(dialogView)
		.create();		

		ImageButton ok = (ImageButton) dialogView.findViewById(
				R.id.dialog_tutorial_start_button_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		return dialog;
	}

}
