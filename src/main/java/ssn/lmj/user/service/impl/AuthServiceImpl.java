package ssn.lmj.user.service.impl;

import com.lmj.stone.idl.IDLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ssn.lmj.soph.db.dobj.SDataDO;
import ssn.lmj.user.db.dao.SAccountDAO;
import ssn.lmj.user.db.dao.SDeviceDAO;
import ssn.lmj.user.db.dao.SUserDAO;
import ssn.lmj.user.db.dobj.SAccountDO;
import ssn.lmj.user.db.dobj.SDeviceDO;
import ssn.lmj.user.jwt.JWT;
import ssn.lmj.user.jwt.utils.AesHelper;
import ssn.lmj.user.jwt.utils.Base64Util;
import ssn.lmj.user.jwt.utils.EccHelper;
import ssn.lmj.user.service.AuthService;
import ssn.lmj.user.service.entities.Token;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-12
 * Time: 上午10:32
 */
public class AuthServiceImpl implements AuthService {

    @Autowired
    SAccountDAO accountDAO;

    @Autowired
    SUserDAO userDAO;

    @Autowired
    SDeviceDAO deviceDAO;

    @Value("${user.auth.ecc.pub.key}")
    String eccPubKey;

    @Value("${user.auth.ecc.pri.key}")
    String eccPriKey;


    @Override
    public Token logDevice(int appId, String manufacturer, String model, String brand, String device, String os, String idfa, String idfv, String imei, String mac, String cip, String ua) throws IDLException {
        long did = JWT.genDID();

//        byte[][] issuKeys = EccHelper.randomKey(0);//颁发Ecc公私秘钥对，
        String issuKey = Base64Util.encodeToString(AesHelper.randomKey(0));//颁发Aes秘钥即可

        Token token = new Token();
        token.did = "" + did;
        token.exp = 0;
        token.typ = "log.device";
        token.csrf = issuKey;//颁发私钥

        //记录设备
        SDeviceDO deviceDO = new SDeviceDO();
        deviceDO.did = did;
        deviceDO.aid = appId;
        deviceDO.manufacturer = manufacturer; // 设备制造商
        deviceDO.model = model; // 设备型号、浏览器则内核型号
        deviceDO.brand = brand; // 品牌名
        deviceDO.device = device; // 设备名称
        deviceDO.os = os; // 设备系统版本
        deviceDO.idfa = idfa; // iOS 广告追踪id
        deviceDO.idfv = idfv; // iOS 广告追踪id 替代方案
        deviceDO.imei = imei; // imei or meid
        deviceDO.mac = mac; // mac地址
        deviceDO.cip = cip; // 客户端id
        deviceDO.ua = ua; // 客户端user agent

        long id = deviceDAO.insert(deviceDO);

        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setDeviceId(token.did);
        builder.setAging(0);
        builder.setJWTGrant(JWT.Grant.Device);
        builder.setIssuedPublicKey(issuKey);
        builder.setLogFlag(id);

        token.jwt = builder.buildCipher();

        return token;
    }

    @Override
    public Token login(String from, String account, String secret, String token, String captcha, String session, boolean user) throws IDLException {
        return null;
    }

    @Override
    public Token refresh(String jwt) throws IDLException {
        JWT.Builder builder = new JWT.Builder();
        builder.setEncryptAlgorithm(JWT.Algorithm.ECC);
        builder.setEncryptKey(eccPubKey,eccPriKey);
        builder.setCiphertext(jwt);
        JWT jwt1 = builder.build();
        
        return null;
    }

    @Override
    public boolean logout(String jwt) throws IDLException {
        return true;
    }
}
