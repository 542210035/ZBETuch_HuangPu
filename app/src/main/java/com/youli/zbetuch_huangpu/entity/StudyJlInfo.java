package com.youli.zbetuch_huangpu.entity;

/**
 * Created by liutao on 2018/1/7.
 *
 * 学习经历
 */

public class StudyJlInfo {

    private String QSDATE;//起始日期
    private String ZZDATE;//终止日期
    private String XXMC;//学校名称
    private String WHCD;//文化程度
    private String SXZY;//所学专业
    private String SFBYY;//毕业肄业
    private String SFQRZ;//全日制
    private String BZ;//备注

    public StudyJlInfo(String QSDATE, String ZZDATE, String XXMC, String WHCD, String SXZY, String SFBYY, String SFQRZ, String BZ) {
        this.QSDATE = QSDATE;
        this.ZZDATE = ZZDATE;
        this.XXMC = XXMC;
        this.WHCD = WHCD;
        this.SXZY = SXZY;
        this.SFBYY = SFBYY;
        this.SFQRZ = SFQRZ;
        this.BZ= BZ;
    }

    public String getQSDATE() {
        return QSDATE;
    }

    public void setQSDATE(String QSDATE) {
        this.QSDATE = QSDATE;
    }

    public String getZZDATE() {
        return ZZDATE;
    }

    public void setZZDATE(String ZZDATE) {
        this.ZZDATE = ZZDATE;
    }

    public String getXXMC() {
        return XXMC;
    }

    public void setXXMC(String XXMC) {
        this.XXMC = XXMC;
    }

    public String getWHCD() {
        return WHCD;
    }

    public void setWHCD(String WHCD) {
        this.WHCD = WHCD;
    }

    public String getSXZY() {
        return SXZY;
    }

    public void setSXZY(String SXZY) {
        this.SXZY = SXZY;
    }

    public String getSFBYY() {
        return SFBYY;
    }

    public void setSFBYY(String SFBYY) {
        this.SFBYY = SFBYY;
    }

    public String getSFQRZ() {
        return SFQRZ;
    }

    public void setSFQRZ(String SFQRZ) {
        this.SFQRZ = SFQRZ;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }
}
