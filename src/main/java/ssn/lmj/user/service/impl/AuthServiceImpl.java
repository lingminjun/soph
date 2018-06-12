package ssn.lmj.user.service.impl;

import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.IDLT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssn.lmj.user.db.dao.SAccountDAO;
import ssn.lmj.user.db.dao.SCaptchaDAO;
import ssn.lmj.user.db.dao.SDeviceDAO;
import ssn.lmj.user.db.dao.SUserDAO;
import ssn.lmj.user.db.dobj.SAccountDO;
import ssn.lmj.user.db.dobj.SCaptchaDO;
import ssn.lmj.user.db.dobj.SDeviceDO;
import ssn.lmj.user.db.dobj.SUserDO;
import ssn.lmj.user.db.ds.UserDataSourceConfig;
import ssn.lmj.user.jwt.JWT;
import ssn.lmj.user.jwt.utils.AesHelper;
import ssn.lmj.user.jwt.utils.Base64Util;
import ssn.lmj.user.jwt.utils.MD5;
import ssn.lmj.user.service.Exceptions;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.entities.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-12
 * Time: 上午10:32
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    SAccountDAO accountDAO;

    @Autowired
    SUserDAO userDAO;

    @Autowired
    SDeviceDAO deviceDAO;

    @Autowired
    SCaptchaDAO captchaDAO;

    @Value("${user.auth.ecc.pub.key}")
    String eccPubKey;

    @Value("${user.auth.ecc.pri.key}")
    String eccPriKey;

    @Value("${user.auth.pswd.salt}")
    String pswdSalt;

    @Value("${user.auth.enable.email.create.user}")
    boolean createUserFromEmail;

    @Value("${user.auth.jwt.aging}")
    int userTokenAging = 720;//30 * 24;

    @Value("${user.sms.captcha.aging}")
    int smsAging = 300;//5 * 60;

    private JWT parserJWT(String jwt) {
        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setBinaryEncode(true);
        builder.setCipherText(jwt);
        return builder.build();
    }

    private JWT.Builder genDeviceJWT(App app, long did, String issuKey, int aging, long flag) {
        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setBinaryEncode(true);
        builder.setApplicationId(app.id);
        builder.setDeviceId("" + did);
        builder.setAging(aging);
        builder.setJWTGrant(JWT.Grant.Device);
        builder.setIssuedPublicKey(issuKey);
        builder.setLogFlag(flag);
        return builder;
    }

    private JWT.Builder genUserJWT(App app, long did, String issuKey, SUserDO userDO, SAccountDO accountDO, int aging, long flag) {

        String nick = null;
        if (userDO != null && userDO.nick != null && userDO.nick.length() > 0) {
            nick = userDO.nick;
        } else if (accountDO != null && accountDO.nick != null && accountDO.nick.length() > 0) {
            nick = userDO.nick;
        }

        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setBinaryEncode(true);
        builder.setApplicationId(app.id);
        builder.setDeviceId("" + did);
        builder.setOpenId(accountDO != null ? accountDO.openId : null);
        builder.setUserId(userDO != null ? "" + userDO.id : null);
        builder.setNick(nick);
        builder.setAging(aging);
        if (userDO != null && userDO.id != null && userDO.id != 0) {
            builder.setJWTGrant(JWT.Grant.User);
        } else if (accountDO != null && accountDO.id != null && accountDO.id != 0) {
            builder.setJWTGrant(JWT.Grant.Account);
        } else {
            builder.setJWTGrant(JWT.Grant.OAuth);
        }
        builder.setIssuedPublicKey(issuKey);
        builder.setLogFlag(flag);
        return builder;
    }

    //短信验证码不需要session 0:不存在, 1:正确, -1:正确，但是过期了
    private int captchaSMSCode(String mobile, String smcCode, CaptchaType type, int aging/*秒*/) {
        long now = System.currentTimeMillis();
        List<SCaptchaDO> captchaDOs = captchaDAO.findLatestCaptchaByCode(type.name(), mobile, smcCode,now - 30 * 60 * 1000);
        if (captchaDOs == null || captchaDOs.size() == 0) {
            return 0;
        }

//        if (captchaDOs.size() >= maxTimes) {
//            return -2;
//        }

        //短信验证码的有效期
        if (captchaDOs.get(0).createAt + (aging * 1000) >= now) {
            return 1;
        } else {
            return -1;
        }
    }

    //风控验证码 0:不存在, 1:正确, -1:正确，但是过期了
    private int captchaSafeguardCode(String session, String smcCode, CaptchaType type) {
        long now = System.currentTimeMillis();
        List<SCaptchaDO> captchaDOs = captchaDAO.findLatestCaptchaByCode(type.name(), session, smcCode,now - 30 * 60 * 1000);
        if (captchaDOs == null || captchaDOs.size() == 0) {
            return 0;
        }

        return 1;
    }

    private String passwordHash(String pswd, String salt) {
        if (salt == null || salt.length() == 0) {
            return MD5.md5(pswd + pswdSalt);
        }
        return MD5.md5(pswd + salt);
    }

    @Transactional(value = UserDataSourceConfig.TRANSACTION_MANAGER)
    SUserDO findAndCreateMobileUser(String mobile, String nick, Gender gender, String source) {
        return findAndCreateUser(mobile,null,nick,gender,source);
    }


    SUserDO findAndCreateEmailUser(String email, String nick, Gender gender, String source) {
        return findAndCreateUser(null,email,nick,gender,source);
    }

    @Transactional(value = UserDataSourceConfig.TRANSACTION_MANAGER)
    SUserDO findAndCreateUser(String mobile, String email, String nick, Gender gender, String source) {
        //此处应该加事务锁
        List<SUserDO> list = null;//
        if (mobile != null && mobile.length() > 0) {
            list = userDAO.findUserByMobile(mobile);
        }

        if ((list == null || list.size() == 0) && email != null && email.length() > 0) {
            list = userDAO.findUserByEmail(email);
        }

        if (list != null && list.size() > 0) {//查到对应的用户，注意更新
            SUserDO userDO = list.get(0);
            boolean update = false;

            if (nick != null && !nick.equals(userDO.nick)) {
                userDO.nick = nick;
                update = true;
            }
            if (userDO.gender != gender.value) {
                userDO.gender = gender.value;
                update = true;
            }

            if (update) {
                userDAO.update(userDO);
            }

            return userDO;
        }

        SUserDO userDO = new SUserDO();
        userDO.mobile = mobile;
        userDO.email = email;
        userDO.source = source;
        userDO.nick = nick;
        userDO.gender = gender.value;
        userDAO.insert(userDO);

        return userDO;
    }

    @Transactional(value = UserDataSourceConfig.TRANSACTION_MANAGER)
    SAccountDO findAndCreateAccount(Platform from, String account, String secret, String smsCode, String nick, Gender gender, String source) throws IDLException {
        //先检查账户是否存在
        SAccountDO accountDO = accountDAO.findAccountByAccount(from.toString(),account);
        if (accountDO != null) {

            // 短信验证码登录
            if (smsCode != null && smsCode.length() > 0) {
                int rt = captchaSMSCode(account,smsCode,CaptchaType.auth,smsAging);
                if (rt == 0) {
                    throw Exceptions.NOT_FOUND_CAPTCHA_CODE_ERROR("验证码错误");
                } else if (rt == -1) {
                    throw Exceptions.FOUND_SMSCODE_EXPIRED("验证码过期");
                }

                //设置密码
                if (secret != null) {
                    accountDO.pswd = passwordHash(secret,pswdSalt);
                    accountDO.pswdSalt = pswdSalt;
                }

            } else if (secret != null && secret.length() > 0 && !accountDO.pswd.equals(passwordHash(secret,accountDO.pswdSalt))) {
                throw Exceptions.NOT_FOUND_ACCOUNT_ERROR("密码错误");
            }

            boolean update = false;

            if (nick != null && !nick.equals(accountDO.nick)) {
                accountDO.nick = nick;
                update = true;
            }
            if (accountDO.gender != gender.value) {
                accountDO.gender = gender.value;
                update = true;
            }

            if (update) {
                accountDAO.update(accountDO);
            }

            return accountDO;

        } else {

            // 短信验证码登录
            if (smsCode != null && smsCode.length() > 0) {
                int rt = captchaSMSCode(account,smsCode,CaptchaType.auth,smsAging);
                if (rt == 0) {
                    throw Exceptions.NOT_FOUND_CAPTCHA_CODE_ERROR("验证码错误");
                } else if (rt == -1) {
                    throw Exceptions.FOUND_SMSCODE_EXPIRED("验证码过期");
                }
            }

            accountDO = new SAccountDO();
            accountDO.platform = from.name();
            accountDO.openId = account;
            accountDO.source = source;
            accountDO.nick = nick;
            accountDO.gender = gender.value;

            //设置密码
            if (secret != null) {
                accountDO.pswd = passwordHash(secret,pswdSalt);
                accountDO.pswdSalt = pswdSalt;
            }

            accountDAO.insert(accountDO);

            return accountDO;
        }
    }

    @Override
    public Token logDevice(App appId, String manufacturer, String model, String brand, String device, String os, String idfa, String idfv, String imei, String mac, String cip, String ua, String source, String jwt) throws IDLException {

        JWT old = null;
        if (jwt != null && jwt.length() > 0) {
            old = parserJWT(jwt);
        }

        long did = old != null ? IDLT.longInteger(old.body.did) : JWT.genDID();
        if (did == 0) {
            did = JWT.genDID();
        }

//        byte[][] issuKeys = EccHelper.randomKey(0);//颁发Ecc公私秘钥对，
        String issuKey = old != null ? old.body.key : Base64Util.encodeToString(AesHelper.randomKey(0));
        if (issuKey == null || issuKey.length() == 0) {
            issuKey = Base64Util.encodeToString(AesHelper.randomKey(0));//颁发Aes秘钥即可
        }

        Token token = new Token();
        token.did = "" + did;
        token.exp = 0;
        token.typ = TokenType.deviceAuth;
        token.csrf = issuKey;//颁发私钥

        //记录设备
        SDeviceDO deviceDO = new SDeviceDO();
        deviceDO.did = did;
        deviceDO.aid = appId.id;
        deviceDO.manufacturer = manufacturer; // 设备制造商
        deviceDO.model = model; // 设备型号、浏览器则内核型号
        deviceDO.brand = brand; // 品牌名
        deviceDO.device = device; // 设备名称
        deviceDO.os = os; // 设备系统版本
        deviceDO.idfa = idfa; // iOS 广告追踪id
        deviceDO.idfv = idfv; // iOS 广告追踪id 替代方案
        deviceDO.imei = imei; // imei or meid
        deviceDO.mac = mac; // mac地址
        deviceDO.cip = cip; // 客户端id
        deviceDO.ua = ua; // 客户端user agent
        deviceDO.source = source;

        long id = deviceDAO.insert(deviceDO); //插入新的日志

        token.jwt = genDeviceJWT(appId,did,issuKey,0,id).buildCipher();

        return token;
    }

    @Override
    public Token login(Platform from, String account, String secret, String smsCode, String captcha, String session, String source, boolean user, String jwt) throws IDLException {

        //先验证图片验证码
        if (captcha != null && captcha.length() > 0) {
            if (captchaSafeguardCode(session,captcha,CaptchaType.safeguard) != 1) {
                throw Exceptions.NOT_FOUND_CAPTCHA_CODE_ERROR("验证码错误");
            }
        }

        //验证jwt是否ok
        JWT djwt = parserJWT(jwt);
        if (djwt == null) {
            throw Exceptions.NO_PERMISSION_ERROR("缺少设备注册TOKEN");
        }

        App appId = App.valueOf(IDLT.integer(djwt.body.aid));
        long did = IDLT.longInteger(djwt.body.did);
        if (did == 0) {
            throw Exceptions.NO_PERMISSION_ERROR("缺少设备注册TOKEN");
        }

        String issuKey = Base64Util.encodeToString(AesHelper.randomKey(0));

        SAccountDO accountDO = findAndCreateAccount(from,account,secret,smsCode,null,Gender.unknown,source);
        if (accountDO == null) {
            throw Exceptions.NOT_CREATE_USER_ERROR("未知原因");
        }

        Token tk = new Token();
        tk.did = djwt.body.did;
        tk.csrf = issuKey;//颁发私钥

        // 继续寻找或产生用户
        if (user) {
            if (accountDO.uid != null && accountDO.uid != 0) {
                SUserDO userDO = userDAO.getById(accountDO.uid);
                if (userDO != null) {
                    JWT.Builder builder = genUserJWT(appId, did, issuKey, userDO, accountDO, userTokenAging, djwt.body.log);
                    JWT jt = builder.build();
                    tk.exp = jt.head.exp;
                    tk.typ = TokenType.userAuth;
                    tk.uid = "" + userDO.id;
                    tk.jwt = builder.buildCipher();
                    return tk;
                }
            } else if (from == Platform.mobile || (createUserFromEmail && from == Platform.email)) {//手机号可以直接生成用户
                SUserDO userDO = from == Platform.mobile ?
                        findAndCreateMobileUser(account, null, Gender.unknown, source) :
                        findAndCreateEmailUser(account, null, Gender.unknown, source);
                if (userDO != null) {

                    accountDO.uid = userDO.id;
                    accountDAO.update(accountDO);//绑定账号非事务

                    JWT.Builder builder = genUserJWT(appId, did, issuKey, userDO, accountDO, userTokenAging, djwt.body.log);
                    JWT jt = builder.build();
                    tk.exp = jt.head.exp;
                    tk.typ = TokenType.userAuth;
                    tk.uid = "" + userDO.id;
                    tk.jwt = builder.buildCipher();
                    return tk;
                }
            }
        }

        // 只需认证到Account级别(业务可以继续生成用)
        JWT.Builder builder = genUserJWT(appId,did,issuKey,null,accountDO,userTokenAging,djwt.body.log);
        JWT jt = builder.build();
        tk.exp = jt.head.exp;
        tk.typ = TokenType.accountAuth;
        tk.uid = "" + accountDO.id;
        tk.jwt = builder.buildCipher();
        return tk;
    }


    @Override
    public Token refresh(String jwt) throws IDLException {

        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setBinaryEncode(true);
        builder.setCipherText(jwt);
        JWT old = builder.build();

        long age = old.head.exp - old.head.iat;//获取有效时长

        JWT.Builder builder1 = new JWT.Builder();
        builder1.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder1.setEncryptKey(eccPubKey,eccPriKey);
        builder1.setJWTGrant(old.head.grt);
        builder1.setAging(age);
        builder1.setIssue(old.head.iss);
        builder1.setUserId(old.body.uid);
        builder1.setApplicationId(old.body.aid);
        builder1.setDeviceId(old.body.did);
        builder1.setOpenId(old.body.oid);
        builder1.setPartnerId(old.body.pid);
        builder1.setLogFlag(old.body.log);
        builder1.setIssuedPublicKey(old.body.key);
        builder1.setNick(old.body.nk);
        builder1.setSignatureAlgorithm(old.sign.stp);

        //refresh签名秘钥，服务端保留
//        builder1.setSignSalt(sign_slat);
        JWT newJWT = builder1.build();

        Token token = new Token();
        token.did = old.body.did;
        token.uid = old.body.uid;
        token.exp = newJWT.head.exp;
        token.jwt = newJWT.toBinaryCipher(eccPubKey);
//        token.csrf = old.body.key; //无法确定是否为秘钥对，刷新不能返回csrf
        token.typ = TokenType.extend;

        return token;
    }

    @Override
    public boolean logout(String jwt) throws IDLException {
        //TODO : FIXME 消除推送通道，如apns ，
        return true;
    }

    @Override
    public Token oauth(Platform from, String oauthResponse, String captcha, String session, String source, boolean user, String jwt) throws IDLException {
        /*
        //String reqParams = URLDecoder.decode(authResp, "UTF-8");
        Map<String, String> paras = CommonUtil.getMapFromReq(authResp);
        if (paras == null) {
            return null;
        }

        //feature 记录cookie信息
        String feature = commonFacade.genKvStringFromMap(cookie);

        // 这里还需要一部state的校验
        //获取accessToken
        class UserPartnerDTO implements Serializable {
            private long id;
            private String partnerId;
            private String appid;
            private String appkey;
            private String connectUrl;
            private String apiUrl;
            private int status;
            private String service;
            private String targetService;
            private String verifyUrl;
            private String charset;
            private String signType;
            private String selfPrikey;
            private String servicePubkey;
            private String pid;
        }
        UserPartnerDTO partner = userCacheFacade.getUserPartnerInfo(partnerId.toString());
        if (partner != null) {
            boolean hasCoupon = false;
            UserPartnerBindDTO bindDTO = new UserPartnerBindDTO();
            if (partnerId == PartnerId.alipay_app ) {
                bindDTO = partnerLoginFacade.alipayAuthLoginByUid(partner, paras, appId);
                if (bindDTO != null && bindDTO.getUserId() <= 0 ) {
                    String bindMobile = bindDTO.getMobile();
                    hasCoupon = userFacade.alipayNewAccountLogin(bindDTO, bindMobile, partnerId.toString(), feature, appId, deviceId); //支付宝联合登录
                }
            } else if (partnerId == PartnerId.wechat_app || partnerId == PartnerId.wechat_mina) {
                if (paras.get("code") == null || paras.get("code").isEmpty()) {
                    throw new ServiceException(UserServiceHttpCode.USER_NOT_AUTH);
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(ptnAppid) && !partner.getAppid().equals(ptnAppid)) {
                    commonFacade.putByPtnAppid(partner, partnerId, ptnAppid);
                }
                bindDTO = partnerLoginFacade.wxLogin(partner, paras.get("code"), appId);
            }
            LoginResp loginResp = coverAppPartnerBindResult(bindDTO, partnerId, appId, deviceId, deviceToken, clientIp, hasCoupon);
            logger.info("[app第三方登录]resp={}", JSON.toJSONString(loginResp));
            return loginResp;
        }*/
        return null;
    }

    @Override
    public OAuthRequst reqLoginAuth(Platform platform, String callback, App ptnAppid) throws IDLException {
        /*
        OAuthRequst result = new OAuthRequst();
        String ptnId = partnerId == PartnerId.wechat_mp_base ? PartnerId.wechat_mp.toString() : partnerId.toString();
        UserPartnerDTO partner = userCacheFacade.getUserPartnerInfo(ptnId);

        String url = redirectUrl == null ? "" : URLEncoder.encode(redirectUrl, "UTF-8");
        if (partner != null) {
            if (partnerId == PartnerId.wechat_open) {
                result.loginAuthLink = partner.getConnectUrl() + "?appid=" + partner.getAppid() + "&redirect_uri=" +
                        url + "&response_type=code&scope=snsapi_login&state=" + "STATE" + "#wechat_redirect";
            } else if (partnerId == PartnerId.wechat_svm || partnerId == PartnerId.wechat_svm_gxs) {
                result.loginAuthLink = partner.getConnectUrl() + "?appid=" + partner.getAppid() + "&redirect_uri=" +
                        url + "&response_type=code&scope=snsapi_userinfo&state=" + "STATE" + "#wechat_redirect";
            } else if (partnerId == PartnerId.alipay_qklg) {
                result.loginAuthLink = partnerLoginFacade.buildAlipayQklgUrl(partner, redirectUrl);
            } else if (partnerId == PartnerId.alipay_fuwu) {
                result.loginAuthLink = partner.getConnectUrl() + "?app_id=" + partner.getAppid() + "&redirect_uri=" +
                        url + "&auth_skip=true&scope=auth_userinfo";
            } else if (partnerId == PartnerId.alipay_app) {
                result.loginAuthLink = partnerLoginFacade.buildAliAuthEntity(partner);
            } else if (partnerId == PartnerId.wechat_mp_base) {
                result.loginAuthLink = partner.getConnectUrl() + "?appid=" + partner.getAppid() + "&redirect_uri=" +
                        url + "&response_type=code&scope=snsapi_base&state=" + "STATE" + "#wechat_redirect";
            } else if (partnerId == PartnerId.qq_app) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(ptnAppid) && !partner.getAppid().equals(ptnAppid)) {
                    commonFacade.putByPtnAppid(partner, partnerId, ptnAppid);
                }
                result.loginAuthLink = partner.getConnectUrl() + "?response_type=code&client_id=" + partner.getAppid() +
                        "&redirect_uri=" + url + "&state=" + Md5Util.computeToHex(String.valueOf(System.currentTimeMillis()).getBytes())
                        + "&scope=get_user_info";
            }
        }

        return result;
        */
        return null;
    }

    @Override
    public Captcha sendSMS(String mobile, CaptchaType type, String captcha, String session) throws IDLException {
        Captcha code = new Captcha();

        String sn = MD5.md5(mobile + type.name() + System.currentTimeMillis());
        code.session = sn;
        code.type = type;

        int random = (int)(Math.random()*1000000);//取小数位，具有不可预测性

        SCaptchaDO captchaDO = new SCaptchaDO();
        captchaDO.code = "" + random;
        captchaDO.session = mobile; //手机号发短信验证码，不需要特意设置session
        captchaDO.type = type.name();
        captchaDO.cmmt = "[验证码] " + random + " ，请妥善保管，注意不要向外泄露";
        captchaDO.aging = 15 * 60;
        captchaDO.account = mobile;

        captchaDAO.insert(captchaDO);

        return code;
    }
}
