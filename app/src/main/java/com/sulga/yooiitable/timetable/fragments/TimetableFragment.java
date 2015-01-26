package com.sulga.yooiitable.timetable.fragments;

import java.text.*;
import java.util.*;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;

import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.*;
import android.widget.FrameLayout.LayoutParams;
import android.widget.*;

import com.flurry.android.*;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.alarm.*;
import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.customviews.*;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.sharetable.*;
import com.sulga.yooiitable.theme.*;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;
import com.sulga.yooiitable.timetable.*;
import com.sulga.yooiitable.timetable.fragments.dialogbuilders.*;
import com.sulga.yooiitable.timetableinfo.*;
import com.sulga.yooiitable.utils.*;


public class TimetableFragment extends Fragment {
//	public static final String PAGE_INDEX_KEY = "PageIndex";
	public static TimetableFragment newInstance() {
		TimetableFragment tableFragment = new TimetableFragment();
//		int idx = fragPageIndex;
//		Bundle b = new Bundle();
//		b.putInt(PAGE_INDEX_KEY, idx);
//		tableFragment.setArguments(b);
		return tableFragment;
	}
//
//	public static final int UPLOAD_TIMETABLE_FREEVER_MAX = 3;
//	public static final int UPLOAD_TIMETABLE_FULLVER_MAX = 6;
	
	//	ActionBar acBar;
	public final static int FRAG_TIMETABLE_DIALOG_DELETE_TIMETABLE = 2;
	public final static int FRAG_TIMETABLE_DIALOG_ADD_LESSON = 1;
	public final static int FRAG_TIMETABLE_DIALOG_EDIT_LESSON = 0;

	public final static float FRAG_TIMETABLE_ROUNDRECT_RXY = 12.0f;
	private final static int LONGCLICK_VIBRATE_TIME = 100;

	private class CellTag{
		private int day;
		private int period;	//start period 기준.
		public CellTag(int day, int period){
			this.day = day;
			this.period = period;
		}
		@Override
		public boolean equals(Object tag){
			if(tag == null)
			{
				return false;
			}
			if(this == tag)
			{
				return true;
			}
			if(this.day == ((CellTag)tag).day &&
					this.period == ((CellTag)tag).period)
			{
				return true;
			}else
			{
				return false;
			}		
		}
	}
	//final static int ACT_EDIT = 0;

	//	private Timetable timetable;
	//	TimetableDataManager tdManager;

	//private LinearLayout timeline;
	public SoftKeyboardDetectLinearLayout softKeyboardDetectLayout;

//	private FrameLayout rootView;
	private ImageView backgroundView;

	//	private TextView titleText;

	private ImageView pageInfoLower;
	private TextView pageInfoUpper;

	private ImageButton optionButton;

	//private TextView pageNumberText;
	private LinearLayout grid;
	private RelativeLayout timetableHead;
	private LinearLayout dayRow;
	private FrameLayout dayRowOverlap;
	//private FrameLayout dayRowDividerWrapper;
	private RelativeLayout timelineWrapper;
	private LinearLayout timeline;
	private TimetableScrollView tableScroll;
	private RelativeLayout tableScrollChild;
	private InterceptTouchFrameLayout gridOverlapLayout;

	private ImageView currentTimeMarker;		//현재 시간을 시간표에 마킹해주는 뷰. 가로로 긴 파란색(?)줄이다.

	public boolean isSelectPeriodMode = false; 
	private LinearLayout markSelectedRangeLayout;	//롱클릭으로 선택한 범위를 표시하는 레이아웃.
	private Button markButtonOK;
	private Button markButtonCancel;
	private Button markButtonEditLength;
	private Button pasteLessonToMark;
	//프레임레이아웃에 임시로 띄워진다.

	//수업 뷰를 클릭하면 나타나는 버튼들, 수업 에딧/수업 삭제기능 담당.
	private Button lessonEditOK;
	private Button lessonRemove;
	private Button lessonCopy;
	private ImageView lessonEditLength;

	//
	ModeRelativeLayout testPanel;
	PathButton modeButton_add_row;
	PathButton modeButton_remove_row;
	PathButton modeButton_clear_timetable;
	PathButton modeButton_delete_timetable;

	//Lesson lessonToAddAndEdit;
	//int dIndexOfChild;
	//타임테이블 테마.
	YTTimetableTheme ytTheme;

	private float cellWidth = -1;
	private float cellHeight = -1;
	//private float cellHeight = 0;
	private float rowHeight = -1;	//margin까지 고려한 row의 높이.
	//private float timelineWidth = 0;

	private Bitmap photoBackgroundBitmap = null;

	//레슨뷰 롱클릭후 드래그를 위한 변수들.
	//레슨뷰 복사 이미지는 윈도우에.
	WindowManager wm;
	WindowManager.LayoutParams lessonViewDragImageParams;
	ImageView lessonViewDragImage;
	//드롭 마커(어디에 떨어질지 표시)는 오버랩 레이아웃 자식으로.
	ImageView lessonViewDropMarker;
	FrameLayout.LayoutParams lessonViewDropMarkerParams;
	int mSlop = 0;		// 롱클릭후 얼마나 움직이면 드래그로 인식할까? 기기마다 다르므로 기본 상수를 받아오는 용도임.
	private View fragmentView;
	private Resources res;
	
	private int myPageIndex = -1;
    private Timetable timetable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		MyLog.d("adapter", "TimetableFragment OnCreateView Called");
        if(savedInstanceState != null)
        {
            myPageIndex = savedInstanceState.getInt("SavedTimetableIndex");
            MyLog.d("TimetableFragment", "onCreateView savedInstanceState not null!! page Idx : " + myPageIndex);
        }
		int tmpMyPageIndex = this.getMyIndexInViewPager();
        if(tmpMyPageIndex != -1)
            myPageIndex = tmpMyPageIndex;
        if(myPageIndex == -1)
            return null;

        timetable = getTimetableDataFromManager(myPageIndex);
		MyLog.d("LazyLoadTest", "onCreate : " + myPageIndex);
		res = getResources();

		if(timetable == null){
			return null;
		}
		MyLog.d("onCreateView", "Timetable Initialized..." + timetable.toString());
		ytTheme = new YTTimetableTheme(timetable.getThemeType());
		fragmentView = inflater.inflate(R.layout.fragment_timetable, container, false);
		softKeyboardDetectLayout = (SoftKeyboardDetectLinearLayout)fragmentView
				.findViewById(R.id.fragment_timetable_softkeyboarddetect);
		backgroundView = (ImageView) fragmentView.findViewById(R.id.fragment_timetable_background);
//		if(ytTheme.getCurrentTheme() == YTTimetableTheme.ThemeType.Photo){
//			//포토모드라면 저장되어있는 사진을 로드해서 배경이미지로.
//			try {
//				if (photoBackgroundBitmap != null) {
//					photoBackgroundBitmap.recycle();
//				}
//				photoBackgroundBitmap = 
//						YTBitmapLoader.loadAutoScaledBitmapFromUri(
//								getSupportActivity(), 
//								YTBitmapLoader.getPortraitCroppedImageUri(
//										getSupportActivity(),
//										timetable.getId())
//										);
//				MyLog.d("TimetableFragment", "timetable id : " + timetable.getId());
//				backgroundView.setBackgroundDrawable(
//						new BitmapDrawable(
//								getResources(), photoBackgroundBitmap));
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}else{
//			
//			ytTheme.getRootBackground().setViewTheme(getSupportActivity(), backgroundView);
//		}
		
		//		}
		//row.addView(timelineCell);

		//		titleText = (TextView) fragmentView.findViewById(R.id.fragment_timetable_title_text);
		//		titleText.setTextColor(ytTheme.getTimetableTextColor());

		//int titleBackground = theme.getTitleBackground();

		//		ytTheme.getTitleBackground().setViewTheme(getSupportActivity(), titleText);
		//		Drawable titleTextBackground = 
		//				BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
		//						act, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT,
		//						FRAG_TIMETABLE_ROUNDRECT_RXY, ytTheme.getTitleBackground());
		//		titleTextBackground.setAlpha(ytTheme.getImageAlpha());
		//		titleText.setBackgroundDrawable(titleTextBackground);

		//		titleText.setText(timetable.getTitle());
		//		titleText.setOnClickListener(new TitleTextViewOnClickListener(
		//				"Edit Title", 
		//				timetable.getTitle())
		//				);

		pageInfoLower = (ImageView)fragmentView.findViewById(R.id.fragment_timetable_pagenumber_lower);
		pageInfoLower.setOnClickListener(onShareDataClickedListener);
		ytTheme.getPageInfoLowerIcon().setViewTheme(getSupportActivity(), pageInfoLower);
		//		pageInfoLower.setBackgroundResource(ytTheme.getPageInfoLowerIcon());
		pageInfoUpper = (TextView)fragmentView.findViewById(R.id.fragment_timetable_pagenumber_upper);
		ytTheme.getPageInfoUpperIcon()
		.setViewTheme(getSupportActivity(), pageInfoUpper, FRAG_TIMETABLE_ROUNDRECT_RXY, true, true, false, false);
		//		pageInfoUpper.setBackgroundResource(ytTheme.getPageInfoUpperIcon());

		optionButton = (ImageButton) fragmentView.findViewById(R.id.fragment_timetable_title_option_button);
		ytTheme.getOptionIcon().setViewTheme(getSupportActivity(), optionButton);
		//		optionButton.setBackgroundResource(ytTheme.getOptionIcon());
		optionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyLog.d("TimetableFragment", "optionButton Clicked");
				// TODO Auto-generated method stub
//				Intent act = new Intent(getSupportActivity(), TimetableSettingPagerActivity.class);
				Intent act = new Intent(getSupportActivity(), TimetableSettingInfoActivity.class);
				act.putExtra("TimetablePageIndex", getMyIndexInViewPager());
				getSupportActivity()
				.startActivityForResult(act, RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING);

				Map<String, String> settingClickInfo = new HashMap<String, String>();
				settingClickInfo.put("Setting_ClickedBy", FlurryConstants.SETTING_CLICKTYPE_LION);
				FlurryAgent.logEvent(FlurryConstants.SETTING_CLICKED, settingClickInfo);
			}
		});

		timetableHead = (RelativeLayout)fragmentView.findViewById(R.id.fragment_timetable_headpart);

		//pageNumberText = (TextView) fragmentView.findViewById(R.id.fragment_timetable_pagenumber_upper);
		//왼쪽 페이지부터 1/3, 2/3, 3/3 이렇게 표시되던 페이지를
		//3/3, 2/3, 1/3 으로 나타내기 위해서 필요.
		int reverseStd = TimetableDataManager.getTimetables().size() + 1;
		pageInfoUpper.setText( 
				(reverseStd - ( TimetableDataManager.getInstance().getTimetableIndex(timetable) + 1 ) )
				+ "/" + TimetableDataManager.getTimetables().size()
				);
		dayRow = (LinearLayout) fragmentView.findViewById(R.id.fragment_timetable_dayrow);
		dayRowOverlap = 
				(FrameLayout) fragmentView.findViewById(
						R.id.fragment_timetable_dayrow_overlap);
        dayRowOverlap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startSettingActivity(false, true);
            }
        });
		//dayRowDividerWrapper = (FrameLayout) fragmentView.findViewById(R.id.fragment_timetable_dayrow_overlap);
		//timeline = (LinearLayout) fragmentView.findViewById(R.id	.fragment_timetable_timeline);
		//		dayRow.setBackgroundDrawable(
		//				BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(getSupportActivity(), 12.0f, R.drawable.yt_timetable_dayrow_background_src));
		grid = (LinearLayout) fragmentView.findViewById(R.id.fragment_timetable_cell_list);
//		ytTheme.getGridBackground().setViewTheme(getSupportActivity(), grid);
		//		if(ytTheme.getGridBackground() != YTTimetableTheme.SRC_NONE){
		//			Drawable gridBackgroundDrawable = 
		//					BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
		//							getSupportActivity(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT,
		//							FRAG_TIMETABLE_ROUNDRECT_RXY, ytTheme.getGridBackground());
		//			gridBackgroundDrawable.setAlpha(ytTheme.getImageAlpha());
		//			grid.setBackgroundDrawable(gridBackgroundDrawable);
		//		}else{
		//			grid.setBackgroundColor(Color.TRANSPARENT);
		//		}

		timeline = (LinearLayout) fragmentView.findViewById(R.id.fragment_timetable_timeline);
		timelineWrapper = (RelativeLayout) fragmentView.findViewById(R.id.fragment_timetable_timeline_wrapper);
		//gridOverlapLayout이 터치를 가져가버려 onclick이 실행 안됨. 따라서 handleSelectPerioModeOnTouchUp에서 관리한다.
//		timeline.setOnClickListener(onTimelineClickedListener);
		

		tableScroll = (TimetableScrollView) fragmentView.findViewById(R.id.fragment_timetable_scrollview);
		tableScroll.setParentFragment(this);
		tableScrollChild = (RelativeLayout)fragmentView.findViewById(R.id.fragment_timetable_scrollview_child);
		//tableScroll.setOnTouchListener(timetableScrollOnTouch);

		gridOverlapLayout = (InterceptTouchFrameLayout) 
				fragmentView.findViewById(R.id.fragment_timetable_body_overrap);
		gridOverlapLayout.setOnLongClickListener(overlapOnLongClickListener);
		gridOverlapLayout.setOnTouchListener(overlapOnTouchListener);
		gridOverlapLayout.setParentFragment(this);
		//gridOverlapLayout.setBackgroundResource(R.color.black);

		currentTimeMarker = (ImageView) fragmentView.findViewById(R.id.view_timetable_current_time_marker);
		ytTheme.getTimelineTime().setViewTheme(getSupportActivity(), currentTimeMarker);
		//		currentTimeMarker.setBackgroundResource(ytTheme.getTimelineTime());

		markSelectedRangeLayout = new LinearLayout(this.getSupportActivity());
		markButtonOK = new Button(this.getSupportActivity());
		markButtonOK.setBackgroundResource(R.drawable.ic_oneditlesson_check);
		markButtonCancel = new Button(this.getSupportActivity());
		markButtonCancel.setBackgroundResource(R.drawable.ic_oneditlesson_cancel);
		pasteLessonToMark = new Button(getSupportActivity());
		pasteLessonToMark.setBackgroundResource(R.drawable.ic_oneditlesson_paste);
		markButtonEditLength = new Button(getSupportActivity());
		markButtonEditLength.setBackgroundResource(R.drawable.ic_oneditlesson_4scroll);
		markSelectedRangeLayout.setVisibility(View.INVISIBLE);
		markButtonOK.setVisibility(View.INVISIBLE);
		//markButtonOK.setOnClickListener(markOKOnClick);
		markButtonCancel.setVisibility(View.INVISIBLE);
		markButtonCancel.setOnClickListener(markCancelOnClick);
		pasteLessonToMark.setVisibility(View.INVISIBLE);
		markButtonEditLength.setVisibility(View.INVISIBLE);
		markButtonEditLength.setOnTouchListener(new MarkButtonEditLengthTouchListener());
		//pasteLessonToMark.setOnClickListener(pasteLessonToMarkOnClick);

		lessonEditOK = new Button(this.getSupportActivity());
		lessonEditOK.setBackgroundResource(R.drawable.ic_oneditlesson_check);
		lessonRemove = new Button(this.getSupportActivity());
		lessonRemove.setBackgroundResource(R.drawable.ic_oneditlesson_cancel);
		lessonCopy = new Button(this.getSupportActivity());
		lessonCopy.setBackgroundResource(R.drawable.ic_oneditlesson_copy);
		lessonEditLength = new ImageView(getSupportActivity());
		lessonEditLength.setBackgroundResource(R.drawable.ic_oneditlesson_2scroll);


		//lessonEditLength.setClickable(false);
		lessonEditOK.setVisibility(View.INVISIBLE);
		lessonRemove.setVisibility(View.INVISIBLE);
		lessonCopy.setVisibility(View.INVISIBLE);
		lessonEditLength.setVisibility(View.INVISIBLE);
		//lessonEditOK.setOnClickListener(lessonEditOKOnClick);
		//lessonRemove.setOnClickListener(lessonRemoveOnClick);
		//lessonCopy.setOnClickListener(lessonCopyOnClick);

		gridOverlapLayout.addView(markSelectedRangeLayout);		
		gridOverlapLayout.addView(markButtonOK);
		gridOverlapLayout.addView(markButtonCancel);
		gridOverlapLayout.addView(markButtonEditLength);
		gridOverlapLayout.addView(pasteLessonToMark);
		gridOverlapLayout.addView(lessonEditOK);
		gridOverlapLayout.addView(lessonRemove);
		gridOverlapLayout.addView(lessonCopy);
		gridOverlapLayout.addView(lessonEditLength);

		testPanel = (ModeRelativeLayout) fragmentView.findViewById(R.id.fragment_timetable_menu_button_panel);
		testPanel.setModeOpenBtnClickedListener(new ModeRelativeLayout.ModeOpenBtnClickedListener() {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub

			}
		});
		ImageView panelWrapperIcon = (ImageView) testPanel.findViewById(R.id.ico_plus);
		ytTheme.getModeButtonsWrapperIcon().setViewTheme(getSupportActivity(), panelWrapperIcon);
		//		panelWrapperIcon.setBackgroundResource(ytTheme.getModeButtonsWrapperIcon());
		ImageView panelWrapperBackgroundIcon = 
				(ImageView) testPanel.findViewById(R.id.ico_plus_background);
		ytTheme.getModeButtonsWrapperbackgroundIcon().setViewTheme(
				getSupportActivity(), panelWrapperBackgroundIcon);
		//		panelWrapperBackgroundIcon.setBackgroundResource(
		//				ytTheme.getModeButtonsWrapperbackgroundIcon());
		//
		modeButton_add_row = (PathButton) fragmentView.findViewById(R.id.fragment_timetable_button_add);
		modeButton_add_row.setmOnModeBtnClickedListener(addRowOnClick);
		ytTheme.getAddRowIcon().setViewTheme(getSupportActivity(), modeButton_add_row);
		//		btn1.setBackgroundResource(ytTheme.getAddRowIcon());
		modeButton_add_row.setTag("1");
		modeButton_remove_row = (PathButton) fragmentView.findViewById(R.id.fragment_timetable_button_remove);
		modeButton_remove_row.setmOnModeBtnClickedListener(removeRowOnClick);
		ytTheme.getRemoveRowIcon().setViewTheme(getSupportActivity(), modeButton_remove_row);
		//		btn2.setBackgroundResource(ytTheme.getRemoveRowIcon());
		modeButton_remove_row.setTag("2");
		modeButton_clear_timetable = (PathButton) fragmentView.findViewById(R.id.fragment_timetable_button_cleartable);
		modeButton_clear_timetable.setmOnModeBtnClickedListener(clearTimetableOnClick);
		ytTheme.getClearTableIcon().setViewTheme(getSupportActivity(), modeButton_clear_timetable);
		modeButton_clear_timetable.setTag("3");

		modeButton_delete_timetable = (PathButton) fragmentView.findViewById(R.id.fragment_timetable_button_deletetable);
		modeButton_delete_timetable.setmOnModeBtnClickedListener(deleteTimetableOnClick);
		ytTheme.getDeleteTimetableIcon().setViewTheme(getSupportActivity(), modeButton_delete_timetable);
		//		btn3.setBackgroundResource(ytTheme.getDeleteTimetableIcon());
		modeButton_delete_timetable.setTag("4");

		testPanel.addMenuButtons(modeButton_add_row);
		testPanel.addMenuButtons(modeButton_remove_row);
		testPanel.addMenuButtons(modeButton_clear_timetable);
		testPanel.addMenuButtons(modeButton_delete_timetable);

		createTimetable();

		ViewTreeObserver vto = grid.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(onGridGlobalLayoutListener);

		prepareLessonViewDrag();
		initLessonViewAnimation();

		return fragmentView;
	}
		
	public void onPageSelected(){
//		for(int i = 0 ; i < gridOverlapLayout.getChildCount() ; i++){
//			View v = gridOverlapLayout.getChildAt(i);
//			if(v.getTag() instanceof Lesson){
//				LazyViewAnimationManager.registerLessonViewAnimation(getSupportActivity(), 
//						0, 
//						v, 
//						animSet);
//			}
//		}
	}

	private static AnimationSet animSet;
	private static TranslateAnimation transAnim;
	private static AlphaAnimation alphaAnim;
	private AnimationSet initLessonViewAnimation(){
		//	    RelativeLayout root = (RelativeLayout) findViewById( R.id.rootLayout );
		animSet = new AnimationSet(true);

		//1.translate animation
		//		int xDest = dm.widthPixels/2;
		//		xDest -= (view.getMeasuredWidth()/2);
		int yFrom = -20;
		transAnim = new TranslateAnimation( 
				0, 0, yFrom, 0);
		transAnim.setDuration(400);
		transAnim.setFillAfter( true );

		alphaAnim = new AlphaAnimation(0, 1);
		alphaAnim.setDuration(400);
		alphaAnim.setFillAfter(true);

		animSet.addAnimation(transAnim);
		animSet.addAnimation(alphaAnim);

		return animSet;
	}
	
	private OnGlobalLayoutListener onGridGlobalLayoutListener =
			new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			int myPageIdx = getMyIndexInViewPager();
			MyLog.d("TimetableFragment", "onGolobalLayout called, page idx : " + myPageIdx);
			initGridOnGlobalLayout();
		}
	};

	//	boolean initialized = false;
	private void initGridOnGlobalLayout(){
		//		if(initialized == true){
		//			return;
		//		}
		//		initialized = true;
		MyLog.d("initOnGlobalLayout", this.toString() + ", Called");
		//adds lesson views and init datas needed for adding lesson views.
		float bodyPadding = res.getDimension(R.dimen.fragment_timetable_body_padding);
		View tmpCell = grid.findViewWithTag(new CellTag(0,0));
		View tmpCellParent = (View) tmpCell.getParent();
		cellWidth = tmpCell.getWidth();
		cellHeight = tmpCell.getHeight();
		if(timetable.getPeriodNum() != 1)
			rowHeight = tmpCellParent.getHeight() - bodyPadding;
		else
			rowHeight = tmpCellParent.getHeight() - bodyPadding * 2;
		clearAndAddLessonViews(View.VISIBLE);

		MyLog.d("initOnGlobalLayout", this.toString() + ", Timeline child count : " + timeline.getChildCount());

		if(timeline.getChildCount() == 0){
			for(int i = 1 ; i <= timetable.getPeriodNum() ; i++){
				//정사각형으로 만들기 위해서, addTimeLine은 viewtreeobsetver내에서.
				MyLog.d("createTimeline", "addTimeline called" + i);
				addTimeLine(i);
			}
		}
		MyLog.d("initOnGlobalLayout", this.toString() + ", Timeline child count : " + timeline.getChildCount());
		//cellHeight를 이용해야되서...이 안으로.
		markCurrentTime();
		//
		//		MyLog.d("createTimetable", 
		//				"cellWidth : " + cellWidth + ", cellHeight : " + cellHeight +
		//				", rowHeight : " + tmpCellParent.getHeight() 
		//				+ "lessonNum : " + timetable.getLessonList().size() +
		//				", timelineWidth : " + timeline.getWidth() + 
		//				", timelineHeight : " + timeline.getHeight() +
		//				", timeline.childCount : " + timeline.getChildCount() + 
		//				", grid.getleftMargin : " + ((FrameLayout.LayoutParams)grid.getLayoutParams()).leftMargin);

		grid
		.getViewTreeObserver()
		.removeGlobalOnLayoutListener(onGridGlobalLayoutListener);
	}

	private void markCurrentTime(){
		FrameLayout.LayoutParams currentTimeMarkerParams = 
				(FrameLayout.LayoutParams)currentTimeMarker.getLayoutParams();
		currentTimeMarkerParams.gravity = Gravity.TOP|Gravity.LEFT;
		Calendar currentCal = Calendar.getInstance();

		float curTimeByPeriod = timetable.getPeriodByFloatFromTime(
				currentCal.get(Calendar.HOUR_OF_DAY), currentCal.get(Calendar.MINUTE));
		MyLog.d("markCurrentTime", "current time by period : " +
				curTimeByPeriod);

		if(curTimeByPeriod >= timetable.getPeriodNum()){
			//시간표의 시간 범위를 넘어간다.
			currentTimeMarker.setVisibility(View.GONE);
			return;
		}

		MyLog.d("markCurrentTime", "getViewTopMarginFromPeriod : " + getViewTopMarginFromPeriod(curTimeByPeriod));
		currentTimeMarkerParams.topMargin = 
				(int) this.getViewTopMarginFromPeriod(curTimeByPeriod);
		//				+ (int)res.getDimension(R.dimen.fragment_timetable_body_padding);
		currentTimeMarker.setLayoutParams(currentTimeMarkerParams);
		currentTimeMarker.setVisibility(View.VISIBLE);
		currentTimeMarker.bringToFront();
	}

	private View.OnClickListener onShareDataClickedListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(TimetableDataManager.getIsFirstConnectorLaunch(getSupportActivity())
					== true){
				StartConnectorTutorialDialogBuilder.createDialog(getSupportActivity(),
						TimetableFragment.this)
				.show();
				return;
			}
			updateConnectorUseInfoAndShowShareDialog();
		}
	};
	
	public void updateConnectorUseInfoAndShowShareDialog(){
		dismissProgressDialog();
		String loading = getString(R.string.loading);
		showProgressDialog(loading, loading, new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dismissProgressDialog();
			}
		});
		String uuid = new DeviceUuidFactory(getSupportActivity()).getDeviceUuid().toString();
		String name = UserNameFactory.getUserName(getSupportActivity());
		boolean isFullVersion = TimetableDataManager.getCurrentFullVersionState(getSupportActivity());
		
		TimetableNetworkManager.updateConnectorUseInfo(uuid, name, isFullVersion, getSupportActivity(),
				new TimetableNetworkManager.OnFinishedConnectorAsync(){
					@Override
					public void onFinished(ConnectorState cs, boolean isSucceed) {
						// TODO Auto-generated method stub
						if(isSucceed == true){
							dismissProgressDialog();
							showShareDataDialog(cs);
						}else{
							dismissProgressDialog();
							String warn = getString(R.string.connector_result_failed);
							Toast.makeText(getSupportActivity(), warn, Toast.LENGTH_SHORT)
							.show();
						}
					}
		});
	}

	private void showShareDataDialog(ConnectorState cs){
		ShareDataDialogBuilder.createDialog(getSupportActivity(), 
				cs,
				timetable, this)
		.show();
	}

