package ssn.lmj.user.db.dao;


import org.apache.ibatis.annotations.Param;

import ssn.lmj.user.db.dao.inc.SAccountIndexQueryDAO;
import com.lmj.stone.dao.SQL;
import ssn.lmj.user.db.dobj.SAccountDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_account
 */
public interface SAccountDAO extends SAccountIndexQueryDAO {
    @SQL("select * from `s_account` where `platform` = #{platform} and `open_id` = #{openId} limit 1")
    SAccountDO findAccountByAccount(@Param("platform") String platform,@Param("openId") String openId);
}