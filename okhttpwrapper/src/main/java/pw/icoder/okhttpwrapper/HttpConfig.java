package pw.icoder.okhttpwrapper;

import java.io.File;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;

/**
 * http连接配置类
 * @author wx@shuzijie.com
 */
public class HttpConfig {

    public int maxConnections=10; // 连接池配置 1

    public long keepAliveDurationMs=5 * 60 * 1000; // 连接池配置 2

    public long connectTimeout=10 * 1000; // 连接超时

    public long writeTimeout=10 * 1000; // 写超时

    public long readTimeout=20 * 1000; // 读超时

    private HttpPoolConfig httpPoolConfig; // 连接池配置

    private CacheControl requestCacheControl; // request缓存控制

    private Cache responseCache; // response 缓存

    private PersistentCookieStore cookieStore; // cookie handler

    private Proxy proxy; // proxy

    private String[][] headers;

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy=proxy;
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(PersistentCookieStore cookieStore) {
        this.cookieStore=cookieStore;
    }

    public Cache getResponseCache() {
        return responseCache;
    }

    public String[][] getHeaders() {
        return headers;
    }

    public void setHeaders(String[][] headers) {
        this.headers=headers;
    }

    /**
     * 设置响应的缓存
     * @param directory
     * @param maxSize
     * @throws Exception
     */
    public void setResponseCache(File directory, long maxSize) throws Exception {
        this.responseCache=new Cache(directory, maxSize);
    }

    /**
     * 获取连接池配置
     * @return
     */
    public HttpPoolConfig getHttpPoolConfig() {
        return httpPoolConfig;
    }

    /**
     * 连接池设置
     */
    public void setHttpPoolConfig() {
        this.httpPoolConfig=new HttpPoolConfig();
        this.httpPoolConfig.setMaxIdleConnections(maxConnections);
        this.httpPoolConfig.setKeepAliveDurationMs(keepAliveDurationMs);
    }

    public CacheControl getRequestCacheControl() {
        return requestCacheControl;
    }

    /**
     * 配置cacheCotrol
     * @param maxAge
     */
    public void setNewCacheControlWithAge(int maxAge) {
        this.requestCacheControl=new CacheControl.Builder().maxAge(maxAge, TimeUnit.SECONDS).build();
    }

    /**
     * 配置cacheCotrol nocache
     */
    public void setNewCacheControlWithNoCache() {
        this.requestCacheControl=new CacheControl.Builder().noCache().build();
    }

    /**
     * 配置cacheCotrol onlyifcached
     */
    public void setNewCacheControlWithOnlyIfCached() {
        this.requestCacheControl=new CacheControl.Builder().onlyIfCached().build();
    }

    /**
     * 配置cacheCotrol stale days
     * @param days
     */
    public void setNewCacheControlWithStale(int days) {
        this.requestCacheControl=new CacheControl.Builder().maxStale(days, TimeUnit.DAYS).build();
    }

    /**
     * 连接池配置类
     * @author wx@shuzijie.com
     */
    public class HttpPoolConfig {

        private int maxIdleConnections;

        private long keepAliveDurationMs;

        public int getMaxIdleConnections() {
            return maxIdleConnections;
        }

        public void setMaxIdleConnections(int maxIdleConnections) {
            this.maxIdleConnections=maxIdleConnections;
        }

        public long getKeepAliveDurationMs() {
            return keepAliveDurationMs;
        }

        public void setKeepAliveDurationMs(long keepAliveDurationMs) {
            this.keepAliveDurationMs=keepAliveDurationMs;
        }
    }

}
