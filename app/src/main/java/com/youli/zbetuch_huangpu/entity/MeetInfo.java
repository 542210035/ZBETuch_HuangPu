package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * 作者: zhengbin on 2017/12/2.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 当前会议和历史会议的实体类
 *
 * http://web.youli.pw:8088/Json/First/Get_Meeting_Master.aspx?page=0&rows=20&type=1 0当前会议 1历史会议
 *
 *
 * [{"ID":2,"TITLE":"​就业召开会议通知","DOC":"","MEETING_TIME":"2017-10-31T09:00:00","MEETING_ADD":"就促中心",
 * "CREATE_STAFF":1,"CREATE_DATE":"2017-10-27T09:30:36.347","RecordCount":2,"CREATE_STAFF_NAME":"admin",
 * "METTING_STAFFS":null,"Checks":null}]
 */

public class MeetInfo implements Serializable{

    private int ID;
    private String TITLE;//会议主题
    private String DOC;//会议内容
    private String MEETING_ADD;//会议地址
    private String MEETING_TIME;//会议时间
    private int CREATE_STAFF;
    private String CREATE_STAFF_NAME;//通知人
    private String CREATE_DATE;//发布时间
    private int RecordCount;//总计
    private String METTING_STAFFS;
    private String Checks;

    public String getChecks() {
        return Checks;
    }

    public void setChecks(String checks) {
        Checks = checks;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public int getCREATE_STAFF() {
        return CREATE_STAFF;
    }

    public void setCREATE_STAFF(int CREATE_STAFF) {
        this.CREATE_STAFF = CREATE_STAFF;
    }

    public String getCREATE_STAFF_NAME() {
        return CREATE_STAFF_NAME;
    }

    public void setCREATE_STAFF_NAME(String CREATE_STAFF_NAME) {
        this.CREATE_STAFF_NAME = CREATE_STAFF_NAME;
    }

    public String getDOC() {
        return DOC;
    }

    public void setDOC(String DOC) {
        this.DOC = DOC;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMEETING_ADD() {
        return MEETING_ADD;
    }

    public void setMEETING_ADD(String MEETING_ADD) {
        this.MEETING_ADD = MEETING_ADD;
    }

    public String getMEETING_TIME() {
        return MEETING_TIME;
    }

    public void setMEETING_TIME(String MEETING_TIME) {
        this.MEETING_TIME = MEETING_TIME;
    }

    public String getMETTING_STAFFS() {
        return METTING_STAFFS;
    }

    public void setMETTING_STAFFS(String METTING_STAFFS) {
        this.METTING_STAFFS = METTING_STAFFS;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}
