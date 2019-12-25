package com.onmobile.rbt.baseline.bottomsheet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.ArtistPickerDialog;
import com.onmobile.rbt.baseline.util.FontUtils;
import com.onmobile.rbt.baseline.util.NameTuneLanguagePickerDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CreateNameTuneBSFragment extends BaseFragment implements View.OnClickListener {

    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;

    private Animation mAnimBottomUp, mAnimBottomDown;
    private String searchText, searchLanguage;
    private TextView info1;
    private TextView mCreateNameTune;
    private ProgressDialog mProgressDialog;
    private List<String> mVoiceList, mLanguageList;
    private int mVoiceSelectedIndex, mLanguageSelectedIndex;
    private String mVoiceSelected, mLanguageSelected;
    private TextView mVoiceText, mLanguageText;
    private RelativeLayout mArtistPickerLayout, mLanguagePickerLayout;
    private ArtistPickerDialog mArtistPickerDialog;
    private NameTuneLanguagePickerDialog mLanguagePickerDialog;
    private EditText mNameTuneEditText;

    public static CreateNameTuneBSFragment newInstance(String searchText, String searchLanguage) {
        CreateNameTuneBSFragment fragment = new CreateNameTuneBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_DATA_SEARCH_QUERY, searchText);
        bundle.putString(AppConstant.KEY_DATA_SEARCH_LANGUAGE, searchLanguage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return CreateNameTuneBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_create_name_tune_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            searchText = bundle.getString(AppConstant.KEY_DATA_SEARCH_QUERY);
            searchLanguage = bundle.getString(AppConstant.KEY_DATA_SEARCH_LANGUAGE);
        }

        mVoiceList = new ArrayList<>();
        mVoiceList.add("Male");
        mVoiceList.add("Female");
        mVoiceSelectedIndex = 0;
        mVoiceSelected = mVoiceList.get(mVoiceSelectedIndex);

        mLanguageList = new ArrayList<>();
        mLanguageList.addAll(AppManager.getInstance().getRbtConnector().getCreateNameTuneLanguageList());
        mLanguageSelectedIndex = 0;
        mLanguageSelected = mLanguageList.get(mLanguageSelectedIndex);
    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected void initViews(View view) {
        info1 = view.findViewById(R.id.create_nametune_info_1);
        mCreateNameTune = view.findViewById(R.id.create_name_tune_btn);
        mCreateNameTune.setOnClickListener(this);
        mVoiceText = view.findViewById(R.id.voice_text);
        mLanguageText = view.findViewById(R.id.language_text);
        mArtistPickerLayout = view.findViewById(R.id.artist_picker_layout);
        mLanguagePickerLayout = view.findViewById(R.id.language_picker_layout);
        mArtistPickerLayout.setOnClickListener(this);
        mLanguagePickerLayout.setOnClickListener(this);
        mNameTuneEditText = view.findViewById(R.id.name_tune_edit_text);
        FontUtils.setMediumFont(getContext(), mNameTuneEditText);
    }

    @Override
    protected void bindViews(View view) {
        String info1Text = String.format(getResources().getString(R.string.create_name_tune_info1), searchText);
        info1.setText(info1Text);
        mVoiceText.setText(mVoiceSelected);
        mLanguageText.setText(mLanguageSelected);
        mNameTuneEditText.setText(searchText);
    }

    public Animation getAnimBottomUp() {
        if (mAnimBottomUp == null)
            mAnimBottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        return mAnimBottomUp;
    }

    public Animation getAnimBottomDown() {
        if (mAnimBottomDown == null)
            mAnimBottomDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
        return mAnimBottomDown;
    }

    private abstract class ShowcaseHolder {
        protected abstract void initViews(View view);

        protected abstract void bindViews(View view);
    }

    public CreateNameTuneBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(CreateNameTuneBSFragment.this);
    }

    public void showProgress(boolean showProgress) {
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(getRootActivity());
            mProgressDialog.setCancelable(false);
        }
        if (showProgress) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.artist_picker_layout) {
            mArtistPickerDialog = new ArtistPickerDialog(getContext(), mVoiceList, mVoiceSelectedIndex, new ArtistPickerDialog.IArtistPickerListener() {
                @Override
                public void onCancel() {
                    mArtistPickerDialog.dismiss();
                }

                @Override
                public void onDone(int index) {
                    mArtistPickerDialog.dismiss();
                    if (mVoiceSelectedIndex != index) {
                        mVoiceSelectedIndex = index;
                        mVoiceSelected = mVoiceList.get(mVoiceSelectedIndex);
                        mVoiceText.setText(mVoiceSelected);
                    }
                }
            });
            mArtistPickerDialog.show();
        }else if(view.getId() == R.id.language_picker_layout) {
            mLanguagePickerDialog = new NameTuneLanguagePickerDialog(getContext(), mLanguageList, mLanguageSelectedIndex, new NameTuneLanguagePickerDialog.ILanguagePickerListener() {
                @Override
                public void onCancel() {
                    mLanguagePickerDialog.dismiss();
                }

                @Override
                public void onDone(int selectedLanagugeIndex) {
                    mLanguagePickerDialog.dismiss();
                    if (mLanguageSelectedIndex != selectedLanagugeIndex) {
                        mLanguageSelectedIndex = selectedLanagugeIndex;
                        mLanguageSelected = mLanguageList.get(mLanguageSelectedIndex);
                        mLanguageText.setText(mLanguageSelected);
                    }

                }
            });
            mLanguagePickerDialog.show();
        }else if(view.getId() == R.id.create_name_tune_btn) {
            showProgress(true);
            AppManager.getInstance().getRbtConnector().createNameTune(searchText, mLanguageSelected, new AppBaselineCallback<String>() {
                @Override
                public void success(String result) {
                    if (!isAdded()) return;
                    showProgress(false);
                    getRootActivity().showShortToast(getString(R.string.create_name_tune_success));
                    if (mCallback != null) {
                        mCallback.done(CreateNameTuneBSFragment.this, null);
                    }

                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    showProgress(false);
                    getRootActivity().showShortToast(errMsg);
                }
            });
        }
    }
}
