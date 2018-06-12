package ssn.lmj.user.controller;

import com.lmj.stone.idl.IDLException;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.entities.*;

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

    public static final String DEVICE_ID_COOKIE_NAME = "__dd";
    public static final String USER_ID_COOKIE_NAME = "__ud";
    public static final String DEVICE_TOKEN_COOKIE_NAME = "__ds";
    public static final String USER_TOKEN_COOKIE_NAME = "__tk";

    @Autowired
    AuthService authService;

    @Value("${user.auth.cps.cookie.keys}")
    String refCookieKeys;

    @Value("${user.auth.device.token.maxAge}")
    int deviceTokenMaxAge;

    @Value("${user.auth.user.token.maxAge}")
    int userTokenMaxAge;



    //http://localhost:8080/auth/device?appId=1&manufacturer=dd&model=xx&brand=iphone&device=iphone4&os=ios8.0
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
        String ip = getClientIP(headers);
        String domain = getHostDomain(headers);

        //支持_src,_spm
        Map<String,String> cookie = UserController.getCookie(headers);
        String source = getCPSStringFromCookie(cookie);

        Token token = authService.logDevice(App.valueOf(appId),manufacturer,model,brand,device,os,idfa,idfv,imei,mac,ip,ua, source,null);

        if (token != null) {
            addDeviceToken(token,response,appId,domain,deviceTokenMaxAge);
        }

        return token;
    }

    // http://localhost:8080/sms/send?mobile=+86-15673886666&type=auth
    @RequestMapping(value = "/sms/send", method = RequestMethod.GET)
    public Captcha sendSMS(@RequestParam(value="mobile", required=true) String mobile,
                           @RequestParam(value="type", required=true) String type,
                           @RequestParam(value="imgCode", required=false) String imgCode,
                           @RequestParam(value="captchaSession", required=false) String captchaSession,
                           @RequestHeader HttpHeaders headers) throws IDLException {
        return authService.sendSMS(mobile, CaptchaType.valueOf(type),imgCode,captchaSession);
    }

    //http://localhost:8080/auth/web/login?appId=1&mobile=+86-15673886666&smsCode=234543
    @RequestMapping(value = "/auth/web/login", method = RequestMethod.GET)
    public Token webLogin(@RequestParam(value="appId", required=true) int appId,
                          @RequestParam(value="mobile", required=true) String mobile,
                          @RequestParam(value="secret", required=false) String secret,
                          @RequestParam(value="smsCode", required=true) String smsCode,
                          @RequestParam(value="imgCode", required=false) String imgCode,
                          @RequestParam(value="captchaSession", required=false) String captchaSession,
                          @RequestHeader HttpHeaders headers,
                          HttpServletResponse response) throws IDLException {

        //支持_src,_spm
        Map<String,String> cookie = UserController.getCookie(headers);
        String source = getCPSStringFromCookie(cookie);
        String domain = getHostDomain(headers);

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
        String ip = getClientIP(headers);

        Token deviceToken = authService.logDevice(App.valueOf(appId),manufacturerName,model,brand,device,osName,null,null,null,null,ip,ua, source,null);
        if (deviceToken != null) {
            addDeviceToken(deviceToken,response,appId,domain,deviceTokenMaxAge);
        }

        Token token = authService.login(Platform.mobile,mobile,secret,smsCode,imgCode,captchaSession,source,true,deviceToken.jwt);
        if (token != null) {
            addUserToken(token,response,appId,domain,userTokenMaxAge);
        }

        return token;
    }

