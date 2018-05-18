package ssn.lmj.user.service;

import com.lmj.stone.idl.IDLException;

/**
 * Created by lingminjun on 17/4/22.
 */
public final class AuthExceptions {

    public final static String IDL_EXCEPTION_DOMAIN = "auth";

    public final static int NOT_FOUND_ACCOUNT_ERROR_CODE = -100;
    public static IDLException NOT_FOUND_ACCOUNT_ERROR(String reason) {
        return new IDLException("用户名或者密码错误",IDL_EXCEPTION_DOMAIN,NOT_FOUND_ACCOUNT_ERROR_CODE,reason);
    }

    public final static int NOT_FOUND_USER_ERROR_CODE = -101;
    public static IDLException NOT_FOUND_USER_ERROR(String reason) {
        return new IDLException("用户不存在",IDL_EXCEPTION_DOMAIN,NOT_FOUND_USER_ERROR_CODE,reason);
    }

}