package com.onmobile.rbt.baseline.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.onmobile.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.PurchaseConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.SingleButtonInfoDialog;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.model.PlanBean;
import com.onmobile.rbt.baseline.util.ContactDetailProvider;
import com.onmobile.rbt.baseline.util.IContactDetailProvider;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.widget.PlanView;
import com.onmobile.rbt.baseline.widget.PlanViewLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import static com.onmobile.rbt.baseline.util.AppConstant.OfflineCGExtras.EXTRA_OFFLINE_CONSENT_CALLBACK;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_ERROR;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CG_RURL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_CONSCENT_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.OnlineCGExtras.EXTRA_R_URL;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_CONSENT_ACTIVITY;
import static com.onmobile.rbt.baseline.util.AppConstant.PreBuyScreenActivityResultExtras.RESULT_OFFLINE_CONSENT_ACTIVITY;

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    //Active Plan
    private RelativeLayout mActivePlanLayout;
    private TextView mActivePlanTitle;
    private TextView mActivePlanPrice;
    private TextView mActivePlanValidity;
    private TextView mActivePlanNextSubscription;
    private TextView mActivePlanLastSubscription;
    private TextView mActivePlanSubscriptionPrice;
    private TextView mActivePlanTuneSelectionPrice;

    //Gift not accepted
    private RelativeLayout mGiftNotAcceptedLayout;
    private TextView mGiftNotAcceptedDescription;
    private TextView mGiftActivateBtn;


    //Gift accepted
    private RelativeLayout mGiftAcceptedLayout;
    private AppCompatImageView mGifterContactImage;
    private TextView mGifterName;
    private TextView mGifterNumber;

    private PlanViewLayout mPlanViewLayout;
    private LinearLayout mCurrentPlanLayout;
    private TextView mActivateText;

    private UserSubscriptionDTO userSubscriptionDTO;
    private TextView mPlanLayoutTitle;
    private ProgressDialog mProgressDialog;
    private PricingSubscriptionDTO mPricingSubscriptionDTO;
    private boolean isGiftActivate = false;
    private ContactModelDTO mContactModelDTO;

    //Child Active Plan
    private RelativeLayout mChildActivePlanLayout;
    private TextView mChildActivePlanTitle;
    private TextView mChildActivePlanPrice;
    private TextView mChildActivePlanValidity;
    private TextView mChildActivePlanNextSubscription;
    private TextView mChildActivePlanLastSubscription;
    private TextView mChildActivePlanSubscriptionPrice;
    private TextView mChildActivePlanTuneSelectionPrice;

    private String mNetworkType;

    @NonNull
    @Override
    protected String initTag() {
        return MyAccountActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {
        mActivePlanLayout = findViewById(R.id.active_plan_layout);
        mActivePlanTitle = findViewById(R.id.tv_plan_title);
        mActivePlanPrice = findViewById(R.id.tv_plan_price);
        mActivePlanValidity = findViewById(R.id.tv_plan_validity);
        mActivePlanNextSubscription = findViewById(R.id.tv_plan_next_subscription);
        mActivePlanLastSubscription = findViewById(R.id.tv_plan_last_subscription);
        mActivePlanSubscriptionPrice = findViewById(R.id.tv_subscription_price);
        mActivePlanTuneSelectionPrice = findViewById(R.id.tv_tune_selection_price);

        mPlanViewLayout = findViewById(R.id.layout_plan);
        mPlanViewLayout.setMyAccount(true);
        mPlanViewLayout.addOnPlanSelectedListener(mPlanSelectedListener);
        mCurrentPlanLayout = findViewById(R.id.current_plan_layout);
        mActivateText = findViewById(R.id.tv_recommended_activate);
        mActivateText.setOnClickListener(this);

        mGiftNotAcceptedLayout = findViewById(R.id.child_plan_not_activated_layout);
        mGiftNotAcceptedDescription = findViewById(R.id.child_plan_not_activated_description);
        mGiftActivateBtn = findViewById(R.id.tv_activate);
        mGiftActivateBtn.setOnClickListener(this);

        mGiftAcceptedLayout = findViewById(R.id.child_plan_activated_layout);
        mGifterContactImage = findViewById(R.id.contact_pic);
        mGifterName = findViewById(R.id.contact_name);
        mGifterNumber = findViewById(R.id.contact_num);

        mPlanLayoutTitle = findViewById(R.id.layout_plan_title);

        mChildActivePlanLayout = findViewById(R.id.child_active_plan_layout);
        mChildActivePlanTitle = findViewById(R.id.tv_child_plan_title);
        mChildActivePlanPrice = findViewById(R.id.tv_child_plan_price);
        mChildActivePlanValidity = findViewById(R.id.tv_child_plan_validity);
        mChildActivePlanNextSubscription = findViewById(R.id.tv_child_plan_next_subscription);
        mChildActivePlanLastSubscription = findViewById(R.id.tv_child_plan_last_subscription);
        mChildActivePlanSubscriptionPrice = findViewById(R.id.tv_child_subscription_price);
        mChildActivePlanTuneSelectionPrice = findViewById(R.id.tv_child_tune_selection);
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);

        setToolbarTitle(getString(R.string.my_account_title));
    }

    @Override
    protected void bindViews() {
        userSubscriptionDTO = BaselineApplication.getApplication().getRbtConnector().getCacheUserSubscription();

        BaselineApplication.getApplication().getRbtConnector().isFamilyAndFriends(new IAppFriendsAndFamily() {
            @Override
            public void isParent(boolean exist) {
                if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                    loadActivePlan();
                }
                populatePlans();
            }

            @Override
            public void isChild(boolean exist) {
                GetChildInfoResponseDTO getChildInfoResponseDTO = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo();
                if (getChildInfoResponseDTO != null) {
                    ContactDetailProvider contactDetailProvider = new ContactDetailProvider(getApplicationContext(), getChildInfoResponseDTO.getParentId(), new IContactDetailProvider() {
                        @Override
                        public void contactRecieved(ContactModelDTO contactModelDTO) {
                            mContactModelDTO = contactModelDTO;
                            if (contactModelDTO.getName() == null) {
                                contactModelDTO.setName(contactModelDTO.getMobileNumber());
                            }
                            if (getChildInfoResponseDTO.getStatus().equals("pending")) {
                                loadGiftNotAccepted(contactModelDTO);
                            } else {
                                loadGiftAccepted(contactModelDTO);
                            }

                            populatePlans();
                        }
                    });
                    contactDetailProvider.execute();
                } else {
                    populatePlans();
                }
            }

            @Override
            public void isNone(boolean exist) {
                if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                    loadActivePlan();
                }
                populatePlans();
            }
        });

    }

    public void loadActivePlan() {
        PlanBean bean = new PlanBean.Builder().build(getActivityContext(),
                userSubscriptionDTO);
        String currentPlanString = bean.finalText;
        currentPlanString = currentPlanString.replaceAll("\n", " / ");
        mActivePlanPrice.setText(currentPlanString);
        mActivePlanTitle.setText(userSubscriptionDTO.getCatalog_subscription().getName());
        mActivePlanPrice.setVisibility(View.GONE);

        String periodText = null;
        if (userSubscriptionDTO.getCatalog_subscription().getPeriod() != null) {
            periodText = userSubscriptionDTO.getCatalog_subscription().getPeriod().getLength() + " " + Util.getPeriodUnit(userSubscriptionDTO.getCatalog_subscription().getPeriod().getUnit());
        }
        mActivePlanValidity.setText(periodText);

        mActivePlanNextSubscription.setText(convertDateForMyAccount(userSubscriptionDTO.getEnd_date()));
        mActivePlanLastSubscription.setText(convertDateForMyAccount(userSubscriptionDTO.getStart_date()));
        mActivePlanSubscriptionPrice.setText(bean.finalSubscriptionPrice);
        mActivePlanTuneSelectionPrice.setText(bean.finalTuneSelection);
        mActivePlanLayout.setVisibility(View.VISIBLE);
        mCurrentPlanLayout.setVisibility(View.VISIBLE);
    }

    public void loadChildActivePlan() {
        PlanBean bean = new PlanBean.Builder().build(getActivityContext(),
                userSubscriptionDTO);
        String currentPlanString = bean.finalText;
        currentPlanString = currentPlanString.replaceAll("\n", " / ");
        mChildActivePlanPrice.setText(currentPlanString);
        mChildActivePlanTitle.setText(userSubscriptionDTO.getCatalog_subscription().getName());
        mChildActivePlanPrice.setVisibility(View.GONE);
        String periodText = null;
        if (userSubscriptionDTO.getCatalog_subscription().getPeriod() != null) {
            periodText = userSubscriptionDTO.getCatalog_subscription().getPeriod().getLength() + " " + Util.getPeriodUnit(userSubscriptionDTO.getCatalog_subscription().getPeriod().getUnit());
        }
        mChildActivePlanValidity.setText(periodText);

        mChildActivePlanNextSubscription.setText(convertDateForMyAccount(userSubscriptionDTO.getEnd_date()));
        mChildActivePlanLastSubscription.setText(convertDateForMyAccount(userSubscriptionDTO.getStart_date()));
        mChildActivePlanSubscriptionPrice.setText(bean.finalSubscriptionPrice);
        mChildActivePlanTuneSelectionPrice.setText(bean.finalTuneSelection);
        mChildActivePlanLayout.setVisibility(View.VISIBLE);
        mCurrentPlanLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private String convertDateForMyAccount(String date) {
        try {
            DateFormat myAccountFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date targetDate = oldFormat.parse(date);
            String myAccountFormattedString = myAccountFormat.format(targetDate);
            return myAccountFormattedString;
        } catch (Exception e) {
            return null;
        }
    }

    private void populatePlans() {
        mPlanViewLayout.loading();
        BaselineApplication.getApplication().getRbtConnector().checkUser(new AppBaselineCallback<Boolean>() {
            @Override
            public void success(Boolean result) {
                fetchPlans();
            }

            @Override
            public void failure(String message) {
                mPlanLayoutTitle.setVisibility(View.INVISIBLE);
                mPlanViewLayout.setVisibility(View.GONE);
            }
        });
    }

    private void fetchPlans() {
        mPlanLayoutTitle.setVisibility(View.INVISIBLE);
        BaselineApplication.getApplication().getRbtConnector().getListOfPlans(new AppBaselineCallback<List<PricingSubscriptionDTO>>() {
            @Override
            public void success(List<PricingSubscriptionDTO> result) {
                if (result != null && result.size() > 0) {
                    for (PricingSubscriptionDTO priceDTO : result) {
                        if (priceDTO == null || TextUtils.isEmpty(priceDTO.getCatalog_subscription_id()))
                            continue;
                        if (userSubscriptionDTO == null
                                || userSubscriptionDTO.getCatalog_subscription_id() == null
                                || !priceDTO.getCatalog_subscription_id().equals(userSubscriptionDTO.getCatalog_subscription_id()) ||
                                !BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                            PlanView plan = new PlanView(getActivityContext());
                            if (priceDTO != null && priceDTO.getRetail_priceObject() != null &&
                                    priceDTO.getRetail_priceObject().getAmount() != null) {
                                mPlanViewLayout.addPlan(plan, priceDTO);
                            }
                            if (mPlanViewLayout.getPlanCount() == 1)
                                plan.setChecked(true);
                        }
                    }
                    mPlanLayoutTitle.setVisibility(View.VISIBLE);
                    mActivateText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(String errMsg) {
                mPlanLayoutTitle.setVisibility(View.INVISIBLE);
                mPlanViewLayout.setVisibility(View.GONE);
                //mPlanViewLayout.error(errMsg);
            }
        });
    }

    private PlanViewLayout.PlanSelectedListener mPlanSelectedListener = this::planSelected;

    private void planSelected(PlanView plan) {
        if (plan == null)
            return;
        if (plan.isChecked()) {
            //mTvSetTune.setEnabled(true);
            if (!plan.isUserSubscription()) {
                mPlanViewLayout.setFooterText(plan.getPriceDTO().getDescription());
                //mTvSetTune.setEnabled(true);
            } else {
                mPlanViewLayout.setFooterText(plan.getUserSubscriptionDTO().getCatalog_subscription().getDescription());
                //mTvSetTune.setEnabled(false);
            }
        }
    }

    private void loadGiftNotAccepted(ContactModelDTO contactModelDTO) {
        String mGiftNotAcceptedDescText = String.format(getString(R.string.gift_not_accepted_description), contactModelDTO.getName());
        mGiftNotAcceptedDescription.setText(mGiftNotAcceptedDescText);
        mGiftNotAcceptedLayout.setVisibility(View.VISIBLE);
        mCurrentPlanLayout.setVisibility(View.VISIBLE);
    }

    private void loadGiftAccepted(ContactModelDTO contactModelDTO) {
        Glide.with(getActivityContext())
                .load(contactModelDTO.getPhotoURI())
                .transform(new RoundTransform(getActivityContext()))
                .placeholder(R.drawable.ic_contct_selectn_icon)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_contct_selectn_icon)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mGifterContactImage);

        mGifterName.setText(contactModelDTO.getName());
        mGifterNumber.setText(contactModelDTO.getMobileNumber());
        mGiftAcceptedLayout.setVisibility(View.VISIBLE);
        mCurrentPlanLayout.setVisibility(View.VISIBLE);

        loadChildActivePlan();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_activate) {
            activateChild();
        }else if(view.getId() == R.id.tv_recommended_activate) {
            activateRecommendedPlan();
        }
    }

    public static class RoundTransform extends BitmapTransformation {
        RoundTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    private void activateChild() {
        isGiftActivate = true;
        showProgress(true);

        String confirmationPopUpDescription = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo().getCatalogSubscription().getDescription();

        BaselineApplication.getApplication().getRbtConnector().getAppUtilityNetworkRequest(new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                mNetworkType = result.getNetworkType();

                BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);

                GetChildInfoResponseDTO dummyChildInfo = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo();
                BaselineApplication.getApplication().getRbtConnector().dummyChildPurchaseSubscription(dummyChildInfo.getCatalogSubscription().getId(), dummyChildInfo.getParentId());

                APIRequestParameters.ConfirmationType confirmationType = null;
                if (mNetworkType.equalsIgnoreCase("opt_network")) {
                    confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationOptNetwork();
                } else {
                    confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationNonOptNetwork();
                }

                if (!isShowConsentPopUp(confirmationType, false)) {
                    createChild();
                } else {
                    showProgress(false);
                    AppDialog.PurchaseConfirmDialogFragment(getActivityContext(), confirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                        @Override
                        public void onContinue() {
                            showProgress(true);
                            BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);
                            createChild();
                        }

                        @Override
                        public void onCancel() {
                            showProgress(false);
                        }
                    });
                }
            }

            @Override
            public void failure(String message) {
                showProgress(false);
                showShortSnackBar(message);
            }
        });

    }

    private void createChild() {
        GetChildInfoResponseDTO childInfo = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo();
        BaselineApplication.getApplication().getRbtConnector().createChildUserSubscription(childInfo.getCatalogSubscription().getId(), childInfo.getParentId(), new AppBaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO result) {
                if (result != null) {
                    PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = result.getThirdpartyconsent();
                    Intent intent = new Intent();
                    if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
                        NonNetworkCGDTO nonNetworkCG = BaselineApplication.getApplication().getRbtConnector().getNonNetworkCG();
                        BaselineApplication.getApplication().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                            @Override
                            public void success(RUrlResponseDto result) {
                                showProgress(false);
                                if (nonNetworkCG != null) {
                                    showCGDialog(nonNetworkCG.getMessageIOS());
                                }
                            }

                            @Override
                            public void failure(String errMsg) {
                                showProgress(false);
                                showCGDialog(errMsg);
                            }
                        }, thirdPartyConsentDTO.getReturn_url(), APIRequestParameters.CG_REQUEST.NO);


                    } else if (thirdPartyConsentDTO != null) {
                        intent.setClass(getActivityContext(), CGWebViewActivity.class);
                        intent.putExtra(EXTRA_CONSCENT_URL, thirdPartyConsentDTO.getThird_party_url());
                        intent.putExtra(EXTRA_R_URL, thirdPartyConsentDTO.getReturn_url());
                        startActivityForResult(intent, RESULT_CONSENT_ACTIVITY);
                    } else {
                        showProgress(false);
                        showAcceptChildSuccess(mContactModelDTO);
                        finish();
                    }

                }

            }

            @Override
            public void failure(String message) {
                showProgress(false);
                showShortSnackBar(message);
            }
        }, null);
    }

    public void showProgress(boolean showProgress) {
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(this);
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
                showProgress(false);
                String status = (String) data.getSerializableExtra(EXTRA_OFFLINE_CONSENT_CALLBACK);
                if (status != null && status.equalsIgnoreCase("success")) {
                    if (isGiftActivate) {
                        showAcceptChildSuccess(mContactModelDTO);
                    } else {
                        showShortSnackBar(getString(R.string.plan_activated_successful));
                    }
                    finish();
                } else {
                    finish();
                }
            } else if (requestCode == RESULT_CONSENT_ACTIVITY) {
                if (data.hasExtra(EXTRA_CG_ERROR)) {
                    if (EXTRA_CG_ERROR.equalsIgnoreCase(data.getStringExtra(EXTRA_CG_ERROR))) {
                        return;
                    }
                }
                String cgrUrl = data.getStringExtra(EXTRA_CG_RURL);
                BaselineApplication.getApplication().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                    @Override
                    public void success(RUrlResponseDto result) {
                        showProgress(false);
                        showShortSnackBar(getString(R.string.plan_activated_successful));
                        finish();
                    }

                    @Override
                    public void failure(String errMsg) {
                        showProgress(false);
                        showShortToast(errMsg);

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

    private void activateRecommendedPlan() {
        isGiftActivate = false;
        showProgress(true);

        mPricingSubscriptionDTO = mPlanViewLayout.getSelectedPlan().getPriceDTO();
        if (mPricingSubscriptionDTO == null) {
            return;
        }

        BaselineApplication.getApplication().getRbtConnector().getAppUtilityNetworkRequest(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_MY_ACCOUNT, AnalyticsConstants.EVENT_PV_PURCHASE_PLAN_TYPE_USER_MIGRATE, null, mPricingSubscriptionDTO, null, null, new AppBaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                mNetworkType = result.getNetworkType();
                BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(result);

                BaselineApplication.getApplication().getRbtConnector().dummyPurchaseSubscriptionchangePlan(mPricingSubscriptionDTO);

                //Commented by PK_PK
//                boolean isAllowed = BaselineApplication.getApplication().getRbtConnector().isThirdPartyPaymentRequired("", mPricingSubscriptionDTO, null);
//                if (AppConfigurationValues.isPayTMOptionEnabled() && isAllowed) {
//                    getPaymentApiPayTM();
//                } else {
//                    changePlanOperator(null);
//                }

                changePlanOperator(null);
            }

            @Override
            public void failure(String message) {
                showProgress(false);
                showShortSnackBar(message);
            }
        });

    }

    public void changePlan(Map<String, String> extraInfoMap) {
        mPricingSubscriptionDTO = mPlanViewLayout.getSelectedPlan().getPriceDTO();
        if (mPricingSubscriptionDTO == null) {
            return;
        }

        BaselineApplication.getApplication().getRbtConnector().changePlan(mPricingSubscriptionDTO, extraInfoMap, new AppBaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO userSubscriptionDTO) {
                if (userSubscriptionDTO != null) {
                    PurchaseComboResponseDTO.Thirdpartyconsent thirdPartyConsentDTO = userSubscriptionDTO.getThirdpartyconsent();
                    Intent intent = new Intent();
                    if (thirdPartyConsentDTO != null && (thirdPartyConsentDTO.getThird_party_url() == null || thirdPartyConsentDTO.getThird_party_url().isEmpty())) {
                        NonNetworkCGDTO nonNetworkCG = BaselineApplication.getApplication().getRbtConnector().getNonNetworkCG();
                        BaselineApplication.getApplication().getRbtConnector().getRurlResponse(new AppBaselineCallback<RUrlResponseDto>() {
                            @Override
                            public void success(RUrlResponseDto result) {
                                showProgress(false);
                                if (nonNetworkCG != null) {
                                    showCGDialog(nonNetworkCG.getMessageIOS());
                                }
                            }

                            @Override
                            public void failure(String errMsg) {
                                showProgress(false);
                                showCGDialog(errMsg);
                            }
                        }, thirdPartyConsentDTO.getReturn_url(), APIRequestParameters.CG_REQUEST.NO);


                    } else if (thirdPartyConsentDTO != null) {
                        intent.setClass(getActivityContext(), CGWebViewActivity.class);
                        intent.putExtra(EXTRA_CONSCENT_URL, thirdPartyConsentDTO.getThird_party_url());
                        intent.putExtra(EXTRA_R_URL, thirdPartyConsentDTO.getReturn_url());
                        startActivityForResult(intent, RESULT_CONSENT_ACTIVITY);
                    } else {
                        showProgress(false);
                        showShortSnackBar(getString(R.string.plan_activated_successful));
                        finish();
                    }

                }
            }

            @Override
            public void failure(String errMsg) {
                showProgress(false);
                showShortToast(errMsg);
            }
        });
    }

    private void showAcceptChildSuccess(ContactModelDTO contactModelDTO) {
        BaselineApplication.getApplication().getRbtConnector().getChildInfo(new AppBaselineCallback<GetChildInfoResponseDTO>() {
            @Override
            public void success(GetChildInfoResponseDTO result) {

            }

            @Override
            public void failure(String message) {

            }
        });
        WidgetUtils.getAcceptGiftSuccessBottomSheet(contactModelDTO).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                finish();
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                finish();
            }
        }).showSheet(getSupportFragmentManager());
    }

    private void showCGDialog(String message) {
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
        customDialogFragment.show(getSupportFragmentManager(), AppConstant.DialogConstants.DIALOG_FRAGMENT_NAME);*/
        AppDialog.getOkAlertDialog(getActivityContext(), null, message, getString(R.string.cg_dialog_btn_ok),
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

    private void setPayTMErrorFlow(String message, Map<String, String> extraInfoMap) {
        mPricingSubscriptionDTO = mPlanViewLayout.getSelectedPlan().getPriceDTO();
        if (mPricingSubscriptionDTO != null) {
            message = message + mPricingSubscriptionDTO.getDescription();
        }

        AppDialog.PurchaseConfirmDialogFragment(MyAccountActivity.this, message, new PurchaseConfirmDialog.ActionCallBack() {
            @Override
            public void onContinue() {
                changePlan(extraInfoMap);
            }

            @Override
            public void onCancel() {
                showProgress(false);
            }
        });
    }

    public void showSingleButtonInfoDialogFragment(String message) {
        AppDialog.SingleButtonInfoDialogFragment(MyAccountActivity.this, message, new SingleButtonInfoDialog.ActionCallBack() {
            @Override
            public void Ok() {
                showProgress(false);
            }
        });
    }

    private void changePlanOperator(Map<String, String> extraInfoMap) {
        mPricingSubscriptionDTO = mPlanViewLayout.getSelectedPlan().getPriceDTO();
        if (mPricingSubscriptionDTO == null) {
            return;
        }

        String confirmationPopUpDescription = mPricingSubscriptionDTO.getDescription();

        AppUtilityDTO appUtilityDTO = BaselineApplication.getApplication().getRbtConnector().getAppUtilityDTO();
        mNetworkType = appUtilityDTO.getNetworkType();

        APIRequestParameters.ConfirmationType confirmationType = null;
        if (mNetworkType.equalsIgnoreCase("opt_network")) {
            confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationOptNetwork();
        } else {
            confirmationType = BaselineApplication.getApplication().getRbtConnector().getConfirmationNonOptNetwork();
        }

        if (!isShowConsentPopUp(confirmationType, false)) {
            changePlan(extraInfoMap);
        } else {
            showProgress(false);
            AppDialog.PurchaseConfirmDialogFragment(getActivityContext(), confirmationPopUpDescription, new PurchaseConfirmDialog.ActionCallBack() {
                @Override
                public void onContinue() {
                    showProgress(true);
                    BaselineApplication.getApplication().getRbtConnector().setAppUtilityDTO(appUtilityDTO);
                    changePlan(extraInfoMap);
                }

                @Override
                public void onCancel() {
                    showProgress(false);
                }
            });
        }
    }
}
