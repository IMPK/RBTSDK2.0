package com.onmobile.rbt.baseline.listener;

import android.text.Editable;

/**
 * Created by Shahbaz Akhtar on 06/11/18.
 *
 * @author Shahbaz Akhtar
 */
public interface ToolbarSearchListener {
    void onBackPressed();
    void beforeTextChanged(String text, int start, int count, int after);
    void onTextChanged(String text, int start, int before, int count);
    void afterTextChanged(Editable editable);
    void onSubmitQuery(String text);
    void onTextClear();
    void onVoiceClick();
}
