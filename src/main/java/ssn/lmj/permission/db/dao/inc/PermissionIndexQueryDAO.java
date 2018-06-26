package ssn.lmj.permission.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.permission.db.dobj.PermissionDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Tue Jun 26 13:42:20 CST 2018
 * Table: s_permission
 */
public interface PermissionIndexQueryDAO extends TableDAO<PermissionDO> { 
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
    public List<PermissionDO> queryByDomain(@Param("domain") String domain, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param domain  权限分类或者权限作用域
     * @param key  权限定义键值(字母数字加下划线)
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<PermissionDO> queryByDomainAndKey(@Param("domain") String domain, @Param("key") String key, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param domain  权限分类或者权限作用域
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByDomain(@Param("domain") String domain, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param domain  权限分类或者权限作用域
     * @param key  权限定义键值(字母数字加下划线)
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByDomainAndKey(@Param("domain") String domain, @Param("key") String key, @Param("isDelete") int isDelete);

}

