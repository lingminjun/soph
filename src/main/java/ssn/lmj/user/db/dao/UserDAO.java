package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dobj.UserDO;
import ssn.lmj.user.db.dao.inc.UserIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_user
 */
public interface UserDAO extends UserIndexQueryDAO {
    @SQL("select * from `s_user` where `mobile` = #{mobile}")
    List<UserDO> findUserByMobile(@Param("mobile") String mobile);

    @SQL("select * from `s_user` where `email` = #{email}")
    List<UserDO> findUserByEmail(@Param("email") String mobile);

    @SQL("select * from `s_user` where `id_number` = #{idNumber} limit 1")
    UserDO findUserByIdNumber(@Param("idNumber") String idNumber);
}