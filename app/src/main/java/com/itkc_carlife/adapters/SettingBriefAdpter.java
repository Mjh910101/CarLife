package com.itkc_carlife.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.RepairActivity;
import com.itkc_carlife.activitys.SettingInputMessageActivity;
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
 * Created by Hua on 16/2/24.
 */
public class SettingBriefAdpter extends BaseAdapter {

    private Context context;
    private List<SettlingBriefObj> itemList;
    private LayoutInflater inflater;

    public SettingBriefAdpter(Context context, List<SettlingBriefObj> list) {
        initAdapter(context);
        this.itemList = list;
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
                    R.layout.layout_settling_brief_item, null);
            holder = new HolderView();

            holder.time = (TextView) convertView.findViewById(R.id.settlingBriefItem_dayText);
            holder.maney = (TextView) convertView.findViewById(R.id.settlingBriefItem_maneyText);
            holder.title = (TextView) convertView.findViewById(R.id.settlingBriefItem_typeText);

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
                b.putString(SettingInputMessageActivity.SRTTLING_ORDER_ID_KEY, obj.getSettlingOrderId());
                b.putBoolean(RepairActivity.IS_REPAIR, true);
                Passageway.jumpActivity(context, RepairActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, SettlingBriefObj obj) {
//        holder.title.setText(obj.getOrder_status_title());
        holder.time.setText(obj.getCreatedAt());
        if (obj.getPayText().equals("")) {
            holder.maney.setText("￥未填维修价");
            holder.maney.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_04));
        } else {
            holder.maney.setText("￥" + obj.getPayText());
            holder.maney.setTextColor(ColorHandle.getColorForID(context, R.color.text_orange));
        }
//        if (obj.getOrder_status() == 1) {
//            holder.title.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
//        } else {
//            holder.title.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
//        }


    }

    class HolderView {
        TextView time;
        TextView maney;
        TextView title;
        TextView tel;

    }
}
