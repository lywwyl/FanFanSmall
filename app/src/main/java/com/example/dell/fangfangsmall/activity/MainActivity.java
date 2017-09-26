package com.example.dell.fangfangsmall.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.asr.MyAiuiListener;
import com.example.dell.fangfangsmall.asr.MyRecognizerListener;
import com.example.dell.fangfangsmall.asr.MySpeech;
import com.example.dell.fangfangsmall.asr.MySynthesizerListener;
import com.example.dell.fangfangsmall.bean.Tab;
import com.example.dell.fangfangsmall.fragment.HomePageFragment;
import com.example.dell.fangfangsmall.fragment.TrainFragment;
import com.example.dell.fangfangsmall.fragment.VideoFragment;
import com.example.dell.fangfangsmall.fragment.VoiceFragment;
import com.example.dell.fangfangsmall.util.FragmentTabHost;
import com.example.dell.fangfangsmall.util.IatSettings;
import com.example.dell.fangfangsmall.util.PermissionsChecker;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.sunflower.FlowerCollector;
import com.yuntongxun.ecsdk.ECDevice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements VoiceFragment.OnDoAnswerListener, MySynthesizerListener.SynListener,
        MyAiuiListener.AiListener, MyRecognizerListener.RecognListener {


    private String mySpeechType;

    private LayoutInflater mInflater;
    public FragmentTabHost mFragmentTabHost;

    private List<Tab> mTabs = new ArrayList<>();
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private boolean quit = false; //设置退出标识
    private PermissionsChecker mChecker;
    private boolean isRequireCheck; // 是否需要系统权限检测
    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案


    //语音合成
    private SpeechSynthesizer mTts;
    //AIUI
    private AIUIAgent mAIUIAgent;
    //语音听写
    private SpeechRecognizer mIat;

    private int mAIUIState = AIUIConstant.STATE_IDLE;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    private SharedPreferences mSharedPreferences;

    private MySynthesizerListener synthesizerListener;
    private MyAiuiListener aiuiListener;
    private MyRecognizerListener recognizerListener;

    private Timer aiuiTimer;
    private TimerTask aiuiTimerTask;
    private Timer recognizerTimer;
    private TimerTask recognizerTimerTask;

    private HomePageFragment homePageFragment;
    private VoiceFragment voiceFragment;
    private VideoFragment videoFragment;

    private boolean isFirst;

    private boolean isTalking;

    /**
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);
        initView();
        initData();
        initSpeech();
        mChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            if (mChecker.lacksPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS); // 请求权限
            } else {
                allPermissionsGranted(); // 全部权限都已获取
            }
        } else {
            isRequireCheck = true;
        }
        if (isFirst) {
            judgeState();
        }
        isFirst = true;

    }

    private void judgeState() {
        if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VOICE)) {
            stopAiuiListener();
            startRecognizerListener();
        } else if (mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            stopRecognizerListener();
            startAiuiListener();
        } else if (mySpeechType.equals(MySpeech.SPEECH_NULL)) {
            stopListener();
        } else if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VIDEO)) {
            stopAiuiListener();
            startRecognizerListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
        stopListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListener();
        Log.e("GG", "onDestory");
    }

    @Override
    public void onBackPressed() {
        if (!quit) { //询问退出程序
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            new Timer(true).schedule(new TimerTask() { //启动定时任务
                @Override
                public void run() {
                    quit = false; //重置退出标识
                }
            }, 2000);
            quit = true;
        } else { //确认退出程序
            super.onBackPressed();
            finish();
            //退出时杀掉所有进程
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    /****************************************/
    private void initView() {
        (mFragmentTabHost) = (FragmentTabHost) findViewById(android.R.id.tabhost);
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);
    }

    private void initData() {
        Tab tab_home = new Tab(HomePageFragment.class, R.string.main_first, 0);
        Tab tab_video = new Tab(VideoFragment.class, R.string.main_two, 0);

        Tab tab_voice = new Tab(VoiceFragment.class, R.string.main_three, 0);
        Tab tab_train = new Tab(TrainFragment.class, R.string.main_four, 0);
        mTabs.add(tab_home);
        mTabs.add(tab_video);
        mTabs.add(tab_voice);
        mTabs.add(tab_train);


        mInflater = LayoutInflater.from(this);
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mFragmentTabHost.addTab(tabSpec, tab.getFragment(), null);

        }
        mFragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (mTts.isSpeaking()) {
                    mTts.stopSpeaking();
                }
                if (tabId.equals(getString(R.string.main_first))) {
                    stopRecognizerListener();
                    startAiuiListener();
                    mySpeechType = MySpeech.SPEECH_AIUI;
                } else if (tabId.equals(getString(R.string.main_three))) {
                    stopAiuiListener();
                    startRecognizerListener();
                    mySpeechType = MySpeech.SPEECH_RECOGNIZER_VOICE;
                } else if (tabId.equals(getString(R.string.main_two))) {
                    stopAiuiListener();
                    startRecognizerListener();
                    mySpeechType = MySpeech.SPEECH_RECOGNIZER_VIDEO;
                } else {
                    stopListener();
                    mySpeechType = MySpeech.SPEECH_NULL;
                }
            }
        });
        //取消默认的Tab间的竖线显示
        mFragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mFragmentTabHost.setCurrentTab(0);

        mySpeechType = MySpeech.SPEECH_AIUI;
    }

    private void initSpeech() {
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        recognizerTimer = new Timer();
        aiuiTimer = new Timer();

        synthesizerListener = new MySynthesizerListener(this);
        aiuiListener = new MyAiuiListener(MainActivity.this, this);
        recognizerListener = new MyRecognizerListener(this);
        initTts();
        initAiui();
        initIat();

//        stopRecognizerListener();
//        startAiuiListener();
    }

    private void initAiui() {
        String params = "";
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAIUIAgent = AIUIAgent.createAgent(this, params, aiuiListener);
        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(startMsg);

        if (AIUIConstant.STATE_WORKING != this.mAIUIState) {
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }

//         打开AIUI内部录音机，开始录音
        String paramss = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, paramss, null);
        mAIUIAgent.sendMessage(writeMsg);
    }

    private void initIat() {
        mIat = SpeechRecognizer.createRecognizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(MainActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_LONG).show();
                }
            }
        });
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        } else {
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        mIat.setParameter(SpeechConstant.VAD_BOS, "99000");
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
        FlowerCollector.onEvent(this, "iat_recognize");
        mIat.startListening(recognizerListener);
    }

    private void initTts() {
        mTts = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(MainActivity.this, "初始化失败,错误码：" + code, Toast.LENGTH_LONG).show();
                }
            }
        });
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 合成语音
     *
     * @param answer
     */
    public void doAnswer(String answer) {
        FlowerCollector.onEvent(this, "tts_play");
        mTts.startSpeaking(answer, synthesizerListener);

    }

    private void stopListener() {
        stopAiuiListener();
        stopRecognizerListener();
    }

    private void startAiuiListener() {
//        showStr("启动AiuiListener");
        AIUIMessage aiuiMessage1 = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(aiuiMessage1);

        AIUIMessage aiuiMessage = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, null, null);
        mAIUIAgent.sendMessage(aiuiMessage);

        String paramss = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, paramss, null);
        mAIUIAgent.sendMessage(writeMsg);
