package com.sulga.yooiitable.splashscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.TimetableActivity;

public class YTSplashScreenActivity extends AppCompatActivity {
	private static final String TAG = "YTSplashScreenActivity";
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 800;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		if(startTimetableActivityHandler == null){
			startTimetableActivityHandler = new Handler();
			startTimetableActivityHandler.postDelayed(new Runnable() {

				/*
				 * Showing splash screen with a timer. This will be useful when you
				 * want to show case your app logo / company
				 */

				@Override
				public void run() {
					MyLog.d(TAG, "Main Activity Called");
					// This method will be executed once the timer is over
					// Start your app main activity
					Intent i = new Intent(YTSplashScreenActivity.this, TimetableActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);

					// close this activity
					finish();
					startTimetableActivityHandler = null;
				}
			}, SPLASH_TIME_OUT);
		}
	}

	private static Handler startTimetableActivityHandler = null;

	public void onResume(){
		super.onResume();
		MyLog.d(TAG, "onResume called");
	}
	
	public void onPause(){
		super.onPause();
		MyLog.d(TAG, "onPause called");
	}

    @Override
    public void onBackPressed(){}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	private static void recycleBitmap(ImageView iv) {
		Drawable d;
		if(iv != null){
			d = iv.getDrawable();
		}else{
			return;
		}

		if (d != null && d instanceof BitmapDrawable) {
			MyLog.d("RecycleBitmap", "Recycling!");
			Bitmap b = ((BitmapDrawable)d).getBitmap();
			b.recycle();
		} // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
		if(d != null)
			d.setCallback(null);
	}
}
