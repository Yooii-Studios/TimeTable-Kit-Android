package com.sulga.yooiitable.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sulga.yooiitable.R;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.sharetable.ConnectorState;
import com.sulga.yooiitable.sharetable.UploadInfoObject;

import java.util.ArrayList;


public class AlertDialogCreator{

	public static AlertDialog getClearTimetableAlertDialog(String title,
														   String message,
														   Context context,
														   DialogInterface.OnClickListener okOnClick,
														   DialogInterface.OnClickListener cancelOnClick){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK",okOnClick)
		.setNegativeButton("Cancel",cancelOnClick); 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		return alertDialog;
	}

	public static Dialog getSimpleEditTextAlertDialog(Context context,
													  int inputType,
													  String hint,
													  String _initialEditTextText,
													  String title,
													  final EditTextDialogOnClickListener positiveOnClick,
													  final EditTextDialogOnClickListener negativeOnClick
			){

		View dialogView = View.inflate(context, 
				R.layout.dialog_alert_simple_edittext,
				null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();

		final EditText editText = (EditText)dialogView
				.findViewById(R.id.dialog_alert_simple_edittext_edittextview);
		editText.setInputType(inputType);
		if(hint != null){
			editText.setHint(hint);
		}
		String initialEditTextText = "";
		if(_initialEditTextText != null){
			initialEditTextText = _initialEditTextText;
		}
		editText.setText(initialEditTextText);

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.dialog_alert_simple_edittext_button_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				positiveOnClick.onClick(editText, dialog);
				//				dialog.dismiss();
			}
		});

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_alert_simple_edittext_button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				negativeOnClick.onClick(editText, dialog);
				//				dialog.dismiss();
			}
		});
		return dialog;

		//		final EditText input = new EditText(getSupportActionBarContext());
		//		String initialEditTextText = "";
		//		final EditText input = new EditText(context);
		//		input.setSelectAllOnFocus(true);
		//		input.setSingleLine();
		////		if(timetable.getTitle() != null){
		////			editTextInitial = timetable.getTitle();
		////		}
		//		if(_initialEditTextText != null){
		//			initialEditTextText = _initialEditTextText;
		//		}
		//		input.setText(initialEditTextText);
		//		
		//		// set title
		//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		//				context);
		//		// set title
		//		if(title != null)
		//			alertDialogBuilder.setTitle(title);
		//		// set dialog message
		//		alertDialogBuilder
		//		.setCancelable(true)
		//		.setView(input)
		//		.setPositiveButton("OK", positiveOnClick)
		//		.setNegativeButton("Cancel", negetiveOnClick);
		//		// create alert dialog
		//		final AlertDialog dialog = alertDialogBuilder.create();
		//		//에딧텍스트가 포커스를 받으면 키보드를 보여준다.
		//		input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		//			@Override
		//			public void onFocusChange(View v, boolean hasFocus) {
		//				if (hasFocus) {
		//					dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		//				}
		//			}
		//		});
		//		return dialog;
	}	

	public static Dialog getShareEditTextAlertDialog(final Context context,
			int inputType,
			String hint,
			String _initialEditTextText,
			String message,
			String message2,
			final EditTextDialogOnClickListener positiveOnClick,
			final EditTextDialogOnClickListener negativeOnClick
			){
		View dialogView = View.inflate(context, 
				R.layout.dialog_alert_connector_edittext,
				null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		//		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();

		final TextView messageView = (TextView)dialogView.findViewById(R.id.dialog_alert_simple_edittext_message);
		messageView.setText(message);
		final TextView messageView2 = (TextView)dialogView.findViewById(R.id.dialog_alert_simple_edittext_message_bottom);
		messageView2.setText(message2);
		final EditText editText = (EditText)dialogView
				.findViewById(R.id.dialog_alert_simple_edittext_edittextview);
		editText.setInputType(inputType);
		if(hint != null){
			editText.setHint(hint);
		}
		String initialEditTextText = "";
		if(_initialEditTextText != null){
			initialEditTextText = _initialEditTextText;
		}
		editText.setText(initialEditTextText);
		editText.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(s.length() > 0){
					messageView2.setVisibility(View.VISIBLE);
				}else{
					messageView2.setVisibility(View.INVISIBLE);
				}
			}
			public void afterTextChanged(Editable s) {}
		});
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});

		//커넥터 배너
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

		Button ok = (Button) dialogView.findViewById(R.id.dialog_alert_simple_edittext_button_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				positiveOnClick.onClick(editText, dialog);
			}
		});

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_alert_simple_edittext_button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				negativeOnClick.onClick(editText, dialog);
			}
		});
		return dialog;
	}	

	public static Dialog getConnectorUploadAlertDialog(final Context context,
			ConnectorState cs,
			int inputType,
			String hint,
			String _initialEditTextText,
			String message,
			String message2,
			final EditTextDialogOnClickListener positiveOnClick,
			final EditTextDialogOnClickListener negativeOnClick
			){
		View dialogView = View.inflate(context, 
				R.layout.dialog_alert_connector_upload,
				null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		//		.setTitle(title)
		.setCancelable(true)
		.setView(dialogView)
		.create();

		final TextView messageView = (TextView)dialogView
				.findViewById(R.id.dialog_alert_connector_upload_message);
		messageView.setText(message);
		final TextView messageView2 = (TextView)dialogView
				.findViewById(R.id.dialog_alert_connector_upload_message_bottom);
		messageView2.setText(message2);
		final EditText editText = (EditText)dialogView
				.findViewById(R.id.dialog_alert_connector_upload_edittextview);
		editText.setInputType(inputType);
		final TextView uploadInfoText = (TextView)dialogView
				.findViewById(R.id.dialog_alert_connector_upload_uploadinfo_text);
		uploadInfoText.setMovementMethod(new ScrollingMovementMethod());

		//커넥터 배너 셋업.
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

		if(hint != null){
			editText.setHint(hint);
		}
		String initialEditTextText = "";
		if(_initialEditTextText != null){
			initialEditTextText = _initialEditTextText;
		}
		editText.setText(initialEditTextText);
		editText.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(s.length() > 0){
					messageView2.setVisibility(View.VISIBLE);
				}else{
					messageView2.setVisibility(View.INVISIBLE);
				}
			}
			public void afterTextChanged(Editable s) {}
		});

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});

		ArrayList<UploadInfoObject> upInfos = cs.getUploadInfoArray();
		MyLog.d("getConnectorUploadInfo", "uio.size : " + upInfos.size());
		if(upInfos != null){
			if(upInfos.size() == 0){
				uploadInfoText.setVisibility(View.GONE);
			}else{
				uploadInfoText.setVisibility(View.VISIBLE);
			}
			String uploadInfoString = "";
			String _timesDownloaded_A = context
					.getString(R.string.dialog_connector_uploadinfo_received_times_A);
			String _timesDownloaded_B = context
					.getString(R.string.dialog_connector_uploadinfo_received_times_B);
			String noOneReceived = context
					.getString(R.string.dialog_connector_uploadinfo_received_noone);
			String received = context
					.getString(R.string.dialog_connector_uploadinfo_received);
			for(int i = 0 ; i < upInfos.size() ; i++){
				//최신 업로드인포가 제일 위에 보이게 하려고 루프를 반대로 돈다.
				UploadInfoObject uio = upInfos.get(i);
				ArrayList<String> downloadedNames = uio.getDownloadedNames();
				String firstReceiverName = "";
				if(downloadedNames != null && downloadedNames.size() != 0){
					firstReceiverName = downloadedNames.get(0);
				}
				if(uio.getDownloadedCount() == 1){
					uploadInfoString += uio.getUploadedKey() + " : " 
							+ firstReceiverName 
							+ received;
				}else if(uio.getDownloadedCount() == 0){
					uploadInfoString += uio.getUploadedKey() + " : " 
							+ noOneReceived;
				}else{
					uploadInfoString += uio.getUploadedKey() + " : " 
							+ firstReceiverName
							+ _timesDownloaded_A
							+ (uio.getDownloadedCount() - 1)
							+ _timesDownloaded_B;
				}
				if(i != upInfos.size() - 1){
					uploadInfoString += "\n";
				}
				MyLog.d("getConnectorUploadInfo", "uploadInfoString : " + uploadInfoString);
			}
			uploadInfoText.setText(uploadInfoString);
		}else{
			uploadInfoText.setVisibility(View.GONE);
		}
		Button ok = (Button) dialogView
				.findViewById(R.id.dialog_alert_connector_upload_button_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				positiveOnClick.onClick(editText, dialog);
			}
		});

		Button cancel = (Button) dialogView
				.findViewById(R.id.dialog_alert_connector_upload_button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				negativeOnClick.onClick(editText, dialog);
			}
		});
		return dialog;
	}	

	public static abstract class EditTextDialogOnClickListener {
		public abstract void onClick(EditText editText, Dialog d);
	}
}