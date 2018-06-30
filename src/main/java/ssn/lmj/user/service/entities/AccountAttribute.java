package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:21
 */
@IDLDesc("账号的某个信息")
public final class AccountAttribute implements Serializable {
    @IDLDesc("account id")
    public long accountId;

    @IDLDesc("属性集")
    public List<Attribute> attributes; //
}
