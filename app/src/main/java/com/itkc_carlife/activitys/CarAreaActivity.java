package com.itkc_carlife.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.box.SettlingObj;
import com.itkc_carlife.box.handler.SettlingObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 16/1/18.
 */
public class CarAreaActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.carArea_majorTag)
    private TextView majorTag;
    @ViewInject(R.id.carArea_secondaryTag)
    private TextView secondaryTag;
    @ViewInject(R.id.carArea_gridView)
    private GridView gridView;

    private SettlingObj majorSettling;
    private SettlingObj secondarySettling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car_area);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
        }
    }

    @OnClick({R.id.carArea_majorTag, R.id.carArea_secondaryTag})
    public void onTag(View view) {
        initTagText();
        switch (view.getId()) {
            case R.id.carArea_majorTag:
                majorTag.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
                setimageGrid(majorSettling.getAreaImageList());
                break;
            case R.id.carArea_secondaryTag:
                secondaryTag.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue));
                setimageGrid(secondarySettling.getAreaImageList());
                break;
        }
    }

    private void initTagText() {
        majorTag.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue_02));
        secondaryTag.setTextColor(ColorHandle.getColorForID(context, R.color.text_blue_02));
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("事故处理单");

        majorSettling = SettlingObjHandler.getMajorSettling();
        secondarySettling = SettlingObjHandler.getSecondarySettling();

        if (majorSettling == null || secondarySettling == null) {

            MessageHandler.showToast(context,"暂未上传图片");
            finish();
        } else {
            setView(majorSettling, secondarySettling);
        }

    }

    private void setView(SettlingObj majorSettling, SettlingObj secondarySettling) {
        majorTag.setText(majorSettling.getCarInfo().getCar_number());
        secondaryTag.setText(secondarySettling.getCarInfo().getCar_number());

        majorTag.performClick();
    }

    public void setimageGrid(List<String> list) {
        gridView.setAdapter(new ImageAdapter(list));
    }

    class ImageAdapter extends BaseAdapter {

        List<String> list;
        LayoutInflater inflater;

        ImageAdapter(List<String> list) {
            this.list = list;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int w = WinTool.getWinWidth(context) / 6;

            ImageView image = new ImageView(context);

            image.setLayoutParams(new GridView.LayoutParams(w, w));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            DownloadImageLoader.loadImage(image, list.get(position));
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putBoolean(ImageActivity.IS_ONLINE, true);
                    b.putString(ImageActivity.URL, list.get(position));
                    Passageway.jumpActivity(context, ImageActivity.class, b);
                }
            });
            return image;
        }

    }
}
