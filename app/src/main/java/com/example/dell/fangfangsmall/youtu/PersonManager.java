package com.example.dell.fangfangsmall.youtu;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.YtFaceids;
import com.example.dell.fangfangsmall.face.yt.YtGroupids;
import com.example.dell.fangfangsmall.face.yt.YtPersonids;
import com.example.dell.fangfangsmall.face.yt.person.YtAddperson;
import com.example.dell.fangfangsmall.face.yt.person.YtDelperson;
import com.example.dell.fangfangsmall.face.yt.person.YtNewperson;
import com.example.dell.fangfangsmall.face.yt.person.YtPersonInfo;
import com.example.dell.fangfangsmall.face.yt.person.YtSetperson;
import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;
import com.example.dell.fangfangsmall.face.yt.person.face.YtDelface;
import com.example.dell.fangfangsmall.face.yt.person.face.YtDetectFace;
import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceIdentify;
import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceInfoResult;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;
import com.example.dell.fangfangsmall.youtu.thread.AddFaceThread;
import com.example.dell.fangfangsmall.youtu.thread.DelFaceThread;
import com.example.dell.fangfangsmall.youtu.thread.DelpersonThread;
import com.example.dell.fangfangsmall.youtu.thread.DetectFaceThread;
import com.example.dell.fangfangsmall.youtu.thread.FaceIdentifyThread;
import com.example.dell.fangfangsmall.youtu.thread.FaceVerifyThread;
import com.example.dell.fangfangsmall.youtu.thread.GetFaceIdsThread;
import com.example.dell.fangfangsmall.youtu.thread.GetFaceInfoThread;
import com.example.dell.fangfangsmall.youtu.thread.GetGroupThread;
import com.example.dell.fangfangsmall.youtu.thread.GetPersonIdsThread;
import com.example.dell.fangfangsmall.youtu.thread.GetinfoThread;
import com.example.dell.fangfangsmall.youtu.thread.ModifyThread;
import com.example.dell.fangfangsmall.youtu.thread.NewPersonThread;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public class PersonManager {

    /**
     * 获取appid下所有组
     * @param handler
     * @param simpleCallback
     */
    public static void getGroup(Handler handler, SimpleCallback<YtGroupids> simpleCallback){
        GetGroupThread getGroupThread = new GetGroupThread(handler, simpleCallback);
        new Thread(getGroupThread).start();
    }
    /**
     * 识别一张人脸图片在一个group中，返回最相似的五个人的信息
     * @param handler
     * @param bitmap
     * @param simpleCallback
     */
    public static void faceIdentify(Handler handler, Bitmap bitmap, SimpleCallback<YtFaceIdentify> simpleCallback){
        FaceIdentifyThread faceIdentifyThread = new FaceIdentifyThread(handler, bitmap, simpleCallback);
        new Thread(faceIdentifyThread).start();
    }

    /**
     * 人脸属性分析 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性。位置包括(x, y, w, h)，
     * 面部属性包括性别(gender), 年龄(age), 表情(expression), 眼镜(glass)和姿态(pitch，roll，yaw).
     * @param handler
     * @param bitmap
     * @param mode
     * @param simpleCallback
     */
    public static void detectFace(Handler handler, Bitmap bitmap, int mode, SimpleCallback<YtDetectFace> simpleCallback){
        DetectFaceThread detectFaceThread = new DetectFaceThread(handler, bitmap, mode, simpleCallback);
        new Thread(detectFaceThread).start();
    }

    /**
     * 删除一个person下的face，包括特征，属性和face_id.
     * @param mHandler
     * @param personId
     * @param faceId
     * @param simpleCallback
     */
    public static void delFace(Handler mHandler, String personId, String faceId, SimpleCallback<YtDelface> simpleCallback){
        List<String> face_id_arr = new ArrayList<>();
        face_id_arr.add(faceId);
        DelFaceThread delFaceThread = new DelFaceThread(mHandler, personId, face_id_arr, simpleCallback);
        new Thread(delFaceThread).start();
    }

    /**
     * 获取一个组person中所有face列表
     * @param mHandler
     * @param personId
     * @param simpleCallback
     */
    public static void getFaceIds(Handler mHandler, String personId, SimpleCallback<YtFaceids> simpleCallback){
        GetFaceIdsThread getFaceIdsThread = new GetFaceIdsThread(mHandler, personId, simpleCallback);
        new Thread(getFaceIdsThread).start();
    }

    /**
     * 获取一个Person的信息, 包括name, id, tag, 相关的face, 以及groups等信息。
     * @param mHandler
     * @param personId
     * @param simpleCallback
     */
    public static void getinfo(Handler mHandler, String personId, SimpleCallback<YtPersonInfo> simpleCallback){
        GetinfoThread getinfoThread = new GetinfoThread(mHandler, personId, simpleCallback);
        new Thread(getinfoThread).start();
    }

    /**
     * 获取一个face的相关特征信息
     * @param mHandler
     * @param faceId
     * @param simpleCallback
     */
    public static void getFaceInfo(Handler mHandler, String faceId, SimpleCallback<YtFaceInfoResult> simpleCallback){
        GetFaceInfoThread getFaceInfoThread = new GetFaceInfoThread(mHandler, faceId, simpleCallback);
        new Thread(getFaceInfoThread).start();
    }

    /**
     * 设置Person的name.
     * @param mHandler
     * @param type
     * @param modify
     * @param personId
     * @param simpleCallback
     */
    public static void modify(Handler mHandler, int type, String modify, String personId, SimpleCallback<YtSetperson> simpleCallback){
        ModifyThread modifyThread = new ModifyThread(mHandler, type, modify, personId, simpleCallback);
        new Thread(modifyThread).start();
    }

    /**
     * 增加一个人脸Face
     * @param mHandler
     * @param personId
     * @param bitmap
     * @param simpleCallback
     */
    public static void addFace(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtAddperson> simpleCallback){
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        bitmaps.add(bitmap);
        AddFaceThread addFaceThread = new AddFaceThread(mHandler, personId, bitmaps, simpleCallback);
        new Thread(addFaceThread).start();
    }

    /**
     * 添加一组人脸
     * @param mHandler
     * @param personId
     * @param bitmaps
     * @param simpleCallback
     */
    public static void addFaces(Handler mHandler, String personId, List<Bitmap> bitmaps, SimpleCallback<YtAddperson> simpleCallback){
        AddFaceThread addFaceThread = new AddFaceThread(mHandler, personId, bitmaps, simpleCallback);
        new Thread(addFaceThread).start();
    }

    /**
     * 获取一个组Group中所有person列表
     * @param mHandler
     * @param simpleCallback
     */
    public static void getPersonIds(Handler mHandler, SimpleCallback<YtPersonids> simpleCallback){
        GetPersonIdsThread getPersonIdsThread = new GetPersonIdsThread(mHandler, simpleCallback);
        new Thread(getPersonIdsThread).start();
    }

    /**
     * 人脸验证，给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。
     * @param mHandler
     * @param personId
     * @param bitmap
     * @param simpleCallback
     */
    public static void faceVerify(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtVerifyperson> simpleCallback){
        FaceVerifyThread faceVerifyThread = new FaceVerifyThread(mHandler, personId, bitmap, simpleCallback);
        new Thread(faceVerifyThread).start();
    }

    /**
     * 删除一个person下的face，包括特征，属性和face_id.
     * @param mHandler
     * @param person_id
     * @param callback
     */
    public static void delperson(Handler mHandler, String person_id, SimpleCallback<YtDelperson> callback) {
        DelpersonThread delpersonThread = new DelpersonThread(mHandler, person_id, callback);
        new Thread(delpersonThread).start();
    }

    /**
     * 创建一个Person，并将Person放置到group_ids指定的组当中
     * @param mHandler
     * @param personId
     * @param bitmap
     * @param simpleCallback
     */
    public static void newperson(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtNewperson> simpleCallback){
        NewPersonThread newPersonThread = new NewPersonThread(mHandler, personId, bitmap, simpleCallback);
        new Thread(newPersonThread).start();
    }


}
