package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-20
 * Time: 上午12:00
 */
@IDLDesc("性别")
public enum Gender {
    @IDLDesc("未知")
    unknown(0),
    @IDLDesc("男")
    male(1),
    @IDLDesc("女")
    female(2),
    @IDLDesc("人妖")
    shemale(3);

    public final int value;

    Gender(int value) {
        this.value = value;
    }

    public static Gender valueOf(int value) {
        //循环输出 值
        for (Gender e : Gender.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return Gender.unknown;
    }
}
