package ssn.lmj.soph.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import ssn.lmj.soph.db.dobj.SDataGroupDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_data_group
 */
public interface SDataGroupIndexQueryDAO extends TableDAO<SDataGroupDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param gid  抽象类型id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SDataGroupDO> queryByGid(@Param("gid") long gid,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param gid  抽象类型id
     * @param did  数据id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SDataGroupDO> queryByGidAndDid(@Param("gid") long gid, @Param("did") long did,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

}

