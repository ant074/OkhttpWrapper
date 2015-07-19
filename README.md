# OkhttpWrapper
okhttp的封装使用见samples项目

1，配置httpconfig
    连接池配置、超时配置、缓存配置、代理配置、http header配置、cookie配置

2，使用HttpManager
    同步get  getSync(String, RequestParams, AdapterCallback)
    异步get  get(String, RequestParams, AdapterCallback)
    同步post  postSync(String, RequestParams, AdapterCallback)
    异步post  post(String, RequestParams, AdapterCallback)
    下载  download(String, RequestParams, AdapterCallback)
    取消请求  cancelRequest(Object)

3，使用样例samples项目
    下载文件，并提示进度
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

    异步获取json数据 
    public void onClick(View v) { // 测试图灵机器人
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

    简单的url测试
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