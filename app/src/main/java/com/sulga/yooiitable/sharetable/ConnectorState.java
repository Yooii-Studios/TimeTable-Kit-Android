package com.sulga.yooiitable.sharetable;

import java.util.*;

import com.sulga.yooiitable.constants.*;

public class ConnectorState {
	private static volatile ConnectorState cs;
	/**
	 * download avail count, reset 12:00PM(NOT UTC TIME! IP's Country Time.)
	 * 10 and decrement to 0.
	 */
	private int availd;
	/**
	 * Upload avail count, reset 12:00PM(NOT UTC TIME! IP's Country Time.)
	 * 10 and decrement to 0.
	 */
	private int availu;
	
	private int maxDownload_p;
	private int maxUpload_p;
	private int maxDownload_f;
	private int maxUpload_f;
	
	private int resultCode;
	private ArrayList<UploadInfoObject> upInfos;
		
	public ConnectorState(int resultCode, ArrayList<UploadInfoObject> upInfos, 
			int availD, int availU, int maxDownload_p, int maxUpload_p, int maxDownload_f, int maxUpload_f){
		this.resultCode = resultCode;
		this.upInfos = upInfos;
		this.availd = availD;
		this.availu = availU;
		this.maxDownload_p = maxDownload_p;
		this.maxUpload_p = maxUpload_p;
		this.maxDownload_f = maxDownload_f;
		this.maxUpload_f = maxUpload_f;
	}
	
	public void setTodayDownloadAvailCount(int availD){
		this.availd = availD;
	}
	public void setTodayUploadAvailCount(int availU){
		this.availu = availU;
	}
	public int getTodayDownloadAvailCount(){
		return availd;
	}
	
	public int getTodayUploadAvailCount(){
		return availu;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<UploadInfoObject> getUploadInfoArray(){
		return upInfos;
	}
	
	public String toString(){
		return resultCode == ConnectorConstants.RESULT_SUCCESS ?
				"resultCode : " + resultCode + ", AvailU : " + availu + ", AvailD : " + availd : 
					"resultCode : " + resultCode;
	}

	public int getMaxUploadP() {
		return maxUpload_p;
	}

	public void setMaxUploadP(int uCount) {
		this.maxUpload_p = uCount;
	}

	public int getMaxDownloadP() {
		return maxDownload_p;
	}

	public void setMaxDownloadP(int dCount) {
		this.maxDownload_p = dCount;
	}

	public int getMaxDownloadF() {
		return maxDownload_f;
	}

	public void setMaxDownloadF(int maxDownload_f) {
		this.maxDownload_f = maxDownload_f;
	}

	public int getMaxUploadF() {
		return maxUpload_f;
	}

	public void setMaxUploadF(int maxUpload_f) {
		this.maxUpload_f = maxUpload_f;
	}
}
