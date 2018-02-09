package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2017/11/8.
 */
//http://web.youli.pw:8088/Json/Get_Resource_Survey_Detil_WY.aspx?page=0&rows=15&dcId=2 &type=1
    //[{"MDID":1,"DCID":2,"ZJLX":"身份证","ZJHM":"210403197906080927","XM":"王丹","GENDER":"女","MINZU":"汉族","CSDATE":"19790608",
// "WHCD":"大学本科(简称大学)","HKDZ":"厦门路115号507室","LXDH":"18930607783","MQZK":"有劳动收入","MDDATE":"20170817","DQYX":"就业条件较好","SFCJR":"否","
// SFYH":"否","JDMC":"南京东路街道","JWMC":"贵州居委","SFWCDC":false,"DCDATE":null,"DCRID":-1,"DCRXM":"","MQZK_NEW":"","DQYX_NEW":"","DCBZ":"","RecordCount":328}]

    //[{"MDID":1,"DCID":2,"ZJLX":"身份证","ZJHM":"210403197906080927","XM":"王丹","GENDER":"女","MINZU":"汉族","CSDATE":"19790608",
// "WHCD":"大学本科(简称大学)","HKDZ":"厦门路115号507室","LXDH":"18930607783","MQZK":"有劳动收入","MDDATE":"20170817","DQYX":"就业条件较好","SFCJR":"否",
// "SFYH":"否","HJJDMC":"南京东路街道","HJJWMC":"贵州居委","SFWCDC":true,"DCDATE":"2017-11-14T16:33:27.593","DCRID":1,"DCRXM":"admin","MQZK_NEW":"有劳动收入","DQYX_NEW":"就业条件较好","DCBZ":"","DCLX":null,"RHFL":null,"RecordCount":328}]

public class ResourcesDetailInfo implements Serializable {

    private String JJ_DATE; //截止日期

    private int MDID;//名单ID(失业，无业，应届生)

    private int DCID;//调查ID(失业，无业，应届生)

    private String ZJLX;//证件类型(失业，无业，应届生)

    private String ZJHM;//要显示的 证件号码(失业，无业，应届生)

    private String XM;//要显示的 姓名(失业，无业，应届生)

    private String GENDER;//性别(失业，无业，应届生)

    private String MINZU;//民族(失业，无业，应届生)

    private String CSDATE;//要显示的 出生日期(失业，无业，应届生)

    private String WHCD;//文化程度(失业，无业，应届生)

    private String HJLB;//户籍类别(失业)

    private String HKDZ;//户口地址(失业，无业，应届生)

    private String LXDH;//联系电话(失业，无业，应届生)

    private String MQZK;//目前状况(失业，无业，应届生)

    private String MDDATE;//摸底日期(失业，无业)

    private String DQYX;//当前意向(失业，无业，应届生)

    private String JYZT;//就业状态(应届生)

    private String XXQSDATE;//学习起始日期(应届生)

    private String XXZZDATE;//学习终止日期(应届生)

    private String XXMC;//学校名称(应届生)

    private String SXZY;//所学专业(应届生)

    private String SFBYY;//是否毕肄业(应届生)

    private String SFQRZ;//是否全日制(应届生)

    private String ZJSYDJDATE;//最近失业登记日期(失业)

    private String SYDJYXQ;//失业登记有效期(失业)

    private String SFCJR;//是否残疾人(失业，无业)

    private String SFYH;//是否已核(无业)

    private String HJJDMC;//街道名称(失业，无业，应届生)

    private String HJJWMC;//居委名称(失业，无业，应届生)

    public String getHJJWMC() {
        return HJJWMC;
    }

    public void setHJJWMC(String HJJWMC) {
        this.HJJWMC = HJJWMC;
    }

    private boolean SFWCDC;//是否已完成调查(0=否;1=是)(失业，无业，应届生)

    private String DCDATE;//调查日期(失业，无业，应届生)

    private int DCRID;//调查人ID(失业，无业，应届生)

    private String DCRXM;//调查人姓名(失业，无业，应届生)

    private String MQZK_NEW;//调查的目前状况(失业，无业，应届生)

    private String DQYX_NEW;//调查的当前意向(失业，无业，应届生)

    private String DCBZ;//调查备注(失业，无业，应届生)

    private boolean RHFL;    //人户分离

    private String DCLX;   //调查类型

    private int RecordCount;

    public String getJJ_DATE() {
        return JJ_DATE;
    }

    public void setJJ_DATE(String JJ_DATE) {
        this.JJ_DATE = JJ_DATE;
    }
    public boolean isRHFL() {
        return RHFL;
    }

    public void setRHFL(boolean RHFL) {
        this.RHFL = RHFL;
    }

