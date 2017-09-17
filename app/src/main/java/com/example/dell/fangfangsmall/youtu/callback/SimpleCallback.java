package com.example.dell.fangfangsmall.youtu.callback;

import android.app.Activity;
import android.app.Dialog;

/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public abstract class SimpleCallback<T> extends Callback<T> {

    private Activity activity;

    private Dialog spotsDialog;

    public SimpleCallback(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onBefore() {
        super.onBefore();
        showDialog();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        dismissDialog();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private void showDialog(){
        if(spotsDialog == null){
            spotsDialog = new Dialog(activity);
        }
        spotsDialog.show();
    }

    private void dismissDialog(){
        if(spotsDialog != null && spotsDialog.isShowing()){
            spotsDialog.dismiss();
        }
    }
}
