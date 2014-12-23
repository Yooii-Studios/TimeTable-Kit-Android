package com.sulga.yooiitable.utils;

import java.io.*;
import java.util.*;

import android.content.*;
import android.telephony.*;


public class Installation {
//	private static String sID = null;
//	private static final String INSTALLATION = "INSTALLATION";
//
//	public synchronized static String id(Context context) {
//		if (sID == null) {  
//			File installation = new File(context.getFilesDir(), INSTALLATION);
//			try {
//				if (!installation.exists())
//					writeInstallationFile(installation);
//				sID = readInstallationFile(installation);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//		return sID;
//	}
//
//	private static String readInstallationFile(File installation) throws IOException {
//		RandomAccessFile f = new RandomAccessFile(installation, "r");
//		byte[] bytes = new byte[(int) f.length()];
//		f.readFully(bytes);
//		f.close();
//		return new String(bytes);
//	}
//
//	private static void writeInstallationFile(File installation) throws IOException {
//		FileOutputStream out = new FileOutputStream(installation);
//		String id = getUUID();
//		out.write(id.getBytes());
//		out.close();
//	}


	public static String GetDevicesUUID(Context mContext){

		final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), 
				android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}

}

