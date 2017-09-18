package com.example.dell.fangfangsmall.face.yt.person.face;

import com.example.dell.fangfangsmall.face.yt.YtPerson;

import java.util.ArrayList;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public class YtFaceIdentify extends YtPerson {

    public String session_id;
    public ArrayList<IdentifyItem> candidates;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public ArrayList<IdentifyItem> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<IdentifyItem> candidates) {
        this.candidates = candidates;
    }
}
