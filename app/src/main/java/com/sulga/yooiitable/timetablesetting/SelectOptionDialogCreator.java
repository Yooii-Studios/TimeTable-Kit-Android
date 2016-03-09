package com.sulga.yooiitable.timetablesetting;

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
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.YTTimetableTheme;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;

import java.util.ArrayList;
import java.util.List;

public class SelectOptionDialogCreator {

	//private OnSelectOptionDialogItemSelectedListener listener;

	public static void showListDialog(
			Context context, String title, 
			String[] itemNames,
			final OnSelectOptionDialogItemSelectedListener listener){
		AlertDialog.Builder selectOptionDialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context, android.R.style.Theme_Dialog));

		ArrayList<String> itemNameList = new ArrayList<String>();
		for(int i = 0; i < itemNames.length ; i++){
			itemNameList.add(itemNames[i]);
		}

		//ArrayList<Object> itemObjs = getItemObjectList(items);

		selectOptionDialog.setTitle(title);

		ArrayAdapter<String> adt = new ArrayAdapter<>(
				context, android.R.layout.select_dialog_item, itemNameList);
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

	public static void showThemeUnlockListDialog(Context context, String title, 
			String[] itemNames, ThemeType[] values,
			final OnSelectOptionDialogItemSelectedListener listener){
		AlertDialog.Builder selectOptionDialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context, android.R.style.Theme_Dialog));

		ArrayList<ThemeAdapterItem> itemList = new ArrayList<>();
		for(int i = 0; i < itemNames.length ; i++){
			//set isLocked Logic through Theme...
			boolean isLocked = false;
			if(!TimetableDataManager.getCurrentFullVersionState(context)){
				for(int j = 0; j < YTTimetableTheme.lockedThemes.length ; j++){
					YTTimetableTheme.ThemeType type = YTTimetableTheme.lockedThemes[j];
					MyLog.d("ThemeTypes", "type : " + type + ", values[i] : " + values[i]);
					if(type == values[i]){
						isLocked = true;
						break;
					}
				}
			}
			itemList.add(new ThemeAdapterItem(itemNames[i], isLocked));
		}
		//ArrayList<Object> itemObjs = getItemObjectList(items);

		selectOptionDialog.setTitle(title);

		ThemeListAdapter adt = new ThemeListAdapter(
				context, android.R.layout.select_dialog_item, itemList);
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

	private static class ThemeAdapterItem{
		String name;
		boolean isLocked;
		//		int day;
		//		int color;
		public ThemeAdapterItem(String name, boolean isLocked){
			this.name = name;
			this.isLocked = isLocked;
			//			this.color = color;
			//			this.day = day;
		}
	}

	static class ThemeListAdapter extends ArrayAdapter<ThemeAdapterItem> {

		//		int promptLayout;
		List<ThemeAdapterItem> items;
		Context context;
		public ThemeListAdapter(Context context, int selectDialogItem,
				ArrayList<ThemeAdapterItem> objects) {
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
			ThemeAdapterItem tai = items.get(position);
			TextView themeName = (TextView)convertView
					.findViewById(R.id.item_theme_spinner_text);
			ImageView lockImg = (ImageView)convertView
					.findViewById(R.id.item_theme_spinner_img); 
			if(!tai.isLocked){
				lockImg.setVisibility(View.GONE);
			}else{
				lockImg.setVisibility(View.VISIBLE);
			}

			String themeNameString = null;
			themeNameString = tai.name;
			themeName.setText(themeNameString);
			//            TextView tvText1 = (TextView)convertView.findViewById(android.R.id.text1);
			//            TextView tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
			//            tvText1.setText(getItem(position).city);
			//            tvText2.setText(getItem(position).distance);
			return convertView;
		}
	}
	//	private static ArrayList<String> getItemNameList(ArrayList<ListDialogItem> items){
	//		ArrayList<String> itemNames = new ArrayList<String>();
	//		
	//		for(int i = 0; i < items.size() ; i++){
	//			itemNames.add(items.get(i).getItemName());
	//		}
	//		return itemNames;
	//	}
	//	
	//	private static ArrayList<Object> getItemObjectList(ArrayList<ListDialogItem> items){
	//		ArrayList<Object> itemObjs = new ArrayList<Object>();
	//		for(int i = 0; i < items.size() ; i++){
	//			itemObjs.add(items.get(i).getItem());
	//		}
	//		return itemObjs;
	//	}

	public interface OnSelectOptionDialogItemSelectedListener{
		public void onClick(int clickedItemPosition);
	}
}
