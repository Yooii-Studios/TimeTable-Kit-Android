package com.sulga.yooiitable.theme;

import android.graphics.*;

import com.sulga.yooiitable.*;
import com.sulga.yooiitable.theme.parts.*;
import com.sulga.yooiitable.timetable.fragments.*;

public class YTTimetableTheme {

	public static final int SRC_NONE = -1;
	public static final String[] LESSON_COLORS_THEME_A = new String[]{
		"#e54848",
		"#70f0d0",
		"#fff556",
		"#fd8136",
		"#ed487a",
		"#86abe8",
		"#ee5d5d",
		"#77cf65",
		"#ffcd73",
		"#4479d4",
		"#9f69d6",
		"#61d8a2",
		"#bf5730",
		"#ee3c7e",
		"#b3f36d",
		"#1e6d74",
		"#ff7373",
		"#269926",
		"#6187cf"
		//		"#7ABA7A",
		//		"#B76EB8",
		//		"#3D72A4",
		//		"#CC9933"
	};

	//	private static final String[] LESSON_COLORS_THEME_CLEAR = new String[]{
	//		"#e2dcc8",
	//		"#969696",
	//		"#7e9cb3",
	//		"#c2c2c2",
	//		"#c3d2df",
	//		"#e49bb6",
	//		//임시
	//		"#ee5d5d",
	//		"#a570c7",
	//		"#77cf65"
	//
	//		//		"#7ABA7A",
	//		//		"#B76EB8",
	//		//		"#3D72A4",
	//		//		"#CC9933"
	//	};

	public static final class Timetable_Theme_One_Spring_Day{
		public static final YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_one_spring_day);

//		public static final YTThemePart timetable_title_background = 
//				new YTTiledRoundedRectBitmap(
//						R.drawable.yt_timetable_title_background_src_theme_a, 
//						255, 
//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
//						true, true, true, true);
		public static final YTRoundRectThemePart timetable_grid_background = 
				new YTTiledRoundedRectBitmap(
						R.drawable.yt_timetable_body_background_src_theme_a,
						255,
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY
						);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				null;
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.parseColor("#c89f90"),
						0, 0);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
//				new YTTiledRoundedRectBitmap(
//						R.drawable.yt_timetable_dayrow_date_background_src_theme_a,
//						255,
//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
//						false, false, false, false
//						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.parseColor("#ca918d"),
						0, 0);
//						R.drawable.yt_timetable_dayrow_day_background_src_theme_a,
//						255,
//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
//						false, false, true, false
//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.WHITE,
						0, 0
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.parseColor("#ca918d"),
						0, 0);
//				new YTTiledRoundedRectBitmap(
//						R.drawable.yt_timetable_dayrow_day_background_src_theme_a,
//						255,
//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
//						false, false, false, false
//						);
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.parseColor("#c89f90"),
						0, 0);
