package pw.icoder.okhttpwrapper.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.Okio;
import pw.icoder.okhttpwrapper.common.HttpConstant;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class DataDecodeFile implements IDataDecode {

    public static final String TAG=DataDecodeFile.class.getSimpleName();

    private File file;

    public DataDecodeFile(File file) {
        this.file=file;
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
        try {
            Log.e(TAG, "load image succ=" + res.body().contentLength());
            long count=res.body().contentLength();
            long current=0;
            BufferedSink sink=Okio.buffer(Okio.sink(file));
            InputStream input=res.body().byteStream();
            int readLen=0;
            byte[] buffer=new byte[5 * 1024];
            while(!(current >= count) && ((readLen=input.read(buffer, 0, 5 * 1024)) > 0)) {// 未全部读取
                sink.write(buffer, 0, readLen);
                current+=readLen;
                int rate=(int)(((float)current / count) * 100);
                sendMessage(handler, HttpConstant.HTTP_CODE_WAIT, rate, 0, null);
            }
            sink.close();
            Log.e(TAG, "send image file");
            sendMessage(handler, HttpConstant.HTTP_CODE_SUCC, 0, 0, file);
        } catch(IOException e) {
            sendMessage(handler, HttpConstant.DATA_DECODE_ERR, 0, 0, HttpConstant.READ_DATA_IO_ERR_MSG);
            e.printStackTrace();
        }
    }

    private void sendMessage(Handler handler, int what, int arg1, int arg2, Object obj) {
        Message msg=handler.obtainMessage();
        msg.what=what;
        msg.arg1=arg1;
        msg.arg2=arg2;
        msg.obj=obj;
        handler.sendMessage(msg);
    }
}
