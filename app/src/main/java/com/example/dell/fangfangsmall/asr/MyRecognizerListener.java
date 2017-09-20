package com.example.dell.fangfangsmall.asr;

import android.os.Bundle;

import com.example.dell.fangfangsmall.util.JsonParser;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

/**
 * Created by dell on 2017/9/20.
 */

public class MyRecognizerListener implements RecognizerListener {
    public MyRecognizerListener(RecognListener recognListener) {
        this.recognListener = recognListener;
    }

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {

    }

    @Override
    public void onBeginOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        if (!b) {
            String text = JsonParser.parseIatResult(recognizerResult.getResultString());
            if(recognListener != null) {
                recognListener.onResult(text);
            }
        }
    }

    @Override
    public void onError(SpeechError speechError) {

    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    private RecognListener recognListener;

    public interface RecognListener{
        void onResult(String result);
    }
}
