package com.sulga.yooiitable.sharetable;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.ConnectorConstants;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.sharetable.TimeTableNetworkTool.DownloadTimetableResult;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;
import com.sulga.yooiitable.utils.DataToByteArrayConverter;

import java.util.HashMap;
import java.util.Map;

public class TimetableNetworkManager {

	public static void uploadTimetable(
			String key, 
			Timetable timetable, 
			String uuid,
			String name,
			TimetableFragment parentFrag){
		byte[] timetableByte = DataToByteArrayConverter.objectToByteArray(timetable);
		MyLog.d("byteTest", "uploading, length : " + timetableByte.length);
		new UploadAsyncTask(key, timetableByte, uuid, name, parentFrag).execute();
	}

	public static void downloadTimetable(
			String key,
			String uuid, 
			String name,
			TimetableFragment parentFrag){
		new DownloadAsyncTask(key, uuid, name, parentFrag).execute();
	}
	
	public static void updateConnectorUseInfo(
			String uuid,
			String name,
			boolean isFullVersion,
			Context context,
			OnFinishedConnectorAsync onFinishedListener){
		new GetUploadInfoAsyncTask(uuid, name, isFullVersion, context, onFinishedListener).execute();
	}
	
	public static void getBannerInfo(
			String uuid, String name,
			Context context,
			OnFinishedBannerInfoAsync onFinishedListener
			){
		new GetBannerInfoAsyncTask(uuid, name, context, onFinishedListener).execute();
	}
	
//	public static void setupDeviceIP(String uuid){
//		new SetupDeviceAsyncTask(uuid).execute();
//	}
	
