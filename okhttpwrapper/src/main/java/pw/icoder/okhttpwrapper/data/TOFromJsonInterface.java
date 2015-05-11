package pw.icoder.okhttpwrapper.data;

import pw.icoder.okhttpwrapper.exception.DataDecodeException;

import com.alibaba.fastjson.JSONObject;

public interface TOFromJsonInterface<T> {

    public T getTOFromJson(JSONObject obj) throws DataDecodeException;

}
