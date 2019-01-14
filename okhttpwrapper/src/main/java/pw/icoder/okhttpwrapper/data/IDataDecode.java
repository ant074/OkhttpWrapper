package pw.icoder.okhttpwrapper.data;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Response;


public interface IDataDecode {

    void handleOnFailure(Handler handler, IOException e);

    void handleOnSucc(Handler handler, Response res);
}
