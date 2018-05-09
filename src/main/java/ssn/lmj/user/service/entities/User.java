package ssn.lmj.user.service.entities;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:21
 */
public class User implements Serializable {
    public long    id;
    public String  nick; // 昵称
    public String  name; // 真实名字
    public String  idNumber; // 身份证号
    public String  head; // 头像url
    public String  mobile; // 手机号
    public String  email; // 邮箱
    public int gender; // 性别:0未知;1男;2女;3人妖;
    public int grade; // 等级
    public String  rank; // 等级
    public String  role; // 角色
    public String  joinFrom; // 加入来源账号,s_account.platform对应
    public String  source; // 来源,记录推荐来源
    public int status; // 状态:0正常;-1禁用;
    public long    createAt; // 创建时间
    public long    modifiedAt; // 修改时间
}
