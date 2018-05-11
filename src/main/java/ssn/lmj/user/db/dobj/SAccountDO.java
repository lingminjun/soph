package ssn.lmj.user.db.dobj;


import java.io.Serializable;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed May 09 23:04:41 CST 2018
 * Table: s_account
 */
public final class SAccountDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public String  platform; // 平台名|大陆手机号(China mobile)|邮箱账号
    public String  openId; // 又名Account: 开放平台id|+86-15673886363|soulshangm@gmail.com
    public String  uuid; // 开放平台唯一id
    public String  nick; // 昵称
    public String  head; // 头像url
    public Integer gender; // 性别:0未知;1男;2女;3人妖;
    public String  mobile; // 手机号
    public String  email; // 邮箱
    public String  pswd; // 密码掩码
    public String  pswdSalt; // 密码加盐
    public Long    uid; // user id,解除绑定后需要清空,且其他信息也需要被抹除
    public String  info; // 第三方其他信息保留
    public String  source; // 来源,记录推荐来源
    public String  preBk; // 备份字段,用于解绑上一个信息备份
    public Integer auth; // 认证级别: 1三方授权; 11短信授权; 21语音授权; 51绑定银行卡; 81客服人工调查; 91视频头像+身份证+人工
    public Long    authAt; // 认证时间点,主要是可以用于将来过滤认证太久远的账号
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}


