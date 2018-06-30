package ssn.lmj.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmj.stone.core.algorithm.AesHelper;
import com.lmj.stone.core.algorithm.Base64Util;
import com.lmj.stone.core.algorithm.MD5;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.IDLT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssn.lmj.user.db.dao.*;
import ssn.lmj.user.db.dobj.*;
import ssn.lmj.user.db.ds.UserDataSourceConfig;
import ssn.lmj.user.jwt.JWT;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.CaptchaService;
import ssn.lmj.user.service.Exceptions;
import ssn.lmj.user.service.entities.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-12
 * Time: 上午10:32
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Autowired
    CaptchaDAO captchaDAO;

    @Value("${user.auth.jwt.aging}")
    int userTokenAging = 720;//30 * 24;

    @Value("${user.sms.captcha.aging}")
    int smsAging = 300;//5 * 60;

    static final int maxTimes = 5;

    private String randomCode() {
        String random = "" + (int)(Math.random()*1000000);//取小数位，具有不可预测性
        while (random.length() < 6) {
            random = "0" + random;
        }
        return random;
    }

    @Override
    public Captcha sendSMS(String mobile, CaptchaType type, String captcha, String session) throws IDLException {
        Captcha code = new Captcha();

        String sn = MD5.md5(mobile + type.name() + System.currentTimeMillis());
        code.session = sn;
        code.type = type;

        String random = randomCode();

        CaptchaDO captchaDO = new CaptchaDO();
        captchaDO.code = "" + random;
        captchaDO.session = mobile; //手机号发短信验证码，不需要特意设置session
        captchaDO.type = type.name();
        captchaDO.cmmt = "[验证码] " + random + " ，请妥善保管，注意不要向外泄露";
        captchaDO.aging = 15 * 60;
        captchaDO.account = mobile;

        captchaDAO.insert(captchaDO);

        return code;
    }

    @Override
    public Captcha prayImageCode(String account,CaptchaType type) throws IDLException {
        Captcha code = new Captcha();

        String random = randomCode();

        String sn = MD5.md5("图片验证码固定值" + account + type.name() + System.currentTimeMillis() + random);
        code.session = sn;
        code.type = type;

        CaptchaDO captchaDO = new CaptchaDO();
        captchaDO.code = "" + random;
        captchaDO.session = sn; //手机号发短信验证码，不需要特意设置session
        captchaDO.type = type.name();
        captchaDO.cmmt = "[验证码] " + random + " ，请妥善保管，注意不要向外泄露";
        captchaDO.aging = 15 * 60;
        captchaDO.account = account;

        captchaDAO.insert(captchaDO);

        return code;
    }

    @Override
    public CaptchaEvaluation evalSMSCode(String mobile, String smcCode, CaptchaType type, int aging) {
        long now = System.currentTimeMillis();
        List<CaptchaDO> captchaDOs = captchaDAO.findLatestCaptchaByCode(type.name(), mobile, smcCode,now - 30 * 60 * 1000);
        if (captchaDOs == null || captchaDOs.size() == 0) {
            return CaptchaEvaluation.danger;
        }

        if (captchaDOs.size() >= maxTimes) {
            return CaptchaEvaluation.uneasy;
        }

        //短信验证码的有效期
        if (captchaDOs.get(0).createAt + (aging * 1000) >= now) {
            return CaptchaEvaluation.safety;
        } else {
            return CaptchaEvaluation.risk;
        }
    }

    @Override
    public CaptchaEvaluation evalSafeguardCode(String session, String smcCode, CaptchaType type) {
        long now = System.currentTimeMillis();
        List<CaptchaDO> captchaDOs = captchaDAO.findLatestCaptchaByCode(type.name(), session, smcCode,now - 30 * 60 * 1000);
        if (captchaDOs == null || captchaDOs.size() == 0) {
            return CaptchaEvaluation.danger;
        }

        return CaptchaEvaluation.safety;
    }
}
