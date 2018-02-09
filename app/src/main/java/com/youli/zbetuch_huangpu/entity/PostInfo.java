package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/2/7.
 */

public class PostInfo implements Serializable{


    /**
     * ID : 10
     * JOBNAME : 财务
     * JOBFAILMASTERID : 8
     * CREATEUSER : 1
     * CREATETIME : 2018-02-04T20:15:47.283
     * UPDATEUSER : 1
     * UPDATETIME : 2018-02-04T20:15:47.283
     * JOBCOMPANYID : 7
     * RecordCount : 1
     * MSNum : 0
     * IsUpdate : false
     * IsDel : false
     * JobFailName : 山龙
     * CompanyName : 上海星火日夜实业公司
     */

    private int ID;
    private String JOBNAME;
    private int JOBFAILMASTERID;
    private int CREATEUSER;
    private String CREATETIME;
    private int UPDATEUSER;
    private String UPDATETIME;
    private int JOBCOMPANYID;
    private int RecordCount;
    private int MSNum;
    private boolean IsUpdate;
    private boolean IsDel;
    private String JobFailName;
    private String CompanyName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getJOBNAME() {
        return JOBNAME;
    }

    public void setJOBNAME(String JOBNAME) {
        this.JOBNAME = JOBNAME;
    }

    public int getJOBFAILMASTERID() {
        return JOBFAILMASTERID;
    }

    public void setJOBFAILMASTERID(int JOBFAILMASTERID) {
        this.JOBFAILMASTERID = JOBFAILMASTERID;
    }

    public int getCREATEUSER() {
        return CREATEUSER;
    }

    public void setCREATEUSER(int CREATEUSER) {
        this.CREATEUSER = CREATEUSER;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public int getUPDATEUSER() {
        return UPDATEUSER;
    }

    public void setUPDATEUSER(int UPDATEUSER) {
        this.UPDATEUSER = UPDATEUSER;
    }

    public String getUPDATETIME() {
        return UPDATETIME;
    }

    public void setUPDATETIME(String UPDATETIME) {
        this.UPDATETIME = UPDATETIME;
    }

    public int getJOBCOMPANYID() {
        return JOBCOMPANYID;
    }

    public void setJOBCOMPANYID(int JOBCOMPANYID) {
        this.JOBCOMPANYID = JOBCOMPANYID;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public int getMSNum() {
        return MSNum;
    }

    public void setMSNum(int MSNum) {
        this.MSNum = MSNum;
    }

    public boolean isIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(boolean IsUpdate) {
        this.IsUpdate = IsUpdate;
    }

    public boolean isIsDel() {
        return IsDel;
    }

    public void setIsDel(boolean IsDel) {
        this.IsDel = IsDel;
    }

    public String getJobFailName() {
        return JobFailName;
    }

    public void setJobFailName(String JobFailName) {
        this.JobFailName = JobFailName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }
}
