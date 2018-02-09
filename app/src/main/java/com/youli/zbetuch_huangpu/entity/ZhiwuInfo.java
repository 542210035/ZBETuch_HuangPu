package com.youli.zbetuch_huangpu.entity;

import com.youli.zbetuch_huangpu.treeview.TreeNodeId;
import com.youli.zbetuch_huangpu.treeview.TreeNodeLabel;
import com.youli.zbetuch_huangpu.treeview.TreeNodePid;

/**
 * 作者: zhengbin on 2017/12/7.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * http://web.youli.pw:8088/Json/Get_Line_level.aspx?window=员工待办事宜
 *
 *
 * [{"ID":2,"NAME":"就业促进中心","PARENT_ID":1},
 * {"ID":17,"NAME":"南京东路街道援助员","PARENT_ID":7},
 * {"ID":18,"NAME":"外滩街道援助员","PARENT_ID":8},
 * {"ID":19,"NAME":"半淞园街道援助员","PARENT_ID":9},
 * {"ID":20,"NAME":"小东门街道援助员","PARENT_ID":10},
 * {"ID":21,"NAME":"老西门街道援助员","PARENT_ID":11},
 * {"ID":22,"NAME":"豫园街道援助员","PARENT_ID":12},
 * {"ID":23,"NAME":"五里桥街道援助员","PARENT_ID":13},
 * {"ID":24,"NAME":"淮海中路街道援助员","PARENT_ID":14},
 * {"ID":25,"NAME":"打浦街道援助员","PARENT_ID":15},
 * {"ID":26,"NAME":"瑞金二路街道援助员","PARENT_ID":16},
 * {"ID":3,"NAME":"劳动力资源","PARENT_ID":2},
 * {"ID":4,"NAME":"创业指导","PARENT_ID":2},
 * {"ID":5,"NAME":"职介服务","PARENT_ID":2},
 * {"ID":6,"NAME":"街道指导","PARENT_ID":2},
 * {"ID":7,"NAME":"南京东路街道","PARENT_ID":2},
 * {"ID":8,"NAME":"外滩街道","PARENT_ID":2},
 * {"ID":9,"NAME":"半淞园街道","PARENT_ID":2},
 * {"ID":10,"NAME":"小东门街道","PARENT_ID":2},
 * {"ID":11,"NAME":"老西门街道","PARENT_ID":2},
 * {"ID":12,"NAME":"豫园街道","PARENT_ID":2},
 * {"ID":13,"NAME":"五里桥街道","PARENT_ID":2},
 * {"ID":14,"NAME":"淮海中路街道","PARENT_ID":2},
 * {"ID":15,"NAME":"打浦街道","PARENT_ID":2},
 * {"ID":16,"NAME":"瑞金二路街道","PARENT_ID":2}]
 */

public class ZhiwuInfo {

    @TreeNodeId
    private int ID;
    @TreeNodeLabel
    private String NAME;
    @TreeNodePid
    private int PARENT_ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getPARENT_ID() {
        return PARENT_ID;
    }

    public void setPARENT_ID(int PARENT_ID) {
        this.PARENT_ID = PARENT_ID;
    }
}
