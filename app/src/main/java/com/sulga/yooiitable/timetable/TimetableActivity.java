package com.sulga.yooiitable.timetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.TimeTableApplication;
import com.sulga.yooiitable.appwidget.YTAppWidgetProvider_2x4;
import com.sulga.yooiitable.appwidget.YTAppWidgetProvider_4x4;
import com.sulga.yooiitable.constants.FixedSizes;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.constants.NaverInApp;
import com.sulga.yooiitable.constants.RequestCodes;
import com.sulga.yooiitable.customviews.ParentViewPager;
import com.sulga.yooiitable.customviews.animation.OnDeletePageTransformer;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.overlapviewer.OverlapTablesViewerActivity;
import com.sulga.yooiitable.sharetable.BannerInfo;
import com.sulga.yooiitable.sharetable.TimetableNetworkManager;
import com.sulga.yooiitable.showalltables.ShowAllTimetablesActivity;
import com.sulga.yooiitable.timetable.fragments.ScheduleFragment;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;
import com.sulga.yooiitable.timetableinfo.TimetableSettingInfoActivity;
import com.sulga.yooiitable.timetableinfo.activity.NaverStoreActivity;
import com.sulga.yooiitable.timetableinfo.activity.StoreActivity;
import com.sulga.yooiitable.utils.AlertDialogCreator;
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
import com.yooiistudios.common.analytics.AnalyticsUtils;
import com.yooiistudios.common.language.LocaleUtils;
import com.yooiistudios.common.network.InternetConnectionManager;

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
import java.util.Map;

public class TimetableActivity extends AppCompatActivity {
	public static final int TIMETABLE_PAGE_OFFSET = 0;	
	public static final int TIMETABLE_MAX_LIMIT = 10;
    public static final int TIMETABLE_FREE_LIMIT = 3;
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
        LocaleUtils.updateLocale(this);

		setContentView(R.layout.activity_timetable_main_withdrawer);
        turnOnScreen();

        InAppBillingManager.updateFullVersionState(this,
                new InAppBillingManager.OnFullVersionStateUpdateFinishedListener() {
            @Override
            public void onFullVersionStateUpdateFinished(boolean isSucceed,
                    boolean isFullVersion) {
                MyLog.d(TAG, "Update Full Version State : " + isSucceed
                        + ", Network Full version : " + isFullVersion);
				if (isSucceed && !isFullVersion) {
					setupAdView();
					dogEar.setVisibility(View.VISIBLE);
				} else if (isSucceed && isFullVersion) {
					removeAdView();
					dogEar.setVisibility(View.GONE);
				}
			}
        });

		addTableProgressDialog = new ProgressDialog(this);
		String addingTableString = getResources()
				.getString(R.string.fragment_timetable_notice_adding_timetable);
		addTableProgressDialog.setMessage(addingTableString);

		initDrawer();

		mPager = (ParentViewPager)findViewById(R.id.activity_timetable_main_pager);
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        if(savedInstanceState != null){
            mAdapter.clearCache();
        }
        tdm = TimetableDataManager.getInstance();
        makePagesFromTimetableDataManager(tdm);

        mPager.setAdapter(mAdapter);

		mPager.setCurrentItem(mAdapter.getCount() - 2);
		mPager.setOffscreenPageLimit(3);

		//페이져의 리스너는 인디케이터가 쓰고 있으므로 따로 인디케이터에 원래 페이저용 리스너 등록.
		mIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(onDefaultPageChangeListener);

