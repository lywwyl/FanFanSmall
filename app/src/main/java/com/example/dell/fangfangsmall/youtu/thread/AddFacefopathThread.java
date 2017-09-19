package com.example.dell.fangfangsmall.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtAddperson;
import com.example.dell.fangfangsmall.util.BitmapUtils;
import com.example.dell.fangfangsmall.util.GsonUtil;
import com.example.dell.fangfangsmall.youtu.YoutuManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/19.
 */

public class AddFacefopathThread extends ReqestThread<YtAddperson> {

    private String personId;
    private List<String> paths;

    public AddFacefopathThread(Handler mHandler, String personId, List<String> paths, SimpleCallback<YtAddperson> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
        this.paths = paths;
    }

    @Override
    protected YtAddperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        List<Bitmap> bitmap_arr = new ArrayList<>();
        for(int i = 0; i <paths.size(); i = i+2){
            String path = paths.get(i);
            Bitmap bitmap = BitmapUtils.transformFiletoBitmap(path);
            if(bitmap != null) {
                bitmap_arr.add(bitmap);
            }
        }
        //{"added":0,"face_ids":["2234449692784078785","2234449695788249025","2234449698729504705","2234449701743112129"],
        // "ret_codes":[-1312,-1312,-1312,-1312],"session_id":"","errorcode":0,"errormsg":"OK"}
        JSONObject jsonObject = YoutuManager.getInstence().AddFace(personId, bitmap_arr);
        YtAddperson ytAddperson = GsonUtil.GsonToBean(jsonObject.toString(), YtAddperson.class);
        return ytAddperson;
    }
}
