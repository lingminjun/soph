package ssn.lmj.user.service.entities;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:22
 */
public class Account {
    public long    id;
    public String  platform; // 平台|大陆手机号(China mobile)|邮箱账号
    public String  openId; // 开放平台id|+86-15673886363|soulshangm@gmail.com
    public String  uuid; // 开放平台唯一id
    public String  nick; // 昵称
    public String  head; // 头像url
    public int gender; // 性别:0未知;1男;2女;3人妖;
    public String  mobile; // 手机号
    public String  email; // 邮箱
    public String  pswd; // 密码掩码
    public String  pswdSalt; // 密码加盐
    public long    uid; // user id,解除绑定后需要清空,且其他信息也需要被抹除
    public String  info; // 第三方其他信息保留
    public String  source; // 来源,记录推荐来源
    public String  preBk; // 备份字段,用于解绑上一个信息备份
    public int auth; // 认证级别: 1三方授权; 11短信授权; 21语音授权; 51绑定银行卡; 81客服人工调查; 91视频头像+身份证+人工
    public long    authAt; // 认证时间点,主要是可以用于将来过滤认证太久远的账号
    public long    createAt; // 创建时间
    public long    modifiedAt; // 修改时间
}
