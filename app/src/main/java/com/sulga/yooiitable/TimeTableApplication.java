package com.sulga.yooiitable;

import android.app.Application;
import android.content.res.Configuration;

import com.sulga.yooiitable.language.YTLanguage;
import com.sulga.yooiitable.language.YTLanguageType;

import java.util.Locale;

/**
 * Created by Wooseong Kim in TimeTable Kit from Yooii Studios Co., LTD. on 2016. 3. 9.
 *
 * TimeTableApplication
 */
public class TimeTableApplication extends Application {
    private Locale mLocale = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());

//        initFlurry();
        initLanguage();
    }

//    private void initFlurry() {
//        FlurryAgent.setLogEnabled(false);
//        FlurryAgent.init(this, FLURRY_API_KEY);
//    }

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
