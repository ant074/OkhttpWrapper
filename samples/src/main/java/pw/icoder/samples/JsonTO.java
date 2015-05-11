package pw.icoder.samples;

import com.alibaba.fastjson.JSONObject;

import pw.icoder.okhttpwrapper.data.TOFromJsonInterface;
import pw.icoder.okhttpwrapper.exception.DataDecodeException;


public class JsonTO implements TOFromJsonInterface<JsonTO> {

    private Integer code;

    private String text;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code=code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text=text;
    }

    @Override
    public JsonTO getTOFromJson(JSONObject obj) throws DataDecodeException {
        if(obj == null)
            return null;
        this.code=obj.containsKey("code") ? obj.getInteger("code") : null;
        this.text=obj.containsKey("text") ? obj.getString("text") : null;
        return this;
    }

}
