package com.example.dell.fangfangsmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.util.JumpItent;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mMainFirst;//首页
    private TextView mMainTwo;//视频
    private TextView mMainThree;//语音
    private TextView mMainFour;//训练

    private TextView mVideoOne, mVideoTwo, mVideoThree, mVideoFour, mVideoFive, mVideoSix;
    private ImageView iv_nextPage, iv_upwardPage;

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
        (iv_nextPage) = (ImageView) findViewById(R.id.iv_nextPage);
        (iv_upwardPage) = (ImageView) findViewById(R.id.iv_upwardPage);

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
        iv_nextPage.setOnClickListener(this);
        iv_upwardPage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
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
                bundle.putString("Url", "http://vjs.zencdn.net/v/oceans.mp4");
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_two:
                bundle.putString("Url", "http://ohjdda8lm.bkt.clouddn.com/course/sample1.mp4");
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_three:
                bundle.putString("Url", "http://mp4.vjshi.com/2017-06-17/b35abad666599dfc86447eb6e75ce88a.mp4");
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_four:
                bundle.putString("Url", "http://mp4.vjshi.com/2016-02-25/2015-b8b4eb2656ac728134371e0f395d5028.mp4");
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_five:
                bundle.putString("Url", "http://mp4.vjshi.com/2016-02-19/2015-7f79ff9992ea1bbb5c901872d0f5fc25.mp4");
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_six:
                bundle.putString("Url", "http://mp4.vjshi.com/2014-06-13/2014061315010763299.mp4");
                JumpItent.jump(VideoActivity.this, VideoDetailActivity.class, bundle);
                break;
            case R.id.iv_nextPage:
                Toast.makeText(VideoActivity.this, "下一页", Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_upwardPage:
                Toast.makeText(VideoActivity.this, "上一页", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
