package com.example.dell.fangfangsmall.youtu;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.dell.fangfangsmall.util.BitmapUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/25.
 */

public class YoutuManager {

    private static List<String> mGroupId;
    public static String GROUP_ID = "Delivery-master";

    public static String APP_ID = "10098751";
    public static String SECRET_ID = "AKIDLVQ6XjvMnzxf6HcrGQKyLRHrBxUmZdCV";
    public static String SECRET_KEY = "C9qIVTCfuue9PQCiz2rRlriKhphKirkN";



    public static int EXPIRED_SECONDS = 2592000;

    public static final int MODIFY_NAME_CODE = 100;
    public static final int MODIFY_TAG_CODE = 101;

    private static YoutuManager mInstence;

    private String sign;

    private YoutuManager() {
        if (mInstence == null) {
            generateSign();
        }
    }

    private static class YoutuManagerHolder {
        private static final YoutuManager INSTANCE = new YoutuManager();
    }

    public static YoutuManager getInstence() {
        return YoutuManagerHolder.INSTANCE;
    }

    private void generateSign() {
        StringBuffer mySign = new StringBuffer("");
        YoutuSign.appSign(APP_ID, SECRET_ID, SECRET_KEY,
                System.currentTimeMillis() / 1000 + EXPIRED_SECONDS, mySign);
        sign = mySign.toString();
    }

    public static List<String> getmGroupId() {
        mGroupId = new ArrayList<String>();
        mGroupId.add(GROUP_ID);
        return mGroupId;
    }

