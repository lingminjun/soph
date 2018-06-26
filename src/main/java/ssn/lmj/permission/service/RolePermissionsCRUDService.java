package ssn.lmj.permission.service;

import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.List;
import ssn.lmj.permission.db.dao.RolePermissionsDAO;
import ssn.lmj.permission.service.entities.RolePermissionsPOJO;
import ssn.lmj.permission.service.entities.RolePermissionsResults;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_role_permissions
 */
@IDLGroup(domain = "permission", desc = "RolePermissions的相关操作", codeDefine = Exceptions.class)
public interface RolePermissionsCRUDService {

    /**
     * RolePermissionsDAO
     * @return 
     */
    public RolePermissionsDAO getRolePermissionsDAO();

    /**
     * insert RolePermissionsPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "addRolePermissions", desc = "插入RolePermissionsPOJO", security = IDLAPISecurity.UserLogin)
    public long addRolePermissions(@IDLParam(name = "rolePermissions", desc = "实体对象", required = true) final RolePermissionsPOJO rolePermissions) throws IDLException;

    /**
     * batch insert RolePermissionsPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "batchAddRolePermissions", desc = "批量插入RolePermissionsPOJO", security = IDLAPISecurity.UserLogin)
    public boolean batchAddRolePermissions(@IDLParam(name = "models", desc = "实体对象", required = true) final List<RolePermissionsPOJO> models,
                                           @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException;

    /**
     * remove RolePermissionsPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "removeTheRolePermissions", desc = "删除RolePermissionsPOJO", security = IDLAPISecurity.UserLogin)
    public boolean removeTheRolePermissions(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * update RolePermissionsPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "updateTheRolePermissions", desc = "更新RolePermissionsPOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
    public boolean updateTheRolePermissions(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                            @IDLParam(name = "roleId", desc = "Role Id", required = false) final Long roleId,
                                            @IDLParam(name = "permissionId", desc = "Permission Id", required = false) final Long permissionId,
                                            @IDLParam(name = "permissionKey", desc = "【冗余】权限定义键值(字母数字加下划线)", required = false) final String permissionKey,
                                            @IDLParam(name = "permissionName", desc = "【冗余】权限名称，冗余存储", required = false) final String permissionName,
                                            @IDLParam(name = "domain", desc = "【冗余】权限分类或者权限作用域", required = false) final String domain) throws IDLException;

    /**
     * find RolePermissionsPOJO by id
     * @return 
     */
    @IDLAPI(module = "permission",name = "findTheRolePermissions", desc = "寻找RolePermissionsPOJO", security = IDLAPISecurity.UserLogin)
    public RolePermissionsPOJO findTheRolePermissions(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query RolePermissionsPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryRolePermissionsByRoleId", desc = "批量插入RolePermissionsPOJO", security = IDLAPISecurity.UserLogin)
    public RolePermissionsResults queryRolePermissionsByRoleId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                               @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                               @IDLParam(name = "roleId", desc = "Role Id", required = true) final long roleId,
                                                               @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                               @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query RolePermissionsPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryRolePermissionsByRoleIdAndPermissionId", desc = "批量插入RolePermissionsPOJO", security = IDLAPISecurity.UserLogin)
    public RolePermissionsResults queryRolePermissionsByRoleIdAndPermissionId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                              @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                              @IDLParam(name = "roleId", desc = "Role Id", required = true) final long roleId,
                                                                              @IDLParam(name = "permissionId", desc = "Permission Id", required = true) final long permissionId,
                                                                              @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                              @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

}

