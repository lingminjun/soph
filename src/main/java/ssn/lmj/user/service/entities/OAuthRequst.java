package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-19
 * Time: 上午11:02
 */
@IDLDesc("第三方登录授权请求组装")
public class OAuthRequst implements Serializable {
    private static final long serialVersionUID = 1L;

    @IDLDesc("第三方登录授权请求链接")
    public String signLink;
}
