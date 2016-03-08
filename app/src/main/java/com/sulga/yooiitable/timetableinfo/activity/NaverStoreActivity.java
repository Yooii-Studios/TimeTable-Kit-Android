package com.sulga.yooiitable.timetableinfo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.nhn.android.appstore.iap.payment.NIAPActivity;
import com.nhn.android.appstore.iap.result.NIAPResult;
import com.nhn.android.appstore.iap.util.AppstoreSecurity;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.constants.NaverInApp;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NaverStoreActivity extends NIAPActivity {
	private final String TAG = "NaverStoreActivity";

	private final String APP_CODE = "TLZQ297331391948071751";
	private final String IAP_KEY = "dktHVjkNr5";	//정산정보 등록 후 가능.
	private final String fullVersionProductCode = "1000005025";

	private boolean isFullVersion;

	private TextView mUnlockFullVersionPriceText;
	private TextView unlockTextA;
	private TextView unlockTextB;
	private String buyStringA;
	private String buyStringB;
	private String alreadyBoughtStringA;
	private String alreadyBoughtStringB;


	//	private TextView mBuyThemePackText;
	//	private TextView mBuyThemePackPriceText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// ...
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_redesign);

		//		mUnlockFullVersionText = (TextView) findViewById(R.id.activity_store_unlock_full_version_text);
		mUnlockFullVersionPriceText = (TextView) 
				findViewById(R.id.activity_store_redesign_text_price);
		unlockTextA = (TextView)findViewById(R.id.activity_store_redesign_text_buyfull_a);
		unlockTextB = (TextView)findViewById(R.id.activity_store_redesign_text_buyfull_b);

		buyStringA = getString(R.string.buy_full_version);
		buyStringB = getString(R.string.buy_full_version_explain);
		alreadyBoughtStringA = getString(R.string.already_full_version);
		alreadyBoughtStringB = getString(R.string.already_full_version_explain);
		if(TimetableDataManager.getCurrentFullVersionState(this) == true){
			unlockTextA.setText(alreadyBoughtStringA);
			unlockTextB.setText(alreadyBoughtStringB);
		}else{
			unlockTextA.setText(buyStringA);
			unlockTextB.setText(buyStringB);
		}
		dialog = new ProgressDialog(this);
		isFullVersion = TimetableDataManager.getCurrentFullVersionState(this);
		setWaitScreen(true);
		initialize(APP_CODE, IAP_KEY);
		getProductInfo();
		getReceiptState();
	}

	private void getProductInfo(){
		ArrayList<String> productCodes = new ArrayList<String>(); 
		productCodes.add(fullVersionProductCode); 
		/**
		 * 상품 목록 조회
		 * 파라미터 : 상품 코드 리스트 */
		requestProductInfos(productCodes);
	}

	private void getReceiptState(){
		String paymentSeq = TimetableDataManager.getNaverPaymentSeq(this);
		if(paymentSeq != null && !paymentSeq.equals("")){
			requestReceipt(paymentSeq);
		}
	}
	
	String fullVersionPrice = "1000";
	private void updateUi(){
		if(isFullVersion == true){
			mUnlockFullVersionPriceText.setText("Sold Out!");
			unlockTextA.setText(alreadyBoughtStringA);
			unlockTextB.setText(alreadyBoughtStringB);
			
		}else{
			mUnlockFullVersionPriceText.setText(fullVersionPrice);
			unlockTextA.setText(buyStringA);
			unlockTextB.setText(buyStringB);
		}
		//		if(isThemePackPurchased == true){
		//			mBuyThemePackText.setText("Already bought theme pack...");
		//		}
		//		mBuyThemePackPriceText.setText(themePackPrice);
	}

	boolean clicked = false;
	public void onUnlockFullVersionClicked(View arg0) {
		if(isFullVersion == true){
			return;
		}
		MyLog.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
		try{
			if(clicked == true){
				return;
			}
			//process unlock
			requestPayment(fullVersionProductCode, 1000, "extra");
			clicked = true;
			setWaitScreen(true);

		}catch(IllegalStateException e){
			String warn = getString(R.string.activity_store_common_error);
			Toast.makeText(this, warn, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}catch(Exception e){
			String warn = getString(R.string.activity_store_common_error);
			Toast.makeText(this, warn, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}finally{
			setWaitScreen(false);
			clicked = false;
		}

	}

	ProgressDialog dialog;
	void setWaitScreen(boolean show){
		if(dialog == null){
			dialog = new ProgressDialog(this);
		}
		if(show == true){
			String title = getResources().getString(R.string.loading);
			String wait = getResources().getString(R.string.pleaseWaitWhileLoading);
			dialog.setTitle(title);
			dialog.setMessage(wait);
			//			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			if(dialog.isShowing() == true){
				dialog.dismiss();
				dialog.show();
			}
		}else{
			if(dialog.isShowing() == true)
				dialog.dismiss();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onStart(){
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryConstants.APP_KEY);
	}

	public void onStop(){
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	public void onError(NIAPResult result) {
		// TODO Auto-generated method stub
		printErrorResult(result);
	}

	private String base64PublicKey;
	@Override
	public void onPaymentCompleted(NIAPResult result) {
		printResult("구매해주셔서 감사합니다!", result);
		Log.i("security", "결제 성공 : " + result.getResult()); 
		try {
			base64PublicKey = NaverInApp.KEY1 + 
					NaverInApp.KEY2 + 
					NaverInApp.KEY3 +
					NaverInApp.KEY4 + 
					NaverInApp.KEY5 + 
					NaverInApp.KEY6;
			// 아래와 같은 방법으로 signature 데이터를 가져온다.
			JSONObject extraValueJson = new JSONObject(result.getExtraValue()); 
			String signature = extraValueJson.getString("signature");
			// SDK 안에서 제공되는 유틸 클래스인 AppstoreSecurity를 사용하여 서명 인증한다.
			boolean isSuccess = AppstoreSecurity.verify(base64PublicKey, 
					result.getResult(), 
					signature);
			
			//영수증 일련번호를 받는다.
			JSONObject resultJSON = new JSONObject(result.getResult());
			JSONObject receiptJSON = resultJSON.getJSONObject("receipt");
			String paymentSeq = receiptJSON.getString("paymentSeq");
			MyLog.d(TAG, "onPaymentCompleted, paymentSeq : " + paymentSeq);
			if(isSuccess == true){
				TimetableDataManager.saveFullVersionState(this, true);
				TimetableDataManager.saveNaverPaymentSeq(this, paymentSeq);
				isFullVersion = true;
			}else{
				TimetableDataManager.saveFullVersionState(this, false);
			}
			updateUi();
			Log.i("security", "인증 결과 : " + isSuccess); 
		}catch (JSONException e) {
			Log.e("security", "인증 정보를 파싱하는 도중 오류 발생", e);
		}
	}

	@Override
	public void onReceivedProductInfos(NIAPResult result) {
		//		printResult("상품 목록 조회", result);
		//		MyLog.d("NaverStoreActivity", result.getResult());
		if (result != null) {
			try{	
				JSONObject resultJson = new JSONObject(result.getResult());
				JSONArray valid = resultJson.getJSONArray("valid");
				JSONArray invalid = resultJson.getJSONArray("invalid");
				MyLog.d("NaverStoreActivity", "valid length : " + valid.length() 
						+ ", invalid length : " + invalid.length());
				JSONObject validObj = null;
				if(valid.length() > 0){
					validObj = valid.getJSONObject(0);
				}else if(valid.length() == 0){
					Toast.makeText(this, "현재 상품이 구매 불가능한 상태입니다!", Toast.LENGTH_LONG).show();
					setWaitScreen(false);
					return;
				}
				int price = validObj.getInt("sellPrice");
				fullVersionPrice = Integer.toString(price) + "₩";
				updateUi();
				setWaitScreen(false);

				MyLog.d("NaverStoreActivity", "validObject : " + validObj);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "상품 정보 불러오기 실패!", Toast.LENGTH_LONG).show();
				setWaitScreen(false);
			}
		}else{
			Toast.makeText(this, "상품 정보 불러오기 실패!", Toast.LENGTH_LONG).show();
			setWaitScreen(false);
		}
	}

	@Override
	public void onReceivedPaymentSeq(NIAPResult result) {
		printResult("결제키 발급", result);
	}

	@Override
	public void onReceivedReceipt(NIAPResult result) {
//		printResult("결제 내역 확인", result);
		String resultString = result.getResult();
		try {
			JSONObject resultObj = new JSONObject(resultString);
			JSONObject receiptObj = resultObj.getJSONObject("receipt");
			String approvalStatus = receiptObj.getString("approvalStatus");
			if(approvalStatus.equals("APPROVED")){
				TimetableDataManager.saveFullVersionState(this, true);
				isFullVersion = true;
			}else if(approvalStatus.equals("CANCELED")){
				TimetableDataManager.saveFullVersionState(this, false);
				isFullVersion = false;
			}
			updateUi();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onPaymentCanceled(final NIAPResult result) {
		printResult("결제 취소", result);
	}

	/**
	 * @param message
	 * @param result
	 * 결제 결과 Toast 출력
	 */
	private void printResult(final String message, final NIAPResult result) {
		if (result != null) {
			//			String resultMessage = "[ " + message + " ]";
			//			String requestDesc = "요청타입 : " + result.getRequestType().getDesc();
			//			String resultDetail = "결과 : " + result.getResult();
			//			Log.i("NIAP_SAMPLE", resultMessage + "\n" + requestDesc + "\n" + resultDetail);
			Toast.makeText(NaverStoreActivity.this, message + "\n" + result.getResult(),
					Toast.LENGTH_SHORT).show();
			MyLog.d(TAG, message + "\n" + result.getResult());
		}
	}

	/**
	 * @param result : 결제 관련 처리 결과
	 * 결제 중 발생한 오류에 대한 결과 Toast 출력
	 */
	private void printErrorResult(final NIAPResult result) {
		String requestDesc = "";//요청타입
		String errorCode = "";//오류코드
		String errorMessage = "";//오류설명
		if (result != null) {
			requestDesc = result.getRequestType().getDesc();
			try {
				JSONObject resultJson = new JSONObject(result.getResult());
				errorCode = resultJson.getString("code");
				errorMessage = resultJson.getString("message");
			} catch (Exception e) {
				errorCode = "ERS999";
				errorMessage = "알 수 없는 오류가 발생하였습니다.";
			}
			Toast.makeText(NaverStoreActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
		}
		Log.i("NIAP_SAMPLE", "요청 타입 : " + requestDesc + ",\n오류 코드 : " + errorCode + ",\n오류 설명 " + errorMessage);
	}
}
