package com.sulga.yooiitable.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.TimetableActivity;
import com.sulga.yooiitable.utils.YTBitmapLoader;

import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TimetableDataManager {

	private static volatile TimetableDataManager tdm;
	private static final String SCHEDULE_KEY_TOKEN =  "/";

	private TimetableDataManager(){
		if(readDatasFromExternalStorage() == false){
			makeDefaultTimetableDatas();
			//			writeDatasToExternalStorage(timetables, scheduleMap, eventIDsToDeleteInGoogleCalendar);			
			writeDatasToExternalStorage();
		}
	}

	public static TimetableDataManager getInstance(){
		if(tdm == null){
			synchronized(TimetableDataManager.class){
				if(tdm == null)
					tdm = new TimetableDataManager();				
			}
		}
		return tdm;
	}

	private static HashMap<String,ArrayList<Schedule>> scheduleMap = null;
	private static ArrayList<Timetable> timetables = null;
	private static ArrayList<String> eventIDsToDeleteInGoogleCalendar = null;

	public synchronized Timetable getTimetableAtPage(int pageIndex){
		if(pageIndex < 0){
			return null;
		}
		return getTimetables().get(pageIndex - TimetableActivity.TIMETABLE_PAGE_OFFSET);
	}

	public static Timetable getMainTimetable(){
		return getTimetables().get(getTimetables().size() - 1);
	}

	//private static int timetableIndexInHistory = 0;
	public static void addTimetableAtHead(Timetable t){
		getTimetables().add(0, t);
	}

	public static synchronized HashMap<String, ArrayList<Schedule>> getSchedules() {
		if(scheduleMap == null){
			readDatasFromExternalStorage();
		}
		return scheduleMap;
	}

	public synchronized void putEventIDToDeleteOnGoogleCalendar(String id){
		eventIDsToDeleteInGoogleCalendar.add(id);
	}

	public synchronized void removeEventIDToDeleteOnGoogleCalendar(String id){
		eventIDsToDeleteInGoogleCalendar.remove(id);
	}

	public void putSchedule(Schedule s){
		String key = makeKeyFromSchedule(s);

		if(getSchedules().get(key) == null){
			ArrayList<Schedule> al = new ArrayList<Schedule>();
			al.add(s);
			getSchedules().put(key, al);
		}else{
			getSchedules().get(key).add(s);
		}
	}	

	public ArrayList<Schedule> getScheduleListOfSameDayWith(Schedule s){
		return getScheduleListFromKey(makeKeyFromSchedule(s));
	}
	public ArrayList<Schedule> getScheduleListFromKey(String key){
		return getSchedules().get(key);
	}

	public int getIndexOfSchedule(Schedule s){
		ArrayList<Schedule> sl = getSchedules().get(makeKeyFromSchedule(s));
		if(sl == null)
			return -1;

		for(int i = 0; i < sl.size() ; i++){
			Schedule _s = sl.get(i);
			if(_s == s)
				return i;
		}
		return -1;
	}

	public boolean removeScheduleInMap(Schedule s){
		String key = makeKeyFromSchedule(s);
		int index = getIndexOfSchedule(s);
		if(index == -1)
			return false;

		getSchedules().get(key).remove(getIndexOfSchedule(s));
		return true;
	}

	public static int getCurrentScheduleSize(){
		Collection<String> keys = scheduleMap.keySet();
		int scheduleCount = 0;
		for(String key : keys){
			ArrayList<Schedule> scheduleList = scheduleMap.get(key);
			scheduleCount += scheduleList.size();
		}
		return scheduleCount;
	}

	public static String makeKeyFromSchedule(Schedule s){
		String year = Integer.toString(s.getScheduleYear());
		String month = s.getScheduleMonth() < 10 ? 
				"0" + Integer.toString(s.getScheduleMonth()) : Integer.toString(s.getScheduleMonth());
				String day = s.getScheduleDay() < 10 ? 
						"0" + Integer.toString(s.getScheduleDay()) : Integer.toString(s.getScheduleDay());

						String key = year + SCHEDULE_KEY_TOKEN
								+ month + SCHEDULE_KEY_TOKEN
								+ day;
						return key;
	}

	public static String makeKeyFromDate(int year, int month, int day){
		String s_year = Integer.toString(year);
		String s_month = month < 10 ? 
				"0" + Integer.toString(month) : Integer.toString(month);
				String s_day = day < 10 ? 
						"0" + Integer.toString(day) : Integer.toString(day);

						String key = s_year + SCHEDULE_KEY_TOKEN
								+ s_month + SCHEDULE_KEY_TOKEN
								+ s_day;
						return key;
	}

	public static int getGregorianYearFromScheduleKey(String key){
		StringTokenizer st = new StringTokenizer(key, SCHEDULE_KEY_TOKEN);
		//스트링이 3개로 잘라지지 않으면 뭔가 이상한거다.
		if(st.countTokens() != 3){
			return -1;
		}

		String year = st.nextToken();

		return Integer.parseInt(year);
	}

	public static int getGregorianMonthFromScheduleKey(String key){
		StringTokenizer st = new StringTokenizer(key, SCHEDULE_KEY_TOKEN);
		//스트링이 3개로 잘라지지 않으면 뭔가 이상한거다.
		if(st.countTokens() != 3){
			return -1;
		}

		st.nextToken();
		String month = st.nextToken();


		return Integer.parseInt(month);
	}

	public static int getGregorianDayFromScheduleKey(String key){
		StringTokenizer st = new StringTokenizer(key, SCHEDULE_KEY_TOKEN);
		//스트링이 3개로 잘라지지 않으면 뭔가 이상한거다.
		if(st.countTokens() != 3){
			return -1;
		}

		st.nextToken();
		st.nextToken();
		String day = st.nextToken();


		return Integer.parseInt(day);
	}

	public static ArrayList<Timetable> getTimetables() {
		if(timetables == null){
			readDatasFromExternalStorage();
		}
		return timetables;
	}

	public static void onPause(){
		//		TimetableDataManager.writeDatasToExternalStorage(timetables, scheduleMap, eventIDsToDeleteInGoogleCalendar);
		writeDatasToExternalStorage();
		MyLog.d("TDM", "onPause called, maintable lesson size : " + getMainTimetable().getLessonList().size() );
		//		destroyStatics();
	}

	//	public static void destroyStatics(){
	//		scheduleMap = null;
	//		timetables = null;
	//		eventIDsToDeleteInGoogleCalendar = null;
	//		tdm = null;
	//	}

	public int getTimetableIndex(Timetable timetable){
		return getTimetables().indexOf(timetable);
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	//	public synchronized static void writeDatasTo(String filename, String data){
	//		File root = android.os.Environment.getExternalStorageDirectory();
	//		File dir = new File(root.getAbsolutePath() + "/YooiiTable");
	//		dir.mkdirs();
	//		File file = new File(dir, filename);
	//		
	//		FileOutputStream fos = null;
	//		ObjectOutputStream oos = null;
	//
	//		try {
	//			fos = new FileOutputStream(file, false);
	//			//file channel test 20140104
	////			FileChannel file = fos.getChannel();
	////			ByteBuffer buf = file.map(FileChannel.MapMode.READ_WRITE, 0, size)
	//			try {
	//				oos = new ObjectOutputStream(fos);
	//				oos.flush();
	//				
	//				oos.writeObject(data);
	//	
	//				MyLog.d("file write path", file.getAbsolutePath());
	//			} catch (IOException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} finally {
	//			//Close the ObjectOutputStream
	//			try {
	//				if (oos != null) {
	//					oos.flush();
	//					oos.close();
	//				}
	//				if(fos != null){
	//					fos.flush();
	//					fos.close();
	//				}
	//			
	//			
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//
	//	}
	/**
	 * 
	 * @param t	: timetable list
	 * @param s : schedule map
	 * @param eventIDsToDeleteOnGC : string list
	 * 만약 null값이 전달되면 file에 저장된 값을 읽어봐 write
	 */
	public synchronized static void writeDatasToExternalStorage(){
		//			ArrayList<Timetable> t, HashMap<String, ArrayList<Schedule>> s, ArrayList<String> eventIDsToDeleteOnGC){
		//만약 어떤 이유로 아래 데이터들이 null이라면(static때문에 불안정하네...)
		if(timetables == null || scheduleMap == null || eventIDsToDeleteInGoogleCalendar == null){
			//			readDatasFromExternalStorage();
			MyLog.d("FileThreadSafeTest", "null data!!!!!!!!");
		}

		if(timetables != null)
			MyLog.d("FileThreadSafeTest", "writeData : 1.read root storage directory / lesson size : " + getMainTimetable().getLessonList().size() );

		// See
		// http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
		//		File dir = new File(root.getAbsolutePath() + "/YooiiTable");
		File dir = new File(Environment.getExternalStorageDirectory() + "/YooiiTable");
		dir.mkdirs();
		File file = new File(dir, "TTDatas.yt");
		if(timetables != null)
			MyLog.d("FileThreadSafeTest", "writeData : 2.file made / lesson size : " + getMainTimetable().getLessonList().size() );

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(file, false);
			//file channel test 20140104
			//			FileChannel file = fos.getChannel();
			//			ByteBuffer buf = file.map(FileChannel.MapMode.READ_WRITE, 0, size)
			try {
				if(timetables != null)
					MyLog.d("FileThreadSafeTest", "writeData : 3.file writing start / lesson size : " + getMainTimetable().getLessonList().size() );
				oos = new ObjectOutputStream(fos);
				oos.flush();

				oos.writeObject(timetables);
				oos.writeObject(scheduleMap);
				oos.writeObject(eventIDsToDeleteInGoogleCalendar);
				
				if(timetables != null)
					MyLog.d("FileThreadSafeTest", "writeData : 4.file writing done / lesson size : " + getMainTimetable().getLessonList().size() );

				MyLog.d("file write path", file.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//Close the ObjectOutputStream
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
				if(fos != null){
					fos.flush();
					fos.close();
				}
				if(timetables != null)
					MyLog.d("FileThreadSafeTest", "writeData : 5.filestream closed / lesson size : " + getMainTimetable().getLessonList().size() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 탐테/스케쥴 저장된 파일을 읽어들인다. 만약 저장되어있으면 읽어들이고 저장안되있으면 return false
	 * @return true if file read success, false if file not exist/reading file failed
	 */
	private synchronized static boolean readDatasFromExternalStorage(){
		//		if( timetables != null || scheduleMap != null || eventIDsToDeleteInGoogleCalendar != null){
		//			MyLog.d("FileThreadSafeTest", "readData Called!!! timetable not null, return." );
		//			return true;
		//		}
		if(timetables != null){
			MyLog.d("FileThreadSafeTest", "readData : start / lesson size : " + getMainTimetable().getLessonList().size() );
		}
		File root = android.os.Environment.getExternalStorageDirectory();

		// See
		// http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

		//		File file = new File(root.getAbsolutePath() + "/YooiiTable/TTDatas.yt");
		File file = new File(Environment.getExternalStorageDirectory() 
				+ "/YooiiTable/TTDatas.yt");
		MyLog.d("TimetableDataManager", "dir from root : " + root.getAbsolutePath() + ", file from Environment : " + file.getAbsolutePath());
		if(timetables != null)
			MyLog.d("FileThreadSafeTest", "readData : 1. make new data / lesson size : " + getMainTimetable().getLessonList().size() );
		if(file.exists() == false){
			//파일이 존재하지 않으면...
			return false;
		}

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(file);
			try {
				ois = new ObjectInputStream(fis);
				try {
					if(timetables != null)
						MyLog.d("FileThreadSafeTest", "readData : 2. start reading data / lesson size : " + getMainTimetable().getLessonList().size() );
					timetables = (ArrayList<Timetable>) ois.readObject();
					scheduleMap = (HashMap<String, ArrayList<Schedule>>) ois.readObject();
					eventIDsToDeleteInGoogleCalendar = (ArrayList<String>) ois.readObject();
					if(timetables != null)
						MyLog.d("FileThreadSafeTest", "readData : 3. read data end / lesson size : " + getMainTimetable().getLessonList().size() );
					MyLog.d("file read path", file.getAbsolutePath());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(InvalidClassException e){
					//Log.e("SerializeExcepiton", "YEAK");
					//Timetable클래스의 Serialize Id가 바뀌면 클래스 내용물이 바뀌었다->역직렬화에 오류 가능성.
					//이 경우 파일을 삭제해버린다.
					file.delete();
					makeDefaultTimetableDatas();
					//					writeDatasToExternalStorage(timetables, scheduleMap, eventIDsToDeleteInGoogleCalendar);
					writeDatasToExternalStorage();
					e.printStackTrace();
				}catch(Exception e){
					file.delete();
					makeDefaultTimetableDatas();
					//					writeDatasToExternalStorage(timetables, scheduleMap, eventIDsToDeleteInGoogleCalendar);
					writeDatasToExternalStorage();
					e.printStackTrace();
				}
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(ois != null){
					fis.close();
					ois.close();
				}
				if(timetables != null)
					MyLog.d("FileThreadSafeTest", "readData : 4. filestream closed");
				//파일에 오류가 나면 삭제해버린다.

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return true;
	}

	public static boolean getIsFirstLaunch(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		//	    SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);   
		boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);
		if(isFirstLaunch == true){
			//Automatically set this value into false.
			Editor edit = prefs.edit();
			edit.putBoolean("isFirstLaunch", false);
			edit.commit();
		}
		return isFirstLaunch;
	}

	public static boolean getIsFirstConnectorLaunch(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		//	    SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);   
		boolean isFirstLaunch = prefs.getBoolean("isFirstConnectorLaunch", true);
		if(isFirstLaunch == true){
			//Automatically set this value into false.
			Editor edit = prefs.edit();
			edit.putBoolean("isFirstConnectorLaunch", false);
			edit.commit();
		}
		return isFirstLaunch;
	}
	
	public static String getNaverPaymentSeq(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		//	    SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);   
		String paymentSeq = prefs.getString("naverPaymentSeq", null);
		return paymentSeq;
	}

	public static void saveNaverPaymentSeq(Context context, String paymentSeq){
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		//	    SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);   
		Editor edit = prefs.edit();
		edit.putString("naverPaymentSeq", paymentSeq);
		edit.commit();
	}


	public static boolean getCurrentFullVersionState(Context context) 
	{
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		//	    SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);        
		return prefs.getBoolean("isFullVersion", false);
	}

	public static void saveFullVersionState(Context context, Boolean isFullVersion) 
	{
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putBoolean("isFullVersion", isFullVersion);        
		edit.commit();
	}

	/**
	 * get today uploaded timetable count, **Starts from 0!**
	 * so for ex) if current max timetable number is 5 and cnt equals 5, 
	 * no more timetables should be uploaded.
	 * if no default value exists(= -1), automatically puts integer 0 to preference.
	 * @param context
	 * @return cnt
	 */
	public static int getTodayUploadAvailCount(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, "shareCount", Context.MODE_PRIVATE);

		//		Calendar c = GregorianCalendar.getInstance();
		//		int year = c.get(GregorianCalendar.YEAR);
		//		int month = c.get(GregorianCalendar.MONTH);
		//		int day = c.get(GregorianCalendar.DAY_OF_MONTH);
		//		String key = "uploadtable" + Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
		String key = "availu";
		int cnt = prefs.getInt(key, 10);
		MyLog.d("ShareCount", "key : " + key + ", uploaded table cnt : " + cnt);
		return cnt;
	}

	/**
	 * adds timetable count and save.
	 * You MUST call this after upload timetable finished.
	 * @param context
	 */
	public static void setTodayUploadAvailCount(Context context, int cnt){
		SharedPreferences prefs = PreferenceManager.wrap(context, "shareCount", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		//		Calendar c = GregorianCalendar.getInstance();
		//		int year = c.get(GregorianCalendar.YEAR);
		//		int month = c.get(GregorianCalendar.MONTH);
		//		int day = c.get(GregorianCalendar.DAY_OF_MONTH);
		//		String key = "downloadtable" + Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
		String key = "availu";
		MyLog.d("ShareCount", "key : " + key + ", downloaded table added, now : " + cnt);
		edit.putInt(key, cnt);
		edit.commit();
	}

	/**
	 * get today downloaded timetable count, **Starts from 0!**
	 * so for ex) if current max timetable number is 5 and cnt equals 5, 
	 * no more timetables should be uploaded.
	 * if no default value exists(= -1), automatically puts integer 0 to preference.
	 * @param context
	 * @return cnt
	 */
	public static int getTodayDownloadAvailCount(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, "shareCount", Context.MODE_PRIVATE);
		//		Calendar c = GregorianCalendar.getInstance();
		//		int year = c.get(GregorianCalendar.YEAR);
		//		int month = c.get(GregorianCalendar.MONTH);
		//		int day = c.get(GregorianCalendar.DAY_OF_MONTH);
		//		String key = "downloadtable" + Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
		//		int cnt = prefs.getInt(key, 0);
		String key = "availd";
		int cnt = prefs.getInt(key, 10);
		MyLog.d("ShareCount", "key : " + key + ", Avail download cnt : " + cnt);
		return cnt;
	}

	/**
	 * adds timetable count and save.
	 * You MUST call this after download timetable finished.
	 * @param context
	 */
	public static void setTodayDownloadAvailCount(Context context, int cnt){
		SharedPreferences prefs = PreferenceManager.wrap(context, "shareCount", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		//		Calendar c = GregorianCalendar.getInstance();
		//		int year = c.get(GregorianCalendar.YEAR);
		//		int month = c.get(GregorianCalendar.MONTH);
		//		int day = c.get(GregorianCalendar.DAY_OF_MONTH);
		//		String key = "downloadtable" + Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
		String key = "availd";
		MyLog.d("ShareCount", "key : " + key + ", downloaded table added, now : " + cnt);
		edit.putInt(key, cnt);
		edit.commit();
	}
	
	public static void setConnectorBannerVersion(Context context, int version){
		SharedPreferences prefs = PreferenceManager.wrap(context,
				"bannerInfo", 
				Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		String key = "version";
		MyLog.d("bannerInfo", "banner version : " + version);
		edit.putInt(key, version);
		edit.commit();
	}
	
	public static int getConnectorBannerVersion(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, 
				"bannerInfo", 
				Context.MODE_PRIVATE);
		String key = "version";
		int version = prefs.getInt(key, -1);
		MyLog.d("bannerInfo", "banner version : " + version);
		return version;
	}
	
	public static void setConnectorBannerLinkUrl(Context context, String linkUrl){
		SharedPreferences prefs = PreferenceManager.wrap(context,
				"bannerInfo", 
				Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		String key = "linkUrl";
		MyLog.d("bannerInfo", "banner link url : " + linkUrl);
		edit.putString(key, linkUrl);
		edit.commit();
	}
	
	public static String getConnectorBannerLinkUrl(Context context){
		SharedPreferences prefs = PreferenceManager.wrap(context, 
				"bannerInfo", 
				Context.MODE_PRIVATE);
		String key = "linkUrl";
		String linkUrl = prefs.getString(key, null);
		MyLog.d("bannerInfo", "banner link url : " + linkUrl);
		return linkUrl;
	}	

	
	public static void saveConnectorBannerBitmap(Context context, Bitmap bitmap){
//		File file = new File(Environment.getExternalStorageDirectory() 
//				+ "/YooiiTable/ConnectorBanner.png");
//		File fileCacheItem = new File(file.getPath());
		File dir = new File(Environment.getExternalStorageDirectory() + "/YooiiTable");
		dir.mkdirs();
		File file = new File(dir, "ConnectorBanner.png");
        OutputStream out = null;
        try{
            file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, out);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
	}
	
	public static Bitmap getConnectorBannerBitmap(Context context){
		File file = new File(Environment.getExternalStorageDirectory() 
				+ "/YooiiTable/ConnectorBanner.png");
		if(file.exists() == true){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
//			selected_photo.setImageBitmap(bitmap);
			return bitmap;
		}else{
			return null;
		}
	}
	
	public static synchronized void makeDefaultTimetableDatas(){
		scheduleMap = new HashMap<String, ArrayList<Schedule>>();
		timetables = new ArrayList<Timetable>();
		eventIDsToDeleteInGoogleCalendar = new ArrayList<String>();

		addTimetableAtHead(new Timetable(Calendar.MONDAY, Calendar.SATURDAY, Timetable.DEFAULT_PERIOD_NUM));
		addTimetableAtHead(new Timetable(Calendar.MONDAY, Calendar.FRIDAY, Timetable.DEFAULT_PERIOD_NUM));		
	}

	public static synchronized ArrayList<String> getEventIDsToDeleteInGoogleCalendar() {
		if(eventIDsToDeleteInGoogleCalendar == null){
			readDatasFromExternalStorage();
		}
		return eventIDsToDeleteInGoogleCalendar;
	}

	public static void setEventIDsToDeleteInGoogleCalendar(
			ArrayList<String> eventIDsToDeleteInGoogleCalendar) {
		TimetableDataManager.eventIDsToDeleteInGoogleCalendar = eventIDsToDeleteInGoogleCalendar;
	}

	public static void deleteTimetableBackgroundPhotoIfExists(Context context, Timetable timetable){
		YTBitmapLoader.getPortraitCroppedImageFile(context, timetable.getId()).delete();
	}
}
