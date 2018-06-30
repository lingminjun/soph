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
import ssn.lmj.permission.db.dao.AccountPermissionDAO;
import ssn.lmj.permission.db.dobj.AccountPermissionDO;
import ssn.lmj.permission.service.entities.AccountPermissionPOJO;
import ssn.lmj.permission.service.entities.AccountPermissionResults;
import ssn.lmj.permission.service.AccountPermissionCRUDService;
import javax.annotation.Resource;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@Service
public class AccountPermissionCRUDServiceBean implements AccountPermissionCRUDService {

    @Resource(name = "demoTransactionManager")
    protected DataSourceTransactionManager transactionManager;

    @Autowired
    private AccountPermissionDAO accountPermissionDAO;

    /**
     * AccountPermissionDAO
     * @return 
     */
    @Override
    public AccountPermissionDAO getAccountPermissionDAO() {
        return this.accountPermissionDAO;
    }

    /**
     * insert AccountPermissionPOJO
     * @return 
     */
    @Override
    public long addAccountPermission(@IDLParam(name = "accountPermission", desc = "实体对象", required = true) final AccountPermissionPOJO accountPermission) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Long>() {
            @Override
            public Long run() throws Throwable {
                AccountPermissionDO dobj = new AccountPermissionDO();
                Injects.fill(accountPermission,dobj);
                if (getAccountPermissionDAO().insert(dobj) > 0) {
                    return (Long)dobj.id;
                } else {
                    return -1l;
                }
            }
        });
    }

    /**
     * batch insert AccountPermissionPOJO
     * @return 
     */
    @Override
    public boolean batchAddAccountPermission(@IDLParam(name = "models", desc = "实体对象", required = true) final List<AccountPermissionPOJO> models,
                                             @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException {
        if (ignoreError) {
            for (final AccountPermissionPOJO  pojo : models) {
                BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        AccountPermissionDO dobj = new AccountPermissionDO();
                        Injects.fill(pojo,dobj);
                        getAccountPermissionDAO().insert(dobj);
                        return true;
                    }
                });
            }
            return true;
        } else {
           return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                @Override
                public Boolean run() throws Throwable {
                    for (AccountPermissionPOJO pojo : models) {
                        AccountPermissionDO dobj = new AccountPermissionDO();
                        Injects.fill(pojo,dobj);
                        getAccountPermissionDAO().insert(dobj);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * remove AccountPermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_PERMISSION_#{id}", evict = true)
    public boolean removeTheAccountPermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                getAccountPermissionDAO().deleteById(id);
                return true;
           }
        });
    }

    /**
     * update AccountPermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_PERMISSION_#{id}", evict = true)
    public boolean updateTheAccountPermission(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                              @IDLParam(name = "accountId", desc = "Account Id", required = false) final Long accountId,
                                              @IDLParam(name = "permissionId", desc = "Permission Id", required = false) final Long permissionId,
                                              @IDLParam(name = "permissionKey", desc = "【冗余】权限定义键值(字母数字加下划线)", required = false) final String permissionKey,
                                              @IDLParam(name = "permissionName", desc = "【冗余】权限名称，冗余存储", required = false) final String permissionName,
                                              @IDLParam(name = "domain", desc = "【冗余】权限分类或者权限作用域", required = false) final String domain) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                AccountPermissionDO dobj = new AccountPermissionDO();
                dobj.id = (long)id;
                dobj.accountId = accountId;
                dobj.permissionId = permissionId;
                dobj.permissionKey = permissionKey;
                dobj.permissionName = permissionName;
                dobj.domain = domain;
                getAccountPermissionDAO().update(dobj);
                return true;
            }
        });
    }

    /**
     * find AccountPermissionPOJO by id
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_PERMISSION_#{id}", async = true, condition="!#{noCache}")
    public AccountPermissionPOJO findTheAccountPermission(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        AccountPermissionDO dobj = getAccountPermissionDAO().getById(id);
        AccountPermissionPOJO pojo = new AccountPermissionPOJO();
        Injects.fill(dobj,pojo);
        return pojo;
    }

    /**
     * query AccountPermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_PERMISSION_QUERY_BY_ACCOUNT_ID:#{accountId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public AccountPermissionResults queryAccountPermissionByAccountId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                      @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                      @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                                      @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                      @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        AccountPermissionResults rlt = new AccountPermissionResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getAccountPermissionDAO().countByAccountId(accountId,(isDeleted ? 1 : 0));
        List<AccountPermissionDO> list = getAccountPermissionDAO().queryByAccountId(accountId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<AccountPermissionPOJO>();
        for (AccountPermissionDO dobj : list) {
            AccountPermissionPOJO pojo = new AccountPermissionPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

    /**
     * query AccountPermissionPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_PERMISSION_QUERY_BY_ACCOUNT_ID:#{accountId}_PERMISSION_ID:#{permissionId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public AccountPermissionResults queryAccountPermissionByAccountIdAndPermissionId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                                     @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                                     @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                                                     @IDLParam(name = "permissionId", desc = "Permission Id", required = true) final long permissionId,
                                                                                     @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                                     @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        AccountPermissionResults rlt = new AccountPermissionResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getAccountPermissionDAO().countByAccountIdAndPermissionId(accountId,permissionId,(isDeleted ? 1 : 0));
        List<AccountPermissionDO> list = getAccountPermissionDAO().queryByAccountIdAndPermissionId(accountId,permissionId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<AccountPermissionPOJO>();
        for (AccountPermissionDO dobj : list) {
            AccountPermissionPOJO pojo = new AccountPermissionPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

}

