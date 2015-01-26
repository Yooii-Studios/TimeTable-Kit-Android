package com.sulga.yooiitable.utils;

import com.sulga.yooiitable.*;

import android.content.*;
import android.view.*;
import android.widget.*;

public class ToastMaker {
	public static void popupToastAtCenter(Context context, String msg) {
		Toast message = Toast.makeText(context, 
				msg, Toast.LENGTH_SHORT);
		message.setGravity(Gravity.CENTER, 0, -200);
		message.show();
	}

	public static final int UNLOCK_FULL_VERSION_TOAST_OVERFLOW_PAGENUM = 0;
    public static final int UNLOCK_FULL_VERSION_TOAST_OVERFLOW_SCHEDULENUM = 1;
	public static final int UNLOCK_FULL_VERSION_TOAST_DEFAULT = 1;
	public static void popupUnlockFullVersionToast(Context context, int messageType, boolean popupCenter){
		if(messageType == UNLOCK_FULL_VERSION_TOAST_OVERFLOW_PAGENUM){
			String fullString_a = context.getResources()
					.getString(R.string.unlock_full_version_pagenum_overflow);
			String fullString = context.getResources()
					.getString(R.string.unlock_full_version);
            if(popupCenter)
			    ToastMaker.popupToastAtCenter(context, fullString_a + "\n" + fullString);
            else
                Toast.makeText(context, fullString_a + "\n" + fullString, Toast.LENGTH_SHORT).show();
		}else if(messageType == UNLOCK_FULL_VERSION_TOAST_OVERFLOW_SCHEDULENUM) {
            String fullString_a = context.getString(R.string.unlock_full_version_schedule_overflow);
            String fullString = context.getResources()
                    .getString(R.string.unlock_full_version);
            if(popupCenter)
                ToastMaker.popupToastAtCenter(context, fullString_a + "\n" + fullString);
            else
                Toast.makeText(context, fullString_a + "\n" + fullString, Toast.LENGTH_SHORT).show();
        }
        else if(messageType == UNLOCK_FULL_VERSION_TOAST_DEFAULT){
			String fullString = context.getResources()
					.getString(R.string.unlock_full_version);
            if(popupCenter)
			    ToastMaker.popupToastAtCenter(context, fullString);
            else
                Toast.makeText(context, fullString, Toast.LENGTH_SHORT).show();
		}
	}
}
