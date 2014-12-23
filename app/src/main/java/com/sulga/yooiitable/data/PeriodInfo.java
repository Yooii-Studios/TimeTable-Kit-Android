package com.sulga.yooiitable.data;

import java.io.*;
import java.util.*;

import com.sulga.yooiitable.data.Timetable.*;
import com.sulga.yooiitable.theme.YTTimetableTheme.*;

import android.os.*;

public class PeriodInfo implements Parcelable, TimeInfo, Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int INTERNAL_VERSION_ID = 1;
	
	private int day;
	private int startHour;
	private int startMin;
	private int endHour;
	private int endMin;
	
	//private Timetable parentTimetable;
	public PeriodInfo(){}
	public PeriodInfo( int mDay,  int mStartHour, int mStartMin, int mEndHour, int mEndMin){
		//this.parentTimetable = parentTimetable;
		
		day = mDay;
		startHour = mStartHour;
		startMin = mStartMin;
		endHour = mEndHour;
		endMin = mEndMin;
	}
	
	public PeriodInfo(Parcel source){
		//parentTimetable = source.readParcelable(Timetable.class.getClassLoader());
		
		day = source.readInt();
		startHour = source.readInt();
		startMin = source.readInt();
		endHour = source.readInt();
		endMin = source.readInt();
	}
	
	public PeriodInfo(PeriodInfo p){
		day = p.getDay();
		startHour = p.getStartHour();
		startMin = p.getStartMin();
		endHour = p.getEndHour();
		endMin = p.getEndMin();
	}
	
	@Override
	public void setDay(int mDay){
		day = mDay;
	}
	
	//지금 수업의 길이가 얼마인가?
	/*public int getPeriodLength(){
		return endTime - startTime + 1;
	}*/

	@Override
	public int getDay(){
		return day;
	}
		
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		//dest.writeParcelable(parentTimetable, flags);
		
		dest.writeInt(day);
		dest.writeInt(startHour);
		dest.writeInt(startMin);
		dest.writeInt(endHour);
		dest.writeInt(endMin);
	}
	
	public static final Parcelable.Creator<PeriodInfo> CREATOR = new Parcelable.Creator<PeriodInfo>(){

		@Override
		public PeriodInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			//Timetable parentTimetable = source.readParcelable(Timetable.class.getClassLoader());
			
			int day = source.readInt();
			int startHour = source.readInt();
			int startMin = source.readInt();
			int endHour = source.readInt();
			int endMin = source.readInt();
			
			return new PeriodInfo(day, startHour, startMin, endHour, endMin);
		}

		@Override
		public PeriodInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new PeriodInfo[size];
		}
	};

	@Override
	public int getStartHour() {
		// TODO Auto-generated method stub
		return startHour;
	}

	@Override
	public void setStartHour(int startHour) {
		// TODO Auto-generated method stub
		this.startHour = startHour;
	}

	@Override
	public int getStartMin() {
		// TODO Auto-generated method stub
		return startMin;
	}

	@Override
	public void setStartMin(int startMin) {
		// TODO Auto-generated method stub
		this.startMin = startMin;
	}
	
	public int getEndHour(){
		return endHour;
	}
	public void setEndHour(int endHour){
		this.endHour = endHour;
	}
	public int getEndMin(){
		return endMin;
	}
	public void setEndMin(int endMin){
		this.endMin = endMin;
	}
	
	@Override
	public PeriodInfo clone(){
		return new PeriodInfo(day, startHour, startMin, endHour, endMin);
	}
	
	private void readObject (java.io.ObjectInputStream in) 
			throws java.io.IOException, ClassNotFoundException{
	    int version = in.readInt();
	    switch (version) {
	        case 1 :
	        	day = in.readInt();
	        	startHour = in.readInt();
	        	startMin = in.readInt();
	        	endHour = in.readInt();
	        	endMin = in.readInt();
	        	break;
	        	//if new version/ params modified, make new cases.
	    }
	}
	private void writeObject(java.io.ObjectOutputStream out) 
			throws java.io.IOException, ClassNotFoundException{
	    out.writeInt(INTERNAL_VERSION_ID);
//	    out.writeLong(paws);
	    out.writeInt(day);
	    out.writeInt(startHour);
	    out.writeInt(startMin);
	    out.writeInt(endHour);
	    out.writeInt(endMin);
	}

	
}	