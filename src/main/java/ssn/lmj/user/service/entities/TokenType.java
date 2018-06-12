package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-18
 * Time: 下午3:26
 */
@IDLDesc("token类型")
public enum TokenType {
    @IDLDesc("设备认证")
    deviceAuth,

    @IDLDesc("账号认证")
    accountAuth,

    @IDLDesc("用户认证")
    userAuth,

    @IDLDesc("一次性")
    once,

    @IDLDesc("延伸，仅仅用于refresh，请使用前面token的定义")
    extend,

    @IDLDesc("其他用途")
    other
}
