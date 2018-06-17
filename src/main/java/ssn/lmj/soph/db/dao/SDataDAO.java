package ssn.lmj.soph.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.soph.db.dobj.SDataDO;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;
import ssn.lmj.soph.db.dao.inc.SDataIndexQueryDAO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 13:35:19 CST 2018
 * Table: s_data
 */
public interface SDataDAO extends SDataIndexQueryDAO {

    @SQL("select * from `s_data` where `hcode` = #{hcode} and `md5` = #{md5} and `len` = #{len} and `bites` = #{bites}")
    SDataDO findByBites(@Param("hcode") int hcode, @Param("md5") String md5,@Param("len") int len, @Param("bites") String bites);

    @SQL("select * from `s_data` where `did` in \n" +
            "        <foreach collection=\"list\" item=\"theId\" index=\"index\"  \n" +
            "           open=\"(\" close=\")\" separator=\",\">  \n" +
            "           #{theId}  \n" +
            "        </foreach> \n" +
            "        order by ts desc limit 3")
    List<SDataDO> queryMaxScoreByIds(List<Long> list);

    @SQL("select * from `s_data` where `len` >= #{flen} and `len` <= #{tlen} limit #{size}")
    List<SDataDO> querySimilarityByLen(@Param("flen") int flen,@Param("tlen") int tlen, @Param("size") int size);
}