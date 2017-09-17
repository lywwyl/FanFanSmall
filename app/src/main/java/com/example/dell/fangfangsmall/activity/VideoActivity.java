package com.example.dell.fangfangsmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.util.JumpItent;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mMainFirst;//首页
    private TextView mMainTwo;//视频
    private TextView mMainThree;//语音
    private TextView mMainFour;//训练

    private TextView mVideoOne, mVideoTwo, mVideoThree, mVideoFour, mVideoFive, mVideoSix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initListener();
    }

    private void initView() {
        (mMainFirst) = (TextView) findViewById(R.id.tv_main_first);
        (mMainTwo) = (TextView) findViewById(R.id.tv_main_two);
        (mMainThree) = (TextView) findViewById(R.id.tv_main_three);
        (mMainFour) = (TextView) findViewById(R.id.tv_main_four);

        (mVideoOne) = (TextView) findViewById(R.id.tv_video_one);
        (mVideoTwo) = (TextView) findViewById(R.id.tv_video_two);
        (mVideoThree) = (TextView) findViewById(R.id.tv_video_three);
        (mVideoFour) = (TextView) findViewById(R.id.tv_video_four);
        (mVideoFive) = (TextView) findViewById(R.id.tv_video_five);
        (mVideoSix) = (TextView) findViewById(R.id.tv_video_six);

    }

    private void initListener() {
        mMainFirst.setOnClickListener(this);
        mMainTwo.setOnClickListener(this);
        mMainThree.setOnClickListener(this);
        mMainFour.setOnClickListener(this);
        mVideoOne.setOnClickListener(this);
        mVideoTwo.setOnClickListener(this);
        mVideoThree.setOnClickListener(this);
        mVideoFour.setOnClickListener(this);
        mVideoFive.setOnClickListener(this);
        mVideoSix.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_first:
                JumpItent.jump(VideoActivity.this, MainActivity.class);
                finish();
                break;
            case R.id.tv_main_two:
//                JumpItent.jump(MainActivity.this, VideoActivity.class);
                break;
            case R.id.tv_main_three:
                JumpItent.jump(VideoActivity.this, VoiceActivity.class);
                finish();
                break;
            case R.id.tv_main_four:
                JumpItent.jump(VideoActivity.this, TrainActivity.class);
                finish();
                break;
            case R.id.tv_video_one:
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class);
                finish();
                break;

        }
    }
}
