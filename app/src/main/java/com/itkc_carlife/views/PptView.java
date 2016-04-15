package com.itkc_carlife.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.itkc_carlife.R;
import com.itkc_carlife.activitys.ImageActivity;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.tool.AnimationTool;
import com.itkc_carlife.tool.PPTFlish;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Hua on 15/11/6.
 */
public class PptView extends LinearLayout {
    private Context context;

    private View view;
    private LayoutInflater inflater;
    private List<String> list;

    private InsideViewFlipper mViewFlipper;
    private LinearLayout pptBallBox;

    private List<ImageView> pptBallList = new ArrayList<ImageView>();

    private PPTFlish flish = null;
    private Thread thread = null;

    private int w, h;

    public PptView(Context context, List<String> list, int w, int h) {

        super(context);
        this.context = context;
        this.list = list;
        this.w = w;
        this.h = h;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_ppt, null);

        mViewFlipper = (InsideViewFlipper) view.findViewById(R.id.ppt_images);
        pptBallBox = (LinearLayout) view.findViewById(R.id.ppt_ball);

        setPptView(list);
        addView(view);
    }

    private void setPptView(final List<String> list) {

        if (pptBallList.size() > 0) {
            mViewFlipper.removeAllViews();
            pptBallBox.removeAllViews();
            pptBallList.removeAll(pptBallList);
        }

        for (int i = 0; i < list.size(); i++) {
            ImageView image = new ImageView(context);
            image.setLayoutParams(new LayoutParams(w, h));
//            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setId(i);
            DownloadImageLoader.loadImage(image, list.get(i));
            mViewFlipper.addView(image);
        }

        for (int i = 0; i < list.size(); i++) {
            ImageView image = new ImageView(context);
            if (i == 0) {
                image.setImageResource(R.drawable.ppt_on);
            } else {
                image.setImageResource(R.drawable.ppt_off);
            }
            image.setLayoutParams(new LayoutParams(25, 25));
            View view = new View(context);
            view.setLayoutParams(new LayoutParams(5, 5));
            pptBallBox.addView(image);
            pptBallBox.addView(view);
            pptBallList.add(image);
        }

        startFlish();

        mViewFlipper.setOnTouchListener(new OnTouchListener() {

            float eventX, eventY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        eventX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (eventX - event.getX() > 120) {// 左
                            setAnimation(true);
                        } else if (event.getX() - eventX > 120) {// 右
                            setAnimation(false);
                        } else if (Math.abs(eventX - event.getX()) < 10) {
                            onClickPPT(list.get(mViewFlipper.getDisplayedChild()));
                        }
                        break;
                }
                return true;
            }

        });

    }

    private void setAnimation(boolean isLeft) {

        long FLISHTIME = 500;

        if (mViewFlipper != null && mViewFlipper.getCurrentView() != null) {
            stopFlish();
            if (isLeft) {
                mViewFlipper.setInAnimation(AnimationTool
                        .toLeftIn(FLISHTIME));
                mViewFlipper.setOutAnimation(AnimationTool
                        .toLeftOut(FLISHTIME));
                mViewFlipper.showNext();
            } else {
                mViewFlipper.setInAnimation(AnimationTool
                        .toRightIn(FLISHTIME));
                mViewFlipper.setOutAnimation(AnimationTool
                        .toRightOut(FLISHTIME));
                mViewFlipper.showPrevious();
            }
            for (ImageView ball : pptBallList) {
                ball.setImageResource(R.drawable.ppt_off);
            }
            pptBallList.get(mViewFlipper.getDisplayedChild()).setImageResource(
                    R.drawable.ppt_on);
            startFlish();
        }
    }

    public void startFlish() {
        if (flish == null && pptBallList.size() > 1) {
            flish = new PPTFlish(handler);
            thread = new Thread(flish);
            thread.start();
        }
    }

    public void stopFlish() {
        if (flish != null) {
            flish.stop();
            flish = null;
            thread = null;
        }
    }

    private void onClickPPT(String str) {
        Bundle b = new Bundle();
        Log.e("", str);
        b.putBoolean(ImageActivity.IS_ONLINE, true);
        b.putString(ImageActivity.URL, str);
        Passageway.jumpActivity(context, ImageActivity.class, ImageActivity.REQUEST_CODE, b);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PPTFlish.FLISHFORPPT:
                    setAnimation(true);
                    break;
            }
        }

    };

}
