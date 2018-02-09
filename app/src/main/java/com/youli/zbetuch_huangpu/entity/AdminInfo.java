package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * 作者: zhengbin on 2017/11/8.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * http://web.youli.pw:8088/Json/Get_Staff.aspx
 *
 *
 * {"ID":1,"NAME":"admin","INPUT_CODE":"admin","PWD":"T3RFK3hyUWhnV2M9","PHONE":"13811111111","EMAIL":"",
 * "PHOTO":null,"CREATE_DATE":"2017-01-01T00:00:00","CREATE_STAFF":1,"UPDATE_DATE":"2017-10-25T11:02:58.333",
 * "UPDATE_STAFF":1,"STOP":false,"DEVICE_NUMBER":"132131","SFZ":"4tte","DEPT":"terterte","JD":"黄浦区",
 * "IMEI":"004401729764676","RecordCount":0,"Enable":true,"Line_name":null}
 */

public class AdminInfo implements Serializable{


    private int ID;
    private String NAME;//姓名
    private String INPUT_CODE;//用户名
    private String PWD;//密码
    private String PHONE;//联系电话
    private String EMAIL;//邮件地址
    private Object PHOTO;//照片
    private String CREATE_DATE;
    private int CREATE_STAFF;
    private String UPDATE_DATE;
    private int UPDATE_STAFF;
    private boolean STOP;//是否停用
    private String DEVICE_NUMBER;//设备电话
    private String SFZ;//身份证
    private String DEPT;//部门
    private String JD;//所属街道
    private String IMEI;//IMEI编码
    private int RecordCount;
    private boolean Enable;
    private Object Line_name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getINPUT_CODE() {
        return INPUT_CODE;
    }

    public void setINPUT_CODE(String INPUT_CODE) {
        this.INPUT_CODE = INPUT_CODE;
    }

    public String getPWD() {
        return PWD;
    }

    public void setPWD(String PWD) {
        this.PWD = PWD;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public Object getPHOTO() {
        return PHOTO;
    }

    public void setPHOTO(Object PHOTO) {
        this.PHOTO = PHOTO;
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

    public boolean isSTOP() {
        return STOP;
    }

    public void setSTOP(boolean STOP) {
        this.STOP = STOP;
    }

    public String getDEVICE_NUMBER() {
        return DEVICE_NUMBER;
    }

    public void setDEVICE_NUMBER(String DEVICE_NUMBER) {
        this.DEVICE_NUMBER = DEVICE_NUMBER;
    }

    public String getSFZ() {
        return SFZ;
    }

    public void setSFZ(String SFZ) {
        this.SFZ = SFZ;
    }

    public String getDEPT() {
        return DEPT;
    }

    public void setDEPT(String DEPT) {
        this.DEPT = DEPT;
    }

    public String getJD() {
        return JD;
    }

    public void setJD(String JD) {
        this.JD = JD;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int RecordCount) {
        this.RecordCount = RecordCount;
    }

    public boolean isEnable() {
        return Enable;
    }

    public void setEnable(boolean Enable) {
        this.Enable = Enable;
    }

    public Object getLine_name() {
        return Line_name;
    }

    public void setLine_name(Object Line_name) {
        this.Line_name = Line_name;
    }
}
