package com.sulga.yooiitable.timetable;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.appwidget.YTAppWidgetProvider_2x4;
import com.sulga.yooiitable.appwidget.YTAppWidgetProvider_4x4;
import com.sulga.yooiitable.constants.FixedSizes;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.constants.NaverInApp;
import com.sulga.yooiitable.constants.RequestCodes;
import com.sulga.yooiitable.constants.YTUrls;
import com.sulga.yooiitable.customviews.ParentViewPager;
import com.sulga.yooiitable.customviews.animation.OnDeletePageTransformer;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.google.calendar.GCAccountManager;
import com.sulga.yooiitable.google.calendar.GCCalendarSyncManager;
import com.sulga.yooiitable.language.YTLanguage;
import com.sulga.yooiitable.language.YTLanguageType;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.overlapviewer.OverlapTablesViewerActivity;
import com.sulga.yooiitable.sharetable.BannerInfo;
import com.sulga.yooiitable.sharetable.TimetableNetworkManager;
import com.sulga.yooiitable.showalltables.ShowAllTimetablesActivity;
import com.sulga.yooiitable.timetable.fragments.ScheduleFragment;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;
import com.sulga.yooiitable.timetableinfo.activity.NaverStoreActivity;
import com.sulga.yooiitable.timetableinfo.activity.StoreActivity;
import com.sulga.yooiitable.timetablesetting.SelectOptionDialogCreator;
import com.sulga.yooiitable.timetablesetting.TimetableSettingFragment;
import com.sulga.yooiitable.utils.AlertDialogCreator;
import com.sulga.yooiitable.utils.AppRater;
import com.sulga.yooiitable.utils.DeviceUuidFactory;
import com.sulga.yooiitable.utils.InAppBillingManager;
import com.sulga.yooiitable.utils.LanguageInitiater;
import com.sulga.yooiitable.utils.ToastMaker;
import com.sulga.yooiitable.utils.UserNameFactory;
import com.sulga.yooiitable.utils.ViewPagerFakeDragger;
import com.sulga.yooiitable.utils.YTBitmapLoader;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.yooiistudios.common.ad.AdUtils;
import com.yooiistudios.common.ad.QuitAdDialogFactory;
import com.yooiistudios.common.network.InternetConnectionManager;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.app.ProgressDialog;
import org.holoeverywhere.widget.DrawerLayout;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;
import org.holoeverywhere.widget.ViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

public class TimetableActivity extends Activity{
	public static final int TIMETABLE_PAGE_OFFSET = 0;	
	public static final int TIMETABLE_MAX_LIMIT = 10;
	private static final String TAG = "TimetableActivity";

	private ParentViewPager mPager;
	//private PagerAdapter mAdapter;
	private MyFragmentPagerAdapter mAdapter;
	private TimetableDataManager tdm;
	private UnderlinePageIndicator mIndicator;
	private ActionBar mActionBar;
	private TextView mActionBarTitle;

	//when adding timetable, show this dialog
	private ProgressDialog addTableProgressDialog;

	//drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private DrawerListAdapter mDrawerAdapter;
	private ActionBarDrawerToggle mDrawerToggle;

	private ImageView dogEar;

	private AdView adView;

    // Quit Ad Dialog
    private AdRequest quitAdRequest;
    private AdView quitAdView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable_main_withdrawer);
		MyLog.d(TAG, "onCreate Called");
		//		Debug.startMethodTracing("TimetableFragment");
		//validation check
		//		AppValidationChecker.validationCheck(this);
		//full version check, must do this when releasing
		if(NaverInApp.isNaverApk == false){
			InAppBillingManager.updateFullVersionState(this, new InAppBillingManager.OnFullVersionStateUpdateFinishedListener() {
				@Override
				public void onFullVersionStateUpdateFinished(boolean isSucceed,
						boolean isFullVersion) {
					// TODO Auto-generated method stub
					MyLog.d(TAG, "Update Full Version State : " + isSucceed 
							+ ", Network Full version : " + isFullVersion);
					if(isSucceed && !isFullVersion){
						//					ToastMaker.popupToastAtCenter(TimetableActivity.this, "NOT FULL VERSION!");
						setupAdView();
						dogEar.setVisibility(View.VISIBLE);
					}else if(isSucceed && isFullVersion){
						removeAdView();
						dogEar.setVisibility(View.GONE);
					}
				}
			});
			AppRater.app_launched(this);
		}
		//temporary