	private static class UploadAsyncTask extends AsyncTask<Void, Void, Void> {
		private String key;
		private byte[] timetableData;
		private String uuid;
		private String name;
		private TimetableFragment parentFrag;
		private int ret = ConnectorConstants.RESULT_FAILED;
		//		private ProgressDialog dialog;
		public UploadAsyncTask(
				String key, byte[] timetableData, 
				String uuid, String name, 
				TimetableFragment parentFrag){
			this.key = key;
			this.timetableData = timetableData;
			this.uuid = uuid;
			this.name = name;
			this.parentFrag = parentFrag;
			//			dialog = new ProgressDialog(
			//					parentFrag.getSupportActivity());
		}
		@Override
		protected void onPreExecute() {
			//			dialog.setMessage("Uploading timetable...");
			//			dialog.show();
			super.onPreExecute();

			String uploadMessage = parentFrag.getResources().getString(R.string.dialog_sharedata_now_uploading_message);
			parentFrag.showProgressDialog(null, uploadMessage, 
					new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					UploadAsyncTask.this.cancel(false);
					String cancelled = parentFrag.getString(R.string.cancelled);
					Toast.makeText(parentFrag.getActivity(), cancelled, Toast.LENGTH_SHORT)
					.show();

					Map<String, String> info = new HashMap<String, String>();
					info.put(FlurryConstants.UPLOAD_INFO_RESULT_KEY, 
							FlurryConstants.UPLOAD_RESULT_CANCELLED);
					FlurryAgent.logEvent(FlurryConstants.UPLOAD_ACTION, info);

				}
			});
		}

		@Override
		protected Void doInBackground(Void... params) {
			ret = TimeTableNetworkTool.uploadTable(key,
					uuid,
					name,
					timetableData);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			//			dataInputBox.setText("");
			boolean isSucceed = false;
			parentFrag.dismissProgressDialog();
			Context ctx = parentFrag.getActivity();
			String warn = ctx.getResources().getString(
					R.string.connector_result_failed);
			if(ret == ConnectorConstants.RESULT_SUCCESS){
				warn = key + " : " + ctx.getString(
						R.string.fragment_timetable_share_upload_succeed);
				isSucceed = true;
			}else if(ret == ConnectorConstants.RESULT_NETWORK_ERROR){
				warn = ctx.getResources().getString(
						R.string.connector_result_network_error);
			}else if(ret == ConnectorConstants.RESULT_FAILED){
				warn = ctx.getResources().getString(
						R.string.connector_result_failed);
			}else if(ret == ConnectorConstants.RESULT_UPLOAD_LIMIT_REACHED){
				warn = ctx.getResources().getString(
						R.string.connector_result_data_limit_reached);
			}else if(ret == ConnectorConstants.RESULT_KEY_DUPLICATED){
				warn = ctx.getResources().getString(
						R.string.connector_result_duplicated_key);
			}else if(ret == ConnectorConstants.RESULT_WRONG_UUID){
				warn = "Wrong UUID...";
			}else{
				warn = ctx.getResources().getString(
						R.string.connector_result_failed);
			}
			Toast.makeText(parentFrag.getActivity(),
					warn,
					Toast.LENGTH_LONG).show();
			parentFrag.onUploadDataFinished(isSucceed);
		}
	}
	private static class DownloadAsyncTask extends AsyncTask<Void, Void, Void> {
		String key;
		String uuid;
		String name;
		DownloadTimetableResult timetableData;
		TimetableFragment parentFrag;

		public DownloadAsyncTask(String key, String uuid, String name, 
				TimetableFragment parentFrag){
			this.key = key;
			this.uuid = uuid;
			this.name = name;
			this.parentFrag = parentFrag;
		}
		//		String dataString;

		@Override
		protected void onPreExecute() {
			//			tokenString = tokenInputBox.getText().toString();
			//			dataString = "";
			//			dialog.setMessage("Downloading timetable...");
			String downloadMessage = parentFrag.getResources().getString(R.string.dialog_sharedata_now_downloading_message);
			parentFrag.showProgressDialog(null, downloadMessage,
					new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					DownloadAsyncTask.this.cancel(false);
					String cancelled = parentFrag.getString(R.string.cancelled);
					Toast.makeText(parentFrag.getActivity(), cancelled, Toast.LENGTH_SHORT)
					.show();
					Map<String, String> info = new HashMap<>();
					info.put(FlurryConstants.DOWNLOAD_INFO_RESULT_KEY, 
							FlurryConstants.DOWNLOAD_RESULT_CANCELLED);
					FlurryAgent.logEvent(FlurryConstants.DOWNLOAD_ACTION, info);
				}
			});
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			timetableData = 
					TimeTableNetworkTool.downloadTable(key, uuid, name);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			//			dataInputBox.setText(dataString);
			if(timetableData.ret != null)
				MyLog.d("byteTest", "download, length : " + timetableData.ret.length);
			parentFrag.dismissProgressDialog();

			String warn = null;
			Context ctx = parentFrag.getActivity();
			if(timetableData.resultCode == ConnectorConstants.RESULT_SUCCESS){
				//succeed in download = no need to show toast.
			}else if(timetableData.resultCode == ConnectorConstants.RESULT_NETWORK_ERROR){
				warn = ctx.getResources().getString(
						R.string.connector_result_network_error);
			}else if(timetableData.resultCode == ConnectorConstants.RESULT_FAILED){
				warn = ctx.getResources().getString(
						R.string.connector_result_failed);
			}else if(timetableData.resultCode == ConnectorConstants.RESULT_DOWNLOAD_LIMIT_REACHED){
				warn = ctx.getResources().getString(
						R.string.connector_result_data_limit_reached);
			}else if(timetableData.resultCode == ConnectorConstants.RESULT_NULL_DATA_DOWNLOADED){
				warn = ctx.getResources().getString(
						R.string.connector_result_data_null);
			}else if(timetableData.resultCode == ConnectorConstants.RESULT_WRONG_UUID){
				warn = "Wrong UUID...";
			}else if(timetableData.resultCode == ConnectorConstants.RESULT_TOKEN_DATA_NOT_EXISTS){
				warn = ctx.getString(
						R.string.connector_result_key_not_exists);
			}
			else{
				warn = ctx.getResources().getString(
						R.string.connector_result_failed);
			}
			if(warn != null){
				Toast.makeText(parentFrag.getActivity(),
						warn,
						Toast.LENGTH_LONG).show();
			}
			Timetable timetable = DataToByteArrayConverter.
					byteArrayToTimetableData(timetableData.ret);
			if(timetable != null)
				timetable.refreshId();
			parentFrag.onDownloadTimetableFinished(key, timetable);
			//			setDownloadedTimetable(timetable);
			super.onPostExecute(result);
		}
	}

	private static class GetUploadInfoAsyncTask extends AsyncTask<Void, Void, Void> {

//		TimetableFragment parentFrag;
		Context context;
		String uuid;
		String name;
		boolean isFullVersion;
		ConnectorState cs;
		OnFinishedConnectorAsync onFinishedListener;
		public GetUploadInfoAsyncTask(String uuid, String name, boolean isFullVersion,
				Context context,
				OnFinishedConnectorAsync onFinishedListener){
			this.uuid = uuid;
			this.name = name;
			this.isFullVersion = isFullVersion;
			this.context = context;
			this.onFinishedListener = onFinishedListener;
		}
		//		String dataString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			cs = TimeTableNetworkTool.getUploadInfo(uuid, name, isFullVersion);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {		
			if(cs.getResultCode() == ConnectorConstants.RESULT_SUCCESS){
				TimetableDataManager.setTodayDownloadAvailCount(
						context, 
						cs.getTodayDownloadAvailCount());
				TimetableDataManager.setTodayUploadAvailCount(
						context,
						cs.getTodayUploadAvailCount());
				if(onFinishedListener != null)
					onFinishedListener.onFinished(cs, true);
			}else{
				if(onFinishedListener != null)
					onFinishedListener.onFinished(cs, false);
			}
			MyLog.d("TimetableNetworkManager", cs.toString());
			super.onPostExecute(result);
		}
	}
	
