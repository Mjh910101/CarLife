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
import com.itkc_carlife.activitys.RepairInputMessageActivity;
import com.itkc_carlife.activitys.RepairPlayDataActivity;
import com.itkc_carlife.activitys.WaitingRepairStoreActivity;
import com.itkc_carlife.box.RepairObj;
import com.itkc_carlife.box.handler.RepairStoreObjHandler;
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
 * Created by Hua on 16/1/21.
 */
public class RepairAdaper extends BaseAdapter {

    private Context context;
    private List<RepairObj> itemList;
    private LayoutInflater inflater;

    public RepairAdaper(Context context, List<RepairObj> list) {
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
                    R.layout.layout_repair_item, null);
            holder = new HolderView();

            holder.time = (TextView) convertView.findViewById(R.id.repairItem_time);
            holder.maney = (TextView) convertView.findViewById(R.id.repairItem_maney);
            holder.address = (TextView) convertView.findViewById(R.id.repairItem_address);
            holder.tel = (TextView) convertView.findViewById(R.id.repairItem_tel);
            holder.status = (TextView) convertView.findViewById(R.id.repairItem_status);
            holder.setllingIcon = (ImageView) convertView.findViewById(R.id.repairItem_setllingIcon);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        RepairObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClick(convertView, obj);
        return convertView;
    }

    private void setOnClick(View view, final RepairObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepairStoreObjHandler.save(obj.getStoreObj());
                Bundle b = new Bundle();
                b.putString(RepairInputMessageActivity.REPAIR_ID_KEY, obj.getObjectId());
                switch (obj.getOrder_status()) {
                    case "1":
                        Passageway.jumpActivity(context, WaitingRepairStoreActivity.class, b);
//                        Passageway.jumpActivity(context, RepairStoreListActivity.class, b);
                        break;
                    case "2":
                        b.putBoolean("isPlay", false);
                        Passageway.jumpActivity(context, RepairPlayDataActivity.class, b);
                        break;
                    case "3":
                        b.putBoolean("isPlay", true);
                        Passageway.jumpActivity(context, RepairPlayDataActivity.class, b);
                        break;
                    default:
                        Passageway.jumpActivity(context, RepairActivity.class, b);
                        break;
                }
            }
        });
    }

    private void setView(HolderView holder, RepairObj obj) {

        holder.time.setText(obj.getCreatedAt());
        holder.address.setText(obj.getStoreObj().getName());
        holder.maney.setText("￥" + obj.getStoreObj().getFee());
        holder.tel.setText(obj.getStoreObj().getContact());

        holder.status.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        holder.status.setBackgroundResource(R.drawable.status_gray_box);
        switch (obj.getOrder_status()) {
            case "1":
                holder.status.setText("等待中");
                break;
            case "2":
                holder.status.setText("未付款");
                holder.status.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
                holder.status.setBackgroundResource(R.drawable.status_red_box);
                break;
            case "3":
                holder.status.setText("已付款");
                break;
            default:
                holder.status.setText("未发布");
                break;
        }

        if (obj.isSettling()) {
            holder.setllingIcon.setVisibility(View.VISIBLE);
        } else {
            holder.setllingIcon.setVisibility(View.GONE);
        }

    }

    class HolderView {
        TextView time;
        TextView maney;
        TextView address;
        TextView tel;
        TextView status;
        ImageView setllingIcon;
    }

}
