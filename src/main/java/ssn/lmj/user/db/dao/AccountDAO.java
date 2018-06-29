package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import ssn.lmj.user.db.dobj.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;
import ssn.lmj.user.db.dao.inc.AccountIndexQueryDAO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Thu Jun 28 22:39:57 CST 2018
 * Table: s_account
 */
public interface AccountDAO extends AccountIndexQueryDAO {
    @SQL("select * from `s_account` where `platform` = #{platform} and `open_id` = #{openId} limit 1")
    AccountDO findAccountByAccount(@Param("platform") String platform, @Param("openId") String openId);
}