    /*!
     * 人脸属性分析 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性。位置包括(x, y, w, h)，
	 * 面部属性包括性别(gender), 年龄(age), 表情(expression), 眼镜(glass)和姿态(pitch，roll，yaw).
	 *
	 * @param bitmap 人脸图片
	 * @param mode 检测模式 0/1 正常/大脸模式
	 * @return 请求json结果***
	*/
    public JSONObject detectFace(Bitmap bitmap, int mode) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);
        data.put("mode", mode);

        JSONObject respose = SendRequest(data, "api/detectface");

        return respose;
    }


    /*!
     * 人脸属性分析 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性。位置包括(x, y, w, h)，
     * 面部属性包括性别(gender), 年龄(age), 表情(expression), 眼镜(glass)和姿态(pitch，roll，yaw).
     *
     * @param url 人脸图片url
     * @param mode 检测模式 0/1 正常/大脸模式
     * @return 请求json结果
    */
    public JSONObject DetectFaceUrl(String url, int mode)
            throws IOException, JSONException, KeyManagementException,
            NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("mode", mode);
        JSONObject respose = SendRequest(data, "api/detectface");

        return respose;
    }


    /*!
     * 五官定位
     *
     * @param image
     *            人脸图片
     */
    public JSONObject FaceShape(Bitmap bitmap, int mode) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);
        data.put("mode", mode);
        JSONObject respose = SendRequest(data, "api/faceshape");

        return respose;
    }

    /*!
     * 五官定位
     *
     * @param url
     *            人脸图片url
     */
    public JSONObject FaceShapeUrl(String url, int mode) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("mode", mode);
        JSONObject respose = SendRequest(data, "api/faceshape");

        return respose;
    }

    /*!
     * 人脸对比， 计算两个Face的相似性以及五官相似度。
     *
     * @param bitmapA 第一张人脸图片
     * @param bitmapB 第二张人脸图片
     */
    public JSONObject FaceCompare(Bitmap bitmapA, Bitmap bitmapB)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmapA);
        data.put("imageA", imageData);

        imageData = BitmapUtils.bitmapToBase64(bitmapB);
        data.put("imageB", imageData);

        JSONObject respose = SendRequest(data, "api/facecompare");

        return respose;
    }

    /*!
     * 人脸对比， 计算两个Face的相似性以及五官相似度。
     *
     * @param urlA 第一张人脸图片url
     * @param urlB 第二张人脸图片url
     */
    public JSONObject FaceCompareUrl(String urlA, String urlB)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("urlA", urlA);
        data.put("urlB", urlB);

        JSONObject respose = SendRequest(data, "api/facecompare");

        return respose;
    }

    /*!
     * 人脸验证，给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。
     *
     * @param bitmap 需要验证的人脸图片
     * @param person_id 验证的目标person
    */
    public JSONObject FaceVerify(Bitmap bitmap, String person_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        data.put("person_id", person_id);

        JSONObject respose = SendRequest(data, "api/faceverify");

        return respose;
    }

    /*!
     * 人脸验证，给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。
     *
     * @param url 需要验证的人脸图片url
     * @param person_id 验证的目标person
    */
    public JSONObject FaceVerifyUrl(String url, String person_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("url", url);

        data.put("person_id", person_id);

        JSONObject respose = SendRequest(data, "api/faceverify");

        return respose;
    }

    /*!
     * 人脸识别，对于一个待识别的人脸图片，在一个Group中识别出最相似的Top5 Person作为其身份返回，返回的Top5中按照相似度从大到小排列。
     *
     * @param bitmap 需要识别的人脸图片
     * @param group_id 人脸face组
     */
    public JSONObject FaceIdentify(Bitmap bitmap, String group_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        data.put("group_id", group_id);

        JSONObject respose = SendRequest(data, "api/faceidentify");

        return respose;
    }

    /*!
     * 人脸识别，对于一个待识别的人脸图片，在一个Group中识别出最相似的Top5 Person作为其身份返回，返回的Top5中按照相似度从大到小排列。
     *
     * @param url 需要识别的人脸图片url
     * @param group_id 人脸face组
     */
    public JSONObject FaceIdentifyUrl(String url, String group_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("group_id", group_id);

        JSONObject respose = SendRequest(data, "api/faceidentify");

        return respose;
    }

    /*!
     * 创建一个Person，并将Person放置到group_ids指定的组当中
     *
     * @param bitmap 需要新建的人脸图片
     * @param person_id 指定创建的人脸
     * @param group_ids 加入的group列表
    */
    public JSONObject newPerson(Bitmap bitmap, String person_id,
                                List<String> group_ids) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        data.put("person_id", person_id);
        data.put("group_ids", new JSONArray(group_ids));

        JSONObject respose = SendRequest(data, "api/newperson");

        return respose;
    }

    /*!
     * 创建一个Person，并将Person放置到group_ids指定的组当中
     *
     * @param url 需要新建的人脸图片url
     * @param person_id 指定创建的人脸
     * @param group_ids 加入的group列表
    */
    public JSONObject NewPersonUrl(String url, String person_id,
                                   List<String> group_ids) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);

        data.put("person_id", person_id);
        data.put("group_ids", new JSONArray(group_ids));

        JSONObject respose = SendRequest(data, "api/newperson");

        return respose;
    }

    /*!
     * 删除一个person下的face，包括特征，属性和face_id.
     *
     * @param person_id 待删除人脸的person ID
    */
    public JSONObject delPerson(String person_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("person_id", person_id);

        JSONObject respose = SendRequest(data, "api/delperson");

        return respose;
    }

    /*!
     * 增加一个人脸Face.将一组Face加入到一个Person中。注意，一个Face只能被加入到一个Person中。
     * 一个Person最多允许包含100个Face。
     *
     * @param person_id 人脸Face的person id
     * @param bitmap_arr 人脸图片列表
    */
    public JSONObject AddFace(String person_id, List<Bitmap> bitmap_arr)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        List<String> images = new ArrayList<String>();
        for (Bitmap bitmap : bitmap_arr) {
            String imageData = BitmapUtils.bitmapToBase64(bitmap);
            images.add(imageData);
        }

        data.put("images", new JSONArray(images));

        data.put("person_id", person_id);

        JSONObject respose = SendRequest(data, "api/addface");

        return respose;
    }

    /*
     * 添加一组人脸
     * @param person_id
     * @param bitmap_arr
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public JSONObject AddFaces(String person_id, List<Bitmap> bitmap_arr)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        List<String> images = new ArrayList<String>();
        for (Bitmap bitmap : bitmap_arr) {
            String imageData = BitmapUtils.bitmapToBase64(bitmap);
            images.add(imageData);
        }

        data.put("images", new JSONArray(images));

        data.put("person_id", person_id);

        JSONObject respose = SendRequest(data, "api/addface");

        return respose;
    }

    /*!
     * 增加一个人脸Face.将一组Face加入到一个Person中。注意，一个Face只能被加入到一个Person中。
     * 一个Person最多允许包含100个Face。
     *
     * @param person_id 人脸Face的person id
     * @param url_arr 人脸图片url列表
    */
    public JSONObject AddFaceUrl(String person_id, List<String> url_arr)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("urls", new JSONArray(url_arr));
        data.put("person_id", person_id);

        JSONObject respose = SendRequest(data, "api/addface");

        return respose;
    }

    /*!
     * 删除一个person下的face，包括特征，属性和face_id.
     *
     * @param person_id 待删除人脸的person ID
     * @param face_id_arr 删除人脸id的列表
    */
    public JSONObject DelFace(String person_id, List<String> face_id_arr)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("face_ids", new JSONArray(face_id_arr));
        data.put("person_id", person_id);
        JSONObject respose = SendRequest(data, "api/delface");

        return respose;

    }

    /*!
     * 设置Person的name.
     *
     * @param person_name 新的name
     * @param person_id 要设置的person id
    */
    public JSONObject SetInfo(int type, String txt, String person_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        if (type == MODIFY_NAME_CODE) {

            data.put("person_name", txt);
        } else if (type == MODIFY_TAG_CODE) {

            data.put("tag", txt);
        }
        data.put("person_id", person_id);
        JSONObject respose = SendRequest(data, "api/setinfo");

        return respose;

    }


    /*!
     * 获取一个Person的信息, 包括name, id, tag, 相关的face, 以及groups等信息。
     *
     * @param person_id 待查询个体的ID
    */
    public JSONObject GetInfo(String person_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("person_id", person_id);
        JSONObject respose = SendRequest(data, "api/getinfo");

        return respose;
    }

    /*!
     * 获取一个AppId下所有group列表
     */
    public JSONObject GetGroupIds() throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        JSONObject respose = SendRequest(data, "api/getgroupids");

        return respose;
    }

    /*!
     * 获取一个组Group中所有person列表
     *
     * @param group_id 待查询的组id
    */
    public JSONObject GetPersonIds(String group_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("group_id", group_id);
        JSONObject respose = SendRequest(data, "api/getpersonids");

        return respose;
    }

    /*!
     * 获取一个组person中所有face列表
     *
     * @param person_id 待查询的个体id
    */
    public JSONObject GetFaceIds(String person_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("person_id", person_id);
        JSONObject respose = SendRequest(data, "api/getfaceids");

        return respose;
    }

    /*!
     * 获取一个face的相关特征信息
     *
     * @param face_id 带查询的人脸ID
    */
    public JSONObject GetFaceInfo(String face_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("face_id", face_id);
        JSONObject respose = SendRequest(data, "api/getfaceinfo");

        return respose;
    }


    /*!
     * 判断一个图像的模糊程度
     *
     * @param bitmap 输入图片
     */
    public JSONObject FuzzyDetect(Bitmap bitmap) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        JSONObject respose = SendRequest(data, "imageapi/fuzzydetect");

        return respose;
    }

    /*!
     * 判断一个图像的模糊程度
     *
     * @param url 输入图片url
     */
    public JSONObject FuzzyDetectUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", url);
        JSONObject respose = SendRequest(data, "imageapi/fuzzydetect");
        return respose;
    }

    /*!
     * 识别一个图像是否为美食图像
     *
     * @param bitmap 输入图片
     */
    public JSONObject FoodDetect(Bitmap bitmap) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        JSONObject respose = SendRequest(data, "imageapi/fooddetect");
        return respose;
    }

    /*!
     * 识别一个图像是否为美食图像
     *
     * @param url 输入图片url
     */
    public JSONObject FoodDetectUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", url);
        JSONObject respose = SendRequest(data, "imageapi/fooddetect");
        return respose;
    }


    /*!
     * 识别一个图像的标签信息,对图像分类。
     *
     * @param bitmap 输入图片
     */
    public JSONObject ImageTag(Bitmap bitmap) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        JSONObject respose = SendRequest(data, "imageapi/imagetag");
        return respose;
    }

    /*!
     * 识别一个图像的标签信息,对图像分类。
     *
     * @param url 输入图片url
     */
    public JSONObject ImageTagUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        JSONObject respose = SendRequest(data, "imageapi/imagetag");
        return respose;
    }

    /*!
     * 识别一个图像是否为色情图像
     *
     * @param bitmap 输入图片
     */
    public JSONObject ImagePorn(Bitmap bitmap) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);
        JSONObject respose = SendRequest(data, "imageapi/imageporn");
        return respose;
    }

    /*!
     * 识别一个图像是否为色情图像
     *
     * @param url 输入图片url
     */
    public JSONObject ImagePornUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        JSONObject respose = SendRequest(data, "imageapi/imageporn");
        return respose;
    }

	/*!
	 * 身份证OCR识别
	 *
	 * @param bitmap  输入图片
	 * @param cardType 身份证图片类型，0-正面，1-反面
	 */

    public JSONObject IdcardOcr(Bitmap bitmap, int cardType) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);
        data.put("card_type", cardType);

        JSONObject response = SendRequest(data, "ocrapi/idcardocr");
        return response;
    }

    /*!
     * 身份证OCR识别
     *
     * @param url  输入图片url
     * @param cardType 身份证图片类型，0-正面，1-反面
     */
    public JSONObject IdcardOcrUrl(String url, int cardType) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("card_type", cardType);

        JSONObject response = SendRequest(data, "ocrapi/idcardocr");
        return response;
    }

    /*!
     * 名片OCR识别
     *
     * @param bitmap  输入图片
     */
    public JSONObject NamecardOcr(Bitmap bitmap) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);

        JSONObject response = SendRequest(data, "ocrapi/namecardocr");
        return response;
    }

    /*!
     * 名片OCR识别
     *
     * @param url  输入图片url
     */
    public JSONObject NamecardOcrUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);

        JSONObject response = SendRequest(data, "ocrapi/namecardocr");
        return response;
    }


    //facein 人脸核身
	/*!
	 * 身份证OCR识别 --人脸核身相关接口
	 *
	 * @param bitmap  输入图片
	 * @param cardType 身份证图片类型，0-正面，1-反面
	 */
    public JSONObject IdcardOcrVIP(Bitmap bitmap, int cardType) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);
        data.put("card_type", cardType);

        JSONObject response = SendRequest(data, "ocrapi/idcardocr");
        return response;
    }

    /*!
 * 身份证实名认证 --人脸核身相关接口
 *
 * @param idcard_number  用户身份证号码
 * @param idcard_name 	用户身份证姓名
 */
    public JSONObject IdcardNameVIP(String idNum, String idName) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("idcard_number", idNum);
        data.put("idcard_name", idName);

        JSONObject response = SendRequest(data, "openliveapi/validateidcard");
        return response;
    }

	/*
	 *静态人脸比对:用户自带数据源比对 --人脸核身相关接口
	 * 人脸对比， 计算两个Face的相似性以及五官相似度。
	 *
	 * @param bitmapA 第一张人脸图片
	 * @param bitmapB 第二张人脸图片
	 */

    public JSONObject FaceCompareVip(Bitmap bitmapA, Bitmap bitmapB) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmapA);
        data.put("imageA", imageData);

        imageData = BitmapUtils.bitmapToBase64(bitmapB);
        data.put("imageB", imageData);

        JSONObject respose = SendRequest(data, "api/facecompare");

        return respose;
    }

    /*!
     * 静态人脸比对:使用优图数据源比对 --人脸核身相关接口
     *
     * @param idcard  用户身份证号码
     * @param name  用户身份证姓名
     * @param bitmap 输入图片
     */
    public JSONObject IdcardFaceCompare(Bitmap bitmap, String name, String idcard) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("image", imageData);
        data.put("idcard_number", idcard);
        data.put("idcard_name", name);

