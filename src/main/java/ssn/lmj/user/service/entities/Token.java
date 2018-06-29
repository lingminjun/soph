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
@IDLDesc("认证通过实体")
public final class Token implements Serializable {
    private static final long serialVersionUID = 1L;

    @IDLDesc("jwt （JSON Web Token）或者说是 access token")
    public String jwt;  //JWT编码机制

    @IDLDesc("jwt使用类型")
    public TokenType typ;  //token类型

    @IDLDesc("过期时间点（秒），服务器时间（ntp同步）（UTC）")
    public long exp;     //过期时间点 秒(s) ， utc时间

    @IDLDesc("下发私钥，用于加签，妥善保存，加密算法在jwt中定义，服务端客户端自行协商 【刷新token时使用】")
    public String csrf; //下发私钥，客户端保存好（Cross-site request forgery）,加签使用

    @IDLDesc("设备id")
    public String did;  //设备id 备用字段

    @IDLDesc("用户id")
    public String uid;  //user id 备用字段

    @IDLDesc("account id")
    public String acnt;  //account id 备用字段

    @IDLDesc("是否为注册")
    public boolean isSignup;  //本次认证是注册

    @IDLDesc("其他信息 JSONString")
    public String ext;  //其他参数,建议存储json
}
