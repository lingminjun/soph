package ssn.lmj.permission.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ssn.lmj.permission.service.entities.RolePOJO;
import ssn.lmj.permission.service.entities.RoleResults;
import ssn.lmj.permission.service.RoleCRUDService;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@RestController
public class RoleRestController {

    @Autowired
    private RoleCRUDService roleCRUDService;

    /**
     * create a Role
     * @return Role.id 
     */
    @RequestMapping(value = "/api/permission/role", method = RequestMethod.POST)
    public long createRole(@RequestBody final RolePOJO rolePOJO) throws IDLException {
        return roleCRUDService.addRole(rolePOJO);
    }

    /**
     * delete the Role
     * @return 
     */
    @RequestMapping(value = "/api/permission/role/{id}", method = RequestMethod.DELETE)
    public boolean deleteTheRole(@PathVariable("id") final long id) throws IDLException {
        return roleCRUDService.removeTheRole(id);
    }

    /**
     * update the Role
     * @return 
     */
    @RequestMapping(value = "/api/permission/role", method = RequestMethod.PUT)
    public boolean updateTheRole(@RequestBody final RolePOJO rolePOJO) throws IDLException {
        return roleCRUDService.updateTheRole(rolePOJO.id,
                rolePOJO.domain,
                rolePOJO.name,
                rolePOJO.cmmt);
    }

    /**
     * find the Role
     * @return 
     */
    @RequestMapping(value = "/api/permission/role/{id}", method = RequestMethod.GET)
    public RolePOJO findTheRole(@PathVariable("id") final long id) throws IDLException {
        return roleCRUDService.findTheRole(id,false);
    }

}

