package com.sulga.yooiitable.data;

import java.io.*;
import java.util.*;

import android.os.*;

import com.sulga.yooiitable.alarm.*;
import com.sulga.yooiitable.data.Timetable.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.theme.YTTimetableTheme.*;

public class Schedule implements Parcelable, TimeInfo, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int INTERNAL_VERSION_ID = 1;

	public static final int PARENT_LESSON_NONE = -1;
	public static final int SCHEDULE_ALARM_NONE = -1;
	public static final String EVENT_KEY_NONE = "NO_GOOGLE_CALENDAR_KEY_EXISTS";
	
	private String scheduleName;
	private String scheduleMemo;
	private String googleCalendarEventKey = EVENT_KEY_NONE;
	//private int parentLessonIndex = -1;
	private Lesson parentLesson;
	
	private int scheduleYear;
	private int scheduleMonth;
	private int scheduleDay;
	
	private int scheduleHour;
	private int scheduleMin;
	
	private long lastUpdatedTimeInMillis;
	//private long scheduleTimeInMillis;
	
	private int scheduleAlarm = SCHEDULE_ALARM_NONE;
	
	//알람 id는 알람매니저가 알람 등록을 시작할때 자동 배정, 지속적 increment...오버플로우는 고려안함.
	private int alarmId = YTAlarmManager.YT_ALARM_NOT_REGISTERED;
	
	public Schedule(){
		setLastUpdated(Calendar.getInstance().getTimeInMillis());
	}
	public Schedule(String scheduleName, String scheduleMemo,
			int scheduleYear, int scheduleMonth, 
			int scheduleDay, int scheduleHour, int scheduleMin){
		this.setScheduleName(scheduleName);
		this.setScheduleMemo(scheduleMemo);
		
		this.setScheduleYear(scheduleYear);
		this.setScheduleMonth(scheduleMonth);
		this.setScheduleDay(scheduleDay);
		this.setScheduleHour(scheduleHour);
		this.setScheduleMin(scheduleMin);
		
		setLastUpdated(Calendar.getInstance().getTimeInMillis());
	}
	
	public Schedule(String scheduleName, String scheduleMemo, Lesson parentLesson, 
			int scheduleYear, int scheduleMonth, int scheduleDay, 
			int scheduleHour, int scheduleMin){
		this.setScheduleName(scheduleName);
		this.setScheduleMemo(scheduleMemo);
		//this.setParentLessonIndex(parentLessonIndex);
		this.setParentLesson(parentLesson);
		this.setScheduleYear(scheduleYear);
		this.setScheduleMonth(scheduleMonth);
		this.setScheduleDay(scheduleDay);
		this.setScheduleHour(scheduleHour);
		this.setScheduleMin(scheduleMin);
		
		setLastUpdated(Calendar.getInstance().getTimeInMillis());

	}

	public Schedule(Parcel source) {
		// TODO Auto-generated constructor stub
		this.scheduleName = source.readString();
		this.scheduleMemo = source.readString();
		//parentLessonIndex = source.readInt();
		parentLesson = source.readParcelable(Lesson.class.getClassLoader());
		this.scheduleYear = source.readInt();
		this.scheduleMonth = source.readInt();
		this.scheduleDay = source.readInt();
		this.scheduleHour = source.readInt();
		this.scheduleMin = source.readInt();
		//this.scheduleTimeInMillis = source.readLong();
		
		//lastUpdated = Calendar.getInstance().getTimeInMillis();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
		dest.writeString(scheduleName);
		dest.writeString(scheduleMemo);
		//dest.writeInt(parentLessonIndex);
		dest.writeParcelable(parentLesson, flag);
		dest.writeInt(scheduleYear);
		dest.writeInt(scheduleMonth);
		dest.writeInt(scheduleDay);
		dest.writeInt(scheduleHour);
		dest.writeInt(scheduleMin);
		//dest.writeLong(scheduleTimeInMillis);
	}
	
	public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>(){

		@Override
		public Schedule createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Schedule(source);
		}

		@Override
		public Schedule[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Schedule[size];
		}
	};
	
	public static long getScheduleTimeInMillis(Schedule s){
		int year = s.getScheduleYear();
		int month = s.getScheduleMonth();
		int day = s.getDay();
		int hour = s.getScheduleHour();
		int min = s.getScheduleMin();
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, min);
		MyLog.d("ScheduleTimeInMillis", c.getTimeInMillis()+"");
		return c.getTimeInMillis();
	}
	
	/*public void setScheduleTimeInMillis(){
		scheduleTimeInMillis = Schedule.getScheduleTimeInMillis(this);
	}
*/	
	
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	/*public int getParentLessonIndex() {
		return parentLessonIndex;
	}
	public void setParentLessonIndex(int parentLessonIndex) {
		this.parentLessonIndex = parentLessonIndex;
	}*/
	public int getScheduleYear() {
		return scheduleYear;
	}
	public void setScheduleYear(int scheduleYear) {
		this.scheduleYear = scheduleYear;
	}
	public int getScheduleMonth() {
		return scheduleMonth;
	}
	public void setScheduleMonth(int scheduleMonth) {
		this.scheduleMonth = scheduleMonth;
	}
	public int getScheduleDay() {
		return scheduleDay;
	}
	public void setScheduleDay(int scheduleDay) {
		this.scheduleDay = scheduleDay;
	}
	public int getScheduleHour() {
		return scheduleHour;
	}
	public void setScheduleHour(int scheduleHour) {
		this.scheduleHour = scheduleHour;
	}
	public int getScheduleMin() {
		return scheduleMin;
	}
	public void setScheduleMin(int scheduleMin) {
		this.scheduleMin = scheduleMin;
	}
	public String getScheduleMemo() {
		return scheduleMemo;
	}
	public void setScheduleMemo(String scheduleMemo) {
		this.scheduleMemo = scheduleMemo;
	}
	public Lesson getParentLesson() {
		return parentLesson;
	}
	public void setParentLesson(Lesson parentLesson) {
		this.parentLesson = parentLesson;
	}
	@Override
	public int getStartHour() {
		// TODO Auto-generated method stub
		return scheduleHour;
	}
	@Override
	public void setStartHour(int startHour) {
		// TODO Auto-generated method stub
		this.scheduleHour = startHour;
		
	}
	@Override
	public int getStartMin() {
		// TODO Auto-generated method stub
		return scheduleMin;
	}
	@Override
	public void setStartMin(int startMin) {
		// TODO Auto-generated method stub
		scheduleMin = startMin;
	}
	@Override
	public int getDay() {
		// TODO Auto-generated method stub
		return scheduleDay;
	}
	@Override
	public void setDay(int day) {
		// TODO Auto-generated method stub
		scheduleDay = day;
	}
	public String getGoogleCalendarEventKey() {
		return googleCalendarEventKey;
	}
	public void setGoogleCalendarEventKey(String googleCalendarEventKey) {
		this.googleCalendarEventKey = googleCalendarEventKey;
	}
	public long getLastUpdated() {
		return lastUpdatedTimeInMillis;
	}
	public void setLastUpdated(long lastUpdated) {
		MyLog.d("setLastUpdated", "Set Last Updated Called : " + lastUpdated);
		this.lastUpdatedTimeInMillis = lastUpdated;
	}
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	public int getScheduleAlarm() {
		return scheduleAlarm;
	}
	public void setScheduleAlarm(int scheduleAlarm) {
		this.scheduleAlarm = scheduleAlarm;
	}
	
	
	private void readObject (java.io.ObjectInputStream in) 
			throws java.io.IOException, ClassNotFoundException{
	    int version = in.readInt();
	    switch (version) {
	        case 1 :
	        	scheduleName = (String) in.readObject();
	        	scheduleMemo = (String) in.readObject();
	        	googleCalendarEventKey = (String) in.readObject();
	        	parentLesson = (Lesson) in.readObject();
	        	scheduleYear = in.readInt();
	        	scheduleMonth = in.readInt();
	        	scheduleDay = in.readInt();
	        	scheduleHour = in.readInt();
	        	scheduleMin = in.readInt();
	        	lastUpdatedTimeInMillis = in.readLong();
	        	scheduleAlarm = in.readInt();
	        	alarmId = in.readInt();
	        	break;
	        	//if new version/ params modified, make new cases.
	    }
	}
	private void writeObject(java.io.ObjectOutputStream out) 
			throws java.io.IOException, ClassNotFoundException{
	    out.writeInt(INTERNAL_VERSION_ID);
//	    out.writeLong(paws);
	    out.writeObject(scheduleName);
	    out.writeObject(scheduleMemo);
	    out.writeObject(googleCalendarEventKey);
	    out.writeObject(parentLesson);
	    out.writeInt(scheduleYear);
	    out.writeInt(scheduleMonth);
	    out.writeInt(scheduleDay);
	    out.writeInt(scheduleHour);
	    out.writeInt(scheduleMin);
	    out.writeLong(lastUpdatedTimeInMillis);
	    out.writeInt(scheduleAlarm);
	    out.writeInt(alarmId);
	}

}
