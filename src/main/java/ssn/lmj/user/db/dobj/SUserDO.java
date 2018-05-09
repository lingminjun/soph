package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed May 09 23:04:41 CST 2018
 * Table: s_user
 */
public final class SUserDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public String  nick; // 昵称
    public String  name; // 真实名字
    public String  idNumber; // 身份证号
    public String  head; // 头像url
    public String  mobile; // 手机号
    public String  email; // 邮箱
    public Integer gender; // 性别:0未知;1男;2女;3人妖;
    public Integer grade; // 等级
    public String  rank; // 等级
    public String  role; // 角色
    public String  joinFrom; // 加入来源账号,s_account.platform对应
    public String  source; // 来源,记录推荐来源
    public Integer status; // 状态:0正常;-1禁用;
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
}

