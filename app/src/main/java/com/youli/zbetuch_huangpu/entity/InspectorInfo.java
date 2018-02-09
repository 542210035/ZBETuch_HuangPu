package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * 作者: zhengbin on 2017/12/4.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * http://web.youli.pw:8088/Json/First/Get_ManageWork.aspx?page=0&rows=1
 *
 * [{"id":3,"title":"测试","notice_time":"2017-12-01T00:00:00","create_date":"2017-12-02T15:58:23.887","type":"已完成"}]
 *
 *
 */

public class InspectorInfo implements Serializable{

    private int id;
    private String title;//工作名称
    private String create_date;//创建时间
    private String notice_time;//完成时间
    private String type;//完成状态

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
