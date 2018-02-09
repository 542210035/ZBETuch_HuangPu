package com.youli.zbetuch_huangpu.entity;

/**
 * Created by liutao on 2018/1/4.
 *
 * http://web.youli.pw:8088/Json/Get_JD.aspx
 *
 */

public class PersonStreetInfo {


    /**
     * JDID : 1
     * JDMC : 南京东路街道
     * QXID : 1
     * RecordCount : 0
     */

    private int JDID;
    private String JDMC;
    private int QXID;
    private int RecordCount;

    public int getJDID() {
        return JDID;
    }

    public void setJDID(int JDID) {
        this.JDID = JDID;
    }

    public String getJDMC() {
        return JDMC;
    }

    public void setJDMC(String JDMC) {
        this.JDMC = JDMC;
    }

    public int getQXID() {
        return QXID;
    }

    public void setQXID(int QXID) {
        this.QXID = QXID;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }
}
