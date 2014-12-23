package com.sulga.yooiitable.theme.parts;

import android.content.*;
import android.view.*;

public interface YTThemePart {
	/**
	 * context is used to get get Resources.
	 * this method returns view which background resource settled.
	 * @param context
	 * @param v
	 * @return
	 */
	public View setViewTheme(Context context, View v);
}
