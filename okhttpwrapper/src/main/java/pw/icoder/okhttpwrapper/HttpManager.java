package pw.icoder.okhttpwrapper;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pw.icoder.okhttpwrapper.common.AdapterCallback;
import pw.icoder.okhttpwrapper.cookie.PersistentCookieJar;
import pw.icoder.okhttpwrapper.cookie.cache.SetCookieCache;
import pw.icoder.okhttpwrapper.cookie.persistence.SharedPrefsCookiePersistor;
import pw.icoder.okhttpwrapper.data.RequestParams;


public class HttpManager {

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Context mContext;
    private OkHttpClient.Builder mClientBuilder = new OkHttpClient.Builder();
    private OkHttpClient mClient;
    private HttpConfig mHttpConfig;
    private String[][] onceHeaders;
    private int tagNumber = 0;

    public HttpManager(Context context) {
        this.mContext = context;
        this.setDefaultHttpConfig();
        this.initOkHttpClient(mClientBuilder, mHttpConfig);
    }

    public HttpManager(Context context, HttpConfig httpConfig) {
        this.mContext = context;
        this.mHttpConfig = httpConfig;
        this.initOkHttpClient(mClientBuilder, mHttpConfig);
    }

    /**
     * 获取httpConfig配置
     *
     * @return
     */
    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    /**
     * 设置默认http config
     */
    public void setDefaultHttpConfig() {
        this.mHttpConfig = new HttpConfig();
        this.mHttpConfig.setHttpPoolConfig();
        this.mHttpConfig.setNewCacheControlWithNoCache();
        this.mHttpConfig.setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext)));
    }

    // 初始化OkHttpClient
    private void initOkHttpClient(OkHttpClient.Builder builder, HttpConfig httpConfig) {
        builder
                .connectionPool(new ConnectionPool(httpConfig.getHttpPoolConfig().getMaxIdleConnections(), httpConfig
                        .getHttpPoolConfig().getKeepAliveDurationMs(), TimeUnit.MILLISECONDS));
        builder.connectTimeout(httpConfig.connectTimeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(httpConfig.writeTimeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(httpConfig.readTimeout, TimeUnit.MILLISECONDS);
        builder.proxy(httpConfig.getProxy());
        builder.cookieJar(httpConfig.getCookieJar());
        builder.cache(httpConfig.getResponseCache());
        mClient = builder.build();
    }

    /**
     * 独立请求时临时设置headers
     *
     * @param headers
     */
    public void setOnceHeaders(String[][] headers) {
        onceHeaders = headers;
    }

    /**
     * 同步get
     *
     * @param url
     * @param requestData
     * @param callback
     * @return
     * @throws IOException
     */
    public Object getSync(String url, RequestParams requestData, AdapterCallback callback) throws IOException {
        return getSyncRun(url, callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 异步get
     *
     * @param url
     * @param requestData
     * @param callback
     * @return
     */
    public Object get(String url, RequestParams requestData, AdapterCallback callback) {
        return getRun(url, callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 同步post
     *
     * @param url
     * @param requestData
     * @param callback
     * @return
     * @throws IOException
     */
    public Object postSync(String url, RequestParams requestData, AdapterCallback callback) throws IOException {
        return postSyncRun(url, requestData.getRequestBody(), callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 异步post 提交键值对
     *
     * @param url
     * @param requestData
     * @param callback
     * @return
     */
    public Object post(String url, RequestParams requestData, AdapterCallback callback) {
        return postRun(url, requestData.getRequestBody(), callback, requestData.getTag(), requestData.getHttpConfig());
    }

    /**
     * 下载
     *
     * @param url
     * @param requestData
     * @param callback
     * @return
     */
    public Object download(String url, RequestParams requestData, AdapterCallback callback) {
        Object tag = requestData.getTag();
        if (tag == null)
            tag = createRequestTag();
        Request req = getRequestBuilder(requestData.getHttpConfig()).tag(tag).url(url).get().build();
        getHttpClient(requestData.getHttpConfig()).newCall(req).enqueue(callback);
        return tag;
    }

    /**
     * 获取request builder，此处将配置header头
     *
     * @return
     */
    private Request.Builder getRequestBuilder(HttpConfig httpConfig) {
        Request.Builder builder = new Request.Builder();
        if (httpConfig == null)
            httpConfig = mHttpConfig;
        CacheControl tempControl = httpConfig.getRequestCacheControl();
        if (tempControl != null)
            builder.cacheControl(tempControl);
        if (httpConfig.getHeaders() != null) {
            String[][] headers = httpConfig.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                builder.addHeader(headers[i][0], headers[i][1]);
            }
        }
        if (onceHeaders == null)
            return builder;
        for (int i = 0; i < onceHeaders.length; i++) {
            builder.addHeader(onceHeaders[i][0], onceHeaders[i][1]);
        }
        onceHeaders = null; // 清理
        return builder;
    }

    /**
     * 生成一个tag
     *
     * @return
     */
    private Object createRequestTag() {
        tagNumber++;
        return new StringBuffer().append(System.currentTimeMillis()).append('-').append(tagNumber).toString();
    }

    /**
     * 同步get
     *
     * @param url
     * @param callback
     * @throws IOException
     */
    private Object getSyncRun(String url, AdapterCallback callback, Object tag, HttpConfig httpConfig) throws IOException {
        if (tag == null)
            tag = createRequestTag();
        try {
            Request req = getRequestBuilder(httpConfig).tag(tag).url(url).build();
            Call call = getHttpClient(httpConfig).newCall(req);
            Response res = call.execute();
            handleResponse(call, req, res, callback);
        } catch (Exception e){
        e.printStackTrace();
        }
        return tag;
    }

    /**
     * 同步post
     *
     * @param url
     * @param reqBody
     * @param callback
     * @throws IOException
     */
    private Object postSyncRun(String url, RequestBody reqBody, AdapterCallback callback, Object tag, HttpConfig httpConfig)
            throws IOException {
        if (tag == null)
            tag = createRequestTag();
        try{
        Request req = getRequestBuilder(httpConfig).tag(tag).url(url).post(reqBody).build();
        Call call = getHttpClient(httpConfig).newCall(req);
        Response res = call.execute();
        handleResponse(call, req, res, callback);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tag;
    }

    /**
     * 异步get
     *
     * @param url
     * @param callback
     */
    private Object getRun(String url, AdapterCallback callback, Object tag, HttpConfig httpConfig) {
        if (tag == null)
            tag = createRequestTag();
        try {
            Request req = getRequestBuilder(httpConfig).tag(tag).url(url).build();
            getHttpClient(httpConfig).newCall(req).enqueue(callback);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tag;
    }

    /**
     * 异步post
     *
     * @param url
     * @param body
     * @param callback
     * @param tag
     * @param httpConfig
     * @return
     */
    private Object postRun(String url, RequestBody body, AdapterCallback callback, Object tag, HttpConfig httpConfig) {
        if (tag == null)
            tag = createRequestTag();
        try {
            Request req = getRequestBuilder(httpConfig).tag(tag).url(url).post(body).build();
            getHttpClient(httpConfig).newCall(req).enqueue(callback);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tag;
    }

    /**
     * 处理response
     *
     * @param req
     * @param res
     * @param callback
     * @throws IOException
     */
    private void handleResponse(Call call, Request req, Response res, AdapterCallback callback) throws IOException {
        if (res.isSuccessful()) { // code is in [200..300)
            callback.onResponse(call, res);
        } else {
            callback.onFailure(call, new IOException(String.valueOf(res.code())));
        }
    }

    /**
     * 获取okHttpClient
     *
     * @return okHttpClient
     */
    public OkHttpClient getHttpClient() {
        return getHttpClient(null);
    }

    /**
     * clone新的httpConfig
     *
     * @param onceHttpConfig
     * @return
     */
    private OkHttpClient getHttpClient(HttpConfig onceHttpConfig) {
        if (onceHttpConfig == null) {
            OkHttpClient.Builder tBuild = new OkHttpClient.Builder();
            this.initOkHttpClient(tBuild, this.mHttpConfig);
            return tBuild.build();
        }
        if (mClient == null) {
            mClient = mClientBuilder.build();
        }
        return mClient;
    }

    /**
     * 取消请求
     *
     * @param tag
     * @return
     */
    public boolean cancelRequest(Object tag) {
        if (mClient == null || tag == null) {
            return false;
        }
        synchronized (mClient.dispatcher().getClass()) {
            for (Call call : mClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }

            for (Call call : mClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }
        }
        return true;
    }
}
