package ssn.lmj.demo.controller;


import com.alibaba.fastjson.JSON;
import com.lmj.stone.cache.AutoCache;
import com.lmj.stone.cache.RemoteCache;
import com.lmj.stone.idl.IDLException;
import org.springframework.cache.annotation.Cacheable;
import ssn.lmj.demo.db.dobj.CityDO;
import ssn.lmj.demo.service.CityCRUDService;
import ssn.lmj.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssn.lmj.demo.service.entities.CityPOJO;
import ssn.lmj.soph.cache.TheCache;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;

    @Resource(name = "theCache")
    private RemoteCache theCache;

    @Autowired
    private CityCRUDService cityCRUDService;

    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.GET)
    public CityPOJO findOneCity(@PathVariable("id") Long id) throws IDLException {
        return cityCRUDService.findTheCity(id,false);

//        CityPOJO cityPOJO = theCache.getJSON("city_"+id,CityPOJO.class);
//        if (cityPOJO == null) {
//            try {
//                cityPOJO = cityCRUDService.findTheCity(id);
//                theCache.setJSON("city_"+id,cityPOJO);
//                System.out.println("从DB中获取到了数据:"+cityPOJO.toString());
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("从缓存中获取到了数据:"+cityPOJO.toString());
//        }
//        return cityPOJO;


//        CityDO cityDO = JSON.parseObject(theCache.get("city_"+id),CityDO.class);
//        if (cityDO != null) {
//            System.out.println("从缓存中获取到了数据:"+cityDO.toString());
//            return cityDO;
//        }
//        cityDO = cityService.findCityById(id);
//        System.out.println("从DB中获取到了数据:"+cityDO.toString());
//        theCache.set("city_"+id,JSON.toJSONString(cityDO),30);
//        return cityDO;
    }

    @RequestMapping(value = "/api/province/{provinceId}", method = RequestMethod.GET)
    public List<CityDO> queryProvinceCity(@PathVariable("provinceId") Long id) {
        return cityService.findProvinceAllCity(id.intValue());
    }

    @RequestMapping(value = "/api/province/count/{provinceId}", method = RequestMethod.GET)
    public long queryProvinceCityCount(@PathVariable("provinceId") Long id) {
        return cityService.countProvinceAllCity(id.intValue());
    }

    // RESTful API
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
