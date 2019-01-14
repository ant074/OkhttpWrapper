package pw.icoder.okhttpwrapper.data;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Response;
import pw.icoder.okhttpwrapper.common.HttpConstant;

public class DataDecodeString implements IDataDecode {

    public static final String TAG = DataDecodeString.class.getSimpleName();

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
            msg.obj = res.body().string();
        } catch (IOException e) {
            msg.what = HttpConstant.DATA_DECODE_ERR;
            msg.obj = HttpConstant.READ_DATA_DECODE_ERR_MSG;
            e.printStackTrace();
        }
        msg.sendToTarget();
    }

}
