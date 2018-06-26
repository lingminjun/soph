package ssn.lmj.permission.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.permission.db.dobj.RoleDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_role
 */
public interface RoleIndexQueryDAO extends TableDAO<RoleDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param domain  权限分类或者权限作用域
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<RoleDO> queryByDomain(@Param("domain") String domain, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param domain  权限分类或者权限作用域
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByDomain(@Param("domain") String domain, @Param("isDelete") int isDelete);

}

