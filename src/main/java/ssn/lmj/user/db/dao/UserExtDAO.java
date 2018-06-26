package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import ssn.lmj.user.db.dao.inc.UserExtIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.UserExtDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_user_ext
 */
public interface UserExtDAO extends UserExtIndexQueryDAO { /* Add custom methods */ }