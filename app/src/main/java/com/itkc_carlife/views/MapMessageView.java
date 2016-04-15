package com.itkc_carlife.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.ServerShopActivity;
import com.itkc_carlife.box.ServicesObj;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;

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
 * Created by Hua on 16/2/16.
 */
public class MapMessageView extends LinearLayout {

    private Context context;
    private LayoutInflater inflater;
    private View view;

    private ServicesObj obj;

    public MapMessageView(Context context, ServicesObj obj) {
        super(context);

        this.context = context;
        this.obj = obj;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.map_message_view, null);
        int w = WinTool.getWinWidth(context) / 2;
        view.setLayoutParams(new LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.MATCH_PARENT));
        setMessage();
        setStarsList();
        setOnClick();
        addView(view);
    }

    private void setOnClick() {
//        findViewById(R.id.mapMessage_jumpBtn)
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ServicesObjHandler.saveServicesObj(obj);
                Bundle b = new Bundle();
                b.putString("id", obj.getObjectId());
                Passageway.jumpActivity(context, ServerShopActivity.class, b);
            }
        });
    }

    private void setStarsList() {
        LinearLayout sBox = (LinearLayout) view.findViewById(R.id.mapMessage_starsBox);
        if (obj.getRate() > 0) {
            for (int i = 0; i < obj.getRate(); i++) {
                sBox.addView(getStars());
            }
        }
    }

    private void setMessage() {
        ((TextView) view.findViewById(R.id.mapMessage_title)).setText(obj.getName());
        ((TextView) view.findViewById(R.id.mapMessage_descript)).setText(obj.getDescript());
    }

    public ImageView getStars() {
        int p = 3;
        ImageView stars = new ImageView(context);
        stars.setImageResource(R.drawable.stars_icon);
        stars.setPadding(p, p, p, p);
        return stars;
    }
}