		dogEar = (ImageView)findViewById(R.id.dogear_to_store);
		dogEar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, StoreActivity.class);
                startActivity(intent);

				Map<String, String> info = new HashMap<>();
				info.put(FlurryConstants.STORE_CLICKTYPE_KEY, FlurryConstants.STORE_CLICKTYPE_DOGEAR);
				FlurryAgent.logEvent(FlurryConstants.STORE_CLICKED, info);
			}
		});
		if(!TimetableDataManager.getCurrentFullVersionState(this)){
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

        if(savedInstanceState == null) {
            initQuitAdView();
            AdUtils.showPopupAdIfSatisfied(this);
        }

		// 풀버전 테스트 시 아래 부분 주석 풀자(삭제하지 말 것)
//		TimetableDataManager.saveFullVersionState(TimetableActivity.this, true);
//		dogEar.setVisibility(View.GONE);

		AnalyticsUtils.startAnalytics((TimeTableApplication) getApplication(), R.string.screen_main);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void turnOnScreen() {
        boolean wakeLock = getIntent().getBooleanExtra("WakeLock", false);
        MyLog.d(TAG, "WakeLock : " + wakeLock);
        if(!wakeLock)
            return;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void initActionBar() {
		// 2) Set your display to custom next
        MyLog.d("ActionBar", "InitActionbar Called");

		mActionBar = getSupportActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// 3) Do any other config to the action bar
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
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Tuesday, R.string.TUE) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				processOnDrawerClosed();
				MyLog.d("onDrawerClosed", "CLOSED");
			}
			public void onDrawerOpened(View drawerView) {
				// Set the title on the action when drawer open
				//                getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.addDrawerListener(mDrawerToggle);
	}

	private void initFirstLaunch(){
		if(TimetableDataManager.getIsFirstLaunch(this)){
			StartTutorialDialogBuilder.createDialog(this).show();
			if(TimetableDataManager.getSchedules().isEmpty()){
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
				if(isSucceed){
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
		int actionBarHeight = 0;
		if (getSupportActionBar() != null) {
			actionBarHeight = getSupportActionBar().getHeight();
		}
		if (actionBarHeight != 0)
			return actionBarHeight;
		final TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
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
		// 강제로 Locale 고정 필요(풀리는 경우 방지)
		LocaleUtils.updateLocale(this);

		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(android.widget.AdapterView<?> parent,
				View view, int position, long id) {
			selectItem(position);
		}
	}

	private int drawerSelected = -1;
	private void selectItem(int position) {		
		drawerSelected = position;
		if(position == 0){
			if( ( TimetableDataManager.getTimetables().size() >= TIMETABLE_FREE_LIMIT ) &&
					(!TimetableDataManager.getCurrentFullVersionState(this)) ){
                AdUtils.showInHouseStoreAd(this, getString(R.string.unlock_full_version_pagenum_overflow));
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
			if( ( TimetableDataManager.getTimetables().size() >= TIMETABLE_FREE_LIMIT ) &&
					(!TimetableDataManager.getCurrentFullVersionState(this)) ){
                //not full version
				return;
			}else if(TimetableDataManager.getTimetables().size() >= TIMETABLE_MAX_LIMIT){
				//Reached max timetable
				return;
			}

			addPageAt(TimetableActivity.TIMETABLE_PAGE_OFFSET, null, false);
			TimetableDataManager.writeDatasToExternalStorage();
			getViewPager().post(new Runnable(){
				@Override
				public void run() {
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

			ArrayList<Integer> checkedItems = new ArrayList<>();
			for(int i = 0; i < TimetableDataManager.getTimetables().size() ; i++){
				checkedItems.add(i);
			}
			overlapIntent.putExtra("OverlapIndex", checkedItems);
			overlapIntent.putExtra("DirectOverlap", true);

			this.startActivityForResult(
					overlapIntent, RequestCodes.CALL_ACTIVITY_OVERLAP);

			Map<String, String> currentPageNumber =
					new HashMap<>();
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
		if(TimetableDataManager.getCurrentFullVersionState(this)){
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
		super.onDestroy();
	}

	private ShareActionProvider mShareActionProvider;
	private Menu menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/** Create an option menu from res/menu/items.xml */
		MyLog.d("onCreateOptionsMenu", "Called");

		// 1)Inflate menu resource file.

		getMenuInflater().inflate(R.menu.actionbar_timetable_menu, menu);
		// 2)Initialize action bar, if not called in onCreateOptionsMenu
		//and called in onCreate(), Custom view is not showing.
		//strange...
		initActionBar();

		this.menu = menu;
		//init share item
		MenuItem item = menu.findItem(R.id.menu_item_share);
		item.setIcon(R.drawable.ic_share_white_32dp);

		// Locate MenuItem with ShareActionProvider
		if( mPager.getCurrentItem() == mAdapter.getCount() - 1){
			item.setVisible(false);
		}else{
			item.setVisible(true);
		}

		// Fetch and store ShareActionProvider
		mShareActionProvider = new ShareActionProvider(this) {
			@Override
			public View onCreateActionView() {
				return null;
			}
		};

		/** Getting the target intent */
		Intent intent = getShareIntent();

		/** Setting a share intent */
		if (intent != null) {
			mShareActionProvider.setShareIntent(intent);
		}
		mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
			@Override
			public boolean onShareTargetSelected(ShareActionProvider actionProvider, Intent intent) {
				saveImageToSD();
				return false;
			}
		});
		MenuItemCompat.setActionProvider(item, mShareActionProvider);

		//init overflow menu
		//		MenuItem overflowItem = menu.findItem(R.id.menu_item_overflow);
		MenuItem storeItem = menu.findItem(R.id.menu_item_store);
		storeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(NaverInApp.isNaverApk == false){
					Intent intent = new Intent(TimetableActivity.this, StoreActivity.class);
					startActivity(intent);
				}else if(NaverInApp.isNaverApk == true){
					Intent intent = new Intent(TimetableActivity.this, NaverStoreActivity.class);
					startActivity(intent);
				}

				Map<String, String> info = new HashMap<>();
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

		MenuItem rateApp = menu.findItem(R.id.menu_item_rate);
		rateApp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
                Uri uri = Uri.parse("market://details?id="
						+ getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ getPackageName())));
				}
				return true;
			}
		});

        MenuItem likeUsOnFacebook = menu.findItem(R.id.menu_item_likeinfacebook);
        likeUsOnFacebook.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent browserIntent =
						new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/YooiiMooii"));
				startActivity(browserIntent);
                return true;
            }
        });

		// Return true to display menu
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyLog.d("onOptionsItemSelected", "clicked");
		switch (item.getItemId()) {
		case android.R.id.home:
			MyLog.d("onOptionsItemSelected", "home clicked");
			if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
				mDrawerLayout.closeDrawer(GravityCompat.START);
			} else {
				mDrawerLayout.openDrawer(GravityCompat.START);
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

		OutputStream outStream;
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
			File file = new File(directory, "shareImage.png");
			try {
				file.createNewFile();
			} catch (IOException e) {
			}

			return file;
		} else {
			return null;
		}
	}

	private class TitleTextViewOnClickListener implements View.OnClickListener {
		private String title="";

		private String editTextInitial="";
		public TitleTextViewOnClickListener(String title){
			this.title = title;
		}
		@Override
		public void onClick(View v) {
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
					v.getContext(),
					InputType.TYPE_CLASS_TEXT,
					null,
					timetable.getTitle(), 
					title, 
					new AlertDialogCreator.EditTextDialogOnClickListener() {

						@Override
						public void onClick(EditText editText, Dialog d) {
							String title = editText.getText().toString();
							timetable.setTitle(title);
							mActionBarTitle.setText(title);
							d.dismiss();
						}
					},
					new AlertDialogCreator.EditTextDialogOnClickListener() {

						@Override
						public void onClick(EditText editText, Dialog d) {
							d.dismiss();
						}
					});
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING) {
			if (resultCode == RESULT_OK) {
				boolean hasLanguageChanged = data.getBooleanExtra(
                        TimetableSettingInfoActivity.KEY_CHANGED_LANGUAGE, false);
				if (hasLanguageChanged) {
					recreate();
				} else {
					int currentPage = data.getIntExtra("TimetablePageIndex", -1);
					mPager.setAdapter(null);
					mPager.setAdapter(mAdapter);
					if (currentPage < 0) {
						mPager.setCurrentItem(mAdapter.getCount() - 2);
					} else {
						mPager.setCurrentItem(currentPage);
					}
				}
			}
		} else if (requestCode == RequestCodes.CALL_ACTIVITY_SHOW_ALL) {
            if (resultCode == RESULT_OK) {
                int idx = data.getIntExtra(
                        "SelectedTimetableIndex", mPager.getChildCount() - 1);
                mPager.setCurrentItem(idx + TIMETABLE_PAGE_OFFSET);
            }
        }
        // 민수에게 문의 결과 없어진 기능. 코드는 남겨두자
        /*
        else if (requestCode == RequestCodes.GCACCOUNT_REQUEST_CODE_PICK_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				//AccountUtils.setAccountName(this, accountName);
				//어카운트 네임을 세이브한다.
				//그리고 구글캘린더로 올리는 다이얼로그 보여주기.
				GCAccountManager.getInstance().showSyncGoogleCalendarDialog(this, accountName);

			} else if (resultCode == RESULT_CANCELED) {
				String warn = getResources()
						.getString(R.string.warning_gc_sync_must_have_google_account);
				Toast.makeText(this, warn, Toast.LENGTH_SHORT).show();
			}
			return;
		} else if (requestCode == RequestCodes.GCACCOUNT_REQUEST_CODE_RECOVER_PLAY_SERVICES) {
			String warn = getResources()
					.getString(R.string.warning_gc_sync_must_have_latest_google_play);
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, warn, Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == RequestCodes.GCACCOUNT_REQUEST_CODE_AUTHORIZATION) {
			if (resultCode == RESULT_OK) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				String loading = getResources().getString(R.string.loading);
				String startingConnection = getResources().getString(R.string.starting_connection);
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setTitle(loading);
				progressDialog.setMessage(startingConnection);
				progressDialog.setCancelable(false);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();

				GCCalendarSyncManager.startSync(this, accountName, progressDialog);
			} else if (resultCode == RESULT_CANCELED) {
				String cancelled = getResources().getString(R.string.cancelled);
				Toast.makeText(this, cancelled, Toast.LENGTH_LONG).show();
			}
        }
        */
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
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final float flingWidth = displayMetrics.widthPixels * .8f;
        MyLog.d(TAG, "RemovePageAt, flingW : " + flingWidth);
