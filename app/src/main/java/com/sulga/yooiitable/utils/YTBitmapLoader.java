package com.sulga.yooiitable.utils;


import java.io.*;
import java.net.*;

import android.content.*;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;

import com.sulga.yooiitable.mylog.*;

public class YTBitmapLoader {
	//SKBitmapLoader에서 유이테이블은 여러 페이지의 테마가 설정되도록 이미지 갯수가 여러개이므로
	//그에 맞춰 자잘한 수정을 가함.

	private static final String TEMP_PHOTO_FILE = "bg_"; // 임시 저장파일
	//private static final String TEMP_PHOTO_FILE_LANDSCAPE = "backgroundImage_Landscape.jpg"; // Landscape 용 임시 저장파일

	/** 임시 저장 파일의 경로를 만들어 반환. */
	public static Uri createPortraitCroppedImage(Context context, Uri originalPortImageUri, long timetableId) {
		return Uri.fromFile(createPortraitTempFile(context, originalPortImageUri, timetableId ));
	}

	public static Uri getPortraitCroppedImageUri(Context context, long timetableId){
		return Uri.fromFile(getPortraitCroppedImageFile(context, timetableId));
	}

	public static void saveTimetableBackgroundBitmap(Context context, Bitmap bitmap, long timetableId){
		try {
			saveBitmapToUri(context, getPortraitCroppedImageUri(context, timetableId), bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public static Uri getLandscapeImageUri(int timetableIndex) {
		return Uri.fromFile(getLandscapeTempFile(timetableIndex));
	}

	 */	/** SD카드가 마운트 되어 있는지 확인 */
	public static boolean isSDCARDMOUNTED() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

	/** 외장메모리에 임시 이미지 파일을 생성하여 원본을 복사해 붙여넣고, 그 파일의 경로를 반환 */
	public static File createPortraitTempFile(
			Context context, Uri originalPortImageUri, long timetableId) {
		if (isSDCARDMOUNTED()) {

			String fileName = getPhotoFileNameFromTimetableIndex(context, timetableId);
			MyLog.d("getPortraitTempFile", fileName);
			/*File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리
					fileName												// 경로
					);*/
			/*
			try {
				f.createNewFile(); // 외장메모리에 temp.jpg 파일 생성
			} catch (IOException e) {
			}*/
			File original_file = getImageFile(context, originalPortImageUri);
			Uri croppedFileUri = getCroppedFileUri(fileName);
			File copy_file = new File(croppedFileUri.getPath());
			copyFile(original_file, copy_file);

			//			mImageCaptureUri = createSaveCropFile();
			//            File cpoy_file = new File(mImageCaptureUri.getPath());

			return copy_file;
		} else
			return null;
	}

	public static File getPortraitCroppedImageFile(Context context, long timetableId){
		if (isSDCARDMOUNTED()) {
			String fileName = getPhotoFileNameFromTimetableIndex(context, timetableId);
			MyLog.d("getPortraitTempFile", fileName);			
			Uri croppedFileUri = getCroppedFileUri(fileName);
			File result_file = new File(croppedFileUri.getPath());
			return result_file;
		} else
			return null;
	}
	/**
	 * Crop된 이미지가 저장될 파일을 만든다.
	 * @return Uri
	 */
	private static Uri getCroppedFileUri(String fileName){
		Uri uri;
		// String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/YooiiTable/"+fileName));
		return uri;
	}

	/**
	 * 선택된 uri의 사진 Path를 가져온다.
	 * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
	 * @param uri
	 * @return
	 */
	private static File getImageFile(Context context, Uri uri) {
		//		Cursor cursor = null;
		//		File file = null;
		//		try { 
		//			String[] proj = { MediaColumns.DATA };
		//			cursor = context.getContentResolver().query(uri,  proj, null, null, null);
		//			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		//			cursor.moveToFirst();
		//			//path is null!!!!!
		//			String path = cursor.getString(column_index);
		//			file = new File(path);
		//
		//		} finally {
		//			if (cursor != null) {
		//				cursor.close();
		//			}
		//		}
		return new File(uri.getPath());
		//		String[] projection = { MediaColumns.DATA };
		//		if (uri == null) {
		//			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		//		}
		//
		//		Cursor mCursor = context.getContentResolver().query(uri, projection, null, null, 
		//				MediaColumns.DATE_MODIFIED + " desc");
		//		if(mCursor == null || mCursor.getCount() < 1) {
		//			return null; // no cursor or no record
		//		}
		//		int column_index = mCursor.getColumnIndexOrThrow(MediaColumns.DATA);
		//		mCursor.moveToFirst();
		//
		//		String path = mCursor.getString(column_index);
		//
		//		if (mCursor !=null ) {
		//			mCursor.close();
		//			mCursor = null;
		//		}
		//
		//		return new File(path);
	}

	/**
	 * 파일 복사
	 * @param srcFile : 복사할 File
	 * @param destFile : 복사될 File
	 * @return
	 */
	public static boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			} finally  {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Copy data from a source stream to destFile.
	 * Return true if succeed, return false if failed.
	 */
	private static boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}	/*
	public static File getLandscapeTempFile() {
		if (isSDCARDMOUNTED()) {
			File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리
																			// 경로
					TEMP_PHOTO_FILE_LANDSCAPE);
			try {
				f.createNewFile(); // 외장메모리에 temp.jpg 파일 생성
			} catch (IOException e) {
			}
			return f;
		} else
			return null;
	}

	 */	
	public static Bitmap loadAutoScaledBitmapFromUri(Context context, Uri uri) throws FileNotFoundException {
		Bitmap bitmap = null;

		DisplayMetrics metrics = new DisplayMetrics();
		// measure device size
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		display.getMetrics(metrics);

		float displayWidth = metrics.widthPixels;
		float displayHeight = metrics.heightPixels;
		//		 int oldWidth = display.getWidth();
		//		 int oldHeight = display.getHeight();
		//		 MyLog.d("YTBitmapLoader", "(old)width : " + oldWidth + ", height : " + oldHeight);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;

		InputStream in = context.getContentResolver().openInputStream(uri);
		bitmap = BitmapFactory.decodeStream(in, null, options);

		// measure rescale size that nearly match the device size
		// rescale value is even for less image loss  

		float widthScale = Math.round(options.outWidth / displayWidth);
		float heightScale = Math.round(options.outHeight / displayHeight);
		float scale = widthScale > heightScale ? widthScale : heightScale;
		MyLog.d("YTBitmapLoader", "From uri " 
				+ ", width : " + displayWidth + ", height : " + displayHeight
				+ ", options.outWidth : " + options.outWidth 
				+ ", options.outHeight : " + options.outHeight
				+ ", widthScale : " + widthScale + ", heightScale : " + heightScale
				+ ", scale : "  + scale);
		if(scale >= 8) {
			options.inSampleSize = 8;
		} else if(scale >= 6) {
			options.inSampleSize = 6;
		} else if(scale >= 4) {
			options.inSampleSize = 4;
		} else if(scale >= 2) {
			options.inSampleSize = 2;
		} else {
			options.inSampleSize = 1;
		}
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeStream(
				context.getContentResolver().openInputStream(uri),
				null, 
				options);
		return bitmap;
	}

	/**
	 * Not tested Enough, don't use this yet.
	 * @param context
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap loadAutoScaledBitmapFromFileName(Context context, 
			String fileName) 
					throws FileNotFoundException{
		File file = new File(
				Environment.getExternalStorageDirectory(), "/YooiiTable/"+fileName);
		if(file.exists() == false){
			throw new FileNotFoundException(
					"background image file doesn't exists : " + file.getPath());
		}
		DisplayMetrics metrics = new DisplayMetrics();
		// measure device size
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		display.getMetrics(metrics);

		float displayWidth = metrics.widthPixels;
		float displayHeight = metrics.heightPixels;
		//		 int oldWidth = display.getWidth();
		//		 int oldHeight = display.getHeight();
		//		 MyLog.d("YTBitmapLoader", "(old)width : " + oldWidth + ", height : " + oldHeight);

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		// measure rescale size that nearly match the device size
		// rescale value is even for less image loss  

		float widthScale = Math.round(options.outWidth / displayWidth);
		float heightScale = Math.round(options.outHeight / displayHeight);
		float scale = widthScale > heightScale ? widthScale : heightScale;
		MyLog.d("YTBitmapLoader", "From file " 
				+ ", width : " + displayWidth + ", height : " + displayHeight
				+ ", options.outWidth : " + options.outWidth 
				+ ", options.outHeight : " + options.outHeight
				+ ", widthScale : " + widthScale + ", heightScale : " + heightScale
				+ ", scale : "  + scale);

		if(scale >= 8) {
			options.inSampleSize = 8;
		} else if(scale >= 6) {
			options.inSampleSize = 6;
		} else if(scale >= 4) {
			options.inSampleSize = 4;
		} else if(scale >= 2) {
			options.inSampleSize = 2;
		} else {
			options.inSampleSize = 1;
		}

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}

	public static Bitmap loadAutoScaledBitmapFromResId(Context context, 
			int resId){

		DisplayMetrics metrics = new DisplayMetrics();
		// measure device size
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		display.getMetrics(metrics);

		float displayWidth = metrics.widthPixels;
		float displayHeight = metrics.heightPixels;
		//		 int oldWidth = display.getWidth();
		//		 int oldHeight = display.getHeight();
		//		 MyLog.d("YTBitmapLoader", "(old)width : " + oldWidth + ", height : " + oldHeight);

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), resId, options);
		// measure rescale size that nearly match the device size
		// rescale value is even for less image loss  

		float widthScale = Math.round(options.outWidth / displayWidth);
		float heightScale = Math.round(options.outHeight / displayHeight);
		float scale = widthScale > heightScale ? widthScale : heightScale;
		MyLog.d("YTBitmapLoader", "From resource Id " 
				+ ", width : " + displayWidth + ", height : " + displayHeight
				+ ", options.outWidth : " + options.outWidth 
				+ ", options.outHeight : " + options.outHeight
				+ ", widthScale : " + widthScale + ", heightScale : " + heightScale
				+ ", scale : "  + scale);
		if(scale >= 8) {
			options.inSampleSize = 8;
		} else if(scale >= 6) {
			options.inSampleSize = 6;
		} else if(scale >= 4) {
			options.inSampleSize = 4;
		} else if(scale >= 2) {
			options.inSampleSize = 2;
		} else {
			options.inSampleSize = 2;
		}
		options.inJustDecodeBounds = false;
		options.inDither = false;
		return BitmapFactory.decodeResource(context.getResources(), resId, options);
	}

	public static void saveBitmapToUri(Context context, Uri uri, Bitmap bitmap) throws FileNotFoundException {
		try {
			OutputStream outStream = context.getContentResolver().openOutputStream(uri);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, context.getContentResolver().openOutputStream(uri));
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPhotoFileNameFromTimetableIndex(Context context, long timetableId){
		//		 return TEMP_PHOTO_FILE + Installation.id(context) + "_" + Long.toString(timetableId) + ".jpg";
		return TEMP_PHOTO_FILE +  "_" + Long.toString(timetableId) + ".jpg";
	}
}
