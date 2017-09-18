package com.example.dell.fangfangsmall.face.yt.person.face;


import com.example.dell.fangfangsmall.face.yt.YtPerson;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/6.
 */

public class YtDetectFace extends YtPerson {

    private String session_id;
    private int image_width;
    private int image_height;
    private List<Face> face;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public List<Face> getFace() {
        return face;
    }

    public void setFace(List<Face> face) {
        this.face = face;
    }
}
