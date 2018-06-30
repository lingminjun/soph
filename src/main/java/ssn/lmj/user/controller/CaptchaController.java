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
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.CaptchaService;
import ssn.lmj.user.service.Exceptions;
import ssn.lmj.user.service.entities.*;
import ssn.lmj.user.service.impl.PhoneNumUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-13
 * Time: 下午12:01
 */
@RestController
public class CaptchaController {

    @Autowired
    CaptchaService captchaService;

    /**
     * 下发验证码
     * // http://localhost:8080/sms/send?mobile=%2b86-15673886666&type=auth
     * @param mobile
     * @param type
     * @param imgCode         风控图片验证码
     * @param captchaSession  风控图片验证码session
     * @param headers
     * @return
     * @throws IDLException
     */
    @RequestMapping(value = "/sms/send", method = RequestMethod.GET)
    public Captcha sendSMS(@RequestParam(value="mobile", required=true) String mobile,
                           @RequestParam(value="type", required=true) String type,
                           @RequestParam(value="imgCode", required=false) String imgCode,
                           @RequestParam(value="captchaSession", required=false) String captchaSession,
                           @RequestHeader HttpHeaders headers) throws IDLException {
        if (!PhoneNumUtil.checkMobileFormat(mobile)) {
            throw Exceptions.MOBILE_FORMAT_ERROR("手机号格式错误");
        }
        return captchaService.sendSMS(mobile, CaptchaType.valueOf(type),imgCode,captchaSession);
    }

}
