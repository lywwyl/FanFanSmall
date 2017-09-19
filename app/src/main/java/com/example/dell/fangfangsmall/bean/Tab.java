package com.example.dell.fangfangsmall.bean;


public class Tab {

    private int image;
    private int title;
    private Class fragment;

    public Tab(Class fragment, int title, int image) {
        this.image = image;
        this.title = title;
        this.fragment = fragment;
    }


    public Class getFragment() {
        return fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
