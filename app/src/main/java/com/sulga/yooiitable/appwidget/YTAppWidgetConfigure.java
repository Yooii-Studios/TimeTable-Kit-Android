package com.sulga.yooiitable.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.YTTimetableTheme;

public class YTAppWidgetConfigure extends AppCompatActivity {
	private static final String TAG = "YTAppWidgetConfigure";

	//컨피규어 액티비티는 앱위젯 아이디를 받는게 필수.
	private int mAppWidgetId;

	//	private SeekBar colorSeekBar;
	private SeekBar alphaSeekBar;

	private LinearLayout colorPickWrapper;
	private ImageView checkImage;

	private RelativeLayout colorPreview;
	private TextView colorPreviewText;

	private Button ok;

	private int alpha = 255;
	//	private int colorRGB = 0x33;
	private int textColor = Color.BLACK;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.d(TAG, "onCreate Called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appwidget_configure);

		initAppWidgetConfigureActivity();
		//		initColorSettingViews();
		initAlphaSettingViews();
		initColorPickWrapperViews();
		initColorPreview();

		ok = (Button)findViewById(R.id.activity_appwidget_configure_btn_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int R = Color.red(currentPickedColor);
				int G = Color.green(currentPickedColor);
				int B = Color.blue(currentPickedColor);
				MyLog.d(TAG, "currentPickedColor : " + currentPickedColor + ", R : " + R + ", G : " + G + ", B : " + B);
				onConfigurationFinished(Color.argb(alpha, R, G, B));
			}
		});
	}
	private void initAppWidgetConfigureActivity(){
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		Intent cancelResultValue = new Intent();
		cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_CANCELED, cancelResultValue);
		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	}

	//	private void initColorSettingViews(){
	//		colorSeekBar = (SeekBar) findViewById(R.id.activity_appwidget_configure_colorseekbar);
	//		colorRGB = (int) (0xff * ((float)colorSeekBar.getProgress() / 100.0f));
	//		colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	//			
	//			@Override
	//			public void onStopTrackingTouch(SeekBar seekBar) {
	//				// TODO Auto-generated method stub
	//				
	//			}
	//			
	//			@Override
	//			public void onStartTrackingTouch(SeekBar seekBar) {
	//				// TODO Auto-generated method stub
	//				
	//			}
	//			
	//			@Override
	//			public void onProgressChanged(SeekBar seekBar, int progress,
	//					boolean fromUser) {
	//				// TODO Auto-generated method stub
	//				colorRGB = (int) (((float)0xff) * ((float)progress / 100.0f));
	//				colorPreview.setBackgroundColor(Color.argb(alpha, colorRGB, colorRGB, colorRGB));
	//				if(colorRGB < 0x7d){
	//					//검정색에 가까워짐
	//					textColor = Color.WHITE;
	//				}else{
	//					textColor = Color.BLACK;
	//				}
	//				colorPreviewText.setTextColor(textColor);
	//				MyLog.d(TAG, "ColorRGB : " + colorRGB);
	//			}
	//		});
	//	}
	private void initAlphaSettingViews(){
		alphaSeekBar = (SeekBar) findViewById(R.id.activity_appwidget_configure_alphaseekbar);
		alpha = (int) (255.0f * ((float)alphaSeekBar.getProgress() / 100.0f));
		//		alphaSeekBarText = (TextView) findViewById(R.id.activity_appwidget_configure_alpha_progress_text);
		//		alphaSeekBarText.setText(Integer.toString(alphaSeekBar.getProgress()));
		alphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				alpha = (int) (255.0f * ((float)progress / 100.0f));
				colorPreview.setBackgroundColor(currentPickedColor);
				colorPreview.getBackground().setAlpha(alpha);

				//				alphaSeekBarText.setText(Integer.toString(progress));
			}
		});
	}

	//	private int defAlpha = (int)(255f * 0.2f);
	//	private static final String[] colors = new String[]{
	//		"#ffffff",
	//		"#000000",
	//		"#e54848",
	//		"#70f0d0",
	//		"#fff556",
	//		"#fd8136",
	//		"#ed487a",
	//	};
	private int currentPickedColor = Color.WHITE;
	private void initColorPickWrapperViews(){
		colorPickWrapper = (LinearLayout) findViewById(R.id.activity_appwidget_configure_pick_color_wrapper);
		checkImage = (ImageView) 
				findViewById(R.id.activity_appwidget_configure_pick_color_checkimage);
		checkImage.bringToFront();
		
		String[] colors = YTTimetableTheme.LESSON_COLORS_THEME_A.clone();
		String[] _colors = new String[colors.length + 1];
		_colors[colors.length] = "#000000";
		for(int i = 0 ; i < colors.length ; i++){
			_colors[i] = colors[i];
		}

		int columnNum = YTTimetableTheme.LESSON_COLORS_THEME_A.length + 1;
		LayoutInflater inflater = this.getLayoutInflater();
		for(int i = 0; i < columnNum ; i++){
			FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.view_appwidget_configure_colorpick, colorPickWrapper, false);
			ll.setBackgroundColor(Color.parseColor(_colors[i]));
			//			if(!_colors[i].equals("#ffffff"))
			//				ll.getBackground().setAlpha(defAlpha);
			//			back.setAlpha((int) (255f * 0.7f));
			colorPickWrapper.addView(ll);
			ll.setOnClickListener(new onColorPickListener(Color.parseColor(_colors[i])));
		}
		setCheckImageTo(colorPickWrapper.getChildAt(0));
	}

	private void setCheckImageTo(View colorView){
//		LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) colorView.getLayoutParams();
		FrameLayout.LayoutParams imgLParams = (FrameLayout.LayoutParams) checkImage.getLayoutParams();
//		MyLog.d(TAG, "lParams : " + lParams.leftMargin + ", imaParams : " + imgLParams.leftMargin);
		imgLParams.leftMargin = colorView.getLeft();
		checkImage.setLayoutParams(imgLParams);
	}

	private class onColorPickListener implements View.OnClickListener{

		private int color;
		public onColorPickListener(int color){

			this.color = color;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//			for(int i = 0; i < colorPickWrapper.getChildCount() ; i++){
			//				colorPickWrapper.getChildAt(i).getBackground().setAlpha(defAlpha);
			//			}
			//			v.getBackground().setAlpha(255);

			currentPickedColor = color;
			colorPreview.setBackgroundColor(currentPickedColor);
			colorPreview.getBackground().setAlpha(alpha);
			textColor = Color.BLACK;
			if(color == Color.BLACK){
				textColor = Color.WHITE;
			}
			colorPreviewText.setTextColor(textColor);
            ImageView check = (ImageView) findViewById(R.id.activity_appwidget_configure_pick_color_checkimage);
            if(color == Color.WHITE){
                check.setImageResource(R.drawable.ic_action_done_black);
            }else{
                check.setImageResource(R.drawable.ic_action_done);
            }
			setCheckImageTo(v);
		}
	};
	private void initColorPreview(){
		colorPreview = (RelativeLayout)findViewById(R.id.activity_appwidget_configure_colorpreview);
		colorPreviewText = (TextView)findViewById(R.id.activity_appwidget_configure_colorpreview_text);
		colorPreview.setBackgroundColor(currentPickedColor);
		colorPreview.getBackground().setAlpha(alpha);
		//		if(colorRGB < 0x7d){
		//			//검정색에 가까워짐
		//			textColor = Color.WHITE;
		//		}else{
		//			textColor = Color.BLACK;
		//		}
		colorPreviewText.setTextColor(textColor);
	}

	private void onConfigurationFinished(int argbColor){

		setAppWidgetConfigure(this, mAppWidgetId, argbColor);
		MyLog.d(TAG, "widgetId : " + mAppWidgetId + ", argbColor : " + argbColor +", currentColor : " + currentPickedColor);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		YTAppWidgetProvider_2x4.onTimetableDataChanged(this);
		YTAppWidgetProvider_4x4.onTimetableDataChanged(this);
		finish();
	}

	public static void setAppWidgetConfigure(Context context, int appWidgetId, 
			int argbColor){
		SharedPreferences prefs =
				context.getSharedPreferences("AppWidgetConfigure", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		String colorKey = Integer.toString(appWidgetId) + "ARGB";
		edit.putInt(colorKey, argbColor);
		edit.apply();
	}

	/**
	 * 
	 * @param context
	 * @param appWidgetId
	 * @return 
	 * ARGB Color
	 */
	public static int getAppWidgetARGBColor(Context context, int appWidgetId){
		SharedPreferences prefs =
				context.getSharedPreferences("AppWidgetConfigure", Context.MODE_PRIVATE);
		String colorKey = Integer.toString(appWidgetId) + "ARGB";
		int argbColor = prefs.getInt(colorKey, 0x7d333333);

		MyLog.d(TAG, "get / widgetId : " + appWidgetId + ", argbColor : " + argbColor);
		return argbColor;
	}

}