//        final float dragWidth = dpWidth;
		//post must be called, beacuse if not 
		//viewpager fakedrag starts immediately whether 
		//the dialog dismiss delay is finished or not, which means
		//some of the fake dragging is not being called and page dragging
		//finishes perfectly.
		mPager.post(new Runnable(){

			@Override
			public void run() {
				ViewPagerFakeDragger.goToRightPage(
						TimetableActivity.this, true, 
						new OnDeletePageTransformer(), 
						mPager,
//                        getResources().getDimension(R.dimen.viewpager_minimum_fling_distance),
                        flingWidth,
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
		if(!addTableProgressDialog.isShowing()){
			addTableProgressDialog.show();
		}
	}
	private void hideAddTableProgressDialog(){
		if(addTableProgressDialog.isShowing()){
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

		if(goToRightPage){
			ViewPagerFakeDragger.goToRightPage(
					this, true,
					null,
					mPager, getResources().getDimension(R.dimen.viewpager_minimum_fling_distance) / 4
					, ViewPagerFakeDragger.SLOW_DELAY_MILLIS);
			mIndicator.setOnPageChangeListener(onAddPageChangeListener);
		}
		if(!goToRightPage){
			mAdapter.addPage(TimetableFragment.newInstance(), TIMETABLE_PAGE_OFFSET, true);
		}else{
			mAdapter.addPage(TimetableFragment.newInstance(), TIMETABLE_PAGE_OFFSET, false);
		}
		mPager.post(new Runnable(){
			@Override
			public void run() {
				//				mPager.setCurrentItem(tmpCurrPage + 1);
				hideAddTableProgressDialog();
			}

		});
	}

	public class MyFragmentPagerAdapter extends MyFragmentStatePagerAdapter{
		private List<Fragment> fragments;
		private FragmentManager fm;
		public MyFragmentPagerAdapter(FragmentManager fm) { 
			super(fm);  
			this.fm = fm;
			this.fragments = new ArrayList<>();
		}
		@Override  
		public Fragment getItem(int index) {  
			MyLog.d("MyFragmentPagerAdapter", "getItem index : " + index + ", fragments length : " + fragments.size()
                    + "fragment : " + fragments.get(index));
			return fragments.get(index);
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
            clearCache();
			mPager.setAdapter(this);
			if(keepCurrentPage)
				mPager.setCurrentItem(currPage + 1);
			//			mPager.setCurrentItem(position);
			//notifyDataSetChanged();
			Log.e("PagerAdapter", fragments.size()+" Pages");
		}
		public void addPage(Fragment fragment){
			mPager.setAdapter(null);
			fragments.add(fragment);
            clearCache();
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
			MyLog.d("indexOfFragmentPage", "-1" + "fragments size : " + fragments.size());
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
			Intent act = new Intent(TimetableActivity.this, TimetableSettingInfoActivity.class);
			act.putExtra("TimetablePageIndex", pageIndex);
			startActivityForResult(act, RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING);

			Map<String, String> info = new HashMap<>();
			info.put(FlurryConstants.SETTING_CLICKTYPE_KEY, 
					FlurryConstants.SETTING_CLICKTYPE_MENUITEM);
			FlurryAgent.logEvent(FlurryConstants.SETTING_CLICKED);
			return true;
		}
	}

	ViewPager.OnPageChangeListener onDeletePageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
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
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if( state == android.support.v4.view.ViewPager.SCROLL_STATE_IDLE && removingPage){

				//페이지 삭제를 위해 한 페이지 오른쪽으로 이동해져있는 상태.
				//데이터 삭
				Timetable timetableToRemove =
						TimetableDataManager.getInstance()
						.getTimetableAtPage(removingPosition);
				TimetableDataManager.getTimetables().remove(timetableToRemove);				

				final int pageIdx = 
						mAdapter.removePage(removingPosition);
				MyLog.d("RemovePage", "removed Page : " + removingPosition);

				mPager.setCurrentItem(pageIdx);
				removingPosition = -1;
				removingPage = false;
				mPager.setPageTransformer(true, null);
				mIndicator.notifyDataSetChanged();
				mIndicator.post(new Runnable(){
					@Override
					public void run() {
						mPager.disablePaging(false);
					}
				});
			}
		}
	};

	ViewPager.OnPageChangeListener onAddPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
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

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if( state == android.support.v4.view.ViewPager.SCROLL_STATE_IDLE){
				mPager.setPageTransformer(true, null);
				mIndicator.notifyDataSetChanged();
				mIndicator.post(new Runnable(){

					@Override
					public void run() {
						mPager.disablePaging(false);
					}

				});
			}
		}
	};

	ViewPager.OnPageChangeListener onDefaultPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			if(position == mAdapter.getCount() - 1){
				((ScheduleFragment)mAdapter.getItem(position)).refresh();
				MyLog.d("onPageChangeListener", "Schedule page refreshed!");
			}else{
				Fragment f = mAdapter.getItem(position);
				if(f instanceof TimetableFragment){
					((TimetableFragment) f).onPageSelected();
				}
			}
            Log.e("onPageChangeListnter", "selected page : " + position);
			setActionBarTitle(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};

	void setActionBarTitle(int position){
        if(mActionBarTitle == null)
            return;

		if(position >= TIMETABLE_PAGE_OFFSET &&
				( position != mAdapter.getCount() - 1 ) ){
            MyLog.d(TAG, "ActionBar : " + mActionBarTitle);
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
		}else if( ( position == (mAdapter.getCount() - 1) ) ){
			String scheduleTitle = getResources().getString(R.string.fragment_schedule_title);
			mActionBarTitle.setText(
					scheduleTitle);
			mActionBarTitle.setClickable(false);
			MenuItem item = menu.findItem(R.id.menu_item_share);
			item.setVisible(false);
			MenuItem settingItem = menu.findItem(R.id.menu_item_settings);
			settingItem.setVisible(false);
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

		@SuppressLint("ViewHolder")
		public View getView(int position, View convertView, ViewGroup parent) {
			// Declare Variables
			TextView text;
			ImageView imgIcon;

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView = inflater.inflate(R.layout.item_drawer, parent,false);

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
			imageViewReference = new WeakReference<>(imageView);
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
			imageViewReference = new WeakReference<>(imageView);
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
        if ((!TimetableDataManager.getCurrentFullVersionState(this)) &&
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

