package ssn.lmj.user.service.impl;

import com.lmj.stone.core.coding.Codeable;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * Description: HTTPUtil
 * User: lingminjun
 * Date: 2018-06-27
 * Time: 下午10:23
 */
public final class WebRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger("httplog");
    private static final int MAX_CONNECTION_SIZE = 50;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 30000;
    private static CloseableHttpClient hc = null;
    private static RequestConfig rc = null;

    public WebRequestUtil() {
    }

    private static CloseableHttpClient getHttpClient() {
        if (hc == null) {
            Class var0 = WebRequestUtil.class;
            synchronized(WebRequestUtil.class) {
                if (hc == null) {
                    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                    cm.setMaxTotal(50);
                    cm.setDefaultMaxPerRoute(50);
                    cm.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Consts.UTF_8).build());
                    hc = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
                        public long getKeepAliveDuration(HttpResponse arg0, HttpContext arg1) {
                            return 30000L;
                        }
                    }).build();
                    rc = RequestConfig.custom().setCookieSpec("ignoreCookies").setSocketTimeout(30000).setConnectTimeout(3000).setConnectionRequestTimeout(30000).setExpectContinueEnabled(false).setRedirectsEnabled(false).build();
                }
            }
        }

        return hc;
    }

    public static String getResponseString(String baseUrl, String params, boolean useGzip) {
        CloseableHttpResponse resp = null;
        String var5 = null;
        try {
            resp = getHttpResponse(baseUrl, params, useGzip);
            String result = EntityUtils.toString(resp.getEntity(), Codeable.UTF8);
            logger.info("req:" + baseUrl + "?" + params + " res:" + result);
            var5 = result;
        } catch (Throwable var15) {
            logger.error("req:" + baseUrl + "?" + params + "err:",var15);
        } finally {
            if (resp != null) {
                try {
                    resp.close();
                } catch (Throwable var14) {
                    var14.printStackTrace();
                }
            }
        }

        return var5;
    }

    public static byte[] getResponseBytes(String baseUrl, String params, boolean useGzip) {
        CloseableHttpResponse resp = null;
        byte[] var4 = null;
        try {
            resp = getHttpResponse(baseUrl, params, useGzip);
            var4 = EntityUtils.toByteArray(resp.getEntity());
            logger.info("req:" + baseUrl + "?" + params + " res-len:" + var4.length);
        } catch (Throwable var14) {
            logger.error("req:" + baseUrl + "?" + params + "err:",var14);
        } finally {
            if (resp != null) {
                try {
                    resp.close();
                } catch (Throwable var13) {
                    var13.printStackTrace();
                }
            }
        }
        return var4;
    }

    public static void fillResponse(String baseUrl, String params, boolean useGzip, WebRequestUtil.ResponseFiller f) {
        CloseableHttpResponse resp = null;
        InputStream is = null;
        try {
            resp = getHttpResponse(baseUrl, params, useGzip);
            is = resp.getEntity().getContent();
            f.fill(is);
            logger.info("req:" + baseUrl + "?" + params + " success true");
        } catch (Throwable var18) {
            logger.error("req:" + baseUrl + "?" + params + "err:",var18);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var17) {
                    var17.printStackTrace();
                }
            }
            if (resp != null) {
                try {
                    resp.close();
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
            }
        }
    }

    private static CloseableHttpResponse getHttpResponse(String baseUrl, String params, boolean useGzip) throws Exception {
        CloseableHttpClient client = getHttpClient();
        HttpRequestBase req = null;
        CloseableHttpResponse resp = null;

        try {
            if (params == null) {
                req = new HttpGet(baseUrl);
            } else if (params.length() > 200) {
                HttpPost post = new HttpPost(baseUrl);
                StringEntity se = new StringEntity(params, Codeable.UTF8);
                se.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
                post.setEntity(se);
                req = post;
            } else {
                req = new HttpGet(baseUrl + "?" + params);
            }

            ((HttpRequestBase)req).setConfig(rc);
            if (useGzip) {
                ((HttpRequestBase)req).setHeader("Accept-Encoding", "gzip");
            }
            resp = client.execute((HttpUriRequest)req);
        } catch (Exception var8) {
            if (req != null) {
                ((HttpRequestBase)req).abort();
            }
            throw var8;
        }

        if (resp != null) {
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                if (req != null) {
                    ((HttpRequestBase)req).abort();
                }
                throw new RuntimeException("请求异常！ code:" + statusCode);
            }
        }

        return resp;
    }

    public interface ResponseFiller {
        void fill(InputStream var1);
    }
}
