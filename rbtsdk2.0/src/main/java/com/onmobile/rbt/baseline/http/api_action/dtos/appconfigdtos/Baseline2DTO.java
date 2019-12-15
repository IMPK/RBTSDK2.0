package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

public class Baseline2DTO {

	@SerializedName("dynamic_shufflegroupid")
	private String dynamicShufflegroupid;

	@SerializedName("batch_api_endpoint_required")
	private String batchApiEndpointRequired;

	@SerializedName("dynamic_chartgroupid")
	private String dynamicChartgroupid;

	@SerializedName("storeindex")
	private Storeindex storeindex;

	@SerializedName("cardindex")
	private Cardindex cardindex;

	@SerializedName("home_chartgroup")
	private String home_chartgroup;

	@SerializedName("best_value_pack")
	private String bestValuePack;

	@SerializedName("home_trending_chart")
	private String homeTrendingChart;

	@SerializedName("store_chart_id")
	private String storeChartId;

	@SerializedName("home_default_tab")
	private String homeDefaultTab;

	@SerializedName("store_banner_required")
	private boolean storeBannerRequired;

	@SerializedName("download_model")
	private DownloadModel downloadModel;

	@SerializedName("azan_chart_id")
	private String azanChartId;

	@SerializedName("consent")
	private ConsentDTO consentDTO;

	@SerializedName("digital_authentication")
	private DigitalAuthenticationDTO digitalAuthenticationDTO;

	@SerializedName("app_locale")
	private LinkedTreeMap<String, Integer> appLocale;

	public void setDynamicShufflegroupid(String dynamicShufflegroupid){
		this.dynamicShufflegroupid = dynamicShufflegroupid;
	}

	public String getDynamicShufflegroupid(){
		return dynamicShufflegroupid;
	}

	public void setBatchApiEndpointRequired(String batchApiEndpointRequired){
		this.batchApiEndpointRequired = batchApiEndpointRequired;
	}

	public String getBatchApiEndpointRequired(){
		return batchApiEndpointRequired;
	}

	public void setDynamicChartgroupid(String dynamicChartgroupid){
		this.dynamicChartgroupid = dynamicChartgroupid;
	}

	public String getDynamicChartgroupid(){
		return dynamicChartgroupid;
	}

	public void setStoreindex(Storeindex storeindex){
		this.storeindex = storeindex;
	}

	public Storeindex getStoreindex(){
		return storeindex;
	}

	public void setCardindex(Cardindex cardindex){
		this.cardindex = cardindex;
	}

	public Cardindex getCardindex(){
		return cardindex;
	}

	public String getHome_chartgroup() {
		return home_chartgroup;
	}

	public void setHome_chartgroup(String home_chartgroup) {
		this.home_chartgroup = home_chartgroup;
	}

	public String getBestValuePack() {
		return bestValuePack;
	}

	public void setBestValuePack(String bestValuePack) {
		this.bestValuePack = bestValuePack;
	}

	public String getHomeTrendingChart() {
		return homeTrendingChart;
	}

	public void setHomeTrendingChart(String homeTrendingChart) {
		this.homeTrendingChart = homeTrendingChart;
	}

	public String getStoreChartId() {
		return storeChartId;
	}

	public void setStoreChartId(String storeChartId) {
		this.storeChartId = storeChartId;
	}

	public String getHomeDefaultTab() {
		return homeDefaultTab;
	}

	public void setHomeDefaultTab(String homeDefaultTab) {
		this.homeDefaultTab = homeDefaultTab;
	}

	public boolean isStoreBannerRequired() {
		return storeBannerRequired;
	}

	public void setStoreBannerRequired(boolean storeBannerRequired) {
		this.storeBannerRequired = storeBannerRequired;
	}

	public DownloadModel getDownloadModel() {
		return downloadModel;
	}

	public void setDownloadModel(DownloadModel downloadModel) {
		this.downloadModel = downloadModel;
	}

	public String getAzanChartId() {
		return azanChartId;
	}

	public void setAzanChartId(String azanChartId) {
		this.azanChartId = azanChartId;
	}

	public ConsentDTO getConsentDTO() {
		return consentDTO;
	}

	public void setConsentDTO(ConsentDTO consentDTO) {
		this.consentDTO = consentDTO;
	}

	public DigitalAuthenticationDTO getDigitalAuthenticationDTO() {
		return digitalAuthenticationDTO;
	}

	public void setDigitalAuthenticationDTO(DigitalAuthenticationDTO digitalAuthenticationDTO) {
		this.digitalAuthenticationDTO = digitalAuthenticationDTO;
	}

	public LinkedTreeMap<String, Integer> getAppLocale() {
		return appLocale;
	}

	public void setAppLocale(LinkedTreeMap<String, Integer> appLocale) {
		this.appLocale = appLocale;
	}

	@Override
	public String toString(){
		return
				"Baseline2DTO{" +
						"dynamic_shufflegroupid = '" + dynamicShufflegroupid + '\'' +
						",batch_api_endpoint_required = '" + batchApiEndpointRequired + '\'' +
						",dynamic_chartgroupid = '" + dynamicChartgroupid + '\'' +
						",storeindex = '" + storeindex + '\'' +
						",cardindex = '" + cardindex + '\'' +
						"}";
	}

}