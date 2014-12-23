package com.sulga.yooiitable.sharetable;

import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.*;
import org.apache.http.util.*;
import org.json.*;

import android.util.*;

import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.mylog.*;

public class TimeTableNetworkTool {
	private static final String TAG = "TimetableNetworkTool";
	public static final String APP_CODE = "timetablekit";

//	static private String URL_DOWNLOAD = "http://www.yooiistudios.com/data/connector/downloadfile.php";
//	static private String URL_UPLOAD = "http://www.yooiistudios.com/data/connector/uploadfile.php";
//	static private String URL_UPLOADINFO = "http://www.yooiistudios.com/data/connector/getuploadinfo.php";
//	static private String URL_BANNERINFO = "http://www.yooiistudios.com/data/connector/getbannerinfo.php";
	
	static private String URL_DOWNLOAD = "http://www.yooiisoft.com/data/connector/downloadfile.php";
	static private String URL_UPLOAD = "http://www.yooiisoft.com/data/connector/uploadfile.php";
	static private String URL_UPLOADINFO = "http://www.yooiisoft.com/data/connector/getuploadinfo.php";
	static private String URL_BANNERINFO = "http://www.yooiisoft.com/data/connector/getbannerinfo.php";
	

	public static class DownloadTimetableResult{
		public DownloadTimetableResult(byte[] ret, int resultCode){
			this.ret = ret;
			this.resultCode = resultCode;
		}
		public byte[] ret = null;
		public int resultCode = 1;
	}
	
