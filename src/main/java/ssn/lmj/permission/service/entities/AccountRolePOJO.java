package ssn.lmj.permission.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;
import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_account_role
 */
@IDLDesc("s_account_role对象生成")
public final class AccountRolePOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDLDesc("")
    public long    id;
    @IDLDesc("Account Id")
    public long    accountId;
    @IDLDesc("Role Id")
    public long    roleId;
    @IDLDesc("创建时间")
    public long    createAt;
    @IDLDesc("修改时间")
    public long    modifiedAt;
}

