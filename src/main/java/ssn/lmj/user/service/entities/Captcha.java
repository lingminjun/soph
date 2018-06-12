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
@IDLDesc("验证码响应")
public class Captcha implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDLDesc("验证码类型")
    public CaptchaType type;

    @IDLDesc("会话id")
    public String session;

    @IDLDesc("描述文案")
    public String message;

    @IDLDesc("存在风险，当为true时，需要使用link做二次验证，表示验证码并没获取")
    public boolean risky;

    @IDLDesc("风险控制二次验证链接")
    public String link;
}
