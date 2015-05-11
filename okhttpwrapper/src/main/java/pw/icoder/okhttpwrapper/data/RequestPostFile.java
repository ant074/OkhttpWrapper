package pw.icoder.okhttpwrapper.data;

import java.io.File;

import com.squareup.okhttp.RequestBody;

public class RequestPostFile extends RequestParams {

    private File mFile;

    public RequestPostFile(File file) {
        this.mFile=file;
    }

    @Override
    public RequestBody getRequestBody() {
        if(mFile == null)
            return null;
        return RequestBody.create(MEDIA_TYPE_MARKDOWN, mFile);
    }

}
