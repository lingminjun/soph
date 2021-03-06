package ssn.lmj.demo.service.impl;

import ssn.lmj.demo.db.dao.CityDAO;
import ssn.lmj.demo.db.dobj.CityDO;
import ssn.lmj.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 城市业务逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDAO cityDAO;


    public List<CityDO> findAllCity(){

//        List<Long> ids = new ArrayList<Long>();
//        ids.add(1l);
//        ids.add(2l);
//        return cityDAO.queryByIds(ids);

        return cityDAO.findAllCity();
    }

    @Override
    public List<CityDO> findProvinceAllCity(Integer id) {
        return cityDAO.queryByProvinceId(id, 0,"id",true,0,100);
    }

    @Override
    public long countProvinceAllCity(Integer id) {
        return cityDAO.countByProvinceId(id, 0);
    }

    /**
     * 获取城市逻辑：
     * 如果缓存不存在，从 DB 中获取城市信息，然后插入缓存
     */
    public CityDO findCityById(Long id) {
        // 从 DB 中获取城市信息
        CityDO CityDO = cityDAO.getById(id);

        return CityDO;
    }

    @Override
    public Long saveCity(CityDO cityDO) {
        return cityDAO.insert(cityDO);
    }

    /**
     * 更新城市逻辑：
     * 如果缓存存在，删除
     * 如果缓存不存在，不操作
     */
    @Override
    public Long updateCity(CityDO cityDO) {
        Long ret = cityDAO.update(cityDO);

        return ret;
    }

    @Override
    public Long deleteCity(Long id) {

        Long ret = cityDAO.deleteById(id);

        return ret;
    }


    public CityDO findCityByName(String cityName) {
        return cityDAO.findByName(cityName);
    }



}
