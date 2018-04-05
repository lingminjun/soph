package ssn.lmj.soph.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed Apr 04 15:06:12 CST 2018
 * Table: city
 */
public final class CityDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Integer id; // 城市编号
    public Integer provinceId; // 省份编号
    public String  cityName; // 城市名称
    public String  description; // 描述
}

