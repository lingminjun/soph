package ssn.lmj.user.db.dao;

import ssn.lmj.user.db.dobj.SUserDO;
import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Mon May 07 21:02:27 CST 2018
 * Table: s_user
 */
@Mapper
public interface SUserDAO extends TableDAO<SUserDO> { }

