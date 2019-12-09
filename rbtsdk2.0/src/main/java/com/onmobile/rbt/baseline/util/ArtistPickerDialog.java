package com.onmobile.rbt.baseline.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import com.onmobile.rbt.baseline.R;

import java.util.List;

public class ArtistPickerDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private IArtistPickerListener mDialogActionListener;
    private NumberPicker mArtistPicker;
    private List<String> mVoiceList;
    private int mSelectedVoiceIndex;

    public ArtistPickerDialog(Context context, List<String> voiceList, int selectedVoiceIndex, IArtistPickerListener dialogActionListener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.artist_picker);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);


        mContext = context;
        mVoiceList = voiceList;
        mSelectedVoiceIndex = selectedVoiceIndex;
        mDialogActionListener = dialogActionListener;

        mArtistPicker = findViewById(R.id.artist_picker_control);
        mArtistPicker.setMinValue(0);
        mArtistPicker.setMaxValue(mVoiceList.size()-1);
        mArtistPicker.setValue(mSelectedVoiceIndex);
        mArtistPicker.setDisplayedValues(mVoiceList.toArray(new String[mVoiceList.size()]));
        mArtistPicker.setWrapSelectorWheel(true);

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
            mDialogActionListener.onDone(mArtistPicker.getValue());
        }
    }


    public interface IArtistPickerListener {
        void onCancel();
        void onDone(int index);
    }
}
