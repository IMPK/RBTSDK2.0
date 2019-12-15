package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

public class Storeindex{

	@SerializedName("recommendation")
	private String recommendation;

	public void setRecommendation(String recommendation){
		this.recommendation = recommendation;
	}

	public String getRecommendation(){
		return recommendation;
	}

	@Override
 	public String toString(){
		return 
			"Storeindex{" + 
			"recommendation = '" + recommendation + '\'' + 
			"}";
		}
}