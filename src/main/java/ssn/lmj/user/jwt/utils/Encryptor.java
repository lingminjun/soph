package ssn.lmj.user.jwt.utils;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-09
 * Time: 上午11:28
 */
public final class Encryptor {

    public static final String AES = "aes";
    public static final String RSA = "rsa";
    public static final String ECC = "ecc";

    public static Encryptable getSignable(String algorithm, String pubKey, String priKey) {
        if (AES.equalsIgnoreCase(algorithm)) {
            return new AesHelper(priKey);
        } else if (RSA.equalsIgnoreCase(algorithm)) {
            return new RsaHelper(pubKey,priKey);
        } else if (ECC.equalsIgnoreCase(algorithm)) {
            return new EccHelper(pubKey,priKey);
        }
        //默认sha1
        return new AesHelper(priKey);
    }
}
