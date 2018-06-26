package ssn.lmj.demo.service.entities;

import com.lmj.stone.idl.annotation.IDLDesc;
import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 09:59:46 CST 2018
 * Table: city
 */
@IDLDesc("city对象生成")
public final class CityPOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDLDesc("城市编号")
    public long    id;
    @IDLDesc("省\'份\'编号")
    public int     provinceId;
    @IDLDesc("城市名称")
    public String  cityName;
    @IDLDesc("描述")
    public String  description;
}

