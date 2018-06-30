package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dao.inc.AccountExtIndexQueryDAO;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;
import ssn.lmj.user.db.dobj.AccountExtDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_account_ext
 */
public interface AccountExtDAO extends AccountExtIndexQueryDAO {
    @SQL("select * from `s_account_ext` where `account_id` = #{accountId} and  and `is_delete` = 0 and `data_type` in \n" +
            "        <foreach collection=\"list\" item=\"theFieldName\" index=\"index\"  \n" +
            "           open=\"(\" close=\")\" separator=\",\">  \n" +
            "           #{theFieldName}  \n" +
            "        </foreach> \n")
    List<AccountExtDO> queryAttributesForKeys(@Param("accountId") long accountId,@Param("list") List<String> list);

    @SQL("select * from `s_account_ext` where `account_id` = #{accountId} and `data_type` = #{fieldName} \n")
    AccountExtDO getAttributeForKey(@Param("accountId") long accountId, @Param("fieldName") String fieldName);
}