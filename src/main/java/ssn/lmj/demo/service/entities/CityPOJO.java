package ssn.lmj.demo.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;
import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 23 10:43:15 CST 2018
 * Table: city
 */
@IDLDesc("city")
public final class CityPOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDLDesc("城市编号")
    public int     id;
    @IDLDesc("省份编号")
    public int     provinceId;
    @IDLDesc("城市名称")
    public String  cityName;
    @IDLDesc("描述")
    public String  description;
}

