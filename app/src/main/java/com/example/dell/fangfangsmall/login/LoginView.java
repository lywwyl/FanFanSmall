package com.example.dell.fangfangsmall.login;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.base.ControlBaseActivity;
import com.yuntongxun.ecsdk.ECDevice;


/**
 * Created by dell on 2017/8/1.
 */

public class LoginView extends ControlBaseActivity<LoginActivityPresenter> implements ILoginView, View.OnClickListener {

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public LoginActivityPresenter createPresenter() {
        return new LoginActivityPresenter(this);
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_login;
    }

    private TextView mLogin;
//    private EditText mUsername;


    @Override
    protected void onViewInit() {
        super.onViewInit();

        (mLogin) = (TextView) findViewById(R.id.tv_login);
//        (mUsername) = (EditText) findViewById(R.id.et_username);
    }

    @Override
    protected void setOnListener() {
        super.setOnListener();
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                mPresenter.DoLogin();
                break;
        }
    }

//    @Override
//    public String getEditText() {
//        return mUsername.getText().toString();
//    }

    @Override
    public void SetEditText(String text) {
        mLogin.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ECDevice.unInitial();
    }
}
