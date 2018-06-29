package ssn.lmj.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayPlatformOpenidGetRequest;
import com.alipay.api.request.AlipayPlatformUseridGetRequest;
import com.alipay.api.response.AlipayPlatformOpenidGetResponse;
import com.alipay.api.response.AlipayPlatformUseridGetResponse;
import com.lmj.stone.core.VolatileReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ssn.lmj.user.db.dobj.OauthPartnerDO;
import ssn.lmj.user.service.entities.Gender;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-06-27
 * Time: 下午3:46
 */
public final class PartnerUtil {

    /**
     * 暂时弄过来，看下整体业务是什么
     */
    public static class UserPartnerBindDTO implements Serializable {
        /**
         * 绑定表id
         */
        private long bindId;
        /**
         * 绑定的用户id
         */
        private long userId;
        /**
         * 第三方账户标示
         */
        private String partnerId;
        /**
         * 第三方账户userid
         */
        private String ptUserId;
        /**
         * 第三方账户
         */
        private String ptAccount;
        /**
         * 第三方账户昵称
         */
        private String nickname;
        /**
         * 第三方账户头像
         */
        private String headImgUrl;
        /**
         * 性别
         */
        private int gender;
        /**
         * 是否删除
         */
        private int isDelete;
        /**
         * 应用id
         */
        private int appId;
        /**
         * 第三方账户openid
         */
        private String ptOpenId;
        /**
         * 用户手机号
         */
        private String mobile;
        /**
         * 第三方用户校验key
         */
        private String ptUserKey;

        public String getPtUserKey() {
            return ptUserKey;
        }

        public void setPtUserKey(String ptUserKey) {
            this.ptUserKey = ptUserKey;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }



        public String getPtOpenId() {
            return ptOpenId;
        }

        public void setPtOpenId(String ptOpenId) {
            this.ptOpenId = ptOpenId;
        }

        public String getPtAccount() {
            return ptAccount;
        }

