package com.sulga.yooiitable.google.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.Schedule;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.timetable.TimetableActivity;
import com.sulga.yooiitable.timetable.fragments.ScheduleFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class GCCalendarSyncManager extends GCBasicAsyncTask{
	private static final String TAG = "GCCalendarSyncManager";

	private com.google.api.services.calendar.Calendar calendarService;
	public GCCalendarSyncManager(
			Activity activity,
			com.google.api.services.calendar.Calendar calendar, 
			ProgressDialog progressDialog
			) {
		super(activity, progressDialog);
		this.calendarService = calendar;
		//this.progressDialog = progressDialog;
		// TODO Auto-generated constructor stub
	}

	private static final String SCOPE = 
			"oauth2:https://www.googleapis.com/auth/calendar";

	@Override
	protected void doInBackground() throws IOException {
		publishProgress(PROGRESS_CHECK_CALENDAR_EXISTS);
		String calendarID = checkCalendarExistsAndGetID(null);

		if(calendarID != null){
			s = "calendar exists";
		}else{
			s = "calendar non-exists!";
			publishProgress(PROGRESS_INSERT_CALENDAR);
			com.google.api.services.calendar.model.Calendar insertedCalendar = insertCalendar();
			s += "\ncalendar Added.";
			publishProgress(PROGRESS_CHECK_CALENDAR_EXISTS);
			calendarID = checkCalendarExistsAndGetID(insertedCalendar);
		}

		uploadEvents(calendarID);

		publishProgress(PROGRESS_DONE);

		//insertCalendarEntry();
	}

	private String s = "";
	private String checkCalendarExistsAndGetID(
			com.google.api.services.calendar.model.Calendar
			insertedCalendar) throws IOException{
		String calendarID;

		if(insertedCalendar == null){
			calendarID = getCalendarID();
			if(calendarID == null){
				return null;
			}
		}else{
			calendarID = insertedCalendar.getId();
		}
		return calendarID;
	}

	private String getCalendarID() throws IOException{

		CalendarList calendars = calendarService.calendarList().list().execute();
		for(CalendarListEntry e : calendars.getItems()){
			if(e.getSummary().equals(calendarSummary)){
				return e.getId();
			}
		}
		return null;
	}
	public final static String calendarSummary = "Timetable Kit";
	private com.google.api.services.calendar.model.Calendar insertCalendar() 
			throws IOException{
		com.google.api.services.calendar.model.Calendar calendar =  
				new com.google.api.services.calendar.model.Calendar();

		calendar.setSummary(calendarSummary);
		calendar.setTimeZone(TimeZone.getDefault().getID());

		return calendarService.calendars().insert(calendar).execute();	
	}

	private void uploadEvents(String calendarID) throws IOException{
		Events events = calendarService.events().list(calendarID).execute();
		publishProgress(PROGRESS_UPLOADING_EVENTS);
		updateEventsToGoogleCalendar(calendarID, events);
		downloadEventsFromGoogleCalendar(calendarID, events);
		//eventIdsToDeleteInGoogleCalendar리스트를 불러들여서 삭제 수행.
	}

	private void updateEventsToGoogleCalendar(String calendarID, Events events) 
			throws IOException{
		HashMap<String, ArrayList<Schedule>> scheduleMap = 
				TimetableDataManager.getSchedules();
		//		Collection<ArrayLis>schedules.values()
		ArrayList<Schedule> schedulesToRemove = new ArrayList<Schedule>();
		//inside for, if hashmap schedule removed and putted again the index is messed up.
		//so modify schedules(=remove and add schedules) after for is ended.
		ArrayList<Schedule> schedulesToModify = new ArrayList<>();
		Collection<String> keys = scheduleMap.keySet();

		for(String key : keys){
			ArrayList<Schedule> scheduleList = scheduleMap.get(key);
			MyLog.d(TAG, "schedule list key : " + key + ", list size : " + scheduleList.size());
			for(int i = 0; i < scheduleList.size() ; i++){
				Schedule s = scheduleList.get(i);
				MyLog.d(TAG, "current Schedule : " + s.getScheduleName());
				if(s.getGoogleCalendarEventKey().equals(Schedule.EVENT_KEY_NONE) ||
						s.getGoogleCalendarEventKey() == null){
					Event event = getEventFromSchedule(s, null);

					Event e = calendarService.events().insert(
							calendarID, 
							event)
							.execute();
					s.setGoogleCalendarEventKey(e.getId());

					MyLog.d("insertEvent", "Event Create Time : " + e.getUpdated().getValue() +", schedule created at : " + s.getLastUpdated());
					MyLog.d(TAG, 
							"schedule : " 
									+ s.getScheduleName() 
									+ ", google calendar event key NONE..."
									+ "Event Create Time : " + e.getUpdated().getValue() 
									+ ", schedule created at : " + s.getLastUpdated());
				}else{
					List<Event> eventList = events.getItems();
					Event evInCalendar = null;
					MyLog.d(TAG, "schedule key NOT NONE, google event count : " + eventList.size());

					for(Event e : eventList){
						if(e.getId().equals(s.getGoogleCalendarEventKey())){
							evInCalendar = e;
							break;
						}
					}
					if(evInCalendar == null){
						//for 다 돌았는데도 null이면 캘린더에 이 스케쥴이 존재하지 않는다 - 즉 이벤트가 사용자에 의해 캘린더에서 삭제되었다.
						//따라서 유이테이블의 스케쥴 삭제.
						//TimetableDataManager.removeScheduleInMap(s);
						schedulesToRemove.add(s);
						MyLog.d(TAG, 
								"schedule : " 
										+ s.getScheduleName() 
										+ ", google calendar DOESN'T HAVE this schedule."
										+ " This schedule is going to be deleted.");
						continue;
					}

					//MyLog.d("insertEvent", "Before Event Update Time : " + evInCalendar.getUpdated().getValue() + 
					//	", schedule created at : " + s.getLastUpdated());
					if(s.getLastUpdated() >= evInCalendar.getUpdated().getValue()){
						//스케쥴이 더 이후에 업데이트되어있으면, 구글 캘린더의 내용을 스케쥴의 내용으로 변경해야한다.
						Event event = getEventFromSchedule(s, evInCalendar);

						Event e = calendarService.events().update(
								calendarID, 
								s.getGoogleCalendarEventKey(),
								event)
								.execute();

						MyLog.d("insertEvent", "이벤트를 구글로 업로드!, 이벤트이름 : " + e.getSummary() + 
								"\n event updated Time : " + evInCalendar.getUpdated().getValue() + 
								"\n schedule updated Time : "+ s.getLastUpdated());

						s.setGoogleCalendarEventKey(e.getId());
						MyLog.d(TAG, 
								"schedule : " 
										+ s.getScheduleName() 
										+ "Event Uploaded to Google.");

					}else{
						//구글 캘린더가 더 최근에 업데이트되었으면 스케쥴을 구글 캘린더에 맞게 바꾼다.
						MyLog.d("insertEvent", "구글에서 스케쥴 새 업데이트!, 이벤트이름 : " + evInCalendar.getSummary() + 
								"\n event updated Time : " + evInCalendar.getUpdated().getValue() + 
								"\n schedule updated Time : "+ s.getLastUpdated());
						//						TimetableDataManager.getInstance().removeScheduleInMap(s);
						Schedule schedule = setScheduleFromEvent(s, evInCalendar);
						//						TimetableDataManager.getInstance().putSchedule(schedule);

						schedule.setLastUpdated(Calendar.getInstance().getTimeInMillis());

						MyLog.d(TAG, 
								"schedule : " 
										+ s.getScheduleName() 
										+ "User Modified this schedule in google calendar. " 
										+ "Updating schedule...");

					}
				}
			}
		}
		for(int i = 0 ; i < schedulesToRemove.size() ; i++){
			//구글 캘린더에서 사용자가 삭제한 스케쥴들을 유이테이블에서도 삭제한다
			TimetableDataManager.getInstance().removeScheduleInMap(schedulesToRemove.get(i));
		}
		for(int i = 0; i < schedulesToModify.size() ; i++){
			Schedule s = schedulesToModify.get(i);
			TimetableDataManager.getInstance().removeScheduleInMap(s);
			TimetableDataManager.getInstance().putSchedule(s);
		}
	}

	private boolean scheduleOverflows = false;
	private void downloadEventsFromGoogleCalendar(String calendarID, Events events) throws IOException{
		List<Event> eventList = events.getItems();
		HashMap<String, ArrayList<Schedule>> scheduleMap = 
				TimetableDataManager.getSchedules();
		Collection<String> keys = scheduleMap.keySet();
		ArrayList<String> eventIDsToDelete = TimetableDataManager.getEventIDsToDeleteInGoogleCalendar();
		ArrayList<Schedule> schedulesToAdd = new ArrayList<>();
		scheduleOverflows = false;
		for(int i = 0 ; i < eventList.size() ; i++){
			Event e = eventList.get(i);
			//구글 캘린더에 현재 유이테이블에 저장된 스케쥴과 동일한 스케쥴이 존재하는지 체크한다.
			Schedule scheduleInCalendar = null;
			for(String key : keys){
				ArrayList<Schedule> scheduleList = scheduleMap.get(key);
				for(int j = 0; j < scheduleList.size() ; j++){
					Schedule s = scheduleList.get(j);
					if(e.getId().equals(s.getGoogleCalendarEventKey())){
						//이벤트의 ID와 스케쥴의 ID가 같다면...위에서 이미 수정했으니 패스해야댐.
						scheduleInCalendar = s;
						break;
					}			
				}
				if(scheduleInCalendar != null)
					break;
			}		
			if(scheduleInCalendar != null)
				continue;

			boolean evDeleted = false;
			for(int j = eventIDsToDelete.size() - 1; j >= 0 ; j--){
				if(e.getId().equals(eventIDsToDelete.get(j))){
					//만약 사용자가 폰에서 삭제한 스케쥴의 이벤트 ID와 동일하다면, 이 이벤트는 구글 캘린더에서 삭제되어야하는 이벤트이다.
					MyLog.d(TAG, e.getSummary()
							+ "User deleted this schedule from Timetable Kit."
							+ " Removing this event from google calendar.");

					String evIdToDelete = eventIDsToDelete.get(j);
					calendarService.events().delete(calendarID, e.getId()).execute();
					eventIDsToDelete.remove(evIdToDelete);
					evDeleted = true;
					MyLog.d("insertEvents", e.getSummary() + " Event Deleted from google cal...");

				}

			}
			if(evDeleted) {
				continue;
			}

			Schedule s = setScheduleFromEvent(new Schedule(), e);
			MyLog.d(TAG, "schedule : "
					+ s.getScheduleName()
					+ "Event Added From Google calendar.");
			schedulesToAdd.add(s);
			//TimetableDataManager.putSchedule(s);
		}


		for(int i = 0; i < schedulesToAdd.size() ; i++){
			if(!TimetableDataManager.getCurrentFullVersionState(activity)){
				int scheduleCount = TimetableDataManager.getCurrentScheduleSize();
				if(scheduleCount >= ScheduleFragment.MAX_SCHEDULE_COUNT_FREE_VERSION){
					scheduleOverflows = true;
					break;
				}
			}
			Schedule toAdd = schedulesToAdd.get(i);
			TimetableDataManager.getInstance().putSchedule(toAdd);
		}
		//만약 사용자가 어플에서 스케쥴을 삭제한 후 싱크하기 전에 구글 캘린더에서도 삭제했다면, 
		//eventIDsToDelete리스트에는 사용되지 않는 삭제용 이벤트 ID들이 점점 쌓일 것이다.
		//그걸 체크하여 삭제 수행한다.
		checkEventAlsoRemovedInGoogleCalendarByUser(eventIDsToDelete, eventList);
	}


	private void checkEventAlsoRemovedInGoogleCalendarByUser(ArrayList<String> eventIDsToDelete, 
			List<Event> eventList){
		for(int i = eventIDsToDelete.size() - 1 ; i >= 0 ; i--){
			String id = eventIDsToDelete.get(i);
			boolean idExistsInGC = false;
			for(Event e : eventList){
				if(e.getId().equals(id)){

					idExistsInGC = true;
					break;
				}
			}
			if(idExistsInGC) {
				continue;
			} else {
				//구글 캘린더에 이벤트 ID가 존재하지 않는다면 이제 더 필요없으니 삭제.
				eventIDsToDelete.remove(id);
			}
		}

	}

	private Schedule setScheduleFromEvent(Schedule s, Event e){
		//Schedule _s = s;

		//TimetableDataManager.removeScheduleInMap(s);


		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(e.getStart().getDateTime().getValue());
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		String scheduleName = e.getSummary();
		String scheduleMemo = e.getDescription();

		MyLog.d("insertEvent", "year : " + year 
				+ ", month : " + month 
				+ ", day : " + day 
				+ ", hour : " + hour 
				+ ", min : " + min
				);

		s.setScheduleYear(year);
		s.setScheduleMonth(month);
		s.setScheduleDay(day);
		s.setScheduleHour(hour);
		s.setScheduleMin(min);
		s.setScheduleName(scheduleName);
		s.setScheduleMemo(scheduleMemo);

		s.setGoogleCalendarEventKey(e.getId());

		s.setLastUpdated(Calendar.getInstance().getTimeInMillis());

		//TimetableDataManager.putSchedule(s);
		return s;
	}


	private Event getEventFromSchedule(Schedule s, Event event){
		//Event event = new Event();
		//event.geti
		if(event == null){
			event = new Event();
		}
		event.setSummary(s.getScheduleName());
		event.setDescription(s.getScheduleMemo());

		java.util.Calendar c = Calendar.getInstance();
		c.set(
				s.getScheduleYear(), 
				s.getScheduleMonth(), 
				s.getScheduleDay(), 
				s.getScheduleHour(), 
				s.getScheduleMin()
				);
		Date startDate = c.getTime();
		Date endDate = new Date(startDate.getTime() + 3600000);

		DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
		event.setStart(new EventDateTime().setDateTime(start));
		DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
		event.setEnd(new EventDateTime().setDateTime(end));
		return event;
	}
	private void clearEvents(String calendarID, Events events) throws IOException{
		//events.clear();
		for(Event e : events.getItems()){
			MyLog.d("clearEvents", "Event ID : " + e.getId());
			calendarService.events().delete(calendarID, e.getId()).execute();
		}
	}

	public static void startSync(
			Activity activity, 
			com.google.api.services.calendar.Calendar calendar, 
			ProgressDialog progressDialog){
		//new GCCalendarSyncManager(activity, calendar, progressDialog).execute();
		//
		// final String yooiiCalendarId;
		new GCCalendarSyncManager(activity, calendar, progressDialog).execute();


	}

	//static GoogleAccountCredential credential;
	public static void startSync(Activity activity, String accountName, ProgressDialog progressDialog){
		GoogleAccountCredential credential =
				GoogleAccountCredential.usingOAuth2(
						activity, Collections.singleton(CalendarScopes.CALENDAR));
		//SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		credential.setSelectedAccountName(accountName);
		// Calendar client
		HttpTransport transport = AndroidHttp.newCompatibleTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		com.google.api.services.calendar.Calendar _calendar = 
				new com.google.api.services.calendar.Calendar.Builder(
						transport, jsonFactory, credential)
		.setApplicationName("Timetable Kit")
		.build();

		//new GCCalendarSyncManager(activity, _calendar, progressDialog).execute();
		startSync(activity, _calendar, progressDialog);
	}

	@Override
	protected void onProgressUpdate(Integer... progress){
		if(progressDialog == null)
			return;

		Resources res = activity.getResources();
		String err = res.getString(R.string.progress_err);
		String checking = res.getString(R.string.progress_checking);
		String createCalendar = res.getString(R.string.progress_create_calendar);
		String clearEvent = res.getString(R.string.progress_clear_event);
		String uploadSchedule = res.getString(R.string.progress_upload_schedule);
		String downloadEvent = res.getString(R.string.progress_download_event);
		String done = res.getString(R.string.progress_done);
		switch(progress[0]){
		case PROGRESS_ERROR : 
			//			progressDialog.setMessage("Connection Stopped!");
			progressDialog.setMessage(err);
			break;

		case PROGRESS_CHECK_CALENDAR_EXISTS : 
			//			progressDialog.setMessage("Checking Calendar Data...");
			progressDialog.setMessage(checking);
			break;

		case PROGRESS_INSERT_CALENDAR : 
			//			progressDialog.setMessage("Creating calendar...");
			progressDialog.setMessage(createCalendar);
			break;

		case PROGRESS_CLEARING_EVENTS : 
			//			progressDialog.setMessage("Clearing Events...");
			progressDialog.setMessage(clearEvent);
			break;

		case PROGRESS_UPLOADING_EVENTS : 
			//			progressDialog.setMessage("Uploading Schedules...");
			progressDialog.setMessage(uploadSchedule);
			break;

		case PROGRESS_DOWNLOADING_EVENTS : 
			//			progressDialog.setMessage("Downloading Events...");
			progressDialog.setMessage(downloadEvent);
			break;

		case PROGRESS_DONE : 
			progressDialog.setMessage(done);
			//progressDialog.dismiss();
			break;
		}
	}

	@Override
	protected void onPostExecute(Boolean isDone) {
		if(!isDone){
			String warn = activity.getResources().getString(R.string.sync_failed);
			Toast.makeText(activity, warn, Toast.LENGTH_LONG).show();
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			return;
		}

		if(progressDialog != null){
			progressDialog.dismiss();
		}
		TimetableDataManager.writeDatasToExternalStorage();
		if(activity instanceof TimetableActivity){
			//ScheduleFragment refresh!
			TimetableActivity tmp = (TimetableActivity) activity;
			if(scheduleOverflows){
				String warn = activity.getString(R.string.fragment_schedule_schedulenum_overflows);
				Toast.makeText(activity, warn, Toast.LENGTH_SHORT)
				.show();
				scheduleOverflows = false;
			}
			int tmpCurItem = tmp.getViewPager().getCurrentItem();
			Fragment tmpFrag = tmp.getPagerAdapter().getItem(tmpCurItem);
			if(tmpFrag instanceof ScheduleFragment){
				//만약 현재 스케쥴페이지면
				((ScheduleFragment) tmpFrag).refresh();
			}
		}
		//		Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
	}
}
