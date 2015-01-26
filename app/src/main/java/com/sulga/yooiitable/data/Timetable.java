package com.sulga.yooiitable.data;

import java.io.*;
import java.util.*;

import android.content.*;
import android.os.*;

import com.sulga.yooiitable.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.theme.*;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;

public class Timetable implements Parcelable, Serializable{

//	private static final long serialVersionUID = -5816402116655372631L;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private static final int INTERNAL_VERSION_ID = 1;
    //Start Minute Added!
    private static final int INTERNAL_VERSION_ID = 2;
//	private static final long serialVersionUID = 6740432258038563321L;
	//private static final long serialVersionUID = -2716683761884537753L;
	//private static final long serialVersionUID = -3139495191579339656L;
	//private static final long serialVersionUID = 1L;
//	public static final int MON = GregorianCalendar.MONDAY;
//	public static final int TUE = GregorianCalendar.TUESDAY;
//	public static final int WED = GregorianCalendar.WEDNESDAY;
//	public static final int THU = GregorianCalendar.THURSDAY;
//	public static final int FRI = GregorianCalendar.FRIDAY;
//	public static final int SAT = GregorianCalendar.SATURDAY;
//	public static final int SUN = GregorianCalendar.SUNDAY;

	public static enum ColumnTypes{
		BY_PERIOD,			//1교시, 2교시, 3교시...
		BY_TIME,			//9:00-10:00 ....
		BY_ALPHABET			//A - B - C....
	}

	public static final ColumnTypes DEFAULT_COLUMN_TYPE = ColumnTypes.BY_TIME;
	public static final int DEFAULT_START_TIME = 9;
	public static final int DEFAULT_PERIOD_NUM = 8;
	public static final int DEFAULT_PERIOD_UNIT = 60;
	public static final YTTimetableTheme.ThemeType DEFAULT_THEME_TYPE = 
			YTTimetableTheme.ThemeType.Icarus; 
	//public static final boolean DEFAULT_LESSON_ALARM = false;
	public static final int LESSON_ALARM_NONE = -1;
	//private int dayNum;
	
	private YTTimetableTheme.ThemeType themeType;
	
	private String title = "No Title";
	/**
	 * Gregorian day
	 */
	private int startDay;
	/**
	 * Gregorian day
	 */
	private int endDay;
	private ColumnTypes columnType;
	private int startTime;
    private int startMin;
	private int periodNum;		//교시 수
	private int periodUnit;		//1교시당 배분 시간
	//private boolean islessonAlarmExists;
	private int lessonAlarmTime = -1;

	private ArrayList<Lesson> lessonList;
	//private TimetableOption timetableOption;

	private long id;
//	private String uuid;
	/**
	 * Bitmap for Sharing. Should be set null after downloading timetable.
	 */
	private byte[] bmpForShare;

	private Timetable(){
		lessonList = new ArrayList<Lesson>();
		id = System.currentTimeMillis();
//		uuid = Installation.id(context)
		//MyLog.d("Timetable", "id : " + id);
	}

	public Timetable(int startDay, int endDay, 
			ColumnTypes columnType, int startHour, int periodNum, int periodUnit, int lessonAlarmTime){
		//this.setDayNum(dayNum);
		this.startDay = startDay;
		this.endDay = endDay;
		this.columnType = columnType;
		this.startTime = startHour;
		this.setPeriodNum(periodNum);
		this.setPeriodUnit(periodUnit);
		//this.islessonAlarmExists = lessonAlarm;
		this.lessonAlarmTime = lessonAlarmTime;
		setLessonList(new ArrayList<Lesson>());
//		theme = new YTTimetableTheme();
		themeType = DEFAULT_THEME_TYPE;
		id = System.currentTimeMillis() + startDay + endDay + periodNum;
		MyLog.d("Timetable", "id : " + id);
		//timetableOption = new TimetableOption();
	}

    public Timetable(int startDay, int endDay,
                     ColumnTypes columnType, int startHour, int startMin, int periodNum, int periodUnit, int lessonAlarmTime){
        //this.setDayNum(dayNum);
        this.startDay = startDay;
        this.endDay = endDay;
        this.columnType = columnType;
        this.startTime = startHour;
        this.startMin = startMin;
        this.setPeriodNum(periodNum);
        this.setPeriodUnit(periodUnit);
        //this.islessonAlarmExists = lessonAlarm;
        this.lessonAlarmTime = lessonAlarmTime;
        setLessonList(new ArrayList<Lesson>());
//		theme = new YTTimetableTheme();
        themeType = DEFAULT_THEME_TYPE;
        id = System.currentTimeMillis() + startDay + endDay + periodNum;
        MyLog.d("Timetable", "id : " + id);
        //timetableOption = new TimetableOption();
    }


