package com.example.dell.fangfangsmall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.MainTwoActivity;
import com.example.dell.fangfangsmall.activity.VideoDetailActivity;
import com.example.dell.fangfangsmall.util.IatSettings;
import com.example.dell.fangfangsmall.util.JsonParser;
import com.example.dell.fangfangsmall.util.JumpItent;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class VideoFragment extends Fragment implements View.OnClickListener {
    private TextView mVideoOne, mVideoTwo, mVideoThree, mVideoFour, mVideoFive, mVideoSix;
    private ImageView iv_nextPage, iv_upwardPage;
    private Context mContext;

    // 语音听写对象
    private SpeechRecognizer mIat;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private MainTwoActivity mainTwoActivity;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private boolean isTurn = true;

    private String onePageContent[] = {"机器人简介", "公司介绍", "我是芳芳", "服务范围", "吃饭", "睡觉"};
    private int oneCount = 0;
    private String twoPageContent[] = {"起床", "刷牙", "洗脸", "喝水", "风扇", "下雨"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mContext = getActivity();
        initView(view);
        doData(onePageContent);
        initListener();
        return view;
    }


    private void initView(View view) {

        (mVideoOne) = (TextView) view.findViewById(R.id.tv_video_one);
        (mVideoTwo) = (TextView) view.findViewById(R.id.tv_video_two);
        (mVideoThree) = (TextView) view.findViewById(R.id.tv_video_three);
        (mVideoFour) = (TextView) view.findViewById(R.id.tv_video_four);
        (mVideoFive) = (TextView) view.findViewById(R.id.tv_video_five);
        (mVideoSix) = (TextView) view.findViewById(R.id.tv_video_six);
        (iv_nextPage) = (ImageView) view.findViewById(R.id.iv_nextPage);
        (iv_upwardPage) = (ImageView) view.findViewById(R.id.iv_upwardPage);
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);

        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        mSharedPreferences = mContext.getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mainTwoActivity = (MainTwoActivity) getActivity();

        //说完记得clear
        //mIatResults.clear();

    }

    private void doData(String[] stringResult) {
        mVideoOne.setText(stringResult[0].toString());
        mVideoTwo.setText(stringResult[1].toString());
        mVideoThree.setText(stringResult[2].toString());
        mVideoFour.setText(stringResult[3].toString());
        mVideoFive.setText(stringResult[4].toString());
        mVideoSix.setText(stringResult[5].toString());
    }

    private void initListener() {
        mVideoOne.setOnClickListener(this);
        mVideoTwo.setOnClickListener(this);
        mVideoThree.setOnClickListener(this);
        mVideoFour.setOnClickListener(this);
        mVideoFive.setOnClickListener(this);
        mVideoSix.setOnClickListener(this);
        iv_nextPage.setOnClickListener(this);
        iv_upwardPage.setOnClickListener(this);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败，错误码：" + code, Toast.LENGTH_LONG).show();
            }
        }
    };

    int ret = 0; // 函数调用返回值

    /**
     * 初始化听写
     */
    private void initRecognizer() {
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(mContext, "iat_recognize");

        // 设置参数
        setParam();
        if (isTurn) {

            ret = mIat.startListening(mRecognizerListener);
        } else {
            mIat.stopListening();
        }
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            showTip("请开始说话");
        }
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "99000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            showTip("开始说话");
            Log.e("GG", "开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
            initRecognizer();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
            Log.e("GG", "结束说话");
            initRecognizer();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {


            if (!isLast) {
                // TODO 最后的结果
                printResult(results);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);


        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        Log.e("resule", text);
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();

        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        String result = "";
//        showTip(resultBuffer.toString());
        //TODO  进行匹配

        if (oneCount == 0) {
            for (int i = 0; i < onePageContent.length; i++) {
                if (text.equals(onePageContent[i].toString())) {
                    result = onePageContent[i].toString();
                    doVideoDetail(i);
                    break;
                } else {
                    result = "抱歉，我没有听懂您说什么";
                }
            }
        } else {
            for (int i = 0; i < twoPageContent.length; i++) {
                if (text.equals(twoPageContent[i].toString())) {
                    result = twoPageContent[i].toString();
                    doVideoDetail(i);
                    break;
                } else {
                    result = "抱歉，我没有听懂您说什么";
                }
            }
        }


    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tv_video_one:
                bundle.putString("Url", "http://vjs.zencdn.net/v/oceans.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_two:
                bundle.putString("Url", "http://ohjdda8lm.bkt.clouddn.com/course/sample1.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_three:
                bundle.putString("Url", "http://mp4.vjshi.com/2017-06-17/b35abad666599dfc86447eb6e75ce88a.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_four:
                bundle.putString("Url", "http://mp4.vjshi.com/2016-02-25/2015-b8b4eb2656ac728134371e0f395d5028.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_five:
                bundle.putString("Url", "http://mp4.vjshi.com/2016-02-19/2015-7f79ff9992ea1bbb5c901872d0f5fc25.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case R.id.tv_video_six:
                bundle.putString("Url", "http://mp4.vjshi.com/2014-06-13/2014061315010763299.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case R.id.iv_nextPage:
                Toast.makeText(getActivity(), "下一页", Toast.LENGTH_LONG).show();
                if (oneCount == 0) {
                    doData(twoPageContent);
                    oneCount++;
                }
                Log.e("GG", oneCount + "");
                break;
            case R.id.iv_upwardPage:
                Toast.makeText(getActivity(), "上一页", Toast.LENGTH_LONG).show();
                if (oneCount == 1) {
                    doData(onePageContent);
                    oneCount--;
                }
                Log.e("GG", oneCount + "");
                break;
        }
    }

    public void doVideoDetail(int item) {
        Bundle bundle = new Bundle();
        switch (item) {
            case 0:
                bundle.putString("Url", "http://vjs.zencdn.net/v/oceans.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case 1:
                bundle.putString("Url", "http://ohjdda8lm.bkt.clouddn.com/course/sample1.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case 2:
                bundle.putString("Url", "http://mp4.vjshi.com/2017-06-17/b35abad666599dfc86447eb6e75ce88a.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case 3:
                bundle.putString("Url", "http://mp4.vjshi.com/2016-02-25/2015-b8b4eb2656ac728134371e0f395d5028.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case 4:
                bundle.putString("Url", "http://mp4.vjshi.com/2016-02-19/2015-7f79ff9992ea1bbb5c901872d0f5fc25.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
            case 5:
                bundle.putString("Url", "http://mp4.vjshi.com/2014-06-13/2014061315010763299.mp4");
                JumpItent.jump(getActivity(), VideoDetailActivity.class, bundle);
                break;
        }
    }

    //    /fragment切换时只走这个方法
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
//            mAIUIListener = null;
            Log.i("GG", "切换到其他fragment" + hidden);
            isTurn = false;
            mIat.cancel();
        } else {
            Log.i("GG", "回到当前fragment" + hidden);
            isTurn = true;
            initRecognizer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("GG", "VideoonResume");
        if (mainTwoActivity.mFragmentTabHost.getCurrentTab() == 1) {
            isTurn = true;
            initRecognizer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("GG", "VideoonStop");
        if (mainTwoActivity.mFragmentTabHost.getCurrentTab() == 1) {
            isTurn = false;
            mIat.stopListening();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("GG", "VideoonDestroy");
        if (mainTwoActivity.mFragmentTabHost.getCurrentTab() == 1) {
            isTurn = false;
            if (null != mIat) {
                // 退出时释放连接
                mIat.cancel();
                mIat.destroy();
            }
        }
    }

}
