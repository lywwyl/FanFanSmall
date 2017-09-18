package com.example.dell.fangfangsmall.face.yt.person.face;

import com.example.dell.fangfangsmall.face.yt.YtPerson;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 * {"face_info":
 *      {"face_id":"2212735432171481725",
 *      "x":277,
 *      "y":473,
 *      "height":864,
 *      "width":864,
 *      "pitch":-1,
 *      "roll":1,
 *      "yaw":3,
 *      "age":30,
 *      "gender":99,
 *      "glass":true,
 *      "expression":18,
 *      "beauty":75},
 *  "tag":"",
 *  "errorcode":0,
 *  "errormsg":"OK"}
 */

public class YtFaceInfoResult extends YtPerson {

    private YtFaceInfo face_info;
    private String tag;

    public YtFaceInfo getFace_info() {
        return face_info;
    }

    public void setFace_info(YtFaceInfo face_info) {
        this.face_info = face_info;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "YtFaceInfoPerson{" +
                "face_info=" + face_info +
                ", tag='" + tag + '\'' +
                '}';
    }
}
