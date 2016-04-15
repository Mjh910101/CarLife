package com.itkc_carlife.tool;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationTool {

	public static Animation getArcTranslate(float fromXDelta, float toXDelta,
			float fromYDelta, float toYDelta) {
		ArcTranslateAnimation alphaAnimation = new ArcTranslateAnimation(
				fromXDelta, toXDelta, fromYDelta, toYDelta);
		alphaAnimation.setDuration(1500);
		return alphaAnimation;
	}

	public static Animation getDisappear(long time, AnimationListener listener) {
		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
		alphaAnimation.setDuration(time);
		alphaAnimation.setAnimationListener(listener);
		return alphaAnimation;
	}

	public static Animation getAppear(long time, AnimationListener listener) {
		Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(time);
		alphaAnimation.setAnimationListener(listener);
		return alphaAnimation;
	}

	public static Animation toLeftIn(long time) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(time);
		animation.setInterpolator(new AccelerateInterpolator());
		return animation;
	}

	public static Animation toLeftOut(long time) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(time);
		animation.setInterpolator(new AccelerateInterpolator());
		return animation;
	}

	public static Animation toRightIn(long time) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(time);
		animation.setInterpolator(new AccelerateInterpolator());
		return animation;

	}

	public static Animation toRightOut(long time) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(time);
		animation.setInterpolator(new AccelerateInterpolator());
		return animation;
	}

	public static AnimationSet getAlphaAndScale(long time,
			AnimationListener listener) {
		ScaleAnimation sAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f);
		Animation aAnimation = new AlphaAnimation(0.1f, 1.0f);
		AnimationSet _AnimationSet = new AnimationSet(true);
		_AnimationSet.addAnimation(sAnimation);
		_AnimationSet.addAnimation(aAnimation);
		_AnimationSet.setFillBefore(false);
		_AnimationSet.setFillAfter(false);
		_AnimationSet.setDuration(time);
		_AnimationSet.setAnimationListener(listener);
		return _AnimationSet;
	}

}
