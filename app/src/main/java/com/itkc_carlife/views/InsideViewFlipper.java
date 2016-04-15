package com.itkc_carlife.views;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

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
 * Created by Hua on 15/10/21.
 */
public class InsideViewFlipper extends ViewFlipper {

    PointF downP = new PointF();
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public InsideViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsideViewFlipper(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        curP.x = arg0.getX();
        curP.y = arg0.getY();
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {
            Log.d("",
                    Math.abs(downP.x - curP.x) + "..."
                            + Math.abs(downP.y - curP.y));
            if ((Math.abs(downP.x - curP.x) <= 10)
                    && (Math.abs(downP.y - curP.y) <= 10)) {
                onSingleTouch();
                return true;
            }
        }
        return super.onTouchEvent(arg0);
    }

    public void onSingleTouch() {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}
