package com.sulga.yooiitable.utils;

import java.util.*;

import org.holoeverywhere.app.*;

import android.content.res.*;

import com.sulga.yooiitable.language.*;

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
