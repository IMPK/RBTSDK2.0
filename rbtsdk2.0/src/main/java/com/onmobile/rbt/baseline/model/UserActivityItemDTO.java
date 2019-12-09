package com.onmobile.rbt.baseline.model;

public class UserActivityItemDTO {

    private String title;
    private String subTitle;
    private String playingFor;
    private String setDate;
    private String previewImgUrl;
    private String endDate;
    private Object data;
    private boolean isDownloadedOnly;

    public UserActivityItemDTO(){

    }

    public UserActivityItemDTO(String title, String subTitle, String playingFor, String setDate, String previewImgUrl, Object data, boolean isDownloadedOnly) {
        this.title = title;
        this.subTitle = subTitle;
        this.playingFor = playingFor;
        this.setDate = setDate;
        this.previewImgUrl = previewImgUrl;
        this.data = data;
        this.isDownloadedOnly = isDownloadedOnly;
    }

    public UserActivityItemDTO(String title, String subTitle, String playingFor, String setDate, String previewImgUrl, String endDate, Object data, boolean isDownloadedOnly) {
        this.title = title;
        this.subTitle = subTitle;
        this.playingFor = playingFor;
        this.setDate = setDate;
        this.previewImgUrl = previewImgUrl;
        this.endDate = endDate;
        this.data = data;
        this.isDownloadedOnly = isDownloadedOnly;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPlayingFor() {
        return playingFor;
    }

    public void setPlayingFor(String playingFor) {
        this.playingFor = playingFor;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getPreviewImgUrl() {
        return previewImgUrl;
    }

    public void setPreviewImgUrl(String previewImgUrl) {
        this.previewImgUrl = previewImgUrl;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isDownloadedOnly() {
        return isDownloadedOnly;
    }

    public void setDownloadedOnly(boolean downloadedOnly) {
        isDownloadedOnly = downloadedOnly;
    }
}
