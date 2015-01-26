package com.sulga.yooiitable.timetable.fragments.dialogbuilders;

import java.io.*;

import org.holoeverywhere.app.*;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.mylog.*;
import com.sulga.yooiitable.sharetable.*;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;
import com.sulga.yooiitable.timetable.*;
import com.sulga.yooiitable.timetable.fragments.*;
import com.sulga.yooiitable.utils.*;
import com.yooiistudios.common.ad.AdUtils;

public class ShareDataDialogBuilder {
	public static Dialog createDialog(final Context context, 
			final ConnectorState cs, 
			final Timetable timetable, 
			final TimetableFragment parentFrag){
		Resources res = context.getResources();
		String title = res.getString(R.string.dialog_sharetimetable_title);

		View dialogView = View.inflate(context, R.layout.dialog_share_data, null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		.setCancelable(true)
		.setView(dialogView)
		.create();

		TextView message = (TextView) dialogView.findViewById(R.id.dialog_share_data_message);
		message.setText(title);

		Button upload = (Button) dialogView.findViewById(R.id.dialog_share_data_upload);
		Button download = (Button) dialogView.findViewById(R.id.dialog_share_data_download);

		final ImageView banner = (ImageView)dialogView
				.findViewById(R.id.dialog_share_data_banner);
		Bitmap bitmap = TimetableDataManager.getConnectorBannerBitmap(context);
		if(bitmap == null){
			banner.setVisibility(View.GONE);
		}else{
			banner.setVisibility(View.VISIBLE);
			banner.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
			banner.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String linkUrl = TimetableDataManager.getConnectorBannerLinkUrl(context);
					context.startActivity(new Intent(Intent.ACTION_VIEW, 
							Uri.parse(linkUrl)));
				}
			});
		}

		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recycleBitmap(banner);
				dialog.dismiss();
				showUploadTimetableDialog(context, cs, timetable, parentFrag);
			}
		});

		download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( ( TimetableDataManager.getTimetables().size() >= 4 ) &&
						( TimetableDataManager.getCurrentFullVersionState(context) == false ) ){
//					ToastMaker.popupUnlockFullVersionToast(context,
//							ToastMaker.UNLOCK_FULL_VERSION_TOAST_OVERFLOW_PAGENUM);
                    AdUtils.showInHouseStoreAd(context);
					return;
				}else if(TimetableDataManager.getTimetables().size() >= TimetableActivity.TIMETABLE_MAX_LIMIT){
					String warn = context.getString(R.string.activity_timetable_max_timetable_count);
					ToastMaker.popupToastAtCenter(context, warn);
					return;
				}
				recycleBitmap(banner);
				showDownloadTimetableDialog(context, cs, timetable, parentFrag);
				dialog.dismiss();
			}
		});
		return dialog;
	}

	private static void showUploadTimetableDialog(final Context context, 
			final ConnectorState cs, 
			final Timetable timetable, 
			final TimetableFragment parentFrag){
		//		String uploadTitle = context.getResources().getString(R.string.dialog_sharedata_upload_title);
		String uploadCountPrompt = context.getString(R.string.dialog_share_data_send_file_num);
		final boolean isFullVersion = TimetableDataManager.getCurrentFullVersionState(context);
		final int uploadedTimetableCount = cs.getMaxUploadP() - TimetableDataManager
				.getTodayUploadAvailCount(context);
		MyLog.d("showUploadTimetableDialog", "cs.getUploadCount : " + cs.getMaxUploadP() 
				+ ", uploadAvail : " 
				+ TimetableDataManager.getTodayUploadAvailCount(context));
		final String maxUploadbleNumber = isFullVersion ? 
				Integer.toString(cs.getMaxUploadP())
				: Integer.toString(cs.getMaxUploadF());
//		int uploadedTimetableCount = cs.getTodayUploadAvailCount();
//		if(uploadedTimetableCount >= 3 && isFullVersion == false ||
//				uploadedTimetableCount >= 6 && isFullVersion == true){
//			String uploadLimited_A = context
//					.getString(R.string.dialog_share_data_upload_limited_A);
//			String uploadLimited_B = context
//					.getString(R.string.dialog_share_data_upload_limited_B);
//			
//			String uploadLimited = uploadLimited_A + 
//					" " + maxUploadbleNumber +
//					uploadLimited_B;
//			Toast.makeText(context, uploadLimited, Toast.LENGTH_SHORT)
//			.show();
//		}

		String message = uploadCountPrompt + " " 
					+ "(" 
					+ Integer.toString(uploadedTimetableCount) 
					+ " / " 
					+ maxUploadbleNumber 
					+ ")";

		String fileRemovedAfter24 = context.getString(R.string.dialog_share_data_dataremovedafter24hours);
		final String hint = context.getResources().getString(R.string.dialog_sharedata_hint);
		Dialog d = AlertDialogCreator.getConnectorUploadAlertDialog(context, 
				cs,
				InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
				hint,
				"", 
				//				"Upload Timetable", 
				message,
				fileRemovedAfter24,
				new AlertDialogCreator.EditTextDialogOnClickListener() {
			@Override
			public void onClick(EditText editText, Dialog d) {
				// TODO Auto-generated method stub
				MyLog.d("ShareDataTest", "Upload timetable : " + timetable + ", key : " + editText.getText().toString());
				if(editText.getText().toString().length() < 1){
//					Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
					return;
				}
				if(uploadedTimetableCount >= 3 && isFullVersion == false ||
						uploadedTimetableCount >= 6 && isFullVersion == true){
					String uploadLimited_A = context
							.getString(R.string.dialog_share_data_upload_limited_A);
					String uploadLimited_B = context
							.getString(R.string.dialog_share_data_upload_limited_B);
					
					String uploadLimited = uploadLimited_A + 
							" " + maxUploadbleNumber +
							uploadLimited_B;
					Toast.makeText(context, uploadLimited, Toast.LENGTH_SHORT)
					.show();
					return;
				}
				try {
					if(timetable.getThemeType() == ThemeType.Photo){
						Bitmap tmpBackBit = YTBitmapLoader.loadAutoScaledBitmapFromUri(
								context, 
								YTBitmapLoader.getPortraitCroppedImageUri(
										context,
										timetable.getId())
								);
						timetable.setBitmapByByteArray(SerializeBitmapUtils.bitmapToByteArray(tmpBackBit));
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "Error with photo theme..." +
							"please change your theme and retry uploading.", 
							Toast.LENGTH_SHORT).show();
					d.dismiss();
					e.printStackTrace();
					return;
				}
				String uuid = new DeviceUuidFactory(context).getDeviceUuid().toString();
				String name = UserNameFactory.getUserName(context);
				MyLog.d("showUploadTimetable", "name : " + name);
				TimetableNetworkManager.uploadTimetable(editText.getText().toString(), 
						timetable, 
						uuid,
						name,
						parentFrag);
				d.dismiss();
			}
		},
		new AlertDialogCreator.EditTextDialogOnClickListener() {

			@Override
			public void onClick(EditText editText, Dialog d) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
		//		AlertDialogCreator.getEditTextDialogEditText(d).setHint("HINTTTT");
		d.show();
	}

	private static void showDownloadTimetableDialog(final Context context, 
			final ConnectorState cs, final Timetable timetable, final TimetableFragment parentFrag){
		//		String downloadTitle = context.getResources().getString(R.string.dialog_sharedata_download_title);
		String downloadCountPrompt = context.getString(R.string.dialog_share_data_receive_file_num);
		final int downloadedTableCount = cs.getMaxDownloadP() - TimetableDataManager
				.getTodayDownloadAvailCount(context);
		final boolean isFullVersion = 
				TimetableDataManager.getCurrentFullVersionState(context);
		final String maxDownloadbleNumber = isFullVersion ? 
				Integer.toString(cs.getMaxDownloadP())
				: Integer.toString(cs.getMaxDownloadF());

//		if(downloadedTableCount >= cs.getMaxDownloadF() && isFullVersion == false ||
//				downloadedTableCount >= cs.getMaxDownloadP() && isFullVersion == true){
//			String downloadLimited_A = context
//					.getString(R.string.dialog_share_data_download_limited_A);
//			String downloadLimited_B = context
//					.getString(R.string.dialog_share_data_download_limited_B);
//			String downloadLimited = downloadLimited_A + 
//					" " + maxDownloadbleNumber + 
//					downloadLimited_B;
//			Toast.makeText(context, downloadLimited, Toast.LENGTH_SHORT)
//			.show();
//		}

		String message = downloadCountPrompt + " "  
				+ "(" 
				+ Integer.toString(downloadedTableCount) 
				+ " / " 
				+ maxDownloadbleNumber 
				+ ")";
		String fileRemovedAfter24 = context.getString(R.string.dialog_share_data_dataremovedafter24hours);
		final String hint = context.getResources().getString(R.string.dialog_sharedata_hint);
		AlertDialogCreator.getShareEditTextAlertDialog(context, 
				InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
				hint,
				"", 
				//				"Download Timetable", 
				message,
				fileRemovedAfter24,
				new AlertDialogCreator.EditTextDialogOnClickListener() {
			@Override
			public void onClick(EditText editText, Dialog d) {
				// TODO Auto-generated method stub
				if(editText.getText().toString().length() < 1){
//					Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
					return;
				}
				MyLog.d("showDownloadTimetableDialog", "downloadedTableCoun : " + downloadedTableCount + ", cs.getMaxDownloadF : " + cs.getMaxDownloadF());
				if(downloadedTableCount >= cs.getMaxDownloadF() && isFullVersion == false ||
						downloadedTableCount >= cs.getMaxDownloadP() && isFullVersion == true){
					String downloadLimited_A = context
							.getString(R.string.dialog_share_data_download_limited_A);
					String downloadLimited_B = context
							.getString(R.string.dialog_share_data_download_limited_B);
					String downloadLimited = downloadLimited_A + 
							" " + maxDownloadbleNumber + 
							downloadLimited_B;
					Toast.makeText(context, downloadLimited, Toast.LENGTH_SHORT)
					.show();
					return;
				}
				String uuid = new DeviceUuidFactory(context).getDeviceUuid().toString();
				String name = UserNameFactory.getUserName(context);
				MyLog.d("showDownloadTimetable", "download name : " + name);
				TimetableNetworkManager.downloadTimetable(editText.getText().toString(),
						uuid, name, parentFrag);
				d.dismiss();
			}
		}, 
		new AlertDialogCreator.EditTextDialogOnClickListener() {

			@Override
			public void onClick(EditText editText, Dialog d) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		}).show();
	}

	private static void recycleBitmap(ImageView iv) {
		Drawable d;
		if(iv != null && iv.getVisibility() == View.VISIBLE){
			d = iv.getDrawable();
		}else{
			return;
		}
		if (d != null && d instanceof BitmapDrawable) {
			MyLog.d("RecycleBitmap", "Recycling!");
			Bitmap b = ((BitmapDrawable)d).getBitmap();
			b.recycle();
		} // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
		if(d != null)
			d.setCallback(null);
	}

}
