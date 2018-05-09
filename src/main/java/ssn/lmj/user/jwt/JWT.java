package ssn.lmj.user.jwt;

import com.alibaba.fastjson.JSON;
import ssn.lmj.user.jwt.utils.*;
import ssn.lmj.user.jwt.utils.Signature;

import java.nio.charset.Charset;
import java.security.*;

/**
 * Created with IntelliJ IDEA.
 * Description: JSON Web Token (JWT) header.claims.signature
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午10:00
 */
public final class JWT {
    public static final String UTF_8                 = "utf-8";
    public static final Charset UTF8                 = Charset.forName(UTF_8);

    // 算法
    public enum Algorithm {

        CRC16("crc16"),
        CRC32("crc32"),
        MD5("md5"),
        SHA1("sha1"),
        HMAC("hmac"),
        RSA("rsa"),
        ECC("ecc");

        public final String name;
        Algorithm(String name) { this.name=name; }

        //命中
        public boolean hit(String algorithm) {
            return name.equalsIgnoreCase(algorithm);
        }
    }

    enum Grant {
        None(0),        //无权限
        Integrated(3),  //服务合作方
        Device(6),      //终端设备注册
        OAuth(9),       //终端合作方
        Cross(12),       //SSO 用于跨域免认证
        User(15),        //用户认证
        Merchant(18),    //MC 合作企业后台用户 Merchant Controller
        Admin(21);       //AC 系统管理员

        public final int code;
        Grant(int code) {
            this.code = code;
        }
    }

    static public class Head {

        public final Algorithm alg; //加密方式
        public final Grant  grt; //grant 授予作用，自行定义 ：did(设备注册)
        public final long   exp; //过期时间点
        public final String iss; //jwt签发者、一般放域名 lmj.com 与 domain一个含义

        private Head(Algorithm alg, Grant grt, Long exp, String iss) {
            this.alg = alg;
            this.grt = grt;
            this.exp = exp == null ? 0 : exp.longValue();
            this.iss = iss;
        }
    }

    static public class Body {
        public final String aid; //应用id
        public final String did; //设备id
        public final String uid; //用户id
        public final String oid; //open id
        public final String pid; //partner id
        public final String key;  //下发到客户端的公钥,与token中csrf对应（用于客户端回传服务端验证签名）
        public final String nk;  //用户名（实际没啥作用）

        private Body(String aid,String did,String uid,String oid,String pid,String key,String nk) {
            this.aid = aid;
            this.did = did;
            this.uid = uid;
            this.oid = oid;
            this.pid = pid;
            this.key = key;
            this.nk = nk;
        }
    }

    static public class Sign {
        public final Algorithm stp; //签名方式 CRC32
        public final String sgn; //签名

        private Sign(Algorithm alg, String sgn) {
            this.stp = alg;
            this.sgn = sgn;
        }
    }

    public final Head head; //必须存在
    public final Body body; //必须存在
    public final Sign sign; //签名，防篡改，意义不大，可为空

    private JWT(Head head,Body body,Sign sign) {
        this.head = head;
        this.body = body;
        this.sign = sign;
    }

    public String toCipher(String pubKey) {
        StringBuilder builder = new StringBuilder();

        Builder.appendToJSON(builder,"l",head.alg.name);
        Builder.appendToJSON(builder,"g",head.grt.code);
        Builder.appendToJSON(builder,"e",head.exp);
        Builder.appendToJSON(builder,"i",head.iss);

        Builder.appendToJSON(builder,"a",body.aid);
        Builder.appendToJSON(builder,"d",body.did);
        Builder.appendToJSON(builder,"u",body.uid);
        Builder.appendToJSON(builder,"o",body.oid);
        Builder.appendToJSON(builder,"p",body.pid);
        Builder.appendToJSON(builder,"k",body.key);
        Builder.appendToJSON(builder,"n",body.nk);

        Builder.appendToJSON(builder,"t",sign == null ? null : sign.stp.name);
        Builder.appendToJSON(builder,"s",sign == null ? null : sign.sgn);

        builder.insert(0,"{");
        builder.append("}");

        Encryptable encryptor = Encryptor.getSignable(head.alg.name,pubKey,null);

//        byte[] data = GZIP.compress(builder.toString(),UTF_8);
        byte[] data = builder.toString().getBytes(UTF8);

        byte[] cipher = encryptor.encrypt(data);
        return Base64Util.encodeToString(cipher);
    }

    static private class Scanner {
        public Algorithm l; //加密方式
        public Grant  g; //grant 授予作用，自行定义 ：did(设备注册)
        public long   e; //过期时间点
        public String i; //jwt签发者

        public String a; //应用id
        public String d; //设备id
        public String u; //用户id
        public String o; //open id
        public String p; //partner id
        public String k;  //下发到客户端的公钥,与token中csrf对应（用于客户端回传服务端验证签名）
        public String n;  //用户名（实际没啥作用）

