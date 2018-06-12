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
 * Description: 认证分很多种，第一步是account，第二步是user
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:09
 *
 * android获取个参数参考：https://www.jianshu.com/p/b6f4b0aca6b0
 */
@IDLGroup(domain = "auth", desc = "身份认证服务 -- Authentication service", codeDefine = Exceptions.class)
public interface AuthService {

    @IDLAPI(module = "auth",name = "logDevice", desc = "认证设备，标记一个设备被登录 -- Sign in device", security = IDLAPISecurity.None)
    Token logDevice(@IDLParam(name = "appId", desc = "Application Id", required = true) App appId,
                    @IDLParam(name = "manufacturer", desc = "设备制造商", required = true) String manufacturer,
                    @IDLParam(name = "model", desc = "设备型号、浏览器则内核型号", required = true) String model,
                    @IDLParam(name = "brand", desc = "品牌名", required = true) String brand,
                    @IDLParam(name = "device", desc = "设备名", required = true) String device,
                    @IDLParam(name = "os", desc = "系统版本，如iOS 9.0/android 8.0等等，浏览器型号版本", required = true) String os,
                    @IDLParam(name = "idfa", desc = "广告追踪id", required = false) String idfa,
                    @IDLParam(name = "idfv", desc = "广告追踪id 替代方案，just ios", required = false) String idfv,
                    @IDLParam(name = "imei", desc = "imei or meid", required = false) String imei,
                    @IDLParam(name = "mac", desc = "mac地址", required = false) String mac,
                    @IDLParam(name = "cip", desc = "客户端ip", required = false) String cip,
                    @IDLParam(name = "ua", desc = "user agent", required = false) String ua,
                    @IDLParam(name = "source", desc = "推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.", required = false) String source,
                    @IDLParam(name = "jwt", desc = "jwt device的token", required = false) String jwt
    ) throws IDLException;


    @IDLAPI(module = "auth",name = "login", desc = "认证 -- Sign in", security = IDLAPISecurity.DeviceRegister)
    @IDLError({Exceptions.NOT_FOUND_ACCOUNT_ERROR_CODE})
    Token login(@IDLParam(name = "from", desc = "账号来源 -- Account from", required = true) Platform from,
                @IDLParam(name = "account", desc = "账号 -- Account", required = true) String account,
                @IDLParam(name = "secret", desc = "密码淹码 -- Password secret", required = false) String secret,
                @IDLParam(name = "smsCode", desc = "短信验证码 -- SMSCode", required = false) String smsCode,
                @IDLParam(name = "captcha", desc = "人机验证码 -- Captcha", required = false) String captcha,
                @IDLParam(name = "session", desc = "人机验证码会话 -- Captcha session", required = false) String session,
                @IDLParam(name = "source", desc = "推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.", required = false) String source,
                @IDLParam(name = "user", desc = "创建用户,其他情况只创建账号 -- Create user", required = false) boolean user,
                @IDLParam(name = "jwt", desc = "jwt device的token 或者其他token，登录前需logDevice", required = true) String jwt
    ) throws IDLException;


    @IDLAPI(module = "auth",name = "refresh", desc = "刷新token -- 更换token", security = IDLAPISecurity.AccountLogin)
    Token refresh(@IDLParam(name = "jwt", desc = "jwt", required = true) String jwt) throws IDLException;


    @IDLAPI(module = "auth",name = "logout", desc = "注销 -- Logout", security = IDLAPISecurity.AccountLogin)
    boolean logout(@IDLParam(name = "jwt", desc = "jwt", required = true) String jwt) throws IDLException;//控制好权限

    @IDLAPI(module = "auth",name = "oauth", desc = "三方认证,若未绑定手机或者邮箱返回账号，并非用户，可调用login绑定用户 -- open auth", security = IDLAPISecurity.DeviceRegister)
    @IDLError({Exceptions.NOT_FOUND_ACCOUNT_ERROR_CODE})
    Token oauth(@IDLParam(name = "from", desc = "账号来源 -- Account from", required = true) Platform from,
                @IDLParam(name = "oauthResponse", desc = "三方认证返回响应，与oauthRequest对应 -- Response", required = true) String oauthResponse,
                @IDLParam(name = "captcha", desc = "验证码 -- Captcha", required = false) String captcha,
                @IDLParam(name = "session", desc = "验证码会话 -- Captcha session", required = false) String session,
                @IDLParam(name = "source", desc = "推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.", required = false) String source,
                @IDLParam(name = "user", desc = "创建用户,其他情况只创建账号 -- Create user", required = false) boolean user,
                @IDLParam(name = "jwt", desc = "jwt device的token，登录前必须logDevice", required = true) String jwt
    ) throws IDLException;

    @IDLAPI(module = "auth",name = "oauthRequest", desc = "获取第三方登录授权请求链接,将三方appid放到服务端", security = IDLAPISecurity.None)
    public OAuthRequst reqLoginAuth(
            @IDLParam(required = true, name = "platform", desc = "合作方id，如微信wechat_open") Platform platform,
            @IDLParam(required = false, name = "callback", desc = "回调链接") String callback,
            @IDLParam(required = false, name = "ptnAppid", desc = "第三方应用id，默认为丰趣海淘app对应的应用id") App ptnAppid
    ) throws IDLException;


    //下发验证码
    @IDLAPI(module = "auth",name = "sendSMS", desc = "下发验证码", security = IDLAPISecurity.DeviceRegister)
    public Captcha sendSMS(
            @IDLParam(required = true, name = "mobile", desc = "手机号，带国家码，如+86-15673886666") String mobile,
            @IDLParam(required = true, name = "captchaType", desc = "验证码类型") CaptchaType type,
            @IDLParam(required = false, name = "captcha", desc = "人机验证码 -- Captcha") String captcha,
            @IDLParam(required = false, name = "session", desc = "人机验证码会话 -- Captcha session") String session
    ) throws IDLException;
}
