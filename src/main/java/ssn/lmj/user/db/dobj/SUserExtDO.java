package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed May 09 23:04:41 CST 2018
 * Table: s_user_ext
 */
public final class SUserExtDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public Long    uid; // user id
    public String  dataType; // 数据类型
    public String  dataValue; // 数据值
    public String  cmmt; // 注释
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

