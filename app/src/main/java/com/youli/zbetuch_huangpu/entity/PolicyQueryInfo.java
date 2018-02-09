package com.youli.zbetuch_huangpu.entity;

/**
 * 作者: zhengbin on 2017/12/19.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 *http://web.youli.pw:8088/Json/Get_QUESTION_ANSWER.aspx?type=职业介绍
 * http://web.youli.pw:8088/Json/Get_QUESTION_ANSWER.aspx?type=职业介绍&code=职业
 *
 * [{"ID":21,"POLICY_TYPE":"职业介绍","QUESTIONS":"个人如何申请公共招聘网求职密码？",
 * "ANSWERS":"在劳动年龄范围内的上海户籍求职者，可持本人有效身份证件至就近的人力资源和社会保障部门所属各区县职介所办理个人网上求职密码。"},{"ID":42,"POLICY_TYPE":"职业介绍","QUESTIONS":"失业人员想找工作，该怎么办？","ANSWERS":"在法定劳动年龄段内的上海户籍求职者，可持本人有效身份证件至就近人力资源保障部门所属各区县职业介绍所办理现场求职登记，以及“上海公共招聘网”网上办事密码，上网求职。"}]
 */

public class PolicyQueryInfo {

    private int ID;
    private String POLICY_TYPE;
    private String QUESTIONS;//问题
    private String ANSWERS;//答案

    public String getANSWERS() {
        return ANSWERS;
    }

    public void setANSWERS(String ANSWERS) {
        this.ANSWERS = ANSWERS;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPOLICY_TYPE() {
        return POLICY_TYPE;
    }

    public void setPOLICY_TYPE(String POLICY_TYPE) {
        this.POLICY_TYPE = POLICY_TYPE;
    }

    public String getQUESTIONS() {
        return QUESTIONS;
    }

    public void setQUESTIONS(String QUESTIONS) {
        this.QUESTIONS = QUESTIONS;
    }


}
