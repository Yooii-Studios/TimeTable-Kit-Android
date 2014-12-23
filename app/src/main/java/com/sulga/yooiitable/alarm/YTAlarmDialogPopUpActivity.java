package com.sulga.yooiitable.alarm;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;


public class YTAlarmDialogPopUpActivity extends Activity
{

    private int m_alarmId;
    private String m_alarmTitle;
    private String m_alarmMessage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get the alarm ID from the intent extra data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            m_alarmId = extras.getInt("AlarmId", -1);
            m_alarmTitle = extras.getString("Title");
            m_alarmMessage = extras.getString("Message");
        } else {
            m_alarmId = -1;
        }

        // Show the popup dialog
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        super.onCreateDialog(id);

        // Build the dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(m_alarmTitle);
        alert.setMessage(m_alarmMessage);
        alert.setCancelable(false);

        alert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int whichButton) {
                YTAlarmDialogPopUpActivity.this.finish();
            }
        });

        // Create and return the dialog
        AlertDialog dlg = alert.create();

        return dlg;
    }
}