package com.onmobile.rbt.baseline.bottomsheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.CGWebViewActivity;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.PurchaseConfirmDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.customview.RegularTextView;

import androidx.annotation.NonNull;

import static android.app.Activity.RESULT_OK;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_RURL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CONSCENT_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_R_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_CONSENT_ACTIVITY;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_OFFLINE_CONSENT_ACTIVITY;

public class AcceptGiftBSFragment extends BaseFragment {

    private BottomSheetFragmentListener<BaseFragment, Boolean> mCallback;

    private Animation mAnimBottomUp, mAnimBottomDown;
    private RegularTextView mAcceptBtn;
    private TextView mAcceptGiftDescription;
    private GetChildInfoResponseDTO mChildInfo;
    private ContactModelDTO mContactModelDTO;
    private ProgressDialog mProgressDialog;

    public static AcceptGiftBSFragment newInstance(GetChildInfoResponseDTO childInfo, ContactModelDTO contactModelDTO) {
        AcceptGiftBSFragment fragment = new AcceptGiftBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_GIFT_CHILD_INFO, childInfo);
        bundle.putSerializable(AppConstant.KEY_GIFT_PARENT_CONTACT, contactModelDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return AcceptGiftBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_accept_gift_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mChildInfo = (GetChildInfoResponseDTO) bundle.getSerializable(AppConstant.KEY_GIFT_CHILD_INFO);
            mContactModelDTO = (ContactModelDTO) bundle.getSerializable(AppConstant.KEY_GIFT_PARENT_CONTACT);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mAcceptBtn = view.findViewById(R.id.tv_accept_gift);
        mAcceptBtn.setOnClickListener(mAcceptBtnClick);
        mAcceptGiftDescription = view.findViewById(R.id.accept_gift_description);
    }

    @Override
    protected void bindViews(View view) {
        String expiry = AppUtils.convertToGiftValidity(mChildInfo.getExpirationTime());
        String acceptGiftDescription = String.format(getString(R.string.accept_gift_bottom_sheet_description), mContactModelDTO.getName(), expiry);
        mAcceptGiftDescription.setText(Html.fromHtml(acceptGiftDescription));
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

    public AcceptGiftBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, Boolean> callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(AcceptGiftBSFragment.this);
    }

    private View.OnClickListener mAcceptBtnClick = view -> {
        activateChild();
    };


    private void activateChild() {
        showProgress(true);

        String confirmationPopUpDescription = AppManager.getInstance().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();

        AppManager.getInstance().getRbtConnector().getAppUtilityNetworkRequest(new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                if (!isAdded()) return;

                String mNetworkType = result.getNetworkType();

                AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);

                GetChildInfoResponseDTO dummyChildInfo = AppManager.getInstance().getRbtConnector().getCacheChildInfo();
                AppManager.getInstance().getRbtConnector().dummyChildPurchaseSubscription(dummyChildInfo.getCatalogSubscription().getId(), dummyChildInfo.getParentId());

                APIRequestParameters.ConfirmationType confirmationType= null;
                if(mNetworkType.equalsIgnoreCase("opt_network")){
                    confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationOptNetwork();
                }
                else{
                    confirmationType = AppManager.getInstance().getRbtConnector().getConfirmationNonOptNetwork();
                }

                if (!isShowConsentPopUp(confirmationType, false)) {
                    createChild();
                } else {
                    showProgress(false);
                    AppDialog.PurchaseConfirmDialogFragment(getRootActivity(), confirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue() {
                            if (!isAdded()) return;
                            showProgress(true);
                            AppManager.getInstance().getRbtConnector().setAppUtilityDTO(result);
                            createChild();
                        }

                        @Override
                        public void onCancel() {
                            if (!isAdded()) return;
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

    public void createChild() {
        GetChildInfoResponseDTO childInfo = AppManager.getInstance().getRbtConnector().getCacheChildInfo();
        AppManager.getInstance().getRbtConnector().createChildUserSubscription(childInfo.getCatalogSubscription().getId(), childInfo.getParentId(), new AppBaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO result) {
                if (!isAdded()) return;
                if (result != null) {
                    PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = result.getThirdpartyconsent();
                    Intent intent = new Intent();
                    if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
                        NonNetworkCGDTO nonNetworkCG = AppManager.getInstance().getRbtConnector().getNonNetworkCG();
                        AppManager.getInstance().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
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
                        showProgress(false);
                        mCallback.next(AcceptGiftBSFragment.this, true);
                    }

                }

            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                showProgress(false);
                getRootActivity().showShortToast(message);
                if (message.equals(getString(R.string.error_gift_expired))) {
                    mCallback.done(AcceptGiftBSFragment.this, true);
                }
            }
        },null);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == RESULT_OFFLINE_CONSENT_ACTIVITY) {
//                showProgress(false);
////                String status = (String) data.getSerializableExtra(EXTRA_OFFLINE_CONSENT_CALLBACK);
////                if (status != null && status.equalsIgnoreCase("success")) {
////                    mCallback.next(AcceptGiftBSFragment.this, true);
////                } else {
////                    mCallback.done(AcceptGiftBSFragment.this, true);
////                }
            } else if (requestCode == RESULT_CONSENT_ACTIVITY) {
                String cgrUrl = data.getStringExtra(EXTRA_CG_RURL);
                AppManager.getInstance().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                    @Override
                    public void success(RUrlResponseDto result) {
                        if (!isAdded()) return;
                        showProgress(false);
                        mCallback.next(AcceptGiftBSFragment.this, true);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showProgress(false);
                        getRootActivity().showShortToast(errMsg);

                    }
                }, cgrUrl, null);
            }
        } else {
            showProgress(false);
        }

    }

    private void showCGDialog(String message) {
        if (!isAdded()) return;
        /*customDialogFragment = CustomDialogFragment.
                newInstance(null, message, null, getString(R.string.cg_dialog_btn_ok), new ICustomDialogBtnHandler() {
                    @Override
                    public void doOkConfirmClick() {
                        customDialogFragment.dismiss();
                    }

                    @Override
                    public void doCancelConfirmClick() {
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
}
