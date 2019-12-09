package com.onmobile.rbt.baseline.configuration;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.onmobile.rbt.baseline.BuildConfig;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Nikita Gurwani .
 */
public class AppConfigurationValues {

    private Context mContext;
    private static HashMap<String, HashMap<String, String>> applicationConfiguration;
    public static final String APP_TRUE = "TRUE";
    public static final String APP_FALSE = "FALSE";

    public AppConfigurationValues(Context context) {
        this.mContext = context;
        loadAppConfiguration();
    }

    private void loadAppConfiguration() {
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream is = assetManager.open("AppFeatures_Config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList parentNodeList = doc.getElementsByTagName("module");
            applicationConfiguration = new HashMap<>();
            for (int temp = 0; temp < parentNodeList.getLength(); temp++) {

                Node parentNode = parentNodeList.item(temp);
                if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element parentElement = (Element) parentNode;
                    NodeList childrenNodeList = parentElement.getChildNodes();
                    HashMap<String, String> config = new HashMap<>();
                    for (int j = 0; j < childrenNodeList.getLength(); j++) {
                        Node childNode = childrenNodeList.item(j);
                        if (BuildConfig.DEBUG)
                            Log.e("loadAppConfiguration: ", j + " app config");
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element childElement = (Element) childNode;
                            config.put(childElement.getAttribute("name"), childElement.getAttribute("value"));
                        }
                    }
                    applicationConfiguration.put(parentElement.getAttribute("name"), config);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isDebuggable() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.Config.toString())
                    .get(AppConfigConstants.Config.Debug.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsServerKeySyncRequired : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean serverOneSignalKeySynRequired() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsServerKeySyncRequired.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsServerKeySyncRequired : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean skipOtpValidation() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.Registration.toString())
                    .get(AppConfigConstants.Registration.SkipOtpValidation.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "SkipOtpValidation : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isAutoPlayEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsAutoPlayEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsAutoPlayEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isDigitalStarToCopyEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsDigitalStarToCopyEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsDigitalStarToCopyEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isFamilyAndFriendsEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.FamilyAndFriendsEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "FamilyAndFriendsEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isPlanUpgradable() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.PlanUpgrade.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "PlanUpgrade : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isContactSyncEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsContactSyncEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "GoogleSmsRetrieverForOtpEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isRetailIdEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsRetailIdEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "isRetailIdEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isLanguageInSearchEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsLanguageInSearchEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "isRetailIdEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean IsShowBannerInStore() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsShowBannerInStore.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "isLanguageInSearchEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean IsShowSinglePlanUpgrade() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsShowSinglePlanUpgrade.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShowSinglePlanUpgrade : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean IsShownConfirmationForProfileTune() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsShownConfirmationForProfileTune.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShownConfirmationForProfileTune : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean IsShowRatingDialogFeature() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsShowRatingDialogFeature.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShowRatingDialogFeature : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean crashReportEnabled() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.Config.toString())
                    .get(AppConfigConstants.Config.CrashReportEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShownConfirmationForProfileTune : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean firebaseAnalyticsEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.Config.toString())
                    .get(AppConfigConstants.Config.FirebaseAnalyticsEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShownConfirmationForProfileTune : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean cleaverTapAnalyticsEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.Config.toString())
                    .get(AppConfigConstants.Config.CleaverTapAnalyticsEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShownConfirmationForProfileTune : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static int getStoreMaxItemPerChart() {
        int count = 32; //Default
        try {
            count = Integer.parseInt(applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.StoreMaxItemPerChart.toString()));
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("TAG", "IsShownConfirmationForProfileTune : " + "could not be read from the AppFeatureConfig xml.");
        }
        return count;
    }

    public static boolean isUDPEnabled() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isUDPEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isUDPEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isSelectionModel() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isSelectionModel.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isSelectionModel : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isDeleteLastSelection() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isDeleteLastSelection.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isDeleteLastSelection : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isShowVideoTutorial() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isShowVideoTutorial.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isShowVideoTutorial : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isShowPlansForNewUser() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isShowPlansForNewUser.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isShowPlansForNewUser : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isPrebuyVisualizerEnabled() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.PrebuyVisualizerEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "PrebuyVisualizerEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }
    public static boolean isSecureAuthenticationFlowEnabled() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.SecureAuthenticationFlow.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isSecureAuthenticationFlowEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }


    public static boolean isDummyPurchaseEnabled() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isDummyPurchaseEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isDummyPurchaseEnabled : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isShowMyAccount() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isShowMyAccount.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isShowMyAccount : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isShowExitPopup() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isShowExitPopup.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isShowExitPopup : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }

    public static boolean isPrebuyPaginationSupported() {
        boolean isSupported = false;
        /*try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.PreBuyPaginationSupported.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "isShowExitPopup : " + "could not be read from the AppFeatureConfig xml.");
        }*/
        return isSupported;
    }

    /*
      Will return app config {IsPayTMEnabled} value from  AppFeatures_Config.xml
    */
    public static boolean isPayTMOptionEnabled() {
        boolean isSupported = true;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.IsPayTMEnabled.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "IsShowConfirmationForOptNetwork : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }


    public static boolean isMultiAppLanguageSupport() {
        boolean isSupported = false;
        try {
            isSupported = applicationConfiguration
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.isMultiAppLanguageSupport.toString())
                    .equalsIgnoreCase(APP_TRUE);
        } catch (Exception e) {
            Log.e("TAG", "DeviseLanguagePicking : " + "could not be read from the AppFeatureConfig xml.");
        }
        return isSupported;
    }
}
