package ssn.lmj.permission.db.dao;

import com.lmj.stone.dao.TableDAO;
import ssn.lmj.permission.db.dobj.AccountRoleDO;
import ssn.lmj.permission.db.dao.inc.AccountRoleIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:50 CST 2018
 * Table: s_account_role
 */
public interface AccountRoleDAO extends AccountRoleIndexQueryDAO { /* Add custom methods */ }