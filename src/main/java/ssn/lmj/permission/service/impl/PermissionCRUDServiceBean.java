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
import ssn.lmj.permission.db.dao.PermissionDAO;
import ssn.lmj.permission.db.dobj.PermissionDO;
import ssn.lmj.permission.service.entities.PermissionPOJO;
import ssn.lmj.permission.service.entities.PermissionResults;
import ssn.lmj.permission.service.PermissionCRUDService;
import javax.annotation.Resource;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@Service
public class PermissionCRUDServiceBean implements PermissionCRUDService {

    @Resource(name = "demoTransactionManager")
    protected DataSourceTransactionManager transactionManager;

    @Autowired
    private PermissionDAO permissionDAO;

    /**
     * PermissionDAO
     * @return 
     */
    @Override
    public PermissionDAO getPermissionDAO() {
        return this.permissionDAO;
    }

    /**
     * insert PermissionPOJO
     * @return 
     */
    @Override
    public long addPermission(@IDLParam(name = "permission", desc = "实体对象", required = true) final PermissionPOJO permission) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Long>() {
            @Override
            public Long run() throws Throwable {
                PermissionDO dobj = new PermissionDO();
                Injects.fill(permission,dobj);
                if (getPermissionDAO().insert(dobj) > 0) {
                    return (Long)dobj.id;
                } else {
                    return -1l;
                }
            }
        });
    }

    /**
     * batch insert PermissionPOJO
     * @return 
     */
    @Override
    public boolean batchAddPermission(@IDLParam(name = "models", desc = "实体对象", required = true) final List<PermissionPOJO> models,
                                      @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException {
        if (ignoreError) {
            for (final PermissionPOJO  pojo : models) {
                BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        PermissionDO dobj = new PermissionDO();
                        Injects.fill(pojo,dobj);
                        getPermissionDAO().insert(dobj);
                        return true;
                    }
                });
            }
            return true;
        } else {
           return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                @Override
                public Boolean run() throws Throwable {
                    for (PermissionPOJO pojo : models) {
                        PermissionDO dobj = new PermissionDO();
                        Injects.fill(pojo,dobj);
                        getPermissionDAO().insert(dobj);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * remove PermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "PERMISSION_#{id}", evict = true)
    public boolean removeThePermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                getPermissionDAO().deleteById(id);
                return true;
           }
        });
    }

    /**
     * update PermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "PERMISSION_#{id}", evict = true)
    public boolean updateThePermission(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                       @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = false) final String domain,
                                       @IDLParam(name = "key", desc = "权限定义键值(字母数字加下划线)", required = false) final String key,
                                       @IDLParam(name = "name", desc = "权限名称", required = false) final String name,
                                       @IDLParam(name = "cmmt", desc = "权限描述", required = false) final String cmmt) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                PermissionDO dobj = new PermissionDO();
                dobj.id = (long)id;
                dobj.domain = domain;
                dobj.key = key;
                dobj.name = name;
                dobj.cmmt = cmmt;
                getPermissionDAO().update(dobj);
                return true;
            }
        });
    }

    /**
     * find PermissionPOJO by id
     * @return 
     */
    @Override
    @AutoCache(key = "PERMISSION_#{id}", async = true, condition="!#{noCache}")
    public PermissionPOJO findThePermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        PermissionDO dobj = getPermissionDAO().getById(id);
        PermissionPOJO pojo = new PermissionPOJO();
        Injects.fill(dobj,pojo);
        return pojo;
    }

    /**
     * query PermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "PERMISSION_QUERY_BY_DOMAIN:#{domain}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public PermissionResults queryPermissionByDomain(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                     @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                     @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                                     @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                     @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        PermissionResults rlt = new PermissionResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getPermissionDAO().countByDomain(domain,(isDeleted ? 1 : 0));
        List<PermissionDO> list = getPermissionDAO().queryByDomain(domain,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<PermissionPOJO>();
        for (PermissionDO dobj : list) {
            PermissionPOJO pojo = new PermissionPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

    /**
     * query PermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "PERMISSION_QUERY_BY_DOMAIN:#{domain}_KEY:#{key}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public PermissionResults queryPermissionByDomainAndKey(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                           @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                           @IDLParam(name = "domain", desc = "权限分类或者权限作用域", required = true) final String domain,
                                                           @IDLParam(name = "key", desc = "权限定义键值(字母数字加下划线)", required = true) final String key,
                                                           @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                           @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        PermissionResults rlt = new PermissionResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getPermissionDAO().countByDomainAndKey(domain,key,(isDeleted ? 1 : 0));
        List<PermissionDO> list = getPermissionDAO().queryByDomainAndKey(domain,key,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<PermissionPOJO>();
        for (PermissionDO dobj : list) {
            PermissionPOJO pojo = new PermissionPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

}

