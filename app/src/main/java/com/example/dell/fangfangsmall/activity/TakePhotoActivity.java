package com.example.dell.fangfangsmall.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.util.JumpItent;

/**
 * 进行拍照   
 *@author Guanluocang
 *created at 2017/9/13 11:29
*/
public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mMainFirst;//首页
    private TextView mMainTwo;//视频
    private TextView mMainThree;//语音
    private TextView mMainFour;//训练
    private TextView mTakePhoto;

    private boolean isTakePhoto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        initView();
        initListener();

    }

    private void initView() {
        (mMainFirst) = (TextView) findViewById(R.id.tv_main_first);
        (mMainTwo) = (TextView) findViewById(R.id.tv_main_two);
        (mMainThree) = (TextView) findViewById(R.id.tv_main_three);
        (mMainFour) = (TextView) findViewById(R.id.tv_main_four);
        (mTakePhoto) = (TextView) findViewById(R.id.tv_take_photo);
    }

    private void initListener() {
        mMainFirst.setOnClickListener(this);
        mMainTwo.setOnClickListener(this);
        mMainThree.setOnClickListener(this);
        mMainFour.setOnClickListener(this);
        mTakePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_first:
                JumpItent.jump(TakePhotoActivity.this, MainActivity.class);
                finish();
                break;
            case R.id.tv_main_two:
                JumpItent.jump(TakePhotoActivity.this, VideoActivity.class);
                finish();
                break;
            case R.id.tv_main_three:
                JumpItent.jump(TakePhotoActivity.this, VoiceActivity.class);
                finish();
                break;
            case R.id.tv_main_four:
                finish();
//                JumpItent.jump(TakePhotoActivity.this, TrainActivity.class);
                break;
            case R.id.tv_take_photo:
//                if (isTakePhoto) {
//                    mTakePhoto.setBackgroundResource(R.mipmap.ic_take_background_pressed);
//                } else {
//                    mTakePhoto.setBackgroundResource(R.mipmap.ic_take_background);
//                }
//                isTakePhoto = !isTakePhoto;
                break;
        }

    }
}
