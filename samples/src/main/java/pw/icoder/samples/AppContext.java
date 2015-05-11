package pw.icoder.samples;

import java.io.File;

import android.app.Application;
import android.os.Environment;

import pw.icoder.okhttpwrapper.HttpManager;

public class AppContext extends Application {

    public static HttpManager httpManager;

    public static final String APP_EXT_EV_SD_ROOT=Environment.getExternalStorageDirectory() + File.separator;

    public static final String HOME_ROOT=APP_EXT_EV_SD_ROOT + "axis_http_text";

    @Override
    public void onCreate() {
        super.onCreate();
        httpManager=new HttpManager(this);
        httpManager.setDefaultHttpConfig();
    }
}
