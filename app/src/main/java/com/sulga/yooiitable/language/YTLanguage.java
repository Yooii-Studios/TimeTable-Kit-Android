package com.sulga.yooiitable.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.sulga.yooiitable.mylog.MyLog;

import java.util.Locale;

public class YTLanguage {

	private static final String LANGUAGE_SHARED_PREFERENCES = "LANGUAGE_SHARED_PREFERENCES";
	private static final String LANGUAGE_MATRIX_KEY= "LANGUAGE_MATRIX_KEY";

	private volatile static YTLanguage instance;
	private YTLanguageType currentLanguageType;

	/**
	 * Singleton
	 */
	@SuppressWarnings("unused")
	private YTLanguage(){}
	private YTLanguage(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES,
				Context.MODE_PRIVATE);

		int uniqueId = prefs
				.getInt(LANGUAGE_MATRIX_KEY, -1);

		// 최초 설치시 디바이스의 언어와 비교해 앱이 지원하는 언어면 해당 언어로 설정, 아닐 경우 영어로 첫 언어 설정
		if (uniqueId == -1) {
			Locale locale = Locale.getDefault();
			currentLanguageType = YTLanguageType.valueOfCodeAndRegion(locale.getLanguage(), locale.getCountry());
			MyLog.d("YTLanguage", "currentLanguageType : " + currentLanguageType.toString());
			// 아카이브
			context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
			.edit().putInt(LANGUAGE_MATRIX_KEY, currentLanguageType.getUniqueId()).commit();
		} else {
			currentLanguageType = YTLanguageType.valueOfUniqueId(uniqueId);
			MyLog.d("YTLanguage", "currentLanguageType : " + currentLanguageType.toString());
		}
	}
	public static YTLanguage getInstance(Context context) {
		if (instance == null) {
			synchronized (YTLanguage.class) {
				if (instance == null) {
					instance = new YTLanguage(context);
				}
			}
		}
		return instance;
	}
	public static YTLanguageType getCurrentLanguageType(Context context) { 
		return YTLanguage.getInstance(context).currentLanguageType; }

	public static void setLanguageType(YTLanguageType newNewLanguage, Context context) {
		// archive selection
		YTLanguage.getInstance(context).currentLanguageType = newNewLanguage;
		context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
		.edit().putInt(LANGUAGE_MATRIX_KEY, newNewLanguage.getUniqueId()).apply();

		// update locale
		YTLanguageType currentLanguageType = YTLanguage.getCurrentLanguageType(context);
		Locale locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
	}
}
