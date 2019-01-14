package pw.icoder.samples;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;

import java.io.File;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import pw.icoder.okhttpwrapper.common.AdapterCallback;
import pw.icoder.okhttpwrapper.common.HttpConstant;
import pw.icoder.okhttpwrapper.common.IDataCallback;
import pw.icoder.okhttpwrapper.data.DataDecodeFile;
import pw.icoder.okhttpwrapper.data.DataDecodeJson;
import pw.icoder.okhttpwrapper.data.DataDecodeString;
import pw.icoder.okhttpwrapper.data.RequestGet;

public class MainActivity extends FragmentActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    Button button1;

    Button button2;

    Button button3;

    EditText editText;

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        text = (TextView) findViewById(R.id.text);
        editText = (EditText) findViewById(R.id.edit_text);
        buttonGetUrl();
        buttonGetJson();
        buttonDownloadFile();
    }

    /**
     * 下载文件
     */
    public void buttonDownloadFile() {
        button3.setOnClickListener(new OnClickListener() {

            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                String url = "http://h.hiphotos.baidu.com/image/pic/item/fc1f4134970a304e725fc8fad3c8a786c9175cb4.jpg";
                File file = new File(AppContext.HOME_ROOT + File.separator + "name4.jpg");
                if (!file.exists()) {
                    Log.e(TAG, file.getParent());
                    new File(file.getParent()).mkdirs();
                }
                DataDecodeFile dataDecodeFile = new DataDecodeFile(file);
                AdapterCallback adapterCallback = new AdapterCallback(MainActivity.this, dataDecodeFile);
                adapterCallback.setResponseCallback(new IDataCallback() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == HttpConstant.HTTP_CODE_WAIT) {
                            int rate = msg.arg1;
                            Log.e(TAG, "rate=" + String.valueOf(rate));
                            text.setText(String.valueOf(rate));
                        } else if (msg.what == HttpConstant.HTTP_CODE_SUCC) {
                            text.setText("下载完成");
                        }
                    }
                });
                AppContext.httpManager.download(url, new RequestGet(), adapterCallback);
            }
        });
    }

    /**
     * 获取json数据
     */
    public void buttonGetJson() { // 测试图灵机器人
        button2.setOnClickListener(new OnClickListener() {

            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if (content.length() == 0)
                    return;
                String url = "http://www" +
                             ".tuling123.com/openapi/api?key=94a6de604b0aa81f38f7bfe146971628&userid=001&info=" + content;
                DataDecodeJson<JsonTO> decodeJson = new DataDecodeJson(new TypeReference<JsonTO>() {});
                AdapterCallback adapterCallback = new AdapterCallback(MainActivity.this,
                        decodeJson);
                adapterCallback.setResponseCallback(new IDataCallback() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == HttpConstant.HTTP_CODE_SUCC) {
                            JsonTO to = (JsonTO) msg.obj;
                            if (to == null)
                                return;
                            text.setText(to.getText());
                        }
                    }
                });
                AppContext.httpManager.get(url, new RequestGet(), adapterCallback);
            }
        });
    }

    /**
     * 简单测试url
     */
    public void buttonGetUrl() {
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "http://m.baidu.com";
                AdapterCallback adapterCallback = new AdapterCallback(MainActivity.this, new DataDecodeString());
                adapterCallback.setResponseCallback(new IDataCallback() {
                    @Override
                    public void handleMessage(Message msg) {
                        text.setText(msg.obj.toString());
                    }
                });
                AppContext.httpManager.get(url, new RequestGet(), adapterCallback);
                List<String> cookies = getSyncCookies(url);
                for (String str : cookies) {
                    Log.e(TAG, "cookie=" + str);
                }

            }
        });
    }

    public static List<String> getSyncCookies(String uri) {
        HttpUrl url = HttpUrl.parse(uri);
        List<Cookie> cookieList = null;
        if (AppContext.httpManager.getHttpConfig().getCookieJar() != null) {
            cookieList = AppContext.httpManager.getHttpConfig().getCookieJar().loadForRequest(url);
        }
        List<String> cookies = new ArrayList<String>();
        for (int i = 0, n = cookieList.size(); i < n; i++) {
            StringBuilder sb = new StringBuilder();
            Cookie cookie = cookieList.get(i);
            sb.append(cookie.name()).append("=").append(cookie.value()).append(";domain=").append(cookie.domain())
              .append("; expires=").append(cookie.expiresAt()).append(";path=").append(cookie.path()).append(";");
            cookies.add(sb.toString());
        }
        return cookies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button1, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
