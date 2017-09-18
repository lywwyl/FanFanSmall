package com.example.dell.fangfangsmall.face.yt.person;

import com.example.dell.fangfangsmall.face.yt.YtPerson;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 */

public class YtSetperson extends YtPerson {

    private String session_id;
    private String person_id;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    @Override
    public String toString() {
        return "YtSetperson{" +
                "session_id='" + session_id + '\'' +
                ", person_id='" + person_id + '\'' +
                '}';
    }
}
