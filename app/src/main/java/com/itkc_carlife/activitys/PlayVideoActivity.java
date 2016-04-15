package com.itkc_carlife.activitys;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.itkc_carlife.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 16/1/15.
 */
public class PlayVideoActivity extends BaseActivity {

    public final static int REQUEST_CODE = 104;

    public final static String IS_FILE = "isFile";
    public final static String IS_DELETE = "isDelete";
    public final static String PATH = "path";
    public final static String URL = "url";


    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleBg)
    private RelativeLayout titleBg;
    @ViewInject(R.id.video_videoView)
    private VideoView videoView;
    @ViewInject(R.id.video_playIcon)
    private ImageView playIcon;
    @ViewInject(R.id.video_btnBox)
    private LinearLayout btnBox;

    private Bundle mBundle;
    private boolean isFile;
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_video);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnCompletionListener();
    }

    @OnClick({R.id.title_backBtn, R.id.video_saveBtn, R.id.video_playIcon, R.id.video_deleteBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
            case R.id.video_saveBtn:
                finish();
                break;
            case R.id.video_playIcon:
                playStart();
                break;
            case R.id.video_deleteBtn:
                closeForDelete();
                break;
        }
    }

    private void closeForDelete() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putBoolean(IS_DELETE, true);
        i.putExtras(b);
        setResult(REQUEST_CODE, i);
        finish();
    }

    private void playStart() {
//        playIcon.setVisibility(View.GONE);
        videoView.start();
        videoView.requestFocus();
    }

    private void setOnCompletionListener() {
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
//                playIcon.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleBg.setBackgroundResource(R.color.text_black);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            controller = new MediaController(context);
            videoView.setMediaController(controller);
            controller.setMediaPlayer(videoView);
            controller.show(0);

            isFile = mBundle.getBoolean(IS_FILE);
            if (isFile) {
                btnBox.setVisibility(View.VISIBLE);
                videoView.setVideoPath(mBundle.getString(PATH));
            } else {
//                btnBox.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse(mBundle.getString(URL)));
            }
            playStart();
        } else {
            finish();
        }

    }
}
