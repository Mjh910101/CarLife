package com.itkc_carlife.handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.itkc_carlife.R;


public class ColorHandle {

    public static int getColorForID(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static int getImageMaximumColor(Bitmap bitmap) {
        for (int i = 1; i < bitmap.getWidth(); i++) {
            for (int j = 1; j < bitmap.getHeight(); j++) {
                int color = bitmap.getPixel(i, j);
                if (color != 0 && color != -1) {
                    Log.d("color", color + "");
                    return Color.rgb(Color.red(color), Color.green(color),
                            Color.blue(color));
                }
            }
        }
        return 0;
    }

    public static int getColor(Context context, String color) {
        try {
            color = color.replace("#", "");
            char[] chars = color.toCharArray();
            int r = Integer.valueOf(
                    String.valueOf(String.valueOf(chars[0])
                            + String.valueOf(chars[1])), 16);
            int g = Integer.valueOf(
                    String.valueOf(String.valueOf(chars[2])
                            + String.valueOf(chars[3])), 16);
            int b = Integer.valueOf(
                    String.valueOf(String.valueOf(chars[4])
                            + String.valueOf(chars[5])), 16);
            Color c = new Color();
            return c.rgb(r, g, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getColorForID(context, R.color.activity_bg);
    }
}
