package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SAccountDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Mon May 07 21:02:27 CST 2018
 * Table: s_account
 */
@Mapper
public interface SAccountDAO extends TableDAO<SAccountDO> { }

