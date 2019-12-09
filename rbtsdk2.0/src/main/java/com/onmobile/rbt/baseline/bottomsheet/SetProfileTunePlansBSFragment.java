package com.onmobile.rbt.baseline.bottomsheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onmobile.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ScheduleDTO;
import com.onmobile.baseline.http.httpmodulemanagers.IPreBuyUDSCheck;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.CGWebViewActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.ApiConfig;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.GiftnNormalPlanConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.PurchaseConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.ShuffleUpgradeDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.ArtistPickerDialog;
import com.onmobile.rbt.baseline.util.ContactDetailProvider;
import com.onmobile.rbt.baseline.util.DurationPickerDialog;
import com.onmobile.rbt.baseline.util.IContactDetailProvider;
import com.onmobile.rbt.baseline.util.LanguagePickerDialog;
import com.onmobile.rbt.baseline.util.ProfileUtil;
import com.onmobile.rbt.baseline.widget.PlanView;
import com.onmobile.rbt.baseline.widget.PlanViewLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

import static android.app.Activity.RESULT_OK;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_ERROR;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_RURL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CONSCENT_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_R_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_CONSENT_ACTIVITY;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_OFFLINE_CONSENT_ACTIVITY;

/**
 * Created by Shahbaz Akhtar on 16/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SetProfileTunePlansBSFragment extends BaseFragment {

    private AppCompatTextView mTvSetTune, mTvVoiceChoice, mTvDurationChoice, mTvLanguageChoice,
            mTvVoiceDisplay, mTvProfileNameDisplay;
    private ViewGroup mLayoutPlayer, mLayoutDataContent, mLayoutDataLoading;
    private AppCompatImageView mIvPlayPause;
    private ContentLoadingProgressBar mProgressPlayer;
    private PlanViewLayout mPlanLayout;
    private LinearLayout mVoiceLayout, mTimeLayout, mLanguageLayout;

    private RingBackToneDTO mBaseRingBackToneDTO;
    private RingBackToneDTO mCurrentRingBackToneDTO;
    private List<RingBackToneDTO> mProfileTuneRingBackList;
    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;

    private PricingSubscriptionDTO pricingSubscriptionDTO = null;
    private PricingIndividualDTO pricingIndividualDTO = null;
    private ProgressDialog progressDialog;
    private LanguagePickerDialog mLanguagePickerDialog;
    private ArtistPickerDialog mArtistPickerDialog;
    private DurationPickerDialog mDurationPickerDialog;

    private ProfileUtil mProfileUtil;
    private List<String> mVoiceList;
    private List<RingBackToneDTO> mVoiceFilteredRingbacks;
    private int mSelectedVoiceIndex = 0;
    private String mSelectedVoice;
    private int mSelectedLanguageIndex = 0;
    private String mSelectedLanguage;
    private HashMap<String, List<RingBackToneDTO>> mVoiceMap;
    private long durationInMillis;
    private int[] mDurationArray;
    private boolean isGiftPresent = false;
    private ContactModelDTO mContactModelDTO;
    private GetChildInfoResponseDTO childInfo;
    //boolean isShowConfimationPopup = true;
    String mConfirmationPopUpDescription = null;
    private LinearLayout mCallersChoiceLayout;
    private TextView mPlayForAllCallersInfoText;

    private String mCallerSource;

    private String mNetworkType;
    private boolean mIsUpgrade = false;

    public static SetProfileTunePlansBSFragment newInstance(String callerSource, RingBackToneDTO ringBackToneDTO) {
        SetProfileTunePlansBSFragment fragment = new SetProfileTunePlansBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return SetProfileTunePlansBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_profile_tune_plans_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mBaseRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {

        mLayoutDataLoading = view.findViewById(R.id.layout_data_loading_profile_tune);
        mLayoutDataContent = view.findViewById(R.id.layout_data_content_profile_tune);

        mTvVoiceChoice = view.findViewById(R.id.tv_voice_choice_profile_tune);
        mTvDurationChoice = view.findViewById(R.id.tv_duration_choice_profile_tune);
        mTvLanguageChoice = view.findViewById(R.id.tv_language_choice_profile_tune);
        mTvVoiceDisplay = view.findViewById(R.id.tv_voice_profile_tune);
        mTvProfileNameDisplay = view.findViewById(R.id.tv_profile_name_profile_tune);

        mLayoutPlayer = view.findViewById(R.id.layout_play_profile_tune);
        mIvPlayPause = view.findViewById(R.id.iv_play_profile_tune);
        mProgressPlayer = view.findViewById(R.id.progress_play_profile_tune);

        mVoiceLayout = view.findViewById(R.id.voice_layout);
        mTimeLayout = view.findViewById(R.id.time_layout);
        mLanguageLayout = view.findViewById(R.id.language_layout);
        mVoiceLayout.setOnClickListener(mClickListener);
        mTimeLayout.setOnClickListener(mClickListener);
        mLanguageLayout.setOnClickListener(mClickListener);

        mPlanLayout = view.findViewById(R.id.layout_plan);

        mTvSetTune = view.findViewById(R.id.tv_set_bottom_sheet);

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

        showDataLoading();
        resetPlayer();
        mTvSetTune.setEnabled(false);

        mPlanLayout.addOnPlanSelectedListener(mPlanSelectedListener);
        mTvSetTune.setOnClickListener(mClickListener);
        mLayoutPlayer.setOnClickListener(mClickListener);

        updateDuration(ApiConfig.MANUAL_PROFILE_TUNES_DEFAULT_DURATION);
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
     * @return SetShufflePlansBSFragment
     */
    public SetProfileTunePlansBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Update UI contents according to data model
     */
    private void updateContent() {
        mTvProfileNameDisplay.setText(mBaseRingBackToneDTO.getName() != null ? mBaseRingBackToneDTO.getName() : mBaseRingBackToneDTO.getTrackName());
        if (mCurrentRingBackToneDTO != null) {
            mTvVoiceChoice.setText(mCurrentRingBackToneDTO.getPrimaryArtistName());
            List<PlayRuleDTO> playRuleDTOList = getPlayRule(mCurrentRingBackToneDTO);
            if (playRuleDTOList != null && playRuleDTOList.size() > 0) {
                ScheduleDTO scheduleDTO = playRuleDTOList.get(playRuleDTOList.size() - 1).getSchedule();
                long diff = ProfileUtil.getDifferenceInMillis(scheduleDTO);
                updateDuration(diff == -1 ? ApiConfig.MANUAL_PROFILE_TUNES_DEFAULT_DURATION : TimeUnit.MILLISECONDS.toMinutes(diff));
            } else {
                updateDuration(ApiConfig.MANUAL_PROFILE_TUNES_DEFAULT_DURATION);
            }
            mTvDurationChoice.setText(mProfileUtil.toDaysHoursAndMinutes(getActivity(), durationInMillis));
            String language = mCurrentRingBackToneDTO.getLanguage();
            if (!TextUtils.isEmpty(language)) {
                language = BaselineApplication.getApplication().getRbtConnector().getContentLanguageToDisplay().get(language);
                if (language == null) {
                    language = mCurrentRingBackToneDTO.getLanguage();
                }
            }
            mTvLanguageChoice.setText(language);
            mTvVoiceDisplay.setText(mCurrentRingBackToneDTO.getPrimaryArtistName());
        }
    }

    /**
     * Fetch list of RingBackToneDTOList for current chart
     */
    private void fetchProfileTunes() {
        if (mBaseRingBackToneDTO.getType().equalsIgnoreCase("chart")) {
            BaselineApplication.getApplication().getRbtConnector().getProfileContents(mBaseRingBackToneDTO.getId(), new AppBaselineCallback<ChartItemDTO>() {
                @Override
                public void success(ChartItemDTO result) {
                    if (!isAdded()) return;
                    mProfileTuneRingBackList = result.getRingBackToneDTOS();
                    if (mProfileTuneRingBackList.size() > 0) {
                        mVoiceMap = mProfileUtil.getVoiceLanguageRingBackDTO(mProfileTuneRingBackList);
                        mVoiceList = mProfileUtil.getVoiceKeys(mVoiceMap);
                        for (RingBackToneDTO item : mProfileTuneRingBackList) {
                            if (BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(item.getId())) {
                                int voiceIndex = 0, languageIndex = 0;
                                for (String voice : mVoiceList) {
                                    if (voice.equals(item.getPrimaryArtistName())) break;
                                    voiceIndex++;
                                }
                                String selectedVoice = mVoiceList.get(voiceIndex);
                                List<RingBackToneDTO> voiceFilteredRingbacks = mProfileUtil.getRingbackToneList(mVoiceMap, selectedVoice);
                                if (voiceFilteredRingbacks != null) {
                                    for (RingBackToneDTO finalItem : voiceFilteredRingbacks) {
                                        if (isSelected(finalItem.getId())) break;
                                        languageIndex++;
                                    }
                                    mSelectedVoiceIndex = voiceIndex;
                                    mSelectedLanguageIndex = languageIndex;
                                }
                                break;
                            }
                        }
                        setCurrentProfile();
                    }
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    mPlanLayout.error(errMsg);
                }
            });
        } else if (mBaseRingBackToneDTO.getType().equalsIgnoreCase("ringback")) {
            BaselineApplication.getApplication().getRbtConnector().getContent(mBaseRingBackToneDTO.getId(), new AppBaselineCallback<RingBackToneDTO>() {
                @Override
                public void success(RingBackToneDTO result) {
                    if (!isAdded()) return;
                    mPlanLayout.resetData();
                    mPlanLayout.setRingBackToneDTO(result);
                    mCurrentRingBackToneDTO = result;
                    List<PricingSubscriptionDTO> pricingSubscriptionDTOS = result.getPricingSubscriptionDTOS();
                    if (pricingSubscriptionDTOS != null && pricingSubscriptionDTOS.size() > 0) {
                        for (PricingSubscriptionDTO priceDTO : pricingSubscriptionDTOS) {
                            PlanView plan = new PlanView(getFragmentContext());
                            if (priceDTO != null && priceDTO.getRetail_priceObject() != null
                                    && priceDTO.getRetail_priceObject().getAmount() != null) {
                                mPlanLayout.addPlan(plan, priceDTO);
                            }
                            if (mPlanLayout.getPlanCount() == 1)
                                plan.setChecked(true);
                        }
                    } else {
                        List<PricingIndividualDTO> individualDTOS = result.getPricingIndividualDTOS();
                        if (individualDTOS != null && individualDTOS.size() > 0) {
                            boolean ack = mPlanLayout.addEmptyPlan(individualDTOS.get(0));
                            mPlanLayout.setExtras(individualDTOS);
                            boolean isSelected = BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(mBaseRingBackToneDTO.getId());
                            if (!isSelected)
                                mTvSetTune.setEnabled(ack);
                        } else {
                            mPlanLayout.error(getString(R.string.msg_empty_plan));
                            mTvSetTune.setEnabled(false);
                        }
                    }
                    updateConfirmationButton();
                    updatePeekHeight();
                    mProfileTuneRingBackList = new ArrayList<>();
                    mProfileTuneRingBackList.add(result);
                    if (mProfileTuneRingBackList.size() > 0) {
                        mVoiceMap = mProfileUtil.getVoiceLanguageRingBackDTO(mProfileTuneRingBackList);
                        mVoiceList = mProfileUtil.getVoiceKeys(mVoiceMap);
                        for (RingBackToneDTO item : mProfileTuneRingBackList) {
                            if (BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(item.getId())) {
                                int voiceIndex = 0, languageIndex = 0;
                                for (String voice : mVoiceList) {
                                    if (voice.equals(item.getPrimaryArtistName())) break;
                                    voiceIndex++;
                                }
                                String selectedVoice = mVoiceList.get(voiceIndex);
                                List<RingBackToneDTO> voiceFilteredRingbacks = mProfileUtil.getRingbackToneList(mVoiceMap, selectedVoice);
                                if (voiceFilteredRingbacks != null) {
                                    for (RingBackToneDTO finalItem : voiceFilteredRingbacks) {
                                        if (isSelected(finalItem.getId())) break;
                                        languageIndex++;
                                    }
                                    mSelectedVoiceIndex = voiceIndex;
                                    mSelectedLanguageIndex = languageIndex;
                                }
                                break;
                            }
                        }
                        setCurrentProfile();
                    }
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    mPlanLayout.error(errMsg);
                    updatePeekHeight();
                }
            });
        }
    }

    private void setCurrentProfile() {
        mSelectedVoice = mVoiceList.get(mSelectedVoiceIndex);
        mVoiceFilteredRingbacks = mProfileUtil.getRingbackToneList(mVoiceMap, mSelectedVoice);
        /*int counter = 0;
        for (RingBackToneDTO item : mVoiceFilteredRingbacks) {
            if (isSelected(item.getId())) {
                mCurrentRingBackToneDTO = mVoiceFilteredRingbacks.get(counter);
                mSelectedLanguageIndex = counter;
                break;
            }
            counter++;
        }
        if (mCurrentRingBackToneDTO == null)*/
        mCurrentRingBackToneDTO = mVoiceFilteredRingbacks.get(mSelectedLanguageIndex);
        mSelectedLanguage = mCurrentRingBackToneDTO.getLanguage();

        if (mVoiceList.size() > 1) {
            mVoiceLayout.setOnClickListener(mClickListener);
        } else {
            mVoiceLayout.setOnClickListener(null);
        }

        if (mVoiceFilteredRingbacks.size() > 1) {
            mLanguageLayout.setOnClickListener(mClickListener);
        } else {
            mLanguageLayout.setOnClickListener(null);
        }

        mTvSetTune.setEnabled(false);

        updateContent();
        showDataLoaded();
        //populatePlans();
        if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
            isGiftPresent = false;
            populatePlans();
        } else {
            BaselineApplication.getApplication().getRbtConnector().isFamilyAndFriends(new IAppFriendsAndFamily() {
                @Override
                public void isParent(boolean exist) {
                    populatePlans();
                }

                @Override
                public void isChild(boolean exist) {
                    isGiftPresent = true;

                    childInfo = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo();
                    mContactModelDTO = new ContactModelDTO();
                    mContactModelDTO.setName(childInfo.getParentId());
                    mContactModelDTO.setMobileNumber(childInfo.getParentId());
                    ContactDetailProvider contactDetailProvider = new ContactDetailProvider(getRootActivity(), childInfo.getParentId(), new IContactDetailProvider() {
                        @Override
                        public void contactRecieved(ContactModelDTO contactModelDTO) {
                            if (contactModelDTO.getName() == null) {
                                contactModelDTO.setName(contactModelDTO.getMobileNumber());
                            }
                            mContactModelDTO.setName(contactModelDTO.getName());
                        }
                    });
                    contactDetailProvider.execute();

                    populatePlans();
                }

                @Override
                public void isNone(boolean exist) {
                    populatePlans();
                }
            });
        }
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
                        if (priceDTO != null && priceDTO.getRetail_priceObject() != null
                                && priceDTO.getRetail_priceObject().getAmount() != null) {
                            mPlanLayout.addPlan(plan, priceDTO);
                        }
                        if (mPlanLayout.getPlanCount() == 1)
                            plan.setChecked(true);
                    }
                } else {
                    List<PricingIndividualDTO> individualDTOS = result.getPricingIndividualDTOS();
                    if (individualDTOS != null && individualDTOS.size() > 0) {
                        boolean ack = mPlanLayout.addEmptyPlan(individualDTOS.get(0));
                        mPlanLayout.setExtras(individualDTOS);
                        boolean isSelected = BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(mCurrentRingBackToneDTO.getId());
                        if (!isSelected)
                            mTvSetTune.setEnabled(ack);
                    } else {
                        mPlanLayout.error(getString(R.string.msg_empty_plan));
                        mTvSetTune.setEnabled(false);
                    }
                }
                updateConfirmationButton();
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
     * Click listener for different components
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == mTvSetTune.getId()) {
                pricingIndividualDTO = null;
                mIsUpgrade = false;

                if (!isGiftPresent) {
                    if (!BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                        setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL);
                    } else {
                        checkPlanUpgrade(GiftnNormalPlanConfirmDialog.PLAN.NORMAL);
                    }
                } else {
                    AppDialog.GiftnNormalPlanConfirmDialog(getRootActivity(), mContactModelDTO, new GiftnNormalPlanConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue(GiftnNormalPlanConfirmDialog.PLAN plan) {
                            if (plan == GiftnNormalPlanConfirmDialog.PLAN.GIFT) {
                                setTune(plan);
                            } else {
                                checkPlanUpgrade(plan);
                            }
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                }
            } else if (id == mLayoutPlayer.getId()) {
                toggleTrack();
            } else if (id == mVoiceLayout.getId()) {
                if (getMusicPlayer().isMediaPlaying()) {
                    getMusicPlayer().stopMusic();
                } else {
                    try {
                        getMusicPlayer().stopMusic();
                    } catch (Exception e) {

                    }
                }
                mArtistPickerDialog = new ArtistPickerDialog(getActivity(), mVoiceList, mSelectedVoiceIndex, new ArtistPickerDialog.IArtistPickerListener() {
                    @Override
                    public void onCancel() {
                        mArtistPickerDialog.dismiss();
                    }

                    @Override
                    public void onDone(int index) {
                        mArtistPickerDialog.dismiss();
                        if (mSelectedVoiceIndex != index) {
                            mSelectedVoiceIndex = index;
                            mSelectedLanguageIndex = 0;
                            setCurrentProfile();
                        }
                    }
                });
                mArtistPickerDialog.show();
            } else if (id == mTimeLayout.getId()) {
                if (getMusicPlayer().isMediaPlaying()) {
                    getMusicPlayer().stopMusic();
                } else {
                    try {
                        getMusicPlayer().stopMusic();
                    } catch (Exception e) {

                    }
                }
                mDurationPickerDialog = new DurationPickerDialog(getActivity(), mDurationArray, new DurationPickerDialog.IDurationPickerListener() {
                    @Override
                    public void onCancel() {
                        mDurationPickerDialog.dismiss();
                    }

                    @Override
                    public void onDone(int[] durationArray) {
                        mDurationPickerDialog.dismiss();
                        mDurationArray = durationArray;
                        durationInMillis = mProfileUtil.toMilliSeconds(mDurationArray[0], mDurationArray[1], mDurationArray[2]);
                        mTvDurationChoice.setText(mProfileUtil.toDaysHoursAndMinutes(getActivity(), durationInMillis));
                        updateConfirmationButton();
                    }
                });
                mDurationPickerDialog.show();
            } else if (id == mLanguageLayout.getId()) {
                if (getMusicPlayer().isMediaPlaying()) {
                    getMusicPlayer().stopMusic();
                } else {
                    try {
                        getMusicPlayer().stopMusic();
                    } catch (Exception e) {

                    }
                }
                mLanguagePickerDialog = new LanguagePickerDialog(getActivity(), mVoiceFilteredRingbacks, mSelectedLanguageIndex, new LanguagePickerDialog.ILanguagePickerListener() {
                    @Override
                    public void onCancel() {
                        mLanguagePickerDialog.dismiss();
                    }

                    @Override
                    public void onDone(int index) {
                        mLanguagePickerDialog.dismiss();
                        if (mSelectedLanguageIndex != index) {
                            mSelectedLanguageIndex = index;
                            setCurrentProfile();
                        }
                    }
                });
                mLanguagePickerDialog.show();
            }
        }
    };

    /**
     * On plan selection
     *
     * @param plan PlanView to set plan
     */
    private void planSelected(PlanView plan) {
        if (plan == null)
            return;
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
            mCallback.updatePeekHeight(SetProfileTunePlansBSFragment.this);
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
//                        mCallback.next(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
//                } else {
//                    if (mCallback != null)
//                        mCallback.done(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
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
                            mCallback.next(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);
                        if (mCallback != null)
                            mCallback.done(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
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

    private void updateDuration(long durationInMinutes) {
        durationInMillis = TimeUnit.MINUTES.toMillis(durationInMinutes);
        mDurationArray = mProfileUtil.toDaysHoursAndMinutesDurationArray(durationInMillis);
    }

    private void updateConfirmationButton() {
        if (!isAdded())
            return;
        if (mCurrentRingBackToneDTO != null) {
            mTvSetTune.setText(getString(!isSelected(mCurrentRingBackToneDTO.getId()) ? R.string.set_small : R.string.tune_update));
            List<PlayRuleDTO> playRuleDTOList = getPlayRule(mCurrentRingBackToneDTO);
            if (playRuleDTOList != null && playRuleDTOList.size() > 0) {
                ScheduleDTO scheduleDTO = playRuleDTOList.get(playRuleDTOList.size() - 1).getSchedule();
                long diff = ProfileUtil.getDifferenceInMillis(scheduleDTO);
                if (diff != -1 && diff == durationInMillis) {
                    mTvSetTune.setEnabled(false);
                    return;
                }
            }
            // mTvSetTune.setEnabled(true);
        }
    }

    private List<PlayRuleDTO> getPlayRule(RingBackToneDTO ringBackToneDTO) {
        if (isSelected(ringBackToneDTO.getId()))
            return BaselineApplication.getApplication().getRbtConnector().getPlayRuleById(mCurrentRingBackToneDTO.getId());
        return null;
    }

    private boolean isSelected(String id) {
        return BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(id);
    }

    private void setTune(GiftnNormalPlanConfirmDialog.PLAN plan) {
        showProgress(true);
        RingBackToneDTO ringBackToneDTO = mPlanLayout.getRingBackToneDTO();
        pricingSubscriptionDTO = null;
        //pricingIndividualDTO = null;

        String parentRefId = null;
        if (isGiftPresent && plan == GiftnNormalPlanConfirmDialog.PLAN.GIFT) {
            pricingSubscriptionDTO = childInfo.getCatalogSubscription();
            parentRefId = childInfo.getParentId();
            pricingIndividualDTO = null;
        } else {
            if (mPlanLayout.getPlanCount() == 0) {
                if (pricingIndividualDTO == null) {
                    pricingIndividualDTO = mPlanLayout.getPricingIndividualDTO();
                    pricingSubscriptionDTO = null;
                }
            } else {
                pricingSubscriptionDTO = mPlanLayout.getSelectedPlan().getPriceDTO();
                pricingIndividualDTO = null;
            }
            parentRefId = null;
        }

        String profileRange = mProfileUtil.toDaysHoursAndMinutesServerFormat(durationInMillis);

        purchaseProfileConfirmation(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, null, profileRange, parentRefId);

//        BaselineApplication.getApplication().getRbtConnector().purchaseProfile(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, null, profileRange, parentRefId, new AppBaselineCallback<PurchaseComboResponseDTO>() {
//            @Override
//            public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
//                if (!isAdded()) return;
//                if (purchaseComboResponseDTO != null) {
//                    PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = purchaseComboResponseDTO.getThirdpartyconsent();
//                    Intent intent = new Intent();
//                    if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
////                        intent.setClass(getRootActivity(), CGOfflineActivity.class);
////                        intent.putExtra(INTENT_EXTRA_OFFLINE_RURL, thirdPartyConsentDTO.getReturn_url());
////                        intent.putExtra(INTENT_EXTRA_IMAGE_URL, mPlanLayout.getRingBackToneDTO().getPrimaryImage());
////                        if (pricingSubscriptionDTO != null) {
////                            intent.putExtra(INTENT_EXTRA_PRICING_TEXT, pricingSubscriptionDTO.getDescription());
////                        } else {
////                            intent.putExtra(INTENT_EXTRA_PRICING_TEXT, mPlanLayout.getPricingIndividualDTO().getShortDescription());
////                        }
////                        startActivityForResult(intent, RESULT_OFFLINE_CONSENT_ACTIVITY);
//                    } else if (thirdPartyConsentDTO != null) {
//                        intent.setClass(getRootActivity(), CGWebViewActivity.class);
//                        intent.putExtra(EXTRA_CONSCENT_URL, thirdPartyConsentDTO.getThird_party_url());
//                        intent.putExtra(EXTRA_R_URL, thirdPartyConsentDTO.getReturn_url());
//                        startActivityForResult(intent, RESULT_CONSENT_ACTIVITY);
//                    } else {
//                        showProgress(false);
//                        if (mCallback != null)
//                            mCallback.next(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
//                        // mPreBuyPresenter.purchaseComboResponsePlayRuleHandler(purchaseComboResponseDTO);
//                    }
//
//                }
//            }
//
//            @Override
//            public void failure(String errMsg) {
//                if (!isAdded()) return;
//                showProgress(false);
//                getRootActivity().showShortToast(errMsg);
////                            if (mCallback != null)
////                                mCallback.done(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
//            }
//        });

//                if (mCallback != null)
//                    mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
        //mCallback.next(SetCallerTunePlansBSFragment.this, mRingBackToneDTO); //TODO Disabled for demo
    }

    private void checkPlanUpgrade(GiftnNormalPlanConfirmDialog.PLAN plan) {
        if (AppConfigurationValues.isPlanUpgradable() && mPlanLayout.getExtras() != null && mPlanLayout.getExtras() instanceof List) {
            try {
                List<PricingIndividualDTO> list = (List<PricingIndividualDTO>) mPlanLayout.getExtras();
                if (list != null && list.size() > 1) {
                    showProgress(false);
                    BaselineApplication.getApplication().getRbtConnector().checkPlanUpgrade(list, new IPreBuyUDSCheck() {
                        @Override
                        public void showUDSUpdatePopUp(List<PricingIndividualDTO> pricingIndividualDTOS) {
                            AppDialog.showPlanUpgradeDialog(getRootActivity(), pricingIndividualDTOS, new ShuffleUpgradeDialog.ActionCallBack() {
                                @Override
                                public void onContinue(PricingIndividualDTO item) {
                                    mIsUpgrade = true;
                                    pricingIndividualDTO = item;
                                    setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL);
                                }

                                @Override
                                public void onCancel() {
                                    //sendUpgradeAnalytics(null, false);
                                    //setTune(callback);
                                }
                            });
                        }

                        @Override
                        public void showNoPopUp(List<PricingIndividualDTO> pricingIndividualDTOS) {
                            mIsUpgrade = true;
                            setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL);
                        }
                    });
                    return;
                } else if (list != null && list.size() == 1 && list.get(0).getCatalogSubscriptionId() != null && BaselineApplication.getApplication().getRbtConnector().getCacheUserSubscription() != null
                        && !list.get(0).getCatalogSubscriptionId().equalsIgnoreCase(BaselineApplication.getApplication().getRbtConnector().getCacheUserSubscription().getCatalog_subscription_id())) {
                    if (AppConfigurationValues.IsShowSinglePlanUpgrade()) {
                        AppDialog.showPlanUpgradeDialog(getRootActivity(), list, new ShuffleUpgradeDialog.ActionCallBack() {
                            @Override
                            public void onContinue(PricingIndividualDTO item) {
                                mIsUpgrade = true;
                                pricingIndividualDTO = item;
                                //sendUpgradeAnalytics(item.getCatalogSubscriptionId(), true);
                                setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL);
                            }

                            @Override
                            public void onCancel() {
                                //sendUpgradeAnalytics(null, false);
                                //setTune(callback);
                            }
                        });
                        return;
                    } else {
                        mIsUpgrade = true;
                    }
                }
            } catch (ClassCastException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        setTune(plan);
    }

    private boolean isShowConsentPopup = true;

    private void purchaseProfileConfirmation(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String profileRange, String parentRefId) {
        if (!AppConfigurationValues.IsShownConfirmationForProfileTune()) {
            isShowConsentPopup = false;
        }
        if (mPlanLayout.getSelectedPlan() != null) {
            mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
        } else {
            mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
        }

        BaselineApplication.getApplication().getRbtConnector().getProfileAppUtilityNetworkRequest(mCallerSource, ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, null, new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                if (!isAdded()) return;
                BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);

                BaselineApplication.getApplication().getRbtConnector().dummyPurchaseProfile(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, null, profileRange, parentRefId);

                mNetworkType = result.getNetworkType();

                APIRequestParameters.ConfirmationType confirmationType = null;
                if (mNetworkType.equalsIgnoreCase("opt_network")) {
                    confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationOptNetwork();
                } else {
                    confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationNonOptNetwork();
                }

                if (!isShowConsentPopup || !isShowConsentPopUp(confirmationType, mIsUpgrade)) {
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                    purchaseProfile(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, profileRange, parentRefId);
                } else {
                    showProgress(false);
                    AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue() {
                            if (!isAdded()) return;
                            showProgress(true);
                            //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                            BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);
                            purchaseProfile(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, profileRange, parentRefId);
                        }

                        @Override
                        public void onCancel() {
                            if (!isAdded()) return;
                            //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
                            showProgress(false);
                        }
                    });
                }
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                showProgress(false);
                getRootActivity().showShortToast(message);
            }
        });
    }


    private void purchaseProfile(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String profileRange, String parentRefId) {
        BaselineApplication.getApplication().getRbtConnector().purchaseProfile(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, null, profileRange, parentRefId, new AppBaselineCallback<PurchaseComboResponseDTO>() {
            @Override
            public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
                if (!isAdded()) return;
                if (purchaseComboResponseDTO != null) {
                    PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = purchaseComboResponseDTO.getThirdpartyconsent();
                    Intent intent = new Intent();
                    if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
                        NonNetworkCGDTO nonNetworkCG = BaselineApplication.getApplication().getRbtConnector().getNonNetworkCG();
                        BaselineApplication.getApplication().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                            @Override
                            public void success(RUrlResponseDto result) {
                                if (!isAdded()) return;
                                showProgress(false);
                                if (nonNetworkCG != null) {
                                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_OFFLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_SMS);
                                    showCGDialog(nonNetworkCG.getMessageIOS());
                                }
                            }

                            @Override
                            public void failure(String errMsg) {
                                if (!isAdded()) return;
                                showProgress(false);
                                showCGDialog(errMsg);
                            }
                        }, thirdPartyConsentDTO.getReturn_url(), APIRequestParameters.CG_REQUEST.NO);

                    } else if (thirdPartyConsentDTO != null) {
                        intent.setClass(getRootActivity(), CGWebViewActivity.class);
                        intent.putExtra(EXTRA_CONSCENT_URL, thirdPartyConsentDTO.getThird_party_url());
                        intent.putExtra(EXTRA_R_URL, thirdPartyConsentDTO.getReturn_url());
                        startActivityForResult(intent, RESULT_CONSENT_ACTIVITY);
                    } else {
                        //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                        showProgress(false);
                        if (mCallback != null)
                            mCallback.next(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
                        // mPreBuyPresenter.purchaseComboResponsePlayRuleHandler(purchaseComboResponseDTO);
                    }

                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                showProgress(false);
                getRootActivity().showShortToast(errMsg);
//                            if (mCallback != null)
//                                mCallback.done(SetProfileTunePlansBSFragment.this, mCurrentRingBackToneDTO);
            }
        });
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

    private boolean isShowConsentPopUp(APIRequestParameters.ConfirmationType confirmationType, boolean isUpgrade) {
        boolean isActiveUser = BaselineApplication.getApplication().getRbtConnector().isActiveUser();
        switch (confirmationType) {
            case ALL:
                return true;
            case UPGRADE:
                return isUpgrade || !isActiveUser;
            case NEW:
                return !isActiveUser;
            case NONE:
                return false;
            default:
                return true;
        }
    }
}
