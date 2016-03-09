package com.sulga.yooiitable.timetableinfo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.FlurryConstants;

public class CreditsActivity extends AppCompatActivity {
	ListView creditListView;

	Menu m_menu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

//		getActionBar().setTitle(getResources().getString(R.string.timetable_setting_info_credit));
//		getActionBar().setDisplayShowHomeEnabled(false);

		setContentView(R.layout.activity_timetable_option_credits);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.app_name));
		//creditListView = (ListView) findViewById(R.id.listView1);
		//initCreditListView();

		//FlurryAgent.onEvent("Configure - InfoPage - Credit");
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

//	protected void initCreditListView() {
//		creditListView.setBackgroundColor( GeneralSetting.getBackwardBackgroundColor() );
//		//			creditListView.setBackgroundResource(GeneralSetting.getBackwardBackgroundRes());
//		//			this.setSelector(new PaintDrawable(0xffff0000));
//		creditListView.setDivider(new ColorDrawable( GeneralSetting.getBackwardBackgroundColor() ));
//		creditListView.setDividerHeight( DipToPixel.getPixel(this, 3) );
//		creditListView.setCacheColorHint(0x00000000);
//		creditListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//
//		MNInfoCreditListAdaptor creditListAdaptor = new MNInfoCreditListAdaptor(this);
//		creditListView.setAdapter(creditListAdaptor);
//	}

}
