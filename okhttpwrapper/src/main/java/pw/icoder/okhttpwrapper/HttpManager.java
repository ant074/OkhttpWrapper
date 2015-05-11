package pw.icoder.okhttpwrapper;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import pw.icoder.okhttpwrapper.common.AdapterCallback;
import pw.icoder.okhttpwrapper.data.RequestParams;

import android.content.Context;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class HttpManager {

    public static final MediaType MEDIA_TYPE_MARKDOWN=MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PNG=MediaType.parse("image/png");
    private Context mContext;
    private OkHttpClient mClient=new OkHttpClient();
    private HttpConfig mHttpConfig;
    private String[][] onceHeaders;
    private int tagNumber=0;

    public HttpManager(Context context) {
        this.mContext=context;
        this.setDefaultHttpConfig();
    }

    public HttpManager(Context context, HttpConfig httpConfig) {
        this.mContext=context;
        this.mHttpConfig=httpConfig;
        this.initOkHttpClient(mClient, mHttpConfig);
    }

    /**
     * 获取httpConfig配置
     * @return
     */
    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    /**
     * 设置默认http config
     */
    public void setDefaultHttpConfig() {
        this.mHttpConfig=new HttpConfig();
        this.mHttpConfig.setHttpPoolConfig();
        this.mHttpConfig.setNewCacheControlWithNoCache();
        this.mHttpConfig.setCookieStore(new PersistentCookieStore(mContext));
        this.initOkHttpClient(mClient, mHttpConfig);
    }

    // 初始化OkHttpClient
    private void initOkHttpClient(OkHttpClient client, HttpConfig httpConfig) {
        client.setConnectionPool(new ConnectionPool(httpConfig.getHttpPoolConfig().getMaxIdleConnections(), httpConfig
            .getHttpPoolConfig().getKeepAliveDurationMs()));
        client.setConnectTimeout(httpConfig.connectTimeout, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(httpConfig.writeTimeout, TimeUnit.MILLISECONDS);
        client.setReadTimeout(httpConfig.readTimeout, TimeUnit.MILLISECONDS);
        client.setProxy(httpConfig.getProxy());
        client.setCookieHandler(new CookieManager(httpConfig.getCookieStore(), CookiePolicy.ACCEPT_ALL));
        client.setCache(httpConfig.getResponseCache());
    }

    /**
     * 独立请求时临时设置headers
     * @param headers
     */
    public void setOnceHeaders(String[][] headers) {
        onceHeaders=headers;
    }

    /**
     * 同步get
     * @param url
     * @param callback
     * @param tag
     * @param httpConfig
     * @return tag what's used to cancel request
     * @throws java.io.IOException
     */
    public Object getSync(String url, RequestParams requestData, AdapterCallback callback) throws IOException {
        return getSyncRun(url, callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 异步get
     * @param url
     * @param callback
     * @param tag
     * @param httpConfig
     * @return tag what's used to cancel request
     */
    public Object get(String url, RequestParams requestData, AdapterCallback callback) {
        return getRun(url, callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 同步post
     * @param url
     * @param postData 键值对
     * @param callback
     * @param tag
     * @param httpConfig
     * @return tag what's used to cancel request
     * @throws java.io.IOException
     */
    public Object postSync(String url, RequestParams requestData, AdapterCallback callback) throws IOException {
        return postSyncRun(url, requestData.getRequestBody(), callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 异步post 提交键值对
     * @param url
     * @param postData 键值对
     * @param callback
     * @param tag
     * @param httpconfig
     * @return tag what's used to cancel request
     */
    public Object post(String url, RequestParams requestData, AdapterCallback callback) {
        return postRun(url, requestData.getRequestBody(), callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 下载
     * @param url
     * @param requestData
     * @param callback
     * @return
     */
    public Object download(String url, RequestParams requestData, AdapterCallback callback) {
        Object tag=requestData.getTag();
        if(tag == null)
            tag=createRequestTag();
        Request req=getRequestBuilder(requestData.getHttpConfig()).tag(tag).url(url).get().build();
        getHttpClient(requestData.getHttpConfig()).newCall(req).enqueue(callback);
        return tag;
    }

    /**
     * 获取request builder，此处将配置header头
     * @return
     */
    private Request.Builder getRequestBuilder(HttpConfig httpConfig) {
        Request.Builder builder=new Request.Builder();
        if(httpConfig == null)
            httpConfig=mHttpConfig;
        CacheControl tempControl=httpConfig.getRequestCacheControl();
        if(tempControl != null)
            builder.cacheControl(tempControl);
        if(httpConfig.getHeaders() != null) {
            String[][] headers=httpConfig.getHeaders();
            for(int i=0; i < headers.length; i++) {
                builder.addHeader(headers[i][0], headers[i][1]);
            }
        }
        if(onceHeaders == null)
            return builder;
        for(int i=0; i < onceHeaders.length; i++) {
            builder.addHeader(onceHeaders[i][0], onceHeaders[i][1]);
        }
        onceHeaders=null; // 清理
        return builder;
    }

    /**
     * 生成一个tag
     * @return
     */
    private Object createRequestTag() {
        tagNumber++;
        return new StringBuffer().append(System.currentTimeMillis()).append('-').append(tagNumber).toString();
    }

    /**
     * 同步get
     * @param url
     * @param callback
     * @throws java.io.IOException
     */
    private Object getSyncRun(String url, AdapterCallback callback, Object tag, HttpConfig httpConfig) throws IOException {
        if(tag == null)
            tag=createRequestTag();
        Request req=getRequestBuilder(httpConfig).tag(tag).url(url).build();
        Response res=getHttpClient(httpConfig).newCall(req).execute();
        handleResponse(req, res, callback);
        return tag;
    }

    /**
     * 同步post
     * @param url
     * @param reqBody
     * @param callback
     * @throws java.io.IOException
     */
    private Object postSyncRun(String url, RequestBody reqBody, AdapterCallback callback, Object tag, HttpConfig httpConfig)
        throws IOException {
        if(tag == null)
            tag=createRequestTag();
        Request req=getRequestBuilder(httpConfig).tag(tag).url(url).post(reqBody).build();
        Response res=getHttpClient(httpConfig).newCall(req).execute();
        handleResponse(req, res, callback);
        return tag;
    }

    /**
     * 异步get
     * @param url
     * @param callback
     */
    private Object getRun(String url, AdapterCallback callback, Object tag, HttpConfig httpConfig) {
        if(tag == null)
            tag=createRequestTag();
        Request req=getRequestBuilder(httpConfig).tag(tag).url(url).build();
        getHttpClient(httpConfig).newCall(req).enqueue(callback);

        return tag;
    }

    /**
     * 异步post
     * @param url
     * @param postData
     * @param callback
     */
    private Object postRun(String url, RequestBody body, AdapterCallback callback, Object tag, HttpConfig httpConfig) {
        if(tag == null)
            tag=createRequestTag();
        Request req=getRequestBuilder(httpConfig).tag(tag).url(url).post(body).build();
        getHttpClient(httpConfig).newCall(req).enqueue(callback);
        return tag;
    }

    /**
     * 处理response
     * @param req
     * @param res
     * @param callback
     * @throws java.io.IOException
     */
    private void handleResponse(Request req, Response res, AdapterCallback callback) throws IOException {
        if(res.isSuccessful()) { // code is in [200..300)
            callback.onResponse(res);
        } else {
            callback.onFailure(req, new IOException(String.valueOf(res.code())));
        }
    }

    /**
     * 获取okHttpClient
     * @return okHttpClient
     */
    public OkHttpClient getHttpClient() {
        return getHttpClient(null);
    }

    /**
     * clone新的httpConfig
     * @param onceHttpConfig
     * @return
     */
    private OkHttpClient getHttpClient(HttpConfig onceHttpConfig) {
        if(onceHttpConfig == null)
            return mClient;
        OkHttpClient newClient=mClient.clone();
        initOkHttpClient(newClient, onceHttpConfig);
        return newClient;
    }

    /**
     * 取消请求
     * @param tag
     * @return
     */
    public boolean cancelRequest(Object tag) {
        mClient.cancel(tag);
        return true;
    }
}
