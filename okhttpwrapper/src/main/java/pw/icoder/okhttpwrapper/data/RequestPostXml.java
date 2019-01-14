package pw.icoder.okhttpwrapper.data;


import okhttp3.RequestBody;

public class RequestPostXml extends RequestParams {

    private String mContent;

    public RequestPostXml(String xmlString) {
        this.mContent = xmlString;
    }

    @Override
    public RequestBody getRequestBody() {
        return RequestBody.create(MEDIA_TYPE_XML, mContent==null?"":mContent);
    }

}
