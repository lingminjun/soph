package ssn.lmj.user.service;

import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import ssn.lmj.user.service.entities.Token;

/**
 * Created with IntelliJ IDEA.
 * Description: 认证分很多种，第一步是account，第二步是user
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:09
 */
@IDLGroup(domain = "auth", desc = "身份认证服务 -- Authentication service", codeDefine = AuthExceptions.class)
public interface AuthService {

    //from定义
    String MOBILE = "mobile";
    String EMAIL  = "email";
    String NAME   = "name";


    @IDLAPI(module = "auth",name = "login", desc = "认证 -- Sign in", security = IDLAPISecurity.None)
    @IDLError({AuthExceptions.NOT_FOUND_USER_ERROR_CODE})
    Token login(@IDLParam(name = "from", desc = "账号来源 -- Account from", required = true) String from,
                @IDLParam(name = "account", desc = "账号 -- Account", required = true) String account,
                @IDLParam(name = "secret", desc = "密码淹码 -- Password secret", required = true) String secret,
                @IDLParam(name = "token", desc = "用于登录验证Token -- A one-time Token", required = false) String token,
                @IDLParam(name = "captcha", desc = "验证码 -- Captcha", required = false) String captcha,
                @IDLParam(name = "session", desc = "验证码会话 -- Captcha session", required = false) String session);


    @IDLAPI(module = "auth",name = "logout", desc = "注销 -- Logout", security = IDLAPISecurity.UserLogin)
    boolean logout(@IDLParam(name = "userId", desc = "登录用户Id -- UserId", required = false) long userId,
                   @IDLParam(name = "accountId", desc = "账号id -- AccountId", required = false) long accountId);//控制好权限
}
