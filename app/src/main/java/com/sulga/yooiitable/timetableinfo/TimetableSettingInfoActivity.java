package com.sulga.yooiitable.timetableinfo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.RequestCodes;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.YTTimetableTheme;
import com.sulga.yooiitable.timetablesetting.TimetableSettingFragment;
import com.sulga.yooiitable.timetablesetting.TimetableSettingsAlarmFragment;
import com.sulga.yooiitable.utils.MNDeviceSizeChecker;
import com.sulga.yooiitable.utils.YTBitmapLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class TimetableSettingInfoActivity extends AppCompatActivity {
	private static String TAG = "TimetableSettingInfoActivity";
	private TimetableAppInfoFragment infoFrag;
    private TimetableSettingFragment settingFrag;
    private TimetableSettingsAlarmFragment settingAlarmFrag;

    private int timetablePageIndex;
    private Timetable timetable;
    private boolean showTimeSettingDialog;
    private boolean showDaySettingDialog;

	private ViewPager mViewPager;
	private ActionBar mActionBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        timetablePageIndex = getIntent().getIntExtra(TimetableSettingFragment.BUNDLE_PAGE_INDEX_KEY, -1);
        timetable = TimetableDataManager
                .getInstance()
                .getTimetableAtPage(timetablePageIndex);
        showTimeSettingDialog = getIntent().getBooleanExtra("ShowTimeSettingDialog", false);
        showDaySettingDialog = getIntent().getBooleanExtra("ShowDaySettingDialog", false);
		//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_setting_viewpager);
		mViewPager = (ViewPager) findViewById(R.id.setting_viewpager);
		//		FixTileModeBug.fixBackgroundRepeat(mViewPager);
        setupViewPager();
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.app_name));
        setupActionBar();

    }

    private void setupViewPager()
    {
        infoFrag = new TimetableAppInfoFragment();
        //Bundles for timetable setting
        Bundle bundle = new Bundle();
        bundle.putInt(TimetableSettingFragment.BUNDLE_PAGE_INDEX_KEY, timetablePageIndex);
        bundle.putBoolean("ShowTimeSettingDialog", showTimeSettingDialog);
        bundle.putBoolean("ShowDaySettingDialog", showDaySettingDialog);
        settingFrag = new TimetableSettingFragment();
        settingFrag.setArguments(bundle);

        settingAlarmFrag = new TimetableSettingsAlarmFragment();
        settingAlarmFrag.setArguments(bundle);

        Fragment[] fragments = new Fragment[]{settingFrag, settingAlarmFrag, infoFrag};
        mViewPager.setAdapter(new SettingPagerAdapter(getSupportFragmentManager(), fragments));
    }

    private void setupActionBar(){
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Activate Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        // Capture ViewPager page swipes
        ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Find the ViewPager Position
                mActionBar.setSelectedNavigationItem(position);
            }
        };
        mViewPager.setOnPageChangeListener(ViewPagerListener);

        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
        };

        // Create first Tab
        String settingStr = getResources().getString(R.string.tab_timetablesetting);
        ActionBar.Tab settingTab = mActionBar.newTab().setText(settingStr).setTabListener(tabListener);
        mActionBar.addTab(settingTab);

        String settingAlarmStr = getString(R.string.tab_alarmandtheme);
        ActionBar.Tab settingAlarmTab = mActionBar.newTab().setText(settingAlarmStr).setTabListener(tabListener);
        mActionBar.addTab(settingAlarmTab);

        // Create second Tab
        String infoStr = getResources().getString(R.string.tab_info);
        ActionBar.Tab infoTab = mActionBar.newTab().setText(infoStr).setTabListener(tabListener);
        mActionBar.addTab(infoTab);
    }

	public class SettingPagerAdapter extends FragmentStatePagerAdapter {
        Fragment[] fragments;
	    public SettingPagerAdapter(FragmentManager fm, Fragment[] fragments) {
	        super(fm);
            this.fragments = fragments;
	    }

	    @Override
	    public Fragment getItem(int i) {
	    	return fragments[i];
	    }
	    @Override
	    public int getCount() {
	        return fragments.length;
	    }
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return "OBJECT " + (position + 1);
	    }
	}

    ///From Timetable Setting Activity, which now changed to fragment///

