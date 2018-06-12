package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-19
 * Time: 下午4:53
 */
@IDLDesc("验证码类型")
public enum  CaptchaType {
    @IDLDesc("防攻击，风控保护验证码")
    safeguard,
    @IDLDesc("登录绑定认证验证码")
    auth,
    @IDLDesc("实名认证")
    certification
}
