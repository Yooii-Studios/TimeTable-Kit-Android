package com.sulga.yooiitable.google.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.sulga.yooiitable.constants.RequestCodes;

import java.io.IOException;

abstract class GCBasicAsyncTask extends AsyncTask<Void, Integer, Boolean> {

	protected final Activity activity;
	protected ProgressDialog progressDialog;

	protected final static int PROGRESS_ERROR = -1;
	protected static final int PROGRESS_CHECK_CALENDAR_EXISTS = 0;
	protected static final int PROGRESS_INSERT_CALENDAR = 1;
	protected static final int PROGRESS_CLEARING_EVENTS = 2;
	protected static final int PROGRESS_UPLOADING_EVENTS = 3;
	protected static final int PROGRESS_DOWNLOADING_EVENTS = 4;
	protected static final int PROGRESS_DONE = 5;

	public GCBasicAsyncTask(Activity activity, ProgressDialog progressDialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected final Boolean doInBackground(Void... ignored) {
		try {
			doInBackground();
			return true;
		} catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
			GCAccountManager.showErrorDialog(
					activity,  
					availabilityException.getConnectionStatusCode()
					);
			//publishProgress()
		} catch (UserRecoverableAuthIOException userRecoverableException) {
			activity.startActivityForResult(
					userRecoverableException.getIntent(), 
					RequestCodes.GCACCOUNT_REQUEST_CODE_AUTHORIZATION
					);
		} catch (IOException e) {
			//Utils.logAndShow(activity, CalendarSampleActivity.TAG, e);
			e.printStackTrace();
		}
		publishProgress(PROGRESS_ERROR);
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean success) {
		super.onPostExecute(success);
	}

	abstract protected void doInBackground() throws IOException;
}