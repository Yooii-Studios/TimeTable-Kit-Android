package com.sulga.yooiitable.timetablesetting;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.R;


public class PanelManager {
	
	
	private static final String TAG = "PanelManager";
	
//	private static PanelManager instance = null;
//	private LinearLayout mPreviewPanel;
//	public static PanelManager getInstance(){
//		if (instance == null)
//			instance = new PanelManager();
//		return instance;
//	}
//	protected PanelManager(){
//	}
//	
//	public void setPreviewPanel(LinearLayout vg){
//		this.mPreviewPanel = vg;
//	}
//	public LinearLayout getPreviewPanel(){
//		return this.mPreviewPanel;
//	}
	
//	public static ArrayList<ArrayList<SettingContentItem>> sSettingItemList;
	
//	static{
//		sSettingItemList = new ArrayList<ArrayList<SettingContentItem>>();
//	}
	public static LinearLayout makeFooterView(Context context, int imgResId, String name){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout item = (LinearLayout)inflater.inflate(R.layout.setting_footer, null);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		item.setLayoutParams(param);
		
		ImageView icon = (ImageView)item.findViewById(R.id.icon);
		icon.setImageResource(imgResId);
//		ThemeColorSet.getInstance().applyIconColor(icon);
		TextView desc = (TextView)item.findViewById(R.id.name);
		desc.setText(name);
//		ThemeColorSet.getInstance().applyFontColor(desc);
		
		return item;
	}
	
	public static RelativeLayout makeContentView(
			Context context, String title, View contentView){
				//		sSettingItemList.add(src);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout item = (RelativeLayout)inflater.inflate(R.layout.setting_content, null);
		FrameLayout wrapper = (FrameLayout) item.findViewById(R.id.contentsViewWrapper);
		wrapper.addView(contentView);
		
//		initHeader(item, title, confirmListener, cancelListener);
		
		return item;
	}
	
	private static void initHeader(RelativeLayout item, String title, View.OnClickListener confirmListener, View.OnClickListener cancelListener){
		TextView titleView = (TextView)item.findViewById(R.id.title);
//		ThemeColorSet.getInstance().applyFontColor(titleView);
		titleView.setText(title);
		View doneBtn = item.findViewById(R.id.done);
		View cancelBtn = item.findViewById(R.id.cancel);
//		ThemeColorSet.getInstance().applyIconColor(doneBtn);
//		ThemeColorSet.getInstance().applyIconColor(cancelBtn);
		if (confirmListener != null){
			doneBtn.setVisibility(View.VISIBLE);
			doneBtn.setOnClickListener(confirmListener);
		}
		else{
			doneBtn.setVisibility(View.INVISIBLE);
			doneBtn.setOnClickListener(null);
		}
		if (cancelListener != null){
			cancelBtn.setVisibility(View.VISIBLE);
			cancelBtn.setOnClickListener(cancelListener);
		}
		else{
			cancelBtn.setVisibility(View.INVISIBLE);
			cancelBtn.setOnClickListener(null);
		}
	}
}