//	public static final int DOWNLOAD_TIMETABLE_MAX_FREE = 3;
//	public static final int DOWNLOAD_TIMETABLE_MAX_FULL = 6;
	public void onDownloadTimetableFinished(String key, Timetable downloadedTable){
		if(downloadedTable == null){
			Map<String, String> info = new HashMap<String, String>();
			info.put(FlurryConstants.DOWNLOAD_INFO_RESULT_KEY, 
					FlurryConstants.DOWNLOAD_RESULT_FAILED);
			FlurryAgent.logEvent(FlurryConstants.DOWNLOAD_ACTION, info);
			return;
		}else{			
			String succeedDownloading = key + " : " + res.getString(
					R.string.fragment_timetable_share_download_succeed);
			Toast.makeText(getSupportActivity(), succeedDownloading, Toast.LENGTH_LONG).show();
		}
	
		if(downloadedTable.getThemeType() == ThemeType.Photo){
			//if photo mode...
			byte[] bitArr = downloadedTable.getBitmapByByteArray();
			Bitmap bit = SerializeBitmapUtils.byteArrayToBitmap(bitArr);
			downloadedTable.setBitmapByByteArray(null);
			if(bit != null){			
				YTBitmapLoader.saveTimetableBackgroundBitmap(getSupportActivity(),
						bit, 
						downloadedTable.getId());
			}
//			downloadedTable.setBmpForShare(null);
		}

		final TimetableActivity ta = (TimetableActivity) this.getSupportActivity();
		ta.addPageAt(TimetableActivity.TIMETABLE_PAGE_OFFSET, downloadedTable, false);
		ta.getViewPager().post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ta.getViewPager().setCurrentItem(TimetableActivity.TIMETABLE_PAGE_OFFSET, 
						false);
			}
		});
		
		Map<String, String> info = new HashMap<String, String>();
		info.put(FlurryConstants.DOWNLOAD_INFO_RESULT_KEY,
				FlurryConstants.DOWNLOAD_RESULT_SUCCEED);
		FlurryAgent.logEvent(FlurryConstants.DOWNLOAD_ACTION, info);
	}

	public void onUploadDataFinished(boolean isSucceed){
		if(isSucceed){
			
			Map<String, String> info = new HashMap<String, String>();
			info.put(FlurryConstants.UPLOAD_INFO_RESULT_KEY,
					FlurryConstants.UPLOAD_RESULT_SUCCEED);
			FlurryAgent.logEvent(FlurryConstants.UPLOAD_ACTION, info);
			
		}else{
			Map<String, String> info = new HashMap<String, String>();
			info.put(FlurryConstants.UPLOAD_INFO_RESULT_KEY, 
					FlurryConstants.UPLOAD_RESULT_FAILED);
			FlurryAgent.logEvent(FlurryConstants.UPLOAD_ACTION, info);
		}
	}

	private ProgressDialog pd;
	public void showProgressDialog(String title, String message,
			DialogInterface.OnCancelListener onCancelListener){
		pd = new ProgressDialog(getSupportActivity());
		if(title != null)
			pd.setTitle(title);
		if(message != null)
			pd.setMessage(message);
		
		if(pd.isShowing() == false){
			pd.show();
		}else{
			pd.dismiss();
			pd.show();
		}

		if(onCancelListener != null){
			pd.setOnCancelListener(onCancelListener);
		}
	}
	public void dismissProgressDialog(){
		if(pd != null){
			if(pd.isShowing() == true)
				pd.dismiss();
		}
	}

	private class TitleTextViewOnClickListener implements View.OnClickListener{
		private String title="";

		private String editTextInitial="";
		public TitleTextViewOnClickListener(
				String title, 

				String editTextInitial){
			this.title = title;
			this.editTextInitial = editTextInitial;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText input = new EditText(getSupportActivity());
			input.setSelectAllOnFocus(true);
			input.setSingleLine();
			if(timetable.getTitle() != null){
				editTextInitial = timetable.getTitle();
			}
			input.setText(editTextInitial);

			// set title
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getSupportActivity());
			// set title
			if(title != null)
				alertDialogBuilder.setTitle(title);
			// set dialog message
			alertDialogBuilder
			.setCancelable(true)
			.setView(input)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					String title = input.getText().toString();
					timetable.setTitle(title);
					//					titleText.setText(title);

//					TimetableDataManager.writeDatasToExternalStorage();
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			}); 
			// create alert dialog
			final AlertDialog dialog = alertDialogBuilder.create();
			//에딧텍스트가 포커스를 받으면 키보드를 보여준다.
			input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					}
				}
			});
			dialog.show();

		}

	}

	boolean lessonViewAdded = false;
	private void prepareLessonViewDrag(){
		lessonViewAdded = true;
		mSlop = ViewConfiguration.get(getSupportActivity()).getScaledTouchSlop();

		wm = (WindowManager)getSupportActivity().getSystemService(Context.WINDOW_SERVICE);

		lessonViewDragImageParams = new WindowManager.LayoutParams();
		//Gravity.top 설정이 있어 줘야 현재 액티비티의 맨 위를 기준(상태 표시줄 위, 즉 화면의 진짜배기 맨 바깥부분. FLAG_LAYOUT_IN_SCREEN으로 인해. )으로 x,y좌표가 저장된다.
		lessonViewDragImageParams.gravity = Gravity.TOP | Gravity.LEFT;
		lessonViewDragImageParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		lessonViewDragImageParams.x = 0;
		lessonViewDragImageParams.y = 0;
		lessonViewDragImageParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		lessonViewDragImageParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		lessonViewDragImageParams.format = PixelFormat.TRANSLUCENT;
		lessonViewDragImageParams.alpha = 0.4f;

		lessonViewDragImage = new ImageView(getSupportActivity());
		lessonViewDragImage.setVisibility(View.GONE);

		lessonViewDropMarkerParams = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT
				);
		//Gravity.top 설정이 있어 줘야 현재 액티비티의 맨 위를 기준(상태 표시줄 위, 즉 화면의 진짜배기 맨 바깥부분. FLAG_LAYOUT_IN_SCREEN으로 인해. )으로 x,y좌표가 저장된다.
		lessonViewDropMarkerParams.gravity = Gravity.TOP | Gravity.LEFT;
		lessonViewDropMarkerParams.leftMargin = 0;
		lessonViewDropMarkerParams.topMargin = 0;
		lessonViewDropMarkerParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		lessonViewDropMarkerParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		//lessonViewDropMarkerParams. = PixelFormat.TRANSLUCENT;
		//lessonViewDropMarkerParams.set = 0.4f;

		lessonViewDropMarker = new ImageView(getSupportActivity());
		lessonViewDropMarker.setVisibility(View.GONE);
		//	lessonViewDropMarker.setAlpha(0.4f);
		lessonViewDropMarker.setLayoutParams(lessonViewDropMarkerParams);

