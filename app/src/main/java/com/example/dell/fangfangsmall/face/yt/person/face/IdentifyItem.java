package com.example.dell.fangfangsmall.face.yt.person.face;

import java.io.Serializable;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public class IdentifyItem implements Serializable {


    private String person_id;
    private String face_id;
    private float confidence;
    private String tag;

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            if (obj instanceof IdentifyItem) {
                IdentifyItem identifyItem = (IdentifyItem) obj;
                if (identifyItem.person_id.equals(this.person_id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
