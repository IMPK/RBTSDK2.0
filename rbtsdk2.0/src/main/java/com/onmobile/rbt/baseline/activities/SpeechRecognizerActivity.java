package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

/**
 * Created by Shahbaz Akhtar on 02/01/19.
 *
 * @author Shahbaz Akhtar
 */
public class SpeechRecognizerActivity extends BaseActivity {

    private ViewGroup mLayoutMicBg;
    private AppCompatTextView mTvSpeechIndicator;

    private GradientDrawable mMicBgGradient;

    private SpeechRecognizer mSpeechRecognizer;

    @NonNull
    @Override
    protected String initTag() {
        return SpeechRecognizerActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_speech_recognizer;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {
        mLayoutMicBg = findViewById(R.id.layout_speech_recognizer_mic_bg);
        mTvSpeechIndicator = findViewById(R.id.tv_speech_recognizer_indicator);
    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    protected void bindViews() {
        updateIndicator(false);
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        initSpeechRecognizer();
    }

    private void initSpeechRecognizer() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateIndicator(boolean listening) {

        if (mMicBgGradient == null) {
            float micBgRadius = getResources().getDimension(R.dimen.speech_recognizer_icon_radius);
            mMicBgGradient = new GradientDrawable();
            mMicBgGradient.setShape(GradientDrawable.RECTANGLE);
            mMicBgGradient.setCornerRadii(new float[]{micBgRadius, micBgRadius, micBgRadius, micBgRadius,
                    micBgRadius, micBgRadius, micBgRadius, micBgRadius});
        }

        mMicBgGradient.setColor(ContextCompat.getColor(this, listening ? R.color.speech_recognizer_ready_mic_bg : R.color.speech_recognizer_listening_mic_bg));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mLayoutMicBg.setBackground(mMicBgGradient);
        } else {
            mLayoutMicBg.setBackgroundDrawable(mMicBgGradient);
        }

        mTvSpeechIndicator.setText(getString(listening ? R.string.speech_recognizer_listening : R.string.speech_recognizer_ready));
    }

    private void stopMusic() {
        if (getMusicPlayer().isMediaPlaying()) {
            getMusicPlayer().stopMusic();
        }
        else{
            try{
                getMusicPlayer().stopMusic();
            }
            catch (Exception e){

            }
        }
    }
}

