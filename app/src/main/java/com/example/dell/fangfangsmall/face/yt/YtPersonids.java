package com.example.dell.fangfangsmall.face.yt;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 * {"person_ids":["asdfghjkl"],"errorcode":0,"errormsg":"OK"}
 */

public class YtPersonids extends YtPerson {

    private List<String> person_ids;

    public List<String> getPerson_ids() {
        return person_ids;
    }

    public void setPerson_ids(List<String> person_ids) {
        this.person_ids = person_ids;
    }

    @Override
    public String toString() {
        return "YtPersonids{" +
                "person_ids=" + person_ids +
                '}';
    }
}
