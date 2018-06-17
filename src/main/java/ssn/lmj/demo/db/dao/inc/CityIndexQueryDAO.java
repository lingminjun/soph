package ssn.lmj.demo.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.demo.db.dobj.CityDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 13:35:19 CST 2018
 * Table: city
 */
public interface CityIndexQueryDAO extends TableDAO<CityDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param provinceId  省份编号
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<CityDO> queryByProvinceId(@Param("provinceId") int provinceId,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param provinceId  省份编号
     * @param cityName  城市名称
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<CityDO> queryByProvinceIdAndCityName(@Param("provinceId") int provinceId, @Param("cityName") String cityName,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

}

