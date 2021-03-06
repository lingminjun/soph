package ssn.lmj.permission.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.permission.db.dobj.RolePermissionsDO;
import ssn.lmj.permission.db.dao.inc.RolePermissionsIndexQueryDAO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * Table: s_role_permissions
 */
public interface RolePermissionsDAO extends RolePermissionsIndexQueryDAO { /* Add custom methods */ }