package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dobj.UserDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Thu Jun 28 22:39:57 CST 2018
 * Table: s_user
 */
public interface UserIndexQueryDAO extends TableDAO<UserDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param email  邮箱
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<UserDO> queryByEmail(@Param("email") String email,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param idNumber  身份证号
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<UserDO> queryByIdNumber(@Param("idNumber") String idNumber,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param mobile  手机号
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<UserDO> queryByMobile(@Param("mobile") String mobile,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param email  邮箱
     * @return
     */
    public long countByEmail(@Param("email") String email);

    /**
     * 根据以下索引字段计算count
     * @param idNumber  身份证号
     * @return
     */
    public long countByIdNumber(@Param("idNumber") String idNumber);

    /**
     * 根据以下索引字段计算count
     * @param mobile  手机号
     * @return
     */
    public long countByMobile(@Param("mobile") String mobile);

}

