package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SPermissionRolePermissionsDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_permission_role_permissions
 */
public interface SPermissionRolePermissionsIndexQueryDAO extends TableDAO<SPermissionRolePermissionsDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param roleId  Role Id
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SPermissionRolePermissionsDO> queryByRoleId(@Param("roleId") long roleId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param roleId  Role Id
     * @param permissionId  Permission Id
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SPermissionRolePermissionsDO> queryByRoleIdAndPermissionId(@Param("roleId") long roleId, @Param("permissionId") long permissionId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param roleId  Role Id
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByRoleId(@Param("roleId") long roleId, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param roleId  Role Id
     * @param permissionId  Permission Id
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByRoleIdAndPermissionId(@Param("roleId") long roleId, @Param("permissionId") long permissionId, @Param("isDelete") int isDelete);

}

