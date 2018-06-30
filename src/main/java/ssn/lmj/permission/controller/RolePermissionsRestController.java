package ssn.lmj.permission.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ssn.lmj.permission.service.entities.RolePermissionsPOJO;
import ssn.lmj.permission.service.entities.RolePermissionsResults;
import ssn.lmj.permission.service.RolePermissionsCRUDService;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@RestController
public class RolePermissionsRestController {

    @Autowired
    private RolePermissionsCRUDService rolePermissionsCRUDService;

    /**
     * create a RolePermissions
     * @return RolePermissions.id 
     */
    @RequestMapping(value = "/api/permission/role_permissions", method = RequestMethod.POST)
    public long createRolePermissions(@RequestBody final RolePermissionsPOJO rolePermissionsPOJO) throws IDLException {
        return rolePermissionsCRUDService.addRolePermissions(rolePermissionsPOJO);
    }

    /**
     * delete the RolePermissions
     * @return 
     */
    @RequestMapping(value = "/api/permission/role_permissions/{id}", method = RequestMethod.DELETE)
    public boolean deleteTheRolePermissions(@PathVariable("id") final long id) throws IDLException {
        return rolePermissionsCRUDService.removeTheRolePermissions(id);
    }

    /**
     * update the RolePermissions
     * @return 
     */
    @RequestMapping(value = "/api/permission/role_permissions", method = RequestMethod.PUT)
    public boolean updateTheRolePermissions(@RequestBody final RolePermissionsPOJO rolePermissionsPOJO) throws IDLException {
        return rolePermissionsCRUDService.updateTheRolePermissions(rolePermissionsPOJO.id,
                rolePermissionsPOJO.roleId,
                rolePermissionsPOJO.permissionId,
                rolePermissionsPOJO.permissionKey,
                rolePermissionsPOJO.permissionName,
                rolePermissionsPOJO.domain);
    }

    /**
     * find the RolePermissions
     * @return 
     */
    @RequestMapping(value = "/api/permission/role_permissions/{id}", method = RequestMethod.GET)
    public RolePermissionsPOJO findTheRolePermissions(@PathVariable("id") final long id) throws IDLException {
        return rolePermissionsCRUDService.findTheRolePermissions(id,false);
    }

}

