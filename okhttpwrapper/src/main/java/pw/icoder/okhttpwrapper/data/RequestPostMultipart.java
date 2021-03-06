package pw.icoder.okhttpwrapper.data;

import java.io.File;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestPostMultipart extends RequestParams {

    private String[][] postData;

    private File[] files;

    private Headers[] fileHeaders;

    public RequestPostMultipart(String[][] postData, File[] files, Headers[] fileHeaders) {
        this.postData = postData;
        this.files = files;
        this.fileHeaders = fileHeaders;
    }

    @Override
    public RequestBody getRequestBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (postData != null) {
            for (int i = 0; i < postData.length; i++) {
                builder.addFormDataPart(postData[i][0], postData[i][1]);
            }
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (fileHeaders != null && fileHeaders.length > i) {
                    builder.addPart(fileHeaders[i], RequestBody.create(MEDIA_TYPE_JPEG, files[i]));
                } else {
                    builder.addPart(RequestBody.create(MEDIA_TYPE_JPEG, files[i]));
                }
            }
        }
        return builder.build();
    }

}
