package com.youli.zbetuch_huangpu.entity;

/**
 * Created by liutao on 2018/1/8.
 * <p>
 * 个人信息里面的劳动经历
 */

public class LdjlInfo {

    private String DWMC; //工作单位名称
    private String DWXZ; //单位性质
    private String QSDATE;//起始日期
    private String ZZDATE;//终止日期
    private String JYLX;//就业类型
    private String YGXS;//用工形式
    private String YGYY;//退工原因
    private String BZ;//备注
    private String JYDJDATE; //就业登记日期
    private String TGDJDATE;//退工登记日期
    private String JYDJSZD;//就业登记所在地
    private String TGDJSZD;//退工登记所在地


    public LdjlInfo(String DWMC, String DWXZ, String QSDATE, String ZZDATE, String JYLX, String YGXS, String YGYY, String BZ, String JYDJDATE, String TGDJDATE, String JYDJSZD, String TGDJSZD) {
        this.DWMC = DWMC;
        this.DWXZ = DWXZ;
        this.QSDATE = QSDATE;
        this.ZZDATE = ZZDATE;
        this.JYLX = JYLX;
        this.YGXS = YGXS;
        this.YGYY = YGYY;
        this.BZ = BZ;
        this.JYDJDATE = JYDJDATE;
        this.TGDJDATE = TGDJDATE;
        this.JYDJSZD = JYDJSZD;
        this.TGDJSZD = TGDJSZD;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWXZ() {
        return DWXZ;
    }

    public void setDWXZ(String DWXZ) {
        this.DWXZ = DWXZ;
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

    public String getJYLX() {
        return JYLX;
    }

    public void setJYLX(String JYLX) {
        this.JYLX = JYLX;
    }

    public String getYGXS() {
        return YGXS;
    }

    public void setYGXS(String YGXS) {
        this.YGXS = YGXS;
    }

    public String getYGYY() {
        return YGYY;
    }

    public void setYGYY(String YGYY) {
        this.YGYY = YGYY;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getJYDJDATE() {
        return JYDJDATE;
    }

    public void setJYDJDATE(String JYDJDATE) {
        this.JYDJDATE = JYDJDATE;
    }

    public String getTGDJDATE() {
        return TGDJDATE;
    }

    public void setTGDJDATE(String TGDJDATE) {
        this.TGDJDATE = TGDJDATE;
    }

    public String getJYDJSZD() {
        return JYDJSZD;
    }

    public void setJYDJSZD(String JYDJSZD) {
        this.JYDJSZD = JYDJSZD;
    }

    public String getTGDJSZD() {
        return TGDJSZD;
    }

    public void setTGDJSZD(String TGDJSZD) {
        this.TGDJSZD = TGDJSZD;
    }
}
