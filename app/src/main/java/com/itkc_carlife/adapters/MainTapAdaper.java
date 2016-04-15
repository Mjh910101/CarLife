package com.itkc_carlife.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.ActionDataListActivity;
import com.itkc_carlife.activitys.SettingDataListActivity;
import com.itkc_carlife.activitys.PartyListActivity;
import com.itkc_carlife.activitys.RepairDataListActivity;
import com.itkc_carlife.box.ServiceObj;
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
 * Created by Hua on 15/12/28.
 */
public class MainTapAdaper extends BaseAdapter {

    private final static String COME_ON_TEXT = "加    油";
    private final static String STOP_CAR_TEXT = "停    车";
    private final static String HIGHWAY_TEXT = "高    速";
    private final static String INSURANCE_TEXT = "快    赔";
    private final static String REPAIR_TEXT = "抢    修";
    private final static String SHOW_TEXT = "活    动";

    private Context context;
    private List<String> itemList;
    private LayoutInflater inflater;

    public MainTapAdaper(Context context) {
        initAdapter(context);
        initItemList();
    }

    private void initItemList() {
        itemList = new ArrayList<String>(6);
        itemList.add(COME_ON_TEXT);
        itemList.add(STOP_CAR_TEXT);
        itemList.add(HIGHWAY_TEXT);
        itemList.add(INSURANCE_TEXT);
        itemList.add(REPAIR_TEXT);
        itemList.add(SHOW_TEXT);
    }

    private void initAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String tag = itemList.get(position);

        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_main_tag, null);
            holder = new HolderView();
            holder.title = (TextView) convertView.findViewById(R.id.main_tag_name);
            holder.pic = (ImageView) convertView.findViewById(R.id.main_tag_pic);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        setView(holder, tag);
        setOnClick(convertView, tag);
        return convertView;
    }

    private void setView(HolderView holder, String tag) {
        int w = WinTool.getWinWidth(context) / 6;
        holder.pic.setLayoutParams(new LinearLayout.LayoutParams(w, w));
        holder.pic.setScaleType(ImageView.ScaleType.FIT_START);
//        DownloadImageLoader.loadImageForID(holder.pic, getPicId(tag));
        holder.pic.setImageResource(getPicId(tag));
        holder.title.setText(tag);
    }

    private int getPicId(String tag) {
        switch (tag) {
            case COME_ON_TEXT:
                return R.drawable.main_com_on_icon;
            case STOP_CAR_TEXT:
                return R.drawable.main_stop_car_icon;
            case HIGHWAY_TEXT:
                return R.drawable.main_highway_icon;
            case INSURANCE_TEXT:
                return R.drawable.main_insurance_icon;
            case REPAIR_TEXT:
                return R.drawable.main_repair_icon;
            case SHOW_TEXT:
                return R.drawable.main_show_icon;
            default:
                return R.mipmap.ic_launcher;
        }
    }

    private void setOnClick(View view, final String s) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                switch (s) {
                    case COME_ON_TEXT:
                        b.putString(ActionDataListActivity.TYPE, ServiceObj.GAS);
                        Passageway.jumpActivity(context, ActionDataListActivity.class, b);
                        break;
                    case STOP_CAR_TEXT:
                        b.putString(ActionDataListActivity.TYPE, ServiceObj.PARKING);
                        Passageway.jumpActivity(context, ActionDataListActivity.class, b);
                        break;
                    case HIGHWAY_TEXT:
                        b.putString(ActionDataListActivity.TYPE, ServiceObj.HIGHWAY);
                        Passageway.jumpActivity(context, ActionDataListActivity.class, b);
                        break;
                    case INSURANCE_TEXT:
                        Passageway.jumpActivity(context, SettingDataListActivity.class);
                        break;
                    case REPAIR_TEXT:
                        Passageway.jumpActivity(context, RepairDataListActivity.class);
                        break;
                    case SHOW_TEXT:
                        Passageway.jumpActivity(context,PartyListActivity.class);
                        break;
                }
            }
        });
    }


    class HolderView {
        TextView title;
        ImageView pic;
    }

}
