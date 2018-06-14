package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SAccountPermissionDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_account_permission
 */
public interface SAccountPermissionIndexQueryDAO extends TableDAO<SAccountPermissionDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param accountId  Account Id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SAccountPermissionDO> queryByAccountId(@Param("accountId") long accountId,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param accountId  Account Id
     * @param permissionId  Permission Id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SAccountPermissionDO> queryByAccountIdAndPermissionId(@Param("accountId") long accountId, @Param("permissionId") long permissionId,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

}

