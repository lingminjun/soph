package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_permission_role
 */
public final class SPermissionRoleDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public String  domain; // 权限分类或者权限作用域
    public String  name; // 角色名称
    public String  cmmt; // 权限描述
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