        public Algorithm t; //签名方式 CRC32
        public String s; //签名

        public JWT toJWT() {
            return new JWT(new Head(l,g,e,i),new Body(a,d,u,o,p,k,n),s == null ? null : new Sign(t,s));
        }
    }

    static public class Builder {
        private Algorithm alg = Algorithm.RSA;
        private Grant grt = Grant.User;
        private Long exp;
        private String iss;
        private Algorithm stp = Algorithm.CRC32;
        private String aid; //应用id
        private String did; //设备id
        private String uid; //用户id
        private String oid; //open id
        private String pid; //partner id
        private String key;  //下发到客户端的公钥,与token中csrf(授予的私钥)对应（用于客户端回传服务端验证签名）
        private String nk;  //用户名（实际没啥作用）
        private String salt; //签名秘钥（加盐）
        private String cipher;//密文
        private String pubKey;//公钥
        private String priKey;//私钥

        //设置加密算法
        public Builder setEncryptAlgorithm(Algorithm algorithm) {
            alg = algorithm;
            return this;
        }

        //设置jwt作用范围，或者说jwt的权限
        public Builder setJWTGrant(Grant grant) {
            grt = grant;
            return this;
        }

        //设置过期时长，设置小于零表示不过期 单位秒(s)
        public Builder setAging(int aging) {
            if (aging > 0) {
                exp = System.currentTimeMillis() / 1000 + aging;
            }
            return this;
        }

        // 设置颁发者所在域
        public Builder setIssue(String domain) {
            iss = domain;
            return this;
        }

        public Builder setUserId(String userId) {
            uid = userId;
            return this;
        }

        public Builder setApplicationId(String appId) {
            aid = appId;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            did = deviceId;
            return this;
        }

        public Builder setOpenId(String openId) {
            oid = openId;
            return this;
        }

        public Builder setPartnerId(String partnerId) {
            pid = partnerId;
            return this;
        }

        // 颁发的公钥（隐藏公钥，主要是返回到验证机上进行验证签名）
        public Builder setIssuedPublicKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setNick(String nick) {
            this.nk = nick;
            return this;
        }

        public Builder setSignatureAlgorithm(Algorithm algorithm) {
            stp = algorithm;
            return this;
        }

        public Builder setSignSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public Builder setCiphertext(String ciphertext) {
            this.cipher = ciphertext;
            return this;
        }

        public Builder setEncryptKey(String pubkey,String priKey) {
            this.pubKey = pubkey;
            this.priKey = priKey;
            return this;
        }

        private static void appendToJSON(StringBuilder builder, String key, Object value) {
            if (key == null || key.length() == 0 || value == null) {
                return;
            }

            if (value instanceof String) {
                String v = (String)value;
                if (v == null || v.length() == 0) {
                    return;
                }
            }

            if (builder.length() > 1) {
                builder.append(",");
            }

            if (value instanceof String) {
                builder.append("\"" + key + "\":\"" + value + "\"");
            } else {
                builder.append("\"" + key + "\":" + value);
            }
        }

        public JWT build() {

            if (cipher != null && cipher.length() > 0) {

                if (priKey == null || priKey.length() == 0) {
                    throw new RuntimeException("无法创建JWT,请设置加密公私秘钥");
                }

                Encryptable encryptor = Encryptor.getSignable(alg.name,pubKey,priKey);
                byte[] data = Base64Util.decode(cipher);
//                byte[] text = GZIP.uncompress(encryptor.decrypt(data));
                byte[] text = encryptor.decrypt(data);
                Scanner scanner = JSON.parseObject(text,Scanner.class);
                return scanner.toJWT();
            }

            StringBuilder builder = new StringBuilder();

            appendToJSON(builder,"l",alg.name);
            appendToJSON(builder,"g",grt.code);
            appendToJSON(builder,"e",exp);
            appendToJSON(builder,"i",iss);

            Head head = new Head(alg,grt,exp,iss);

            appendToJSON(builder,"a",aid);
            appendToJSON(builder,"d",did);
            appendToJSON(builder,"u",uid);
            appendToJSON(builder,"o",oid);
            appendToJSON(builder,"p",pid);
            appendToJSON(builder,"k",key);
            appendToJSON(builder,"n",nk);

            Body body = new Body(aid,did,uid,oid,pid,key,nk);

            Sign sign = null;
            if (salt != null && salt.length() > 0) {

                Signable s = Signature.getSignable(stp.name, salt,null);
                String sgn = s.sign(builder.toString().getBytes(UTF8));
                sign = new Sign(stp,sgn);

//                appendToJSON(builder,"t",stp.name);
//                appendToJSON(builder,"s",sgn);
            }

//            builder.insert(0,"{");
//            builder.append("}");

            return new JWT(head,body,sign);
        }

    }

