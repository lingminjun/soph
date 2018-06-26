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
import ssn.lmj.permission.db.dao.AccountRoleDAO;
import ssn.lmj.permission.db.dobj.AccountRoleDO;
import ssn.lmj.permission.service.entities.AccountRolePOJO;
import ssn.lmj.permission.service.entities.AccountRoleResults;
import ssn.lmj.permission.service.AccountRoleCRUDService;
import javax.annotation.Resource;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * SQLFile: sqls/permission.sql
 */
@Service
public class AccountRoleCRUDServiceBean implements AccountRoleCRUDService {

    @Resource(name = "demoTransactionManager")
    protected DataSourceTransactionManager transactionManager;

    @Autowired
    private AccountRoleDAO accountRoleDAO;

    /**
     * AccountRoleDAO
     * @return 
     */
    @Override
    public AccountRoleDAO getAccountRoleDAO() {
        return this.accountRoleDAO;
    }

    /**
     * insert AccountRolePOJO
     * @return 
     */
    @Override
    public long addAccountRole(@IDLParam(name = "accountRole", desc = "实体对象", required = true) final AccountRolePOJO accountRole) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Long>() {
            @Override
            public Long run() throws Throwable {
                AccountRoleDO dobj = new AccountRoleDO();
                Injects.fill(accountRole,dobj);
                if (getAccountRoleDAO().insert(dobj) > 0) {
                    return (Long)dobj.id;
                } else {
                    return -1l;
                }
            }
        });
    }

    /**
     * batch insert AccountRolePOJO
     * @return 
     */
    @Override
    public boolean batchAddAccountRole(@IDLParam(name = "models", desc = "实体对象", required = true) final List<AccountRolePOJO> models,
                                       @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException {
        if (ignoreError) {
            for (final AccountRolePOJO  pojo : models) {
                BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        AccountRoleDO dobj = new AccountRoleDO();
                        Injects.fill(pojo,dobj);
                        getAccountRoleDAO().insert(dobj);
                        return true;
                    }
                });
            }
            return true;
        } else {
           return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                @Override
                public Boolean run() throws Throwable {
                    for (AccountRolePOJO pojo : models) {
                        AccountRoleDO dobj = new AccountRoleDO();
                        Injects.fill(pojo,dobj);
                        getAccountRoleDAO().insert(dobj);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * remove AccountRolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_ROLE_#{id}", evict = true)
    public boolean removeTheAccountRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                getAccountRoleDAO().deleteById(id);
                return true;
           }
        });
    }

    /**
     * update AccountRolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_ROLE_#{id}", evict = true)
    public boolean updateTheAccountRole(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                        @IDLParam(name = "accountId", desc = "Account Id", required = false) final Long accountId,
                                        @IDLParam(name = "roleId", desc = "Role Id", required = false) final Long roleId) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                AccountRoleDO dobj = new AccountRoleDO();
                dobj.id = (long)id;
                dobj.accountId = accountId;
                dobj.roleId = roleId;
                getAccountRoleDAO().update(dobj);
                return true;
            }
        });
    }

    /**
     * find AccountRolePOJO by id
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_ROLE_#{id}", async = true, condition="!#{noCache}")
    public AccountRolePOJO findTheAccountRole(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        AccountRoleDO dobj = getAccountRoleDAO().getById(id);
        AccountRolePOJO pojo = new AccountRolePOJO();
        Injects.fill(dobj,pojo);
        return pojo;
    }

    /**
     * query AccountRolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_ROLE_QUERY_BY_ACCOUNT_ID:#{accountId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public AccountRoleResults queryAccountRoleByAccountId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                          @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                          @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                          @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                          @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        AccountRoleResults rlt = new AccountRoleResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getAccountRoleDAO().countByAccountId(accountId,(isDeleted ? 1 : 0));
        List<AccountRoleDO> list = getAccountRoleDAO().queryByAccountId(accountId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<AccountRolePOJO>();
        for (AccountRoleDO dobj : list) {
            AccountRolePOJO pojo = new AccountRolePOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

    /**
     * query AccountRolePOJO
     * @return 
     */
    @Override
    @AutoCache(key = "ACCOUNT_ROLE_QUERY_BY_ACCOUNT_ID:#{accountId}_ROLE_ID:#{roleId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public AccountRoleResults queryAccountRoleByAccountIdAndRoleId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                                   @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                                   @IDLParam(name = "accountId", desc = "Account Id", required = true) final long accountId,
                                                                   @IDLParam(name = "roleId", desc = "Role Id", required = true) final long roleId,
                                                                   @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                                   @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","permission",-1,"翻页参数传入错误");
        }
        AccountRoleResults rlt = new AccountRoleResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getAccountRoleDAO().countByAccountIdAndRoleId(accountId,roleId,(isDeleted ? 1 : 0));
        List<AccountRoleDO> list = getAccountRoleDAO().queryByAccountIdAndRoleId(accountId,roleId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<AccountRolePOJO>();
        for (AccountRoleDO dobj : list) {
            AccountRolePOJO pojo = new AccountRolePOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

}

