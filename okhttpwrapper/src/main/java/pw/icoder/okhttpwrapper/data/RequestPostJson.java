package pw.icoder.okhttpwrapper.data;

import org.json.JSONObject;

import okhttp3.RequestBody;


public class RequestPostJson extends RequestParams {

    private String mJson;

    private JSONObject mJsonObj;

    public RequestPostJson(JSONObject jsonObj) {
        this.mJsonObj = jsonObj;
    }

    public RequestPostJson(String json) {
        this.mJson = json;
    }

    @Override
    public RequestBody getRequestBody() {
        if (mJson != null) {
            return RequestBody.create(MEDIA_TYPE_JSON, mJson);
        } else if (mJsonObj != null) {
            RequestBody.create(MEDIA_TYPE_JSON, mJsonObj.toString());
        }
        return null;
    }

}
