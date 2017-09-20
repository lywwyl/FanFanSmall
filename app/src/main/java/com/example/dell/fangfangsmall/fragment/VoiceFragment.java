package com.example.dell.fangfangsmall.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.MainTwoActivity;
import com.example.dell.fangfangsmall.adapter.VoiceQuestionAdapter;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
public class VoiceFragment extends Fragment {
    //
    private RecyclerView mQuestion; //问题列表
    private TextView mAnswerv;//答案
    //
    private VoiceQuestionAdapter questionAdapter;
    //问题
    private String[] voiceQuestion = null;
    private List<String> voiceQuestionList = new ArrayList<>();
    //答案
    private String[] voiceAnswer = null;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";
    private OnDoAnswerListener onDoAnswerListener;

    public void setOnDoAnswerListener(OnDoAnswerListener onDoAnswerListener) {
        this.onDoAnswerListener = onDoAnswerListener;
    }

    public interface OnDoAnswerListener {
        void doAnswer(String answer);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onDoAnswerListener = (MainTwoActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        (mQuestion) = (RecyclerView) view.findViewById(R.id.rv_question);
        (mAnswerv) = (TextView) view.findViewById(R.id.tv_answer);

    }

    private void initData() {
        LinearLayoutManager linearLayoutManager_list_question = new LinearLayoutManager(getActivity());
        linearLayoutManager_list_question.setOrientation(LinearLayoutManager.VERTICAL);
        mQuestion.setLayoutManager(linearLayoutManager_list_question);
        mQuestion.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        questionAdapter = new VoiceQuestionAdapter(getActivity());
        mQuestion.setAdapter(questionAdapter);

        voiceQuestion = getActivity().getResources().getStringArray(R.array.voice_question_array);
        voiceQuestionList = Arrays.asList(voiceQuestion);

        voiceAnswer = getActivity().getResources().getStringArray(R.array.voice_answer_array);

        questionAdapter.refreshQuestion(voiceQuestionList);


    }

    //
    private void initListener() {
        questionAdapter.setOnItemClickListener(new VoiceQuestionAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAnswerv.setText(voiceAnswer[position].toString());
                if (onDoAnswerListener != null) {
                    onDoAnswerListener.doAnswer(voiceAnswer[position].toString());
                }
            }
        });

    }

    public void printResult(String text) {
        String result = "";
        for (int i = 0; i < voiceQuestion.length; i++) {

            if (text.equals(voiceQuestion[i].toString())) {
                mAnswerv.setText(voiceAnswer[i].toString());
                result = voiceAnswer[i].toString();
                break;
            } else {
                result = "抱歉，我没有听懂您说什么";
            }
        }
        if (onDoAnswerListener != null) {
            onDoAnswerListener.doAnswer(result);
        }

    }


}