//        if (aiuiTimerTask != null) {
//            stopAiuiListener();
//        }
        //        setAiuiCountDown();
        aiuiTimerTask = null;
    }

    private void stopAiuiListener() {
//        showStr("停止AiuiListener");
        stopAiuiTask();
        String paramss = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, paramss, null);
        mAIUIAgent.sendMessage(writeMsg);
        AIUIMessage aiuiMessage = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null);
        mAIUIAgent.sendMessage(aiuiMessage);
    }

    /**
     * 开始语音识别
     */
    private void startRecognizerListener() {
//        showStr("启动recognizerListener");
        mIat.startListening(recognizerListener);
//        stopRecognizerListener();
    }

    private void stopRecognizerListener() {
//        showStr("停止RecognizerListener");
        stopRecognizerTask();
        mIat.startListening(null);
        mIat.stopListening();
    }

    private void setAiuiCountDown() {
        if (isTalking == false) {
            if (aiuiTimerTask == null) {
                aiuiTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showStr("aiui两秒后我执行了");
                                startAiuiListener();
                            }
                        });

                    }
                };
                aiuiTimer.schedule(aiuiTimerTask, 1000 * 2);
            } else {
                stopAiuiTask();
            }
        }
    }

    private void stopAiuiTask() {

        if (aiuiTimerTask != null) {
            aiuiTimerTask.cancel();
            aiuiTimerTask = null;
        }
    }

    private void setRecognizerCountDown() {
        if (recognizerTimerTask == null) {
            recognizerTimerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startRecognizerListener();
                            showStr("Recognizer两秒后我执行了");
                        }
                    });

                }
            };
            recognizerTimer.schedule(recognizerTimerTask, 1000);
        } else {
            stopRecognizerTask();
        }
    }

    private void stopRecognizerTask() {
        if (recognizerTimerTask != null) {
            recognizerTimerTask.cancel();
            recognizerTimerTask = null;
        }
    }


    /**
     * 点击首页item设置
     *
     * @param question
     * @param finalText
     */
    private void refHomePage(String question, String finalText) {

        if (homePageFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.main_first));
            if (fragment != null) {
                homePageFragment = (HomePageFragment) fragment;
                homePageFragment.setTestView(question, finalText);
            }
        } else {
            homePageFragment.setTestView(question, finalText);

        }
    }

    private void refVoicePage(String result) {

        if (voiceFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.main_three));
            if (fragment != null) {
                voiceFragment = (VoiceFragment) fragment;
                voiceFragment.printResult(result);
            }
        } else {
            voiceFragment.printResult(result);

        }
    }

    private void refVideoPage(String result) {
        if (videoFragment == null) {


            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.main_two));
            if (fragment != null) {
                videoFragment = (VideoFragment) fragment;
                videoFragment.printResult(result);
            }
        } else {
            videoFragment.printResult(result);

        }
    }

    /***********************************/


    private View buildIndicator(Tab tab) {


        View view = mInflater.inflate(R.layout.tab_item, null);
        TextView text = (TextView) view.findViewById(R.id.iv_title);

        text.setText(tab.getTitle());

        return view;
    }


    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
