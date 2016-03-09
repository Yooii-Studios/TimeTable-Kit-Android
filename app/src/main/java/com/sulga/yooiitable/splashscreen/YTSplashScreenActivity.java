package com.sulga.yooiitable.splashscreen;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.TimetableActivity;
import com.yooiistudios.common.permission.PermissionUtils;

public class YTSplashScreenActivity extends AppCompatActivity {
	private static final int REQ_PERMISSION_MULTIPLE = 108;
	private static final int REQ_PERMISSION_READ_STORAGE = 136;
	public static final int REQ_PERMISSION_READ_CONTACTS = 137;
	private static final String TAG = "YTSplashScreenActivity";

	RelativeLayout containerLayout;

	// Splash screen timer
	@SuppressWarnings("FieldCanBeLocal")
	private static int SPLASH_TIME_OUT = 200;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		containerLayout = (RelativeLayout) findViewById(R.id.splash_container);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			startMainActivity();
		} else {
			if (hasAllPermissions()) {
				startMainActivity();
			} else {
				// 첫 시작시에 권한 요청(6.0 이상)
				requestPermissionsToStart();
			}
		}
	}

	@SuppressWarnings("RedundantIfStatement")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private boolean hasAllPermissions() {
		if (PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
				PermissionUtils.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
			return true;
		} else {
			return false;
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void requestPermissionsToStart() {
		String[] permissions = {
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.READ_CONTACTS};
		PermissionUtils.requestPermissions(this, permissions, REQ_PERMISSION_MULTIPLE);
	}

	private void startMainActivity() {
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

	/*
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
	*/

	/**
	 * 안드로이드 6.0 이후 권한 처리 콜백
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (PermissionUtils.isPermissionGranted(grantResults)) {
			Snackbar.make(containerLayout, R.string.permission_granted,
					Snackbar.LENGTH_SHORT).show();
		} else {
			Snackbar.make(containerLayout, R.string.permission_not_granted,
					Snackbar.LENGTH_SHORT).show();
		}

		if (hasAllPermissions()) {
			startMainActivity();
		} else {
			requestSpecificPermissions();
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void requestSpecificPermissions() {
		if (!PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
			PermissionUtils.requestPermission(this, containerLayout,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					R.string.need_permission_read_storage, REQ_PERMISSION_READ_STORAGE);
		}
		if (!PermissionUtils.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
			PermissionUtils.requestPermission(this, containerLayout,
					Manifest.permission.READ_CONTACTS,
					R.string.need_permission_read_contacts, REQ_PERMISSION_READ_CONTACTS);
		}
	}
}
