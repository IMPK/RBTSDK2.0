package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.onmobile.baseline.http.api_action.dtos.CatalogSubLanguageDTO;
import com.onmobile.baseline.http.api_action.dtos.UserLanguageSettingDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.cache.LocalCacheManager;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AppConfigDataManipulator {

    static AppConfigParentDTO appConfigParentDTO;
    static LinkedTreeMap<String, LanguageDTO> langKeyMap;

    public static AppConfigParentDTO getAppConfigParentDTO() {
        //appConfigParentDTO = UserSettingsCacheManager.getAppConfigParentDTO();
        if (appConfigParentDTO == null) {
            appConfigParentDTO = LocalCacheManager.getInstance().getLocalAppConfigCache();
        }
        return appConfigParentDTO;
    }

    public static void setAppConfigParentDTO(AppConfigParentDTO appConfigParentDTO) {
        AppConfigDTO appConfigDTO = appConfigParentDTO.getAppConfigDTO();
        Object contentLanguage = appConfigDTO.getContentLanguage();
        objectToLanguageDTO(contentLanguage);
        //UserSettingsCacheManager.setAppConfigParentDTO(appConfigParentDTO);
        LocalCacheManager.getInstance().updateAppConfigCache(appConfigParentDTO);
        AppConfigDataManipulator.appConfigParentDTO = appConfigParentDTO;
        getAppConfigParentDTO();
    }


    private static LinkedTreeMap<String, LanguageDTO> objectToLanguageDTO(Object object) {
        LinkedTreeMap<String, Object> hasTreeMap = (LinkedTreeMap<String, Object>) object;
        langKeyMap = new LinkedTreeMap<>();
        for (String key : hasTreeMap.keySet()) {
            Object subObject = hasTreeMap.get(key);
            LinkedTreeMap<String, Object> hasSubTreeMap = (LinkedTreeMap<String, Object>) subObject;
            Object language_code = hasSubTreeMap.get("language_code");
            Object language_chartgroup = hasSubTreeMap.get("language_chartgroup");
            Object indexValue = hasSubTreeMap.get("index");
            Object language_musicshuffle_chartid = hasSubTreeMap.get("language_musicshuffle_chartid");
            Object language_eocn_chartId = hasSubTreeMap.get("language_eocn_chartId");
            Object user_circle_mapping = hasSubTreeMap.get("user_circle_mapping");
            Object user_language_mapping = hasSubTreeMap.get("user_language_mapping");
            Object language_banner_chartId = hasSubTreeMap.get("language_banner_chartId");
            if (language_musicshuffle_chartid == null) {
                language_musicshuffle_chartid = "";
            }

            if (language_eocn_chartId == null) {
                language_eocn_chartId = "";
            }

            if (language_banner_chartId == null) {
                language_banner_chartId = "";
            }

            if (user_circle_mapping == null) {
                user_circle_mapping = "";
            }

            if (user_language_mapping == null) {
                user_language_mapping = "";
            }

            String index = "10000";
            if (indexValue != null) {
                index = indexValue.toString();
            }
            index = trimBeforeAndAfter(index);
            String langDisplayName = key;
            LanguageDTO languageDTO = new LanguageDTO(index, language_code.toString(), language_chartgroup.toString(), langDisplayName, language_musicshuffle_chartid.toString(), language_eocn_chartId.toString(), user_circle_mapping.toString(), user_language_mapping.toString(), language_banner_chartId.toString());
            langKeyMap.put(key, languageDTO);
        }


        for (String key : langKeyMap.keySet()) {
            LanguageDTO languageDTO = langKeyMap.get(key);
            Log.e("lang Key @" + key, "lang Value @" + languageDTO.getLanguageChartGroup());
        }
        return langKeyMap;
    }

    private static String trimBeforeAndAfter(String text) {
        return text.trim().replaceAll(" +", " ");
    }

    private static String getDefaultHomeChartId() {
        AppDTO appDTO = getAppConfigParentDTO().getAppConfigDTO().getAppDTO();
        String chartGroupId = appDTO.chartGroupId;
        return chartGroupId;
    }

    public static void handleUserLanguage(UserSubscriptionDTO userSubscriptionDTO) {
        setUserLanguageSetting(userSubscriptionDTO.getCircle(), userSubscriptionDTO.getLanguage(), getDefaultHomeChartId());
    }

    public static void handleUserLanguage(String lang) {
        setUserLanguageSetting(null, lang, null);
    }

    private static void setUserLanguageSetting(String circle, String lang, String home_chart) {
        LanguageDTO languageDTO = getUserDefaultSetting();
        if (lang != null) {
            languageDTO = getLanguageSettingLanguage(lang);
        } else if (circle != null) {
            languageDTO = getLanguageSettingCircle(circle);
        } else if (home_chart != null) {
            languageDTO = getLanguageSettingChartId(home_chart);
        }
        UserLanguageSettingDTO userLanguageSettingDTO = new UserLanguageSettingDTO();
        userLanguageSettingDTO.setLanguageDTO(languageDTO);
        UserSettingsCacheManager.setUserLanguageSetting(userLanguageSettingDTO);
    }

    private static LanguageDTO getLanguageSettingCircle(String circle) {
        if (langKeyMap != null && !langKeyMap.isEmpty()) {
            for (LanguageDTO value : langKeyMap.values()) {
                if (value.getUserCircleMapping().equalsIgnoreCase(circle)) {

                    return value;
                }
            }
            return getUserDefaultSetting();
        }
        return getUserDefaultSetting();
    }

    private static LanguageDTO getLanguageSettingLanguage(String lang) {
        if (langKeyMap != null && !langKeyMap.isEmpty()) {
            for (LanguageDTO value : langKeyMap.values()) {
                if (value.getLanguageCode().equalsIgnoreCase(lang)) {

                    return value;
                }
            }
            return getUserDefaultSetting();
        }
        return getUserDefaultSetting();
    }

    private static LanguageDTO getLanguageSettingChartId(String chart_id) {
        if (langKeyMap != null && !langKeyMap.isEmpty()) {
            for (LanguageDTO value : langKeyMap.values()) {
                if (value.getLanguageChartGroup().equalsIgnoreCase(chart_id)) {

                    return value;
                }
            }
            return getUserDefaultSetting();
        }
        return getUserDefaultSetting();
    }

    public static FriendsAndFamilyConfigDTO getFriendsAndFamilyConfigDTO() {
        AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
        FriendsAndFamilyConfigDTO friendsAndFamilyConfigDTO = appConfigDTO.getFriendsAndFamilyConfigDTO();


        return friendsAndFamilyConfigDTO;
    }

    public static LanguageDTO getUserDefaultSetting() {
        AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
        AppDTO appDTO = appConfigDTO.getAppDTO();

        String chartGroupId = appDTO.chartGroupId;

        if (langKeyMap != null && !langKeyMap.isEmpty()) {
            for (LanguageDTO value : langKeyMap.values()) {
                if (value.getLanguageChartGroup().equalsIgnoreCase(chartGroupId)) {

                    return value;
                }
            }
            return null;
        }
        return null;
    }


    public static NonNetworkCGDTO getOfflineCGConsent() {
        AppConfigDTO appConfigDTO = appConfigParentDTO.getAppConfigDTO();
        AppDTO appDTO = appConfigDTO.getAppDTO();
        Object consentGateWayDTO = appDTO.getConsentGateWayDTO();
        LinkedTreeMap<String, Object> consent = (LinkedTreeMap<String, Object>) consentGateWayDTO;
        Object object = consent.get(APIRequestParameters.APIConfigParsingKeys.NON_NETWROK_CG);
        Gson gson = new Gson();
        String obj = gson.toJson(object);
        NonNetworkCGDTO nonNetworkCGDTO = gson.fromJson(obj, NonNetworkCGDTO.class);


        return nonNetworkCGDTO;
    }

    public static String getManualProfileContent() {
        if (getAppConfigParentDTO() != null) {
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            ProfiletunesDTO appConfigProfileTuneDTO = appConfigDTO.getAppConfigProfileDTO();

            String manual_chart_id = appConfigProfileTuneDTO.getManual().getContentchartid();
            return manual_chart_id;
        }
        return null;
    }

    public static Object getDigitalStarAppConfig() {
        if (getAppConfigParentDTO() != null) {
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            Object widgetappconfig = appConfigDTO.getWidget();
            LinkedTreeMap<String, Object> widget = (LinkedTreeMap<String, Object>) widgetappconfig;
            Object digitalStarWidget = widget.get(APIRequestParameters.APIConfigParsingKeys.DIGITALSTARCOPY);
            return digitalStarWidget;
        }
        return null;
    }

    public static String getShuffleContent() {
        if (getAppConfigParentDTO() != null) {
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            String chart_id = UserSettingsCacheManager.getUserLanguageSetting().getLanguageDTO().getLanguageMusicShuffleChartid();
            return chart_id;
        }
        return null;
    }

    public static Map<String, String> getAutoProfileContentIds() {
        if (getAppConfigParentDTO() != null) {
            Map<String, String> autoprofileids = new HashMap<>();
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            ProfiletunesDTO appConfigProfileTuneDTO = appConfigDTO.getAppConfigProfileDTO();
            Autodetect autodetect = appConfigProfileTuneDTO.getAutodetect();
            autoprofileids.put(autodetect.getSilent().getTag(), autodetect.getSilent().getTrackid());
            autoprofileids.put(autodetect.getLowBattery().getTag(), autodetect.getLowBattery().getTrackid());
            autoprofileids.put(autodetect.getRoaming().getTag(), autodetect.getRoaming().getTrackid());
            autoprofileids.put(autodetect.getMeeting().getTag(), autodetect.getMeeting().getTrackid());
            return autoprofileids;
        }
        return null;
    }

    public static Autodetect getAutoProfileAppConfig() {
        if (getAppConfigParentDTO() != null) {
            Map<String, String> autoprofileids = new HashMap<>();
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            ProfiletunesDTO appConfigProfileTuneDTO = appConfigDTO.getAppConfigProfileDTO();

            return appConfigProfileTuneDTO.getAutodetect();
        }
        return null;
    }

    public static String getDynamicMusicShuffleId() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            String id = baseline2DTO.getDynamicShufflegroupid();
            return id;
        }

        return null;
    }

    public static String getStoreChartId() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            String id = baseline2DTO.getStoreChartId();
            return id;
        }

        return null;
    }

    public static String getHomeTrendingChart() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            String id = baseline2DTO.getHomeTrendingChart();
            return id;
        }

        return null;
    }

    public static String getDynamicChartId() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            String id = baseline2DTO.getDynamicChartgroupid();
            return id;
        }

        return null;
    }

    public static String getBestValuePack() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            return baseline2DTO.getBestValuePack();
        }

        return null;
    }

    public static Baseline2DTO getBaseline2DtoAppConfig() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();

            return baseline2DTO;
        }
        return null;
    }

    public static HashMap<Integer, Integer> getCardIndexMap() {
        if (getAppConfigParentDTO() != null) {
            TreeMap<Integer, Integer> cardIndexTreeMap = new TreeMap<>();
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();

            if(baseline2DTO != null){

                String trendingIndex = baseline2DTO.getCardindex().getTrending();
                if(!TextUtils.isEmpty(trendingIndex) && Integer.parseInt(trendingIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(trendingIndex), Cardindex.CARD_TRENDING);
                }

                String profileTuneIndex = baseline2DTO.getCardindex().getProfiletune();
                if(!TextUtils.isEmpty(profileTuneIndex) && Integer.parseInt(profileTuneIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(profileTuneIndex), Cardindex.CARD_PROFILE);
                }

                String nameTuneIndex = baseline2DTO.getCardindex().getNametune();
                if(!TextUtils.isEmpty(nameTuneIndex) && Integer.parseInt(nameTuneIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(nameTuneIndex), Cardindex.CARD_NAMETUNE);
                }

                String shuffleIndex = baseline2DTO.getCardindex().getShuffle();
                if(!TextUtils.isEmpty(shuffleIndex) && Integer.parseInt(shuffleIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(shuffleIndex), Cardindex.CARD_SHUFFLE);
                }

                String recommendationIndex = baseline2DTO.getCardindex().getRecommendation();
                if(!TextUtils.isEmpty(recommendationIndex) && Integer.parseInt(recommendationIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(recommendationIndex), Cardindex.CARD_RECOMMENDATION);
                }

                String bannerIndex = baseline2DTO.getCardindex().getBanner();
                if(!TextUtils.isEmpty(bannerIndex) && Integer.parseInt(bannerIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(bannerIndex), Cardindex.CARD_BANNER);
                }

                String azanIndex = baseline2DTO.getCardindex().getAzan();
                if(!TextUtils.isEmpty(azanIndex) && Integer.parseInt(azanIndex) > 0){
                    cardIndexTreeMap.put(Integer.parseInt(azanIndex), Cardindex.CARD_AZAN);
                }

                if(cardIndexTreeMap != null && cardIndexTreeMap.size() > 0) {
                    HashMap<Integer, Integer> cardIndexMap = new HashMap<>();
                    int currentIndex = 0;
                    for (Map.Entry<Integer, Integer> cardEntry : cardIndexTreeMap.entrySet()) {
                        Integer cardEntryValue = cardEntry.getValue();
                        cardIndexMap.put(++currentIndex, cardEntryValue);
                    }
                    return cardIndexMap;
                }
                return null;
            }

        }
        return null;
    }


    public static List<String> getNameTuneConfig() {
        if (getAppConfigParentDTO() != null) {
            List<Integer> integers = new ArrayList<>();
            Map<Integer, String> nametuneLanguageDTO = new HashMap<>();
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            NametuneDTO nametuneDTO = appConfigDTO.getNametuneDTO();
            LinkedTreeMap<String, Object> hasTreeMap = (LinkedTreeMap<String, Object>) nametuneDTO.getLanguage();
            TreeMap<Integer, String> sortedMap = new TreeMap<>();

            for (String key : hasTreeMap.keySet()) {
                Object subObject = hasTreeMap.get(key);
                LinkedTreeMap<String, Object> hasSubTreeMap = (LinkedTreeMap<String, Object>) subObject;
                Object language_code = hasSubTreeMap.get("langauge_code");
                Object indexValue = hasSubTreeMap.get("index");
                integers.add(Integer.parseInt(indexValue.toString()));
                //if(indexValue.toString().equalsIgnoreCase(nametuneLanguageDTO.get))
                nametuneLanguageDTO.put(Integer.parseInt(indexValue.toString()), key);
            }
            for (Map.Entry<Integer, String> entry : nametuneLanguageDTO.entrySet()) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            List<String> returnValue = new ArrayList<String>(sortedMap.values());

            return returnValue;
        }
        return null;
    }

    public static Object getAutoProfileDTO() {
        if (getAppConfigParentDTO() != null) {
            List<String> autoprofileids = new ArrayList<>();
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            ProfiletunesDTO appConfigProfileTuneDTO = appConfigDTO.getAppConfigProfileDTO();
            Autodetect autodetect = appConfigProfileTuneDTO.getAutodetect();

            Gson gson = new Gson();
            String obj = gson.toJson(autodetect);
            Object auto_detect = gson.toJson(autodetect);

            return auto_detect;
        }
        return null;
    }


    public static LinkedHashMap<String, String> getConfigLanguage() {
        if (langKeyMap != null) {
            TreeMap<Integer, LanguageDTO> sortedMap = new TreeMap<>();
            final LinkedHashMap<String, String> languageNames = new LinkedHashMap<>();
            final Map<Integer, LanguageDTO> temp = new HashMap<>();
            for (Map.Entry<String, LanguageDTO> entry : langKeyMap.entrySet()) {
                temp.put(Integer.parseInt(entry.getValue().index), entry.getValue());
            }

            for (Map.Entry<Integer, LanguageDTO> entry : temp.entrySet()) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<Integer, LanguageDTO> entry : sortedMap.entrySet()) {
                languageNames.put(entry.getValue().languageCode, entry.getValue().languageDisplayName);
            }
            return languageNames;
        }
        return null;
    }


    public static LinkedHashMap<String, String> getCatalogLanguage() {
        if (objectToCatalogLanguageDTO() != null) {
            LinkedTreeMap<String, CatalogSubLanguageDTO> sortedMap = objectToCatalogLanguageDTO();
            final LinkedHashMap<String, String> languageNames = new LinkedHashMap<>();


            for (Map.Entry<String, CatalogSubLanguageDTO> entry : sortedMap.entrySet()) {
                languageNames.put(entry.getValue().getmIsoCode(), entry.getKey());
            }
            return languageNames;
        }
        return null;
    }

    public static LinkedTreeMap<String, CatalogSubLanguageDTO> objectToCatalogLanguageDTO() {
        Object object = getAppConfigParentDTO().getAppConfigDTO().getCatalogLanguageDTO();
        LinkedTreeMap<String, Object> hasTreeMap = (LinkedTreeMap<String, Object>) object;
        LinkedTreeMap<String, CatalogSubLanguageDTO> langKeyMap = new LinkedTreeMap<>();
        for (String key : hasTreeMap.keySet()) {
            Object subObject = hasTreeMap.get(key);
            LinkedTreeMap<String, Object> hasSubTreeMap = (LinkedTreeMap<String, Object>) subObject;
            for (String key1 : hasSubTreeMap.keySet()) {
                Object subObject1 = hasSubTreeMap.get(key1);
                LinkedTreeMap<String, Object> hasSubTreeMap1 = (LinkedTreeMap<String, Object>) subObject1;
                Object iso_code = hasSubTreeMap1.get("iso_code");
                CatalogSubLanguageDTO catalogSubLanguageDTO = new CatalogSubLanguageDTO(iso_code.toString());

                langKeyMap.put(key1, catalogSubLanguageDTO);
            }


        }


        for (String key : langKeyMap.keySet()) {
            CatalogSubLanguageDTO catalogSubLanguageDTO = langKeyMap.get(key);
            Log.e("lang Key @" + key, "lang Value @" + catalogSubLanguageDTO.getmIsoCode());
        }
        return langKeyMap;
    }

    public static String getBannerChartId(List<String> lang) {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            String chartgroup = baseline2DTO.getHome_chartgroup();
            return chartgroup;
        }
        return null;
    }

    public static UserRbtHistoryDTO getUserHistoryParams() {
        if (getAppConfigParentDTO() != null) {
            if (getAppConfigParentDTO().getAppConfigDTO() != null) {
                return getAppConfigParentDTO().getAppConfigDTO().getUserRbtHistoryDTO();
            }
            return null;
        }
        return null;
    }

    public static ShareContentDTO shareContentDTO() {
        AppDTO appDTO = getAppConfigParentDTO().getAppConfigDTO().getAppDTO();
        ShareContentDTO shareContentDTO = appDTO.getShareContentDTO();
        return shareContentDTO;
    }

    public static ShareAppDTO getShareAppConfig() {
        AppDTO appDTO = getAppConfigParentDTO().getAppConfigDTO().getAppDTO();
        return appDTO.getShareApp();
    }

    public static UpgradeDTO getUpgradeAppconfig() {
        AppDTO appDTO = getAppConfigParentDTO().getAppConfigDTO().getAppDTO();
        return appDTO.getUpgradeDTO();
    }

    public static String getPurchaseModeAppConfig() {
        AppDTO appDTO = getAppConfigParentDTO().getAppConfigDTO().getAppDTO();
        return appDTO.getPurchaseMode();
    }

    public static String getProfileRetailId() {
        AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
        if (appConfigDTO != null && appConfigDTO.getSubscriptionInfoDTO() != null && appConfigDTO.getSubscriptionInfoDTO().getProfiletune() != null && appConfigDTO.getSubscriptionInfoDTO().getProfiletune().getChargeClass() != null) {
            return appConfigDTO.getSubscriptionInfoDTO().getProfiletune().getChargeClass();
        } else {
            return null;
        }
    }


    public static String getTuneAlreadyPurchasedMessage() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            if(baseline2DTO == null){
                return null;
            }
            DownloadModel downloadModel = baseline2DTO.getDownloadModel();
            if(downloadModel == null){
                return null;
            }
            return downloadModel.getMessageTuneAlreadyPurchased();
        }

        return null;
    }

    public static String getAzanChartId() {
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            if(baseline2DTO == null){
                return null;
            }

            return baseline2DTO.getAzanChartId();
        }

        return null;
    }

    public static ConsentDTO getBaseline2ConsentDTO(){
        if (getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            if(baseline2DTO == null){
                return null;
            }

            return baseline2DTO.getConsentDTO();
        }
        return null;
    }


    public static LinkedTreeMap<String, Integer> getAppLocale() {

        if(getAppConfigParentDTO() == null) {
            return null;
        }

        Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
        if(baseline2DTO == null){
            return null;
        }

        return baseline2DTO.getAppLocale();

    }
    public static boolean isDigitalAuthenticationSupported() {
        boolean isSupported = false;
        if(getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            if(baseline2DTO != null && baseline2DTO.getDigitalAuthenticationDTO() != null){
                isSupported = baseline2DTO.getDigitalAuthenticationDTO().isSupported();
            }
        }
        return isSupported;
    }

    public static String getDigitalAuthenticationBaseUrl() {
        String baseUrl = "";
        if(getAppConfigParentDTO() != null) {
            Baseline2DTO baseline2DTO = getAppConfigParentDTO().getAppConfigDTO().getBaseline2DTO();
            if(baseline2DTO != null && baseline2DTO.getDigitalAuthenticationDTO() != null){
                baseUrl = baseline2DTO.getDigitalAuthenticationDTO().getBaseUrl();
            }
        }
        return baseUrl;
    }
}
