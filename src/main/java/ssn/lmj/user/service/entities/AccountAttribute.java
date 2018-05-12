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
@IDLDesc("账号的某个信息")
public final class AccountAttribute implements Serializable {
    @IDLDesc("account id")
    public long accountId;
    @IDLDesc("数据类型")
    public String  type; //
    @IDLDesc("数据值")
    public String  value; //
    @IDLDesc("描述")
    public String  cmmt; //
    @IDLDesc("创建时间")
    public long    createAt; // 创建时间
    @IDLDesc("修改时间")
    public long    modifiedAt; // 修改时间
}
