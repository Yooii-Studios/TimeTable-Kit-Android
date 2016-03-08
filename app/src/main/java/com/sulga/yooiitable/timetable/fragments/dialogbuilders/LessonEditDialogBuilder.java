package com.sulga.yooiitable.timetable.fragments.dialogbuilders;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.FixedSizes;
import com.sulga.yooiitable.customviews.SelectOneItemLinearLayout;
import com.sulga.yooiitable.data.Lesson;
import com.sulga.yooiitable.data.PeriodInfo;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.theme.YTTimetableTheme;
import com.sulga.yooiitable.theme.parts.YTRoundRectThemePart;
import com.sulga.yooiitable.theme.parts.YTShapeRoundRectThemePart;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class LessonEditDialogBuilder{

	public LessonEditDialogBuilder(Context context, TimetableFragment parent){
		this.context = context;
		this.parent = parent;
	}
	private Context context;
	private TimetableFragment parent;
	private Timetable timetable;

	/*private static final String[] lessonColors = new String[]{
		"#527578",
		"#F1F0FF",
		"#C3C3E5",
		"#587058",
		"#FFFFBE",
		"#C0C0C0",
		"#7ABA7A",
		"#B76EB8",
		"#3D72A4",
		"#CC9933"
	};
	 */
	private static String[] lessonColors = null;

	//View dialogView;
	//Lesson lessonToAddAndEdit = null;
	private TextView textSubject;
	private EditText editSubject;
	private TextView textLocation;
	private EditText editLocation;
	private TextView textProfessor;
	private EditText editProfessor;
	private RelativeLayout pickStartTime;
	private TextView pickStartTimeText;
	private RelativeLayout pickEndTime;
	private TextView pickEndTimeText;
	private TextView textColor;
	private SelectOneItemLinearLayout editColor;
	private TextView textMemo;
	private EditText editMemo;

	private RelativeLayout colorPickBorder;
	//FrameLayout colorPickOverlapLayout;
	private Bundle args;
	private String tag;
	private Dialog dialog;

	Lesson resultLesson = null;
	
	Lesson lessonToEdit = null;
	
	private int startDay;
	private int endDay;
	
	public Dialog createDialog(
			Lesson lessonToEdit,
			Bundle args,
			final String tag			
			){
		this.tag = tag;
		this.args = args;
		
		Resources res = context.getResources();

		final LinearLayout dialogView = (LinearLayout) 
				View.inflate(context, R.layout.dialog_editlesson_normal, null);
		dialog =  new Dialog(context, R.style.TransparentDialog);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		dialog.getWindow().setLayout(
				(int)res.getDimension(R.dimen.dialog_editelsson_width), 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		float topOffset = res.getDimension(R.dimen.dialog_top_offset);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.y += FixedSizes.ACTIONBAR_HEIGHT + topOffset;
		wlp.gravity = Gravity.TOP;
		window.setAttributes(wlp);

        int pageIndex = args.getInt("TimetablePageIndex");
		timetable = parent.getTimetableDataFromManager(pageIndex);
		if(lessonToEdit != null){
			this.lessonToEdit = lessonToEdit;
			startDay = lessonToEdit.getDay();
			endDay = lessonToEdit.getDay();
		}else{
			startDay = args.getInt("StartDay");
			endDay = args.getInt("EndDay"); 
		}

		lessonColors = YTTimetableTheme.LESSON_COLORS_THEME_A;

		colorPickBorder = (RelativeLayout) dialogView.findViewById(R.id.dialog_editlesson_edit_color_border);
		new YTShapeRoundRectThemePart(12.0f, 255, Color.WHITE, 0, 0)
		.setViewTheme(context, colorPickBorder);
		//3.키보드 화면 표시상태 디텍팅하는 리스너 등록. parent에 키보드를 디텍트하는 리니어레이아웃이 존재함.
		//		SoftKeyboardDetectLinearLayout.OnSoftKeyboardStateChangedListener softKeyboardListener = 
		//				new SoftKeyboardDetectLinearLayout.OnSoftKeyboardStateChangedListener() {
		//			private YTRoundRectThemePart theme = 
		//					new YTShapeRoundRectThemePart(
		//							12.0f, 255, 
		//							Color.parseColor("#1f1f1f"), 
		//							0, 0);
		//			@Override
		//			public void onKeyboardHidden() {
		//				// TODO Auto-generated method stub
		//				MyLog.d("dialog_c", "Keyboard Hidden!!");
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_subject).setBackgroundColor(Color.TRANSPARENT);
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_location).setBackgroundColor(Color.TRANSPARENT);
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_professor).setBackgroundColor(Color.TRANSPARENT);
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_memo).setBackgroundColor(Color.TRANSPARENT);
		//			}
		//			@Override
		//			public void onKeyboardAppear() {
		//				// TODO Auto-generated method stub
		//				MyLog.d("dialog_c", "Keyboard Not Hidden!!");
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_subject).setBackgroundColor(Color.WHITE);
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_location).setBackgroundColor(Color.WHITE);
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_professor).setBackgroundColor(Color.WHITE);
		//				dialogView.findViewById(R.id.dialog_editlesson_edit_memo).setBackgroundColor(Color.WHITE);
		//			}
		//		};
		//
		//		parent.softKeyboardDetectLayout.setOnSoftKeyboardStateChangedListener(softKeyboardListener);

		initViews(lessonToEdit, dialogView);
		initViewBackground();

		View rootView = dialogView.findViewById(R.id.dialog_editlesson_root);
		rootView.requestFocus();
		new YTShapeRoundRectThemePart(12.0f, 
				(int)(255f * 0.85f),
				Color.parseColor("#404040"), 
				Color.parseColor("#d2d0c6"), 3)
		.setViewTheme(context, rootView);

		//루트 레이아웃에 포커스가 감으로서 다이얼로그가 처음 띄워졌을때 가상 키보드/하드 키보드 모두 작동하지 않는 것을 확실히 해둘 수 있다.

		//3.텍스트에디터 위젯 초기화 및 Lesson Edit상태라면 lesson데이터를 읽어와 텍스트에디터에 미리 채워준다.
		//final Spinner editDaySpinner = (Spinner) dialogView.findViewById(R.id.dialog_editlesson_spinner_editday);
		//setEditDaySpinner(editDaySpinner);


		for(int i = 0; i < lessonColors.length ; i++){
			addColorPickButton(editColor, lessonColors[i]);
		}

		editColor.setOnOneItemSelectedListener(new SelectOneItemLinearLayout.OnOneItemSelectedListener() {

			@Override
			public void onSelected(View v) {
				// TODO Auto-generated method stub
				FrameLayout.LayoutParams cpbParam = (LayoutParams) 
						colorPickBorder.getLayoutParams();
				cpbParam.leftMargin = v.getLeft() 
						- ( (colorPickBorder.getWidth() - v.getWidth()) / 2 );
				colorPickBorder.setLayoutParams(cpbParam);

				colorPickBorder.setVisibility(View.VISIBLE);

				//				lessonToEdit.setColor((Integer) v.getTag());
				//				
				MyLog.d("onSelected", "ColorPickBorder left : " + v.getLeft());
			}
		});

		if(lessonToEdit != null){
			((EditText)dialogView.findViewById(R.id.dialog_editlesson_edit_subject)).setText(lessonToEdit.getLessonName());
			((EditText)dialogView.findViewById(R.id.dialog_editlesson_edit_location)).setText(lessonToEdit.getLessonWhere());
			((EditText)dialogView.findViewById(R.id.dialog_editlesson_edit_professor)).setText(lessonToEdit.getProfessor());
			((EditText)dialogView.findViewById(R.id.dialog_editlesson_edit_memo)).setText(lessonToEdit.getLessonMemo());
		}

		//4.다이얼로그에서 OK/Cancel 버튼 등록 및 클릭리스너 등록.

		ImageButton buttonDialogOK = (ImageButton) dialogView.findViewById(R.id.dialog_editlesson_button_check);
		buttonDialogOK.setOnClickListener(
				new ButtonDialogOKOnClick(lessonToEdit)
				);

		ImageButton buttonDialogCancel = (ImageButton) dialogView.findViewById(R.id.dialog_editlesson_button_cancel);
		buttonDialogCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//lessonToAddAndEdit = null;
				int targetRequestCode = -1;
				if(tag.equals("Edit")){
					targetRequestCode = TimetableFragment.FRAG_TIMETABLE_DIALOG_EDIT_LESSON;
				}else if(tag.equals("Add")){
					targetRequestCode = TimetableFragment.FRAG_TIMETABLE_DIALOG_ADD_LESSON;
				}
				parent.onActivityResult(
						targetRequestCode,
						android.app.Activity.RESULT_CANCELED, 
						null
						);
				dialog.dismiss();
			}
		});
		return dialog;
	}

	private void initViews(Lesson lessonToEdit, ViewGroup dialogView){
		textSubject = (TextView) dialogView.findViewById(R.id.dialog_editlesson_textview_subject);
		editSubject = (EditText) dialogView.findViewById(R.id.dialog_editlesson_edit_subject);
		editSubject.setText("");
		textLocation = (TextView) dialogView.findViewById(R.id.dialog_editlesson_textview_location);
		editLocation = (EditText) dialogView.findViewById(R.id.dialog_editlesson_edit_location);
		editLocation.setText("");
		textProfessor = (TextView) dialogView.findViewById(R.id.dialog_editlesson_textview_professor);
		editProfessor = (EditText) dialogView.findViewById(R.id.dialog_editlesson_edit_professor);
		editProfessor.setText("");

		int startHour, startMin, endHour, endMin = 0;
		if(lessonToEdit == null){
			float startPeriod = args.getFloat("StartPeriod");
			float endPeriod = args.getFloat("EndPeriod");
			startHour = timetable.getStartHourOfPeriod(startPeriod);
			startMin = timetable.getStartMinOfPeriod(startPeriod);
			endHour = timetable.getEndHourOfPeriod(endPeriod);
			endMin = timetable.getEndMinOfPeriod(endPeriod);
		}else{
			startHour = lessonToEdit.getStartHour();
			startMin = lessonToEdit.getStartMin();
			endHour = lessonToEdit.getEndHour();
			endMin = lessonToEdit.getEndMin();
		}

		pickStartTime = (RelativeLayout) dialogView.findViewById(R.id.dialog_editlesson_button_editstarttime);
		pickStartTime.setOnClickListener(
				new OnStartTimeClickListener(startHour, startMin, endHour, endMin));
		pickStartTimeText = (TextView) dialogView.findViewById(R.id.dialog_editlesson_editstarttime_text);
		pickStartTimeText.setText(getTimeTextFrom(startHour, startMin));
		pickEndTime = (RelativeLayout) dialogView.findViewById(R.id.dialog_editlesson_button_editendtime);
		pickEndTime.setOnClickListener(
				new OnEndTimeClickListener(startHour, startMin, endHour, endMin));
		pickEndTimeText = (TextView) dialogView.findViewById(R.id.dialog_editlesson_editendtime_text);
		pickEndTimeText.setText(getTimeTextFrom(endHour, endMin));

		textMemo = (TextView) dialogView.findViewById(R.id.dialog_editlesson_textview_memo);
		editMemo = (EditText) dialogView.findViewById(R.id.dialog_editlesson_edit_memo);
		editMemo.setText("");
		textColor = (TextView) dialogView.findViewById(R.id.dialog_editlesson_textview_color);
		editColor = 
				(SelectOneItemLinearLayout) dialogView.findViewById(R.id.dialog_editlesson_edit_color);
	}

	private String getTimeTextFrom(int hour, int min){
		Calendar startC = GregorianCalendar.getInstance();
		startC.set(GregorianCalendar.HOUR_OF_DAY, hour);
		startC.set(GregorianCalendar.MINUTE, min);
		Date d = startC.getTime();
		SimpleDateFormat startSdfTime = new SimpleDateFormat("HH : mm");
		return startSdfTime.format(d);
	}

	private void initViewBackground(){
		YTRoundRectThemePart backPart = new YTShapeRoundRectThemePart(
				12.0f, 
				255, Color.parseColor("#1f1f1f"), 
				0, 0);
		backPart.setViewTheme(context, textSubject);
		backPart.setViewTheme(context, editSubject);
		backPart.setViewTheme(context, textLocation);
		backPart.setViewTheme(context, editLocation);
		backPart.setViewTheme(context, textProfessor);
		backPart.setViewTheme(context, editProfessor);
		backPart.setViewTheme(context, textColor);
		backPart.setViewTheme(context, textMemo);
		backPart.setViewTheme(context, editMemo);

	}

	public boolean checkLessonCollide(Lesson A, Lesson comparator){
		PeriodInfo piA = A.getLessonInfo();
		PeriodInfo piB = comparator.getLessonInfo();

		if(piA.getDay() != piB.getDay()){
			return false;
		}

		int aStart = piA.getStartHour() * 60 + piA.getStartMin();
		int aEnd = piA.getEndHour() * 60 + piA.getStartMin();
		int bStart = piB.getStartHour() * 60 + piB.getStartMin();
		int bEnd = piB.getEndHour() * 60 + piB.getEndMin();

		//		if( ( aStart < bStart && aEnd < bStart ) ||
		//				( aStart > bEnd && aEnd > bEnd ) ||
		//				( bStart < aStart && bEnd < aEnd) ||
		//				( bStart > aEnd && bEnd > aEnd)){
		//			return false;
		//		}
		int resA = ( aStart - bStart) * ( aStart - bEnd);
		int resB = ( aEnd - bStart ) * ( aEnd - bEnd);
		if(  ( resA <= 0 )	|| ( resB <= 0 ) ){
			return true;
		}else{
			return false;
		}
	}

	private void addColorPickButton(SelectOneItemLinearLayout parent, String color){
		LayoutInflater inflater = this.parent.getActivity().getLayoutInflater();

		ToggleButton tb = (ToggleButton) 
				inflater.inflate(
						R.layout.view_lessonview_colorpick_togglebutton, 
						parent, 
						false
						);
		YTRoundRectThemePart themePart = new YTShapeRoundRectThemePart(
				12.0f,
				255, Color.parseColor(color),
				0, 0
				);
		int margins = (int) context.getResources()
				.getDimension(R.dimen.dialog_editlesson_colorpick_buttons_margins);
		LinearLayout.LayoutParams lp = 
				(android.widget.LinearLayout.LayoutParams) tb.getLayoutParams();
		lp.setMargins(margins, margins, margins, margins);
		//		tb.setBackgroundColor(Color.parseColor(color));
		themePart.setViewTheme(context, tb);
		parent.addToggleButton("", color, tb);

	}

	private class ButtonDialogOKOnClick implements View.OnClickListener {
		private Lesson lessonToEdit;
		public ButtonDialogOKOnClick(Lesson lessonToEdit){
			this.lessonToEdit = lessonToEdit;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int targetRequestCode = -1;
			if(tag.equals("Edit")){
				targetRequestCode = TimetableFragment.FRAG_TIMETABLE_DIALOG_EDIT_LESSON;
				editLesson(lessonToEdit);
			}else if(tag.equals("Add")){
				targetRequestCode = TimetableFragment.FRAG_TIMETABLE_DIALOG_ADD_LESSON;
				float startPeriod = args.getFloat("StartPeriod");
				float endPeriod = args.getFloat("EndPeriod");

				addLesson(startDay, endDay, startPeriod, endPeriod);
			}

			//			int lessonToAddAndEditIdx = timetable.getLessonList().indexOf(resultLesson);
			//			if(lessonToAddAndEditIdx != -1){
			//				Log.e("dialog", 
			//						"index of lesson : " + lessonToAddAndEditIdx 
			//						+ ", timetable lesson length : " + timetable.getLessonList().size());
			//			}else
			//				timetable.addLesson(resultLesson);

			parent.onActivityResult(
					targetRequestCode, 
					android.app.Activity.RESULT_OK
					);

			//만약 현재 Timetable의 뷰페이저에 보이고 있는 페이지가 메인타임테이블이면 
			//스케쥴 프래그먼트를 받아와 refresh
			//				if(pparent.getViewPager().getCurrentItem() == pparent.getViewPager().getAdapter().getCount() - 2)
			//					((ScheduleFragment)pparent.getPagerAdapter().getItem(pparent.getPagerAdapter().getCount() - 1)).refresh();

			dialog.dismiss();
		}
	};

	private void editLesson(Lesson lesson){
		lesson.setLessonName(editSubject.getText().toString());
		lesson.setLessonWhere(editLocation.getText().toString());
		lesson.setProfessor(editProfessor.getText().toString());
		lesson.setLessonMemo(editMemo.getText().toString());
		if(editColor.getSelectedItem() != null){
			lesson.setColor(Color.parseColor((String) editColor.getSelectedItem()));
		}

		int startHour = lesson.getStartHour();
		int startMin = lesson.getStartMin();
		int endHour = lesson.getEndHour();
		int endMin = lesson.getEndMin();
		if(editedStartHour != -1){
			startHour = editedStartHour;
			editedStartHour = -1;
		}
		if(editedStartMin != -1){
			startMin = editedStartMin;
			editedStartMin = -1;
		}
		if(editedEndHour != -1){
			endHour = editedEndHour;
			editedEndHour = -1;
		}
		if(editedEndMin != -1){
			endMin = editedEndMin;
			editedEndMin = -1;
		}
		lesson.setPeriodInfo(
				new PeriodInfo(
						lesson.getDay(),
						startHour,
						startMin,
						endHour,
						endMin
						)
				);
	}

	private void addLesson(int startDay, int endDay, float startPeriod, float endPeriod){
		String selectedColor;
		if(editColor.getSelectedItem() != null){
			Log.d("LessonColor", "editColor selected item exists");
			selectedColor = (String) editColor.getSelectedItem();
			//			lesson.setColor(Color.parseColor(selectedColor));
		}else{
			Log.d("LessonColor", "editColor selected item not exists");
			//			if(lesson.getColor() == Lesson.LESSON_COLOR_NONE){
			Log.d("LessonColor", "NONE");
			Random rand = new Random();
			//				String[] themeColors = timetable.getTheme().getLessonColors();
			int randomColorIdx = rand.nextInt(lessonColors.length);
			selectedColor = lessonColors[randomColorIdx];
			//				lesson.setColor(Color.parseColor(lessonColors[randomColorIdx]));
			//			}
		}
		int startHour = timetable.getStartHourOfPeriod(startPeriod);
		int startMin = timetable.getStartMinOfPeriod(startPeriod);
		int endHour = timetable.getEndHourOfPeriod(endPeriod);
		int endMin = timetable.getEndMinOfPeriod(endPeriod);
		if(editedStartHour != -1){
			startHour = editedStartHour;
			editedStartHour = -1;
		}
		if(editedStartMin != -1){
			startMin = editedStartMin;
			editedStartMin = -1;
		}
		if(editedEndHour != -1){
			endHour = editedEndHour;
			editedEndHour = -1;
		}
		if(editedEndMin != -1){
			endMin = editedEndMin;
			editedEndMin = -1;
		}

		int startDayIndex = timetable.getDayIndexFromGregorianCalendarDay(startDay);
		int endDayIndex = timetable.getDayIndexFromGregorianCalendarDay(endDay);
		for(int i = startDayIndex ; i <= endDayIndex ; i++){
			Lesson lesson = new Lesson(timetable);
			lesson.setPeriodInfo(
					new PeriodInfo(
							timetable.getGregorianCalendarDayFromDayIndex(i),
							startHour,
							startMin,
							endHour,
							endMin
							)
					);
			lesson.setColor(Color.parseColor(selectedColor));

			lesson.setLessonName(editSubject.getText().toString());
			lesson.setLessonWhere(editLocation.getText().toString());
			lesson.setProfessor(editProfessor.getText().toString());
			lesson.setLessonMemo(editMemo.getText().toString());

			timetable.addLesson(lesson);
		}
	}

	private boolean checkLessonCollision(Timetable timetable, int dayIndex, float editedStartPeriod, float editedEndPeriod){
		ArrayList<Lesson> lessonList = timetable.getLessonList();
		for(Lesson l : lessonList){
			if(l == lessonToEdit){
				continue;
			}
			
			boolean isCollide = Lesson.checkLessonCollideWith(
					timetable, timetable.getDayIndexFromGregorianCalendarDay(l.getDay()), l.getLessonStartPeriodByFloat(), l.getLessonEndPeriodByFloat(), 
					timetable, dayIndex, editedStartPeriod, editedEndPeriod);
			if(isCollide == true){
				return true;
			}
		}
		return false;
	}

	private class OnStartTimeClickListener implements View.OnClickListener{
		private int startHour, startMin, endHour, endMin = 0;
		public OnStartTimeClickListener(int startHour, int startMin, int endHour, int endMin){
			this.startHour = startHour;
			this.startMin = startMin;
			this.endHour = endHour;
			this.endMin = endMin;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();

			c.set(Calendar.HOUR_OF_DAY, startHour);
			c.set(Calendar.MINUTE, startMin);
			if(editedStartHour != -1){
				c.set(Calendar.HOUR_OF_DAY, editedStartHour);
			}
			if(editedStartMin != -1){
				c.set(Calendar.MINUTE, editedStartMin);
			}
			if(editedEndHour != -1){
				endHour = editedEndHour;
			}
			if(editedEndMin != -1){
				endMin = editedEndMin;
			}

			TimePickerDialog timeDialog = new TimePickerDialog(context,
					new OnStartTimeSetListener(endHour, endMin), 
					c.get(Calendar.HOUR_OF_DAY),
					c.get(Calendar.MINUTE), 
					true);
			timeDialog.show();
		}
	}

	private int editedStartHour = -1;
	private int editedStartMin = -1;
	private class OnStartTimeSetListener implements TimePickerDialog.OnTimeSetListener { 
		private int currEndHour, currEndMin = 0;
		public OnStartTimeSetListener(int currEndHour, int currEndMin){
			this.currEndHour = currEndHour;
			this.currEndMin = currEndMin;
		}
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			MyLog.d("LessonEditDialog", "StartTimeSet called");	
			//schedule.setScheduleHour(hourOfDay);
			//schedule.setScheduleMin(minute);

			//끝나는 시간이 시작시간보다 이를때.
			float startPeriod = timetable.getPeriodByFloatFromTime(hourOfDay, minute);
			float endPeriod = timetable.getPeriodByFloatFromTime(currEndHour, currEndMin);
			float halfHourByPeriod = 30.0f / (float)timetable.getPeriodUnit();
			MyLog.d("LessonEditDialogBuilder", "startPeriod : " + startPeriod 
					+ ", endPeriod : " + endPeriod 
					+ ", halfHourByPeriod : " + halfHourByPeriod);
			if( (endPeriod - startPeriod) < 0 ||
					endPeriod > timetable.getPeriodNum() ||
					startPeriod > ((float)timetable.getPeriodNum())){
				String warn = context.getString(R.string.dialog_editlesson_warn_timewrong);
				Toast.makeText(context, warn, Toast.LENGTH_LONG).show();
				return;
			}
			if(startPeriod > ((float)timetable.getPeriodNum() - halfHourByPeriod) ||
					(endPeriod - startPeriod) < halfHourByPeriod){
				String warn = context.getString(R.string.dialog_editlesson_warn_lessonUnder30Min);
				Toast.makeText(context, warn, Toast.LENGTH_LONG)
				.show();
				return;
			}

			//수업시간 간에 충돌 검사
			int startDayIndex = timetable.getDayIndexFromGregorianCalendarDay(startDay);
			int endDayIndex = timetable.getDayIndexFromGregorianCalendarDay(endDay);

			for(int i = startDayIndex ; i <= endDayIndex ; i++){
				if(checkLessonCollision(timetable, i, startPeriod, endPeriod) == true){
					String warn = context.getString(R.string.fragment_timetable_warning_lessonAlreadyExists);
					Toast.makeText(
							context, 
							warn, 
							Toast.LENGTH_LONG
							).show();
					return;
				}
			}
			editedStartHour = hourOfDay;
			editedStartMin = minute;

			SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
			GregorianCalendar c = new GregorianCalendar();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);
			String time = sdf.format(c.getTime());
			pickStartTimeText.setText(time);
		}
	}


	private class OnEndTimeClickListener implements View.OnClickListener{
		private int startHour, startMin, endHour, endMin = 0;
		public OnEndTimeClickListener(int startHour, int startMin, int endHour, int endMin){
			this.startHour = startHour;
			this.startMin = startMin;
			this.endHour = endHour;
			this.endMin = endMin;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();

			c.set(Calendar.HOUR_OF_DAY, endHour);
			c.set(Calendar.MINUTE, endMin);
			if(editedEndHour != -1){
				c.set(Calendar.HOUR_OF_DAY, editedEndHour);
			}
			if(editedEndMin != -1){
				c.set(Calendar.MINUTE, editedEndMin);
			}
			if(editedStartHour != -1){
				startHour = editedStartHour;
			}
			if(editedStartMin != -1){
				startMin = editedStartMin;
			}

			TimePickerDialog timeDialog = new TimePickerDialog(context,
					new OnEndTimeSetListener(startHour, startMin), 
					c.get(Calendar.HOUR_OF_DAY),
					c.get(Calendar.MINUTE), 
					true);
			timeDialog.show();
		}
	}

	private int editedEndHour = -1;
	private int editedEndMin = -1;
	private class OnEndTimeSetListener implements TimePickerDialog.OnTimeSetListener { 
		private int currStartHour, currStartMin = 0;
		public OnEndTimeSetListener(int currStartHour, int currStartMin){
			this.currStartHour = currStartHour;
			this.currStartMin = currStartMin;
		}		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			MyLog.d("LessonEditDialog", "EndTimeSet called");	
			//schedule.setScheduleHour(hourOfDay);
			//schedule.setScheduleMin(minute);

			//끝나는 시간이 시작시간보다 이를때.
			float endPeriod = timetable.getPeriodByFloatFromTime(hourOfDay, minute);
			float startPeriod = timetable.getPeriodByFloatFromTime(currStartHour, currStartMin);
			float halfHourByPeriod = 30.0f / (float)timetable.getPeriodUnit();
			MyLog.d("LessonEditDialogBuilder", "startPeriod : " + startPeriod 
					+ ", endPeriod : " + endPeriod 
					+ ", halfHourByPeriod : " + halfHourByPeriod);
			if( (endPeriod - startPeriod) < 0 ||
					endPeriod > timetable.getPeriodNum() ||
					startPeriod > ((float)timetable.getPeriodNum())){
				String warn = context.getString(R.string.dialog_editlesson_warn_timewrong);
				Toast.makeText(context, warn, Toast.LENGTH_LONG).show();
				return;
			}
			if(startPeriod > ((float)timetable.getPeriodNum() - halfHourByPeriod) ||
					(endPeriod - startPeriod) < halfHourByPeriod){
				String warn = context.getString(R.string.dialog_editlesson_warn_lessonUnder30Min);
				Toast.makeText(context, warn, Toast.LENGTH_LONG)
				.show();
				return;
			}
			//수업시간 간에 충돌 검사
			int startDayIndex = timetable.getDayIndexFromGregorianCalendarDay(startDay);
			int endDayIndex = timetable.getDayIndexFromGregorianCalendarDay(endDay);
			for(int i = startDayIndex ; i <= endDayIndex ; i++){
				if(checkLessonCollision(timetable, i, startPeriod, endPeriod) == true){
					String warn = context.getString(R.string.fragment_timetable_warning_lessonAlreadyExists);
					Toast.makeText(
							context, 
							warn, 
							Toast.LENGTH_LONG
							).show();
					return;
				}
			}
			editedEndHour = hourOfDay;
			editedEndMin = minute;

			SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
			GregorianCalendar c = new GregorianCalendar();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);
			String time = sdf.format(c.getTime());

			pickEndTimeText.setText(time);
		}
	};


}


