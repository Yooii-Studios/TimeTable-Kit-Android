package com.sulga.yooiitable.google.calendar;

import java.util.*;

import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.*;

import android.accounts.*;
import android.content.*;
import android.content.res.*;

import com.flurry.android.*;
import com.google.android.gms.auth.*;
import com.google.android.gms.common.*;
import com.google.api.client.extensions.android.http.*;
import com.google.api.client.googleapis.extensions.android.gms.auth.*;
import com.google.api.client.http.*;
import com.google.api.client.json.*;
import com.google.api.client.json.jackson2.*;
import com.google.api.services.calendar.*;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.*;

public class GCAccountManager {
	private static volatile GCAccountManager manager;
	public static GCAccountManager getInstance(){
		if(manager == null){
			synchronized(GCAccountManager.class){
				if(manager == null)
					manager = new GCAccountManager();				
			}
		}
		return manager;
	}

	Account[] accounts;
	AccountManager accountManager;
	public void showAccountSelectDialog(Activity activity){
		//accountManager = AccountManager.get(activity);
		if(checkPlayServices(activity) == false){
			return;
		}

		Intent pickAccountIntent = AccountPicker.newChooseAccountIntent(null, null,
				new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, true, null, null, null, null);
		activity.startActivityForResult(pickAccountIntent, 
				RequestCodes.GCACCOUNT_REQUEST_CODE_PICK_ACCOUNT);
	}

	private final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	private final JsonFactory jsonFactory = new JacksonFactory();
	private GoogleAccountCredential credential;
	public void showSyncGoogleCalendarDialog(
			final Activity activity, final String accountName){
		//Account account = getGoogleAccountByName(activity, accountName);
		final Resources res = activity.getResources();
		String uploadScheduleTitle = res.getString(R.string.title_gc_sync);
		String sync = res.getString(R.string.gc_sync_btn);
		
		AlertDialog syncDialog = new AlertDialog.Builder(activity)
//		.setTitle("Upload Schedule!")
		.setTitle(uploadScheduleTitle)
		.setMessage(accountName)
		.setPositiveButton(sync, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				com.google.api.services.calendar.Calendar calendar = 
						getCalendarClient(activity, accountName);

				ProgressDialog progressDialog = new ProgressDialog(activity);
				String loading = res.getString(R.string.loading);
				String connection = res.getString(R.string.starting_connection);
				progressDialog.setTitle(loading);
				progressDialog.setMessage(connection);
				progressDialog.setCancelable(false);
				//progressDialog.setMax(3);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();

				GCCalendarSyncManager.startSync(activity, calendar, progressDialog);
				FlurryAgent.logEvent(FlurryConstants.SYNC_GOOGLE_CALENDAR);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.create();

		syncDialog.show();
	}

	
	private com.google.api.services.calendar.Calendar getCalendarClient(
			Activity activity, String accountName){
		credential =
				GoogleAccountCredential.usingOAuth2(
						activity, Collections.singleton(CalendarScopes.CALENDAR));
		//SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		credential.setSelectedAccountName(accountName);

		// Calendar client
		String appname = activity.getResources().getString(R.string.app_name);
		return new com.google.api.services.calendar.Calendar.Builder(
				transport, jsonFactory, credential)
//		.setApplicationName("YooiiTable")
		.setApplicationName(appname)
		.build();
	}

	public static Account getGoogleAccountByName(Activity activity, String accountName) {
		if (accountName != null) {
			AccountManager am = AccountManager.get(activity);
			Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
			for (Account account : accounts) {
				if (accountName.equals(account.name)) {
					return account;
				}
			}
		}
		return null;
	}

	private boolean checkPlayServices(Activity activity) {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (status != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
				showErrorDialog(activity, status);
			} else {
				String warn = activity.getResources().getString(R.string.warning_gc_sync_device_reject);
				Toast.makeText(activity, warn, Toast.LENGTH_LONG).show();
			}
			return false;
		}
		return true;
	}

	static void showErrorDialog(Activity activity, int code) {
		GooglePlayServicesUtil.getErrorDialog(code, activity, RequestCodes.GCACCOUNT_REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
	}

	//	private boolean checkUserAccount() {
	//		String accountName = AccountUtils.getAccountName(this);
	//		if (accountName == null) {
	//			// Then the user was not found in the SharedPreferences. Either the
	//			// application deliberately removed the account, or the application's
	//			// data has been forcefully erased.
	//			showAccountPicker();
	//			return false;
	//		}
	//
	//		Account account = AccountUtils.getGoogleAccountByName(this, accountName);
	//		if (account == null) {
	//			// Then the account has since been removed.
	//			AccountUtils.removeAccount(this);
	//			showAccountPicker();
	//			return false;
	//		}
	//
	//		return true;
	//	}

	//	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/calendar";
	//	private final static int REQUEST_AUTHORIZATION = 0;
	//
	//	private class GetAuthTokenAsyncTask extends AsyncTask<String, Integer, String> {
	//
	//		private Activity activity;
	//		private String token;
	//		public GetAuthTokenAsyncTask(Activity activity){
	//			this.activity = activity;
	//		}
	//		@Override
	//		protected String doInBackground(String... args) {
	//			// TODO Auto-generated method stub
	//			try {
	//				token = GoogleAuthUtil.getToken(activity, args[0], SCOPE);
	//			} catch (UserRecoverableAuthException e1) {
	//				// TODO Auto-generated catch block
	//				//new ExceptionAsync(e1).execute(args[0], token);	
	//				activity.startActivityForResult(e1.getIntent(), REQUEST_AUTHORIZATION);
	//				e1.printStackTrace();
	//			} catch (IOException e1) {
	//				// TODO Auto-generated catch block
	//				e1.printStackTrace();
	//			} catch (GoogleAuthException e1) {
	//				// TODO Auto-generated catch block
	//				e1.printStackTrace();
	//			}
	//
	//			GoogleAccountCredential credential;
	//			credential =
	//					GoogleAccountCredential.usingOAuth2(
	//							activity, Collections.singleton(CalendarScopes.CALENDAR));
	//			//SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
	//			credential.setSelectedAccountName(args[0]);
	//			// Calendar client
	//			
	////			HttpTransport transport = AndroidHttp.newCompatibleTransport();
	////			JsonFactory jsonFactory = new JacksonFactory();
	////			client = new Calendar.Builder(
	////					transport, jsonFactory, credential)
	////			.setApplicationName("CalendarTest")
	////			.build();
	//
	//			
	//			//Account account = GCAccountManager.getGoogleAccountByName(activity, accountName)
	//			return null;
	//		}
	//		
	//		
	//	}


}


