package com.sulga.yooiitable.utils;

import android.accounts.*;
import android.annotation.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;

import com.sulga.yooiitable.mylog.*;

/**
 * This class uses the AccountManager to get the primary email address of the
 * current user.
 */
public class UserNameFactory {

	private static final String TAG = "UserNameFactory";
	/**
	 * 빌드가 ICS이상이면 유저이름을, 아닐 경우에는 디바이스 이름을 리턴.
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			String userName = getUserNameOnIcsDevice(context);
			MyLog.d(TAG, userName);
			return userName;
		}else{
			String deviceName = getDeviceName();
			MyLog.d(TAG, deviceName);
			return deviceName;
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static String getUserNameOnIcsDevice(Context context)
	{
		final ContentResolver content = context.getContentResolver();
		final Cursor cursor = content.query(
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,
				ContactsContract.Contacts.Data.MIMETYPE + "=?",
						new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE},
						ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
				);

		String deviceUserName = null;

		while (cursor.moveToNext())
		{
			if (cursor.getString(ProfileQuery.GIVEN_NAME) == null && 
					cursor.getString(ProfileQuery.FAMILY_NAME) == null)
			{
				continue;
			}
			String givenName = cursor.getString(ProfileQuery.GIVEN_NAME);
			String familyName = cursor.getString(ProfileQuery.FAMILY_NAME);
			if(givenName == null){
				givenName = "";
			}
			if(familyName == null){
				familyName = "";
			}
			deviceUserName = givenName +  
					" " + familyName;
		}

		cursor.close();

		if (deviceUserName == null)
		{
			return "Unknown";
		}
		else
		{
			return deviceUserName;
		}
	}

	private interface ProfileQuery
	{
		/**
		 * The set of columns to extract from the profile query results
		 */
		String[] PROJECTION = {
				ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
				ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
		};

		/**
		 * Column index for the family name in the profile query results
		 */
		int FAMILY_NAME = 0;
		/**
		 * Column index for the given name in the profile query results
		 */
		int GIVEN_NAME = 1;
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}


	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	} 


	public static String getEmailName(Context context) {
		AccountManager accountManager = AccountManager.get(context); 
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			MyLog.d("UserEmailFactory", "Name : " + account.name);
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];      
		} else {
			account = null;
		}
		return account;
	}
}