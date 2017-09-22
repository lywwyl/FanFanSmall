package com.example.dell.fangfangsmall.login;

import android.os.Bundle;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.MainActivity;
import com.example.dell.fangfangsmall.base.ControlBasePresenter;
import com.ocean.mvp.library.utils.L;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECNotifyOptions;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;

import static com.example.dell.fangfangsmall.login.SingleLogin.isInitSuccess;


/**
 * Created by dell on 2017/8/1.
 */

public class LoginPresenter extends ControlBasePresenter<ILoginView> {

    private String username = "17600738557";
    private String appKey = "8a216da85ea31fdd015ea399cb4b0075";
    private String token = "c3f9bc4ed6f62a1b2888f4dc4d6604ab";
    private SingleLogin mLogin;
    private ECNotifyOptions mOptions;

    public LoginPresenter(ILoginView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mLogin = SingleLogin.getInstance(getContext(), mCallId);
        mLogin.initInitialized();
        mLogin.setOnInitListener(initListener);
    }

    SingleLogin.OnInitListener initListener = new SingleLogin.OnInitListener() {
        @Override
        public void onSuccess() {
            L.e("key", "loginSuccess");
//            DoLogin();
        }

        @Override
        public void onError(Exception exception) {
            L.e("key", "loginError");
        }
    };

    void DoLogin() {
        if (isInitSuccess) {
            loginMain();
        } else {
            showToast("初始化失败");
        }
    }

    private void loginMain() {
        if (!isInitSuccess)
            return;
        L.e("key", "初始化SDK及登陆代码完成");
        /**
         * 设置接收VoIP来电事件通知Intent
         * 呼入界面activity、开发者需修改该类
         * */
//        Intent intent = new Intent(getContext(), VoiceView.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        ECDevice.setPendingIntent(pendingIntent);

        /**
         * 登录回调
         */
        ECDevice.setOnDeviceConnectListener(onECDeviceConnectListener);

        ECInitParams params = ECInitParams.createParams();
        params.setUserid(username);
        params.setAppKey(appKey);
        params.setToken(token);
        //设置登陆验证模式：自定义登录方式
        params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        //DloginMode（强制上线：FORCE_DlogIN  默认登录：AUTO。使用方式详见注意事项）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

        /**
         * 验证参数是否正确
         * */
        if (params.validate()) {
            // 登录函数
            ECDevice.login(params);
        }
        // 设置VOIP 自定义铃声路径
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if (setupManager != null) {
            // 目前支持下面三种路径查找方式
            // 1、如果是assets目录则设置为前缀[assets://]
            setupManager.setInComingRingUrl(true, "assets://phonering.mp3");
            setupManager.setOutGoingRingUrl(true, "assets://phonering.mp3");
            setupManager.setBusyRingTone(true, "assets://played.mp3");
            // 2、如果是raw目录则设置为前缀[raw://]
            // 3、如果是SDCard目录则设置为前缀[file://]
        }
    }

    /**
     * 设置通知回调监听包含登录状态监听，接收消息监听，呼叫事件回调监听和
     * 设置接收来电事件通知Intent等
     */
    private ECDevice.OnECDeviceConnectListener onECDeviceConnectListener = new ECDevice.OnECDeviceConnectListener() {
        public void onConnect() {
            //兼容旧版本的方法，不必处理
        }

        @Override
        public void onDisconnect(ECError error) {
            //兼容旧版本的方法，不必处理
        }

        @Override
        public void onConnectState(ECDevice.ECConnectState state, ECError error) {
            if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
                if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                    L.e("key", "==帐号异地登陆");
                } else {
                    L.e("key", "==其他登录失败,错误码：" + error.errorMsg + error.errorCode);
                    showToast("正在重新登录。。。");
                    DoLogin();
                }
                return;
            } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
                L.e("key", "==登陆成功" + error.toString());
                startActivity(MainActivity.class);
            }
        }
    };

    private void initNotifyOptions() {
        if (mOptions == null) {
            mOptions = new ECNotifyOptions();
        }
        // 设置新消息是否提醒
        mOptions.setNewMsgNotify(true);
        // 设置状态栏通知图标
        mOptions.setIcon(R.mipmap.ic_launcher);
        // 设置是否启用勿扰模式（不会声音/震动提醒）
        mOptions.setSilenceEnable(false);
        // 设置勿扰模式时间段（开始小时/开始分钟-结束小时/结束分钟）
        // 小时采用24小时制
        // 如果设置勿扰模式不启用，则设置勿扰时间段无效
        // 当前设置晚上11点到第二天早上8点之间不提醒
        mOptions.setSilenceTime(23, 0, 8, 0);
        // 设置是否震动提醒(如果处于免打扰模式则设置无效，没有震动)
        mOptions.enableShake(true);
        // 设置是否声音提醒(如果处于免打扰模式则设置无效，没有声音)
        mOptions.enableSound(true);
    }
}
