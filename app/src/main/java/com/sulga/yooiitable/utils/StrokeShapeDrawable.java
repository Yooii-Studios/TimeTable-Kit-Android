package com.sulga.yooiitable.utils;

import android.graphics.*;
import android.graphics.drawable.*;
import android.graphics.drawable.shapes.*;

public class StrokeShapeDrawable extends ShapeDrawable {

	private final Paint fillpaint, strokepaint;
	private Matrix matrix;
	private boolean[] willStroke;
	/**
	 * Shape s, fill color, stroke color, stroke width
	 * @param s
	 * @param fill
	 * @param stroke
	 * @param strokeWidth
	 */
	public StrokeShapeDrawable(Shape s, int fill, int alpha, int stroke, int strokeWidth) {
		super(s);
		matrix = new Matrix();
		fillpaint = new Paint(this.getPaint());
		fillpaint.setColor(fill);
		fillpaint.setAlpha(alpha);
		if(strokeWidth != 0){
			strokepaint = new Paint(fillpaint);
			strokepaint.setStyle(Paint.Style.STROKE);
			strokepaint.setStrokeWidth(strokeWidth);
			strokepaint.setColor(stroke);
		}else{
			strokepaint = null;
		}
		willStroke = new boolean[]{
			true, true, true, true
		};
	}
	
	public StrokeShapeDrawable(Shape s, int fill, int alpha, int stroke, int strokeWidth,
			boolean strokeL, boolean strokeT, boolean strokeR, boolean strokeB) {
		super(s);
		matrix = new Matrix();
		fillpaint = new Paint(this.getPaint());
		fillpaint.setColor(fill);
		fillpaint.setAlpha(alpha);
		if(strokeWidth != 0){
			strokepaint = new Paint(fillpaint);
			strokepaint.setStyle(Paint.Style.STROKE);
			strokepaint.setStrokeWidth(strokeWidth);
			strokepaint.setColor(stroke);
		}else{
			strokepaint = null;
		}
		willStroke = new boolean[]{
			strokeL,
			strokeT,
			strokeR,
			strokeB
		};
	}


	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {		
		
		shape.resize(canvas.getClipBounds().right,
				canvas.getClipBounds().bottom);
		shape.draw(canvas, fillpaint);

		if(strokepaint != null){
			matrix.reset();
			float strokeWidth = strokepaint.getStrokeWidth();
			float strokeOffset =  (float) Math.ceil(strokeWidth / 2f);
			
			float[] destOffset = new float[]{
				strokeOffset,
				strokeOffset,
				strokeOffset,
				strokeOffset
			};
			for(int i = 0; i < willStroke.length ; i++){
				if(willStroke[i] == false){
					destOffset[i] *= -1;
				}
			}
			matrix.setRectToRect(new RectF(0, 0, canvas.getClipBounds().right,
					canvas.getClipBounds().bottom),
					new RectF(destOffset[0], destOffset[1], 
							canvas.getClipBounds().right - destOffset[2],
							canvas.getClipBounds().bottom - destOffset[3]),
							Matrix.ScaleToFit.FILL);
			canvas.concat(matrix);
			shape.draw(canvas, strokepaint);
			
		}
		//		this.invalidateSelf();		//성능 존나 잡아먹음;;;
	}
}
