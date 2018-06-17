package ssn.lmj.soph.db.dao.inc;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import ssn.lmj.soph.db.dobj.SDataDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_data
 */
public interface SDataIndexQueryDAO extends TableDAO<SDataDO> { 
    /**
     * 根据以下索引字段查询实体对象集
     * @param hcode  内容 hash code，这里取java hash code
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SDataDO> queryByHcode(@Param("hcode") int hcode,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param hcode  内容 hash code，这里取java hash code
     * @param md5  内容 md5
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SDataDO> queryByHcodeAndMd5(@Param("hcode") int hcode, @Param("md5") String md5,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段查询实体对象集
     * @param hcode  内容 hash code，这里取java hash code
     * @param md5  内容 md5
     * @param len  内容长度
     * @param sortField 排序字段，传入null时表示不写入sql
     * @param isDesc 排序为降序
     * @param offset 其实位置
     * @param limit  返回条数
     * @return
     */
    public List<SDataDO> queryByHcodeAndMd5AndLen(@Param("hcode") int hcode, @Param("md5") String md5, @Param("len") int len,@Param("sortField") String sortField,@Param("isDesc") boolean isDesc,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据以下索引字段计算count
     * @param hcode  内容 hash code，这里取java hash code
     * @return
     */
    public long countByHcode(@Param("hcode") int hcode);

    /**
     * 根据以下索引字段计算count
     * @param hcode  内容 hash code，这里取java hash code
     * @param md5  内容 md5
     * @return
     */
    public long countByHcodeAndMd5(@Param("hcode") int hcode, @Param("md5") String md5);

    /**
     * 根据以下索引字段计算count
     * @param hcode  内容 hash code，这里取java hash code
     * @param md5  内容 md5
     * @param len  内容长度
     * @return
     */
    public long countByHcodeAndMd5AndLen(@Param("hcode") int hcode, @Param("md5") String md5, @Param("len") int len);

}