//		wm.removeView(lessonViewDragImage);
		wm.addView(lessonViewDragImage, lessonViewDragImageParams);
		//wm.addView(lessonViewDropMarker, lessonViewDropMarkerParams);
		gridOverlapLayout.addView(lessonViewDropMarker);

	}

	private View itemForDrag;
	private Lesson longClickedLesson;
	public boolean isDraggingLessonMode = false;
	View.OnLongClickListener lessonViewOnLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			MyLog.d("TouchSystemCheck", "touched : " + "lessonViewOnLongClickListener");
			// TODO Auto-generated method stub
			//Toast.makeText(getSupportActivity(), "수업뷰 롱클릭!", Toast.LENGTH_LONG).show();
			if(isEditLessonLengthMode == true){
				return false;
			}
			dismissOverlapMarker(true);

			Animation lessonViewAnimation = 
					AnimationUtils.loadAnimation(getSupportActivity(), R.anim.lesson_longclicked);
			//			lessonViewAnimation.setAnimationListener(new Animation.AnimationListener() {				
			//				public void onAnimationStart(Animation animation) {}
			//				public void onAnimationRepeat(Animation animation) {}
			//				public void onAnimationEnd(Animation animation) {
			//					v.setAlpha(0.5f);
			//				}
			//			});
			v.startAnimation(lessonViewAnimation);
			//int top = v.getTop();

			itemForDrag = v;
			longClickedLesson = (Lesson) v.getTag();
			isDraggingLessonMode = true;
			tableScroll.requestDisallowInterceptTouchEvent(true);

			makeImageForDrag();
			vibratePhone(getSupportActivity(), LONGCLICK_VIBRATE_TIME);

			TimetableActivity ta = (TimetableActivity) getSupportActivity();
			ta.getViewPager().requestDisallowInterceptTouchEvent(true);

			return true;
		}
	};

	private void vibratePhone(Context ctx, int duration){
		Vibrator vi = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vi.vibrate(duration);
	}

	private void makeImageForDrag(){
		//Resources res = getResources();
		//		Timetable timetable = getTimetableDataFromManager();

		itemForDrag.buildDrawingCache();
		Bitmap capture = itemForDrag.getDrawingCache();
		lessonViewDragImage.setImageBitmap(capture);

		lessonViewDropMarkerParams.width = itemForDrag.getWidth();
		lessonViewDropMarkerParams.height = itemForDrag.getHeight();
		ytTheme.getLessonViewDropMarkerBackgroundShape().setViewTheme(
				getSupportActivity(), lessonViewDropMarker);
		//		Drawable lessonViewDropMarkerBackground = 
		//				res.getDrawable(ytTheme.getLessonViewDropMarkerBackgroundShape());
		//
		//		lessonViewDropMarkerBackground.setAlpha(ytTheme.getShapeAlpha());
		//		lessonViewDropMarker.setBackgroundDrawable(lessonViewDropMarkerBackground);
		//		GradientDrawable gd = (GradientDrawable) lessonViewDropMarker.getBackground();
		//		gd.setColor(Color.CYAN);
		//		lessonViewDropMarker.setBackgroundDrawable(gd);
	}

	private void processDragging(TimetableScrollView scr, 
			int yInScrollViewScrolled){
		Rect scrRect = new Rect();
		//scrRect.bottom은 안 보이는 길이까지(스크롤되어야할 길이들까지)포함해 총 길이를 표현한다.
		gridOverlapLayout.getLocalVisibleRect(scrRect);

		//		MyLog.d("moveScrollBar",  
		//				"yScrolled : " + yScrolled +
		//				", scrRect.bottom : " + scrRect.bottom + 
		//				", scrRect.top : " + scrRect.top + 
		//				", scr.getSCrollY : " + scr.getScrollY()
		//				);

		if( scrRect.bottom - yInScrollViewScrolled <= cellHeight ){
			moveScrollBar(scr, 1);
		}else if(yInScrollViewScrolled - scr.getScrollY() <= cellHeight){
			moveScrollBar(scr, -1);
		}
	}

	private void moveScrollBar(
			TimetableScrollView scr, int movePeriod){
		int moveHeight = (int) (movePeriod * cellHeight) / 10;

		scr.smoothScrollBy(0, moveHeight);
	}

	private float getDroppedItemStartPeriodByFloat(int y){

		int centerY = y - itemForDrag.getHeight() / 2;
		Lesson l = (Lesson) itemForDrag.getTag();

		float centerPeriod = this.getPeriodByFloatFromTouchedPosition(centerY);

		int startPeriodByInt = Math.round(centerPeriod - l.getPeriodLengthByFloat() / 2);
		int endPeriodByInt = Math.round(centerPeriod + l.getPeriodLengthByFloat() / 2);


		//float centerOfIntPeriod = (float)(startPeriodByInt + endPeriodByInt) / 2f;

		float upperOffset = Math.abs(
				( getPeriodByFloatFromTouchedPosition(y - itemForDrag.getHeight()) ) 
				- startPeriodByInt
				);
		float lowerOffset = Math.abs(
				getPeriodByFloatFromTouchedPosition(y) 
				-endPeriodByInt
				);

		float startPeriod = -10f;
		if(upperOffset <= lowerOffset){
			startPeriod = Math.round(centerPeriod - l.getPeriodLengthByFloat() / 2);			
		}else{
			startPeriod = 
					Math.round(centerPeriod + l.getPeriodLengthByFloat() / 2) 
					- l.getPeriodLengthByFloat();
		}

		MyLog.d("getDroppedItem", "startPeriodByInt : " + startPeriodByInt
				+ ", endPeriodByInt : " + endPeriodByInt
				+ ", upperOffset : " + upperOffset 
				+ ", lowerOffset : " + lowerOffset);
		MyLog.d("getDroppedItem", 
				"centerPeriod : " + centerPeriod +  
				", startPeriod : " + startPeriod);

		if(startPeriod <= 0)
			startPeriod = 0;
		float endPeriod = startPeriod + l.getPeriodLengthByFloat();
		if(endPeriod > timetable.getPeriodNum()){
			//startPeriod -= 1;
			startPeriod = timetable.getPeriodNum() - l.getPeriodLengthByFloat();
		}

		return startPeriod;
	}

	private int getTimetableBodyViewLeftMargin(int startDay){
		//Resources res = getResources();
		View lt_cell = grid.findViewWithTag(new CellTag(startDay, 1));

		float timelineOffset =  res.getDimension(R.dimen.fragment_timetable_timeline_width);
//				res.getDimension(R.dimen.fragment_timetable_body_padding);								

		int cellLeft = lt_cell.getLeft();
		int left = Math.round(timelineOffset + cellLeft);

		return left;
	}

	private int getTimetableBodyViewTopMargin(float startPeriod){
		int intStartPeriod = (int)startPeriod;
		View cell = grid.findViewWithTag(new CellTag(0, intStartPeriod));
		float topOffset = (startPeriod - (float)intStartPeriod) * rowHeight;
		int[] loc = new int[2];
		cell.getLocationOnScreen(loc);
		MyLog.d("getTimetableBodyViewTopMargin", loc[1]+"");
		return loc[1] - getScrollViewTop() + tableScroll.getScrollY() + (int)topOffset;
	}


	private int getScrollViewTop(){
		int loc[] = new int[2];
		tableScroll.getLocationOnScreen(loc);  
		return loc[1];
	}
	private int getScrollViewLeft(){
		int loc[] = new int[2];
		tableScroll.getLocationOnScreen(loc);  
		return loc[0];

	}

	@Override
	public void onSaveInstanceState (Bundle outState){
        MyLog.d("onSaveInstanceState", "my idx now : " + getMyIndexInViewPager() + ", me : " + this);
        outState.putInt("SavedTimetableIndex", getMyIndexInViewPager());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated (Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            myPageIndex = savedInstanceState.getInt("SavedTimetableIndex");
            MyLog.d("TimetableFragment", "onActivityCreated, savedInstance not null, saved timetable idx : " + myPageIndex );
        }
        int tmpMyPageIndex = this.getMyIndexInViewPager();
        if(tmpMyPageIndex != -1)
            myPageIndex = tmpMyPageIndex;
        timetable = getTimetableDataFromManager(myPageIndex);
        if(myPageIndex == -1)
            return;

        MyLog.d("TimetableFragment", "onActivityCreated" + ", index : " + myPageIndex + ", timetable : " + timetable);
        if(ytTheme.getCurrentTheme() == YTTimetableTheme.ThemeType.Photo){
			//포토모드라면 저장되어있는 사진을 로드해서 배경이미지로.
			((TimetableActivity)getSupportActivity())
			.loadBitmapFromTimetableId(timetable.getId(), backgroundView);
		}else{
			ytTheme.getRootBackground().setViewTheme(getSupportActivity(), backgroundView);
		}
	}
	@Override
	public void onResume(){
		super.onResume();
        MyLog.d("onResume", "index now : " + myPageIndex);
		if(lessonViewAdded == false && myPageIndex != -1)
			prepareLessonViewDrag();
	}

	@Override
	public void onPause(){
		super.onPause();
		MyLog.d("TimetableFragment", "onPause" + ", " + this.toString());
	}
	@Override
	public void onStop(){
		super.onStop();
		MyLog.d("TimetableFragment", "onStop" + ", " + this.toString());
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		MyLog.d("TimetableFragment", "onDestroyView" + ", " + this.toString());
	}
	@Override
	public void onDestroy(){
		recycleBitmap(backgroundView);
        if(wm != null)
		    wm.removeView(lessonViewDragImage);
		lessonViewAdded = false;
		//		YTAppWidgetProvider_2x4.onTimetableDataChanged(getSupportActivity());
		//		YTAppWidgetProvider_4x4.onTimetableDataChanged(getSupportActivity());
		MyLog.d("TimetableFragment", "onDestroy" + ", " + this.toString());
		
		super.onDestroy();
	}

	private static void recycleBitmap(ImageView iv) {
		Drawable d;
		if(iv != null){
			d = iv.getDrawable();
		}else{
			return;
		}
		if (d != null && d instanceof BitmapDrawable) {
			MyLog.d("RecycleBitmap", "Recycling!");
			Bitmap b = ((BitmapDrawable)d).getBitmap();
			b.recycle();
		} // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
		if(d != null)
			d.setCallback(null);
    }
	public void onDetatch(){
		super.onDetach();
		MyLog.d("TimetableFragment", "onDetach" + ", " + this.toString());
	}
	
	public Timetable getTimetableDataFromManager(int pageIndex){
		if(pageIndex == -1){
			Log.e("TimetableFragment", "ALERT : PAGE INEDX -1");
		}
		return TimetableDataManager.getInstance().getTimetableAtPage(pageIndex);
	}

	private int getMyIndexInViewPager(){
		TimetableActivity parent = (TimetableActivity) getSupportActivity();
		if(parent == null){
			MyLog.d("getMyIndexInViewPager", "parent null");
		}
		int indexOfFragmentPage = parent.getPagerAdapter().indexOfFragmentPage(this);
		MyLog.d("getMyIndexInViewPager", "indexOfFragmentPage : " + indexOfFragmentPage);
		return indexOfFragmentPage;
	}

	PathButton.ModeBtnClickedListener addRowOnClick = new PathButton.ModeBtnClickedListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(getSupportActivity(), "PathButton : " + v.getTag() , Toast.LENGTH_SHORT).show();
			//			Timetable currentTable = timetable;
			Timetable currentTable = timetable;
			if(currentTable.isTimetableOverflow24Hours(
					currentTable.getPeriodNum() + 1
					, currentTable.getPeriodUnit())
					){
				String warn = res.getString(R.string.fragment_timetable_warning_over24h);
				Toast.makeText(getSupportActivity(), warn, Toast.LENGTH_LONG).show();
				return;
			}
			currentTable.setPeriodNum(currentTable.getPeriodNum() + 1);

			
			//addTimeLine(timetable.getPeriodNum());
			addTimetableRow(currentTable.getPeriodNum() - 1);
			refreshTimeline();
			refreshTimecells();
			refreshTimeMarker();
			tableScroll.post(new Runnable() {
				@Override
				public void run() {
					tableScroll.fullScroll(View.FOCUS_DOWN);
				}
			});
		}
	};
	PathButton.ModeBtnClickedListener removeRowOnClick = new PathButton.ModeBtnClickedListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			MyLog.d("onClick", "minus");
			if(timetable.getPeriodNum() <= 1){
				String warn = res.getString(R.string.fragment_timetable_warning_leaveoneperiod);
				Toast.makeText(getSupportActivity(), warn, Toast.LENGTH_LONG).show();
				return;
			}
			for(int i = 0; i < timetable.getLessonList().size() ; i++){
				Lesson l = timetable.getLessonList().get(i);
				if(l.getLessonEndPeriodByFloat() > timetable.getPeriodNum() - 1){
					String warnA = res.getString(
							R.string.fragment_timetable_warning_tableLengthLowerThanLesson_A);
					String warnB = res.getString(
							R.string.fragment_timetable_warning_tableLengthLowerThanLesson_B);
					String warnC = res.getString(
							R.string.fragment_timetable_warning_tableLengthLowerThanLesson_C);
					String warnD = res.getString(
							R.string.fragment_timetable_warning_tableLengthLowerThanLesson_D);
					Toast.makeText(
							getSupportActivity(), 
							warnA + "\"" + l.getLessonName() + "\" " 
							+ warnB + warnC + (int)l.getLessonEndPeriodByFloat() 
							+ warnD,									
							Toast.LENGTH_LONG
							).show();
					return;
				}
			}
			//timeline.removeViewAt(timetable.getPeriodNum() - 1);
			timetable.setPeriodNum(timetable.getPeriodNum() - 1);
			
			dismissOverlapMarker(true);
			refreshTimeline();
			refreshTimecells();
			refreshTimeMarker();
			//grid.removeViewAt(timetable.getPeriodNum() - 1);
			MyLog.d("removeRow", "grid height : " + grid.getHeight() + ", gridOVerlap height : " + gridOverlapLayout.getHeight());

		}
	};

	PathButton.ModeBtnClickedListener clearTimetableOnClick = new PathButton.ModeBtnClickedListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MyLog.d("onClick", "clear Table");
			ClearTimetableAlertDialogBuilder.createDialog(getSupportActivity(), myPageIndex, TimetableFragment.this).show();
		}
	};

	PathButton.ModeBtnClickedListener deleteTimetableOnClick = new PathButton.ModeBtnClickedListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MyLog.d("onClick", "delete Table");
			Timetable cur = timetable;
			Timetable main = TimetableDataManager.getMainTimetable();
			if(cur == main){
				String warn = res.getString(R.string.fragment_timetable_warning_deleteMainTable);
				Toast.makeText(getSupportActivity(), warn, Toast.LENGTH_LONG).show();
				return;
			}
			//팝업창을 띄우고 타임테이블이 삭제됨을 경고한 뒤 테이블을 삭제.
			DeleteTimetableAlertDialogBuilder.createDialog(getSupportActivity(), 
					TimetableFragment.this).show();
			//			dialog.show(getFragmentManager(), "DeleteTimetable");
			//			Bundle b = new Bundle();
			//			//b.putParcelable("LessonView", v);
			//			//b.putParcelable("Lesson", lesson);
			//			dialog.setArguments(b);
			//			dialog.setTargetFragment(
			//					TimetableFragment.this, 
			//					TimetableFragment.FRAG_TIMETABLE_DIALOG_DELETE_TIMETABLE);

		}
	};

	public void clearTimetableLessons(){
		for(Lesson l : timetable.getLessonList()){
			timetable.onRemoveLesson(l);
		}
		timetable.getLessonList().clear();
		MyLog.d("clearTimetableLessons", "size : " + timetable.getLessonList().size() + ", timetable : " + timetable );
	}	

	private void createTimetableHead(){
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);

		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		//first day는 sunday로 치고 있는듯.
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		if(cal.getFirstDayOfWeek() == Calendar.SUNDAY){
            if(timetable.getStartDay() == Calendar.MONDAY){
                if(today == Calendar.SUNDAY){
                    cal.add(Calendar.WEEK_OF_YEAR, -1);
                }
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }else if(timetable.getStartDay() == Calendar.TUESDAY){
                MyLog.d("DayRow", "Timetable Start Day is Tuesday");
                cal.add(Calendar.DAY_OF_WEEK, 2);
                MyLog.d("DayRow", "Calendar day : " + cal.get(Calendar.DAY_OF_WEEK));
            }
		}else if(cal.getFirstDayOfWeek() == Calendar.MONDAY){
			if(timetable.getStartDay() == Calendar.SUNDAY){
				cal.add(Calendar.DAY_OF_WEEK, -1);
			}
		}
		if(timetable.doesTimetableIncludesGregorianDay(today) == false){
			//타임테이블에 오늘의 요일이 없다-> 다음주 시간표를 보여주자.
			//cal.add(field, value)
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}

		//2.Timetable클래스의 날짜수만큼 타이틀의 요일 표시용 LinearLayout을 더한다.
		for(int i = 0; i < timetable.getDayNum() ; i++){
			LayoutInflater li = getSupportActivity().getLayoutInflater();
			LinearLayout dayCell = (LinearLayout) li.inflate(R.layout.view_timetable_dayrow_daycell, dayRow, false);

			int gregorianDay = cal.get(Calendar.DAY_OF_WEEK);

			TextView day = (TextView) dayCell.findViewById(R.id.view_timetable_dayrow_daycell_day);
			String sDay = new String();
			sDay = Timetable.getDayStringFromGregorianCalendar(getSupportActivity(), gregorianDay); 
			day.setText(sDay);
			day.setTextColor(ytTheme.getTimetableTextColor());

			//			View dayCellDivider = dayCell.findViewById(R.id.view_timetable_dayrow_daycell_divider);

			//			dayCellDivider.setBackgroundResource(
			//					ytTheme.getDayrowDividerBackground());
			//

			TextView date = (TextView) dayCell.findViewById(R.id.view_timetable_dayrow_daycell_date);

			SimpleDateFormat sdfDate = new SimpleDateFormat("M/d");
			String sDate = sdfDate.format(cal.getTime());
			date.setText(sDate);
			date.setTextColor(ytTheme.getTimetableTextColor());

			if(cal.get(Calendar.DAY_OF_WEEK) == today){
				day.setTypeface(null, Typeface.BOLD_ITALIC);
				date.setTypeface(null, Typeface.BOLD_ITALIC);
			}

			//float[] corners;
			if(ytTheme.getDayrowWrapperBackground() == null){
				if(i == 0){
					ytTheme.getDayrowDateBakcground().setViewTheme(
							getSupportActivity(), 
							date, 
							TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
							true, false, false, false);
					//					ytTheme
					//					.getDayrowDividerBackground()
					//					.setViewTheme(getSupportActivity(), dayCellDivider);
					ytTheme.getDayrowDayBackground().setViewTheme(
							getSupportActivity(), 
							day, 
							TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, false, true);
				}else if(i == timetable.getDayNum() - 1){
					ytTheme.getDayrowDateBakcground().setViewTheme(
							getSupportActivity(), 
							date, 
							TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, true, false, false);
					//					ytTheme
					//					.getDayrowDividerBackground()
					//					.setViewTheme(getSupportActivity(), dayCellDivider);
					ytTheme.getDayrowDayBackground().setViewTheme(
							getSupportActivity(), 
							day, 
							TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, true, false);
				}else{
					ytTheme.getDayrowDateBakcground().setViewTheme(
							getSupportActivity(), 
							date, 
							TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, false, false);
					//					ytTheme
					//					.getDayrowDividerBackground()
					//					.setViewTheme(getSupportActivity(), dayCellDivider);
					ytTheme.getDayrowDayBackground().setViewTheme(
							getSupportActivity(), 
							day, 
							TimetableFragment.FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, false, false);
				}
			}else{
				//				dayCellDivider.setBackgroundColor(Color.TRANSPARENT);
				ytTheme.getDayrowWrapperBackground().setViewTheme(
						getSupportActivity(), dayRowOverlap, 
						FRAG_TIMETABLE_ROUNDRECT_RXY, 
						true, true, true, true);
			}

			dayCell.setTag(gregorianDay);

			dayRow.addView(dayCell);
			cal.add(Calendar.DAY_OF_WEEK, 1);

			//addDayRowDivider(dayCell);
		}

		//addDayRowDivider();

	}


	/*private void addDayRowDivider(){

		for(int i = 0; i < dayRow.getChildCount(); i++){
			if(dayRow.getChildAt(i) instanceof LinearLayout){
				final int index = i;
				ViewTreeObserver vto = dayRow.getViewTreeObserver();
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				    @SuppressLint("NewApi")
					@Override
				    public void onGlobalLayout() {
				    	LinearLayout dayCell = (LinearLayout) dayRow.getChildAt(index);

				    	LinearLayout divider = 
				    			new LinearLayout(TimetableFragment.this.getSupportActivity());
				    	divider.setOrientation(LinearLayout.VERTICAL);
				    	divider.setBackgroundColor(Color.RED);
				    	FrameLayout.LayoutParams dividerParams = 
				    			new FrameLayout.LayoutParams(2, FrameLayout.LayoutParams.FILL_PARENT);
				    	dividerParams.leftMargin = dayCell.getRight() - 1;
				    	dividerParams.height = dayCell.getHeight();
				    	dividerParams.width = 2;
				    	divider.setLayoutParams(dividerParams);
				    	dayRowDividerWrapper.addView(divider);
				    	MyLog.d("dayRowDivider", "Position : " + 
				    	dayCell.getRight() + ", height : " + divider.getHeight() + 
				    	"dayCell Height : " + dayCell.getHeight());


				    	ViewTreeObserver obs = dayRow.getViewTreeObserver();

				        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				            obs.removeOnGlobalLayoutListener(this);
				        } else {
				            obs.removeGlobalOnLayoutListener(this);
				        }
				    }

				});

			}
		}

	}*/


	private void createTimetableBody(){
		//MyLog.d("cellWidth", "Body, cellWidth : " + cellWidth);
		for(int i = 0 ; i < timetable.getPeriodNum() ; i++){
			//정사각형으로 만들기 위해서, addTimeLine은 viewtreeobsetver내에서.
			//addTimeLine(i);
			addTimetableRow(i);
		}
	}

	private void addTimeLine(int period){
		timeline.addView(createTimelineCell(period));
	}

    public void refreshTimetable()
    {
        dismissOverlapMarker(true);
        refreshTimeline();
        refreshTimecells();
        refreshTimeMarker();
    }

    private void refreshTimeMarker(){
		Calendar currentCal = Calendar.getInstance();

		float curTimeByPeriod = timetable.getPeriodByFloatFromTime(
				currentCal.get(Calendar.HOUR_OF_DAY), currentCal.get(Calendar.MINUTE));
		MyLog.d("markCurrentTime", "current time by period : " +
				curTimeByPeriod);

		if(curTimeByPeriod > timetable.getPeriodNum()){
			//시간표의 시간 범위를 넘어간다.
			currentTimeMarker.setVisibility(View.GONE);
			return;
		}else{
			markCurrentTime();
		}

	}
	private void refreshTimeline(){

		MyLog.d("createTimeLine", "refreshTimeLine() called");
		MyLog.d("initOnGlobalLayout", "refreshTimeLine() called");
		timeline.removeAllViews();
		for(int i = 1; i <= timetable.getPeriodNum() ; i++){
			addTimeLine(i);
		}
	}

	public void refreshTimecells(){
		grid.removeAllViews();
		for(int i = 0; i < timetable.getPeriodNum() ; i++){
			addTimetableRow(i);
		}
		grid.invalidate();

//		ytTheme.getGridBackground().setViewTheme(getSupportActivity(), grid);
		//		Drawable gridBackgroundDrawable = 
		//				BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
		//						getSupportActivity(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT,
		//						FRAG_TIMETABLE_ROUNDRECT_RXY, ytTheme.getGridBackground());
		//		gridBackgroundDrawable.setAlpha(ytTheme.getImageAlpha());
		//		grid.setBackgroundDrawable(gridBackgroundDrawable);
//		rootView.invalidate();
		//grid overlap이 grid height와 같지 않게 된다. 뭐지? 버그인가?
		//	RelativeLayout.LayoutParams gridOverlapParams = (RelativeLayout.LayoutParams) gridOverlapLayout.getLayoutParams();
	}

	public void refreshLessonViews(){
		this.clearAndAddLessonViews(View.VISIBLE);
	}

	public void removeAllLessonViews(){
		this.clearLessonViews();
	}
	/**
	 * @param period
	 * period starts from 0
	 */
	private void addTimetableRow(int period){
		//Resources res = getResources();
		int cellMargin = Math.round(res.getDimension(R.dimen.fragment_timetable_cell_margin));
		int bodyPadding = Math.round(res.getDimension(R.dimen.fragment_timetable_body_padding));
		LinearLayout row = new LinearLayout(getSupportActivity());
		LinearLayout.LayoutParams rowParams = 
				new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT, 
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		rowParams.weight = 1;
		if(period == 0){
			row.setPadding(bodyPadding, bodyPadding, bodyPadding, 0);
		}else if(period == timetable.getPeriodNum() - 1){
			row.setPadding(bodyPadding, 0, bodyPadding, bodyPadding);
		}else{
			row.setPadding(bodyPadding, 0, bodyPadding, 0);
		}
		//rowParams.height = (int) res.getDimension(R.dimen.fragment_timetable_cell_height);

		row.setLayoutParams(rowParams);
		row.setOrientation(LinearLayout.HORIZONTAL);

		
		//row.addView(timelineCell);
		//2.요일별로 타임셀을 만든다. 실질적인 수업 표시용 그리드가 만들어지는 과정.
		for(int j = 0; j < timetable.getDayNum() ; j++){

			View cell = null;
			if(timetable.getPeriodUnit() == 30
					|| timetable.getPeriodUnit() == 45){
				cell = View.inflate(this.getSupportActivity(),
						R.layout.view_timetable_timecell_half_square, null);
			}else{
				cell = View.inflate(this.getSupportActivity(),
						R.layout.view_timetable_timecell, null);
			}
		
			ytTheme.getTimecellBackgroundShape().setViewTheme(getSupportActivity(), cell);
			//			Drawable cellBackground =
			//					res.getDrawable(ytTheme.getTimecellBackgroundShape());
			//			cellBackground.setAlpha(ytTheme.getShapeAlpha());
			//			cell.setBackgroundDrawable(cellBackground);
			//			GradientDrawable gd = (GradientDrawable) cell.getBackground();
			//			gd.setColor(Color.WHITE);
			//			cell.setBackgroundDrawable(gd);

			cell.setTag(new CellTag(j, period));
			LinearLayout.LayoutParams cellParams = 
					new LinearLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.FILL_PARENT, 
							android.view.ViewGroup.LayoutParams.FILL_PARENT);
			cellParams.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
			cellParams.weight = 1;
			cell.setLayoutParams(cellParams);

			row.addView(cell);
		}

		if(ytTheme.getGridBackground() != null){
			if(period == 0){
				ytTheme.getGridBackground().setViewTheme(getSupportActivity(), 
						row, FRAG_TIMETABLE_ROUNDRECT_RXY, 
						true, true, false, false);
			}else if(period == timetable.getPeriodNum() - 1){
				ytTheme.getGridBackground().setViewTheme(getSupportActivity(), 
						row, FRAG_TIMETABLE_ROUNDRECT_RXY, 
						false, false, true, true);
			}else{
				ytTheme.getGridBackground().setViewTheme(getSupportActivity(), 
						row, FRAG_TIMETABLE_ROUNDRECT_RXY, 
						false, false, false, false);
			}
		}
		//타임라인(교시 표시용)을 추가하고
		//addTimeline(period);
		//timecell들을 추가한다.
		grid.addView(row);
	}

	/*private View.OnClickListener cellOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			MyLog.d("TouchSystemCheck", "touched : " + "cellOnClickListener");
			// TODO Auto-generated method stub
			dismissOverlapMarker();

			CellTag tag = (CellTag) v.getTag();
			int day = tag.day;
			int period = tag.period;
			markSelectedCells(day,day,period,period+1);
			addSelectPeriodModeButtons(day,day,period,period+1);
		}
	};
	 */	

	/**
	 * @param period
	 * period starts from 1
	 * @return
	 * created timeline cell
	 */
	private View createTimelineCell(int period){
		//Resources res = getResources();
		//2-2.타임라인용 셀(=교시 표시용 셀)을 만든다.
		TextView timelineCell = (TextView) 
				View.inflate(getSupportActivity(),
						R.layout.view_timetable_timeline_cell,
						null);
		LinearLayout.LayoutParams timelineCellParams = 
				new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT, 
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		//timelineCellParams.width = (int) res.getDimension(R.dimen.fragment_timetable_timeline_width);
		timelineCellParams.weight = 1;

		float cellMargin = res.getDimension(R.dimen.fragment_timetable_cell_margin);
		float bodyPadding = res.getDimension(R.dimen.fragment_timetable_body_padding);
		//		MyLog.d("createTimeLine", "cell margin : " + cellMargin +", bodyPadding : " + bodyPadding + ", rowHeight : " + rowHeight + ", cellHeight : " + cellHeight);

		//timelineCellParams.height = (int) res.getDimension(R.dimen.fragment_timetable_timeline_cell_height);
		if(period == 1 || period == timetable.getPeriodNum()){
			//첫번째 타임라인셀이나 마지막 타임라인셀은 바디 패딩값을 더해줘야함.
			int height = (int) rowHeight;
			timelineCellParams.height = height;
			//			MyLog.d("createTimeLine", "height first&last : " + height);
		}else{
			int height = (int) rowHeight;
			timelineCellParams.height = height;
			//			MyLog.d("createTimeLine", "height : " + height);
		}

		timelineCell.setLayoutParams(timelineCellParams);
		timelineCell.setGravity(Gravity.RIGHT | Gravity.TOP);
		timelineCell.setTextColor(ytTheme.getTimetableTextColor());
		//패딩 관련해서 조심해야한다. 타임테이블 셀은 위아래로 마진이 주어져있고, 
		//타임라인셀은 그 마진만큼 높이가 살짝 높아져야한다.
		timelineCell.setPadding(0, 0, 5, 0);
		float textSize = res.getDimension(R.dimen.fragment_timetable_timeline_textsize);
		timelineCell.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		//		int resId = 0;
		if(ytTheme.getTimelineWrapperBackground() == null){
			if(period % 2 == 1){
				if(period == 1){
					if(period == timetable.getPeriodNum()){
						//period only exists one
						ytTheme.getTimelineBackground_1().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								true, true, true, true);
					}else{
						//first period.
						ytTheme.getTimelineBackground_1().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								true, true, false, false);
					}
				}else{
					//not first period
					if(period == timetable.getPeriodNum()){
						//final period - rb lb roundrect
						ytTheme.getTimelineBackground_1().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								false, false, true, true);
					}else{
						ytTheme.getTimelineBackground_1().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								false, false, false, false);
					}
				}
			}else{
				if(period == timetable.getPeriodNum()){
					//final period - rb lb roundrect
					ytTheme.getTimelineBackground_2().setViewTheme(
							getSupportActivity(), timelineCell, 
							FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, true, true);
				}else{
					ytTheme.getTimelineBackground_2().setViewTheme(
							getSupportActivity(), timelineCell, 
							FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, false, false);
				}
			}
		}else{
//			ytTheme.getTimelineWrapperBackground().setViewTheme(getSupportActivity(), 
//					timelineWrapper, FRAG_TIMETABLE_ROUNDRECT_RXY, 
//					false, false, false, false,
//					true, false, true, false);
//			ytTheme.getTimelineWrapperBackground().setViewTheme(getSupportActivity(), 
//					timelineUpperRound, FRAG_TIMETABLE_ROUNDRECT_RXY, 
//					true, true, false, false,
//					true, true, true, false);
//			ytTheme.getTimelineWrapperBackground().setViewTheme(getSupportActivity(), 
//					timelineBottomRound, FRAG_TIMETABLE_ROUNDRECT_RXY, 
//					false, false, true, true,
//					true, false, true, true);
			//temporary code
			if(period % 2 == 1){
				if(period == 1){
					if(period == timetable.getPeriodNum()){
						//period only exists one
						ytTheme.getTimelineWrapperBackground().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								true, true, true, true);
					}else{
						//first period.
						ytTheme.getTimelineWrapperBackground().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								true, true, false, false,
								true, true, true, false);
					}
				}else{
					//not first period
					if(period == timetable.getPeriodNum()){
						//final period - rb lb roundrect
						ytTheme.getTimelineWrapperBackground().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								false, false, true, true,
								true, false, true, true);
					}else{
						ytTheme.getTimelineWrapperBackground().setViewTheme(
								getSupportActivity(), timelineCell, 
								FRAG_TIMETABLE_ROUNDRECT_RXY, 
								false, false, false, false,
								true, false, true, false);
					}
				}
			}else{
				if(period == timetable.getPeriodNum()){
					//final period - rb lb roundrect
					ytTheme.getTimelineWrapperBackground().setViewTheme(
							getSupportActivity(), timelineCell, 
							FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, true, true,
							true, false, true, true);
				}else{
					ytTheme.getTimelineWrapperBackground().setViewTheme(
							getSupportActivity(), timelineCell, 
							FRAG_TIMETABLE_ROUNDRECT_RXY, 
							false, false, false, false,
							true, false, true, false);
				}
			}
		}
		//		float[] corners = new float[]{0,0,0,0,0,0,0,0};
		//		if(period == 1){
		//			corners = new float[]{12f,12f,12f,12f,0,0,0,0};
		//			//교시가 하나밖에 없을때
		//			if(period == timetable.getPeriodNum()){
		//				corners = new float[]{12f,12f,12f,12f,12f,12f,12f,12f};
		//			}
		//			//			timelineCell.setPadding(0, top, 5, 0);
		//		}else if(period == timetable.getPeriodNum()){
		//			corners = new float[]{0,0,0,0,12f,12f,12f,12f};
		//		}
		//
		//		Drawable timelineCellBackgroundDrawable = 
		//				BackgroundDrawableCreator.getTiledRoundedRectBitmapDrawable(
		//						getSupportActivity(),
		//						Shader.TileMode.REPEAT, Shader.TileMode.REPEAT, 
		//						corners, resId);
		//		timelineCellBackgroundDrawable.setAlpha(ytTheme.getImageAlpha());
		//		timelineCell.setBackgroundDrawable(timelineCellBackgroundDrawable);

		if(timetable.getColumnType() == Timetable.ColumnTypes.BY_PERIOD){
			//String s_period = res.getString(R.string.fragment_timetable_timeline_period);
			timelineCell.setText((period) + "");
			timelineCell.setGravity(Gravity.CENTER);
		}
		else if(timetable.getColumnType() == Timetable.ColumnTypes.BY_ALPHABET){
			char ch = (char) (65 + ( period - 1 ) );
			timelineCell.setText(ch+"");
			timelineCell.setGravity(Gravity.CENTER);
		}else if(timetable.getColumnType() == Timetable.ColumnTypes.BY_TIME){
			int timeInMinute = timetable.getStartTimeByMin() + ( period - 1 ) * timetable.getPeriodUnit();
			//int timeInMinute_end = timeInMinute + timetable.getPeriodUnit();
			int startHour = ( timeInMinute / 60 ) % 24 ;
			int startMinute = timeInMinute % 60;
			//int endHour = ( timeInMinute_end / 60 ) % 24;
			//int endMinute = timeInMinute_end % 60;
			String ampm = ((startHour >= 12) && (startHour <= 24)) ? 
					"pm" : "am";
			startHour = startHour > 12 ?
					startHour - 12 : startHour;

			//			String sH = startHour < 10 ? "0"+startHour : Integer.toString(startHour);
			String sH = Integer.toString(startHour);

			//String eH = endHour < 10 ? "0" + endHour : Integer.toString(endHour);
			String sM = startMinute < 10 ? "0" + startMinute : Integer.toString(startMinute);
			//String eM = endMinute < 10 ? "0" + endMinute : Integer.toString(endMinute);

			//String s = sH + " : " + sM + " ~ " + eH + " : " + eM;
			String s = sH + " : " + sM + "\n" + ampm;

			//			if(startHour < 12){
			//				s += "am";
			//			}else{
			//				s += "pm";
			//			}

			timelineCell.setText(s);
		}
		//timeline.addView(timelineCell);
		return timelineCell;
	}

	private void createTimetableTail(){}

	private void clearAndAddLessonViews(int lessonViewVisibility){
		MyLog.d("clearTimetableLessons", "timetable size : " + timetable.getLessonList().size() + ", timetable : " + timetable);
		//1.현재 화면에 나온 레슨뷰를 모두 삭제후
		for(int i = 0 ; i < gridOverlapLayout.getChildCount() ; i++){
			for(int j = 0; j < timetable.getLessonList().size() ; j++){
				Lesson tag = timetable.getLessonList().get(j);

				View lessonView = gridOverlapLayout.findViewWithTag(tag);
				gridOverlapLayout.removeView(lessonView);
			}
		}
		//다시 갱신한 데이터 바탕으로 레슨뷰 추가.
		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			View lessonView = createLessonViewFromLesson(timetable.getLessonList().get(i));
			if(lessonView != null){
				gridOverlapLayout.addView(lessonView);
				lessonView.setVisibility(lessonViewVisibility);
			}
		}
	}

	private void clearLessonViews(){
		MyLog.d("clearTimetableLessons", "timetable size : " + timetable.getLessonList().size() + ", timetable : " + timetable);
		//1.현재 화면에 나온 레슨뷰를 모두 삭제후
		for(int i = 0 ; i < gridOverlapLayout.getChildCount() ; i++){
			for(int j = 0; j < timetable.getLessonList().size() ; j++){
				Lesson tag = timetable.getLessonList().get(j);

				View lessonView = gridOverlapLayout.findViewWithTag(tag);
				gridOverlapLayout.removeView(lessonView);
			}
		}
	}
	public void createTimetable(){
		MyLog.d("TimetableFragment", "CreateTimetable called!");
		dayRow.removeAllViews();
		grid.removeAllViews();

		createTimetableHead();
		createTimetableBody();
		createTimetableTail();
	}

	View.OnLongClickListener overlapOnLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			MyLog.d("TouchSystemCheck", "touched : " + "overlapOnLongClickListener");
			if(isEditLessonLengthMode == true){
				//만약 edit lesson length버튼을 터치해 수업뷰 길이 조정하는 상태면 롱클릭을 받지 않는다.
				return false;
			}
			MyLog.d("overlap", "long clicked!");
			//0.롱클릭 발생한 부분이 TimeLine부분일 가능성이 있으므로 예외처리.
			if( getDayIndexFromTouchedPosition(touchedDownPositionX) == -1){

				return false;
			}

			//1.롱클릭 발생하면 selectPeriodMode가 false(즉, 첫번째 발생한 Long Click인지 여부를 확인한 뒤)를 true로 설정후 
			//timetable객체의 Lesson 리스트에 새로운 수업을 추가하기 전의 임시 lessonToAdd객체를 만든다.
			//또한 이전의 Long Click때 발생한 뷰들은 초기화.
			dismissOverlapMarker(true);

			if(isSelectPeriodMode == false){
				isSelectPeriodMode = true;
				int longClickedDay = getDayIndexFromTouchedPosition(touchedDownPositionX);
				int longClickedPeriod = (int)getPeriodFromTouchedPosition(touchedDownPositionY);
				MyLog.d("TouchSystemCheck", "selectPeriodMode set to true" +
						", long clicked day : " + longClickedDay + 
						", longClickedPeriod : " + longClickedPeriod);
				markSelectedCells(longClickedDay, longClickedDay, 
						longClickedPeriod, longClickedPeriod + 1);

				//
				//				MyLog.d("longClick", "day : " + longClickedDay + ", period : " + longClickedPeriod);
				//
				//				markSelectedCells(longClickedDay, longClickedPeriod, longClickedPeriod);
			}
			//스크롤뷰의 interceptTouchEvent를 블로킹해두지 않으면 selectPeriodMode가 활성화된 와중에 드래그를 스크롤뷰가 빼앗아가버려 event가 Cancel된다.
			tableScroll.requestDisallowInterceptTouchEvent(true);
			vibratePhone(getSupportActivity(), LONGCLICK_VIBRATE_TIME);

			MyLog.d("longClick", "lessonList Size : " + timetable.getLessonList().size());
			return false;
		}
	};

	float touchedDownPositionX = -1;
	float touchedDownPositionY = -1;
	final static float DRAGSCROLL_OFFSETY = 50;		//교시선택모드일때 스크롤뷰의 스크롤을 블로킹해놓았으므로 수동으로 드래그를 허락해야한다.
	//boolean isCurrentMarkersVisible = false;
	//boolean isClicked = false;
	View.OnTouchListener overlapOnTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//1.onLongClickListener에서 터치된 포지션을 받아오는건 불가능하므로 ACTION_DOWN상태에서 우선 터지되었던 포지션을 저장해둔다.
			MyLog.d("TouchSystemCheck", "touched : " + "overlapOnTouchListener" + ", event : " + event.getAction());
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				MyLog.d("LessonEditLength", "overlap touched down!");
				touchedDownPositionX = event.getX();
				touchedDownPositionY = event.getY();

				//edit lesson length버튼이 터치되었을때는 버튼들을 없애버리면 안된다.
				if(isEditLessonLengthMode == false){
					//dismissOverlapMarker();
				}
				if(isSelectPeriodMode == true){
					//만약 터치다운인데 select period mode가 true라면?
					//init!
					isSelectPeriodMode = false;
					isMarkButtonEditLengthTouched = false;
				}
				//dismissLessonViewAndDatas();
				Log.e("grid touched down", "!!");
			}

			//2.period select모드가 아니라면 아무것도 하지 않는다. 여기서 touchedDownPosition을 초기화하면 안됨.
			//롱클릭 발생-ACTION_DOWN은 호출 안되고 곧바로 ACTION_MOVE 호출, 이 순서를 타게 되면 망함
			//			if(selectPeriodMode == false){
			//				if(event.getAction() == MotionEvent.ACTION_CANCEL ||
			//						event.getAction() == MotionEvent.ACTION_UP){
			//					touchedDownPositionX = -1;
			//					touchedDownPositionY = -1;
			//				}
			//				return false;
			//			}

			if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(isSelectPeriodMode == true){
					//					if(touchedDownPositionX == -1 || touchedDownPositionY == -1){
					//						//이 경우 select period mode가 발동하고 사용자가 up을 한 뒤,
					//						//markButtonEditLength를 터치해서 움직이는 경우라고 판단한다.
					//						touchedDownPositionX = event.getX();
					//						touchedDownPositionY = event.getY();
					//					}
					boolean result = handleSelectPeriodModeOnTouchMove(event);

					//새로 add된 레슨뷰들에 가려져버린 currentTimeMarker가 제일 위에 올라와야함.
					currentTimeMarker.bringToFront();

					return result; 

				}else if(isEditLessonLengthMode == true){
					boolean result = handleEditLessonLengthModeOnTouchMove(event);
					//새로 add된 레슨뷰들에 가려져버린 currentTimeMarker가 제일 위에 올라와야함.
					currentTimeMarker.bringToFront();
					return result;
				}else if(isDraggingLessonMode == true){
					boolean result = handleDragLessonModeOnTouchMove(event);
					return result;
				}

			}else if(event.getAction() == MotionEvent.ACTION_CANCEL ||
					event.getAction() == MotionEvent.ACTION_UP){

				if(isSelectPeriodMode == true){

					boolean result = handleSelectPeriodModeOnTouchUp(event);
					MyLog.d("TouchSystemCheck", "selectPeriodmode false");
					//새로 add된 레슨뷰들에 가려져버린 currentTimeMarker가 제일 위에 올라와야함.
					currentTimeMarker.bringToFront();
					return result;

				}else if(isEditLessonLengthMode == true){
					//손가락을 움직여 edit lesson length버튼 바깥으로 터치좌표가 나가면
					//editlessonlengthontouch리스너가 콜링되지 않는다. 
					//따라서 up 관련 처리는 여기서 모두 해준다.
					MyLog.d("TouchSystemCheck", "isLessonEditLengthMode true, now to false");
					tableScroll.requestDisallowInterceptTouchEvent(false);
					isEditLessonLengthMode = false;
					//touch up하는게 절묘한 타이밍동안 누르고 있었다면 long click이 up직후 호출, 
					//selectPeriodMode는 활성화되는데 touch down은 활성화가 안 되어 이상해진다.
					//그래서 롱클릭리스너를 잠시 빼버린 후 0.05s의 딜레이를 주어 long click호출이 꼬이지 않도록
					//처리해준다.
					//					gridOverlapLayout.setOnLongClickListener(null);
					//					MyLog.d("TouchSystemCheck", 
					//							"handler requested gridoverlaplayout not to long click for 0.05s");
					//					new Handler().postDelayed(new Runnable(){
					//						@Override
					//						public void run() {
					//							MyLog.d("TouchSystemCheck", "longclick block ended!");
					//							gridOverlapLayout.setOnLongClickListener(overlapOnLongClickListener);
					//						}		
					//					}, 500);
					blockGridOverlapLongClick();
					MyLog.d("LessonEditLength", "isLessonEditLengthMode = false");	
					boolean result = handleEditLessonLengthModeOnTouchUp(event);
					//새로 add된 레슨뷰들에 가려져버린 currentTimeMarker가 제일 위에 올라와야함.
					currentTimeMarker.bringToFront();

					return result;
				}else if(isDraggingLessonMode == true){
					boolean result = handleDragLessonModeOnTouchUp(event);
					tableScroll.requestDisallowInterceptTouchEvent(false);
					return result;
				}else{
					//롱클릭이 되어 selectPeriodMode가 호출된것도 아니고, 레슨뷰 클릭후 lesson edit length mode로 들어간것도 아니고, 
					//레슨뷰를 클릭한것도 아니고(레슨뷰 클릭시 overlapOnTouchListener호출되지 않음.)
					//마지막 경우로 비어있는 셀을 선택한 경우가 있다.
					//overlap을 long click하면 select period mode가 활성화될테니 이게 불려질 일은 없을테고,
					//tableScroll의 intercept가 좀 걱정되는데, 체크해보자 - isLessonDraggingMode관련 사항들 말하는거.
					if(event.getAction() == MotionEvent.ACTION_UP){
						MyLog.d("TouchSystemCheck", 
								"touched : overlapOnTouch, ACTION_UP, onClick?");
						//handleEmptyCellOnClick(event);
						//0.만약 markSelectedRangeLayout이 보이는 도중이거나, 
						//수업뷰를 클릭해서 버튼이 띄워져있는 도중이라면
						//버튼 or View를 안 보이도록 해 준다.
						MyLog.d("TouchSystemCheck", "markSelectedRangeLayout visibility : " + markSelectedRangeLayout.getVisibility()
								+ "isLessonEditbuttonShowing : " + isLessonEditButtonsShowing);
						//if you don't call this, double tap or fast tapping calls longclick.
						gridOverlapLayout.cancelLongPress();

						if(markSelectedRangeLayout.getVisibility() == View.VISIBLE ||
								isLessonEditButtonsShowing == true){
							MyLog.d("dismissOverlapMarkers", "isSelectPeriodMode : " + isSelectPeriodMode);
							//							blockGridOverlapLongClick();
							dismissOverlapMarker(true);
							return true;
						}



						//						//****아주아주 짧게 buttonEditLength를 클릭하고 떼버리면, 
						//						//selectPeriodMode는 true, touchedDownPositionX,Y가 설정된채로
						//						//markEditLength의 터치리스너의 ACTION_CANCELED는 호출되면서
						//						//overlapOnTouchListener가 호출되지 않는 문제가 있다.
						//						//그래서 짧은 터치가 호출되었다면 초기화.
						//						isSelectPeriodMode = false;
						//						isMarkButtonEditLengthTouched = false;
						//						touchedDownPositionX = -1;
						//						touchedDownPositionY = -1;

						//2.이후 제대로 다시 처리.
						boolean result = handleSelectPeriodModeOnTouchUp(event);
						MyLog.d("TouchSystemCheck", "selectPeriodmode false" + ", result : "+ result);
						//새로 add된 레슨뷰들에 가려져버린 currentTimeMarker가 제일 위에 올라와야함.
						currentTimeMarker.bringToFront();
						return result;
					}

				}
			}
			return false;
		}
	};

	private void blockGridOverlapLongClick(){
		gridOverlapLayout.setOnLongClickListener(null);
		MyLog.d("TouchSystemCheck", 
				"handler requested gridoverlaplayout not to long click for 0.05s");
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				MyLog.d("TouchSystemCheck", "longclick block ended!");
				gridOverlapLayout.setOnLongClickListener(overlapOnLongClickListener);
			}						
		}, 500);
	}
	private boolean handleSelectPeriodModeOnTouchMove(MotionEvent event){
		dismissOverlapMarker(false);

		//2-1. 오버랩뷰 롱클릭이 발생하였다. 터치다운된 포지션 기준으로 롱클릭된 날과 교시를 가져온다.
		int startDay = getDayIndexFromTouchedPosition(touchedDownPositionX);
		float startPeriod = (float)((int)getPeriodFromTouchedPosition(touchedDownPositionY));


		int btnWidth = (int)cellWidth / 3 * 2;
		int btnHeight = btnWidth;
		float x = event.getX();
		float y = event.getY();

		//롱클릭으로 드래깅중에 뗀 게 아니라, editlength버튼을 통해 드래깅중이라면...
		if(isMarkButtonEditLengthTouched == true){
			if(x >= btnWidth){
				x -= btnWidth;
			}

			if(y >= btnHeight){
				y -= btnHeight;
			}
		}
        int endDay = getDayIndexFromTouchedPosition(x);
        float endPeriod;
        if(isMarkButtonEditLengthTouched) {
            endPeriod = getPeriodByFloatFromTouchedPosition(y);
            MyLog.d("TouchedDownPos > y", "startP : " + startPeriod + ", endP : " + endPeriod + ", getPeriodByFloatFromTouchedPosition(y) : " + getPeriodByFloatFromTouchedPosition(y));
            if (startPeriod >= endPeriod) {
                endPeriod = startPeriod + .5f;
                startPeriod = Math.round(getPeriodByFloatFromTouchedPosition(y) * 2f) / 2f;
                MyLog.d("TouchedDownPos > y", "AFTER / startP : " + startPeriod + ", endP : " + endPeriod);
            } else {
                endPeriod = Math.round(endPeriod * 2f) / 2f + 1f;
            }
        }
        else{
            endPeriod = getPeriodFromTouchedPosition(y) + 1f;
            if(touchedDownPositionY >= y){
                startPeriod = getPeriodFromTouchedPosition(y);
                endPeriod = (float)getPeriodFromTouchedPosition(touchedDownPositionY) + 1f;
            }
        }

		if(startDay < 0){
			MyLog.d("overlapOnTouch", "startDay < 0");
			isSelectPeriodMode = false;
			touchedDownPositionX = -1;
			touchedDownPositionY = -1;

			tableScroll.requestDisallowInterceptTouchEvent(false);
			return false;
		}

		//startDay와 endDay의 정렬!
		if(startPeriod - endPeriod >= 0){
			float tmp = startPeriod;
			startPeriod = endPeriod;
			endPeriod = tmp;
		}
		if(startDay - endDay >= 0){
			int tmp = startDay;
			startDay = endDay;
			endDay = tmp;
		}

		if(startDay < 0){
			startDay = 0;
		}
		if(endDay >= timetable.getDayNum()){
			endDay = timetable.getDayNum() - 1;
		}
        if(endPeriod >= timetable.getPeriodNum())
        {
            endPeriod = timetable.getPeriodNum();
        }

		MyLog.d("overlapOnTouch", 
				"startDay : " + startDay + ", endDay : " + endDay + 
				", startPeriod : " + startPeriod + ", endPeriod : " + endPeriod
				);

		//2-2.또한 selectPeriodMode가 true(즉 Long Click이 발생한 경우)라도 해당 셀에 이미 수업이 존재하면 
		//터치 이벤트를 처리하지 않는다.
		//****이 경우 보통 lesson View가 롱클릭을 가져가므로 굳이 다시 검사할필요 없겠지만...혹시몰라서
		//다시 한번 체크해본다.
		//			if(checkSelectedRangeAvaliable(startDay, endDay, startPeriod, startPeriod) 
		//					== false){
		//				//이미 수업이 존재하면 겹치고 있는 수업 뷰에게 터치이벤트를 넘겨줘야하므로 return false;
		//				selectPeriodMode = false;
		//				dismissOverlapMarker();
		//				return true;
		//			}
		//****
		//4.교시선택모드일때 액션 처리.
		markSelectedCells(startDay, endDay, startPeriod, endPeriod);
		processDragging(tableScroll, (int)y);
		return true;
	}

	private boolean handleSelectPeriodModeOnTouchUp(MotionEvent event){
		MyLog.d("LessonEditLength", "overlap touched up!");
		//2-1. 오버랩뷰 롱클릭이 발생하였다. 터치다운된 포지션 기준으로 롱클릭된 날과 교시를 가져온다.
		int startDayIndex = getDayIndexFromTouchedPosition(touchedDownPositionX);
		float startPeriod = getPeriodFromTouchedPosition(touchedDownPositionY);

		int btnWidth = (int)cellWidth / 3 * 2;
		int btnHeight = btnWidth;
		float x = event.getX();
		float y = event.getY();

		//롱클릭으로 드래깅중에 뗀 게 아니라, editlength버튼을 통해 드래깅중이라면...
		if(isMarkButtonEditLengthTouched == true){
			if(x >= btnWidth){
				x -= btnWidth;
			}

			if(y >= btnHeight){
				y -= btnHeight;
			}

		}
        int endDayIndex = getDayIndexFromTouchedPosition(x);
        float endPeriod;
        if(isMarkButtonEditLengthTouched) {
            endPeriod = getPeriodByFloatFromTouchedPosition(y);
            MyLog.d("TouchedDownPos > y", "startP : " + startPeriod + ", endP : " + endPeriod + ", getPeriodByFloatFromTouchedPosition(y) : " + getPeriodByFloatFromTouchedPosition(y));
            if (startPeriod >= endPeriod) {
                endPeriod = startPeriod + .5f;
                startPeriod = Math.round(getPeriodByFloatFromTouchedPosition(y) * 2f) / 2f;
                MyLog.d("TouchedDownPos > y", "AFTER / startP : " + startPeriod + ", endP : " + endPeriod);
            } else {
                endPeriod = Math.round(endPeriod * 2f) / 2f + 1f;
            }
        }
        else{
            endPeriod = getPeriodFromTouchedPosition(y) + 1f;
            if(touchedDownPositionY >= y){
                startPeriod = getPeriodFromTouchedPosition(y);
                endPeriod = (float)getPeriodFromTouchedPosition(touchedDownPositionY) + 1f;
            }
        }

		if(startDayIndex < 0){
			isSelectPeriodMode = false;
			touchedDownPositionX = -1;
			touchedDownPositionY = -1;

			tableScroll.requestDisallowInterceptTouchEvent(false);
			//Timeline부분을 누르면 세팅 액티비티가 실행되어야 하는데 gridOverlapLayout이 죄다 가져가버려서
			//dayIndex가 -1이면 timeline 클릭된걸로 알고 세팅 액티비티를 실행하도록 한다.
			startSettingActivity(true, false);
			return false;
		}

		//startDay와 endDay의 정렬!
		if(startPeriod - endPeriod >= 0){
			float tmp = startPeriod;
			startPeriod = endPeriod;
			endPeriod = tmp;
		}
		if(startDayIndex - endDayIndex >= 0){
			int tmp = startDayIndex;
			startDayIndex = endDayIndex;
			endDayIndex = tmp;
		}

		if(startDayIndex < 0){
			startDayIndex = 0;
		}
		if(endDayIndex >= timetable.getDayNum()){
			endDayIndex = timetable.getDayNum() - 1;
		}

		MyLog.d("overlapOnTouch", 
				"startDay : " + startDayIndex + ", endDay : " + endDayIndex + 
				", startPeriod : " + startPeriod + ", endPeriod : " + endPeriod
				);

		//1.롱클릭 이후 선택된 부분을 위해 이용한 변수들 초기화 및 수업시간정보(PeriodInfo)용 데이터 정리.
		isSelectPeriodMode = false;
		isMarkButtonEditLengthTouched = false;
		touchedDownPositionX = -1;
		touchedDownPositionY = -1;


		//2.현재 시간표에 저장된 수업들의 시간과 겹치는지 여부를 체크하여 겹칠경우 다이얼로그 안 띄우고 경고 토스트 띄운후 리턴.
		if(checkSelectedRangeAvaliable(startDayIndex, endDayIndex, startPeriod, endPeriod) == false){
			String warn = res.getString(R.string.fragment_timetable_warning_lessonAlreadyExists);
			isSelectPeriodMode = false;
			dismissOverlapMarker(true);
			Toast.makeText(
					getSupportActivity(), 
					warn,
					Toast.LENGTH_LONG)
					.show();
			return true;
		}

		//3. 안 겹치면 좌우측 버튼 클릭을 listening하여 OK면 세이브, X면 선택부분 ADD안하고 취소.
		//O - 3을 진행한 후 timetable에 LessonToAdd를 timetable에 ADD
		//X - 선택부분 없앤다. 전부 없었던 일로...

		//showDialog(DIALOG_SELECT_PERIOD_FINISHED);

		//3.timetable에서 lessonList의 마지막 레슨(즉, PeriodInfo리스트가 비어있는 Lesson)을 받아와 현재 입력받은 period정보를 삽입.

		//				lessonToAddAndEdit.setPeriodInfo(new PeriodInfo(
		//						longClickedDay, 
		//						timetable.getStartHourOfPeriod(start),
		//						timetable.getStartMinOfPeriod(start),
		//						timetable.getEndHourOfPeriod(end),
		//						timetable.getEndMinOfPeriod(end)
		//						)
		//						);
		markSelectedCells(startDayIndex, endDayIndex, startPeriod, endPeriod);
		addSelectPeriodModeButtons(startDayIndex, endDayIndex, startPeriod, endPeriod);
		//3.스크롤뷰 intercept 터치이벤트 다시 활성화.
		tableScroll.requestDisallowInterceptTouchEvent(false);
		return true;
	}

	private boolean handleEditLessonLengthModeOnTouchMove(MotionEvent event){
		//2-1. 오버랩뷰 롱클릭이 발생하였다. 터치다운된 포지션 기준으로 롱클릭된 날과 교시를 가져온다.
		//		int startDay = getDayIndexFromTouchedPosition(touchedDownPositionX);
		float f_startPeriod = lessonToEditLength.getLessonStartPeriodByFloat();
		//		int endDay = getDayIndexFromTouchedPosition(event.getX());
		float f_endPeriod = getPeriodByFloatFromTouchedPosition(event.getY());
		float rounded_endPeriod = Math.round(f_endPeriod * 2f) / 2f;
		//		if(startDay < 0){
		//			selectPeriodMode = false;
		//			touchedDownPositionX = -1;
		//			touchedDownPositionY = -1;
		//
		//			tableScroll.requestDisallowInterceptTouchEvent(false);
		//			return false;
		//		}
		//startDay와 endDay의 정렬!
		MyLog.d("LessonEditLength", "before/ start p : " + f_startPeriod 
				+ ", end p : " + f_endPeriod);
		if(f_startPeriod - rounded_endPeriod >= 0){
            rounded_endPeriod = (int) (f_startPeriod + 0.5);
		}
		if(rounded_endPeriod - f_startPeriod < 0.5){
            rounded_endPeriod += 0.5;
		}
		f_endPeriod = rounded_endPeriod;

		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			Lesson tmpLesson = timetable.getLessonList().get(i);
			if(tmpLesson == lessonToEditLength){
				continue;
			}
			if(Lesson.checkLessonCollideWith(
					tmpLesson.getParentTimetable(), 
					tmpLesson.getDay(), 
					tmpLesson.getLessonStartPeriodByFloat(),
					tmpLesson.getLessonEndPeriodByFloat(), 
					timetable, lessonToEditLength.getDay(), f_startPeriod, f_endPeriod) == true){
//				endPeriod = (int) tmpLesson.getLessonStartPeriodByFloat();
				MyLog.d("LessonEditLength", "Lesson Collides, start : " + 
						f_startPeriod + ", endPeriod : " + f_endPeriod);
				return true;
			}

		}


		PeriodInfo pInfo = lessonToEditLength.getLessonInfo();
		//		pInfo.setStartHour(timetable.getStartHourOfPeriod(startPeriod));
		//		pInfo.setStartMin(timetable.getStartMinOfPeriod(startPeriod));
		pInfo.setEndHour(timetable.getEndHourOfPeriod(f_endPeriod));
		pInfo.setEndMin(timetable.getEndMinOfPeriod(f_endPeriod));


		MyLog.d("LessonEditLength", "after/ start p : " + f_startPeriod + ", end p : " + f_endPeriod);

		lessonToEditLength.setPeriodInfo(pInfo);
		gridOverlapLayout.removeView(gridOverlapLayout.findViewWithTag(lessonToEditLength));
		View lessonView = createLessonViewFromLesson(lessonToEditLength);
		if(lessonView != null)
			gridOverlapLayout.addView(lessonView);

		FrameLayout.LayoutParams lelParams = (LayoutParams) lessonEditLength.getLayoutParams();
		lelParams.topMargin = 
				getLessonViewTLMarginFromLesson(lessonToEditLength)[0] +
				getLessonViewWHFromLesson(lessonToEditLength)[1];
		lessonEditLength.setLayoutParams(lelParams);
		FrameLayout.LayoutParams lcpyParams = (LayoutParams) lessonCopy.getLayoutParams();
		lcpyParams.topMargin = 
				getLessonViewTLMarginFromLesson(lessonToEditLength)[0] +
				getLessonViewWHFromLesson(lessonToEditLength)[1];
		lessonCopy.setLayoutParams(lcpyParams);
		lessonRemove.bringToFront();
		MyLog.d("LessonEditLength", "LessonView Bottom : " + lessonView.getBottom());
		return true;

	}

	private boolean handleEditLessonLengthModeOnTouchUp(MotionEvent event){
//		TimetableDataManager.writeDatasToExternalStorage();
//		YTAppWidgetProvider_2x4.onTimetableDataChanged(getSupportActivity());
//		YTAppWidgetProvider_4x4.onTimetableDataChanged(getSupportActivity());
		return true;
	}

	private boolean handleDragLessonModeOnTouchMove(MotionEvent ev){
		int xInGridOverlap = (int) ev.getX();   //스크롤뷰 내부에서 x,y좌 표. 스크롤바는 관련없고 '보이는 그대로' 값이다.
		int yInGridOverlap = (int) ev.getY();	//스크롤바 관련 없음.
		//int yInWindow = (int) yInScrollView + getScrollViewTop();
		//itemForDrag.getHeight() / 2는 현재 터치된 y포지션을 
		//int yInGridOverlapScrolled = (int) ev.getY() + tableScroll.getScrollY();

		if(isDraggingLessonMode == false){
			//드래그중이 아니므로 해줄 일이 없다.
			return true;
		}

		//mDrag로 드래그 여부를 판단해 드래그를 하는것으로 알게 되면 이미지를 띄우게 된다.
		if(Math.abs(touchedDownPositionY - ev.getY()) < mSlop && isDraggingLessonMode == false){
			return true;
		}

		//화면에 반투명하게 이미지를 띄운다.
		//y = y + getScrollViewTop() - itemForDrag.getHeight();
		//x = x - getScrollViewLeft() - itemForDrag.getWidth();
		itemForDrag.setVisibility(View.INVISIBLE);

		lessonViewDragImageParams.y = 
				yInGridOverlap + (getScrollViewTop() - tableScroll.getScrollY()) - itemForDrag.getHeight() / 2;
		lessonViewDragImageParams.x = 
				xInGridOverlap - getScrollViewLeft() - itemForDrag.getWidth() / 2;
		lessonViewDragImage.setVisibility(View.VISIBLE);
		wm.updateViewLayout(lessonViewDragImage, lessonViewDragImageParams);

		//드래그 후 드롭했을때 어디에 떨어질지 마킹해줌.
		int centerX = xInGridOverlap - itemForDrag.getWidth() / 2;
		//int centerYInScrollView = yInGridOverlapScrolled + itemForDrag.getHeight() / 2;
		int centerYInScrollView = yInGridOverlap + itemForDrag.getHeight() / 2;
		//			MyLog.d("onIntercept", "x : " + x + 
		//					", getScrollViewLeft() : " + getScrollViewLeft() + 
		//					", itemForDrag.getWidth() : " + itemForDrag.getWidth());
		int day = getDayIndexFromTouchedPosition(centerX);
		if(day <= 0){
			day = 0;
		}

		float startPeriod = 
				getDroppedItemStartPeriodByFloat(centerYInScrollView);

		/*lessonViewDropMarkerParams.x = (int) 
				(timelineWidth + getScrollViewLeft() + cellWidth * day );
		lessonViewDropMarkerParams.y = (int) (
				getScrollViewTop() + cellHeight * startPeriod - scr.getScrollY()
				);*/
		lessonViewDropMarkerParams.leftMargin = 
				getTimetableBodyViewLeftMargin(day);
		lessonViewDropMarkerParams.topMargin = 
				getTimetableBodyViewTopMargin(startPeriod);
		lessonViewDropMarker.setLayoutParams(lessonViewDropMarkerParams);
		//lessonViewDropMarker.up


		//processDragging(tableScroll, yInGridOverlapScrolled);
		processDragging(tableScroll, yInGridOverlap);
		lessonViewDropMarker.setVisibility(View.VISIBLE);
		//lessonViewDropMarker.invalidate();
		//wm.updateViewLayout(lessonViewDropMarker, lessonViewDropMarkerParams);

		return true;

	}

	private boolean handleDragLessonModeOnTouchUp(MotionEvent ev){
		int xInGridOverlap = (int) ev.getX();   //스크롤뷰 내부에서 x,y좌 표. 스크롤바는 관련없고 '보이는 그대로' 값이다.
		int yInGridOverlap = (int) ev.getY();	//스크롤바 관련 없음.
		//int yInWindow = (int) yInScrollView + getScrollViewTop();
		//itemForDrag.getHeight() / 2는 현재 터치된 y포지션을 
		//int yInGridOverlapScrolled = (int) ev.getY() + tableScroll.getScrollY();

		lessonViewDragImage.setVisibility(View.GONE);
		lessonViewDropMarker.setVisibility(View.GONE);
		wm.updateViewLayout(lessonViewDragImage, lessonViewDragImageParams);
		//wm.updateViewLayout(lessonViewDropMarker, lessonViewDropMarkerParams);
		int _centerX = xInGridOverlap - itemForDrag.getWidth() / 2;
		int _day = getDayIndexFromTouchedPosition(_centerX);
		if(_day <= 0){
			_day = 0;
		}
		//int _centerY = yInGridOverlapScrolled + itemForDrag.getHeight() / 2;
		int _centerY = yInGridOverlap + itemForDrag.getHeight() / 2;

		float _startPeriod = getDroppedItemStartPeriodByFloat(_centerY);
		float _endPeriod = _startPeriod + longClickedLesson.getPeriodLengthByFloat();

		//Lesson tmpLesson = (Lesson) itemForDrag.getTag();
		PeriodInfo tmpPInfo = longClickedLesson.getLessonInfo();

		longClickedLesson.setPeriodInfo(new PeriodInfo(
				timetable.getGregorianCalendarDayFromDayIndex(_day),
				timetable.getStartHourOfPeriod(_startPeriod),
				timetable.getStartMinOfPeriod(_startPeriod),
				timetable.getEndHourOfPeriod(_endPeriod),
				timetable.getEndMinOfPeriod(_endPeriod)
				));
		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			Lesson l = timetable.getLessonList().get(i);
			if(l == longClickedLesson)
				continue;
			if(Lesson.checkLessonCollideWith(longClickedLesson, l) == true){
				longClickedLesson.setPeriodInfo(tmpPInfo);
				String warn = res.getString(R.string.fragment_timetable_warning_lessonAlreadyExists);
				Toast.makeText(
						getSupportActivity(), 
						warn, 
						Toast.LENGTH_LONG
						).show();
				break;
			}
		}

		gridOverlapLayout.removeView(itemForDrag);
		//itemForDrag.setVisibility(View.VISIBLE);
		itemForDrag = null;

		View lv = createLessonViewFromLesson(longClickedLesson);
		if(lv != null){
			gridOverlapLayout.addView(lv);
		}

		isDraggingLessonMode = false;
		//current time marker위로 뷰를 놓으면 가려져버림. 
		currentTimeMarker.bringToFront();

		TimetableActivity ta = (TimetableActivity) getSupportActivity();
		ta.getViewPager().requestDisallowInterceptTouchEvent(false);
