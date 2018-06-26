package ssn.lmj.permission.controller;

import com.lmj.stone.idl.IDLException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ssn.lmj.permission.service.entities.AccountRolePOJO;
import ssn.lmj.permission.service.entities.AccountRoleResults;
import ssn.lmj.permission.service.AccountRoleCRUDService;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * SQLFile: sqls/permission.sql
 */
@RestController
public class AccountRoleRestController {

    @Autowired
    private AccountRoleCRUDService accountRoleCRUDService;

    /**
     * create a AccountRole
     * @return AccountRole.id 
     */
    @RequestMapping(value = "/api/permission/account_role", method = RequestMethod.POST)
    public long createAccountRole(@RequestBody final AccountRolePOJO accountRolePOJO) throws IDLException {
        return accountRoleCRUDService.addAccountRole(accountRolePOJO);
    }

    /**
     * delete the AccountRole
     * @return 
     */
    @RequestMapping(value = "/api/permission/account_role/{id}", method = RequestMethod.DELETE)
    public boolean deleteTheAccountRole(@PathVariable("id") final long id) throws IDLException {
        return accountRoleCRUDService.removeTheAccountRole(id);
    }

    /**
     * update the AccountRole
     * @return 
     */
    @RequestMapping(value = "/api/permission/account_role", method = RequestMethod.PUT)
    public boolean updateTheAccountRole(@RequestBody final AccountRolePOJO accountRolePOJO) throws IDLException {
        return accountRoleCRUDService.updateTheAccountRole(accountRolePOJO.id,
                accountRolePOJO.accountId,
                accountRolePOJO.roleId);
    }

    /**
     * find the AccountRole
     * @return 
     */
    @RequestMapping(value = "/api/permission/account_role/{id}", method = RequestMethod.GET)
    public AccountRolePOJO findTheAccountRole(@PathVariable("id") final long id) throws IDLException {
        return accountRoleCRUDService.findTheAccountRole(id,false);
    }

}

