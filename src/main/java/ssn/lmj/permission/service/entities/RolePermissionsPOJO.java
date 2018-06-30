package ssn.lmj.permission.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;
import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * Table: s_role_permissions
 */
@IDLDesc("s_role_permissions对象生成")
public final class RolePermissionsPOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDLDesc("")
    public long    id;
    @IDLDesc("Role Id")
    public long    roleId;
    @IDLDesc("Permission Id")
    public long    permissionId;
    @IDLDesc("【冗余】权限定义键值(字母数字加下划线)")
    public String  permissionKey;
    @IDLDesc("【冗余】权限名称，冗余存储")
    public String  permissionName;
    @IDLDesc("【冗余】权限分类或者权限作用域")
    public String  domain;
    @IDLDesc("创建时间")
    public long    createAt;
    @IDLDesc("修改时间")
    public long    modifiedAt;
}

