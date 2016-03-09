package com.sulga.yooiitable.timetableinfo.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.constants.YTUrls;


public class YTInfoActivity extends AppCompatActivity {
	ListView creditListView;

	//Menu m_menu;
	
	View yt_yooiistudios;
	View yt_termsOfService;
	View yt_help;
	View yt_license;
	View yt_version;
	TextView yt_version_prompt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		getActionBar().setTitle(getResources().getString(R.string.timetable_setting_info_yooii_info));
//		getActionBar().setDisplayShowHomeEnabled(false);

		setContentView(R.layout.activity_timetable_option_ytinfo);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.app_name));
		yt_yooiistudios = findViewById(R.id.activity_timetable_option_ytinfo_yooiistudios);
		yt_termsOfService = findViewById(R.id.activity_timetable_option_ytinfo_termsofservice);
		yt_termsOfService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = 
						new Intent(Intent.ACTION_VIEW, 
								Uri.parse(YTUrls.TERMS_OF_SERVICE_URL));
				startActivity(browserIntent);
			}
		});
		yt_help = findViewById(R.id.activity_timetable_option_ytinfo_help);
		yt_help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = 
						new Intent(Intent.ACTION_VIEW, 
								Uri.parse(YTUrls.TIMETABLE_HELP_URL));
				startActivity(browserIntent);
			}
		});
		//		yt_restorePurchase = findViewById(R.id.activity_timetable_option_ytinfo_restore_purchases);
		yt_license = findViewById(R.id.activity_timetable_option_ytinfo_license);
		yt_license.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent licenseIntent = 
						new Intent(YTInfoActivity.this, LicenseActivity.class);
				YTInfoActivity.this.startActivity(licenseIntent);

			}
		});
		yt_version = findViewById(R.id.activity_timetable_option_ytinfo_version);
		
		yt_yooiistudios.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = 
						new Intent(Intent.ACTION_VIEW, Uri.parse(YTUrls.HOMPAGE_URL));
				YTInfoActivity.this.startActivity(browserIntent);
			}
		});
		yt_version_prompt = (TextView) findViewById(R.id.activity_timetable_option_ytinfo_version_prompt2);
		PackageInfo pinfo;
		try {
			pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String versionName = pinfo.versionName;
			yt_version_prompt.setText(versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//creditListView = (ListView) findViewById(R.id.listView1);
		//initCreditListView();

		//FlurryAgent.onEvent("Configure - InfoPage - Credit");
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

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.actionbar_menu, menu);
//
//		m_menu = menu;
//		m_menu.findItem(R.id.actionbar_menu_configure_cancel).setVisible(false);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.actionbar_menu_configure_done:
//			finish();
//			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//			return true;
//		}
//
//		return false;
//	}

	

}
