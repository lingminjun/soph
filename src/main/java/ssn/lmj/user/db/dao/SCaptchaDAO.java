package ssn.lmj.user.db.dao;

import com.lmj.stone.dao.TableDAO;
import org.apache.ibatis.annotations.Mapper;
import ssn.lmj.user.db.dobj.SCaptchaDO;


/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Wed May 09 23:04:41 CST 2018
 * Table: s_captcha
 */
@Mapper
public interface SCaptchaDAO extends TableDAO<SCaptchaDO> { }

