package com.onmobile.rbt.baseline.bottomsheet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.IPreBuyUDSCheck;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.CGWebViewActivity;
import com.onmobile.rbt.baseline.activities.ContactViewActivity;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.base.TuneBottomSheetUtil;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.PurchaseConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.ShuffleUpgradeDialog;
import com.onmobile.rbt.baseline.dialog.custom.SingleButtonInfoDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.PurchaseMode;
import com.onmobile.rbt.baseline.widget.LabeledView;
import com.onmobile.rbt.baseline.widget.PlanView;
import com.onmobile.rbt.baseline.widget.PlanViewLayout;
import com.onmobile.rbt.baseline.widget.ShowCaseBuilderManager;
import com.onmobile.rbt.baseline.widget.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

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

public class SetCallerTunePlansBSFragment extends BaseFragment {

    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;
    public static boolean fromPreBuyAudioShowPopUp = false;
    private AppCompatTextView mTvSetTune, mTvAddMoreContactCount;
    private PlanViewLayout mPlanLayout;
    private LabeledView mAllCallerLabeledView, mSpecialCallersLabeledView;
    private Chip mChipAddMoreContact;
    private final String TAG = SetCallerTunePlansBSFragment.class.getSimpleName();
    private RingBackToneDTO mRingBackToneDTO;
    private PricingSubscriptionDTO pricingSubscriptionDTO = null;
    private PricingIndividualDTO pricingIndividualDTO = null;

    private ProgressDialog progressDialog;
    private Map<String, ContactModelDTO> mContactModel;
    private List<PlayRuleDTO> mPlayRuleDTOList;
    private boolean isSelected;
    private boolean mUserInteractedWithLabelView;
    private Map<String, Boolean> mAlreadySetContacts;

    private boolean isGiftPresent = false;
    String mConfirmationPopUpDescription = null;
    private LinearLayout mCallersChoiceLayout;
    private TextView mPlayForAllCallersInfoText;
    private String mCallerSource;
    //private boolean isPaymentInitiatedViaPayTM;

    private Map<String, String> mSelectedContacts;
    private String mNetworkType;

    private ShowCaseBuilderManager mShowCaseBuilderManager;
    private boolean mIsUpgrade = false;

