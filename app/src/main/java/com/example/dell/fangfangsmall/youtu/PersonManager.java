package com.example.dell.fangfangsmall.youtu;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;
import com.example.dell.fangfangsmall.youtu.thread.FaceVerifyThread;


/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public class PersonManager {

//    public static void detectFace(Handler handler, Bitmap bitmap, int mode, SimpleCallback<YtDetectFace> simpleCallback){
//        DetectFaceThread detectFaceThread = new DetectFaceThread(handler, bitmap, mode, simpleCallback);
//        new Thread(detectFaceThread).start();
//    }
//
//    public static void delFace(Handler mHandler, String personId, String faceId, SimpleCallback<YtDelface> simpleCallback){
//        List<String> face_id_arr = new ArrayList<>();
//        face_id_arr.add(faceId);
//        DelFaceThread delFaceThread = new DelFaceThread(mHandler, personId, face_id_arr, simpleCallback);
//        new Thread(delFaceThread).start();
//    }
//
//    public static void getFaceIds(Handler mHandler, String personId, SimpleCallback<YtFaceids> simpleCallback){
//        GetFaceIdsThread getFaceIdsThread = new GetFaceIdsThread(mHandler, personId, simpleCallback);
//        new Thread(getFaceIdsThread).start();
//    }
//
//    public static void getinfo(Handler mHandler, String personId, SimpleCallback<YtPersonInfo> simpleCallback){
//        GetinfoThread getinfoThread = new GetinfoThread(mHandler, personId, simpleCallback);
//        new Thread(getinfoThread).start();
//    }
//
//    public static void getFaceInfo(Handler mHandler, String faceId, SimpleCallback<YtFaceInfoResult> simpleCallback){
//        GetFaceInfoThread getFaceInfoThread = new GetFaceInfoThread(mHandler, faceId, simpleCallback);
//        new Thread(getFaceInfoThread).start();
//    }
//
//    public static void modify(Handler mHandler, int type, String modify, String personId, SimpleCallback<YtSetperson> simpleCallback){
//        ModifyThread modifyThread = new ModifyThread(mHandler, type, modify, personId, simpleCallback);
//        new Thread(modifyThread).start();
//    }
//
//    public static void addFace(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtAddperson> simpleCallback){
//        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
//        bitmaps.add(bitmap);
//        AddFaceThread addFaceThread = new AddFaceThread(mHandler, personId, bitmaps, simpleCallback);
//        new Thread(addFaceThread).start();
//    }
//
//    public static void getPersonIds(Handler mHandler, SimpleCallback<YtPersonids> simpleCallback){
//        GetPersonIdsThread getPersonIdsThread = new GetPersonIdsThread(mHandler, simpleCallback);
//        new Thread(getPersonIdsThread).start();
//    }

    public static void faceVerify(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtVerifyperson> simpleCallback){
        FaceVerifyThread faceVerifyThread = new FaceVerifyThread(mHandler, personId, bitmap, simpleCallback);
        new Thread(faceVerifyThread).start();
    }

//    public static void delperson(Handler mHandler, String person_id, SimpleCallback<YtDelperson> callback) {
//        DelpersonThread delpersonThread = new DelpersonThread(mHandler, person_id, callback);
//        new Thread(delpersonThread).start();
//    }

//    public static void newperson(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtNewperson> simpleCallback){
//        NewPersonThread newPersonThread = new NewPersonThread(mHandler, personId, bitmap, simpleCallback);
//        new Thread(newPersonThread).start();
//    }


}
