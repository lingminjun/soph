package ssn.lmj.user.service.impl;

import com.lmj.stone.core.algorithm.MD5;
import com.lmj.stone.utils.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssn.lmj.user.db.dobj.OauthPartnerDO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by syevia on 2014/11/13.
 */
public class SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);
    public static final String actKey = "sfhaitaodev!activate@mail"; //激活链接签名salt
    public static final String resetKey = "sfhaitaodev!resetpwd@mail"; //重置密码链接签名salt   主要是为了防止收到两个链接的人互相拼接使用
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean needSort) {
        //Map<String, String> spara = paraFilter(params);
        List<String> keys = new ArrayList<String>(params.keySet());
        if (needSort) {
            Collections.sort(keys);
        }

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     * @throws UnsupportedEncodingException
     */
    public static String buildRequestMysign(Map<String, String> sPara, String charset, String signType, String signKey, boolean needSort) {
        Map<String, String> spara = paraFilter(sPara);
        String prestr = createLinkString(spara, needSort); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        logger.info("alipay prestr: " + prestr);
        String mysign = "";
        String content = "";
        if(signType.equals("MD5") ) {
            content = prestr + signKey;
            mysign = MD5.computeToHex(getContentBytes(content, charset));
        } else if (signType.equals("RSA")) {
            mysign = RSA.sign(prestr, signKey, charset);
        }
        return mysign;
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws java.security.SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     *
     * @param sParamTemp
     * @throws UnsupportedEncodingException
     * @return
     */
    public static String buildRequestUrl(Map<String, String> sParamTemp, String charset, String signType, String signKey) {
        StringBuilder sb = new StringBuilder();
        String mysign = buildRequestMysign(sParamTemp, charset, signType, signKey, true);

        try {
            for(String paraKey: sParamTemp.keySet()) {
                String value = sParamTemp.get(paraKey);
                if(value == null || value.trim().length() ==0){
                    continue;
                }
                sb = sb.append(paraKey).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
            }
            sb.append("sign=").append(URLEncoder.encode(mysign,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     *
     * @param sParamTemp
     * @throws UnsupportedEncodingException
     * @return
     */
    public static String buildRequestUrlForApp(Map<String, String> sParamTemp, String charset, String signType, String signKey) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> sPara = paraFilter(sParamTemp);
        //因为移动支付签名要求无序，故orderFlag为false
        String prestr = createLinkString(sPara, false);
        logger.info(prestr);
        String mysign = RSA.sign(prestr, signKey, charset);
        try {
            for (String paraKey : sPara.keySet()) {
                String value = sPara.get(paraKey);
                if (value == null || value.trim().length() == 0) {
                    continue;
                }
                sb = sb.append(paraKey).append("=").append(value).append("&");
            }
            sb.append("sign=").append("\"").append(URLEncoder.encode(mysign, charset)).append("\"").append("&sign_type=").append("\"").append(signType).append("\"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取远程服务器ATN结果,用于向支付宝询问此次回调是否是支付宝发出
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String verifyResponse(String verifyUrl,String notify_id, String partner) throws UnsupportedEncodingException {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String verify_url = verifyUrl + "&partner=" + partner + "&notify_id=" + notify_id;
        logger.info("alipay verify notify_id, request=" + verify_url);
        return checkUrl(verify_url);
    }

    /**
     * 获取远程服务器ATN结果
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String checkUrl(String urlvalue) {
        String inputLine = "";
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                    .getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }

        return inputLine;
    }

    /**
     * 验证消息是否是支付宝发出的合法消息
     * 支付宝移动支付所需 add by yanjinjin 2014-12-24
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params, OauthPartnerDO info, boolean needSort) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = "true";
        if(params.get("notify_id") != null) {
            String notify_id = params.get("notify_id");
            try {
                responseTxt = verifyResponse(info.verifyUrl, notify_id, info.appId);
                logger.info("alipay notify verify is " + responseTxt);
            } catch (UnsupportedEncodingException ex) {
                logger.error("UnsupportedEncodingException, ex");
            }
        }
        String sign = "";
        if(params.get("sign") != null) {sign = params.get("sign");}
        boolean isSign = getSignVerify(params, sign, info, needSort);
        if (!isSign) {
            logger.error("sign verify failed");
        }

        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * 支付宝移动支付所需 add by yanjinjin 2014-12-24
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    public static boolean getSignVerify(Map<String, String> Params, String sign, OauthPartnerDO info, boolean needSort) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(Params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew, needSort);
        //获得签名验证结果
        boolean isSign = false;
        if("RSA".equals(Params.get("sign_type"))) {
            logger.info("RSA, prestr=" + preSignStr);
            isSign = RSA.verify(preSignStr, sign, info.pubKey, info.charset);
        } else if ("MD5".equals(Params.get("sign_type"))) {
            if (sign.equals(buildRequestMysign(sParaNew, info.charset, "MD5", info.appKey, needSort)))
                isSign = true;
        }

        return isSign;
    }

    private static final String charDigits[] = { "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P","Q","R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String forInviteCode(byte[] by, int len) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int n = by[i];
            if (n < 0)
                n += 256;
            result = result + charDigits[n%32];
        }
        return result;
    }


}
