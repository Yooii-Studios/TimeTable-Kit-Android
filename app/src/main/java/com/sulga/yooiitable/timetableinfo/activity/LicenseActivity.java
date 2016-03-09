package com.sulga.yooiitable.timetableinfo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.FlurryConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LicenseActivity extends AppCompatActivity {

	TextView viewPagerIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// ...
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_license);
		viewPagerIndicator = (TextView) findViewById(R.id.activity_license_viewpagerIndicator);

		String vpS = getLicenseString(R.raw.viewpagerindicator_license_text);
		viewPagerIndicator.setText(vpS);
		getSupportActionBar().setTitle(getString(R.string.app_name));
	}

	public String getLicenseString(int rawId){
		InputStream inputStream = getResources().openRawResource(
				rawId);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int i;
		try {
			i = inputStream.read();
			while (i != -1)
			{
				byteArrayOutputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArrayOutputStream.toString();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			break;
		case KeyEvent.KEYCODE_MENU:
			break;
		}
		return true;
	}
	
	public void onStart(){
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryConstants.APP_KEY);
	}

	public void onStop(){
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