    public Timetable(int startDay, int endDay, int periodNum){
		this.startDay = startDay;
		this.endDay = endDay;
		this.columnType = DEFAULT_COLUMN_TYPE;
		this.startTime = DEFAULT_START_TIME;
		this.setPeriodNum(DEFAULT_PERIOD_NUM);
		this.setPeriodUnit(DEFAULT_PERIOD_UNIT);
		//this.islessonAlarmExists = DEFAULT_LESSON_ALARM;
		this.lessonAlarmTime = LESSON_ALARM_NONE;
		setLessonList(new ArrayList<Lesson>());
//		theme = new YTTimetableTheme();
		themeType = DEFAULT_THEME_TYPE;
		id = System.currentTimeMillis() + startDay + endDay + periodNum;
		MyLog.d("Timetable", "id : " + id);
	}

	public Timetable(int startDay, int endDay, int periodNum, int periodUnit, ArrayList<Lesson> lessonList){
		//this.setDayNum(dayNum);
		//		this.setPeriodNum(periodNum);
		//		this.setPeriodUnit(periodUnit);
		//		this.setLessonList(lessonList);
		//timetableOption = new TimetableOption();
		this.startDay = startDay;
		this.endDay = endDay;
		this.columnType = DEFAULT_COLUMN_TYPE;
		this.startTime = DEFAULT_START_TIME;
        this.startMin = 0;
		this.setPeriodNum(periodNum);
		this.setPeriodUnit(periodUnit);
		//this.islessonAlarmExists = DEFAULT_LESSON_ALARM;
		this.lessonAlarmTime = LESSON_ALARM_NONE;
		setLessonList(new ArrayList<Lesson>());
//		theme = new YTTimetableTheme();
		themeType = DEFAULT_THEME_TYPE;
		id = System.currentTimeMillis() + startDay + endDay + periodNum;
		MyLog.d("Timetable", "id : " + id);
	}

	public Timetable(Parcel source){
		this();
		//dayNum = source.readInt();
		title = source.readString();
		startDay = source.readInt();
		endDay = source.readInt();
		try {
			columnType = ColumnTypes.valueOf(source.readString());
		} catch (IllegalArgumentException x) {
			columnType = null;
		}
		startTime = source.readInt();
        startMin = source.readInt();
		periodNum = source.readInt();
		periodUnit = source.readInt();
		//islessonAlarmExists = (source.readInt() == 1) ? true : false;
		lessonAlarmTime = source.readInt();
		source.readTypedList(lessonList, Lesson.CREATOR);
//		theme = YTTimetableTheme.class.cast(source.readValue(YTTimetableTheme.class.getClassLoader()));
		themeType = YTTimetableTheme.ThemeType.valueOf(source.readString());
		id = source.readLong();
		source.readByteArray(bmpForShare);

		MyLog.d("Timetable", "id : " + id);
		//timetableOption = source.readParcelable(TimetableOption.class.getClassLoader());
	}

	/**
	 * 파라미터로 들어온 period의 start Hour 반환.
	 * 24시가 넘으면 새벽 1시,2시 식으로 자동 변환하여 리턴.
	 * @param _startPeriod
	 * @return
	 */
	public int getStartHourOfPeriod(float _startPeriod){
		//하루를 시간이 아닌 분으로 치환한후에 계산하는게 훨 나은듯...

		int periodStartHour = (int) (( startTime * 60 + startMin + ( periodUnit * ( _startPeriod ) ) )
				/ 60);
		return periodStartHour % 24;
	}
	/**
	 * 파라미터로 들어온 period의 start Min 반환.
	 * @param _startPeriod
	 * @return
	 */
	public int getStartMinOfPeriod(float _startPeriod){
		int periodStartMin = (int) (( startTime * 60 + startMin + ( periodUnit * ( _startPeriod ) ) )
				% 60);
		return periodStartMin;
	}
	/**
	 * 파라미터로 들어온 period의 end Hour 반환.
	 * 24시가 넘으면 새벽 1시,2시 식으로 자동 변환하여 리턴.
	 * @param _endPeriod
	 * @return
	 */
	public int getEndHourOfPeriod(float _endPeriod){
		int periodEndHour = (int) (( startTime * 60 + startMin + ( periodUnit * _endPeriod ) )
				/ 60);
		return periodEndHour % 24;
	}
	/**
	 * 파라미터로 들어온 period의 end Min 반환.
	 * @param _endPeriod
	 * @return
	 */
	public int getEndMinOfPeriod(float _endPeriod){
		int periodEndMin = (int) (( startTime * 60 + startMin + ( periodUnit * _endPeriod ) )
				% 60);
		return periodEndMin;
	}
	
