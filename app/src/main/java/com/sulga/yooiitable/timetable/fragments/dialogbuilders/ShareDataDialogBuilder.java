package com.sulga.yooiitable.timetable.fragments.dialogbuilders;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.Timetable;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.sharetable.ConnectorState;
import com.sulga.yooiitable.sharetable.TimetableNetworkManager;
import com.sulga.yooiitable.theme.YTTimetableTheme.ThemeType;
import com.sulga.yooiitable.timetable.TimetableActivity;
import com.sulga.yooiitable.timetable.fragments.TimetableFragment;
import com.sulga.yooiitable.utils.AlertDialogCreator;
import com.sulga.yooiitable.utils.DeviceUuidFactory;
import com.sulga.yooiitable.utils.SerializeBitmapUtils;
import com.sulga.yooiitable.utils.ToastMaker;
import com.sulga.yooiitable.utils.UserNameFactory;
import com.sulga.yooiitable.utils.YTBitmapLoader;
import com.yooiistudios.common.ad.AdUtils;

import java.io.FileNotFoundException;

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
				if( ( TimetableDataManager.getTimetables().size() >= 4 ) &&
						(!TimetableDataManager.getCurrentFullVersionState(context)) ){
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
				MyLog.d("ShareDataTest", "Upload timetable : " + timetable + ", key : " + editText.getText().toString());
				if(editText.getText().toString().length() < 1){
//					Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
					return;
				}
				if(uploadedTimetableCount >= 3 && !isFullVersion ||
						uploadedTimetableCount >= 6 && isFullVersion){
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
				d.dismiss();
			}
		});
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
		final String maxDownloadableNumber = isFullVersion ?
				Integer.toString(cs.getMaxDownloadP())
				: Integer.toString(cs.getMaxDownloadF());

		String message = downloadCountPrompt + " "
				+ "(" 
				+ Integer.toString(downloadedTableCount) 
				+ " / " 
				+ maxDownloadableNumber
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
				if(editText.getText().toString().length() < 1){
//					Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
					return;
				}
				MyLog.d("showDownloadTimetableDialog", "downloadedTableCoun : " + downloadedTableCount + ", cs.getMaxDownloadF : " + cs.getMaxDownloadF());
				if(downloadedTableCount >= cs.getMaxDownloadF() && !isFullVersion ||
						downloadedTableCount >= cs.getMaxDownloadP() && isFullVersion){
					String downloadLimited_A = context
							.getString(R.string.dialog_share_data_download_limited_A);
					String downloadLimited_B = context
							.getString(R.string.dialog_share_data_download_limited_B);
					String downloadLimited = downloadLimited_A + 
							" " + maxDownloadableNumber +
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
