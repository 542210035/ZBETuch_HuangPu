package com.youli.zbetuch_huangpu.entity;

/**
 * 作者: zhengbin on 2017/12/5.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * [{"ID":2,"STAFF":1,"SFZ":"310102197209103215","CREATE_TIME":"2017-12-08T10:37:04.733","NAME":"陈建荣","RecordCount":1,"Delete1":false}]
 */

public class MyFollowInfo {

    private int ID;
    private int STAFF;
    private String NAME;
    private String SFZ;
    private String CREATE_TIME;
    private int RecordCount;
    private boolean Delete1;

    public MyFollowInfo(String NAME, String SFZ) {
        this.NAME = NAME;
        this.SFZ = SFZ;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public boolean isDelete1() {
        return Delete1;
    }

    public void setDelete1(boolean delete1) {
        Delete1 = delete1;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public String getSFZ() {
        return SFZ;
    }

    public void setSFZ(String SFZ) {
        this.SFZ = SFZ;
    }

    public int getSTAFF() {
        return STAFF;
    }

    public void setSTAFF(int STAFF) {
        this.STAFF = STAFF;
    }
}
