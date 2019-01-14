package pw.icoder.okhttpwrapper.data;

import java.io.File;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestPostFormMultipart extends RequestParams {

    private String[][] postData;
    private File[] files;

    private String[] fileNames;

    public RequestPostFormMultipart(String[][] postData, String[] fileNames, File[] files) {
        this.postData = postData;
        this.fileNames = fileNames;
        this.files = files;
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
                builder.addFormDataPart(fileNames[i], files[i].getName(), RequestBody.create(MEDIA_TYPE_JPEG, files[i]));
            }
        }
        return builder.build();
    }

}