//				TimetableDataManager.saveFullVersionState(TimetableActivity.this, true);
		//update connector count state
		//		TimetableDataManager.getTodayDownloadedTimetable(this);
		//		TimetableDataManager.getTodayUploadedTimetable(this);

		//Update Connector count info
		//		String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
		//		TimetableNetworkManager.setupDeviceIP(uuid);
		////		TimeTableNetworkTool.getUploadInfo(uuid);
		//		TimetableNetworkManager.updateConnectorUseInfo(uuid, this, null);

		addTableProgressDialog = new ProgressDialog(this);
		String addingTableString = getResources()
				.getString(R.string.fragment_timetable_notice_adding_timetable);
		addTableProgressDialog.setMessage(addingTableString);

		//		initActionBar();
		initDrawer();
		//		TimetableDataManager.saveFullVersionState(this, true);
		//		boolean _isFullVersion = TimetableDataManager.getCurrentFullVersionState(this);
		//		MyLog.d("InAppBillingManager", "full version state : " + _isFullVersion);

		mPager = (ParentViewPager)findViewById(R.id.activity_timetable_main_pager);
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);

		tdm = TimetableDataManager.getInstance();
		makePagesFromTimetableDataManager(tdm);		

		mPager.setCurrentItem(mAdapter.getCount() - 2);
		mPager.setOffscreenPageLimit(3);
		//mPager.setOnPageChangeListener(onDefaultPageChangeListener);

		//페이져의 리스너는 인디케이터가 쓰고 있으므로 따로 인디케이터에 원래 페이저용 리스너 등록.
		mIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(onDefaultPageChangeListener);

		dogEar = (ImageView)findViewById(R.id.dogear_to_store);
		dogEar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(NaverInApp.isNaverApk == false){
					Intent intent = new Intent(TimetableActivity.this, StoreActivity.class);
					startActivity(intent);
				}else if(NaverInApp.isNaverApk == true){
					Intent intent = new Intent(TimetableActivity.this, NaverStoreActivity.class);
					startActivity(intent);

				}

				Map<String, String> info = new HashMap<String, String>();
				info.put(FlurryConstants.STORE_CLICKTYPE_KEY, FlurryConstants.STORE_CLICKTYPE_DOGEAR);
				FlurryAgent.logEvent(FlurryConstants.STORE_CLICKED, info);
			}
		});
		if(TimetableDataManager.getCurrentFullVersionState(this) == false){
			//if not full version...
			MyLog.d(TAG, "Not Full Version local");
			setupAdView();
			dogEar.setVisibility(View.VISIBLE);
		}else{
			//if full version...
			MyLog.d(TAG, "Full Version local");
			removeAdView();
			dogEar.setVisibility(View.GONE);
		}

		initFirstLaunch();
		initConnectorBanner();
		LanguageInitiater.setActivityLanguage(this);
		FixedSizes.ACTIONBAR_HEIGHT = getActionBarHeight();
		MyLog.d("FixedSizes", "Actionbar size : " + FixedSizes.ACTIONBAR_HEIGHT);

        initQuitAdView();
        AdUtils.showPopupAdIfSatisfied(this);
    }

	private void initActionBar(){
		// 2) Set your display to custom next
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// 3) Do any other config to the action bar
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
		// 4) Now set your custom view
		mActionBar.setCustomView(R.layout.actionbar_timetable_customlayout);
		mActionBarTitle = (TextView) mActionBar.getCustomView().findViewById(R.id.menu_title);		
		mActionBarTitle.setText(TimetableDataManager.getMainTimetable().getTitle());

		String editYourTitle = getResources().getString(R.string.edit_your_timetable_title);
		mActionBarTitle.setOnClickListener(new TitleTextViewOnClickListener(editYourTitle));
	}

	private void initDrawer(){
		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); 
		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);
		//     // Set a custom shadow that overlays the main content when the drawer
		//        // opens
		//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		//                GravityCompat.START);

		String addTable = getResources().getString(R.string.drawer_add_timetable);
		String overlapTable = getResources().getString(R.string.drawer_overlap_timetables);
		String showAllTable = getResources().getString(R.string.drawer_show_all_timetables);
		DrawerItem[] items = new DrawerItem[]{
				new DrawerItem(addTable, R.drawable.yt_icon_add_addtable_theme_a),
				new DrawerItem(overlapTable, R.drawable.yt_icon_overlap_theme_a),
				new DrawerItem(showAllTable, R.drawable.yt_icon_add_showall_theme_a)
		};
		mDrawerAdapter = new DrawerListAdapter(this, items);

		mDrawerList.setAdapter(mDrawerAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.Tuesday,
				R.string.TUE) {
			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
				processOnDrawerClosed();
				MyLog.d("onDrawerClosed", "CLOSED");
			}
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				//                getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void initFirstLaunch(){
		if(TimetableDataManager.getIsFirstLaunch(this) == true ){
			StartTutorialDialogBuilder.createDialog(this).show();
			if(TimetableDataManager.getSchedules().isEmpty() == true){
				addDefaultSchedule(); 
			}
		}
	}

	private void initConnectorBanner(){
		String uuid = new DeviceUuidFactory(this).getDeviceUuid().toString();
		String name = UserNameFactory.getUserName(this);
		
		TimetableNetworkManager.getBannerInfo(uuid, name, this, new TimetableNetworkManager.OnFinishedBannerInfoAsync(){
			@Override
			public void onFinished(BannerInfo bi, boolean isSucceed) {
				// TODO Auto-generated method stub
				if(isSucceed == true){
					int currentVersion = TimetableDataManager.getConnectorBannerVersion(TimetableActivity.this);
					MyLog.d("TimetableActivity", 
							"current banner version : " + currentVersion 
							+ ", downloaded banner version : " + bi.getVersion());
					MyLog.d("TimetableActivity", 
							"BannerInfo : " + bi.getVersion() + ", " + "image url : " 
					+ bi.getImageUrl() + ", link url : " 
					+ bi.getLinkUrl());
					if(bi.getVersion() > currentVersion){
						new DownloadImageTask(bi).execute();
					}
				}else{
					MyLog.d("TimetableActivity", "connector banner info download failed");
				}
			}
		});
	}

    private void initQuitAdView() {
        // 애드몹 - Quit Dialog
        quitAdRequest = new AdRequest.Builder().build();
        quitAdView = QuitAdDialogFactory.initAdView(this, AdSize.MEDIUM_RECTANGLE, quitAdRequest);
    }

	private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
		private BannerInfo bi;
		public DownloadImageTask(BannerInfo bi){
			this.bi = bi;
		}
		protected Bitmap doInBackground(Void... params) {
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(bi.getImageUrl()).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}
		protected void onPostExecute(Bitmap result) {
			MyLog.d("TimetableActivity", "Banner Image Downloaded, url : " + bi.getImageUrl());
			TimetableDataManager.saveConnectorBannerBitmap(TimetableActivity.this, result);
			TimetableDataManager.setConnectorBannerVersion(
					TimetableActivity.this, bi.getVersion());
			TimetableDataManager.setConnectorBannerLinkUrl(
					TimetableActivity.this, bi.getLinkUrl());
		}
	}

	private void addDefaultSchedule(){
		Schedule newSchedule =  new Schedule();		
		//1.스케쥴 이름 저장
		newSchedule.setScheduleName("Schedule");
		//2.스케쥴 메모 저장
		newSchedule.setScheduleMemo("Write Your Memo!");
		//3.스케쥴 날짜 저장
		Calendar c = GregorianCalendar.getInstance();
		newSchedule.setScheduleYear(c.get(GregorianCalendar.YEAR));
		newSchedule.setScheduleMonth(c.get(GregorianCalendar.MONTH));
		newSchedule.setScheduleDay(c.get(GregorianCalendar.DAY_OF_MONTH));
		//4.스케쥴 시간 저장
		newSchedule.setScheduleHour(c.get(GregorianCalendar.HOUR_OF_DAY));
		newSchedule.setScheduleMin(c.get(GregorianCalendar.MINUTE));

		newSchedule.setLastUpdated(Calendar.getInstance().getTimeInMillis());

		TimetableDataManager.getInstance().putSchedule(newSchedule);

	}

	private int getActionBarHeight() {
		int actionBarHeight = getSupportActionBar().getHeight();
		if (actionBarHeight != 0)
			return actionBarHeight;
		final TypedValue tv = new TypedValue();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
				actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
		} else if (getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true))
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
		return actionBarHeight;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		MyLog.d(TAG, "onConfigurationChanged");
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(android.widget.AdapterView<?> parent,
				View view, int position, long id) {
			// TODO Auto-generated method stub
			selectItem(position);
		}
	}

	private int drawerSelected = -1;
	private void selectItem(int position) {		
		drawerSelected = position;
		if(position == 0){
			if( ( TimetableDataManager.getTimetables().size() == 4 ) &&
					( TimetableDataManager.getCurrentFullVersionState(this) == false ) ){
				ToastMaker.popupUnlockFullVersionToast(getSupportApplication(),
						ToastMaker.UNLOCK_FULL_VERSION_TOAST_OVERFLOW_PAGENUM);
			}else if(TimetableDataManager.getTimetables().size() >= TIMETABLE_MAX_LIMIT){
				String warn = getString(R.string.activity_timetable_max_timetable_count);
				ToastMaker.popupToastAtCenter(this, warn);
			}else{
				showAddTableProgressDialog();
			}
		}
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
		mDrawerList.clearChoices();
		mDrawerList.setItemChecked(0, false);
		mDrawerList.setItemChecked(1, false);
		mDrawerList.setItemChecked(2, false);
		mDrawerList.post(new Runnable() {
			@Override
			public void run() {
				mDrawerList.setChoiceMode(ListView.CHOICE_MODE_NONE);
			}
		});
		//		mDrawerList.requestLayout();
	}

	private void processOnDrawerClosed(){
		switch(drawerSelected){
		case -1 : 
			return;
		case 0 : 
			if( ( TimetableDataManager.getTimetables().size() == 4 ) &&
					( TimetableDataManager.getCurrentFullVersionState(this) == false ) ){
				//				ToastMaker.popupUnlockFullVersionToast(getSupportApplication(),
				//						ToastMaker.UNLOCK_FULL_VERSION_TOAST_OVERFLOW_PAGENUM);
				return;
			}else if(TimetableDataManager.getTimetables().size() >= TIMETABLE_MAX_LIMIT){

				//				ToastMaker.popupToastAtCenter(this, warn);
				return;
			}
			//ta.addPage(TimetableFragment.newInstance(new Timetable(5,10,60)), 1);

			//				TimetableDataManager.getInstance().addTimetableAtHead(
			//						new Timetable(Timetable.MON, Timetable.FRI, Timetable.DEFAULT_PERIOD_NUM)
			//						);
			addPageAt(TimetableActivity.TIMETABLE_PAGE_OFFSET, null, false);
			TimetableDataManager.writeDatasToExternalStorage();
			getViewPager().post(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getViewPager().setCurrentItem(
							TimetableActivity.TIMETABLE_PAGE_OFFSET,
							false);
				}
			});
			FlurryAgent.logEvent(FlurryConstants.TIMETABLE_ADDED);
			break;
		case 1 : 
			Intent overlapIntent = new Intent(
					this, OverlapTablesViewerActivity.class);

			ArrayList<Integer> checkedItems = new ArrayList<Integer>();
			for(int i = 0; i < TimetableDataManager.getTimetables().size() ; i++){
				checkedItems.add(i);
			}
			overlapIntent.putExtra("OverlapIndex", checkedItems);
			overlapIntent.putExtra("DirectOverlap", true);

			this.startActivityForResult(
					overlapIntent, RequestCodes.CALL_ACTIVITY_OVERLAP);

			Map<String, String> currentPageNumber = 
					new HashMap<String, String>();
			int timetableNum = TimetableDataManager.getTimetables().size();
			currentPageNumber.put(FlurryConstants.CURRENT_TIMETABLE_PAGE_NUM, 
					Integer.toString(timetableNum));

			FlurryAgent.logEvent(FlurryConstants.OVERLAP_PAGE_CLICKED, currentPageNumber);
			break;

		case 2 : 
			Intent showAllIntent = new Intent(
					this, ShowAllTimetablesActivity.class);

			showAllIntent.putExtra("ShowAll", true);
			this.startActivityForResult(
					showAllIntent, RequestCodes.CALL_ACTIVITY_SHOW_ALL);
			FlurryAgent.logEvent(FlurryConstants.SHOWALL_PAGE_CLICKED);
			break;
		}
		drawerSelected = -1;
	}

	private void setupAdView(){
		adView = (AdView) findViewById(R.id.adView);
		adView.setBackgroundColor(Color.TRANSPARENT);
		AdRequest request = new AdRequest.Builder().build();
		
		//		request.addTestDevice(AdRequest.TEST_EMULATOR);
		adView.loadAd(request);
	}

	private void removeAdView(){
		adView = (AdView) findViewById(R.id.adView);
		adView.setVisibility(View.GONE);
	}

	public void onStart(){
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryConstants.APP_KEY);
	}

	public void onStop(){
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	public void onPause(){
		super.onPause();
		MyLog.d(TAG, "onPause called, FILE SAVED");
		TimetableDataManager.onPause();
	}

	public void onResume(){
		super.onResume();
		if(TimetableDataManager.getCurrentFullVersionState(this) == true){
			removeAdView();
			dogEar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy(){
		adView.removeAllViews();
		adView.destroy();
		YTAppWidgetProvider_2x4.onTimetableDataChanged(this);
		YTAppWidgetProvider_4x4.onTimetableDataChanged(this);
		//		Debug.stopMethodTracing();
		//		TimetableDataManager.getInstance().onDestroy();
		//		YTAppWidgetProvider_2x4.onTimetableDataChanged(this);
		//		YTAppWidgetProvider_4x4.onTimetableDataChanged(this);
		super.onDestroy();
	}

	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//		if (keyCode == KeyEvent.KEYCODE_BACK) {
	//			//Display confirmation here, finish() activity.
	//			YTAppWidgetProvider_2x4.onTimetableDataChanged(getApplicationContext());
	//			YTAppWidgetProvider_4x4.onTimetableDataChanged(getApplicationContext());
	//			//	        return true;
	//		}
	//		return super.onKeyDown(keyCode, event);
	//	}

	private ShareActionProvider mShareActionProvider;
	private Menu menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/** Create an option menu from res/menu/items.xml */
		MyLog.d("onCreateOptionsMenu", "Called");

		// 1)Inflate menu resource file.
		getSupportMenuInflater().inflate(R.menu.actionbar_timetable_menu, menu);
		// 2)Initialize action bar, if not called in onCreateOptionsMenu
		//and called in onCreate(), Custom view is not showing.
		//strange...
		initActionBar();

		this.menu = menu;
		//init share item
		MenuItem item = menu.findItem(R.id.menu_item_share);

		// Locate MenuItem with ShareActionProvider
		if( mPager.getCurrentItem() == mAdapter.getCount() - 1){
			item.setVisible(false);
		}else{
			item.setVisible(true);
		}

		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();

		/** Getting the target intent */
		Intent intent = getShareIntent();

		/** Setting a share intent */
		if(intent!=null)
			mShareActionProvider.setShareIntent(intent);
		mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
			@Override
			public boolean onShareTargetSelected(ShareActionProvider actionProvider, Intent intent) {
				saveImageToSD();
				return false;
			}
		});
		//init overflow menu
		//		MenuItem overflowItem = menu.findItem(R.id.menu_item_overflow);
		MenuItem storeItem = menu.findItem(R.id.menu_item_store);
		storeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				if(NaverInApp.isNaverApk == false){
					Intent intent = new Intent(TimetableActivity.this, StoreActivity.class);
					startActivity(intent);
				}else if(NaverInApp.isNaverApk == true){
					Intent intent = new Intent(TimetableActivity.this, NaverStoreActivity.class);
					startActivity(intent);
				}

				Map<String, String> info = new HashMap<String, String>();
				info.put(FlurryConstants.STORE_CLICKTYPE_KEY, FlurryConstants.STORE_CLICKTYPE_MENUITEM);
				FlurryAgent.logEvent(FlurryConstants.STORE_CLICKED, info);
				return true;
			}
		});
		MenuItem settingItem = menu.findItem(R.id.menu_item_settings);
		if(	mPager.getCurrentItem() == mAdapter.getCount() - 1){
			settingItem.setVisible(false);
		}else{
			settingItem.setVisible(true);
		}
		settingItem.setOnMenuItemClickListener(new OnSettingsMenuItemClickedListener(
				mPager.getCurrentItem()));
		
		MenuItem languageItem = menu.findItem(R.id.menu_item_language);
		languageItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				String langTitle = getString(R.string.activity_setting_viewpager_languageprompt);
				SettingLanguageDialogCreator.showSettingsLanguageListDialog(TimetableActivity.this, 
						langTitle,
						new SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener(){

					@Override
					public void onClick(int clickedItemPosition) {
						// TODO Auto-generated method stub
						onLanguageChanged(clickedItemPosition);
					}
				});
				return true;
			}
		});

		MenuItem sendFeedback = menu.findItem(R.id.menu_item_send_feedback);
		sendFeedback.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				String sendFeedSubject = getString(R.string.send_feedback_subject);
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("*/*");
				//				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(crashLogFile));
				i.putExtra(Intent.EXTRA_EMAIL, new String[] {
						YTUrls.SEND_FEEDBACK_EMAIL
				});
				i.putExtra(Intent.EXTRA_SUBJECT, sendFeedSubject);
				//				i.putExtra(Intent.EXTRA_TEXT, "Some crash report details");

				String mailUsTitle = getString(R.string.send_feedback_title);
				startActivity(createEmailOnlyChooserIntent(i, mailUsTitle));
				return true;
			}
		});

		MenuItem help = menu.findItem(R.id.menu_item_help);
		help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				// http://www.yooiistudios.com/timetable/help.php
				Intent browserIntent;
				if(YTLanguageType.JAPANESE == 
						YTLanguage.getCurrentLanguageType(TimetableActivity.this)){
					browserIntent = new Intent(Intent.ACTION_VIEW, 
							Uri.parse(YTUrls.TIMETABLE_HELP_URL_JP));
				}else{
					browserIntent = new Intent(Intent.ACTION_VIEW, 
							Uri.parse(YTUrls.TIMETABLE_HELP_URL));
				}						
				startActivity(browserIntent);

				return true;
			}
		});
		
		MenuItem recommendFriends = menu.findItem(R.id.menu_item_recommend);
		recommendFriends.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				String recommendToFriendsSubject = getString(R.string.recommend_to_friends_subject);
				String recommendToFriendsBody = getString(R.string.recommend_to_friends_body)
						+ "\n"
						+ "https://play.google.com/store/apps/details?id=com.sulga.yooiitable";
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				//				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(crashLogFile));

				i.putExtra(Intent.EXTRA_SUBJECT, recommendToFriendsSubject);
				i.putExtra(Intent.EXTRA_TEXT, recommendToFriendsBody);
				//				i.putExtra(Intent.EXTRA_TEXT, "Some crash report details");

				String sendToFriendsTitle = getString(R.string.menu_recommend_friends);
				startActivity(Intent.createChooser(i, sendToFriendsTitle));

				return true;
			}
		});

		// Return true to display menu
		return true;
	}
	private void refreshTextForLocaleChange(){
		//		if(menu != null){
		//			MenuItem storeItem = menu.findItem(R.id.menu_item_store);
		//			storeItem.setTitle(R.string.menu_store);
		//			MenuItem settingItem = menu.findItem(R.id.menu_item_settings);
		//			settingItem.setTitle(R.string.menu_settings);
		//			MenuItem sendFeedback = menu.findItem(R.id.menu_item_send_feedback);
		//			sendFeedback.setTitle(R.string.menu_send_feedback);
		//			MenuItem help = menu.findItem(R.id.menu_item_help);
		//			help.setTitle(R.string.menu_help);
		//
		//			String editYourTitle = getResources().getString(R.string.edit_your_timetable_title);
		//			mActionBarTitle.setOnClickListener(new TitleTextViewOnClickListener(editYourTitle));
		//			String addingTableString = getResources()
		//					.getString(R.string.fragment_timetable_notice_adding_timetable);
		//			addTableProgressDialog.setMessage(addingTableString);
		//			
		//			if(mDrawerList != null){
		//				String addTable = getResources().getString(R.string.drawer_add_timetable);
		//				String showAllTable = getResources().getString(R.string.drawer_show_all_timetables);
		//				String overlapTable = getResources().getString(R.string.drawer_overlap_timetables);
		//				DrawerItem[] items = new DrawerItem[]{
		//						new DrawerItem(addTable, R.drawable.yt_icon_add_addtable_theme_a),
		//						new DrawerItem(showAllTable, R.drawable.yt_icon_add_showall_theme_a),
		//						new DrawerItem(overlapTable, R.drawable.yt_icon_overlap_theme_a)
		//				};
		//				mDrawerAdapter = new DrawerListAdapter(this, items);
		//				mDrawerList.setAdapter(mDrawerAdapter);
		//			}			
		//		}
		Intent intent = new Intent(TimetableActivity.this, TimetableActivity.class);
		startActivity(intent);
		finish();
	}

	public Intent createEmailOnlyChooserIntent(Intent source,
			CharSequence chooserTitle) {
		Stack<Intent> intents = new Stack<Intent>();
		Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
				YTUrls.SEND_FEEDBACK_EMAIL, null));
		List<ResolveInfo> activities = getPackageManager()
				.queryIntentActivities(i, 0);

		for(ResolveInfo ri : activities) {
			Intent target = new Intent(source);
			target.setPackage(ri.activityInfo.packageName);
			intents.add(target);
		}

		if(!intents.isEmpty()) {
			Intent chooserIntent = Intent.createChooser(intents.remove(0),
					chooserTitle);
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
					intents.toArray(new Parcelable[intents.size()]));

			return chooserIntent;
		} else {
			return Intent.createChooser(source, chooserTitle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyLog.d("onOptionsItemSelected", "clicked");
		switch (item.getItemId()) {
		case android.R.id.home:
			MyLog.d("onOptionsItemSelected", "home clicked");
			if (mDrawerLayout.isDrawerVisible(Gravity.LEFT)) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			} else {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	/** Returns a share intent */
	private Intent getShareIntent(){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);

		File sdCard = Environment.getExternalStorageDirectory();

		File sharedFile = new File(sdCard+"/Yooiitable/shareImage.png");
		Uri uri = Uri.fromFile(sharedFile);

		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	private void saveImageToSD() {

		if(mPager.getCurrentItem() == mAdapter.getCount() - 1){
			MyLog.d("ShareAction", "THIS IS NOT TIMETABLE PAGE");
			return;
		}
		TimetableFragment currentFrag = (TimetableFragment) 
				mAdapter.getItem(mPager.getCurrentItem());
		Bitmap bm = currentFrag.getTimetableShareImageBitmap();

		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(getTempFile());
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			bm.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getTempFile() {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			File directory = new File(Environment.getExternalStorageDirectory() + "/Yooiitable/");
			directory.mkdirs();

			File file = new File(directory ,"shareImage.png");

			try  {
				file.createNewFile();
			}  catch (IOException e) {}

			return file;
		} else  {
			return null;
		}
	} 
	private class TitleTextViewOnClickListener implements View.OnClickListener{
		private String title="";

		private String editTextInitial="";
		public TitleTextViewOnClickListener(String title){
			this.title = title;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final int currentPage = mPager.getCurrentItem();

			if(currentPage == mAdapter.getCount() - 1){
				return;
			}
			final Timetable timetable = TimetableDataManager
					.getTimetables().get(currentPage - TIMETABLE_PAGE_OFFSET);

			if(title == null){
				title = "";
			}

			Dialog dialog = AlertDialogCreator.getSimpleEditTextAlertDialog(
					getSupportActionBarContext(), 
					InputType.TYPE_CLASS_TEXT,
					null,
					timetable.getTitle(), 
					title, 
					new AlertDialogCreator.EditTextDialogOnClickListener() {

						@Override
						public void onClick(EditText editText, Dialog d) {
							// TODO Auto-generated method stub
							String title = editText.getText().toString();
							timetable.setTitle(title);
							mActionBarTitle.setText(title);
							//					TimetableDataManager.writeDatasToExternalStorage();
							d.dismiss();
						}
					},
					new AlertDialogCreator.EditTextDialogOnClickListener() {

						@Override
						public void onClick(EditText editText, Dialog d) {
							// TODO Auto-generated method stub
							d.dismiss();
						}
					});
			//			final EditText input = new EditText(getSupportActionBarContext());
			//			input.setSelectAllOnFocus(true);
			//			input.setSingleLine();
			//			if(timetable.getTitle() != null){
			//				editTextInitial = timetable.getTitle();
			//			}
			//			input.setText(editTextInitial);
			//
			//			// set title
			//			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			//					getSupportActionBarContext());
			//			// set title
			//			if(title != null)
			//				alertDialogBuilder.setTitle(title);
			//			// set dialog message
			//			alertDialogBuilder
			//			.setCancelable(true)
			//			.setView(input)
			//			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			//
			//				@Override
			//				public void onClick(DialogInterface dialog, int which) {
			//					// TODO Auto-generated method stub
			//					//					Timetable timetable = getTimetableDataFromManager();
			//
			//					String title = input.getText().toString();
			//					timetable.setTitle(title);
			//					mActionBarTitle.setText(title);
			//
			//					TimetableDataManager.writeDatasToExternalStorage();
			//				}
			//			})
			//			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			//				@Override
			//				public void onClick(DialogInterface dialog, int which) {}
			//			}); 
			//			// create alert dialog
			//			final AlertDialog dialog = alertDialogBuilder.create();
			//			//에딧텍스트가 포커스를 받으면 키보드를 보여준다.
			//			input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			//				@Override
			//				public void onFocusChange(View v, boolean hasFocus) {
			//					if (hasFocus) {
			//						dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			//					}
			//				}
			//			});
			dialog.show();

		}

	}
	public MyFragmentPagerAdapter getPagerAdapter(){
		return mAdapter;
	}

	public ViewPager getViewPager(){
		return mPager;
	}

	public void makePagesFromTimetableDataManager(TimetableDataManager tdm){
		ArrayList<Timetable> timetables = TimetableDataManager.getTimetables();

		if(timetables == null){
			Toast.makeText(this,
					"Sorry, Timetable datas are broken. Deleting every broken datas.", 
					Toast.LENGTH_LONG).show();
			TimetableDataManager.makeDefaultTimetableDatas();
			TimetableDataManager.writeDatasToExternalStorage();
			timetables = TimetableDataManager.getTimetables();
		}
		for(int i = 0; i < timetables.size() ; i++){
			mAdapter.addPage(TimetableFragment.newInstance());
		}
		mAdapter.addPage(ScheduleFragment.newInstance());
		//		mAdapter.addPage(AddTableFragment.newInstance(),0,false);
	}

	public boolean refreshTimetablePage(int pageIndex){
		if(pageIndex == 0 || pageIndex > TimetableDataManager.getTimetables().size()){
			return false;
		}
		Fragment f = mAdapter.getItem(pageIndex);
		if(f instanceof TimetableFragment){
			((TimetableFragment) f).refreshLessonViews();
			return true;
		}
		return false;
	}
	

	public void onLanguageChanged(int position){
		YTLanguage.setLanguageType(YTLanguageType.valueOf(position), this);
		// update locale
		YTLanguageType currentLanguageType = YTLanguage.getCurrentLanguageType(this);
		Locale locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
		refreshTextForLocaleChange();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		//this.onActivityResult(requestCode, resultCode, data)
		if(requestCode == RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING){
			if(resultCode == android.app.Activity.RESULT_OK){
				MyLog.d("onActivityResult", "EDIT_TIMETABLE_END!!!!!");
				/*MyLog.d("onActivityResult", 
						"start : " + ((Timetable) data.getParcelableExtra("Timetable")).getStartDay() 
						+ ", end : " + ((Timetable) data.getParcelableExtra("Timetable")).getEndDay());*/

				/**
				 * 데이터매니저가 관리하게 되었으므로 세팅바뀐후 타임테이블객체 다시 세팅하는건 패스해도 될듯?
				 */
				/*	((TimetableFragment)mAdapter.getItem(mPager.getCurrentItem()))
					.setTimetable((Timetable) data.getParcelableExtra("Timetable"));*/
				//				int pageIndex = data.getIntExtra("TimetablePageIndex", -1);
				//				if(pageIndex < 0){
				//					mAdapter.notifyDataSetChanged();
				//				}else{
				//					Fragment f = mAdapter.getItem(pageIndex);
				//					if(f instanceof TimetableFragment){
				//						((TimetableFragment) f).refreshEverything();
				//					}
				//				}
				int currentPage = data.getIntExtra("TimetablePageIndex", -1);
				MyLog.d(TAG, "Current Page : " + currentPage);
				mPager.setAdapter(null);
				mPager.setAdapter(mAdapter);
				if(currentPage < 0){
					mPager.setCurrentItem(mAdapter.getCount() - 2);
				}else{
					mPager.setCurrentItem(currentPage);
				}
//				boolean languageChanged = data.getBooleanExtra("LanguageChanged", false);
//				if(languageChanged == true){
//					refreshTextForLocaleChange();
//				}
			}
		}
		//		else if(requestCode == RequestCodes.CALL_ACTIVITY_EDIT_SCHEDULE_ACTIVITY || 
		//				requestCode == RequestCodes.CALL_ACTIVITY_ADD_SCHEDULE_ACTIVITY){
		//			if(resultCode == Activity.RESULT_OK){
		//				//Schedule s = data.getParcelableExtra("Schedule");
		//				final ScheduleFragment sf = (ScheduleFragment)mAdapter.getItem(mPager.getCurrentItem());
		//				//sf.refresh();
		//
		//				mAdapter.notifyDataSetChanged();
		//
		//				String scheduleKey = data.getStringExtra("ScheduleKey");
		//				int scheduleIndex = data.getIntExtra("ScheduleIndex", -1);
		//				final Schedule s = TimetableDataManager.getInstance().getScheduleListFromKey(scheduleKey).get(scheduleIndex);
		//
		//				sf.getView().post(new Runnable(){
		//
		//					@Override
		//					public void run() {
		//						// TODO Auto-generated method stub
		//						sf.startAddAndEditScheduleAnimation(s);
		//					}
		//
		//				});
		//			}
		//		}
		else if(requestCode == RequestCodes.GCACCOUNT_REQUEST_CODE_PICK_ACCOUNT){
			if (resultCode == RESULT_OK) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				//AccountUtils.setAccountName(this, accountName);
				//어카운트 네임을 세이브한다.
				//그리고 구글캘린더로 올리는 다이얼로그 보여주기.
				GCAccountManager.getInstance().showSyncGoogleCalendarDialog(this, accountName);

			} else if (resultCode == RESULT_CANCELED) {
				String warn = getResources()
						.getString(R.string.warning_gc_sync_must_have_google_account);
				Toast.makeText(
						this, 
						//						"구글 계정이 있어야 싱크가 가능합니다.", 
						warn,
						Toast.LENGTH_SHORT).
						show();
				//finish();
			}
			return;
		}else if (requestCode == RequestCodes.GCACCOUNT_REQUEST_CODE_RECOVER_PLAY_SERVICES){
			String warn = getResources()
					.getString(R.string.warning_gc_sync_must_have_latest_google_play);
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(
						this, 
						//						"싱크를 위해 최신버전의 구글 플레이 서비스가 설치되어 있어야 합니다.",
						warn,
						Toast.LENGTH_SHORT)
						.show();
				//finish();
			}
		}else if(requestCode == RequestCodes.GCACCOUNT_REQUEST_CODE_AUTHORIZATION){
			if(resultCode == RESULT_OK){
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				String loading = getResources().getString(R.string.loading);
				String startingConnection = getResources().getString(R.string.starting_connection);
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setTitle(loading);
				progressDialog.setMessage(startingConnection);
				progressDialog.setCancelable(false);
				//progressDialog.setMax(3);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();

				GCCalendarSyncManager.startSync(this, accountName, progressDialog);
			}else if(resultCode == RESULT_CANCELED){
				String cancelled = getResources().getString(R.string.cancelled);
				Toast.makeText(
						this, 
						//						"접근 권한을 허용하셔야 싱크가 가능합니다.",
						cancelled,
						Toast.LENGTH_LONG)
						.show();
			}
		}else if(requestCode == RequestCodes.CALL_ACTIVITY_SHOW_ALL){
			if(resultCode == RESULT_OK){
				int idx = data.getIntExtra(
						"SelectedTimetableIndex", mPager.getChildCount() - 1);
				mPager.setCurrentItem(idx + TIMETABLE_PAGE_OFFSET);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private boolean removingPage = false;
	private int removingPosition = -1;
	public void removePageAt(int position){
		//removePage = true;
		////////////////여기서 애니메이션으로 마치 삭제하는듯 눈속임 후, 페이지스크롤 완료시 애니메이션을 다시 디폴트로!
		//mPager.setPageTransformer(true, new OnDeletePageTransformer());
		//화면이 넘어가는 최소 거리를 계산하기 위해서 받아온다.
		//fakeDragPagerToRight();
		//mPager.setOnPageChangeListener(onDeletePageChangeListener);

		//post must be called, beacuse if not 
		//viewpager fakedrag starts immediately whether 
		//the dialog dismiss delay is finished or not, which means
		//some of the fake dragging is not being called and page dragging
		//finishes perfectly.
		mPager.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ViewPagerFakeDragger.goToRightPage(
						TimetableActivity.this, true, 
						new OnDeletePageTransformer(), 
						mPager, getResources().getDimension(R.dimen.viewpager_minimum_fling_distance),
						ViewPagerFakeDragger.DEFAULT_DELAY_MILLIS);
			}
		});
		mIndicator.setOnPageChangeListener(onDeletePageChangeListener);
		//MyLog.d("RemovePage", "Go To Right Page Done!");
		//mPager.setPageTransformer(false, null);
		removingPage = true;
		removingPosition = position;
	}

	private void showAddTableProgressDialog(){
		if(addTableProgressDialog.isShowing() == false){
			addTableProgressDialog.show();
		}
	}
	private void hideAddTableProgressDialog(){
		if(addTableProgressDialog.isShowing() == true){
			addTableProgressDialog.dismiss();
		}
	}

	//	private ProgressDialog pd;
	public void addPageAt(final int position, Timetable timetable, boolean goToRightPage){
		showAddTableProgressDialog();

		if(timetable == null){
			TimetableDataManager.addTimetableAtHead(
					new Timetable(Calendar.MONDAY, Calendar.SATURDAY, Timetable.DEFAULT_PERIOD_NUM));
		}else{
			TimetableDataManager.addTimetableAtHead(timetable);
		}
		TimetableDataManager.writeDatasToExternalStorage();

		if(goToRightPage == true){
			ViewPagerFakeDragger.goToRightPage(
					this, true,
					null,
					mPager, getResources().getDimension(R.dimen.viewpager_minimum_fling_distance) / 4
					, ViewPagerFakeDragger.SLOW_DELAY_MILLIS);
			mIndicator.setOnPageChangeListener(onAddPageChangeListener);
		}
		if(goToRightPage == false){
			mAdapter.addPage(TimetableFragment.newInstance(), TIMETABLE_PAGE_OFFSET, true);
		}else{
			mAdapter.addPage(TimetableFragment.newInstance(), TIMETABLE_PAGE_OFFSET, false);
		}
		mPager.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//				mPager.setCurrentItem(tmpCurrPage + 1);
				hideAddTableProgressDialog();
			}

		});
	}

	public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{  
		private List<Fragment> fragments;
		private FragmentManager fm;
		public MyFragmentPagerAdapter(FragmentManager fm) { 
			super(fm);  
			this.fm = fm;
			this.fragments = new ArrayList<Fragment>();
		} 
		@Override  
		public Fragment getItem(int index) {  
			MyLog.d("adapter", "getItem index : " + index + ", fragments length : " + fragments.size());
			return this.fragments.get(index);
		}
		@Override  
		public int getCount() {  
			return this.fragments.size();
		}  

		@Override
		public float getPageWidth(int position) {
			//			if(position == 0){
			//				float nbPages = 4; // You could display partial pages using a float value
			//				return (1 / nbPages);
			//			}else{
			return super.getPageWidth(position);
			//			}		    
		}
		public void addPage(Fragment fragment, int position, boolean keepCurrentPage){
			int currPage = mPager.getCurrentItem();
			mPager.setAdapter(null);
			fragments.add(position, fragment);
			mPager.setAdapter(this);
			if(keepCurrentPage == true)
				mPager.setCurrentItem(currPage + 1);
			//			mPager.setCurrentItem(position);
			//notifyDataSetChanged();
			Log.e("PagerAdapter", fragments.size()+" Pages");
		}
		public void addPage(Fragment fragment){
			mPager.setAdapter(null);
			fragments.add(fragment);
			mPager.setAdapter(this);
			//notifyDataSetChanged();
			Log.e("PagerAdapter", fragments.size()+" Pages");
		}
		public int removePage(int position){
			mPager.setAdapter(null);
			fragments.remove(position);

			mPager.setAdapter(this);
			//notifyDataSetChanged();
			Log.e("PagerAdapter", fragments.size() + " Pages, " + position + " position table removed");
			return position;
		}
		public int indexOfFragmentPage(Fragment f){
			for(int i = 0 ; i < fragments.size() ; i++){
				if(f.equals(fragments.get(i))){
					return i;
				}
			}
			MyLog.d("indexOfFragmentPage", "-1");
			return -1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			/***페이지 넘기면서 뷰 재사용시에 SavedInstance가 mess up. 
			저장된 타임테이블 데이터가 재사용되며 완전히 꼬여버린다!!! */
		}
	}  

	private class OnSettingsMenuItemClickedListener implements MenuItem.OnMenuItemClickListener{
		int pageIndex;
		OnSettingsMenuItemClickedListener(int pageIndex){
			this.pageIndex = pageIndex;
		}
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			Intent act = new Intent(TimetableActivity.this, TimetableSettingFragment.class);
			act.putExtra("TimetablePageIndex", pageIndex);
			startActivityForResult(act, RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING);

			Map<String, String> info = new HashMap<String, String>();
			info.put(FlurryConstants.SETTING_CLICKTYPE_KEY, 
					FlurryConstants.SETTING_CLICKTYPE_MENUITEM);
			FlurryAgent.logEvent(FlurryConstants.SETTING_CLICKED);
			return true;
		}
	}

	ViewPager.OnPageChangeListener onDeletePageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if(position == mAdapter.getCount() - 1){
				((ScheduleFragment)mAdapter.getItem(position)).refresh();
				MyLog.d("onPageChangeListener", "Schedule page refreshed!");
			}else{
				Fragment f = mAdapter.getItem(position);
				if(f instanceof TimetableFragment){
					((TimetableFragment) f).onPageSelected();
				}
			}
			setActionBarTitle(position);
			Log.e("onPageChangeListnter", "selected page : " + position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			if( state == android.support.v4.view.ViewPager.SCROLL_STATE_IDLE &&
					removingPage == true){

				//페이지 삭제를 위해 한 페이지 오른쪽으로 이동해져있는 상태.
				//데이터 삭
				Timetable timetableToRemove =
						TimetableDataManager.getInstance()
						.getTimetableAtPage(removingPosition);
				TimetableDataManager.getTimetables().remove(timetableToRemove);				

				final int pageIdx = 
						mAdapter.removePage(removingPosition);
				MyLog.d("RemovePage", "removed Page : " + removingPosition);

				//					mPager.post(new Runnable(){
				//						@Override
				//						public void run() {
				//							// TODO Auto-generated method stub
				//							mPager.setCurrentItem(pageIdx);
				//							
				//							removingPosition = -1;
				//						}						
				//					});		
				mPager.setCurrentItem(pageIdx);
				removingPosition = -1;
				removingPage = false;
				mPager.setPageTransformer(true, null);
				mIndicator.notifyDataSetChanged();
				mIndicator.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mPager.disablePaging(false);
					}
				});

				//				TimetableDataManager.writeDatasToExternalStorage();
				//removingPosition = -1;
			}
		}
	};

	ViewPager.OnPageChangeListener onAddPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if(position == mAdapter.getCount() - 1){
				((ScheduleFragment)mAdapter.getItem(position)).refresh();
				MyLog.d("onPageChangeListener", "Schedule page refreshed!");
			}else{
				Fragment f = mAdapter.getItem(position);
				if(f instanceof TimetableFragment){
					((TimetableFragment) f).onPageSelected();
				}
			}
			setActionBarTitle(position);

			Log.e("onPageChangeListnter", "selected page : " + position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			if( state == android.support.v4.view.ViewPager.SCROLL_STATE_IDLE){
				mPager.setPageTransformer(true, null);
				mIndicator.notifyDataSetChanged();
				mIndicator.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub

						mPager.disablePaging(false);
					}

				});

				//				//페이지 삭제를 위해 한 페이지 오른쪽으로 이동해져있는 상태.
				//				//데이터 삭
				//				Timetable timetableToRemove =
				//						TimetableDataManager.getInstance().getTimetableAtPage(removingPosition);
				//				TimetableDataManager.getTimetables().remove(timetableToRemove);				
				//
				//
				//				final int pageIdx = 
				//						mAdapter.removePage(removingPosition);
				//				MyLog.d("RemovePage", "removed Page : " + removingPosition);
				//
				//				//					mPager.post(new Runnable(){
				//				//						@Override
				//				//						public void run() {
				//				//							// TODO Auto-generated method stub
				//				//							mPager.setCurrentItem(pageIdx);
				//				//							
				//				//							removingPosition = -1;
				//				//						}						
				//				//					});		
				//				mPager.setCurrentItem(pageIdx);
				//				removingPosition = -1;
				//				removingPage = false;
				//				mPager.setPageTransformer(true, null);
				//				
				//				TimetableDataManager.getInstance().writeDatasToExternalStorage();
				//				//removingPosition = -1;
			}
		}
	};

	ViewPager.OnPageChangeListener onDefaultPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if(position == mAdapter.getCount() - 1){
				((ScheduleFragment)mAdapter.getItem(position)).refresh();
				MyLog.d("onPageChangeListener", "Schedule page refreshed!");
			}else{
				Fragment f = mAdapter.getItem(position);
				if(f instanceof TimetableFragment){
					((TimetableFragment) f).onPageSelected();
				}
			}
			setActionBarTitle(position);
			Log.e("onPageChangeListnter", "selected page : " + position);
			MyLog.d("LazyLoadTest", "onPageSelected : " + position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
		}
	};

	void setActionBarTitle(int position){
		//		MenuItem titleItem = menu.findItem(R.id.menu_title);

		//		if(position == 0){
		//			String addTableTitle = getResources()
		//					.getString(R.string.fragment_to_addtable_title);
		//
		//			mActionBarTitle.setText(addTableTitle);
		//			mActionBarTitle.setClickable(false);
		//
		//			MenuItem item = menu.findItem(R.id.menu_item_share);
		//			item.setVisible(false);
		//			MenuItem settingItem = menu.findItem(R.id.menu_item_settings);
		//			settingItem.setVisible(false);
		//			//			mActionBar.setText(addTableTitle);
		//		}else
		if(position >= TIMETABLE_PAGE_OFFSET && 
				( position != mAdapter.getCount() - 1 ) ){
			//add table fragment
			mActionBarTitle.setText(
					TimetableDataManager.getTimetables().get(position - TIMETABLE_PAGE_OFFSET)
					.getTitle());
			mActionBarTitle.setClickable(true);
			MenuItem item = menu.findItem(R.id.menu_item_share);
			item.setVisible(true);

			MenuItem settingItem = menu.findItem(R.id.menu_item_settings);
			settingItem.setVisible(true);
			settingItem.setOnMenuItemClickListener(new OnSettingsMenuItemClickedListener(
					mPager.getCurrentItem()));
			//			mActionBar.setTitle(
			//					TimetableDataManager.getTimetables().get(position - 1).getTitle());
		}else if( ( position == (mAdapter.getCount() - 1) ) ){
			String scheduleTitle = getResources().getString(R.string.fragment_schedule_title);
			mActionBarTitle.setText(
					scheduleTitle);
			mActionBarTitle.setClickable(false);
			MenuItem item = menu.findItem(R.id.menu_item_share);
			item.setVisible(false);
			MenuItem settingItem = menu.findItem(R.id.menu_item_settings);
			settingItem.setVisible(false);
			//			mActionBar.setTitle(scheduleTitle);
		}
	}


	class DrawerItem{
		int icon;
		String text;
		public DrawerItem(String text, int icon){
			this.icon = icon;
			this.text = text;
		}
	}

	class DrawerListAdapter extends BaseAdapter {

		// Declare Variables
		Context context;
		DrawerItem[] items;
		LayoutInflater inflater;
		public DrawerListAdapter(Context context, DrawerItem[] items){
			this.context = context;
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// Declare Variables
			TextView text;
			ImageView imgIcon;

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.item_drawer, parent,
					false);
			// Locate the TextViews in drawer_list_item.xml
			text = (TextView) itemView.findViewById(R.id.item_drawer_text); 
			// Locate the ImageView in drawer_list_item.xml
			imgIcon = (ImageView) itemView.findViewById(R.id.item_drawer_icon);
			// Set the results into TextViews
			text.setText(items[position].text); 
			// Set the results into ImageView
			imgIcon.setImageResource(items[position].icon); 
			int ic_wh = (int) TimetableActivity.this.getResources()
					.getDimension(R.dimen.drawer_icon_wh);
			LinearLayout.LayoutParams param = (LayoutParams) 
					imgIcon.getLayoutParams();
			param.width = ic_wh;
			param.height = ic_wh;
			imgIcon.setLayoutParams(param);
			return itemView;
		}
	}

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private int data = 0;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			return YTBitmapLoader.loadAutoScaledBitmapFromResId(TimetableActivity.this, data);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	class BitmapWorkerTaskFromPhoto extends AsyncTask<Long, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private Long tableId;

		public BitmapWorkerTaskFromPhoto(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Long... params) {
			tableId = params[0];

			try {
				return YTBitmapLoader.loadAutoScaledBitmapFromUri(
						TimetableActivity.this, 
						YTBitmapLoader.getPortraitCroppedImageUri(
								TimetableActivity.this,
								tableId)
						);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}


	public void loadBitmap(int resId, ImageView imageView) {
		//        imageView.setImageResource(R.drawable.image_placeholder);
		BitmapWorkerTask task = new BitmapWorkerTask(imageView);
		task.execute(resId);
	}

	public void loadBitmapFromTimetableId(Long tableId, ImageView imageView){
		BitmapWorkerTaskFromPhoto task = new BitmapWorkerTaskFromPhoto(imageView);
		task.execute(tableId);
	}

    @Override
    public void onBackPressed() {
        if ((TimetableDataManager.getCurrentFullVersionState(this) == false) &&
                InternetConnectionManager.isNetworkAvailable(this)) {
            AlertDialog adDialog = QuitAdDialogFactory.makeDialog(TimetableActivity.this,
                    quitAdView);
            if (adDialog != null) {
                adDialog.show();
                // make AdView again for next quit dialog
                // prevent child reference
                // 가로 모드는 7.5% 가량 사용하고 있기에 속도를 위해서 광고를 계속 불러오지 않음
                quitAdView = QuitAdDialogFactory.initAdView(this, AdSize.MEDIUM_RECTANGLE,
                        quitAdRequest);
            } else {
                // just finish activity when dialog is null
                super.onBackPressed();
            }
        } else {
            // just finish activity when no ad item is bought
            super.onBackPressed();
        }
    }
}

