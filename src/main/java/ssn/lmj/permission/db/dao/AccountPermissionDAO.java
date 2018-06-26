package ssn.lmj.permission.db.dao;

import com.lmj.stone.dao.TableDAO;
import ssn.lmj.permission.db.dao.inc.AccountPermissionIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.permission.db.dobj.AccountPermissionDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_account_permission
 */
public interface AccountPermissionDAO extends AccountPermissionIndexQueryDAO { /* Add custom methods */ }