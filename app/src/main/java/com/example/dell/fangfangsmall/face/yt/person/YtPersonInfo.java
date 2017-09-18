package com.example.dell.fangfangsmall.face.yt.person;


import com.example.dell.fangfangsmall.face.yt.YtPerson;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 */

public class YtPersonInfo extends YtPerson {

    private String person_name;
    private String person_id;
    private List<String> face_ids;
    private List<String> group_ids;
    private String session_id;
    private String tag;

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }

    public List<String> getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(List<String> group_ids) {
        this.group_ids = group_ids;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "YtPersonInfo{" +
                "person_name='" + person_name + '\'' +
                ", person_id='" + person_id + '\'' +
                ", face_ids=" + face_ids +
                ", group_ids=" + group_ids +
                ", session_id='" + session_id + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
