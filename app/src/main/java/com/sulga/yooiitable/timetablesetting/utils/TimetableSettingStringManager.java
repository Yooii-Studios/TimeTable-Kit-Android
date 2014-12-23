package com.sulga.yooiitable.timetablesetting.utils;

import com.sulga.yooiitable.data.Timetable.ColumnTypes;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;

public class TimetableSettingStringManager {
	
	public static int getIntegerItemIndexOfArray(int item, int[] items){
		for(int i = 0; i < items.length ; i++){
			if(item == items[i]){
				return i;
			}
		}
		return -1;
	}
	
	public static int getColumnTypeItemIndexOfArray(ColumnTypes item, ColumnTypes[] items){
		for(int i = 0; i < items.length ; i++){
			if(item == items[i]){
				return i;
			}
		}
		return -1;
	}
	
	public static int getThemeTypeItemIndexOfArray(ThemeType item, ThemeType[] items){
		for(int i = 0; i < items.length ; i++){
			if(item == items[i]){
				return i;
			}
		}
		return -1;
	}
}