//	private static class SetupDeviceAsyncTask extends AsyncTask<Void, Void, Void> {
//
////		TimetableFragment parentFrag;
////		Context context;
//		String uuid;
//		private int resultCode = ConnectorConstants.RESULT_FAILED;
//		public SetupDeviceAsyncTask(String uuid){
////				Context context,
////				OnFinishedConnectorAsync onFinishedListener){
//			this.uuid = uuid;
////			this.context = context;
////			this.onFinishedListener = onFinishedListener;
//		}
//		//		String dataString;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			resultCode = TimeTableNetworkTool.setupDevice(uuid);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {		
//			MyLog.d("TimetableNetworkManager", "setupDevice result : " + resultCode);
//			super.onPostExecute(result);
//		}
//	}
	
	private static class GetBannerInfoAsyncTask extends AsyncTask<Void, Void, Void> {

//		TimetableFragment parentFrag;
		Context context;
		String uuid;
		String name;
		BannerInfo bi;
		OnFinishedBannerInfoAsync onFinishedListener;
		public GetBannerInfoAsyncTask( 
				String uuid,
				String name,
				Context context,
				OnFinishedBannerInfoAsync onFinishedListener){
			this.context = context;
			this.uuid = uuid;
			this.name = name;
			this.onFinishedListener = onFinishedListener;
		}
		//		String dataString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			bi = TimeTableNetworkTool.getBannerInfo(uuid, name);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {		
			if(bi != null){
				//process banner managing after download info...
				if(onFinishedListener != null)
					onFinishedListener.onFinished(bi, true);
			}else{
				if(onFinishedListener != null)
					onFinishedListener.onFinished(bi, false);
			}
//			
//			if(cs.getResultCode() == ConnectorConstants.RESULT_SUCCESS){
//				TimetableDataManager.setTodayDownloadAvailCount(
//						context, 
//						cs.getTodayDownloadAvailCount());
//				TimetableDataManager.setTodayUploadAvailCount(
//						context,
//						cs.getTodayUploadAvailCount());
//				if(onFinishedListener != null)
//					onFinishedListener.onFinished(cs, true);
//			}else{
//				if(onFinishedListener != null)
//					onFinishedListener.onFinished(cs, false);
//			}
//			MyLog.d("TimetableNetworkManager", cs.toString());
			super.onPostExecute(result);
		}
	}

	public interface OnFinishedBannerInfoAsync{
		void onFinished(BannerInfo bi, boolean isSucceed);
	}
	
	public interface OnFinishedConnectorAsync{
		void onFinished(ConnectorState cs, boolean isSucceed);
	}

}