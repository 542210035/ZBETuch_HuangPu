package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2017/12/1.
 *
 * [{"ID":4,"TITLE":"人情味若群若翁群若","DOC":"沃尔沃群若群若群无","CREATE_TIME":"2017-12-08T10:33:15.91","CREATE_STAFF":1,"RecordCount":4,"NewImg":"new"}]
 */

public class NoticeInfo implements Serializable {

    /**
     * ID : 2
     * TITLE : 十二届全国人大常委会第三十次会议在京闭幕 张德江主持会议
     * DOC :
     * CREATE_TIME : 2017-11-05T18:36:30.197
     * CREATE_STAFF : 1
     * RecordCount : 2
     */

    private int ID;
    private String TITLE;
    private String DOC;
    private String CREATE_TIME;
    private int CREATE_STAFF;
    private int RecordCount;
    private String NewImg;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDOC() {
        return DOC;
    }

    public void setDOC(String DOC) {
        this.DOC = DOC;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public int getCREATE_STAFF() {
        return CREATE_STAFF;
    }

    public void setCREATE_STAFF(int CREATE_STAFF) {
        this.CREATE_STAFF = CREATE_STAFF;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public String getNewImg() {
        return NewImg;
    }

    public void setNewImg(String newImg) {
        NewImg = newImg;
    }

    @Override
    public String toString() {
        return DOC;
    }
}
