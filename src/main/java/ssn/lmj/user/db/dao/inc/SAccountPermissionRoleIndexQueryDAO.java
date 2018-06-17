package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dobj.SAccountPermissionRoleDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_account_permission_role
 */
public interface SAccountPermissionRoleIndexQueryDAO extends TableDAO<SAccountPermissionRoleDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param accountId  Account Id
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountPermissionRoleDO> queryByAccountId(@Param("accountId") long accountId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param accountId  Account Id
     * @param roleId  Role Id
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountPermissionRoleDO> queryByAccountIdAndRoleId(@Param("accountId") long accountId, @Param("roleId") long roleId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param accountId  Account Id
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByAccountId(@Param("accountId") long accountId, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param accountId  Account Id
     * @param roleId  Role Id
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByAccountIdAndRoleId(@Param("accountId") long accountId, @Param("roleId") long roleId, @Param("isDelete") int isDelete);

}

