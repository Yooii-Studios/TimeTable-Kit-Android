package com.sulga.yooiitable.mylog;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sulga.yooiitable.constants.Devs;

public class MyLog {

	public static void d(String tag, String msg){
		if(Devs.isDevMode){
			Log.d(tag, msg);
		}
	}
	
	public static void complain(Context context, String TAG, String message) {
		Log.e(TAG, "**** YooiiTable Error: " + message);
		alert(context, TAG, message);
	}

	public static void alert(Context context, String TAG, String message) {
//		AlertDialog.Builder bld = new AlertDialog.Builder(context);
//		bld.setMessage(message);
//		bld.setNeutralButton("OK", null);
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		MyLog.d(TAG, "Showing alert dialog: " + message);
//		bld.create().show();
	}

}
