package ssn.lmj.user.jwt.utils;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-09
 * Time: 上午11:18
 */
public interface Encryptable {
    byte[] encrypt(byte[] content);
    byte[] decrypt(byte[] secret);
}
