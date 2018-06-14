package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_account_permission_role
 */
public final class SAccountPermissionRoleDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public Long    accountId; // Account Id
    public Long    roleId; // Role Id
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

