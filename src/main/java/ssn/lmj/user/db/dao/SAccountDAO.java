package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;
import ssn.lmj.user.db.dobj.SAccountDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Sat May 19 17:51:47 CST 2018
 * Table: s_account
 */
public interface SAccountDAO extends TableDAO<SAccountDO> {
    @SQL("select * from `s_account` where `platform` = #{platform} and `open_id` = #{openId} limit 1")
    SAccountDO findAccountByAccount(@Param("platform") String platform,@Param("openId") String openId);
}