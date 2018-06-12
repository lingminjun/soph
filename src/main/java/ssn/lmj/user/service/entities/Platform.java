package ssn.lmj.user.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-18
 * Time: 下午3:26
 */
@IDLDesc("账号类型，账号所属平台")
public enum Platform {
    @IDLDesc("手机号，带国家码格式[可直接生成用户]")
    mobile,
    @IDLDesc("邮箱，[根据配置生成用户]")
    email,
    
    @IDLDesc("微信开放平台登录")
    wechat_open,
    @IDLDesc("微信公众平台")
    wechat_mp,
    @IDLDesc("微信服务号登录")
    wechat_svm,
    @IDLDesc("微信公众号base信息")
    wechat_mp_base,
    @IDLDesc("微信小程序登录")
    wechat_mina,
    @IDLDesc("支付宝快捷登陆")
    alipay_qklg,
    @IDLDesc("支付宝服务窗授权登录")
    alipay_fuwu,
    @IDLDesc("支付宝无线账户授权")
    alipay_app,
    @IDLDesc("微信移动app")
    wechat_app,
    @IDLDesc("供销社微信服务号")
    wechat_svm_gxs,
    @IDLDesc("qq移动app")
    qq_app
}