//		TimetableDataManager.writeDatasToExternalStorage();
		YTAlarmManager.cancelLessonAlarm(getSupportActivity(), longClickedLesson);
		if(timetable.getLessonAlarmTime() != Timetable.LESSON_ALARM_NONE){
			YTAlarmManager.cancelLessonAlarm(getSupportActivity(), longClickedLesson);
			YTAlarmManager.startLessonAlarm(
					getSupportActivity(), longClickedLesson, timetable.getLessonAlarmTime());
		}
		longClickedLesson = null;
		return true;
	}

	private void handleEmptyCellOnClick(MotionEvent event){
		int day = getDayIndexFromTouchedPosition(event.getX());
		int startPeriod = (int)getPeriodFromTouchedPosition(event.getY());
		int endPeriod = startPeriod + 1;
		//	MyLog.d("handleEmptyCellOnClick", "day : " + day);
		if(day == -1){
			return;
		}

		Lesson tmp = new Lesson(timetable);
		tmp.setPeriodInfo(new PeriodInfo(
				timetable.getGregorianCalendarDayFromDayIndex(day),
				timetable.getStartHourOfPeriod(startPeriod),
				timetable.getStartMinOfPeriod(startPeriod),
				timetable.getEndHourOfPeriod(endPeriod),
				timetable.getEndMinOfPeriod(endPeriod)
				));

		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			Lesson l = timetable.getLessonList().get(i);
			if(Lesson.checkLessonCollideWith(tmp, l) == true){
				//만약 클릭한부분 수업이 충돌하면
				return;
			}
		}

		Random rand = new Random();
		String[] themeColors = YTTimetableTheme.LESSON_COLORS_THEME_A;
		int randomColorIdx = rand.nextInt(themeColors.length);
		tmp.setColor(Color.parseColor(themeColors[randomColorIdx]));
		timetable.addLesson(tmp);
