package ssn.lmj.user.jwt;

import com.alibaba.fastjson.JSON;
import ssn.lmj.user.jwt.utils.*;
import ssn.lmj.user.jwt.utils.Signature;

import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.util.concurrent.atomic.AtomicInteger;

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

        public static Algorithm get(String name) {
            if (CRC16.hit(name)) {
                return CRC16;
            } else if (CRC32.hit(name)) {
                return CRC32;
            } else if (MD5.hit(name)) {
                return MD5;
            } else if (SHA1.hit(name)) {
                return SHA1;
            } else if (HMAC.hit(name)) {
                return HMAC;
            } else if (RSA.hit(name)) {
                return RSA;
            } else if (ECC.hit(name)) {
                return ECC;
            } else {
                return CRC32;
            }
        }

        //命中
        public boolean hit(String algorithm) {
            return name.equalsIgnoreCase(algorithm);
        }
    }

    public enum Grant {
        None(0),        //无权限
        Integrated(3),  //服务合作方
        Device(6),      //终端设备注册
        OAuth(9),       //终端合作方
        Cross(12),       //SSO 用于跨域免认证
        Account(15),     //账户认证 （有些平台到这个级别即可）
        User(18),        //用户认证
        Merchant(21),    //MC 合作企业后台用户 Merchant Controller
        Admin(24);       //AC 系统管理员

        public final int code;
        Grant(int code) {
            this.code = code;
        }

        public static Grant get(int code) {
            if (Integrated.code == code) {
                return Integrated;
            } else if (Device.code == code) {
                return Device;
            } else if (OAuth.code == code) {
                return OAuth;
            } else if (Cross.code == code) {
                return Cross;
            } else if (Account.code == code) {
                return Account;
            } else if (User.code == code) {
                return User;
            } else if (Merchant.code == code) {
                return Merchant;
            } else if (Admin.code == code) {
                return Admin;
            } else {
                return None;
            }
        }
    }

    static public class Head {

        public final Algorithm alg; //加密方式
        public final Grant  grt; //grant 授予作用，自行定义 ：did(设备注册)
        public final long   exp; //过期时间点
        public final long   iat; //签发时间(秒)
        public final String iss; //jwt签发者、一般放域名 lmj.com 与 domain一个含义

        private Head(Algorithm alg, Grant grt, long age, String iss) {
            this.alg = alg;
            this.grt = grt;
            this.iat = System.currentTimeMillis() / 1000;
            this.exp = age <= 0 ? 0 : age + this.iat;
            this.iss = iss;
        }

        private Head(Algorithm alg, Grant grt, long exp, long iat, String iss) {
            this.alg = alg;
            this.grt = grt;
            this.iat = iat;
            this.exp = exp;
            this.iss = iss;
        }
    }

    static public class Body {
        public final String aid; //应用id
        public final String did; //设备id
        public final String uid; //用户id
        public final String oid; //open id
        public final String pid; //partner id
        public final String key;  //下发到客户端的公钥,与token中csrf(私钥)对应（用于客户端回传服务端验证签名，网关算法仅依赖）
        public final String nk;  //用户名（实际没啥作用）
        public final long log;   //用于记录日志标记

        private Body(String aid,String did,String uid,String oid,String pid,String key,String nk,long log) {
            this.aid = aid;
            this.did = did;
            this.uid = uid;
            this.oid = oid;
            this.pid = pid;
            this.key = key;
            this.nk = nk;
            this.log = log;
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
        Builder.appendToJSON(builder,"x",head.iat);
        Builder.appendToJSON(builder,"i",head.iss);

        Builder.appendToJSON(builder,"a",body.aid);
        Builder.appendToJSON(builder,"d",body.did);
        Builder.appendToJSON(builder,"u",body.uid);
        Builder.appendToJSON(builder,"o",body.oid);
        Builder.appendToJSON(builder,"p",body.pid);
        Builder.appendToJSON(builder,"k",body.key);
        Builder.appendToJSON(builder,"n",body.nk);
        Builder.appendToJSON(builder,"f",body.log);

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

    public String toBinaryCipher(String pubKey) {
        Scanner scanner = new Scanner();
        scanner.fillJWT(this);
        Encryptable encryptor = Encryptor.getSignable(head.alg.name,pubKey,null);

        byte[] data = scanner.toBinaryToken();

        byte[] cipher = encryptor.encrypt(data);
        return Base64Util.encodeToString(cipher);
    }

    //产生一个唯一的
    private static final long TIME_2018_01_01 = 1514736000000l;
    private static final AtomicInteger SEQUENCER = new AtomicInteger(1);
    private static final int SEQ_SIZE = 1000;
    //分布式系统不完全唯一，由于随机数引入，基本可以忽略其冲突（用户行为观察不受影响）
    public static long genDID() {
        int random = (int)(Math.random()*SEQ_SIZE);//取小数位，具有不可预测性
        int num = SEQUENCER.getAndIncrement();
        num = num > 0 ? num % SEQ_SIZE : -num % SEQ_SIZE;
        return (System.currentTimeMillis() - TIME_2018_01_01) * SEQ_SIZE * SEQ_SIZE + random * SEQ_SIZE + num;
    }

    static private class Scanner {
        public String l; //加密方式
        public int  g; //grant 授予作用，自行定义 ：did(设备注册)
        public long   e; //过期时间点
        public long   x; //颁发时间点
        public String i; //jwt签发者

        public String a; //应用id
        public String d; //设备id
        public String u; //用户id
        public String o; //open id
        public String p; //partner id
        public String k;  //下发到客户端的公钥,与token中csrf对应（用于客户端回传服务端验证签名）
        public String n;  //用户名（实际没啥作用）
        public long f;    //日志标记

        public String t; //签名方式 CRC32
        public String s; //签名

        public JWT toJWT() {
            return new JWT(new Head(Algorithm.get(l),Grant.get(g),e,x,i),new Body(a,d,u,o,p,k,n,f),s == null ? null : new Sign(Algorithm.get(t),s));
        }

        public void fillJWT(JWT jwt) {
            this.l = jwt.head.alg.name;
            this.g = jwt.head.grt.code;
            this.e = jwt.head.exp;
            this.x = jwt.head.iat;
            this.i = jwt.head.iss;

            this.a = jwt.body.aid;
            this.d = jwt.body.did;
            this.u = jwt.body.uid;
            this.o = jwt.body.oid;
            this.p = jwt.body.pid;
            this.k = jwt.body.key;
            this.n = jwt.body.nk;
            this.f = jwt.body.log;

            if (jwt.sign != null) {
                this.t = jwt.sign.stp.name;
                this.s = jwt.sign.sgn;
            }
        }

        private static String readStringFromStream(DataInputStream inputStream) throws IOException {
            if (inputStream.available() <= 0) {//非必填
                return null;
            }

            short len = inputStream.readShort();
            if (len > 0) {
                byte[] bys = new byte[len];
                if (len != inputStream.read(bys)) {
                    return null;
                }
                return new String(bys,UTF8);
            }
            return null;
        }

        private static void writeStringFromStream(DataOutputStream outputStream, String string) throws IOException {
            byte[] bys = null;
            if (string != null && string.length() > 0) {
                bys = string.getBytes(UTF8);
            } else {
                bys = new byte[0];
            }
            outputStream.writeShort(bys.length);
            outputStream.write(bys);
        }

        /**
         * 二级制 jwt 支持，会大幅度降低jwt长度
         */
        private static Scanner parseFromBinaryToken(byte[] token) {
            DataInputStream dis = null;
            Scanner client = null;
            try {
                dis = new DataInputStream(new ByteArrayInputStream(token));
                client = new Scanner();

                client.l = readStringFromStream(dis);
                client.g = dis.readInt();
                client.e = dis.readLong();
                client.x = dis.readLong();
                client.i = readStringFromStream(dis);

                client.a = readStringFromStream(dis);
                client.d = readStringFromStream(dis);
                client.u = readStringFromStream(dis);
                client.o = readStringFromStream(dis);
                client.p = readStringFromStream(dis);
                client.k = readStringFromStream(dis);
                client.n = readStringFromStream(dis);
                client.f = dis.readLong();

                if (dis.available() > 0) {//非必填
                    client.t = readStringFromStream(dis);
                    client.s = readStringFromStream(dis);
                }

                //发现还有数据,退出
                if (dis.available() > 0) {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return client;
        }

        /**
         * 生成token
         */
        private byte[] toBinaryToken() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(64);
            DataOutputStream dos = new DataOutputStream(baos);
            byte[] token = null;
            try {

                writeStringFromStream(dos,this.l);
                dos.writeInt(this.g);
                dos.writeLong(this.e);
                dos.writeLong(this.x);
                writeStringFromStream(dos,this.i);

                writeStringFromStream(dos,this.a);
                writeStringFromStream(dos,this.d);
                writeStringFromStream(dos,this.u);
                writeStringFromStream(dos,this.o);
                writeStringFromStream(dos,this.p);
                writeStringFromStream(dos,this.k);
                writeStringFromStream(dos,this.n);
                dos.writeLong(this.f);

                if (this.s != null && this.s.length() > 0) {//签名
                    writeStringFromStream(dos,this.t);
                    writeStringFromStream(dos,this.s);
                }

                token = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return token;
        }
    }

    static public class Builder {
        private Algorithm alg = Algorithm.RSA;
        private Grant grt = Grant.User;
        private long age;
        private String iss;
        private Algorithm stp = Algorithm.CRC32;
        private String aid; //应用id
        private String did; //设备id
        private String uid; //用户id
        private String oid; //open id
        private String pid; //partner id
        private String key;  //下发到客户端的公钥,与token中csrf(授予的私钥)对应（用于客户端回传服务端验证签名）
        private String nk;  //用户名（实际没啥作用）
        private long log;   //日志log flag
        private String salt; //签名秘钥（加盐）
        private String cipher;//密文
        private boolean binary = false;
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
        public Builder setAging(long aging) {
            age = aging;
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

        public Builder setLogFlag(long flag) {
            log = flag;
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

        public Builder setCipherText(String cipherText) {
            this.cipher = cipherText;
            return this;
        }

        public Builder setCipherText(String cipherText, boolean binary) {
            this.cipher = cipherText;
            this.binary = binary;
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

        //支持两种格式的jwt解析
        public JWT build() {

            if (cipher != null && cipher.length() > 0) {

                if (priKey == null || priKey.length() == 0) {
                    throw new RuntimeException("无法创建JWT,请设置加密公私秘钥");
                }

                Encryptable encryptor = Encryptor.getSignable(alg.name,pubKey,priKey);
                byte[] data = Base64Util.decode(cipher);
//                byte[] text = GZIP.uncompress(encryptor.decrypt(data));
                byte[] text = encryptor.decrypt(data);
                Scanner scanner = null;
                if (binary) {
                    scanner = Scanner.parseFromBinaryToken(text);
                } else {
                    scanner = JSON.parseObject(text, Scanner.class);
                }
                return scanner.toJWT();
            }

            StringBuilder builder = new StringBuilder();

            Head head = new Head(alg,grt,age,iss);
            Body body = new Body(aid,did,uid,oid,pid,key,nk,log);

            appendToJSON(builder,"l",alg.name);
            appendToJSON(builder,"g",grt.code);
            appendToJSON(builder,"e",head.exp);
            appendToJSON(builder,"x",head.iat);
            appendToJSON(builder,"i",iss);

            appendToJSON(builder,"a",aid);
            appendToJSON(builder,"d",did);
            appendToJSON(builder,"u",uid);
            appendToJSON(builder,"o",oid);
            appendToJSON(builder,"p",pid);
            appendToJSON(builder,"k",key);
            appendToJSON(builder,"n",nk);
            appendToJSON(builder,"f",log);

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

        public String buildCipher() {
            if (this.pubKey == null || this.pubKey.length() == 0) {
                throw new RuntimeException("无法加密JWT，请设置加密公钥");
            }
            JWT jwt = build();
            return jwt.toCipher(this.pubKey);
        }

        public String buildBinaryCipher() {
            if (this.pubKey == null || this.pubKey.length() == 0) {
                throw new RuntimeException("无法加密JWT，请设置加密公钥");
            }
            JWT jwt = build();
            return jwt.toBinaryCipher(this.pubKey);
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
            builder.setLogFlag(12);
            JWT jwt = builder.build();

            String code = jwt.toCipher(pubkey);

            System.out.println("jwt编码后：" + code);

            Builder builder1 = new Builder();
            builder1.setEncryptKey(pubkey, prikey);
            builder1.setCipherText(code);
            JWT jwt1 = builder1.build();

            System.out.println("jwt解码后：" + JSON.toJSONString(jwt1));

            {
                String code1 = jwt.toBinaryCipher(pubkey);
                System.out.println("jwt编码后1：" + code1);
                Builder builder2 = new Builder();
                builder2.setEncryptKey(pubkey, prikey);
                builder2.setCipherText(code1,true);
                JWT jwt2 = builder2.build();
                System.out.println("jwt解码后1：" + JSON.toJSONString(jwt2));
            }
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
            builder.setLogFlag(12);
            builder.setEncryptAlgorithm(Algorithm.ECC);

            JWT jwt = builder.build();

            String code = jwt.toCipher(pubkey);
            System.out.println("jwt编码后：" + code);

            Builder builder1 = new Builder();
            builder1.setEncryptKey(pubkey, prikey);
            builder1.setCipherText(code);
            builder1.setEncryptAlgorithm(Algorithm.ECC);
            JWT jwt1 = builder1.build();
            System.out.println("jwt解码后：" + JSON.toJSONString(jwt1));

            {
                String code1 = jwt.toBinaryCipher(pubkey);
                System.out.println("jwt编码后1：" + code1);
                Builder builder2 = new Builder();
                builder2.setEncryptKey(pubkey, prikey);
                builder2.setCipherText(code1,true);
                builder2.setEncryptAlgorithm(Algorithm.ECC);
                JWT jwt2 = builder2.build();
                System.out.println("jwt解码后1：" + JSON.toJSONString(jwt2));
            }
        }
    }

}
