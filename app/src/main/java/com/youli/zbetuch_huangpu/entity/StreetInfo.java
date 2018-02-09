package com.youli.zbetuch_huangpu.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfhan on 2017/11/7.
 */

public class StreetInfo {
    private String streetId;
    private String streetName;

    public StreetInfo() {

    }

    public StreetInfo(String streetId, String streetName) {
        this.streetId = streetId;
        this.streetName = streetName;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public List<StreetInfo> getSteetList(){

        List<StreetInfo> streetList=new ArrayList<>();

        streetList.add(new StreetInfo("0", "全部"));
        streetList.add(new StreetInfo("8001", "南京东路街道"));
        streetList.add(new StreetInfo("8006", "外滩街道"));
        streetList.add(new StreetInfo("8007", "半淞园街道"));
        streetList.add(new StreetInfo("8012", "小东门街道"));
        streetList.add(new StreetInfo("8013", "老西门街道"));
        streetList.add(new StreetInfo("8014", "豫园街道"));
        streetList.add(new StreetInfo("8015", "五里桥街道"));
        streetList.add(new StreetInfo("8016", "淮海中路街道"));
        streetList.add(new StreetInfo("8101", "打浦街道"));
        streetList.add(new StreetInfo("8107", "瑞金二路街道"));
        streetList.add(new StreetInfo("8108", "街道不明"));
        streetList.add(new StreetInfo("8109", "街道为空"));

        return streetList;
    }
    @Override
    public String toString() {
        return streetName;
    }


}
