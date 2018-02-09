package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2017/11/7.
 *
 * http://web.youli.pw:8088/Json/GetResourceSurvey.aspx?page=0&rows=15&TYPE=
 *
 * [{"dcid":3,"dclx":"应届生","bzdate":"2017-11-02T00:00:00","dccjrid":1,"dccjrxm":"admin","dccjsj":"2017-11-02T12:19:25.427",
 * "dcjdmc":"黄浦区","dcmdrs":28,"dcwcrs":0,"bz":"","rows":3}]
 *
 */

public class ResourcesInfo implements Serializable {

    private int dcid;//要显示的       本次调查ID
    private String dclx;//要显示的    本次调查类型(失业、无业、应届生)
    private String dccjrxm;//要显示的  本次调查创建人姓名
    private String bzdate;//要显示的   本次调查布置日期
    private int dcmdrs;//要显示的   本次调查名单人数
    private int dcwcrs;//要显示的   本次已完成调查人数

    private int dccjrid; //本次调查创建人ID
    private String dccjsj;//本次调查创建时间
    private String dcjdmc;//本次调查街道名称
    private String bz;//备注
    private int rows;//总记录数

    public ResourcesInfo(int dcid, String dclx, String dccjrxm,String bzdate,int dcmdrs,int dcwcrs) {
        this.dcid=dcid;
        this.dclx=dclx;
        this.dccjrxm=dccjrxm;
        this.bzdate=bzdate;
        this.dcmdrs=dcmdrs;
        this.dcwcrs=dcwcrs;
    }


    public int getDcid() {
        return dcid;
    }

    public void setDcid(int dcid) {
        this.dcid = dcid;
    }

    public String getDclx() {
        return dclx;
    }

    public void setDclx(String dclx) {
        this.dclx = dclx;
    }

    public String getDccjrxm() {
        return dccjrxm;
    }

    public void setDccjrxm(String dccjrxm) {
        this.dccjrxm = dccjrxm;
    }

    public String getBzdate() {
        return bzdate;
    }

    public void setBzdate(String bzdate) {
        this.bzdate = bzdate;
    }

    public int getDcmdrs() {
        return dcmdrs;
    }

    public void setDcmdrs(int dcmdrs) {
        this.dcmdrs = dcmdrs;
    }

    public int getDcwcrs() {
        return dcwcrs;
    }

    public void setDcwcrs(int dcwcrs) {
        this.dcwcrs = dcwcrs;
    }

    public int getDccjrid() {
        return dccjrid;
    }

    public void setDccjrid(int dccjrid) {
        this.dccjrid = dccjrid;
    }

    public String getDccjsj() {
        return dccjsj;
    }

    public void setDccjsj(String dccjsj) {
        this.dccjsj = dccjsj;
    }

    public String getDcjdmc() {
        return dcjdmc;
    }

    public void setDcjdmc(String dcjdmc) {
        this.dcjdmc = dcjdmc;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }



    @Override
    public String toString() {
        return "ResourcesInfo{" + "dcid='" + dcid + '\''
                + ", dclx=" + dclx + "," +
                " dccjrxm='" + dccjrxm + '\'' +
                ", bzdate='" + bzdate + '\'' +
                ", dcmdrs=" + dcmdrs + ", " +
                "dcwcrs=" + dcwcrs +
                ", dccjrid=" + dccjrid + "," +
                " dccjsj=" + dccjsj + "," +
                " dcjdmc='" + dcjdmc + '\'' +
                "bz="+bz+",rows="+rows+"}";
    }
}

