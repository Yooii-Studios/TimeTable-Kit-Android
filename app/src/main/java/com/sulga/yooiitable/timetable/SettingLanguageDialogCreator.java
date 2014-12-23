package com.sulga.yooiitable.timetable;

import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.ArrayAdapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.widget.TextView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.language.*;
import com.sulga.yooiitable.timetablesetting.SelectOptionDialogCreator.OnSelectOptionDialogItemSelectedListener;

public class SettingLanguageDialogCreator {

	public static void showSettingsLanguageListDialog(Context context, String title, 
				final OnSelectOptionDialogItemSelectedListener listener){
		AlertDialog.Builder selectOptionDialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context, R.style.Holo_Theme_Dialog));

		YTLanguageType[] languageTypes = YTLanguageType.values();	
		selectOptionDialog.setTitle(title);

		LanguageListAdapter adt = new LanguageListAdapter(
				context, android.R.layout.select_dialog_item, languageTypes);
		selectOptionDialog.setAdapter(adt, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onClick(which);
				}
			}
		});
		selectOptionDialog.create().show();			
	}

	static class LanguageListAdapter extends ArrayAdapter<YTLanguageType> {

		//		int promptLayout;
		YTLanguageType[] items;
		Context context;
		public LanguageListAdapter(Context context, int selectDialogItem,
				YTLanguageType[] objects) {
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
			//            TextView tvText1 = (TextView)convertView.findViewById(android.R.id.text1);
			//            TextView tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
			//            tvText1.setText(getItem(position).city);
			//            tvText2.setText(getItem(position).distance);
			return convertView;
		}
	}
}
