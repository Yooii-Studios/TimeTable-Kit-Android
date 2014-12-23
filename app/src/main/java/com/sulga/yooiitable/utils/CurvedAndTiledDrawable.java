package com.sulga.yooiitable.utils;

import android.graphics.*;
import android.graphics.drawable.*;


class CurvedAndTiledDrawable extends Drawable {

	private final float mCornerRadius;
	private float[] mCorners = new float[8];
	private final RectF mRect = new RectF();
	private final BitmapShader mBitmapShader;
	private final Paint mTilePaint;        
	private final Path mPath = new Path();

	CurvedAndTiledDrawable(
			Bitmap bitmap,
			Shader.TileMode tileX,
			Shader.TileMode tileY,
			float cornerRadius) {
		mCornerRadius = cornerRadius;

		mBitmapShader = new BitmapShader(bitmap,
				tileX, tileY);

		mTilePaint = new Paint();
		mTilePaint.setAntiAlias(true);
		mTilePaint.setShader(mBitmapShader);   

		for(int i = 0 ; i < mCorners.length ; i++ ){
			mCorners[i] = mCornerRadius;
		}
	}

	CurvedAndTiledDrawable(
			Bitmap bitmap, 
			Shader.TileMode tileX,
			Shader.TileMode tileY,
			float[] corners){
		mCornerRadius = 0;
		mBitmapShader = new BitmapShader(bitmap,
				tileX, tileY);

		mTilePaint = new Paint();
		mTilePaint.setAntiAlias(true);
		mTilePaint.setShader(mBitmapShader);   

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
		canvas.drawPath(mPath, mTilePaint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		mTilePaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mTilePaint.setColorFilter(cf);
	}       
}