package pw.icoder.samples;

import java.io.File;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import pw.icoder.okhttpwrapper.common.AdapterCallback;
import pw.icoder.okhttpwrapper.common.HttpConstant;
import pw.icoder.okhttpwrapper.data.DataDecodeFile;
import pw.icoder.okhttpwrapper.data.DataDecodeJson;
import pw.icoder.okhttpwrapper.data.DataDecodeString;
import pw.icoder.okhttpwrapper.data.RequestGet;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

public class MainActivity extends ActionBarActivity {

    public static final String TAG=MainActivity.class.getSimpleName();

    Button button1;

    Button button2;

    Button button3;

    EditText editText;

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        text=(TextView)findViewById(R.id.text);
        editText=(EditText)findViewById(R.id.edit_text);
        buttonGetUrl();
        buttonGetJson();
        buttonDownloadFile();
    }

    /**
     * 下载文件
     */
    public void buttonDownloadFile() {
        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String url="http://h.hiphotos.baidu.com/image/pic/item/fc1f4134970a304e725fc8fad3c8a786c9175cb4.jpg";
                File file=new File(AppContext.HOME_ROOT + File.separator + "name4.jpg");
                if(!file.exists()) {
                    Log.e(TAG, file.getParent());
                    new File(file.getParent()).mkdirs();
                }
                AppContext.httpManager.download(url, new RequestGet(), new AdapterCallback(MainActivity.this, new DataDecodeFile(
                    file)) {

                    @Override
                    public void dispatchMessage(Message msg) {
                        if(msg.what == HttpConstant.HTTP_CODE_WAIT) {
                            int rate=msg.arg1;
                            Log.e(TAG, "rate=" + String.valueOf(rate));
                            text.setText(String.valueOf(rate));
                        } else if(msg.what == HttpConstant.HTTP_CODE_SUCC) {
                            text.setText("下载完成");
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取json数据
     */
    public void buttonGetJson() { // 测试图灵机器人
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content=editText.getText().toString();
                if(content.length() == 0)
                    return;
                String url="http://www.tuling123.com/openapi/api?key=94a6de604b0aa81f38f7bfe146971628&userid=001&info=" + content;
                AppContext.httpManager.get(url, new RequestGet(), new AdapterCallback(MainActivity.this,
                    new DataDecodeJson<JsonTO>(new JsonTO()) {

                        @Override
                        public JSONObject getJSONObject(String jsonString) {
                            return JSONObject.parseObject(jsonString);
                        }
                    }) {

                    @Override
                    public void dispatchMessage(Message msg) {
                        if(msg.what == HttpConstant.HTTP_CODE_SUCC) {
                            JsonTO to=(JsonTO)msg.obj;
                            if(to == null)
                                return;
                            text.setText(to.getText());
                        }
                    }
                });
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
                String url="http://m.baidu.com";
                AppContext.httpManager.get(url, new RequestGet(), new AdapterCallback(MainActivity.this, new DataDecodeString()) {

                    @Override
                    public void dispatchMessage(Message msg) {
                        text.setText(msg.obj.toString());
                    }
                });
                List<String> cookies=getSyncCookies();
                for(String str: cookies) {
                    Log.e(TAG, "cookie=" + str);
                }

            }
        });
    }

    public static List<String> getSyncCookies() {
        List<HttpCookie> cookieList=AppContext.httpManager.getHttpConfig().getCookieStore().getCookies();
        List<String> cookies=new ArrayList<String>();
        for(int i=0, n=cookieList.size(); i < n; i++) {
            StringBuilder sb=new StringBuilder();
            HttpCookie cookie=cookieList.get(i);
            sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";domain=").append(cookie.getDomain())
                .append("; expires=").append(cookie.getMaxAge()).append(";path=").append(cookie.getPath()).append(";");
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
        int id=item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
