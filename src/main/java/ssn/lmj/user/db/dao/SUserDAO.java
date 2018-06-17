package ssn.lmj.user.db.dao;

import ssn.lmj.user.db.dobj.SUserDO;
import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dao.inc.SUserIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
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