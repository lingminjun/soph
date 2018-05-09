package ssn.lmj.user.service.entities;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:21
 */
public class Token implements Serializable {
    /*{
        "access_token":"2YotnFZFEjr1zCsicMWpAA",
            "token_type":"example",
            "expires_in":3600,
            "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
            "example_parameter":"example_value"
    }*/
    public String jwt;  //JWT编码机制
    public String typ;  //token类型
    public int exp;     //过期时间点 秒(s) ， utc时间
    public String csrf; //下发私钥，客户端保存好（Cross-site request forgery）
    public String ext;  //其他参数,建议存储json
    public String did;  //设备id 备用字段
    public String uid;  //user id 备用字段
}
