package ssn.lmj.soph.db.dao;


import com.lmj.stone.dao.TableDAO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import ssn.lmj.soph.db.dao.inc.SDataDetailIndexQueryDAO;
import ssn.lmj.soph.db.dobj.SDataDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 22:49:13 CST 2018
 * Table: s_data_detail
 */
public interface SDataDetailDAO extends SDataDetailIndexQueryDAO {

    @SQL("select * from `s_data_detail` where `did` = #{did} and `subid` = #{subid}")
    SDataDetailDO findByDidAndSubId(@Param("did") long did, @Param("subid") long subid);

    @SQL("select * from `s_data_detail` where `subid` en \n" +
            "        <foreach collection=\"list\" item=\"theId\" index=\"index\"  \n" +
            "           open=\"(\" close=\")\" separator=\",\">  \n" +
            "           #{theId}  \n" +
            "        </foreach> ")
    List<SDataDetailDO> queryBySuIds(List<Long> list);

    //最小母串
    @SQL("select * from `s_data_detail` where `subid` = #{subid} ORDER BY `len` AES limit 5")
    List<SDataDetailDO> findByMinSubId(@Param("subid") long subid);

    //最大子串
    @SQL("select * from `s_data_detail` where `did` = #{did} ORDER BY `slen` DESC limit 5")
    List<SDataDetailDO> findByMaxDId(@Param("did") long did);
}