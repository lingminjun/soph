package ssn.lmj.demo.controller;


import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class CityRestControllerBK {

//    @Autowired
//    private CityService cityService;
//
//    @Resource(name = "theCache")
//    private RemoteCache theCache;
//
//    @Autowired
//    private CityCRUDService cityCRUDService;
//
//    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.GET)
//    public CityPOJO findOneCity(@PathVariable("id") Long id) throws IDLException {
//        return cityCRUDService.findTheCity(id,false);
//
////        CityPOJO cityPOJO = theCache.getJSON("city_"+id,CityPOJO.class);
////        if (cityPOJO == null) {
////            try {
////                cityPOJO = cityCRUDService.findTheCity(id);
////                theCache.setJSON("city_"+id,cityPOJO);
////                System.out.println("从DB中获取到了数据:"+cityPOJO.toString());
////            } catch (Throwable e) {
////                e.printStackTrace();
////            }
////        } else {
////            System.out.println("从缓存中获取到了数据:"+cityPOJO.toString());
////        }
////        return cityPOJO;
//
//
////        CityDO cityDO = JSON.parseObject(theCache.get("city_"+id),CityDO.class);
////        if (cityDO != null) {
////            System.out.println("从缓存中获取到了数据:"+cityDO.toString());
////            return cityDO;
////        }
////        cityDO = cityService.findCityById(id);
////        System.out.println("从DB中获取到了数据:"+cityDO.toString());
////        theCache.set("city_"+id,JSON.toJSONString(cityDO),30);
////        return cityDO;
//    }
//
//    @RequestMapping(value = "/api/province/{provinceId}", method = RequestMethod.GET)
//    public List<CityDO> queryProvinceCity(@PathVariable("provinceId") Long id) {
//        return cityService.findProvinceAllCity(id.intValue());
//    }
//
//    @RequestMapping(value = "/api/province/count/{provinceId}", method = RequestMethod.GET)
//    public long queryProvinceCityCount(@PathVariable("provinceId") Long id) {
//        return cityService.countProvinceAllCity(id.intValue());
//    }
//
//    // RESTful API
//    @RequestMapping(value = "/api/city", method = RequestMethod.GET)
//    public List<CityDO> findAllCity() {
//        return cityService.findAllCity();
//    }
//
//    @RequestMapping(value = "/api/city", method = RequestMethod.POST)
//    public void createCity(@RequestBody final CityPOJO cityPOJO) throws IDLException {
//        List<CityPOJO> pojos = new ArrayList<CityPOJO>();
//        pojos.add(cityPOJO);
//        cityCRUDService.batchAddCity(pojos,false);
////        cityService.saveCity(CityDO);
//    }
//
//    @RequestMapping(value = "/api/city", method = RequestMethod.PUT)
//    public void modifyCity(@RequestBody final CityPOJO cityPOJO) throws IDLException {
//        cityCRUDService.updateTheCity(cityPOJO.id,
//                cityPOJO.provinceId,
//                cityPOJO.cityName,
//                cityPOJO.description);
////        cityService.updateCity(CityDO);
//    }
//
//    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.DELETE)
//    public void modifyCity(@PathVariable("id") final Long id) throws IDLException {
//        cityCRUDService.removeTheCity(id);
////        cityService.deleteCity(id);
//    }
//
//    @RequestMapping(value = "/api/city/search", method = RequestMethod.GET)
//    public CityResults findOneCity(@RequestParam(value = "cityName", required = true) String cityName) {
//        return cityCRUDService.queryCityByProvinceId(cityName,
//                cityName);
//    }

}
