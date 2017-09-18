package com.example.dell.fangfangsmall.face.yt.person.face;

/**
 * Created by zhangyuanyuan on 2017/9/4.
 * {"face_id":"2212735432171481725",
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
 */

public class YtFaceInfo {

    private String face_id;
    private int x;//人脸框左上角 x
    private int y;//人脸框左上角 y
    private float height;//人脸框高度
    private float width;//人脸框宽度
    private int pitch;//上下偏移[-30,30]
    private int roll;//平面旋转[-180,180]
    private int yaw;//左右偏移[-30,30]
    private int age;//年龄 [0~100]
    private int gender;//性别 [0(female)~100(male)]
    private boolean glass;//是否有眼镜 [true,false]
    private int expression;//微笑[0(normal)~50(smile)~100(laugh)]
    private int beauty;//美女

    @Override
    public String toString() {
        return "YtFaceInfo{" +
                "年龄=" + age +
                ", 性别=" + gender +
                ", 眼镜=" + glass +
                ", 微笑=" + expression +
                ", 美女=" + beauty +
                '}';
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isGlass() {
        return glass;
    }

    public void setGlass(boolean glass) {
        this.glass = glass;
    }

    public int getExpression() {
        return expression;
    }

    public void setExpression(int expression) {
        this.expression = expression;
    }

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }

}
