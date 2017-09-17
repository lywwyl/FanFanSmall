package com.example.dell.fangfangsmall.face.yt.person;

import com.example.dell.fangfangsmall.face.yt.YtPerson;

public class YtVerifyperson extends YtPerson {

    private int confidence;
    private boolean ismatch;
    private String session_id;

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public boolean ismatch() {
        return ismatch;
    }

    public void setIsmatch(boolean ismatch) {
        this.ismatch = ismatch;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    @Override
    public String toString() {
        return "YtVerifyperson{" +
                "confidence=" + confidence +
                ", ismatch=" + ismatch +
                ", session_id='" + session_id + '\'' +
                '}';
    }
}
