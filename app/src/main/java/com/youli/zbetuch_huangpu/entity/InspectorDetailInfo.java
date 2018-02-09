package com.youli.zbetuch_huangpu.entity;

/**
 * 作者: zhengbin on 2017/12/11.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 *
 * http://web.youli.pw:8088/Json/First/Get_Work_Notice_finish.aspx?master_id=1
 *
 * [{"type":"已完成","check_date":"2017-10-30T14:53:00.783","name":"admin","mark":null}
 *
 */

public class InspectorDetailInfo {

    private String type;//状态
    private String check_date;//时间
    private String name;//姓名
    private String mark;//备注

    public String getCheck_date() {
        return check_date;
    }

    public void setCheck_date(String check_date) {
        this.check_date = check_date;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
