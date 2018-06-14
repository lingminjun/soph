package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SPermissionRolePermissionsDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Jun 14 23:52:47 CST 2018
 * Table: s_permission_role_permissions
 */
public interface SPermissionRolePermissionsIndexQueryDAO extends TableDAO<SPermissionRolePermissionsDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param roleId  Role Id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SPermissionRolePermissionsDO> queryByRoleId(@Param("roleId") long roleId,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param roleId  Role Id
     * @param permissionId  Permission Id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param limit 排序为降序
     * @return
     */
    public List<SPermissionRolePermissionsDO> queryByRoleIdAndPermissionId(@Param("roleId") long roleId, @Param("permissionId") long permissionId,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("limit") int limit);

}

