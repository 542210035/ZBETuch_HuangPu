package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/1/29.
 */

public class TubeInfo implements Serializable {


    /**
     * ID : 6
     * JOBFAIRNAME : 4s
     * JOBFAIRDATA : 2018-01-29T12:39:51
     * JOBFAILADDRESS : 中山广场
     * CREATE_DATE : 2018-01-29T12:40:16.29
     * CREATE_STAFF : 1
     * UPDATE_DATE : 2018-01-29T12:40:16.29
     * UPDATE_STAFF : 1
     * RecordCount : 6
     * InterviewNum : 1
     * ThroughNum : 0
     * NotThroughNum : 0
     */

    private int ID;
    private String JOBFAIRNAME;
    private String JOBFAIRDATA;
    private String JOBFAILADDRESS;
    private String CREATE_DATE;
    private int CREATE_STAFF;
    private String UPDATE_DATE;
    private int UPDATE_STAFF;
    private int RecordCount;
    private int InterviewNum;
    private int ThroughNum;
    private int NotThroughNum;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getJOBFAIRNAME() {
        return JOBFAIRNAME;
    }

    public void setJOBFAIRNAME(String JOBFAIRNAME) {
        this.JOBFAIRNAME = JOBFAIRNAME;
    }

    public String getJOBFAIRDATA() {
        return JOBFAIRDATA;
    }

    public void setJOBFAIRDATA(String JOBFAIRDATA) {
        this.JOBFAIRDATA = JOBFAIRDATA;
    }

    public String getJOBFAILADDRESS() {
        return JOBFAILADDRESS;
    }

    public void setJOBFAILADDRESS(String JOBFAILADDRESS) {
        this.JOBFAILADDRESS = JOBFAILADDRESS;
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

    public int getInterviewNum() {
        return InterviewNum;
    }

    public void setInterviewNum(int InterviewNum) {
        this.InterviewNum = InterviewNum;
    }

    public int getThroughNum() {
        return ThroughNum;
    }

    public void setThroughNum(int ThroughNum) {
        this.ThroughNum = ThroughNum;
    }

    public int getNotThroughNum() {
        return NotThroughNum;
    }

    public void setNotThroughNum(int NotThroughNum) {
        this.NotThroughNum = NotThroughNum;
    }
}