//    public void onThemeSettingsChanged(){
//        YTTimetableTheme.ThemeType[] themes =
//                YTTimetableTheme.getThemeTypeValues();
//        MyLog.d("TimetableThemeFragment", "pickTemeClickedItemPosition : "
//                + pickThemeClickedItemPosition);
//        timetable.setThemeType(
//                themes[pickThemeClickedItemPosition]
//        );
//        //		pickThemeText.setText(
//        //				themes[pickThemeClickedItemPosition].toString()
//        //				);
//    }

    /** 다시 액티비티로 복귀하였을때 이미지를 셋팅 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.i(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // 모토롤라 폰에서, 아트를 선택하면 배경이 변경되어 버리는 버그가 있다. 추후 모토롤라 폰에서 예외처리를 해 주어야 할듯
            case RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_PICK_IMAGE_PORTRAIT:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Log.i(TAG, "REQ_CODE_PICK_IMAGE_PORTRAIT && RESULT_OK");

                        // 기기의 가로세로 비율을 찾아낸다.
                        int width = MNDeviceSizeChecker.getDeviceWidth(this);
                        int height = MNDeviceSizeChecker.getDeviceHeight(this);
                        float ratio = (float)height / (float)width;

                        //					Uri originalPortImageUri = data.getData();

                        //					if (originalPortImageUri == null || originalPortImageUri.toString().length() == 0) {
                        Uri originalPortImageUri = Uri.fromFile(mTempFile);
                        //	                    file = mTempFile;
                        //	                }
                        startCropActivity(data, ratio,
                                originalPortImageUri,
                                RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_CROP_FROM_IMAGE_PORTRAIT);
                    }
                }
                break;

            case RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_CROP_FROM_IMAGE_PORTRAIT:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Log.i(TAG, "REQ_CROP_FROM_CAMERA && RESULT_OK");
                        try {
                            setThemeAndRelaunchApp(
                                    YTBitmapLoader.getPortraitCroppedImageUri(
                                            this,
                                            timetable.getId()));
                            onThemeSettled();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                }
                break;
        }
    }

    private File mTempFile;
    public void showImagePicker() {
        mTempFile = getFileStreamPath("tempCropFile");
        mTempFile.getParentFile().mkdirs();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, RequestCodes.YT_TIMETABLE_THEME_REQUEST_CODE_PICK_IMAGE_PORTRAIT);
    }

    public void setThemeAndRelaunchApp(Uri imageUri) throws FileNotFoundException {
        Bitmap bitmap = YTBitmapLoader.loadAutoScaledBitmapFromUri(this, imageUri);
        // crop이 완료된 비트맵을 로컬에 저장한 후 메인에서 보여줄 수 있게 구현
        YTBitmapLoader.saveBitmapToUri(this, imageUri, bitmap);
        //GeneralSetting.setTheme(photoTheme);
        timetable.setThemeType(YTTimetableTheme.ThemeType.Photo);

        //settingsConfigureView.onGeneralSettingChanged();
        bitmap.recycle();
        //timetable.getTheme().setRootBackground(resId);
//        onThemeSettingsChanged();
        //relaunchApplication();
    }


    public void startCropActivity(Intent data, float ratio, Uri originalPortImageUri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        //Uri originalImageUri = data.getData();
        MyLog.d("CropActivity", "ImageToBeCropped : " + originalPortImageUri.getPath());
		/*mImageCaptureUri = data.getData();
        File original_file = getImageFile(mImageCaptureUri);

        mImageCaptureUri = createSaveCropFile();
        File cpoy_file = new File(mImageCaptureUri.getPath());

        // SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
        copyFile(original_file , cpoy_file);*/
        Uri croppedImageUri = YTBitmapLoader.createPortraitCroppedImage(
                this, originalPortImageUri, timetable.getId());

        intent.setDataAndType(croppedImageUri, "image/*");

        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", (int)(100 * ratio));
        intent.putExtra("noFaceDetection", true);	// 얼굴부분 찾아내지 않기.
        intent.putExtra("output", croppedImageUri);

        List list = getPackageManager().queryIntentActivities(intent, 0);
        intent.setData(data.getData());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);	// 이 부분이 추가되면 모토롤라 폰은 배경이 바뀐다.

        Intent i = new Intent(intent);
        ResolveInfo res = (ResolveInfo) list.get(0);
        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                setResultAndFinish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
//        onThemeSettingsChanged();
        setResultAndFinish();
    }
    public void onThemeSettled(){
//		setResultAndFinish();
//        onThemeSettingsChanged();
        MyLog.d(TAG, "onThemeSettled theme : " + timetable.getThemeType());
        setResultAndFinish();
    }

    public void onLanguageChanged(){
        MyLog.d(TAG, "onLanguageChanged called");
        Intent data = new Intent();
        data.putExtra("TimetablePageIndex", timetablePageIndex);
        data.putExtra("LanguageChanged", true);
        setResult(Activity.RESULT_OK, data);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    public void setResultAndFinish(){
        Intent data = new Intent();
        data.putExtra("TimetablePageIndex", timetablePageIndex);
        setResult(Activity.RESULT_OK, data);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

}
