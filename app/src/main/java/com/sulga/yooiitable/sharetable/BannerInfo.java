package com.sulga.yooiitable.sharetable;

public class BannerInfo {

	private int version;
	private String imageUrl;
	private String linkUrl;
	
	public BannerInfo(int version, String imageUrl, String linkUrl) {
		// TODO Auto-generated constructor stub
		this.version = version;
		this.imageUrl = imageUrl;
		this.linkUrl = linkUrl;
	}
	
	public int getVersion(){
		return version;
	}
	public String getImageUrl(){
		return imageUrl;
	}
	public String getLinkUrl(){
		return linkUrl;
	}

}
