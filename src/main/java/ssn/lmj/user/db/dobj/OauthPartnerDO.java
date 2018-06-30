package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_oauth_partner
 */
public final class OauthPartnerDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public String  platform; // 第三方开放平台应用定义wechat_open,wechat_app
    public String  appId; // 三方开放平台注册的应用id
    public String  partnerId; // 三方开放平台注册的服务id，类似appId概念，一个针对应用，一个针对服务
    public String  appKey; // 三方开放平台注册的应用Key
    public String  connectUrl; // connectUrl是获取code的链接
    public String  apiUrl; // 开放平台接口地址
    public String  service; // 接口服务
    public String  targetService; // 接口服务
    public String  verifyUrl; // verify应该老版的alipay网关接口有个验证alipan返回值是否合法
    public String  charset; // 编码格式
    public String  signType; // 签名类型
    public String  priKey; // 分配给我们的私钥 self private key
    public String  pubKey; // 分配给我们的公钥 service public key
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

