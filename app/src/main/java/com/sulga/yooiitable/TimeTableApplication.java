package com.sulga.yooiitable;

import android.app.Application;
import android.content.res.Configuration;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sulga.yooiitable.language.YTLanguage;
import com.sulga.yooiitable.language.YTLanguageType;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Wooseong Kim in TimeTable Kit from Yooii Studios Co., LTD. on 2016. 3. 9.
 *
 * TimeTableApplication
 */
public class TimeTableApplication extends Application {
    private Locale mLocale = null;

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    private Tracker mTracker;
    public synchronized Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initLanguage();
    }

    private void initLanguage() {
        // load language from MNLanguage
        YTLanguageType currentLanguage = YTLanguage.getCurrentLanguageType(getApplicationContext());

        Configuration config = getApplicationContext().getResources().getConfiguration();
        // update locale to current language
        mLocale = new Locale(currentLanguage.getCode(), currentLanguage.getRegion());
        applyLocale(config);
    }

    private void applyLocale(Configuration config) {
        Locale.setDefault(mLocale);
        config.locale = mLocale;
        getApplicationContext().getResources().updateConfiguration(config,
                getApplicationContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mLocale != null) {
            newConfig.locale = mLocale;
            Locale.setDefault(mLocale);
            getApplicationContext().getResources().updateConfiguration(newConfig,
                    getApplicationContext().getResources().getDisplayMetrics());
        }
    }
}
