package com.sulga.yooiitable.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sulga.yooiitable.R;

import java.util.ArrayList;


public class ModeRelativeLayout extends RelativeLayout{
	private final long MENU_ROTATE_DURATION = 500;
//	private final int SUB_MENU_TRANS_LENGTH_IN_DP = 80;
	private int subMenuTransLength;
	private final long SUB_MENU_TRANS_DURATION = 500;
//	private final int SUB_MENU_SEL_GAP = 80;
	private int subMenuSelectorGap;
	private final long SUB_MENU_SEL_DURATION = 500;
	
	private Context mContext;
	private ArrayList<PathButton> menuButtons;
	private Button plusBtn;
	private ModeOpenBtnClickedListener mModeOpenBtnClickedListener;
	private boolean isOpened = false;
	private ImageView plusIconImageView;
	
	public enum ANIMATION_DIRECTION {RIGHT, LEFT, UP, DOWN}
	private ANIMATION_DIRECTION mAnimationDirection = ANIMATION_DIRECTION.RIGHT;

	public void setAnimationDirection(ANIMATION_DIRECTION animationDirection) {
		this.mAnimationDirection = animationDirection;
	}

	public ModeOpenBtnClickedListener getModeOpenBtnClickedListener() {
		return mModeOpenBtnClickedListener;
	}

	public void setModeOpenBtnClickedListener(
			ModeOpenBtnClickedListener mModeOpenBtnClickedListener) {
		this.mModeOpenBtnClickedListener = mModeOpenBtnClickedListener;
	}

	private static final String TAG = "mode button";
	private static final String TAG_INDEX = "index";

	public void clearMenuButtons(){
		menuButtons.clear();
	}
	public void addMenuButtons(PathButton btn){
		menuButtons.add(btn);
	}

