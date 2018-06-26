package ssn.lmj.demo.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: city
 */
public final class CityDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id; // 城市编号
    public Integer provinceId; // 省\'份\'编号
    public String  cityName; // 城市名称
    public String  description; // 描述
    public Integer isDelete; // 0: enabled, 1: deleted
}

