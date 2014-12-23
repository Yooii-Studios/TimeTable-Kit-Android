package com.sulga.yooiitable.overlapviewer;

import java.util.*;

import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.*;

import android.content.*;
import android.os.*;
import android.view.*;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.*;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.showalltables.*;
import com.sulga.yooiitable.utils.*;

public class OverlapTablesViewerActivity extends Activity {

	private boolean goDirectOverlapMode = false;
	private Button goToShowAll;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overlap_tables);
		FixTileModeBug.fixBackgroundRepeat(findViewById(R.id.activity_overlap_tables_root));
		
		Intent intent = getIntent();
		final ArrayList<Integer> overlapIndex = (ArrayList<Integer>)
				intent.getExtras().getSerializable("OverlapIndex");
		
		FrameLayout root = (FrameLayout) findViewById(R.id.activity_overlap_root);
		goToShowAll = (Button)findViewById(R.id.activity_overlap_toshowall_btn);
		goToShowAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = NavUtils.getParentActivityIntent(OverlapTablesViewerActivity.this); 
				Intent intent = new Intent(OverlapTablesViewerActivity.this, 
						ShowAllTimetablesActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MyLog.d("OverlapTablesViewerACtivity", intent.toString());
				//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
				intent.putExtra("OverlapMode", true);		
				intent.putExtra("OverlapIndex", overlapIndex);
				
				startActivityForResult(intent, RequestCodes.CALL_ACTIVITY_SHOW_ALL);
				finish();
			}
		});
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String title = getResources().getString(R.string.activity_overlap_title);
		actionBar.setTitle(title);
		
		
		
		goDirectOverlapMode = intent.getExtras().getBoolean("DirectOverlap", false);
		if(overlapIndex == null){
			String warn = getResources().getString(R.string.activity_overlap_error_null);
			Toast.makeText(
					this, 
//					"Error : overlap timetable datas null", 
					warn,
					Toast.LENGTH_LONG)
					.show();
			finish();
		}

		final ArrayList<Timetable> selectedTables = new ArrayList<Timetable>();
		ArrayList<Timetable> originalTables = TimetableDataManager.getTimetables();
		for(int i = 0; i < overlapIndex.size() ; i++){
			selectedTables.add(originalTables.get(overlapIndex.get(i)));
		}
		final FrameLayout widgetView = OverlapTablesViewCreator
				.createAppWidgetView(this,
				selectedTables);

		ViewTreeObserver vto = widgetView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				OverlapTablesViewCreator
				.addLessonViews(OverlapTablesViewerActivity.this,
						widgetView,
						selectedTables);				
				widgetView
				.getViewTreeObserver()
				.removeGlobalOnLayoutListener(this);
			}

		});

		root.addView(widgetView);
		//		OverlapTablesViewCreator.addLessonViews(context, widgetView, widgetRowNum, widgetColumnNum, timetable)
	}
//
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
//			if(goDirectOverlapMode == true){
//				//if user clicked overlap icon directly from TimetableActivity AddTableFragment, 
//				//then on nav button clicked, app launches ShowAllTablesActivity and go directly to overlap mode.
//				
//				MyLog.d("OverlapTablesViewerActivity", "called onOptionsItemSelected");
//				Intent intent = NavUtils.getParentActivityIntent(this); 
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				MyLog.d("OverlapTablesViewerACtivity", intent.toString());
//				//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
//
//				intent.putExtra("OverlapMode", true);				
//
//				startActivityForResult(intent, RequestCodes.CALL_ACTIVITY_SHOW_ALL);
//				finish();
//				return true;
//			}else{
//				finish();
//				return true;
//			}
		}
		return super.onOptionsItemSelected(item);
	}	
	
	
	public void onStart(){
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryConstants.APP_KEY);
	}

	public void onStop(){
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
//
//	@Override
//	public void onBackPressed(){
//		if(goDirectOverlapMode == true){
//			//if user clicked overlap icon directly from TimetableActivity AddTableFragment, 
//			//then on nav button clicked, app launches ShowAllTablesActivity and go directly to overlap mode.
//			MyLog.d("OverlapTablesViewerActivity", "called onOptionsItemSelected");
//			Intent intent = NavUtils.getParentActivityIntent(this); 
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			MyLog.d("OverlapTablesViewerACtivity", intent.toString());
//			//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
//
//			intent.putExtra("OverlapMode", true);				
//
//			startActivityForResult(intent, RequestCodes.CALL_ACTIVITY_SHOW_ALL);
//			finish();
//		}else{
//			finish();
//		}
//	}

}
