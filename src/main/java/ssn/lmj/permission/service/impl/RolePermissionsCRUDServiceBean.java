package ssn.lmj.permission.service.impl;

import com.lmj.stone.cache.AutoCache;
import com.lmj.stone.service.Injects;
import com.lmj.stone.service.BlockUtil;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssn.lmj.permission.db.dao.RolePermissionsDAO;
import ssn.lmj.permission.db.dobj.RolePermissionsDO;
import ssn.lmj.permission.service.entities.RolePermissionsPOJO;
import ssn.lmj.permission.service.entities.RolePermissionsResults;
import ssn.lmj.permission.service.RolePermissionsCRUDService;
import javax.annotation.Resource;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@Service
public class RolePermissionsCRUDServiceBean implements RolePermissionsCRUDService {

    @Resource(name = "demoTransactionManager")
    protected DataSourceTransactionManager transactionManager;

    @Autowired
    private RolePermissionsDAO rolePermissionsDAO;

    /**
     * RolePermissionsDAO
     * @return 
     */
    @Override
    public RolePermissionsDAO getRolePermissionsDAO() {
        return this.rolePermissionsDAO;
    }

    /**
     * insert RolePermissionsPOJO
     * @return 
     */
    @Override
    public long addRolePermissions(@IDLParam(name = "rolePermissions", desc = "实体对象", required = true) final RolePermissionsPOJO rolePermissions) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Long>() {
            @Override
            public Long run() throws Throwable {
                RolePermissionsDO dobj = new RolePermissionsDO();
                Injects.fill(rolePermissions,dobj);
                if (getRolePermissionsDAO().insert(dobj) > 0) {
                    return (Long)dobj.id;
                } else {
                    return -1l;
                }
            }
        });
    }

    /**
     * batch insert RolePermissionsPOJO
     * @return 
     */
    @Override
    public boolean batchAddRolePermissions(@IDLParam(name = "models", desc = "实体对象", required = true) final List<RolePermissionsPOJO> models,
                                           @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException {
        if (ignoreError) {
            for (final RolePermissionsPOJO  pojo : models) {
                BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        RolePermissionsDO dobj = new RolePermissionsDO();
                        Injects.fill(pojo,dobj);
                        getRolePermissionsDAO().insert(dobj);
                        return true;
                    }
                });
            }
            return true;
        } else {
           return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                @Override
                public Boolean run() throws Throwable {
                    for (RolePermissionsPOJO pojo : models) {
                        RolePermissionsDO dobj = new RolePermissionsDO();
                        Injects.fill(pojo,dobj);
                        getRolePermissionsDAO().insert(dobj);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * remove RolePermissionsPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_PERMISSIONS_#{id}", evict = true)
    public boolean removeTheRolePermissions(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                getRolePermissionsDAO().deleteById(id);
                return true;
           }
        });
    }

    /**
     * update RolePermissionsPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_PERMISSIONS_#{id}", evict = true)
    public boolean updateTheRolePermissions(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                            @IDLParam(name = "roleId", desc = "Role Id", required = false) final Long roleId,
                                            @IDLParam(name = "permissionId", desc = "Permission Id", required = false) final Long permissionId,
                                            @IDLParam(name = "permissionKey", desc = "【冗余】权限定义键值(字母数字加下划线)", required = false) final String permissionKey,
                                            @IDLParam(name = "permissionName", desc = "【冗余】权限名称，冗余存储", required = false) final String permissionName,
                                            @IDLParam(name = "domain", desc = "【冗余】权限分类或者权限作用域", required = false) final String domain) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                RolePermissionsDO dobj = new RolePermissionsDO();
                dobj.id = (long)id;
                dobj.roleId = roleId;
                dobj.permissionId = permissionId;
                dobj.permissionKey = permissionKey;
                dobj.permissionName = permissionName;
                dobj.domain = domain;
                getRolePermissionsDAO().update(dobj);
                return true;
            }
        });
    }

    /**
     * find RolePermissionsPOJO by id
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_PERMISSIONS_#{id}", async = true, condition="!#{noCache}")
    public RolePermissionsPOJO findTheRolePermissions(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        RolePermissionsDO dobj = getRolePermissionsDAO().getById(id);
        RolePermissionsPOJO pojo = new RolePermissionsPOJO();
        Injects.fill(dobj,pojo);
        return pojo;
    }

    /**
     * query RolePermissionsPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_PERMISSIONS_QUERY_BY_ROLE_ID:#{roleId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public RolePermissionsResults queryRolePermissionsByRoleId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                               @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                               @IDLParam(name = "roleId", desc = "Role Id", required = true) final long roleId,
                                                               @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                               @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        RolePermissionsResults rlt = new RolePermissionsResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getRolePermissionsDAO().countByRoleId(roleId,(isDeleted ? 1 : 0));
        List<RolePermissionsDO> list = getRolePermissionsDAO().queryByRoleId(roleId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<RolePermissionsPOJO>();
        for (RolePermissionsDO dobj : list) {
            RolePermissionsPOJO pojo = new RolePermissionsPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

    /**
     * query RolePermissionsPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_PERMISSIONS_QUERY_BY_ROLE_ID:#{roleId}_PERMISSION_ID:#{permissionId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public RolePermissionsResults queryRolePermissionsByRoleIdAndPermissionId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                              @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                              @IDLParam(name = "roleId", desc = "Role Id", required = true) final long roleId,
                                                                              @IDLParam(name = "permissionId", desc = "Permission Id", required = true) final long permissionId,
                                                                              @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                              @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        RolePermissionsResults rlt = new RolePermissionsResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getRolePermissionsDAO().countByRoleIdAndPermissionId(roleId,permissionId,(isDeleted ? 1 : 0));
        List<RolePermissionsDO> list = getRolePermissionsDAO().queryByRoleIdAndPermissionId(roleId,permissionId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<RolePermissionsPOJO>();
        for (RolePermissionsDO dobj : list) {
            RolePermissionsPOJO pojo = new RolePermissionsPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

}

