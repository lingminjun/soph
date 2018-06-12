package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.lmj.stone.dao.SQL;
import ssn.lmj.user.db.dobj.SCaptchaDO;
import ssn.lmj.user.db.dobj.SAccountDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Sat May 19 17:51:47 CST 2018
 * Table: s_captcha
 */
public interface SCaptchaDAO extends TableDAO<SCaptchaDO> {
    @SQL("select * from `s_captcha` where `type` = #{type} and `session` = #{session} and create_at > #{after} order by id desc")
    List<SCaptchaDO> findLatestCaptchaBySession(@Param("type") String type,
                                                @Param("session") String session,
                                                @Param("after") long after);

    @SQL("select * from `s_captcha` where `type` = #{type} and `session` = #{session} and `code` = #{code} and create_at > #{after} order by id desc")
    List<SCaptchaDO> findLatestCaptchaByCode(@Param("type") String type,
                                             @Param("session") String session,
                                             @Param("code") String code,
                                             @Param("after") long after);
}