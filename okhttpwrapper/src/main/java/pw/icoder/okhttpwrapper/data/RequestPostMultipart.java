package pw.icoder.okhttpwrapper.data;

import java.io.File;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

public class RequestPostMultipart extends RequestParams {

    private String[][] postData;

    private File[] files;

    private Headers[] fileHeaders;

    public RequestPostMultipart(String[][] postData, File[] files, Headers[] fileHeaders) {
        this.postData=postData;
        this.files=files;
        this.fileHeaders=fileHeaders;
    }

    @Override
    public RequestBody getRequestBody() {
        MultipartBuilder builder=new MultipartBuilder().type(MultipartBuilder.FORM);
        if(postData != null) {
            for(int i=0; i < postData.length; i++) {
                builder.addFormDataPart(postData[i][0], postData[i][1]);
            }
        }
        if(files != null) {
            for(int i=0; i < files.length; i++) {
                if(fileHeaders != null && fileHeaders.length > i) {
                    builder.addPart(fileHeaders[i], RequestBody.create(MEDIA_TYPE_PNG, files[i]));
                } else {
                    builder.addPart(RequestBody.create(MEDIA_TYPE_PNG, files[i]));
                }
            }
        }
        return builder.build();
    }

}
