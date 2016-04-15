package com.itkc_carlife.tool;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class WinTool {

	private static int widthPixels;
	private static int heightPixels;

	public static int getWinWidth() {
		return widthPixels;
	}

	public static int getWinHeight() {
		return heightPixels;
	}

	public static void initWinTool(Context context) {
		widthPixels = getWinWidth(context);
		heightPixels = getWinHeight(context);
	}

	public static int getWinWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getWinHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	public static int getWinWidth(Activity a) {
		DisplayMetrics dm = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getWinHeight(Activity a) {
		DisplayMetrics dm = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static int dipToPx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int pxToDip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
