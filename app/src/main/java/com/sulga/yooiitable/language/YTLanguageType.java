package com.sulga.yooiitable.language;

import android.content.*;

import com.sulga.yooiitable.*;

public enum YTLanguageType {
	ENGLISH(0, 0, "en", ""),
    KOREAN(1, 1, "ko", ""),
    JAPANESE(2, 2, "ja", ""),
    SIMPLIFIED_CHINESE(3, 3, "zh", "CN"),
    TRADITIONAL_CHINESE(4, 4, "zh", "TW");

	private final int index; // 리스트뷰에 표시할 용도의 index
	private final int uniqueId; // SharedPreferences에 저장될 용도의 unique id
	private final String code;
	private final String region;

	YTLanguageType(int index, int uniqueId, String code, String region) {
		this.index = index;
		this.uniqueId = uniqueId;
		this.code = code;
		this.region = region;
	}

	public static YTLanguageType valueOf(int index) {

		switch (index) {
		case 0: return ENGLISH;
		case 1: return KOREAN;
		case 2: return JAPANESE;
		case 3: return SIMPLIFIED_CHINESE;
		case 4: return TRADITIONAL_CHINESE;
		default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
		}
	}

	public static YTLanguageType valueOfUniqueId(int uniqueId) {

		switch (uniqueId) {
		case 0: return ENGLISH;
		case 1: return KOREAN;
		case 2: return JAPANESE;
		case 3: return SIMPLIFIED_CHINESE;
		case 4: return TRADITIONAL_CHINESE;
		default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
		}
	}
	
	public static YTLanguageType valueOfCodeAndRegion(String code, String region){
		
		//한국어와 일본어만 지원, 일단은.
//		if(code.equals(KOREAN.code)){
//			return KOREAN;
//		}else if(code.equals(JAPANESE.code)){
//			return JAPANESE;
//		}else{
//			return KOREAN;
//		}
		if(code.equals(ENGLISH.code)){
			return ENGLISH;
		}else if(code.equals(KOREAN.code)){
			return KOREAN;
		}else if(code.equals(JAPANESE.code)){
			return JAPANESE;
		}else if(code.equals(SIMPLIFIED_CHINESE.code)){
			if(region.equals(SIMPLIFIED_CHINESE.region)){
				return SIMPLIFIED_CHINESE;
			}else if(region.equals(TRADITIONAL_CHINESE.region)){
				return TRADITIONAL_CHINESE;
			}
		}
		return ENGLISH;
	}

	public static String toTranselatedString(int position, Context context) {
		switch (position) {
		case 0: return context.getString(R.string.setting_language_english);
		case 1: return context.getString(R.string.setting_language_korean);
		case 2: return context.getString(R.string.setting_language_japanese);
		case 3: return context.getString(R.string.setting_language_simplified_chinese);
		case 4: return context.getString(R.string.setting_language_traditional_chinese);
		default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
		}
	}
	
	public static String toEnglishString(int position, Context context) {
		switch (position) {
		case 0: return "English";
		case 1: return "Korean";
		case 2: return "Japanese";
		case 3: return "Chinese (Simplified)";
		case 4: return "Chinese (Traditional)";
		default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
		}
	}
	
	public int getUniqueId() {
		return uniqueId;
	}

	public int getIndex() {
		return index;
	}

	public String getRegion() {
		return region;
	}

	public String getCode() {
		return code;
	}

}
