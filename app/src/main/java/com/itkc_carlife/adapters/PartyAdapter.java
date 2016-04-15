package com.itkc_carlife.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.activitys.AddPartyActivity;
import com.itkc_carlife.activitys.ParytActivity;
import com.itkc_carlife.activitys.UserPartyListActivity;
import com.itkc_carlife.box.PartyObj;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONObject;

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
 * Created by Hua on 16/2/1.
 */
public class PartyAdapter extends BaseAdapter {

    private Context context;
    private List<PartyObj> itemList;
    private LayoutInflater inflater;
    private int type;

    public PartyAdapter(Context context, List<PartyObj> list) {
        this(context, list, 0);
    }

    public PartyAdapter(Context context, List<PartyObj> list, int type) {
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
                    R.layout.layout_party_item, null);
            holder = new HolderView();

            holder.pic = (ImageView) convertView.findViewById(R.id.partyItem_pic);
            holder.title = (TextView) convertView.findViewById(R.id.partyItem_title);
            holder.type = (TextView) convertView.findViewById(R.id.partyItem_type);
            holder.descript = (TextView) convertView.findViewById(R.id.partyItem_descript);
            holder.inText = (TextView) convertView.findViewById(R.id.partyItem_inText);
            holder.inIcon = (ImageView) convertView.findViewById(R.id.partyItem_inIcon);
            holder.statusText = (TextView) convertView.findViewById(R.id.partyItem_statusText);
            holder.statusIcon = (ImageView) convertView.findViewById(R.id.partyItem_statusIcon);
            holder.deleteIcon = (ImageView) convertView.findViewById(R.id.partyItem_deleteIcon);
            holder.contentIcon = (ImageView) convertView.findViewById(R.id.partyItem_contentIcon);
            holder.likeIcon = (ImageView) convertView.findViewById(R.id.partyItem_likeIcon);
            holder.removeIcon = (ImageView) convertView.findViewById(R.id.partyItem_removeIcon);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        PartyObj obj = itemList.get(position);
        setView(holder, obj);
        setOnDelete(holder.deleteIcon, obj);
        setOnLike(holder.likeIcon, obj, type);
        setOnRemove(holder.removeIcon, obj, type);
        setOnClickView(convertView, obj);
        setToolView(holder, obj);

