package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/1/31.
 */

public class CompanyInfo implements Serializable {


    /**
     * ID : 1
     * JOBFAILMASTERID : 9
     * COMPANYNAME : 上海之心有限公司
     * CREATEUSER : 1
     * CREATETIME : 2018-02-04T17:08:15.08
     * JOBNUM : 3
     * UPDATE_DATE : 2018-02-04T18:03:30.553
     * UPDATE_STAFF : 1
     * RecordCount : 1
     * CreateUserName : admin
     */

    private int ID;
    private int JOBFAILMASTERID;
    private String COMPANYNAME;
    private int CREATEUSER;
    private String CREATETIME;
    private int JOBNUM;
    private String UPDATE_DATE;
    private int UPDATE_STAFF;
    private int RecordCount;
    private String CreateUserName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getJOBFAILMASTERID() {
        return JOBFAILMASTERID;
    }

    public void setJOBFAILMASTERID(int JOBFAILMASTERID) {
        this.JOBFAILMASTERID = JOBFAILMASTERID;
    }

    public String getCOMPANYNAME() {
        return COMPANYNAME;
    }

    public void setCOMPANYNAME(String COMPANYNAME) {
        this.COMPANYNAME = COMPANYNAME;
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

    public int getJOBNUM() {
        return JOBNUM;
    }

    public void setJOBNUM(int JOBNUM) {
        this.JOBNUM = JOBNUM;
    }

    public String getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(String UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }

    public int getUPDATE_STAFF() {
        return UPDATE_STAFF;
    }

    public void setUPDATE_STAFF(int UPDATE_STAFF) {
        this.UPDATE_STAFF = UPDATE_STAFF;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String CreateUserName) {
        this.CreateUserName = CreateUserName;
    }
}
