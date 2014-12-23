package com.sulga.yooiitable.showalltables;

import java.util.*;

import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.TextView;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.Toast;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.*;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.customviews.*;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.overlapviewer.*;
import com.sulga.yooiitable.utils.*;

public class ShowAllTimetablesActivity extends Activity {

	//LinearLayout root;
	//ArrayList<Bitmap> timetableImages;
	//ShowAllTimetablesViewPager mPager;
	ArrayList<View> timetablePreViewHolder;
	SnappingHorizontalScrollView scr;
	TransformLinearLayout wrapper;
	TextView timetableTitle;
	Button goToOverlapModeButton;
	//	ArrayList<Integer> selectedItems;
	//	private SharedPreferences mPrefs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		mPrefs = getPreferences(MODE_PRIVATE);

		setContentView(R.layout.activity_show_all);
		View root = findViewById(R.id.activity_show_all_root);
		FixTileModeBug.fixBackgroundRepeat(root);
		timetablePreViewHolder = new ArrayList<View>();

		//		ArrayList<Integer> tmp = getSelectedItemsArrayPref();
		//		if(tmp == null){
		//			selectedItems = new ArrayList<Integer>();
		//		}else{
		//			selectedItems = tmp;
		//		}

		//adds back button to app icon on action bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String title = getResources().getString(R.string.activity_showall_title);
		actionBar.setTitle(title);

		scr = (SnappingHorizontalScrollView)findViewById(R.id.activity_show_all_scroll);
		wrapper = (TransformLinearLayout)findViewById(R.id.activity_show_all_wrapper);
		timetableTitle = (TextView)findViewById(R.id.activity_show_all_tabletitle);
		goToOverlapModeButton = (Button)findViewById(R.id.activity_show_all_tooverlap_btn);
		boolean showAllMode = getIntent().getBooleanExtra("ShowAll", false);
		boolean fromOverlapMode = getIntent().getBooleanExtra("OverlapMode", false);