//				new YTTiledRoundedRectBitmap(
//						R.drawable.yt_timetable_dayrow_date_background_src_theme_a,
//						255,
//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
//						false, false, false, false
//						);

		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.WHITE,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_a);			
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_background_theme_a);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_a);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_a);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_a);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_delete_timetable_theme_a);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_a);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#D1A49E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_white);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.WHITE;
	}

	public static final class Timetable_Theme_Icarus{
		private static final int basicFrameAlpha = (int) (255f * 0.5f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.75f);
		public static final int lessonViewAlpha = (int) (255f * 0.75f);

		public static final YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_icarus);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTRoundRectThemePart timetable_grid_background =
				null;
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1);
		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;

	}

	public static final class Timetable_Theme_Grid_Paper{

		private static final int basicFrameAlpha = (int) (255f * 0.5f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		public static final int markRangeAlpha = (int) (255f * 0.75f);
		public static final int lessonViewAlpha = (int) (255f * 0.75f);

		public static final YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_notebook);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						(int)(255f * 0.7f), 
						frameColorCode,
						Color.parseColor("#2e2e2e"), 2
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);

		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						0, 0
						);

		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						0, 0
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						0, 0
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}
	
	public static class Timetable_Theme_Key_Lime_Pie{
		private static final int basicFrameAlpha = (int) (255f * 0.35f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.9f);
		public static final int lessonViewAlpha = (int) (255f * 0.9f);

		//사진 예시용 테스트.
		public static YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_lime_pie);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background =
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}

	public static class Timetable_Theme_Magnolia{
		private static final int basicFrameAlpha = (int) (255f * 0.35f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.9f);
		public static final int lessonViewAlpha = (int) (255f * 0.9f);

		//사진 예시용 테스트.
		public static YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_magnolia);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background =
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}

	public static class Timetable_Theme_Strawberry_Smoothie{
		private static final int basicFrameAlpha = (int) (255f * 0.35f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.9f);
		public static final int lessonViewAlpha = (int) (255f * 0.9f);

		//사진 예시용 테스트.
		public static YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_strawberry_smoothie);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background =
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}

	public static class Timetable_Theme_Summer_Time{
		private static final int basicFrameAlpha = (int) (255f * 0.35f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.9f);
		public static final int lessonViewAlpha = (int) (255f * 0.9f);

		//사진 예시용 테스트.
		public static YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_summer_time);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background =
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}

	public static class Timetable_Theme_Doggie_Overload{
		private static final int basicFrameAlpha = (int) (255f * 0.35f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.9f);
		public static final int lessonViewAlpha = (int) (255f * 0.9f);

		//사진 예시용 테스트.
		public static YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_doggie_overload);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background =
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}

		
	public static final class Timetable_Theme_BlackAndWhite{

		private static final int basicFrameAlpha = 255;
		private static final int titleFrameColorCode = Color.parseColor("#ffffff");
		private static final int titleFrameOutlineColorCode = Color.parseColor("#d2d0c6");
		private static final int titleFrameOutlineWidth = 1;
		private static final int timelineFrameColorCode = Color.parseColor("#ffffff");
		private static final int timelineFrameOutlineColorCode = Color.parseColor("#d2d0c6");
		private static final int timelineFrameOutlineWidth = 1;
		private static final int dayrowFrameColorCode = Color.parseColor("#ffffff");
		private static final int dayrowFrameOutlineColorCode = Color.parseColor("#d2d0c6");;
		private static final int dayrowFrameOutlineWidth = 1;
		private static final int timecellFrameColorCode = Color.WHITE;
		private static final int timecellFrameOutlineColorCode = Color.parseColor("#d2d0c6");;
		private static final int timecellFrameOutlineWidth = 1;
		
//		private static final int outlineColorCode = Color.parseColor("#d2d0c6");
		public static final int markRangeAlpha = 255;
		public static final int lessonViewAlpha = 255;

		public static final YTThemePart timetable_root_background = 
				new YTColorThemePart(Color.parseColor("#ffffff"));

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						(int)(255f * 0.7f), 
						titleFrameColorCode,
						titleFrameOutlineColorCode, titleFrameOutlineWidth
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);

		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						dayrowFrameColorCode,
						dayrowFrameOutlineColorCode, dayrowFrameOutlineWidth
						);

		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						0, 0
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						0, 0
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						timecellFrameColorCode,
						timecellFrameOutlineColorCode, timecellFrameOutlineWidth
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						timelineFrameColorCode,
						timelineFrameOutlineColorCode, timelineFrameOutlineWidth
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						Color.WHITE,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);
		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}	
	
	public static final class Timetable_Theme_Grey{

		private static final int basicFrameAlpha = 255;
		private static final int titleFrameColorCode = Color.parseColor("#2e2e2e");
		private static final int titleFrameOutlineColorCode = 0;
		private static final int titleFrameOutlineWidth = 0;
		private static final int timelineFrameColorCode = Color.parseColor("#2e2e2e");
		private static final int timelineFrameOutlineColorCode = 0;
		private static final int timelineFrameOutlineWidth = 0;
		private static final int dayrowFrameColorCode = Color.parseColor("#2e2e2e");
		private static final int dayrowFrameOutlineColorCode = 0;
		private static final int dayrowFrameOutlineWidth = 0;
		private static final int timecellFrameColorCode = Color.parseColor("#bababa");
		private static final int timecellFrameOutlineColorCode = Color.WHITE;
		private static final int timecellFrameOutlineWidth = 3;
		
//		private static final int outlineColorCode = Color.parseColor("#d2d0c6");
		public static final int markRangeAlpha = 255;
		public static final int lessonViewAlpha = 255;

		public static final YTThemePart timetable_root_background = 
				new YTColorThemePart(Color.parseColor("#bababa"));

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						(int)(255f * 0.7f), 
						titleFrameColorCode,
						titleFrameOutlineColorCode, titleFrameOutlineWidth
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);

		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						dayrowFrameColorCode,
						dayrowFrameOutlineColorCode, dayrowFrameOutlineWidth
						);

		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						0, 0
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						0, 0
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						timecellFrameColorCode,
						timecellFrameOutlineColorCode, timecellFrameOutlineWidth
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						timelineFrameColorCode,
						timelineFrameOutlineColorCode, timelineFrameOutlineWidth
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						Color.WHITE,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_white);
		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.WHITE;
	}
	
	public static class Timetable_Theme_Photo{
		private static final int basicFrameAlpha = (int) (255f * 0.35f);
		private static final int frameColorCode = Color.parseColor("#ffffff");
		private static final int borderColorCode = Color.parseColor("#d2d0c6");

		public static final int markRangeAlpha = (int) (255f * 0.9f);
		public static final int lessonViewAlpha = (int) (255f * 0.9f);

		//사진 예시용 테스트.
		public static YTThemePart timetable_root_background = 
				new YTBackgroundBitmapThemePart(R.drawable.yt_root_background_summer_time);

		public static final YTThemePart timetable_title_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha, 
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTRoundRectThemePart timetable_grid_background =
				null;
//				new YTColorThemePart(Color.TRANSPARENT);
		public static final YTThemePart timetable_lessonViewDropMarkerBackground_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255,
						Color.CYAN,
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_wrapper_background = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_dayrow_date_background =
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_dayrow_daycell_divider_background = 
				null;
		//				new YTNormalThemePart(
		//						R.drawable.yt_timetable_empty_white_background_shape, 
		//						basicFrameAlpha);
		public static final YTShapeRoundRectThemePart timetable_dayrow_day_background = 
				null;
		//				new YTShapeRoundRectThemePart(
		//						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
		//						basicFrameAlpha,
		//						frameColorCode,
		//						borderColorCode, 1
		//						);
		public static final YTThemePart timetable_timecell_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);
		public static final YTShapeRoundRectThemePart timetable_timeline_wrapper_background =
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						basicFrameAlpha,
						frameColorCode,
						borderColorCode, 1
						);

		public static final YTShapeRoundRectThemePart timetable_timeline_background_1 =  
				null;
		public static final YTShapeRoundRectThemePart timetable_timeline_background_2 =  
				null;
		public static final YTThemePart timetable_selected_range_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						markRangeAlpha,
						Color.parseColor("#aabbcc"),
						0, 0
						);
		public static final YTShapeRoundRectThemePart timetable_lessonview_background_shape = 
				new YTShapeRoundRectThemePart(
						TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						lessonViewAlpha,
						frameColorCode,
						0, 0
						);
		//icons
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_wrapper_theme_clear);
		public static final YTThemePart yt_timetable_icon_modebuttons_wrapper_background = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_wrapper_background_theme_clear);
		public static final YTThemePart yt_timetable_icon_addrow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_add_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_removerow = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_remove_row_theme_clear);
		public static final YTThemePart yt_timetable_icon_cleartable = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_clear_theme_clear);
		public static final YTThemePart yt_timetable_icon_deletetable = 
				new YTIdOnlyThemePart(
						R.drawable.yt_icon_timetable_delete_timetable_theme_clear);
		public static final YTThemePart yt_timetable_icon_option = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_timetable_option_theme_clear);
		public static final YTThemePart yt_timetable_icon_pageinfo_lower = 
				new YTIdOnlyThemePart(R.drawable.yt_icon_share_data);