        return convertView;
    }


    private void setToolView(HolderView holder, PartyObj obj) {
        if (type == 0) {
            return;
        }
        holder.contentIcon.setVisibility(View.VISIBLE);
        switch (type) {
            case UserPartyListActivity.JOIN:
                setJoinView(holder, obj);
                break;
            case UserPartyListActivity.FAVOR:
                setFavorView(holder, obj);
                break;
            case UserPartyListActivity.POST:
                setPostView(holder, obj);
                break;
        }
    }

    private void setPostView(HolderView holder, PartyObj obj) {
        holder.deleteIcon.setVisibility(View.VISIBLE);
        holder.inIcon.setVisibility(View.VISIBLE);
        holder.inText.setVisibility(View.VISIBLE);
        holder.statusIcon.setVisibility(View.VISIBLE);
        holder.statusText.setVisibility(View.VISIBLE);

        if (obj.getStatus() == 1 || obj.getStatus() == 0) {
            holder.inIcon.setImageResource(R.drawable.stare_party_icon);
            holder.inText.setTextColor(ColorHandle.getColorForID(context, R.color.bule));
        } else {
            holder.inIcon.setImageResource(R.drawable.over_party_icon);
            holder.inText.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        }
        holder.inText.setText(obj.getStatusLabel());

        holder.statusIcon.setImageResource(R.drawable.prople_party_icon);
        holder.statusText.setText("名额" + obj.getJoin_total() + "/" + obj.getLimit());

    }


    public void setJoinView(HolderView holder, PartyObj obj) {
        holder.inIcon.setVisibility(View.VISIBLE);
        holder.inText.setVisibility(View.VISIBLE);
//        holder.removeIcon.setVisibility(View.VISIBLE);

        holder.inIcon.setImageResource(R.drawable.in_prity_icon);
        holder.inText.setText("已参加");

        if (obj.isfavor()) {
            holder.likeIcon.setVisibility(View.VISIBLE);
        }

        if (obj.getStatus()!=2) {
            holder.removeIcon.setVisibility(View.VISIBLE);
        }
    }

    private void setFavorView(HolderView holder, PartyObj obj) {
//        holder.deleteIcon.setVisibility(View.VISIBLE);
        holder.likeIcon.setVisibility(View.VISIBLE);
        holder.inIcon.setVisibility(View.VISIBLE);
        holder.inText.setVisibility(View.VISIBLE);
        holder.statusIcon.setVisibility(View.VISIBLE);
        holder.statusText.setVisibility(View.VISIBLE);
        if (obj.isjoin()) {
            holder.inIcon.setImageResource(R.drawable.in_prity_icon);
            holder.inText.setText("已参加");
        } else {
            holder.inIcon.setImageResource(R.drawable.out_prity_icon);
            holder.inText.setText("未参加");
            holder.inText.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        }

        switch (obj.getStatus()) {
            case 2:
                holder.statusIcon.setImageResource(R.drawable.over_party_icon);
                holder.statusText.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
                holder.statusText.setText(obj.getStatusLabel());
                break;
            default:
                holder.statusIcon.setImageResource(R.drawable.stare_party_icon);
                holder.statusText.setTextColor(ColorHandle.getColorForID(context, R.color.bule));
                holder.statusText.setText(obj.getStatusLabel());
                break;
        }

        if (obj.isjoin()&&obj.getStatus()!=2) {
            holder.removeIcon.setVisibility(View.VISIBLE);
        }
    }

    private void deleteItem(PartyObj obj) {
        itemList.remove(obj);
        notifyDataSetChanged();
    }

    private void setOnRemove(final ImageView view, final PartyObj obj, final int type) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavoreObj(obj);
                if (type == UserPartyListActivity.JOIN) {
                    deleteItem(obj);
                }else{
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setOnLike(final ImageView view, final PartyObj obj, final int type) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavoreObj(obj);
                if (type == UserPartyListActivity.FAVOR) {
                    deleteItem(obj);
                }else{
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setOnDelete(ImageView view, final PartyObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteObj(obj);
                deleteItem(obj);
            }
        });
    }

    private void deleteObj(PartyObj obj) {
        String url = UrlHandler.getPost() + "/" + obj.getObjectId();
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.DELETE, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            JSONObject resultJson = JsonHandle.getJSON(json, "result");
                            if (status == 1) {
                            }
                        }
                    }

                });
    }

    private void removeFavoreObj(PartyObj obj) {
        postObj(obj, UrlHandler.getJoinPostRemove());
    }

    private void deleteFavoreObj(PartyObj obj) {
        postObj(obj, UrlHandler.getFavorPostRemove());
    }

    private void postObj(PartyObj obj, String url) {
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("id", obj.getObjectId());

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            JSONObject resultJson = JsonHandle.getJSON(json, "result");
                            if (status == 1) {
                            }
                        }
                    }

                });
    }


    private void setOnClickView(View view, final PartyObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(ParytActivity.ID_KEY, obj.getObjectId());
                Passageway.jumpActivity(context, ParytActivity.class, AddPartyActivity.REQUEST_CODE, b);
            }
        });
    }

    private void setView(HolderView holder, PartyObj obj) {
        if (type == 0) {
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText(obj.getType());
            holder.descript.setText(obj.getInfo());
        } else {
            holder.type.setVisibility(View.GONE);
            holder.descript.setText(obj.getType() + "/" + obj.getInfo());
        }
        holder.title.setText(obj.getTitle());
        setPic(holder.pic, obj.getCoverUrl());
    }

    private void setPic(ImageView pic, String coverUrl) {
        double w = WinTool.getWinWidth(context) / 5;
        double h = w / 17 * 23;
        pic.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DownloadImageLoader.loadImage(pic, coverUrl);
    }

    class HolderView {

        ImageView pic;
        TextView title;
        TextView type;
        TextView descript;
        ImageView deleteIcon;
        ImageView contentIcon;
        ImageView likeIcon;
        ImageView removeIcon;
        ImageView inIcon;
        TextView inText;
        ImageView statusIcon;
        TextView statusText;
    }

    public void addItems(List<PartyObj> list) {
        for (PartyObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }
}
