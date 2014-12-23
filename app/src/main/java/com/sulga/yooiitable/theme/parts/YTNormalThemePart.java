package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.graphics.drawable.*;
import android.view.*;

public class YTNormalThemePart implements YTThemePart {

	private int resId;
	private int alpha;
	public YTNormalThemePart(int resId, int alpha) {
		this.resId = resId;
		this.alpha = alpha;
	}
	@Override
	public View setViewTheme(Context context, View v) {
		// TODO Auto-generated method stub
		Drawable d = context.getResources().getDrawable(resId);
		d.setAlpha(alpha);
		v.setBackgroundResource(resId);
		return v;
	}
}