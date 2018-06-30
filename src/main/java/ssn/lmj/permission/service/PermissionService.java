package ssn.lmj.permission.service;


import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import ssn.lmj.permission.db.dao.PermissionDAO;
import ssn.lmj.permission.service.entities.PermissionPOJO;
import ssn.lmj.permission.service.entities.PermissionResults;
import ssn.lmj.user.service.Exceptions;

import java.util.List;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_permission
 */
@IDLGroup(domain = "permission", desc = "Permission的相关操作", codeDefine = Exceptions.class)
public interface PermissionService {

    /**
     * PermissionDAO
     * @return
     */
    public PermissionDAO getPermissionDAO();

    /**
     * insert PermissionPOJO
     * @return
     */
    @IDLAPI(module = "permission",name = "addPermission", desc = "插入PermissionPOJO", security = IDLAPISecurity.UserLogin)
    public long addPermission(@IDLParam(name = "permission", desc = "实体对象", required = true) final PermissionPOJO permission) throws IDLException;

    /**
     * batch insert PermissionPOJO
     * @return
     */
    @IDLAPI(module = "permission",name = "batchAddPermission", desc = "批量插入PermissionPOJO", security = IDLAPISecurity.UserLogin)
    public boolean batchAddPermission(@IDLParam(name = "models", desc = "实体对象", required = true) final List<PermissionPOJO> models,
                                      @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException;

    /**
     * remove PermissionPOJO
     * @return
     */
    @IDLAPI(module = "permission",name = "removeThePermission", desc = "删除PermissionPOJO", security = IDLAPISecurity.UserLogin)
    public boolean removeThePermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * update PermissionPOJO
     * @return
     */
    @IDLAPI(module = "permission",name = "updateThePermission", desc = "更新PermissionPOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
    public boolean updateThePermission(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                       @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = false) final String domain,
                                       @IDLParam(name = "key", desc = "权限定义键值(字母数字加下划线)", required = false) final String key,
                                       @IDLParam(name = "name", desc = "权限名称", required = false) final String name,
                                       @IDLParam(name = "cmmt", desc = "权限描述", required = false) final String cmmt) throws IDLException;

    /**
     * find PermissionPOJO by id
     * @return
     */
    @IDLAPI(module = "permission",name = "findThePermission", desc = "寻找PermissionPOJO", security = IDLAPISecurity.UserLogin)
    public PermissionPOJO findThePermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id, @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query PermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryPermissionByDomain", desc = "批量插入PermissionPOJO", security = IDLAPISecurity.UserLogin)
    public PermissionResults queryPermissionByDomain(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                     @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                     @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                                     @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                     @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

    /**
     * query PermissionPOJO
     * @return 
     */
    @IDLAPI(module = "permission",name = "queryPermissionByDomainAndKey", desc = "批量插入PermissionPOJO", security = IDLAPISecurity.UserLogin)
    public PermissionResults queryPermissionByDomainAndKey(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                           @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                           @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                                           @IDLParam(name = "key", desc = "权限定义键值(字母数字加下划线)", required = true) final String key,
                                                           @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                           @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException;

}


