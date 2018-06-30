package ssn.lmj.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmj.stone.core.algorithm.AesHelper;
import com.lmj.stone.core.algorithm.Base64Util;
import com.lmj.stone.core.algorithm.MD5;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.IDLT;
import com.lmj.stone.idl.annotation.IDLDesc;
import com.lmj.stone.utils.AuthUtil;
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
import ssn.lmj.user.service.CaptchaService;
import ssn.lmj.user.service.Exceptions;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.entities.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    OauthPartnerDAO oauthPartnerDAO;

    @Autowired
    DeviceDAO deviceDAO;

    @Autowired
    CaptchaDAO captchaDAO;

    @Autowired
    CaptchaService captchaService;

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

    private JWT.Builder genUserJWT(App app, long did, String issuKey, UserDO userDO, AccountDO accountDO, int aging, long flag) {

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
        builder.setAccountId(accountDO != null ? "" + accountDO.id : null);
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

    private String passwordHash(String pswd, String salt) {
        if (salt == null || salt.length() == 0) {
            return MD5.md5(pswd + pswdSalt);
        }
        return MD5.md5(pswd + salt);
    }

    private Token createAccountToken(AccountDO accountDO,JWT djwt,String issuKey,long did) {
        if (accountDO == null) {
            return null;
        }

        App appId = App.valueOf(IDLT.integer(djwt.body.aid));

        Token tk = new Token();
        tk.did = djwt.body.did;
        tk.csrf = issuKey;//颁发私钥
        // 只需认证到Account级别(业务可以继续生成用)
        JWT.Builder builder = genUserJWT(appId,did,issuKey,null,accountDO,userTokenAging,djwt.body.log);
        JWT jt = builder.build();
        tk.exp = jt.head.exp;
        tk.typ = TokenType.accountAuth;
        tk.acnt = "" + accountDO.id;
        tk.jwt = builder.buildCipher();
        tk.isSignup = accountDO.createAt == null;
        return tk;
    }

    private Token createUserToken(AccountDO accountDO,UserDO userDO,JWT djwt,String issuKey,long did) {
        if (userDO == null) {
            return null;
        }

        App appId = App.valueOf(IDLT.integer(djwt.body.aid));

        Token tk = new Token();
        tk.did = djwt.body.did;
        tk.csrf = issuKey;//颁发私钥
        // 只需认证到Account级别(业务可以继续生成用)
        JWT.Builder builder = genUserJWT(appId,did,issuKey,userDO,null,userTokenAging,djwt.body.log);
        JWT jt = builder.build();
        tk.exp = jt.head.exp;
        tk.typ = TokenType.userAuth;
        tk.acnt = "" + accountDO.id;
        tk.uid = "" + userDO.id;
        tk.jwt = builder.buildCipher();
        tk.isSignup = userDO.createAt == null;//create时这个字段将没有值
        return tk;
    }

    @Transactional(value = UserDataSourceConfig.TRANSACTION_MANAGER)
    UserDO findAndCreateUser(AccountDO account, String source) {
        if (account.id == null) {
            return null;
        }

        /*
        if (!account.platform.equals(Platform.mobile.name())
                && (!createUserFromEmail || !account.platform.equals(Platform.email.name()))) {
            return null;
        }*/

        //此处应该加事务锁
        List<UserDO> list = null;//
        if (account.platform.equals(Platform.mobile.name()) && account.openId != null && account.openId.length() > 0) {
            list = userDAO.findUserByMobile(account.openId);
        } else {
            list = userDAO.findUserByEmail(account.openId);
        }

        UserDO userDO = null;
        if (list != null && list.size() > 0) {//查到对应的用户，注意更新
            userDO = list.get(0);
            userDO.joinFrom = "" + account.id;
            userDO.source = source;
        } else {

            userDO = new UserDO();
        }

        //尽量补全数据
        if (userDO.mobile == null) {
            userDO.mobile = account.mobile;
        }
        if (userDO.email == null) {
            userDO.email = account.email;
        }
        if (userDO.nick == null) {
            userDO.nick = account.nick;
        }
        if (userDO.gender == null) {
            userDO.gender = account.gender;
        }
        if (userDO.head == null) {
            userDO.head = account.head;
        }
        userDAO.insertOrUpdate(userDO);

        return userDO;
    }

    @Transactional(value = UserDataSourceConfig.TRANSACTION_MANAGER)
    AccountDO findAndCreateAccount(Platform from, String account,long uid, String secret, String nick, String head, Gender gender, String mobile, String email, String union, String unionId,String source) throws IDLException {
        //先检查账户是否存在
        AccountDO accountDO = accountDAO.findAccountByAccount(from.name(),account);

        //创建新的账号
        if (accountDO == null) {
            accountDO = new AccountDO();
            accountDO.platform = from.name();
            accountDO.openId = account;

            //只有创建的时候才设置
            accountDO.source = source;

            //可以直接从账号上设置的数据
            if (Platform.mobile == from) {
                accountDO.mobile = account;
            } else if (Platform.email == from) {
                accountDO.email = account;
            }
        }

        //以下是尽可能填充信息
        if (accountDO.nick == null) {
            accountDO.nick = nick;
        }

        if (accountDO.head == null) {
            accountDO.head = head;
        }

        if (accountDO.gender == null && gender != null) {
            accountDO.gender = gender.value;
        }

        //设置密码
        if (accountDO.pswd == null &&  secret != null) {
            accountDO.pswd = passwordHash(secret,pswdSalt);
            accountDO.pswdSalt = pswdSalt;
        }

        //绑定到用户
        if (accountDO.uid == null && uid > 0) {
            accountDO.uid = uid;
        }

        if (accountDO.mobile == null) {
            accountDO.mobile = mobile;
        }

        if (accountDO.email == null) {
            accountDO.email = email;
        }

        if (accountDO.unionId == null && union != null && unionId != null) {
            accountDO.union = union;
            accountDO.unionId = unionId;
        }

        accountDAO.insertOrUpdate(accountDO);

        return accountDO;
    }

    @Override
    public Token logDevice(App appId, String manufacturer, String model, String brand, String device, String os, String idfa, String idfv, String imei, String mac, String cip, String ua, String source, boolean browser, String jwt) throws IDLException {

        JWT old = null;
        if (jwt != null && jwt.length() > 0) {
            old = parserJWT(jwt);
        }

        long did = old != null ? IDLT.longInteger(old.body.did) : JWT.genDID();
        if (did == 0) {
            did = JWT.genDID();
        }
        if (browser) {//浏览器一律将did标识成负数
            did = - did;
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
        DeviceDO deviceDO = new DeviceDO();
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

    /**
     * 用于各种登录场景
     * 1、账号和密码登录：from,account,secret 【必填】
     * 2、手机号验证登录：from,mobile,smsCode 【必填】
     * 3、绑定手机并登录：from,account,mobile,smsCode 【必填】
     * 4、设置密码并登录：from,account,secret,mobile,smsCode 【必填】
     */
    @Override
    public Token login(Platform from, String account, String secret, String mobile, String smsCode, String captcha, String session, String source, boolean user, String jwt) throws IDLException {

        //1、先验证图片验证码
        if (captcha != null && captcha.length() > 0) {
            if (captchaService.evalSafeguardCode(session,captcha,CaptchaType.safeguard) != CaptchaEvaluation.safety) {
                throw Exceptions.NOT_FOUND_CAPTCHA_CODE_ERROR("验证码错误");
            }
        }

        //2、验证jwt是否ok
        JWT djwt = parserJWT(jwt);
        if (djwt == null || !djwt.head.grt.eval(JWT.Grant.Device)) {
            throw Exceptions.NO_PERMISSION_ERROR("缺少设备注册TOKEN");
        }

        long did = IDLT.longInteger(djwt.body.did);
        if (did == 0) {
            throw Exceptions.NO_PERMISSION_ERROR("缺少设备注册TOKEN");
        }

        String issuKey = Base64Util.encodeToString(AesHelper.randomKey(0));

        //3、验证手机验证码：可登录，可设置密码，可绑定账号
        boolean verifySMS = false;
        if (smsCode != null) {
            CaptchaEvaluation rt = captchaService.evalSMSCode(mobile,smsCode,CaptchaType.auth,smsAging);
            if (rt == CaptchaEvaluation.danger) {
                throw Exceptions.NOT_FOUND_CAPTCHA_CODE_ERROR("验证码错误");
            } else if (rt == CaptchaEvaluation.risk) {
                throw Exceptions.FOUND_SMSCODE_EXPIRED("验证码过期");
            }
            verifySMS = true;
        }

        //4、账号密码登录：可登录，可绑定手机
        AccountDO accountDO = null;
        if (!verifySMS && account != null && secret != null) {
            accountDO = accountDAO.findAccountByAccount(from.name(),account);
            if (accountDO != null && !accountDO.pswd.equals(passwordHash(secret,accountDO.pswdSalt))) {
                throw Exceptions.NOT_FOUND_ACCOUNT_ERROR("密码错误");
            }
        } else if (verifySMS && account != null && from != Platform.mobile) {//4.1 账号+手机号验证登录的一种情况，如使用qq号+手机号验证登录
            accountDO = accountDAO.findAccountByAccount(from.name(),account);
            if (accountDO != null && !mobile.equals(accountDO.mobile)) {
                throw Exceptions.ACCOUNT_MOBILE_MATCH_ERROR("手机不一致");
            }
        }

        //没有验证通过的，则不做任何处理
        if (!verifySMS && accountDO == null) {
            return null;
        }

        //5、取绑定账号信息，必须从jwt中获取，且当前是account的，一般表明是第三方的账号
        if (djwt.head.grt == JWT.Grant.Account && accountDO == null) {
            if (djwt.body.cnt != null && djwt.body.cnt.length() > 0) {//直接通过账号id获取
                accountDO = accountDAO.getById(IDLT.longInteger(djwt.body.cnt));
            } else if (djwt.body.oid != null) {
                accountDO = accountDAO.findAccountByAccount(from.name(), djwt.body.oid);
            }
        }

        //6、创建mobile账号，a、手机号+密码登录得到；b、手机验证码验证通过得到，只有这两个场景是允许的
        AccountDO mobileAccountDO = null;
        if (mobile != null) {
            //是否前面手机号加密码已经登录了
            if (mobile.equals(account) && accountDO != null && Platform.valueOf(accountDO.platform) == Platform.mobile) {
                mobileAccountDO = accountDO;
            } else if (verifySMS) {//只要短信验证通过了，就可以创建手机号账户
                String nick = accountDO != null ? accountDO.nick : null;
                String head = accountDO != null ? accountDO.head : null;
                Gender gender = accountDO != null && accountDO.gender != null ? Gender.valueOf(accountDO.gender) : null;
                String tempMobile = accountDO != null ? accountDO.mobile : null;
                String tempEmail = accountDO != null ? accountDO.email : null;
                long tempUid = accountDO != null && accountDO.uid != null ? accountDO.uid : -1;
                mobileAccountDO = findAndCreateAccount(Platform.mobile, mobile, tempUid, secret, nick, head, gender, tempMobile, tempEmail,null,null, source);
            }
        }

        //7、给账号设置密码+绑定手机，方便日后账号+密码登录或手机验证登录；注意此处 不做重置，若需要重置密码，则走重置密码接口
        if (verifySMS && accountDO != null) {
            if (secret != null && accountDO.pswd == null) {
                accountDO.pswd = passwordHash(secret, pswdSalt);
                accountDO.pswdSalt = pswdSalt;
            }
            if (mobile != null && accountDO.mobile == null) {
                accountDO.mobile = mobile;
            }
            accountDAO.update(accountDO);
        }

        //针对只需要返回Account账号的情况应该直接返回Account
        Token accountToken = createAccountToken(accountDO != null ? accountDO : mobileAccountDO, djwt,issuKey,did);
        if (!user) {
            return accountToken;
        }

        //8、手机号是不是已经绑过用户，先看看用户是不是被绑定冲突了  (不做账号合并)
        if (accountDO != null && mobileAccountDO != null && accountDO != mobileAccountDO
                && accountDO.uid != null && mobileAccountDO.uid != null && accountDO.uid !=  mobileAccountDO.uid) {
            throw Exceptions.MOBILE_ACCOUNT_EXIST_USER_ERROR("手机号已经被占用");
        }

        //9、找用户
        long uid = 0;
        if (accountDO != null && accountDO.uid != null) {
            uid = accountDO.uid;
        } else if (mobileAccountDO != null && mobileAccountDO.uid != null) {
            uid = mobileAccountDO.uid;
        }
        UserDO userDO = null;
        if (uid > 0) {
            userDO = userDAO.getById(uid);
        } else if (mobileAccountDO != null) {//从手机账号创建用户
            userDO = findAndCreateUser(mobileAccountDO,source);
        } else if (createUserFromEmail && accountDO != null && accountDO.platform.equals(Platform.email)) {//从邮箱创建用户
            userDO = findAndCreateUser(accountDO,source);
        }
//        else if () {} //如果需要，可以拓展其他账号生成用户

        //10、账号全部绑定到用户
        if (userDO != null) {
            if (accountDO != null && accountDO.uid == null) {
                accountDO.uid = userDO.id;
                accountDAO.update(accountDO);//暂时不考虑性能问题（此处存在更新多个字段问题）
            } else if (mobileAccountDO != null && mobileAccountDO.uid == null) {
                mobileAccountDO.uid = userDO.id;
                accountDAO.update(mobileAccountDO);//暂时不考虑性能问题（此处存在更新多个字段问题）
            }
        }

        //11、存在union_id的全部绑定到同一个uid
        if (userDO != null && accountDO != null && accountDO.unionId != null && accountDO.union != null) {
            List<AccountDO> list = accountDAO.queryByUnionAndUnionId(accountDO.union,accountDO.unionId,0,null,false,0,1000);

            for (AccountDO dobj : list) {
                if (dobj.id == accountDO.id) {continue;}
                if (dobj.unionId == null) {
                    dobj.union = accountDO.union;
                    dobj.unionId = accountDO.unionId;
                    accountDAO.update(dobj);//不能批量update 暂时不考虑性能问题（此处存在更新多个字段问题）
                }
            }
        }

        Token userToken = createUserToken(accountDO != null?accountDO:mobileAccountDO,userDO,djwt,issuKey,did);
        if (userToken != null) {
            return userToken;
        }
        return accountToken;
    }



    @Override
    public Token refresh(String jwt, String csrf) throws IDLException {

        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setBinaryEncode(true);
        builder.setCipherText(jwt);
        JWT old = builder.build();

        //校验有效性
        if (!csrf.equals(old.body.key)) {
            throw Exceptions.REFRESH_TOKEN_ERROR("手机号已经被占用");
        }

        long age = old.head.exp - old.head.iat;//获取有效时长

        JWT.Builder builder1 = new JWT.Builder();
        builder1.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder1.setEncryptKey(eccPubKey,eccPriKey);
        builder1.setJWTGrant(old.head.grt);
        builder1.setAging(age);
        builder1.setIssue(old.head.iss);
        builder1.setUserId(old.body.uid);
        builder1.setAccountId(old.body.cnt);
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
        token.acnt = old.body.cnt;
        token.exp = newJWT.head.exp;
        token.jwt = newJWT.toBinaryCipher(eccPubKey);

        //理论应该刷新，更换秘钥对
//        token.csrf = old.body.key; //无法确定是否为秘钥对，刷新不能返回csrf

        token.typ = TokenType.extend;

        return token;
    }

    @Override
    public boolean logout(String jwt) throws IDLException {
        //TODO : FIXME 消除推送通道，如apns ，
        return true;
    }

    /**
     * 第三方验证登录
     */
    @Override
    public Token oauth(Platform from, String oauthResponse, String captcha, String session, String source, boolean user, String jwt) throws IDLException {

        //1、先验证图片验证码
        if (captcha != null && captcha.length() > 0) {
            if (captchaService.evalSafeguardCode(session,captcha,CaptchaType.safeguard) != CaptchaEvaluation.safety) {
                throw Exceptions.NOT_FOUND_CAPTCHA_CODE_ERROR("验证码错误");
            }
        }

        //2、验证jwt是否ok
        JWT djwt = parserJWT(jwt);
        if (djwt == null || !djwt.head.grt.eval(JWT.Grant.Device)) {
            throw Exceptions.NO_PERMISSION_ERROR("缺少设备注册TOKEN");
        }

        long did = IDLT.longInteger(djwt.body.did);
        if (did == 0) {
            throw Exceptions.NO_PERMISSION_ERROR("缺少设备注册TOKEN");
        }

        String issuKey = Base64Util.encodeToString(AesHelper.randomKey(0));

        //3、查找三方收取数据
        OauthPartnerDO partner = getTheOauthPartnerDO(from);
        if (partner == null) {
            throw Exceptions.NOT_FOUND_THIRD_PARTY_PLATFORM_ERROR("找不到对应的三方平台配置");
        }

        //4、整理三方授权信息
        Map<String, String> paras = CommonUtil.getMapFromReq(oauthResponse);
        if (paras == null || paras.size() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("验证失败，没有解析到三方返回的数据");
        }

        switch (from) {
            case wechat_mp:
            case wechat_mp_base:
            case wechat_open:
            case wechat_svm:
            case wechat_app: {//微信的统一处理
                return wechatAuth(from,partner,paras,issuKey,djwt,did,source,user);
            }
            case wechat_mina: {//因为小程序流程和公众平台授权流程不一样 小程序无法获取用户信息 所以单独判断小程序登录
                return wechatMinaAuth(from,partner,paras,issuKey,djwt,did,source,user);
            }
            case alipay_qklg:
            case alipay_app:
            case alipay_fuwu: {// https://my.oschina.net/bddiudiu/blog/834607
                return alipayAuth(from,partner,paras,issuKey,djwt,did,source,user);
            }
            case qq_app: {
                return qqAuth(from,partner,paras,issuKey,djwt,did,source,user);
            }
        }

        return null;
    }

    //微信公众号单独授权
    private Token wechatMPAuth(Platform from, OauthPartnerDO partner, Map<String, String> paras, String issuKey, JWT djwt, long did, String source, boolean user) throws IDLException {
        /* //放到微信登录一起处理
            case wechat_mp:
            case wechat_mp_base: {
                //微信登录过程
                String code = paras.get("code");
                if (code == null || code.isEmpty()) {
                    throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信验证失败");
                }

                //获取微信用户的openid
//                @HttpApi(name = "user.isWxMpSubed", desc = "查询用户是否已关注微信公众号", security = SecurityType.None, owner = "yanjinjin")
//                if (partnerId == PartnerId.wechat_mp_base) {
//                    partner = userCacheFacade.getUserPartnerInfo(PartnerId.wechat_mp.toString());
//                }
//                String openId = partnerLoginFacade.getOpenId(partner, paras.get("code"));
//
//                UserWxSubInfo subInfo = new UserWxSubInfo();
//                subInfo.partnerId = partnerId;
//                subInfo.wxOpenId = openId;
//                boolean isSubed = PartnerUtil.isWxSubscribed(partner, openId);
//                subInfo.isSubed = isSubed ? 1 : 0;
//                return subInfo;

                //查询是否订阅
                //@HttpApi(name = "user.isWxMpSubscribed", desc = "查询是否关注微信公众号", security = SecurityType.None, owner = "yanjinjin")
//                String openId = PartnerUtil.getOpenId(partner, paras.get("code"));
//                return PartnerUtil.isWxSubscribed(partner, openId);

                //微信公众号登录，在微信浏览器中，因为是浏览器(jwt是临时生成的）
                //@HttpApi(name = "user.partnerBaseAuth", desc = "联合登录base auth", security = SecurityType.None, owner = "yanjinjin")
                String openId = "";
                if (partner != null) {
                    String url = "appid=" + partner.appId + "&secret=" + partner.appKey + "&code=" + code + "&grant_type=authorization_code";
                    String result = WebRequestUtil.getResponseString(partner.apiUrl + "oauth2/access_token", url, false);
                    logger.info(String.format("询问微信accesstoken，request=%s,result=%s", partner.apiUrl + "oauth2/access_token?" + url, result));
                    if (result != null) {
                        JSONObject ob = (JSONObject) JSON.parse(result);
                        openId =  ob.getString("openid");
                    }
                }
                if (openId != null && openId.length() > 0 ) {
                    //生成下发token
                    PartnerAccountInfo info = new PartnerAccountInfo();
                    info.partnerType = "微信";
                    info.openId = openId;

                    //生成token
//                    Token authToken = createAccountToken() //TODO : 替换下面结果
//                    info.baseAuthToken = TokenHelper.generateBaseAuthToken(appId, deviceId, "wechat|" + openId, expire, issuKey);
                    logger.info("[微信base auth]ptn={},openId={},authToken={}", "wechat", openId, info.baseAuthToken);
                }
                break;
            }
            */
        return null;
    }

    private Token qqAuth(Platform from, OauthPartnerDO partner, Map<String, String> paras, String issuKey, JWT djwt, long did, String source, boolean user) throws IDLException {
        AccountDO accountDO = null;

        //第一步客户单获取Authorization Code oauthResponse
        String code = paras.get("code");
        if (code == null || code.isEmpty()) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("QQ验证失败");
        }

        //第二步取access_token 可放到客户端，因为有刷新token动作
        // https://graph.qq.com/oauth2.0/token
        String tokenUrl = "https://graph.qq.com/oauth2.0/token";
        String tokenQuery = "grant_type=authorization_code&client_id=" + partner.appId + "&client_secret=" + partner.appKey + "&code=" + code;
        String result = WebRequestUtil.getResponseString(tokenUrl, tokenQuery, false);
        if (result == null || result.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("QQ授权失败");
        }
        String accessToken = null;
        try {
            JSONObject ob = (JSONObject) JSON.parse(result);
            accessToken = ob.getString("access_token");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (accessToken == null || accessToken.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("QQ授权失败");
        }

        //第三步取openId 可放客户端
        String openUrl = "https://graph.qq.com/oauth2.0/me";
        String openQuery = "access_token=" + accessToken;
        result = WebRequestUtil.getResponseString(openUrl, openQuery, false);
        if (result == null || result.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("QQ授权失败");
        }
        String openId = null;
        try {
            JSONObject ob = (JSONObject) JSON.parse(result);
            openId = ob.getString("openid");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (openId == null || openId.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("QQ授权失败");
        }

        // 先看账号是否存在
        String nick  = null;
        Gender gender = Gender.unknown;
        String head = null;
        String mobile = null;
        String email = null;
        long uid = 0;
        String realName = "";
        String ptn = partner.platform;
        if (ptn.indexOf("_") > 0) {
            ptn = ptn.substring(0, ptn.indexOf("_"));
        }

        // 通过openId查找账号
        accountDO = findAccountByOpenIdAndUnionId(from,openId,ptn,openId);
        if (accountDO != null && accountDO.uid != null) {//采用unionId查一遍
            uid = IDLT.longInteger(accountDO.uid);
        }

        // 账号已经注册，则不再注册账号
        if (accountDO == null) {

            //第四步获取qq信息
            String url = "access_token=" + accessToken + "&oauth_consumer_key=" + partner.appId + "&openid=" + openId;
            result = WebRequestUtil.getResponseString(partner.apiUrl, url, false);
            if (result == null || result.length() == 0) {
                throw Exceptions.THIRD_PARTY_AUTH_ERROR("QQ获取用户信息失败");
            }

            try {
//                    result = URLDecoder.decode(result, "UTF-8"); //是否还需要url decode
                JSONObject ob = (JSONObject) JSON.parse(result);

                nick = ob.getString("nickname");
                if (ob.getString("nickname") != null) {//昵称需要过滤,表情字符，如果库支持，则不需要
                    nick = URLEncoder.encode(ob.getString("nickname"), "UTF-8");
                }
                String sex = ob.getString("gender");
                if (sex != null) {
                    if ("男".equals(ob.getString("gender"))) {
                        gender = Gender.male;
                    } else {
                        gender = Gender.female;
                    }
                }
                head = ob.getString("figureurl_qq_2");
            } catch (Throwable e) {
                e.printStackTrace();
            }

            //自动绑定上去
            accountDO = findAndCreateAccount(from, openId, uid, null, nick, head, gender, mobile, email, ptn, openId, source);
        }

        Token accountToken = createAccountToken(accountDO, djwt,issuKey,did);
        //不需要生成用户
        if (!user) {
            return accountToken;
        }

        // 生成用户信息
        UserDO userDO = null;
        if (uid > 0) {
            userDO = userDAO.getById(uid);
        }


        Token userToken = createUserToken(accountDO,userDO,djwt,issuKey,did);
        if (userToken != null) {
            return userToken;
        }
        return accountToken;
    }

        private Token alipayAuth(Platform from, OauthPartnerDO partner, Map<String, String> paras, String issuKey, JWT djwt, long did, String source, boolean user) throws IDLException {
        AccountDO accountDO = null;
        AccountDO mobileAccountDO = null;

        String apiUrl = "https://mapi.alipay.com/gateway.do";
        String uinfoAndToken = PartnerUtil.alipayAuthTokenExchange(partner, paras, apiUrl);
        if (uinfoAndToken == null || uinfoAndToken.isEmpty()) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("支付宝授权失败");
        }
        String[] strs = uinfoAndToken.split("\\|");
        String aliUid = strs[0];
        String accessToken = strs[1];
        String ptn = partner.platform;
        if (ptn.indexOf("_") > 0) {
            ptn = ptn.substring(0, ptn.indexOf("_"));
        }

        String unionId = aliUid; //支付宝直接用alipay user id做unionId即可

        String nick  = null;
        Gender gender = Gender.unknown;
        String head = null;
        String mobile = null;
        String email = null;
        long uid = 0;
        String realName = "";

        // 通过aliUid查找账号(ali的uid)
        accountDO = findAccountByOpenIdAndUnionId(from,aliUid,ptn,aliUid);
        if (accountDO != null && accountDO.uid != null) {//采用unionId查一遍
            uid = IDLT.longInteger(accountDO.uid);
        }

        // 账号已经注册，则不再注册账号
        if (accountDO == null) {

            // 获取支付宝用户信息
            String response = PartnerUtil.alipayUserInfoShare(partner, accessToken, apiUrl);
            if (response == null || !"T".equals(XMLUtil.getNodeValue(response, "is_success"))) {
                throw Exceptions.THIRD_PARTY_AUTH_ERROR("支付宝信息获取失败");
            }

            Map<String, String> respMap = XMLUtil.doXMLParse(response);
            Map<String, String> contactMap = XMLUtil.doXMLParse(respMap.get("response"));
            String key = "";
            String signType = respMap.get("sign_type");
            if ("MD5".equals(signType)) {
                key = partner.appKey;
            } else if ("RSA".equals(signType)) {
                key = partner.priKey;
            }
            String mysign = SignUtil.buildRequestMysign(contactMap, partner.charset, signType, key, true);
            if (!respMap.get("sign").equals(mysign)) { //验签成功
                throw Exceptions.THIRD_PARTY_AUTH_ERROR("支付宝签名失败");
            }
            //记录第三方用户信息，若有地址列表同时记录地址信息
            email = contactMap.get("email");
            mobile = contactMap.get("mobile");
            realName = contactMap.get("real_name");
            if (paras.get("real_name") != null) {//过滤掉表情
                try {
                    realName = URLEncoder.encode(paras.get("real_name"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            nick = contactMap.get("nick_name") != null ? contactMap.get("nick_name") : realName;
            head = contactMap.get("avatar");
            boolean is_certified = IDLT.bool(contactMap.get("is_certified"));
            if (is_certified) {
                String sex = contactMap.get("gender");//【注意】只有is_certified为T的时候才有意义，否则不保证准确性. 性别（F：女性；M：男性）。
                if (sex != null) {
                    if (sex.equals("M")) {
                        gender = Gender.male;
                    } else if (sex.equals("F")) {
                        gender = Gender.female;
                    }
                }
            }

            //自动绑定上去
            accountDO = findAndCreateAccount(from, aliUid, uid, null, nick, head, gender, mobile, email, ptn, unionId, source);
            uid = IDLT.longInteger(accountDO.uid);
        }

        // 如果支付宝账号返回了手机号，是否通过手机号生成用户
        if (mobile != null && uid <= 0) {
            mobileAccountDO = findAndCreateAccount(Platform.mobile, PhoneNumUtil.tidyChinaMobile(mobile), uid, null, nick, head, gender, mobile, email,null,null, "_src=" + from.name()+":"+aliUid);
            uid = IDLT.longInteger(mobileAccountDO.uid);
        }

        Token accountToken = createAccountToken(accountDO != null ? accountDO : mobileAccountDO, djwt,issuKey,did);
        //不需要生成用户
        if (!user) {
            return accountToken;
        }

        // 生成用户信息
        UserDO userDO = null;
        if (uid > 0) {
            userDO = userDAO.getById(uid);
        } else if (mobileAccountDO != null) {
            userDO = findAndCreateUser(mobileAccountDO,source);
            if (uid <= 0) {
                uid = IDLT.longInteger(userDO.id);
            }
        }

        // 更新支付宝账号绑定user信息
        if (uid > 0 && accountDO != null && accountDO.uid == null) {
            accountDO.uid = uid;
            accountDAO.update(accountDO);
        }

        Token userToken = createUserToken(accountDO != null?accountDO:mobileAccountDO,userDO,djwt,issuKey,did);
        if (userToken != null) {
            return userToken;
        }
        return accountToken;
    }

    private Token wechatAuth(Platform from, OauthPartnerDO partner, Map<String, String> paras, String issuKey, JWT djwt, long did, String source, boolean user) throws IDLException {
        AccountDO accountDO = null;
        AccountDO mobileAccountDO = null;

        //微信登录过程
        String code = paras.get("code");
        if (code == null || code.isEmpty()) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信验证失败");
        }
//                bindDTO = PartnerUtil.wxLogin(partner, paras.get("code"), djwt.body.aid);

        PartnerUtil.UserPartnerBindDTO infoDTO = null;
        String result = "";

        String url = "appid=" + partner.appId + "&secret=" + partner.appKey + "&code=" + code + "&grant_type=authorization_code";
        result = WebRequestUtil.getResponseString(partner.apiUrl + "oauth2/access_token", url, false);

        if (result == null || result.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信验证失败");
        }

        String accessToken = null;
        String openId = "";
        try {
            JSONObject ob = (JSONObject) JSON.parse(result);
            accessToken = ob.getString("access_token");
            openId = ob.getString("openid");
        } catch (Throwable e) {
            logger.error("JSON parse error! ", e);
        }

        if (accessToken == null || accessToken.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信授权AccessToken失败");
        }

        //获取用户凭证
        url = "access_token=" + accessToken + "&openid=" + openId;
        result = WebRequestUtil.getResponseString(partner.apiUrl + "auth", url, false);
        if (result == null || result.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信获取用户信息失败");
        }

        //logger.info(String.format("检验微信凭证的有效性：request=%s,result=%s", partner.apiUrl + "auth?" + url, result));
        int errorCode = 0;
        try {
            JSONObject ob = (JSONObject) JSON.parse(result);
            errorCode = ob.getIntValue("errcode");
        } catch (Throwable e) {
            logger.error("JSON parse error! ", e);
        }

        if (errorCode != 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信获取用户信息失败");
        }

        //获取用户信息
        result = WebRequestUtil.getResponseString(partner.apiUrl + "userinfo", url, false);
        if (result == null || result.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信获取用户信息请求错误");
        }

        String unionId = null;
        String nick  = null;
        Gender gender = Gender.unknown;
        String head = null;
        String mobile = null;
        String email = null;
        long uid = 0;
        try {
            JSONObject ob = (JSONObject) JSON.parse(result);
            unionId = ob.getString("unionid");
            nick = ob.getString("nickname");
                        if (ob.getString("nickname") != null) {//昵称需要过滤,表情字符，如果库支持，则不需要
                            nick = URLEncoder.encode(ob.getString("nickname"), "UTF-8");
                        }
            int sex = ob.getIntValue("sex");//0未确认；1男；2女
            gender = Gender.valueOf(sex);
            head = ob.getString("headimgurl");
        } catch (Throwable e) {
            logger.error("JSON parse error! ", e);
        }

        //保留前缀，此处逻辑不再需要
        String ptn = partner.platform;
        if (ptn.indexOf("_") > 0) {
            ptn = ptn.substring(0, ptn.indexOf("_"));
        }

        //通过unionId查找
        AccountDO unionAccount = null;
        if (unionId != null && unionId.length() > 0) {
            List<AccountDO> temp = accountDAO.queryByUnionAndUnionId(ptn,unionId,0,null,false,0,1);
            if (temp != null && temp.size() > 0) {
                unionAccount = temp.get(0);

                uid = IDLT.longInteger(unionAccount.uid);
                if (nick == null && unionAccount.nick != null) {
                    nick = unionAccount.nick;
                }
                if (head == null && unionAccount.head != null) {
                    head = unionAccount.head;
                }
                if (mobile == null && unionAccount.mobile != null) {
                    mobile = unionAccount.mobile;
                }
                if (email == null && unionAccount.email != null) {
                    email = unionAccount.email;
                }
            }
        }

        //自动绑定上去
        accountDO = findAndCreateAccount(from, openId, uid, null, nick, head, gender, mobile, email, ptn, unionId, source);


        Token accountToken = createAccountToken(accountDO != null ? accountDO : mobileAccountDO, djwt,issuKey,did);
        //不需要生成用户
        if (!user) {
            return accountToken;
        }

        //查找用户
        UserDO userDO = null;
        if (uid > 0) {
            userDO = userDAO.getById(uid);
        }

        Token userToken = createUserToken(accountDO != null?accountDO:mobileAccountDO,userDO,djwt,issuKey,did);
        if (userToken != null) {
            return userToken;
        }
        return accountToken;
    }

    private Token wechatMinaAuth(Platform from, OauthPartnerDO partner, Map<String, String> paras, String issuKey, JWT djwt, long did, String source, boolean user) throws IDLException {
        AccountDO accountDO = null;
        AccountDO mobileAccountDO = null;


        //微信登陆过程
        String code = paras.get("code");
        if (code == null || code.isEmpty()) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信验证失败");
        }
        String params = "appid=" + partner.appId + "&secret=" + partner.appKey + "&js_code=" + code + "&grant_type=authorization_code";
        String result = WebRequestUtil.getResponseString(partner.apiUrl, params, false);
        if (result == null || result.length() == 0) {
            throw Exceptions.THIRD_PARTY_AUTH_ERROR("微信验证失败");
        }
        String unionId = null;
        String openId = null;
        try {
            JSONObject ob = (JSONObject) JSON.parse(result);
            unionId = ob.getString("unionid");
            openId = ob.getString("openid");
        } catch (Throwable e) {
            logger.error("JSON parse error! ", e);
        }

        //保留前缀，此处逻辑不再需要
        String ptn = partner.platform;
        if (ptn.indexOf("_") > 0) {
            ptn = ptn.substring(0, ptn.indexOf("_"));
        }

        AccountDO unionAccount = null;
        long uid = 0;
        Gender gender = Gender.unknown;
        String nick = null;
        String head = null;
        String mobile = null;
        String email = null;

        //通过unionId查找
        if (unionId != null && unionId.length() > 0) {
            List<AccountDO> temp = accountDAO.queryByUnionAndUnionId(ptn,unionId,0,null,false,0,1);
            if (temp != null && temp.size() > 0) {
                unionAccount = temp.get(0);

                uid = IDLT.longInteger(unionAccount.uid);
                nick = unionAccount.nick;
                head = unionAccount.head;
                mobile = unionAccount.mobile;
                email = unionAccount.email;
            }
        }

        //自动绑定上去
        accountDO = findAndCreateAccount(from, openId, uid, null, nick, head, gender, mobile, email, ptn, unionId, source);

        //不需要生成用户
        Token accountToken = createAccountToken(accountDO != null ? accountDO : mobileAccountDO, djwt,issuKey,did);
        if (!user) {
            return accountToken;
        }

        //查找用户
        UserDO userDO = null;
        if (uid > 0) {
            userDO = userDAO.getById(uid);
        }

        Token userToken = createUserToken(accountDO != null?accountDO:mobileAccountDO,userDO,djwt,issuKey,did);
        if (userToken != null) {
            return userToken;
        }
        return accountToken;
    }

//    @IDLDesc("用户的微信订阅信息")
//    public static class UserWxSubInfo implements Serializable {
//        @IDLDesc("partner id")
//        public Platform partnerId;
//        @IDLDesc("wx open id")
//        public String wxOpenId;
//        @IDLDesc("是否已订阅微信公众号,1表示已订阅，0未订阅")
//        public int isSubed;
//    }
//
//    @IDLDesc("第三方账户信息")
//    public static class PartnerAccountInfo implements Serializable {
//        @IDLDesc("第三方标示,wechat：微信，alipay：支付宝，fanli：返利网，heike：嘿客")
//        public String partnerType;
//        @IDLDesc("第三方账户昵称")
//        public String partnerNick;
//        @IDLDesc("第三方账户")
//        public String partnerAccount;
//        @IDLDesc("第三方用户身份信息,openId")
//        public String openId;
//        @IDLDesc("第三方bashAuth的token")
//        public String baseAuthToken;
//    }

    @Override
    public OAuthRequest reqLoginAuth(Platform platform, String callback) throws IDLException {

        OauthPartnerDO partner = getTheOauthPartnerDO(platform);
        if (partner == null) {
            throw Exceptions.NOT_FOUND_THIRD_PARTY_PLATFORM_ERROR("找不到Platform信息");
        }

        OAuthRequest result = new OAuthRequest();

        //处理callback参数 url encode
        String url = null;
        try {
            url = callback == null ? "" : URLEncoder.encode(callback, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            url = "";
        }

        switch (platform) {
            case wechat_open:{
                result.signLink = partner.connectUrl + "?appid=" + partner.appId + "&redirect_uri=" +
                        url + "&response_type=code&scope=snsapi_login&state=" + "STATE" + "#wechat_redirect";
                break;
            }
            case wechat_svm:{
                result.signLink = partner.connectUrl + "?appid=" + partner.appId + "&redirect_uri=" +
                        url + "&response_type=code&scope=snsapi_userinfo&state=" + "STATE" + "#wechat_redirect";
                break;
            }
            case alipay_qklg: {
                result.signLink = PartnerUtil.buildAlipayQklgUrl(partner, callback);
                break;
            }
            case alipay_fuwu: {
                result.signLink = partner.connectUrl + "?app_id=" + partner.appId + "&redirect_uri=" +
                        url + "&auth_skip=true&scope=auth_userinfo";
                break;
            }
            case alipay_app: {
                result.signLink = PartnerUtil.buildAliAuthEntity(partner);
                break;
            }
            case wechat_mp_base: {
                result.signLink = partner.connectUrl + "?appid=" + partner.appId + "&redirect_uri=" +
                        url + "&response_type=code&scope=snsapi_base&state=" + "STATE" + "#wechat_redirect";
                break;
            }
            case qq_app: {
                //一下代码主要是防止OauthPartnerDO填充数据
//                if (org.apache.commons.lang3.StringUtils.isNotBlank(ptnAppid) && !partner.getAppid().equals(ptnAppid)) {
//                    commonFacade.putByPtnAppid(partner, partnerId, ptnAppid);
//                }
                result.signLink = partner.connectUrl + "?response_type=code&client_id=" + partner.appId +
                        "&redirect_uri=" + url + "&state=" + MD5.computeToHex(String.valueOf(System.currentTimeMillis()).getBytes())
                        + "&scope=get_user_info";
                break;
            }
        }

        return null;
    }

    private AccountDO findAccountByOpenIdAndUnionId(Platform from, String openId, String union,String unionId) {
        AccountDO accountDO = accountDAO.findAccountByAccount(from.name(),openId);
        if (accountDO == null && openId != null && openId.length() > 0) {//采用unionId查一遍
            List<AccountDO> temp = accountDAO.queryByUnionAndUnionId(union, unionId,0,null,false,0,1);
            if (temp != null && temp.size() > 0) {
                accountDO = temp.get(0);
            }
        }
        return accountDO;
    }

    private OauthPartnerDO getTheOauthPartnerDO(Platform platform) {
        String platformName = platform == Platform.wechat_mp_base ? Platform.wechat_mp.name() : platform.name();
        List<OauthPartnerDO> list = oauthPartnerDAO.queryByPlatform(platformName,0,null,false,0,1);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public static boolean isEmail(String string) {
        if (string == null) {
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(string);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
