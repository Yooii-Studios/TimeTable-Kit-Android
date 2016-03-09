package com.sulga.yooiitable.timetable;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.language.YTLanguageType;
import com.sulga.yooiitable.timetablesetting.SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener;

public class SettingLanguageDialogCreator {

	public static void showSettingsLanguageListDialog(Context context, String title, 
				final OnSelectOptionDialogItemSelectedListener listener){
		AlertDialog.Builder selectOptionDialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context, android.R.style.Theme_Dialog));

		YTLanguageType[] languageTypes = YTLanguageType.values();	
		selectOptionDialog.setTitle(title);

		LanguageListAdapter adt = new LanguageListAdapter(context, languageTypes);
		selectOptionDialog.setAdapter(adt, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(listener != null){
					listener.onClick(which);
				}
			}
		});
		selectOptionDialog.create().show();			
	}

	static class LanguageListAdapter extends ArrayAdapter<YTLanguageType> {
		YTLanguageType[] items;
		Context context;
		public LanguageListAdapter(Context context, YTLanguageType[] objects) {
			super(context, R.layout.item_theme_spinner, objects);
			this.items = objects;
			this.context = context;
		}

		@Override //don't override if you don't want the default spinner to be a two line view
		public View getView(int position, View convertView, ViewGroup parent) {
			return initView(position, convertView, parent);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return initView(position, convertView, parent);
		}

		private View initView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
				convertView = View.inflate(
						context,
						R.layout.item_theme_spinner, 
						null);
			TextView themeName = (TextView)convertView
					.findViewById(R.id.item_theme_spinner_text);
			ImageView lockImg = (ImageView)convertView
					.findViewById(R.id.item_theme_spinner_img); 
			lockImg.setVisibility(View.GONE);
			themeName.setText(YTLanguageType.toTranselatedString(position, context));
			return convertView;
		}
	}
}
