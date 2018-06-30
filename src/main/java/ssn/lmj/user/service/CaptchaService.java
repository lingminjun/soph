package ssn.lmj.user.service;

import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import ssn.lmj.user.service.entities.*;


/**
 * Created with IntelliJ IDEA.
 * Description: 人机验证服务
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:09
 *
 * android获取个参数参考：https://www.jianshu.com/p/b6f4b0aca6b0
 */
@IDLGroup(domain = "captcha", desc = "验证服务器", codeDefine = Exceptions.class)
public interface CaptchaService {

    //下发验证码
    @IDLAPI(module = "captcha",name = "sendSMS", desc = "下发验证码", security = IDLAPISecurity.DeviceRegister)
    public Captcha sendSMS(
            @IDLParam(required = true, name = "mobile", desc = "手机号，带国家码，如+86-15673886666") String mobile,
            @IDLParam(required = true, name = "captchaType", desc = "验证码类型") CaptchaType type,
            @IDLParam(required = false, name = "captcha", desc = "人机验证码 -- Captcha") String captcha,
            @IDLParam(required = false, name = "session", desc = "人机验证码会话 -- Captcha session") String session
    ) throws IDLException;

    //图片验证码
    @IDLAPI(module = "captcha",name = "prayImageCode", desc = "下发验证码", security = IDLAPISecurity.DeviceRegister)
    public Captcha prayImageCode(
            @IDLParam(required = true, name = "account", desc = "账号信息，如手机号+86-15673886666；邮箱；open_id等等") String account,
            @IDLParam(required = true, name = "captchaType", desc = "验证码类型") CaptchaType type) throws IDLException;

    @IDLAPI(module = "captcha",name = "evalSMSCode", desc = "验证短信验证码", security = IDLAPISecurity.DeviceRegister)
    public CaptchaEvaluation evalSMSCode(String mobile, String smcCode, CaptchaType type, int aging/*秒*/);

    @IDLAPI(module = "captcha",name = "evalSafeguardCode", desc = "验证验证码", security = IDLAPISecurity.DeviceRegister)
    public CaptchaEvaluation evalSafeguardCode(String session, String smcCode, CaptchaType type);
}
