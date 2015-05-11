package pw.icoder.okhttpwrapper.data;

import java.io.IOException;

import android.os.Handler;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public interface IDataDecode {

    public void handleOnFailure(Handler handler, Request req, IOException e);

    public void handleOnSucc(Handler handler, Response res);
}
