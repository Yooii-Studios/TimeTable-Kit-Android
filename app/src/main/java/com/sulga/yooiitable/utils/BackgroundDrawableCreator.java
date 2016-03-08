package com.sulga.yooiitable.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class BackgroundDrawableCreator {
	public static BitmapDrawable getTileBackground(Activity activity, Shader.TileMode tileModeX, Shader.TileMode tileModeY, int drawableId){

		//		DisplayMetrics dm = new DisplayMetrics();
		//		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//
		//		BitmapFactory.Options options = new BitmapFactory.Options();
		//		options.inScaled=true;
		//		//options.inScreenDensity
		//
		//		Bitmap center_bmp = BitmapFactory.decodeResource(activity.getResources(), drawableId, options);
		//		center_bmp.setDensity(Bitmap.DENSITY_NONE);
		//		center_bmp=Bitmap.createScaledBitmap(center_bmp, dm.widthPixels , center_bmp.getHeight(), true);
		//
		//		BitmapDrawable center_drawable = new BitmapDrawable(activity.getResources(),center_bmp);
		//		
		//		if(tileModeX != null){
		//			center_drawable.setTileModeX(tileModeX);
		//		}
		//		if(tileModeY != null){
		//			center_drawable.setTileModeY(tileModeY);
		//		}
		//		//change here setTileModeY to setTileModeX if you want to repear in X
		//		//center_drawable.setTileModeY(Shader.TileMode.REPEAT);
		//
		//		return center_drawable;


		BitmapDrawable draw = (BitmapDrawable)activity.getResources().getDrawable(drawableId);
		//		if(tileModeX != null)
		//			draw.setTileModeX(tileModeX);
		if(tileModeY != null)
			draw.setTileModeY(tileModeY);
		//draw.set

		return draw;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	public static Bitmap getRoundedCornerBitmap(BitmapDrawable bitmapDrawable) {
		Bitmap bitmap = bitmapDrawable.getBitmap();


		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Drawable getTiledRoundedRectBitmapDrawable(
			Context context, 
			Shader.TileMode tileX, Shader.TileMode tileY, 
			float radius, int drawableId){
		/*BitmapDrawable bd_before = getTileBackground(activity, tileModeX, tileModeY, drawableId);
		Bitmap bitmap = getRoundedCornerBitmap(bd_before);
		BitmapDrawable bd_after =new BitmapDrawable(activity.getResources(),bitmap);
		return bd_after;*/
		Bitmap bitmap = 
				 BitmapFactory.decodeResource(context.getResources(), 
						 drawableId);  
		return new CurvedAndTiledDrawable(bitmap, tileX, tileY, radius);


		//		BackgroundDrawableCreator.getTileBackground(
		//				getActivity(), null, Shader.TileMode.REPEAT, R.drawable.yt_timetable_timeline_background)

	}

	public static Drawable getTiledRoundedRectBitmapDrawable(
			Context context, 
			Shader.TileMode tileX,
			Shader.TileMode tileY,
			float[] corners,
			int drawableId){
		Bitmap bitmap = 
				 BitmapFactory.decodeResource(context.getResources(), 
						 drawableId);  
		return new CurvedAndTiledDrawable(bitmap, tileX, tileY, 
				corners);
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}

	/*public static Drawable getNinepatchRoundedRectBitmapDrawable(
			Activity activity, 
			float[] corners,
			int drawableId){
		
		 Bitmap bitmap = 
				 BitmapFactory.decodeResource(activity.getResources(), 
						 drawableId);  
		//Bitmap bitmap = (activity.getResources().getDrawable(drawableId)).getBitmap();

		return new CurvedNinepatchDrawable(bitmap, corners);


	}
*/
}
