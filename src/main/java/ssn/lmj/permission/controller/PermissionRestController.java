package ssn.lmj.permission.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ssn.lmj.permission.service.entities.PermissionPOJO;
import ssn.lmj.permission.service.entities.PermissionResults;
import ssn.lmj.permission.service.PermissionCRUDService;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * SQLFile: sqls/permission.sql
 */
@RestController
public class PermissionRestController {

    @Autowired
    private PermissionCRUDService permissionCRUDService;

    /**
     * create a Permission
     * @return Permission.id 
     */
    @RequestMapping(value = "/api/permission/permission", method = RequestMethod.POST)
    public long createPermission(@RequestBody final PermissionPOJO permissionPOJO) throws IDLException {
        return permissionCRUDService.addPermission(permissionPOJO);
    }

    /**
     * delete the Permission
     * @return 
     */
    @RequestMapping(value = "/api/permission/permission/{id}", method = RequestMethod.DELETE)
    public boolean deleteThePermission(@PathVariable("id") final long id) throws IDLException {
        return permissionCRUDService.removeThePermission(id);
    }

    /**
     * update the Permission
     * @return 
     */
    @RequestMapping(value = "/api/permission/permission", method = RequestMethod.PUT)
    public boolean updateThePermission(@RequestBody final PermissionPOJO permissionPOJO) throws IDLException {
        return permissionCRUDService.updateThePermission(permissionPOJO.id,
                permissionPOJO.domain,
                permissionPOJO.key,
                permissionPOJO.name,
                permissionPOJO.cmmt);
    }

    /**
     * find the Permission
     * @return 
     */
    @RequestMapping(value = "/api/permission/permission/{id}", method = RequestMethod.GET)
    public PermissionPOJO findThePermission(@PathVariable("id") final long id) throws IDLException {
        return permissionCRUDService.findThePermission(id,false);
    }

}

