package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/3/5.
 */

public class SignInfo implements Serializable {

    /**
     * MDID : 3
     * HDID : 8
     * ZJHM : 310230198204154258
     * XM : 的士速递
     * GENDER : 男
     * WHCD : 不明
     * LXDH : 123123123123
     * EMAIL :
     * CYZLX :
     * CYXM :
     * FWXM :
     * TJDATE : 2018-03-05T13:09:20.427
     * TJJD : 黄浦区
     * TJRXM : admin
     * BZSM :
     * BMZT : 确认报名
     * RecordCount : 1
     * IsQD : true
     */

    private int MDID;
    private int HDID;
    private String ZJHM;
    private String XM;
    private String GENDER;
    private String WHCD;
    private String LXDH;
    private String EMAIL;
    private String CYZLX;
    private String CYXM;
    private String FWXM;
    private String TJDATE;
    private String TJJD;
    private String TJRXM;
    private String BZSM;
    private String BMZT;
    private int RecordCount;
    private boolean IsQD;

    public int getMDID() {
        return MDID;
    }

    public void setMDID(int MDID) {
        this.MDID = MDID;
    }

    public int getHDID() {
        return HDID;
    }

    public void setHDID(int HDID) {
        this.HDID = HDID;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getWHCD() {
        return WHCD;
    }

    public void setWHCD(String WHCD) {
        this.WHCD = WHCD;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCYZLX() {
        return CYZLX;
    }

    public void setCYZLX(String CYZLX) {
        this.CYZLX = CYZLX;
    }

    public String getCYXM() {
        return CYXM;
    }

    public void setCYXM(String CYXM) {
        this.CYXM = CYXM;
    }

    public String getFWXM() {
        return FWXM;
    }

    public void setFWXM(String FWXM) {
        this.FWXM = FWXM;
    }

    public String getTJDATE() {
        return TJDATE;
    }

    public void setTJDATE(String TJDATE) {
        this.TJDATE = TJDATE;
    }

    public String getTJJD() {
        return TJJD;
    }

    public void setTJJD(String TJJD) {
        this.TJJD = TJJD;
    }

    public String getTJRXM() {
        return TJRXM;
    }

    public void setTJRXM(String TJRXM) {
        this.TJRXM = TJRXM;
    }

    public String getBZSM() {
        return BZSM;
    }

    public void setBZSM(String BZSM) {
        this.BZSM = BZSM;
    }

    public String getBMZT() {
        return BMZT;
    }

    public void setBMZT(String BMZT) {
        this.BMZT = BMZT;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public boolean isIsQD() {
        return IsQD;
    }

    public void setIsQD(boolean IsQD) {
        this.IsQD = IsQD;
    }
}
