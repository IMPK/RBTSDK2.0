package com.onmobile.rbt.baseline.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.onmobile.rbt.baseline.R;

public class DurationPickerDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private IDurationPickerListener mDialogActionListener;
    private NumberPicker mDayPicker, mHourPicker, mMinutePicker;
    private int[] mDurationArray;

    public DurationPickerDialog(Context context, int[] durationArray, IDurationPickerListener dialogActionListener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.time_picker);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);

        mContext = context;
        mDurationArray = durationArray;
        mDialogActionListener = dialogActionListener;

        mDayPicker = findViewById(R.id.day_picker_control);
        mDayPicker.setMinValue(0);
        mDayPicker.setMaxValue(31);
        mDayPicker.setValue(mDurationArray[0]);
        String[] dayPickerValues = new String[32];
        for(int i=0; i<32; i++){
            dayPickerValues[i] = i + mContext.getString(R.string.picker_days);
        }
        mDayPicker.setDisplayedValues(dayPickerValues);
        mDayPicker.setWrapSelectorWheel(true);

        mHourPicker = findViewById(R.id.hour_picker_control);
        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(23);
        mHourPicker.setValue(mDurationArray[1]);
        String[] hourPickerValues = new String[24];
        for(int i=0; i<24; i++){
            hourPickerValues[i] = i + mContext.getString(R.string.picker_hours);
        }
        mHourPicker.setDisplayedValues(hourPickerValues);
        mHourPicker.setWrapSelectorWheel(true);

        mMinutePicker = findViewById(R.id.minute_picker_control);
        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setValue(mDurationArray[2]);
        String[] minutePickerValues = new String[60];
        for(int i=0; i<60; i++){
            minutePickerValues[i] = i + mContext.getString(R.string.picker_minutes);
        }
        mMinutePicker.setDisplayedValues(minutePickerValues);
        mMinutePicker.setWrapSelectorWheel(true);

        ImageButton cancelButton = findViewById(R.id.ib_back);
        ImageButton doneButton = findViewById(R.id.ib_done);

        cancelButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ib_back) {
            mDialogActionListener.onCancel();
        }else if(view.getId() == R.id.ib_done) {
            if(mDayPicker.getValue() == 0 &&
                    mHourPicker.getValue() == 0 &&
                    mMinutePicker.getValue() == 0){
                Toast.makeText(mContext, R.string.profile_tune_invalid_duration_message, Toast.LENGTH_SHORT).show();
                return;
            }
            mDurationArray[0] = mDayPicker.getValue();
            mDurationArray[1] = mHourPicker.getValue();
            mDurationArray[2] = mMinutePicker.getValue();
            mDialogActionListener.onDone(mDurationArray);
        }
    }


    public interface IDurationPickerListener {
        void onCancel();
        void onDone(int[] durationArray);
    }
}
