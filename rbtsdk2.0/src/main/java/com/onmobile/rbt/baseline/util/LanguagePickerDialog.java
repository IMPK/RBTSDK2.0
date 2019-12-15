package com.onmobile.rbt.baseline.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;

import java.util.ArrayList;
import java.util.List;

public class LanguagePickerDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private ILanguagePickerListener mDialogActionListener;
    private NumberPicker mLanguagePicker;
    private List<RingBackToneDTO> mRingBackToneDTOList;
    private int mSelectedLanguageIndex;

    public LanguagePickerDialog(Context context, List<RingBackToneDTO> ringBackToneDTOList, int selectedLanguageIndex, ILanguagePickerListener dialogActionListener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.language_picker);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);


        mContext = context;
        mRingBackToneDTOList = ringBackToneDTOList;
        mSelectedLanguageIndex = selectedLanguageIndex;
        mDialogActionListener = dialogActionListener;

        List<String> languages = new ArrayList<>();
        for(RingBackToneDTO ringBackToneDTO : mRingBackToneDTOList){
            String language = ringBackToneDTO.getLanguage();
            String languageName = BaselineApplication.getApplication().getRbtConnector().getContentLanguageToDisplay().get(language);
            if(languageName != null){
                language = languageName;
            }
            languages.add(language);
        }


        mLanguagePicker = findViewById(R.id.language_picker_control);
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setMaxValue(languages.size()-1);
        mLanguagePicker.setValue(mSelectedLanguageIndex);
        mLanguagePicker.setDisplayedValues(languages.toArray(new String[languages.size()]));
        mLanguagePicker.setWrapSelectorWheel(true);

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
            mDialogActionListener.onDone(mLanguagePicker.getValue());
        }
    }


    public interface ILanguagePickerListener {
        void onCancel();
        void onDone(int selectedLanagugeIndex);
    }
}
