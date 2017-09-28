package com.example.dell.fangfangsmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.dao.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/9/14.
 */

public class VoiceQuestionAdapter extends RecyclerView.Adapter<VoiceQuestionAdapter.VoiceQuestionViewHolder> {

    private Context mContext;
    private List<UserInfo> mVoiceQuestionList = new ArrayList<>();
    private LayoutInflater mInflater;
    private onItemClickListener onItemClickListener;

    public VoiceQuestionAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    public void refreshQuestion(List<UserInfo> voiceQuestions) {
        this.mVoiceQuestionList = voiceQuestions;
        notifyDataSetChanged();
    }

    //点击事件
    public interface onItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public VoiceQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VoiceQuestionViewHolder holder = new VoiceQuestionViewHolder(mInflater.inflate(R.layout.layout_voice_question_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final VoiceQuestionViewHolder holder, int position) {
        holder.tv_question.setText(mVoiceQuestionList.get(position).getQuestion());

        if (onItemClickListener != null) {
            holder.tv_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mVoiceQuestionList.size();
    }

    class VoiceQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView tv_question;

        public VoiceQuestionViewHolder(View itemView) {
            super(itemView);
            tv_question = (TextView) itemView.findViewById(R.id.tv_question);
        }
    }
}
