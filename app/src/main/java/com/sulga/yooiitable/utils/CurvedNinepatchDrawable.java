package com.sulga.yooiitable.utils;

import android.graphics.*;
import android.graphics.drawable.*;

public class CurvedNinepatchDrawable extends Drawable {
	private final float mCornerRadius;
	private float[] mCorners = new float[8];
	private final RectF mRect = new RectF();
	private final Paint mPaint;        
	private final Path mPath = new Path();
	private final NinePatch mNinePatch;

	CurvedNinepatchDrawable(
			Bitmap bitmap,
			float cornerRadius) {
		mCornerRadius = cornerRadius;

		mNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), "");
		//sc.draw(canvas, new Rect(0,0,100,100), mPaint);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		//mPaint.setShader(mBitmapShader);  
		//mPaint.setShader(new Shader(bitmap));

		for(int i = 0 ; i < mCorners.length ; i++ ){
			mCorners[i] = mCornerRadius;
		}
	}

	CurvedNinepatchDrawable(
			Bitmap bitmap, 
			float[] corners){
		mCornerRadius = 0;

		mNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), "");

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		//mPaint.setShader(mBitmapShader);   

		mCorners = corners;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mRect.set(0, 0, bounds.width(), bounds.height());

	}

	@Override
	public void draw(Canvas canvas) {
		//canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mTilePaint);  
		mPath.addRoundRect(mRect, mCorners, Path.Direction.CW);
		//mPaint.
		mNinePatch.draw(canvas, mRect);
		canvas.clipPath(mPath);

		//mNinePatch.draw(canvas, mRect);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}       

}
