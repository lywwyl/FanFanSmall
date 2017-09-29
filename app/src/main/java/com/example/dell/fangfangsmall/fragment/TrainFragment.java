package com.example.dell.fangfangsmall.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.DataManagementActivity;
import com.example.dell.fangfangsmall.activity.ManageActivity;
import com.example.dell.fangfangsmall.activity.TakePhotoActivity;
import com.example.dell.fangfangsmall.activity.VerificationActivity;
import com.example.dell.fangfangsmall.util.JumpItent;

public class TrainFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout mTakephoto;//拍照
    private RelativeLayout mFace;//人脸提取
    private RelativeLayout mManage;
    public static final int RESULT_CODE_STARTVIDEO = 200;
    public static final int RESULT_CODE_STARTAUDIO = 100;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        mContext = getActivity();
        if (view != null) {
            initView(view);
            initListener();
        }
        return view;
    }

    private void initView(View view) {
        (mTakephoto) = (RelativeLayout) view.findViewById(R.id.rl_takephoto);
        (mFace) = (RelativeLayout) view.findViewById(R.id.rl_face);
        mManage = (RelativeLayout) view.findViewById(R.id.rl_manage);
    }

    private void initListener() {
        mTakephoto.setOnClickListener(this);
        mFace.setOnClickListener(this);
        mManage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_takephoto:
                JumpItent.jump(getActivity(), TakePhotoActivity.class);
                break;
            case R.id.rl_face:
                videoPermission();
                break;
            case R.id.rl_manage:
                startActivity(new Intent(getActivity(), DataManagementActivity.class));
                break;
        }
    }

    //视频权限
    public void videoPermission() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.
                checkSelfPermission(getActivity(), Manifest.permission.CAMERA) && PackageManager.PERMISSION_GRANTED == ContextCompat.
                checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            verificationLisence();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //提示用户开户权限
                String[] perms = {"android.permission.CAMERA"};
                ActivityCompat.requestPermissions(getActivity(), perms, RESULT_CODE_STARTVIDEO);
            }
        }
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RESULT_CODE_STARTVIDEO:
                boolean albumAccepted_video = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!albumAccepted_video) {
                    Toast.makeText(mContext, "请开启应用视频权限", Toast.LENGTH_LONG).show();
                } else {
                    verificationLisence();
                }
                break;
        }
    }

    public void verificationLisence() {

        Bundle bundle = new Bundle();
        bundle.putString("AuthId", "zhangT");
        JumpItent.jump(getActivity(), VerificationActivity.class, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
