package com.example.dell.fangfangsmall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.FangFangSmallApplication;
import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.MainTwoActivity;
import com.example.dell.fangfangsmall.asr.NlpControl;
import com.example.dell.fangfangsmall.camera.CameraPresenter;
import com.example.dell.fangfangsmall.camera.FaceVerifPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.ICameraPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.IFaceVerifPresenter;
import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceIdentify;
import com.example.dell.fangfangsmall.view.VoiceLineView;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageFragment extends Fragment implements IFaceVerifPresenter.IFaceverifView, SurfaceHolder.Callback, ICameraPresenter.ICameraView, View.OnClickListener {

    public static final int RESULT_CODE_STARTAUDIO = 100;
    private NlpControl nlpControl;
    private int mAIUIState = AIUIConstant.STATE_IDLE;
    //语音识别内容
    private String finalText = "";
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";
    private Toast mToast;

    private TextView mQuestion;//问题
    private TextView mAnswer;//答案
    private Context mContext;
    private boolean isTurn = false;

    private ImageView imFace;
    private boolean faceVerifiOpen;
    private SurfaceView cameraSurfaceView;
    private FaceVerifPresenter mFaceVerifPresenter;
    private CameraPresenter mCameraPresenter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            }
        }
    };

    private MainTwoActivity mainTwoActivity;
    private VoiceLineView voicLineView;


    //计时器
    private Timer mTimer;
    private TimerTask mTimeTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        mContext = getActivity();
        initView(view);
        initData();
        initListener();
