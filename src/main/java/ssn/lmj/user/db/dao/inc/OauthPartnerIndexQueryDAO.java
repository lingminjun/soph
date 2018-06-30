package ssn.lmj.user.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import ssn.lmj.user.db.dobj.OauthPartnerDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 30 15:34:49 CST 2018
 * Table: s_oauth_partner
 */
public interface OauthPartnerIndexQueryDAO extends TableDAO<OauthPartnerDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param platform  第三方开放平台应用定义wechat_open,wechat_app
     * @param isDelete  0: enabled, 1: deleted
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<OauthPartnerDO> queryByPlatform(@Param("platform") String platform, @Param("isDelete") int isDelete,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param platform  第三方开放平台应用定义wechat_open,wechat_app
     * @param isDelete  0: enabled, 1: deleted
     * @return
     */
    public long countByPlatform(@Param("platform") String platform, @Param("isDelete") int isDelete);

}

