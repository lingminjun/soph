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

}
