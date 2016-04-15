package com.itkc_carlife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.ServersCommentObj;

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
 * Created by Hua on 16/2/19.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<ServersCommentObj> itemList;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<ServersCommentObj> list) {
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
                    R.layout.layout_comment_item, null);
            holder = new HolderView();

            holder.tel = (TextView) convertView.findViewById(R.id.commentItem_tel);
            holder.day = (TextView) convertView.findViewById(R.id.commentItem_day);
            holder.comment = (TextView) convertView.findViewById(R.id.commentItem_comment);
            holder.startText = (TextView) convertView.findViewById(R.id.commentItem_startText);
            holder.startBox = (LinearLayout) convertView.findViewById(R.id.commentItem_startBox);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        ServersCommentObj obj = itemList.get(position);
        setView(holder, obj);

        return convertView;
    }

    private void setView(HolderView holder, ServersCommentObj obj) {
        holder.tel.setText(obj.getUserGoneTel());
        holder.day.setText(obj.getCreatedAt());
        holder.comment.setText(obj.getContent());
        holder.startBox.removeAllViews();
        for (int i = 0; i < obj.getRate(); i++) {
            holder.startBox.addView(getStars());
        }
        switch (obj.getRate()) {
            case 3:
                holder.startText.setText("好评");
                break;
            case 2:
                holder.startText.setText("中评");
                break;
            default:
                holder.startText.setText("差评");
                break;
        }
    }

    public ImageView getStars() {
        int p = 3;
        ImageView stars = new ImageView(context);
        stars.setImageResource(R.drawable.stars_icon);
        stars.setPadding(p, p, p, p);
        return stars;
    }

    public void addItem(List<ServersCommentObj> list) {
        for (ServersCommentObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }

    class HolderView {

        TextView tel;
        TextView day;
        TextView comment;
        LinearLayout startBox;
        TextView startText;

    }

}
