package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.view.*;

public class YTColorThemePart implements YTThemePart {

	private int color;
	public YTColorThemePart(int color) {
		// TODO Auto-generated constructor stub
		this.color = color;
	}

	@Override
	public View setViewTheme(Context context, View v) {
		// TODO Auto-generated method stub
		v.setBackgroundColor(color);
		return v;
	}

}
