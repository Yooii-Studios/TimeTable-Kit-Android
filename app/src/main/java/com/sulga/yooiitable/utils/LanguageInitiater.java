package com.sulga.yooiitable.utils;

import android.app.Activity;
import android.content.res.Configuration;

import com.sulga.yooiitable.language.YTLanguage;
import com.sulga.yooiitable.language.YTLanguageType;

import java.util.Locale;

public class LanguageInitiater {

	public static void setActivityLanguage(Activity activity){
		YTLanguage lang = YTLanguage.getInstance(activity);
		// update locale
		YTLanguageType currentLanguageType = lang.getCurrentLanguageType(activity);
		Locale locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
	}
}
