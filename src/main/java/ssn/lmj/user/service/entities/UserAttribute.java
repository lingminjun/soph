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
@IDLDesc("用户某个信息")
public final class UserAttribute implements Serializable {
    @IDLDesc("user id")
    public long uid;

    @IDLDesc("属性集")
    public List<Attribute> attributes; //
}
