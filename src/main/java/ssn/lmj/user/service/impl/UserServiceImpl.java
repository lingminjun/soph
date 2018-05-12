package ssn.lmj.user.service.impl;

import com.lmj.stone.idl.IDLException;
import ssn.lmj.user.service.UserService;
import ssn.lmj.user.service.entities.Account;
import ssn.lmj.user.service.entities.AccountAttribute;
import ssn.lmj.user.service.entities.User;
import ssn.lmj.user.service.entities.UserAttribute;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-12
 * Time: 上午10:33
 */
public class UserServiceImpl implements UserService {

    @Override
    public Account queryAccount(long accountId) throws IDLException {
        return null;
    }

    @Override
    public User queryUser(long userId) throws IDLException {
        return null;
    }

    @Override
    public User attachedUser(long accountId) throws IDLException {
        return null;
    }

    @Override
    public List<Account> bindingAccounts(long userId) throws IDLException {
        return null;
    }

    @Override
    public UserAttribute userAttribute(long userId, long fieldName) throws IDLException {
        return null;
    }

    @Override
    public AccountAttribute accountAttribute(long accountId, long fieldName) throws IDLException {
        return null;
    }
}
