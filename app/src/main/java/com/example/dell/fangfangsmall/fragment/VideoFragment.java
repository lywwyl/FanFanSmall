package com.example.dell.fangfangsmall.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.VideoDetailActivity;
import com.example.dell.fangfangsmall.util.JumpItent;

public class VideoFragment extends Fragment implements View.OnClickListener {
    private TextView mVideoOne, mVideoTwo, mVideoThree, mVideoFour, mVideoFive, mVideoSix;
    private ImageView iv_nextPage, iv_upwardPage;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mContext = getActivity();
        initView(view);
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
                break;
            case R.id.iv_upwardPage:
                Toast.makeText(getActivity(), "上一页", Toast.LENGTH_LONG).show();
                break;
        }
    }

}
