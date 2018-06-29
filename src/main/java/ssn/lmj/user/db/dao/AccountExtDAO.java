package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import ssn.lmj.user.db.dao.inc.AccountExtIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.AccountExtDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Thu Jun 28 22:39:57 CST 2018
 * Table: s_account_ext
 */
public interface AccountExtDAO extends AccountExtIndexQueryDAO { /* Add custom methods */ }