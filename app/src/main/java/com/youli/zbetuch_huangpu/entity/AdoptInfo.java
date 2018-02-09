package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/2/8.
 */

public class AdoptInfo implements Serializable{


    /**
     * ID : 2
     * COMPANYNAME : 上海星火日夜实业公司
     * JOBNAME : 财务
     * NAME : 益卿
     * IDCARD : 632800198002041422
     * PHONE : 15129009320
     * YXLY :
     * FX : √
     * BLY :
     * YBLYLSX :
     * WBLLYSX :
     * JYCOMAPNAY :
     * NOTE :
     * CREATE_USER : 1
     * CREATE_TIME : 2018-02-07T15:22:38.82
     * UPDATE_USER : 1
     * UPDATE_TIME : 2018-02-07T15:45:40.533
     * JOBID : 24
     * JOBFAILID : 11
     * RecordCount : 5
     * IsCreate : false
     */

    private int ID;
    private String COMPANYNAME;
    private String JOBNAME;
    private String NAME;
    private String IDCARD;
    private String PHONE;
    private String YXLY;
    private String FX;
    private String BLY;
    private String YBLYLSX;
    private String WBLLYSX;
    private String JYCOMAPNAY;
    private String NOTE;
    private int CREATE_USER;
    private String CREATE_TIME;
    private int UPDATE_USER;
    private String UPDATE_TIME;
    private int JOBID;
    private int JOBFAILID;
    private int RecordCount;
    private boolean IsCreate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCOMPANYNAME() {
        return COMPANYNAME;
    }

    public void setCOMPANYNAME(String COMPANYNAME) {
        this.COMPANYNAME = COMPANYNAME;
    }

    public String getJOBNAME() {
        return JOBNAME;
    }

    public void setJOBNAME(String JOBNAME) {
        this.JOBNAME = JOBNAME;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getIDCARD() {
        return IDCARD;
    }

    public void setIDCARD(String IDCARD) {
        this.IDCARD = IDCARD;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getYXLY() {
        return YXLY;
    }

    public void setYXLY(String YXLY) {
        this.YXLY = YXLY;
    }

    public String getFX() {
        return FX;
    }

    public void setFX(String FX) {
        this.FX = FX;
    }

    public String getBLY() {
        return BLY;
    }

    public void setBLY(String BLY) {
        this.BLY = BLY;
    }

    public String getYBLYLSX() {
        return YBLYLSX;
    }

    public void setYBLYLSX(String YBLYLSX) {
        this.YBLYLSX = YBLYLSX;
    }

    public String getWBLLYSX() {
        return WBLLYSX;
    }

    public void setWBLLYSX(String WBLLYSX) {
        this.WBLLYSX = WBLLYSX;
    }

    public String getJYCOMAPNAY() {
        return JYCOMAPNAY;
    }

    public void setJYCOMAPNAY(String JYCOMAPNAY) {
        this.JYCOMAPNAY = JYCOMAPNAY;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public int getCREATE_USER() {
        return CREATE_USER;
    }

    public void setCREATE_USER(int CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public int getUPDATE_USER() {
        return UPDATE_USER;
    }

    public void setUPDATE_USER(int UPDATE_USER) {
        this.UPDATE_USER = UPDATE_USER;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public int getJOBID() {
        return JOBID;
    }

    public void setJOBID(int JOBID) {
        this.JOBID = JOBID;
    }

    public int getJOBFAILID() {
        return JOBFAILID;
    }

    public void setJOBFAILID(int JOBFAILID) {
        this.JOBFAILID = JOBFAILID;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public boolean isIsCreate() {
        return IsCreate;
    }

    public void setIsCreate(boolean IsCreate) {
        this.IsCreate = IsCreate;
    }
}
