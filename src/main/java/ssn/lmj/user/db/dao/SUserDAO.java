package ssn.lmj.user.db.dao;


import com.lmj.stone.dao.SQL;
import org.apache.ibatis.annotations.Param;
import ssn.lmj.user.db.dobj.SAccountDO;
import ssn.lmj.user.db.dobj.SUserDO;
import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Sat May 19 17:51:47 CST 2018
 * Table: s_user
 */
public interface SUserDAO extends TableDAO<SUserDO> {
    @SQL("select * from `s_user` where `mobile` = #{mobile}")
    List<SUserDO> findUserByMobile(@Param("mobile") String mobile);

    @SQL("select * from `s_user` where `email` = #{email}")
    List<SUserDO> findUserByEmail(@Param("email") String mobile);

    @SQL("select * from `s_user` where `id_number` = #{idNumber} limit 1")
    SUserDO findUserByIdNumber(@Param("idNumber") String idNumber);
}