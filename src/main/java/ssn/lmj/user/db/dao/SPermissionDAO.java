package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dao.inc.SPermissionIndexQueryDAO;
import ssn.lmj.user.db.dobj.SPermissionDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 22:49:13 CST 2018
 * Table: s_permission
 */
public interface SPermissionDAO extends SPermissionIndexQueryDAO { /* Add custom methods */ }