package com.sulga.yooiitable.timetableinfo;

import org.holoeverywhere.app.*;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.*;

import android.os.*;
import android.support.v4.app.*;

import com.actionbarsherlock.app.*;
import com.sulga.yooiitable.R;

public class TimetableInfoActivity extends Activity {
	private static String TAG = "TimetableInfoActivity";
	private TimetableAppInfoFragment infoFrag;
	
	private ViewPager mViewPager;
	private ActionBar actionBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_setting_viewpager);
		mViewPager = (ViewPager) findViewById(R.id.setting_viewpager);
		//		FixTileModeBug.fixBackgroundRepeat(mViewPager);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle(getString(R.string.tab_info));

		infoFrag = new TimetableAppInfoFragment();
		mViewPager.setAdapter(new SettingPagerAdapter(this.getSupportFragmentManager()));
	}

	public class SettingPagerAdapter extends FragmentStatePagerAdapter {
	    public SettingPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public Fragment getItem(int i) {
	    	return infoFrag;
	    }
	    @Override
	    public int getCount() {
	        return 1;
	    }
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return "OBJECT " + (position + 1);
	    }
	}

}