    public static SetCallerTunePlansBSFragment newInstance(String callerSource, RingBackToneDTO ringBackToneDTO) {
        SetCallerTunePlansBSFragment fragment = new SetCallerTunePlansBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return SetCallerTunePlansBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_caller_tune_plans_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
        }
    }

    @Override
    protected void initComponents() {
        mContactModel = new HashMap<>();
    }

    @Override
    protected void initViews(View view) {
        mShowCaseBuilderManager = new ShowCaseBuilderManager(FunkyAnnotation.TYPE_BS_REG_SET_TUNES, getFragmentContext(), view.findViewById(R.id.layout_showcase_bottom_sheet));

        mPlanLayout = view.findViewById(R.id.layout_plan);

        mTvSetTune = view.findViewById(R.id.tv_set_bottom_sheet);

        mChipAddMoreContact = view.findViewById(R.id.chip_add_more_contact_root_set_action);
        mTvAddMoreContactCount = view.findViewById(R.id.tv_add_more_contact_root_set_action);

        mAllCallerLabeledView = view.findViewById(R.id.all_callers_labeled_view);
        mSpecialCallersLabeledView = view.findViewById(R.id.special_callers_labeled_view);

        mCallersChoiceLayout = view.findViewById(R.id.caller_choice_layout);
        mPlayForAllCallersInfoText = view.findViewById(R.id.play_for_all_callers_info);
    }

    public void loadView() {
        if (AppManager.getInstance().getRbtConnector().isActiveUser()) {
            mCallersChoiceLayout.setVisibility(View.VISIBLE);
            mPlayForAllCallersInfoText.setVisibility(View.GONE);
        } else {
            mCallersChoiceLayout.setVisibility(View.GONE);
            mPlayForAllCallersInfoText.setVisibility(View.VISIBLE);
        }

        isSelected = AppManager.getInstance().getRbtConnector().isRingbackSelected(mRingBackToneDTO.getId()) && !mRingBackToneDTO.isCut();
        if (isSelected) {
            mPlayRuleDTOList = AppManager.getInstance().getRbtConnector().getPlayRuleById(mRingBackToneDTO.getId());
        }

        mAllCallerLabeledView.setListener(mLabeledListener);
        mSpecialCallersLabeledView.setListener(mLabeledListener);
        mChipAddMoreContact.setOnChipClickListener(v -> {
            mUserInteractedWithLabelView = true;
            openContactActivity();
        });

        /*hidePlans();
        showPlanLoading();*/
        mTvSetTune.setEnabled(false);
        mChipAddMoreContact.setVisibility(View.GONE);

        mPlanLayout.addOnPlanSelectedListener(mPlanSelectedListener);
        mTvSetTune.setOnClickListener(mClickListener);

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

        AppManager.getInstance().getRbtConnector().isFamilyAndFriends(new IAppFriendsAndFamily() {
            @Override
            public void isParent(boolean exist) {
                populatePlans();
            }

            @Override
            public void isChild(boolean exist) {
                isGiftPresent = true;
                populatePlans();
            }

            @Override
            public void isNone(boolean exist) {
                if (!AppConfigurationValues.isSelectionModel() && AppManager.getInstance().getRbtConnector().isSongPurchased(mRingBackToneDTO.getId())) {
                    mPlanLayout.setRingBackToneDTO(mRingBackToneDTO);
                    if (AppManager.getInstance().getRbtConnector().getTuneAlreadyPurchasedMessage() != null) {
                        mPlanLayout.error(AppManager.getInstance().getRbtConnector().getTuneAlreadyPurchasedMessage());
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

    @Override
    protected void bindViews(View view) {
        if (mShowCaseBuilderManager != null)
            mShowCaseBuilderManager.build(true, mRingBackToneDTO);
        loadView();
    }

    public SetCallerTunePlansBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    private void populatePlans() {
        mPlanLayout.loading();
        AppManager.getInstance().getRbtConnector().checkUser(new AppBaselineCallback<Boolean>() {
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
        AppManager.getInstance().getRbtConnector().getContent(mRingBackToneDTO.getId(), new AppBaselineCallback<RingBackToneDTO>() {
            @Override
            public void success(RingBackToneDTO result) {
                if (!isAdded()) return;
                result.setCut(mRingBackToneDTO.isCut());
                result.setCutStart(mRingBackToneDTO.getCutStart());
                result.setCutEnd(mRingBackToneDTO.getCutEnd());
                result.setDigitalStar(mRingBackToneDTO.isDigitalStar());
                mPlanLayout.setRingBackToneDTO(result);
                List<PricingSubscriptionDTO> pricingSubscriptionDTOS = result.getPricingSubscriptionDTOS();
                if (pricingSubscriptionDTOS != null && pricingSubscriptionDTOS.size() > 0) {
                    if (isGiftPresent) {
                        PlanView giftPlan = new PlanView(getFragmentContext());
                        giftPlan.setGift(true);
                        giftPlan.setParentRefId(AppManager.getInstance().getRbtConnector().getCacheChildInfo().getParentId());
                        mPlanLayout.addPlan(giftPlan, AppManager.getInstance().getRbtConnector().getCacheChildInfo().getCatalogSubscription());
                        if (mPlanLayout.getPlanCount() == 1)
                            giftPlan.setChecked(true);
                    }
                    for (PricingSubscriptionDTO priceDTO : pricingSubscriptionDTOS) {
                        PlanView plan = new PlanView(getFragmentContext());
                        if (priceDTO.getSong_prices() != null && !priceDTO.getSong_prices().isEmpty()) {
                            UserSubscriptionDTO.Song_prices song_prices = priceDTO.getSong_prices().get(0);
                            if (song_prices.getRetail_price() != null &&
                                    song_prices.getRetail_price().getAmount() != null) {
                                mPlanLayout.addPlan(plan, priceDTO);
                            }
                        } else if (priceDTO.getRetail_priceObject() != null &&
                                priceDTO.getRetail_priceObject().getAmount() != null) {
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
                        if (!isSelected)
                            mTvSetTune.setEnabled(ack);
                    } else {
                        mPlanLayout.error(getString(R.string.msg_empty_plan));
                    }
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
    }

    private PlanViewLayout.PlanSelectedListener mPlanSelectedListener = this::planSelected;

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mTvSetTune.getId()) {
                pricingIndividualDTO = null;
                mIsUpgrade = false;
                checkAndSetTune();
            }
        }
    };

    private void planSelected(PlanView plan) {
        if (plan == null)
            return;
        if (plan.isChecked()) {
            mTvSetTune.setEnabled(true);
            updateConfirmationButton();
            if (plan.isGift()) {
                try {
                    mPlanLayout.setFooterText(AppManager.getInstance().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription());
                } catch (Exception e) {
                    mPlanLayout.setFooterText("");
                }
            } else {
                mPlanLayout.setFooterText(plan.getPriceDTO().getDescription());
            }
        }
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(SetCallerTunePlansBSFragment.this);
    }

    private LabeledView.OnLabeledListener mLabeledListener = new LabeledView.OnLabeledListener() {
        @Override
        public void onClick(LabeledView view) {

        }

        @Override
        public void onSwitch(LabeledView view, boolean checked) {
            mUserInteractedWithLabelView = true;
            if (view.getId() == mAllCallerLabeledView.getId()) {
                /*if (!isSelected) {
                    if (checked) {
                        mSpecialCallersLabeledView.disableSwitchStatusSilently();
                    } else {
                        if (!mSpecialCallersLabeledView.getSwitchStatus()) {
                            mAllCallerLabeledView.enableSwitchStatusSilently();
                        }
                    }
                } else {
                    if (checked) {
                        if (!isSetForSpecialCallers(mPlayRuleDTOList)) {
                            mSpecialCallersLabeledView.disableSwitchStatusSilently();
                        } else {
                            mAllCallerLabeledView.disableSwitchStatusSilently();
                            getRootActivity().showShortToast(getString(R.string.msg_remove_special_callers));
                        }
                    } else {
                        if (!mSpecialCallersLabeledView.getSwitchStatus()) {
                            mAllCallerLabeledView.enableSwitchStatusSilently();
                            deletePlayRule();
                            //getRootActivity().showShortToast("This will delete all callers");
                        }
                    }
                }*/
                if (checked) {
                    mSpecialCallersLabeledView.disableSwitchStatusSilently();
                } else {
                    mAllCallerLabeledView.enableSwitchStatusSilently();
                }
                //mContactModel = null;
            } else if (view.getId() == mSpecialCallersLabeledView.getId()) {
                /*if (!isSelected) {
                    if (checked) {
                        //TODO redirect and select contacts
                        openContactActivity(
;                        mSpecialCallersLabeledView.disableSwitchStatusSilently();
                    } else {
                        mAllCallerLabeledView.enableSwitchStatusSilently();
                    }
                } else {
                    if (checked) {
                        mSpecialCallersLabeledView.disableSwitchStatusSilently();
                        getRootActivity().showShortToast(getString(R.string.msg_remove_all_callers));
                    } else {
                        if (!(isSetForSpecialCallers(mPlayRuleDTOList))) {
                            mAllCallerLabeledView.enableSwitchStatusSilently();
                        } else {
                            mSpecialCallersLabeledView.enableSwitchStatusSilently();
                            deletePlayRule();
                        }
                    }
                }*/
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
            // PAYTM RURL parsing

            if (resultCode == RESULT_OK && data != null) {
                ContactViewActivity.ContactData resultData = (ContactViewActivity.ContactData) data.getSerializableExtra(AppConstant.KEY_DATA_CONTACT);
                ContactModelDTO modelDTO = resultData.getSelectedContact();
                if (modelDTO != null && !TuneBottomSheetUtil.isSpecialCallerExists(mPlayRuleDTOList, modelDTO.getMobileNumber())) {
                    if (mContactModel == null)
                        mContactModel = new HashMap<>();
                    //mContactModel.put(modelDTO.mobileNumber, modelDTO); //TODO purchaseDefault combo doesn't support multiple contact purchaseDefault in one request.

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
//                    if(mPlanLayout != null && mPlanLayout.getSelectedPlan() != null &&
//                            mPlanLayout.getSelectedPlan().isGift()){
//                        updateChildInfo();
//                    }
//                    if (mCallback != null)
//                        mCallback.next(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
//                } else {
//                    if (mCallback != null)
//                        mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
//                }
            } else if (requestCode == RESULT_CONSENT_ACTIVITY) {
                if (data.hasExtra(EXTRA_CG_ERROR)) {
                    if (EXTRA_CG_ERROR.equalsIgnoreCase(data.getStringExtra(EXTRA_CG_ERROR))) {
                        //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_ONLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_FAILURE);
                        return;
                    }
                }
                String cgrUrl = data.getStringExtra(EXTRA_CG_RURL);
                AppManager.getInstance().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                    @Override
                    public void success(RUrlResponseDto result) {
                        if (!isAdded()) return;
                        /*sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_ONLINE,
                                AnalyticsConstants.SUCCESS.equalsIgnoreCase(result.getMessage()) ?
                                        AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES : AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_NO);*/
                        showProgress(false);
                        if (mPlanLayout != null && mPlanLayout.getSelectedPlan() != null &&
                                mPlanLayout.getSelectedPlan().isGift()) {
                            updateChildInfo();
                        } else if (AppConfigurationValues.isFamilyAndFriendsEnabled() && mPlanLayout != null && mPlanLayout.getSelectedPlan() != null &&
                                mPlanLayout.getSelectedPlan().getPriceDTO().getClass_of_service()
                                        .equalsIgnoreCase(APIRequestParameters.FAMILY_AND_FRIENDS.FAMILY_PARENT.toString())) {
                            showGiftRbtSuggestionPopUp();
                            return;
                        }


                        if (mCallback != null)
                            mCallback.next(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);
                        if (mCallback != null)
                            mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
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

    public void showProgress(boolean showProgress) {
        if (showProgress)
            stopMedia();
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

    private void updateConfirmationButton() {
        if (!isAdded())
            return;
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

    private void checkAndSetTune() {
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
                        checkPlanUpgrade(new AppBaselineCallback<PurchaseComboResponseDTO>() {
                            @Override
                            public void success(PurchaseComboResponseDTO purchaseComboResponseDTO) {
                                if (!isAdded()) return;
                                handleSetTuneSuccess(purchaseComboResponseDTO);
                            }

                            @Override
                            public void failure(String errMsg) {
                                if (!isAdded()) return;
                                handleSetDeleteTuneError(errMsg);
                                loadView();
                                updateConfirmationButton();
                                updateAddMoreContactButton();
                            }
                        });
                    } else {
                        showProgress(false);
                        getRootActivity().showShortToast(getString(R.string.unset_success_message));
                        mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
                    }

                }

                @Override
                public void failure(String message) {
                    if (!isAdded()) return;
                    handleSetDeleteTuneError(message);
                }
            });
        } else {
            checkPlanUpgrade(new AppBaselineCallback<PurchaseComboResponseDTO>() {
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

    private AppBaselineCallback<PurchaseComboResponseDTO> callbackPayTM;
    RingBackToneDTO ringBackToneDTO;

    private void setTune(AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        callbackPayTM = callback;
        showProgress(true);
        ringBackToneDTO = mPlanLayout.getRingBackToneDTO();
        pricingSubscriptionDTO = null;
        //pricingIndividualDTO = null;
        if (mPlanLayout.getPlanCount() == 0) {
            if (pricingIndividualDTO == null)
                pricingIndividualDTO = mPlanLayout.getPricingIndividualDTO();
        } else {
            pricingSubscriptionDTO = mPlanLayout.getSelectedPlan().getPriceDTO();
        }
        Map<String, String> contactMap = new HashMap<>();
        if (mSpecialCallersLabeledView.getSwitchStatus() && mContactModel != null) {
            for (ContactModelDTO modelDTO : mContactModel.values()) {
                //contactMap.put(modelDTO.getName(), modelDTO.getMobileNumber());
                contactMap.put(modelDTO.getMobileNumber(), modelDTO.getMobileNumber());
            }
        }
        mSelectedContacts = contactMap;
        AppManager.getInstance().getRbtConnector().getAppUtilityNetworkRequest(mCallerSource, ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                if (!isAdded()) return;
                AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);

                dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap);

                if (mPlanLayout.getSelectedPlan() != null && mPlanLayout.getSelectedPlan().isGift()) {
                    purchaseGiftDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, null, mPlanLayout.getSelectedPlan().getParentRefId(), callback);
                } else {
                    purchaseDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, null, callback);
                }

            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                handleSetDeleteTuneError(message);
            }
        });

        /*if (mPlanLayout.getSelectedPlan() != null && mPlanLayout.getSelectedPlan().isGift()) {
            purchaseGiftDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, mPlanLayout.getSelectedPlan().getParentRefId(), callback);
            //AppManager.getInstance().getRbtConnector().purchaseGiftDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, mPlanLayout.getSelectedPlan().getParentRefId(), callback);
        } else {
            //TODO needs to be verified
            // Earlier call  to purchaseDefault API
            //purchaseDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, callback);

        }*/
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
        AppManager.getInstance().getRbtConnector().deletePlayRule(playRuleIds, callback);
    }

    private void handleSetTuneSuccess(PurchaseComboResponseDTO purchaseComboResponseDTO) {
        showProgress(false);
        if (purchaseComboResponseDTO != null) {
            PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = purchaseComboResponseDTO.getThirdpartyconsent();
            Intent intent = new Intent();
            if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
                NonNetworkCGDTO nonNetworkCG = AppManager.getInstance().getRbtConnector().getNonNetworkCG();
                AppManager.getInstance().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
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
                if (mPlanLayout != null && mPlanLayout.getSelectedPlan() != null &&
                        mPlanLayout.getSelectedPlan().isGift()) {
                    updateChildInfo();
                } else if (AppConfigurationValues.isFamilyAndFriendsEnabled() && purchaseComboResponseDTO.getSubscription() != null &&
                        mPlanLayout != null && mPlanLayout.getSelectedPlan() != null &&
                        mPlanLayout.getSelectedPlan().getPriceDTO().getClass_of_service()
                                .equalsIgnoreCase(APIRequestParameters.FAMILY_AND_FRIENDS.FAMILY_PARENT.toString())) {
                    showGiftRbtSuggestionPopUp();
                    return;
                }
                if (mCallback != null)
                    mCallback.next(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
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

    private void checkPlanUpgrade(AppBaselineCallback<PurchaseComboResponseDTO> callback) {

        if (AppConfigurationValues.isPlanUpgradable() && mPlanLayout.getExtras() != null && mPlanLayout.getExtras() instanceof List) {
            try {
                List<PricingIndividualDTO> list = (List<PricingIndividualDTO>) mPlanLayout.getExtras();
                if (list != null && list.size() > 1) {
                    showProgress(false);
                    AppManager.getInstance().getRbtConnector().checkPlanUpgrade(list, new IPreBuyUDSCheck() {
                        @Override
                        public void showUDSUpdatePopUp(List<PricingIndividualDTO> pricingIndividualDTOS) {
                            AppDialog.showPlanUpgradeDialog(getRootActivity(), pricingIndividualDTOS, new ShuffleUpgradeDialog.ActionCallBack() {
                                @Override
                                public void onContinue(PricingIndividualDTO item) {
                                    mIsUpgrade = true;
                                    pricingIndividualDTO = item;
                                    //sendUpgradeAnalytics(item.getCatalogSubscriptionId(), true);
                                    setTune(callback);
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
                            setTune(callback);
                        }
                    });
                    return;
                } else if (list != null && list.size() == 1 && list.get(0).getCatalogSubscriptionId() != null && AppManager.getInstance().getRbtConnector().getCacheUserSubscription() != null
                        && !list.get(0).getCatalogSubscriptionId().equalsIgnoreCase(AppManager.getInstance().getRbtConnector().getCacheUserSubscription().getCatalog_subscription_id())) {
                    if (AppConfigurationValues.IsShowSinglePlanUpgrade()) {
                        AppDialog.showPlanUpgradeDialog(getRootActivity(), list, new ShuffleUpgradeDialog.ActionCallBack() {
                            @Override
                            public void onContinue(PricingIndividualDTO item) {
                                mIsUpgrade = true;
                                pricingIndividualDTO = item;
                                //sendUpgradeAnalytics(item.getCatalogSubscriptionId(), true);
                                setTune(callback);
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
        setTune(callback);
    }

    private void updateChildInfo() {
        AppManager.getInstance().getRbtConnector().getChildInfo(new AppBaselineCallback<GetChildInfoResponseDTO>() {
            @Override
            public void success(GetChildInfoResponseDTO result) {
                if (!isAdded()) return;
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
            }
        });
    }

    private void purchaseDefault(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        if (mPlanLayout.getSelectedPlan() != null) {
            mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
        } else if(pricingIndividualDTO != null) {
            mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
        }

        AppUtilityDTO result = AppManager.getInstance().getRbtConnector().getAppUtilityDTO();

        mNetworkType = result.getNetworkType();
        APIRequestParameters.ConfirmationType confirmationType= null;
        if(mNetworkType.equalsIgnoreCase("opt_network")){
            confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationOptNetwork();
        }
        else{
            confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationNonOptNetwork();
        }

        if (!isShowConsentPopUp(confirmationType, mIsUpgrade)) {
            AppManager.getInstance().getRbtConnector().purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, callback);

        } else {
            showProgress(false);
            AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                @Override
                public void onContinue() {
                    if (!isAdded()) return;
                    showProgress(true);
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                    AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);
                    AppManager.getInstance().getRbtConnector().purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, callback);
                }

                @Override
                public void onCancel() {
                    if (!isAdded()) return;
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_CANCEL);
                    showProgress(false);
                }
            });
        }
        /*AppManager.getInstance().getRbtConnector().getAppUtilityNetworkRequest(mCallerSource, ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                if (!isAdded()) return;
                AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);

                AppManager.getInstance().getRbtConnector().dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, null);

                mNetworkType = result.getNetworkType();

                APIRequestParameters.ConfirmationType confirmationType= null;
                if(mNetworkType.equalsIgnoreCase("opt_network")){
                    confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationOptNetwork();
                }
                else{
                    confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationNonOptNetwork();
                }

                if (!isShowConsentPopUp(confirmationType, mIsUpgrade)) {
                    AppManager.getInstance().getRbtConnector().purchaseDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, callback);
                } else {
                    showProgress(false);
                    AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue() {
                            if (!isAdded()) return;
                            showProgress(true);
                            sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                            AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);
                            AppManager.getInstance().getRbtConnector().purchaseDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, callback);
                        }

                        @Override
                        public void onCancel() {
                            if (!isAdded()) return;
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

    private void purchaseGiftDefault(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        mConfirmationPopUpDescription = AppManager.getInstance().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();
        AppUtilityDTO result = AppManager.getInstance().getRbtConnector().getAppUtilityDTO();
        //AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);
        mNetworkType = result.getNetworkType();

        APIRequestParameters.ConfirmationType confirmationType= null;
        if(mNetworkType.equalsIgnoreCase("opt_network")){
            confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationOptNetwork();
        }
        else{
            confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationNonOptNetwork();
        }

        if (!isShowConsentPopUp(confirmationType, mIsUpgrade)) {
            //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
            AppManager.getInstance().getRbtConnector().purchaseGift(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, mPlanLayout.getSelectedPlan().getParentRefId(), callback);
        } else  {
            showProgress(false);
            AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), mConfirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                @Override
                public void onContinue() {
                    if (!isAdded()) return;
                    showProgress(true);
                    //sendConsentAnalytics(AnalyticsConstants.EVENT_PV_USER_CONSENT_TYPE_INLINE, AnalyticsConstants.EVENT_PV_USER_CONSENT_RESULT_YES);
                    AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);
                    AppManager.getInstance().getRbtConnector().purchaseGift(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, mPlanLayout.getSelectedPlan().getParentRefId(), callback);
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

    private void showGiftRbtSuggestionPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getRootActivity());
        builder.setMessage(getString(R.string.gift_suggestion_popup_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.gift_suggestion_popup_continue), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showProgress(false);
                        dialog.cancel();

                        if (mCallback != null)
                            mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);

                        if (getActivity() instanceof HomeActivity) {
                            ((HomeActivity) getActivity()).moveToProfile();
                        } else {
                            fromPreBuyAudioShowPopUp = true;
                        }

                    }
                })
                .setNegativeButton(getString(R.string.gift_suggestion_popup_cancel), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopMedia();
    }

    private void stopMedia() {
        if (mShowCaseBuilderManager != null)
            mShowCaseBuilderManager.stopMusic();
    }

    private boolean isShowConsentPopUp(APIRequestParameters.ConfirmationType confirmationType, boolean isUpgrade) {
        boolean isActiveUser = AppManager.getInstance().getRbtConnector().isActiveUser();
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
                purchase(PurchaseMode.PAY_TM,extraInfoMap);
            }

            @Override
            public void onCancel() {
                if (!isAdded()) return;
                showProgress(false);
            }
        });
    }


    private void purchase(PurchaseMode purchaseMode, Map<String, String> extraInfoMap) {

        switch (purchaseMode) {
            case PAY_TM:
                purchasePayTM(extraInfoMap);
                break;
            case OPERATOR:
                purchaseOperator(extraInfoMap);
                break;
        }

    }


    private void purchaseOperator(Map<String, String> extraInfoMap) {

        if (mPlanLayout.getSelectedPlan() != null && mPlanLayout.getSelectedPlan().isGift()) {
            purchaseGiftDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, mPlanLayout.getSelectedPlan().getParentRefId(), callbackPayTM);
        } else {
            purchaseDefault(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, callbackPayTM);
        }
    }


    private void purchasePayTM(Map<String, String> extraInfoMap) {

        if (mPlanLayout.getSelectedPlan() != null && mPlanLayout.getSelectedPlan().isGift()) {

            if (mPlanLayout.getSelectedPlan() != null) {
                mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
            } else {
                mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
            }

            AppUtilityDTO result = AppManager.getInstance().getRbtConnector().getAppUtilityDTO();
            mNetworkType = result.getNetworkType();
            AppManager.getInstance().getRbtConnector().purchaseGift(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, mPlanLayout.getSelectedPlan().getParentRefId(),callbackPayTM);


        } else {

            if (mPlanLayout.getSelectedPlan() != null) {
                mConfirmationPopUpDescription = mPlanLayout.getSelectedPlan().getPriceDTO().getDescription();
            } else {
                mConfirmationPopUpDescription = pricingIndividualDTO.getLongDescription();
            }

            AppUtilityDTO result = AppManager.getInstance().getRbtConnector().getAppUtilityDTO();
            mNetworkType = result.getNetworkType();
            AppManager.getInstance().getRbtConnector().purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, mSelectedContacts, extraInfoMap, callbackPayTM);


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

    private void dummyPurchase(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap){
        if (mPlanLayout.getSelectedPlan() != null && mPlanLayout.getSelectedPlan().isGift()) {
            AppManager.getInstance().getRbtConnector().dummyPurchaseGift(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, mPlanLayout.getSelectedPlan().getParentRefId());
        } else {
            AppManager.getInstance().getRbtConnector().dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap);
        }
    }
}
