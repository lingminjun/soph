package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SAccountDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 22:49:13 CST 2018
 * Table: s_account
 */
public interface SAccountIndexQueryDAO extends TableDAO<SAccountDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param email  邮箱
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountDO> queryByEmail(@Param("email") String email, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param platform  平台名|大陆手机号(China mobile)|邮箱账号
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountDO> queryByPlatform(@Param("platform") String platform, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param platform  平台名|大陆手机号(China mobile)|邮箱账号
     * @param openId  又名Account:开放平台id|+86-15673886363|soulshangm@gmail.com
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountDO> queryByPlatformAndOpenId(@Param("platform") String platform, @Param("openId") String openId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param uid  user id,解除绑定后需要清空,且其他信息也需要被抹除
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SAccountDO> queryByUid(@Param("uid") long uid, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param email  邮箱
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByEmail(@Param("email") String email, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param platform  平台名|大陆手机号(China mobile)|邮箱账号
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByPlatform(@Param("platform") String platform, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param platform  平台名|大陆手机号(China mobile)|邮箱账号
     * @param openId  又名Account:开放平台id|+86-15673886363|soulshangm@gmail.com
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByPlatformAndOpenId(@Param("platform") String platform, @Param("openId") String openId, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param uid  user id,解除绑定后需要清空,且其他信息也需要被抹除
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByUid(@Param("uid") long uid, @Param("isDelete") int isDelete);

}

