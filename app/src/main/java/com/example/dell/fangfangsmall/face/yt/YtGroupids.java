package com.example.dell.fangfangsmall.face.yt;


import com.example.dell.fangfangsmall.face.yt.YtPerson;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 * {"group_ids":["person_id001","1234567","person_id1503654826108","123456","autheneication"],"errorcode":0,"errormsg":"OK"}
 */

public class YtGroupids extends YtPerson {

    private List<String> group_ids;

    public List<String> getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(List<String> group_ids) {
        this.group_ids = group_ids;
    }

    @Override
    public String toString() {
        return "YtGroupids{" +
                "group_ids=" + group_ids +
                '}';
    }
}
