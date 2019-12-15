package com.onmobile.rbt.baseline.bottomsheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PayTMGetPaymentDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.IPreBuyUDSCheck;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.CGWebViewActivity;
import com.onmobile.rbt.baseline.activities.ContactViewActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.base.TuneBottomSheetUtil;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.GiftnNormalPlanConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.PurchaseConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.ShuffleUpgradeDialog;
import com.onmobile.rbt.baseline.dialog.custom.SingleButtonInfoDialog;
import com.onmobile.rbt.baseline.fragment.FragmentHorizontalMusic;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.ContactDetailProvider;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.IContactDetailProvider;
import com.onmobile.rbt.baseline.util.Logger;
import com.onmobile.rbt.baseline.util.PurchaseMode;
import com.onmobile.rbt.baseline.widget.LabeledView;
import com.onmobile.rbt.baseline.widget.PlanView;
import com.onmobile.rbt.baseline.widget.PlanViewLayout;
import com.onmobile.rbt.baseline.widget.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class SetShufflePlansBSFragment extends BaseFragment {

    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;
    private static final String TAG = SetNameTunePlansBSFragment.class.getSimpleName();
    private AppCompatTextView mTvSetTune, mTvAddMoreContactCount;
    private PlanViewLayout mPlanLayout;
    private LabeledView mAllCallerLabeledView, mSpecialCallersLabeledView;
    private ViewGroup mLayoutRootAction;
    private Chip mChipAddMoreContact;

    private RingBackToneDTO mRingBackToneDTO;
    private PricingIndividualDTO pricingIndividualDTO = null;

    private ProgressDialog progressDialog;
    private Map<String, ContactModelDTO> mContactModel;
    private List<PlayRuleDTO> mPlayRuleDTOList;

    private boolean mIsSystemShuffle;
    private boolean mIsShuffleEditable;
    private boolean isSelected;
    private boolean mUserInteractedWithLabelView;
    private Map<String, Boolean> mAlreadySetContacts;
    private boolean isGiftPresent = false;
    private ContactModelDTO mContactModelDTO;
    private GetChildInfoResponseDTO childInfo;
    private boolean mFullPlayerRedirection;

    //boolean isShowConfimationPopup = true;
    String mConfirmationPopUpDescription = null;
    private LinearLayout mCallersChoiceLayout;
    private TextView mPlayForAllCallersInfoText;

    private String mCallerSource;
    private boolean isPaymentInitiatedViaPayTM;
    private Map<String, String> mSelectedContacts;
    private String mNetworkType;
    private ChartItemDTO mChartItemDTO;
    private boolean mIsUpgrade = false;

    public static SetShufflePlansBSFragment newInstance(String callerSource, RingBackToneDTO ringBackToneDTO, boolean isSystemShuffle, boolean isShuffleEditable, boolean fullPlayerRedirection, ChartItemDTO chartItemDTO) {
        SetShufflePlansBSFragment fragment = new SetShufflePlansBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, isSystemShuffle);
        bundle.putBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, isShuffleEditable);
        bundle.putBoolean(AppConstant.KEY_IS_FULL_PLAYER_REDIRECTION, fullPlayerRedirection);
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM_CHART, chartItemDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return SetShufflePlansBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_shuffle_plans_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mChartItemDTO = (ChartItemDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM_CHART);
            mIsSystemShuffle = bundle.getBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, true);
            mIsShuffleEditable = bundle.getBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, false);
            mFullPlayerRedirection = bundle.getBoolean(AppConstant.KEY_IS_FULL_PLAYER_REDIRECTION, true);
        }
    }

    @Override
    protected void initComponents() {
        mContactModel = new HashMap<>();
    }

    @Override
    protected void initViews(View view) {

        mPlanLayout = view.findViewById(R.id.layout_plan);
        mLayoutRootAction = view.findViewById(R.id.layout_root_set_action);
        mTvSetTune = view.findViewById(R.id.tv_set_bottom_sheet);

        mTvSetTune.setEnabled(false);

        mChipAddMoreContact = view.findViewById(R.id.chip_add_more_contact_root_set_action);
        mTvAddMoreContactCount = view.findViewById(R.id.tv_add_more_contact_root_set_action);

        mPlanLayout.addOnPlanSelectedListener(mPlanSelectedListener);
        mTvSetTune.setOnClickListener(mClickListener);

        mAllCallerLabeledView = view.findViewById(R.id.all_callers_labeled_view);
        mSpecialCallersLabeledView = view.findViewById(R.id.special_callers_labeled_view);
        mAllCallerLabeledView.setListener(mLabeledListener);
        mSpecialCallersLabeledView.setListener(mLabeledListener);

        mCallersChoiceLayout = view.findViewById(R.id.caller_choice_layout);
        mPlayForAllCallersInfoText = view.findViewById(R.id.play_for_all_callers_info);

    }

    @Override
    protected void bindViews(View view) {
        if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
            mCallersChoiceLayout.setVisibility(View.VISIBLE);
            mPlayForAllCallersInfoText.setVisibility(View.GONE);
        } else {
            mCallersChoiceLayout.setVisibility(View.GONE);
            mPlayForAllCallersInfoText.setVisibility(View.VISIBLE);
        }


        /*int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
        final String imageUrl = AppUtils.getFitableImage(getFragmentContext(), mRingBackToneDTO.getPrimaryImage(), imageSize);
        Glide.with(getFragmentContext()).load(imageUrl).asBitmap().centerCrop().placeholder(R.drawable.default_album_art).error(R.drawable.default_album_art).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(mIvPreview) {
            @Override
            protected void setResource(Bitmap resource) {
                WidgetUtils.setCircularImage(mIvPreview, mIvPreviewDisc, resource, 0);
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                WidgetUtils.setCircularImage(mIvPreview, mIvPreviewDisc, resource, 0);
            }
        });

        mTvArtistPreview.setText(mRingBackToneDTO.getTrackName());
        mTvTrackPreview.setText(mRingBackToneDTO.getTrackName());*/

        mChipAddMoreContact.setVisibility(View.GONE);
        mChipAddMoreContact.setOnChipClickListener(v -> {
            mUserInteractedWithLabelView = true;
            openContactActivity();
        });
        attachShowcase();
        updateLayout();
    }

    private void setupEditableLayout() {
        mLayoutRootAction.setVisibility(View.GONE);
        mPlanLayout.setVisibility(View.GONE);
        mTvSetTune.setVisibility(View.GONE);
    }

    /**
     * Attach a fragment to show the content of the shuffle
     */
    FragmentHorizontalMusic fragmentHorizontalMusic;

    private void attachShowcase() {
        fragmentHorizontalMusic = FragmentHorizontalMusic.newInstance(FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SHUFFLE, mRingBackToneDTO, true, mIsSystemShuffle, mIsShuffleEditable, mFullPlayerRedirection);
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_showcase_bottom_sheet, fragmentHorizontalMusic, fragmentHorizontalMusic.getFragmentTag()).commitAllowingStateLoss();
    }

    /**
     * Set callback for BottomSheet
     *
     * @param callback Listener
     * @return SetShufflePlansBSFragment
     */
    public SetShufflePlansBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
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
        if (mIsSystemShuffle)
            BaselineApplication.getApplication().getRbtConnector().getShuffleContentAndPrice(mRingBackToneDTO.getId(), new AppBaselineCallback<ChartItemDTO>() {
                @Override
                public void success(ChartItemDTO result) {
                    if (!isAdded()) return;
                    mPlanLayout.setChartItemDTO(result);
                    if (result.getAvailability() != null && result.getAvailability().size() > 0) {
                        List<PricingIndividualDTO> individualDTOS = result.getAvailability().get(0).getIndividual();
                        if (individualDTOS != null && individualDTOS.size() > 0) {
                            //mTvSetTune.setEnabled(mPlanLayout.addEmptyPlan(individualDTOS.get(0)));
                            boolean ack = mPlanLayout.addEmptyPlan(individualDTOS.get(0));
                            mPlanLayout.setExtras(individualDTOS);
                            if (!isSelected)
                                mTvSetTune.setEnabled(ack);
                        } else {
                            mPlanLayout.error(getString(R.string.msg_empty_plan));
                        }
                    } else {
                        mPlanLayout.error(getString(R.string.something_went_wrong));
                    }
                    initAlreadySetContacts();
                    updateConfirmationButton();
                    updateAddMoreContactButton();
                    updatePeekHeight();
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    mPlanLayout.error(errMsg);
                    updatePeekHeight();
                }
            });
        else
            BaselineApplication.getApplication().getRbtConnector().getUserDefinedPlaylistPricing(mRingBackToneDTO.getId(), new AppBaselineCallback<List<AvailabilityDTO>>() {
                @Override
                public void success(List<AvailabilityDTO> result) {
                    if (!isAdded()) return;
                    if (result != null && result.size() > 0) {
                        List<PricingIndividualDTO> individualDTOS = result.get(0).getIndividual();
                        if (individualDTOS != null && individualDTOS.size() > 0) {
                            //mTvSetTune.setEnabled(mPlanLayout.addEmptyPlan(individualDTOS.get(0)));
                            boolean ack = mPlanLayout.addEmptyPlan(individualDTOS.get(0));
                            mPlanLayout.setExtras(individualDTOS);
                            if (!isSelected)
                                mTvSetTune.setEnabled(ack);
                        } else {
                            mPlanLayout.error(getString(R.string.msg_empty_plan));
                        }
                    } else {
                        mPlanLayout.error(getString(R.string.something_went_wrong));
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
            if (view.getId() == mTvSetTune.getId()) {
                if (getMusicPlayer() != null) {
                    getMusicPlayer().stopMusic();
                } else {
                    try {
                        getMusicPlayer().stopMusic();
                    } catch (Exception e) {

                    }
                }

                pricingIndividualDTO = null;
                mIsUpgrade = false;

                //checkAndSetTune();
                if (!isGiftPresent) {
                    if (!BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                        setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL, new AppBaselineCallback<PurchaseComboResponseDTO>() {
                            @Override
                            public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
                                if (!isAdded()) return;
                                handleSetTuneSuccess(purchaseComboResponseDTO);
                            }

                            @Override
                            public void failure(String errMsg) {
                                if (!isAdded()) return;
                                handleSetDeleteTuneError(errMsg);
                                updateLayout();
                                updateConfirmationButton();
                                updateAddMoreContactButton();
                            }
                        });
                    } else {
                        checkAndSetTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL);
                    }
                } else {
                    AppDialog.GiftnNormalPlanConfirmDialog(getRootActivity(), mContactModelDTO, new GiftnNormalPlanConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue(GiftnNormalPlanConfirmDialog.PLAN plan) {
                            if (plan == GiftnNormalPlanConfirmDialog.PLAN.GIFT) {
                                setTune(plan, new AppBaselineCallback<PurchaseComboResponseDTO>() {
                                    @Override
                                    public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
                                        if (!isAdded()) return;
                                        handleSetTuneSuccess(purchaseComboResponseDTO);
                                    }

                                    @Override
                                    public void failure(String errMsg) {
                                        if (!isAdded()) return;
                                        handleSetDeleteTuneError(errMsg);
                                    }
                                });
                            } else {
                                checkAndSetTune(plan);
                            }
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                }
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
            updateConfirmationButton();
            mPlanLayout.setFooterText(plan.getPriceDTO().getDescription());
        }
    }

    /**
     * Plan change listener
     */
    private PlanViewLayout.PlanSelectedListener mPlanSelectedListener = this::planSelected;

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(SetShufflePlansBSFragment.this);
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

    private LabeledView.OnLabeledListener mLabeledListener = new LabeledView.OnLabeledListener() {
        @Override
        public void onClick(LabeledView view) {

        }

        @Override
        public void onSwitch(LabeledView view, boolean checked) {
            /*if (view.getId() == mAllCallerLabeledView.getId()) {
                if (checked) {
                    mSpecialCallersLabeledView.disableSwitchStatusSilently();
                } else {
                    if (!mSpecialCallersLabeledView.getSwitchStatus()) {
                        mAllCallerLabeledView.enableSwitchStatusSilently();
                    }
                }
            } else if (view.getId() == mSpecialCallersLabeledView.getId()) {
                if (checked) {
                    //TODO redirect and select contacts
                    startActivityForResult(new Intent(getRootActivity(), ContactViewActivity.class), AppConstant.ACTIVIY_RESULT_CONTACT);
                    mSpecialCallersLabeledView.disableSwitchStatusSilently();
                } else {
                    mAllCallerLabeledView.enableSwitchStatusSilently();
                }
            }*/

            mUserInteractedWithLabelView = true;
            if (view.getId() == mAllCallerLabeledView.getId()) {
                if (checked) {
                    mSpecialCallersLabeledView.disableSwitchStatusSilently();
                } else {
                    mAllCallerLabeledView.enableSwitchStatusSilently();
                }
                //mContactModel = null;
            } else if (view.getId() == mSpecialCallersLabeledView.getId()) {
                int specialCallerCount = TuneBottomSheetUtil.getSpecialCallerCount(mPlayRuleDTOList);
                int selectedSpecialCallerCount = mContactModel != null ? mContactModel.size() : 0;
                int totalCount = specialCallerCount + selectedSpecialCallerCount;
                if (checked) {
                    mAllCallerLabeledView.disableSwitchStatusSilently();
                    if ((totalCount == 0 || (specialCallerCount > 0 && selectedSpecialCallerCount == 0)))
                        openContactActivity();
                } else {
                    mAllCallerLabeledView.enableSwitchStatusSilently();
                }
                /*else {
                    mAllCallerLabeledView.enableSwitchStatusSilently();
                    mContactModel = null;
                }*/
                //mSpecialCallersLabeledView.disableSwitchStatusSilently();
            }
            updateConfirmationButton();
            updateAddMoreContactButton();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == AppConstant.ACTIVIY_RESULT_CONTACT) {

            if (resultCode == RESULT_OK && data != null) {
                ContactViewActivity.ContactData resultData = (ContactViewActivity.ContactData) data.getSerializableExtra(AppConstant.KEY_DATA_CONTACT);
                ContactModelDTO modelDTO = resultData.getSelectedContact();
                if (modelDTO != null && !TuneBottomSheetUtil.isSpecialCallerExists(mPlayRuleDTOList, modelDTO.getMobileNumber())) {
                    if (mContactModel == null)
                        mContactModel = new HashMap<>();
                    //mContactModel.put(modelDTO.mobileNumber, modelDTO); //TODO purchase combo doesn't support multiple contact purchase in one request.

                    mContactModel.clear();
                    mContactModel.put(modelDTO.getMobileNumber(), modelDTO); //TODO So clearing and replacing the contacts
                }
                if (resultData.getAlreadySetContacts() != null)
                    mAlreadySetContacts = resultData.getAlreadySetContacts();
            }
            if (TuneBottomSheetUtil.isSetForSpecialCallers(mPlayRuleDTOList) || (mContactModel != null && mContactModel.size() > 0) || TuneBottomSheetUtil.anySpecialCallerUnchecked(mAlreadySetContacts)) {
                mSpecialCallersLabeledView.enableSwitchStatusSilently();
                mAllCallerLabeledView.disableSwitchStatusSilently();
            } else {
                mSpecialCallersLabeledView.disableSwitchStatusSilently();
                mAllCallerLabeledView.enableSwitchStatusSilently();
            }
            updateConfirmationButton();
            updateAddMoreContactButton();
        } else if (resultCode == RESULT_OK && data != null) {
            if (requestCode == RESULT_OFFLINE_CONSENT_ACTIVITY) {
//                showProgress(false);
//                String status = (String) data.getSerializableExtra(EXTRA_OFFLINE_CONSENT_CALLBACK);
//                if (status != null && status.equalsIgnoreCase("success")) {
//                    if (mCallback != null)
//                        mCallback.next(SetShufflePlansBSFragment.this, mRingBackToneDTO);
//                } else {
//                    if (mCallback != null)
//                        mCallback.done(SetShufflePlansBSFragment.this, mRingBackToneDTO);
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
                        /*sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_ONLINE,
                                AnalyticsConstants.SUCCESS.equalsIgnoreCase(result.getMessage()) ?
                                        AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES : AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_NO);*/
                        showProgress(false);
                        if (mCallback != null)
                            mCallback.next(SetShufflePlansBSFragment.this, mRingBackToneDTO);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);
                        if (mCallback != null)
                            mCallback.done(SetShufflePlansBSFragment.this, mRingBackToneDTO);
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

    private void updateLayout() {
        isSelected = BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(mRingBackToneDTO.getId()) && !mRingBackToneDTO.isCut();
        if (isSelected) {
            mPlayRuleDTOList = BaselineApplication.getApplication().getRbtConnector().getPlayRuleById(mRingBackToneDTO.getId());
        }
        mTvSetTune.setEnabled(false);
        if (isSelected) {
            List<String> callingParty = TuneBottomSheetUtil.getCallingParties(mPlayRuleDTOList);
            if (callingParty != null && callingParty.size() > 0) {
                mAllCallerLabeledView.disableSwitchStatusSilently();
                mSpecialCallersLabeledView.enableSwitchStatusSilently();
            } else {
                mSpecialCallersLabeledView.disableSwitchStatusSilently();
                mAllCallerLabeledView.enableSwitchStatusSilently();
            }
        } else {
            mSpecialCallersLabeledView.disableSwitchStatusSilently();
            mAllCallerLabeledView.enableSwitchStatusSilently();
        }

        if (mIsSystemShuffle || !mIsShuffleEditable) {
            //populatePlans();
            if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                isGiftPresent = false;

                if (!AppConfigurationValues.isSelectionModel() && BaselineApplication.getApplication().getRbtConnector().isSongPurchased(mRingBackToneDTO.getId()) && mChartItemDTO != null) {
                    mPlanLayout.setChartItemDTO(mChartItemDTO);
                    if (BaselineApplication.getApplication().getRbtConnector().getTuneAlreadyPurchasedMessage() != null) {
                        mPlanLayout.error(BaselineApplication.getApplication().getRbtConnector().getTuneAlreadyPurchasedMessage());
                    } else {
                        mPlanLayout.error("");
                    }
                    if (!isSelected)
                        mTvSetTune.setEnabled(true);
                } else {
                    populatePlans();
                }
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
                        if (!AppConfigurationValues.isSelectionModel() && BaselineApplication.getApplication().getRbtConnector().isSongPurchased(mRingBackToneDTO.getId()) && mChartItemDTO != null) {
                            mPlanLayout.setChartItemDTO(mChartItemDTO);
                            if (BaselineApplication.getApplication().getRbtConnector().getTuneAlreadyPurchasedMessage() != null) {
                                mPlanLayout.error(BaselineApplication.getApplication().getRbtConnector().getTuneAlreadyPurchasedMessage());
                            } else {
                                mPlanLayout.error("");
                            }
                            if (!isSelected)
                                mTvSetTune.setEnabled(true);
                        } else {
                            populatePlans();
                        }
                    }
                });
            }


        } else {
            setupEditableLayout();
        }
    }

    private void updateConfirmationButton() {
        if (!isAdded()) return;
        mTvSetTune.setText(getString(isSelected ? R.string.tune_update : R.string.set_small));
        if (isSelected) {
            if (mUserInteractedWithLabelView) {
                boolean specialCallerEnabled = mSpecialCallersLabeledView.getSwitchStatus();
                boolean allCallerEnabled = mAllCallerLabeledView.getSwitchStatus();
                boolean isSetForSpecialCallers = TuneBottomSheetUtil.isSetForSpecialCallers(mPlayRuleDTOList);
                if (specialCallerEnabled && mContactModel != null && mContactModel.size() > 0)
                    mTvSetTune.setEnabled(true);
                else if (specialCallerEnabled && !isSetForSpecialCallers)
                    mTvSetTune.setEnabled(true);
                else if (specialCallerEnabled && TuneBottomSheetUtil.anySpecialCallerUnchecked(mAlreadySetContacts))
                    mTvSetTune.setEnabled(true);
                else if (allCallerEnabled && isSetForSpecialCallers)
                    mTvSetTune.setEnabled(true);
                else
                    mTvSetTune.setEnabled(false);
            }
        }
    }

    private void checkAndSetTune(GiftnNormalPlanConfirmDialog.PLAN plan) {
        boolean setRequired = false;
        boolean deleteRequired = false;
        if (isSelected) {
            final boolean isSetForSpecialCallers = TuneBottomSheetUtil.isSetForSpecialCallers(mPlayRuleDTOList);
            final boolean anySpecialCallerUnchecked = TuneBottomSheetUtil.anySpecialCallerUnchecked(mAlreadySetContacts);
            if (mAllCallerLabeledView.getSwitchStatus() && isSetForSpecialCallers) {
                setRequired = true;
                deleteRequired = true;
            } else if (mSpecialCallersLabeledView.getSwitchStatus() && !isSetForSpecialCallers && mContactModel != null && mContactModel.size() > 0) {
                setRequired = true;
                deleteRequired = true;
            } else if (mSpecialCallersLabeledView.getSwitchStatus() && mContactModel != null && mContactModel.size() > 0 && anySpecialCallerUnchecked) {
                setRequired = true;
                deleteRequired = true;
            } else if (mSpecialCallersLabeledView.getSwitchStatus() && isSetForSpecialCallers && mContactModel != null && mContactModel.size() > 0 && anySpecialCallerUnchecked) {
                setRequired = true;
                deleteRequired = true;
            } else if (mSpecialCallersLabeledView.getSwitchStatus() && isSetForSpecialCallers && anySpecialCallerUnchecked) {
                deleteRequired = true;
            } else if (mSpecialCallersLabeledView.getSwitchStatus() && !isSetForSpecialCallers) {
                setRequired = true;
                deleteRequired = true;
            } else {
                setRequired = true;
            }
        }
        if (deleteRequired) {
            boolean finalSetRequired = setRequired;
            deletePlayRule(new AppBaselineCallback<List<String>>() {
                @Override
                public void success(List<String> result) {
                    if (!isAdded()) return;
                    if (finalSetRequired) {
                        isSelected = false;
                        checkPlanUpgrade(plan, new AppBaselineCallback<PurchaseComboResponseDTO>() {
                            @Override
                            public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
                                if (!isAdded()) return;
                                handleSetTuneSuccess(purchaseComboResponseDTO);
                            }

                            @Override
                            public void failure(String errMsg) {
                                if (!isAdded()) return;
                                handleSetDeleteTuneError(errMsg);
                                updateLayout();
                                updateConfirmationButton();
                                updateAddMoreContactButton();
                            }
                        });
                    } else {
                        showProgress(false);
                        getRootActivity().showShortToast(getString(R.string.unset_success_message));
                        mCallback.done(SetShufflePlansBSFragment.this, mRingBackToneDTO);
                    }

                }

                @Override
                public void failure(String message) {
                    if (!isAdded()) return;
                    handleSetDeleteTuneError(message);
                }
            });
        } else {
            checkPlanUpgrade(plan, new AppBaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
                    if (!isAdded()) return;
                    handleSetTuneSuccess(purchaseComboResponseDTO);
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    handleSetDeleteTuneError(errMsg);
                }
            });
        }
    }

    String parentRefId = null;
    PricingSubscriptionDTO pricingSubscriptionDTO = null;
    private AppBaselineCallback<PurchaseComboResponseDTO> payTmCallBack;

    private void setTune(GiftnNormalPlanConfirmDialog.PLAN plan, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        payTmCallBack = callback;
        showProgress(true);
        Map<String, String> contactMap = new HashMap<>();
        if (!mAllCallerLabeledView.getSwitchStatus()) {
            for (ContactModelDTO modelDTO : mContactModel.values()) {
                contactMap.put(modelDTO.getMobileNumber(), modelDTO.getMobileNumber());
            }
        }
        mSelectedContacts = contactMap;
        if (isGiftPresent && plan == GiftnNormalPlanConfirmDialog.PLAN.GIFT) {
            pricingSubscriptionDTO = childInfo.getCatalogSubscription();
            parentRefId = childInfo.getParentId();
            pricingIndividualDTO = null;
        } else {
            if (pricingIndividualDTO == null)
                pricingIndividualDTO = mPlanLayout.getPricingIndividualDTO();
            pricingSubscriptionDTO = null;
            parentRefId = null;

        }
        if (mIsSystemShuffle) {
            mChartItemDTO = mPlanLayout.getChartItemDTO();
        }
        BaselineApplication.getApplication().getRbtConnector().getAppUtilityNetworkRequest(mCallerSource, mChartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                if (!isAdded()) return;
                BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);

                String thirdPartyCheckId;
                if(mIsSystemShuffle){
                    ChartItemDTO chartItemDTO = mPlanLayout.getChartItemDTO();
                    BaselineApplication.getApplication().getRbtConnector().dummyPurchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, parentRefId);
                    thirdPartyCheckId = chartItemDTO.getId()+"";
                }
                else{
                    UdpAssetDTO udpAssetDTO = fragmentHorizontalMusic.getUdpAssetDTO();
                    BaselineApplication.getApplication().getRbtConnector().dummyPurchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, udpAssetDTO, parentRefId);
                    thirdPartyCheckId = udpAssetDTO.getId();
                }



                    if (mIsSystemShuffle) {
                        ChartItemDTO chartItemDTO = mPlanLayout.getChartItemDTO();
                        //BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, parentRefId, callback);
                        purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, null, parentRefId, callback);
                    } else {
                        UdpAssetDTO udpAssetDTO = fragmentHorizontalMusic.getUdpAssetDTO();
                        //BaselineApplication.getApplication().getRbtConnector().purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, udpAssetDTO, parentRefId, callback);
                        purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, null, udpAssetDTO, parentRefId, callback);
                    }



            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                handleSetDeleteTuneError(message);
            }
        });
    }





    private void deletePlayRule(AppBaselineCallback<List<String>> callback) {
        showProgress(true);
        ArrayList<String> playRuleIds = new ArrayList<>();
        Map<String, Boolean> uncheckedContacts = TuneBottomSheetUtil.getUncheckedSpecialCaller(mAlreadySetContacts);
        boolean uncheckedContactsCheckRequired = uncheckedContacts.size() > 0;
        if (mPlayRuleDTOList != null) {
            for (PlayRuleDTO playRuleDTO : mPlayRuleDTOList) {
                if (playRuleDTO != null) {
                    if (uncheckedContactsCheckRequired) {
                        final CallingParty callingParty = playRuleDTO.getCallingparty();
                        if (callingParty != null) {
                            final String party = callingParty.getId();
                            if (uncheckedContacts.containsKey(party))
                                playRuleIds.add(playRuleDTO.getId());
                        }
                    } else {
                        playRuleIds.add(playRuleDTO.getId());
                    }
                }
            }
        }
        BaselineApplication.getApplication().getRbtConnector().deletePlayRule(playRuleIds, callback);
    }

    private void handleSetTuneSuccess(PurchaseComboResponseDTO purchaseComboResponseDTO) {
        showProgress(false);

        if (mIsSystemShuffle) {
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
                        mCallback.next(SetShufflePlansBSFragment.this, mRingBackToneDTO);
                }
            }
        } else {
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
                        mCallback.next(SetShufflePlansBSFragment.this, mRingBackToneDTO);
                }

            }
        }
    }

    private void handleSetDeleteTuneError(String errorMessage) {
        showProgress(false);
        getRootActivity().showShortToast(errorMessage);
    }

    private void updateAddMoreContactButton() {
        if (!isAdded())
            return;
        int count = TuneBottomSheetUtil.getTotalCount(mPlayRuleDTOList, mContactModel, mAlreadySetContacts);
        if (count > 0) {
            mChipAddMoreContact.setVisibility(View.VISIBLE);
            mTvAddMoreContactCount.setText(String.valueOf(count));
        } else {
            mChipAddMoreContact.setVisibility(View.GONE);
        }
    }

    private void openContactActivity() {
        initAlreadySetContacts();
        Intent intent = new Intent(getRootActivity(), ContactViewActivity.class);
        ContactViewActivity.ContactData data = null;
        if (mContactModel != null && mContactModel.size() > 0) {
            data = new ContactViewActivity.ContactData();
            for (ContactModelDTO contactModelDTO : mContactModel.values()) {
                if (contactModelDTO != null) {
                    data.setSelectedContact(contactModelDTO);
                    break;
                }
            }
        }
        if (mAlreadySetContacts != null && mAlreadySetContacts.size() > 0) {
            if (data == null)
                data = new ContactViewActivity.ContactData();
            data.setAlreadySetContacts(mAlreadySetContacts);
        }
        if (data != null)
            intent.putExtra(AppConstant.KEY_DATA_CONTACT, data);
        startActivityForResult(intent, AppConstant.ACTIVIY_RESULT_CONTACT);
    }

    private void initAlreadySetContacts() {
        if (mAlreadySetContacts == null) {
            if (mPlayRuleDTOList != null) {
                mAlreadySetContacts = new HashMap<>();
                for (PlayRuleDTO playRuleDTO : mPlayRuleDTOList) {
                    if (playRuleDTO == null)
                        continue;
                    final CallingParty callingPartyObj = playRuleDTO.getCallingparty();
                    if (callingPartyObj == null)
                        continue;
                    final String party = callingPartyObj.getId();
                    if (!TextUtils.isEmpty(party) && !party.equals("0"))
                        mAlreadySetContacts.put(party, true);
                }
            }
        }
    }

    private void checkPlanUpgrade(GiftnNormalPlanConfirmDialog.PLAN plan, final AppBaselineCallback<PurchaseComboResponseDTO> callback) {
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
                                    setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL, callback);
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
                            setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL, callback);
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
                                setTune(GiftnNormalPlanConfirmDialog.PLAN.NORMAL, callback);
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
        setTune(plan, callback);
    }


    public void purchaseShuffle(ChartItemDTO chartItemDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        if (parentRefId != null) {
            mConfirmationPopUpDescription = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();
        } else {
            if (mPlanLayout.getSelectedPlan() != null) {
                mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
            } else {
                mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
            }
        }
        AppUtilityDTO result = BaselineApplication.getApplication().getRbtConnector().getAppUtilityDTO();
        mNetworkType = result.getNetworkType();

        APIRequestParameters.ConfirmationType confirmationType= null;
        if(mNetworkType.equalsIgnoreCase("opt_network")){
            confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationOptNetwork();
        }
        else{
            confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationNonOptNetwork();
        }

        if (!isShowConsentPopUp(confirmationType, mIsUpgrade)) {
            //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
            BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, parentRefId, callback);
        } else {
            showProgress(false);
            AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                @Override
                public void onContinue() {
                    showProgress(true);
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                    BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);
                    BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, parentRefId, callback);
                }

                @Override
                public void onCancel() {
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
                    showProgress(false);
                }
            });
        }

        /*BaselineApplication.getApplication().getRbtConnector().getAppUtilityNetworkRequest(mCallerSource, chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                if (!isAdded()) return;
                BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);

                BaselineApplication.getApplication().getRbtConnector().dummyPurchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, parentRefId);

                mNetworkType = result.getNetworkType();


                APIRequestParameters.ConfirmationType confirmationType= null;
                if(mNetworkType.equalsIgnoreCase("opt_network")){
                    confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationOptNetwork();
                }
                else{
                    confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationNonOptNetwork();
                }

                if (!isShowConsentPopUp(confirmationType, mIsUpgrade)) {
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                    BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, parentRefId, callback);
                } else {
                    showProgress(false);
                    AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue() {
                            showProgress(true);
                            sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                            BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);
                            BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, parentRefId, callback);
                        }

                        @Override
                        public void onCancel() {
                            sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
                            showProgress(false);
                        }
                    });
                }
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                handleSetDeleteTuneError(message);
            }
        });*/

    }

    public void purchaseUDP(RingBackToneDTO item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, UdpAssetDTO udpAssetDTO, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        if (parentRefId != null) {
            mConfirmationPopUpDescription = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();
        } else {
            if (mPlanLayout.getSelectedPlan() != null) {
                mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
            } else {
                mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
            }
        }

        AppUtilityDTO result = BaselineApplication.getApplication().getRbtConnector().getAppUtilityDTO();

        mNetworkType = result.getNetworkType();


        APIRequestParameters.ConfirmationType confirmationType= null;
        if(mNetworkType.equalsIgnoreCase("opt_network")){
            confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationOptNetwork();
        }
        else{
            confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationNonOptNetwork();
        }

        if (!isShowConsentPopUp(confirmationType, mIsUpgrade)) {
            //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
            BaselineApplication.getApplication().getRbtConnector().purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, udpAssetDTO, parentRefId, callback);
        } else {
            showProgress(false);
            AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                @Override
                public void onContinue() {
                    showProgress(true);
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                    BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);
                    BaselineApplication.getApplication().getRbtConnector().purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, udpAssetDTO, parentRefId, callback);
                }

                @Override
                public void onCancel() {
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
                    showProgress(false);
                }
            });
        }
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

    private void setPayTMErrorFlow(String message, Map<String,String> extraInfoMap) {

        if (mPlanLayout.getSelectedPlan() != null) {
            mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
        } else {
            mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
        }
        if(mConfirmationPopUpDescription!=null &&  !mConfirmationPopUpDescription.equalsIgnoreCase("null")){
            message=message + mConfirmationPopUpDescription;
        }

        AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), message, new PurchaseConfirmDialog.ActionCallBack() {
            @Override
            public void onContinue() {
                if (!isAdded()) return;
                //showProgress(true);
                //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                //AppUtilityDTO  result=BaselineApplication.getApplication().getRbtConnector().getAppUtilityDTO();
                //BaselineApplication.getApplication().getRbtConnector().purchaseGift(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, null, mPlanLayout.getSelectedPlan().getParentRefId(), callbackPayTM);
                if (mPlanLayout.getSelectedPlan() != null) {
                    mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
                } else {
                    mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
                }
                isPaymentInitiatedViaPayTM = true;//this will avaoid showing pop up
                shuffleAndUDP(PurchaseMode.PAY_TM, extraInfoMap);
            }

            @Override
            public void onCancel() {
                if (!isAdded()) return;
                showProgress(false);
            }
        });
    }


    private void shuffleAndUDP(PurchaseMode purchaseMode, Map<String, String> extraInfoMap) {

        switch (purchaseMode) {
            case PAY_TM:
                shuffleAndUDPPayTM(extraInfoMap);
                break;
            case OPERATOR:
                shuffleAndUDPOperator(extraInfoMap);
                break;
        }

    }

    private void shuffleAndUDPOperator(Map<String, String> extraInfoMap) {

        if (mIsSystemShuffle) {
            ChartItemDTO chartItemDTO = mPlanLayout.getChartItemDTO();
            //BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, parentRefId, callback);
            purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, parentRefId, payTmCallBack);
        } else {
            UdpAssetDTO udpAssetDTO = fragmentHorizontalMusic.getUdpAssetDTO();
            //BaselineApplication.getApplication().getRbtConnector().purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, udpAssetDTO, parentRefId, callback);
            purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, udpAssetDTO, parentRefId, payTmCallBack);
        }
    }


    private void shuffleAndUDPPayTM(Map<String, String> extraInfoMap) {

        if (mIsSystemShuffle) {
            ChartItemDTO chartItemDTO = mPlanLayout.getChartItemDTO();
            if (parentRefId != null) {
                mConfirmationPopUpDescription = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();
            } else {
                if (mPlanLayout.getSelectedPlan() != null) {
                    mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
                } else {
                    mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
                }
            }
            AppUtilityDTO result = BaselineApplication.getApplication().getRbtConnector().getAppUtilityDTO();
            mNetworkType = result.getNetworkType();


            //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
            BaselineApplication.getApplication().getRbtConnector().purchaseShuffle(chartItemDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, parentRefId, payTmCallBack);

        } else {
            if (parentRefId != null) {
                mConfirmationPopUpDescription = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();
            } else {
                if (mPlanLayout.getSelectedPlan() != null) {
                    mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
                } else {
                    mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
                }
            }
            UdpAssetDTO udpAssetDTO = fragmentHorizontalMusic.getUdpAssetDTO();
            BaselineApplication.getApplication().getRbtConnector().purchaseUDP(mRingBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, udpAssetDTO, parentRefId, payTmCallBack);
        }
    }

    public void showSingleButtonInfoDialogFragment(String message){
        AppDialog.SingleButtonInfoDialogFragment(getRootActivity(), message, new SingleButtonInfoDialog.ActionCallBack() {
            @Override
            public void Ok() {
                showProgress(false);
            }
        });
    }
}
