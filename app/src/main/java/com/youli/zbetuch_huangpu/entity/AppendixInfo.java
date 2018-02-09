package com.youli.zbetuch_huangpu.entity;

import android.widget.TextView;

/**
 * 作者: zhengbin on 2017/12/11.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 *
 * http://web.youli.pw:8088/Json/First/Get_Meeting_File.aspx?MASTER_ID=1
 * [{"ID":1,"FILE_PATH":"FileUpLoad/Manage/5aca6923-f27f-4c54-8d47-38efc3993dbd_timg.jpg","FILE_NAME":"timg.jpg","MASTER_ID":1,"RecordCount":0,"type":0}]
 */

public class AppendixInfo {

    private int ID;

    private String FILE_PATH;

    private String FILE_NAME;

    private int MASTER_ID;

    private int RecordCount;

    private int type;

    public AppendixInfo(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }

    public String getFILE_NAME() {
        return FILE_NAME;
    }

    public void setFILE_NAME(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }

    public String getFILE_PATH() {
        return FILE_PATH;
    }

    public void setFILE_PATH(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getMASTER_ID() {
        return MASTER_ID;
    }

    public void setMASTER_ID(int MASTER_ID) {
        this.MASTER_ID = MASTER_ID;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
