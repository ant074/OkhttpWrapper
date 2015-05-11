package pw.icoder.okhttpwrapper.data;

import java.io.IOException;

import okio.BufferedSink;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

public class RequestPostBufferedSink extends RequestParams {

    private BufferedSink sink;

    public RequestPostBufferedSink(BufferedSink sink) {
        this.sink=sink;
    }

    @Override
    public RequestBody getRequestBody() {
        if(sink == null)
            return null;
        RequestBody requestBody=new RequestBody() {

            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
            }
        };
        return requestBody;
    }

}
