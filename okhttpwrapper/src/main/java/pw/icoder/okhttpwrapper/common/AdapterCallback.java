package pw.icoder.okhttpwrapper.common;

import java.io.IOException;

import pw.icoder.okhttpwrapper.data.IDataDecode;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class AdapterCallback extends Handler implements Callback {

    Context mContext;

    IDataDecode mDataDecode;

    public AdapterCallback(Context context, IDataDecode decode) {
        this.mContext=context;
        this.mDataDecode=decode;
    }

    @Override
    public void onFailure(Request req, IOException e) {
        mDataDecode.handleOnFailure(this, req, e);
    };

    @Override
    public void onResponse(Response res) throws IOException {
        mDataDecode.handleOnSucc(this, res);
    }
}
