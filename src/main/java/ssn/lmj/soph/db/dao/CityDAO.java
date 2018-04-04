package ssn.lmj.soph.db.dao;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.com.dao.SSNMapper;
import ssn.lmj.com.dao.SSNTableDAO;
import ssn.lmj.soph.db.dobj.CityDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed Apr 04 10:59:06 CST 2018
 * Table: city
 */
public interface CityDAO extends SSNTableDAO<CityDO> {
    /**
     * 获取城市信息列表
     *
     * @return
     */
    @SSNMapper("select `id`,`province_id`,`city_name`,`description` from `city` ")
    List<CityDO> findAllCity();

    /**
     * 根据城市名称，查询城市信息
     *
     * @param cityName 城市名
     */
    @SSNMapper("select `id`,`province_id`,`city_name`,`description` from `city` where `city_name` = #{cityName}")
    CityDO findByName(@Param("cityName") String cityName);
}