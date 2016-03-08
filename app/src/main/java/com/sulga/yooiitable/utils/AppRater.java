package com.sulga.yooiitable.utils;

/*
public class AppRater {
	
	private final static int LAUNCHES_UNTIL_PROMPT = 10;

	public static void app_launched(Context context) {
		SharedPreferences prefs = PreferenceManager.wrap(context, "prefs", Context.MODE_PRIVATE);
		if (prefs.getBoolean("dontshowagain", false)) { return ; }

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);
		MyLog.d("AppRater", "launch_count : " + launch_count);
		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			showRateDialog(context, editor);			
		}

		editor.commit();
	}   

	public static void showRateDialog(final Context context, final SharedPreferences.Editor editor) {
		Resources res = context.getResources();
		String title = res.getString(R.string.dialog_rate_title);
		String message = res.getString(R.string.dialog_rate_message);
		View dialogView = View.inflate(context, R.layout.dialog_alert_simple, null);

		final Dialog dialog =  new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setCancelable(true)
		.setView(dialogView)
		.create();		

		Button cancel = (Button) dialogView.findViewById(R.id.dialog_alert_simple_button_cancel);
		String laterText = res.getString(R.string.dialog_rate_later);
		cancel.setText(laterText);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();

			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.dialog_alert_simple_button_ok);
		String rateText = res.getString(R.string.dialog_rate_now);
		ok.setText(rateText);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				context.startActivity(new Intent(Intent.ACTION_VIEW, 
						Uri.parse("market://details?id=com.sulga.yooiitable")));
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
*/