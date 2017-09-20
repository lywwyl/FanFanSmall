package com.example.dell.fangfangsmall.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.MainTwoActivity;
import com.example.dell.fangfangsmall.camera.CameraPresenter;
import com.example.dell.fangfangsmall.camera.FaceVerifPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.ICameraPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.IFaceVerifPresenter;
import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceIdentify;
import com.example.dell.fangfangsmall.view.VoiceLineView;

public class HomePageFragment extends Fragment  implements IFaceVerifPresenter.IFaceverifView, SurfaceHolder.Callback,
        ICameraPresenter.ICameraView, View.OnClickListener {

    private TextView mQuestion;//问题
    private TextView mAnswer;//答案
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        (mQuestion) = (TextView) view.findViewById(R.id.tv_main_question);
        (mAnswer) = (TextView) view.findViewById(R.id.tv_main_answer);
        imFace = (ImageView) view.findViewById(R.id.im_face);
        cameraSurfaceView = (SurfaceView) view.findViewById(R.id.opengl_layout_surfaceview);
        mainTwoActivity = (MainTwoActivity) getActivity();
        (voicLineView) = (VoiceLineView) view.findViewById(R.id.voicLine);
    }

    private void initData() {
        // 初始化合成对象
        SurfaceHolder holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        holder.addCallback(this); // 为SurfaceView添加状态监听
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mFaceVerifPresenter = new FaceVerifPresenter(this);
        mCameraPresenter = new CameraPresenter(this, holder);
    }

    private void initListener() {
        imFace.setOnClickListener(this);
    }

    public void setTestView(String question, String finalText) {
        mQuestion.setText(question);
        mAnswer.setText(finalText);
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
        return getActivity();
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
                Toast toast = Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT);
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
