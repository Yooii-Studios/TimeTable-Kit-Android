package com.sulga.yooiitable.customviews;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ToggleButton;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.mylog.*;


public class SelectOneItemLinearLayout extends LinearLayout {
	private OnOneItemSelectedListener onSelectItemListener;
	public SelectOneItemLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SelectOneItemLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SelectOneItemLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private int toggleButtonLayoutID;
	private ToggleButton buttonToAdd = null;
	public void addToggleButton(String btnText, final Object item, int toggleButtonLayoutID){
		this.toggleButtonLayoutID = toggleButtonLayoutID;
		
		this.addToggleButton(btnText, item);
	}
	
	public void addToggleButton(String btnText, final Object item, ToggleButton buttonToAdd){
		this.buttonToAdd = buttonToAdd;
		this.addToggleButton(btnText, item);
	}
	
	public void addToggleButton(String btnText, final Object item){
		//
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//ToggleButton btn = (ToggleButton) inflater.inflate(R.layout.view_timetablesetting_togglebutton, this, false);
		ToggleButton btn;
		if(buttonToAdd != null){
			btn = buttonToAdd;
			buttonToAdd = null;
		}else{
			btn = (ToggleButton) inflater.inflate(toggleButtonLayoutID, this, false);
		}
		//ToggleButton btn = (ToggleButton) View.inflate(getContext(), );
		btn.setTextOff(btnText);
		btn.setTextOn(btnText);
		btn.setText(btnText);

		CompoundButton.OnCheckedChangeListener onCheckedChange = 
				new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//이미 체크되있는 뷰가 다시 체크됬을때 클릭을 막아 변화를 못 주도록 한다.
				MyLog.d("onCheckedChange", "buttonView checked : "+ buttonView.isChecked() + ", isChecked : " + isChecked +", Button : " + buttonView.getText());
				/*if(isChecked == false && buttonView.isChecked() == false){
					MyLog.d("onCheckedChange", "Checked button checked again! Return!" + ", Button : " + buttonView.getText());
					buttonView.setChecked(true);
					return; 
				}*/

				LinearLayout parent = (LinearLayout) buttonView.getParent();
				int checkedItemCount = 0;
				for(int i = 0; i < parent.getChildCount() ; i++){
					if( ((ToggleButton)parent.getChildAt(i)).isChecked() == true){
						checkedItemCount++;
					}
				} 
				MyLog.d("onCheckedChange", "buttonView " + buttonView.getText() + ", checkedItemCount : " + checkedItemCount +", Item : " + item.toString());
				if(checkedItemCount == 0){
					buttonView.setChecked(true);
					return;
				}
				for(int i = 0; i < parent.getChildCount() ; i++){
					if(buttonView == parent.getChildAt(i)){
						continue;
					}
					((ToggleButton)parent.getChildAt(i)).setChecked(false);
				}
				//buttonView.seton
				buttonView.setChecked(isChecked);
				if(isChecked == true){
					if(onSelectItemListener != null)
						onSelectItemListener.onSelected(buttonView);
				}
			}
		};
		btn.setOnCheckedChangeListener(onCheckedChange);
		btn.setTag(item);
		
		this.addView(btn);
	}

	public int getSelectedItemPosition(){
		for(int i = 0; i < this.getChildCount() ; i++){
			if(((ToggleButton)getChildAt(i)).isChecked() == true){
				return i;
			}
		}
		return -1;
	}

	public Object getSelectedItem(){
		for(int i = 0; i < this.getChildCount() ; i++){
			if(((ToggleButton)getChildAt(i)).isChecked() == true){
				return getChildAt(i).getTag();
			}
		}
		return null;
	}
	
	public void setDefaultSelectedItem(Object item){
		ToggleButton d = (ToggleButton)findViewWithTag(item);
		if(d != null)
			d.setChecked(true);
	}

	public OnOneItemSelectedListener getOnOneItemSelectedListener() {
		return onSelectItemListener;
	}

	public void setOnOneItemSelectedListener(OnOneItemSelectedListener onSelectItemListener) {
		this.onSelectItemListener = onSelectItemListener;
	}

	public int getToggleButtonLayoutID() {
		return toggleButtonLayoutID;
	}

	public void setToggleButtonLayoutID(int toggleButtonLayoutID) {
		this.toggleButtonLayoutID = toggleButtonLayoutID;
	}

	public interface OnOneItemSelectedListener{
		public abstract void onSelected(View v);
	}	
}
