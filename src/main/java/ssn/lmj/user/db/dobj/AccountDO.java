package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_account
 */
public final class AccountDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public String  platform; // 平台名|大陆手机号(China mobile)|邮箱账号
    public String  openId; // 又名Account:开放平台id|+86-15673886363|soulshangm@gmail.com
    public String  union; // 三方平台联盟平台，如微信，主要是与下面unionId对应
    public String  unionId; // 开放平台唯一id，部分平台才有，如微信unionId
    public String  nick; // 昵称
    public String  head; // 头像url
    public Integer gender; // 性别:0未知;1男;2女;3人妖;
    public String  mobile; // 手机号
    public String  email; // 邮箱
    public String  pswd; // 密码掩码
    public String  pswdSalt; // 密码加盐
    public Long    uid; // user id,解除绑定后需要清空,且其他信息也需要被抹除
    public String  info; // 第三方其他信息保留
    public String  source; // 来源,记录推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.
    public String  preBk; // 备份字段,用于解绑上一个信息备份
    public Integer auth; // 认证级别: 1三方授权; 11短信授权; 21语音授权; 51绑定银行卡; 81客服人工调查; 91视频头像+身份证+人工
    public Long    authAt; // 认证时间点,主要是可以用于将来过滤认证太久远的账号
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

