package ssn.lmj.user.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syevia on 2014/12/15.
 */
public class PhoneNumUtil {
    private void PhoneNumUtil() {}

    private static Pattern pattern= Pattern.compile("^(\\+86)?1[123456789]\\d{9}$");

    public static boolean isMobileNum(String mobile) {
        if (mobile == null || mobile.length() == 0) {
            return false;
        }

        Matcher m = pattern.matcher(mobile.trim());
        return m.matches();
    }

    public static boolean checkMobileFormat(String mobile) {
        int size = mobile.length();
        for (int i = 0; i < size; i++) {
            char c = mobile.charAt(i);
            if (i == 0 && c != '+') {
                return false;
            } else if ((i <= 1 || i + 1 >= size) && c == '-') {
                return false;
            } else if (c != '+' && c != '-' && c < '0' && c > '9') {
                return false;
            }
        }
        return true;
    }

    public static String tidyChinaMobile(String mobile) {
        String str = mobile;
        if (mobile.startsWith("+86")) {
            str = mobile.substring(3,mobile.length());
            if (str.startsWith("-")) {
                str = str.substring(1,str.length());
            }
        }
        return "+86-" + str;
    }

    /**
     * 生成6位的短信验证码
     * @return
     */
    public static int genSmsCode() {
        int code = ((int)(Math.random() * 9) + 1) * 100000 + (int)(Math.random() * 90000);
        return code;
    }
}
