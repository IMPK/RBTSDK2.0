package com.onmobile.rbt.baseline.configuration;

/**
 * Created by titto.jose on 2/29/2016.
 */
public class AppConfigConstants {

    public enum Modules {
        Config, Registration, MainApp,
    }

    public enum Config {
        Debug,
        CrashReportEnabled,
        FirebaseAnalyticsEnabled,
        CleaverTapAnalyticsEnabled,
    }

    public enum Registration {
        SkipOtpValidation
    }

    public enum MainApp {
        IsAutoPlayEnabled,
        IsDigitalStarToCopyEnabled,
        IsServerKeySyncRequired,
        FamilyAndFriendsEnabled,
        PlanUpgrade,
        IsCertificatePinningRequired,
        IsContactSyncEnabled,
        IsRetailIdEnabled,
        IsLanguageInSearchEnabled,
        IsShowBannerInStore,
        IsShowSinglePlanUpgrade,
        IsShownConfirmationForProfileTune,
        IsShowRatingDialogFeature,
        StoreMaxItemPerChart,
        isUDPEnabled,
        PrebuyVisualizerEnabled,
        isSelectionModel,
        isDeleteLastSelection,
        isShowVideoTutorial,
        isShowPlansForNewUser,
        isShowMyAccount,
        isShowExitPopup,
        isDummyPurchaseEnabled,
        IsPayTMEnabled,
        PreBuyPaginationSupported,
        SecureAuthenticationFlow,
        isMultiAppLanguageSupport
    }


}
