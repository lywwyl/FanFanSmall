package com.example.dell.fangfangsmall.bean;


public class Tab {

    private  int title;
    private Class fragment;

    public Tab(Class fragment, int title) {
        this.title = title;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
