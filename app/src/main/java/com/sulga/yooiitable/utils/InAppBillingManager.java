package com.sulga.yooiitable.utils;

import java.util.*;

import android.content.*;
import android.util.*;

import com.sulga.inappbilling.testapp.util.*;
import com.sulga.inappbilling.testapp.util.IabHelper.QueryInventoryFinishedListener;
import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.data.*;
import com.sulga.yooiitable.mylog.*;

public class InAppBillingManager {
	private static final String TAG = "InAppBillingManager";
	
//	private static IabHelper mHelper;
	public static void queryInventory(
			final Context context,
			final IabHelper iabHelper,
			final QueryInventoryFinishedListener mGotInventoryListener){
		// compute your public key and store it in base64EncodedPublicKey
//		iabHelper = new IabHelper(context, base64EncodedPublicKey);
		//publish할때는 false로!!!!
		if(iabHelper == null)
			return;
		
		iabHelper.enableDebugLogging(true);

		MyLog.d(TAG, "Starting iabHelper setup.");
		iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				MyLog.d(TAG, "iabHelper Setup finished.");
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					MyLog.complain(
							context, 
							TAG, 
							"Problem setting up in-app billing: " + result);
					return;
				}
				// Have we been disposed of in the meantime? If so, quit.
				if (iabHelper == null) return;
				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				
				final List<String> skuList = new ArrayList<String>();
//				skuList.add(InApp.SKU_TEST_PURCHASE);
//				skuList.add(InApp.SKU_TEST_CANCELED);
//				skuList.add(InApp.SKU_TEST_REFUND);
//				skuList.add(InApp.SKU_TEST_UNAVALIABLE);
				skuList.add(InApp.SKU_FULL_VERSION);
//				skuList.add(InApp.SKU_THEME_PACK);

				if (skuList != null) {
				    if (skuList.size() > 0) {
				        try {
				            iabHelper.queryInventoryAsync(true, skuList, mGotInventoryListener);
				        } catch (Exception e) { 
//				            ACRA.getErrorReporter().handleException(e);
				        	e.printStackTrace();
				        }
				    }
				}
				
//				iabHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}
	
	public static void queryInventory(
			final Context context,
			final IabHelper iabHelper,
			final IabHelper.OnIabSetupFinishedListener onSetupFinishedListener,
			final QueryInventoryFinishedListener mGotInventoryListener){
		// compute your public key and store it in base64EncodedPublicKey
//		iabHelper = new IabHelper(context, base64EncodedPublicKey);
		//publish할때는 false로!!!!
		if(iabHelper == null)
			return;
		
		iabHelper.enableDebugLogging(true);

		MyLog.d(TAG, "Starting iabHelper setup.");
		iabHelper.startSetup(onSetupFinishedListener);
	}

	
	/**
	 * Automatically update and saves full version unlock state to sharedpreferences.
	 * if failed connection, do nothing and return.
	 * @param context
	 */
	public static void updateFullVersionState(final Context context,
			OnFullVersionStateUpdateFinishedListener listener){
		String base64EncodedPublicKey = 
				InApp.KEY1 + InApp.KEY2 + InApp.KEY3 + InApp.KEY4 + 
				InApp.KEY5 + InApp.KEY6 + InApp.KEY7 + InApp.KEY8;
		IabHelper mHelper = new IabHelper(context, base64EncodedPublicKey);
		queryInventory(context, mHelper, new MyQueryInventoryFinishedListener(context, listener));
	}
	
	private static class MyQueryInventoryFinishedListener implements QueryInventoryFinishedListener{
		private Context context;
		private OnFullVersionStateUpdateFinishedListener listener;
		MyQueryInventoryFinishedListener(Context context, OnFullVersionStateUpdateFinishedListener listener){
			this.context = context;
			this.listener = listener;
			if(listener == null){
				this.listener = emptyOnFullVersionUpdatedListener;
			}
		}
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			// TODO Auto-generated method stub
			if (result.isFailure()) {
				MyLog.d(TAG, "Query inventory was failed.");
				listener.onFullVersionStateUpdateFinished(false, false);
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
			MyLog.d(TAG, "fullVersionPuschase : " + fullVersionPurchase);
			
			boolean isFullVersion = (fullVersionPurchase != null);
//					&& verifyDeveloperPayload(fullVersionPurchase));
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
			listener.onFullVersionStateUpdateFinished(true, isFullVersion);
			TimetableDataManager.saveFullVersionState(context, isFullVersion);
			MyLog.d(TAG, "User unlocked " 
					+ (isFullVersion ? "Full Version" : "NOT Full Version"));
			MyLog.d(TAG, "Initial inventory query finished; enabling main UI.");

		}
		
	}
	
	public static interface OnFullVersionStateUpdateFinishedListener{
		/**
		 * 
		 * @param isSucceed : if retreiving data succeed true, failed false.
		 * @param isFullVersion : full version state. if retreiving data failed, automatically sets false. ignore it.
		 */
		public void onFullVersionStateUpdateFinished(boolean isSucceed, boolean isFullVersion);
	}
	
	private static OnFullVersionStateUpdateFinishedListener emptyOnFullVersionUpdatedListener = 
			new OnFullVersionStateUpdateFinishedListener(){	
		public void onFullVersionStateUpdateFinished(boolean isSucceed,
				boolean isFullVersion) {}};
//	
//	private static QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener(){
//		@Override
//		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//			// TODO Auto-generated method stub
//					}
//	};
	
}
