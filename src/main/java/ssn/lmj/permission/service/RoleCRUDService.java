package ssn.lmj.permission.service;

import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.List;
import ssn.lmj.permission.db.dao.RoleDAO;
import ssn.lmj.permission.service.entities.RolePOJO;
import ssn.lmj.permission.service.entities.RoleResults;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * Table: s_role
 */
@IDLGroup(domain = "permission", desc = "Role的相关操作", codeDefine = Exceptions.class)
public interface RoleCRUDService {

    /**
     * RoleDAO
     * @return 
     */
    public RoleDAO getRoleDAO();

    /**
     * insert RolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "addRole", desc = "插入RolePOJO", security = IDLAPISecurity.UserLogin)
    public long addRole(@IDLParam(name = "role", desc = "实体对象", required = true) final RolePOJO role) throws IDLException;

    /**
     * batch insert RolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "batchAddRole", desc = "批量插入RolePOJO", security = IDLAPISecurity.UserLogin)
    public boolean batchAddRole(@IDLParam(name = "models", desc = "实体对象", required = true) final List<RolePOJO> models,
                                @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException;

    /**
     * remove RolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "removeTheRole", desc = "删除RolePOJO", security = IDLAPISecurity.UserLogin)
    public boolean removeTheRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * update RolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "updateTheRole", desc = "更新RolePOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
    public boolean updateTheRole(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                 @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = false) final String domain,
                                 @IDLParam(name = "name", desc = "角色名称", required = false) final String name,
                                 @IDLParam(name = "cmmt", desc = "权限描述", required = false) final String cmmt) throws IDLException;

    /**
     * find RolePOJO by id
     * @return 
     */
    @IDLAPI(module = "permission",name = "findTheRole", desc = "寻找RolePOJO", security = IDLAPISecurity.UserLogin)
    public RolePOJO findTheRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query RolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryRoleByDomain", desc = "批量插入RolePOJO", security = IDLAPISecurity.UserLogin)
    public RoleResults queryRoleByDomain(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                         @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                         @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                         @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                         @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query RolePOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryRoleByDomainAndName", desc = "批量插入RolePOJO", security = IDLAPISecurity.UserLogin)
    public RoleResults queryRoleByDomainAndName(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                                @IDLParam(name = "name", desc = "角色名称", required = true) final String name,
                                                @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

}

