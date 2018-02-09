package com.youli.zbetuch_huangpu.entity;

import java.util.List;

/**
 * Created by sfhan on 2018/1/11.
 */

//http://web.youli.pw:8088/JSON/Get_HJDZ.aspx?sfz=000000198401070007

public class HouseholdInfo {


//    private String title;
//
//    private boolean isChecked;
//
//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setChecked(boolean checked) {
//        isChecked = checked;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//
//    private List<FamilyAddressInfoList> list;
//
//    public List<FamilyAddressInfoList> getList() {
//        return list;
//    }
//
//    public void setList(List<FamilyAddressInfoList> list) {
//        this.list = list;
//    }
//
//    public HouseholdInfo(boolean isChecked, String title, List<FamilyAddressInfoList> list) {
//        this.isChecked = isChecked;
//        this.title = title;
//        this.list = list;
//    }

    /**
     * ZJHM : 31010119840107108X* XM : 尤海晴* GRPID : 200207114864280* CSDATE : 19840107
     * GENDER : 女* MINZU : 汉族* ZZMM : 群众* WHCD : 大学专科和专科学校* HJLB : 非农业户口* HJQX : 黄浦
     * HJJDMC : 南京东路街道* HJJWMC : 新桥居委* HJDZ : 成都北路962弄47号* LXDZ : 成都北路962弄47号* LXDH : 56415694* JYZT : 就业
     * * JYLX : 单位就业* JYDWXTCID : 200301070654721* JYDWZZJGDM : 744204487
     * JYDWSHXYDM :* JYDWMC : 上海安迪人力资源服务有限公司* JYDWLX : 有限责任* ZYGZ : 办事人员和有关人员* JYDJDATE : 2015110
     * * TGDJDATE :* LDJLJYKSDATE : 20151026* LDJLJYZZDATE :* ISSHIYE :
     * SYDJDATE * SYDJDATE_LAST : * ISDC : 1* DCDATE_LAST : 2015061* DCMQZK_LAST : 有劳动收入
     * DCDQYX_LAST : 就业条件一般* DANGANSZD : 上海市黄浦区* GRSHBXJFXZ : 城* GRSHBXJFZT : 缴费* JFDWZZJGDM : 744204487
     * * JFDWXTCID : 200301070654721* JFDWSHBXDJM : 00107137* JFDWMC : 上海安迪人力资源服务有限公* DCID_LAST : -1* DCLX_LAST :* HJDZ_ROAD : 成都北路* HJDZ_LANE : 962* HJDZ_NUMBER : 47号* HJDZ_ROOMNO :* LXDZ_ROAD : 成都北路* LXDZ_LANE : 962弄* LXDZ_NUMBER : 47号
     * LXDZ_ROOMNO :* SFCJR :* JLCJSJ : 2017-11-02T10:05:25.333* JLGXSJ : 2017-11-02T10:05:25.333* ISVALID : tru* RecordCount : 0
     * * GetPhotoUrl : ../../img/ooopic_1369286910.png* CSDATE1 : 1984-01-07T00:00:00
     */
//    public static class FamilyAddressInfoList {
        private String ZJHM;
        private String XM;
        private String GRPID;
        private String CSDATE;
        private String GENDER;
        private String MINZU;
        private String ZZMM;
        private String WHCD;
        private String HJLB;
        private String HJQX;
        private String HJJDMC;
        private String HJJWMC;
        private String HJDZ;
        private String LXDZ;
        private String LXDH;
        private String JYZT;
        private String JYLX;
        private String JYDWXTCID;
        private String JYDWZZJGDM;
        private String JYDWSHXYDM;
        private String JYDWMC;
        private String JYDWLX;
        private String ZYGZ;
        private String JYDJDATE;
        private String TGDJDATE;
        private String LDJLJYKSDATE;
        private String LDJLJYZZDATE;
        private String ISSHIYE;
        private String SYDJDATE;
        private String SYDJDATE_LAST;
        private String ISJYKN;
        private String JYKNRDDATE;
        private String JYKNLB;
        private String ISDC;
        private String DCDATE_LAST;
        private String DCMQZK_LAST;
        private String DCDQYX_LAST;
        private String DANGANSZD;
        private String GRSHBXJFXZ;
        private String GRSHBXJFZT;
        private String JFDWZZJGDM;
        private String JFDWXTCID;
        private String JFDWSHBXDJM;
        private String JFDWMC;
        private int DCID_LAST;
        private String DCLX_LAST;
        private String HJDZ_ROAD;
        private String HJDZ_LANE;
        private String HJDZ_NUMBER;
        private String HJDZ_ROOMNO;
        private String LXDZ_ROAD;
        private String LXDZ_LANE;
        private String LXDZ_NUMBER;
        private String LXDZ_ROOMNO;
        private String SFCJR;
        private String JLCJSJ;
        private String JLGXSJ;
        private boolean ISVALID;
        private int RecordCount;
        private String GetPhotoUrl;
        private String CSDATE1;

