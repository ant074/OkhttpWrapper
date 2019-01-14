package pw.icoder.okhttpwrapper.data;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;

import okhttp3.Response;
import pw.icoder.okhttpwrapper.common.HttpConstant;

public  class DataDecodeJson<T> implements IDataDecode {

    public static final String TAG = DataDecodeJson.class.getSimpleName();

    private TypeReference<T> typeReference;

    public DataDecodeJson(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public void handleOnFailure(Handler handler, IOException e) {
        Message msg = handler.obtainMessage();
        msg.what = HttpConstant.HTTP_CODE_FAIL;
        msg.obj = e.getMessage();
        msg.sendToTarget();
    }

    @Override
    public void handleOnSucc(Handler handler, Response res) {
        Message msg = handler.obtainMessage();
        msg.what = HttpConstant.HTTP_CODE_SUCC;
        try {
            String jsonString = res.body().string();
            if (jsonString == null) {
                msg.obj = null;
            }
            msg.obj = JSONObject.parseObject(jsonString, typeReference);
        } catch (Exception e) {
            msg.what = HttpConstant.DATA_DECODE_ERR;
            msg.obj = HttpConstant.READ_DATA_DECODE_ERR_MSG;
            e.printStackTrace();
        }
        msg.sendToTarget();
    }

}
