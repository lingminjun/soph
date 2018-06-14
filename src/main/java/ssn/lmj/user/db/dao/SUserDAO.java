package ssn.lmj.user.db.dao;


import ssn.lmj.user.db.dobj.SUserDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dao.inc.SUserIndexQueryDAO;
import com.lmj.stone.dao.SQL;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_user
 */
public interface SUserDAO extends SUserIndexQueryDAO {
    @SQL("select * from `s_user` where `mobile` = #{mobile}")
    List<SUserDO> findUserByMobile(@Param("mobile") String mobile);

    @SQL("select * from `s_user` where `email` = #{email}")
    List<SUserDO> findUserByEmail(@Param("email") String mobile);

    @SQL("select * from `s_user` where `id_number` = #{idNumber} limit 1")
    SUserDO findUserByIdNumber(@Param("idNumber") String idNumber);
}