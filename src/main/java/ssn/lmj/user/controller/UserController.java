package ssn.lmj.user.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.entities.Token;

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
            @RequestHeader HttpHeaders headers
    ) throws IDLException {
        String ua = headers.getFirst("User-Agent");
        String ip = getClientIP(headers);

        Token token = authService.logDevice(appId,manufacturer,model,brand,device,os,idfa,idfv,imei,mac,ip,ua);

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

    private static final String HTTP_HEADER_SEPARATE = ", ";
    public static String getClientIP(HttpHeaders headers) {
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
