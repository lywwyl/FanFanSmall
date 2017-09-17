package com.example.dell.fangfangsmall.youtu.callback;

public abstract class Callback<T> {

    public void onBefore() {
    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T t);

    public abstract void onFail(int code, String msg);


    public void onEnd(){

    }

}
