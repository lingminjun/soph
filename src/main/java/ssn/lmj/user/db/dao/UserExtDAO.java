package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import ssn.lmj.user.db.dao.inc.UserExtIndexQueryDAO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;
import ssn.lmj.user.db.dobj.UserExtDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_user_ext
 */
public interface UserExtDAO extends UserExtIndexQueryDAO {
    @SQL("select * from `s_user_ext` where `uid` = #{uid} and `is_delete` = 0 and `data_type` in \n" +
            "        <foreach collection=\"list\" item=\"theFieldName\" index=\"index\"  \n" +
            "           open=\"(\" close=\")\" separator=\",\">  \n" +
            "           #{theFieldName}  \n" +
            "        </foreach> \n")
    List<UserExtDO> queryAttributesForKeys(@Param("uid") long uid, @Param("list") List<String> list);

    @SQL("select * from `s_user_ext` where `uid` = #{uid} and `data_type` = #{fieldName} \n")
    UserExtDO getAttributeForKey(@Param("uid") long uid, @Param("fieldName") String fieldName);
}