//		JSONObject respose = SendRequest(data, "openliveapi/idcardfacecompare");
        JSONObject respose = SendRequest(data, "face/idcardcompare");

        return respose;
    }


    /*
    *唇语获取 --人脸核身相关接口
    *
    */
    public JSONObject LivegetFour() throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        JSONObject respose = SendRequest(data, "openliveapi/livegetfour");

        return respose;
    }

    /*!
     * 视频人脸核身:用户自带数据源核身 --人脸核身相关接口
     *
     * @param video 需要检测的视频base64编码
     * @param validateDat livegetfour得到的唇语验证数据
     * @param bitmap 输入图片
     * @param isCompare video中的照片和card是否做对比，True做对比，False不做对比
     */
    public JSONObject LiveDetectFour(byte[] video, Bitmap bitmap, String validateData, boolean isCompare) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        String vedioData = Base64.encodeToString(video, Base64.DEFAULT);
        String imageData = BitmapUtils.bitmapToBase64(bitmap);
        data.put("video", vedioData);
        data.put("card", imageData);
        data.put("validate_data", validateData);
        data.put("compare_flag", isCompare);

        JSONObject respose = SendRequest(data, "openliveapi/livedetectfour");

        return respose;
    }

    /*!
     * 视频人脸核身:使用优图数据源核身 --人脸核身相关接口
     *
     * @param video 需要检测的视频base64编码
     * @param idcard 用户身份证号码
     * @param name 用户身份证姓名
     * @param validateData livegetfour得到的唇语验证数据
     */
    public JSONObject IdcardLiveDetectFour(byte[] video, String validateData, String name, String idcard) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        String vedioData = Base64.encodeToString(video, Base64.DEFAULT);
        data.put("video", vedioData);
        data.put("idcard_number", idcard);
        data.put("idcard_name", name);
        data.put("validate_data", validateData);

        JSONObject respose = SendRequest(data, "openliveapi/idcardlivedetectfour");

        return respose;
    }


    private JSONObject SendRequest(JSONObject postData, String method) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        return YoutuRequest.API_YOUTU_BASE.startsWith("https") ?
                YoutuRequest.sendHttpsRequest(postData, method, sign) : YoutuRequest.sendHttpRequest(postData, method, sign);
    }

}
