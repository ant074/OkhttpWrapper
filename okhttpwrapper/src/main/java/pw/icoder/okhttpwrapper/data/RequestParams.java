package pw.icoder.okhttpwrapper.data;

import pw.icoder.okhttpwrapper.HttpConfig;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

public abstract class RequestParams {

    public static final MediaType MEDIA_TYPE_MARKDOWN=MediaType.parse("text/x-markdown; charset=utf-8");

    public static final MediaType MEDIA_TYPE_PNG=MediaType.parse("image/png");

    public static final MediaType MEDIA_TYPE_JSON=MediaType.parse("application/json; charset=utf-8");

    public static final MediaType MEDIA_TYPE_XML=MediaType.parse("application/xml; charset=utf-8");

    protected String url;

    protected Object tag;

    protected HttpConfig httpConfig;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag=tag;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig=httpConfig;
    }

    public abstract RequestBody getRequestBody();

}
