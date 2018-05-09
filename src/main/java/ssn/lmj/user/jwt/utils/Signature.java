package ssn.lmj.user.jwt.utils;


/**
 * Created by lingminjun on 17/4/13.
 */
public final class Signature {

    public static final String CRC16 = "crc16";
    public static final String CRC32 = "crc32";
    public static final String MD5 = "md5";
    public static final String SHA1 = "sha1";
    public static final String HMAC = "hmac";
    public static final String RSA = "rsa";
    public static final String ECC = "ecc";

    public static Signable getSignable(String algorithm, String pubKey, String priKey) {
        if (CRC16.equalsIgnoreCase(algorithm)) {
            return new CRC16Helper(pubKey);
        } else if (CRC32.equalsIgnoreCase(algorithm)) {
            return new CRC32Helper(pubKey);
        } else if (MD5.equalsIgnoreCase(algorithm)) {
            return new Md5Helper(pubKey);
        } else if (SHA1.equalsIgnoreCase(algorithm)) {
            return new SHAHelper(pubKey);
        } else if (HMAC.equalsIgnoreCase(algorithm)) {
            return new HMacHelper(pubKey);
        } else if (RSA.equalsIgnoreCase(algorithm)) {
            return new RsaHelper(pubKey,priKey);
        } else if (ECC.equalsIgnoreCase(algorithm)) {
            return new EccHelper(pubKey,priKey);
        }
        //默认sha1
        return new SHAHelper(pubKey);
    }
}
