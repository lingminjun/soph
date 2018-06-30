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
@IDLGroup(domain = "user", desc = "用户服务 -- User service", codeDefine = Exceptions.class)
public interface UserService {

    @IDLAPI(module = "user",name = "queryAccount", desc = "账号查询 -- query account", security = IDLAPISecurity.AccountLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    Account queryAccount(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId) throws IDLException;

    @IDLAPI(module = "user",name = "queryAccounts", desc = "账号查询 -- query account", security = IDLAPISecurity.AccountLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    List<Account> queryAccounts(@IDLParam(name = "accountIds", desc = "账号id -- account id", required = true) long[] accountIds) throws IDLException;

    @IDLAPI(module = "user",name = "queryUser", desc = "用户查询 -- query user", security = IDLAPISecurity.UserLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    User queryUser(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId) throws IDLException;

    @IDLAPI(module = "user",name = "queryUsers", desc = "用户查询 -- query user", security = IDLAPISecurity.UserLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    List<User> queryUsers(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long[] userIds) throws IDLException;

    @IDLAPI(module = "user",name = "attachedUser", desc = "账号所依附的用户 -- The user that the account attached to.", security = IDLAPISecurity.AccountLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    User attachedUser(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId) throws IDLException;

    @IDLAPI(module = "user",name = "bindingAccounts", desc = "查询绑定的账号 -- query binding accounts", security = IDLAPISecurity.UserLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    List<Account> bindingAccounts(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId) throws IDLException;

    @IDLAPI(module = "user",name = "userAttribute", desc = "用户的属性值 -- query user other attribute", security = IDLAPISecurity.UserLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    UserAttribute userAttribute(@IDLParam(name = "userId", desc = "用户id -- user id", required = true) long userId,
                                @IDLParam(name = "fieldNames", desc = "属性名 -- FieldName", required = true) String[] fieldNames) throws IDLException;

    @IDLAPI(module = "user",name = "accountAttribute", desc = "账号的属性值 -- query account other attribute", security = IDLAPISecurity.AccountLogin)
    @IDLError({Exceptions.NOT_FOUND_USER_ERROR_CODE})
    AccountAttribute accountAttribute(@IDLParam(name = "accountId", desc = "账号id -- account id", required = true) long accountId,
                                      @IDLParam(name = "fieldNames", desc = "属性名 -- FieldName", required = true) String[] fieldNames) throws IDLException;


    @IDLAPI(module = "user",name = "updateUser", desc = "更新UserPOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
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
                              @IDLParam(name = "role", desc = "角色描述,业务角色描述", required = false) final String role) throws IDLException;

    @IDLAPI(module = "user",name = "updateAccount", desc = "更新AccountPOJO，仅更新不为空的字段", security = IDLAPISecurity.AccountLogin)
    public boolean updateAccount(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                 @IDLParam(name = "nick", desc = "昵称", required = false) final String nick,
                                 @IDLParam(name = "head", desc = "头像url", required = false) final String head,
                                 @IDLParam(name = "gender", desc = "性别:0未知;1男;2女;3人妖;", required = false) final Integer gender,
                                 @IDLParam(name = "mobile", desc = "手机号", required = false) final String mobile,
                                 @IDLParam(name = "email", desc = "邮箱", required = false) final String email) throws IDLException;

    @IDLAPI(module = "user",name = "addUserAttribute", desc = "添加用户额外属性", security = IDLAPISecurity.UserLogin)
    public boolean addUserAttribute(@IDLParam(name = "uid", desc = "user id", required = true) final long uid,
                                    @IDLParam(name = "key", desc = "属性名", required = true) final String key,
                                    @IDLParam(name = "value", desc = "值", required = true) final String value) throws IDLException;


    @IDLAPI(module = "user",name = "removeUserAttribute", desc = "删除User额外属性", security = IDLAPISecurity.UserLogin)
    public boolean removeUserAttribute(@IDLParam(name = "uid", desc = "user id", required = true) final long uid,
                                       @IDLParam(name = "key", desc = "属性名", required = true) final String key) throws IDLException;


    @IDLAPI(module = "user",name = "addAccountAttribute", desc = "添加用户额外属性", security = IDLAPISecurity.AccountLogin)
    public boolean addAccountAttribute(@IDLParam(name = "accountId", desc = "account id", required = true) final long accountId,
                                       @IDLParam(name = "key", desc = "属性名", required = true) final String key,
                                       @IDLParam(name = "value", desc = "值", required = true) final String value) throws IDLException;

    @IDLAPI(module = "user",name = "removeTheAccountExt", desc = "删除Account额外属性", security = IDLAPISecurity.AccountLogin)
    public boolean removeAccountAttribute(@IDLParam(name = "accountId", desc = "account id", required = true) final long accountId,
                                          @IDLParam(name = "key", desc = "属性名", required = true) final String key) throws IDLException;

}
