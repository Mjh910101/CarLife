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
import com.itkc_carlife.download.DownloadImageLoader;
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
 * Created by Hua on 16/1/20.
 */
public class ImageGridActivity extends BaseActivity {

    public final static String LIST_KEY = "list";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.imageGrid_dataGrid)
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_grid);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.imageGrid_backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
            case R.id.imageGrid_backBtn:
                finish();
                break;
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("事故处理单");


        Bundle b = getIntent().getExtras();
        if (b != null) {
            gridView.setAdapter(new ImageAdapter(b.getStringArrayList(LIST_KEY)));
        }
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
