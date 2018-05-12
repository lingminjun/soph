package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:21
 */
@IDLDesc("用户信息")
public final class User implements Serializable {
    @IDLDesc("user id")
    public long    id;
    @IDLDesc("昵称")
    public String  nick; // 昵称
    @IDLDesc("姓名-真实姓名")
    public String  name; // 真实名字
    @IDLDesc("身份证号")
    public String  idNumber; // 身份证号
    @IDLDesc("头像地址")
    public String  head; // 头像url
    @IDLDesc("手机号")
    public String  mobile; // 手机号
    @IDLDesc("邮箱")
    public String  email; // 邮箱
    @IDLDesc("性别:0未知;1男;2女;3人妖;")
    public int gender; // Gender 性别:0未知;1男;2女;3人妖;
    @IDLDesc("用户等级")
    public int grade; // 等级
    @IDLDesc("头衔")
    public String  rank; // 等级
    @IDLDesc("角色")
    public String  role; // 角色 (普通用户、MC、AC)
    @IDLDesc("账号创建来源,s_account.platform对应")
    public String  joinFrom; // 加入来源账号,s_account.platform对应
    @IDLDesc("账号推荐来源")
    public String  source; // 来源,记录推荐来源
    @IDLDesc("状态")
    public int status; // 状态:0正常;-1禁用;
    @IDLDesc("创建时间")
    public long    createAt; // 创建时间
    @IDLDesc("修改时间")
    public long    modifiedAt; // 修改时间
}
