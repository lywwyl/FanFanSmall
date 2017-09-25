package com.example.dell.fangfangsmall.login;

import android.content.Context;
import android.util.Log;

import com.example.dell.fangfangsmall.util.ReceiveMessage;
import com.ocean.mvp.library.utils.L;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.OnMeetingListener;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.VoipMediaChangedInfo;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;
import com.yuntongxun.ecsdk.meeting.intercom.ECInterPhoneMeetingMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMsg;

import java.util.List;

/**
 * Created by dell on 2017/8/1.
 */

public class SingleLogin {

    public static boolean isInitSuccess = false;

    private String mCallId;
    private Context mContext;
    public volatile static SingleLogin login = null;
    private OnInitListener onInitListener;
    private ReceiveMessage receive;

    public void setReceive(ReceiveMessage receive) {
        this.receive = receive;
    }
    //    public SingleLogin(ReceiveMessage receive ) {
//        this.receive=receive;
//    }

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

                    }

                    @Override
                    public void onReceiveInterPhoneMeetingMsg(ECInterPhoneMeetingMsg msg) {
                        // 处理实时对讲消息Push
                    }

                    @Override
                    public void onReceiveVoiceMeetingMsg(ECVoiceMeetingMsg msg) {
                        // 处理语音会议消息push
                    }

                    @Override
                    public void onReceiveVideoMeetingMsg(ECVideoMeetingMsg msg) {
                        // 处理视频会议消息Push（暂未提供）
                    }

                    @Override
                    public void onMeetingPermission(String s) {

                    }
                });
            }
            /***
             * 语音通话的回调监听
             * */
            final ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();
            if (callInterface != null) {
                callInterface.setOnVoIPCallListener(new ECVoIPCallManager.OnVoIPListener() {
                    @Override
                    public void onVideoRatioChanged(VideoRatio videoRatio) {
                    }

                    @Override
                    public void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {
                    }

                    @Override
                    public void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {
                    }

                    @Override
                    public void onDtmfReceived(String s, char c) {
                    }

                    @Override
                    public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
                        // 处理呼叫事件回调
                        if (voipCall == null) {
                            return;
                        }
                        // 根据不同的事件通知类型来处理不同的业务
                        ECVoIPCallManager.ECCallState callState = voipCall.callState;
                        switch (callState) {
                            case ECCALL_PROCEEDING:
                                L.i("key", "正在连接服务器处理呼叫请求，callid：" + voipCall.callId);
                                break;
                            case ECCALL_ALERTING:
                                L.i("key", "呼叫到达对方，正在振铃，callid：" + voipCall.callId);
                                break;
                            case ECCALL_ANSWERED:
                                L.i("key", "对方接听本次呼叫,callid：" + voipCall.callId);
                                break;
                            case ECCALL_FAILED:
                                // 本次呼叫失败，根据失败原因进行业务处理或跳转
                                L.i("key", "called:" + voipCall.callId + ",reason:" + voipCall.reason);
                                break;
                            case ECCALL_RELEASED:
                                // 通话释放[完成一次呼叫]
                                ECDevice.getECVoIPCallManager().releaseCall(mCallId);
                                break;
                            default:
                                L.i("key", "handle call event error , callState " + callState);
                                break;
                        }
                    }

                    @Override
                    public void onMediaDestinationChanged(VoipMediaChangedInfo voipMediaChangedInfo) {

                    }
                });
            }

            //IM接收消息监听，使用IM功能的开发者需要设置。
            ECDevice.setOnChatReceiveListener(new OnChatReceiveListener() {
                @Override
                public void OnReceivedMessage(ECMessage msg) {
                 if(receive != null) {
                     receive.OnReceivedMessage(msg);
                 }


                }

                @Override
                public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {

                }

                @Override
                public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
                    //收到群组通知消息,可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
                    Log.i("", "==收到群组通知消息（有人加入、退出...）");
                }

                @Override
                public void onOfflineMessageCount(int count) {
                    // 登陆成功之后SDK回调该接口通知帐号离线消息数
                }

                @Override
                public int onGetOfflineMessage() {
                    return 0;
                }

                @Override
                public void onReceiveOfflineMessage(List msgs) {
                    // SDK根据应用设置的离线消息拉取规则通知应用离线消息
                }

                @Override
                public void onReceiveOfflineMessageCompletion() {
                    // SDK通知应用离线消息拉取完成
                }

                @Override
                public void onServicePersonVersion(int version) {
                    // SDK通知应用当前帐号的个人信息版本号
                }

                @Override
                public void onReceiveDeskMessage(ECMessage ecMessage) {

                }

                @Override
                public void onSoftVersion(String s, int i) {

                }
            });
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

//    public interface ReceiveMessage{
//
//        void OnReceivedMessage(ECMessage msg);
//    }
    public interface OnInitListener {
        void onSuccess();
        void onError(Exception exception);

    }


}