	/**
	 * 시간표 다운로드
	 * @param token 
	 * 받고자 하는 시간표에 대한 키 값
	 * @param uuid
	 * 디바이스 uuid
	 * @return
	 * DownloadTimetableResult, return is NEVER null.
	 * DownloadTimetableResult.ret will be null if network problem occurred.
	 */
	static public DownloadTimetableResult downloadTable(String token, String uuid, String name) {
		byte[] ret = null;
		int resultCode = ConnectorConstants.RESULT_FAILED;
		DownloadTimetableResult res = 
				new DownloadTimetableResult(ret, resultCode);
		try {
			JSONObject input = new JSONObject();
			JSONObject result = null;
			input.put("auth", uuid);
			input.put("app", APP_CODE);
			input.put("name", name);
			input.put("token", token);
//			input.put("uuid", uuid);

			String s = TimeTableNetworkTool.doPost(URL_DOWNLOAD,
					input);
			if(s == null){
				return res;
			}
			result = new JSONObject(s);
			resultCode = result.getInt("result");
			res.resultCode = resultCode;
			if(resultCode == ConnectorConstants.RESULT_SUCCESS){
				String dataString = result.getString("data");
				ret = Base64.decode(dataString, Base64.NO_WRAP);
			}
			if(ret == null){
				MyLog.d(TAG, "downloaded byte null");
				return res;
			}
			if(	ret.length == 0 ){
				resultCode = ConnectorConstants.RESULT_NULL_DATA_DOWNLOADED;
			}
			
			res = new DownloadTimetableResult(ret, resultCode);
			String retString = "";
			MyLog.d(TAG, retString + ", resultCode : " + resultCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 시간표 업로드
	 * @param token
	 * 시간표에 대한 키 값
	 * @param uuid
	 * 기기의 uuid
	 * @param data
	 * 보내고자 하는 시간표 데이터 
	 * @return
	 * 네트워크 결과 코드 
	 */
	static public int uploadTable(String token, String uuid, String name, byte[] data) {
		int resultCode = ConnectorConstants.RESULT_FAILED;
		try {
			JSONObject input = new JSONObject();
			JSONObject result = null;
			
			String dataString = Base64.encodeToString(data, Base64.NO_WRAP);
			input.put("auth", uuid);
			input.put("app", APP_CODE);
			input.put("name", name);
			input.put("token", token);
			input.put("data", dataString);

			Log.e("dbg", "sending uuid:" + uuid);
			
			String s = TimeTableNetworkTool.doPost(URL_UPLOAD, input);
			if(s == null){
				return resultCode;
			}
			result = new JSONObject(s);
			resultCode = result.getInt("result");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return resultCode;
	}
	
	/**
	 * uuid 유저의 업로드 상태를 받아온다.
	 * "result" : result code(int)
	 * "msg" : result String
	 * "list" 
	 * [
	 * 		{
	 * 			"count" : 다른 유저들이 파일을 받은 횟수
	 * 			"date" : 파일을 업로드한 날짜
	 * 		}
	 * ]
	 * @param uuid
	 * @return JSONObject
	 */
	static public ConnectorState getUploadInfo(String uuid, String name, boolean isFullVersion){
		int resultCode = ConnectorConstants.RESULT_FAILED;
		try {
			JSONObject input = new JSONObject();
			JSONObject result = null;

			input.put("auth", uuid);
			input.put("app", APP_CODE);
			input.put("name", name);

			Log.e(TAG, "sending uuid:" + uuid + ", name : " + name);

			String s = TimeTableNetworkTool.doPost(URL_UPLOADINFO, input);
			if(s == null){
				return new ConnectorState(resultCode, null, -1, -1, -1, -1, -1, -1);
			}
			result = new JSONObject(s);

			resultCode = result.getInt("result");
			if(resultCode == ConnectorConstants.RESULT_SUCCESS){
				int availd = result.getInt("availd");
				int availu = result.getInt("availu");
				
				int maxDownloadP = result.getInt("maxdownload_p");
				int maxUploadP = result.getInt("maxupload_p");
				int maxDownloadF = result.getInt("maxdownload_f");
				int maxUploadF = result.getInt("maxupload_f");
				MyLog.d(TAG, "maxDownloadP : " + maxDownloadP + ", maxUploadP : " + maxUploadP + 
						"maxDownloadF : " + maxDownloadF + ", maxUploadF : " + maxUploadF +
						"availd : " + availd + ", availu : " + availu);

				//uploaded info list
				JSONArray list = result.getJSONArray("list");
				MyLog.d("getUploadInfo", "json list size : " + list.length());
				ArrayList<UploadInfoObject> upInfos = new ArrayList<UploadInfoObject>();
				for(int i = 0; i < list.length() ; i++){
					JSONObject obj = list.getJSONObject(i);
					String uploadedKey = obj.getString("token");
					int downloadedCount = obj.getInt("count");
					JSONArray receivers = obj.getJSONArray("receivers");
					ArrayList<String> receiverNames = new ArrayList<String>();
					for(int j = 0; j < receivers.length() ; j++){
						String userName = receivers.getJSONObject(j).getString("name");
						receiverNames.add(userName);
					}
					MyLog.d(TAG, receivers.toString());
					MyLog.d(TAG, receiverNames.toString() + ", " + receiverNames.size());
					upInfos.add(new UploadInfoObject(uploadedKey, receiverNames, downloadedCount));
				}
				return new ConnectorState(resultCode, upInfos, availd, availu, maxDownloadP, maxUploadP, maxDownloadF, maxUploadF);
			}else{
				return new ConnectorState(resultCode, null, -1, -1, -1, -1, -1, -1);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ConnectorState(resultCode, null, -1, -1, -1, -1, -1, -1);
	}
	
//	/**
//	 * 앱 실행시에 아이피를 서버에 전송해 국가정보를 받아와서 업로드/다운로드카운트 타이밍을 설정한다.
//	 * @param token
//	 * 시간표에 대한 키 값
//	 * @param uuid
//	 * 기기의 uuid
//	 * @return
//	 * 네트워크 결과 코드 
//	 */
//	static public int setupDevice(String uuid) {
//		int resultCode = ConnectorConstants.RESULT_FAILED;
//		try {
//			JSONObject input = new JSONObject();
//			JSONObject result = null;
//			
//			input.put("auth", "yooii");
//			input.put("uuid", uuid);
//
//			Log.e("dbg", "sending uuid:" + uuid);
//			
//			String s = TimeTableNetworkTool.doPost(URL_SETUP_DEVICE, input);
//			if(s == null){
//				return resultCode;
//			}
//			result = new JSONObject(s);
//			resultCode = result.getInt("result");
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		return resultCode;
//	}
//	
	static public BannerInfo getBannerInfo(String uuid, String name){
		int resultCode = ConnectorConstants.RESULT_FAILED;
		try {
			JSONObject input = new JSONObject();
			JSONObject result = null;
			
			input.put("auth", uuid);
			input.put("app", APP_CODE);
			input.put("name", name);
			
			String s = TimeTableNetworkTool.doPost(URL_BANNERINFO, input);
			if(s == null){
				return null;
			}
			result = new JSONObject(s);
			resultCode = result.getInt("result");
			if(resultCode == ConnectorConstants.RESULT_SUCCESS){
				int version = result.getInt("version");
				String imageUrl = result.getString("image_url");
				String linkUrl = result.getString("link_url");
				
				return new BannerInfo(version, imageUrl, linkUrl);
			}else{
				return null;
			}			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * HTTP Post를 이용한 AJAX 데이터 통신
	 * @param url
	 * 주소
	 * @param input
	 * 입력 json 데이터
	 * @return
	 * 출력 json string
	 */
	static private String doPost(String url, JSONObject input) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			post.setEntity(new StringEntity(input.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);

			post.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");

			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
