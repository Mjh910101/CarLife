package com.itkc_carlife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.ActionDataListActivity;
import com.itkc_carlife.box.PayObj;
import com.itkc_carlife.box.ServiceObj;

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
 * Created by Hua on 16/1/12.
 */
public class ActionAdaper extends BaseAdapter {

    private Context context;
    private List<PayObj> itemList;
    private LayoutInflater inflater;
    private String type;

    public ActionAdaper(Context context, List<PayObj> list, String type) {
        initAdapter(context);
        this.itemList = list;
        this.type = type;
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
                    R.layout.layout_action_item, null);
            holder = new HolderView();

            holder.time = (TextView) convertView.findViewById(R.id.actionItem_timeText);
            holder.address = (TextView) convertView.findViewById(R.id.actionItem_addressText);
            holder.remark = (TextView) convertView.findViewById(R.id.actionItem_remarkText);
            holder.total = (TextView) convertView.findViewById(R.id.actionItem_totalText);
            holder.money = (TextView) convertView.findViewById(R.id.actionItem_moneyText);
            holder.remarkBox = (RelativeLayout) convertView.findViewById(R.id.actionItem_remarkBox);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        PayObj obj = itemList.get(position);
        setView(holder, obj, type);

        return convertView;
    }

    private void setView(HolderView holder, PayObj obj, String type) {

        switch (type) {
            case ServiceObj.GAS:
                holder.time.setText(obj.getCreatedAt());
                holder.address.setVisibility(View.VISIBLE);
                holder.remarkBox.setVisibility(View.GONE);
                holder.address.setText(obj.getServiceObj().getRemark()+" "+obj.getServiceObj().getName() + obj.getServiceObj().getAddress());
                holder.money.setText(obj.getTotalText() + "元");
                break;
            case ServiceObj.PARKING:
                holder.time.setText(obj.getCreatedAt());
                holder.remark.setText(obj.getServiceObj().getAddress());
                holder.total.setText(obj.getTotalText() + "元");
                break;
            case ServiceObj.HIGHWAY:
                holder.time.setText(obj.getCreatedAt());
                holder.remark.setText(obj.getServiceObj().getName() + obj.getServiceObj().getRemark());
                holder.total.setText(obj.getTotalText() + "元");
                break;
        }


    }

    class HolderView {
        TextView remark;
        TextView address;
        TextView total;
        TextView time;
        TextView money;
        RelativeLayout remarkBox;
    }

    public void addItems(List<PayObj> list) {
        for (PayObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }
}
