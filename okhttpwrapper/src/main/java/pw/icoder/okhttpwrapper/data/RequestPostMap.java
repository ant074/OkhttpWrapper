package pw.icoder.okhttpwrapper.data;


import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RequestPostMap extends RequestParams {

    private String[][] mPostData;

    public RequestPostMap(String[][] postData) {
        this.mPostData = postData;
    }

    @Override
    public RequestBody getRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        for (int i = 0; i < (mPostData==null?0:mPostData.length); i++) {
            builder.add(mPostData[i][0], mPostData[i][1]);
        }
        return builder.build();
    }

}
