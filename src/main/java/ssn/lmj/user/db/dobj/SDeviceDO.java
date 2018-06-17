package ssn.lmj.user.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_device
 */
public final class SDeviceDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id;
    public Long    did; // 设备ID，通过JWT.genDID()生成
    public Integer aid; // appId
    public Long    uid; // userId
    public Long    accountId; // account id
    public String  manufacturer; // 设备制造商
    public String  model; // 设备型号、浏览器则内核型号
    public String  brand; // 品牌名
    public String  device; // 设备名称
    public String  os; // 设备系统版本
    public String  idfa; // iOS 广告追踪id
    public String  idfv; // iOS 广告追踪id 替代方案
    public String  imei; // imei or meid
    public String  mac; // mac地址
    public String  cip; // 客户端id
    public String  ua; // 客户端user agent
    public String  source; // 来源,记录推荐来源: mobile:15673881111; code:313333; uid:2123; src:baidutuijian; etc.
    public Long    createAt; // 创建时间
    public Long    modifiedAt; // 修改时间
    public Integer isDelete; // 0: enabled, 1: deleted
}

