package ssn.lmj.user.service;

import ssn.lmj.user.service.entities.Token;

/**
 * Created with IntelliJ IDEA.
 * Description: 认证分很多种，第一步是account，第二步是user
 * User: lingminjun
 * Date: 2018-05-07
 * Time: 下午9:09
 */
public interface AuthService {
    Token login(String account,String secret, String captcha, String token);
}
