package ssn.lmj.permission.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;
import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_role
 */
@IDLDesc("s_role对象生成")
public final class RolePOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDLDesc("")
    public long    id;
    @IDLDesc("权限分类或者权限作用域")
    public String  domain;
    @IDLDesc("角色名称")
    public String  name;
    @IDLDesc("权限描述")
    public String  cmmt;
    @IDLDesc("创建时间")
    public long    createAt;
    @IDLDesc("修改时间")
    public long    modifiedAt;
}

