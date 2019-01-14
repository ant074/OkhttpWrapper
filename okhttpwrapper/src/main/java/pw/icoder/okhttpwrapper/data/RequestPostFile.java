package pw.icoder.okhttpwrapper.data;

import java.io.File;

import okhttp3.RequestBody;


public class RequestPostFile extends RequestParams {

    private File mFile;

    public RequestPostFile(File file) {
        this.mFile = file;
    }

    @Override
    public RequestBody getRequestBody() {
        return RequestBody.create(MEDIA_TYPE_MARKDOWN, mFile == null ? new File("") : mFile);
    }

}
