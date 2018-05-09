package ssn.lmj.user.jwt.utils;

/**
 * Created by lingminjun on 17/4/13.
 */
public interface Signable {
    String sign(byte[] content);
    boolean verify(String sign, byte[] content);
}