//        audioPermission();
        return view;
    }

    private void initView(View view) {
        (mQuestion) = (TextView) view.findViewById(R.id.tv_main_question);
        (mAnswer) = (TextView) view.findViewById(R.id.tv_main_answer);
        imFace = (ImageView) view.findViewById(R.id.im_face);
        cameraSurfaceView = (SurfaceView) view.findViewById(R.id.opengl_layout_surfaceview);
        mainTwoActivity = (MainTwoActivity) getActivity();
        (voicLineView) = (VoiceLineView) view.findViewById(R.id.voicLine);
        mTimer = new Timer();
    }

    private void initData() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        nlpControl = new NlpControl(FangFangSmallApplication.from(mContext));
        nlpControl.setmAIUIListener(mAIUIListener);
        nlpControl.init();
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        SurfaceHolder holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        holder.addCallback(this); // 为SurfaceView添加状态监听
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mFaceVerifPresenter = new FaceVerifPresenter(this);
        mCameraPresenter = new CameraPresenter(this, holder);
    }

    private void initListener() {
        imFace.setOnClickListener(this);
    }

    /**
     * 初始化监听。语音合成
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败,错误码：" + code, Toast.LENGTH_LONG).show();
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 音频权限
     */
    public void audioPermission() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.
                checkSelfPermission(mContext, android.Manifest.permission.RECORD_AUDIO)) {
            startAsr();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //提示用户开户权限音频
                String[] perms = {"android.permission.RECORD_AUDIO"};
                ActivityCompat.requestPermissions((Activity) mContext, perms, RESULT_CODE_STARTAUDIO);
            }
        }

    }

    /**
     * 发送语音
     */
    void startAsr() {
        Log.e("GG", "startStr");
        if (isTurn) {
            Log.i("WWDZ", "已经跳转");
        } else {
            nlpControl.startVoiceNlp();
        }
    }


    /**
     * 进行点击回答问题
     */
    public void doAnswer(String answer) {

        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(mContext, "tts_play");

        // 设置参数
        setSynthesizerParam();
        if (isTurn) {
            Log.i("WWDZ", "已经跳转");
        } else {
            int code = mTts.startSpeaking(answer, mTtsListener);
        }


    }

    /**
     * AIUI 回调
     */
    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_WAKEUP:
                    Log.e("GG", "进入识别状态");
                    showTip("进入识别状态");
                    break;

                case AIUIConstant.EVENT_RESULT: {
//                    Log.i( TAG,  "on event: "+ event.eventType );
                    boolean isSend = false;
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));
                            Log.i("wcj", "--1--" + cntJson);
                            String sub = params.optString("sub");
                            if ("nlp".equals(sub)) {

                                String resultStr = cntJson.optString("intent");
                                JSONObject jsonObject = new JSONObject(resultStr);
                                Log.i("wcj", "--2--" + resultStr);
//                                Log.e("GG", resultStr.length() + "'");
                                if (resultStr != null && resultStr.length() > 3) {
                                    Log.i("wcj", "--3--" + resultStr);
                                    String question = jsonObject.optString("text");
                                    mQuestion.setText(question);
                                    //语义分析
                                    if (jsonObject.has("answer")) {
                                        //被语音语义识别，返回结果
                                        JSONObject answerObj = jsonObject.getJSONObject("answer");
                                        finalText = answerObj.optString("text");
                                        Log.e("GG", finalText);
                                        mAnswer.setText(finalText);
                                        doAnswer(finalText);
                                    } else if (jsonObject.has("rc") && "4".equals(jsonObject.getString("rc"))) {
                                        String[] arrResult = getResources().getStringArray(R.array.no_result);
                                        Log.e("GG", finalText);
                                        finalText = arrResult[new Random().nextInt(arrResult.length)];
                                        mAnswer.setText(finalText);
                                        doAnswer(finalText);
                                    }
                                }
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
                break;

                case AIUIConstant.EVENT_ERROR: {
                    showTip("error");
                    Log.e("GG", "on event:error " + event.eventType);
                }
                break;

                case AIUIConstant.EVENT_VAD: {
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        showTip("找到vad_bos");
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        showTip("找到vad_eos");
                    } else {
//                        showTip("" + event.arg2);
                    }
                }
                break;

                case AIUIConstant.EVENT_START_RECORD: {
                    Log.e("GG", "开始录音");
                    showTip("开始录音");
                    mTimeTask.cancel();
                }
                break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    Log.e("GG", "停止录音");
                    showTip("停止录音");
                }
                break;

                case AIUIConstant.EVENT_STATE: {    // 状态事件
                    mAIUIState = event.arg1;

                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
                        Log.e("GG", "闲置状态，AIUI未开启");
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
                        Log.e("GG", "AIUI已就绪，等待唤醒");
                        mTimeTask = new TimerTask() {
                            @Override
                            public void run() {
                                //执行
                                startAsr();
                                Log.i("GG", "timer.schedule");
                            }
                        };
                        mTimer.schedule(mTimeTask, 1000 * 2);

                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
                        showTip("STATE_WORKING");
                    }
                }
                break;

                case AIUIConstant.EVENT_CMD_RETURN: {
                    if (AIUIConstant.CMD_UPLOAD_LEXICON == event.arg1) {
//                        showTip("上传" + (0 == event.arg2 ? "成功" : "失败"));
                    }
                }
                break;

                default:
                    break;
            }
        }

    };


    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    private void setSynthesizerParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
            voicLineView.setVisibility(View.VISIBLE);
            mTimeTask.cancel();
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            // 在播放进度完成的时候 重新调用监听
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.e("GG", "播放完毕 重新初始化");
                voicLineView.setVisibility(View.GONE);
                startAsr();
            } else if (error != null) {
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_IDSpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid == eventType) {
            //		String sid = obj.getString();
            //	}
        }
    };

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RESULT_CODE_STARTAUDIO:
                boolean albumAccepted_audio = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!albumAccepted_audio) {
                    Toast.makeText(mContext, "请开启应用音频权限", Toast.LENGTH_LONG).show();
                } else {
                    startAsr();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainTwoActivity.mFragmentTabHost.getCurrentTab() == 0) {
            Log.e("GG", "onResume");
            isTurn = false;
            startAsr();
            mTts.isSpeaking();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainTwoActivity.mFragmentTabHost.getCurrentTab() == 0) {
            Log.e("GG", "onStop");
            isTurn = true;
            nlpControl.stopVoiceNlp();
            mTts.pauseSpeaking();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mainTwoActivity.mFragmentTabHost.getCurrentTab() == 0) {
            isTurn = true;
            mTts.pauseSpeaking();
            // 退出时释放连接
//            mTts.destroy();
            nlpControl.stopVoiceNlp();
//            mAIUIListener = null;
        }
    }

    //    /fragment切换时只走这个方法
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);


        if (hidden) {
//            mAIUIListener = null;
            isTurn = true;
            nlpControl.stopVoiceNlp();
            mTts.stopSpeaking();

        } else {
            isTurn = false;
            startAsr();
            mTts.isSpeaking();

        }
    }

    /**
     * ********************************************zt
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCameraPresenter.openCamera();
        mCameraPresenter.doStartPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCameraPresenter.setMatrix(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCameraPresenter.closeCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        mCameraPresenter.closeCamera();
    }

    @Override
    public void previewFinish() {

    }

    @Override
    public void pictureTakenSuccess() {

    }

    @Override
    public void pictureTakenFail() {

    }


    @Override
    public void autoFocusSuccess() {

    }

    @Override
    public void tranBitmap(Bitmap bitmap, int num) {
        mFaceVerifPresenter.faceIdentifyFace(mHandler, bitmap);
    }

    @Override
    public Context getContext() {
        return mContext;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void verificationSuccess(YtFaceIdentify ytFaceIdentify) {
        mFaceVerifPresenter.compareFace(ytFaceIdentify);

    }

    @Override
    public void identifyFace(String personId) {
        showToast("检测到您是：" + personId);
        imFace.setBackgroundResource(R.mipmap.face_close);
        faceVerifiOpen = false;
        cameraSurfaceView.setVisibility(View.GONE);
    }

    @Override
    public void identifyNoFace() {
        showToast("没有检测到，您未注册");
    }

    @Override
    public void verificationFail(int code, String msg) {
        if (code == -1101) {
            showToast("正对摄像头，保证画面清晰");
        } else {
            showToast(msg);
        }
    }


    public void showToast(final String resStr) {

        if (TextUtils.isEmpty(resStr)) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(mContext, resStr, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_face:
                if (faceVerifiOpen) {
                    imFace.setBackgroundResource(R.mipmap.face_close);
                    faceVerifiOpen = false;
                    cameraSurfaceView.setVisibility(View.GONE);
                } else {
                    imFace.setBackgroundResource(R.mipmap.face_open);
                    faceVerifiOpen = true;
                    cameraSurfaceView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