        public String getZJHM() {
            return ZJHM;
        }

        public void setZJHM(String ZJHM) {
            this.ZJHM = ZJHM;
        }

        public String getXM() {
            return XM;
        }

        public void setXM(String XM) {
            this.XM = XM;
        }

        public String getGRPID() {
            return GRPID;
        }

        public void setGRPID(String GRPID) {
            this.GRPID = GRPID;
        }

        public String getCSDATE() {
            return CSDATE;
        }

        public void setCSDATE(String CSDATE) {
            this.CSDATE = CSDATE;
        }

        public String getGENDER() {
            return GENDER;
        }

        public void setGENDER(String GENDER) {
            this.GENDER = GENDER;
        }

        public String getMINZU() {
            return MINZU;
        }

        public void setMINZU(String MINZU) {
            this.MINZU = MINZU;
        }

        public String getZZMM() {
            return ZZMM;
        }

        public void setZZMM(String ZZMM) {
            this.ZZMM = ZZMM;
        }

        public String getWHCD() {
            return WHCD;
        }

        public void setWHCD(String WHCD) {
            this.WHCD = WHCD;
        }

        public String getHJLB() {
            return HJLB;
        }

        public void setHJLB(String HJLB) {
            this.HJLB = HJLB;
        }

        public String getHJQX() {
            return HJQX;
        }

        public void setHJQX(String HJQX) {
            this.HJQX = HJQX;
        }

        public String getHJJDMC() {
            return HJJDMC;
        }

        public void setHJJDMC(String HJJDMC) {
            this.HJJDMC = HJJDMC;
        }

        public String getHJJWMC() {
            return HJJWMC;
        }

        public void setHJJWMC(String HJJWMC) {
            this.HJJWMC = HJJWMC;
        }

        public String getHJDZ() {
            return HJDZ;
        }

        public void setHJDZ(String HJDZ) {
            this.HJDZ = HJDZ;
        }

        public String getLXDZ() {
            return LXDZ;
        }

        public void setLXDZ(String LXDZ) {
            this.LXDZ = LXDZ;
        }

        public String getLXDH() {
            return LXDH;
        }

        public void setLXDH(String LXDH) {
            this.LXDH = LXDH;
        }

        public String getJYZT() {
            return JYZT;
        }

        public void setJYZT(String JYZT) {
            this.JYZT = JYZT;
        }

        public String getJYLX() {
            return JYLX;
        }

        public void setJYLX(String JYLX) {
            this.JYLX = JYLX;
        }

        public String getJYDWXTCID() {
            return JYDWXTCID;
        }

        public void setJYDWXTCID(String JYDWXTCID) {
            this.JYDWXTCID = JYDWXTCID;
        }

        public String getJYDWZZJGDM() {
            return JYDWZZJGDM;
        }

        public void setJYDWZZJGDM(String JYDWZZJGDM) {
            this.JYDWZZJGDM = JYDWZZJGDM;
        }

        public String getJYDWSHXYDM() {
            return JYDWSHXYDM;
        }

        public void setJYDWSHXYDM(String JYDWSHXYDM) {
            this.JYDWSHXYDM = JYDWSHXYDM;
        }

