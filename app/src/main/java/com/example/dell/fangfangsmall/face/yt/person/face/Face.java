package com.example.dell.fangfangsmall.face.yt.person.face;

/**
 * Created by zhangyuanyuan on 2017/9/6.
 */

public class Face {


    private String face_id;//人脸标识
    private int x;//人脸框左上角 x
    private int y;//人脸框左上角 y
    private float width;//人脸框宽度
    private float height;//人脸框高度
    private int gender;//性别 [0(female)~100(male)]
    private int age;//年龄 [0~100]
    private int expression;//微笑[0(normal)~50(smile)~100(laugh)]
    private boolean glass;//是否有眼镜 [true,false]
    private int pitch;//上下偏移[-30,30]
    private int yaw;//左右偏移[-30,30]
    private int roll;//平面旋转[-180,180]
    private int beauty;//魅力[0~100]
//    private Face_shape face_shape;

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }

//    public Face_shape getFace_shape() {
//        return face_shape;
//    }
//
//    public void setFace_shape(Face_shape face_shape) {
//        this.face_shape = face_shape;
//    }

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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getGender() {
        return gender > 50 ? "男" : "女";
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getExpression() {
        if(expression > 0 && expression < 33){
            return "正常";
        }else if(expression >= 33 && expression <= 66){
            return "微笑";
        }else{
            return "笑";
        }
    }

    public void setExpression(int expression) {
        this.expression = expression;
    }

    public boolean isGlass() {
        return glass;
    }

    public void setGlass(boolean glass) {
        this.glass = glass;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

}
