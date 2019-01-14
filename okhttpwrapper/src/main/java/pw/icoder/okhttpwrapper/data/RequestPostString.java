package pw.icoder.okhttpwrapper.data;


import okhttp3.RequestBody;

public class RequestPostString extends RequestParams {

    private String mContent;

    public RequestPostString(String content) {
        this.mContent = content;
    }

    @Override
    public RequestBody getRequestBody() {
        return RequestBody.create(MEDIA_TYPE_MARKDOWN, mContent==null?"":mContent);
    }

}