    public String getDCLX() {
        return DCLX;
    }

    public void setDCLX(String DCLX) {
        this.DCLX = DCLX;
    }



    public String getCSDATE() {
        return CSDATE;
    }

    public void setCSDATE(String CSDATE) {
        this.CSDATE = CSDATE;
    }

    public String getDCBZ() {
        return DCBZ;
    }

    public void setDCBZ(String DCBZ) {
        this.DCBZ = DCBZ;
    }

    public String getDCDATE() {
        return DCDATE;
    }

    public void setDCDATE(String DCDATE) {
        this.DCDATE = DCDATE;
    }

    public int getDCID() {
        return DCID;
    }

    public void setDCID(int DCID) {
        this.DCID = DCID;
    }

    public int getDCRID() {
        return DCRID;
    }

    public void setDCRID(int DCRID) {
        this.DCRID = DCRID;
    }

    public String getDCRXM() {
        return DCRXM;
    }

    public void setDCRXM(String DCRXM) {
        this.DCRXM = DCRXM;
    }

    public String getDQYX() {
        return DQYX;
    }

    public void setDQYX(String DQYX) {
        this.DQYX = DQYX;
    }

    public String getDQYX_NEW() {
        return DQYX_NEW;
    }

    public void setDQYX_NEW(String DQYX_NEW) {
        this.DQYX_NEW = DQYX_NEW;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getHJLB() {
        return HJLB;
    }

    public void setHJLB(String HJLB) {
        this.HJLB = HJLB;
    }

    public String getHKDZ() {
        return HKDZ;
    }

    public void setHKDZ(String HKDZ) {
        this.HKDZ = HKDZ;
    }

    public String getHJJDMC() {
        return HJJDMC;
    }

    public void setHJJDMC(String HJJDMC) {
        this.HJJDMC = HJJDMC;
    }

    public String getJYZT() {
        return JYZT;
    }

    public void setJYZT(String JYZT) {
        this.JYZT = JYZT;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getMDDATE() {
        return MDDATE;
    }

    public void setMDDATE(String MDDATE) {
        this.MDDATE = MDDATE;
    }

    public int getMDID() {
        return MDID;
    }

    public void setMDID(int MDID) {
        this.MDID = MDID;
    }

    public String getMINZU() {
        return MINZU;
    }

    public void setMINZU(String MINZU) {
        this.MINZU = MINZU;
    }

    public String getMQZK() {
        return MQZK;
    }

    public void setMQZK(String MQZK) {
        this.MQZK = MQZK;
    }

    public String getMQZK_NEW() {
        return MQZK_NEW;
    }

    public void setMQZK_NEW(String MQZK_NEW) {
        this.MQZK_NEW = MQZK_NEW;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public String getSFBYY() {
        return SFBYY;
    }

    public void setSFBYY(String SFBYY) {
        this.SFBYY = SFBYY;
    }

    public String getSFCJR() {
        return SFCJR;
    }

    public void setSFCJR(String SFCJR) {
        this.SFCJR = SFCJR;
    }

    public String getSFQRZ() {
        return SFQRZ;
    }

    public void setSFQRZ(String SFQRZ) {
        this.SFQRZ = SFQRZ;
    }

    public boolean isSFWCDC() {
        return SFWCDC;
    }

    public void setSFWCDC(boolean SFWCDC) {
        this.SFWCDC = SFWCDC;
    }

    public String getSFYH() {
        return SFYH;
    }

    public void setSFYH(String SFYH) {
        this.SFYH = SFYH;
    }

    public String getSXZY() {
        return SXZY;
    }

    public void setSXZY(String SXZY) {
        this.SXZY = SXZY;
    }

    public String getSYDJYXQ() {
        return SYDJYXQ;
    }

    public void setSYDJYXQ(String SYDJYXQ) {
        this.SYDJYXQ = SYDJYXQ;
    }

    public String getWHCD() {
        return WHCD;
    }

    public void setWHCD(String WHCD) {
        this.WHCD = WHCD;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getXXMC() {
        return XXMC;
    }

    public void setXXMC(String XXMC) {
        this.XXMC = XXMC;
    }

    public String getXXQSDATE() {
        return XXQSDATE;
    }

    public void setXXQSDATE(String XXQSDATE) {
        this.XXQSDATE = XXQSDATE;
    }

    public String getXXZZDATE() {
        return XXZZDATE;
    }

    public void setXXZZDATE(String XXZZDATE) {
        this.XXZZDATE = XXZZDATE;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getZJLX() {
        return ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJSYDJDATE() {
        return ZJSYDJDATE;
    }

    public void setZJSYDJDATE(String ZJSYDJDATE) {
        this.ZJSYDJDATE = ZJSYDJDATE;
    }
}

