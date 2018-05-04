package ssn.lmj.soph.cache;

import com.lmj.stone.jedis.JedisPoolHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-05-04
 * Time: 上午11:45
 */
@Component("cacheRedis")
public class CacheRedis extends JedisPoolHolder {

    public CacheRedis(@Value("${default.redis.host}") String host,
                      @Value("${default.redis.port}") int port,
                      @Value("${default.redis.passWord}") String passWord,
                      @Value("${default.redis.maxTotal}") int maxTotal,
                      @Value("${default.redis.maxWait}") long maxWait,
                      @Value("${default.redis.minIdle}") int minIdle,
                      @Value("${default.redis.maxIdle}") int maxIdle,
                      @Value("${default.redis.testOnBorrow}") boolean testOnBorrow,
                      @Value("${default.redis.timeout}") int timeout) {
        super(host, port, passWord, maxTotal, maxWait, minIdle, maxIdle, testOnBorrow, timeout);
    }
}