//    @RequestMapping(value="/testRequestParam")
//    public String testRequestParam(@RequestParam(value="username") String username, @RequestParam(value="age", required=false, defaultValue="0") int age){
//        System.out.println("testRequestParam" + " username:" + username + " age:" +age);
//        return null;
//    }
//
//    @RequestMapping(value="/printname/{name}", method= RequestMethod.GET)
//    public String printName(@PathVariable String name,
//                            @RequestHeader HttpHeaders headers) {
//        System.out.println("from request:" + request.getHeader("code"));
//        System.out.println("from parameter:" + headers.getFirst("code"));
//
//        return "hello";
//    }

    private static void addDeviceToken(Token token,HttpServletResponse response,int appId, String domain, int maxAge) {
        if (token != null) {
            if (token.did != null && token.did.length() > 0) {

                /*
                Cookie deviceId_cookie = new Cookie(CommonParameter.cookieDeviceId, context.deviceIdStr);
                deviceId_cookie.setMaxAge(Integer.MAX_VALUE);
                deviceId_cookie.setSecure(false);
                deviceId_cookie.setPath("/");
                */

                setCookie(response,appId + DEVICE_ID_COOKIE_NAME,token.did,0,false,false,domain);
            }

            if (token.jwt != null && token.jwt.length() > 0) {
                setCookie(response,appId + DEVICE_TOKEN_COOKIE_NAME,token.jwt,maxAge,true,false,domain);
            }
        }
    }

    private static void addUserToken(Token token,HttpServletResponse response,int appId, String domain, int maxAge) {
        if (token != null) {
//                Cookie tk_cookie = new Cookie(context.appid + CommonParameter.token, URLEncoder.encode(context.token, "utf-8"));
//                tk_cookie.setMaxAge(-1);
//                tk_cookie.setHttpOnly(true);
//                tk_cookie.setSecure(false);
//                tk_cookie.setPath("/");
//
//                Cookie stk_cookie = new Cookie(context.appid + CommonParameter.stoken,URLEncoder.encode(
//                        context.stoken, "utf-8"));
//                stk_cookie.setMaxAge(1800000);
//                stk_cookie.setHttpOnly(true);
//                stk_cookie.setSecure(true);
//                stk_cookie.setPath("/");

            if (token.did != null && token.did.length() > 0) {
                setCookie(response,appId + DEVICE_ID_COOKIE_NAME,token.did,0,false,false,domain);
            }

            if (token.uid != null && token.uid.length() > 0) {
                setCookie(response,appId + USER_ID_COOKIE_NAME,token.uid,0,false,false,domain);
            }

            if (token.jwt != null && token.jwt.length() > 0) {
                setCookie(response,appId + USER_TOKEN_COOKIE_NAME,token.jwt,maxAge,true,false,domain);
            }
        }
    }

    private static void setCookie(HttpServletResponse response, String key, String value, int maxAge, boolean httpOnly, boolean secure, String domain) {
        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(maxAge <= 0 ? Integer.MAX_VALUE : maxAge); //有效时长 秒
        cookie.setHttpOnly(httpOnly); // 有設定時，Cookie只限被伺服端存取，無法在用戶端讀取。
        cookie.setSecure(secure);     // 有設定時，Cookie只能透過https的方式傳輸。
        cookie.setPath("/");
        cookie.setDomain(domain);

        response.addCookie(cookie);
    }

    //获取cookie: __da=-847366894926686; 301_uinfo=15673886363%2C%E6%9C%AA%E7%A1%AE%E8%AE%A4%2Ccms%2C1%2C0
    private static Map<String,String> getCookie(HttpHeaders headers) {
        List<String> cookies = headers.get("Cookie");//直接取cookie
        Map<String,String> map = new HashMap<String, String>();
        for (String str : cookies) {
            String[] ss = str.split("; ");
            for (String s : ss) {
                int idx = s.indexOf("=");
                if (idx > 0 && idx + 1 < s.length()) {
                    String key = s.substring(0,idx);
                    String value = s.substring(idx+1,s.length());//decode
                    try {
                        value = URLDecoder.decode(value,"utf-8");
                    } catch (Throwable e) {e.printStackTrace();}
                    map.put(key,value);
                }
            }
        }
        return map;
    }

    private String getCPSStringFromCookie(Map<String,String> cookie) {
        if (refCookieKeys == null || refCookieKeys.length() == 0 || cookie == null || cookie.size() == 0) {
            return null;
        }
        String[] keys = refCookieKeys.split(";");
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            String value = cookie.get(key);

            if (value != null && value.length() > 0) {

                if (builder.length() > 0) {
                    builder.append("; ");//分隔符
                }

                builder.append(key);
                builder.append(":");
                builder.append(value);
            }
        }

        if (builder.length() == 0) {
            return null;
        }

        return builder.toString();
    }
    private static String getHostDomain(HttpHeaders headers) {
        String host = headers.getFirst("Host");
        String[] ss = host.split("\\:");//去掉host
        if (ss.length > 0) {
            host = ss[0];
        }

        //判断是否为ip，若为ip则直接返回
        if (isipv4(host)) {
            return host;
        }

        //放到到主域名下
        String[] strs = host.split("\\.");
        if (strs.length <= 2) {
            return host;
        } else {
            return "." + strs[strs.length - 2] + "." + strs[strs.length - 1];
        }
    }

    public static boolean isipv4(String ip){
        //判断是否是一个ip地址
        boolean a=false;
        boolean flag =ip.matches("\\d{1,3}\\.d{1,3}\\.d{1,3}\\.d{1,3}");
        if (flag) {
            String s[] = ip.split("\\.");
            int i1= Integer.parseInt(s[0]);
            int i2= Integer.parseInt(s[1]);
            System.out.println(i2);
            int i3= Integer.parseInt(s[2]);
            int i4= Integer.parseInt(s[3]);
            if(i1>0&&i1<256&&i2<256&&i3<256&&i4<256) {
                a = true;
            }

        }
        return a;
    }


    private static final String HTTP_HEADER_SEPARATE = ", ";
    private static String getClientIP(HttpHeaders headers) {
        String ip = null;
        if (headers != null) {
            ip = headers.getFirst("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = headers.getFirst("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = headers.getFirst("http-x-forwarded-for");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = headers.getFirst("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = headers.getFirst("remote-addr");
            }
            //
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = headers.getFirst("Remote Address");//request.getRemoteAddr();
            }
        }

        if (ip  ==   null   ||  ip.length()  ==   0   ||  "unknown" .equalsIgnoreCase(ip)) {
            return ip;
        }

        //X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
        String[] ips = ip.split(",");
        if (ips != null && ips.length > 0) {
            return ips[0];
        }

        return ip;
    }
}
