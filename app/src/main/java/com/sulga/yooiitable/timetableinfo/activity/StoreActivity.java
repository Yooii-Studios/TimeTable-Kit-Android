package com.sulga.yooiitable.timetableinfo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.sulga.inappbilling.testapp.util.IabHelper;
import com.sulga.inappbilling.testapp.util.IabResult;
import com.sulga.inappbilling.testapp.util.Inventory;
import com.sulga.inappbilling.testapp.util.Purchase;
import com.sulga.inappbilling.testapp.util.SkuDetails;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.TimeTableApplication;
import com.sulga.yooiitable.constants.FlurryConstants;
import com.sulga.yooiitable.constants.InApp;
import com.sulga.yooiitable.constants.RequestCodes;
import com.sulga.yooiitable.data.TimetableDataManager;
import com.sulga.yooiitable.mylog.MyLog;
import com.sulga.yooiitable.utils.InAppBillingManager;
import com.yooiistudios.common.analytics.AnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {
	private final String TAG = "StoreActivity";

	private boolean isFullVersion;
	//	private boolean isThemePackPurchased;
	private IabHelper mHelper;
	//	private IInAppBillingService iap;

	//	private TextView mUnlockFullVersionText;
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
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.app_name));
		
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
		//		mBuyThemePackText = (TextView) findViewById(R.id.activity_store_buy_theme_pack_text);
		//		mBuyThemePackPriceText = (TextView) 
		//				findViewById(R.id.activity_store_buy_theme_pack_text_price);

		String base64EncodedPublicKey = 
				InApp.KEY1 + InApp.KEY2 + InApp.KEY3 + InApp.KEY4 + 
				InApp.KEY5 + InApp.KEY6 + InApp.KEY7 + InApp.KEY8;
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		isFullVersion = TimetableDataManager.getCurrentFullVersionState(this);
		dialog = new ProgressDialog(this);
		setWaitScreen(true);
		InAppBillingManager.queryInventory(
				StoreActivity.this,
				mHelper,
				mOnSetupFinishedListener,
				mGotInventoryListener);
		AnalyticsUtils.startAnalytics((TimeTableApplication) getApplication(), R.string.screen_store);
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	private String fullVersionPrice="";
	//	private String themePackPrice="";
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = 
			new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			MyLog.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) {
				setWaitScreen(false);
				return;
			}
			// Is it a failure?
			if (result.isFailure()) {
				MyLog.complain(
						StoreActivity.this, 
						TAG,
						"Failed to query inventory: " + result.getMessage());
				setWaitScreen(false);
				return;
			}
			MyLog.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Full version unlocked?
			Purchase fullVersionPurchase = 
					inventory.getPurchase(InApp.SKU_FULL_VERSION);
			SkuDetails skuDetails = inventory.getSkuDetails(InApp.SKU_FULL_VERSION);
			if(skuDetails != null){
				fullVersionPrice = 
						skuDetails.getPrice();
			}
			isFullVersion = (fullVersionPurchase != null
					&& verifyDeveloperPayload(fullVersionPurchase));
			if(fullVersionPurchase != null){
				int fullVersionPurchaseState = fullVersionPurchase.getPurchaseState();
				MyLog.d(TAG, "fullVersionPurchaseState : " + fullVersionPurchaseState);
				if(fullVersionPurchaseState == 1) {
					//Do cancelled purchase stuff here
					MyLog.d(TAG, "Full Version purchase cancelled");
					isFullVersion = false;
				}
				else if(fullVersionPurchaseState == 2) {
					MyLog.d(TAG, "Full Version purchase refunded");
					isFullVersion = false;
					//Do refunded purchase stuff here
				}
			}

			MyLog.d(TAG, "User unlocked " 
					+ (isFullVersion ? "Full Version" : "NOT Full Version"));

			//Theme pack unlocked?
			//			Purchase themePackPurchase = 
			//					inventory.getPurchase(InApp.SKU_THEME_PACK);
			//			isThemePackPurchased = ( themePackPurchase != null 
			//					&& verifyDeveloperPayload(fullVersionPurchase));
			//			if(themePackPurchase != null){
			//				int themePackPurchaseState = themePackPurchase.getPurchaseState();
			//				MyLog.d(TAG, "themePackPurchaseState : " + themePackPurchase.getPurchaseState());
			//				if(themePackPurchaseState == 1) {
			//					//Do cancelled purchase stuff here
			//					MyLog.d(TAG, "Theme pack purchase cancelled");
			//					isThemePackPurchased = false;
			//				}
			//				else if(themePackPurchaseState == 2) {
			//					MyLog.d(TAG, "Theme pack purchase refunded");
			//					isThemePackPurchased = false;
			//					//Do refunded purchase stuff here
			//				}
			//			}

			//			themePackPrice = 
			//					inventory.getSkuDetails(InApp.SKU_THEME_PACK).getPrice();
			//			MyLog.d(TAG, "User bought " 
			//					+ (isThemePackPurchased ? "Theme Pack" : "NO Theme Pack"));
			// Do we have the infinite gas plan?
			//            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
			//            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
			//                    verifyDeveloperPayload(infiniteGasPurchase));
			//            MyLog.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
			//                        + " infinite gas subscription.");
			//            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

			// Check for gas delivery -- if we own gas, we should fill up the tank immediately
			//            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
			//            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
			//                MyLog.d(TAG, "We have gas. Consuming it.");
			//                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
			//                return;
			//            }

			updateUi();
			setWaitScreen(false);
			MyLog.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		//payload : 식별 정보.
		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
		 * the same one that you sent when initiating the purchase.
		 *
		 * WARNING: Locally generating a random string when starting a purchase and
		 * verifying it here might seem like a good approach, but this will fail in the
		 * case where the user purchases an item on one device and then uses your app on
		 * a different device, because on the other device you will not have access to the
		 * random string you originally generated.
		 *
		 * So a good developer payload has these characteristics:
		 *
		 * 1. If two different users purchase an item, the payload is different between them,
		 *    so that one user's purchase can't be replayed to another user.
		 *
		 * 2. The payload must be such that you can verify it even when the app wasn't the
		 *    one who initiated the purchase flow (so that items purchased by the user on
		 *    one device work on other devices owned by the user).
		 *
		 * Using your own server to store and verify developer payloads across app
		 * installations is recommended.
		 */

		return true;
	}

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

	private IabHelper.OnIabSetupFinishedListener mOnSetupFinishedListener = 
			new IabHelper.OnIabSetupFinishedListener() {
		@Override
		public void onIabSetupFinished(IabResult result) {
			MyLog.d(TAG, "iabHelper Setup finished.");
			if (!result.isSuccess()) {
				// Oh noes, there was a problem.
				MyLog.complain(
						StoreActivity.this, 
						TAG, 
						"Problem setting up in-app billing: " + result);
				setWaitScreen(false);
				return;
			}
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;
			// IAB is fully set up. Now, let's get an inventory of stuff we own.
			Log.d(TAG, "Setup successful. Querying inventory.");
			
			final List<String> skuList = new ArrayList<String>();
//			skuList.add(InApp.SKU_TEST_PURCHASE);
//			skuList.add(InApp.SKU_TEST_CANCELED);
//			skuList.add(InApp.SKU_TEST_REFUND);
//			skuList.add(InApp.SKU_TEST_UNAVALIABLE);
			skuList.add(InApp.SKU_FULL_VERSION);
//			skuList.add(InApp.SKU_THEME_PACK);

			if (skuList != null) {
			    if (skuList.size() > 0) {
			        try {
			            mHelper.queryInventoryAsync(true, skuList, mGotInventoryListener);
			        } catch (Exception e) { 
//			            ACRA.getErrorReporter().handleException(e);
			        	e.printStackTrace();
			        }finally{
			        	setWaitScreen(false);
			        }
			    }
			}
			
//			iabHelper.queryInventoryAsync(mGotInventoryListener);
		}
	};
	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			clicked = false;
			// if we were disposed of in the meantime, quit.
			if (mHelper == null) return;

			String alreadyBought = getResources().getString(R.string.activity_store_already_unlocked_full);
			String inappUnable = getResources().getString(R.string.activity_store_unable_to_pay);
			String purchaseCancelled = getResources().getString(R.string.activity_store_purchase_cancelled);
			//			String unableBuy = getResources().getString(R.string.activity_store_purchase_unable);

			if (result.isFailure()) {

				if(result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED){
					//					MyLog.complain(StoreActivity.this, TAG, "이미 구매하신 상품입니다!");
					MyLog.complain(StoreActivity.this, TAG, alreadyBought);
				}else if(result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE){
					//					MyLog.complain(StoreActivity.this, TAG, "죄송합니다. 현재 상품 구매가 불가능합니다.");
					MyLog.complain(StoreActivity.this, TAG, inappUnable);
				}else if(result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_USER_CANCELED){
					//					MyLog.complain(StoreActivity.this, TAG, "결제가 취소되었습니다.");
					MyLog.complain(StoreActivity.this, TAG, purchaseCancelled);
				}else if(result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE){
					//					MyLog.complain(StoreActivity.this, TAG, "결제가 불가능합니다.");
					MyLog.complain(StoreActivity.this, TAG, inappUnable);
				}else if(result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED){
					//					MyLog.complain(StoreActivity.this, TAG, "결제가 취소되었습니다.");
					MyLog.complain(StoreActivity.this, TAG, purchaseCancelled);
				}else{
					MyLog.complain(StoreActivity.this, TAG, "Error purchasing: " + result);
				}
				setWaitScreen(false);
				return;
			}

			if (!verifyDeveloperPayload(purchase)) {
				MyLog.complain(StoreActivity.this, TAG, 
						"Error purchasing. Authenticity verification failed.");
				setWaitScreen(false);
				return;
			}

			MyLog.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(InApp.SKU_FULL_VERSION)) {
				// bought 1/4 tank of gas. So consume it.
				MyLog.d(TAG, "Unlocked Full Version!!");
				String thank = getResources().getString(R.string.activity_store_thanks_for_unlock);
				MyLog.alert(StoreActivity.this, TAG, thank);
				setWaitScreen(false);
				isFullVersion = true;
				TimetableDataManager.saveFullVersionState(StoreActivity.this, isFullVersion);
				//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
			//			else if (purchase.getSku().equals(InApp.SKU_THEME_PACK)) {
			//				// bought the premium upgrade!
			//				MyLog.d(TAG, "Purchased Theme pack");
			//				isThemePackPurchased = true;
			//				updateUi();
			//				setWaitScreen(false);
			//			}
			//            else if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
			//                // bought the infinite gas subscription
			//                Log.d(TAG, "Infinite gas subscription purchased.");
			//                alert("Thank you for subscribing to infinite gas!");
			//                mSubscribedToInfiniteGas = true;
			//                mTank = TANK_MAX;
			//                updateUi();
			//                setWaitScreen(false);
			//            }
		}
	};

	//	public void onTestBuyClicked(View arg0) {
	//		MyLog.d(TAG, "Test Buy Clicked; launching purchase flow for buy.");
	//		setWaitScreen(true);
	//		String payload = "";
	//
	//		mHelper.launchPurchaseFlow(this, InApp.SKU_TEST_PURCHASE, RequestCodes.IAB_REQUEST,
	//				mPurchaseFinishedListener, payload);
	//	}

	//	public void onTestRefundClicked(View arg0) {
	//		MyLog.d(TAG, "Refund button clicked; launching purchase flow for refund.");
	//		setWaitScreen(true);
	//		String payload = "";
	//
	////		mHelper.launchPurchaseFlow(this, InApp.SKU_TEST_REFUND, RequestCodes.IAB_REQUEST,
	////				mPurchaseFinishedListener, payload);
	//	}
	//
	//	public void onTestCancelClicked(View arg0) {
	//		MyLog.d(TAG, "Cancel button clicked; launching purchase flow for cancel.");
	//		setWaitScreen(true);
	//		String payload = "";
	//
	////		mHelper.launchPurchaseFlow(this, InApp.SKU_TEST_CANCELED, RequestCodes.IAB_REQUEST,
	////				mPurchaseFinishedListener, payload);
	//
	//	}
	//
	//	public void onTestUnAvaliableClicked(View arg0) {
	//		MyLog.d(TAG, "Unavaliable item button clicked; launching purchase flow for unavaliable item.");
	//		setWaitScreen(true);
	//
	//		String payload = "";
	//
	////		mHelper.launchPurchaseFlow(this, InApp.SKU_TEST_UNAVALIABLE, RequestCodes.IAB_REQUEST,
	////				mPurchaseFinishedListener, payload);
	//
	//	}

	boolean clicked = false;
	public void onUnlockFullVersionClicked(View arg0) {
		MyLog.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");


		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
		try{
			if(clicked == true){
				return;
			}
			String payload = "";
			mHelper.launchPurchaseFlow(this, InApp.SKU_FULL_VERSION, RequestCodes.IAB_REQUEST,
					mPurchaseFinishedListener, payload);
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
	//
	//	public void onBuyThemePackClicked(View arg0){
	//		MyLog.d(TAG, "Buy Theme pack clicked; launching purchase flow for buy.");
	//		setWaitScreen(true);
	//
	//		/* TODO: for security, generate your payload here for verification. See the comments on
	//		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
	//		 *        an empty string, but on a production app you should carefully generate this. */
	//		String payload = "";
	//
	////		mHelper.launchPurchaseFlow(this, InApp.SKU_THEME_PACK, RequestCodes.IAB_REQUEST,
	////				mPurchaseFinishedListener, payload);
	//	}

	ProgressDialog dialog;
	void setWaitScreen(boolean show){
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
		if (mHelper != null) {
			try{
				mHelper.dispose();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		mHelper = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		MyLog.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if (mHelper == null) {
			setWaitScreen(false);
			return;
		}

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			setWaitScreen(false);
			MyLog.d(TAG, "onActivityResult NOT handled by IABUtil.");
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			setWaitScreen(false);
			MyLog.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

	public void onStart(){
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryConstants.APP_KEY);
	}

	public void onStop(){
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
