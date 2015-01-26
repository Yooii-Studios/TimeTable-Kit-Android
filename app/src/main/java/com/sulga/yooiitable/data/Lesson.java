package com.sulga.yooiitable.data;

import java.io.*;
import java.util.*;

import android.os.*;

import com.sulga.yooiitable.alarm.*;
import com.sulga.yooiitable.data.Timetable.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.theme.YTTimetableTheme.*;

public class Lesson implements Parcelable, TimeInfo, Cloneable, Serializable{
	public static final int LESSON_COLOR_NONE = -1;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	private static final int INTERNAL_VERSION_ID = 1;
	//public static final String DEFAULT_LESSON_NAME = "Lesson";
	//public static final String DEFAULT_LESSON_WHERE = "Class";
	//public static final String DEFAULT_LESSON_PROFESSOR = "Professor";
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	//수업의 교시 정보를 저장하는 리스트.
	private PeriodInfo periodInfo;
	private String lessonName="";	
	private String lessonWhere="";
	private String professor="";
	private String lessonMemo="";

	private Timetable parentTimetable;

	//알람 id는 알람매니저가 알람 등록을 시작할때 자동 배정, 지속적 increment...오버플로우는 고려안함.
	private int alarmId = YTAlarmManager.YT_ALARM_NOT_REGISTERED;
	
	private int lessonColor = LESSON_COLOR_NONE;
	//private int credit;
	//생성자는 필요없지 않나?
	public Lesson(Timetable parentTimetable, 
			String mLessonName,String mLessonWhere, 
			String mProfessor, String mLessonMemo, 
			PeriodInfo pInfo, 
			int mColor){
		this.parentTimetable = parentTimetable;
		lessonName = mLessonName;
		lessonWhere = mLessonWhere;
		/*
		credit = mCredit;
		color = mColor;		
		 */

		professor = mProfessor;
		lessonMemo = mLessonMemo;
		setColor(mColor);
		periodInfo = pInfo;
	}
	public Lesson(Timetable parentTimetable, String mLessonName, String mLessonWhere, String mProfessor){
		this.parentTimetable = parentTimetable;

		lessonName = mLessonName;
		lessonWhere = mLessonWhere;
		professor = mProfessor;
//		lessonColor = LESSON_COLOR_NONE;
	}
	public Lesson(Timetable parentTimetable){
		this.parentTimetable = parentTimetable;
//		lessonColor = LESSON_COLOR_NONE;
	}

	public Lesson(Parcel source){
		///////////////////////////////readTypedList하니까 왜 오류가 나지???
		//this();
		parentTimetable = source.readParcelable(Timetable.class.getClassLoader());

		periodInfo =  source.readParcelable(PeriodInfo.class.getClassLoader());
		lessonName = source.readString();
		lessonWhere = source.readString();
		professor = source.readString();
		lessonMemo = source.readString();
		lessonColor = source.readInt();
		/*
		credit = source.readInt();
		color = source.readInt();
		 */
	}
	public String getProfessor() {
		return professor;
	}
	public void setProfessor(String professor) {
		this.professor = professor;
	}

	/*
	 *//**
	 * 이 수업이 몇교시부터 시작하는지 반환한다. 
	 * @return lessonStartPeriod
	 *//*	
	public int getLessonStartPeriod(){
		int timetableStartByMin = parentTimetable.getStartTime() * 60;
		int lessonStartByMin = periodInfo.getStartHour() * 60 + periodInfo.getStartMin();

		if(lessonStartByMin < timetableStartByMin){
			lessonStartByMin += 60 * 24;
		}

		int offset = lessonStartByMin - timetableStartByMin;

		return offset / parentTimetable.getPeriodUnit() + 1;
	}
	  *//**
	  * 이 수업이 몇교시에 끝나는지 반환한다. 새벽 1시,2시도 알아서 계산하도록 함.
	  * @return lessonEndPeriod
	  *//*
	public int getLessonEndPeriod(){
		int timetableStartByMin = parentTimetable.getStartTime() * 60;
		int lessonEndByMin = periodInfo.getEndHour() * 60 + periodInfo.getEndMin();
		//수업 끝나는 시간이 시간표 시작시간보다 값이 작다는건 새벽 1시, 2시 등등이 되었다는 뜻. 계산을 위해 24시간을 더해두자.
		if(lessonEndByMin < timetableStartByMin){
			lessonEndByMin += 60 * 24;
		}
		int offset = lessonEndByMin - timetableStartByMin;
		return offset / parentTimetable.getPeriodUnit();
	}*/

