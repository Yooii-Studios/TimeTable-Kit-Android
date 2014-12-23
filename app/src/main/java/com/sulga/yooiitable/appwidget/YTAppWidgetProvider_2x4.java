package com.sulga.yooiitable.appwidget;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.*;
import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.theme.parts.*;
import com.sulga.yooiitable.timetable.*;

public class YTAppWidgetProvider_2x4 extends AppWidgetProvider {
	//TimetableDataManager tdm;
	private int rowHeight = 0;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

		MyLog.d("YTAppWidgetProvider_2x4", "onUpdate");

//		MyLog.d("FileThreadSafeTest", "get timetabledatamanager instance from appwidget");
//		ArrayList<Timetable> timetables = TimetableDataManager.getInstance().getTimetables();
		Timetable mainTable = TimetableDataManager.getMainTimetable();

		final int N = appWidgetIds.length;
		MyLog.d("YTAppWidgetProvider_2x4", "appwidgetId length : " + N);
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];
			/*RemoteViews widgetViews =
					new RemoteViews(context.getPackageName(), R.layout.appwidget_2x4_yt);
//			views.setTextViewText(R.id.appwidget_2x4_yt_test_text, 
//					timetables.get(timetables.size() - 1).getLessonList().size() 
//					+ " Maintable Lessons exist!");

			Drawable backgroundDrawable = 
				 BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
					context, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT,
					TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, mainTable.getTheme().getGridBackground());
			backgroundDrawable.setAlpha(mainTable.getTheme().getImageAlpha());

			Bitmap bitmap = BackgroundDrawableCreator.drawableToBitmap(backgroundDrawable);

			widgetViews.setBitmap(R.id.appwidget_2x4_root, "setBackground", bitmap);

			widgetViews.setInt(R.id.appwidget_2x4_root, "setBackgroundResource", R.drawable.yt_appwidget_2x4_root_background_shape);


			RemoteViews tmp =
					new RemoteViews(context.getPackageName(), R.layout.appwidget_view_timecell);
			//views.removeAllViews(R.id.appwidget_2x4_dynamic_addview_wrapper_test);
			//views.addView(R.id.appwidget_2x4_dynamic_addview_wrapper_test, tmp);
			 
*/			
			RemoteViews widgetViews = getWidgetViews(context, mainTable, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, widgetViews);
//			TimetableDataManager.getInstance().destroyStatics();
		}
	}

	public RemoteViews getWidgetViews(Context context, Timetable mainTable, int appWidgetId){
		//동적 위젯뷰(꽉참)
//		RemoteViews widgetViews = 
//				createAppWidgetView(context,mainTable,2,10);
		//비트맵으로 하는 방법.(이러니까 꽉 안참)
		//위젯 컨피규어 로드.
		int argbColor= YTAppWidgetConfigure.getAppWidgetARGBColor(context, appWidgetId);
		int alpha = Color.alpha(argbColor);
		int r = Color.red(argbColor);
		int g = Color.green(argbColor);
		int b = Color.blue(argbColor);
		int rgbColor = Color.rgb(r, g, b);

		YTShapeRoundRectThemePart widgetThemePart = 
				new YTShapeRoundRectThemePart(AppWidgetTableViewCreator.ROUNDRECT_RADIUS, 
						alpha, rgbColor, 0, 0);
		
		int periodNum = mainTable.getPeriodNum();
		
		View timetableView = 
				AppWidgetTableViewCreator.createAppWidgetView(context, 
						widgetThemePart, mainTable, 2, periodNum, false);
		int w = ((int) context.getResources().getDimension(R.dimen.appwidget_2x4_minWidth));
		int h = ((int) context.getResources().getDimension(R.dimen.appwidget_2x4_realHeight));
		//216, 441 - 288, 588 //1.3333, 1.333333
		MyLog.d("onUpdate", "w : " + w + ", h : " + h);
//		int w = timetableView.getMeasuredWidth();tag
//		int h = timetableView.getMeasuredHeight();
		MyLog.d("createAppWidgetView", ""+timetableView.getHeight());
		
		
		
		AppWidgetTableViewCreator.prepareToAddLessonViews(w, h, mainTable, periodNum, timetableView, false);
		AppWidgetTableViewCreator.addLessonViews(context, timetableView, periodNum, 2, mainTable, false);
		MyLog.d("createAppWidgetView", ""+timetableView.getHeight());
		Bitmap bmp = AppWidgetTableViewCreator.getBitmapOfView(w, h, timetableView);
		
		RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout_byimg);
		Intent configIntent = new Intent (context, TimetableActivity.class);
	    configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	    PendingIntent pIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    widgetViews.setOnClickPendingIntent(R.id.appwidget_root, pIntent);
	    
		widgetViews.setBitmap(R.id.appwidget_byimg_imgview, "setImageBitmap", bmp);
		return widgetViews;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		MyLog.d("YTAppWidgetProvider_2x4", "onReceive");
		if (intent.getAction().equals(Actions.YT_ACTION_TIMETABLE_DATA_CHANGED_2x4)) {
			// handle intent here
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidget = new ComponentName(
					context.getPackageName(), YTAppWidgetProvider_2x4.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

			onUpdate(context, appWidgetManager, appWidgetIds);

			//String s = intent.getStringExtra("NewString");
//			MyLog.d("YTAppWidgetProvider_2x4", "YT DATA CHANGED!");
		}
	}

	public static void onTimetableDataChanged(Context context){
		MyLog.d("YTAppWidgetProvider_2x4", "onTimetableDataChanged");
		Intent intent = new Intent(Actions.YT_ACTION_TIMETABLE_DATA_CHANGED_2x4);
		context.sendBroadcast(intent);
	}
}