//				new YTIdOnlyThemePart(
//						R.drawable.yt_icon_timetable_pageinfo_lower_theme_clear);
		public static final YTShapeRoundRectThemePart yt_timetable_icon_pageinfo_upper = 
				new YTShapeRoundRectThemePart(TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY,
						255, Color.parseColor("#2E2E2E"),
						0, 0);
		//timeline
		public static final YTThemePart yt_timetable_timeline_time = 
				new YTIdOnlyThemePart(R.drawable.yt_timetable_timeline_time_black);

		//timetable textcolor
		public static final int yt_timetable_textcolor = 
				Color.BLACK;
	}

	public enum ThemeType // 순서대로 정렬
	{
		One_Spring_Day,
		Icarus,
		Grid_Paper,
		Key_Lime_Pie,
		BlackAndWhite,
		Grey,
		Magnolia,
		Strawberry_Smoothie,
		Summer_Time,
		Doggie_Overload,
		Photo
	}
	
	public static ThemeType[] lockedThemes = {
			ThemeType.One_Spring_Day,
			ThemeType.Magnolia,
			ThemeType.Doggie_Overload,
			ThemeType.Summer_Time,
			ThemeType.Key_Lime_Pie,
			ThemeType.Strawberry_Smoothie
	};

	//	public String[] getLessonColors(){
	//		switch(m_Theme){
	//		case Photo:
	//			return LESSON_COLORS_THEME_CLEAR;
	//		case Emerald_Blue : 
	//			return LESSON_COLORS_THEME_CLEAR;
	//		case NoteBook : 
	//			return LESSON_COLORS_THEME_CLEAR;
	//		case Theme_A:
	//			return LESSON_COLORS_THEME_A;
	//		default:
	//			return null;
	//		}
	//	}

	private ThemeType m_Theme;
	public ThemeType getCurrentTheme(){
		return m_Theme;
	}
	private YTThemePart root_background;
	public YTThemePart getRootBackground(){
		return root_background;
	}

	public void setRootBackground(int resId){
		root_background = new YTIdOnlyThemePart(resId);
	}

