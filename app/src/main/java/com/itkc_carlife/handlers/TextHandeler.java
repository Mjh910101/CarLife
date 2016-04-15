package com.itkc_carlife.handlers;

import android.content.Context;
import android.text.Html;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 15/12/22.
 */
public class TextHandeler {

    public static String getText(Context context, int rid) {
        return context.getResources().getString(rid);
    }


    public static String getText(EditText et) {
        return et.getText().toString();
    }


    public static String getText(TextView v) {
        return v.getText().toString();
    }

    public static String getMoneyText(EditText text) {
        try {
            double b = Double.valueOf(getText(text));
            return new DecimalFormat("0.00").format(b);
        } catch (Exception e) {
            return "0.00";
        }
    }

    private final static String HEMLTEXT = "<html> \n" +
            " <head></head> \n" +
            " <body style=\"background-color:#F5F5F5;\"> \n" +
            "    <p style=\"text-align:justify;font-size:18px;color:#323232;text-indent:2em;\"> \n" +
            "?" +
            "    </p> \n" +
            "      </body> \n" +
            "</html> ";

    public static void setNeatText(TextView view, String str) {
        view.setText(Html.fromHtml(HEMLTEXT.replace("?", str)));
    }

    /**
     * 显示文字
     *
     * @param view
     *            显示文字的webview
     * @param content
     *            显示的文字
     * @param fontSize
     *            文字大小
     * @param fontColor
     *            文字颜色
     */
    public static void setNeatText(WebView view, String content,
                                   int fontSize, String fontColor) {
        view.loadDataWithBaseURL("",
                "<![CDATA[<html> <head></head> <body style=\"text-align:justify;font-size:"
                        + fontSize + "px;color:" + fontColor
                        + ";text-indent:2em;\"> " + content
                        + " <p> </body></html>", "text/html", "utf-8", "");
    }
}
