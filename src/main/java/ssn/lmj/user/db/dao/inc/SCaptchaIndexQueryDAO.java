package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SCaptchaDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_captcha
 */
public interface SCaptchaIndexQueryDAO extends TableDAO<SCaptchaDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param type  验证码类型
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SCaptchaDO> queryByType(@Param("type") String type, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param type  验证码类型
     * @param session  会话、或者token,或者手机号
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SCaptchaDO> queryByTypeAndSession(@Param("type") String type, @Param("session") String session, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param type  验证码类型
     * @param session  会话、或者token,或者手机号
     * @param code  验证码
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SCaptchaDO> queryByTypeAndSessionAndCode(@Param("type") String type, @Param("session") String session, @Param("code") String code, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param type  验证码类型
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByType(@Param("type") String type, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param type  验证码类型
     * @param session  会话、或者token,或者手机号
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByTypeAndSession(@Param("type") String type, @Param("session") String session, @Param("isDelete") int isDelete);

    /**
     * 根据以下索引字段计算count
     * @param type  验证码类型
     * @param session  会话、或者token,或者手机号
     * @param code  验证码
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByTypeAndSessionAndCode(@Param("type") String type, @Param("session") String session, @Param("code") String code, @Param("isDelete") int isDelete);

}

