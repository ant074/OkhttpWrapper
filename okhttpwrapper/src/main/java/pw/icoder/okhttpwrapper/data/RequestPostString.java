package pw.icoder.okhttpwrapper.data;

import com.squareup.okhttp.RequestBody;

public class RequestPostString extends RequestParams {

    private String mContent;

    public RequestPostString(String content) {
        this.mContent=content;
    }

    @Override
    public RequestBody getRequestBody() {
        if(mContent == null)
            return null;
        return RequestBody.create(MEDIA_TYPE_MARKDOWN, mContent);
    }

}
