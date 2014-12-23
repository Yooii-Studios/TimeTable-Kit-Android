package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.timetable.*;

public class YTBackgroundBitmapThemePart implements YTThemePart {

	int resId;
	public YTBackgroundBitmapThemePart(int resId) {
		// TODO Auto-generated constructor stub
		this.resId = resId;
	}
	@Override
	public View setViewTheme(Context context, View v) {
		// TODO Auto-generated method stub
		if (TimetableActivity.class.isInstance(context)) {
			((TimetableActivity) context).loadBitmap(resId, (ImageView)v);
		}		
		return v;
	}
}
