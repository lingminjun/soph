package ssn.lmj.user.service;

import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
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
 *
 * android获取个参数参考：https://www.jianshu.com/p/b6f4b0aca6b0
 */
@IDLGroup(domain = "auth", desc = "身份认证服务 -- Authentication service", codeDefine = AuthExceptions.class)
public interface AuthService {

    //from定义
    String MOBILE = "mobile";
    String EMAIL  = "email";
    String NAME   = "name";

    @IDLAPI(module = "auth",name = "logDevice", desc = "认证设备 -- Sign in device", security = IDLAPISecurity.None)
    Token logDevice(@IDLParam(name = "appId", desc = "Application Id", required = true) int appId,
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
                    @IDLParam(name = "source", desc = "推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.", required = false) String source
    ) throws IDLException;


    @IDLAPI(module = "auth",name = "login", desc = "认证 -- Sign in", security = IDLAPISecurity.None)
    @IDLError({AuthExceptions.NOT_FOUND_ACCOUNT_ERROR_CODE})
    Token login(@IDLParam(name = "from", desc = "账号来源 -- Account from", required = true) String from,
                @IDLParam(name = "account", desc = "账号 -- Account", required = true) String account,
                @IDLParam(name = "secret", desc = "密码淹码 -- Password secret", required = true) String secret,
                @IDLParam(name = "token", desc = "用于登录验证Token -- A one-time Token", required = false) String token,
                @IDLParam(name = "captcha", desc = "验证码 -- Captcha", required = false) String captcha,
                @IDLParam(name = "session", desc = "验证码会话 -- Captcha session", required = false) String session,
                @IDLParam(name = "source", desc = "推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.", required = false) String source,
                @IDLParam(name = "user", desc = "创建用户,其他情况只创建账号 -- Create user", required = false) boolean user) throws IDLException;

    @IDLAPI(module = "auth",name = "refresh", desc = "刷新token -- 更换token", security = IDLAPISecurity.AccountLogin)
    Token refresh(@IDLParam(name = "jwt", desc = "jwt", required = true) String jwt) throws IDLException;


    @IDLAPI(module = "auth",name = "logout", desc = "注销 -- Logout", security = IDLAPISecurity.AccountLogin)
    boolean logout(@IDLParam(name = "jwt", desc = "jwt", required = true) String jwt) throws IDLException;//控制好权限
}
