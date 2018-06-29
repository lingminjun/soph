package ssn.lmj.user.service;

import com.lmj.stone.idl.IDLException;

/**
 * Created by lingminjun on 17/4/22.
 */
public final class Exceptions {

    public final static String IDL_EXCEPTION_DOMAIN = "auth";

    public final static int NO_PERMISSION_ERROR_CODE = -400;
    public static IDLException NO_PERMISSION_ERROR(String reason) {
        return new IDLException("您没有访问权限",IDL_EXCEPTION_DOMAIN,NO_PERMISSION_ERROR_CODE,reason);
    }

    public final static int NOT_FOUND_ACCOUNT_ERROR_CODE = -100;
    public static IDLException NOT_FOUND_ACCOUNT_ERROR(String reason) {
        return new IDLException("用户名或者密码错误",IDL_EXCEPTION_DOMAIN,NOT_FOUND_ACCOUNT_ERROR_CODE,reason);
    }

    public final static int NOT_FOUND_USER_ERROR_CODE = -101;
    public static IDLException NOT_FOUND_USER_ERROR(String reason) {
        return new IDLException("用户不存在",IDL_EXCEPTION_DOMAIN,NOT_FOUND_USER_ERROR_CODE,reason);
    }

    public final static int NOT_CREATE_USER_ERROR_CODE = -102;
    public static IDLException NOT_CREATE_USER_ERROR(String reason) {
        return new IDLException("无法创建用户",IDL_EXCEPTION_DOMAIN,NOT_CREATE_USER_ERROR_CODE,reason);
    }

    public final static int NOT_FOUND_CAPTCHA_CODE_ERROR_CODE = -103;
    public static IDLException NOT_FOUND_CAPTCHA_CODE_ERROR(String reason) {
        return new IDLException("验证码错误",IDL_EXCEPTION_DOMAIN,NOT_FOUND_CAPTCHA_CODE_ERROR_CODE,reason);
    }

    public final static int FOUND_SMSCODE_EXPIRED_CODE = -104;
    public static IDLException FOUND_SMSCODE_EXPIRED(String reason) {
        return new IDLException("验证码已过期",IDL_EXCEPTION_DOMAIN,FOUND_SMSCODE_EXPIRED_CODE,reason);
    }

    public final static int FOUND_SMSCODE_OVERMUCH_CODE = -105;
    public static IDLException FOUND_SMSCODE_OVERMUCH(String reason) {
        return new IDLException("验证码次数过多，请稍后再试",IDL_EXCEPTION_DOMAIN,FOUND_SMSCODE_OVERMUCH_CODE,reason);
    }

    public final static int MOBILE_FORMAT_ERROR_CODE = -106;
    public static IDLException MOBILE_FORMAT_ERROR(String reason) {
        return new IDLException("手机号格式错误",IDL_EXCEPTION_DOMAIN,MOBILE_FORMAT_ERROR_CODE,reason);
    }

    public final static int THIRD_PARTY_NOT_SUPPORT_ERROR_CODE = -107;
    public static IDLException THIRD_PARTY_NOT_SUPPORT_ERROR(String reason) {
        return new IDLException("不支持的三方登录平台",IDL_EXCEPTION_DOMAIN,THIRD_PARTY_NOT_SUPPORT_ERROR_CODE,reason);
    }

    public final static int ACCOUNT_MOBILE_MATCH_ERROR_CODE = -108;
    public static IDLException ACCOUNT_MOBILE_MATCH_ERROR(String reason) {
        return new IDLException("与账号绑定手机不一致",IDL_EXCEPTION_DOMAIN,ACCOUNT_MOBILE_MATCH_ERROR_CODE,reason);
    }

    public final static int MOBILE_ACCOUNT_EXIST_USER_ERROR_CODE = -109;
    public static IDLException MOBILE_ACCOUNT_EXIST_USER_ERROR(String reason) {
        return new IDLException("手机号已被其他账号使用",IDL_EXCEPTION_DOMAIN,MOBILE_ACCOUNT_EXIST_USER_ERROR_CODE,reason);
    }

    public final static int NOT_FOUND_THIRD_PARTY_PLATFORM_ERROR_CODE = -110;
    public static IDLException NOT_FOUND_THIRD_PARTY_PLATFORM_ERROR(String reason) {
        return new IDLException("未找到合作的第三方平台信息",IDL_EXCEPTION_DOMAIN,NOT_FOUND_THIRD_PARTY_PLATFORM_ERROR_CODE,reason);
    }

    public final static int THIRD_PARTY_AUTH_ERROR_CODE = -111;
    public static IDLException THIRD_PARTY_AUTH_ERROR(String reason) {
        return new IDLException("第三方平台验证失败",IDL_EXCEPTION_DOMAIN,THIRD_PARTY_AUTH_ERROR_CODE,reason);
    }

    public final static int BAD_REQUEST_ERROR_CODE = 400;
    public static IDLException BAD_REQUEST_ERROR(String reason) {
        return new IDLException("请求异常",IDL_EXCEPTION_DOMAIN,BAD_REQUEST_ERROR_CODE,reason);
    }

    //过期token
    public final static int INVALID_TOKEN_ERROR_CODE = 401;
    public static IDLException INVALID_TOKEN_ERROR(String reason) {
        return new IDLException("令牌已失效",IDL_EXCEPTION_DOMAIN,INVALID_TOKEN_ERROR_CODE,reason);
    }

    public final static int FORBIDDEN_REQUEST_ERROR_CODE = 404;
    public static IDLException FORBIDDEN_REQUEST_ERROR_(String reason) {
        return new IDLException("非法请求",IDL_EXCEPTION_DOMAIN,FORBIDDEN_REQUEST_ERROR_CODE,reason);
    }

    public final static int REFRESH_TOKEN_ERROR_CODE = 404;
    public static IDLException REFRESH_TOKEN_ERROR(String reason) {
        return new IDLException("刷新令牌验证失败",IDL_EXCEPTION_DOMAIN,REFRESH_TOKEN_ERROR_CODE,reason);
    }
}