	public ModeRelativeLayout(Context context) {
		super(context);
		init(context);
	}
	public ModeRelativeLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	public ModeRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		menuButtons = new ArrayList<>();
		mContext = context;
		subMenuTransLength = (int) context.getResources()
				.getDimension(R.dimen.fragment_timetable_panel_button_offset);
		subMenuSelectorGap = (int) context.getResources()
				.getDimension(R.dimen.fragment_timetable_panel_button_offset);
	}

	public void setUpViews() {
		plusBtn = (Button) findViewById(R.id.btn_plus);
		plusIconImageView = (ImageView) findViewById(R.id.ico_plus);

		bringMenuIconViewToFront();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void bringMenuIconViewToFront() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			RelativeLayout hitLayout = (RelativeLayout) findViewById(R.id.hit_layout);
			hitLayout.setZ(9.f);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		int inRectX = (int)e.getX();
		int inRectY = (int)e.getY();
		if (e.getAction() == MotionEvent.ACTION_UP){
			Rect curr = new Rect();
			int[] thisLocationOnScreen = new int[2];
			int[] plusBtnLocationOnScreen = new int[2];
			
			this.getLocationOnScreen(thisLocationOnScreen);
			plusBtn.getLocationOnScreen(plusBtnLocationOnScreen);
			plusBtn.getHitRect(curr);
			
			int btnLeft = plusBtnLocationOnScreen[0] - thisLocationOnScreen[0];
			int btnTop = plusBtnLocationOnScreen[1] - thisLocationOnScreen[1];
			int btnRight = btnLeft + curr.right;
			int btnBottom = btnTop + curr.bottom;
			
			if (inRectX >= btnLeft && inRectX <= btnRight && inRectY >= btnTop && inRectY <= btnBottom){
				Log.i(TAG, "menu pressed");
				isOpened = !isOpened;
				if (!isOpened)
					mModeOpenBtnClickedListener.onClick();

				startMenuAnimation(isOpened);
				return true;
			}
			for (PathButton btn : menuButtons){
				btn.getLocationOnScreen(plusBtnLocationOnScreen);

				btn.getHitRect(curr);
				
				btnLeft = plusBtnLocationOnScreen[0] - thisLocationOnScreen[0] + btn.getXOffset();
				btnTop = plusBtnLocationOnScreen[1] - thisLocationOnScreen[1] + btn.getYOffset();
				btnRight = btnLeft + curr.right;
				btnBottom = btnTop + curr.bottom;
				
				if (inRectX >= btnLeft && inRectX <= btnRight && inRectY >= btnTop && inRectY <= btnBottom){
					Log.i(TAG, "mode pressed");
					//startSubButtonSelectedAnimation(menuButtons.indexOf(btn));
					//isOpened = false;
					btn.getOnModeBtnClickedListener().onClick(btn);
					return true;
				}
			}
			return false;
		}
		return super.dispatchTouchEvent(e);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		if (isOpened)
			return true;
		else
			return false;
//		return super.onInterceptTouchEvent(e);
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (isOpened)
			return true;
		else
			return false;
//			return super.onTouchEvent(e);
	}

	public interface ModeOpenBtnClickedListener{
		void onClick();
	}
	

	private void startMenuAnimation(boolean open) {
		Animation rotate;

		if(open)
			rotate = new RotateAnimation(
					0, 45
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
		else
			rotate = new RotateAnimation(
					-45, 0
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);

        //noinspection ResourceType
        rotate.setInterpolator(AnimationUtils.loadInterpolator(mContext,
				android.R.anim.anticipate_overshoot_interpolator));
		rotate.setFillAfter(true);
		rotate.setDuration(MENU_ROTATE_DURATION);

		plusIconImageView.startAnimation(rotate);

		for(int i = 0 ; i < menuButtons.size() ; i++) {
			startSubButtonAnimation(i, open);
			Log.i(TAG_INDEX, "index : " + i);
		}
	}

	@SuppressWarnings("ResourceType")
    private void startSubButtonAnimation(int index, boolean open) {
		PathButton view = menuButtons.get(index);

//		int endX = (int)(Constants.MapSettingConstants.SUB_MENU_TRANS_LENGTH_IN_DP * FloatMath.cos(
//				(float) (Math.PI * 1/2 * (index)/(menuButtons.size()-1))
//				));
//		int endY = (int)(Constants.MapSettingConstants.SUB_MENU_TRANS_LENGTH_IN_DP * FloatMath.sin(
//				(float) (Math.PI * 1/2 * (index)/(menuButtons.size()-1))
//				));
		int endX;
		int endY;
		if (mAnimationDirection == ANIMATION_DIRECTION.LEFT){
//			endX = -SUB_MENU_TRANS_LENGTH_IN_DP * (index+1);
			endX = -subMenuTransLength * (index + 1);
			endY = 0;
		}
		else{
//			endX = SUB_MENU_TRANS_LENGTH_IN_DP * (index+1);
			endX = subMenuTransLength * (index+1);
			endY = 0;
		}

		AnimationSet animation = new AnimationSet(false);
		Animation translate;
		Animation rotate = new RotateAnimation(
				0, 360
				, Animation.RELATIVE_TO_SELF, 0.5f
				, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(SUB_MENU_TRANS_DURATION);
		rotate.setRepeatCount(0);
		rotate.setInterpolator(AnimationUtils.loadInterpolator(mContext,
                android.R.anim.accelerate_interpolator));
		if(open) {
			translate = new TranslateAnimation(
					0.0f, endX
					, 0.0f, -endY);
			translate.setDuration(SUB_MENU_TRANS_DURATION);
			translate.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.overshoot_interpolator));
//			translate.setStartOffset(SUB_MENU_SEL_GAP*index);
			translate.setStartOffset(subMenuSelectorGap*index);

			view.setOffset(endX, -endY);
		} else {
			translate = new TranslateAnimation(
					endX, 0
					, -endY, 0);
			translate.setDuration(SUB_MENU_TRANS_DURATION);
//			translate.setStartOffset(SUB_MENU_SEL_GAP*(menuButtons.size()-(index+1)));
			translate.setStartOffset(subMenuSelectorGap*(menuButtons.size()-(index+1)));
			translate.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.anticipate_interpolator));

			view.setOffset(-endX, endY);
		}

		animation.setFillAfter(true);
		animation.addAnimation(rotate);
		animation.addAnimation(translate);

		view.startAnimation(animation);
	}

	private void startSubButtonSelectedAnimation(int index) {
		for(int i = 0 ; i < menuButtons.size() ; i++) {
			if(index == i) {
				PathButton view = menuButtons.get(i);

				AnimationSet animation = new AnimationSet(false);

				Animation translate = new TranslateAnimation(
						0.0f, view.getXOffset()
						, 0.0f, view.getYOffset());
				translate.setDuration(0);

				Animation scale = new ScaleAnimation(
						1.0f, 2.5f
						, 1.0f, 2.5f
						, Animation.RELATIVE_TO_SELF, 0.5f
						, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(SUB_MENU_SEL_DURATION);

				Animation alpha = new AlphaAnimation(1.0f, 0.0f);
				alpha.setDuration(SUB_MENU_SEL_DURATION);

				animation.addAnimation(scale);
				animation.addAnimation(translate);
				animation.addAnimation(alpha);

				view.startAnimation(animation);
			} else {
				PathButton view = menuButtons.get(i);

				AnimationSet animation = new AnimationSet(false);

				Animation translate = new TranslateAnimation(
						0.0f, view.getXOffset()
						, 0.0f, view.getYOffset());
				translate.setDuration(0);

				Animation scale = new ScaleAnimation(
						1.0f, 0.0f
						, 1.0f, 0.0f
						, Animation.RELATIVE_TO_SELF, 0.5f
						, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(SUB_MENU_SEL_DURATION);

				Animation alpha = new AlphaAnimation(1.0f, 0.0f);
				alpha.setDuration(SUB_MENU_SEL_DURATION);

				animation.addAnimation(scale);
				animation.addAnimation(translate);
				animation.addAnimation(alpha);

				view.startAnimation(animation);
			}
		}

		if(isOpened) {
			isOpened = false;

			Animation rotate = new RotateAnimation(
					-45, 0
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);

			rotate.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.anticipate_overshoot_interpolator));
			rotate.setFillAfter(true);
			rotate.setDuration(SUB_MENU_SEL_DURATION);
			plusIconImageView.startAnimation(rotate);
		}
	}
}
