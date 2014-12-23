package com.sulga.yooiitable.language;

import java.util.*;

import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;

import com.sulga.yooiitable.mylog.*;

import android.content.*;

public class YTLanguage {

	private static final String LANGUAGE_SHARED_PREFERENCES = "LANGUAGE_SHARED_PREFERENCES";
	private static final String LANGUAGE_MATRIX_KEY= "LANGUAGE_MATRIX_KEY";

	private volatile static YTLanguage instance;
	private YTLanguageType currentLanguageType;

	/**
	 * Singleton
	 */
	private YTLanguage(){}
	private YTLanguage(Context context) {
		SharedPreferences prefs = PreferenceManager.wrap(context, LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE);

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
		YTLanguage.getInstance(context).currentLanguageType = newNewLanguage;
		context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
		.edit().putInt(LANGUAGE_MATRIX_KEY, newNewLanguage.getUniqueId()).commit();
	}

}