	public float getPeriodByFloatFromTime(int hour, int min){
		int timetableStartByMin = getStartTimeByMin();
		int inputTimeByMin = hour * 60 + min;

		if(inputTimeByMin < timetableStartByMin){
			inputTimeByMin += 60 * 24;
		}
		MyLog.d("getPeriodByFloatFromTime", "timetableStartByMin : " + timetableStartByMin +", inputTimeByMin : " + inputTimeByMin);

		int offset = inputTimeByMin - timetableStartByMin;

		float result = (float)offset / (float)getPeriodUnit();
		MyLog.d("Timetable", "getPeriodByFloatFromTime : " + result);
		return result;
		
	}

	public int getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(int periodNum) {
		this.periodNum = periodNum;
	}

	public int getPeriodUnit() {
		return periodUnit;
	}

	public void setPeriodUnit(int periodUnit) {
		this.periodUnit = periodUnit;
	}

	public ArrayList<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(ArrayList<Lesson> lessonList) {
		this.lessonList = lessonList;
	}

	public boolean addLesson(Lesson lesson){
		if(lesson == null)
			return false;

		lessonList.add(lesson);
		return true;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		//dest.writeInt(dayNum);
		//		dest.writeInt(periodNum);
		//		dest.writeInt(periodUnit);
		//		dest.writeTypedList(lessonList);
		//dest.writeParcelable(timetableOption, flags);
		dest.writeString(title);
		dest.writeInt(startDay);
		dest.writeInt(endDay);
		dest.writeString((columnType == null) ? "" : columnType.name());
		dest.writeInt(startTime);
        dest.writeInt(startMin);
		dest.writeInt(periodNum);
		dest.writeInt(periodUnit);
		//		if(islessonAlarmExists == true)
		//			dest.writeInt(1);
		//		else if(islessonAlarmExists == false)
		//			dest.writeInt(0);

		dest.writeInt(lessonAlarmTime);
		dest.writeTypedList(lessonList);
//		dest.writeValue(theme);
		dest.writeString((themeType == null) ? "" : themeType.name());
		dest.writeLong(id);
		dest.writeByteArray(bmpForShare);
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public ColumnTypes getColumnType() {
		return columnType;
	}

	public void setColumnType(ColumnTypes columnType) {
		this.columnType = columnType;
	}

    @Deprecated
	public int getStartTime() {
		return startTime;
	}
    public int getStartMin(){
        return startMin;
    }

    public int getStartTimeByMin()
    {return startTime * 60 + startMin;}

	public void setStartHour(int startHour) {
		this.startTime = startHour;
	}
    public void setStartMin(int startMin){this.startMin = startMin;}
	/*public boolean getIsLessonAlarmExists() {
		return islessonAlarmExists;
	}

	public void setIsLessonAlarmExists(boolean lessonAlarm) {
		this.islessonAlarmExists = lessonAlarm;
	}*/

	public int getLessonAlarmTime() {
		return lessonAlarmTime;
	}

	public void setLessonAlarmTime(int lessonAlarmTime) {
		this.lessonAlarmTime = lessonAlarmTime;
	}

	public int getDayNum(){
		if(startDay > endDay){
			return 7 - (startDay - endDay) + 1;
		}else if(startDay < endDay){
			return endDay - startDay + 1;
		}else{
			return -1;
		}
	}
	public static final Parcelable.Creator<Timetable> CREATOR = new Parcelable.Creator<Timetable>(){

		@Override
		public Timetable createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Timetable(source);
		}
		@Override
		public Timetable[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Timetable[size];
		}
	};

	public int getDayIndexFromGregorianCalendarDay(int gregorianDay){
		int idx = gregorianDay - startDay;
		if(idx < 0){
			idx += 7;
		}		
		return idx;
	}

	public int getGregorianCalendarDayFromDayIndex(int dayIdx){
		int[] gregorianDays = new int[]{
				Calendar.MONDAY,
				Calendar.TUESDAY,
				Calendar.WEDNESDAY,
				Calendar.THURSDAY,
				Calendar.FRIDAY,
				Calendar.SATURDAY,
				Calendar.SUNDAY
		};
		int biggest = -100;
		for(int i = 0; i < gregorianDays.length ; i++){
			int day = gregorianDays[i];
			if(day >= biggest){
				biggest = day;
			}
		}

		int gregorianDay = dayIdx + startDay;
		if(gregorianDay > biggest){
			gregorianDay -= biggest;
		}

		return gregorianDay;
	}

	public static String getDayStringFromGregorianCalendar(Context context, int day){
		String[] days = context.getResources().getStringArray(R.array.days_short);
		//days는 0부터 월,화,.....토,일
		switch(day){
		case Calendar.MONDAY : 
			return days[0];
		case Calendar.TUESDAY : 
			return days[1];
		case Calendar.WEDNESDAY : 
			return days[2];
		case Calendar.THURSDAY : 
			return days[3];
		case Calendar.FRIDAY : 
			return days[4];
		case Calendar.SATURDAY : 
			return days[5];
		case Calendar.SUNDAY : 
			return days[6];
		default : 
			return "???";
		}
	}

