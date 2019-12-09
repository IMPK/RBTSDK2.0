package com.onmobile.baseline.http.cache;

import com.onmobile.baseline.http.api_action.dtos.ChartGroupDTO;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfPurchasedSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.UserInfoDTO;
import com.onmobile.baseline.http.api_action.dtos.UserLanguageSettingDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class UserSettingsCacheManager {

    static UserSubscriptionDTO userSubscriptionDTO;

    static UserLanguageSettingDTO userLanguageSetting;

    static String authenticationToken;

    static ChartGroupDTO homeChartIds;

    static UserInfoDTO userInfoDTO;

    static String msisdn;

    static ListOfSongsResponseDTO listOfSongsResponseDTO;
    static ListOfPurchasedSongsResponseDTO listOfPurchasedSongsResponseDTO;

    static List<String> selectedPlayRuleList;
    static List<String> selectedPurchasedSongList;

    static RecommendationDTO recommendationDTO;

    static String serverDateHeader;

    static GetParentInfoResponseDTO parentInfo;
    static GetChildInfoResponseDTO childInfo;

    public static UserSubscriptionDTO getUserSubscriptionDTO() {
        if(userSubscriptionDTO==null){
            LocalCacheManager.getInstance().getUserSubscriptionInfo();
        }
        return userSubscriptionDTO;
    }

    public static void setUserSubscriptionDTO(UserSubscriptionDTO userSubscriptionDTO) {
        LocalCacheManager.getInstance().setUserSubscriptionInfo(userSubscriptionDTO);
        UserSettingsCacheManager.userSubscriptionDTO = userSubscriptionDTO;
    }

    public static UserLanguageSettingDTO getUserLanguageSetting() {
        return userLanguageSetting;
    }

    public static void setUserLanguageSetting(UserLanguageSettingDTO userLanguageSetting) {
        UserSettingsCacheManager.userLanguageSetting = userLanguageSetting;
    }

    public static String getAuthenticationToken() {

        return LocalCacheManager.getInstance().getUserAuthToken();
    }



    public static ChartGroupDTO getHomeChartIds() {
        return homeChartIds;
    }

    public static void setHomeChartIds(ChartGroupDTO homeChartIds) {
        UserSettingsCacheManager.homeChartIds = homeChartIds;
    }

    public static UserInfoDTO getUserInfoDTO() {
        if(userInfoDTO==null){
            userInfoDTO = LocalCacheManager.getInstance().getUserInfo();
        }
        return userInfoDTO;
    }

    public static ListOfPurchasedSongsResponseDTO getListOfPurchasedSongsResponseDTO() {
        return listOfPurchasedSongsResponseDTO;
    }

    public static void setListOfPurchasedSongsResponseDTO(ListOfPurchasedSongsResponseDTO listOfPurchasedSongsResponseDTO) {
        if (listOfPurchasedSongsResponseDTO != null) {
            UserSettingsCacheManager.selectedPurchasedSongList = new ArrayList<>();
            if (listOfPurchasedSongsResponseDTO.getRingBackToneDTOS() != null)
                for (RingBackToneDTO ringBackToneDTO : listOfPurchasedSongsResponseDTO.getRingBackToneDTOS()) {
                    UserSettingsCacheManager.selectedPurchasedSongList.add(ringBackToneDTO.getId());
                }
            if (listOfPurchasedSongsResponseDTO.getChartItemDTO() != null)
                for (ChartItemDTO chartItemDTO : listOfPurchasedSongsResponseDTO.getChartItemDTO()) {
                    UserSettingsCacheManager.selectedPurchasedSongList.add(String.valueOf(chartItemDTO.getId()));
                }
        }
        UserSettingsCacheManager.listOfPurchasedSongsResponseDTO = listOfPurchasedSongsResponseDTO;

    }

    public static void setUserInfoDTO(UserInfoDTO userInfoDTO) {
        LocalCacheManager.getInstance().setUserInfo(userInfoDTO);
        UserSettingsCacheManager.userInfoDTO = userInfoDTO;
    }

    public static String getMsisdn() {
        if(msisdn==null)
            msisdn = LocalCacheManager.getInstance().getUserMsisdn();
        return msisdn;
    }

    public static void setMsisdn(String msisdn) {
        LocalCacheManager.getInstance().setUserMsisdn(msisdn);
        UserSettingsCacheManager.msisdn = msisdn;
    }

    public static ListOfSongsResponseDTO getListOfSongsResponseDTO() {
        return listOfSongsResponseDTO;
    }

    public static void setListOfSongsResponseDTO(ListOfSongsResponseDTO listOfSongsResponseDTO) {
        if (listOfSongsResponseDTO != null) {
            UserSettingsCacheManager.selectedPlayRuleList = new ArrayList<>();
            if (listOfSongsResponseDTO.getRingBackToneDTOS() != null)
                for (RingBackToneDTO ringBackToneDTO : listOfSongsResponseDTO.getRingBackToneDTOS()) {
                    UserSettingsCacheManager.selectedPlayRuleList.add(ringBackToneDTO.getId());
                }
            if (listOfSongsResponseDTO.getChartItemDTO() != null)
                for (ChartItemDTO chartItemDTO : listOfSongsResponseDTO.getChartItemDTO()) {
                    UserSettingsCacheManager.selectedPlayRuleList.add(String.valueOf(chartItemDTO.getId()));
                }
        }
        UserSettingsCacheManager.listOfSongsResponseDTO = listOfSongsResponseDTO;
    }

    public static List<String> getSelectedPlayRuleList() {
        return selectedPlayRuleList;
    }

    public static List<String> getSelectedPurchasedSongList() {
        return selectedPurchasedSongList;
    }

    public static void deletePlayRuleFromCache(String playRuleId) {
        if (listOfSongsResponseDTO != null) {
            int itemCounter = 0;
            if (listOfSongsResponseDTO.getRingBackToneDTOS() != null &&
                    listOfSongsResponseDTO.getRingBackToneDTOS().size() > 0) {
                itemCounter = listOfSongsResponseDTO.getRingBackToneDTOS().size();
                for (RingBackToneDTO ringBackToneDTO : listOfSongsResponseDTO.getRingBackToneDTOS()) {
                    try {
                        if (ringBackToneDTO.getPlayRuleDTO().getId().equals(playRuleId)) {
                            listOfSongsResponseDTO.getRingBackToneDTOS().remove(ringBackToneDTO);
                            selectedPlayRuleList.remove(ringBackToneDTO.getId());
                            itemCounter--;
                            break;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (listOfSongsResponseDTO.getChartItemDTO() != null &&
                    listOfSongsResponseDTO.getChartItemDTO().size() > 0) {
                itemCounter += listOfSongsResponseDTO.getChartItemDTO().size();
                for (ChartItemDTO chartItemDTO : listOfSongsResponseDTO.getChartItemDTO()) {
                    try {
                        if (chartItemDTO.getPlayRuleDTO().getId().equals(playRuleId)) {
                            listOfSongsResponseDTO.getChartItemDTO().remove(chartItemDTO);
                            selectedPlayRuleList.remove(String.valueOf(chartItemDTO.getId()));
                            itemCounter--;
                            break;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (itemCounter == 0) {
                listOfSongsResponseDTO = null;
            }
        }
    }

    public static void deletePurchasedSongFromCache(String id) {
        if (listOfPurchasedSongsResponseDTO != null) {
            int itemCounter = 0;
            if (listOfPurchasedSongsResponseDTO.getRingBackToneDTOS() != null &&
                    listOfPurchasedSongsResponseDTO.getRingBackToneDTOS().size() > 0) {
                itemCounter = listOfPurchasedSongsResponseDTO.getRingBackToneDTOS().size();
                for (RingBackToneDTO ringBackToneDTO : listOfPurchasedSongsResponseDTO.getRingBackToneDTOS()) {
                    try {
                        if (ringBackToneDTO.getId().equals(id)) {
                            listOfPurchasedSongsResponseDTO.getRingBackToneDTOS().remove(ringBackToneDTO);
                            selectedPurchasedSongList.remove(ringBackToneDTO.getId());
                            itemCounter--;
                            break;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (listOfPurchasedSongsResponseDTO.getChartItemDTO() != null &&
                    listOfPurchasedSongsResponseDTO.getChartItemDTO().size() > 0) {
                itemCounter += listOfPurchasedSongsResponseDTO.getChartItemDTO().size();
                for (ChartItemDTO chartItemDTO : listOfPurchasedSongsResponseDTO.getChartItemDTO()) {
                    try {
                        if ((chartItemDTO.getId()+"").equals(id)) {
                            listOfPurchasedSongsResponseDTO.getChartItemDTO().remove(chartItemDTO);
                            selectedPurchasedSongList.remove(String.valueOf(chartItemDTO.getId()));
                            itemCounter--;
                            break;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (itemCounter == 0) {
                listOfPurchasedSongsResponseDTO = null;
            }
        }
    }

    public static RecommendationDTO getRecommendationDTO() {
        return recommendationDTO;
    }

    public static void setRecommendationDTO(RecommendationDTO recommendationDTO) {
        UserSettingsCacheManager.recommendationDTO = recommendationDTO;
    }

    public static void setServerDateHeader(String dateHeader) {
        serverDateHeader = dateHeader;
    }

    public static String getServerDateHeader() {
        return UserSettingsCacheManager.serverDateHeader;
    }

    public static GetParentInfoResponseDTO getParentInfo() {
        return parentInfo;
    }

    public static void setParentInfo(GetParentInfoResponseDTO parentInfo) {
        UserSettingsCacheManager.parentInfo = parentInfo;
    }

    public static GetChildInfoResponseDTO getChildInfo() {
        return childInfo;
    }

    public static void setChildInfo(GetChildInfoResponseDTO childInfo) {
        UserSettingsCacheManager.childInfo = childInfo;
    }

    public static void clearTempCache() {
        //appConfigParentDTO = null;
        userSubscriptionDTO = null;
        LocalCacheManager.getInstance().setUserSubscriptionInfo(null);

        userLanguageSetting = null;

        authenticationToken = null;
        LocalCacheManager.getInstance().setUserAuthToken(null);

        homeChartIds = null;

        userInfoDTO = null;
        LocalCacheManager.getInstance().setUserInfo(null);

        msisdn = null;
        LocalCacheManager.getInstance().setUserMsisdn(null);

        listOfSongsResponseDTO = null;
        listOfPurchasedSongsResponseDTO = null;
        selectedPlayRuleList = null;
        selectedPurchasedSongList =null;
        recommendationDTO = null;
        parentInfo = null;
        childInfo = null;
    }

}
