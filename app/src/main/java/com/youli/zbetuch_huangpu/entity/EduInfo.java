package com.youli.zbetuch_huangpu.entity;

/**
 * Created by liutao on 2018/1/4.
 *
 *
 * http://web.youli.pw:8088/Json/Get_WHCD.aspx
 */

public class EduInfo {


    /**
     * WHCDID : 37
     * WHCD : 不明
     * RecordCount : 0
     */

    private int WHCDID;
    private String WHCD;
    private int RecordCount;

    public int getWHCDID() {
        return WHCDID;
    }

    public void setWHCDID(int WHCDID) {
        this.WHCDID = WHCDID;
    }

    public String getWHCD() {
        return WHCD;
    }

    public void setWHCD(String WHCD) {
        this.WHCD = WHCD;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }
}
