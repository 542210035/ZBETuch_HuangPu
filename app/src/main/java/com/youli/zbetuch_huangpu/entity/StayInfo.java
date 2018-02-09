package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2017/12/5.
 *
 * [{"id":4,"title":"任务1","doc":"热歌如果套改他人","notice_time":"2017-12-05T00:00:00","create_staff":1,
 * "create_date":"2017-12-05T13:58:33.297","update_staff":1,"update_date":"2017-12-08T09:55:59.99","check_staff":-1,
 * "mark":null,"type":"未完成"}]
 */

public class StayInfo implements Serializable {

    private int id;
    private String title;
    private String doc;
    private String notice_time;
    private  int create_staff;
    private String create_date;
    private  int update_staff;
    private String update_date;
    private int check_staff;
    private String mark;
    private String type;

    public int getCheck_staff() {
        return check_staff;
    }

    public void setCheck_staff(int check_staff) {
        this.check_staff = check_staff;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getCreate_staff() {
        return create_staff;
    }

    public void setCreate_staff(int create_staff) {
        this.create_staff = create_staff;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNotice_time() {
        return notice_time;
    }

    public void setNotice_time(String notice_time) {
        this.notice_time = notice_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public int getUpdate_staff() {
        return update_staff;
    }

    public void setUpdate_staff(int update_staff) {
        this.update_staff = update_staff;
    }
}
