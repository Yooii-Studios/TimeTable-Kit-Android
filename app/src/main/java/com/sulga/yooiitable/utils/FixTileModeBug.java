package com.sulga.yooiitable.utils;

import com.sulga.yooiitable.mylog.*;

import android.graphics.Shader.TileMode;
import android.graphics.drawable.*;
import android.view.*;

public class FixTileModeBug {

	public static void fixBackgroundRepeat(View view) {
		MyLog.d("FixTileModeBug", "Called");
	    Drawable bg = view.getBackground();
	    if (bg != null) {
	        if (bg instanceof BitmapDrawable) {
	            BitmapDrawable bmp = (BitmapDrawable) bg;
	            bmp.mutate(); // make sure that we aren't sharing state anymore
	            bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	        }
	    }
	}
}
