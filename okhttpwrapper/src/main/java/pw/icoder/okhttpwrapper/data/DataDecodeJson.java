package pw.icoder.okhttpwrapper.data;

import java.io.IOException;

import pw.icoder.okhttpwrapper.common.HttpConstant;
import pw.icoder.okhttpwrapper.exception.DataDecodeException;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public abstract class DataDecodeJson<T> implements IDataDecode {

    public static final String TAG=DataDecodeJson.class.getSimpleName();

    private TOFromJsonInterface<T> to;

    public DataDecodeJson(TOFromJsonInterface<T> to) {
        this.to=to;
    }

    @Override
    public void handleOnFailure(Handler handler, Request req, IOException e) {
        Message msg=handler.obtainMessage();
        msg.what=HttpConstant.HTTP_CODE_FAIL;
        msg.obj=e.getMessage();
        msg.sendToTarget();
    }

    @Override
    public void handleOnSucc(Handler handler, Response res) {
        Message msg=handler.obtainMessage();
        msg.what=HttpConstant.HTTP_CODE_SUCC;
        try {
            String jsonString=res.body().string();
            if(jsonString == null)
                msg.obj=null;
            msg.obj=to.getTOFromJson(getJSONObject(jsonString));
        } catch(IOException e) {
            msg.what=HttpConstant.DATA_DECODE_ERR;
            msg.obj=HttpConstant.READ_DATA_DECODE_ERR_MSG;
            e.printStackTrace();
        } catch(DataDecodeException e) {
            msg.what=HttpConstant.DATA_DECODE_ERR;
            msg.obj=HttpConstant.READ_DATA_DECODE_ERR_MSG;
            e.printStackTrace();
        }
        msg.sendToTarget();
    }

    public abstract JSONObject getJSONObject(String jsonString);
}
