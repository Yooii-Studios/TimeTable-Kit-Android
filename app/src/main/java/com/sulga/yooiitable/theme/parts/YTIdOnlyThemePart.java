package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.view.*;

public class YTIdOnlyThemePart implements YTThemePart {

	int resId;
	public YTIdOnlyThemePart(int resId) {
		// TODO Auto-generated constructor stub
		this.resId = resId;
	}

	@Override
	public View setViewTheme(Context context, View v) {
		// TODO Auto-generated method stub
		v.setBackgroundResource(resId);
		return v;
	}
}