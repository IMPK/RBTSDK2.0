package com.onmobile.rbt.baseline.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onmobile.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.ShareAppDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.baseline.http.cache.LocalCacheManager;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.AboutUsActivity;
import com.onmobile.rbt.baseline.activities.AppLanguageActivity;
import com.onmobile.rbt.baseline.activities.FeedbackActivity;
import com.onmobile.rbt.baseline.activities.MusicLanguageActivity;
import com.onmobile.rbt.baseline.activities.MyAccountActivity;
import com.onmobile.rbt.baseline.activities.PermissionActivity;
import com.onmobile.rbt.baseline.activities.SplashActivity;
import com.onmobile.rbt.baseline.activities.VideoActivity;
import com.onmobile.rbt.baseline.activities.WebViewActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.exception.IllegalFragmentBindingException;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.PlanBean;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.CustomTabService;
import com.onmobile.rbt.baseline.util.PermissionUtil;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.widget.LabeledView;
import com.onmobile.rbt.baseline.widget.chip.Chip;

import java.util.Map;
import java.util.SortedMap;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentHomeProfile extends BaseFragment implements View.OnClickListener {

    private final boolean IS_PROFILE_PIC_AVAILABLE = false;
    private AppCompatImageView mIvProfilePicture;
    private CardView mCardProfilePictureRoot, mCardProfilePictureSelector;
    private LabeledView mLabeledNotification, mLabeledPlan, mLabeledMyAccount, mLabeledGiftRbt, mLabeledChangeNumber, mLabeledChangeLanguage, mLabeledAppTour,
            mLabeledHelpFaq, mLabeledAboutApp, mLabeledContactSync, mLabeledPermission, mLabeledTermCondition, mLabeledFeedback, mLabeledDigitalStar,
            mLabeledShareApp, mLabeledPersonalizedShuffle,mLabeledChangeAppLanguage;
    private AppCompatTextView mTvLogout;
    private TextView mChildCountText;

    private InternalCallback mActivityCallback;

    private CustomTabService mCustomTabService;


    private LabeledView.OnLabeledListener mLabeledListener = new LabeledView.OnLabeledListener() {
        @Override
        public void onClick(LabeledView view) {
            int id = view.getId();
            if (id == mLabeledMyAccount.getId()) {
                //getRootActivity().redirect(MyAccountActivity.class, null, false, false);
                Intent myAccountIntent = new Intent(getRootActivity(), MyAccountActivity.class);
                myAccountIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(myAccountIntent, AppConstant.ACTIVITY_RESULT_MY_ACCOUNT);
            } else if (id == mLabeledChangeNumber.getId()) {
                changeNumber();
            } else if (id == mLabeledAppTour.getId()) {
                Intent appTourIntent = new Intent(getRootActivity(), VideoActivity.class);
                appTourIntent.putExtra(AppConstant.KEY_CALLED_SOURCE, true);
                startActivity(appTourIntent);
            } else if (id == mLabeledHelpFaq.getId()) {
                Intent intent = new Intent(getRootActivity(), WebViewActivity.class);
                intent.putExtra(AppConstant.WebViewConstant.DRAWER_HEADING, getResources().getString(R.string.label_help_faq));
                intent.putExtra(AppConstant.WebViewConstant.LOAD, AppConstant.WebViewType.FAQ);
                startActivity(intent);
                //mCustomTabService.launchUrl("http://onmobile.com");
            } else if (id == mLabeledTermCondition.getId()) {
                Intent intent = new Intent(getRootActivity(), WebViewActivity.class);
                intent.putExtra(AppConstant.WebViewConstant.DRAWER_HEADING, getResources().getString(R.string.label_terms_conditions));
                intent.putExtra(AppConstant.WebViewConstant.LOAD, AppConstant.WebViewType.TNC);
                startActivity(intent);
            } else if (id == mLabeledFeedback.getId()) {
                Intent intent = new Intent(getRootActivity(), FeedbackActivity.class);
                startActivity(intent);
            } else if (id == mLabeledAboutApp.getId()) {
                Intent intent = new Intent(getRootActivity(), AboutUsActivity.class);
                startActivity(intent);
            } else if (id == mLabeledPermission.getId()) {
                Intent intent = new Intent(getRootActivity(), PermissionActivity.class);
                startActivity(intent);
            } else if (id == mLabeledChangeLanguage.getId()) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_CONTENT_LANG_SELECTED_PROFILE);
                getRootActivity().redirect(MusicLanguageActivity.class, bundle, false, false);
            } else if (id==mLabeledChangeAppLanguage.getId()){

                getRootActivity().redirect(AppLanguageActivity.class, null, false, false);
            }
        }

        @Override
        public void onSwitch(LabeledView view, boolean checked) {
            if (view.getId() == mLabeledNotification.getId()) {
                SharedPrefProvider.getInstance(getFragmentContext()).setAppNotificationStatus(checked);
            } else if (view.getId() == mLabeledPersonalizedShuffle.getId()) {
                if (checked) {
                    mLabeledPersonalizedShuffle.disableSwitchStatusSilently();
                } else {
                    mLabeledPersonalizedShuffle.enableSwitchStatusSilently();
                }
                updatePersonalizedShuffle(checked);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityCallback = (InternalCallback) context;
        } catch (ClassCastException e) {
            throw new IllegalFragmentBindingException("Must implement AdapterInternalCallback");
        }
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentHomeProfile.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_profile;
    }
    @Override
    protected void unbindExtras(Bundle bundle) {

    }

    @Override
    protected void initComponents() {
        mCustomTabService = new CustomTabService(getRootActivity(), R.color.colorAccent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCustomTabService != null) mCustomTabService.unBind();
    }

    @Override
    protected void initViews(View view) {
        mCardProfilePictureRoot = view.findViewById(R.id.card_profile_picture);
        mCardProfilePictureSelector = view.findViewById(R.id.card_avatar_selector);
        mIvProfilePicture = view.findViewById(R.id.iv_profile_picture);
        mLabeledNotification = view.findViewById(R.id.labeled_switch_notification);
        mLabeledContactSync = view.findViewById(R.id.labeled_switch_contact_sync);
        mLabeledDigitalStar = view.findViewById(R.id.labeled_digital_star);
        mLabeledPlan = view.findViewById(R.id.labeled_change_plan);
        mLabeledMyAccount = view.findViewById(R.id.labeled_my_account);
        mLabeledGiftRbt = view.findViewById(R.id.labeled_gift_rbt);
        mLabeledChangeNumber = view.findViewById(R.id.labeled_change_number);
        mLabeledChangeLanguage = view.findViewById(R.id.labeled_change_language);
        mLabeledAppTour = view.findViewById(R.id.labeled_app_tour);
        mLabeledHelpFaq = view.findViewById(R.id.labeled_help_faq);
        mLabeledAboutApp = view.findViewById(R.id.labeled_about_app);
        mLabeledPermission = view.findViewById(R.id.labeled_permissions);
        mLabeledTermCondition = view.findViewById(R.id.labeled_terms_condition);
        mLabeledFeedback = view.findViewById(R.id.labeled_feedback);
        mLabeledShareApp = view.findViewById(R.id.labeled_share_app);
        mLabeledPersonalizedShuffle = view.findViewById(R.id.labeled_personalized_shuffle);
        mLabeledChangeAppLanguage=view.findViewById(R.id.labeled_change_app_language);

        Chip chip = view.findViewById(R.id.chip_add_more_contact_root_set_action);
//        chip.setOnChipClickListener(new OnChipClickListener() {
//            @Override
//            public void onChipClick(View v) {
//                getRootActivity().redirect(GiftRbtActivity.class, null, false, false);
//            }
//        });
        mChildCountText = view.findViewById(R.id.tv_add_more_contact_root_set_action);
        mTvLogout = view.findViewById(R.id.tv_logout);

        mLabeledNotification.setListener(mLabeledListener);


        if (AppConfigurationValues.isShowMyAccount()) {
            mLabeledMyAccount.setListener(mLabeledListener);
            mLabeledMyAccount.setVisibility(View.VISIBLE);
        } else {
            mLabeledMyAccount.setVisibility(View.GONE);
        }

        if (AppConfigurationValues.isContactSyncEnabled()) {
            mLabeledContactSync.setListener(mLabeledListener);
            mLabeledContactSync.setVisibility(View.VISIBLE);
        } else {
            mLabeledContactSync.setVisibility(View.GONE);
        }

        if (AppConfigurationValues.isShowVideoTutorial()) {
            mLabeledAppTour.setListener(mLabeledListener);
            mLabeledAppTour.setVisibility(View.VISIBLE);
        } else {
            mLabeledAppTour.setVisibility(View.GONE);
        }


        if (AppConfigurationValues.isDigitalStarToCopyEnabled()) {
            mLabeledDigitalStar.setListener(mLabeledListener);
            mLabeledDigitalStar.setVisibility(View.VISIBLE);
        } else {
            mLabeledDigitalStar.setVisibility(View.GONE);
        }

        Map<String, String> languageMap = BaselineApplication.getApplication().getRbtConnector().getLanguageToDisplay();
        if (languageMap != null && languageMap.size() > 1) {
            mLabeledChangeLanguage.setListener(mLabeledListener);
            mLabeledChangeLanguage.setVisibility(View.VISIBLE);
        } else {
            mLabeledChangeLanguage.setVisibility(View.GONE);
        }

        SortedMap<Integer, String> appLanguageMap = BaselineApplication.getApplication().getRbtConnector().getAppLocale();

        if (appLanguageMap!=null && appLanguageMap.size()>1){

            mLabeledChangeAppLanguage.setListener(mLabeledListener);
            mLabeledChangeAppLanguage.setVisibility(View.VISIBLE);

        }else {
            mLabeledChangeAppLanguage.setVisibility(View.GONE);
        }


        mLabeledPlan.setListener(mLabeledListener);
        mLabeledGiftRbt.setListener(mLabeledListener);
        mLabeledChangeNumber.setListener(mLabeledListener);

        mLabeledHelpFaq.setListener(mLabeledListener);
        mLabeledAboutApp.setListener(mLabeledListener);
        mLabeledPermission.setListener(mLabeledListener);
        mLabeledTermCondition.setListener(mLabeledListener);
        mLabeledFeedback.setListener(mLabeledListener);
        mLabeledShareApp.setListener(mLabeledListener);
        mLabeledPersonalizedShuffle.setListener(mLabeledListener);
        mTvLogout.setOnClickListener(this);

    }

    @Override
    protected void bindViews(View view) {
        mCustomTabService.bind();
        initProfileImage();
        mLabeledChangeNumber.setValue(SharedPrefProvider.getInstance(getRootActivity()).getMsisdn());

        if (AppConfigurationValues.isContactSyncEnabled()) {
            boolean isEnabled = SharedPrefProvider.getInstance(getRootActivity()).isAppContactSyncEnabled();
            if (isEnabled && PermissionUtil.hasPermission(getFragmentContext(), PermissionUtil.Permission.CONTACTS)) {
                mLabeledContactSync.enableSwitchStatusSilently();
            } else {
                mLabeledContactSync.disableSwitchStatusSilently();
            }
        }
        try {
            PackageInfo packageInfo = getRootActivity().getPackageManager().getPackageInfo(getRootActivity().getPackageName(), 0);
            String versionName = packageInfo.versionName;
            mLabeledAboutApp.setValue(versionName);
        } catch (Exception e) {
            mLabeledAboutApp.setValue("");
        }
    }

    private void initProfileImage() {
        if (IS_PROFILE_PIC_AVAILABLE) {
            mCardProfilePictureSelector.setVisibility(View.GONE);
            mIvProfilePicture.setVisibility(View.VISIBLE);
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
            //TODO Need to change dummy data
            final String imageUrl = AppUtils.getFitableImage(getFragmentContext(), "https://avante.biz/wp-content/uploads/Android-Wallpapers-3D/Android-Wallpapers-3D-001.jpg", imageSize);
            Glide.with(getFragmentContext()).load(imageUrl).asBitmap().centerCrop().error(R.drawable.default_album_art_circle)
                    .placeholder(R.drawable.default_album_art_circle).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(mIvProfilePicture) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    WidgetUtils.setCircularImage(mIvProfilePicture, resource, getResources().getDimension(R.dimen.profile_pic_radius));
                }
            });
        } else {
            mCardProfilePictureSelector.setVisibility(View.VISIBLE);
            mIvProfilePicture.setVisibility(View.GONE);
        }
    }

    private void changePlan() {
        UserSubscriptionDTO userSubscriptionDTO = BaselineApplication.getApplication().getRbtConnector().getCacheUserSubscription();
        if (userSubscriptionDTO == null) {
            return;
        }
        WidgetUtils.getChangePlanBottomSheet(userSubscriptionDTO).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                if (success) {
                    getRootActivity().showShortSnackBar(getString(R.string.change_plan_successful));
                }
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
            }
        }).showSheet(getChildFragmentManager());
    }

    private void changeNumber() {
        WidgetUtils.getChangeNumberBottomSheet().setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                if (success)
                    getRootActivity().redirect(SplashActivity.class, null, true, true);
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                if (success)
                    getRootActivity().redirect(SplashActivity.class, null, true, true);
            }
        }).showSheet(getChildFragmentManager());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                if (BaselineApplication.getApplication().getRbtConnector().isUserUDPEnabled()) {
                    mLabeledPersonalizedShuffle.enableSwitchStatusSilently();
                } else {
                    mLabeledPersonalizedShuffle.disableSwitchStatusSilently();
                }
                mLabeledPersonalizedShuffle.setVisibility(View.VISIBLE);
            } else {
                mLabeledPersonalizedShuffle.setVisibility(View.GONE);
            }

            BaselineApplication.getApplication().getRbtConnector().isFamilyAndFriends(new IAppFriendsAndFamily() {
                @Override
                public void isParent(boolean exist) {
                    refreshMyAccount();
                }

                @Override
                public void isChild(boolean exist) {

                }

                @Override
                public void isNone(boolean exist) {
                    refreshMyAccount();
                }
            });

            if (AppConfigurationValues.isContactSyncEnabled()) {
                boolean isEnabled = SharedPrefProvider.getInstance(getRootActivity()).isAppContactSyncEnabled();
                if (isEnabled && PermissionUtil.hasPermission(getFragmentContext(), PermissionUtil.Permission.CONTACTS)) {
                    if (mLabeledContactSync != null)
                        mLabeledContactSync.enableSwitchStatusSilently();
                } else {
                    if (mLabeledContactSync != null)
                        mLabeledContactSync.disableSwitchStatusSilently();
                }
            }
            refreshGift();
        }
    }

    public void logout() {
        LocalCacheManager.getInstance().setUserMsisdn(null);
        UserSettingsCacheManager.clearTempCache();

        SharedPrefProvider.getInstance(getRootActivity()).clear();

        Intent intent = new Intent(getRootActivity(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getRootActivity().finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_logout) {
            logout();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.ACTIVITY_RESULT_MY_ACCOUNT) {
            setUserVisibleHint(true);
        }
    }

    private void refreshGift() {
        BaselineApplication.getApplication().getRbtConnector().isFamilyAndFriends(new IAppFriendsAndFamily() {
            @Override
            public void isParent(boolean exist) {
                if (BaselineApplication.getApplication().getRbtConnector().getCacheParentInfo() != null) {
                    GetParentInfoResponseDTO parentInfo = BaselineApplication.getApplication().getRbtConnector().getCacheParentInfo();
                    if (parentInfo.getChilds() != null && parentInfo.getChilds().size() > 0) {
                        mChildCountText.setText(parentInfo.getChilds().size() + "");
                    } else {
                        mChildCountText.setText("");
                    }
                    mLabeledGiftRbt.setVisibility(View.VISIBLE);
                } else {
                    mLabeledGiftRbt.setVisibility(View.GONE);
                }
            }

            @Override
            public void isChild(boolean exist) {
                mLabeledGiftRbt.setVisibility(View.GONE);
            }

            @Override
            public void isNone(boolean exist) {
                mLabeledGiftRbt.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtil.RequestCode.PHONE_STATE: {
                if (PermissionUtil.isPermissionGranted(grantResults)) {
                    if (!isAdded()) return;
                    if (mLabeledDigitalStar != null)
                        mLabeledDigitalStar.enableSwitchStatusSilently();
                }
                break;
            }
        }
    }

    private void refreshMyAccount() {
        if (!AppConfigurationValues.isShowMyAccount()) {
            return;
        }
        if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
            try {
                PlanBean bean = new PlanBean.Builder().build(getContext(), BaselineApplication.getApplication().getRbtConnector().getCacheUserSubscription());
                String currentPlanString = bean.finalText;
                currentPlanString = currentPlanString.replaceAll("\n", " / ");
                mLabeledMyAccount.setValue(currentPlanString);
            } catch (Exception e) {
                e.printStackTrace();
                mLabeledMyAccount.setValue("");
            }
        } else {
            mLabeledMyAccount.setValue("");
        }
    }

    private void ShowPersonalizedShuffleConfirmation(boolean checked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getRootActivity());
        String message = null;
        if (checked) {
            message = getString(R.string.personalized_shuffle_enabled_success_message);
        } else {
            message = getString(R.string.personalized_shuffle_disable_success_message);
        }
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.personalized_shuffle_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (checked) {
                            mLabeledPersonalizedShuffle.enableSwitchStatusSilently();
                        } else {
                            mLabeledPersonalizedShuffle.disableSwitchStatusSilently();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updatePersonalizedShuffle(boolean status) {
        showProgress(true);
        BaselineApplication.getApplication().getRbtConnector().updateUSerDefinedShuffleStatus(AnalyticsConstants.EVENT_PV_PERSONALIZED_SHUFFLE_SOURCE_PROFILE, status, new AppBaselineCallback<UpdateUserDefinedShuffleResponseDTO>() {
            @Override
            public void success(UpdateUserDefinedShuffleResponseDTO result) {
                if (!isAdded()) return;
                showProgress(false);
                ShowPersonalizedShuffleConfirmation(status);
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                showProgress(false);
                getRootActivity().showShortToast(message);
            }
        });
    }

    ProgressDialog progressDialog;

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
