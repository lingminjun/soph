package ssn.lmj.soph.db.dao;


import java.util.List;

import com.lmj.stone.dao.SQL;
import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;

import ssn.lmj.soph.db.dobj.SDataDetailDO;



/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Apr 05 17:25:20 CST 2018
 * Table: s_data_detail
 */
public interface SDataDetailDAO extends TableDAO<SDataDetailDO> {

    @SQL("select * from `s_data_detail` where `did` = #{did} and `subid` = #{subid}")
    SDataDetailDO findByDidAndSubId(@Param("did") long did, @Param("subid") long subid);

    @SQL("select * from `s_data_detail` where `subid` in \n" +
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