package ssn.lmj.user.service;

import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import ssn.lmj.user.service.entities.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:09
 */
@IDLGroup(domain = "user", desc = "用户服务 -- User service", codeDefine = AuthExceptions.class)
public interface UserService {

    @IDLAPI(module = "user",name = "queryAccount", desc = "账号查询 -- query account", security = IDLAPISecurity.AccountLogin)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    Account queryAccount(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId) throws IDLException;

    @IDLAPI(module = "user",name = "queryUser", desc = "用户查询 -- query user", security = IDLAPISecurity.UserLogin)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    User queryUser(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId) throws IDLException;

    @IDLAPI(module = "user",name = "attachedUser", desc = "账号所依附的用户 -- The user that the account attached to.", security = IDLAPISecurity.AccountLogin)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    User attachedUser(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId) throws IDLException;

    @IDLAPI(module = "user",name = "bindingAccounts", desc = "查询绑定的账号 -- query binding accounts", security = IDLAPISecurity.UserLogin)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    List<Account> bindingAccounts(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId) throws IDLException;

    @IDLAPI(module = "user",name = "userAttribute", desc = "用户的属性值 -- query user other attribute", security = IDLAPISecurity.UserLogin)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    UserAttribute userAttribute(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId,
                                @IDLParam(name = "fieldName", desc = "属性名 -- FieldName", required = true) long fieldName) throws IDLException;

    @IDLAPI(module = "user",name = "accountAttribute", desc = "账号的属性值 -- query account other attribute", security = IDLAPISecurity.UserLogin)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    AccountAttribute accountAttribute(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId,
                                      @IDLParam(name = "fieldName", desc = "属性名 -- FieldName", required = true) long fieldName) throws IDLException;
}
