package com.youli.zbetuch_huangpu.entity;

/**
 * Created by liutao on 2018/1/5.
 *
 * http://web.youli.pw:8088/Json/Get_JW.aspx?JDID=3
 */

public class PersonJwInfo {


    /**
     * JWID : 47
     * JWMC : 保屯居委
     * JDID : 3
     * RecordCount : 0
     */

    private int JWID;
    private String JWMC;
    private int JDID;
    private int RecordCount;

    public int getJWID() {
        return JWID;
    }

    public void setJWID(int JWID) {
        this.JWID = JWID;
    }

    public String getJWMC() {
        return JWMC;
    }

    public void setJWMC(String JWMC) {
        this.JWMC = JWMC;
    }

    public int getJDID() {
        return JDID;
    }

    public void setJDID(int JDID) {
        this.JDID = JDID;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }
}
