package com.itkc_carlife.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.RepairStoreActivity;
import com.itkc_carlife.box.RepairStoreObj;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
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
 * Created by Hua on 16/1/20.
 */
public class RepairStoreAadpter extends BaseAdapter {


    private Context context;
    private List<RepairStoreObj> itemList;
    private LayoutInflater inflater;
    private String repairId;

    public RepairStoreAadpter(Context context, List<RepairStoreObj> list, String repairId) {
        initAdapter(context);
        this.itemList = list;
        this.repairId = repairId;
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
                    R.layout.layout_repair_store_item, null);
            holder = new HolderView();

            holder.name = (TextView) convertView.findViewById(R.id.repairStoreItem_name);
            holder.maney = (TextView) convertView.findViewById(R.id.repairStoreItem_maney);
            holder.address = (TextView) convertView.findViewById(R.id.repairStoreItem_address);
            holder.days = (TextView) convertView.findViewById(R.id.repairStoreItem_days);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        RepairStoreObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClickView(convertView, obj);
        return convertView;
    }

    private void setOnClickView(View view, final RepairStoreObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepairStoreObjHandler.save(obj);
                Bundle b = new Bundle();
                b.putString("id", repairId);
                b.putString("fee", obj.getFee());
                b.putInt("day", obj.getDays());
                Passageway.jumpActivity(context, RepairStoreActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, RepairStoreObj obj) {
        holder.address.setText(obj.getAddress());
        holder.name.setText(obj.getName());
        holder.maney.setText("￥" + obj.getFee());
        holder.days.setText(obj.getDays() + "天");
    }

    class HolderView {
        TextView address;
        TextView maney;
        TextView name;
        TextView days;
    }
}
