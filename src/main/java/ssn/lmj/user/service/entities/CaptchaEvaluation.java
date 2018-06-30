package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-23
 * Time: 下午3:38
 */
@IDLDesc("验证结果")
public enum  CaptchaEvaluation {
    @IDLDesc("危险的，不予通过的")
    danger,

    @IDLDesc("存在风险的，过期的")
    risk,

    @IDLDesc("频繁的，过多的，不安的")
    uneasy,

    @IDLDesc("安全的")
    safety;
}
