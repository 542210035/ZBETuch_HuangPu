package com.youli.zbetuch_huangpu.entity;

/**
 * 作者: zhengbin on 2017/12/18.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * http://web.youli.pw:8088/Json/Get_policy_Type.aspx
 *
 * [{"ID":1,"TYPE_NAME":"失业保险"},
 * {"ID":2,"TYPE_NAME":"就业补贴"},
 * {"ID":3,"TYPE_NAME":"职业介绍"},
 * {"ID":4,"TYPE_NAME":"职业指导"},{"ID":5,"TYPE_NAME":"职业培训"},
 * {"ID":6,"TYPE_NAME":"职业见习"},{"ID":7,"TYPE_NAME":"劳动力资源"},
 * {"ID":8,"TYPE_NAME":"开业指导"},{"ID":1002,"TYPE_NAME":"便民信息"}]
 *
 * 政策查询里面的政策类别
 */

public class PolicyTypeInfo {

    private int ID;
    private String TYPE_NAME;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTYPE_NAME() {
        return TYPE_NAME;
    }

    public void setTYPE_NAME(String TYPE_NAME) {
        this.TYPE_NAME = TYPE_NAME;
    }
}
