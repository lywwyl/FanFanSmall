package com.example.dell.fangfangsmall.homevideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.base.ControlBaseActivity;
import com.yuntongxun.ecsdk.voip.video.ECOpenGlView;

/**
 * 首页视频对话
 *
 * @author Guanluocang
 *         created at 2017/9/22 18:33
 */

public class HomeVideoCallActivity extends ControlBaseActivity<HomeVideoPresenter> implements IHomeVideoView, View.OnClickListener {

    //视频View
    private ECOpenGlView mRemoteVideoView, mLocalVideoView;
    private TextView mTip;

    private ImageView mClose;

    @Override
    public HomeVideoPresenter createPresenter() {
        return new HomeVideoPresenter(this);
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_home_video_call;
    }

    @Override
    protected void onViewInit() {
        super.onViewInit();
        (mRemoteVideoView) = (ECOpenGlView) findViewById(R.id.remote_video_view);
        mRemoteVideoView.setGlType(ECOpenGlView.RenderType.RENDER_REMOTE);
        mRemoteVideoView.setAspectMode(ECOpenGlView.AspectMode.CROP);

        (mLocalVideoView) = (ECOpenGlView) findViewById(R.id.local_video_view);
        mLocalVideoView.setGlType(ECOpenGlView.RenderType.RENDER_PREVIEW);
        mLocalVideoView.setAspectMode(ECOpenGlView.AspectMode.CROP);

        (mTip) = (TextView) findViewById(R.id.tv_tip);
        (mClose) = (ImageView) findViewById(R.id.iv_close);

        mPresenter.attachGlView(mLocalVideoView, mRemoteVideoView);
    }

    @Override
    protected void setOnListener() {
        super.setOnListener();
        mClose.setOnClickListener(this);
    }


    @Override
    public void setTopText(String text) {
        mTip.setText(text);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setRemoteVisiable(boolean visiable) {
        mRemoteVideoView.setVisibility(visiable ? View.VISIBLE : View.GONE);
//        mLocalVideoView.setVisibility(visiable ? View.VISIBLE : View.GONE);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                mPresenter.ReleaseCall();
                break;
        }
    }
}