//        setResult(PERMISSIONS_GRANTED);
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
        }
    }

    public void videoPageRestart() {
        startRecognizerListener();
    }


    /**************************/
    @Override
    public void onCompleted() {
        isTalking = false;
        judgeState();
        if (mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            if (homePageFragment != null) {
                homePageFragment.setVoiceViewVisibilty(false);
            }
        } else if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VOICE)) {
            if (voiceFragment != null) {
                voiceFragment.setVoiceViewVisibilty(false);
                startRecognizerListener();
            }
        }
    }

    @Override
    public void onSpeakBegin() {
        isTalking = true;
        stopAiuiTask();
        if (mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            if (homePageFragment != null) {
                homePageFragment.setVoiceViewVisibilty(true);
            }
        } else if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VOICE)) {
            if (voiceFragment != null) {
                voiceFragment.setVoiceViewVisibilty(true);
                stopRecognizerListener();
            }
        }
    }

    @Override
    public void onDoAnswer(String question, String finalText) {
        doAnswer(finalText);
        refHomePage(question, finalText);
    }

    @Override
    public void onError() {
        if (mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            initAiui();
        }
    }

    @Override
    public void onAIUIDowm() {
        showStr("onAIUIDowm");
        Log.e("onAIUIDowm", "onAIUIDowm");
        if (mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            setAiuiCountDown();
        }
    }

    @Override
    public void onResult(String result) {
        if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VOICE)) {
            refVoicePage(result);
        } else if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VIDEO)) {
            if (!TextUtils.isEmpty(result)) {
                Log.d("Video+++", result);
                refVideoPage(result);
            }
        }
    }

    @Override
    public void onErrInfo() {
        showStr("recogn错误 20006");
        if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VOICE) || mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VIDEO)) {
            stopAiuiListener();
            if (mIat == null) {
                initIat();
            } else {
                setRecognizerCountDown();
            }
        }
    }

    @Override
    public void onRecognDown() {
        showStr("recogn错误 10018");
        Log.e("onRecognDown", "onRecognDown");
//        setRecognizerCountDown();
        if (mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VOICE) || mySpeechType.equals(MySpeech.SPEECH_RECOGNIZER_VIDEO)) {
            startRecognizerListener();
        }
    }

    public void showStr(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
