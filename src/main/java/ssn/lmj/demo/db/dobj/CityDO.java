package ssn.lmj.demo.db.dobj;




 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 13:35:18 CST 2018
 * Table: city
 */
public final class CityDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Integer id; // 城市编号
    public Integer provinceId; // 省份编号
    public String  cityName; // 城市名称
    public String  description; // 描述
}