	/**
	 * get lesson start period by float, count from 0. 
	 * @return lesson start period by float.
	 */
	public float getLessonStartPeriodByFloat(){
		int timetableStartByMin = parentTimetable.getStartTimeByMin();
		int lessonStartByMin = periodInfo.getStartHour() * 60 + periodInfo.getStartMin();

		if(lessonStartByMin < timetableStartByMin){
			lessonStartByMin += 60 * 24;
		}

		int offset = lessonStartByMin - timetableStartByMin;

		float result = (float)offset / (float)parentTimetable.getPeriodUnit();
		MyLog.d("lessonStartPeriod", result+"");
		return result;
	}

	/**
	 * Get lesson end period by float, count from 0 and this is "Actual" finished period.
	 * EX) we say a class starts from period 1 and ends at 3, 
	 * but actually the end of period 3 equals start of period 4, 
	 * so we expect lesson end period as 4.
	 * @return Actual lesson end period by float. counts from 0.
	 */
	public float getLessonEndPeriodByFloat(){
		int timetableStartByMin = parentTimetable.getStartTimeByMin();
		int lessonEndByMin = periodInfo.getEndHour() * 60 + periodInfo.getEndMin();
		//수업 끝나는 시간이 시간표 시작시간보다 값이 작다는건 새벽 1시, 2시 등등이 되었다는 뜻. 계산을 위해 24시간을 더해두자.
		if(lessonEndByMin <= timetableStartByMin){
			lessonEndByMin += 60 * 24;
		}
		int offset = lessonEndByMin - timetableStartByMin;

		float result =  (float)offset / (float)parentTimetable.getPeriodUnit();
		MyLog.d("lessonEndPeriod", result+"");

		return result;
	}

	/**
	 * if lesson start time is before timetable start time, 
	 * and if lesson starts after 24:00, lesson alarm should work at next day.
	 * if true, alarm should 
	 * @return
	 */
	public boolean shouldAlarmNextDay(){
		int timetableStartByMin = parentTimetable.getStartTimeByMin();
		int lessonStartByMin = periodInfo.getStartHour() * 60 + periodInfo.getStartMin();
		//수업 끝나는 시간이 시간표 시작시간보다 값이 작다는건 새벽 1시, 2시 등등이 되었다는 뜻. 계산을 위해 24시간을 더해두자.
		if(lessonStartByMin <= timetableStartByMin){
			return true;
		}
		return false;
	}
	
	public static float getLessonStartPeriodByFloat(Timetable parentTimetable, PeriodInfo periodInfo){
		int timetableStartByMin = parentTimetable.getStartTimeByMin();
		int lessonStartByMin = periodInfo.getStartHour() * 60 + periodInfo.getStartMin();

		if(lessonStartByMin < timetableStartByMin){
			lessonStartByMin += 60 * 24;
		}

		int offset = lessonStartByMin - timetableStartByMin;

		float result = (float)offset / (float)parentTimetable.getPeriodUnit();
		MyLog.d("lessonStartPeriod", result+"");
		return result;
	}

