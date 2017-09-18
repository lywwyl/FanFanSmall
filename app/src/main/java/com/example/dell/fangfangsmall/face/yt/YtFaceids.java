package com.example.dell.fangfangsmall.face.yt;


import com.example.dell.fangfangsmall.face.yt.YtPerson;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 * YtFaceids{face_ids=[2212735432171481725, 2212736722652916461, 2212737794406640181]}
 */

public class YtFaceids extends YtPerson {

    private List<String> face_ids;

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }

    @Override
    public String toString() {
        return "YtFaceids{" +
                "face_ids=" + face_ids +
                '}';
    }
}
