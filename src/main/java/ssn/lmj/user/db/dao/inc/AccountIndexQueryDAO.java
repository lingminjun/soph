package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.user.db.dobj.AccountDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Thu Jun 28 22:39:57 CST 2018
 * Table: s_account
 */
public interface AccountIndexQueryDAO extends TableDAO<AccountDO> { 
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
    public List<AccountDO> queryByEmail(@Param("email") String email, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param mobile  手机号
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<AccountDO> queryByMobile(@Param("mobile") String mobile, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

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
    public List<AccountDO> queryByPlatform(@Param("platform") String platform, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

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
    public List<AccountDO> queryByPlatformAndOpenId(@Param("platform") String platform, @Param("openId") String openId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

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
    public List<AccountDO> queryByUid(@Param("uid") long uid, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param union  三方平台联盟平台，如微信，主要是与下面unionId对应
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<AccountDO> queryByUnion(@Param("union") String union, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param union  三方平台联盟平台，如微信，主要是与下面unionId对应
     * @param unionId  开放平台唯一id，部分平台才有，如微信unionId
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<AccountDO> queryByUnionAndUnionId(@Param("union") String union, @Param("unionId") String unionId, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param email  邮箱
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByEmail(@Param("email") String email, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param mobile  手机号
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByMobile(@Param("mobile") String mobile, @Param("isDelete") int isDelete);

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

    /**
     * 根据以下索引字段计算count
     * @param union  三方平台联盟平台，如微信，主要是与下面unionId对应
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByUnion(@Param("union") String union, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param union  三方平台联盟平台，如微信，主要是与下面unionId对应
     * @param unionId  开放平台唯一id，部分平台才有，如微信unionId
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByUnionAndUnionId(@Param("union") String union, @Param("unionId") String unionId, @Param("isDelete") int isDelete);

}