	public static float getLessonEndPeriodByFloat(Timetable parentTimetable, PeriodInfo periodInfo){
		int timetableStartByMin = parentTimetable.getStartTimeByMin();
		int lessonEndByMin = periodInfo.getEndHour() * 60 + periodInfo.getEndMin();
		//수업 끝나는 시간이 시간표 시작시간보다 값이 작다는건 새벽 1시, 2시 등등이 되었다는 뜻. 계산을 위해 24시간을 더해두자.
		if(lessonEndByMin <= timetableStartByMin){
			lessonEndByMin += 60 * 24;
		}
		int offset = lessonEndByMin - timetableStartByMin;

		float result =  (float)offset / (float)parentTimetable.getPeriodUnit();
		MyLog.d("lessonEndPeriod", result+"");

		return result;
	}

	/**
	 * Get period length normally. 
	 * @return
	 */
	public float getPeriodLengthByFloat(){
		float result =  getLessonEndPeriodByFloat(parentTimetable, periodInfo) 
				- getLessonStartPeriodByFloat(parentTimetable, periodInfo);
		MyLog.d("lessonPeriodLength",  result+"");
		return result;
	}
	public void setLessonName(String mLessonName){
		lessonName = mLessonName;
	}
	public void setLessonWhere(String mLessonWhere){
		lessonWhere = mLessonWhere;
	}
	/*
	public void setHexColor(int mcolor){
		color = mcolor;
	}
	public void setCredit(int mCredit){
		credit = mCredit;
	}
	 */
	public String getLessonName(){
		return lessonName;
	}

	/*public PeriodInfo getLessonInfoAt(int idx){
		return periodInfoList.get(idx);
	}*/
	public String getLessonWhere(){
		return lessonWhere;
	}
	/*
	public int getCredit(){
		return credit;
	}
	public int getColor(){
		return color;
	}
	 */
	/*public void addPeriodInfo(PeriodInfo mInfo){
		periodInfoList.add(mInfo);
	}*/

	public void setPeriodInfo(PeriodInfo pInfo){
		periodInfo = pInfo;
	}

	/*//이 수업에 대한 시간 정보가 몇개 들어있나?
	public int getLessonInfoNum(){		
		return periodInfoList.size();
	}
	 */
	public PeriodInfo getLessonInfo(){
		return periodInfo;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeParcelable(parentTimetable, flags);
		dest.writeParcelable(periodInfo, flags);
		dest.writeString(lessonName);
		dest.writeString(lessonWhere);
		dest.writeString(professor);
		dest.writeString(lessonMemo);
		dest.writeInt(lessonColor);
		/*
		dest.writeInt(credit);
		dest.writeInt(color);
		 */
	}


	public int getColor() {
		return lessonColor;
	}
	public void setColor(int color) {
		this.lessonColor = color;
	}


	public static final Parcelable.Creator<Lesson> CREATOR = new Parcelable.Creator<Lesson>(){

		@Override
		public Lesson createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Lesson(source);
		}

