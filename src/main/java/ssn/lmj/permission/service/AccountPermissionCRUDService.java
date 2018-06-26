package ssn.lmj.permission.service;

import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.List;
import ssn.lmj.permission.db.dao.AccountPermissionDAO;
import ssn.lmj.permission.service.entities.AccountPermissionPOJO;
import ssn.lmj.permission.service.entities.AccountPermissionResults;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_account_permission
 */
@IDLGroup(domain = "permission", desc = "AccountPermission的相关操作", codeDefine = Exceptions.class)
public interface AccountPermissionCRUDService {

    /**
     * AccountPermissionDAO
     * @return 
     */
    public AccountPermissionDAO getAccountPermissionDAO();

    /**
     * insert AccountPermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "addAccountPermission", desc = "插入AccountPermissionPOJO", security = IDLAPISecurity.UserLogin)
    public long addAccountPermission(@IDLParam(name = "accountPermission", desc = "实体对象", required = true) final AccountPermissionPOJO accountPermission) throws IDLException;

    /**
     * batch insert AccountPermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "batchAddAccountPermission", desc = "批量插入AccountPermissionPOJO", security = IDLAPISecurity.UserLogin)
    public boolean batchAddAccountPermission(@IDLParam(name = "models", desc = "实体对象", required = true) final List<AccountPermissionPOJO> models,
                                             @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException;

    /**
     * remove AccountPermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "removeTheAccountPermission", desc = "删除AccountPermissionPOJO", security = IDLAPISecurity.UserLogin)
    public boolean removeTheAccountPermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * update AccountPermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "updateTheAccountPermission", desc = "更新AccountPermissionPOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
    public boolean updateTheAccountPermission(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                              @IDLParam(name = "accountId", desc = "Account Id", required = false) final Long accountId,
                                              @IDLParam(name = "permissionId", desc = "Permission Id", required = false) final Long permissionId,
                                              @IDLParam(name = "permissionKey", desc = "【冗余】权限定义键值(字母数字加下划线)", required = false) final String permissionKey,
                                              @IDLParam(name = "permissionName", desc = "【冗余】权限名称，冗余存储", required = false) final String permissionName,
                                              @IDLParam(name = "domain", desc = "【冗余】权限分类或者权限作用域", required = false) final String domain) throws IDLException;

    /**
     * find AccountPermissionPOJO by id
     * @return 
     */
    @IDLAPI(module = "permission",name = "findTheAccountPermission", desc = "寻找AccountPermissionPOJO", security = IDLAPISecurity.UserLogin)
    public AccountPermissionPOJO findTheAccountPermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query AccountPermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryAccountPermissionByAccountId", desc = "批量插入AccountPermissionPOJO", security = IDLAPISecurity.UserLogin)
    public AccountPermissionResults queryAccountPermissionByAccountId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                      @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                      @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                                      @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                      @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query AccountPermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryAccountPermissionByAccountIdAndPermissionId", desc = "批量插入AccountPermissionPOJO", security = IDLAPISecurity.UserLogin)
    public AccountPermissionResults queryAccountPermissionByAccountIdAndPermissionId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                                     @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                                     @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                                                     @IDLParam(name = "permissionId", desc = "Permission Id", required = true) final long permissionId,
                                                                                     @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                                     @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

}

