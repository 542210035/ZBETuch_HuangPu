package com.youli.zbetuch_huangpu.entity;

/**
 * 作者: zhengbin on 2017/12/7.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 http://web.youli.pw:8088/Json/Get_Line_Staff.aspx?all=false&ID=2&window=员工待办事宜
 [{"line_id":2,"staff_id":1,"name":"admin"}]

 */

public class MeetPersonInfo {

    private int line_id;
    private int staff_id;
    private String name;
    private boolean isCheck;

    public MeetPersonInfo(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getLine_id() {
        return line_id;
    }

    public void setLine_id(int line_id) {
        this.line_id = line_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }
}
