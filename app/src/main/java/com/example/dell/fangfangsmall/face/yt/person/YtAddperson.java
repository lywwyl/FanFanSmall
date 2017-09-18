package com.example.dell.fangfangsmall.face.yt.person;

import com.example.dell.fangfangsmall.face.yt.YtPerson;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/3.
 * {"added":1,"face_ids":["2212736722652916461"],"ret_codes":[0],"session_id":"","errorcode":0,"errormsg":"OK"}
 * {"added":1,"face_ids":["2212737794406640181"],"ret_codes":[0],"session_id":"","errorcode":0,"errormsg":"OK"}
 * {"added":0,"face_ids":["2212738117759166992"],"ret_codes":[-1312],"session_id":"","errorcode":0,"errormsg":"OK"}
 */

public class YtAddperson extends YtPerson {


    private int added;
    private List<String> face_ids;
    private List<Integer> ret_codes;
    private String session_id;

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }

    public List<Integer> getRet_codes() {
        return ret_codes;
    }

    public void setRet_codes(List<Integer> ret_codes) {
        this.ret_codes = ret_codes;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    @Override
    public String toString() {
        return "YtAddperson{" +
                "added=" + added +
                ", face_ids=" + face_ids +
                ", ret_codes=" + ret_codes +
                ", session_id='" + session_id + '\'' +
                '}';
    }
}
