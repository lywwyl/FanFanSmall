package com.example.dell.fangfangsmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.dell.fangfangsmall.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * 视频播放详情
 *
 * @author Guanluocang
 *         created at 2017/9/17 9:30
 */
public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private JCVideoPlayerStandard mJcVideo;

    private String upfile;//视频地址
    private String title;//视频地址;
    private String pic;//视频图片

    private ImageView mTopBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        initView();
        initData();
        initListener();
    }


    private void initView() {

        (mJcVideo) = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        (mTopBack) = (ImageView) findViewById(R.id.iv_top_back);
    }

    private void initData() {
        upfile = getIntent().getStringExtra("Url");
        mJcVideo.setUp(upfile, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        mJcVideo.startVideo();
        //视频暂未播放静止页面
//        Picasso.with(VideoDetailActivity.this)
//                .load(Config.pic_base_url + pic)
//                .into(mJcVideo.thumbImageView);
//        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
    }

    private void initListener() {
        mTopBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_back:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJcVideo.releaseAllVideos();
    }
}