		@Override
		public Lesson[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Lesson[size];
		}

	};

	public static boolean checkLessonCollideWith(Lesson me, Lesson oth){
		PeriodInfo my_p = me.getLessonInfo();
		PeriodInfo oth_p = oth.getLessonInfo();

		int my_p_day = me.getParentTimetable().getDayIndexFromGregorianCalendarDay(my_p.getDay());
		int oth_p_day = oth.getParentTimetable().getDayIndexFromGregorianCalendarDay(oth_p.getDay());

		if(my_p_day != oth_p_day)
			return false;

		if( ( Lesson.getLessonStartPeriodByFloat(oth.parentTimetable, oth.periodInfo) 
				>= Lesson.getLessonStartPeriodByFloat(me.parentTimetable, me.periodInfo) 
				&& Lesson.getLessonStartPeriodByFloat(oth.parentTimetable, oth.periodInfo) < 
				Lesson.getLessonEndPeriodByFloat(me.parentTimetable, me.periodInfo) ) ||
				( Lesson.getLessonEndPeriodByFloat(oth.parentTimetable, oth.periodInfo) > 
				Lesson.getLessonStartPeriodByFloat(me.parentTimetable, me.periodInfo)
						&& Lesson.getLessonEndPeriodByFloat(oth.parentTimetable, oth.periodInfo) <= 
						Lesson.getLessonEndPeriodByFloat(me.parentTimetable, me.periodInfo)) ||
						( Lesson.getLessonStartPeriodByFloat(oth.parentTimetable, oth.periodInfo) >= 
						Lesson.getLessonStartPeriodByFloat(me.parentTimetable, me.periodInfo) 
						&& Lesson.getLessonEndPeriodByFloat(oth.parentTimetable, oth.periodInfo) <= 
						Lesson.getLessonEndPeriodByFloat(me.parentTimetable, me.periodInfo)) ||
						(Lesson.getLessonStartPeriodByFloat(oth.parentTimetable, oth.periodInfo) <= 
						Lesson.getLessonStartPeriodByFloat(me.parentTimetable, me.periodInfo) 
						&& Lesson.getLessonEndPeriodByFloat(oth.parentTimetable, oth.periodInfo) >= 
						Lesson.getLessonEndPeriodByFloat(me.parentTimetable, me.periodInfo) )							
				)
		{
			MyLog.d("checkLessonCollideWith", "return true");
			//겹침.
			return true;
		}else
			return false;
	}
	
	public static boolean checkLessonCollideWith(
			Timetable timetable, int day, float startPeriod, float endPeriod,
			Timetable _timetable, int _day, float _startPeriod, float _endPeriod){
		PeriodInfo my_p = new PeriodInfo(
				day, 
				timetable.getStartHourOfPeriod(startPeriod), 
				timetable.getStartMinOfPeriod(startPeriod), 
				timetable.getEndHourOfPeriod(endPeriod), 
				timetable.getEndMinOfPeriod(endPeriod));
		//PeriodInfo oth_p = oth.getLessonInfo();
		PeriodInfo oth_p = new PeriodInfo(
				_day, 
				_timetable.getStartHourOfPeriod(_startPeriod), 
				_timetable.getStartMinOfPeriod(_startPeriod), 
				_timetable.getEndHourOfPeriod(_endPeriod), 
				_timetable.getEndMinOfPeriod(_endPeriod));

		int my_p_day = timetable.getDayIndexFromGregorianCalendarDay(my_p.getDay());
		int oth_p_day = _timetable.getDayIndexFromGregorianCalendarDay(oth_p.getDay());

		if(my_p_day != oth_p_day)
			return false;

		Lesson A = new Lesson(timetable);
		A.setPeriodInfo(my_p);
		Lesson B = new Lesson(_timetable);
		B.setPeriodInfo(oth_p);
		return checkLessonCollideWith(A, B);
//		if( ( Lesson.getLessonStartPeriodByFloat(_timetable, oth_p) 
//				>= Lesson.getLessonStartPeriodByFloat(timetable, my_p) 
//				&& Lesson.getLessonStartPeriodByFloat(_timetable, oth_p) < 
//				Lesson.getLessonEndPeriodByFloat(timetable, my_p) ) ||
//				( Lesson.getLessonEndPeriodByFloat(_timetable, oth_p) > 
//				Lesson.getLessonStartPeriodByFloat(timetable, my_p)
//						&& Lesson.getLessonEndPeriodByFloat(_timetable, oth_p) <= 
//						Lesson.getLessonEndPeriodByFloat(timetable, my_p)) ||
//						( Lesson.getLessonStartPeriodByFloat(_timetable, oth_p) >= 
//						Lesson.getLessonStartPeriodByFloat(timetable, my_p) 
//						&& Lesson.getLessonEndPeriodByFloat(_timetable, oth_p) <= 
//						Lesson.getLessonEndPeriodByFloat(timetable, my_p)) ||
//						(Lesson.getLessonStartPeriodByFloat(_timetable, oth_p) <= 
//						Lesson.getLessonStartPeriodByFloat(timetable, my_p) 
//						&& Lesson.getLessonEndPeriodByFloat(_timetable, oth_p) >= 
//						Lesson.getLessonEndPeriodByFloat(timetable, my_p) )							
//				)
//		{
//			MyLog.d("checkLessonCollideWith", "return true");
//			//겹침.
//			return true;
//		}else
//			return false;

	}


	@Override
	public String toString(){

		String periodInfoString;
		if(periodInfo == null){
			periodInfoString = "null";
		}else{
			String day;
			switch(periodInfo.getDay()){
			case 0 : 
				day = "MON";
				break;
			case 1 : 
				day = "TUE";
				break;
			case 2 : 
				day = "WED";
				break;
			case 3 : 
				day = "THU";
				break;
			case 4 : 
				day = "FRI";
				break;
			case 5 : 
				day = "SAT";
				break;
			case 6 : 
				day = "SUN";
				break;
			default : 
				day = "???";
			}
			periodInfoString = day + ", start - " + periodInfo.getStartHour() + " : " + periodInfo.getStartMin()
					+ ", end - " +periodInfo.getEndHour() + " : " + periodInfo.getEndMin();
		}

		String str = "Lesson Name : " + lessonName + ", Lesson Location : " + lessonWhere + ", Professor : " + professor + ", periodInfo : " + periodInfoString;
		return str;
	}
	public String getLessonMemo() {
		return lessonMemo;
	}
	public void setLessonMemo(String lessonMemo) {
		this.lessonMemo = lessonMemo;
	}

	public Timetable getParentTimetable(){
		return parentTimetable;
	}
	public void setParentTimetable(Timetable p){
		parentTimetable = p;
	}
	@Override
	public int getStartHour() {
		// TODO Auto-generated method stub
		return periodInfo.getStartHour();
	}
	@Override
	public void setStartHour(int startHour) {
		// TODO Auto-generated method stub
		periodInfo.setStartHour(startHour);
	}
	@Override
	public int getStartMin() {
		// TODO Auto-generated method stub
		return periodInfo.getStartMin();
	}
	@Override
	public void setStartMin(int startMin) {
		// TODO Auto-generated method stub
		periodInfo.setStartMin(startMin);
	}
	
	public int getEndHour(){
		return periodInfo.getEndHour();
	}
	
	public int getEndMin(){
		return periodInfo.getEndMin();
	}
	/**
	 * returns gregorian day.
	 */
	@Override
	public int getDay() {
		// TODO Auto-generated method stub
		return periodInfo.getDay();
	}
	/**
	 * you must set gregorian day.
	 */
	@Override
	public void setDay(int day) {
		// TODO Auto-generated method stub
		periodInfo.setDay(day);
	}

	@Override
	public Lesson clone(){
		MyLog.d("Lesson", lessonName + lessonWhere + professor + lessonMemo);
		return new Lesson(parentTimetable, 
				new String(lessonName), new String(lessonWhere), 
				new String(professor), new String(lessonMemo), 
				new PeriodInfo(periodInfo), 
				lessonColor
				);
	}
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	
	private void readObject (java.io.ObjectInputStream in) 
			throws java.io.IOException, ClassNotFoundException{
	    int version = in.readInt();
	    switch (version) {
	        case 1 :
	        	periodInfo = (PeriodInfo) in.readObject();
	        	lessonName = (String) in.readObject();
	        	lessonWhere = (String) in.readObject();
	        	professor = (String) in.readObject();
	        	lessonMemo = (String) in.readObject();
	        	parentTimetable = (Timetable) in.readObject();
	        	alarmId = in.readInt();
	        	lessonColor = in.readInt();
	        	break;
	        	//if new version/ params modified, make new cases.
	    }
	}
	private void writeObject(java.io.ObjectOutputStream out) 
			throws java.io.IOException{
	   out.writeInt(INTERNAL_VERSION_ID);
//	    out.writeLong(paws);
	   out.writeObject(periodInfo);
	   out.writeObject(lessonName);
	   out.writeObject(lessonWhere);
	   out.writeObject(professor);
	   out.writeObject(lessonMemo);
	   out.writeObject(parentTimetable);
	   out.writeInt(alarmId);
	   out.writeInt(lessonColor);
	}

}