	public static String getColumnTypeString(ColumnTypes ct){
		switch(ct){
		case BY_ALPHABET:
			return "BY ALPHABET";
		case BY_PERIOD:
			return "BY PERIOD";
		case BY_TIME:
			return "BY TIME";
		default:
			return "Type Error";

		}
	}
	
	public boolean doesTimetableIncludesGregorianDay(int gregorianDay){		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, startDay);
		for(int i = 0; i < getDayNum() ; i++){
			if(cal.get(Calendar.DAY_OF_WEEK) == gregorianDay){
				MyLog.d("doesTimetableIncludesGregorianDay", "true");
				return true;
			}
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
		return false;
	}

	public boolean isTimetableOverflow24Hours(int expectedPeriodUnit, int expectedPeriodNum){
		
		int offset = expectedPeriodNum * expectedPeriodUnit;		
			
		if(offset > 1440){
			return true;
		}else{
			return false;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public YTTimetableTheme.ThemeType getThemeType() {
		return themeType;
	}

	public long getId() {
		return id;
	}
	public byte[] getBitmapByByteArray(){
		return bmpForShare;
	}
	
	public void setBitmapByByteArray(byte[] arr){
		bmpForShare = arr;
	}
	/**
	 * refreshes timetable id. connected with theme system(used in photo file id.), you must be very careful using this.
	 * recommended only to call when downloading other's table.
	 */
	public void refreshId(){
		id = System.currentTimeMillis();
	}

	public void setThemeType(YTTimetableTheme.ThemeType themeType) {
		// TODO Auto-generated method stub
		this.themeType = themeType;
	}
	
	public void onRemoveLesson(Lesson tmpLesson){
		if(this != TimetableDataManager.getMainTimetable()){
			return;
		}
		//스케쥴의 Lesson parentLesson은 아직 tmpLesson이 의미하는 객체 가리키고 있다.
		//포인터가 없어서 일단 당장 생각나는 방법은 한바퀴 돌면서 동일 여부 체크해 삭제...
		HashMap<String, ArrayList<Schedule>> scheduleMap = 
				TimetableDataManager.getSchedules();
		//scheduleMap.
		for(ArrayList<Schedule> sl : scheduleMap.values()){
			for(int j = 0; j < sl.size() ; j++){
				Schedule s = sl.get(j);
				if(s.getParentLesson() == null){
					continue;
				}
				if(s.getParentLesson().equals(tmpLesson)){
					s.setParentLesson(null);
					MyLog.d("schedule parent lesson removed", "OK!");
				}
			}
		}

	}
	
	private void readObject (java.io.ObjectInputStream in) 
			throws java.io.IOException, ClassNotFoundException{
	    int version = in.readInt();
	    switch (version) {
	        case 1 :
	        	themeType = (ThemeType) in.readObject();
	        	title = (String) in.readObject();
	        	startDay = in.readInt();
	        	endDay = in.readInt();
	        	columnType = (ColumnTypes) in.readObject();
	        	startTime = in.readInt();
	        	periodNum = in.readInt();
	        	periodUnit = in.readInt();
	        	lessonAlarmTime = in.readInt();
	        	lessonList = (ArrayList<Lesson>) in.readObject();
	        	id = in.readLong();
	        	bmpForShare = (byte[]) in.readObject();
	        	break;
            case 2:
                MyLog.d("Timetable", "Successfully loaded case 2 data");
                themeType = (ThemeType) in.readObject();
                title = (String) in.readObject();
                startDay = in.readInt();
                endDay = in.readInt();
                columnType = (ColumnTypes) in.readObject();
                startTime = in.readInt();
                startMin = in.readInt();
                periodNum = in.readInt();
                periodUnit = in.readInt();
                lessonAlarmTime = in.readInt();
                lessonList = (ArrayList<Lesson>) in.readObject();
                id = in.readLong();
                bmpForShare = (byte[]) in.readObject();
                break;
	        	//if new version/ params modified, make new cases.
	    }
	}
	private void writeObject(java.io.ObjectOutputStream out) 
			throws java.io.IOException, ClassNotFoundException{
	    out.writeInt(INTERNAL_VERSION_ID);
//	    out.writeLong(paws);
	    out.writeObject(themeType);
	    out.writeObject(title);
	    out.writeInt(startDay);
	    out.writeInt(endDay);
	    out.writeObject(columnType);
	    out.writeInt(startTime);
        out.writeInt(startMin);
	    out.writeInt(periodNum);
	    out.writeInt(periodUnit);
	    out.writeInt(lessonAlarmTime);
	    out.writeObject(lessonList);
	    out.writeLong(id);
	    out.writeObject(bmpForShare);
	}
	
	
}