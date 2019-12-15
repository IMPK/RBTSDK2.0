package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

/**
 * Created by nikita
 */
public class LanguageDTO implements Comparable<LanguageDTO> {

    String languageCode;
    String languageChartGroup;
    String languageDisplayName;
    String index;
    String languageMusicShuffleChartid;
    String languageEocnChartId;
    String userLanguageMapping;
    String userCircleMapping;
    String languageBannerChartId;

    public LanguageDTO(String languageDisplayName) {
        this.languageDisplayName = languageDisplayName;
    }

    public LanguageDTO(String index, String language_code, String language_chartgroup, String languageDisplayName,
                       String language_musicshuffle_chartid, String language_eocn_chartId, String user_circle_mapping,
                       String user_language_mapping, String language_banner_chartId) {
        this.index = index;
        this.languageCode = language_code;
        this.languageChartGroup = language_chartgroup;
        this.languageDisplayName = languageDisplayName;
        this.languageMusicShuffleChartid = language_musicshuffle_chartid;
        this.languageEocnChartId = language_eocn_chartId;
        this.userCircleMapping = user_circle_mapping;
        this.userLanguageMapping = user_language_mapping;
        this.languageBannerChartId = language_banner_chartId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageChartGroup() {
        return languageChartGroup;
    }

    public void setLanguageChartGroup(String languageChartGroup) {
        this.languageChartGroup = languageChartGroup;
    }

    public String getLanguageDisplayName() {
        return languageDisplayName;
    }

    public void setLanguageDisplayName(String languageDisplayName) {
        this.languageDisplayName = languageDisplayName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getLanguageMusicShuffleChartid() {
        return languageMusicShuffleChartid;
    }

    public void setLanguageMusicShuffleChartid(String languageMusicShuffleChartid) {
        this.languageMusicShuffleChartid = languageMusicShuffleChartid;
    }

    public String getLanguageEocnChartId() {
        return languageEocnChartId;
    }

    public void setLanguageEocnChartId(String languageEocnChartId) {
        this.languageEocnChartId = languageEocnChartId;
    }

    @Override
    public int compareTo(LanguageDTO another) {
        try {
            int i = Integer.parseInt(getIndex()) - Integer.parseInt(another.getIndex());
            return i;
        } catch (Exception e) {
            return -1;
        }
    }


    public String getUserLanguageMapping() {
        return userLanguageMapping;
    }

    public void setUserLanguageMapping(String userLanguageMapping) {
        this.userLanguageMapping = userLanguageMapping;
    }

    public String getUserCircleMapping() {
        return userCircleMapping;
    }

    public void setUserCircleMapping(String userCircleMapping) {
        this.userCircleMapping = userCircleMapping;
    }

    public String getLanguageBannerChartId() {
        return languageBannerChartId;
    }

    public void setLanguageBannerChartId(String languageBannerChartId) {
        this.languageBannerChartId = languageBannerChartId;
    }
}
