package com.example.dell.fangfangsmall.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by kaifa on 2017/6/28.
 */
@Entity
public class UserInfo {
    @Id(autoincrement = true)
    private Long id;
    private String name;

    private String type;
    private String sendtype;
    private String question;
    private String content;
    private String action;
    private String expression;
    private String img;
    @Generated(hash = 1715706603)
    public UserInfo(Long id, String name, String type, String sendtype,
            String question, String content, String action, String expression,
            String img) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sendtype = sendtype;
        this.question = question;
        this.content = content;
        this.action = action;
        this.expression = expression;
        this.img = img;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public UserInfo(String type, String sendtype,
                    String question, String content) {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendtype() {
        return this.sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
