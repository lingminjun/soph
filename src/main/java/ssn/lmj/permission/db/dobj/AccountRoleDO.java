package ssn.lmj.permission.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * Table: s_account_role
 */
public final class AccountRoleDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public Long    accountId; // Account Id
    public Long    roleId; // Role Id
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

