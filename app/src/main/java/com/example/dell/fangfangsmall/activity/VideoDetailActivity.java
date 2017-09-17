package com.example.dell.fangfangsmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.fangfangsmall.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 视频播放详情
 *
 * @author Guanluocang
 *         created at 2017/9/17 9:30
 */
public class VideoDetailActivity extends AppCompatActivity {

    private JCVideoPlayerStandard mJcVideo;

    private String upfile;//视频地址
    private String title;//视频地址;
    private String pic;//视频图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        initView();
        initData();
    }

    private void initView() {

        (mJcVideo) = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
    }

    private void initData() {
        //视频播放地址
//        mJcVideo.setUp(upfile
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
//        //视频暂未播放静止页面
//        Picasso.with(VideoDetailActivity.this)
//                .load( pic)
//                .into(mJcVideo.thumbImageView);
//
//        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());

    }
}
