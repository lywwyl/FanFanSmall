package com.example.dell.fangfangsmall.login;

import android.content.Context;

import com.ocean.mvp.library.utils.L;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.OnMeetingListener;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.meeting.intercom.ECInterPhoneMeetingMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMsg;

/**
 * Created by dell on 2017/8/1.
 */

public class SingleLogin {

    public static boolean isInitSuccess = false;

    private String mCallId;
    private Context mContext;
    public volatile static SingleLogin login = null;
    private OnInitListener onInitListener;


    public void setOnInitListener(OnInitListener onInitListener) {
        this.onInitListener = onInitListener;
    }

    public SingleLogin(Context mContext, String mCallId) {
        this.mContext = mContext;
        this.mCallId = mCallId;
    }

    public static SingleLogin getInstance(Context mContext, String callId) {
        if (login == null) {
            login = new SingleLogin(mContext, callId);
        }
        return login;
    }

    /**
     * 初始化
     */
    public void initInitialized() {
        //判断SDK是否已经初始化
        if (!ECDevice.isInitialized()) {
            ECDevice.initial(mContext, initListener);
        }
    }


    private ECDevice.InitListener initListener = new ECDevice.InitListener() {
        @Override
        public void onInitialized() {
            L.e("key", "初始化SDK成功");
            if (onInitListener != null)
                onInitListener.onSuccess();
            /**
             * 音视频回调
             * */
            if (ECDevice.getECMeetingManager() != null) {
                ECDevice.getECMeetingManager().setOnMeetingListener(new OnMeetingListener() {
                    @Override
                    public void onVideoRatioChanged(VideoRatio videoRatio) {
                        L.e("key", "初始化SDK成功");
                    }

                    @Override
                    public void onReceiveInterPhoneMeetingMsg(ECInterPhoneMeetingMsg msg) {
                        // 处理实时对讲消息Push
                        L.e("key", "初始化SDK成功");
                    }

                    @Override
                    public void onReceiveVoiceMeetingMsg(ECVoiceMeetingMsg msg) {
                        // 处理语音会议消息push
                        L.e("key", "初始化SDK成功");
                    }

                    @Override
                    public void onReceiveVideoMeetingMsg(ECVideoMeetingMsg msg) {
                        // 处理视频会议消息Push（暂未提供）
                        L.e("key", "初始化SDK成功");
                    }

                    @Override
                    public void onMeetingPermission(String s) {
                        L.e("key", "初始化SDK成功");
                    }
                });
            }
            isInitSuccess = true;
        }

        @Override
        public void onError(Exception exception) {
            L.e("key", "初始化SDK失败" + exception.getMessage());
            isInitSuccess = false;
            if (onInitListener != null)
                onInitListener.onError(exception);
        }
    };


    interface OnInitListener {
        void onSuccess();

        void onError(Exception exception);
    }


}
