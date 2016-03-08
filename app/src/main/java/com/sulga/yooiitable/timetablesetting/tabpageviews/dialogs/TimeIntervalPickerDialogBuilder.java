package com.sulga.yooiitable.timetablesetting.tabpageviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.sulga.yooiitable.R;


/**
 * Created by fldldi0212 on 15. 1. 14..
 *
 * TimeIntervalPickerDialogBuilder
 */
public class TimeIntervalPickerDialogBuilder {
    public static Dialog build(Context context, String title, int originalValue, int periodNum,
                               final OnTimeIntervalPickedListener onTimeIntervalPickedListener) {
        View dialogView = View.inflate(context, R.layout.dialog_numberpicker_layout, null);

        final Dialog dialog =  new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(true)
                .setView(dialogView)
                .create();
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        int maxTimeInterval = 180;
        int ableTimeInterval = 60 * 24 / periodNum;
        maxTimeInterval = ableTimeInterval > maxTimeInterval ?
                180 : ableTimeInterval;
        numberPicker.setMaxValue(maxTimeInterval);
        numberPicker.setMinValue(30);
        numberPicker.setValue(originalValue);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setClickable(false);
        numberPicker.setEnabled(true);

        Button ok = (Button) dialogView.findViewById(R.id.dialog_settings_numberpicker_ok);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onTimeIntervalPickedListener.onTimeIntervalPicked(numberPicker.getValue());
                dialog.dismiss();
            }
        });
        Button cancel = (Button) dialogView.findViewById(R.id.dialog_settings_numberpicker_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface OnTimeIntervalPickedListener{
        void onTimeIntervalPicked(int pickedNumber);
    }
}
