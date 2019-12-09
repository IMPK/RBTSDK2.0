package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

public class Cardindex{

	public static final int CARD_TRENDING = 1001;
	public static final int CARD_PROFILE = 1002;
	public static final int CARD_NAMETUNE = 1003;
	public static final int CARD_SHUFFLE = 1004;
	public static final int CARD_RECOMMENDATION = 1005;
	public static final int CARD_BANNER = 1006;
	public static final int CARD_AZAN = 1007;


	@SerializedName("trending")
	private String trending;

	@SerializedName("nametune")
	private String nametune;

	@SerializedName("profiletune")
	private String profiletune;

	@SerializedName("recommendation")
	private String recommendation;

	@SerializedName("shuffle")
	private String shuffle;

	@SerializedName("banner")
	private String banner;

	@SerializedName("azan")
	private String azan;

	public void setTrending(String trending){
		this.trending = trending;
	}

	public String getTrending(){
		return trending;
	}

	public void setNametune(String nametune){
		this.nametune = nametune;
	}

	public String getNametune(){
		return nametune;
	}

	public void setProfiletune(String profiletune){
		this.profiletune = profiletune;
	}

	public String getProfiletune(){
		return profiletune;
	}

	public void setRecommendation(String recommendation){
		this.recommendation = recommendation;
	}

	public String getRecommendation(){
		return recommendation;
	}

	public void setShuffle(String shuffle){
		this.shuffle = shuffle;
	}

	public String getShuffle(){
		return shuffle;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getAzan() {
		return azan;
	}

	public void setAzan(String azan) {
		this.azan = azan;
	}

	@Override
	public String toString(){
		return
				"Cardindex{" +
						"trending = '" + trending + '\'' +
						",nametune = '" + nametune + '\'' +
						",profiletune = '" + profiletune + '\'' +
						",recommendation = '" + recommendation + '\'' +
						",shuffle = '" + shuffle + '\'' +
						",banner = '" + banner + '\'' +
						"}";
	}
}