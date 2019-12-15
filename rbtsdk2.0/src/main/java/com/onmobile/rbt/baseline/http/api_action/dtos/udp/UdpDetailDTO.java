package com.onmobile.rbt.baseline.http.api_action.dtos.udp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class UdpDetailDTO implements Serializable {

	@SerializedName("extra_info")
	private String extraInfo;

	@SerializedName("pager")
	private Pager pager;

	@SerializedName("songs")
	private List<SongsItem> songs;

	@SerializedName("count")
	private int count;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("type")
	private String type;

	public void setExtraInfo(String extraInfo){
		this.extraInfo = extraInfo;
	}

	public String getExtraInfo(){
		return extraInfo;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public Pager getPager(){
		return pager;
	}

	public void setSongs(List<SongsItem> songs){
		this.songs = songs;
	}

	public List<SongsItem> getSongs(){
		return songs;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"UdpDetailDTO{" +
			"extra_info = '" + extraInfo + '\'' + 
			",pager = '" + pager + '\'' + 
			",songs = '" + songs + '\'' + 
			",count = '" + count + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}