package ssn.lmj.soph.cache;

import com.lmj.stone.cache.RemoteCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lingminjun
 * Date: 2018-06-22
 * Time: 下午4:34
 */
@Aspect
@Component
public class TheRemoteCacheAspect extends com.lmj.stone.cache.RemoteCacheAspect {

    @Resource(name = "theCache")
    private RemoteCache theCache;

    @Override
    public RemoteCache remoteCache() {
        return theCache;
    }

}
