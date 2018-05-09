package ssn.lmj.user.service.entities;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-08
 * Time: 下午1:10
 */
public enum Gender {
    unknown(0),        //无权限
    male(1),    //文档查看
    female(2);  //服务合作方

    public final int code;
    Gender(int code) {
        this.code = code;
    }
}
