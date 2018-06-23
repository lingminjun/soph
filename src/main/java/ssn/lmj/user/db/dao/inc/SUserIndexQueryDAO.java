package ssn.lmj.user.db.dao.inc;

import ssn.lmj.user.db.dobj.SUserDO;
import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 22:49:13 CST 2018
 * Table: s_user
 */
public interface SUserIndexQueryDAO extends TableDAO<SUserDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param idNumber  身份证号
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SUserDO> queryByIdNumber(@Param("idNumber") String idNumber,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param mobile  手机号
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SUserDO> queryByMobile(@Param("mobile") String mobile,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

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

