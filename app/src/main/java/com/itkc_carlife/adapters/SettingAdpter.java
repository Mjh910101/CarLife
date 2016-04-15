package com.itkc_carlife.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.RepairActivity;
import com.itkc_carlife.activitys.SettingInputMessageActivity;
import com.itkc_carlife.activitys.SettlingActivity;
import com.itkc_carlife.box.SettlingBriefObj;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.tool.Passageway;

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
 * Created by Hua on 16/1/19.
 */
public class SettingAdpter extends BaseAdapter {

    private Context context;
    private List<SettlingBriefObj> itemList;
    private LayoutInflater inflater;
    private boolean isRepair;

    public SettingAdpter(Context context, List<SettlingBriefObj> list, boolean isRepair) {
        initAdapter(context);
        this.itemList = list;
        this.isRepair = isRepair;
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

        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_settling_item, null);
            holder = new HolderView();

            holder.icon = (ImageView) convertView.findViewById(R.id.settlingImte_icon);
            holder.time = (TextView) convertView.findViewById(R.id.settlingImte_day);
            holder.maney = (TextView) convertView.findViewById(R.id.settlingImte_maney);
            holder.title = (TextView) convertView.findViewById(R.id.settlingImte_title);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        SettlingBriefObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClick(convertView, obj);

        return convertView;
    }

    private void setOnClick(View view, final SettlingBriefObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(SettingInputMessageActivity.SRTTLING_ID_KEY, obj.getSettlingId());
                if (isRepair) {
                    Passageway.jumpActivity(context, RepairActivity.class, b);
                } else {
                    switch (obj.getOrder_status()) {
                        case 1:
                            b.putBoolean(SettlingBriefObj.ORDER_STATUS, true);
                            break;
                    }
                    b.putBoolean(SettlingActivity.IS_DATA, true);
                    Passageway.jumpActivity(context, SettlingActivity.class, b);

                }
            }
        });
    }

    private void setView(HolderView holder, SettlingBriefObj obj) {
        holder.title.setText(obj.getOrder_status_title());
        holder.time.setText(obj.getCreatedAt());
        if (!obj.getPay().equals("")) {
            holder.maney.setText("￥" + obj.getPay());
        } else {
            holder.maney.setText("");
        }
        if (obj.getOrder_status() == 1) {
            holder.title.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
        } else {
            holder.title.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        }

        if (obj.isMajor()) {
            holder.icon.setImageResource(R.drawable.z_icon);
        } else {
            holder.icon.setImageResource(R.drawable.c_icon);
        }

    }

    class HolderView {
        TextView time;
        TextView maney;
        TextView title;
        ImageView icon;
    }
}
