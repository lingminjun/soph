package ssn.lmj.demo.controller;


import com.alibaba.fastjson.JSON;
import ssn.lmj.demo.db.dobj.CityDO;
import ssn.lmj.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssn.lmj.soph.cache.TheCache;

import java.util.List;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;

    @Autowired
    private TheCache theCache;

    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.GET)
    public CityDO findOneCity(@PathVariable("id") Long id) {
        CityDO cityDO = JSON.parseObject(theCache.get("city_"+id),CityDO.class);
        if (cityDO != null) {
            System.out.println("从缓存中获取到了数据:"+cityDO.toString());
            return cityDO;
        }
        cityDO = cityService.findCityById(id);
        System.out.println("从DB中获取到了数据:"+cityDO.toString());
        theCache.set("city_"+id,JSON.toJSONString(cityDO),30);
        return cityDO;
    }

    @RequestMapping(value = "/api/province/{provinceId}", method = RequestMethod.GET)
    public List<CityDO> queryProvinceCity(@PathVariable("provinceId") Long id) {
        return cityService.findProvinceAllCity(id.intValue());
    }

    @RequestMapping(value = "/api/province/count/{provinceId}", method = RequestMethod.GET)
    public long queryProvinceCityCount(@PathVariable("provinceId") Long id) {
        return cityService.countProvinceAllCity(id.intValue());
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.GET)
    public List<CityDO> findAllCity() {
        return cityService.findAllCity();
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.POST)
    public void createCity(@RequestBody CityDO CityDO) {
        cityService.saveCity(CityDO);
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.PUT)
    public void modifyCity(@RequestBody CityDO CityDO) {
        cityService.updateCity(CityDO);
    }

    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.DELETE)
    public void modifyCity(@PathVariable("id") Long id) {
        cityService.deleteCity(id);
    }

    @RequestMapping(value = "/api/city/search", method = RequestMethod.GET)
    public CityDO findOneCity(@RequestParam(value = "cityName", required = true) String cityName) {
        return cityService.findCityByName(cityName);
    }

}
