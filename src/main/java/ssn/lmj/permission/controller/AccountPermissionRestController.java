package ssn.lmj.permission.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ssn.lmj.permission.service.entities.AccountPermissionPOJO;
import ssn.lmj.permission.service.entities.AccountPermissionResults;
import ssn.lmj.permission.service.AccountPermissionCRUDService;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * SQLFile: sqls/permission.sql
 */
@RestController
public class AccountPermissionRestController {

    @Autowired
    private AccountPermissionCRUDService accountPermissionCRUDService;

    /**
     * create a AccountPermission
     * @return AccountPermission.id 
     */
    @RequestMapping(value = "/api/permission/account_permission", method = RequestMethod.POST)
    public long createAccountPermission(@RequestBody final AccountPermissionPOJO accountPermissionPOJO) throws IDLException {
        return accountPermissionCRUDService.addAccountPermission(accountPermissionPOJO);
    }

    /**
     * delete the AccountPermission
     * @return 
     */
    @RequestMapping(value = "/api/permission/account_permission/{id}", method = RequestMethod.DELETE)
    public boolean deleteTheAccountPermission(@PathVariable("id") final long id) throws IDLException {
        return accountPermissionCRUDService.removeTheAccountPermission(id);
    }

    /**
     * update the AccountPermission
     * @return 
     */
    @RequestMapping(value = "/api/permission/account_permission", method = RequestMethod.PUT)
    public boolean updateTheAccountPermission(@RequestBody final AccountPermissionPOJO accountPermissionPOJO) throws IDLException {
        return accountPermissionCRUDService.updateTheAccountPermission(accountPermissionPOJO.id,
                accountPermissionPOJO.accountId,
                accountPermissionPOJO.permissionId,
                accountPermissionPOJO.permissionKey,
                accountPermissionPOJO.permissionName,
                accountPermissionPOJO.domain);
    }

    /**
     * find the AccountPermission
     * @return 
     */
    @RequestMapping(value = "/api/permission/account_permission/{id}", method = RequestMethod.GET)
    public AccountPermissionPOJO findTheAccountPermission(@PathVariable("id") final long id) throws IDLException {
        return accountPermissionCRUDService.findTheAccountPermission(id,false);
    }

}

