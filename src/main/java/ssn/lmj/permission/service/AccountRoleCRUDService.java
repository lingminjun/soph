package ssn.lmj.permission.service;

import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.List;
import ssn.lmj.permission.db.dao.AccountRoleDAO;
import ssn.lmj.permission.service.entities.AccountRolePOJO;
import ssn.lmj.permission.service.entities.AccountRoleResults;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * Table: s_account_role
 */
@IDLGroup(domain = "permission", desc = "AccountRole的相关操作", codeDefine = Exceptions.class)
public interface AccountRoleCRUDService {

    /**
     * AccountRoleDAO
     * @return 
     */
    public AccountRoleDAO getAccountRoleDAO();

    /**
     * insert AccountRolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "addAccountRole", desc = "插入AccountRolePOJO", security = IDLAPISecurity.UserLogin)
    public long addAccountRole(@IDLParam(name = "accountRole", desc = "实体对象", required = true) final AccountRolePOJO accountRole) throws IDLException;

    /**
     * batch insert AccountRolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "batchAddAccountRole", desc = "批量插入AccountRolePOJO", security = IDLAPISecurity.UserLogin)
    public boolean batchAddAccountRole(@IDLParam(name = "models", desc = "实体对象", required = true) final List<AccountRolePOJO> models,
                                       @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException;

    /**
     * remove AccountRolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "removeTheAccountRole", desc = "删除AccountRolePOJO", security = IDLAPISecurity.UserLogin)
    public boolean removeTheAccountRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * update AccountRolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "updateTheAccountRole", desc = "更新AccountRolePOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
    public boolean updateTheAccountRole(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                        @IDLParam(name = "accountId", desc = "Account Id", required = false) final Long accountId,
                                        @IDLParam(name = "roleId", desc = "Role Id", required = false) final Long roleId) throws IDLException;

    /**
     * find AccountRolePOJO by id
     * @return 
     */
    @IDLAPI(module = "permission",name = "findTheAccountRole", desc = "寻找AccountRolePOJO", security = IDLAPISecurity.UserLogin)
    public AccountRolePOJO findTheAccountRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query AccountRolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryAccountRoleByAccountId", desc = "批量插入AccountRolePOJO", security = IDLAPISecurity.UserLogin)
    public AccountRoleResults queryAccountRoleByAccountId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                          @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                          @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                          @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                          @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query AccountRolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryAccountRoleByAccountIdAndRoleId", desc = "批量插入AccountRolePOJO", security = IDLAPISecurity.UserLogin)
    public AccountRoleResults queryAccountRoleByAccountIdAndRoleId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                   @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                   @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                                   @IDLParam(name = "roleId", desc = "Role Id", required = true) final long roleId,
                                                                   @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                   @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

}

