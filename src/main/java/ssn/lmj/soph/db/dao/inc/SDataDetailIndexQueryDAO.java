package ssn.lmj.soph.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.soph.db.dobj.SDataDetailDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_data_detail
 */
public interface SDataDetailIndexQueryDAO extends TableDAO<SDataDetailDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param did  data id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SDataDetailDO> queryByDid(@Param("did") long did,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param did  data id
     * @param subid  sub data id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SDataDetailDO> queryByDidAndSubid(@Param("did") long did, @Param("subid") long subid,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

}

