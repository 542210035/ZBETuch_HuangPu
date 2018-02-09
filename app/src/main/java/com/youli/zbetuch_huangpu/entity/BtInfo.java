package com.youli.zbetuch_huangpu.entity;

/**
 * Created by liutao on 2018/1/5.
 *
 * 补贴信息
 */

public class BtInfo {

    private String btzfny;//支付年月
    private String btxmmc;//补贴项目名称
    private String btje;//补贴金额
    private String btxz;//补贴性质

    public BtInfo(String btzfny, String btxmmc, String btje, String btxz) {
        this.btzfny = btzfny;
        this.btxmmc = btxmmc;
        this.btje = btje;
        this.btxz = btxz;
    }

    public String getBtzfny() {
        return btzfny;
    }

    public void setBtzfny(String btzfny) {
        this.btzfny = btzfny;
    }

    public String getBtxmmc() {
        return btxmmc;
    }

    public void setBtxmmc(String btxmmc) {
        this.btxmmc = btxmmc;
    }

    public String getBtje() {
        return btje;
    }

    public void setBtje(String btje) {
        this.btje = btje;
    }

    public String getBtxz() {
        return btxz;
    }

    public void setBtxz(String btxz) {
        this.btxz = btxz;
    }
}
