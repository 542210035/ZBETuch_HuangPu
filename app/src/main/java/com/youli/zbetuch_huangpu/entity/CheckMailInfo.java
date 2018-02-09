package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sfhan on 2018/2/1.
 */

public class CheckMailInfo implements Serializable{


    /**
     * ID : 2
     * TITLE : 55555555555555555555555555
     * DOC :  ------------------ 原始邮件 ------------------  发件人: <admin>;  发送时间: 2017年12月27日(星期三)15:20:25  主题: 游戏加速度飞试试是发送   	水电费离开水电费圣诞法律手段发斯蒂芬撒旦法拉水电费纳斯达克没发少df sad发送adfsd飞sadfsdfsdfasdfasdf水电费水电费   水电费撒地方水电费说的    啊水电费撒地方
     * SEND_USER : admin
     * STAFF_ID : 1
     * SEND_TIME : 2017-12-27T15:26:21.27
     * HIDDEN : false
     * DEL : false
     * RecordCount : 0
     * filelist : [{"ID":2,"PATH":"upload/636499848237288090_宝华更新.rar","FILE_NAME":"宝华更新.rar","EMAIL_ID":2,"RecordCount":0}]
     * SENDER : null
     */

    private int ID;
    private String TITLE;
    private String DOC;
    private String SEND_USER;
    private int STAFF_ID;
    private String SEND_TIME;
    private boolean HIDDEN;
    private boolean DEL;
    private int RecordCount;
    private Object SENDER;
    private List<FilelistBean> filelist;

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

    public String getSEND_USER() {
        return SEND_USER;
    }

    public void setSEND_USER(String SEND_USER) {
        this.SEND_USER = SEND_USER;
    }

    public int getSTAFF_ID() {
        return STAFF_ID;
    }

    public void setSTAFF_ID(int STAFF_ID) {
        this.STAFF_ID = STAFF_ID;
    }

    public String getSEND_TIME() {
        return SEND_TIME;
    }

    public void setSEND_TIME(String SEND_TIME) {
        this.SEND_TIME = SEND_TIME;
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

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public Object getSENDER() {
        return SENDER;
    }

    public void setSENDER(Object SENDER) {
        this.SENDER = SENDER;
    }

    public List<FilelistBean> getFilelist() {
        return filelist;
    }

    public void setFilelist(List<FilelistBean> filelist) {
        this.filelist = filelist;
    }

    public static class FilelistBean {
        /**
         * ID : 2
         * PATH : upload/636499848237288090_宝华更新.rar
         * FILE_NAME : 宝华更新.rar
         * EMAIL_ID : 2
         * RecordCount : 0
         */

        private int ID;
        private String PATH;
        private String FILE_NAME;
        private int EMAIL_ID;
        private int RecordCount;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getPATH() {
            return PATH;
        }

        public void setPATH(String PATH) {
            this.PATH = PATH;
        }

        public String getFILE_NAME() {
            return FILE_NAME;
        }

        public void setFILE_NAME(String FILE_NAME) {
            this.FILE_NAME = FILE_NAME;
        }

        public int getEMAIL_ID() {
            return EMAIL_ID;
        }

        public void setEMAIL_ID(int EMAIL_ID) {
            this.EMAIL_ID = EMAIL_ID;
        }

        public int getRecordCount() {
            return RecordCount;
        }

        public void setRecordCount(int RecordCount) {
            this.RecordCount = RecordCount;
        }
    }
}
