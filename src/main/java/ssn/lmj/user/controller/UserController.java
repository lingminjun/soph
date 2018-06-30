package ssn.lmj.user.controller;

import com.lmj.stone.idl.IDLException;
import com.lmj.stone.utils.AuthUtil;
import com.lmj.stone.utils.CookieUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ssn.lmj.user.jwt.JWT;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.Exceptions;
import ssn.lmj.user.service.UserService;
import ssn.lmj.user.service.entities.*;
import ssn.lmj.user.service.impl.PhoneNumUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-13
 * Time: 下午12:01
 */
@RestController
public class UserController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Value("${user.auth.cps.cookie.keys}")
    String refCookieKeys;

    @Value("${user.auth.device.token.maxAge}")
    int deviceTokenMaxAge;

    @Value("${user.auth.user.token.maxAge}")
    int userTokenMaxAge;


    /**
     * 设备注册，用于iOS、Android、小程序等等，客户端需要做一次设备授信
     * //http://localhost:8080/auth/device?appId=1&manufacturer=dd&model=xx&brand=iphone&device=iphone4&os=ios8.0
     * @param appId
     * @param manufacturer
     * @param model
     * @param brand
     * @param device
     * @param os
     * @param idfa
     * @param idfv
     * @param imei
     * @param mac
     * @param headers
     * @param response
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/auth/device", method = RequestMethod.GET)
    public Token authDevice(
            @RequestParam(value="appId", required=true) int appId,
            @RequestParam(value="manufacturer", required=true) String manufacturer,
            @RequestParam(value="model", required=true) String model,
            @RequestParam(value="brand", required=true) String brand,
            @RequestParam(value="device", required=true) String device,
            @RequestParam(value="os", required=true) String os,
            @RequestParam(value="idfa", required=false) String idfa,
            @RequestParam(value="idfv", required=false) String idfv,
            @RequestParam(value="imei", required=false) String imei,
            @RequestParam(value="mac", required=false) String mac,
            @RequestHeader HttpHeaders headers,
            HttpServletResponse response
    ) throws IDLException {

        if (appId < 0) {
            appId = 0;
        }

        String ua = headers.getFirst("User-Agent");
        String ip = AuthUtil.getClientIP(headers);
        String domain = AuthUtil.getHostDomain(headers);

        //支持_src,_spm
        Map<String,String> cookie = UserController.getCookie(headers);
        String source = getCPSStringFromCookie(cookie);

        Token token = authService.logDevice(App.valueOf(appId),manufacturer,model,brand,device,os,idfa,idfv,imei,mac,ip,ua, source,false,null);

        if (token != null) {
            AuthUtil.addDeviceToken(response,token.jwt,token.did,appId,domain,deviceTokenMaxAge);
        }

        return token;
    }

    /**
     * 浏览器登录，不能需要注册设备，生成用户，返回用户token
     * 自动设置cookie，客户端(非浏览器)不能使用此接口,不返回
     * //http://localhost:8080/auth/web/login?appId=1&mobile=%2b86-15673886666&smsCode=234543
     * @param appId
     * @param mobile
     * @param secret
     * @param smsCode
     * @param imgCode
     * @param captchaSession
     * @param headers
     * @param response
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/auth/web/login", method = RequestMethod.GET)
    public Token webLogin(@RequestParam(value="appId", required=true) int appId,
                          @RequestParam(value="mobile", required=true) String mobile,
                          @RequestParam(value="secret", required=false) String secret,
                          @RequestParam(value="smsCode", required=true) String smsCode,
                          @RequestParam(value="imgCode", required=false) String imgCode,
                          @RequestParam(value="captchaSession", required=false) String captchaSession,
                          @RequestHeader HttpHeaders headers,
                          HttpServletResponse response) throws IDLException {
        if (!PhoneNumUtil.checkMobileFormat(mobile)) {
            throw Exceptions.MOBILE_FORMAT_ERROR("手机号格式错误");
        }

        //支持_src,_spm
        Map<String,String> cookie = UserController.getCookie(headers);
        String source = getCPSStringFromCookie(cookie);
        String domain = AuthUtil.getHostDomain(headers);

        //通过user agent生成 deviceToken
        Token deviceToken = generateDeviceToken(headers,App.valueOf(appId),source);
        if (deviceToken != null) {
            AuthUtil.addDeviceToken(response,deviceToken.jwt,deviceToken.did,appId,domain,deviceTokenMaxAge);
        }

        Token token = authService.login(Platform.mobile,mobile,secret,mobile,smsCode,imgCode,captchaSession,source,true,deviceToken.jwt);
        if (token != null) {
            AuthUtil.addUserToken(response,token.jwt,token.did,token.uid,token.acnt,appId,domain,userTokenMaxAge);
            token.jwt = "浏览器登录，TOKEN将自动写入Cookie中";//设置完token后不再返回浏览器jwt,后面自动从token中获取
        }

        return token;
    }

    /**
     * 移动端手机号登录接口，支持一下登录场景
     * 1、账号密码登录
     * 2、验证码登录
     * 3、验证码注册及登录（并设置密码）
     * 4、验证码设置密码及登录
     * 5、风控异常配合以上4中场景
     * @param appId
     * @param mobile
     * @param secret
     * @param smsCode
     * @param imgCode
     * @param captchaSession
     * @param headers
     * @return
     * @throws IDLException
     */
    //http://localhost:8080/auth/app/login?appId=1&mobile=%2b86-15673886666&smsCode=234543
    @RequestMapping(value = "/auth/app/login", method = RequestMethod.GET)
    public Token appLogin(@RequestParam(value="appId", required=true) int appId,
                          @RequestParam(value="mobile", required=true) String mobile,
                          @RequestParam(value="secret", required=false) String secret,
                          @RequestParam(value="smsCode", required=true) String smsCode,
                          @RequestParam(value="imgCode", required=false) String imgCode,
                          @RequestParam(value="captchaSession", required=false) String captchaSession,
                          @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,false,appId);//移动端登录接口，不设置cookie，不支持cookie取值
        //支持_src,_spm
        Map<String,String> cookie = UserController.getCookie(headers);
        String source = getCPSStringFromCookie(cookie);
        Token token = authService.login(Platform.mobile,mobile,mobile,secret,smsCode,imgCode,captchaSession,source,true,jwt);
        return token;
    }


    /**
     * 第三方认证加签算法
     * @param platform
     * @param callback
     * @return
     * @throws IDLException
     */
    //http://localhost:8080/auth/app/login?appId=1&mobile=%2b86-15673886666&smsCode=234543
    @RequestMapping(value = "/auth/3rd/sign/request", method = RequestMethod.GET)
    public OAuthRequest signRequest(@RequestParam(value="platform", required=true) String platform,
                                    @RequestParam(value="callback", required=false) String callback) throws IDLException {
        return authService.reqLoginAuth(Platform.valueOf(platform),callback);
    }

    /**
     * 三方登录，通过 signRequest 加签后（微信公众号登录省略），客户端通过第三方应用验证后拿到对方的响应，传给服务端，并创建账号
     * 1、如果账号已经绑定过手机号，则直接返回userToken
     * 2、如果没有绑定过手机，则会返回一个AccountToken，如果仍然需要绑定手机，则需要调用bindLogin方法绑定手机
     *
     * @param from
     * @param oauthResponse
     * @param captcha
     * @param session
     * @param headers
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/auth/3rd/login", method = RequestMethod.GET)
    public Token oauthLogin(@RequestParam(value="appId", required=true) int appId,
                            @RequestParam(value="from", required=true) String from,
                            @RequestParam(value="oauthResponse", required=true) String oauthResponse,
                            @RequestParam(value="captcha", required=false) String captcha,
                            @RequestParam(value="session", required=false) String session,
                            @RequestHeader HttpHeaders headers,
                            HttpServletResponse response) throws IDLException {

        Map<String,String> cookie = UserController.getCookie(headers);
        //支持_src,_spm
        String source = getCPSStringFromCookie(cookie);

        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        Platform platform = Platform.valueOf(from);

        //浏览器特殊处理
        if (jwt == null && (platform == Platform.wechat_mp
                || platform == Platform.wechat_mp_base
                || platform == Platform.wechat_open
                || platform == Platform.wechat_svm
        )) {
            String domain = AuthUtil.getHostDomain(headers);
            //通过user agent生成 deviceToken
            Token deviceToken = generateDeviceToken(headers,App.valueOf(appId),source);
            if (deviceToken != null) {
                AuthUtil.addDeviceToken(response,deviceToken.jwt,deviceToken.did,appId,domain,deviceTokenMaxAge);
            }
            jwt = deviceToken.jwt;
        }

        Token token = authService.oauth(platform,oauthResponse,captcha,session,source,true,jwt);
        return token;
    }

    /**
     * 绑定登录，主要是第三方登录时,没有绑定过手机号场景
     * Head Authorization必须是AccountToken，否则无法登录
     * @param appId
     * @param from
     * @param mobile
     * @param secret
     * @param smsCode
     * @param imgCode
     * @param captchaSession
     * @param headers
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/auth/bind/login", method = RequestMethod.GET)
    public Token bindLogin(@RequestParam(value="appId", required=true) int appId,
                           @RequestParam(value="from", required=true) String from,
                           @RequestParam(value="mobile", required=true) String mobile,
                           @RequestParam(value="secret", required=false) String secret,
                           @RequestParam(value="smsCode", required=true) String smsCode,
                           @RequestParam(value="imgCode", required=false) String imgCode,
                           @RequestParam(value="captchaSession", required=false) String captchaSession,
                           @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        //支持_src,_spm
        Map<String,String> cookie = UserController.getCookie(headers);
        String source = getCPSStringFromCookie(cookie);

        Platform platform = Platform.valueOf(from);
        if (platform == Platform.mobile || platform == Platform.email || platform == null) {
            throw Exceptions.THIRD_PARTY_NOT_SUPPORT_ERROR("绑定时不支持的mobile和email");
        }

        Token token = authService.login(platform,null,secret,mobile,smsCode,imgCode,captchaSession,source,true,jwt);
        return token;
    }

    //spring mvc方式
//    @POST
//    @Path("demo")
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
//    public Result function(@FormParam(value="string1")String string1, @FormParam(value="string2")String string2);

    /**
     * 刷新token,post接口
     * 注意客户端Content-Type: application/x-www-form-urlencoded
     * @param appId
     * @param csrf
     * @param headers
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.POST)
    public Token bindLogin(@RequestParam(value="appId", required=true) int appId, @RequestParam(value="csrf", required=true) String csrf, @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);
        return authService.refresh(jwt,csrf);
    }


    @RequestMapping(value = "/user/{uid}", method = RequestMethod.GET)
    public User getUser(@PathVariable("uid") int uid,
                        @RequestParam(value="appId", required=true) int appId,
                        @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        return userService.queryUser(uid);
    }

    @RequestMapping(value = "/user/account/{accountId}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable("accountId") int accountId,
                              @RequestParam(value="appId", required=true) int appId,
                              @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        return userService.queryAccount(accountId);
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    public boolean updateUser(@RequestBody final User user,
                              @RequestParam(value="appId", required=true) int appId,
                              @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth

        return userService.updateUser(user.id,
                user.nick,
                user.name,
                user.idNumber,
                user.head,
                user.mobile,
                user.email,
                user.gender != null?user.gender.value:null,
                user.grade,
                user.rank,
                user.role);
    }

    @RequestMapping(value = "/user/account", method = RequestMethod.PUT)
    public boolean updateAccount(@RequestBody final Account account,
                                 @RequestParam(value="appId", required=true) int appId,
                                 @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        return userService.updateAccount(account.id,
                account.nick,
                account.head,
                account.gender != null?account.gender.value:null,
                account.mobile,
                account.email);
    }


    /**
     * 获取用户额外的属性值
     * @param uid
     * @param appId
     * @param keys 用逗号隔开多个kay
     * @param headers
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/user/attribute/{uid}", method = RequestMethod.GET)
    public UserAttribute getUserAttributes(@PathVariable("uid") int uid,
                                  @RequestParam(value="appId", required=true) int appId,
                                  @RequestParam(value="keys", required=true) String keys,
                                  @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        String[] names = keys.split(",");
        return userService.userAttribute(uid,names);
    }

    /**
     * 获取账号额外属性值
     * @param accountId
     * @param appId
     * @param keys
     * @param headers
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/user/account/attribute/{accountId}", method = RequestMethod.GET)
    public AccountAttribute getAccountAttributes(@PathVariable("accountId") int accountId,
                                        @RequestParam(value="appId", required=true) int appId,
                                        @RequestParam(value="keys", required=true) String keys,
                                        @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        String[] names = keys.split(",");
        return userService.accountAttribute(accountId,names);
    }

    @RequestMapping(value = "/user/attribute/{uid}/{key}", method = RequestMethod.GET)
    public boolean addUserAttribute(@PathVariable("uid") int uid,
                                    @PathVariable("key") String key,
                                           @RequestParam(value="appId", required=true) int appId,
                                           @RequestParam(value="value", required=true) String value,
                                           @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        return userService.addUserAttribute(uid,key,value);
    }

    @RequestMapping(value = "/user/account/attribute/{accountId}/{key}", method = RequestMethod.GET)
    public boolean addAccountAttribute(@PathVariable("accountId") int accountId,
                                    @PathVariable("key") String key,
                                    @RequestParam(value="appId", required=true) int appId,
                                    @RequestParam(value="value", required=true) String value,
                                    @RequestHeader HttpHeaders headers) throws IDLException {
        String jwt = AuthUtil.getAuthorization(headers,true,appId);//app和web通用，所以需要从cookie中获取auth
        return userService.addAccountAttribute(accountId,key,value);
    }


    private Token generateDeviceToken(HttpHeaders headers,App appId,String source) throws IDLException {
        //解析ua，直接冲ua中获取信息
        String ua = headers.getFirst("User-Agent");
        // Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36
        // 解析ua: https://blog.csdn.net/laozhaokun/article/details/42024663
        System.out.println("LMJ_UA:[" + ua + "]");
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        //获取系统信息
        OperatingSystem os = userAgent.getOperatingSystem();
        Manufacturer manufacturer = os != null ? os.getManufacturer() : null;

        //浏览器名称
        String browserName = browser.getName();
        String manufacturerName = manufacturer != null ? manufacturer.getName() : browserName;
        String model = browser.getRenderingEngine().getName();
        String brand = browser.getName();
        String device = browser.getManufacturer().getName();
        String osName = os.getName();
        String ip = AuthUtil.getClientIP(headers);

        Token deviceToken = authService.logDevice(appId,manufacturerName,model,brand,device,osName,null,null,null,null,ip,ua, source,true,null);

        return deviceToken;
    }


    private static void setCookie(HttpServletResponse response, String key, String value, int maxAge, boolean httpOnly, boolean secure, String domain) {
        CookieUtil.setCookie(response,key,value,maxAge,httpOnly,secure,domain);
    }

    //获取cookie: __da=-847366894926686; 301_uinfo=15673886363%2C%E6%9C%AA%E7%A1%AE%E8%AE%A4%2Ccms%2C1%2C0
    private static Map<String,String> getCookie(HttpHeaders headers) {
        return CookieUtil.getCookie(headers);
    }

    private String getCPSStringFromCookie(Map<String,String> cookie) {
        if (refCookieKeys == null || refCookieKeys.length() == 0 || cookie == null || cookie.size() == 0) {
            return null;
        }
        String[] keys = refCookieKeys.split(";");
        return CookieUtil.getCPSStringFromCookie(keys,cookie);
    }
}
