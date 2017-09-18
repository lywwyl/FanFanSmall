package com.example.dell.fangfangsmall.face.yt.person;

import com.example.dell.fangfangsmall.face.yt.YtPerson;

/**
 * Created by zhangyuanyuan on 2017/9/3.
 * {"deleted":1,"session_id":"","person_id":"asdfghjkl","errorcode":0,"errormsg":"OK"}
 */


public class YtDelperson extends YtPerson {

    private int deleted;
    private String session_id;
    private String person_id;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

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
        return "YtDelperson{" +
                "deleted=" + deleted +
                ", session_id='" + session_id + '\'' +
                ", person_id='" + person_id + '\'' +
                '}';
    }
}
