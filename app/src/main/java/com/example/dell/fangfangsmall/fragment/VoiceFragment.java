package com.example.dell.fangfangsmall.fragment;

import android.annotation.SuppressLint;
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
import com.example.dell.fangfangsmall.activity.MainActivity;
import com.example.dell.fangfangsmall.adapter.VoiceQuestionAdapter;
import com.example.dell.fangfangsmall.dao.UserDao;
import com.example.dell.fangfangsmall.dao.UserInfo;
import com.example.dell.fangfangsmall.view.VoiceLineView;

import java.util.ArrayList;
import java.util.List;

//
public class VoiceFragment extends Fragment {
    //
    private RecyclerView mQuestion; //问题列表
    private TextView mAnswerv;//答案
    //
    private VoiceQuestionAdapter questionAdapter;
    //问题
//    private String[] voiceQuestion = null;

    private List<UserInfo> userInfoArrayList = new ArrayList<>();
    //答案
//    private String[] voiceAnswer = null;

    private OnDoAnswerListener onDoAnswerListener;

    private VoiceLineView voicLineView;

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
            onDoAnswerListener = (MainActivity) activity;
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
        (voicLineView) = (VoiceLineView) view.findViewById(R.id.voicLine);

    }

    private void initData() {
        LinearLayoutManager linearLayoutManager_list_question = new LinearLayoutManager(getActivity());
        linearLayoutManager_list_question.setOrientation(LinearLayoutManager.VERTICAL);
        mQuestion.setLayoutManager(linearLayoutManager_list_question);
        mQuestion.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        questionAdapter = new VoiceQuestionAdapter(getActivity());
        mQuestion.setAdapter(questionAdapter);


 //       queryListData();
//        voiceQuestion = getActivity().getResources().getStringArray(R.array.voice_question_array);
//        voiceQuestionList = Arrays.asList(voiceQuestion);

//        voiceAnswer = getActivity().getResources().getStringArray(R.array.voice_answer_array);
//        questionAdapter.refreshQuestion(voiceQuestionList);


    }
    @Override
    public void onResume() {
        super.onResume();

        queryListData();
    }
    /**
     * 查询数据
     */
    private void queryListData() {
        userInfoArrayList = UserDao.getInstance().queryUserByType("Data");
        questionAdapter.refreshQuestion(userInfoArrayList);
        //  Toast.makeText(getActivity(), "查询到" + lists.size() + "条数据", Toast.LENGTH_SHORT).show();
    }

    //
    private void initListener() {
        questionAdapter.setOnItemClickListener(new VoiceQuestionAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAnswerv.setText(userInfoArrayList.get(position).getContent());
                if (onDoAnswerListener != null) {
                    onDoAnswerListener.doAnswer(userInfoArrayList.get(position).getContent());
                }
            }
        });

    }

    @SuppressLint("WrongConstant")
    public void setVoiceViewVisibilty(boolean visibilty) {
        voicLineView.setVisibility(visibilty ? View.VISIBLE : View.GONE);
    }

    public void printResult(String text) {
        String result = "";
        for (int i = 0; i < userInfoArrayList.size(); i++) {

            if (text.equals(userInfoArrayList.get(i).getQuestion().toString())) {
                mAnswerv.setText(userInfoArrayList.get(i).getContent());
                result = userInfoArrayList.get(i).getContent().toString();

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
