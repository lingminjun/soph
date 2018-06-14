package ssn.lmj.soph.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.soph.db.dobj.CityDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: city
 */
public interface CityIndexQueryDAO extends TableDAO<CityDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param provinceId  省份编号
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<CityDO> queryByProvinceId(@Param("provinceId") int provinceId,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param provinceId  省份编号
     * @param cityName  城市名称
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<CityDO> queryByProvinceIdAndCityName(@Param("provinceId") int provinceId, @Param("cityName") String cityName,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

}

