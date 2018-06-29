package ssn.lmj.user.service.impl;


import com.lmj.stone.core.algorithm.Base64Util;
import com.lmj.stone.core.algorithm.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syevia on 2014/12/17.
 */
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 校验验证码
     * @param vfcode
     * @return
     */
    public static boolean checkVfCode( String vfcode, boolean isNew) {
        //验证码的验证
        if (vfcode == null || vfcode.isEmpty()) {
            return false;
        }

        /*//暂时屏蔽
        if (isNew) {
            int index = vfcode.indexOf("code=");
            if (index >= 0) {
                try {
                    vfcode = vfcode.substring(0, index) + "code=" + URLEncoder.encode(vfcode.substring(index + 5), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return false;
                }
            } else {
                logger.error("[验证码]输入有误，new。vfcode={}", vfcode);
                return false;
            }
            String checkResp = WebRequestUtil.getResponseString(ApiConfig.getVfcodeCheckUrlNew(), vfcode, false);
            if (checkResp.equals("SUCCESS.\n")) {
                return true;
            }
        } else {
            String requestBody = "action=check&key=&" + vfcode;
            String checkResp = WebRequestUtil.getResponseString(ApiConfig.getVfcodeCheckUrl(), requestBody, false);
            try {
                JSONObject ob = (JSONObject) JSON.parse(checkResp);
                String resp = ob.getString("succeeded");
                if (resp.equals("true"))
                    return true;
            } catch (ParseException ex) {
            }
        }
        */
        return false;
    }

    /**
     * 激活邮件内容
     * @param mailId
     * @return
     */
    public static String actMailContent(String mailId) {
        /*
        Map<String, String> spara = new HashMap<String, String>();
        spara.put("mailId", mailId);
        spara.put("tp", "ACT");//tp为type，ACT表示此链接为激活链接
        spara.put("tm", String.valueOf(DateUtils.addMinutes(new Date(), 1440).getTime()));
        String body = SignUtil.buildRequestUrl(spara, "UTF-8", "MD5", SignUtil.actKey);
        String url = MessageFormat.format(ApiConfig.getActivateUrl(), body);
        String content = MessageFormat.format(StrTeplUtil.activateContent, url, url);
        return content;
        */
        return "";
    }

    /**
     * 重置密码邮件内容
     * @param mailId
     * @return
     */
    public static String resetMailContent(String mailId, long tsp, int appId) {
        /*
        Map<String, String> spara = new HashMap<String, String>();
        spara.put("mailId", mailId);
        spara.put("tp", "RESETPWD");//tp为type，RESETPWD表示重置
        spara.put("tm", String.valueOf(DateUtils.addMinutes(new Date(), 15).getTime()));//重置密码链接15分钟失效
        spara.put("tsp", String.valueOf(tsp));
        String body = SignUtil.buildRequestUrl(spara, "UTF-8", "MD5", SignUtil.resetKey);
        String url = "";
        if (appId == 3) {
            url = MessageFormat.format(ApiConfig.getResetPwdUrl4H5(), body);
        } else {
            url = MessageFormat.format(ApiConfig.getResetPwdUrl(), body);
        }

        return MessageFormat.format(StrTeplUtil.resetPwdContent, url, url);
        */
        return "";
    }

    /**
     * 从请求key1=xx&key2=ddd&..中解析出对应的key,value的map
     * @param req
     * @return
     */
    public static Map<String, String> getMapFromReq(String req) {
        if (req == null || req.isEmpty())
            return null;

        Map<String, String> spara = new HashMap<String, String>();
        List<String> strlist = Arrays.asList(req.split("&"));
        for (String str : strlist) {
            if (str != null && str.indexOf("=") > 0) {
                String key = str.substring(0, str.indexOf("="));
                String value = str.substring(str.indexOf("=")+1);
//                    String value = "";
//                    try {
//                        value = URLDecoder.decode(pair[1], "UTF-8"); //
//                    } catch (Throwable throwable) {}
                spara.put(key, value);

            }
        }
        return spara;
    }

    /**
     * 从请求key1=xx&key2=ddd&..中得到decode之后的req
     * @param req
     * @return
     */
    public static String getDecodeReq(String req) {
        if (req == null || req.isEmpty())
            return null;

        String result = "";
        String[] strlist = req.split("&");
        for (String str : strlist) {
            if (str != null) {
                String[] pair = str.split("=");
                if (pair.length == 2) {
                    String key = pair[0];
                    String value = "";
                    try {
                        value = URLDecoder.decode(pair[1], "UTF-8"); //
                    } catch (Throwable throwable) {}
                    result += key + "=" + value + "&";
                }
            }
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 证件号码隐藏
     * @param type
     * @param credtNum
     * @return
     */
    public static String hidKeys(String type, String credtNum) {
        if (credtNum == null || credtNum.isEmpty())
            return "";
        StringBuilder result = new StringBuilder();
        String pre = "";
        String end = "";
        int len = credtNum.length();
        if (type.equals("ID") && len < 18)
            return result.append(credtNum).toString();

        pre = credtNum.substring(0, 3);
        end = credtNum.substring(len-4);
        result.append(pre);
        for (int i = 0; i < len-7; i++)
            result.append("*");
        result.append(end);
        return result.toString();
    }

    public static final String DEFAULT_NICK = "海淘用户";

    /**
     * 格式化昵称
     * update by yanjinjin 2015-01-16 将原先：若昵称的值为null或空字符串设置默认昵称为“海淘用户”，改为“”
     * @param nick
     * @return
     */
    public static String formatNick(String nick) throws UnsupportedEncodingException {
        String nickFormat = "";
        if (nick == null || nick.length() == 0) {
            nickFormat = DEFAULT_NICK;
        } else {
            if (nick.length() > 20) { //update om 2015-10-12
                nick = nick.substring(0, 20);
            }
            nickFormat = URLEncoder.encode(nick, "UTF-8");
        }
        return nickFormat;
    }

    /**
     * 格式化用户问候语
     * @param nick 昵称
     * @param accountId 登录账户
     * @return 返回问候语
     */
    public static String formatUserGreeting(String nick, String accountId, String accountType) {
        String result = "";
        boolean isNickOk = true;
        if (nick != null && nick.length() > 0) {
            try {
                result = URLDecoder.decode(nick, "UTF-8");
                if ("海淘访客".equals(result) || "海淘用户".equals(result)) {
                    isNickOk = false;
                }
            } catch (UnsupportedEncodingException e) {
            }
        } else {
            isNickOk = false;
        }

        if (!isNickOk) {
            if (accountId != null && accountId.length() > 0) {
                if ("MAIL".equals(accountType)) {
                    int flag = accountId.indexOf("@");
                    result = flag > 3 ? accountId.substring(0, 3).concat("...").concat(accountId.substring(flag)) : accountId;
                } else if ("MOBILE".equals(accountType)) {
                    result = accountId.substring(0, 3) + "...." + accountId.substring(7);
                }
            } else {
                result = "海淘用户"; //若是未绑定账户的第三方用户，就用默认
            }
        }

        return result;
    }

    /**
     * 格式化用户问候语
     * @param nick 昵称
     * @param mobile 手机号
     * @param mailId 邮箱
     * @param flag 标示，0 表示需要....;1表示需要****
     * @return 返回问候语
     */
    public static String formatUserGreeting(String nick, String mobile, String mailId, int flag) {
        String result = "";
        String padding = "....";
        if (flag == 1) {
            padding = "****";
        }
        boolean isNickOk = true;
        if (nick != null && nick.length() > 0) {
            try {
                result = URLDecoder.decode(nick, "UTF-8");
                if ("海淘访客".equals(result) || "海淘用户".equals(result)) {
                    isNickOk = false;
                }
            } catch (UnsupportedEncodingException e) {
            }
        } else {
            isNickOk = false;
        }

        if (!isNickOk) {
            if (mobile != null && mobile.length() > 0) {
                result = mobile.substring(0, 3) + padding + mobile.substring(7);
            } else if (mailId != null && mailId.length() > 0) {
                int pos = mailId.indexOf("@");
                result = pos > 3 ? mailId.substring(0, 3) + padding + mailId.substring(pos) : mailId;
            } else {
                result = "海淘用户"; //实在是什么都没有吗
            }
        }

        return result;
    }

    /**
     * 支付宝账户隐藏
     * @param aliact
     * @return
     */
    public static String formatAliact(String aliact) {
        String result = "";
        if (aliact == null || aliact.length() == 0) {
            return null;
        }
        int flag = aliact.indexOf("@");
        if (flag > 0) {
            result = flag > 5 ? aliact.substring(0, 5).concat("***").concat(aliact.substring(flag)) : aliact;
        } else if (PhoneNumUtil.isMobileNum(aliact)) {
            result = aliact.substring(0, 3) + "****" + aliact.substring(7);
        }
        return result;
    }

    /**
     * 邮箱账户隐藏
     * @param mailId
     * @return
     */
    public static String hideMailId(String mailId) {
        String result = "";
        if (mailId == null || mailId.length() == 0) {
            return null;
        }
        int flag = mailId.indexOf("@");
        if (flag > 0) {
            result = flag > 3 ? mailId.substring(0, 3).concat("****").concat(mailId.substring(flag)) : mailId;
        } else {
            return null;
        }

        return result;
    }

    /**
     * 手机号隐藏
     * @param mobile
     * @return
     */
    public static String formatMobile(String mobile) {
        if (mobile ==  null || mobile.length() == 0) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 邮箱账户隐藏
     * @param mailId
     * @return
     */
    public static String formatMailId (String mailId) {
        int flag = mailId.indexOf("@");
        if (flag > 0) {
            return flag > 3 ? mailId.substring(0, 3).concat("***").concat(mailId.substring(flag)) : mailId;
        }
        return mailId;
    }

    private static final int[] wi = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    private static final String[] ck = {"1","0","X","9","8","7","6","5","4","3","2"};
    private static Pattern id18 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$");
    private static Pattern id15 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");

    /**
     * 身份证号校验
     * @param credtNum
     * @return
     */
    public static boolean checkCredtNum(String credtNum) {
        if (credtNum == null || credtNum.length() != 18) {
            return false;
        }

        if (!id18.matcher(credtNum).matches()) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < credtNum.length() - 1; i++) {
            sum += Integer.parseInt(String.valueOf(credtNum.charAt(i))) * wi[i];
        }

        String ckValue = ck[sum % 11];//获取校验位的值
        if (!ckValue.equals(credtNum.substring(17).toUpperCase())) {
            return false;
        }
        return true;
    }

    private static Pattern pattern= Pattern.compile("^\\d{6}$"); //邮编简单规则
    /**
     * 验证字符串是否全是由数字组成的
     * @param zipCode
     * @return
     */
    public static boolean checkZipCode(String zipCode) {
        if ( zipCode == null)
            return false;

        Matcher m = pattern.matcher(zipCode);
        return m.matches();
    }

    private static Pattern patternUid = Pattern.compile("^[0-9]*$");
    public static boolean checkNum(String srcUid) {
        if (srcUid == null || srcUid.length() == 0) {
            return false;
        }

        return patternUid.matcher(srcUid).matches();
    }

    /**
     * emoji表情过滤
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++)
        {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint))
            {
                buf.append(codePoint);
            }
        }
        return buf.toString();
    }

    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * json字符串parse为map，针对比较简单的json entity
     * @param jsonStr
     * @return
     */
    public static Map<String, String> jsonStrParseMap(String jsonStr) {
        Map<String, String> map = new HashMap<String, String>();
        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(jsonStr);
        for (String k : json.keySet()) {
            Object v = json.get(k);
            map.put(k, (String) v);
        }
        return map;
    }

    /**
     * json parse map
     * @param json
     * @return
     */
    public static Map<String, String> jsonParseMap(com.alibaba.fastjson.JSONObject json) {
        Map<String, String> map = new HashMap<String, String>();
        for (String k : json.keySet()) {
            Object v = json.get(k);
            map.put(k, (String) v);
        }
        return map;
    }

    private static final String[] unValidStr = {"女士","先生","小姐","夫人","男士"};

    /**
     * 检查收货人姓名是否合理
     * @param rcvName
     * @return
     */
    public static boolean checkRcverName(String rcvName) {
        if (rcvName == null || rcvName.length() == 0) {
            return true;
        }
        if (rcvName.length() == 1){
            return false;
        }
        for (String str : unValidStr) {
            if (rcvName.contains(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 完成paswd包装逻辑
     * @param orgPaswd
     * @return
     */
    public static String formatPswd(String orgPaswd) {
        if (orgPaswd == null || orgPaswd.length() == 0) {
            return "";
        }
        if (orgPaswd.length() != 32) {
            return orgPaswd;
        }
        String disPaswd = "";
        try {
            disPaswd = MD5.computeToHex(orgPaswd.getBytes("UTF-8"));//再次md5
        } catch (UnsupportedEncodingException e) {
            logger.error("format paswd fail.", e);
        }
        return disPaswd;
    }

    public static final String wechatHeadUrlPrefix = "http://wx.qlogo.cn";



    /**
     * 简单异或实现
     * @param source 预加密字符串
     * @param key 加密key
     * @return 返回加密后的结果
     */
    public static String simpleXor(String source, String key) {
        if (source == null || source.length() == 0 || key == null || key.length() == 0) {
            return null;
        }
        int j = 0;
        byte[] byteKey = key.getBytes();
        byte[] byteSource = source.getBytes();
        for(int i = 0; i < byteSource.length; i++) {
            byteSource[i] = (byte)(byteSource[i] ^ byteKey[j]);
            j++;
            j = j % byteKey.length;
        }

        return Base64Util.encodeToString(new String(byteSource).getBytes());
    }

    /**
     * 生成积分操作的交易单号
     * @return
     */
    public static String genTransNo(String opType) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String random = "00" + (int) (Math.random() * 100);
        return opType + format.format(new Date()) + random.substring(random.length() -3);
    }

}