		if(showAllMode == false){

			goToOverlapModeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ArrayList<Integer> selectedItems = getCheckedPreViewIndex();
					if(selectedItems.size() == 0){
						String warn = getResources().getString(R.string.activity_show_all_select_least_one_table);
						Toast.makeText(ShowAllTimetablesActivity.this, 
//								"At least one timetable should be selected.",
								warn,
								Toast.LENGTH_LONG).show();
						return;
					}else{
						//					finishOverlapMode();
						Intent intent = new Intent(ShowAllTimetablesActivity.this,
								OverlapTablesViewerActivity.class);
						intent.putExtra("OverlapIndex", selectedItems);
						startActivity(intent);
						finish();
					}
				}
			});
		}else{
			goToOverlapModeButton.setVisibility(View.GONE);
		}

		//		new YTShapeRoundRectThemePart(12.0f, 
		//				255, Color.parseColor("#1f1f1f"),
		//				0, 0).setViewTheme(this, timetableTitle);

		scr.setSnappingFeatures(wrapper);
		//scr.setSnappingFeatures(wrapper);
		addPreViews(fromOverlapMode);

		final ArrayList<Timetable> timetables = TimetableDataManager.getTimetables();
		scr.setOnSnappingFinishedListener(new SnappingHorizontalScrollView.OnScrollStoppedListener(){

			@Override
			public void onScrollStopped() {
				// TODO Auto-generated method stub
				timetableTitle.setText(timetables.get(scr.getCurrentSnappedItem()).getTitle());
			}

		});
		timetableTitle.setText(timetables.get(0).getTitle());
		
		//		initModeButtons();

		if(fromOverlapMode == true){
			
			//must post this because checkbox is hidden before timetable view.
			wrapper.post(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ArrayList<Integer> selectedItems = 
							(ArrayList<Integer>) 
							getIntent().getSerializableExtra("OverlapIndex");
					if(selectedItems == null)
						goOverlapMode();
					else
						goOverlapMode(selectedItems);
					scr.scrollTo(scr.getWidth(), 0);
				}
			});
		}else{
			hidePreViewsCheckBox();
		}
	}
	
	public void onStart(){
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryConstants.APP_KEY);
	}

	public void onStop(){
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	//	private ArrayList<Timetable> timetables = TimetableDataManager.getInstance().getTimetables();
	private void addPreViews(boolean isFromOverlap){
		TimetablePreviewViewCreator creator = new TimetablePreviewViewCreator(this);
		ArrayList<Timetable> timetables = TimetableDataManager.getTimetables();
		for(int i = 0; i < timetables.size(); i++){
			View v = creator.getTTPreView(timetables.get(i));
			final int index = i;
			v.setClickable(true);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent data = new Intent();
					data.putExtra("SelectedTimetableIndex", index);
					setResult(android.app.Activity.RESULT_OK, data);
					finish();
				}
			});
			wrapper.addView(v);
			timetablePreViewHolder.add(v);
		}
	}

	//	@Override
	//	protected void onPause(){
	//		super.onPause();
	//	
	//		StringBuilder str = new StringBuilder();
	//		for(int i = 0; i < selectedItems.size() ; i++){
	//			str.append(selectedItems.get(i)).append(",");
	//		}
	//		mPrefs.edit().putString("SelectedItems", str.toString());
	//		mPrefs.edit().putInt("SelectedItemSize", selectedItems.size());
	//	}	
	//	private ArrayList<Integer> getSelectedItemsArrayPref(){
	//		String savedString = mPrefs.getString("SelectedItems", null);
	//		int size = mPrefs.getInt("SelectedItemSize", -1);
	//		if(size <= 0 || savedString == null){
	//			return null;
	//		}
	//		StringTokenizer st = new StringTokenizer(savedString, ",");
	//		ArrayList<Integer> items = new ArrayList<Integer>();
	//		for(int i = 0; i < size ; i++){
	//			items.add(Integer.parseInt(st.nextToken()));
	//		}
	//		return items;
	//	}

	//	private LinearLayout modeSelectPanel;
	//	private LinearLayout confirmPanel;
	//	private RelativeLayout confirm;
	//	private TextView modeText;
	//	private RelativeLayout cancel;
	//	private ImageButton overlapModeButton;
	//	private void initModeButtons(){
	//		modeSelectPanel = 
	//				(LinearLayout)findViewById(R.id.activity_show_all_modebuttons_wrapper);
	//		confirmPanel = 
	//				(LinearLayout)findViewById(R.id.activity_show_all_confirmbuttons_wrapper);
	//		confirm = 
	//				(RelativeLayout)findViewById(R.id.activity_show_all_ok);
	//		modeText = 
	//				(TextView)findViewById(R.id.activity_show_all_modeText);
	//		cancel =
	//				(RelativeLayout)findViewById(R.id.activity_show_all_cancel);
	//
	//		overlapModeButton = 
	//				(ImageButton)findViewById(R.id.activity_show_all_overlapmode_button);
	//
	//		/**
	//		 * 
	//		 */
	//		ImageButton deleteModeButton =
	//				(ImageButton)findViewById(R.id.activity_show_all_deletemode_button);
	//		//delete not supported currently...
	//		deleteModeButton.setVisibility(View.GONE);
	//		/**
	//		 * 
	//		 */
	//
	//		overlapModeButton.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				goOverlapMode();
	//			}
	//		});
	//
	//		//		deleteModeButton.setOnClickListener(new View.OnClickListener() {
	//		//
	//		//			@Override
	//		//			public void onClick(View v) {
	//		//				// TODO Auto-generated method stub
	//		//				showPreViewsCheckBox();
	//		//				modeSelectPanel.setVisibility(View.INVISIBLE);
	//		//				confirmPanel.setVisibility(View.VISIBLE);
	//		//				modeText.setText("Delete?");
	//		//			}
	//		//		});
	//
	//		confirm.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				ArrayList<Integer> checkedItems = getCheckedPreViewIndex();
	//				if(checkedItems.size() == 0){
	//					Toast.makeText(ShowAllTimetablesActivity.this, 
	//							"At least one timetable should be selected.",
	//							Toast.LENGTH_LONG).show();
	//					return;
	//				}else{
	//						
	////					finishOverlapMode();
	//					Intent intent = new Intent(ShowAllTimetablesActivity.this,
	//							OverlapTablesViewerActivity.class);
	//					intent.putExtra("OverlapIndex", checkedItems);
	//					startActivity(intent);
	////					
	////					Toast.makeText(ShowAllTimetablesActivity.this, 
	////							checkedItems.toString(),
	////							Toast.LENGTH_LONG).show();
	//					//start overlap.
	//				}
	//			}
	//		});
	//		cancel.setOnClickListener(new View.OnClickListener() {
	//			@Override
	//			public void onClick(View v) {
	//				finishOverlapMode();
	//			}
	//		});
	//	}


	private void goOverlapMode(){

		showPreViewsCheckBox();
		//		checkCheckBoxes();
		blockPreViewsClick();
		//		modeSelectPanel.setVisibility(View.INVISIBLE);
		//		confirmPanel.setVisibility(View.VISIBLE);
		//		modeText.setText("Overlap?");
	}

	private void goOverlapMode(ArrayList<Integer> selectedItems){

		showPreViewsCheckBox(selectedItems);
		//		checkCheckBoxes();
		blockPreViewsClick();
		//		modeSelectPanel.setVisibility(View.INVISIBLE);
		//		confirmPanel.setVisibility(View.VISIBLE);
		//		modeText.setText("Overlap?");
	}


	private void finishOverlapMode(){
		allowPreViewsClick();
		hidePreViewsCheckBox();
		//		modeSelectPanel.setVisibility(View.VISIBLE);
		//		confirmPanel.setVisibility(View.INVISIBLE);
	}

	private void showPreViewsCheckBox(){
		for(int i = 0; i < timetablePreViewHolder.size(); i++){
			View v = timetablePreViewHolder.get(i);
			CheckBox cb = (CheckBox) 
					v.findViewById(R.id.view_showalltables_item_checkbox);
//			cb.setVisibility(View.VISIBLE);
			cb.setChecked(true);
			cb.bringToFront();
		}
	}

	private void showPreViewsCheckBox(ArrayList<Integer> selectedItems){

		for(int i = 0; i < timetablePreViewHolder.size(); i++){
			View v = timetablePreViewHolder.get(i);
			CheckBox cb = (CheckBox) 
					v.findViewById(R.id.view_showalltables_item_checkbox);
//			cb.setVisibility(View.VISIBLE);
			cb.setChecked(false);
			cb.bringToFront();
		}
		for(int i = 0; i < selectedItems.size() ; i++){
			View v = timetablePreViewHolder.get(selectedItems.get(i));
			CheckBox cb = (CheckBox) 
					v.findViewById(R.id.view_showalltables_item_checkbox);
			cb.setChecked(true);
			cb.bringToFront();
		}
	}


	private void blockPreViewsClick(){
		for(int i = 0; i < timetablePreViewHolder.size() ; i++){
			View v = timetablePreViewHolder.get(i);
			v.setOnClickListener(null);
			CheckBox cb = (CheckBox)
					v.findViewById(R.id.view_showalltables_item_checkbox);
			cb.setClickable(true);

		}
	}

	private void hidePreViewsCheckBox(){
		for(int i = 0; i < timetablePreViewHolder.size(); i++){
			View v = timetablePreViewHolder.get(i);
			CheckBox cb = (CheckBox) 
					v.findViewById(R.id.view_showalltables_item_checkbox);
			cb.setChecked(false);
			cb.setVisibility(View.GONE);
			cb.bringToFront();
		}
	}
	private void allowPreViewsClick(){
		for(int i = 0; i < timetablePreViewHolder.size() ; i++){
			View v = timetablePreViewHolder.get(i);
			v.setClickable(true);
		}
	}

	private ArrayList<Integer> getCheckedPreViewIndex(){
		ArrayList<Integer> checkedItems = new ArrayList<Integer>();

		for(int i = 0; i < timetablePreViewHolder.size(); i++){
			View v = timetablePreViewHolder.get(i);
			CheckBox cb = (CheckBox) 
					v.findViewById(R.id.view_showalltables_item_checkbox);
			//			cb.setVisibility(View.GONE);
			//			cb.bringToFront();
			if(cb.isChecked() == true){
				checkedItems.add(i);
			}
		}
		return checkedItems;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	


	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//	    getMenuInflater().inflate(R.menu.menu_main, menu);
	//	    return true;
	//	}
	//
	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//	    switch (item.getItemId()) {
	//	    case android.R.id.home:
	//	        NavUtils.navigateUpFromSameTask(this);
	//	        break;
	//	    case R.id.back:
	//	        Intent in = new Intent(this, <classname which you want to go back>.class);
	//	        startActivity(in);
	//	        break;
	//	    }
	//	    return false;
	//	}

}

