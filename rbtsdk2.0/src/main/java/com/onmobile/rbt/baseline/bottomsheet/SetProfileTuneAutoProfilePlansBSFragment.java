package com.onmobile.rbt.baseline.bottomsheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.LowBattery;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Meeting;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Roaming;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Silent;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;

import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.ProfileUtil;
import com.onmobile.rbt.baseline.util.customview.RegularTextView;
import com.onmobile.rbt.baseline.widget.LabeledView;
import com.onmobile.rbt.baseline.widget.PlanView;
import com.onmobile.rbt.baseline.widget.PlanViewLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_ERROR;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_RURL;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_CONSENT_ACTIVITY;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_OFFLINE_CONSENT_ACTIVITY;

/**
 * Created by Shahbaz Akhtar on 28/12/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SetProfileTuneAutoProfilePlansBSFragment extends BaseFragment {

    static Boolean isTouched = false;
    RecyclerView upcomingMeetingsRecylerView;
    RecyclerView currentMeetingsRecylerView;
    NestedScrollView mOverAllMeetingList;
    boolean isSet = false;
    boolean isOff = false;
    private AppCompatTextView mTvSetTune, mTvVoiceDisplay, mTvProfileNameDisplay;
    private ViewGroup mLayoutPlayer, mLayoutDataContent, mLayoutDataLoading;
    private AppCompatImageView mIvPlayPause;
    private ContentLoadingProgressBar mProgressPlayer;
    private PlanViewLayout mPlanLayout;
    private LabeledView mLabeledViewAllCaller;
    private LinearLayout meeting_list_view;
    private LabeledView lv_all_caller_play_profile_tune;
    private RegularTextView set_for_all_txt;
    private RingBackToneDTO mBaseRingBackToneDTO;
    private RingBackToneDTO mCurrentRingBackToneDTO;
    private List<RingBackToneDTO> mProfileTuneRingBackList;
    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;
    private ContentLoadingProgressBar meetingProgressBar;
    private ProgressDialog progressDialog;
    private ProfileUtil mProfileUtil;
    private List<String> mVoiceList;
    private List<RingBackToneDTO> mVoiceFilteredRingbacks;
    private int mSelectedVoiceIndex = 0;
    private String mSelectedVoice;
    private int mSelectedLanguageIndex = 0;
    private HashMap<String, List<RingBackToneDTO>> mVoiceMap;
    private boolean mIsAutoProfileEnabled;
    private String mProfileTag;
    private boolean mAutoSetProfile;
    private RegularTextView noMeetings;
    private SwitchCompat mTodaysMeeting;
    private RelativeLayout todays_meeting_layout;
    private boolean mIsVisibleToUser;
    private LinearLayout mCallersChoiceLayout;
    private TextView mPlayForAllCallersInfoText;
    private CloseButtonToggleListener mCloseButtonToggleListener;

    private String mCallerSource;

    private String mNetworkType;

    /* new View.OnTouchListener() {
 @Override
 public boolean onTouch(View view, MotionEvent motionEvent) {
     if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
         SwitchCompat btn = (SwitchCompat) view;


     }
     return true;
 }
};*/

    /**
     * Plan change listener
     */
    private PlanViewLayout.PlanSelectedListener mPlanSelectedListener = this::planSelected;
    private BaselineMusicPlayer.MusicPreviewListener mMusicPreviewListener = new BaselineMusicPlayer.MusicPreviewListener() {
        @Override
        public void onPreviewPlaying(int progressStatus) {
            if (progressStatus < AppConstant.MAX_PROGRESS_TO_UPDATE_PLAYER)
                showPlayerPlayingTrack();
        }

        @Override
        public void onPreviewBuffering() {
            showPlayerBufferingTrack();
        }

        @Override
        public void onPreviewStopped() {
            showPlayerStopTrack();
        }

        @Override
        public void onPreviewCompleted() {
            showPlayerStopTrack();
        }

        @Override
        public void onPreviewError() {
            showPlayerStopTrack();
        }
    };


    /**
     * Click listener for different components
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == mTvSetTune.getId()) {

            } else if (id == mLayoutPlayer.getId()) {
                toggleTrack();
            }
        }
    };

    public static SetProfileTuneAutoProfilePlansBSFragment newInstance(String callerSource, RingBackToneDTO ringBackToneDTO, boolean autoSet) {
        SetProfileTuneAutoProfilePlansBSFragment fragment = new SetProfileTuneAutoProfilePlansBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putBoolean(AppConstant.KEY_AUTO_SET, autoSet);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
    }


    @NonNull
    @Override
    protected String initTag() {
        return SetProfileTuneAutoProfilePlansBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_auto_profile_tune_plans_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mBaseRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mAutoSetProfile = bundle.getBoolean(AppConstant.KEY_AUTO_SET, false);
        }
    }

    @Override
    protected void initComponents() {
//        autoProfileTuneRepository = new AutoProfileTuneRepository(getFragmentContext());
//        initProfileEnableStatus();
    }

    @Override
    protected void initViews(View view) {
       // meetingProfileSyncDBManager = new MeetingProfileSyncDBManager(getActivity());


        mLayoutDataLoading = view.findViewById(R.id.layout_data_loading_profile_tune);
        mLayoutDataContent = view.findViewById(R.id.layout_data_content_profile_tune);

        mTvVoiceDisplay = view.findViewById(R.id.tv_voice_profile_tune);
        mTvProfileNameDisplay = view.findViewById(R.id.tv_profile_name_profile_tune);

        mLayoutPlayer = view.findViewById(R.id.layout_play_profile_tune);
        mIvPlayPause = view.findViewById(R.id.iv_play_profile_tune);
        mProgressPlayer = view.findViewById(R.id.progress_play_profile_tune);

        mLabeledViewAllCaller = view.findViewById(R.id.lv_all_caller_play_profile_tune);

        mPlanLayout = view.findViewById(R.id.layout_plan);

        mTvSetTune = view.findViewById(R.id.tv_set_bottom_sheet);
        mTodaysMeeting = view.findViewById(R.id.todays_meeting_schedule);
        todays_meeting_layout = view.findViewById(R.id.todays_meeting);
        meeting_list_view = view.findViewById(R.id.meeting_listview);
        currentMeetingsRecylerView = view.findViewById(R.id.currentMeetingRecylerView);
        mOverAllMeetingList = view.findViewById(R.id.overall_meeting_list);
        upcomingMeetingsRecylerView = view.findViewById(R.id.upcomingMeetingRecylerView);

        noMeetings = view.findViewById(R.id.no_meetings_txt);
        set_for_all_txt = view.findViewById(R.id.set_for_all_txt);
        lv_all_caller_play_profile_tune = view.findViewById(R.id.lv_all_caller_play_profile_tune);
        meetingProgressBar = view.findViewById(R.id.pb_meeting);

        mCallersChoiceLayout = view.findViewById(R.id.caller_choice_layout);
        mPlayForAllCallersInfoText = view.findViewById(R.id.play_for_all_callers_info);
        if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
            mCallersChoiceLayout.setVisibility(View.VISIBLE);
            mPlayForAllCallersInfoText.setVisibility(View.GONE);
        } else {
            mCallersChoiceLayout.setVisibility(View.GONE);
            mPlayForAllCallersInfoText.setVisibility(View.VISIBLE);
        }

        mProfileUtil = ProfileUtil.getInstance();

        // initMeetingService();

        showDataLoading();
        resetPlayer();
        if (!mIsAutoProfileEnabled) {
            mTvSetTune.setEnabled(false);
        }

        mPlanLayout.addOnPlanSelectedListener(mPlanSelectedListener);
        mTvSetTune.setOnClickListener(mClickListener);
        mLayoutPlayer.setOnClickListener(mClickListener);
        //mTodaysMeeting.setOnTouchListener(mTodaysMeetingTouch);


       /* Meeting meetingProfile = HttpModuleMethodManager.getAutoProfileConfig().getMeeting();

        if (mProfileTag != null && mProfileTag.equalsIgnoreCase((meetingProfile.getTag()))) {
            checkCalendarPermissionState(new HandleCalendarPermissions.IHandlePermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    AutoProfileTuneRepository autoProfileTuneRepository = new AutoProfileTuneRepository(getFragmentContext());
                    mIsAutoProfileEnabled = autoProfileTuneRepository.getAutoTuneIsEnabled(mProfileTag);
                    updateButton();
                    if (mProfileTag.equalsIgnoreCase(meetingProfile.getTag()) && mIsAutoProfileEnabled) {
                        meetingProgressBar.setVisibility(View.VISIBLE);
                        initMeetingService();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    set_for_all_txt.setVisibility(View.GONE);
                    todays_meeting_layout.setVisibility(View.GONE);
                    currentMeetingsRecylerView.setVisibility(View.GONE);
                    upcomingMeetingsRecylerView.setVisibility(View.GONE);
                    meetingProgressBar.setVisibility(View.GONE);
                    mPlanLayout.setVisibility(View.VISIBLE);
                    mLabeledViewAllCaller.setVisibility(View.VISIBLE);
                    noMeetings.setVisibility(View.VISIBLE);
                }
            });
        }*/
    }

    @Override
    protected void bindViews(View view) {
        updateContent();
        fetchProfileTunes();
        //populatePlans();
    }

    @Override
    public void onStop() {
        stopTrack();
        super.onStop();
    }

    /**
     * Set callback for BottomSheet
     *
     * @param callback Listener
     * @return SetProfileTuneAutoProfilePlansBSFragment
     */
    public SetProfileTuneAutoProfilePlansBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * xxxx
     * Update UI contents according to data model
     */
    private void updateContent() {
        String displayName = mBaseRingBackToneDTO.getName();
        if (TextUtils.isEmpty(displayName)) displayName = mBaseRingBackToneDTO.getCaption();
        mTvProfileNameDisplay.setText(displayName);
        if (mCurrentRingBackToneDTO != null) {
            mTvVoiceDisplay.setText(mCurrentRingBackToneDTO.getPrimaryArtistName());
        }
    }

    /**
     * Fetch list of RingBackToneDTOList for current chart
     */
    private void fetchProfileTunes() {
        Silent silent = HttpModuleMethodManager.getAutoProfileConfig().getSilent();
        if (silent != null) {
            if (mBaseRingBackToneDTO.getId().equals(silent.getTrackid())) {
                ChartItemDTO chartItemDTO = new ChartItemDTO();
                chartItemDTO.setRingBackToneDTOS(new ArrayList<RingBackToneDTO>() {{
                    add(mBaseRingBackToneDTO);
                }});
                profileDataSuccess(chartItemDTO);
                return;
            }
        }
        BaselineApplication.getApplication().getRbtConnector().getProfileContents(mBaseRingBackToneDTO.getId(), new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                if (!isAdded()) return;
                profileDataSuccess(result);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                mPlanLayout.error(errMsg);
            }
        });
    }

    private void profileDataSuccess(ChartItemDTO result) {
        mProfileTuneRingBackList = result.getRingBackToneDTOS();
        if (mProfileTuneRingBackList.size() > 0) {
            mVoiceMap = mProfileUtil.getVoiceLanguageRingBackDTO(mProfileTuneRingBackList);
            mVoiceList = mProfileUtil.getVoiceKeys(mVoiceMap);
            setCurrentProfile();
        }
    }

    private void setCurrentProfile() {
        mSelectedVoice = mVoiceList.get(mSelectedVoiceIndex);
        mVoiceFilteredRingbacks = mProfileUtil.getRingbackToneList(mVoiceMap, mSelectedVoice);
        if (mProfileTag != null) {
            //mCurrentRingBackToneDTO = HttpModuleMethodManager.getJsonObject(autoProfileTuneRepository.getRBTObject(mProfileTag));
        } else {
            mCurrentRingBackToneDTO = mVoiceFilteredRingbacks.get(mSelectedLanguageIndex);
        }

        updateContent();
        showDataLoaded();
        populatePlans();
    }

    /**
     * Fetch plans from server and attach to UI
     */
    private void populatePlans() {
        mPlanLayout.loading();
        BaselineApplication.getApplication().getRbtConnector().checkUser(new AppBaselineCallback<Boolean>() {
            @Override
            public void success(Boolean result) {
                fetchPlans();
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                mPlanLayout.error(message);
                updatePeekHeight();
            }
        });

    }

    private void fetchPlans(){
        BaselineApplication.getApplication().getRbtConnector().getContent(mCurrentRingBackToneDTO.getId(), new AppBaselineCallback<RingBackToneDTO>() {
            @Override
            public void success(RingBackToneDTO result) {
                if (!isAdded()) return;
                mPlanLayout.resetData();
                mPlanLayout.setRingBackToneDTO(result);
                List<PricingSubscriptionDTO> pricingSubscriptionDTOS = result.getPricingSubscriptionDTOS();
                if (pricingSubscriptionDTOS != null && pricingSubscriptionDTOS.size() > 0) {
                    for (PricingSubscriptionDTO priceDTO : pricingSubscriptionDTOS) {
                        PlanView plan = new PlanView(getFragmentContext());
                        if (priceDTO != null && priceDTO.getRetail_priceObject() != null &&
                                priceDTO.getRetail_priceObject().getAmount() != null) {
                            mPlanLayout.addPlan(plan, priceDTO);
                        }
                        if (mPlanLayout.getPlanCount() == 1) plan.setChecked(true);
                    }
                } else {
                    List<PricingIndividualDTO> individualDTOS = result.getPricingIndividualDTOS();
                    if (individualDTOS != null && individualDTOS.size() > 0 && individualDTOS.get(0).getShortDescription() != null && !individualDTOS.get(0).getShortDescription().isEmpty()) {
                        mTvSetTune.setEnabled(mPlanLayout.addEmptyPlan(individualDTOS.get(0)));
                    } else {
                        mPlanLayout.error(getString(R.string.msg_empty_plan));
                    }
                }
                Meeting meetingProfile = HttpModuleMethodManager.getAutoProfileConfig().getMeeting();
                Silent silentProfile = HttpModuleMethodManager.getAutoProfileConfig().getSilent();
                LowBattery lowBatteryProfile = HttpModuleMethodManager.getAutoProfileConfig().getLowBattery();
                Roaming roamingProfile = HttpModuleMethodManager.getAutoProfileConfig().getRoaming();

                    String profileRange = null;

                    if (mProfileTag != null && mProfileTag.equalsIgnoreCase((silentProfile.getTag()))) {
                        profileRange = mProfileUtil.toDaysHoursAndMinutesServerFormat(60 * 60 * 1000);
                    } else if (mProfileTag != null && mProfileTag.equalsIgnoreCase((roamingProfile.getTag()))) {
                        profileRange = mProfileUtil.toDaysHoursAndMinutesServerFormat(24 * 60 * 60 * 1000);
                    } else if (mProfileTag != null && mProfileTag.equalsIgnoreCase((lowBatteryProfile.getTag()))) {
                        profileRange = mProfileUtil.toDaysHoursAndMinutesServerFormat(60 * 60 * 1000);
                    } else {
                        return;
                    }
                    //autoSetProfile(profileRange, mAutoSetProfile);

                updatePeekHeight();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                mPlanLayout.error(errMsg);
                updatePeekHeight();
            }
        });
    }



    /**
     * On plan selection
     *
     * @param plan PlanView to set plan
     */
    private void planSelected(PlanView plan) {
        if (plan == null) return;
        if (plan.isChecked()) {
            mTvSetTune.setEnabled(true);
            mPlanLayout.setFooterText(plan.getPriceDTO().getDescription());
        }
    }

    /**
     * Show data loading layout
     */
    private void showDataLoading() {
        mLayoutDataLoading.setVisibility(View.VISIBLE);
        mLayoutDataContent.setVisibility(View.GONE);
    }

    /**
     * Show data content layout
     */
    private void showDataLoaded() {
        mLayoutDataLoading.setVisibility(View.GONE);
        mLayoutDataContent.setVisibility(View.VISIBLE);
    }

    private void toggleTrack() {
        if (getMusicPlayer().isMediaPlaying()) {
            getMusicPlayer().stopMusic();
            return;
        } else {
            try {
                getMusicPlayer().stopMusic();
            } catch (Exception e) {

            }
        }
        if (mCurrentRingBackToneDTO != null) {
            String playURL = mCurrentRingBackToneDTO.getPreviewStreamUrl();
            getMusicPlayer().setMusicUrl(playURL);
            getMusicPlayer().setPreviewListener(mMusicPreviewListener);
            getMusicPlayer().startMusic(getContext());
        }
    }

    private void stopTrack() {
        if (getMusicPlayer().isMediaPlaying()) {
            getMusicPlayer().stopMusic();
        } else {
            try {
                getMusicPlayer().stopMusic();
            } catch (Exception e) {

            }
        }
    }

    private void resetPlayer() {
        showPlayerStopTrack();
    }

    private void showPlayerPlayingTrack() {
        mProgressPlayer.setVisibility(View.GONE);
        mIvPlayPause.setVisibility(View.VISIBLE);
        mIvPlayPause.setImageResource(R.drawable.ic_pause_accent_16dp);
    }

    private void showPlayerBufferingTrack() {
        mProgressPlayer.setVisibility(View.VISIBLE);
        mIvPlayPause.setVisibility(View.GONE);
    }

    private void showPlayerStopTrack() {
        mProgressPlayer.setVisibility(View.GONE);
        mIvPlayPause.setVisibility(View.VISIBLE);
        mIvPlayPause.setImageResource(R.drawable.ic_play_accent_16dp);
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(SetProfileTuneAutoProfilePlansBSFragment.this);
    }

    public void showProgress(boolean showProgress) {
        if (progressDialog == null) {
            progressDialog = AppDialog.getProgressDialog(getRootActivity());
            progressDialog.setCancelable(false);
        }
        if (showProgress) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == RESULT_OFFLINE_CONSENT_ACTIVITY) {
//                showProgress(false);
//                String status = (String) data.getSerializableExtra(EXTRA_OFFLINE_CONSENT_CALLBACK);
//                if (status != null && status.equalsIgnoreCase("success")) {
//                    if (mCallback != null)
//                        mCallback.next(SetProfileTuneAutoProfilePlansBSFragment.this, mCurrentRingBackToneDTO);
//                } else {
//                    if (mCallback != null)
//                        mCallback.done(SetProfileTuneAutoProfilePlansBSFragment.this, mCurrentRingBackToneDTO);
//                }
            } else if (requestCode == RESULT_CONSENT_ACTIVITY) {
                if (data.hasExtra(EXTRA_CG_ERROR)) {
                    if (EXTRA_CG_ERROR.equalsIgnoreCase(data.getStringExtra(EXTRA_CG_ERROR))) {
                        //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_ONLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_FAILURE);
                        return;
                    }
                }
                String cgrUrl = data.getStringExtra(EXTRA_CG_RURL);
                BaselineApplication.getApplication().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                    @Override
                    public void success(RUrlResponseDto result) {
                        if (!isAdded()) return;
                        showProgress(false);
                        if (mCallback != null)
                            mCallback.next(SetProfileTuneAutoProfilePlansBSFragment.this, mCurrentRingBackToneDTO);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);
                        if (mCallback != null)
                            mCallback.done(SetProfileTuneAutoProfilePlansBSFragment.this, mCurrentRingBackToneDTO);
//                        if (mCallback != null)
//                            mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);

                    }
                }, cgrUrl, null);
            }
        } else {
            if (requestCode == RESULT_CONSENT_ACTIVITY) {
                //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_ONLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
            }
            showProgress(false);
        }
    }


    /**
     * Set callback for BottomSheet
     *
     * @param callback Listener
     * @return SetProfileTuneAutoProfilePlansBSFragment
     */
    public SetProfileTuneAutoProfilePlansBSFragment setCloseButtonCallback(CloseButtonToggleListener callback) {
        this.mCloseButtonToggleListener = callback;
        return this;
    }

    private void showCGDialog(String message) {
        if (!isAdded()) return;
        /*customDialogFragment = CustomDialogFragment.
                newInstance(null, message, null, getString(R.string.cg_dialog_btn_ok), new ICustomDialogBtnHandler() {
                    @Override
                    public void doOkConfirmClick() {
                        //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_OFFLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                        customDialogFragment.dismiss();
                    }

                    @Override
                    public void doCancelConfirmClick() {
                        //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_OFFLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
                        customDialogFragment.dismiss();
                    }
                });
        customDialogFragment.setTitle(null);
        customDialogFragment.setCancelable(false);
        customDialogFragment.setMessage(message);
        customDialogFragment.setPositive_btn(null);
        customDialogFragment.setNegative_btn(getString(R.string.cg_dialog_btn_ok));
        customDialogFragment.show(getChildFragmentManager(), AppConstant.DialogConstants.DIALOG_FRAGMENT_NAME);*/
        AppDialog.getOkAlertDialog(getRootActivity(), null, message, getString(R.string.cg_dialog_btn_ok),
                false, false, null);
    }

    public interface CloseButtonToggleListener {
        void showButton();

        void hideButton();
    }

}
