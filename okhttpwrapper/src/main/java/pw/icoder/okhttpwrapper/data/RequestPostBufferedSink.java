package pw.icoder.okhttpwrapper.data;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;


public class RequestPostBufferedSink extends RequestParams {

    private BufferedSink sink;

    public RequestPostBufferedSink(BufferedSink sink) {
        this.sink = sink;
    }

    @Override
    public RequestBody getRequestBody() {
        RequestBody requestBody = new RequestBody() {

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
