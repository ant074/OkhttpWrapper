package pw.icoder.okhttpwrapper.cookie;

import okhttp3.CookieJar;

/**
 * Created by wevan on 16/7/19.
 */
public interface ClearableCookieJar extends CookieJar {

    void clear();
}
