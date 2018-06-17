package ssn.lmj.soph.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.soph.db.dobj.SGroupAsDO;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 13:35:19 CST 2018
 * Table: s_group_as
 */
public interface SGroupAsIndexQueryDAO extends TableDAO<SGroupAsDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param gid  抽象类型id
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SGroupAsDO> queryByGid(@Param("gid") long gid,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param gid  抽象类型id
     * @param asid  
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SGroupAsDO> queryByGidAndAsid(@Param("gid") long gid, @Param("asid") long asid,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

}

