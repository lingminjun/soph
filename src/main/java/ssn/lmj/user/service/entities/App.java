package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

/**
 * Created with IntelliJ IDEA.
 * Description: 系统app定义，此处仅仅给出示例，自行修改
 * User: lingminjun
 * Date: 2018-05-19
 * Time: 下午2:02
 */
@IDLDesc("客户端应用")
public enum  App {
    @IDLDesc("未知")
    NAN(0),
    @IDLDesc("iOS应用")
    iOS(1),
    @IDLDesc("Android应用")
    Android(2),
    @IDLDesc("手机浏览")
    H5(3),
    @IDLDesc("PC浏览器")
    PC(4);

    public final int id;

    App(int id) {
        this.id = id;
    }

    public static App valueOf(int id) {
        //循环输出 值
        for (App e : App.values()) {
            if (e.id == id) {
                return e;
            }
        }
        return App.NAN;
    }
}