//	private YTThemePart title_background;
//	public YTThemePart getTitleBackground(){
//		return title_background;
//	}

	private YTRoundRectThemePart grid_background;
	public YTRoundRectThemePart getGridBackground(){
		return grid_background;
	}

	private YTThemePart lessonViewDropMarkerBackground_Shape;
	public YTThemePart getLessonViewDropMarkerBackgroundShape(){
		return lessonViewDropMarkerBackground_Shape;
	}

	private YTRoundRectThemePart dayrow_wrapper_background;
	public YTRoundRectThemePart getDayrowWrapperBackground(){
		return dayrow_wrapper_background;
	}
	private YTRoundRectThemePart dayrow_date_background;
	public YTRoundRectThemePart getDayrowDateBakcground(){
		return dayrow_date_background;
	}

	private YTThemePart dayrow_divider_background;
	public YTThemePart getDayrowDividerBackground(){
		return dayrow_divider_background;
	}
	private YTRoundRectThemePart dayrow_day_background;
	public YTRoundRectThemePart getDayrowDayBackground(){
		return dayrow_day_background;
	}

	private YTThemePart timecell_background_shape;
	public YTThemePart getTimecellBackgroundShape(){
		return timecell_background_shape;
	}

	private YTRoundRectThemePart timeline_wrapper_background;
	public YTRoundRectThemePart getTimelineWrapperBackground(){
		return timeline_wrapper_background;
	}

	private YTRoundRectThemePart timeline_background_1;
	public YTRoundRectThemePart getTimelineBackground_1(){
		return timeline_background_1;
	}

	private YTRoundRectThemePart timeline_background_2;
	public YTRoundRectThemePart getTimelineBackground_2(){
		return timeline_background_2;
	}

	private YTThemePart timeline_time;
	public YTThemePart getTimelineTime(){
		return timeline_time;
	}

	private YTThemePart selected_range_background;
	public YTThemePart getSelectedRangeBackground(){
		return selected_range_background;
	}

	private YTShapeRoundRectThemePart lessonview_background_shape;
	public YTShapeRoundRectThemePart getLessonViewBackgroundShape(){
		return lessonview_background_shape;
	}

	//icons	
	private YTThemePart icon_modebuttons_wrapper;
	public YTThemePart getModeButtonsWrapperIcon(){
		return icon_modebuttons_wrapper;
	}

	private YTThemePart icon_modebuttons_wrapper_background;
	public YTThemePart getModeButtonsWrapperbackgroundIcon(){
		return icon_modebuttons_wrapper_background;
	}

	private YTThemePart icon_addRow;
	public YTThemePart getAddRowIcon(){
		return icon_addRow;
	}

	private YTThemePart icon_removeRow;
	public YTThemePart getRemoveRowIcon(){
		return icon_removeRow;
	}
	
	private YTThemePart icon_clearTable;
	public YTThemePart getClearTableIcon(){
		return icon_clearTable;
	}

	private YTThemePart icon_deleteTimetable;
	public YTThemePart getDeleteTimetableIcon(){
		return icon_deleteTimetable;
	}

	private YTThemePart icon_option;
	public YTThemePart getOptionIcon(){
		return icon_option;
	}

	private YTThemePart icon_pageinfo_lower;
	public YTThemePart getPageInfoLowerIcon(){
		return icon_pageinfo_lower;
	}

	private YTRoundRectThemePart icon_pageinfo_upper;
	public YTRoundRectThemePart getPageInfoUpperIcon(){
		return icon_pageinfo_upper;
	}

	private int timetable_textcolor;
	public int getTimetableTextColor(){
		return timetable_textcolor;
	}


	//	
	//	public YTTimetableTheme()
	//	{
	//		m_Theme = ThemeType.Theme_A;
	//		setTheme(m_Theme);
	//	}

	public YTTimetableTheme(YTTimetableTheme.ThemeType themeType)
	{
		m_Theme = themeType;
		setTheme(m_Theme);
	}
	//
	//	public YTTimetableTheme(Parcel source){
	//		///////////////////////////////readTypedList하니까 왜 오류가 나지???
	//		//this();
	//		//		parentTimetable = source.readParcelable(Timetable.class.getClassLoader());
	//		//
	//		//		periodInfo =  source.readParcelable(PeriodInfo.class.getClassLoader());
	//		//		lessonName = source.readString();
	//		//		lessonWhere = source.readString();
	//		//		professor = source.readString();
	//		//		lessonMemo = source.readString();
	//		//		lessonColor = source.readInt();
	//
	//
	//		/*
	//		credit = source.readInt();
	//		color = source.readInt();
	//		 */
	//		try {
	//			m_Theme = ThemeType.valueOf(source.readString());
	//		} catch (IllegalArgumentException x) {
	//			m_Theme = null;
	//		}
	//
	//		//m_Theme = source.read;
	//
	//		imageAlpha = 
	//				source.readInt();
	//		shapeAlpha = 
	//				source.readInt();
	//		markRangeAlpha = 
	//				source.readInt();
	//		lessonViewAlpha = 
	//				source.readInt();
	//		root_background = 
	//				source.readInt();
	//		title_background = 
	//				source.readInt();
	//		grid_background = 
	//				source.readInt();
	//		lessonViewDropMarkerBackground_Shape = 
	//				source.readInt();
	//		dayrow_date_background = 
	//				source.readInt();
	//		dayrow_divider_background = 
	//				source.readInt();
	//		dayrow_day_background = 
	//				source.readInt();
	//		timecell_background_shape = 
	//				source.readInt();
	//		timeline_background_1 = 
	//				source.readInt();
	//		timeline_background_2 = 
	//				source.readInt();
	//		selected_range_background = 
	//				source.readInt();
	//		lessonview_background_shape =
	//				source.readInt();
	//		
	//		//icons
	//		icon_modebuttons_wrapper = 
	//				source.readInt();
	//		icon_addRow = 
	//				source.readInt();
	//		icon_removeRow = 
	//				source.readInt();
	//		icon_deleteTimetable = 
	//				source.readInt();
	//		icon_option = 
	//				source.readInt();		
	//		icon_pageinfo_lower = 
	//				source.readInt();
	//		icon_pageinfo_upper = 
	//				source.readInt();
	//	}

	public void setTheme(ThemeType theme){
		switch(theme){
		case One_Spring_Day:
			m_Theme = ThemeType.One_Spring_Day;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_One_Spring_Day.timetable_root_background;
//			title_background = 
//					Timetable_Theme_A.timetable_title_background;
			grid_background = 
					Timetable_Theme_One_Spring_Day.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_One_Spring_Day.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_One_Spring_Day.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_One_Spring_Day.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_One_Spring_Day.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_One_Spring_Day.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_One_Spring_Day.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_One_Spring_Day.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_One_Spring_Day.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_One_Spring_Day.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_One_Spring_Day.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_One_Spring_Day.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_One_Spring_Day.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_One_Spring_Day.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_One_Spring_Day.yt_timetable_textcolor;
			break;
		case Icarus :
			m_Theme = ThemeType.Icarus;

			//			basicFrameColor = 
			//					Timetable_Theme_Emerald_Blue.timetable_frame_color;
			//			imageAlpha = 
			//					Timetable_Theme_Emerald_Blue.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_Emerald_Blue.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_Emerald_Blue.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_Emerald_Blue.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Icarus.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Emerald_Blue.timetable_title_background;
			grid_background = 
					Timetable_Theme_Icarus.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Icarus.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Icarus.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Icarus.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Icarus.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Icarus.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Icarus.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Icarus.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Icarus.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Icarus.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Icarus.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Icarus.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Icarus.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Icarus.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Icarus.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Icarus.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Icarus.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Icarus.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Icarus.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Icarus.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Icarus.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Icarus.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Icarus.yt_timetable_textcolor;
			break;
		case Grid_Paper :
			m_Theme = ThemeType.Grid_Paper;

			//			basicFrameColor = 
			//					Timetable_Theme_NoteBook.timetable_frame_color;
			//			imageAlpha = 
			//					Timetable_Theme_NoteBook.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_NoteBook.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_NoteBook.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_NoteBook.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Grid_Paper.timetable_root_background;
//			title_background = 
//					Timetable_Theme_NoteBook.timetable_title_background;
			grid_background = 
					Timetable_Theme_Grid_Paper.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Grid_Paper.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Grid_Paper.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Grid_Paper.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Grid_Paper.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Grid_Paper.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Grid_Paper.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Grid_Paper.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Grid_Paper.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Grid_Paper.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Grid_Paper.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Grid_Paper.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Grid_Paper.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Grid_Paper.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Grid_Paper.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Grid_Paper.yt_timetable_textcolor;
			break;
			
		case Key_Lime_Pie : 
			m_Theme = ThemeType.Key_Lime_Pie;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Lime_Pie.timetable_title_background;
			grid_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Key_Lime_Pie.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Key_Lime_Pie.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Key_Lime_Pie.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Key_Lime_Pie.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Key_Lime_Pie.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Key_Lime_Pie.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Key_Lime_Pie.yt_timetable_textcolor;
			break;
		case Magnolia : 
			m_Theme = ThemeType.Magnolia;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Magnolia.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Magnolia.timetable_title_background;
			grid_background = 
					Timetable_Theme_Magnolia.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Magnolia.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Magnolia.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Magnolia.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Magnolia.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Magnolia.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Magnolia.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Magnolia.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Magnolia.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Magnolia.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Magnolia.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Magnolia.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Magnolia.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Magnolia.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Magnolia.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Magnolia.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Magnolia.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Magnolia.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Magnolia.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Magnolia.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Magnolia.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Magnolia.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Magnolia.yt_timetable_textcolor;
			break;

		case Strawberry_Smoothie : 
			m_Theme = ThemeType.Strawberry_Smoothie;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Strawberry_Smoothie.timetable_title_background;
			grid_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Strawberry_Smoothie.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Strawberry_Smoothie.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Strawberry_Smoothie.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Strawberry_Smoothie.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Strawberry_Smoothie.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Strawberry_Smoothie.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Strawberry_Smoothie.yt_timetable_textcolor;
			break;

		case Summer_Time : 
			m_Theme = ThemeType.Summer_Time;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Summer_Time.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Summer_Time.timetable_title_background;
			grid_background = 
					Timetable_Theme_Summer_Time.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Summer_Time.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Summer_Time.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Summer_Time.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Summer_Time.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Summer_Time.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Summer_Time.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Summer_Time.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Summer_Time.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Summer_Time.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Summer_Time.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Summer_Time.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Summer_Time.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Summer_Time.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Summer_Time.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Summer_Time.yt_timetable_textcolor;
			break;

		case Doggie_Overload : 
			
			m_Theme = ThemeType.Doggie_Overload;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Doggie_Overload.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Doggie_Overload.timetable_title_background;
			grid_background = 
					Timetable_Theme_Doggie_Overload.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Doggie_Overload.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Doggie_Overload.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Doggie_Overload.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Doggie_Overload.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Doggie_Overload.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Doggie_Overload.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Doggie_Overload.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Doggie_Overload.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Doggie_Overload.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Doggie_Overload.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Doggie_Overload.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Doggie_Overload.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Doggie_Overload.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Doggie_Overload.yt_timetable_textcolor;
			break;

		case BlackAndWhite:
			m_Theme = ThemeType.BlackAndWhite;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_BlackAndWhite.timetable_root_background;
//			title_background = 
//					Timetable_Theme_BlackAndWhite.timetable_title_background;
			grid_background = 
					Timetable_Theme_BlackAndWhite.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_BlackAndWhite.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_BlackAndWhite.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_BlackAndWhite.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_BlackAndWhite.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_BlackAndWhite.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_BlackAndWhite.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_BlackAndWhite.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_BlackAndWhite.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_BlackAndWhite.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_BlackAndWhite.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_BlackAndWhite.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_BlackAndWhite.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_BlackAndWhite.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_BlackAndWhite.yt_timetable_textcolor;
			break;

		case Grey:
			m_Theme = ThemeType.Grey;

			//			imageAlpha = 
			//					Timetable_Theme_A.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_A.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_A.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_A.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Grey.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Grey.timetable_title_background;
			grid_background = 
					Timetable_Theme_Grey.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Grey.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Grey.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Grey.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Grey.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Grey.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Grey.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Grey.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Grey.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Grey.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Grey.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Grey.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Grey.timetable_lessonview_background_shape;
			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Grey.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Grey.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Grey.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Grey.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Grey.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Grey.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Grey.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Grey.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Grey.yt_timetable_icon_pageinfo_upper;
			timetable_textcolor = 
					Timetable_Theme_Grey.yt_timetable_textcolor;
			break;
	
		case Photo:
			m_Theme = ThemeType.Photo;

			//			basicFrameColor = 
			//					Timetable_Theme_Photo.timetable_frame_color;
			//			imageAlpha = 
			//					Timetable_Theme_Photo.imageAlpha;
			//			shapeAlpha = 
			//					Timetable_Theme_Photo.shapeAlpha;
			//			markRangeAlpha = 
			//					Timetable_Theme_Photo.markRangeAlpha;
			//			lessonViewAlpha = 
			//					Timetable_Theme_Photo.lessonViewAlpha;
			root_background = 
					Timetable_Theme_Photo.timetable_root_background;
//			title_background = 
//					Timetable_Theme_Photo.timetable_title_background;
			grid_background = 
					Timetable_Theme_Photo.timetable_grid_background;
			lessonViewDropMarkerBackground_Shape = 
					Timetable_Theme_Photo.timetable_lessonViewDropMarkerBackground_shape;
			dayrow_wrapper_background = 
					Timetable_Theme_Photo.timetable_dayrow_wrapper_background;
			dayrow_date_background = 
					Timetable_Theme_Photo.timetable_dayrow_date_background;
			dayrow_divider_background = 
					Timetable_Theme_Photo.timetable_dayrow_daycell_divider_background;
			dayrow_day_background = 
					Timetable_Theme_Photo.timetable_dayrow_day_background;
			timecell_background_shape = 
					Timetable_Theme_Photo.timetable_timecell_background_shape;
			timeline_wrapper_background = 
					Timetable_Theme_Photo.timetable_timeline_wrapper_background;
			timeline_background_1 = 
					Timetable_Theme_Photo.timetable_timeline_background_1;
			timeline_background_2 = 
					Timetable_Theme_Photo.timetable_timeline_background_2;
			timeline_time = 
					Timetable_Theme_Photo.yt_timetable_timeline_time;
			selected_range_background = 
					Timetable_Theme_Photo.timetable_selected_range_background_shape;
			lessonview_background_shape =
					Timetable_Theme_Photo.timetable_lessonview_background_shape;

			//icons
			icon_modebuttons_wrapper = 
					Timetable_Theme_Photo.yt_timetable_icon_modebuttons_wrapper;
			icon_modebuttons_wrapper_background = 
					Timetable_Theme_Photo.yt_timetable_icon_modebuttons_wrapper_background;
			icon_addRow = 
					Timetable_Theme_Photo.yt_timetable_icon_addrow;
			icon_removeRow = 
					Timetable_Theme_Photo.yt_timetable_icon_removerow;
			icon_clearTable = 
					Timetable_Theme_Photo.yt_timetable_icon_cleartable;
			icon_deleteTimetable = 
					Timetable_Theme_Photo.yt_timetable_icon_deletetable;
			icon_option =
					Timetable_Theme_Photo.yt_timetable_icon_option;
			icon_pageinfo_lower = 
					Timetable_Theme_Photo.yt_timetable_icon_pageinfo_lower;
			icon_pageinfo_upper = 
					Timetable_Theme_Photo.yt_timetable_icon_pageinfo_upper;

			timetable_textcolor = 
					Timetable_Theme_Photo.yt_timetable_textcolor;
			break;

		default:
			break;
		}
	}

	//	public static String[] getLessonColors(YTTimetableTheme.ThemeType themeType){
	//		switch(themeType){
	//		case Photo:
	//			return LESSON_COLORS_THEME_CLEAR;
	//		case Emerald_Blue : 
	//			return LESSON_COLORS_THEME_CLEAR;
	//		case NoteBook : 
	//			return LESSON_COLORS_THEME_CLEAR;
	//		case Theme_A:
	//			return LESSON_COLORS_THEME_A;
	//		default:
	//			return LESSON_COLORS_THEME_A;		
	//		}
	//	}

	public static String[] getThemeTypeStrings(){
		ThemeType[] themes = ThemeType.values();
		String[] results = new String[themes.length];
		for(int i = 0; i < themes.length ; i++){
			results[i] = themes[i].toString();
		}
		return results;
	}

	public static ThemeType[] getThemeTypeValues(){
		return ThemeType.values();
	}

	//	@Override
	//	public int describeContents() {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	@Override
	//	public void writeToParcel(Parcel dest, int flags) {
	//		// TODO Auto-generated method stub
	//		dest.writeString((m_Theme == null) ? "" : m_Theme.name());
	//
	//		dest.writeInt(imageAlpha);
	//		dest.writeInt(shapeAlpha);
	//		dest.writeInt(markRangeAlpha);
	//		dest.writeInt(lessonViewAlpha);
	//		dest.writeInt(root_background);
	//		dest.writeInt(title_background);
	//		dest.writeInt(grid_background);
	//		dest.writeInt(lessonViewDropMarkerBackground_Shape) ;
	//		dest.writeInt(dayrow_date_background); 
	//		dest.writeInt(dayrow_divider_background);
	//		dest.writeInt(dayrow_day_background);
	//		dest.writeInt(timecell_background_shape); 
	//		dest.writeInt(timeline_background_1); 
	//		dest.writeInt(timeline_background_2);
	//		dest.writeInt(selected_range_background); 
	//		dest.writeInt(lessonview_background_shape);
	//		
	//		dest.writeInt(icon_modebuttons_wrapper);
	//		dest.writeInt(icon_addRow);
	//		dest.writeInt(icon_removeRow);
	//		dest.writeInt(icon_deleteTimetable);
	//		dest.writeInt(icon_option);
	//		dest.writeInt(icon_pageinfo_lower);
	//		dest.writeInt(icon_pageinfo_upper);
	//
	//	}
	//	
	//	public static final Parcelable.Creator<YTTimetableTheme> CREATOR = 
	//			new Parcelable.Creator<YTTimetableTheme>(){
	//
	//		@Override
	//		public YTTimetableTheme createFromParcel(Parcel source) {
	//			// TODO Auto-generated method stub
	//			return new YTTimetableTheme(source);
	//		}
	//
	//		@Override
	//		public YTTimetableTheme[] newArray(int size) {
	//			// TODO Auto-generated method stub
	//			return new YTTimetableTheme[size];
	//		}
	//	};

}