    //head
    /*{
        "alg" : "AES256",
         "typ" : "JWT",
         "exp" : "3000"
    }*/

    //claim
    /*{
        "sub": "1234567890",
        "name": "John Doe",
        "admin": true
    }*/

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException {

        {
            String pubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoIjY+VacM/v0q47oQkbE4eVo4AS/Px07EMCmlYmRjY9x1OeippSppQ1eNRIuFCbZRqpMoayDO68UdWPCSqOt1I8Uw03MzVDmy38ZBo6dVTRrqWW9z7vbQQ1nWkEcUWcRTIQIktQ2ptO4AOlZa1x1/zvsNBodTNqhqCGPeTNUwyQIDAQAB";
            String prikey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOgiNj5Vpwz+/SrjuhCRsTh5WjgBL8/HTsQwKaViZGNj3HU56KmlKmlDV41Ei4UJtlGqkyhrIM7rxR1Y8JKo63UjxTDTczNUObLfxkGjp1VNGupZb3Pu9tBDWdaQRxRZxFMhAiS1Dam07gA6VlrXHX/O+w0Gh1M2qGoIY95M1TDJAgMBAAECgYEAy6jIYmQPTc2hHDHzmnnYPC3rw1r3MZoxNtryjtEGNlT0pDyMLdpknTmh5KUQq0XcGUZIGZvzitc7dwYC+wkMmWsEQXMt3pYGaNDvhz967zrGvHk7NX6KDqK7ExsBRsjGdoXd8ohZjt4DsN2egrqhASr5iKd7G9pJuKQLuRWMwAkCQQD+hb4pQ4rixQ7qkDVCqRjrsFM6YBWwRTFL0HRkVeejcV8MKVtnWWh0pDwj1J44qZ+3mFj9YAITvdu63xNvjHJHAkEA6XsyMpTzEtbWV+QZFNJlI3CqqIXL/vTPbnWrV0L0unfSZMXe/nPHnYtM4mP+YjkEN7cab08R/wO5ptb37yE8bwJASLSpwp3Rsb+66BRzsmwde04uKDHEYEsTruWIKhVECzNahF3YB2jJ5u/3YgDdhbAmuMpjOVXQohUNDI+mvKmxBwJAGyorP/lWgqWeUKGITYThIYt1P6A5iNNg4wArzD5NDPjt8K2Y8U/1NJ5Fdr2dlj3+AKF8IOE5PctoKUIvnJHoXwJBAJFFf3kuqC0JhfelmlaLV1atcCuCp8QtvyHgtOE3IuDobhgwThVZYOc066/fzEqjQRrGs4GKmjN+SPH92WHAXqE=";


            Builder builder = new Builder();
            builder.setAging(3600 * 24);
            builder.setApplicationId("4");
            builder.setDeviceId("21434323243433");
            builder.setUserId("76534");
            builder.setIssuedPublicKey("bY7813NzNt548KAC4QI+PwpK1khDkWPQC+SHbT1njRs=");
            builder.setSignSalt("lmj.xxxx.com");
            builder.setNick("杨世亮");
            JWT jwt = builder.build();

            String code = jwt.toCipher(pubkey);

            System.out.println("jwt编码后：" + code);

            Builder builder1 = new Builder();
            builder1.setEncryptKey(pubkey, prikey);
            builder1.setCiphertext(code);
            JWT jwt1 = builder1.build();

            System.out.println("jwt解码后：" + JSON.toJSONString(jwt1));
        }

        {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("EC", "BC");
            keygen.initialize(192, SecureRandom.getInstance("SHA1PRNG"));
            KeyPair kp = keygen.generateKeyPair();
            String pubkey = Base64Util.encodeToString(kp.getPublic().getEncoded());
            String prikey = Base64Util.encodeToString(kp.getPrivate().getEncoded());

            System.out.println(pubkey);
            System.out.println(prikey);

            Builder builder = new Builder();
            builder.setAging(3600 * 24);
            builder.setApplicationId("4");
            builder.setDeviceId("21434323243433");
            builder.setUserId("76534");
            builder.setIssuedPublicKey("bY7813NzNt548KAC4QI+PwpK1khDkWPQC+SHbT1njRs=");
//            builder.setSignSalt("lmj.xxxx.com");
            builder.setNick("杨世亮");
            builder.setEncryptAlgorithm(Algorithm.ECC);

            JWT jwt = builder.build();

            String code = jwt.toCipher(pubkey);

            System.out.println("jwt编码后：" + code);

            Builder builder1 = new Builder();
            builder1.setEncryptKey(pubkey, prikey);
            builder1.setCiphertext(code);
            builder1.setEncryptAlgorithm(Algorithm.ECC);
            JWT jwt1 = builder1.build();

            System.out.println("jwt解码后：" + JSON.toJSONString(jwt1));
        }
    }

}