        public void setPtAccount(String ptAccount) {
            this.ptAccount = ptAccount;
        }

        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
        }



        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }



        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPtUserId() {
            return ptUserId;
        }

        public void setPtUserId(String ptUserId) {
            this.ptUserId = ptUserId;
        }

        public long getBindId() {
            return bindId;
        }

        public void setBindId(long id) {
            this.bindId = id;
        }


    }

    private static final Logger logger = LoggerFactory.getLogger(PartnerUtil.class);

    private static final String wxPmPubUrl = "https://api.weixin.qq.com/cgi-bin/";

    private static final Map<String,VolatileReference<String>> cacheTKs = new ConcurrentHashMap<String, VolatileReference<String>>();
    private static String getCacheToken(String key) {
        VolatileReference<String> v = cacheTKs.get(key);
        if (v != null) {
            return v.get();
        }
        return null;
    }
    private static void setCacheToken(String key, String value,long s) {
        VolatileReference<String> v = new VolatileReference<String>(value,s * 1000);
        cacheTKs.put(key,v);
    }

    /**
     * 获取access_token,这里是获取普通的access_token，和授权登录的授权access_token不同
     * @param partner 公众号配置信息
     * @return 返回普通的accessToken
     */
    public static String getAccessToken(OauthPartnerDO partner) {
        //获取accessToken
        String cacheKey = "accessToken_" + partner.appId;
        String accessToken = getCacheToken(cacheKey);
        if (accessToken == null || accessToken.length() == 0) {
            String tokenUrl = wxPmPubUrl + "token";
            String tokenBody = "grant_type=client_credential&appid=" + partner.appId + "&secret=" + partner.appKey;
            String result = null;
            try {
                result = WebRequestUtil.getResponseString(tokenUrl, tokenBody, false);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return "";
            }
            logger.info("[获取accessToken]request={},result={}", tokenUrl + "?" + tokenBody, result);
            JSONObject ob = (JSONObject) JSON.parse(result);
            if (ob.getString("errcode") != null) {
                logger.error("[获取accessToken]失败。code={}", ob.getString("errcode"));
            } else {
                accessToken = ob.getString("access_token");
                int expiresIn = ob.getIntValue("expires_in");
                if (expiresIn > 0) {
                    //缓存accessToken，每天的限额是2000次，所以要缓存的，缓存时间2小时不是减10秒，是为了保证新的accessToken有效
                    setCacheToken(cacheKey, accessToken, expiresIn - 10 );
                }
            }
        }

        return accessToken;
    }

    /**
     * 获取微信用户的openid
     * @param partner
     * @param code
     * @return
     * @throws
     */
    public static String getOpenId(OauthPartnerDO partner, String code) throws Throwable {
        String url = "appid=" + partner.appId + "&secret=" + partner.appKey +
                "&code=" + code + "&grant_type=authorization_code";
        String result = WebRequestUtil.getResponseString(partner.apiUrl + "oauth2/access_token", url, false);
        logger.info(String.format("询问微信accesstoken，request=%s,result=%s", partner.apiUrl + "oauth2/access_token?" + url, result));
        if (result != null) {
            JSONObject ob = (JSONObject) JSON.parse(result);
            return ob.getString("openid");
        }
        return null;
    }

    /**
     * 查询是否订阅公众号
     * @param partner 公众号标示
     * @return
     */
    public static boolean isWxSubscribed(OauthPartnerDO partner, String openId) {
        String accessToken = getAccessToken(partner);
        //获取用户基本信息，可获取到是否关注公众号信息
        String subBody = "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        String result = null;
        try {
            result = WebRequestUtil.getResponseString(wxPmPubUrl + "user/info", subBody, false);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
        logger.info("[获取用户基本信息]result={}", result);
        JSONObject ob = (JSONObject) JSON.parse(result);
        String errorCode = ob.getString("errcode");
        if (errorCode != null && errorCode.length() > 0) {
            logger.error("[获取用户基本信息]订阅信息失败。result={}", result);
        } else {
            if (1 == ob.getIntValue( "subscribe")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 微信登陆过程
     * @param partner
     * @param code
     *
     * @return
     */
//    public static UserPartnerBindDTO wxLogin(OauthPartnerDO partner, String code, int appId) {
//        UserPartnerBindDTO infoDTO = null;
//        String result = "";
//        String openId = "";
//        try {
//            //因为小程序流程和公众平台授权流程不一样 小程序无法获取用户信息 所以单独判断小程序登录
//            if("wechat_mina".equals(partner.platform)){//如果是小程序登录 则走小程序流程
//                String params = "appid=" + partner.appId + "&secret=" + partner.appKey +
//                        "&js_code=" + code + "&grant_type=authorization_code";
//                result = WebRequestUtil.getResponseString(partner.apiUrl, params, false);
//            } else {
//                String url = "appid=" + partner.appId + "&secret=" + partner.appKey +
//                        "&code=" + code + "&grant_type=authorization_code";
//                result = WebRequestUtil.getResponseString(partner.apiUrl + "oauth2/access_token", url, false);
//                if (result != null) {
//                    JSONObject ob = (JSONObject) JSON.parse(result);
//                    String accessToken = ob.getString("access_token");
//                    if (accessToken == null || accessToken.length() == 0) {
//                        logger.info(String.format("询问微信授权accesstoken失败，request=%s,result=%s", partner.apiUrl + "oauth2/access_token?" + url, result));
//                        return null;
//                    } else {
//                        openId = ob.getString("openid");
//                        url = "access_token=" + accessToken + "&openid=" + openId;
//                        //获取用户信息
//                        result = WebRequestUtil.getResponseString(partner.apiUrl + "auth", url, false);
//                        //logger.info(String.format("检验微信凭证的有效性：request=%s,result=%s", partner.apiUrl + "auth?" + url, result));
//                        if (result != null) {
//                            ob = (JSONObject) JSON.parse(result);
//                            if (ob.getIntValue("errcode") == 0) { //true，获取用户信息
//                                result = WebRequestUtil.getResponseString(partner.apiUrl + "userinfo", url, false);
//                                //logger.info(String.format("到微信获取用户信息，request=%s, result=%s", partner.apiUrl + "userinfo?" + url, result));
//                            } else {
//                                logger.error(String.format("到微信获取用户信息失败，request=%s, result=%s", partner.apiUrl) + "userinfo?" + url, result));
//                                return null;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Throwable throwable) {
//            logger.error("微信登录失败，" + throwable);
//        }
//        if (result != null) {
//            try {
//                JSONObject ob = (JSONObject) JSON.parse(result);
//                if (ob.getString("unionid") != null) {
//                    String ptn = partner.platform;
//                    if (ptn.indexOf("_") > 0) {
//                        ptn = ptn.substring(0, ptn.indexOf("_"));
//                    }
//                    //因为小程序用户有可能刚开始没有unionId 后来关注了主站 又有unionId了
//                    if("wechat_mina".equals(partner.platform)) {
//                        //是微信小程序 如果该根据openId 查出绑定记录 并且unionId 为空 则更新unionId
//                        UserPartnerBindDTO bindDTO = userPartnerBindMapper.queryPtUinfoByPtOid(ptn, ob.getString("openid"));
//                        if(bindDTO != null && StringUtils.isEmpty(bindDTO.getPtUserId())){
//                            //更新user_partner_bind表中的unionId
//                            UserPartnerBindDTO userPartnerBindDTO = new UserPartnerBindDTO();
//                            userPartnerBindDTO.setPtUserId(ob.getString("unionid"));
//                            userPartnerBindDTO.setBindId(bindDTO.getBindId());
//                            userPartnerBindMapper.updateUserPartnerInfo(userPartnerBindDTO);
//                            //更新user_partner_info表中的unionId
//                            UserPartnerInfoDTO userPartnerInfo = new UserPartnerInfoDTO();
//                            userPartnerInfo.setUserPartnerBindId(bindDTO.getBindId());
//                            userPartnerInfo.setPartnerUserId(ob.getString("unionid"));
//                            userPartnerInfoMapper.updateUserPartnerInfo(userPartnerInfo);
//                        }
//                    }
//                    infoDTO = userPartnerBindMapper.queryPTUinfoByPtUid(ptn, ob.getString("unionid"));
//                    //因为小程序流程和公众平台授权流程不一样 小程序无法获取用户信息 所以单独判断小程序登录
//                    if("wechat_mina".equals(partner.platform)) {//如果是小程序登录 则走小程序流程
//                        if(infoDTO == null){
//                            infoDTO = new UserPartnerBindDTO();
//                            infoDTO.setUserId(0);
//                            infoDTO.setPartnerId(ptn);
//                            infoDTO.setPtUserId(ob.getString("unionid"));
//                            infoDTO.setPtOpenId(ob.getString("openid"));
//                            infoDTO.setAppId(appId);
//                            if (1 != userPartnerBindMapper.insertUserPartnerInfo(infoDTO)) {
//                                logger.error("[数据库更新]insert weixin login user to db failed, ali user_id" + ob.getString("unionid"));
//                                throw new ServiceException(UserServiceHttpCode.SERVER_ERROR);
//                            }
//                        }
//                        BaseQuery<UserPartnerInfoDTO> baseQuery = BaseQuery.getInstance(new UserPartnerInfoDTO());
//                        baseQuery.getData().setPartnerId(partner.platform);
//                        baseQuery.getData().setPartnerOpenId(ob.getString("openid"));
//                        if(userPartnerInfoMapper.count(baseQuery) == 0) {
//                            //将微信小程序的openid记录到user_partner_info表中
//                            UserPartnerInfoDTO userPartnerInfo = new UserPartnerInfoDTO();
//                            userPartnerInfo.setUserPartnerBindId(infoDTO.getBindId());
//                            userPartnerInfo.setUserId(0L);
//                            userPartnerInfo.setPartnerId(partner.platform);
//                            userPartnerInfo.setPartnerUserId(ob.getString("unionid"));
//                            userPartnerInfo.setPartnerOpenId(ob.getString("openid"));
//                            userPartnerInfo.setAppId(appId);
//                            userPartnerInfoMapper.insert(userPartnerInfo);
//                            infoDTO.setUserId(0); //当小程序不存在时设置userId为0，走生成临时token逻辑。但是不可以改数据库
//                        }
//                    }else {
//                        if (infoDTO != null) {
//                            if (ob.getString("nickname") != null) {
//                                infoDTO.setNickname(URLEncoder.encode(ob.getString("nickname"), "UTF-8"));//昵称需要过滤
//                            }
//                            infoDTO.setGender(ob.getInt("sex", 0));
//                            infoDTO.setHeadImgUrl(ob.getString("headimgurl"));
//                            userPartnerBindMapper.updateUserPartnerInfo(infoDTO);
//                        } else {
//                            infoDTO = new UserPartnerBindDTO();
//                            infoDTO.setUserId(0);
//                            if (ob.getString("nickname") != null) {
//                                infoDTO.setNickname(URLEncoder.encode(ob.getString("nickname"), "UTF-8"));
//                            }
//                            infoDTO.setGender(ob.getInt("sex", 0));
//                            infoDTO.setHeadImgUrl(ob.getString("headimgurl"));
//                            infoDTO.setPartnerId(ptn);
//                            infoDTO.setPtUserId(ob.getString("unionid"));
//                            infoDTO.setPtOpenId(ob.getString("openid"));
//                            infoDTO.setAppId(appId);
//                            if (1 != userPartnerBindMapper.insertUserPartnerInfo(infoDTO)) {
//                                logger.error("[数据库更新]insert weixin login user to db failed, ali user_id" + ob.getString("unionid"));
//                                throw new ServiceException(UserServiceHttpCode.SERVER_ERROR);
//                            }
//                        }
//                    }
//                }else {
//                    //有部分人 使用微信小程序登录  之前又没用使用微信登录过主站 或者关注过我们的其他产品 则没有unionId
//                    //如果是小程序登录 则走小程序流程
//                    if ("wechat_mina".equals(partner.platform)) {
//                        String ptn = partner.platform;
//                        if (ptn.indexOf("_") > 0) {
//                            ptn = ptn.substring(0, ptn.indexOf("_"));
//                        }
//                        infoDTO = userPartnerBindMapper.queryPtUinfoByPtOid(ptn, ob.getString("openid"));
//                        //因为小程序流程和公众平台授权流程不一样 小程序无法获取用户信息 所以单独判断小程序登录
//                        if (infoDTO == null) {
//                            infoDTO = new UserPartnerBindDTO();
//                            infoDTO.setUserId(0);
//                            infoDTO.setPartnerId(ptn);
//                            infoDTO.setPtUserId(ob.getString("unionid"));
//                            infoDTO.setPtOpenId(ob.getString("openid"));
//                            infoDTO.setAppId(appId);
//                            if (1 != userPartnerBindMapper.insertUserPartnerInfo(infoDTO)) {
//                                logger.error("[数据库更新]insert weixin login user to db failed, ali user_id" + ob.getString("unionid"));
//                                throw new ServiceException(UserServiceHttpCode.SERVER_ERROR);
//                            }
//                        }
//                        BaseQuery<UserPartnerInfoDTO> baseQuery = BaseQuery.getInstance(new UserPartnerInfoDTO());
//                        baseQuery.getData().setPartnerId(partner.platform);
//                        baseQuery.getData().setPartnerOpenId(ob.getString("openid"));
//                        if (userPartnerInfoMapper.count(baseQuery) == 0) {
//                            //将微信小程序的openid记录到user_partner_info表中
//                            UserPartnerInfoDTO userPartnerInfo = new UserPartnerInfoDTO();
//                            userPartnerInfo.setUserPartnerBindId(infoDTO.getBindId());
//                            userPartnerInfo.setUserId(0L);
//                            userPartnerInfo.setPartnerId(partner.platform);
//                            userPartnerInfo.setPartnerUserId(ob.getString("unionid"));
//                            userPartnerInfo.setPartnerOpenId(ob.getString("openid"));
//                            userPartnerInfo.setAppId(appId);
//                            userPartnerInfoMapper.insert(userPartnerInfo);
//                            infoDTO.setUserId(0); //当小程序不存在时设置userId为0，走生成临时token逻辑。但是不可以改数据库
//                        }
//                    }
//                }
//            } catch (com.alibaba.dubbo.common.json.ParseException pex) {
//                logger.error("JSON parse error, " + pex);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return infoDTO;
//    }

    /**
     * 支付宝快速登录
     * @param partner
     * @param params
     * @return
     */
//    public UserPartnerBindDTO alipayQklg(OauthPartnerDO partner, Map<String, String> params, int appId) {
//        UserPartnerBindDTO result = null;
//        if (params == null) {
//            return null;
//        }
//        if (SignUtil.verify(params, partner, true)) {
//            if (!("T".equals(params.get("is_success")))) {
//                return null;
//            }
//            String ptn = partner.platform;
//            if (ptn.indexOf("_") > 0) {
//                ptn = ptn.substring(0, ptn.indexOf("_"));
//            }
//
//            /* update by yanjinjin 20150915 账户切换需要，不再需要openid
//            String bizCon = "{\"user_ids\":[" + params.get("user_id") + "]}";
//            String openIds = getAliOidByUid(bizCon);//根据拿到的userid来获取对应的openid
//            Map<String, String> map = CommonUtil.jsonStrParseMap(openIds);
//            //logger.info(String.format("[支付宝快捷登陆]=%s,account=%s", map.get(params.get("user_id")), params.get("email")));
//            result = userPartnerBindMapper.queryPtUinfoByPtOid(ptn, map.get(params.get("user_id")));
//            */
//            result = userPartnerBindMapper.queryPTUinfoByPtUid(ptn, params.get("user_id"));
//            if (result != null) {
//                result.setPtAccount(params.get("email"));
//                result.setNickname(URLEncoder.encode(params.get("real_name"), "UTF-8"));
//                userPartnerBindMapper.updateUserPartnerInfo(result);
//            } else {
//                result = new UserPartnerBindDTO();
//                result.setPartnerId(ptn);
//                result.setPtUserId(params.get("user_id"));
//                result.setPtAccount(params.get("email"));
//                result.setAppId(appId);
//                result.setNickname(URLEncoder.encode(params.get("real_name"), "UTF-8"));
//                result.setUserId(0);
//                result.setGender(Gender.INVALID_GENDER.ordinal());
//                if (1 != userPartnerBindMapper.insertUserPartnerInfo(result)) {
//                    logger.error("[数据库更新]insert alipay QKLG user to db failed, ali user_id" + params.get("user_id"));
//                    throw new ServiceException(UserServiceHttpCode.SERVER_ERROR);
//                }
//            }
//        } else {
//            logger.error("[结果验证失败]alipayQKLG," + params.toString());
//            throw new ServiceException(UserServiceHttpCode.ALIPAY_VERIFY_FAIL);
//        }
//        return result;
//    }


    /**
     * qq登陆过程
     * @param partner
     * @param openId
     * @param accessToken
     * @return
     */
//    public static UserPartnerBindDTO qqLogin(OauthPartnerDO partner, String openId, String accessToken, int appId) throws ServiceException{
//        UserPartnerBindDTO bindDTO = null;
//        String result = "";
//        try {
//            String url = "access_token=" + accessToken + "&oauth_consumer_key=" + partner.appId
//                    + "&openid=" + openId;
//            result = WebRequestUtil.getResponseString(partner.apiUrl, url, false);
//            if (StringUtils.isNotBlank(result)) {
//                String ret = URLDecoder.decode(result, "UTF-8");
//                JSONObject ob = (JSONObject) JSON.parse(ret);
//                String ptn = partner.platform;
//                if (ptn.indexOf("_") > 0) {
//                    ptn = ptn.substring(0, ptn.indexOf("_"));
//                }
//                openId = partner.appId + "_" + openId;
//                bindDTO = userPartnerBindMapper.queryPtUinfoByPtOid(ptn, openId);
//                if (bindDTO != null) {
//                    if (ob.getString("nickname") != null) {
//                        bindDTO.setNickname(URLEncoder.encode(ob.getString("nickname"), "UTF-8"));//昵称需要过滤
//                    }
//                    if (StringUtils.isNotBlank(ob.getString("gender"))) {
//                        if ("男".equals(ob.getString("gender"))) {
//                            bindDTO.setGender(1);
//                        } else {
//                            bindDTO.setGender(2);
//                        }
//                    }
//                    bindDTO.setHeadImgUrl(ob.getString("figureurl_qq_2"));
//                    userPartnerBindMapper.updateUserPartnerInfo(bindDTO);
//                } else {
//                    bindDTO = new UserPartnerBindDTO();
//                    bindDTO.setUserId(0);
//                    if (ob.getString("nickname") != null) {
//                        bindDTO.setNickname(URLEncoder.encode(ob.getString("nickname"), "UTF-8"));
//                    }
//                    if (StringUtils.isNotBlank(ob.getString("gender"))) {
//                        if ("男".equals(ob.getString("gender"))) {
//                            bindDTO.setGender(1);
//                        } else {
//                            bindDTO.setGender(2);
//                        }
//                    }
//                    bindDTO.setHeadImgUrl(ob.getString("figureurl_qq_2"));
//                    bindDTO.setPartnerId(ptn);
//                    bindDTO.setPtOpenId(openId);
//                    bindDTO.setAppId(appId);
//                    if (1 != userPartnerBindMapper.insertUserPartnerInfo(bindDTO)) {
//                        logger.error("[数据库更新]insert QQ APP login user to db failed, qq openid=" + openId);
//                        throw new ServiceException(UserServiceHttpCode.SERVER_ERROR);
//                    }
//                }
//            }
//        } catch (Throwable throwable) {
//            logger.error("qq登录失败，" + throwable);
//        }
//
//        return bindDTO;
//    }

    /**
     * 拼接支付宝快捷登陆的url
     * @param partnerDTO
     * @return
     */
    public static String buildAlipayQklgUrl(OauthPartnerDO partnerDTO, String returnUrl) {
        String result = "";
        Map<String, String> reqParam = new HashMap<String, String>();
        reqParam.put("service", partnerDTO.service);
        reqParam.put("partner", partnerDTO.appId);
        reqParam.put("_input_charset", partnerDTO.charset);
        reqParam.put("sign_type", partnerDTO.signType);
        reqParam.put("return_url", returnUrl);
        reqParam.put("target_service", partnerDTO.targetService);
        result = partnerDTO.apiUrl + "?" + SignUtil.buildRequestUrl(reqParam, partnerDTO.charset,
                partnerDTO.signType, partnerDTO.appKey);
        return result;
    }

    /**
     * 构建支付宝app无线账户授权实体
     * @return
     */
    public static String buildAliAuthEntity(OauthPartnerDO partner) {
        if (partner == null) {
            logger.error("[联合登录]合作方配置信息为空，method=buildAliAuthEntity。");
            return null;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiname", "\"" + partner.service + "\"");
        params.put("app_id", "\"" + partner.appId + "\"");
        params.put("app_name", "\"" + "mc" + "\"");
        params.put("biz_type", "\"openservice\"");
        params.put("pid", "\"" + partner.partnerId + "\"");
        params.put("product_id", "\"WAP_FAST_LOGIN\"");
        params.put("scope", "\"kuaijie\"");
        params.put("target_id", "\"" + String.valueOf(System.currentTimeMillis()) + "\"");
        params.put("auth_type", "\"AUTHACCOUNT\"");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("sign_date", "\"" + format.format(new Date()) + "\"");
        params.put("sign_type", "\"" + partner.signType + "\"");
        String key = "";
        if ("MD5".equals(partner.signType)) {
            key = partner.appKey;
        } else if ("RSA".equals(partner.signType)) {
            key = partner.priKey;
        }
        String result = SignUtil.buildRequestUrlForApp(params, partner.charset, partner.signType, key);
        logger.info("[app授权登录]result=" + result);

        return result;
    }

    /**
     * 支付宝授权登录
     * @param partner
     * @param params
     * @param appid
     * @return
     */
//    public static UserPartnerBindDTO alipayLoginAuth(OauthPartnerDO partner, Map<String, String> params, int appid) {
//        //1. 获得authCode
//        String authCode = params.get("auth_code");
//
//        try {
//            //2. 利用authCode获得authToken
//            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
//            oauthTokenRequest.setCode(authCode);
//            oauthTokenRequest.setGrantType("authorization_code");
//            AlipayClient alipayClient = new DefaultAlipayClient(partner.apiUrl, partner.appId,
//                    partner.priKey, "json", partner.charset);
//            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
//                    .execute(oauthTokenRequest);
//
//            //成功获得authToken
//            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
//
//                //4. 利用authToken获取用户信息
//                AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
//                AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
//                        userinfoShareRequest, oauthTokenResponse.getAccessToken());
//
//                //成功获得用户信息
//                if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
//                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
//                    logger.info("[支付宝授权登录]获取用户信息成功：" + userinfoShareResponse.getBody());
//                    String ptn = partner.platform;
//                    if (ptn.indexOf("_") > 0) {
//                        ptn = ptn.substring(0, ptn.indexOf("_"));
//                    }
//                    UserPartnerBindDTO bindDTO = userPartnerBindMapper.queryPtUinfoByPtOid(ptn, userinfoShareResponse.getUserId());
//                    if (bindDTO == null) {
//                        bindDTO = new UserPartnerBindDTO();
//                        bindDTO.setAppId(appid);
//                        bindDTO.setPartnerId(ptn);
//                        String realName = userinfoShareResponse.getRealName();
//                        bindDTO.setNickname(realName == null ? null : URLEncoder.encode(realName, "UTF-8"));
//                        bindDTO.setMobile(userinfoShareResponse.getMobile());
//                        bindDTO.setPtOpenId(userinfoShareResponse.getUserId());
//                        bindDTO.setHeadImgUrl(userinfoShareResponse.getAvatar()); //头像
//                        for (int i = 0; i < 3; i++) {
//                            if (1 == userPartnerBindMapper.insertUserPartnerInfo(bindDTO)) {
//                                break;
//                            }
//                            if (i == 2) {
//                                logger.error(String.format("[支付宝授权登录入库失败]%s,%s", bindDTO.getMobile(), bindDTO.getPtOpenId()));
//                            }
//                        }
//                    } else {
//                        bindDTO.setMobile(userinfoShareResponse.getMobile());
//                        bindDTO.setHeadImgUrl(userinfoShareResponse.getAvatar());
//                        String realName = userinfoShareResponse.getRealName();
//                        bindDTO.setNickname(realName == null ? null : URLEncoder.encode(realName, "UTF-8"));
//                        userPartnerBindMapper.updateUserPartnerInfo(bindDTO);
//                    }
//                    return bindDTO;
//                } else {
//                    logger.error("[支付宝授权登录]获取用户信息失败");
//                }
//            } else {
//                //这里仅是简单打印， 请开发者按实际情况自行进行处理
//                logger.error("[支付宝授权登录]authCode换取authToken失败");
//            }
//        } catch (AlipayApiException alipayApiException) {
//            //自行处理异常
//            logger.error(String.format("[支付宝授权登录]失败。ex=%s", alipayApiException));
//        } catch (UnsupportedEncodingException e) {
//            logger.error(String.format("[支付宝授权登录]失败。ex=%s", e));
//        } catch (Throwable throwable) {
//            logger.error(String.format("[支付宝授权登录]失败。ex=%s", throwable));
//        }
//        return null;
//    }

    /**
     * 支付宝授权登录，可得到alipay userid
     * @param partner
     * @param sparam
     * @return
     */
//    public static UserPartnerBindDTO alipayAuthLoginByUid(OauthPartnerDO partner, Map<String, String> sparam, int appid) throws IOException, SAXException, ParserConfigurationException {
//        String apiUrl = "https://mapi.alipay.com/gateway.do";
//        String uinfoAndToken = alipayAuthTokenExchange(partner, sparam, apiUrl);
//        if (uinfoAndToken != null && !uinfoAndToken.isEmpty()) {
//            String[] strs = uinfoAndToken.split("\\|");
//            String uid = strs[0];
//            String accessToken = strs[1];
//
//            /* update by yanjinjin 20150915 账户切换，不再需要openid了
//            //根据uid获取openid
//            String bizCon = "{\"user_ids\":[" + uid + "]}";
//            String openIds = getAliOidByUid(bizCon);//根据拿到的userid来获取对应的openid
//            Map<String, String> map = CommonUtil.jsonStrParseMap(openIds);
//            UserPartnerBindDTO bindDTO = userPartnerBindMapper.queryPtUinfoByPtOid("alipay", map.get(uid));
//            */
//            UserPartnerBindDTO bindDTO = userPartnerBindMapper.queryPTUinfoByPtUid("alipay", uid);
//            //获取用户共享信息
//            String response = alipayUserInfoShare(partner, accessToken, apiUrl);
//            logger.info(String.format("[支付宝联合登录]getUserShareInfo result=%s", response));
//            if (response != null && "T".equals(XMLUtil.getNodeValue(response, "is_success"))) {
//                Map<String, String> respMap = XMLUtil.doXMLParse(response);
//                Map<String, String> contactMap = XMLUtil.doXMLParse(respMap.get("response"));
//                String key = "";
//                String signType = respMap.get("sign_type");
//                if ("MD5".equals(signType)) {
//                    key = partner.appKey;
//                } else if ("RSA".equals(signType)) {
//                    key = partner.priKey;
//                }
//                String mysign = SignUtil.buildRequestMysign(contactMap, partner.charset, signType, key, true);
//                if (respMap.get("sign").equals(mysign)) { //验签成功
//                    //记录第三方用户信息，若有地址列表同时记录地址信息
//                    if (bindDTO == null) {
//                        bindDTO = new UserPartnerBindDTO();
//                        bindDTO.setAppId(appid);
//                        bindDTO.setPartnerId("alipay");
//                        bindDTO.setPtAccount(contactMap.get("email"));
//                        String realName = contactMap.get("real_name");
//                        bindDTO.setNickname(realName == null ? null : URLEncoder.encode(realName, "UTF-8"));
//                        bindDTO.setMobile(contactMap.get("mobile"));
//                        bindDTO.setPtUserId(contactMap.get("user_id"));
//                        for (int i = 0; i < 3; i++) {
//                            if (1 == userPartnerBindMapper.insertUserPartnerInfo(bindDTO)) {
//                                break;
//                            }
//                            if (i == 2) {
//                                logger.error(String.format("[支付宝授权登录入库失败]%s,%s", bindDTO.getMobile(), bindDTO.getPtOpenId()));
//                            }
//                        }
//                    } else {
//                        bindDTO.setPtAccount(contactMap.get("email"));
//                        String realName = contactMap.get("real_name");
//                        bindDTO.setNickname(realName == null ? null : URLEncoder.encode(realName, "UTF-8"));
//                        bindDTO.setMobile(contactMap.get("mobile"));
//                        userPartnerBindMapper.updateUserPartnerInfo(bindDTO);
//                    }
//                }
//            }
//            return bindDTO;
//        }
//
//        return null;
//    }

    /**
     * 支付宝授权token交换接口
     * @return
     */
    public static String alipayAuthTokenExchange(OauthPartnerDO partner, Map<String, String> sparam, String apiUrl) {
        String authCode = sparam.get("auth_code");
        String signType = "MD5";
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", "alipay.open.auth.token.exchange");
        params.put("partner", partner.partnerId);
        params.put("_input_charset", partner.charset);
        params.put("sign_type", signType);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("timestamp", format.format(new Date()));
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);
        String key = "";
        if ("MD5".equals(signType)) {
            key = partner.appKey;
        } else if ("RSA".equals(signType)) {
            key = partner.priKey;
        }
        String rqBody = SignUtil.buildRequestUrl(params, partner.charset, signType, key);
        try {
            String result = WebRequestUtil.getResponseString(apiUrl, rqBody, false);
            if ("T".equals(XMLUtil.getNodeValue(result, "is_success"))) { //说明成功
                Map<String, String> respMap = XMLUtil.doXMLParse(result);
                Map<String, String> tokenMap = XMLUtil.doXMLParse(respMap.get("response"));
                String mySign = SignUtil.buildRequestMysign(tokenMap, partner.charset, respMap.get("sign_type"), key, true);
                if (respMap.get("sign").equals(mySign)) {
                    String uid = tokenMap.get("user_id");
                    String accessToken = tokenMap.get("access_token");
                    return uid + "|" + accessToken; //将此次获取到的uid 和accessToken返回
                }
            } else {
                logger.error(String.format("[支付宝授权令牌交换]失败。url=%s,result=%s", apiUrl + "?" + rqBody, result));
            }
        } catch (Throwable throwable) {
            logger.error(throwable.toString());
        }

        return "";
    }

    /**
     * 支付宝用户信息共享接口
     * @param partner
     * @return
     */
    public static String alipayUserInfoShare(OauthPartnerDO partner, String accessToken, String apiUrl){
        Map<String, String> params = new HashMap<String, String>();
        String signType = "MD5";
        params.put("service", "alipay.user.userinfo.share");
        params.put("partner", partner.partnerId);
        params.put("sign_type", signType);
        params.put("_input_charset", partner.charset);
        params.put("query_token", accessToken);
        String key = "";
        if ("MD5".equals(signType)) {
            key = partner.appKey;
        } else if ("RSA".equals(signType)) {
            key = partner.priKey;
        }
        String body = SignUtil.buildRequestUrl(params, partner.charset, signType, key);
        String response = WebRequestUtil.getResponseString(apiUrl, body, false);
        if (response != null) {
            return response;
        }
        return "";
    }

    /**
     * 通过支付宝的uid换取对应的openid，主要是用于快登和授权登录的用户统一查询
     * @return
     */
//    public static boolean aliUid4Openid() throws AlipayApiException, com.alibaba.dubbo.common.json.ParseException {
//        List<UserPartnerBindDTO> bindList = null;
//        int i = 0;
//        String bizCon = "{\"user_ids\":[";
//        int pgIndex = 0;
//        int pgSize = 500;
//        bindList = userPartnerBindMapper.queryAliNoOpenIdList(0, 500);
//        while (bindList != null && bindList.size() > 0) {
//            for (UserPartnerBindDTO bindDTO : bindList) {
//                i++;
//                bizCon += "\"" + bindDTO.getPtUserId() + "\",";
//                if (i == 50) {
//                    bizCon = bizCon.substring(0, bizCon.length() - 1) + "]}";
//                    batchUpdateAliOpenId(bizCon);
//                    i = 0;
//                    bizCon = "{\"user_ids\":[";
//                }
//            }
//            if (i > 0) {
//                bizCon = bizCon.substring(0, bizCon.length() - 1) + "]}";
//                batchUpdateAliOpenId(bizCon);
//                i = 0;
//                bizCon = "{\"user_ids\":[";
//            }
//            bindList = userPartnerBindMapper.queryAliNoOpenIdList(pgIndex * pgSize, pgSize);
//        }
//        return true;
//    }

    /**
     * 批量更新支付宝的openId
     * @param bizCon
     * @return
     * @throws AlipayApiException
     * @throws com.alibaba.dubbo.common.json.ParseException
     */
//    private static boolean batchUpdateAliOpenId(String bizCon) throws AlipayApiException, com.alibaba.dubbo.common.json.ParseException {
//        String result = getAliOidByUid(bizCon);
//        Map<String, String> params = CommonUtil.jsonStrParseMap(result);
//        if (params != null && params.size() > 0) {
//            for (String k : params.keySet()) { //更新openid入库
//                int i = 0;
//                for ( i = 0; i < 3; i++) {
//                    if (1 == userPartnerBindMapper.updateOpenIdByUid("alipay", k, params.get(k))) {
//                        break;
//                    }
//                }
//            }
//            logger.info(String.format("[支付宝uid到openid的初始化]处理{%s}个,时间：%s", params.size(),
//                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
//        }
//        return true;
//    }

    /**
     * 根据支付宝userid获取其openid的值
     * @param bizCon
     * @return
     * @throws AlipayApiException
     */
    private static String getAliOidByUid(OauthPartnerDO partner, String bizCon) throws AlipayApiException {

//        UserPartnerDTO partner = userCacheFacade.getUserPartnerInfo("alipay_fuwu");

        AlipayClient alipayClient = new DefaultAlipayClient(partner.apiUrl, partner.appId,
                partner.priKey, "json", partner.charset);
        AlipayPlatformOpenidGetRequest openidGetRequest = new AlipayPlatformOpenidGetRequest();
        openidGetRequest.setBizContent(bizCon);
        AlipayPlatformOpenidGetResponse openidGetResponse = alipayClient.execute(openidGetRequest);
        return openidGetResponse.getDict();
    }

    /**
     * 通过支付宝的uid换取对应的openid，主要是用于快登和授权登录的用户统一查询
     * @return
     */
//    public static boolean aliOid4Uid() throws AlipayApiException, com.alibaba.dubbo.common.json.ParseException {
//        int i = 0;
//        int pgIndex = 0;
//        int pgSize = 500;
//        String bizCon = "{\"open_ids\":[";
//        List<UserPartnerBindDTO> bindList = userPartnerBindMapper.queryAliNoUidList(0, pgSize);
//        while (bindList != null && bindList.size() > 0) {
//            logger.info("bindlist's size is {}", bindList.size());
//            for (UserPartnerBindDTO bindDTO : bindList) {
//                i++;
//                bizCon += "\"" + bindDTO.getPtOpenId() + "\",";
//                if (i == 50) {
//                    bizCon = bizCon.substring(0, bizCon.length() - 1) + "]}";
//                    batchUpdateAliUserId(bizCon);
//                    i = 0;
//                    bizCon = "{\"open_ids\":[";
//                }
//            }
//            if (i > 0) {
//                bizCon = bizCon.substring(0, bizCon.length() - 1) + "]}";
//                batchUpdateAliUserId(bizCon);
//                i = 0;
//                bizCon = "{\"open_ids\":[";
//            }
//            bindList = userPartnerBindMapper.queryAliNoUidList(pgIndex * pgSize, pgSize);
//        }
//
//        return true;
//    }

    /**
     * 批量更新支付宝的userId
     * @param bizCon
     * @return
     * @throws AlipayApiException
     * @throws com.alibaba.dubbo.common.json.ParseException
     */
//    private static boolean batchUpdateAliUserId(String bizCon) throws AlipayApiException, com.alibaba.dubbo.common.json.ParseException {
//        String result = getAliUidByOid(bizCon);
//        Map<String, String> params = CommonUtil.jsonStrParseMap(result);
//        if (params != null && params.size() > 0) {
//            for (String k : params.keySet()) { //更新userid入库
//                int i = 0;
//                for ( i = 0; i < 3; i++) {
//                    if (1 == userPartnerBindMapper.updateUidByOpenid("alipay", k, params.get(k))) {
//                        break;
//                    }
//                }
//            }
//            logger.info(String.format("[支付宝openid到userid的初始化]处理{%s}个,时间：%s", params.size(),
//                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
//        } else {
//            logger.info("[支付宝openid到userid的初始化]结果为空");
//        }
//        return true;
//    }

    /**
     * 根据openid从支付宝获取userid的值
     * @param bizCon
     * @return
     * @throws AlipayApiException
     */
    private static String getAliUidByOid(String bizCon, OauthPartnerDO partner) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(partner.apiUrl, partner.appId,
                partner.priKey, "json", partner.charset);
        AlipayPlatformUseridGetRequest useridGetRequest = new AlipayPlatformUseridGetRequest();
        useridGetRequest.setBizContent(bizCon);
        AlipayPlatformUseridGetResponse useridGetResponse = alipayClient.execute(useridGetRequest);
        return useridGetResponse.getDict();
    }

    /**
     * 返利网登录验证
     * @param reqMap
     * @return
     */
//    public static UserPartnerBindDTO fanliLoginCheck(Map<String, String> reqMap, int appId) throws ServiceException, UnsupportedEncodingException {
//        String username = URLDecoder.decode(reqMap.get("username"), "gb2312");
//        String userkey = reqMap.get("usersafekey");
//        if (StringUtils.isEmpty(userkey)) { return null; }
//        String uid = reqMap.get("u_id");
//        String nickname = StringUtils.isBlank(reqMap.get("show_name")) ? "" :
//                URLDecoder.decode(reqMap.get("show_name"), "gb2312");
//        UserPartnerBindDTO bindDTO = userPartnerBindMapper.queryPtUinfoByPtOid("51fanli", username);
//        if (bindDTO != null) {
//            if (StringUtils.isEmpty(bindDTO.getPtUserKey())) {
//                //说明用户已经解绑
//                return null;
//            }
//            if (userkey.equals(bindDTO.getPtUserKey())) {
//                //usersafekey验证成功
//                return bindDTO;
//            } else {
//                logger.error("[返利网]验证失败。userSafeKey验证失败。key={}，myDbKey={}", userkey, bindDTO.getPtUserKey());
//            }
//        } else {
//            bindDTO = new UserPartnerBindDTO();
//            bindDTO.setPartnerId("51fanli");
//            bindDTO.setAppId(appId);
//            bindDTO.setPtUserId(uid);
//            bindDTO.setPtOpenId(username);
//            bindDTO.setPtUserKey(userkey);
//            bindDTO.setNickname(URLEncoder.encode(nickname, "UTF-8"));
//            if (1 != userPartnerBindMapper.insertUserPartnerInfo(bindDTO)) {
//                logger.error("[数据库更新]insert fanli login user to db failed, fanlo username is " + username);
//                throw new ServiceException(UserServiceHttpCode.SERVER_ERROR);
//            }
//        }
//
//        return bindDTO;
//    }

}
