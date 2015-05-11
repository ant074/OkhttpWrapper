package pw.icoder.okhttpwrapper.exception;

/**
 * 数据解析错误
 * @author wx@shuzijie.com
 */
public class DataDecodeException extends Exception {

    private static final long serialVersionUID=5723056104918190056L;

    public DataDecodeException() {
        super();
    }

    public DataDecodeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DataDecodeException(String detailMessage) {
        super(detailMessage);
    }

    public DataDecodeException(Throwable throwable) {
        super(throwable);
    }

}
