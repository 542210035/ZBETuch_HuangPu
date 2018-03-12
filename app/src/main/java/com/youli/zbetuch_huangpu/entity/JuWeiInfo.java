package com.youli.zbetuch_huangpu.entity;

import java.io.Serializable;

/**
 * Created by sfhan on 2018/3/9.
 */

public class JuWeiInfo implements Serializable {

//http://192.168.191.3:8088/Json/GetResourceSurvey_JW_Info.aspx?TYPE=无业&dcId=28   //全部居委
    /**
     * hjjwmc : 全部
     */

    private String hjjwmc;

    public String getHjjwmc() {
        return hjjwmc;
    }

    public void setHjjwmc(String hjjwmc) {
        this.hjjwmc = hjjwmc;
    }

    @Override
    public String toString() {
        return hjjwmc
                ;
    }
}
