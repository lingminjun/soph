package ssn.lmj.demo.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ssn.lmj.demo.service.entities.CityPOJO;
import ssn.lmj.demo.service.entities.CityResults;
import ssn.lmj.demo.service.CityCRUDService;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Mon Jun 25 09:42:13 CST 2018
 * SQLFile: sqls/city.sql
 */
@RestController
public class CityRestController {

    @Autowired
    private CityCRUDService cityCRUDService;

    /**
     * create a City
     * @return City.id 
     */
    @RequestMapping(value = "/api/city", method = RequestMethod.POST)
    public long createCity(@RequestBody final CityPOJO cityPOJO) throws IDLException {
        return cityCRUDService.addCity(cityPOJO);
    }

    /**
     * delete the City
     * @return 
     */
    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.DELETE)
    public boolean deleteTheCity(@PathVariable("id") final long id) throws IDLException {
        return cityCRUDService.removeTheCity(id);
    }

    /**
     * update the City
     * @return 
     */
    @RequestMapping(value = "/api/city", method = RequestMethod.PUT)
    public boolean updateTheCity(@RequestBody final CityPOJO cityPOJO) throws IDLException {
        return cityCRUDService.updateTheCity(cityPOJO.id,
                cityPOJO.provinceId,
                cityPOJO.cityName,
                cityPOJO.description);
    }

    /**
     * find the City
     * @return 
     */
    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.GET)
    public CityPOJO findTheCity(@PathVariable("id") final long id) throws IDLException {
        return cityCRUDService.findTheCity(id,false);    }

}

