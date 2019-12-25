package com.onmobile.rbt.baseline.bottomsheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.CGWebViewActivity;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.widget.PlanView;
import com.onmobile.rbt.baseline.widget.PlanViewLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import static android.app.Activity.RESULT_OK;
import static com.onmobile.rbt.baseline.util.AppConstant.OfflineCGExtras.EXTRA_OFFLINE_CONSENT_CALLBACK;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_RURL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CONSCENT_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_R_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_CONSENT_ACTIVITY;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_OFFLINE_CONSENT_ACTIVITY;

public class ChangePlanBSFragment extends BaseFragment {

    private BottomSheetFragmentListener<BaseFragment, UserSubscriptionDTO> mCallback;

    private AppCompatTextView mTvSetTune;
    private PlanViewLayout mPlanLayout;

    private UserSubscriptionDTO mUserSubscriptionDTO;
    private PricingSubscriptionDTO pricingSubscriptionDTO = null;

    private ProgressDialog progressDialog;

    public static ChangePlanBSFragment newInstance(UserSubscriptionDTO userSubscriptionDTO) {
        ChangePlanBSFragment fragment = new ChangePlanBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, userSubscriptionDTO);
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
        return R.layout.fragment_change_plan_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mUserSubscriptionDTO = (UserSubscriptionDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mPlanLayout = view.findViewById(R.id.layout_plan);

        mTvSetTune = view.findViewById(R.id.tv_save_bottom_sheet);

        loadView();
    }

    public void loadView() {
        mTvSetTune.setEnabled(false);

        mPlanLayout.addOnPlanSelectedListener(mPlanSelectedListener);
        mTvSetTune.setOnClickListener(mClickListener);

        populatePlans();
    }

    @Override
    protected void bindViews(View view) {

    }

    public ChangePlanBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, UserSubscriptionDTO> callback) {
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
                //mPlanLayout.error(errMsg);
                PlanView userSubscriptionPlanView = new PlanView(getFragmentContext());
                userSubscriptionPlanView.setUserSubscription(true);
                mPlanLayout.addPlan(userSubscriptionPlanView, mUserSubscriptionDTO);
                userSubscriptionPlanView.setChecked(true);
                updatePeekHeight();
            }
        });



    }

    private void fetchPlans(){
        AppManager.getInstance().getRbtConnector().getListOfPlans(new AppBaselineCallback<List<PricingSubscriptionDTO>>() {
            @Override
            public void success(List<PricingSubscriptionDTO> result) {
                if (!isAdded()) return;
                PlanView userSubscriptionPlanView = new PlanView(getFragmentContext());
                userSubscriptionPlanView.setUserSubscription(true);
                mPlanLayout.addPlan(userSubscriptionPlanView, mUserSubscriptionDTO);
                userSubscriptionPlanView.setChecked(true);
                if (result != null && result.size() > 0) {
                    for (PricingSubscriptionDTO priceDTO : result) {
                        if (!priceDTO.getCatalog_subscription_id().equals(mUserSubscriptionDTO.getCatalog_subscription_id())) {
                            PlanView plan = new PlanView(getFragmentContext());
                            mPlanLayout.addPlan(plan, priceDTO);
                            if (mUserSubscriptionDTO == null &&
                                    mPlanLayout.getPlanCount() == 1) {
                                plan.setChecked(true);
                            }
                        }
                    }
                }
                updatePeekHeight();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                //mPlanLayout.error(errMsg);
                PlanView userSubscriptionPlanView = new PlanView(getFragmentContext());
                userSubscriptionPlanView.setUserSubscription(true);
                mPlanLayout.addPlan(userSubscriptionPlanView, mUserSubscriptionDTO);
                userSubscriptionPlanView.setChecked(true);
                updatePeekHeight();
            }
        });
    }

    private PlanViewLayout.PlanSelectedListener mPlanSelectedListener = this::planSelected;

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mTvSetTune.getId()) {
                showProgress(true);

                pricingSubscriptionDTO = mPlanLayout.getSelectedPlan().getPriceDTO();
                if (pricingSubscriptionDTO == null) {
                    return;
                }

                AppManager.getInstance().getRbtConnector().changePlan(pricingSubscriptionDTO, null,new AppBaselineCallback<UserSubscriptionDTO>() {
                    @Override
                    public void success(UserSubscriptionDTO purchaseComboResponseDTO) {
                        if (!isAdded()) return;
                        if (purchaseComboResponseDTO != null) {
                            PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = purchaseComboResponseDTO.getThirdpartyconsent();
                            Intent intent = new Intent();
                            if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
//                                intent.setClass(getRootActivity(), CGOfflineActivity.class);
//                                intent.putExtra(INTENT_EXTRA_OFFLINE_RURL, thirdPartyConsentDTO.getReturn_url());
//                                //intent.putExtra(INTENT_EXTRA_IMAGE_URL, null);
//                                intent.putExtra(INTENT_EXTRA_PRICING_TEXT, pricingSubscriptionDTO.getDescription());
//                                startActivityForResult(intent, RESULT_OFFLINE_CONSENT_ACTIVITY);
                            } else if (thirdPartyConsentDTO != null) {
                                intent.setClass(getRootActivity(), CGWebViewActivity.class);
                                intent.putExtra(EXTRA_CONSCENT_URL, thirdPartyConsentDTO.getThird_party_url());
                                intent.putExtra(EXTRA_R_URL, thirdPartyConsentDTO.getReturn_url());
                                startActivityForResult(intent, RESULT_CONSENT_ACTIVITY);
                            } else {
                                showProgress(false);
                                if (mCallback != null)
                                    mCallback.next(ChangePlanBSFragment.this, null);
                                // mPreBuyPresenter.purchaseComboResponsePlayRuleHandler(purchaseComboResponseDTO);
                            }

                        }
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);
//                        if (mCallback != null)
//                            mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);
                    }
                });

            }
        }
    };

    private void planSelected(PlanView plan) {
        if (plan == null)
            return;
        if (plan.isChecked()) {
            mTvSetTune.setEnabled(true);
            if (!plan.isUserSubscription()) {
                mPlanLayout.setFooterText(plan.getPriceDTO().getDescription());
                mTvSetTune.setEnabled(true);
            } else {
                mPlanLayout.setFooterText(plan.getUserSubscriptionDTO().getCatalog_subscription().getDescription());
                mTvSetTune.setEnabled(false);
            }
        }
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(ChangePlanBSFragment.this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == RESULT_OFFLINE_CONSENT_ACTIVITY) {
                showProgress(false);
                String status = (String) data.getSerializableExtra(EXTRA_OFFLINE_CONSENT_CALLBACK);
                if (status != null && status.equalsIgnoreCase("success")) {
                    if (mCallback != null)
                        mCallback.next(ChangePlanBSFragment.this, null);
                } else {
                    if (mCallback != null)
                        mCallback.done(ChangePlanBSFragment.this, null);
                }
            } else if (requestCode == RESULT_CONSENT_ACTIVITY) {
                String cgrUrl = data.getStringExtra(EXTRA_CG_RURL);
                AppManager.getInstance().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                    @Override
                    public void success(RUrlResponseDto result) {
                        if (!isAdded()) return;
                        showProgress(false);
                        if (mCallback != null)
                            mCallback.next(ChangePlanBSFragment.this, null);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);
                        if (mCallback != null)
                            mCallback.done(ChangePlanBSFragment.this, null);
//                        if (mCallback != null)
//                            mCallback.done(SetCallerTunePlansBSFragment.this, mRingBackToneDTO);

                    }
                }, cgrUrl, null);
            }
        } else {
            showProgress(false);
        }
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
}