        public String getJYDWMC() {
            return JYDWMC;
        }

        public void setJYDWMC(String JYDWMC) {
            this.JYDWMC = JYDWMC;
        }

        public String getJYDWLX() {
            return JYDWLX;
        }

        public void setJYDWLX(String JYDWLX) {
            this.JYDWLX = JYDWLX;
        }

        public String getZYGZ() {
            return ZYGZ;
        }

        public void setZYGZ(String ZYGZ) {
            this.ZYGZ = ZYGZ;
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

        public String getLDJLJYKSDATE() {
            return LDJLJYKSDATE;
        }

        public void setLDJLJYKSDATE(String LDJLJYKSDATE) {
            this.LDJLJYKSDATE = LDJLJYKSDATE;
        }

        public String getLDJLJYZZDATE() {
            return LDJLJYZZDATE;
        }

        public void setLDJLJYZZDATE(String LDJLJYZZDATE) {
            this.LDJLJYZZDATE = LDJLJYZZDATE;
        }

        public String getISSHIYE() {
            return ISSHIYE;
        }

        public void setISSHIYE(String ISSHIYE) {
            this.ISSHIYE = ISSHIYE;
        }

        public String getSYDJDATE() {
            return SYDJDATE;
        }

        public void setSYDJDATE(String SYDJDATE) {
            this.SYDJDATE = SYDJDATE;
        }

        public String getSYDJDATE_LAST() {
            return SYDJDATE_LAST;
        }

        public void setSYDJDATE_LAST(String SYDJDATE_LAST) {
            this.SYDJDATE_LAST = SYDJDATE_LAST;
        }

        public String getISJYKN() {
            return ISJYKN;
        }

        public void setISJYKN(String ISJYKN) {
            this.ISJYKN = ISJYKN;
        }

        public String getJYKNRDDATE() {
            return JYKNRDDATE;
        }

        public void setJYKNRDDATE(String JYKNRDDATE) {
            this.JYKNRDDATE = JYKNRDDATE;
        }

        public String getJYKNLB() {
            return JYKNLB;
        }

        public void setJYKNLB(String JYKNLB) {
            this.JYKNLB = JYKNLB;
        }

        public String getISDC() {
            return ISDC;
        }

        public void setISDC(String ISDC) {
            this.ISDC = ISDC;
        }

        public String getDCDATE_LAST() {
            return DCDATE_LAST;
        }

        public void setDCDATE_LAST(String DCDATE_LAST) {
            this.DCDATE_LAST = DCDATE_LAST;
        }

        public String getDCMQZK_LAST() {
            return DCMQZK_LAST;
        }

        public void setDCMQZK_LAST(String DCMQZK_LAST) {
            this.DCMQZK_LAST = DCMQZK_LAST;
        }

        public String getDCDQYX_LAST() {
            return DCDQYX_LAST;
        }

        public void setDCDQYX_LAST(String DCDQYX_LAST) {
            this.DCDQYX_LAST = DCDQYX_LAST;
        }

        public String getDANGANSZD() {
            return DANGANSZD;
        }

        public void setDANGANSZD(String DANGANSZD) {
            this.DANGANSZD = DANGANSZD;
        }

        public String getGRSHBXJFXZ() {
            return GRSHBXJFXZ;
        }

        public void setGRSHBXJFXZ(String GRSHBXJFXZ) {
            this.GRSHBXJFXZ = GRSHBXJFXZ;
        }

        public String getGRSHBXJFZT() {
            return GRSHBXJFZT;
        }

        public void setGRSHBXJFZT(String GRSHBXJFZT) {
            this.GRSHBXJFZT = GRSHBXJFZT;
        }

        public String getJFDWZZJGDM() {
            return JFDWZZJGDM;
        }

        public void setJFDWZZJGDM(String JFDWZZJGDM) {
            this.JFDWZZJGDM = JFDWZZJGDM;
        }

        public String getJFDWXTCID() {
            return JFDWXTCID;
        }

        public void setJFDWXTCID(String JFDWXTCID) {
            this.JFDWXTCID = JFDWXTCID;
        }

        public String getJFDWSHBXDJM() {
            return JFDWSHBXDJM;
        }

        public void setJFDWSHBXDJM(String JFDWSHBXDJM) {
            this.JFDWSHBXDJM = JFDWSHBXDJM;
        }

        public String getJFDWMC() {
            return JFDWMC;
        }

        public void setJFDWMC(String JFDWMC) {
            this.JFDWMC = JFDWMC;
        }

        public int getDCID_LAST() {
            return DCID_LAST;
        }

        public void setDCID_LAST(int DCID_LAST) {
            this.DCID_LAST = DCID_LAST;
        }

        public String getDCLX_LAST() {
            return DCLX_LAST;
        }

        public void setDCLX_LAST(String DCLX_LAST) {
            this.DCLX_LAST = DCLX_LAST;
        }

        public String getHJDZ_ROAD() {
            return HJDZ_ROAD;
        }

        public void setHJDZ_ROAD(String HJDZ_ROAD) {
            this.HJDZ_ROAD = HJDZ_ROAD;
        }

        public String getHJDZ_LANE() {
            return HJDZ_LANE;
        }

        public void setHJDZ_LANE(String HJDZ_LANE) {
            this.HJDZ_LANE = HJDZ_LANE;
        }

        public String getHJDZ_NUMBER() {
            return HJDZ_NUMBER;
        }

        public void setHJDZ_NUMBER(String HJDZ_NUMBER) {
            this.HJDZ_NUMBER = HJDZ_NUMBER;
        }

        public String getHJDZ_ROOMNO() {
            return HJDZ_ROOMNO;
        }

        public void setHJDZ_ROOMNO(String HJDZ_ROOMNO) {
            this.HJDZ_ROOMNO = HJDZ_ROOMNO;
        }

        public String getLXDZ_ROAD() {
            return LXDZ_ROAD;
        }

        public void setLXDZ_ROAD(String LXDZ_ROAD) {
            this.LXDZ_ROAD = LXDZ_ROAD;
        }

        public String getLXDZ_LANE() {
            return LXDZ_LANE;
        }

        public void setLXDZ_LANE(String LXDZ_LANE) {
            this.LXDZ_LANE = LXDZ_LANE;
        }

        public String getLXDZ_NUMBER() {
            return LXDZ_NUMBER;
        }

        public void setLXDZ_NUMBER(String LXDZ_NUMBER) {
            this.LXDZ_NUMBER = LXDZ_NUMBER;
        }

        public String getLXDZ_ROOMNO() {
            return LXDZ_ROOMNO;
        }

        public void setLXDZ_ROOMNO(String LXDZ_ROOMNO) {
            this.LXDZ_ROOMNO = LXDZ_ROOMNO;
        }

        public String getSFCJR() {
            return SFCJR;
        }

        public void setSFCJR(String SFCJR) {
            this.SFCJR = SFCJR;
        }

        public String getJLCJSJ() {
            return JLCJSJ;
        }

        public void setJLCJSJ(String JLCJSJ) {
            this.JLCJSJ = JLCJSJ;
        }

        public String getJLGXSJ() {
            return JLGXSJ;
        }

        public void setJLGXSJ(String JLGXSJ) {
            this.JLGXSJ = JLGXSJ;
        }

        public boolean isISVALID() {
            return ISVALID;
        }

        public void setISVALID(boolean ISVALID) {
            this.ISVALID = ISVALID;
        }

        public int getRecordCount() {
            return RecordCount;
        }

        public void setRecordCount(int RecordCount) {
            this.RecordCount = RecordCount;
        }

        public String getGetPhotoUrl() {
            return GetPhotoUrl;
        }

        public void setGetPhotoUrl(String GetPhotoUrl) {
            this.GetPhotoUrl = GetPhotoUrl;
        }

        public String getCSDATE1() {
            return CSDATE1;
        }

        public void setCSDATE1(String CSDATE1) {
            this.CSDATE1 = CSDATE1;
        }
//    }
}