//		TimetableDataManager.writeDatasToExternalStorage();

		View lessonView = createLessonViewFromLesson(tmp);
		gridOverlapLayout.addView(lessonView);

		showLessonEditButtons(lessonView, tmp);
	}
	
	private void startSettingActivity(boolean showTimeSettingDialog, boolean showDaySettingDialog){
//		Intent intent = new Intent(getSupportActivity(), TimetableSettingFragment.class);
        Intent intent = new Intent(getSupportActivity(), TimetableSettingInfoActivity.class);
		intent.putExtra("ShowTimeSettingDialog", showTimeSettingDialog);
        intent.putExtra("ShowDaySettingDialog", showDaySettingDialog);
		intent.putExtra("TimetablePageIndex", getMyIndexInViewPager());
		getSupportActivity().startActivityForResult(intent, RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING);
	}


	private int getDayIndexFromTouchedPosition(float x){
		//만약 타임라인부분이 터치됬으면 return -1
		float timelineOffset = res.getDimension(R.dimen.fragment_timetable_timeline_width);
		if(x < timelineOffset){
			return -1;
		}
		//그외에는 제대로된 day 부분이 터치되었을 것이다. 
		int index = (int) ( ( x - timelineOffset ) / cellWidth);

		if(index >= timetable.getDayNum()){
			return timetable.getDayNum() - 1;
		}

		MyLog.d("getDayFromTouchedPosition", "day index : " + index);

		return index;		//timeline offset때문에 인덱스+1

		//		if(timetable.getStartDay() == GregorianCalendar.MONDAY){
		//			int day = (int) (x / cellWidth) - 1;
		//			MyLog.d("leftIndex", "start mon, day : " + day);
		//			return day;
		//		}
		//		else{
		//			int rawDay = (int) (x / cellWidth) - 1;
		//			MyLog.d("leftIndex", "start not mon, rawDay : " + rawDay);
		//			if(rawDay == -1){
		//
		//				return rawDay;
		//			}
		//			int day = ( ( rawDay ) + timetable.getStartDay());
		//			if(day >= 7){
		//				day -= 7;
		//			}
		//			MyLog.d("leftIndex", "start not mon, day : " + day);
		//			return day;
		//		}
	}

	private float getPeriodFromTouchedPosition(float y){
		MyLog.d("getPeriodFromTouchedPosition", "y : " + y + 
				", cellHeight : " + cellHeight + ", rowHeight : " + rowHeight);
		float res = ( (int)(y / rowHeight)) >= timetable.getPeriodNum() ?
				timetable.getPeriodNum() - 1 : ( (int) ( y / rowHeight ));
		if(res < 0){
			return 0;
		}
		return res;
	}

	private float getPeriodByFloatFromTouchedPosition(float y){
		float res = ( y / rowHeight ) >= timetable.getPeriodNum() 
				? timetable.getPeriodNum() : (  y / rowHeight );
		if(res < 0){
			return 0;
		}
		return res;
	}


	protected boolean checkSelectedRangeAvaliable(
			int startDay,	int endDay, 
			float startPeriod, float endPeriod) {

		for(int i = startDay ; i <= endDay ; i++){
			if(checkSelectedRangeAvaliableVertical(
					i, startPeriod, endPeriod) 
					== false){
				return false;
			}
		}
		return true;
	}

	private boolean checkSelectedRangeAvaliableVertical(int day, float startPeriod, float endPeriod){
		MyLog.d("checkSelectedRangeVertical", 
				"day : " + day + ", startPeriod : " + startPeriod + ", endPeriod : " + endPeriod);
		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			Lesson tmp = timetable.getLessonList().get(i);

			PeriodInfo p = tmp.getLessonInfo();
			int pDayIdx = timetable.getDayIndexFromGregorianCalendarDay(p.getDay());

			if(day != pDayIdx)
				continue;

			MyLog.d("checkSelectedRangeAvaliable", 
					"tmp.getLessonStartPeriodByFloat() : " + tmp.getLessonStartPeriodByFloat()
					+ ", startPeriod : " + startPeriod + 
					", tmp.getLessonEndPeriodByFloat()" + tmp.getLessonEndPeriodByFloat() 
					+ ", endPeriod : " + endPeriod);
			if( ( tmp.getLessonStartPeriodByFloat() >= startPeriod 
					&& tmp.getLessonStartPeriodByFloat() < endPeriod ) ||
					( tmp.getLessonEndPeriodByFloat() > startPeriod
							&& tmp.getLessonEndPeriodByFloat() <= endPeriod) ||
							( tmp.getLessonStartPeriodByFloat() >= startPeriod 
							&& tmp.getLessonEndPeriodByFloat() <= endPeriod) ||
							(tmp.getLessonStartPeriodByFloat() <= startPeriod 
							&& tmp.getLessonEndPeriodByFloat() >= endPeriod )							
					)
			{
				MyLog.d("checkSelectedRangeAvaliable", "return false");
				//겹침.
				return false;
			}
		}
		return true;
	}
	/*private boolean checkSelectedRangeAvaliableHorizontal(int period, int startDay, int endDay){
		MyLog.d("checkSelectedRangeHorizontal", 
				"Period : " + period + ", startDay : " + startDay + ", endDay : " + endDay);
		for(int i = 0; i < timetable.getLessonList().size() ; i++){
			Lesson tmp = timetable.getLessonList().get(i);

			PeriodInfo p = tmp.getLessonInfo();
			int pDayIdx = timetable.getDayIndexFromGregorianCalendarDay(p.getDay());

			for(int j = startDay ; j <= endDay ; j++){
				if(j != pDayIdx)
					continue;
				if( ( tmp.getLessonStartPeriodByFloat() - period) * ( tmp.getLessonStartPeriodByFloat() - period) <= 0
						|| ( tmp.getLessonEndPeriodByFloat() - period ) * ( tmp.getLessonEndPeriodByFloat() - period) <= 0 )
				{
					//겹침.
					return false;
				}
			}
		}
		return true;
	}
	 */
	/**
	 * 
	 * @param startDayIndex
	 * @param endDayIndex
	 * @param startPeriod - 0부터 카운트 기준. 
	 * @param endPeriod - 0부터 카운트 기준, '끝나는'시점의 교시이므로 만약 2교시 한칸만 마킹한대도 startP는 1, endP는 2여야함.
	 * @return
	 */
	private boolean markSelectedCells(int startDayIndex, int endDayIndex, 
			float startPeriod, float endPeriod){
		//Resources res = getResources();

		markSelectedRangeLayout.setVisibility(View.VISIBLE);
		markSelectedRangeLayout.bringToFront();

		float startP = startPeriod;
		float endP = endPeriod;
		int startD = startDayIndex;
		int endD = endDayIndex;

		//2.롱클릭을 시작해 드래그가 진행된 셀까지, 순서를 정렬한다.
		if(endDayIndex - startDayIndex <= 0){
			startD = endD;
			endD = startDayIndex;
		}else{
			startD = startDayIndex;
			endD = endDayIndex;
		}

		if(endD > timetable.getDayNum() - 1){
			endD = timetable.getDayNum() - 1;
		}
		if(startD < 0){
			startD = 0;
		}

		if(endPeriod - startPeriod <= 0){
			startP = endPeriod;
			endP = startPeriod;
		}else{
			startP = startPeriod;
			endP = endPeriod;
		}

		if(endP > timetable.getPeriodNum()){
			endP = timetable.getPeriodNum();
		}

		MyLog.d("markSelectedCells", "startD : " + startD + ", endD : " + endD + ", startP : " + startP + ", endP : " + endP);

		//3.롱클릭된 셀부터 드래그 종료된 셀까지의 사각형을 그려 overlapLayout에다 임시로 겹쳐줌. 
		//ACTION_UP이 호출되면 이 임시 레이아웃을 overlapLayout에서 삭제해줘야함.

		//		View lt_cell = grid.findViewWithTag(new CellTag(startDay, startPeriod));
		//		View rb_cell = grid.findViewWithTag(new CellTag(endDay, endPeriod - 1));		//1을 빼주는 이유는 celltag가 start period를 기준으로 설정되어서.
		//		View lt_cell_parent_row = (View) lt_cell.getParent();
		//		View rb_cell_parent_row = (View) rb_cell.getParent();
		//		float cellMargin = res.getDimension(R.dimen.fragment_timetable_cell_margin);
		//
		//		int markWidth = rb_cell.getRight()- lt_cell.getLeft();
		//		int markHeight = (int) (( rb_cell_parent_row.getBottom() - cellMargin ) - ( lt_cell_parent_row.getTop() + cellMargin ));  

		//		Drawable selectedBD = 
		//				res.getDrawable(ytTheme.getSelectedRangeBackground());
		//		selectedBD.setAlpha(ytTheme.getMarkRangeAlpha());
		//		markSelectedRangeLayout.setBackgroundDrawable(selectedBD);

		//needed????
		ytTheme.getSelectedRangeBackground().setViewTheme(
				getSupportActivity(), markSelectedRangeLayout);
		int[] wh = getGridOverlapViewWH(startDayIndex, endDayIndex, startPeriod, endPeriod);
		MyLog.d("markSelectedCells", "width : " + wh[0] + ", height : " + wh[1]);
		FrameLayout.LayoutParams params = 
				(FrameLayout.LayoutParams) markSelectedRangeLayout.getLayoutParams();
		params.gravity = Gravity.LEFT | Gravity.TOP;
		//params.width = (int) cellWidth * (endD - startD + 1);
		//params.height = (int) (cellHeight * (endP - startP));
		params.width = wh[0];
		params.height = wh[1];



		Log.e("params.height", params.height + ", end : " + endP + ", start : " + startP);

		//		int leftIndex;
		//		if(timetable.getStartDay() == GregorianCalendar.MONDAY)
		//			leftIndex = day;
		//		else{
		//			leftIndex = day + (7 - timetable.getStartDay());
		//			if(leftIndex >= 7){
		//				leftIndex -= 7;
		//			}
		//		}

		//만약 타임라인부분이 터치됬으면 return -1
		float timelineOffset =  res.getDimension(R.dimen.fragment_timetable_timeline_width) + 
				res.getDimension(R.dimen.fragment_timetable_body_padding);				
		//float cellWidthIncludeMargin = cellWidth + res.getDimension(R.dimen.fragment_timetable_cell_margin) * 2;				

		//		int cellLeft = lt_cell.getLeft();
		//		//int cellTop = lt_cell.getTop();
		//
		//		int left = Math.round(timelineOffset + cellLeft);
		//		int top = Math.round(lt_cell_parent_row.getTop());
		int[] tl = this.getGridOverlapViewTLMargin(startD, startP);

		//MyLog.d("MarkView Left", "cellLeft : " + cellLeft);
		params.topMargin = tl[0];
		params.leftMargin = tl[1];
		//params.topMargin = (int) ( (startP) * cellHeight );


		markSelectedRangeLayout.setLayoutParams(params);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			gridOverlapLayout.invalidate();
		//markSelectedRangeLayout.invalidate();
		return true;
	}

	private boolean addSelectPeriodModeButtons(
			int startDayIndex,
			int endDayIndex,
			float startPeriod, 
			float endPeriod){
		if(markButtonOK == null || markButtonCancel == null){
			return false;
		}

		int cellMargin = (int) res.getDimension(R.dimen.fragment_timetable_cell_margin);

		markButtonOK.setVisibility(View.VISIBLE);
		markButtonEditLength.setVisibility(View.VISIBLE);
		markButtonOK.bringToFront();
		markButtonEditLength.bringToFront();

		int btnWidth = (int)cellWidth / 2;
		int btnHeight = btnWidth;

		markButtonOK.setOnClickListener(
				new MarkOKOnClickListener(startDayIndex, endDayIndex, startPeriod, endPeriod));

		//4.이제 좌우에 버튼 추가.
		//4-1. markButtonOK
		//		markButtonOK.setText("E");
		//btnOK.setWidth(markSelectedRangeLayout.getWidth() / 3);
		//btnOK.setHeight(markSelectedRangeLayout.getWidth() / 3);
		FrameLayout.LayoutParams btnOKParams = 
				(FrameLayout.LayoutParams) markButtonOK.getLayoutParams();
		btnOKParams.gravity = Gravity.LEFT | Gravity.TOP;
		btnOKParams.width = btnWidth;
		btnOKParams.height = btnHeight;
		//버튼 절반 겹치도록
		//		btnOKParams.leftMargin = (int) (
		//				((FrameLayout.LayoutParams)
		//						markSelectedRangeLayout.getLayoutParams()).leftMargin - ( ( cellWidth / 2)  / 2 )) ;
		//		btnOKParams.topMargin = (int) (((FrameLayout.LayoutParams)
		//				markSelectedRangeLayout.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
		//버튼이 완전히 왼쪽에 오도록
		btnOKParams.leftMargin = ((FrameLayout.LayoutParams)
				markSelectedRangeLayout.getLayoutParams()).leftMargin - btnWidth ;
		btnOKParams.topMargin = ((FrameLayout.LayoutParams)
				markSelectedRangeLayout.getLayoutParams()).topMargin - btnHeight ;
		//만약 선택한게 요일표시 행 바로 아랫부분이라면
		if(startPeriod == 0){
			btnOKParams.topMargin = (((FrameLayout.LayoutParams)
					markSelectedRangeLayout.getLayoutParams()).topMargin) ;
		}
		//		if(lessonLongClickedDay == timetable.getEndDay()){
		//			btnOKParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							markSelectedRangeLayout.getLayoutParams()).leftMargin) ;
		//		}

		markButtonOK.setLayoutParams(btnOKParams);

		//4-3.paste
		if(lessonCopied != null){
			//1.add cancel button
			markButtonCancel.setVisibility(View.VISIBLE);
			markButtonCancel.bringToFront();

			//4-2. markButtonCancel
			//		markButtonCancel.setText("D");
			FrameLayout.LayoutParams btnCancelParams = 
					(FrameLayout.LayoutParams) markButtonCancel.getLayoutParams();
			btnCancelParams.gravity = Gravity.LEFT | Gravity.TOP;
			btnCancelParams.width = btnWidth;
			btnCancelParams.height = btnHeight;
			//		btnCancelParams.leftMargin = (int) (
			//				((FrameLayout.LayoutParams)
			//						markSelectedRangeLayout.getLayoutParams()).leftMargin + cellWidth - ( ( cellWidth / 2)  / 2 )) ;
			//		btnCancelParams.topMargin = (int) (
			//				((FrameLayout.LayoutParams)
			//						markSelectedRangeLayout.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
			btnCancelParams.leftMargin = (int) (
					((FrameLayout.LayoutParams)
							markSelectedRangeLayout.getLayoutParams()).leftMargin + 
							(cellWidth + cellMargin*2) * (endDayIndex - startDayIndex + 1)  ) ;
			btnCancelParams.topMargin = ((FrameLayout.LayoutParams)
					markSelectedRangeLayout.getLayoutParams()).topMargin - btnHeight ;
			if(startPeriod == 0){
				btnCancelParams.topMargin = (((FrameLayout.LayoutParams)
						markSelectedRangeLayout.getLayoutParams()).topMargin) ;
			}
			if(startDayIndex == timetable.getDayIndexFromGregorianCalendarDay(timetable.getEndDay()) ||
					endDayIndex == timetable.getDayIndexFromGregorianCalendarDay(timetable.getEndDay())){
				btnCancelParams.leftMargin = (int) (
						((FrameLayout.LayoutParams)
								markSelectedRangeLayout.getLayoutParams()).leftMargin 
								+ (cellWidth + cellMargin*2) * (endDayIndex - startDayIndex + 1) 
								- btnCancelParams.width);
			}
			markButtonCancel.setLayoutParams(btnCancelParams);



			//2.add paste button
			pasteLessonToMark.setVisibility(View.VISIBLE);
			pasteLessonToMark.bringToFront();

			pasteLessonToMark.setOnClickListener(
					new PasteLessonToMarkOnClick(
							startDayIndex, endDayIndex, startPeriod, endPeriod
							)
					);

			//			pasteLessonToMark.setText("P");
			FrameLayout.LayoutParams btnPasteParams = 
					(FrameLayout.LayoutParams) pasteLessonToMark.getLayoutParams();
			btnPasteParams.gravity = Gravity.LEFT | Gravity.TOP;
			btnPasteParams.width = btnWidth;
			btnPasteParams.height = btnHeight;
			//		btnCancelParams.leftMargin = (int) (
			//				((FrameLayout.LayoutParams)
			//						v.getLayoutParams()).leftMargin + cellWidth - ( ( cellWidth / 2)  / 2 )) ;
			//		btnCancelParams.topMargin = (int) (
			//				((FrameLayout.LayoutParams)
			//						v.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
			btnPasteParams.leftMargin = ((FrameLayout.LayoutParams)
					markSelectedRangeLayout.getLayoutParams()).leftMargin - ( btnWidth  ) ;
			btnPasteParams.topMargin = ((FrameLayout.LayoutParams)
					markSelectedRangeLayout.getLayoutParams()).topMargin + 
					markSelectedRangeLayout.getLayoutParams().height ;
			if(endPeriod == timetable.getPeriodNum()){
				btnPasteParams.topMargin = ((FrameLayout.LayoutParams)
						markSelectedRangeLayout.getLayoutParams()).topMargin +
						markSelectedRangeLayout.getLayoutParams().height - (btnHeight) ;
			}
			//		if(tmpLesson.getLessonInfo().getDay() == timetable.getEndDay()){
			//			btnCopyParams.leftMargin = (int) (
			//					((FrameLayout.LayoutParams)
			//							v.getLayoutParams()).leftMargin + cellWidth - btnCopyParams.width) ;
			//		}
			pasteLessonToMark.setLayoutParams(btnPasteParams);
		}

		//4-4.marked layout edit length button
		FrameLayout.LayoutParams btnEditLengthParams = 
				(FrameLayout.LayoutParams) lessonEditLength.getLayoutParams();
		btnEditLengthParams.gravity = Gravity.LEFT | Gravity.TOP;
		btnEditLengthParams.width = btnWidth;
		btnEditLengthParams.height = btnHeight;
		//			btnEditLengthParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							attatchedView.getLayoutParams()).leftMargin + cellWidth - ( ( cellWidth / 2)  / 2 )) ;
		//			btnEditLengthParams.topMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							attatchedView.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
		//		btnEditLengthParams.leftMargin = (int) (
		//				((FrameLayout.LayoutParams)
		//						markSelectedRangeLayout.getLayoutParams()).leftMargin + cellWidth  ) ;
		btnEditLengthParams.leftMargin = (int) (
				((FrameLayout.LayoutParams)
						markSelectedRangeLayout.getLayoutParams()).leftMargin + 
						(cellWidth + cellMargin*2) * (endDayIndex - startDayIndex + 1)  ) ;
		btnEditLengthParams.topMargin = ((FrameLayout.LayoutParams)
				markSelectedRangeLayout.getLayoutParams()).topMargin 
				+ markSelectedRangeLayout.getLayoutParams().height ;
		if(endPeriod == timetable.getPeriodNum()){
			btnEditLengthParams.topMargin = ((FrameLayout.LayoutParams)
					markSelectedRangeLayout.getLayoutParams()).topMargin 
					+ markSelectedRangeLayout.getLayoutParams().height - btnHeight ;
		}
		//		if(startDay == timetable.getDayIndexFromGregorianCalendarDay(timetable.getEndDay()) ||
		//				endDay == timetable.getDayIndexFromGregorianCalendarDay(timetable.getEndDay())){
		//			btnEditLengthParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							markSelectedRangeLayout.getLayoutParams()).leftMargin 
		//							+ cellWidth - btnEditLengthParams.width) ;
		//		}
		if(startDayIndex == timetable.getDayIndexFromGregorianCalendarDay(timetable.getEndDay()) ||
				endDayIndex == timetable.getDayIndexFromGregorianCalendarDay(timetable.getEndDay())){
			btnEditLengthParams.leftMargin = (int) (
					((FrameLayout.LayoutParams)
							markSelectedRangeLayout.getLayoutParams()).leftMargin 
							+ (cellWidth + cellMargin*2) * (endDayIndex - startDayIndex + 1) 
							- btnEditLengthParams.width);
		}

		markButtonEditLength.setLayoutParams(btnEditLengthParams);

		return true;
	}

	private void dismissOverlapMarker(boolean dismissSelectedRangeLayout){
		isLessonEditButtonsShowing = false;
		if(dismissSelectedRangeLayout == true)
			markSelectedRangeLayout.setVisibility(View.GONE);
		markButtonOK.setVisibility(View.GONE);
		markButtonCancel.setVisibility(View.GONE);
		pasteLessonToMark.setVisibility(View.GONE);
		markButtonEditLength.setVisibility(View.GONE);

		lessonEditOK.setVisibility(View.GONE);
		lessonRemove.setVisibility(View.GONE);
		lessonCopy.setVisibility(View.GONE);
		lessonEditLength.setVisibility(View.GONE);
	}

