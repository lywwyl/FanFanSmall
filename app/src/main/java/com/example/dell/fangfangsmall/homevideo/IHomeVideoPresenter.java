package com.example.dell.fangfangsmall.homevideo;

import android.content.Context;

/**
 * Created by dell on 2017/9/21.
 */

public abstract class IHomeVideoPresenter {

    private IHomeVideoView mBaseView;

    public IHomeVideoPresenter(IHomeVideoView baseView) {
        mBaseView = baseView;
    }

    public interface IHomeVideoView {
        Context getContext();
    }
}
