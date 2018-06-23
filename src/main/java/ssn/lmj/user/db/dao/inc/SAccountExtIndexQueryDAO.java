package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dobj.SAccountExtDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 22:49:13 CST 2018
 * Table: s_account_ext
 */
public interface SAccountExtIndexQueryDAO extends TableDAO<SAccountExtDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param accountId  account id
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountExtDO> queryByAccountId(@Param("accountId") long accountId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param accountId  account id
     * @param dataType  数据类型
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountExtDO> queryByAccountIdAndDataType(@Param("accountId") long accountId, @Param("dataType") String dataType, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param accountId  account id
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByAccountId(@Param("accountId") long accountId, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param accountId  account id
     * @param dataType  数据类型
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByAccountIdAndDataType(@Param("accountId") long accountId, @Param("dataType") String dataType, @Param("isDelete") int isDelete);

}