//	private View.OnClickListener onTimelineClickedListener = new View.OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(getSupportActivity(), TimetableSettingPagerActivity.class);
//			intent.putExtra("ShowTimeSettingDialog", true);
//			intent.putExtra("TimetablePageIndex", getMyIndexInViewPager());
//			startActivityForResult(intent, RequestCodes.CALL_ACTIVITY_EDIT_TIMETABLE_SETTING);
//		}
//	};
	
	private class MarkOKOnClickListener implements View.OnClickListener{
		private int startDayIndex;
		private int endDayIndex;
		private float startPeriod;
		private float endPeriod;

		public MarkOKOnClickListener(int startDayIndex, int endDayIndex, 
				float startPeriod2, float endPeriod2){
			this.startDayIndex = startDayIndex;
			this.endDayIndex = endDayIndex;
			this.startPeriod = startPeriod2;
			this.endPeriod = endPeriod2;
			MyLog.d("MarkOKOnClick", "startPeriod : " + startPeriod + " ,endPeriod : " + endPeriod);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//			LessonEditDialogFragment dialog = new LessonEditDialogFragment();
			String s = getString(R.string.fragment_timetable_oneditlesson_toast);
			Toast.makeText(getSupportActivity(), s, Toast.LENGTH_LONG).show();

			LessonEditDialogBuilder builder = new LessonEditDialogBuilder(
					TimetableFragment.this.getActivity(), TimetableFragment.this);

			Bundle b = new Bundle();


			int startDay = timetable.getGregorianCalendarDayFromDayIndex(startDayIndex);
			int endDay = timetable.getGregorianCalendarDayFromDayIndex(endDayIndex);
			b.putInt("StartDay", startDay);
			b.putInt("EndDay", endDay);
			b.putFloat("StartPeriod", startPeriod);
			b.putFloat("EndPeriod", endPeriod);
            b.putInt("TimetablePageIndex", myPageIndex);
			builder.createDialog(null, b, "Add").show();

			//			dialog.setArguments(b);


			//			dialog.setTargetFragment(
			//					TimetableFragment.this, 
			//					TimetableFragment.FRAG_TIMETABLE_DIALOG_ADD_LESSON
			//					);

		}

	}

	View.OnClickListener markCancelOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dismissOverlapMarker(true);
		}
	};

	private class PasteLessonToMarkOnClick implements View.OnClickListener{

		private int startDay;
		private int endDay;
		private float startPeriod;
		//		private float endPeriod;

		public PasteLessonToMarkOnClick(int startDay, int endDay, 
				float startPeriod2, float endPeriod2){
			this.startDay = startDay;
			this.endDay = endDay;
			this.startPeriod = startPeriod2;
			//			this.endPeriod = endPeriod2;	//don't use endperiod
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for(int i = startDay ; i <= endDay ; i++){
				//				Lesson pastedLesson = pasteLesson(i, startPeriod, endPeriod);
				Lesson pastedLesson = pasteLesson(i, startPeriod);
				if(timetable.getLessonAlarmTime() != Timetable.LESSON_ALARM_NONE){
					YTAlarmManager.startLessonAlarm(
							getSupportActivity(), pastedLesson, timetable.getLessonAlarmTime());
				}
			}
			//			MyLog.d("FileThreadSafeTest", "Lessons pasted, size : " + timetable.getLessonList().size());
//			TimetableDataManager.writeDatasToExternalStorage();
			MyLog.d("FileThreadSafeTest", "Lessons pasted, size : " + timetable.getLessonList().size());
			lessonCopied = null;
		}

		private Lesson pasteLesson(int day, float _startPeriod){
			Lesson lessonToPaste = lessonCopied.clone();

			float _endPeriod = _startPeriod + lessonToPaste.getPeriodLengthByFloat();
			if(_endPeriod > timetable.getPeriodNum()){
				_endPeriod = timetable.getPeriodNum();
			}
			lessonToPaste.setPeriodInfo(
					new PeriodInfo(
							timetable.getGregorianCalendarDayFromDayIndex(day),
							timetable.getStartHourOfPeriod(_startPeriod),
							timetable.getStartMinOfPeriod(_startPeriod),
							timetable.getEndHourOfPeriod(_endPeriod),
							timetable.getEndMinOfPeriod(_endPeriod)
							)
					);
			for(int i = 0; i < timetable.getLessonList().size() ; i++){
				Lesson tmp = timetable.getLessonList().get(i);
				if(Lesson.checkLessonCollideWith(tmp, lessonToPaste)
						== true){
					_endPeriod = tmp.getLessonStartPeriodByFloat();
					lessonToPaste.setPeriodInfo(
							new PeriodInfo(
									timetable.getGregorianCalendarDayFromDayIndex(day),
									timetable.getStartHourOfPeriod(_startPeriod),
									timetable.getStartMinOfPeriod(_startPeriod),
									timetable.getEndHourOfPeriod(_endPeriod),
									timetable.getEndMinOfPeriod(_endPeriod)
									)
							);
					break;
				}
			}

			if(timetable.addLesson(lessonToPaste) == false){
				MyLog.d("pasteLesson", "lesson to paste NULL!");
				return null;
			}

			View lessonView = createLessonViewFromLesson(lessonToPaste);
			if(lessonView != null)
				gridOverlapLayout.addView(lessonView);
			dismissOverlapMarker(true);
			return lessonToPaste;
		}
	}


	//수업 뷰를 클릭하면 에딧 or erase 선택하도록 함.
	private class LessonEditOKOnClick implements View.OnClickListener{
		private View lessonView;
		public LessonEditOKOnClick(View lessonView){
			this.lessonView = lessonView;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Lesson lesson = (Lesson) lessonView.getTag();
			
			String s = getString(R.string.fragment_timetable_oneditlesson_toast);
			Toast.makeText(getSupportActivity(), s, Toast.LENGTH_LONG).show();
			//lessonViewToEdit = v;
			//lessonToAddAndEdit = lesson;

			//			LessonEditDialogFragment dialog = new LessonEditDialogFragment();
			LessonEditDialogBuilder builder =
					new LessonEditDialogBuilder(
							TimetableFragment.this.getActivity(), TimetableFragment.this);
            Bundle bundle = new Bundle();
            bundle.putInt("TimetablePageIndex", myPageIndex);
			builder.createDialog(lesson, bundle, "Edit").show();

			//			dialog.show(getFragmentManager(), "Edit");
			//			Bundle b = new Bundle();
			//b.putParcelable("LessonView", v);
			//b.putParcelable("Lesson", lesson);

			//			int lessonIndex = -1;
			//			for(int i = 0; i < timetable.getLessonList().size() ; i++){
			//				Lesson tmp = timetable.getLessonList().get(i);
			//				if(tmp.equals(lesson)){
			//					lessonIndex = i;
			//					break;
			//				}
			//			}
			//			b.putInt("LessonIndex", lessonIndex);
			//			dialog.setArguments(b);

			//			dialog.setTargetFragment(TimetableFragment.this, 
			//					TimetableFragment.FRAG_TIMETABLE_DIALOG_EDIT_LESSON);

		}

	}
	private class LessonRemoveOnClick implements View.OnClickListener{
		View lessonView;
		public LessonRemoveOnClick(View lessonView){
			this.lessonView = lessonView;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Lesson tmpLesson = (Lesson) lessonView.getTag();
			timetable.onRemoveLesson(tmpLesson);
			timetable.getLessonList().remove(tmpLesson);			
			gridOverlapLayout.removeView(lessonView);
//			TimetableDataManager.writeDatasToExternalStorage();

			YTAlarmManager.cancelLessonAlarm(getSupportActivity(), tmpLesson);

			dismissOverlapMarker(true);
		}

	}

	View.OnClickListener lessonOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			MyLog.d("TouchSystemCheck", "touched : " + "lessonOnClick");
			// TODO Auto-generated method stub
			if(isEditLessonLengthMode == true || isSelectPeriodMode == true){
				return;
			}
			dismissOverlapMarker(true);

			//lessonViewToEdit = v;
			Lesson tmpLesson = (Lesson) v.getTag();
			showLessonEditButtons(v, tmpLesson);

		}
	};

	private boolean isLessonEditButtonsShowing = false;
	private void showLessonEditButtons(View attatchedView, Lesson attatchedLesson){
		isLessonEditButtonsShowing = true;

		lessonEditOK.setVisibility(View.VISIBLE);
		lessonRemove.setVisibility(View.VISIBLE);
		lessonCopy.setVisibility(View.VISIBLE);
		lessonEditLength.setVisibility(View.VISIBLE);

		lessonEditOK.setOnClickListener(new LessonEditOKOnClick(attatchedView));
		lessonRemove.setOnClickListener(new LessonRemoveOnClick(attatchedView));
		lessonCopy.setOnClickListener(new LessonCopyOnClick(attatchedView));
		lessonEditLength.setOnTouchListener(new LessonEditLengthTouchListener(attatchedView));

		lessonEditOK.bringToFront();
		lessonRemove.bringToFront();			
		lessonCopy.bringToFront();
		lessonEditLength.bringToFront();

		int btnWidth = (int) cellWidth / 2;
		int btnHeight = btnWidth;
		

		//4.이제 좌우에 버튼 추가.
		//4-1. lessonEditOK
		//		lessonEditOK.setText("E");
		//btnOK.setWidth(markSelectedRangeLayout.getWidth() / 3);
		//btnOK.setHeight(markSelectedRangeLayout.getWidth() / 3);
		FrameLayout.LayoutParams btnOKParams = 
				(FrameLayout.LayoutParams) lessonEditOK.getLayoutParams();
		btnOKParams.gravity = Gravity.LEFT | Gravity.TOP;
		btnOKParams.width = btnWidth;
		btnOKParams.height = btnHeight;
		//			btnOKParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							v.getLayoutParams()).leftMargin - ( ( cellWidth / 2)  / 2 )) ;
		//			btnOKParams.topMargin = (int) (((FrameLayout.LayoutParams)
		//					v.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
		btnOKParams.leftMargin = ((FrameLayout.LayoutParams)
				attatchedView.getLayoutParams()).leftMargin - btnWidth ;
		btnOKParams.topMargin = ((FrameLayout.LayoutParams)
				attatchedView.getLayoutParams()).topMargin - (btnHeight) ;
		//만약 선택한게 요일표시 행 바로 아랫부분이라면
		float halfHourByPeriod = 30.0f / (float)timetable.getPeriodUnit();
		if(attatchedLesson.getLessonStartPeriodByFloat() <= halfHourByPeriod){
			btnOKParams.topMargin = (((FrameLayout.LayoutParams)
					attatchedView.getLayoutParams()).topMargin) ;
		}
		//			if(attatchedLesson.getLessonInfo().getDay() == timetable.getEndDay()){
		//				btnOKParams.leftMargin = (int) (
		//						((FrameLayout.LayoutParams)
		//								v.getLayoutParams()).leftMargin) ;
		//			}

		lessonEditOK.setLayoutParams(btnOKParams);

		//4-2. lessonRemove
		//		lessonRemove.setText("D");
		FrameLayout.LayoutParams btnCancelParams = 
				(FrameLayout.LayoutParams) lessonRemove.getLayoutParams();
		btnCancelParams.gravity = Gravity.LEFT | Gravity.TOP;
		btnCancelParams.width = btnWidth;
		btnCancelParams.height = btnHeight;
		//			btnCancelParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							v.getLayoutParams()).leftMargin + cellWidth - ( ( cellWidth / 2)  / 2 )) ;
		//			btnCancelParams.topMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							v.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
		btnCancelParams.leftMargin = (int) (
				((FrameLayout.LayoutParams)
						attatchedView.getLayoutParams()).leftMargin + cellWidth  ) ;
		btnCancelParams.topMargin = ((FrameLayout.LayoutParams)
				attatchedView.getLayoutParams()).topMargin - btnHeight ;
		if(attatchedLesson.getLessonStartPeriodByFloat() <= halfHourByPeriod){
			btnCancelParams.topMargin = (((FrameLayout.LayoutParams)
					attatchedView.getLayoutParams()).topMargin) ;
		}
		if(attatchedLesson.getLessonInfo().getDay() == timetable.getEndDay()){
			btnCancelParams.leftMargin = (int) (
					((FrameLayout.LayoutParams)
							attatchedView.getLayoutParams()).leftMargin + cellWidth - btnCancelParams.width) ;
		}
		lessonRemove.setLayoutParams(btnCancelParams);

		//4-3.lessonCopy
		//		lessonCopy.setText("C");

		FrameLayout.LayoutParams btnCopyParams = 
				(FrameLayout.LayoutParams) lessonCopy.getLayoutParams();
		btnCopyParams.gravity = Gravity.LEFT | Gravity.TOP;
		//btnCopyParams.width = (int) cellWidth / 2 ;
		//btnCopyParams.height = (int) cellWidth / 2;
		btnCopyParams.width = btnWidth;
		btnCopyParams.height = btnHeight;
		//			btnCancelParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							attatchedView.getLayoutParams()).leftMargin + cellWidth - ( ( cellWidth / 2)  / 2 )) ;
		//			btnCancelParams.topMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							attatchedView.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
		btnCopyParams.leftMargin = ((FrameLayout.LayoutParams)
				attatchedView.getLayoutParams()).leftMargin - ( btnWidth  ) ;
		btnCopyParams.topMargin = ((FrameLayout.LayoutParams)
				attatchedView.getLayoutParams()).topMargin + attatchedView.getLayoutParams().height ;
		if(attatchedLesson.getLessonEndPeriodByFloat() == timetable.getPeriodNum()){
			btnCopyParams.topMargin = ((FrameLayout.LayoutParams)
					attatchedView.getLayoutParams()).topMargin + attatchedView.getLayoutParams().height - btnHeight ;
		}
		//			if(attatchedLesson.getLessonInfo().getDay() == timetable.getEndDay()){
		//				btnCopyParams.leftMargin = (int) (
		//						((FrameLayout.LayoutParams)
		//								attatchedView.getLayoutParams()).leftMargin + cellWidth - btnCopyParams.width) ;
		//			}
		lessonCopy.setLayoutParams(btnCopyParams);
		lessonCopy.setGravity(Gravity.CENTER);

		//lesson Edit Length setting params
		//4-2. lessonRemove

		FrameLayout.LayoutParams btnEditLengthParams = 
				(FrameLayout.LayoutParams) lessonEditLength.getLayoutParams();
		btnEditLengthParams.gravity = Gravity.LEFT | Gravity.TOP;
		btnEditLengthParams.width = btnWidth;
		btnEditLengthParams.height = btnHeight;
		//			btnEditLengthParams.leftMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							attatchedView.getLayoutParams()).leftMargin + cellWidth - ( ( cellWidth / 2)  / 2 )) ;
		//			btnEditLengthParams.topMargin = (int) (
		//					((FrameLayout.LayoutParams)
		//							attatchedView.getLayoutParams()).topMargin - ( ( cellWidth / 2)  / 2 )) ;
		btnEditLengthParams.leftMargin = (int) (
				((FrameLayout.LayoutParams)
						attatchedView.getLayoutParams()).leftMargin + cellWidth  ) ;
		btnEditLengthParams.topMargin = ((FrameLayout.LayoutParams)
				attatchedView.getLayoutParams()).topMargin + attatchedView.getLayoutParams().height ;
		if(attatchedLesson.getLessonEndPeriodByFloat() == timetable.getPeriodNum()){
			btnEditLengthParams.topMargin = ((FrameLayout.LayoutParams)
					attatchedView.getLayoutParams()).topMargin + attatchedView.getLayoutParams().height - btnHeight ;
		}
		if(attatchedLesson.getLessonInfo().getDay() == timetable.getEndDay()){
			btnEditLengthParams.leftMargin = (int) (
					((FrameLayout.LayoutParams)
							attatchedView.getLayoutParams()).leftMargin + cellWidth - btnEditLengthParams.width) ;
		}
		lessonEditLength.setLayoutParams(btnEditLengthParams);
	}
	/**
	 * lesson을 받아서 grid에 새롭게 생길 lesson view의 width와 height를 int배열 wh[2]에 넣어 반환.
	 * width = wh[0], height = wh[1]
	 * @param lesson
	 * @return wh[2]
	 */
	private int[] getLessonViewWHFromLesson(Lesson lesson){
		//int[] wh = new int[2];
		int startDay = timetable.getDayIndexFromGregorianCalendarDay(lesson.getLessonInfo().getDay());
		float startPeriod = lesson.getLessonStartPeriodByFloat();
		float endPeriod = lesson.getLessonEndPeriodByFloat();
		return getGridOverlapViewWH(startDay, startDay, startPeriod, endPeriod);
	}

	private int[] getGridOverlapViewWH(int startDay, int endDay, float startPeriod, float endPeriod){
		int[] wh = new int[2];
		//
		//		View lt_cell = grid.findViewWithTag(new CellTag(startDay, startPeriod));
		//		View rb_cell = grid.findViewWithTag(new CellTag(endDay, endPeriod - 1));		//1을 빼주는 이유는 celltag가 start period를 기준으로 설정되어서.
		//		View lt_cell_parent_row = (View) lt_cell.getParent();
		//		View rb_cell_parent_row = (View) rb_cell.getParent();
		//		float cellMargin = res.getDimension(R.dimen.fragment_timetable_cell_margin);
		//
		//		int markWidth = rb_cell.getRight()- lt_cell.getLeft();
		//		int markHeight = (int) (( rb_cell_parent_row.getBottom() - cellMargin ) 
		//		- ( lt_cell_parent_row.getTop() + cellMargin ));
		int intStartPeriod = (int)startPeriod;
		int intEndPeriod = (int)endPeriod;
		
		float startOffsetToMinus = ( startPeriod - (float)intStartPeriod ) * rowHeight;
		float endOffsetToPlus = (endPeriod - (float)intEndPeriod) * rowHeight;
		MyLog.d("getGridOverlapViewWH", "intStartPeriod : " + intStartPeriod 
				+ ", startOffset : " + startOffsetToMinus);
		
		View lt_cell = grid.findViewWithTag(new CellTag(startDay, intStartPeriod));
		View rb_cell = grid.findViewWithTag(new CellTag(endDay, intEndPeriod - 1));
		if(intEndPeriod == 0){
			rb_cell = grid.findViewWithTag(new CellTag(endDay, 0));
		}
		if(lt_cell == null || rb_cell == null){
			return null;
		}
		View lt_cell_parent_row = (View) lt_cell.getParent();
		View rb_cell_parent_row = (View) rb_cell.getParent();
		float cellMargin = res.getDimension(R.dimen.fragment_timetable_cell_margin);
		float bodyPadding = res.getDimension(R.dimen.fragment_timetable_body_padding);
		
		int rb_cell_right = rb_cell.getRight();
		int lt_cell_left = lt_cell.getLeft();
		int rb_cell_parent_bottom = rb_cell_parent_row.getBottom();
		int lt_cell_parent_top = lt_cell_parent_row.getTop();
		if(startPeriod == 0){
			lt_cell_parent_top += bodyPadding;
		}
		if(endPeriod == timetable.getPeriodNum()){
			rb_cell_parent_bottom -= bodyPadding;
		}
		wh[0] = rb_cell_right- lt_cell_left;
		wh[1] = (int) ( ( rb_cell_parent_bottom - cellMargin ) 
						- ( lt_cell_parent_top + cellMargin ) 
						- startOffsetToMinus + endOffsetToPlus);
		if(intEndPeriod == 0){
			wh[1] = (int) (endOffsetToPlus - startOffsetToMinus - cellMargin);
		}
		MyLog.d("getLessonViewWHFromLesson", "rb_cell_right : " + rb_cell_right 
				+ "lt_cell_left : " + lt_cell_left
				+ "rb_cell_parent_bottom : " + rb_cell_parent_bottom 
				+ "lt_cell_parent_top : " + lt_cell_parent_top
				+ "wh[0] : " + wh[0]
						+ "wh[1] : " + wh[1]);
		return wh;
	}

	/**
	 * float값 period를 받아 gridOVerlapLayout기준으로 뷰의 top margin을 가져온다.
	 * @param period
	 * @return
	 */
	private float getViewTopMarginFromPeriod(float period){
		float height = period * rowHeight;

		MyLog.d("getViewTopMarginFromPeriod", height+"");
		return height;
	}

	/**
	 * lesson을 받아서 grid에 새롭게 생길 lesson view의 top margin과 left margin int배열 tl[2]에 넣어 반환.
	 * timeline offset등의 기타 마진값들도 미리 계산되어 넘겨준다.
	 * tl[0] : top margin, tl[1] : left margin
	 * @param lesson
	 * @return lt[2]
	 */
	private int[] getLessonViewTLMarginFromLesson(Lesson lesson){
		//int[] tl = new int[2];
		int startDay = timetable.getDayIndexFromGregorianCalendarDay(lesson.getLessonInfo().getDay());
		float startPeriod = lesson.getLessonStartPeriodByFloat();

		return getGridOverlapViewTLMargin(startDay, startPeriod);
	}

	private int[] getGridOverlapViewTLMargin(int startDayIndex, float startPeriod){
		int[] tl = new int[2];
		View lt_cell = 
				grid.findViewWithTag(new CellTag(startDayIndex, (int)startPeriod));
		int intStartPeriod = (int)startPeriod;
		float topOffsetToAdd = (startPeriod - (float)intStartPeriod) * rowHeight;
		if(lt_cell == null){
			return null;
		}
		View lt_cell_parent_row = (View) lt_cell.getParent();
		float cellMargin = res.getDimension(R.dimen.fragment_timetable_cell_margin);		
		float bodyPadding = res.getDimension(R.dimen.fragment_timetable_body_padding);
		float timelineOffset =  res.getDimension(R.dimen.fragment_timetable_timeline_width);
//				res.getDimension(R.dimen.fragment_timetable_body_padding);				

		int cellLeft = lt_cell.getLeft();
		
		if(startPeriod == 0){
			tl[0] = Math.round(lt_cell_parent_row.getTop() + cellMargin
					+ bodyPadding);
		}else{
			tl[0] = Math.round(lt_cell_parent_row.getTop() + cellMargin);
		}
		tl[0] += topOffsetToAdd;
		tl[1] = Math.round(timelineOffset + cellLeft);
		return tl;
	}


	private Lesson lessonCopied;
	private class LessonCopyOnClick implements View.OnClickListener{
		private View lessonView;
		public LessonCopyOnClick(View lessonView){
			this.lessonView = lessonView;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			lessonCopied = (Lesson) lessonView.getTag();
			String nt = res.getString(R.string.fragment_timetable_notice_lessoncopied);
			Toast.makeText(getSupportActivity(), nt, Toast.LENGTH_SHORT).show();

			dismissOverlapMarker(true);
		}

	}

	public boolean isEditLessonLengthMode = false;
	Lesson lessonToEditLength;
	private class LessonEditLengthTouchListener implements View.OnTouchListener{
		private View lessonView;
		public LessonEditLengthTouchListener(View lessonView){
			this.lessonView = lessonView;

		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			MyLog.d("TouchSystemCheck", "touched : " + "LessonEditLengthTouchListener");
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN : 
				//				touchedDownX = event.getX();
				//				touchedDownY = event.getY();
				//				lessonViewDefaultHeight = lessonView.getHeight();
				tableScroll.requestDisallowInterceptTouchEvent(true);

				isEditLessonLengthMode = true;
				lessonToEditLength = (Lesson) lessonView.getTag();
				//gridOverlapLayout.dispatchTouchEvent(event);
				//자꾸 그리드오버랩레이아웃의 롱클릭리스너가 호출되니 블락해버리자.

				MyLog.d("TouchSystemCheck", "isLessonEditLengthMode = true");
				MyLog.d("LessonEditLength", "isLessonEditLengthMode = true");
				return false;
			case MotionEvent.ACTION_MOVE : 
			case MotionEvent.ACTION_UP : 
			case MotionEvent.ACTION_CANCEL : 
				//				isEditLessonLengthMode = false;
				//				MyLog.d("LessonEditLength", "isLessonEditLengthMode = false");
			}
			return false;
		}
	}

	private boolean isMarkButtonEditLengthTouched = false;
	private class MarkButtonEditLengthTouchListener implements View.OnTouchListener{
		//private View lessonView;
		//		public LessonEditLengthTouchListener(View lessonView){
		//			this.lessonView = lessonView;
		//
		//		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			MyLog.d("TouchSystemCheck", "touched : " + "MarkButtonEditLengthTouchListener, event : " + event.getAction());
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN : 
				//				touchedDownX = event.getX();
				//				touchedDownY = event.getY();
				//				lessonViewDefaultHeight = lessonView.getHeight();
				if(markSelectedRangeLayout.getVisibility() == View.INVISIBLE ||
				markSelectedRangeLayout.getVisibility() == View.GONE){
					dismissOverlapMarker(true);
					isSelectPeriodMode = false;
					isMarkButtonEditLengthTouched = false;
					return false;
				}
				isMarkButtonEditLengthTouched = true;
				touchedDownPositionX =
						markSelectedRangeLayout.getLeft() + cellWidth / 2;
				touchedDownPositionY = 
						markSelectedRangeLayout.getTop() + rowHeight / 2;
				tableScroll.requestDisallowInterceptTouchEvent(true);
				isSelectPeriodMode = true;
				//				dismissOverlapMarker(false);

				//asd

				MyLog.d("TouchSystemCheck", "markButtonEditLengthOnTouch, selectPeriodMode = true");
				return false;
			case MotionEvent.ACTION_MOVE : 
			case MotionEvent.ACTION_UP : 
				return false;
			case MotionEvent.ACTION_CANCEL : 
				return false;
			}
			return false;
		}
	}


	public void onActivityResult(int requestCode, int resultCode){
		//this.onActivityResult(requestCode, resultCode, data)
		if(requestCode == TimetableFragment.FRAG_TIMETABLE_DIALOG_EDIT_LESSON ||
				requestCode == TimetableFragment.FRAG_TIMETABLE_DIALOG_ADD_LESSON){
			if(resultCode == android.app.Activity.RESULT_OK){
				//				int index = data.getIntExtra("LessonIndex", -1);
				//				Lesson editedLesson = timetable.getLessonList().get(index);
				//				View lessonViewToEdit = gridOverlapLayout.findViewWithTag(editedLesson);
				//
				//				if(lessonViewToEdit != null)
				//					gridOverlapLayout.removeView(lessonViewToEdit);
				//
				//				//Lesson lesson = (Lesson) data.getParcelableExtra("LessonOut");
				//				//Lesson lesson = data.getExtras().getParcelable("LessonOut");
				//				View lessonView = createLessonViewFromLesson(editedLesson);			
				//
				//				gridOverlapLayout.addView(lessonView);
				//
				//				for(int i = 0; i < timetable.getLessonList().size() ; i++)
				//					Log.e("onActivityResult", timetable.getLessonList().get(i).toString());

				for(int i = 0; i < timetable.getLessonList().size() ; i++){
					Lesson l = timetable.getLessonList().get(i);
					View lessonView = gridOverlapLayout.findViewWithTag(l);
					gridOverlapLayout.removeView(lessonView);
				}
				for(int i = 0; i < timetable.getLessonList().size() ; i++){
					View lessonView = createLessonViewFromLesson(
							timetable.getLessonList().get(i)
							);					
					if(lessonView != null)
						gridOverlapLayout.addView(lessonView);
				}
				//새로 add된 레슨뷰들에 가려져버린 currentTimeMarker가 제일 위에 올라와야함.
				currentTimeMarker.bringToFront();

//				TimetableDataManager.writeDatasToExternalStorage();
				YTAlarmManager.cancelTimetableAlarm(getSupportActivity(), timetable);
				if(timetable.getLessonAlarmTime() != Timetable.LESSON_ALARM_NONE){
					YTAlarmManager.startTimetableAlarm(getSupportActivity(), timetable);
				}
//				YTAppWidgetProvider_2x4.onTimetableDataChanged(getSupportActivity());
//				YTAppWidgetProvider_4x4.onTimetableDataChanged(getSupportActivity());

			}else if(resultCode == android.app.Activity.RESULT_CANCELED){
				for(int i = 0; i < timetable.getLessonList().size() ; i++)
					Log.e("onActivityResult", timetable.getLessonList().get(i).toString());
			}
			dismissOverlapMarker(true);
		}else if(requestCode == TimetableFragment.FRAG_TIMETABLE_DIALOG_DELETE_TIMETABLE){
			if(resultCode == android.app.Activity.RESULT_OK){

				TimetableActivity ta = (TimetableActivity) getSupportActivity();
				int position =  ta.getViewPager().getCurrentItem();
				TimetableDataManager.deleteTimetableBackgroundPhotoIfExists(
						getSupportActivity(), 
						timetable);
				ta.removePageAt(position);
				TimetableDataManager.writeDatasToExternalStorage();
				YTAlarmManager.cancelTimetableAlarm(getSupportActivity(), timetable);
			}else if(resultCode == android.app.Activity.RESULT_CANCELED){

			}
		}
		//여기서 Timetable Setting 처리하지 않는 이유는, 세팅이 바뀌면 이 프래그먼트를 완전히 새로 만들어내야하는데
		//그게 불가능해서임.
	}

	public View createLessonViewFromLesson(Lesson lesson){
		View lessonView= View.inflate(getSupportActivity(), R.layout.view_timetable_lessonview, null);
		//grid.setclip
		TextView textSubject = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_subject)
				);
		textSubject.setText(lesson.getLessonName());
		TextView textLocation = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_location)
				);
		textLocation.setText(lesson.getLessonWhere());
		TextView textProfessor = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_professor)
				);
		textProfessor.setText(lesson.getProfessor());
		TextView textScheduleNum = (
				(TextView)lessonView.findViewById(R.id.view_timetable_lessoninfo_schedulenum)
				);
		float lessonViewTextSize;
		switch(timetable.getDayNum()){
		case 5 :
			lessonViewTextSize = res.getDimension(R.dimen.fragment_timetable_lessonview_5days_textsize);
			break;
		case 6 : 
			lessonViewTextSize = res.getDimension(R.dimen.fragment_timetable_lessonview_6days_textsize);
			break;
		case 7 :
			lessonViewTextSize = res.getDimension(R.dimen.fragment_timetable_lessonview_7days_textsize);
			break;
		default :
			lessonViewTextSize = 8;
			break;
		}
		textSubject.setTextSize(TypedValue.COMPLEX_UNIT_PX, lessonViewTextSize);
		textLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX, lessonViewTextSize);
		textProfessor.setTextSize(TypedValue.COMPLEX_UNIT_PX, lessonViewTextSize);

		if(timetable == TimetableDataManager.getMainTimetable()){
			HashMap<String, ArrayList<Schedule>> scheduleMap = TimetableDataManager.getSchedules();

			Collection<ArrayList<Schedule>> scheduleCollection = scheduleMap.values();

			int scheduleNum = 0;
			for(ArrayList<Schedule> scheduleList : scheduleCollection){
				for(int i = 0; i < scheduleList.size() ; i++){
					Schedule s = scheduleList.get(i);
					if(s.getParentLesson() == lesson){
						scheduleNum++;
					}
				}
			}

			if(scheduleNum == 0){
				textScheduleNum.setVisibility(View.GONE);
			}else{
				textScheduleNum.setText(scheduleNum+"");
			}
		}else{
			textScheduleNum.setVisibility(View.GONE);
		}

		//Log.e("dialog", "editSubject : " + editSubject.getText());
		if(lesson.getLessonName().equals("")){						
			textSubject.setVisibility(View.GONE);
		}else {
			textSubject.setVisibility(View.VISIBLE);
		}

		if(lesson.getLessonWhere().equals("")){
			textLocation.setVisibility(View.GONE);
		}else{
			textLocation.setVisibility(View.VISIBLE);
		}

		if(lesson.getProfessor().equals("")){
			textProfessor.setVisibility(View.GONE);
		}else{
			textProfessor.setVisibility(View.VISIBLE);
		}

		//Resources res = getResources();

		int[] wh = getLessonViewWHFromLesson(lesson);
		if(wh == null){
			return null;
		}
		//markSelectedRangeLayout.setBackgroundColor(Color.CYAN);
		FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT, 
						android.view.ViewGroup.LayoutParams.FILL_PARENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		//params.width = (int) cellWidth * (endD - startD + 1);
		//params.height = (int) (cellHeight * (endP - startP));
		params.width = wh[0];
		params.height = wh[1];



		/*FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.width = (int) cellWidth;
		params.height = (int) (cellHeight * (lesson.getPeriodLengthByFloat()));
		Log.e("params.height", params.height + 
				", end : " + lesson.getLessonEndPeriodByFloat() + 
				", start : " + lesson.getLessonStartPeriodByFloat()
				);*/


		/*//만약 타임라인부분이 터치됬으면 return -1
		float timelineOffset =  res.getDimension(R.dimen.fragment_timetable_timeline_width)
				+ res.getDimension(R.dimen.fragment_timetable_body_padding)
				+ res.getDimension(R.dimen.fragment_timetable_cell_margin);
		int left = (int) (timelineOffset + dayIdx * cellWidth);

		MyLog.d("LessonView Left", left+"");
		params.leftMargin = left;
		params.topMargin = (int) ( ( lesson.getLessonStartPeriodByFloat()) * cellHeight );*/

		int[] tl = getLessonViewTLMarginFromLesson(lesson);
		if(tl == null){
			return null;
		}
		params.topMargin = tl[0];
		params.leftMargin = tl[1];
		//params.topMargin = (int) ( (startP) * cellHeight );

		lessonView.setLayoutParams(params);

		ytTheme.getLessonViewBackgroundShape().setColor(lesson.getColor());
		ytTheme.getLessonViewBackgroundShape().setViewTheme(
				getSupportActivity(), lessonView);

		lessonView.setOnClickListener(lessonOnClick);
		lessonView.setOnLongClickListener(lessonViewOnLongClickListener);
		//lessonView.setOnTouchListener(lessonViewOnTouchListener);

		lessonView.setTag(lesson);
		return lessonView;
	}

	public Bitmap getTimetableShareImageBitmap(){
		int height = timetableHead.getHeight()+ 
				tableScrollChild.getHeight();
		int width = timetableHead.getWidth();
		MyLog.d("ShareAction", "height : " + height + ", width : " + width);

		timetableHead.setDrawingCacheEnabled(true);
		timetableHead.buildDrawingCache();
		tableScrollChild.setDrawingCacheEnabled(true);
		tableScrollChild.buildDrawingCache();

		Bitmap b = Bitmap.createBitmap(
				width,
				height, 
				Bitmap.Config.ARGB_8888);
		//		rootView.layout(0, 0, rootView.getLayoutParams().width, rootView.getLayoutParams().height);
		Canvas c = new Canvas(b);
		c.drawColor(Color.WHITE);
		timetableHead.draw(c);
		c.translate(0, timetableHead.getHeight());
		tableScrollChild.draw(c);

		timetableHead.setDrawingCacheEnabled(false);
		timetableHead.destroyDrawingCache();
		tableScrollChild.setDrawingCacheEnabled(false);
		tableScrollChild.destroyDrawingCache();
		return b;
	}
}