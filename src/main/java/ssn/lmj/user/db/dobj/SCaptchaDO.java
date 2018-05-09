package ssn.lmj.user.db.dobj;




 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed May 09 23:04:41 CST 2018
 * Table: s_captcha
 */
public final class SCaptchaDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public String  session; // 会话、或者token
    public String  type; // 验证码类型
    public String  code; // 验证码
    public String  cmmt; // 描述
    public Integer status; // 状态:1 已验证，0 未验证，-1 验证失败
    public Integer aging; // 时效，单位秒
    public String  account; // 管理账号信息，有多个时;号隔开
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

