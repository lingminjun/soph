package ssn.lmj.soph.db.dao;


import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.soph.db.dao.inc.CityIndexQueryDAO;
import com.lmj.stone.dao.SQL;
import ssn.lmj.soph.db.dobj.CityDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: city
 */
public interface CityDAO extends CityIndexQueryDAO {
    /**
     * 获取城市信息列表
     *
     * @return
     */
    @SQL("select `id`,`province_id`,`city_name`,`description` from `city` ")
    List<CityDO> findAllCity();

    /**
     * 根据城市名称，查询城市信息
     *
     * @param cityName 城市名
     */
    @SQL("select `id`,`province_id`,`city_name`,`description` from `city` where `city_name` = #{cityName}")
    CityDO findByName(@Param("cityName") String cityName);
}