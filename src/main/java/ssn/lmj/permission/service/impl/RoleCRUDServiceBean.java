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
import ssn.lmj.permission.db.dao.RoleDAO;
import ssn.lmj.permission.db.dobj.RoleDO;
import ssn.lmj.permission.service.entities.RolePOJO;
import ssn.lmj.permission.service.entities.RoleResults;
import ssn.lmj.permission.service.RoleCRUDService;
import javax.annotation.Resource;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@Service
public class RoleCRUDServiceBean implements RoleCRUDService {

    @Resource(name = "demoTransactionManager")
    protected DataSourceTransactionManager transactionManager;

    @Autowired
    private RoleDAO roleDAO;

    /**
     * RoleDAO
     * @return 
     */
    @Override
    public RoleDAO getRoleDAO() {
        return this.roleDAO;
    }

    /**
     * insert RolePOJO
     * @return 
     */
    @Override
    public long addRole(@IDLParam(name = "role", desc = "实体对象", required = true) final RolePOJO role) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Long>() {
            @Override
            public Long run() throws Throwable {
                RoleDO dobj = new RoleDO();
                Injects.fill(role,dobj);
                if (getRoleDAO().insert(dobj) > 0) {
                    return (Long)dobj.id;
                } else {
                    return -1l;
                }
            }
        });
    }

    /**
     * batch insert RolePOJO
     * @return 
     */
    @Override
    public boolean batchAddRole(@IDLParam(name = "models", desc = "实体对象", required = true) final List<RolePOJO> models,
                                @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException {
        if (ignoreError) {
            for (final RolePOJO  pojo : models) {
                BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        RoleDO dobj = new RoleDO();
                        Injects.fill(pojo,dobj);
                        getRoleDAO().insert(dobj);
                        return true;
                    }
                });
            }
            return true;
        } else {
           return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                @Override
                public Boolean run() throws Throwable {
                    for (RolePOJO pojo : models) {
                        RoleDO dobj = new RoleDO();
                        Injects.fill(pojo,dobj);
                        getRoleDAO().insert(dobj);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * remove RolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_#{id}", evict = true)
    public boolean removeTheRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                getRoleDAO().deleteById(id);
                return true;
           }
        });
    }

    /**
     * update RolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_#{id}", evict = true)
    public boolean updateTheRole(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                 @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = false) final String domain,
                                 @IDLParam(name = "name", desc = "角色名称", required = false) final String name,
                                 @IDLParam(name = "cmmt", desc = "权限描述", required = false) final String cmmt) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                RoleDO dobj = new RoleDO();
                dobj.id = (long)id;
                dobj.domain = domain;
                dobj.name = name;
                dobj.cmmt = cmmt;
                getRoleDAO().update(dobj);
                return true;
            }
        });
    }

    /**
     * find RolePOJO by id
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_#{id}", async = true, condition="!#{noCache}")
    public RolePOJO findTheRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        RoleDO dobj = getRoleDAO().getById(id);
        RolePOJO pojo = new RolePOJO();
        Injects.fill(dobj,pojo);
        return pojo;
    }

    /**
     * query RolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_QUERY_BY_DOMAIN:#{domain}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public RoleResults queryRoleByDomain(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                         @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                         @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                         @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                         @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        RoleResults rlt = new RoleResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getRoleDAO().countByDomain(domain,(isDeleted ? 1 : 0));
        List<RoleDO> list = getRoleDAO().queryByDomain(domain,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<RolePOJO>();
        for (RoleDO dobj : list) {
            RolePOJO pojo = new RolePOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

    /**
     * query RolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ROLE_QUERY_BY_DOMAIN:#{domain}_NAME:#{name}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public RoleResults queryRoleByDomainAndName(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                                @IDLParam(name = "name", desc = "角色名称", required = true) final String name,
                                                @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        RoleResults rlt = new RoleResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getRoleDAO().countByDomainAndName(domain,name,(isDeleted ? 1 : 0));
        List<RoleDO> list = getRoleDAO().queryByDomainAndName(domain,name,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<RolePOJO>();
        for (RoleDO dobj : list) {
            RolePOJO pojo = new RolePOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

}

