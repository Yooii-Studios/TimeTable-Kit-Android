package com.sulga.yooiitable.sharetable;

import java.util.*;

public class UploadInfoObject {
	
	private String uploadedKey;
	private int downloadedCount;
	private ArrayList<String> downloadedNames;
	public UploadInfoObject(String uploadedKey, 
			ArrayList<String> downloadedNames, 
			int downloadedCount) {
		// TODO Auto-generated constructor stub
		this.uploadedKey = uploadedKey;
		this.downloadedNames = downloadedNames;
		this.downloadedCount = downloadedCount;
	}
	public String getUploadedKey(){
		return uploadedKey;
	}
	
	public ArrayList<String> getDownloadedNames(){
		return downloadedNames;
	}
	public int getDownloadedCount(){
		return downloadedCount;
	}
}
