package ssn.lmj.permission.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.permission.db.dao.inc.RoleIndexQueryDAO;
import ssn.lmj.permission.db.dobj.RoleDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_role
 */
public interface RoleDAO extends RoleIndexQueryDAO { /* Add custom methods */ }