package com.example.dell.fangfangsmall.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.util.JumpItent;

public class TrainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mMainFirst;//首页
    private TextView mMainTwo;//视频
    private TextView mMainThree;//语音
    private TextView mMainFour;//训练
    private RelativeLayout mTakephoto;//拍照
    private RelativeLayout mFace;//人脸提取

    public static final int RESULT_CODE_STARTVIDEO = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        initView();
        initListener();
    }

    private void initView() {
        (mMainFirst) = (TextView) findViewById(R.id.tv_main_first);
        (mMainTwo) = (TextView) findViewById(R.id.tv_main_two);
        (mMainThree) = (TextView) findViewById(R.id.tv_main_three);
        (mMainFour) = (TextView) findViewById(R.id.tv_main_four);
        (mTakephoto) = (RelativeLayout) findViewById(R.id.rl_takephoto);
        (mFace) = (RelativeLayout) findViewById(R.id.rl_face);
    }

    private void initListener() {
        mMainFirst.setOnClickListener(this);
        mMainTwo.setOnClickListener(this);
        mMainThree.setOnClickListener(this);
        mMainFour.setOnClickListener(this);
        mTakephoto.setOnClickListener(this);
        mFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_first:
                JumpItent.jump(TrainActivity.this, MainActivity.class);
                finish();
                break;
            case R.id.tv_main_two:
                JumpItent.jump(TrainActivity.this, VideoActivity.class);
                finish();
                break;
            case R.id.tv_main_three:
                JumpItent.jump(TrainActivity.this, VoiceActivity.class);
                finish();
                break;
            case R.id.tv_main_four:
//                JumpItent.jump(TrainActivity.this, TrainActivity.class);
                break;
            case R.id.rl_takephoto:
                JumpItent.jump(TrainActivity.this, TakePhotoActivity.class);
                break;
            case R.id.rl_face:
                videoPermission();
                break;

        }
    }
    //视频权限
    public void videoPermission() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.
                checkSelfPermission(TrainActivity.this, android.Manifest.permission.CAMERA)) {
            JumpItent.jump(TrainActivity.this, FaceExtractActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //提示用户开户权限
                String[] perms = {"android.permission.CAMERA"};
                ActivityCompat.requestPermissions(TrainActivity.this, perms, RESULT_CODE_STARTVIDEO);
            }
        }
    }
    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RESULT_CODE_STARTVIDEO:
                boolean albumAccepted_video = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!albumAccepted_video) {
                    Toast.makeText(TrainActivity.this, "请开启应用视频权限", Toast.LENGTH_LONG).show();
                } else {
                    JumpItent.jump(TrainActivity.this, FaceExtractActivity.class);
                }
                break;
        }
    }
}
