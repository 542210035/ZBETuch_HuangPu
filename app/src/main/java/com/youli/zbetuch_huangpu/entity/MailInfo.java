package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/2/1.
 */

public class MailInfo implements Serializable{


    /**
     * ID : 23
     * RECEIVE_STAFF : 1
     * RECEIVE_STAFF_NAME : admin
     * READED : true
     * READTIME : 2018-01-31T16:36:50.833
     * HIDDEN : false
     * DEL : false
     * TITLE : 兰克福的是莱克斯顿付了款看技
     * EMAIL_ID : 11
     * SENDER : admin
     * SENDTIME : 2017-12-28T16:49:52.513
     * RecordCount : 10
     * READED_state : 已读
     */

    private int ID;
    private int RECEIVE_STAFF;
    private String RECEIVE_STAFF_NAME;
    private boolean READED;
    private String READTIME;
    private boolean HIDDEN;
    private boolean DEL;
    private String TITLE;
    private int EMAIL_ID;
    private String SENDER;
    private String SENDTIME;
    private int RecordCount;
    private String READED_state;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRECEIVE_STAFF() {
        return RECEIVE_STAFF;
    }

    public void setRECEIVE_STAFF(int RECEIVE_STAFF) {
        this.RECEIVE_STAFF = RECEIVE_STAFF;
    }

    public String getRECEIVE_STAFF_NAME() {
        return RECEIVE_STAFF_NAME;
    }

    public void setRECEIVE_STAFF_NAME(String RECEIVE_STAFF_NAME) {
        this.RECEIVE_STAFF_NAME = RECEIVE_STAFF_NAME;
    }

    public boolean isREADED() {
        return READED;
    }

    public void setREADED(boolean READED) {
        this.READED = READED;
    }

    public String getREADTIME() {
        return READTIME;
    }

    public void setREADTIME(String READTIME) {
        this.READTIME = READTIME;
    }

    public boolean isHIDDEN() {
        return HIDDEN;
    }

    public void setHIDDEN(boolean HIDDEN) {
        this.HIDDEN = HIDDEN;
    }

    public boolean isDEL() {
        return DEL;
    }

    public void setDEL(boolean DEL) {
        this.DEL = DEL;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(int EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getSENDER() {
        return SENDER;
    }

    public void setSENDER(String SENDER) {
        this.SENDER = SENDER;
    }

    public String getSENDTIME() {
        return SENDTIME;
    }

    public void setSENDTIME(String SENDTIME) {
        this.SENDTIME = SENDTIME;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public String getREADED_state() {
        return READED_state;
    }

    public void setREADED_state(String READED_state) {
        this.READED_state = READED_state;
    }
}
