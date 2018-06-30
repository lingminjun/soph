package ssn.lmj.user.service.impl;

import com.lmj.stone.cache.AutoCache;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLParam;
import com.lmj.stone.service.BlockUtil;
import com.lmj.stone.service.Injects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssn.lmj.user.db.dao.AccountDAO;
import ssn.lmj.user.db.dao.AccountExtDAO;
import ssn.lmj.user.db.dao.UserDAO;
import ssn.lmj.user.db.dao.UserExtDAO;
import ssn.lmj.user.db.dobj.AccountDO;
import ssn.lmj.user.db.dobj.AccountExtDO;
import ssn.lmj.user.db.dobj.UserDO;
import ssn.lmj.user.db.dobj.UserExtDO;
import ssn.lmj.user.service.UserService;
import ssn.lmj.user.service.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-12
 * Time: 上午10:33
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    AccountExtDAO accountExtDAO;

    @Autowired
    UserExtDAO userExtDAO;

    @Override
    @AutoCache(key = "ACCOUNT_#{accountId}", evict = true)
    public Account queryAccount(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId) throws IDLException {
        AccountDO accountDO = accountDAO.getById(accountId);
        if (accountDO == null) {
            return null;
        }
        Account account = new Account();
        Injects.fill(accountDO,account);
        return account;
    }

    @Override
    public List<Account> queryAccounts(long[] accountIds) throws IDLException {
        List<Long> ids = new ArrayList<Long>();
        for (long id : accountIds) {
            ids.add(id);
        }
        List<AccountDO> list = accountDAO.queryByIds(ids);
        List<Account> result = new ArrayList<Account>();
        for (AccountDO accountDO : list) {
            Account account = new Account();
            Injects.fill(accountDO,account);
            result.add(account);
        }
        return result;
    }

    @Override
    @AutoCache(key = "USER_#{userId}", evict = true)
    public User queryUser(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId) throws IDLException {
        UserDO userDO = userDAO.getById(userId);
        if (userDO == null) {
            return null;
        }
        User user = new User();
        Injects.fill(userDO,user);
        return user;
    }

    @Override
    public List<User> queryUsers(long[] userIds) throws IDLException {
        List<Long> ids = new ArrayList<Long>();
        for (long id : userIds) {
            ids.add(id);
        }
        List<UserDO> list = userDAO.queryByIds(ids);
        List<User> result = new ArrayList<User>();
        for (UserDO userDO : list) {
            User user = new User();
            Injects.fill(userDO,user);
            result.add(user);
        }
        return result;
    }

    @Override
    public User attachedUser(long accountId) throws IDLException {
        AccountDO accountDO = accountDAO.getById(accountId);
        if (accountDO == null || accountDO.uid == null) {
            return null;
        }
        UserDO userDO = userDAO.getById(accountDO.uid);
        if (userDO == null) {
            return null;
        }
        User user = new User();
        Injects.fill(userDO,user);
        return user;
    }

    @Override
    public List<Account> bindingAccounts(long userId) throws IDLException {
        List<AccountDO> list = accountDAO.queryByUid(userId,0,null,false,0,1000);
        List<Account> result = new ArrayList<Account>();
        for (AccountDO accountDO : list) {
            Account account = new Account();
            Injects.fill(accountDO,account);
            result.add(account);
        }
        return result;
    }

    @Override
    public UserAttribute userAttribute(long userId, String[] fieldNames) throws IDLException {
        List<String> keys = new ArrayList<String>();
        for (String key : fieldNames) {
            keys.add(key);
        }
        List<UserExtDO> list = userExtDAO.queryAttributesForKeys(userId,keys);
        UserAttribute userAttribute = new UserAttribute();
        userAttribute.uid = userId;
        userAttribute.attributes = new ArrayList<Attribute>();
        if (list == null || list.size() == 0) {
            return userAttribute;
        }
        for (UserExtDO userExtDO : list) {
            Attribute attribute = new Attribute();
            Injects.fill(userExtDO,attribute);
            attribute.name = userExtDO.dataType;
            attribute.value = userExtDO.dataValue;
            userAttribute.attributes.add(attribute);
        }
        return userAttribute;
    }

    @Override
    public AccountAttribute accountAttribute(long accountId, String[] fieldNames) throws IDLException {
        List<String> keys = new ArrayList<String>();
        for (String key : fieldNames) {
            keys.add(key);
        }
        List<AccountExtDO> list = accountExtDAO.queryAttributesForKeys(accountId,keys);
        AccountAttribute accountAttribute = new AccountAttribute();
        accountAttribute.accountId = accountId;
        accountAttribute.attributes = new ArrayList<Attribute>();
        if (list == null || list.size() == 0) {
            return accountAttribute;
        }
        for (AccountExtDO accountExtDO : list) {
            Attribute attribute = new Attribute();
            Injects.fill(accountExtDO,attribute);
            attribute.name = accountExtDO.dataType;
            attribute.value = accountExtDO.dataValue;
            accountAttribute.attributes.add(attribute);
        }
        return accountAttribute;
    }

    @Override
    @AutoCache(key = "USER_#{id}", evict = true)
    public boolean updateUser(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                              @IDLParam(name = "nick", desc = "昵称", required = false) final String nick,
                              @IDLParam(name = "name", desc = "真实名字", required = false) final String name,
                              @IDLParam(name = "idNumber", desc = "身份证号", required = false) final String idNumber,
                              @IDLParam(name = "head", desc = "头像url", required = false) final String head,
                              @IDLParam(name = "mobile", desc = "手机号", required = false) final String mobile,
                              @IDLParam(name = "email", desc = "邮箱", required = false) final String email,
                              @IDLParam(name = "gender", desc = "性别:0未知;1男;2女;3人妖;", required = false) final Integer gender,
                              @IDLParam(name = "grade", desc = "等级", required = false) final Integer grade,
                              @IDLParam(name = "rank", desc = "等级", required = false) final String rank,
                              @IDLParam(name = "role", desc = "角色描述,业务角色描述", required = false) final String role) throws IDLException {

        UserDO dobj = new UserDO();
        dobj.id = (long)id;
        dobj.nick = nick;
        dobj.name = name;
        dobj.idNumber = idNumber;
        dobj.head = head;
        dobj.mobile = mobile;
        dobj.email = email;
        dobj.gender = gender;
        dobj.grade = grade;
        dobj.rank = rank;
        dobj.role = role;
        userDAO.update(dobj);
        return true;

    }

    @Override
    @AutoCache(key = "ACCOUNT_#{id}", evict = true)
    public boolean updateAccount(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                 @IDLParam(name = "nick", desc = "昵称", required = false) final String nick,
                                 @IDLParam(name = "head", desc = "头像url", required = false) final String head,
                                 @IDLParam(name = "gender", desc = "性别:0未知;1男;2女;3人妖;", required = false) final Integer gender,
                                 @IDLParam(name = "mobile", desc = "手机号", required = false) final String mobile,
                                 @IDLParam(name = "email", desc = "邮箱", required = false) final String email) throws IDLException {

        AccountDO dobj = new AccountDO();
        dobj.id = (long)id;
        dobj.nick = nick;
        dobj.head = head;
        dobj.gender = gender;
        dobj.mobile = mobile;
        dobj.email = email;
        accountDAO.update(dobj);
        return true;
    }

    @Override
    public boolean addUserAttribute(long uid, String key, String value) throws IDLException {
        UserExtDO userExtDO = userExtDAO.getAttributeForKey(uid,key);

        if (userExtDO == null) {
            userExtDO = new UserExtDO();
            userExtDO.uid = uid;
            userExtDO.dataType = key;
        }

        userExtDO.dataValue = value;
        userExtDO.isDelete = 0;
        userExtDAO.insertOrUpdate(userExtDO);
        return true;
    }

    @Override
    public boolean removeUserAttribute(long uid, String key) throws IDLException {
        UserExtDO userExtDO = userExtDAO.getAttributeForKey(uid,key);
        if (userExtDO == null) {
            return true;
        }
        if (userExtDO.isDelete != null && userExtDO.isDelete.intValue() == 1) {
            return true;
        }
        userExtDO.isDelete = 0;
        userExtDAO.update(userExtDO);
        return true;
    }

    @Override
    public boolean addAccountAttribute(long accountId, String key, String value) throws IDLException {
        AccountExtDO accountExtDO = accountExtDAO.getAttributeForKey(accountId,key);

        if (accountExtDO == null) {
            accountExtDO = new AccountExtDO();
            accountExtDO.accountId = accountId;
            accountExtDO.dataType = key;
        }

        accountExtDO.dataValue = value;
        accountExtDO.isDelete = 0;
        accountExtDAO.insertOrUpdate(accountExtDO);
        return true;
    }

    @Override
    public boolean removeAccountAttribute(long accountId, String key) throws IDLException {
        AccountExtDO accountExtDO = accountExtDAO.getAttributeForKey(accountId,key);
        if (accountExtDO == null) {
            return true;
        }
        if (accountExtDO.isDelete != null && accountExtDO.isDelete.intValue() == 1) {
            return true;
        }
        accountExtDO.isDelete = 0;
        accountExtDAO.update(accountExtDO);
        return true;
    }


}
