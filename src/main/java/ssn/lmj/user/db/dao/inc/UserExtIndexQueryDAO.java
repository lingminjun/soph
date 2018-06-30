package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.UserExtDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_user_ext
 */
public interface UserExtIndexQueryDAO extends TableDAO<UserExtDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param uid  user id
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<UserExtDO> queryByUid(@Param("uid") long uid, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param uid  user id
     * @param dataType  数据类型
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<UserExtDO> queryByUidAndDataType(@Param("uid") long uid, @Param("dataType") String dataType, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param uid  user id
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByUid(@Param("uid") long uid, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param uid  user id
     * @param dataType  数据类型
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByUidAndDataType(@Param("uid") long uid, @Param("dataType") String dataType, @Param("isDelete") int isDelete);

}

