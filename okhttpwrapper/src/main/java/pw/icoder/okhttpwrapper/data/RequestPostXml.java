package pw.icoder.okhttpwrapper.data;

import com.squareup.okhttp.RequestBody;

public class RequestPostXml extends RequestParams {

    private String mContent;

    public RequestPostXml(String xmlString) {
        this.mContent=xmlString;
    }

    @Override
    public RequestBody getRequestBody() {
        if(mContent == null)
            return null;
        return RequestBody.create(MEDIA_TYPE_XML, mContent);
    }

}
