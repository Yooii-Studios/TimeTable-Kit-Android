package com.sulga.yooiitable.utils;

import java.io.*;

import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;

public class SerializeBitmapUtils {
	public static byte[] bitmapToByteArray( Bitmap bitmap ) {  
		ByteArrayOutputStream stream = new ByteArrayOutputStream() ;  
		bitmap.compress( CompressFormat.PNG, 100, stream) ;  
		byte[] byteArray = stream.toByteArray() ;  
		return byteArray;  
	}  

	public static Bitmap byteArrayToBitmap( byte[] byteArray ) {  
		Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;  
		return bitmap ;  
	} 
}
