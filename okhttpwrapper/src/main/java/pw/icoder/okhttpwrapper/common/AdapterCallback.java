package pw.icoder.okhttpwrapper.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pw.icoder.okhttpwrapper.data.IDataDecode;

public class AdapterCallback extends Handler implements Callback {

    Context mContext;
    IDataDecode mDataDecode;
    IDataCallback responseCallback;

    public AdapterCallback(Context context, IDataDecode decode) {
        this.mContext = context;
        this.mDataDecode = decode;
    }

    public void setResponseCallback(IDataCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        mDataDecode.handleOnFailure(this, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        mDataDecode.handleOnSucc(this, response);
    }

    @Override
    public void dispatchMessage(Message msg) {
        if (responseCallback != null) {
            responseCallback.handleMessage(msg);
        }
    }

}