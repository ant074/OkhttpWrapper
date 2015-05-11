package pw.icoder.okhttpwrapper.data;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

public class RequestPostMap extends RequestParams {

    private String[][] mPostData;

    public RequestPostMap(String[][] postData) {
        this.mPostData=postData;
    }

    @Override
    public RequestBody getRequestBody() {
        if(mPostData == null)
            return null;
        FormEncodingBuilder builder=new FormEncodingBuilder();
        for(int i=0; i < mPostData.length; i++) {
            builder.add(mPostData[i][0], mPostData[i][1]);
        }
        return builder.build();
    }

}
