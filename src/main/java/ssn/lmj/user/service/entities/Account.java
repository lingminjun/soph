package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:22
 */
@IDLDesc("账号信息")
public class Account {
    @IDLDesc("账号id")
    public long    id;
    @IDLDesc("账号类型，标识三方平台,平台|大陆手机号(China mobile)|邮箱账号")
    public String  platform; // 平台|大陆手机号(China mobile)|邮箱账号
    @IDLDesc("三方平台id 手机号和邮箱自成一系,开放平台id|+86-15673886363|soulshangm@gmail.com")
    public String  openId; // 开放平台id|+86-15673886363|soulshangm@gmail.com
    @IDLDesc("三方平台uuid")
    public String  uuid; // 开放平台唯一id
    @IDLDesc("昵称")
    public String  nick; // 昵称
    @IDLDesc("头像地址")
    public String  head; // 头像url
    @IDLDesc("性别:0未知;1男;2女;3人妖;")
    public Gender gender; // 性别:0未知;1男;2女;3人妖;
    @IDLDesc("手机号")
    public String  mobile; // 手机号
    @IDLDesc("邮箱")
    public String  email; // 邮箱
    @IDLDesc("密码掩码")
    public String  pswd; // 密码掩码
    @IDLDesc("密码加盐码")
    public String  pswdSalt; // 密码加盐
    @IDLDesc("所关联上的用户id")
    public long    uid; // user id,解除绑定后需要清空,且其他信息也需要被抹除
    @IDLDesc("其他第三方信息JSON")
    public String  info; // 第三方其他信息保留
    @IDLDesc("推荐来源")
    public String  source; // 来源,记录推荐来源
    @IDLDesc("解绑备份字段， User信息的JSON")
    public String  preBk; // 备份字段,用于解绑上一个信息备份
    @IDLDesc("此账号的安全级别，记录账号安全等级，可以根据不同登录场景进行权限划分,认证级别: 1三方授权; 11短信授权; 21语音授权; 51绑定银行卡; 81客服人工调查; 91视频头像+身份证+人工")
    public int auth; // 认证级别: 1三方授权; 11短信授权; 21语音授权; 51绑定银行卡; 81客服人工调查; 91视频头像+身份证+人工
    @IDLDesc("此账号认证时间，最后一次认证时间,主要是可以用于将来过滤认证太久远的账号")
    public long    authAt; // 认证时间点,主要是可以用于将来过滤认证太久远的账号
    @IDLDesc("创建时间")
    public long    createAt; // 创建时间
    @IDLDesc("修改时间")
    public long    modifiedAt; // 修改时间
}
