package com.example.dell.fangfangsmall.homevideo;


import android.app.Activity;
import android.os.Bundle;

import com.example.dell.fangfangsmall.base.ControlBasePresenter;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.voip.video.ECOpenGlView;

/**
 * Created by dell on 2017/9/21.
 */

public class HomeVideoPresenter extends ControlBasePresenter<IHomeVideoView> {


    public HomeVideoPresenter(IHomeVideoView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        initData();
    }

    private void initData() {
        if (mIncomingCall) {
            // 来电
            //获取当前的callid
            mCallId = ((Activity) mView).getIntent().getStringExtra(ECDevice.CALLID);
            mCallNumber = ((Activity) mView).getIntent().getStringExtra(ECDevice.CALLER);
//            mView.setBottomVisible(true);
            mView.setTopText(mCallNumber + "向您发起视频");
        }

    }


    /**
     * 初始化界面
     * 如果视频呼叫，则在接受呼叫之前，需要先设置视频通话显示的view
     * localView本地显示视频的view
     * view 显示远端视频的surfaceview
     */
    public void attachGlView(ECOpenGlView localView, ECOpenGlView remoteView) {
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if (setupManager == null) {
            return;
        }
        setupManager.setGlDisplayWindow(localView, remoteView);
    }


}
