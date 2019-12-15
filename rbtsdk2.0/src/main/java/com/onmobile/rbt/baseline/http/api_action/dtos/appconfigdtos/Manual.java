package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Manual implements Serializable {

	@SerializedName("contentchartid")
	private String contentchartid;

	public void setContentchartid(String contentchartid){
		this.contentchartid = contentchartid;
	}

	public String getContentchartid(){
		return contentchartid;
	}

	@Override
 	public String toString(){
		return 
			"Manual{" + 
			"contentchartid = '" + contentchartid + '\'' + 
			"}";
		}
}