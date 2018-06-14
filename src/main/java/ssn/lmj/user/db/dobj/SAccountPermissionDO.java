package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_account_permission
 */
public final class SAccountPermissionDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public Long    accountId; // Account Id
    public Long    permissionId; // Permission Id
    public String  permission; // 【冗余】权限定义键值(字母数字加下划线)
    public String  permissionName; // 【冗余】权限名称，冗余存储
    public String  domain; // 【冗余】权限分类或者权限作用域
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

